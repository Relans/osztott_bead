package hu.elte.osztott.main;

import hu.elte.osztott.graph.Graph;
import hu.elte.osztott.graph.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Algorithm {
    private final int r;
    private final int k;
    Random random = new Random();
    private Graph graph;
    private Map<Integer, Node> centers;
    private Map<Integer, Map<Integer, Node>> cells;

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
        cells = new HashMap<>();
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

    private void updateCenterOfNodes(Integer centerId, List<Node> nodes, int d) {
        for (Node node : nodes) {
            if (node.getCenter() != null) {
                if ((node.getDistanceToCenter() < d)
                        || (node.getDistanceToCenter() == d && node.getCenter().getId() > centerId)) {
                    continue;
                }
                cells.get(node.getCenter().getId()).remove(node.getId());
            }
            node.setDistanceToCenter(d);
            node.setCenter(centers.get(centerId));
            cells.get(centerId).put(node.getId(), node);
            updateCenterOfNodes(centerId, graph.getNeighbours(node.getId()), d + 1);
        }
    }

    private void crateCells() {
        for (Integer centerId : cells.keySet()) {
            updateCenterOfNodes(centerId, graph.getNeighbours(centerId), 1);
        }
    }

    private void createCenters() {
        int maxId = graph.getMaxId();
        int minId = graph.getMinId();
        for (int i = 0; i < this.r; i++) {
            Node newCenter = choseRandomNode(minId, maxId, true);
            newCenter.setCenter(newCenter);
            newCenter.setDistanceToCenter(0);
            centers.put(newCenter.getId(), newCenter);
            cells.put(newCenter.getId(), new HashMap<>());
            cells.get(newCenter.getId()).put(newCenter.getId(), newCenter);
        }
    }

    private void createClusters() {
        throw new RuntimeException("createClusters not implemented");
    }

    /* if we want to implement te Elkin-Neiman algorithm
    private void createRemoteNodes(){

    }

    private void runElkinNeiman(){

    }
    */

    public void run() {
        System.out.println("Run start");
        init();
        createCenters();
        crateCells();
        System.out.println("Run complete");
    }
}
