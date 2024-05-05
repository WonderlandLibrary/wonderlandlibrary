package net.optifine.util;

import java.util.Iterator;
import net.optifine.util.LinkedList;

/*
 * Exception performing whole class analysis ignored.
 */
class LinkedList.1
implements Iterator<LinkedList.Node<T>> {
    LinkedList.Node<T> node;

    LinkedList.1() {
        this.node = LinkedList.this.getFirst();
    }

    public boolean hasNext() {
        return this.node != null;
    }

    public LinkedList.Node<T> next() {
        LinkedList.Node node = this.node;
        if (this.node != null) {
            this.node = LinkedList.Node.access$400(this.node);
        }
        return node;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
