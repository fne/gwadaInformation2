package fr.info.antillesinfov2;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.info.antillesinfov2.business.model.News;
import fr.info.antillesinfov2.business.service.NewsManager;
import fr.info.antillesinfov2.business.service.NewsManagerImpl;
import fr.info.antillesinfov2.business.service.android.NewsAdapter;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "news";
	private ListView vue;
	private List<News> listNews;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressDialog = ProgressDialog.show(this, "", "Chargement", true);
		HttpRequestTask httpRequestTask = new HttpRequestTask();
		httpRequestTask.execute("http://www.domactu.com/rss/actu/");
	}

	/**
	 * Tache permettant de recuperer les infos via un flux et de completer le
	 * layout
	 * 
	 * @author NEBLAI
	 * 
	 */
	private class HttpRequestTask extends AsyncTask<String, Integer, String> {
		private NewsManager newsManager = new NewsManagerImpl();;

		@Override
		protected String doInBackground(String... params) {
			String rssSource = newsManager.getNewsChannel(params[0]);
			return rssSource;
		}

		protected void onPostExecute(String rssSource) {
			try {
				newsManager = new NewsManagerImpl();
				List<News> arrayListNews = newsManager.getListNews(rssSource);
				setListNews(arrayListNews);
				// represente la liste
				vue = (ListView) findViewById(R.id.listView);

				NewsAdapter adapter = new NewsAdapter(getApplicationContext(),
						arrayListNews);
				// Pour finir association du simpleadapter a la vue
				vue.setAdapter(adapter);
				vue.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(getApplicationContext(),
								DetailInfoActivity.class);
						intent.putExtra(MainActivity.EXTRA_MESSAGE,
								getListNews().get(arg2));
						startActivity(intent);
					}
				});
				progressDialog.dismiss();
			} catch (IOException e) {
				Log.i("mainActivity", "network exception");
				e.printStackTrace();
			} catch (Exception e) {
				Log.i("mainActivity", "no news");
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public List<News> getListNews() {
		return listNews;
	}

	public void setListNews(List<News> listNews) {
		this.listNews = listNews;
	}

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

}
