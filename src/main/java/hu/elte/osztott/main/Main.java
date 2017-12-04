package hu.elte.osztott.main;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class Main {
    private Algorithm alg;

    public Main() {
        alg = new Algorithm();
        alg.run();
        Graph g0 = createGraph("G0", true);
        addOriginalNodes(g0);
        addOriginalEdges(g0);

        Graph g1 = createGraph("G1", false);
        addStyledNodes(g1);
        addSelectedEdges(g1);
        addOriginalEdges(g1);

        Graph g2 = createGraph("G2", false);
        addStyledNodes(g2);
        addSelectedEdges(g2);
        display(g2);
        display(g1);
        display(g0);
    }

    private void addOriginalEdges(Graph g) {
        alg.getGraph().getData().forEach(edge ->
                g.addEdge(edge[0], edge[1], edge[2])
        );
    }

    private void addOriginalNodes(Graph g) {
        alg.getGraph().getNodes().forEach(node -> {
            Node n = g.addNode(node.getLabel());
            n.setAttribute("ui.label", node.getLabel());
        });
    }

    private void addStyledNodes(Graph g) {
        alg.getGraph().getNodes().forEach(node -> {
            Node n = g.addNode(node.getLabel());
            n.setAttribute("ui.label", node.getLabel());
            n.addAttribute("ui.class", node.getStyleClass());
        });
    }

    private void addSelectedEdges(Graph g) {
        alg.getEdges().forEach(edge -> {
            Edge e = g.addEdge(String.valueOf(edge.getStart().getId() + "-" + edge.getEnd().getId()), edge.getStart().getLabel(), edge.getEnd().getLabel());
            if (e != null) {
                if ("CLUSTERCONNECT".equals(edge.getType())) {
                    e.addAttribute("ui.class", "connector");
                } else {
                    e.addAttribute("ui.class", "tree");
                }
            }
        });
    }

    private Graph createGraph(String id, boolean original) {
        Graph g = new SingleGraph(id);
        g.setStrict(false);
        if (!original)
            g.addAttribute("ui.stylesheet", "url('" + getClass().getClassLoader().getResource("stylesheet") + "')");
        return g;
    }

    private void display(Graph graph) {
        Viewer viewer = graph.display();
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        new Main();
    }
}
