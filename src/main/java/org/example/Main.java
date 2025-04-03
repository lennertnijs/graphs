package org.example;

import graph.Graph;
import graph.IGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args)
    {
        SecretSantaSelector selector = new SecretSantaSelector();
        List<IGraph<String>> graphs = loadGraphs();
        int count = 0;
        int total  = 0;
        long start = System.currentTimeMillis();
        List<Map<String, List<String>>> selections = new ArrayList<>();
        for(IGraph<String> graph : graphs){
            Map<String, List<String>> selection;
            total++;
            try{
                selection = selector.attempt(graph);
                System.out.println(selection);
            }catch(Exception e){
                e.printStackTrace();
                count++;
            }
        }

        System.out.println("Duration: " + (System.currentTimeMillis() - start));
        System.out.println("Duration per graph: " + (float)(System.currentTimeMillis() - start) / total);
        System.out.println("Impossible ones: " + count);
        System.out.println("Total ones: " + total);
    }

    private static List<IGraph<String>> loadGraphs()
    {
        List<IGraph<String>> graphs = new ArrayList<>();
        System.out.println(System.getProperty("user.dir"));

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