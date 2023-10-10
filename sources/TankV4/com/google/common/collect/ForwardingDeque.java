package com.google.common.collect;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

public abstract class ForwardingDeque extends ForwardingQueue implements Deque {
   protected ForwardingDeque() {
   }

   protected abstract Deque delegate();

   public void addFirst(Object var1) {
      this.delegate().addFirst(var1);
   }

   public void addLast(Object var1) {
      this.delegate().addLast(var1);
   }

   public Iterator descendingIterator() {
      return this.delegate().descendingIterator();
   }

   public Object getFirst() {
      return this.delegate().getFirst();
   }

   public Object getLast() {
      return this.delegate().getLast();
   }

   public boolean offerFirst(Object var1) {
      return this.delegate().offerFirst(var1);
   }

   public boolean offerLast(Object var1) {
      return this.delegate().offerLast(var1);
   }

   public Object peekFirst() {
      return this.delegate().peekFirst();
   }

   public Object peekLast() {
      return this.delegate().peekLast();
   }

   public Object pollFirst() {
      return this.delegate().pollFirst();
   }

   public Object pollLast() {
      return this.delegate().pollLast();
   }

   public Object pop() {
      return this.delegate().pop();
   }

   public void push(Object var1) {
      this.delegate().push(var1);
   }

   public Object removeFirst() {
      return this.delegate().removeFirst();
   }

   public Object removeLast() {
      return this.delegate().removeLast();
   }

   public boolean removeFirstOccurrence(Object var1) {
      return this.delegate().removeFirstOccurrence(var1);
   }

   public boolean removeLastOccurrence(Object var1) {
      return this.delegate().removeLastOccurrence(var1);
   }

   protected Queue delegate() {
      return this.delegate();
   }

   protected Collection delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
