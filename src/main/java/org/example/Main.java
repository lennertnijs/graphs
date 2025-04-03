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
                checkUniqueMapping(selection);
            }catch(Exception e){
                count++;
                //System.out.println("nope");
            }
        }
        writeToCSV("D:/secret santa/untitled/src/test/java/results.csv", selections);
        System.out.println("Duration: " + (System.currentTimeMillis() - start));
        System.out.println("Duration per graph: " + (float)(System.currentTimeMillis() - start) / total);
        System.out.println("Impossible ones: " + count);
        System.out.println("Total ones: " + total);
    }

    public static void checkUniqueMapping(Map<String, List<String>> selection)
    {
        Set<String> keys = new HashSet<>();
        Set<String> values = new HashSet<>();
        for(String key : selection.keySet()){
            String value = selection.get(key).get(0);
            keys.add(key);
            values.add(value);
        }
        assert(keys.size() == values.size());
    }

    public static void writeToCSV(String filename, List<Map<String, List<String>>> data) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map<String, List<String>> item : data) {
                for(String key : item.keySet()){
                    String s = null;
                    if(item.get(key).size() == 1){
                        s = item.get(key).getFirst();
                    }else{
                        s = "this one aint solvable dawg";
                    }
                    writer.append(key).append(":").append(s).append(",");
                }
                writer.append("\n"); // Write each string to a new line
            }
            System.out.println("Data successfully written to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
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