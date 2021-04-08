package IMERISoin.Model.deserializers;

import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import IMERISoin.Model.Room;
import IMERISoin.services.HttpServices;
import com.google.gson.*;

import java.lang.reflect.Type;

public class RoomDeserializer implements JsonDeserializer<Room> {
    @Override
    public Room deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject();

        int id = jObject.get("id").getAsInt();

//        Patient patient = new Gson().fromJson(jObject.get("patient").getAsJsonObject(), Patient.class);
//
////        JsonObject medicineJ = jObject.get("medicine").getAsJsonObject();
////        Drug drug = new Drug(medicineJ.get("id").getAsInt(), medicineJ.get("name").toString());
//
//        Drug drug = null;
//        JsonElement medicineJElement = jsonElement.getAsJsonObject().get("medicine");
//
//        if (!medicineJElement.isJsonNull()) {
//            JsonObject medicineJObject = medicineJElement.getAsJsonObject();
//            drug = new Drug(medicineJObject.get("id").getAsInt(), medicineJObject.get("name").getAsString());
//        }


        //        Drug drug = new Gson().fromJson(jObject.get("medicine").getAsJsonObject(), Drug.class);

        String path = jObject.get("path").getAsString();
        String name = jObject.get("name").getAsString();

        return new Room(id, path, name);
    }
}
