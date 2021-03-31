package IMERISoin.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {

    private int id;

    private StringProperty name;

    private StringProperty status;

    private Drug drug;

    private Room room;

    /**
     * @param id     id
     * @param name   Name of Patient
     * @param status Status
     */
    public Patient(int id, String name, String status) {
        this(id, name, status, null);
    }

    /**
     * @param id     id
     * @param name   Name of Patient
     * @param status Status
     * @param drug   Object drug
     */
    public Patient(int id, String name, String status, Drug drug) {
        this(id, name, status, drug, null);
    }

    /**
     * @param id     id
     * @param name   Name of Patient
     * @param status Status
     * @param drug   Object drug
     * @param room   Object Room
     */
    public Patient(int id, String name, String status, Drug drug, Room room) {
        super();
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.status = new SimpleStringProperty(status);
        this.drug = drug;
        this.room = room;
    }


    /**
     * @return id of Patient
     */
    public int getId() {
        return id;
    }

    /**
     * @return name of Patient
     */
    public StringProperty getName() {
        return name;
    }

    /**
     * @param name set Name of Patient
     */
    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * @return status of Patient
     */
    public StringProperty getStatus() {
        return status;
    }

    /**
     * @param status set Status of Patient
     */
    public void setStatus(String status) {
        this.status = new SimpleStringProperty(status);
    }

    /**
     * @return drug Object of Patient
     */
    public Drug getDrug() {
        return drug;
    }

    /**
     * @param drug set Object Drug og Patient
     */
    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    /**
     * @return room Object of Room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room set Object of Room
     */
    public void setRoom(Room room) {
        this.room = room;
    }
}
