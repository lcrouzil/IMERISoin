package IMERISoin.Model.deserializers;

import IMERISoin.Model.Patient;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PatientDeserializer implements JsonDeserializer<Patient> {
    @Override
    public Patient deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject();

        int id = jObject.get("id").getAsInt();
        String Status = jObject.get("status").toString();

        return new Patient(id, Status);
    }
}
