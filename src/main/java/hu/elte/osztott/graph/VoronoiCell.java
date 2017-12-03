package hu.elte.osztott.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoronoiCell {
    private Integer id;
    private Node center;
    private Map<Integer, Node> nodes;
    private int rang;
    private List<Cluster> clusters;

    public VoronoiCell(Node center) {
        this.center = center;
        this.nodes = new HashMap<>();
        this.nodes.put(center.getId(), center);
        this.id = center.getId();
        this.clusters = new ArrayList<>();
    }

    public boolean contains(Node node) {
        return node != null && nodes.containsKey(node.getId());
    }

    public void remove(Node node) {
        if (node != null && !node.equals(center)) {
            nodes.remove(node.getId());
        }
    }

    public void remove(Integer nodeId) {
        Node node = nodes.get(nodeId);
        if (node != null && !node.equals(center)) {
            nodes.remove(node.getId());
        }
    }

    public void addNode(Node node) {
        if (node != null && !node.equals(center)) {
            nodes.put(node.getId(), node);
        }
    }

    public void addCluster(Cluster cluster) {
        clusters.add(cluster);
    }

    public List<Node> getNodeList() {
        return new ArrayList<>(nodes.values());
    }

    public Node getCenter() {
        return center;
    }

    public void setCenter(Node center) {
        this.center = center;
        this.id = center.getId();
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public Node getNode(Integer nodeId) {
        return nodes.get(nodeId);
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public Integer getId() {
        return id;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }
}
