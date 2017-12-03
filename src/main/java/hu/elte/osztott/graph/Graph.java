package hu.elte.osztott.graph;

import hu.elte.osztott.service.DataService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<Integer, Node> nodes;
    private Map<Node, List<Node>> adj;
    private List<Node> nodeList;
    private DataService service = new DataService();
    private int maxId;
    private int minId;

    public Graph() {
        initGraph();
    }

    private void initGraph() {
        nodes = new HashMap<>();
        nodeList = new ArrayList<>();
        adj = new HashMap<>();
        List<String[]> data = service.getData();

        Map<String, Node> nodeMap = new HashMap<>();
        for (String[] row : data) {
            nodeMap.putIfAbsent(row[1], new Node(nodeMap.size(), row[1]));
            nodeMap.putIfAbsent(row[2], new Node(nodeMap.size(), row[2]));
            adj.putIfAbsent(nodeMap.get(row[1]), new ArrayList<>());
            adj.putIfAbsent(nodeMap.get(row[2]), new ArrayList<>());
            adj.get(nodeMap.get(row[1])).add(nodeMap.get(row[2]));
            adj.get(nodeMap.get(row[2])).add(nodeMap.get(row[1]));
        }
        minId = 0;
        maxId = nodeMap.size() - 1;
        nodeMap.values().forEach(n -> {
            nodes.put(n.getId(), n);
            nodeList.add(n);
        });
    }

    public Node getNode(Integer id) {
        return nodes.get(id);
    }

    public List<Node> getNeighbours(Integer id) {
        return adj.get(nodes.get(id));
    }

    public List<Node> getNodes() {
        return nodeList;
    }

    public int getMaxId() {
        return maxId;
    }

    public int getMinId() {
        return minId;
    }
}
