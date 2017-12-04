package hu.elte.osztott.graph;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private Node center;
    private List<Node> nodes;

    public Cluster(Node center) {
        this.center = center;
        this.nodes = new ArrayList<>();
        nodes.add(center);
    }

    public Cluster(Node center, List<Node> nodes) {
        this.center = center;
        this.nodes = nodes;
    }

    public Node getCenter() {
        return center;
    }

    public void setCenter(Node center) {
        this.center = center;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cluster cluster = (Cluster) o;

        if (center != null ? !center.equals(cluster.center) : cluster.center != null) return false;
        return nodes != null ? nodes.equals(cluster.nodes) : cluster.nodes == null;
    }

    @Override
    public int hashCode() {
        int result = center != null ? center.hashCode() : 0;
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        return result;
    }
}
