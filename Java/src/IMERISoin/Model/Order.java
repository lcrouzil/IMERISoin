package IMERISoin.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private int id;
    private String room;
    private String drug;
    private String status;
    private Date date;

    public Order (int id, String room, String drug, String status, Date date) {
        super();
        this.id = id;
        this.room = room;
        this.drug = drug;
        this.status = status;
        this.date = date;
    }

    public StringProperty getIdFx() {
        return new SimpleStringProperty(String.valueOf(id));
    }

    public StringProperty getRoomFx() {
        return new SimpleStringProperty(room);
    }

    public StringProperty getDrugFx() {
        return new SimpleStringProperty(drug);
    }

    public StringProperty getStatusFx() {
        return new SimpleStringProperty(status);
    }

    public StringProperty getDateFx() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return new SimpleStringProperty(dateFormat.format(date));
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", room='" + room + '\'' +
                ", drug='" + drug + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
