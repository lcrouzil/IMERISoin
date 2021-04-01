package IMERISoin.Model.deserializers;

import IMERISoin.Model.Drug;
import IMERISoin.Model.Patient;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PatientDeserializer implements JsonDeserializer<Patient> {
    @Override
    public Patient deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject();

        int id = jObject.get("id").getAsInt();
        String name = jObject.get("name").getAsString();
        String Status = jObject.get("status").getAsString();

        return new Patient(id, name, Status);
    }
}
