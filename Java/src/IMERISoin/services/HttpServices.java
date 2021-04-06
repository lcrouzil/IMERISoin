package IMERISoin.services;

import IMERISoin.Model.Drug;
import IMERISoin.Model.Order;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import com.google.gson.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe de gestion des requetes http / json
 * <p>
 * Librairies à ajouter à votre projet :
 * Apache HttpClient 4.5 et HttpCore Apache Gson 2.8.5
 * Apache Gson 2.8.5
 * <p>
 * Telechargements depuis MavenRepositories
 * ex : https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5
 * <p>
 * Tutoriels
 * HttpClient Baeldung : https://www.baeldung.com/httpclient-post-http-request
 * MyKong : https://mkyong.com/java/apache-httpclient-examples/
 *
 * @author emarchand
 */
public class HttpServices {

    //    private static final String ROOT_URL = "http://10.3.6.197:8000/";
//    private static final String ROOT_URL = "http://172.20.10.2:8000/";
    private static final String ROOT_URL = "http://127.0.0.1:8000/";


    public static void getObjectRoom(ArrayList<Room> rooms) throws JsonParseException {

        try {
            URL url = new URL(ROOT_URL + "Patients/getObjectRoom");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

//            Platform.runLater

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonArray list = rootObj.getAsJsonArray("list"); //just grab the zipcode

                for (JsonElement jsonElement : list) {

                    System.out.println(jsonElement);

                    Drug drug = null;
                    JsonElement medicineJElement = jsonElement.getAsJsonObject().get("medicine");

                    if (!medicineJElement.isJsonNull()) {
                        System.out.println("not null ");
                        JsonObject medicineJObject = medicineJElement.getAsJsonObject();
                        drug = new Drug(medicineJObject.get("id").getAsInt(), medicineJObject.get("name").getAsString());
                    }

//                    Patient patient = null;
//                    JsonElement patientJElement = jsonElement.getAsJsonObject().get("patient");
//
//                    if (patientJElement != null) {
//                        JsonObject patientJObject = patientJElement.getAsJsonObject();
//                        patient = new Patient(patientJObject.get("id").getAsInt());
//                    }

                    Room room = new Gson().fromJson(jsonElement, Room.class);

                    System.out.println(room);

//                    room.setDrug(drug);
                    rooms.add(room);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getDrugList(ArrayList<Drug> drugs) {

        try {
            URL url = new URL(ROOT_URL + "Patients/listMedicines");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonArray medicines = rootObj.getAsJsonArray("list"); //just grab the zipcode

                for (JsonElement jsonElement : medicines) {

                    drugs.add(new Gson().fromJson(jsonElement, Drug.class));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPatientList(ArrayList<Patient> patients) {

        try {
            URL url = new URL(ROOT_URL + "Patients/listPatients");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonArray list = rootObj.getAsJsonArray("list"); //just grab the zipcode

                for (JsonElement jsonElement : list) {

                    patients.add(new Gson().fromJson(jsonElement, Patient.class));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getRoomList(ArrayList<Room> rooms) {

        try {
            URL url = new URL(ROOT_URL + "Patients/listRooms");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonArray list = rootObj.getAsJsonArray("list"); //just grab the zipcode

                for (JsonElement jsonElement : list) {

                    rooms.add(new Gson().fromJson(jsonElement, Room.class));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getOrderList(ArrayList<Order> orderList) {

        try {
            URL url = new URL(ROOT_URL + "Patients/listOrders");
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {

                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                JsonObject rootObj = root.getAsJsonObject(); //May be an array, may be an object.
                JsonArray list = rootObj.getAsJsonArray("list"); //just grab the zipcode

                for (JsonElement jsonElement : list) {

                    System.out.println(list);
                    JsonObject jObject = jsonElement.getAsJsonObject();

                    int id = jObject.get("id").getAsInt();
                    String room = jObject.get("room").getAsString();
                    String drug = jObject.get("drug").getAsString();
                    String status = jObject.get("status").getAsString();
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jObject.get("timestamp").getAsString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Order order = new Order(id, room, drug, status, date);
                    System.out.println(order);
                    orderList.add(order);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addOrder(int room_id) {
        try {
            URL url = new URL(ROOT_URL + "Robots/addOrder/" + room_id);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {

                System.out.println("Order added");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRoomMedicine(int Room, int medicine) {
        try {
            URL url = new URL(ROOT_URL + "Patients/setRoomMedicine/" + Room + "/" + medicine);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {

                System.out.println("Medicine : " + medicine + " in room : " + Room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addDrug(int drugId, String drugName) {
        try {
            URL url = new URL(ROOT_URL + "Patients/setMedicine/" + drugId + "/" + drugName);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {
                System.out.println("id : " + drugId + " drugName : " + drugName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addPatient(Integer patient_id, Integer room) {
        addPatient(patient_id, room, null);
    }

    public static void addPatient(Integer patient_id, Integer room, Integer week) {
        try {

            String sURL = ROOT_URL + "Patients/addPatient/" + room + "/" + patient_id;

            if (week != null) {
                sURL = sURL + "/" + week;
            }

            URL url = new URL(sURL);

            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {
                System.out.println("id : " + patient_id + " drugName : " + room + " on week " + week);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPatientCondition(int patientID, String status) {

        try {
            URL url = new URL(ROOT_URL + "Patients/setPatientCondition/" + patientID + "/" + status);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            System.out.println(request.getResponseCode() + " from : " + url);

            if (request.getResponseCode() == 200) {
                System.out.println("nSeSo : " + patientID + " Status : " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
