package org.example;

import graph.IGraph;
import tree.Node;

import java.util.*;

public final class SecretSantaSelector {


    public SecretSantaSelector()
    {

    }

//    public Map<String, List<String>> attempt(IGraph<String> graph)
//    {
//        Set<String> U = new ArrayList<>(graph.vertexCount());
//        List<String> V = new ArrayList<>(graph.vertexCount());
//        Map<String, List<String>> edges = new HashMap<>();
//        createBipartite(graph, U, V, edges);
//        return runHopCroftKarp(U, V, edges);
//    }

    public Map<String, List<String>> attemptTree(IGraph<String> graph)
    {
        Set<String> U = new HashSet<>(graph.vertexCount());
        Set<String> V = new HashSet<>(graph.vertexCount());
        Map<String, List<String>> edges = new HashMap<>();
        createBipartite(graph, U, V, edges);
        return runHopCroftKarpTree(U, V, edges);
    }

    /**
     * Creates a bipartite from a given graph
     * A bipartite will hold all the vertices in both U (left side) and V (right side), with the edges in the graph
     * now mapping from U to V (so within U nor V there aren't any edges)
     */
    private void createBipartite(IGraph<String> graph,
                                 Set<String> U,
                                 Set<String> V,
                                 Map<String, List<String>> edges)
    {
        Set<String> vertices = graph.getVertices();
        for(String s : vertices){
            U.add(s);
            V.add(s + "2");
            for(String successor : graph.getSuccessors(s)){
                if(!edges.containsKey(s)){
                    edges.put(s, new ArrayList<>());
                }
                edges.get(s).add(successor + "2");
            }
        }
    }


    /**
     * Runs Hopcroft-Karp's algorithm. In short, the following steps are done:
     * Setup:
     * - vertices in U and V are considered "unmarked"
     * Step 1: a BFS search algorithm is used to construct paths from unmarked elements of U to unmarked elements
     *         to V. The layer of BFS at which the first unmarked V is found, it will complete all paths and then
     *         stop the BFS. F will contain all elements from V found at said layer k.
     * Step 2: for each element e in F, the algorithm will walk through all the paths from the BFS step that finished
     *         in the element e. If a path is found, both the start node (from in U) and the end node (from in V) are
     *         marked as "marked", all the intermediate nodes are considered "gone" for the remainder of this DFs step
     *         and all the edges on the path are flipped (marked -> unmarked and vice versa)
     * The algorithm will return the marked edges, which is exactly the thing we're looking for.
     * P.S.: because of the nature of the algorithm, edges that are unmarked will always be followed U -> V,
     * while edges that are marked will always be followed from V -> U.
     * P.P.S.: any traversal, both in BFS and DFS, will need to keep in mind that adjacent edges should always be
     * opposite (so from marked -> unmarked -> ...)
     */
    private Map<String, List<String>> runHopCroftKarp(List<String> U,
                                                      List<String> V,
                                                      Map<String, List<String>> edges)
    {
        Map<String, List<String>> matchedEdges = new HashMap<>();
        for(String key : edges.keySet()){
            matchedEdges.put(key, new ArrayList<>());
        }
        while(true){
            Set<String> F = new HashSet<>();
            Map<String, List<List<String>>> bfsPaths = new HashMap<>();
            runBFS(U, V, edges, matchedEdges, F, bfsPaths);
            if(F.isEmpty()){
                if(!U.isEmpty() || !V.isEmpty()){
                    throw new IllegalArgumentException();
                }
                break;
            }

            HashSet<String> dfsUsedVertices = new HashSet<>();
            for(String f : F){
                List<String> path = selectPath(dfsUsedVertices, U, bfsPaths.get(f));
                if(path.isEmpty()) continue;
                U.remove(path.getFirst());
                V.remove(f);
                for(int i = 1; i < path.size(); i++){
                    String start = path.get(i - 1);
                    String next = path.get(i);
                    if(edges.containsKey(start)){
                        // start = a, b, c, d, next = a2, b2, c2, d2
                        edges.get(start).remove(next);
                        matchedEdges.get(start).add(next);
                    }else{
                        // start = a2, b2, c2, d2, next = a, b, c, d
                        matchedEdges.get(next).remove(start);
                        edges.get(next).add(start);
                    }
                }
            }
        }
        return matchedEdges;
    }

    private void runBFS(
            List<String> U,
            List<String> V,
            Map<String, List<String>> unmatched,
            Map<String, List<String>> matched,
            Set<String> F,
            Map<String, List<List<String>>> paths)
    {
        boolean UtoV = true;
        Queue<List<String>> queue = new LinkedList<>(U.stream().map(List::of).toList());
        while(!queue.isEmpty()){
            int queueSize = queue.size();
            for(int i = 0; i < queueSize; i++){
                List<String> path = queue.poll();
                String last = path.getLast();
                List<String> nextOptions = UtoV ? unmatched.get(last) : getReverse(last, matched);
                if(nextOptions == null) continue;
                for(String next : nextOptions){
                    if(path.contains(next)) continue;
                    List<String> copy = new ArrayList<>(path);
                    copy.add(next);
                    if(!UtoV || !V.contains(next)){
                        queue.add(copy);
                    }else{
                        if(!paths.containsKey(next)) paths.put(next, new ArrayList<>());
                        paths.get(next).add(copy);
                        F.add(next);
                    }
                }
            }
            if(!F.isEmpty()) return;
            UtoV  = !UtoV;
        }
    }

    private Map<String, List<String>> runHopCroftKarpTree(Set<String> U,
                                                          Set<String> V,
                                                          Map<String, List<String>> edges)
    {
        Map<String, List<String>> matchedEdges = new HashMap<>();
        for(String key : edges.keySet()){
            matchedEdges.put(key, new ArrayList<>());
        }
        while(true){
            List<Node<String>> endpoints = new ArrayList<>();
            runBFS(U, V, edges, matchedEdges, endpoints);
            if(endpoints.isEmpty()){
                if(!U.isEmpty() || !V.isEmpty()){
                    throw new IllegalArgumentException();
                }
                break;
            }

            HashSet<String> usedVertices = new HashSet<>();
            for(Node<String> endpoint : endpoints){
                if(!V.contains(endpoint.value())) continue;
                Node<String> copy = endpoint.copy();
                boolean skip = false;
                boolean UtoV = false;
                while(copy.parent() != null){
                    if(usedVertices.contains(copy.value())){
                        skip = true;
                        break;
                    }
                    copy = copy.parent();
                    UtoV = !UtoV;
                }
                if(usedVertices.contains(copy.value())) skip = true;
                if(skip) continue;
                U.remove(copy.value());
                V.remove(endpoint.value());
                while(endpoint.parent() != null){
                    String start = endpoint.value();
                    String next = endpoint.parent().value();
                    if(edges.containsKey(next)){
                        edges.get(next).remove(start);
                        matchedEdges.get(next).add(start);
                    }else{
                        matchedEdges.get(start).remove(next);
                        edges.get(start).add(next);
                    }
                    usedVertices.add(start);
                    endpoint = endpoint.parent();
                }
                usedVertices.add(endpoint.value());
            }
        }
        return matchedEdges;
    }

    private void runBFS(Set<String> U,
                        Set<String> V,
                        Map<String, List<String>> unmatched,
                        Map<String, List<String>> matched,
                        List<Node<String>> endpoints)
    {
        boolean UtoV = true;
        Queue<Node<String>> queue = new LinkedList<>();
        for(String u : U){
            queue.add(new Node<>(u));
        }
        while(!queue.isEmpty()){
            int queueSize = queue.size();
            for(int i = 0; i < queueSize; i++){
                Node<String> node = queue.poll();
                List<String> nextOptions = UtoV ? unmatched.get(node.value()) : getReverse(node.value(), matched);
                if(nextOptions == null) continue;
                for(String next : nextOptions){
                    Node<String> copy = node.copy();
                    boolean alreadyVisited = false;
                    while(copy != null){
                        if(copy.value().equals(next)){
                            alreadyVisited = true;
                            break;
                        }
                        copy = copy.parent();
                    }
                    if(alreadyVisited) continue;
                    Node<String> nextNode = new Node<>(node, next);
                    if(!UtoV || !V.contains(next)){
                        queue.add(nextNode);
                    }else{
                        endpoints.add(nextNode);
                    }
                }
            }
            if(!endpoints.isEmpty()) return;
            UtoV  = !UtoV;
        }
    }

    private List<String> getReverse(String s, Map<String, List<String>> map)
    {
        List<String> list = new ArrayList<>();
        for(String key : map.keySet()){
            if(map.get(key).contains(s)) list.add(key);
        }
        return list;
    }

    private List<String> selectPath(Set<String> used,
                                    List<String> U,
                                    List<List<String>> paths)
    {
        for(List<String> path : paths){
            if(!U.contains(path.getFirst())) continue;
            if(used.stream().anyMatch(path::contains)) continue;
            used.addAll(path);
            return path;
        }
        return new ArrayList<>();
    }
}
