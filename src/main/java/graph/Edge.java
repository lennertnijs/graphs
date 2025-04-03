package graph;

import java.util.Objects;

/**
 * Represents an edge in a graph.
 * An edge connects two vertices in the graph, and binds a weight to this connection.
 */
public final class Edge<T> {

    private final Vertex<T> start;
    private final Vertex<T> end;
    private final int weight;

    /**
     * Creates a new immutable {@link Edge}.
     * The edge is ONLY immutable if the objects being stored in the vertices are immutable.
     * @param start The start object's {@link Vertex}. Cannot be null.
     * @param end The end object's {@link Vertex}. Cannot be null.
     * @param weight The weight. Cannot be negative.
     */
    public Edge(Vertex<T> start, Vertex<T> end, int weight){
        Objects.requireNonNull(start, "Start Vertex is null.");
        Objects.requireNonNull(end, "End vertex is null.");
        if(weight < 0)
            throw new IllegalArgumentException("Weight is negative.");
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    /**
     * @return The start object's {@link Vertex}.
     */
    public Vertex<T> getStart(){
        return start;
    }

    /**
     * @return The end object's {@link Vertex}.
     */
    public Vertex<T> getEnd(){
        return end;
    }

    /**
     * @return The weight of this {@link Edge}.
     */
    public int getWeight(){
        return weight;
    }

    /**
     * Checks whether this {@link Edge} and the given {@link Edge} connect the same vertices.
     * @param edge The other {@link Edge}. Cannot be null.
     *
     * @return True if they connect the same vertices. False otherwise.
     */
    public boolean hasSameVertices(Edge<T> edge){
        Objects.requireNonNull(edge, "Edge is null.");
        return start.equals(edge.start) && end.equals(edge.end);
    }

    /**
     * Compares this {@link Edge} to the given object and returns true if equal. False otherwise.
     * They're considered equal, if connect the same vertices with the same weight.
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof Edge<?>))
            return false;
        Edge<?> edge = (Edge<?>) other;
        return start.equals(edge.start) && end.equals(edge.end) && weight == edge.weight;
    }

    /**
     * @return The hash code of this {@link Edge}.
     */
    @Override
    public int hashCode(){
        int result = start.hashCode();
        result = result * 31 + end.hashCode();
        result = result * 31 + weight;
        return result;
    }

    /**
     * @return The string representation of this edge.
     */
    @Override
    public String toString(){
        return String.format("Edge[start=%s, end=%s, weight=%d]", start, end, weight);
    }
}
