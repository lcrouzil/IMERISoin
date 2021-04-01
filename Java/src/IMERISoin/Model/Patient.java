package IMERISoin.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {

    private int id;

    private String name;

    private String status;

    /**
     * @param id     id
     * @param name   Name of Patient
     */
    public Patient(int id, String name) {
        this(id, name, null);
    }

    /**
     * @param id     id
     * @param name   Name of Patient
     * @param status Status
     */
    public Patient(int id, String name, String status) {
        super();
        this.id = id;
        this.name = name;
        this.status = status;
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
    public String getName() {
        return name;
    }

    public StringProperty getNameFx() {
        return new SimpleStringProperty(name);
    }

    /**
     * @param name set Name of Patient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return status of Patient
     */
    public String getStatus() {
        return status;
    }

    public StringProperty getStatusFx() {
        return new SimpleStringProperty(status);
    }

    /**
     * @param status set Status of Patient
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
