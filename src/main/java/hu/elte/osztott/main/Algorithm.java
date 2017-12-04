package hu.elte.osztott.main;

import hu.elte.osztott.graph.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Algorithm {
    private final int r;
    private final int k;
    private Random random = new Random(0);
    private Graph graph;
    private Map<Integer, Node> centers;
    private Map<Integer, VoronoiCell> cells;
    private Map<Integer, List<Node>> bfsTrees;
    private Map<Cluster, List<Cluster>> clusterOfClusters;
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
        clusterOfClusters = new HashMap<>();
    }

    private int getRandom(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    private Node choseRandomNode(int minId, int maxId, boolean checkForCenter) {
        int randomId = getRandom(minId, maxId);
        int i = minId;
        Node newCenter = graph.getNode(randomId);
        while ((newCenter == null || (checkForCenter && newCenter.equals(newCenter.getCenter()))) && i <= maxId) {
            randomId = getRandom(minId, maxId);
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
            newCenter.setStyleClass("centerNode");
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
                    node.setStyleClass(node.getStyleClass() + ", singletonCluster");
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

    private void markRandomCenters() {
        cells.values().forEach(cell -> {
            cell.getCenter().setMarked(getRandom(0, 100) < 25);
            cell.getNodes().values().forEach(node -> {
                node.setMarked(cell.getCenter().isMarked());
                if (node.isMarked()) {
                    node.setStyleClass(node.getStyleClass() + ", marked");
                }
            });
        });
    }

    private List<Cluster> getAdjacentCluster(Cluster c) {
        List<Cluster> result = new ArrayList<>();
        for (Node node : c.getNodes()) {
            for (Node neighbour : graph.getNeighbours(node.getId())) {
                result.addAll(cells.get(neighbour.getCenter().getId()).getClusters()
                        .stream().filter(cluster -> cluster.getNodes().contains(neighbour)
                                && !result.contains(cluster)
                                && !cluster.equals(c)
                                && !neighbour.getCenter().equals(node.getCenter()))
                        .collect(Collectors.toList()));
            }
        }
        return result;
    }

    private void generateClusterOfClusters() {
        cells.values().stream().filter(VoronoiCell::isMarked).forEach(cell ->
                cell.getClusters().forEach(cluster ->
                        clusterOfClusters.put(cluster, getAdjacentCluster(cluster))));
    }

    private void generateEdgesBetweenClusters() {

        for (Cluster markedCluster : clusterOfClusters.keySet()) {
            for (Cluster cluster : clusterOfClusters.get(markedCluster)) {
                for (Node node : cluster.getNodes()) {
                    Node node1 = graph.getNeighbours(node.getId()).stream().filter(n -> markedCluster.getNodes().contains(n)).findFirst().orElse(null);
                    if (node1 != null) {
                        edges.add(new Edge(edges.size(), node, node1, "CLUSTERCONNECT"));
                        break;
                    }
                }
            }
        }
        for (VoronoiCell cell : cells.values()) {
            for (Cluster cluster : cell.getClusters()) {
                boolean isAdjToMarked = false;
                for (List<Cluster> clusters : clusterOfClusters.values()) {
                    isAdjToMarked = clusters.contains(cluster);
                    if (isAdjToMarked) {
                        break;
                    }
                }
                if (!isAdjToMarked) {
                    for (Cluster cluster1 : getAdjacentCluster(cluster)) {
                        for (Node node : cluster.getNodes()) {
                            Node node1 = graph.getNeighbours(node.getId()).stream().filter(n -> cluster1.getNodes().contains(n)).findFirst().orElse(null);
                            if (node1 != null) {
                                edges.add(new Edge(edges.size(), node, node1, "CLUSTERCONNECT"));
                                break;
                            }
                        }
                    }
                }
            }
        }
        /*
        * Minden olyan cluster, ami nem szerepel a clusterOfClusters értékei között
        * minden szomszédos (getAdjacentCluster) clusterhez megkeresni a minimális élet
        * hozzáadni ezeket az élekhez
        * */
    }

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
        markRandomCenters();
        generateClusterOfClusters();
        generateEdgesBetweenClusters();
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

    public Map<Cluster, List<Cluster>> getClusterOfClusters() {
        return clusterOfClusters;
    }
}
