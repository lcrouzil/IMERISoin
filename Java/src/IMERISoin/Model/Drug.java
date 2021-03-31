package IMERISoin.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

    public IntegerProperty getIdFx() {
        return new SimpleIntegerProperty(id);
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
