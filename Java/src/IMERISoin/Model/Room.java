package IMERISoin.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Room {

    private int id;
    private String path;
    private String name;

    public Room(int id, String path, String name) {
        super();
        this.id = id;
        this.path = path;
        this.name = name;
    }

    public int getId() {
        return id;
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
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }



}
