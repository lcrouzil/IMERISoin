package IMERISoin.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {

    private final int id;

    private String status;

    /**
     * @param id     id
     */
    public Patient(int id) {
        this(id, null);
    }

    /**
     * @param id     id
     * @param status Status
     */
    public Patient(int id, String status) {
        super();
        this.id = id;
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
                ", status='" + status + '\'' +
                '}';
    }
}
