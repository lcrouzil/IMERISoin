package IMERISoin.Model.deserializers;

import IMERISoin.Model.Patient;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PatientDeserializer implements JsonDeserializer<Patient> {
    @Override
    public Patient deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject();

        int id = jObject.get("id").isJsonNull() ? 0 : jObject.get("id").getAsInt();
        String status = jObject.get("status").isJsonNull() ? null : jObject.get("status").getAsString();
        Integer week = jObject.get("week").isJsonNull() ? null : jObject.get("week").getAsInt();
        Integer room_id = jObject.get("room_id").isJsonNull() ? null : jObject.get("room_id").getAsInt();
        String drug = jObject.get("drug").isJsonNull() ? null : jObject.get("drug").getAsString();

        return new Patient(id, room_id, week, drug, status);
    }
}
