package fr.info.antillesinfov2.library;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpClientAntilles {
	//private boolean isProxy;
	private String proxyUrl = "web.pandore.log.intra.laposte.fr";
	private int proxyPort = 8080;
	private String proxyUser = "pfwn061";
	private String proxyPass = "fwn061";
	//private static final String logger = "httpclientantilles";

	public HttpClientAntilles() {

	}

	public HttpResponse get(String urlRequest) {
		HttpGet request = new HttpGet(urlRequest);
		try {
			// utilisation du proxy
			DefaultHttpClient httpclient = new DefaultHttpClient();
			// définition du login/mdp du proxy
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(proxyUrl, proxyPort),
					new UsernamePasswordCredentials(proxyUser, proxyPass));
			httpclient.setCredentialsProvider(credsProvider);
			// execution de la requete
			HttpResponse httpResponse = httpclient.execute(request);
			return httpResponse;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("info", "Pas de connexions internet");
		}
		return null;
	}

	public HttpResponse postRequest() {
		return null;
	}

}
