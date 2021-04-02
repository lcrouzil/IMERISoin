package IMERISoin.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Room {

    private int id;
    private Patient patient;
    private Drug drug;
    private String path;
    private String name;

    public Room(int id, String path, String name) {
        this(id, null, null, path, name);
    }

    public Room(int id, Patient patient, Drug drug, String path, String name){
        super();
        this.id = id;
        this.patient = patient;
        this.drug = drug;
        this.path = path;
        this.name = name;
    }

    public Drug getDrug(){
        if (drug != null) {
            return drug;
        }

        return new Drug(0, "");
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Patient getPatient(){

        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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

    public StringProperty getDrugNameFx(){
        if (drug != null) {
            System.out.println("drug not null " + drug.getNameFx());
            return drug.getNameFx();
        }
        System.out.println("drug is null !");

        return new SimpleStringProperty("test");
    }



    public StringProperty getPatientNameFx() {
        return new SimpleStringProperty("test");
    }

    @Override
    public String toString() {
        return id + " " + name;
    }

    public String debugString() {
        return "Room{" +
                "id=" + id +
                ", patient=" + patient +
                ", drug=" + drug +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }



}
