package IMERISoin.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {

    private final int id;
    private Integer week;
    private Integer room;
    private String drug;
    private String status;

    /**
     * @param id     id
     */
//    public Patient(int id, Integer room, Integer week) {
//        this(id, room, week, null);
//    }

    /**
     * @param id id
     * @param room room id
     * @param week week format ( weekYear) : 122020
     * @param drug drug String name
     * @param status status
     */
    public Patient(int id, Integer room, Integer week, String drug, String status) {
        super();
        this.id = id;
        this.room = room;
        this.week = week;
        this.drug = drug;
        this.status = status;
    }

    /**
     * @return id of Patient
     */
    public int getId() {
        return id;
    }


    public StringProperty getIdFx() {
        if (id == 0) {
            return new SimpleStringProperty("Vide");
        }
        return new SimpleStringProperty(String.valueOf(id));
    }

    public StringProperty getStatusFx() {
        return new SimpleStringProperty(status);
    }

    public StringProperty getWeekFx() {
        return new SimpleStringProperty(String.valueOf(week));
    }

    public StringProperty getRoomFx() {
        return new SimpleStringProperty(String.valueOf(room));
    }

    public StringProperty getDrugFx() {
        return new SimpleStringProperty(drug);
    }


    /**
     * @return status of Patient
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status set Status of Patient
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }



    public String debugString() {
        return "Patient{" +
                "id=" + id +
                ", week=" + week +
                ", room=" + room +
                ", status='" + status + '\'' +
                '}';
    }
}
