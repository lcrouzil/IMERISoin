package IMERISoin;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;


/**
 *
 * Classe de gestion des requetes http / json
 *
 * Librairies à ajouter à votre projet :
 * Apache HttpClient 4.5 et HttpCore Apache Gson 2.8.5
 * Apache Gson 2.8.5
 *
 * Telechargements depuis MavenRepositories 
 * ex : https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5 
 *
 * Tutoriels
 * HttpClient Baeldung : https://www.baeldung.com/httpclient-post-http-request
 * MyKong : https://mkyong.com/java/apache-httpclient-examples/
 *
 * @author emarchand
 *
 */
public class HttpServices {

	private static final String ROOT_URL = "http://10.3.6.197:8000";

	public static void example1() {

		// cette forme de try ferme les ressources ouvertes en fin de block
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

//			String json = "{'id':1,'name':'John'}";
			HttpPost request = new HttpPost(ROOT_URL + "/");
//			StringEntity params = new StringEntity(json);
			request.addHeader("content-type", "application/json");
//			request.setEntity(params);

			HttpResponse response = httpClient.execute(request);
			// handle response here...

		} catch (Exception ex) {
			// handle exception here
		}

	}

	/**
	 * Example 2 : Simple Http POST with parameters
	 *
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void example2() throws ClientProtocolException, IOException {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(ROOT_URL + "post");

//		String json = "{'id':1,'name':'John'}";
//		StringEntity entity = new StringEntity(json);
//		httpPost.setEntity(entity);
//		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		CloseableHttpResponse response = client.execute(httpPost);
		System.out.println(response.getStatusLine().getStatusCode() == 200);
		client.close();
	}

}
