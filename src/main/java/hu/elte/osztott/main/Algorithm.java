package hu.elte.osztott.main;

import hu.elte.osztott.graph.Graph;
import hu.elte.osztott.graph.Node;

import java.util.*;

public class Algorithm {
    private final int r;
    private final int k;
    Random random = new Random();
    private Graph graph;
    private Map<Node, List<Node>> centers;

    public Algorithm() {
        this.r = 5;
        this.k = 2;
    }

    public Algorithm(int r, int k) {
        this.r = r;
        this.k = k;
    }

    private void init() {
        graph = new Graph();
        centers = new HashMap<>();
    }

    private Node choseRandomNode(int minId, int maxId, boolean checkForCenter) {
        int randomId = random.nextInt(maxId + 1 - minId) + minId;
        int i = minId;
        Node newCenter = graph.getNode(randomId);
        while ((newCenter == null || (checkForCenter && newCenter.equals(newCenter.getCenter()))) && i <= maxId) {
            randomId = random.nextInt(maxId + 1 - minId) + minId;
            newCenter = graph.getNode(randomId);
            i++;
        }
        if (i > maxId) {
            throw new RuntimeException("Infinite loop detected while choosing random Node");
        }
        ;
        return newCenter;
    }

    private void updateCenterOfNodes(Node center, List<Node> nodes, int d) {
        if (nodes != null && nodes.size() > 0) {
            for (Node node : nodes) {
                if (node.getDistanceToCenter() > d || node.getDistanceToCenter() < 0) {
                    node.setCenter(center);
                    node.setDistanceToCenter(d);
                    centers.get(center).add(node);
                    updateCenterOfNodes(center, graph.getNeighbours(node.getId()), d + 1);
                }
            }
        }
    }

    private void updateCenters() {
        for (Node center : centers.keySet()) {
            updateCenterOfNodes(center, graph.getNeighbours(center.getId()), 1);
        }
        ;
    }

    private void createCenters() {
        int maxId = graph.getMaxId();
        int minId = graph.getMinId();
        for (int i = 0; i < this.r; i++) {
            Node newCenter = choseRandomNode(minId, maxId, true);
            newCenter.setCenter(newCenter);
            newCenter.setDistanceToCenter(0);
            centers.put(newCenter, new ArrayList<>());
            centers.get(newCenter).add(newCenter);
        }
        updateCenters();
    }

    public void run() {
        System.out.println("Run start");
        init();
        createCenters();
        System.out.println("Run complete");
    }
}
