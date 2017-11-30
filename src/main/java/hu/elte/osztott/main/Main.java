package hu.elte.osztott.main;

import hu.elte.osztott.graph.Algorithm;

import java.util.Random;

public class Main {
    private static Random generator = new Random(0);

    private static int generateRandom() {
        double v = generator.nextDouble();
        return (int) (v*100) % 50;
    }

    public static void main(String[] args) throws InterruptedException {
        Algorithm alg = new Algorithm();
        alg.show();
    }
}
