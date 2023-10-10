package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible
final class Constraints {
   private Constraints() {
   }

   public static Collection constrainedCollection(Collection var0, Constraint var1) {
      return new Constraints.ConstrainedCollection(var0, var1);
   }

   public static Set constrainedSet(Set var0, Constraint var1) {
      return new Constraints.ConstrainedSet(var0, var1);
   }

   public static SortedSet constrainedSortedSet(SortedSet var0, Constraint var1) {
      return new Constraints.ConstrainedSortedSet(var0, var1);
   }

   public static List constrainedList(List var0, Constraint var1) {
      return (List)(var0 instanceof RandomAccess ? new Constraints.ConstrainedRandomAccessList(var0, var1) : new Constraints.ConstrainedList(var0, var1));
   }

   private static ListIterator constrainedListIterator(ListIterator var0, Constraint var1) {
      return new Constraints.ConstrainedListIterator(var0, var1);
   }

   static Collection constrainedTypePreservingCollection(Collection var0, Constraint var1) {
      if (var0 instanceof SortedSet) {
         return constrainedSortedSet((SortedSet)var0, var1);
      } else if (var0 instanceof Set) {
         return constrainedSet((Set)var0, var1);
      } else {
         return (Collection)(var0 instanceof List ? constrainedList((List)var0, var1) : constrainedCollection(var0, var1));
      }
   }

   private static Collection checkElements(Collection var0, Constraint var1) {
      ArrayList var2 = Lists.newArrayList((Iterable)var0);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         var1.checkElement(var4);
      }

      return var2;
   }

   static Collection access$000(Collection var0, Constraint var1) {
      return checkElements(var0, var1);
   }

   static ListIterator access$100(ListIterator var0, Constraint var1) {
      return constrainedListIterator(var0, var1);
   }

   static class ConstrainedListIterator extends ForwardingListIterator {
      private final ListIterator delegate;
      private final Constraint constraint;

      public ConstrainedListIterator(ListIterator var1, Constraint var2) {
         this.delegate = var1;
         this.constraint = var2;
      }

      protected ListIterator delegate() {
         return this.delegate;
      }

      public void add(Object var1) {
         this.constraint.checkElement(var1);
         this.delegate.add(var1);
      }

      public void set(Object var1) {
         this.constraint.checkElement(var1);
         this.delegate.set(var1);
      }

      protected Iterator delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   static class ConstrainedRandomAccessList extends Constraints.ConstrainedList implements RandomAccess {
      ConstrainedRandomAccessList(List var1, Constraint var2) {
         super(var1, var2);
      }
   }

   @GwtCompatible
   private static class ConstrainedList extends ForwardingList {
      final List delegate;
      final Constraint constraint;

      ConstrainedList(List var1, Constraint var2) {
         this.delegate = (List)Preconditions.checkNotNull(var1);
         this.constraint = (Constraint)Preconditions.checkNotNull(var2);
      }

      protected List delegate() {
         return this.delegate;
      }

      public boolean add(Object var1) {
         this.constraint.checkElement(var1);
         return this.delegate.add(var1);
      }

      public void add(int var1, Object var2) {
         this.constraint.checkElement(var2);
         this.delegate.add(var1, var2);
      }

      public boolean addAll(Collection var1) {
         return this.delegate.addAll(Constraints.access$000(var1, this.constraint));
      }

      public boolean addAll(int var1, Collection var2) {
         return this.delegate.addAll(var1, Constraints.access$000(var2, this.constraint));
      }

      public ListIterator listIterator() {
         return Constraints.access$100(this.delegate.listIterator(), this.constraint);
      }

      public ListIterator listIterator(int var1) {
         return Constraints.access$100(this.delegate.listIterator(var1), this.constraint);
      }

      public Object set(int var1, Object var2) {
         this.constraint.checkElement(var2);
         return this.delegate.set(var1, var2);
      }

      public List subList(int var1, int var2) {
         return Constraints.constrainedList(this.delegate.subList(var1, var2), this.constraint);
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class ConstrainedSortedSet extends ForwardingSortedSet {
      final SortedSet delegate;
      final Constraint constraint;

      ConstrainedSortedSet(SortedSet var1, Constraint var2) {
         this.delegate = (SortedSet)Preconditions.checkNotNull(var1);
         this.constraint = (Constraint)Preconditions.checkNotNull(var2);
      }

      protected SortedSet delegate() {
         return this.delegate;
      }

      public SortedSet headSet(Object var1) {
         return Constraints.constrainedSortedSet(this.delegate.headSet(var1), this.constraint);
      }

      public SortedSet subSet(Object var1, Object var2) {
         return Constraints.constrainedSortedSet(this.delegate.subSet(var1, var2), this.constraint);
      }

      public SortedSet tailSet(Object var1) {
         return Constraints.constrainedSortedSet(this.delegate.tailSet(var1), this.constraint);
      }

      public boolean add(Object var1) {
         this.constraint.checkElement(var1);
         return this.delegate.add(var1);
      }

      public boolean addAll(Collection var1) {
         return this.delegate.addAll(Constraints.access$000(var1, this.constraint));
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
   }

   static class ConstrainedSet extends ForwardingSet {
      private final Set delegate;
      private final Constraint constraint;

      public ConstrainedSet(Set var1, Constraint var2) {
         this.delegate = (Set)Preconditions.checkNotNull(var1);
         this.constraint = (Constraint)Preconditions.checkNotNull(var2);
      }

      protected Set delegate() {
         return this.delegate;
      }

      public boolean add(Object var1) {
         this.constraint.checkElement(var1);
         return this.delegate.add(var1);
      }

      public boolean addAll(Collection var1) {
         return this.delegate.addAll(Constraints.access$000(var1, this.constraint));
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   static class ConstrainedCollection extends ForwardingCollection {
      private final Collection delegate;
      private final Constraint constraint;

      public ConstrainedCollection(Collection var1, Constraint var2) {
         this.delegate = (Collection)Preconditions.checkNotNull(var1);
         this.constraint = (Constraint)Preconditions.checkNotNull(var2);
      }

      protected Collection delegate() {
         return this.delegate;
      }

      public boolean add(Object var1) {
         this.constraint.checkElement(var1);
         return this.delegate.add(var1);
      }

      public boolean addAll(Collection var1) {
         return this.delegate.addAll(Constraints.access$000(var1, this.constraint));
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
