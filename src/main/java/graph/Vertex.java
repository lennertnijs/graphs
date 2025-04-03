package graph;

import java.util.Objects;

/**
 * Represents a vertex for a graph.
 * Holds an object.
 */
public final class Vertex<T> {

    /**
     * The value. Cannot be null.
     */
    private final T object;

    /**
     * Creates a new immutable {@link Vertex}.
     * Note that it is ONLY immutable if the stored object is also immutable.
     * @param object The object. Cannot be null.
     */
    public Vertex(T object){
        Objects.requireNonNull(object, "Vertex cannot store null object.");
        this.object = object;
    }

    /**
     * @return The object stored in this {@link Vertex}.
     */
    public T getValue(){
        return object;
    }

    /**
     * Compares this {@link Vertex} to the given object and returns true if equal. False otherwise.
     * They're considered equal, if they hold the same object.
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof Vertex<?> vertex))
            return false;
        return object.equals(vertex.object);
    }

    /**
     * @return The hash code of this {@link Vertex}, which is the hash code of the object it holds.
     */
    @Override
    public int hashCode(){
        return object.hashCode();
    }

    /**
     * @return The string representation of this {@link Vertex}.
     */
    @Override
    public String toString(){
        return String.format("Vertex[object=%s]", object);
    }
}
