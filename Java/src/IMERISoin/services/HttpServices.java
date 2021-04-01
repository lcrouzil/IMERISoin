package IMERISoin.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
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

//    private static final String ROOT_URL = "http://10.3.6.197:8000/";
    private static final String ROOT_URL = "http://127.0.0.1:8000/";



    /**
     * Ask to webservice the robot location/intersect
     * and return Robot object with location/intersect
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

        try {
            URL url = new URL(ROOT_URL + "Patients/listMedicines/");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

//            System.out.println(request.getResponseCode());
            if (request.getResponseCode() == 200) {

                System.out.println("Drug get !");

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonArray medicines = rootObj.getAsJsonArray("list"); //just grab the zipcode

//                System.out.println(medicines.toString());
//                System.out.println(rootObj.toString());

                for (JsonElement jsonElement : medicines) {
                    drugs.add(new Gson().fromJson(jsonElement, Drug.class));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRoomList(ArrayList<Room> rooms) {

        try {
            URL url = new URL(ROOT_URL + "Patients/listRooms/");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            if (request.getResponseCode() == 200) {

                System.out.println("Room get !");

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonArray list = rootObj.getAsJsonArray("list"); //just grab the zipcode

//                System.out.println(rootObj.toString());
//                System.out.println(list.toString());
                for (JsonElement jsonElement : list) {
//                    System.out.println(jsonElement.toString());
                    rooms.add(new Gson().fromJson(jsonElement, Room.class));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPatientList(ArrayList<Patient> patients) {

        try {
            URL url = new URL(ROOT_URL + "Patients/listPatients/");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

//            System.out.println(request.getResponseCode());
            if (request.getResponseCode() == 200) {
                System.out.println("Patient get !");

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonArray list = rootObj.getAsJsonArray("list"); //just grab the zipcode

//                System.out.println(rootObj.toString());
//                System.out.println(list.toString());
                for (JsonElement jsonElement : list) {
                    patients.add(new Gson().fromJson(jsonElement, Patient.class));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
