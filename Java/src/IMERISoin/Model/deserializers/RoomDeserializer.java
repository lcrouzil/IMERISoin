package IMERISoin.Model.deserializers;

import IMERISoin.Model.Drug;
import IMERISoin.Model.Room;
import IMERISoin.services.HttpServices;
import com.google.gson.*;

import java.lang.reflect.Type;

public class RoomDeserializer implements JsonDeserializer<Room> {
    @Override
    public Room deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject();
//        System.out.println(jObject.toString());

        int id = jObject.get("id").getAsInt();
        int drug_id = jObject.get("drug_id").getAsInt();
        int patient_id = jObject.get("patient_id").getAsInt();
        String path = jObject.get("path").getAsString();
        String name = jObject.get("name").getAsString();

        return new Room(id, path, name);
    }
}
