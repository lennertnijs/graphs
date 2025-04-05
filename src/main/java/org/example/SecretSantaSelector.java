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
    public SecretSantaSolution findMaximumMatching(IGraph<String> graph)
    {
        Set<String> U = graph.getVertices();
        Set<String> V = new HashSet<>(graph.vertexCount());
        Map<String, List<String>> edges = new HashMap<>(graph.vertexCount());
        for(String vertex : graph.getVertices()){
            V.add(vertex + SecretSantaSolution.SEPARATOR);
            edges.put(vertex, new ArrayList<>());
            for(String s : graph.getSuccessors(vertex)){
                edges.get(vertex).add(s + SecretSantaSolution.SEPARATOR);
            }
        }
        for(String v : V){
            edges.put(v, new ArrayList<>());
        }
        Map<String, List<String>> solution = hopCroftKarp(U, V, edges);
        return new SecretSantaSolution(solution);
    }

    /**
     * Runs Hopcroft-Karp's algorithm, which is a 2-step algorithm for finding a maximal matching.
     * In step 1, using BFS, the algorithm finds unmatched points in V to match up to unmatched points in U.
     * This is possible thanks to the tree formed in this step.
     * *
     * In step 2, the algorithm will traverse back up these paths in opposite direction (so, V -> U) to finalize
     * the path. During the ENTIRE step (so, for all paths found in step 1), will it keep track of a set of used
     * vertices in the graph. No path is allowed to use any vertices from this set, so when a new path is selected
     * it locks all the nodes it traverses over. On top of that, all edges on the path are flipped directionally,
     * so edges from U->V now go from V->U and vice versa. Note that edges are also not allowed to be re-used for
     * the entire iteration, but that's already enforced by the vertex rule. After locking in a path, the start
     * and end point are removed from V and U respectively.
     * *
     * If at any point step 1 results in no new endpoints, the algorithm is done. This can both mean the graph
     * has been solved (a maximal matching has been found), or no such matching exists.
     * @param U The set of vertices in U. Is changed.
     * @param V The set of vertices in V. Is changed.
     * @param edges The map of edges between U and V. Is changed.
     *
     * @return The final map of edges.
     */
    private Map<String, List<String>> hopCroftKarp(Set<String> U, Set<String> V, Map<String, List<String>> edges)
    {
        while(true){
            List<Node<String>> endpoints = findEndpoints(U, V, edges);
            if(endpoints.isEmpty()) break;
            Set<String> usedVertices = new HashSet<>();
            for(Node<String> endpoint : endpoints){
                if(anyInPath(usedVertices, endpoint.copy())) continue;
                V.remove(endpoint.value());
                while(endpoint.parent() != null){
                    String start = endpoint.value();
                    String next = endpoint.parent().value();
                    edges.get(next).remove(start);
                    edges.get(start).add(next);
                    usedVertices.add(start);
                    endpoint = endpoint.parent();
                }
                usedVertices.add(endpoint.value());
                U.remove(endpoint.value());
            }
        }
        return edges;
    }

    /**
     * Runs breadth-first search to find all possible paths from elements in U to elements in V and returns said
     * elements in V as tree nodes, whereby using .parent() will rebuild the found path back up from V -> U.
     * Note that traversal over a path will always go back and forth between U/V, ping pong style.
     * @param U The set of *yet* unmatched vertices in U. Remains unchanged.
     * @param V The set of *yet* unmatched vertices in V. Remains unchanged.
     * @param edges The map of edges. Remains unchanged.
     *
     * @return The list of elements in V that were found to have a path to (from U), in tree Node form.
     */
    private List<Node<String>> findEndpoints(Set<String> U, Set<String> V, Map<String, List<String>> edges)
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
                for(String next : edges.get(current.value())){
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

    private boolean anyInPath(Set<String> values, Node<String> node)
    {
        while(node != null){
            if(values.contains(node.value())) return true;
            node = node.parent();
        }
        return false;
    }
}
