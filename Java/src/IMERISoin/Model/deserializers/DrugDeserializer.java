package IMERISoin.Model.deserializers;

import IMERISoin.Model.Drug;
import com.google.gson.*;

import java.lang.reflect.Type;

public class DrugDeserializer implements JsonDeserializer<Drug> {
    @Override
    public Drug deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject();

        int id = jObject.get("medicine").getAsInt();
        String name = jObject.get("name").getAsString();

        return new Drug(id, name);
    }
}
