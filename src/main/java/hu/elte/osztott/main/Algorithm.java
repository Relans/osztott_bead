package hu.elte.osztott.main;

import hu.elte.osztott.graph.*;

import java.util.*;
import java.util.stream.IntStream;

public class Algorithm {
    private final int r;
    private final int k;
    private Random random = new Random(0);
    private Graph graph;
    private Map<Integer, Node> centers;
    private Map<Integer, VoronoiCell> cells;
    private Map<Integer, List<Node>> bfsTrees;
    private List<Edge> edges;

    public Algorithm() {
        this.r = 5;
        this.k = 3;
    }

    public Algorithm(int r, int k) {
        this.r = r;
        this.k = k;
    }

    private void init() {
        graph = new Graph();
        centers = new HashMap<>(this.r);
        cells = new HashMap<>(this.r);
        bfsTrees = new HashMap<>(this.r);
        edges = new ArrayList<>();
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
            cells.get(centerId).addNode(node);
            updateCenterOfNodes(centerId, graph.getNeighbours(node.getId()), d + 1);
        }
    }

    private void crateCells() {
        for (Integer centerId : centers.keySet()) {
            cells.put(centerId, new VoronoiCell(centers.get(centerId)));
            cells.get(centerId).setRang(random.nextInt());
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
        }
    }

    private List<Node> bfsTree(Node s) {
        Map<Integer, Boolean> visited = new HashMap<>();
        Node center = s;
        LinkedList<Node> queue = new LinkedList<>();
        List<Node> bfs = new ArrayList<>();

        visited.put(s.getId(), true);
        s.setParent(s);
        queue.add(s);

        while (queue.size() != 0) {
            s = queue.poll();
            bfs.add(s);

            Node finalS = s;
            graph.getNeighbours(s.getId()).stream().sorted(Comparator.comparingInt(Node::getId)).forEach(neighbour -> {
                if (cells.get(center.getId()).contains(neighbour)) {
                    int n = neighbour.getId();
                    if (!visited.containsKey(n)) {
                        visited.put(n, true);
                        neighbour.setParent(finalS);
                        queue.add(neighbour);
                    }
                }
            });
        }
        return bfs;
    }

    private void createBfsTrees() {
        for (VoronoiCell voronoiCell : cells.values()) {
            bfsTrees.put(voronoiCell.getId(), bfsTree(voronoiCell.getCenter()));
        }
    }

    private List<Node> getSubTreeForNode(Node node) {
        List<Node> bfsTree = bfsTrees.get(cells.get(node.getCenter().getId()).getId());
        if (node.getCenter() == node) {
            return bfsTree;
        }
        List<Node> result = new ArrayList<>(bfsTree.size());
        for (int i = 0; i < bfsTree.size(); i++) {
            if (bfsTree.get(i).equals(node)) {
                result.add(node);
                IntStream.range(i, bfsTree.size()).filter(j -> result.contains(bfsTree.get(j).getParent())).forEach(j -> result.add(bfsTree.get(j)));
                return result;
            }
        }
        return result;
    }

    private void createClusters() {
        cells.values().forEach(this::setClustersForCell);
    }

    private void setClustersForCell(VoronoiCell cell) {
        if (cell != null) {
            if (cell.getNodes().size() <= k) {
                cell.addCluster(new Cluster(cell.getCenter(), cell.getNodeList()));
                return;
            }

            Map<Node, Cluster> clusterMap = new HashMap<>();
            for (Node node : cell.getNodes().values()) {
                if (getSubTreeForNode(node).size() >= k) {
                    clusterMap.put(node, new Cluster(node));
                } else {
                    Node u = node;
                    while (getSubTreeForNode(u.getParent()).size() < k) {
                        u = u.getParent();
                    }
                    clusterMap.put(u, new Cluster(u, getSubTreeForNode(u)));
                }
            }
            cell.getClusters().addAll(clusterMap.values());
        }
    }

    /* if we want to implement te Elkin-Neiman algorithm
    private void createRemoteNodes(){

    }

    private void runElkinNeiman(){

    }
    */

    private void createEdges() {
        for (VoronoiCell cell : cells.values()) {
            List<Node> nodes = bfsTrees.get(cell.getId());
            for (Node node : nodes) {
                if (!node.equals(node.getParent())) {
                    edges.add(new Edge(edges.size(), node.getParent(), node));
                }
            }
        }
    }

    public void run() {
        System.out.println("Run start");
        init();
        createCenters();
        crateCells();
        createBfsTrees();
        createClusters();
        createEdges();
        System.out.println("Run complete");
    }

    public Map<Integer, Node> getCenters() {
        return centers;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Map<Integer, VoronoiCell> getCells() {
        return cells;
    }

    public Graph getGraph() {
        return graph;
    }
}
