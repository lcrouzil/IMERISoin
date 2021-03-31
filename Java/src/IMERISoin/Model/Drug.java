package IMERISoin.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Drug {

    private IntegerProperty id;
    private StringProperty name;

    public Drug(int id, String name) {
        super();

        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public StringProperty getName() {
        return name;
    }
    @Override
    public String toString() {
        return "Drug{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
