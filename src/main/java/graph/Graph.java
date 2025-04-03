package graph;

import java.util.*;
import java.util.stream.Collectors;

public final class Graph<T> implements IGraph<T> {

    private Map<Vertex<T>, Set<Edge<T>>> adjacencyMap;

    public Graph(){
        this.adjacencyMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVertex(T object){
        Vertex<T> vertex = new Vertex<>(object);
        if(adjacencyMap.containsKey(vertex))
            throw new IllegalStateException("The Vertex already exists in the Navigation.");
        adjacencyMap.put(vertex, new HashSet<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVertices(List<T> objects){
        Objects.requireNonNull(objects, "List is null.");
        for(T object : objects)
            addVertex(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(T start, T end){
        createAndStoreEdge(start, end, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdge(T start, T end, int weight) {
        createAndStoreEdge(start, end, weight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdges(T start, List<T> ends){
        Objects.requireNonNull(ends, "List is null.");
        for(T end : ends)
            createAndStoreEdge(start, end, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdges(T start, List<T> ends, List<Integer> weights) {
        Objects.requireNonNull(ends, "List is null.");
        Objects.requireNonNull(weights, "List is null.");
        if(weights.size() != ends.size())
            throw new IllegalArgumentException("The List of weights is not the same length as the list of end objects.");
        if(weights.contains(null))
            throw new NullPointerException("A weight is null.");
        for(int i = 0; i < ends.size(); i++)
            createAndStoreEdge(start, ends.get(i), weights.get(i));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(T start, T end) {
        createAndStoreEdge(start, end, 0);
        createAndStoreEdge(end, start, 0);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(T start, T end, int weight) {
        createAndStoreEdge(start, end, weight);
        createAndStoreEdge(end, start, weight);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void connectAll(T start, List<T> ends) {
        Objects.requireNonNull(ends, "List is null.");
        for(T end : ends) {
            createAndStoreEdge(start, end, 0);
            createAndStoreEdge(end, start, 0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connectAll(T start, List<T> ends, List<Integer> weights) {
        Objects.requireNonNull(ends, "List is null.");
        Objects.requireNonNull(weights, "List is null.");
        if(weights.size() != ends.size())
            throw new IllegalArgumentException("The List of weights is not the same length as the list of end objects.");
        if(weights.contains(null))
            throw new NullPointerException("A weight is null.");
        for(int i = 0; i < ends.size(); i++){
            int weight = weights.get(i);
            createAndStoreEdge(start, ends.get(i), weight);
            createAndStoreEdge(ends.get(i), start, weight);
        }
    }

    /**
     * Creates and stores a directed edge starting at the given object and ending in the other, with the given weight.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     * @param weight The weight. Cannot be negative.
     *
     * @throws IllegalStateException If an edge already existed between the two objects.
     * @throws IllegalArgumentException If the weight is negative.
     * @throws NoSuchElementException If the start or end object is not a vertex in the graph.
     */
    private void createAndStoreEdge(T start, T end, int weight){
        Vertex<T> startVertex = new Vertex<>(start);
        Vertex<T> endVertex = new Vertex<>(end);
        if(!adjacencyMap.containsKey(startVertex))
            throw new NoSuchElementException("Start Vertex is not part of the Navigation.");
        if(!adjacencyMap.containsKey(endVertex)){
            System.out.println(adjacencyMap.keySet());
            System.out.println(endVertex);
            throw new NoSuchElementException("End Vertex is not part of the Navigation.");
        }
        Edge<T> edge = new Edge<>(startVertex, endVertex, weight);
        if(adjacencyMap.get(startVertex).stream().anyMatch(e -> e.hasSameVertices(edge)))
            throw new IllegalStateException("Edge already exists in the Navigation.");
        adjacencyMap.get(startVertex).add(edge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVertex(T object){
        adjacencyMap.remove(new Vertex<>(object));
        for(Set<Edge<T>> edgeList : adjacencyMap.values()){
            edgeList.removeIf(edge -> edge.getEnd().equals(object));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdge(T start, T end){
        Vertex<T> startVertex = new Vertex<>(start);
        Vertex<T> endVertex = new Vertex<>(end);
        Edge<T> dummyEdge = new Edge<>(startVertex, endVertex, 0);
        if(!adjacencyMap.containsKey(startVertex))
            return;
        adjacencyMap.get(startVertex).removeIf(edge -> edge.hasSameVertices(dummyEdge));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(T start, T end) {
        removeEdge(start, end);
        removeEdge(end, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<T> getVertices(){
        return adjacencyMap.keySet().stream().map(Vertex::getValue).collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> getSuccessors(T object) {
        Vertex<T> vertex = new Vertex<>(object);
        if(!adjacencyMap.containsKey(vertex))
            throw new NoSuchElementException("The object is not a vertex in the Navigation.");
        return adjacencyMap.get(vertex).stream()
                .map(Edge::getEnd)
                .map(Vertex::getValue)
                .toList();
    }

    @Override
    public List<T> getPredecessors(T object){
        Vertex<T> vertex = new Vertex<>(object);
        if(!adjacencyMap.containsKey(vertex))
            throw new NoSuchElementException("The object is not a vertex in the Navigation.");
        List<T> pred = new ArrayList<>();
        for(Vertex<T> key : adjacencyMap.keySet()){
            Edge<T> edge = new Edge<>(key, vertex, 0);
            if(adjacencyMap.get(key).contains(edge)){
                pred.add(key.getValue());
            }
        }
        return pred;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWeight(T start, T end){
        Vertex<T> startVertex = new Vertex<>(start);
        Vertex<T> endVertex = new Vertex<>(end);
        if(!adjacencyMap.containsKey(startVertex))
            throw new NoSuchElementException("Start object is not a vertex in the Navigation.");
        Edge<T> dummyEdge = new Edge<>(startVertex, endVertex, 0);
        for(Edge<T> edge : adjacencyMap.get(startVertex))
            if(edge.hasSameVertices(dummyEdge))
                return edge.getWeight();
        throw new NoSuchElementException("No edge between the two objects was found.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDegree(T object){
        Vertex<T> vertex = new Vertex<>(object);
        if(!adjacencyMap.containsKey(vertex))
            throw new NoSuchElementException("Object is not a Vertex in the Navigation.");
        return adjacencyMap.get(vertex).size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasVertex(T object){
        return adjacencyMap.containsKey(new Vertex<>(object));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasEdge(T start, T end){
        Vertex<T> startVertex = new Vertex<>(start);
        Vertex<T> endVertex = new Vertex<>(end);
        if(!adjacencyMap.containsKey(startVertex))
            throw new NoSuchElementException("Start Vertex not found.");
        if(!adjacencyMap.containsKey(endVertex))
            throw new NoSuchElementException("End Vertex not found.");
        Edge<T> firstEdge = new Edge<>(startVertex, endVertex, 0);
        for(Edge<T> edge : adjacencyMap.get(startVertex)){
            if(firstEdge.hasSameVertices(edge))
                return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty(){
        return adjacencyMap.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int vertexCount(){
        return adjacencyMap.keySet().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int edgeCount(){
        int edgeCount = 0;
        for(Vertex<T> vertex : adjacencyMap.keySet()){
            edgeCount += adjacencyMap.get(vertex).size();
        }
        return edgeCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear(){
        adjacencyMap = new HashMap<>();
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Graph<?>))
            return false;
        Graph<?> graph = (Graph<?>) other;
        return adjacencyMap.equals(graph.adjacencyMap);
    }

    @Override
    public int hashCode(){
        return adjacencyMap.hashCode();
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Navigation[");
        for(Vertex<T> vertex : adjacencyMap.keySet()){
            builder.append(String.format("Vertex[%s] -> {", vertex.getValue()));
            for(Edge<T> edge : adjacencyMap.get(vertex))
                builder.append(String.format("Vertex[%s]", edge.getEnd().getValue()));
            builder.append("}, ");
        }
        if(!adjacencyMap.isEmpty())
            builder.delete(builder.length() - 2, builder.length());
        builder.append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Graph<T> copy(){
        Graph<T> copy = new Graph<>();
        for(Vertex<T> vertex : this.adjacencyMap.keySet())
            copy.adjacencyMap.put(vertex, new HashSet<>(this.adjacencyMap.get(vertex)));
        return copy;
    }
}
