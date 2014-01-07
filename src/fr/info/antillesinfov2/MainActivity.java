package fr.info.antillesinfov2;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import fr.info.antillesinfov2.business.model.News;
import fr.info.antillesinfov2.business.service.NewsManager;
import fr.info.antillesinfov2.business.service.NewsManagerImpl;
import fr.info.antillesinfov2.business.service.android.NewsAdapter;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "news";
	public static final String DEFAULT_RSS = "http://www.domactu.com/rss/actu/";
	public static final String GP_RSS = "http://www.domactu.com/rss/actu/guadeloupe/";
	public static final String MQ_RSS = "http://www.domactu.com/rss/actu/martinique/";
	public static String RSS_TO_OPEN;
	private ListView vue;
	private List<News> listNews;
	// declare the dialog as a member field of your activity
	private ProgressDialog mProgressDialog;
	private HttpRequestTask httpRequestTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(MainActivity.this);
		mProgressDialog.setMessage("Loading news !");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);

		// Show the Up button in the action bar.
		// Get the message from the intent
		Intent intent = getIntent();
		// TODO : A mettre dans les paramètres de l'appli
		String myChoosenRss = (String) intent
				.getSerializableExtra(MainActivity.RSS_TO_OPEN);

		httpRequestTask = new HttpRequestTask();
		if (myChoosenRss == null) {
			httpRequestTask.execute(DEFAULT_RSS);
		} else {
			httpRequestTask.execute(myChoosenRss);
		}
		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						httpRequestTask.cancel(true);
					}
				});
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

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		protected void onPostExecute(String rssSource) {
			String result = null;
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
			} catch (IOException e) {
				Log.i("mainActivity", "network exception");
				result = e.getMessage();
				e.printStackTrace();
			} catch (Exception e) {
				Log.i("mainActivity", "no news");
				result = e.getMessage();
				e.printStackTrace();
			} finally {
				mProgressDialog.dismiss();
				if (result != null)
					Toast.makeText(getApplicationContext(),
							"Download error: " + result, Toast.LENGTH_LONG)
							.show();
				else
					Toast.makeText(getApplicationContext(),
							"News downloaded !", Toast.LENGTH_SHORT).show();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_rss_gp:
			intent.putExtra(MainActivity.RSS_TO_OPEN, MainActivity.GP_RSS);
			startActivity(intent);
			return true;
		case R.id.action_rss_mq:
			intent.putExtra(MainActivity.RSS_TO_OPEN, MainActivity.MQ_RSS);
			startActivity(intent);
			return true;
		case R.id.action_rss_all:
			intent.putExtra(MainActivity.RSS_TO_OPEN, MainActivity.DEFAULT_RSS);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public List<News> getListNews() {
		return listNews;
	}

	public void setListNews(List<News> listNews) {
		this.listNews = listNews;
	}

}
