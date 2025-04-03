package dense_graph;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public interface IGraph<T> {

    /**
     * Adds a new vertex to the graph with the given object.
     * The vertex is initialized with no edges.
     * @param object The object. Cannot be null.
     *
     * @throws IllegalStateException If the object already was a vertex in the graph.
     * @throws NullPointerException If the object is null.
     */
    void addVertex(T object);

    /**
     * Adds a new vertex to graph for each of the objects.
     * Each vertex is initialized with no edges.
     * @param objects The list of objects. Cannot be null. Cannot contain null.
     *
     * @throws IllegalStateException If one of the objects already was a vertex in the graph.
     * @throws NoSuchElementException If the list of objects is null, or contains a null.
     */
    void addVertices(List<T> objects);

    /**
     * Adds a directed edge from the start object to the end object with a weight of 0.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     *
     * @throws NoSuchElementException If the start or end object is not a vertex in the graph.
     * @throws IllegalStateException If an edge between the two objects already existed.
     * @throws NullPointerException If the start object or end object is null.
     */
    void addEdge(T start, T end);

    /**
     * Adds a directed edge from the start object to end object with the given weight.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     * @param weight The weight. Cannot be negative.
     *
     * @throws NoSuchElementException If the start or end object is not a vertex in the graph.
     * @throws IllegalStateException If an edge between the two objects already existed.
     * @throws IllegalArgumentException If the weight is negative.
     * @throws NullPointerException If the start object or end object is null.
     */
    void addEdge(T start, T end, int weight);

    /**
     * Adds directed edges from the start object to all the end objects with a weight of 0.
     * @param start The start object. Cannot be null.
     * @param ends The list of end objects. Cannot be null. Cannot contain null.
     *
     * @throws NoSuchElementException If the start object or any of the end objects are not a vertex in the graph.
     * @throws IllegalStateException If an edge between any pair of objects already existed.
     * @throws NullPointerException If the start object, the list or any of the end objects are null.
     */
    void addEdges(T start, List<T> ends);

    /**
     * Adds directed edges from the start object to all the end objects with the respective weights.
     * @param start The start object. Cannot be null.
     * @param ends The list of end objects. Cannot be null. Cannot contain null.
     * @param weights The list of weights. Cannot be null. Cannot contain null. Cannot contain a negative value.
     *
     * @throws NoSuchElementException If the start object or any of the end objects are not a vertex in the graph.
     * @throws IllegalStateException If an edge between any pair of objects already existed. (weight does not matter)
     * @throws IllegalArgumentException If the list of weights is a different length as the list of end objects.
     * @throws IllegalArgumentException If any of the weights are negative.
     * @throws NullPointerException If the start object, the two lists, or any of the elements in the lists are null.
     */
    void addEdges(T start, List<T> ends, List<Integer> weights);

    /**
     * Adds two edges connecting start object with the end object both ways, with a weight of 0.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     *
     * @throws NoSuchElementException If the start object or end object are not a vertex in the graph.
     * @throws IllegalStateException If an edge between the two objects already existed.
     * @throws NullPointerException If the start object or end object is null.
     */
    void connect(T start, T end);

    /**
     * Adds two edges connecting start object with the end object both ways, with the given weight.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     * @param weight The weight. Cannot be negative.
     *
     * @throws NoSuchElementException If the start object or end object are not a vertex in the graph.
     * @throws IllegalStateException If an edge between the two objects already existed.
     * @throws IllegalArgumentException If the weight is negative.
     * @throws NullPointerException If the start or end object is null.
     */
    void connect(T start, T end, int weight);

    /**
     * Adds two edges connecting the start object with each of the end objects both ways, with a weight of 0.
     * @param start The start object. Cannot be null.
     * @param ends The list of end objects. Cannot be null. Cannot contain null.
     *
     * @throws NoSuchElementException If the start object or any of the end objects are not a vertex in the graph.
     * @throws IllegalStateException If any of the edges already existed.
     * @throws NullPointerException If the given start object, list, or any of the end objects are null.
     */
    void connectAll(T start, List<T> ends);

    /**
     * Adds two edges connecting the start object to all the end objects with the given weights.
     * @param start The start object. Cannot be null.
     * @param ends The list of end objects. Cannot be null. Cannot contain null.
     * @param weights The list of weights. Cannot be null. Cannot contain null. Cannot contain a negative value.
     *
     * @throws NoSuchElementException If the start object or any of the end objects are not a vertex in the graph.
     * @throws IllegalStateException If an edge between any pair of objects already existed.
     * @throws IllegalArgumentException If the list of weights is a different length as the list of end objects.
     * @throws IllegalArgumentException If any of the weights are negative.
     * @throws NullPointerException If the start object, the two lists, or any of the elements in the lists are null.
     */
    void connectAll(T start, List<T> ends, List<Integer> weights);

    /**
     * Removes the given object from the graph. Also removes all outgoing, and incoming edges.
     * @param object The object. Cannot be null.
     *
     * @throws NoSuchElementException If the object is not a vertex in the graph.
     */
    void removeVertex(T object);

    /**
     * Removes the edge from the start object to the end object.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     *
     * @throws NoSuchElementException If the start or end object is not a vertex in the graph.
     */
    void removeEdge(T start, T end);
    /**
     * Removes any edges connecting the two objects with each other.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     *
     * @throws NoSuchElementException If the start object or end object are not a vertex in the graph.
     */
    void disconnect(T start, T end);

    /**
     * Fetches and returns all the objects that are currently vertices in the graph.
     *
     * @return All vertex' objects.
     */
    Set<T> getVertices();

    /**
     * Fetches and returns all the objects that are directly linked by an edge with the given object.
     * @param object The object. Cannot be null.
     *
     * @return The neighboring objects.
     * @throws NoSuchElementException If the object is not a vertex in the graph.
     */
    List<T> getSuccessors(T object);
    List<T> getPredecessors(T object);

    /**
     * Fetches and returns the weight of the edge from the start object to the end object.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     *
     * @return The weight.
     * @throws NoSuchElementException If the start or end object are not vertices in the graph.
     * @throws NoSuchElementException If no edge exists from the start to the end object.
     */
    int getWeight(T start, T end);

    /**
     * Returns the amount of objects the given object is connected to through an edge.
     * @param object The object. Cannot be null.
     *
     * @return The amount of neighbors.
     */
    int getDegree(T object);

    /**
     * Checks whether the given object is a vertex in the graph.
     * @param object The object. Cannot be null.
     *
     * @return True if present. False otherwise.
     */
    boolean hasVertex(T object);

    /**
     * Checks whether an edge exists from the start object to the end object.
     * @param start The start object. Cannot be null.
     * @param end The end object. Cannot be null.
     *
     * @return True if present. False otherwise.
     */
    boolean hasEdge(T start, T end);

    /**
     * Checks whether the graph is empty, meaning it has no vertices.
     *
     * @return True if empty. False otherwise.
     */
    boolean isEmpty();

    /**
     * @return The amount of vertices.
     */
    int vertexCount();

    /**
     * @return The amount of edges.
     */
    int edgeCount();

    /**
     * Clears the entire graph.
     */
    void clear();

    /**
     * Copies the graph. The objects in the vertices will remain the same. (Thus, they have to be immutable).
     */
    graph.IGraph<T> copy();
}
