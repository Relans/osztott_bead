package hu.elte.osztott;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static Random generator = new Random(0);

    private static int generateRandom() {
        double v = generator.nextDouble();
        return (int) (v*100) % 50;
    }

    public static void main(String[] args){
        System.out.println("Hello world");
        Graph graph = new SingleGraph("Tutorial 1");
        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.addNode("0");
        for (int i = 0; i < 100; i++) {
            graph.addEdge("a"+i,""+(i),""+(i+1),true);
            graph.addEdge("b"+i,""+(i),""+(i+2),true);
        }
        for(Node n : graph){
            n.setAttribute("label",n.getId());
        }
        graph.display();
        Viewer viewer = graph.display();
    }
}
