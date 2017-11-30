package hu.elte.osztott.graph;

import hu.elte.osztott.service.DataService;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;

public class Algorithm {

    private DataService service = new DataService();
    private Graph graph;

    public Algorithm() {
        graph = new SingleGraph("Test");
        graph.setAutoCreate(true);
        graph.setStrict(false);
        createGraph();
    }

    private void createGraph() {
        List<String[]> lines = service.getData();
        for (String[] line : lines) {
            addEdge(line[0], line[1], line[2]);
        }
    }

    public void addNode(String id) {
        addNode(id, id);
    }

    private void addNode(String id, String label) {
        if (graph.getNode(id) == null) {
            graph.addNode(id);
            graph.getNode(id).setAttribute("label", label);
        }
    }

    private void addEdge(String edgeId, String fromId, String toId) {
        addEdge(edgeId, fromId, fromId, toId, toId);
    }

    private void addEdge(String edgeId, String fromId, String fromName, String toId, String toName) {
        addNode(fromId, fromName);
        addNode(toId, toName);
        graph.addEdge(edgeId, fromId, toId, true);
    }

    public void show() {
        graph.display();
    }
}
