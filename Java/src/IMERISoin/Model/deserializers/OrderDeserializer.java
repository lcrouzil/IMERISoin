package IMERISoin.Model.deserializers;

import IMERISoin.Model.Room;
import com.google.gson.*;

import java.lang.reflect.Type;

public class OrderDeserializer implements JsonDeserializer<Room> {
    @Override
    public Room deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject();

        int id = jObject.get("id").getAsInt();
        int drug_id = jObject.get("drug_id").getAsInt();
        String path = jObject.get("path").toString();
        String name = jObject.get("name").toString();

        return new Room(id, path, name);
    }
}
