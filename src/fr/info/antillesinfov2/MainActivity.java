package fr.info.antillesinfov2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import fr.info.antillesinfov2.dao.DaoNews;
import fr.info.antillesinfov2.model.Item;
import fr.info.antillesinfov2.model.RSS;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = null;
	private ListView vue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		HttpRequestTask httpRequestTask = new HttpRequestTask();
		httpRequestTask.execute("http://www.domactu.com/rss/actu/");
	}

	/**
	 * Tache permettant de r�cup�rer les infos via un flux et de compl�ter le
	 * layout
	 * 
	 * @author NEBLAI
	 * 
	 */
	private class HttpRequestTask extends AsyncTask<String, Integer, String> {
		private DaoNews daoNews;

		@Override
		protected String doInBackground(String... params) {
			daoNews = new DaoNews();
			String rssSource = daoNews.readContentsOfUrl(params[0]);
			return rssSource;
		}

		protected void onPostExecute(String rssSource) {
			RSS rssObject = null;
			try {
				Serializer serializer = new Persister();
				rssObject = serializer.read(RSS.class, rssSource);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// return example.getChannel().getItem();*/
			List<Item> arrayListItem = rssObject.getChannel().getItem();

			// showDialog("Downloaded " + result + " bytes");
			// On r�cup�re une ListView de notre layout en XML, c'est la vue qui
			// repr�sente la liste
			vue = (ListView) findViewById(R.id.listView1);

			/*
			 * On entrepose nos donn�es dans un tableau qui contient deux
			 * colonnes : - la premi�re contiendra le nom de l'utilisateur - la
			 * seconde contiendra le num�ro de t�l�phone de l'utilisateur
			 */
			String[][] repertoire = new String[][] {
					{ "Bill Gates", "06 06 06 06 06" },
					{ "Niels Bohr", "05 05 05 05 05" },
					{ "Alexandre III de Mac�doine", "04 04 04 04 04" } };

			/*
			 * On doit donner � notre adaptateur une liste du type �
			 * List<Map<String, ?> � : - la cl� doit forc�ment �tre une cha�ne
			 * de caract�res - en revanche, la valeur peut �tre n'importe quoi,
			 * un objet ou un entier par exemple, si c'est un objet, on
			 * affichera son contenu avec la m�thode � toString() �
			 * 
			 * Dans notre cas, la valeur sera une cha�ne de caract�res, puisque
			 * le nom et le num�ro de t�l�phone sont entrepos�s dans des cha�nes
			 * de caract�res
			 */
			List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();

			HashMap<String, String> element;
			for (Iterator<Item> iterator = arrayListItem.iterator(); iterator
					.hasNext();) {
				Item item = (Item) iterator.next();
				System.out.println(item.getDescription());
				// � on cr�e un �l�ment pour la liste�
				element = new HashMap<String, String>();
				/*
				 * � on d�clare que la cl� est � text1 � (j'ai choisi ce mot au
				 * hasard, sans sens technique particulier) pour le nom de la
				 * personne (premi�re dimension du tableau de valeurs)�
				 */
				element.put("text1", item.getTitle());
				/*
				 * � on d�clare que la cl� est � text2 � pour le num�ro de cette
				 * personne (seconde dimension du tableau de valeurs)
				 */
				element.put("text2", item.getCategory());
				liste.add(element);
			}
			// // Pour chaque personne dans notre r�pertoire�
			// for (int i = 0; i < repertoire.length; i++) {
			// // � on cr�e un �l�ment pour la liste�
			// element = new HashMap<String, String>();
			// /*
			// * � on d�clare que la cl� est � text1 � (j'ai choisi ce mot au
			// * hasard, sans sens technique particulier) pour le nom de la
			// * personne (premi�re dimension du tableau de valeurs)�
			// */
			// element.put("text1", repertoire[i][0]);
			// /*
			// * � on d�clare que la cl� est � text2 � pour le num�ro de cette
			// * personne (seconde dimension du tableau de valeurs)
			// */
			// element.put("text2", repertoire[i][1]);
			// liste.add(element);
			// }

			ListAdapter adapter = new SimpleAdapter(getApplicationContext(),
			// Valeurs � ins�rer
					liste,
					/*
					 * Layout de chaque �l�ment (l�, il s'agit d'un layout par
					 * d�faut pour avoir deux textes l'un au-dessus de l'autre,
					 * c'est pourquoi on n'affiche que le nom et le num�ro d'une
					 * personne)
					 */
					android.R.layout.simple_expandable_list_item_1,
					/*
					 * Les cl�s des informations � afficher pour chaque �l�ment
					 * : - la valeur associ�e � la cl� � text1 � sera la
					 * premi�re information - la valeur associ�e � la cl� �
					 * text2 � sera la seconde information
					 */
					new String[] { "text1", "text2" },
					/*
					 * Enfin, les layouts � appliquer � chaque widget de notre
					 * �l�ment (ce sont des layouts fournis par d�faut) : - la
					 * premi�re information appliquera le layout �
					 * android.R.id.text1 � - la seconde information appliquera
					 * le layout � android.R.id.text2 �
					 */
					new int[] { android.R.id.text1, android.R.id.text2 });
			// Pour finir, on donne � la ListView le SimpleAdapter
			vue.setAdapter(adapter);
			vue.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(),
							DetailInfoActivity.class);

					TextView textView = (TextView) arg1
							.findViewById(android.R.id.text1);
					String message = textView.getText().toString();
					intent.putExtra(EXTRA_MESSAGE, message);
					startActivity(intent);
				}
			});
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
