package IMERISoin.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Room {

    private int id;
    private Patient patient;
    private Drug drug;
    private String path;
    private String name;

    private Integer patient_id;
    private Integer drug_id;

    public Room(int id, String path, String name) {
        this(id, null, null, path, name);
    }

    public Room(int id, Integer patient, Integer drug, String path, String name){
        super();
        this.id = id;
        this.patient_id = patient;
        this.drug_id = drug;
        this.path = path;
        this.name = name;
    }

    public StringProperty getIdFx() {
        return new SimpleStringProperty(String.valueOf(id));
    }

    public StringProperty getNameFx() {
        return new SimpleStringProperty(name);
    }

    public StringProperty getPathFx() {
        return new SimpleStringProperty(path);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", patient=" + patient +
                ", drug=" + drug +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public Integer getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(Integer drug_id) {
        this.drug_id = drug_id;
    }
}
