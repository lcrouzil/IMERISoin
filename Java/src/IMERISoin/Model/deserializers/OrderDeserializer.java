package IMERISoin.Model.deserializers;

import IMERISoin.Model.Order;
import IMERISoin.Model.Room;
import com.google.gson.*;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDeserializer implements JsonDeserializer<Order> {
    @Override
    public Order deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
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

        return new Order(id, room, drug, status, date);
    }
}
