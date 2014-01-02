package fr.info.antillesinfov2.library;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;
import android.view.InflateException;

public class HttpClientAntilles {
	private boolean isProxy;
	private String proxyUrl = "web.pandore.log.intra.laposte.fr";
	private int proxyPort = 8080;
	private String proxyUser = "pfwn061";
	private String proxyPass = "fwn061";
	private static final String logger = "httpclientantilles";
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

	/**
	 * Parsing de la r�ponse xml
	 * 
	 * @param in
	 * @param responseParser
	 * @throws IOException
	 */
	private void parseResponse(InputStream in, ResponseHandler responseParser)
			throws IOException {
		final XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(new InputStreamReader(in));

			int type;
			while ((type = parser.next()) != XmlPullParser.START_TAG
					&& type != XmlPullParser.END_DOCUMENT) {
				// Empty
			}

			if (type != XmlPullParser.START_TAG) {
				throw new InflateException(parser.getPositionDescription()
						+ ": No start tag found!");
			}
			String name = parser.getName();
		} catch (XmlPullParserException e) {
			final IOException ioe = new IOException(
					"Could not parse the response");
			ioe.initCause(e);
			throw ioe;
		}
	}
}
