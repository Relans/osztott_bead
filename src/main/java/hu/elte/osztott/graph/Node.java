package hu.elte.osztott.graph;

public class Node {
    private int id;
    private String label;
    private Node center;
    private int distanceToCenter = -1;

    public Node(int id) {
        this.id = id;
        this.label = String.valueOf(id);
    }

    public Node(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Node getCenter() {
        return center;
    }

    public void setCenter(Node center) {
        this.center = center;
    }

    public int getDistanceToCenter() {
        return distanceToCenter;
    }

    public void setDistanceToCenter(int distanceToCenter) {
        this.distanceToCenter = distanceToCenter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
