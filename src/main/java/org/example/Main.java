package org.example;

import graph.Graph;
import graph.IGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        //GraphGenerator.generateAndWrite(1_000, "large");
        runSingleTest();
    }

    public static void runSingleTest()
    {
        SecretSantaSelector selector = new SecretSantaSelector();
        int total = 0;
        int impossible = 0;
        List<IGraph<String>> graphs = loadGraphs();
        long start = System.currentTimeMillis();
        for (IGraph<String> graph : graphs) {
            total++;
            if(!selector.findMaximumMatching(graph).isSolved()){
                impossible++;
            }
        }
        System.out.printf("Total runtime: %d\n", System.currentTimeMillis() - start);
        System.out.printf("Unsolvable graphs: %d\n", impossible);
        System.out.printf("Total amount of graphs: %d\n", total);
    }

    public static void runIterTest()
    {
        long total = 0;
        int tests = 10;
        int iter = 10;
        for(int i = 0; i < tests; i++){
            total += runTest(iter);
            System.out.println("Iter done");
        }
        System.out.printf("Average time for the whole test: %f \n", (float)total / tests);
        System.out.printf("Total time: %d", total);
    }

    public static long runTest(int iter)
    {
        SecretSantaSelector selector = new SecretSantaSelector();
        long total = 0;
        List<IGraph<String>> graphs = loadGraphs();
        for(int i = 0; i < iter; i++){
            long start = System.currentTimeMillis();
            for(IGraph<String> graph : graphs){
                selector.findMaximumMatching(graph);
            }
            total += (System.currentTimeMillis() - start);
        }
        return total;
    }

    private static List<IGraph<String>> loadGraphs()
    {
        List<IGraph<String>> graphs = new ArrayList<>();
        // System.out.println(System.getProperty("user.dir"));

        String filePath = "D:/secret santa/untitled/src/test/java/configurations.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.mark(1);
            if (br.read() != '\uFEFF') {
                br.reset(); // No BOM, reset the stream
            }
            String line;
            IGraph<String> graph = new Graph<>();
            Map<String, List<String>> info = new HashMap<>();
            while ((line = br.readLine()) != null) {

                if(line.isEmpty() || line.equals("endoffile,")){
                    graph.addVertices(new ArrayList<>(info.keySet()));
                    for(String key : info.keySet()){
                        graph.addEdges(key, info.get(key));
                    }
                    graphs.add(graph);
                    info = new HashMap<>();
                    graph = new Graph<>();
                    if(line.equals("endoffile,")){
                        return graphs;
                    }
                }else{
                    String[] keyValue = line.split(",");
                    List<String> values = keyValue.length == 2 ? Arrays.stream(keyValue[1].split(" ")).toList() : new ArrayList<>();
                    info.put(keyValue[0].trim(), values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graphs;
    }
}