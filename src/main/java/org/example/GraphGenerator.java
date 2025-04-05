package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphGenerator
{

    public GraphGenerator()
    {

    }

    public static void generateAndWrite(int size, String fileName)
    {
        write(generateFullGraph(size), fileName);
    }

    public static Map<String, List<String>> generateFullGraph(int size)
    {
        Map<String, List<String>> map = new HashMap<>();
        for(int i = 0; i < size; i++){
            String str = String.valueOf(i);
            map.put(str, new ArrayList<>());
            List<String> values = map.get(str);
            for(int j = 0; j < size; j++){
                if(i == j) continue;
                values.add(String.valueOf(j));
            }
        }
        return map;
    }

    public static void write(Map<String, List<String>> map, String name)
    {
        String csvFile = "D:/secret santa/untitled/src/test/java/" + name + ".csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            for(String key : map.keySet()){
                writer.append(key).append(",");
                for(String value : map.get(key)){
                    writer.append(value).append(" ");
                }
                writer.append("\n");
            }
            writer.append("endoffile,");
            System.out.println("CSV file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
