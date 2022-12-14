package metro;

public class Line {
    String id;
    String name;

    public Line(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "ID: " + getId() + "," + " Name: " + getName();
    }
}
