package com.sun.javafx.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class VetoableListDecorator implements ObservableList {
   private final ObservableList list;
   private int modCount;
   private ListListenerHelper helper;

   protected abstract void onProposedChange(List var1, int... var2);

   public VetoableListDecorator(ObservableList var1) {
      this.list = var1;
      this.list.addListener((var1x) -> {
         ListListenerHelper.fireValueChangedEvent(this.helper, new SourceAdapterChange(this, var1x));
      });
   }

   public void addListener(ListChangeListener var1) {
      this.helper = ListListenerHelper.addListener(this.helper, var1);
   }

   public void removeListener(ListChangeListener var1) {
      this.helper = ListListenerHelper.removeListener(this.helper, var1);
   }

   public void addListener(InvalidationListener var1) {
      this.helper = ListListenerHelper.addListener(this.helper, var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = ListListenerHelper.removeListener(this.helper, var1);
   }

   public boolean addAll(Object... var1) {
      return this.addAll((Collection)Arrays.asList(var1));
   }

   public boolean setAll(Object... var1) {
      return this.setAll((Collection)Arrays.asList(var1));
   }

   public boolean setAll(Collection var1) {
      this.onProposedChange(Collections.unmodifiableList(new ArrayList(var1)), 0, this.size());

      try {
         ++this.modCount;
         this.list.setAll(var1);
         return true;
      } catch (Exception var3) {
         --this.modCount;
         throw var3;
      }
   }

   private void removeFromList(List var1, int var2, Collection var3, boolean var4) {
      int[] var5 = new int[2];
      int var6 = -1;

      for(int var7 = 0; var7 < var1.size(); ++var7) {
         Object var8 = var1.get(var7);
         if (var3.contains(var8) ^ var4) {
            if (var6 == -1) {
               var5[var6 + 1] = var2 + var7;
               var5[var6 + 2] = var2 + var7 + 1;
               var6 += 2;
            } else if (var5[var6 - 1] == var2 + var7) {
               var5[var6 - 1] = var2 + var7 + 1;
            } else {
               int[] var9 = new int[var5.length + 2];
               System.arraycopy(var5, 0, var9, 0, var5.length);
               var5 = var9;
               var9[var6 + 1] = var2 + var7;
               var9[var6 + 2] = var2 + var7 + 1;
               var6 += 2;
            }
         }
      }

      if (var6 != -1) {
         this.onProposedChange(Collections.emptyList(), var5);
      }

   }

   public boolean removeAll(Object... var1) {
      return this.removeAll((Collection)Arrays.asList(var1));
   }

   public boolean retainAll(Object... var1) {
      return this.retainAll((Collection)Arrays.asList(var1));
   }

   public void remove(int var1, int var2) {
      this.onProposedChange(Collections.emptyList(), var1, var2);

      try {
         ++this.modCount;
         this.list.remove(var1, var2);
      } catch (Exception var4) {
         --this.modCount;
      }

   }

   public int size() {
      return this.list.size();
   }

   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   public boolean contains(Object var1) {
      return this.list.contains(var1);
   }

   public Iterator iterator() {
      return new VetoableIteratorDecorator(new ModCountAccessorImpl(), this.list.iterator(), 0);
   }

   public Object[] toArray() {
      return this.list.toArray();
   }

   public Object[] toArray(Object[] var1) {
      return this.list.toArray(var1);
   }

   public boolean add(Object var1) {
      this.onProposedChange(Collections.singletonList(var1), this.size(), this.size());

      try {
         ++this.modCount;
         this.list.add(var1);
         return true;
      } catch (Exception var3) {
         --this.modCount;
         throw var3;
      }
   }

   public boolean remove(Object var1) {
      int var2 = this.list.indexOf(var1);
      if (var2 != -1) {
         this.remove(var2);
         return true;
      } else {
         return false;
      }
   }

   public boolean containsAll(Collection var1) {
      return this.list.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      this.onProposedChange(Collections.unmodifiableList(new ArrayList(var1)), this.size(), this.size());

      try {
         ++this.modCount;
         boolean var2 = this.list.addAll(var1);
         if (!var2) {
            --this.modCount;
         }

         return var2;
      } catch (Exception var3) {
         --this.modCount;
         throw var3;
      }
   }

   public boolean addAll(int var1, Collection var2) {
      this.onProposedChange(Collections.unmodifiableList(new ArrayList(var2)), var1, var1);

      try {
         ++this.modCount;
         boolean var3 = this.list.addAll(var1, var2);
         if (!var3) {
            --this.modCount;
         }

         return var3;
      } catch (Exception var4) {
         --this.modCount;
         throw var4;
      }
   }

   public boolean removeAll(Collection var1) {
      this.removeFromList(this, 0, var1, false);

      try {
         ++this.modCount;
         boolean var2 = this.list.removeAll(var1);
         if (!var2) {
            --this.modCount;
         }

         return var2;
      } catch (Exception var3) {
         --this.modCount;
         throw var3;
      }
   }

   public boolean retainAll(Collection var1) {
      this.removeFromList(this, 0, var1, true);

      try {
         ++this.modCount;
         boolean var2 = this.list.retainAll(var1);
         if (!var2) {
            --this.modCount;
         }

         return var2;
      } catch (Exception var3) {
         --this.modCount;
         throw var3;
      }
   }

   public void clear() {
      this.onProposedChange(Collections.emptyList(), 0, this.size());

      try {
         ++this.modCount;
         this.list.clear();
      } catch (Exception var2) {
         --this.modCount;
         throw var2;
      }
   }

   public Object get(int var1) {
      return this.list.get(var1);
   }

   public Object set(int var1, Object var2) {
      this.onProposedChange(Collections.singletonList(var2), var1, var1 + 1);
      return this.list.set(var1, var2);
   }

   public void add(int var1, Object var2) {
      this.onProposedChange(Collections.singletonList(var2), var1, var1);

      try {
         ++this.modCount;
         this.list.add(var1, var2);
      } catch (Exception var4) {
         --this.modCount;
         throw var4;
      }
   }

   public Object remove(int var1) {
      this.onProposedChange(Collections.emptyList(), var1, var1 + 1);

      try {
         ++this.modCount;
         Object var2 = this.list.remove(var1);
         return var2;
      } catch (Exception var3) {
         --this.modCount;
         throw var3;
      }
   }

   public int indexOf(Object var1) {
      return this.list.indexOf(var1);
   }

   public int lastIndexOf(Object var1) {
      return this.list.lastIndexOf(var1);
   }

   public ListIterator listIterator() {
      return new VetoableListIteratorDecorator(new ModCountAccessorImpl(), this.list.listIterator(), 0);
   }

   public ListIterator listIterator(int var1) {
      return new VetoableListIteratorDecorator(new ModCountAccessorImpl(), this.list.listIterator(var1), var1);
   }

   public List subList(int var1, int var2) {
      return new VetoableSubListDecorator(new ModCountAccessorImpl(), this.list.subList(var1, var2), var1);
   }

   public String toString() {
      return this.list.toString();
   }

   public boolean equals(Object var1) {
      return this.list.equals(var1);
   }

   public int hashCode() {
      return this.list.hashCode();
   }

   private class ModCountAccessorImpl implements ModCountAccessor {
      public ModCountAccessorImpl() {
      }

      public int get() {
         return VetoableListDecorator.this.modCount;
      }

      public int incrementAndGet() {
         return ++VetoableListDecorator.this.modCount;
      }

      public int decrementAndGet() {
         return --VetoableListDecorator.this.modCount;
      }
   }

   private class VetoableListIteratorDecorator extends VetoableIteratorDecorator implements ListIterator {
      private final ListIterator lit;

      public VetoableListIteratorDecorator(ModCountAccessor var2, ListIterator var3, int var4) {
         super(var2, var3, var4);
         this.lit = var3;
      }

      public boolean hasPrevious() {
         this.checkForComodification();
         return this.lit.hasPrevious();
      }

      public Object previous() {
         this.checkForComodification();
         Object var1 = this.lit.previous();
         this.lastReturned = --this.cursor;
         return var1;
      }

      public int nextIndex() {
         this.checkForComodification();
         return this.lit.nextIndex();
      }

      public int previousIndex() {
         this.checkForComodification();
         return this.lit.previousIndex();
      }

      public void set(Object var1) {
         this.checkForComodification();
         if (this.lastReturned == -1) {
            throw new IllegalStateException();
         } else {
            VetoableListDecorator.this.onProposedChange(Collections.singletonList(var1), this.offset + this.lastReturned, this.offset + this.lastReturned + 1);
            this.lit.set(var1);
         }
      }

      public void add(Object var1) {
         this.checkForComodification();
         VetoableListDecorator.this.onProposedChange(Collections.singletonList(var1), this.offset + this.cursor, this.offset + this.cursor);

         try {
            this.incrementModCount();
            this.lit.add(var1);
         } catch (Exception var3) {
            this.decrementModCount();
            throw var3;
         }

         ++this.cursor;
      }
   }

   private class VetoableIteratorDecorator implements Iterator {
      private final Iterator it;
      private final ModCountAccessor modCountAccessor;
      private int modCount;
      protected final int offset;
      protected int cursor;
      protected int lastReturned;

      public VetoableIteratorDecorator(ModCountAccessor var2, Iterator var3, int var4) {
         this.modCountAccessor = var2;
         this.modCount = var2.get();
         this.it = var3;
         this.offset = var4;
      }

      public boolean hasNext() {
         this.checkForComodification();
         return this.it.hasNext();
      }

      public Object next() {
         this.checkForComodification();
         Object var1 = this.it.next();
         this.lastReturned = this.cursor++;
         return var1;
      }

      public void remove() {
         this.checkForComodification();
         if (this.lastReturned == -1) {
            throw new IllegalStateException();
         } else {
            VetoableListDecorator.this.onProposedChange(Collections.emptyList(), this.offset + this.lastReturned, this.offset + this.lastReturned + 1);

            try {
               this.incrementModCount();
               this.it.remove();
            } catch (Exception var2) {
               this.decrementModCount();
               throw var2;
            }

            this.lastReturned = -1;
            --this.cursor;
         }
      }

      protected void checkForComodification() {
         if (this.modCount != this.modCountAccessor.get()) {
            throw new ConcurrentModificationException();
         }
      }

      protected void incrementModCount() {
         this.modCount = this.modCountAccessor.incrementAndGet();
      }

      protected void decrementModCount() {
         this.modCount = this.modCountAccessor.decrementAndGet();
      }
   }

   private class VetoableSubListDecorator implements List {
      private final List subList;
      private final int offset;
      private final ModCountAccessor modCountAccessor;
      private int modCount;

      public VetoableSubListDecorator(ModCountAccessor var2, List var3, int var4) {
         this.modCountAccessor = var2;
         this.modCount = var2.get();
         this.subList = var3;
         this.offset = var4;
      }

      public int size() {
         this.checkForComodification();
         return this.subList.size();
      }

      public boolean isEmpty() {
         this.checkForComodification();
         return this.subList.isEmpty();
      }

      public boolean contains(Object var1) {
         this.checkForComodification();
         return this.subList.contains(var1);
      }

      public Iterator iterator() {
         this.checkForComodification();
         return VetoableListDecorator.this.new VetoableIteratorDecorator(new ModCountAccessorImplSub(), this.subList.iterator(), this.offset);
      }

      public Object[] toArray() {
         this.checkForComodification();
         return this.subList.toArray();
      }

      public Object[] toArray(Object[] var1) {
         this.checkForComodification();
         return this.subList.toArray(var1);
      }

      public boolean add(Object var1) {
         this.checkForComodification();
         VetoableListDecorator.this.onProposedChange(Collections.singletonList(var1), this.offset + this.size(), this.offset + this.size());

         try {
            this.incrementModCount();
            this.subList.add(var1);
            return true;
         } catch (Exception var3) {
            this.decrementModCount();
            throw var3;
         }
      }

      public boolean remove(Object var1) {
         this.checkForComodification();
         int var2 = this.indexOf(var1);
         if (var2 != -1) {
            this.remove(var2);
            return true;
         } else {
            return false;
         }
      }

      public boolean containsAll(Collection var1) {
         this.checkForComodification();
         return this.subList.containsAll(var1);
      }

      public boolean addAll(Collection var1) {
         this.checkForComodification();
         VetoableListDecorator.this.onProposedChange(Collections.unmodifiableList(new ArrayList(var1)), this.offset + this.size(), this.offset + this.size());

         try {
            this.incrementModCount();
            boolean var2 = this.subList.addAll(var1);
            if (!var2) {
               this.decrementModCount();
            }

            return var2;
         } catch (Exception var3) {
            this.decrementModCount();
            throw var3;
         }
      }

      public boolean addAll(int var1, Collection var2) {
         this.checkForComodification();
         VetoableListDecorator.this.onProposedChange(Collections.unmodifiableList(new ArrayList(var2)), this.offset + var1, this.offset + var1);

         try {
            this.incrementModCount();
            boolean var3 = this.subList.addAll(var1, var2);
            if (!var3) {
               this.decrementModCount();
            }

            return var3;
         } catch (Exception var4) {
            this.decrementModCount();
            throw var4;
         }
      }

      public boolean removeAll(Collection var1) {
         this.checkForComodification();
         VetoableListDecorator.this.removeFromList(this, this.offset, var1, false);

         try {
            this.incrementModCount();
            boolean var2 = this.subList.removeAll(var1);
            if (!var2) {
               this.decrementModCount();
            }

            return var2;
         } catch (Exception var3) {
            this.decrementModCount();
            throw var3;
         }
      }

      public boolean retainAll(Collection var1) {
         this.checkForComodification();
         VetoableListDecorator.this.removeFromList(this, this.offset, var1, true);

         try {
            this.incrementModCount();
            boolean var2 = this.subList.retainAll(var1);
            if (!var2) {
               this.decrementModCount();
            }

            return var2;
         } catch (Exception var3) {
            this.decrementModCount();
            throw var3;
         }
      }

      public void clear() {
         this.checkForComodification();
         VetoableListDecorator.this.onProposedChange(Collections.emptyList(), this.offset, this.offset + this.size());

         try {
            this.incrementModCount();
            this.subList.clear();
         } catch (Exception var2) {
            this.decrementModCount();
            throw var2;
         }
      }

      public Object get(int var1) {
         this.checkForComodification();
         return this.subList.get(var1);
      }

      public Object set(int var1, Object var2) {
         this.checkForComodification();
         VetoableListDecorator.this.onProposedChange(Collections.singletonList(var2), this.offset + var1, this.offset + var1 + 1);
         return this.subList.set(var1, var2);
      }

      public void add(int var1, Object var2) {
         this.checkForComodification();
         VetoableListDecorator.this.onProposedChange(Collections.singletonList(var2), this.offset + var1, this.offset + var1);

         try {
            this.incrementModCount();
            this.subList.add(var1, var2);
         } catch (Exception var4) {
            this.decrementModCount();
            throw var4;
         }
      }

      public Object remove(int var1) {
         this.checkForComodification();
         VetoableListDecorator.this.onProposedChange(Collections.emptyList(), this.offset + var1, this.offset + var1 + 1);

         try {
            this.incrementModCount();
            Object var2 = this.subList.remove(var1);
            return var2;
         } catch (Exception var3) {
            this.decrementModCount();
            throw var3;
         }
      }

      public int indexOf(Object var1) {
         this.checkForComodification();
         return this.subList.indexOf(var1);
      }

      public int lastIndexOf(Object var1) {
         this.checkForComodification();
         return this.subList.lastIndexOf(var1);
      }

      public ListIterator listIterator() {
         this.checkForComodification();
         return VetoableListDecorator.this.new VetoableListIteratorDecorator(new ModCountAccessorImplSub(), this.subList.listIterator(), this.offset);
      }

      public ListIterator listIterator(int var1) {
         this.checkForComodification();
         return VetoableListDecorator.this.new VetoableListIteratorDecorator(new ModCountAccessorImplSub(), this.subList.listIterator(var1), this.offset + var1);
      }

      public List subList(int var1, int var2) {
         this.checkForComodification();
         return VetoableListDecorator.this.new VetoableSubListDecorator(new ModCountAccessorImplSub(), this.subList.subList(var1, var2), this.offset + var1);
      }

      public String toString() {
         this.checkForComodification();
         return this.subList.toString();
      }

      public boolean equals(Object var1) {
         this.checkForComodification();
         return this.subList.equals(var1);
      }

      public int hashCode() {
         this.checkForComodification();
         return this.subList.hashCode();
      }

      private void checkForComodification() {
         if (this.modCount != this.modCountAccessor.get()) {
            throw new ConcurrentModificationException();
         }
      }

      private void incrementModCount() {
         this.modCount = this.modCountAccessor.incrementAndGet();
      }

      private void decrementModCount() {
         this.modCount = this.modCountAccessor.decrementAndGet();
      }

      private class ModCountAccessorImplSub implements ModCountAccessor {
         private ModCountAccessorImplSub() {
         }

         public int get() {
            return VetoableSubListDecorator.this.modCount;
         }

         public int incrementAndGet() {
            return VetoableSubListDecorator.this.modCount = VetoableSubListDecorator.this.modCountAccessor.incrementAndGet();
         }

         public int decrementAndGet() {
            return VetoableSubListDecorator.this.modCount = VetoableSubListDecorator.this.modCountAccessor.decrementAndGet();
         }

         // $FF: synthetic method
         ModCountAccessorImplSub(Object var2) {
            this();
         }
      }
   }

   private interface ModCountAccessor {
      int get();

      int incrementAndGet();

      int decrementAndGet();
   }
}
