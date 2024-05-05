package net.optifine.util;

import net.optifine.util.LinkedList;

public static class LinkedList.Node<T> {
    private final T item;
    private LinkedList.Node<T> prev;
    private LinkedList.Node<T> next;
    private LinkedList<T> parent;

    public LinkedList.Node(T item) {
        this.item = item;
    }

    public T getItem() {
        return this.item;
    }

    public LinkedList.Node<T> getPrev() {
        return this.prev;
    }

    public LinkedList.Node<T> getNext() {
        return this.next;
    }

    private void setPrev(LinkedList.Node<T> prev) {
        this.prev = prev;
    }

    private void setNext(LinkedList.Node<T> next) {
        this.next = next;
    }

    private void setParent(LinkedList<T> parent) {
        this.parent = parent;
    }

    public String toString() {
        return "" + this.item;
    }

    static /* synthetic */ void access$000(LinkedList.Node x0, LinkedList.Node x1) {
        x0.setNext(x1);
    }

    static /* synthetic */ void access$100(LinkedList.Node x0, LinkedList.Node x1) {
        x0.setPrev(x1);
    }

    static /* synthetic */ void access$200(LinkedList.Node x0, LinkedList x1) {
        x0.setParent(x1);
    }

    static /* synthetic */ LinkedList access$300(LinkedList.Node x0) {
        return x0.parent;
    }

    static /* synthetic */ LinkedList.Node access$400(LinkedList.Node x0) {
        return x0.next;
    }
}
