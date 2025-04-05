package org.example;

import java.util.*;

public final class SecretSantaSolution
{
    private final Map<String, String> solution;
    private final boolean solved;
    public final static String SEPARATOR = "~";

    public SecretSantaSolution(Map<String, List<String>> result)
    {
        this.solution = extractSolution(result);
        this.solved = isSolved(solution);
    }

    public Map<String, String> getSolution()
    {
        return solution;
    }

    public boolean isSolved()
    {
        return solved;
    }

    private Map<String, String> extractSolution(Map<String, List<String>> result)
    {
        Map<String, String> solution = new HashMap<>();
        Set<String> solvedKeys = new HashSet<>();
        for(String key : result.keySet()){
            if(!key.contains(SEPARATOR)){
                solvedKeys.add(key);
                continue;
            }
            List<String> values = result.get(key);
            if(values.size() == 1) solution.put(values.get(0), key);
            if(values.size() > 1) throw new IllegalStateException("Error");
        }
        for(String key : solvedKeys){
            if(solution.containsKey(key)) continue;
            solution.put(key, "No mapping found!");
        }
        return solution;
    }

    private boolean isSolved(Map<String, String> mapping)
    {
        for(String key : mapping.keySet()){
            if(mapping.get(key).equals("No mapping found!")) return false;
        }
        return true;
    }
}
