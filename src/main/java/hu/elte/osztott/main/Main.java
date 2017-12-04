package hu.elte.osztott.main;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class Main {

    public Main() {
        Algorithm alg = new Algorithm();
        alg.run();
        Graph g = new SingleGraph("Test");
        g.setStrict(false);
        g.addAttribute("ui.stylesheet", "url('" + getClass().getClassLoader().getResource("stylesheet") + "')");

        alg.getGraph().getNodes().forEach(node -> {
            Node n = g.addNode(node.getLabel());
            n.setAttribute("ui.label", node.getLabel());
            n.addAttribute("ui.class", node.getStyleClass());
        });

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

        alg.getGraph().getData().forEach(edge ->
                g.addEdge(edge[0], edge[1], edge[2])
        );

        display(g);
    }

    private void display(Graph graph) {
        Viewer viewer = graph.display();
        View view = viewer.getDefaultView();
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        new Main();
    }
}
