package org.example;

import graph.IGraph;
import tree.Node;

import java.util.*;

public final class SecretSantaSelector {


    public SecretSantaSelector()
    {

    }

    /**
     * Creates a bipartite graph of the given input graph, setting both U and V to the set of vertices of the input
     * graph. Then runs Hopcroft-Karp's algorithm to solve.
     * Note that to discern the same vertices in U and V, a "2" is added for vertices in V.
     */
    public Map<String, List<String>> findMaximumMatching(IGraph<String> graph)
    {
        Set<String> U = graph.getVertices();
        Set<String> V = new HashSet<>(graph.vertexCount());
        Map<String, List<String>> edges = new HashMap<>(graph.vertexCount());
        for(String vertex : graph.getVertices()){
            V.add(vertex + "2");
            edges.put(vertex, new ArrayList<>());
            for(String s : graph.getSuccessors(vertex)){
                edges.get(vertex).add(s + "2");
            }
        }
        return runHopCroftKarp(U, V, edges);
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
    private Map<String, List<String>> runHopCroftKarp(Set<String> U,
                                                          Set<String> V,
                                                          Map<String, List<String>> unmatchedEdges)
    {
        Map<String, List<String>> matchedEdges = new HashMap<>();
        for(String key : unmatchedEdges.keySet()){
            matchedEdges.put(key, new ArrayList<>());
        }
        while(true){
            List<Node<String>> endpoints = runBFS(U, V, unmatchedEdges, matchedEdges);
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
                    if(unmatchedEdges.containsKey(next)){
                        unmatchedEdges.get(next).remove(start);
                        matchedEdges.get(next).add(start);
                    }else{
                        matchedEdges.get(start).remove(next);
                        unmatchedEdges.get(start).add(next);
                    }
                    usedVertices.add(start);
                    endpoint = endpoint.parent();
                }
                usedVertices.add(endpoint.value());
            }
        }
        return matchedEdges;
    }

    /**
     * Runs breadth-first search to find all possible paths from elements in U to elements in V and returns said
     * elements in V as tree nodes, whereby using .parent() will rebuild the found path from V -> U.
     * @param U The set of *yet* unmatched vertices in U. Remains unchanged.
     * @param V The set of *yet* unmatched vertices in V. Remains unchanged.
     * @param unmatched The set of unmatched edges (so from U -> V).
     * @param matched The set of matched edges (so from V -> U).
     *
     * @return The list of elements in V that were found to have a path to (from U), in tree Node form.
     */
    private List<Node<String>> runBFS(Set<String> U, Set<String> V,
                                      Map<String, List<String>> unmatched,
                                      Map<String, List<String>> matched)
    {
        List<Node<String>> endpoints = new ArrayList<>();
        boolean UtoV = true;
        Queue<Node<String>> queue = new LinkedList<>();
        for(String u : U){
            queue.add(new Node<>(u));
        }
        while(!queue.isEmpty()){
            int queueSize = queue.size();
            for(int i = 0; i < queueSize; i++){
                Node<String> current = queue.poll();
                List<String> nextList = UtoV
                        ? unmatched.get(current.value())
                        : getReverse(current.value(), matched);
                if(nextList == null) continue;
                for(String next : nextList){
                    if(isInPath(next, current.copy())) continue;
                    Node<String> nextNode = new Node<>(current, next);
                    boolean isUnmatchedInV = UtoV && V.contains(next);
                    if(isUnmatchedInV){
                        endpoints.add(nextNode);
                    }else{
                        queue.add(nextNode);
                    }
                }
            }
            if(!endpoints.isEmpty()) break;
            UtoV  = !UtoV;
        }
        return endpoints;
    }

    private boolean isInPath(String value, Node<String> node)
    {
        while(node != null){
            if(node.value().equals(value)) return true;
            node = node.parent();
        }
        return false;
    }

    private List<String> getReverse(String s, Map<String, List<String>> map)
    {
        List<String> list = new ArrayList<>();
        for(String key : map.keySet()){
            if(map.get(key).contains(s)) list.add(key);
        }
        return list;
    }
}
