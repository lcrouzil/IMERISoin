package IMERISoin.Model;

public class Room {

    private int id;

    private Patient patient;

    public Room(int roomNumber){
        super();
        id = roomNumber;
        patient = null;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
