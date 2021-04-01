package IMERISoin.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import IMERISoin.Model.Drug;
import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import sun.net.www.protocol.http.HttpURLConnection;

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

    private static final String ROOT_URL = "http://10.3.6.197:8000/";



    /**
     * Ask to webservice the robot location/intersect
     * and return Robot object with location/intersect
     * @param bot
     */
//    public static void getLocation(Robot bot) {

//        try {
//            Robot result = queryLocation(bot);
//            System.out.println(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }

    public static void getDrugList(ArrayList<Drug> drugs) {

    }

    /**
     * Query location/intersect of the bot in Json format
     *
     * GSON translate Java POJO to Json string.
     * @param bot
     * @return Robot with location updated
     * @throws IOException
     */
//    private static Robot queryLocation(Robot bot) throws IOException {
//
//        Robot result = null;
//        HttpPost post = new HttpPost(ROOT_URL + "post");
//
//        Gson gson = new Gson();
//
//        // transform java Robot object to JSON
//        post.setEntity(new StringEntity(gson.toJson(bot)));
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault();
//             CloseableHttpResponse response = httpClient.execute(post)) {
//
//            HttpEntity entity = response.getEntity();
//            if(entity != null) {
//
//                String json = EntityUtils.toString(entity);
//                GsonBuilder gsonBldr = new GsonBuilder();
//                gsonBldr.registerTypeAdapter(Robot.class, new RobotDeserializer());
//
//                result = gsonBldr.create().fromJson(json, Robot.class);
//
//            }
//
//        }
//
//        return result;
//    }

    /**
     * Example 1 : Send an Http POST in Json format
     *
     * @param bot
     */
//    public static void example1(Robot bot) {
//
//        // cette forme de try ferme les ressources ouvertes en fin de block
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
//
//            String json = "{'id':1,'name':'John'}";
//            HttpPost request = new HttpPost(ROOT_URL + "post");
//            StringEntity params = new StringEntity(json);
//            request.addHeader("content-type", "application/json");
//            request.setEntity(params);
//
//            HttpResponse response = httpClient.execute(request);
//            // handle response here...
//
//        } catch (Exception ex) {
//            // handle exception here
//        }
//
//    }

    /**
     * Example 2 : Simple Http POST with parameters
     *
     * @throws ClientProtocolException
     * @throws IOException
     */
//    public static void example2() throws ClientProtocolException, IOException {
//
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(ROOT_URL + "post");
//
//        String json = "{'id':1,'name':'John'}";
//        StringEntity entity = new StringEntity(json);
//        httpPost.setEntity(entity);
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-type", "application/json");
//
//        CloseableHttpResponse response = client.execute(httpPost);
//        System.out.println(response.getStatusLine().getStatusCode() == 200);
//        client.close();
//    }

    public static void addOrder(int room_id, int drug_id) {
        try {
            URL url = new URL(ROOT_URL + "Robots/addOrder/" + room_id + "/" + drug_id);
//            URLConnection con = aurl.openConnection();
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode());
            if (request.getResponseCode() == 200) {
//                JsonParser jp = new JsonParser(); //from gson
//                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
//                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
//                JsonArray medicines = rootObj.getAsJsonArray("list"); //just grab the zipcode
//
//                System.out.println(medicines.toString());
//                System.out.println(rootObj.toString());
//
//                for (JsonElement jsonElement : medicines) {
//                    drugs.add(new Gson().fromJson(jsonElement, Drug.class));
//                }
//
//                return drugs;

                System.out.println("Oreder added");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
