package javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class ModifiableObservableListBase extends ObservableListBase {
   public boolean setAll(Collection var1) {
      this.beginChange();

      try {
         this.clear();
         this.addAll(var1);
      } finally {
         this.endChange();
      }

      return true;
   }

   public boolean addAll(Collection var1) {
      this.beginChange();

      boolean var3;
      try {
         boolean var2 = super.addAll(var1);
         var3 = var2;
      } finally {
         this.endChange();
      }

      return var3;
   }

   public boolean addAll(int var1, Collection var2) {
      this.beginChange();

      boolean var4;
      try {
         boolean var3 = super.addAll(var1, var2);
         var4 = var3;
      } finally {
         this.endChange();
      }

      return var4;
   }

   protected void removeRange(int var1, int var2) {
      this.beginChange();

      try {
         super.removeRange(var1, var2);
      } finally {
         this.endChange();
      }

   }

   public boolean removeAll(Collection var1) {
      this.beginChange();

      boolean var3;
      try {
         boolean var2 = super.removeAll(var1);
         var3 = var2;
      } finally {
         this.endChange();
      }

      return var3;
   }

   public boolean retainAll(Collection var1) {
      this.beginChange();

      boolean var3;
      try {
         boolean var2 = super.retainAll(var1);
         var3 = var2;
      } finally {
         this.endChange();
      }

      return var3;
   }

   public void add(int var1, Object var2) {
      this.doAdd(var1, var2);
      this.beginChange();
      this.nextAdd(var1, var1 + 1);
      ++this.modCount;
      this.endChange();
   }

   public Object set(int var1, Object var2) {
      Object var3 = this.doSet(var1, var2);
      this.beginChange();
      this.nextSet(var1, var3);
      this.endChange();
      return var3;
   }

   public boolean remove(Object var1) {
      int var2 = this.indexOf(var1);
      if (var2 != -1) {
         this.remove(var2);
         return true;
      } else {
         return false;
      }
   }

   public Object remove(int var1) {
      Object var2 = this.doRemove(var1);
      this.beginChange();
      this.nextRemove(var1, var2);
      ++this.modCount;
      this.endChange();
      return var2;
   }

   public List subList(int var1, int var2) {
      return new SubObservableList(super.subList(var1, var2));
   }

   public abstract Object get(int var1);

   public abstract int size();

   protected abstract void doAdd(int var1, Object var2);

   protected abstract Object doSet(int var1, Object var2);

   protected abstract Object doRemove(int var1);

   private class SubObservableList implements List {
      private List sublist;

      public SubObservableList(List var2) {
         this.sublist = var2;
      }

      public int size() {
         return this.sublist.size();
      }

      public boolean isEmpty() {
         return this.sublist.isEmpty();
      }

      public boolean contains(Object var1) {
         return this.sublist.contains(var1);
      }

      public Iterator iterator() {
         return this.sublist.iterator();
      }

      public Object[] toArray() {
         return this.sublist.toArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.sublist.toArray(var1);
      }

      public boolean add(Object var1) {
         return this.sublist.add(var1);
      }

      public boolean remove(Object var1) {
         return this.sublist.remove(var1);
      }

      public boolean containsAll(Collection var1) {
         return this.sublist.containsAll(var1);
      }

      public boolean addAll(Collection var1) {
         ModifiableObservableListBase.this.beginChange();

         boolean var3;
         try {
            boolean var2 = this.sublist.addAll(var1);
            var3 = var2;
         } finally {
            ModifiableObservableListBase.this.endChange();
         }

         return var3;
      }

      public boolean addAll(int var1, Collection var2) {
         ModifiableObservableListBase.this.beginChange();

         boolean var4;
         try {
            boolean var3 = this.sublist.addAll(var1, var2);
            var4 = var3;
         } finally {
            ModifiableObservableListBase.this.endChange();
         }

         return var4;
      }

      public boolean removeAll(Collection var1) {
         ModifiableObservableListBase.this.beginChange();

         boolean var3;
         try {
            boolean var2 = this.sublist.removeAll(var1);
            var3 = var2;
         } finally {
            ModifiableObservableListBase.this.endChange();
         }

         return var3;
      }

      public boolean retainAll(Collection var1) {
         ModifiableObservableListBase.this.beginChange();

         boolean var3;
         try {
            boolean var2 = this.sublist.retainAll(var1);
            var3 = var2;
         } finally {
            ModifiableObservableListBase.this.endChange();
         }

         return var3;
      }

      public void clear() {
         ModifiableObservableListBase.this.beginChange();

         try {
            this.sublist.clear();
         } finally {
            ModifiableObservableListBase.this.endChange();
         }

      }

      public Object get(int var1) {
         return this.sublist.get(var1);
      }

      public Object set(int var1, Object var2) {
         return this.sublist.set(var1, var2);
      }

      public void add(int var1, Object var2) {
         this.sublist.add(var1, var2);
      }

      public Object remove(int var1) {
         return this.sublist.remove(var1);
      }

      public int indexOf(Object var1) {
         return this.sublist.indexOf(var1);
      }

      public int lastIndexOf(Object var1) {
         return this.sublist.lastIndexOf(var1);
      }

      public ListIterator listIterator() {
         return this.sublist.listIterator();
      }

      public ListIterator listIterator(int var1) {
         return this.sublist.listIterator(var1);
      }

      public List subList(int var1, int var2) {
         return ModifiableObservableListBase.this.new SubObservableList(this.sublist.subList(var1, var2));
      }

      public boolean equals(Object var1) {
         return this.sublist.equals(var1);
      }

      public int hashCode() {
         return this.sublist.hashCode();
      }

      public String toString() {
         return this.sublist.toString();
      }
   }
}
