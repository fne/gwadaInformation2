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

import android.util.Xml;
import android.view.InflateException;

public class HttpClientAntilles {
	private boolean isProxy;
	private String proxyUrl = "";
	private int proxyPort = 8080;
	private String proxyUser = "";
	private String proxyPass = "";	

	public HttpClientAntilles() {	 
				
	}

	public HttpResponse get(String urlRequest) {
		HttpGet request = new HttpGet(urlRequest);
		try {
			//utilisation du proxy
			DefaultHttpClient httpclient = new DefaultHttpClient();			
			//définition du login/mdp du proxy
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
	        credsProvider.setCredentials(
	                new AuthScope(proxyUrl, proxyPort),
	                new UsernamePasswordCredentials(proxyUser, proxyPass));	        
	        httpclient.setCredentialsProvider(credsProvider);	        
	        //exécution de la requête
			HttpResponse httpResponse = httpclient.execute(request);
			return httpResponse;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public HttpResponse postRequest() {
		return null;
	}
	
	/**
	 * Parsing de la réponse xml
	 * @param in
	 * @param responseParser
	 * @throws IOException
	 */
	private void parseResponse(InputStream in, ResponseHandler responseParser) throws IOException {
	    final XmlPullParser parser = Xml.newPullParser();
	    try {
	        parser.setInput(new InputStreamReader(in));

	        int type;
	        while ((type = parser.next()) != XmlPullParser.START_TAG &&
	                type != XmlPullParser.END_DOCUMENT) {
	            // Empty
	        }

	        if (type != XmlPullParser.START_TAG) {
	            throw new InflateException(parser.getPositionDescription()
	                    + ": No start tag found!");
	        }

	        String name = parser.getName();
	        /*if (RESPONSE_TAG_RSP.equals(name)) {
	            final String value = parser.getAttributeValue(null, RESPONSE_ATTR_STAT);
	            if (!RESPONSE_STATUS_OK.equals(value)) {
	                throw new IOException("Wrong status: " + value);
	            }
	        }*/

	        //responseParser.parseResponse(parser);

	    } catch (XmlPullParserException e) {
	        final IOException ioe = new IOException("Could not parse the response");
	        ioe.initCause(e);
	        throw ioe;
	    }
	}
	
	/**
	 * renvoie une url connection
	 * 
	 * @return
	 * @throws IOException
	 */
	public HttpURLConnection getUrlConnection(String url) throws IOException {
		URL urlConnection;		
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"web.pandore.log.intra.laposte.fr", 8080));
		Authenticator authenticator = new Authenticator() {

			public PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication("pfwn061",
						"fwn061".toCharArray()));
			}
		};
		Authenticator.setDefault(authenticator);
		urlConnection = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urlConnection
				.openConnection(proxy);
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setRequestProperty("Proxy-Connection", "Keep-Alive");		 
		return conn;
	}
}
