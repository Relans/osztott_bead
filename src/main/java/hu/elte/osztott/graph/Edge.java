package hu.elte.osztott.graph;

public class Edge {
    private int id;
    private Node start;
    private Node end;
    private String type;

    public Edge(int id, Node start, Node end) {
        this.id = id;
        this.start = start;
        this.end = end;
        type = "NORMAL";
    }

    public Edge(int id, Node start, Node end, String type) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (id != edge.id) return false;
        if (start != null ? !start.equals(edge.start) : edge.start != null) return false;
        if (end != null ? !end.equals(edge.end) : edge.end != null) return false;
        return type != null ? type.equals(edge.type) : edge.type == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
