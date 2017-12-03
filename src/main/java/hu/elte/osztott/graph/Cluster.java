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
}
