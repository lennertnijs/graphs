package tree;

public final class Node<T>
{
    private final Node<T> parent;
    private final T value;

    public Node(Node<T> parent, T value)
    {
        this.parent = parent;
        this.value = value;
    }

    public Node(T value)
    {
        this.parent = null;
        this.value = value;
    }

    public T value()
    {
        return value;
    }

    public Node<T> parent()
    {
        return parent;
    }

    public Node<T> copy()
    {
        return new Node<>(parent, value);
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Node<?> node))
            return false;
        return value.equals(node.value);
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }
}
