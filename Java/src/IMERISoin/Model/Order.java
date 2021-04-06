package IMERISoin.Model;

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
