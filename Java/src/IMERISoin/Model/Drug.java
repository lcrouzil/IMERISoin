package IMERISoin.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Drug {

    private int id;
    private String name;

    public Drug(int id, String name) {
        super();

        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public StringProperty getIdFx() {
        return new SimpleStringProperty(String.valueOf(id));
    }

    public String getName() {
        return name;
    }

    public StringProperty getNameFx() {
        return new SimpleStringProperty(name);
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
