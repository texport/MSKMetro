package metro;

public class Station {
    String name;
    String idLine;

    public Station(String name, String idLine) {
        this.name = name;
        this.idLine = idLine;
    }

    public String getName() {
        return name;
    }

    public String getIdLine() {
        return idLine;
    }

    public String toString() {
        return "Name: " + getName() + "," + " ID LINE: " + getIdLine();
    }
}
