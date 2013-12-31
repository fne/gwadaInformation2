package fr.info.antillesinfov2.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;
import fr.info.antillesinfov2.library.HttpClientAntilles;
import fr.info.antillesinfov2.model.RSS;

public class DaoNews {
	private static final String DEBUG_TAG = "DaoNews";
	private HttpClientAntilles httpClient;
	/**
	 * Lecture des donn�es sur un serveur distant
	 * 
	 * @param url
	 * @return
	 */
	public String readContentsOfUrl(String url) {
		String result = null;
		try {
			//requete http avec set du proxy
			httpClient = new HttpClientAntilles();
			HttpResponse httpResponse = httpClient.get(url);
			
			int response = httpResponse.getStatusLine().getStatusCode();
			Log.d(DEBUG_TAG, "The response is: " + response);
			//lecture de la requete http
			InputStream is = httpResponse.getEntity().getContent();
			/*BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = reader.readLine();
			result = line;
			while ((line = reader.readLine()) != null) {
				result += line;
			}*/
			Serializer serializer = new Persister();			
			RSS example = serializer.read(RSS.class, is);
			example.getChannel();
			
		} catch (MalformedURLException e) {			 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//public List getNewsList(){
		
	//}
}
