package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

public abstract class ForwardingNavigableSet extends ForwardingSortedSet implements NavigableSet {
   protected ForwardingNavigableSet() {
   }

   protected abstract NavigableSet delegate();

   public Object lower(Object var1) {
      return this.delegate().lower(var1);
   }

   protected Object standardLower(Object var1) {
      return Iterators.getNext(this.headSet(var1, false).descendingIterator(), (Object)null);
   }

   public Object floor(Object var1) {
      return this.delegate().floor(var1);
   }

   protected Object standardFloor(Object var1) {
      return Iterators.getNext(this.headSet(var1, true).descendingIterator(), (Object)null);
   }

   public Object ceiling(Object var1) {
      return this.delegate().ceiling(var1);
   }

   protected Object standardCeiling(Object var1) {
      return Iterators.getNext(this.tailSet(var1, true).iterator(), (Object)null);
   }

   public Object higher(Object var1) {
      return this.delegate().higher(var1);
   }

   protected Object standardHigher(Object var1) {
      return Iterators.getNext(this.tailSet(var1, false).iterator(), (Object)null);
   }

   public Object pollFirst() {
      return this.delegate().pollFirst();
   }

   protected Object standardPollFirst() {
      return Iterators.pollNext(this.iterator());
   }

   public Object pollLast() {
      return this.delegate().pollLast();
   }

   protected Object standardPollLast() {
      return Iterators.pollNext(this.descendingIterator());
   }

   protected Object standardFirst() {
      return this.iterator().next();
   }

   protected Object standardLast() {
      return this.descendingIterator().next();
   }

   public NavigableSet descendingSet() {
      return this.delegate().descendingSet();
   }

   public Iterator descendingIterator() {
      return this.delegate().descendingIterator();
   }

   public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
      return this.delegate().subSet(var1, var2, var3, var4);
   }

   @Beta
   protected NavigableSet standardSubSet(Object var1, boolean var2, Object var3, boolean var4) {
      return this.tailSet(var1, var2).headSet(var3, var4);
   }

   protected SortedSet standardSubSet(Object var1, Object var2) {
      return this.subSet(var1, true, var2, false);
   }

   public NavigableSet headSet(Object var1, boolean var2) {
      return this.delegate().headSet(var1, var2);
   }

   protected SortedSet standardHeadSet(Object var1) {
      return this.headSet(var1, false);
   }

   public NavigableSet tailSet(Object var1, boolean var2) {
      return this.delegate().tailSet(var1, var2);
   }

   protected SortedSet standardTailSet(Object var1) {
      return this.tailSet(var1, true);
   }

   protected SortedSet delegate() {
      return this.delegate();
   }

   protected Set delegate() {
      return this.delegate();
   }

   protected Collection delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }

   @Beta
   protected class StandardDescendingSet extends Sets.DescendingSet {
      final ForwardingNavigableSet this$0;

      public StandardDescendingSet(ForwardingNavigableSet var1) {
         super(var1);
         this.this$0 = var1;
      }
   }
}
