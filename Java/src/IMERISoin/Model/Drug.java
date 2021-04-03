package IMERISoin.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Drug {

    private final int id;

    private final String name;

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

    public String debugString() {
        return "Drug{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
