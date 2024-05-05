package net.optifine.util;

import java.util.Iterator;
import net.optifine.util.LinkedList;

/*
 * Exception performing whole class analysis ignored.
 */
public class LinkedList<T> {
    private Node<T> first;
    private Node<T> last;
    private int size;

    public void addFirst(Node<T> cNode) {
        this.checkNoParent(cNode);
        if (this.isEmpty()) {
            this.first = cNode;
            this.last = cNode;
        } else {
            Node<T> node = this.first;
            Node.access$000(cNode, node);
            Node.access$100(node, cNode);
            this.first = cNode;
        }
        Node.access$200(cNode, (LinkedList)this);
        ++this.size;
    }

    public void addLast(Node<T> cNode) {
        this.checkNoParent(cNode);
        if (this.isEmpty()) {
            this.first = cNode;
            this.last = cNode;
        } else {
            Node<T> node = this.last;
            Node.access$100(cNode, node);
            Node.access$000(node, cNode);
            this.last = cNode;
        }
        Node.access$200(cNode, (LinkedList)this);
        ++this.size;
    }

    public void addAfter(Node<T> nodePrev, Node<T> cNode) {
        if (nodePrev == null) {
            this.addFirst(cNode);
        } else if (nodePrev == this.last) {
            this.addLast(cNode);
        } else {
            this.checkParent(nodePrev);
            this.checkNoParent(cNode);
            Node nodeNext = nodePrev.getNext();
            Node.access$000(nodePrev, cNode);
            Node.access$100(cNode, nodePrev);
            Node.access$100((Node)nodeNext, cNode);
            Node.access$000(cNode, (Node)nodeNext);
            Node.access$200(cNode, (LinkedList)this);
            ++this.size;
        }
    }

    public Node<T> remove(Node<T> cNode) {
        this.checkParent(cNode);
        Node prev = cNode.getPrev();
        Node next = cNode.getNext();
        if (prev != null) {
            Node.access$000((Node)prev, (Node)next);
        } else {
            this.first = next;
        }
        if (next != null) {
            Node.access$100((Node)next, (Node)prev);
        } else {
            this.last = prev;
        }
        Node.access$100(cNode, null);
        Node.access$000(cNode, null);
        Node.access$200(cNode, null);
        --this.size;
        return cNode;
    }

    public void moveAfter(Node<T> nodePrev, Node<T> node) {
        this.remove(node);
        this.addAfter(nodePrev, node);
    }

    public boolean find(Node<T> nodeFind, Node<T> nodeFrom, Node<T> nodeTo) {
        Node node;
        this.checkParent(nodeFrom);
        if (nodeTo != null) {
            this.checkParent(nodeTo);
        }
        for (node = nodeFrom; node != null && node != nodeTo; node = node.getNext()) {
            if (node != nodeFind) continue;
            return true;
        }
        if (node != nodeTo) {
            throw new IllegalArgumentException("Sublist is not linked, from: " + nodeFrom + ", to: " + nodeTo);
        }
        return false;
    }

    private void checkParent(Node<T> node) {
        if (Node.access$300(node) != this) {
            throw new IllegalArgumentException("Node has different parent, node: " + node + ", parent: " + Node.access$300(node) + ", this: " + this);
        }
    }

    private void checkNoParent(Node<T> node) {
        if (Node.access$300(node) != null) {
            throw new IllegalArgumentException("Node has different parent, node: " + node + ", parent: " + Node.access$300(node) + ", this: " + this);
        }
    }

    public boolean contains(Node<T> node) {
        return Node.access$300(node) == this;
    }

    public Iterator<Node<T>> iterator() {
        1 iterator = new /* Unavailable Anonymous Inner Class!! */;
        return iterator;
    }

    public Node<T> getFirst() {
        return this.first;
    }

    public Node<T> getLast() {
        return this.last;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size <= 0;
    }

    public String toString() {
        StringBuffer stringbuffer = new StringBuffer();
        Iterator<Node<T>> it = this.iterator();
        while (it.hasNext()) {
            Node node = (Node)it.next();
            if (stringbuffer.length() > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append(node.getItem());
        }
        return "" + this.size + " [" + stringbuffer.toString() + "]";
    }
}
