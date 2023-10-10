package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public class LinkedListMultimap extends AbstractMultimap implements ListMultimap, Serializable {
   private transient LinkedListMultimap.Node head;
   private transient LinkedListMultimap.Node tail;
   private transient Map keyToKeyList;
   private transient int size;
   private transient int modCount;
   @GwtIncompatible("java serialization not supported")
   private static final long serialVersionUID = 0L;

   public static LinkedListMultimap create() {
      return new LinkedListMultimap();
   }

   public static LinkedListMultimap create(int var0) {
      return new LinkedListMultimap(var0);
   }

   public static LinkedListMultimap create(Multimap var0) {
      return new LinkedListMultimap(var0);
   }

   LinkedListMultimap() {
      this.keyToKeyList = Maps.newHashMap();
   }

   private LinkedListMultimap(int var1) {
      this.keyToKeyList = new HashMap(var1);
   }

   private LinkedListMultimap(Multimap var1) {
      this(var1.keySet().size());
      this.putAll(var1);
   }

   private LinkedListMultimap.Node addNode(@Nullable Object var1, @Nullable Object var2, @Nullable LinkedListMultimap.Node var3) {
      LinkedListMultimap.Node var4 = new LinkedListMultimap.Node(var1, var2);
      if (this.head == null) {
         this.head = this.tail = var4;
         this.keyToKeyList.put(var1, new LinkedListMultimap.KeyList(var4));
         ++this.modCount;
      } else {
         LinkedListMultimap.KeyList var5;
         if (var3 == null) {
            this.tail.next = var4;
            var4.previous = this.tail;
            this.tail = var4;
            var5 = (LinkedListMultimap.KeyList)this.keyToKeyList.get(var1);
            if (var5 == null) {
               this.keyToKeyList.put(var1, new LinkedListMultimap.KeyList(var4));
               ++this.modCount;
            } else {
               ++var5.count;
               LinkedListMultimap.Node var6 = var5.tail;
               var6.nextSibling = var4;
               var4.previousSibling = var6;
               var5.tail = var4;
            }
         } else {
            var5 = (LinkedListMultimap.KeyList)this.keyToKeyList.get(var1);
            ++var5.count;
            var4.previous = var3.previous;
            var4.previousSibling = var3.previousSibling;
            var4.next = var3;
            var4.nextSibling = var3;
            if (var3.previousSibling == null) {
               ((LinkedListMultimap.KeyList)this.keyToKeyList.get(var1)).head = var4;
            } else {
               var3.previousSibling.nextSibling = var4;
            }

            if (var3.previous == null) {
               this.head = var4;
            } else {
               var3.previous.next = var4;
            }

            var3.previous = var4;
            var3.previousSibling = var4;
         }
      }

      ++this.size;
      return var4;
   }

   private void removeNode(LinkedListMultimap.Node var1) {
      if (var1.previous != null) {
         var1.previous.next = var1.next;
      } else {
         this.head = var1.next;
      }

      if (var1.next != null) {
         var1.next.previous = var1.previous;
      } else {
         this.tail = var1.previous;
      }

      LinkedListMultimap.KeyList var2;
      if (var1.previousSibling == null && var1.nextSibling == null) {
         var2 = (LinkedListMultimap.KeyList)this.keyToKeyList.remove(var1.key);
         var2.count = 0;
         ++this.modCount;
      } else {
         var2 = (LinkedListMultimap.KeyList)this.keyToKeyList.get(var1.key);
         --var2.count;
         if (var1.previousSibling == null) {
            var2.head = var1.nextSibling;
         } else {
            var1.previousSibling.nextSibling = var1.nextSibling;
         }

         if (var1.nextSibling == null) {
            var2.tail = var1.previousSibling;
         } else {
            var1.nextSibling.previousSibling = var1.previousSibling;
         }
      }

      --this.size;
   }

   private void removeAllNodes(@Nullable Object var1) {
      Iterators.clear(new LinkedListMultimap.ValueForKeyIterator(this, var1));
   }

   private static void checkElement(@Nullable Object var0) {
      if (var0 == null) {
         throw new NoSuchElementException();
      }
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.head == null;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.keyToKeyList.containsKey(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.values().contains(var1);
   }

   public boolean put(@Nullable Object var1, @Nullable Object var2) {
      this.addNode(var1, var2, (LinkedListMultimap.Node)null);
      return true;
   }

   public List replaceValues(@Nullable Object var1, Iterable var2) {
      List var3 = this.getCopy(var1);
      LinkedListMultimap.ValueForKeyIterator var4 = new LinkedListMultimap.ValueForKeyIterator(this, var1);
      Iterator var5 = var2.iterator();

      while(var4.hasNext() && var5.hasNext()) {
         var4.next();
         var4.set(var5.next());
      }

      while(var4.hasNext()) {
         var4.next();
         var4.remove();
      }

      while(var5.hasNext()) {
         var4.add(var5.next());
      }

      return var3;
   }

   private List getCopy(@Nullable Object var1) {
      return Collections.unmodifiableList(Lists.newArrayList((Iterator)(new LinkedListMultimap.ValueForKeyIterator(this, var1))));
   }

   public List removeAll(@Nullable Object var1) {
      List var2 = this.getCopy(var1);
      this.removeAllNodes(var1);
      return var2;
   }

   public void clear() {
      this.head = null;
      this.tail = null;
      this.keyToKeyList.clear();
      this.size = 0;
      ++this.modCount;
   }

   public List get(@Nullable Object var1) {
      return new AbstractSequentialList(this, var1) {
         final Object val$key;
         final LinkedListMultimap this$0;

         {
            this.this$0 = var1;
            this.val$key = var2;
         }

         public int size() {
            LinkedListMultimap.KeyList var1 = (LinkedListMultimap.KeyList)LinkedListMultimap.access$600(this.this$0).get(this.val$key);
            return var1 == null ? 0 : var1.count;
         }

         public ListIterator listIterator(int var1) {
            return this.this$0.new ValueForKeyIterator(this.this$0, this.val$key, var1);
         }
      };
   }

   Set createKeySet() {
      return new Sets.ImprovedAbstractSet(this) {
         final LinkedListMultimap this$0;

         {
            this.this$0 = var1;
         }

         public int size() {
            return LinkedListMultimap.access$600(this.this$0).size();
         }

         public Iterator iterator() {
            return this.this$0.new DistinctKeyIterator(this.this$0);
         }

         public boolean contains(Object var1) {
            return this.this$0.containsKey(var1);
         }

         public boolean remove(Object var1) {
            return !this.this$0.removeAll(var1).isEmpty();
         }
      };
   }

   public List values() {
      return (List)super.values();
   }

   List createValues() {
      return new AbstractSequentialList(this) {
         final LinkedListMultimap this$0;

         {
            this.this$0 = var1;
         }

         public int size() {
            return LinkedListMultimap.access$900(this.this$0);
         }

         public ListIterator listIterator(int var1) {
            LinkedListMultimap.NodeIterator var2 = this.this$0.new NodeIterator(this.this$0, var1);
            return new TransformedListIterator(this, var2, var2) {
               final LinkedListMultimap.NodeIterator val$nodeItr;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.val$nodeItr = var3;
               }

               Object transform(Entry var1) {
                  return var1.getValue();
               }

               public void set(Object var1) {
                  this.val$nodeItr.setValue(var1);
               }

               Object transform(Object var1) {
                  return this.transform((Entry)var1);
               }
            };
         }
      };
   }

   public List entries() {
      return (List)super.entries();
   }

   List createEntries() {
      return new AbstractSequentialList(this) {
         final LinkedListMultimap this$0;

         {
            this.this$0 = var1;
         }

         public int size() {
            return LinkedListMultimap.access$900(this.this$0);
         }

         public ListIterator listIterator(int var1) {
            return this.this$0.new NodeIterator(this.this$0, var1);
         }
      };
   }

   Iterator entryIterator() {
      throw new AssertionError("should never be called");
   }

   Map createAsMap() {
      return new Multimaps.AsMap(this);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.size());
      Iterator var2 = this.entries().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.writeObject(var3.getKey());
         var1.writeObject(var3.getValue());
      }

   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.keyToKeyList = Maps.newLinkedHashMap();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         Object var4 = var1.readObject();
         Object var5 = var1.readObject();
         this.put(var4, var5);
      }

   }

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public Map asMap() {
      return super.asMap();
   }

   Collection createValues() {
      return this.createValues();
   }

   public Collection values() {
      return this.values();
   }

   public Multiset keys() {
      return super.keys();
   }

   public Set keySet() {
      return super.keySet();
   }

   Collection createEntries() {
      return this.createEntries();
   }

   public Collection entries() {
      return this.entries();
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public boolean putAll(Multimap var1) {
      return super.putAll(var1);
   }

   public boolean putAll(Object var1, Iterable var2) {
      return super.putAll(var1, var2);
   }

   public boolean remove(Object var1, Object var2) {
      return super.remove(var1, var2);
   }

   public boolean containsEntry(Object var1, Object var2) {
      return super.containsEntry(var1, var2);
   }

   public Collection get(Object var1) {
      return this.get(var1);
   }

   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   static int access$000(LinkedListMultimap var0) {
      return var0.modCount;
   }

   static LinkedListMultimap.Node access$100(LinkedListMultimap var0) {
      return var0.tail;
   }

   static LinkedListMultimap.Node access$200(LinkedListMultimap var0) {
      return var0.head;
   }

   static void access$300(Object var0) {
      checkElement(var0);
   }

   static void access$400(LinkedListMultimap var0, LinkedListMultimap.Node var1) {
      var0.removeNode(var1);
   }

   static void access$500(LinkedListMultimap var0, Object var1) {
      var0.removeAllNodes(var1);
   }

   static Map access$600(LinkedListMultimap var0) {
      return var0.keyToKeyList;
   }

   static LinkedListMultimap.Node access$700(LinkedListMultimap var0, Object var1, Object var2, LinkedListMultimap.Node var3) {
      return var0.addNode(var1, var2, var3);
   }

   static int access$900(LinkedListMultimap var0) {
      return var0.size;
   }

   private class ValueForKeyIterator implements ListIterator {
      final Object key;
      int nextIndex;
      LinkedListMultimap.Node next;
      LinkedListMultimap.Node current;
      LinkedListMultimap.Node previous;
      final LinkedListMultimap this$0;

      ValueForKeyIterator(@Nullable LinkedListMultimap var1, Object var2) {
         this.this$0 = var1;
         this.key = var2;
         LinkedListMultimap.KeyList var3 = (LinkedListMultimap.KeyList)LinkedListMultimap.access$600(var1).get(var2);
         this.next = var3 == null ? null : var3.head;
      }

      public ValueForKeyIterator(@Nullable LinkedListMultimap var1, Object var2, int var3) {
         this.this$0 = var1;
         LinkedListMultimap.KeyList var4 = (LinkedListMultimap.KeyList)LinkedListMultimap.access$600(var1).get(var2);
         int var5 = var4 == null ? 0 : var4.count;
         Preconditions.checkPositionIndex(var3, var5);
         if (var3 >= var5 / 2) {
            this.previous = var4 == null ? null : var4.tail;
            this.nextIndex = var5;

            while(var3++ < var5) {
               this.previous();
            }
         } else {
            this.next = var4 == null ? null : var4.head;

            while(var3-- > 0) {
               this.next();
            }
         }

         this.key = var2;
         this.current = null;
      }

      public boolean hasNext() {
         return this.next != null;
      }

      public Object next() {
         LinkedListMultimap.access$300(this.next);
         this.previous = this.current = this.next;
         this.next = this.next.nextSibling;
         ++this.nextIndex;
         return this.current.value;
      }

      public boolean hasPrevious() {
         return this.previous != null;
      }

      public Object previous() {
         LinkedListMultimap.access$300(this.previous);
         this.next = this.current = this.previous;
         this.previous = this.previous.previousSibling;
         --this.nextIndex;
         return this.current.value;
      }

      public int nextIndex() {
         return this.nextIndex;
      }

      public int previousIndex() {
         return this.nextIndex - 1;
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.current != null);
         if (this.current != this.next) {
            this.previous = this.current.previousSibling;
            --this.nextIndex;
         } else {
            this.next = this.current.nextSibling;
         }

         LinkedListMultimap.access$400(this.this$0, this.current);
         this.current = null;
      }

      public void set(Object var1) {
         Preconditions.checkState(this.current != null);
         this.current.value = var1;
      }

      public void add(Object var1) {
         this.previous = LinkedListMultimap.access$700(this.this$0, this.key, var1, this.next);
         ++this.nextIndex;
         this.current = null;
      }
   }

   private class DistinctKeyIterator implements Iterator {
      final Set seenKeys;
      LinkedListMultimap.Node next;
      LinkedListMultimap.Node current;
      int expectedModCount;
      final LinkedListMultimap this$0;

      private DistinctKeyIterator(LinkedListMultimap var1) {
         this.this$0 = var1;
         this.seenKeys = Sets.newHashSetWithExpectedSize(this.this$0.keySet().size());
         this.next = LinkedListMultimap.access$200(this.this$0);
         this.expectedModCount = LinkedListMultimap.access$000(this.this$0);
      }

      private void checkForConcurrentModification() {
         if (LinkedListMultimap.access$000(this.this$0) != this.expectedModCount) {
            throw new ConcurrentModificationException();
         }
      }

      public boolean hasNext() {
         this.checkForConcurrentModification();
         return this.next != null;
      }

      public Object next() {
         this.checkForConcurrentModification();
         LinkedListMultimap.access$300(this.next);
         this.current = this.next;
         this.seenKeys.add(this.current.key);

         do {
            this.next = this.next.next;
         } while(this.next != null && !this.seenKeys.add(this.next.key));

         return this.current.key;
      }

      public void remove() {
         this.checkForConcurrentModification();
         CollectPreconditions.checkRemove(this.current != null);
         LinkedListMultimap.access$500(this.this$0, this.current.key);
         this.current = null;
         this.expectedModCount = LinkedListMultimap.access$000(this.this$0);
      }

      DistinctKeyIterator(LinkedListMultimap var1, Object var2) {
         this(var1);
      }
   }

   private class NodeIterator implements ListIterator {
      int nextIndex;
      LinkedListMultimap.Node next;
      LinkedListMultimap.Node current;
      LinkedListMultimap.Node previous;
      int expectedModCount;
      final LinkedListMultimap this$0;

      NodeIterator(LinkedListMultimap var1, int var2) {
         this.this$0 = var1;
         this.expectedModCount = LinkedListMultimap.access$000(this.this$0);
         int var3 = var1.size();
         Preconditions.checkPositionIndex(var2, var3);
         if (var2 >= var3 / 2) {
            this.previous = LinkedListMultimap.access$100(var1);
            this.nextIndex = var3;

            while(var2++ < var3) {
               this.previous();
            }
         } else {
            this.next = LinkedListMultimap.access$200(var1);

            while(var2-- > 0) {
               this.next();
            }
         }

         this.current = null;
      }

      private void checkForConcurrentModification() {
         if (LinkedListMultimap.access$000(this.this$0) != this.expectedModCount) {
            throw new ConcurrentModificationException();
         }
      }

      public boolean hasNext() {
         this.checkForConcurrentModification();
         return this.next != null;
      }

      public LinkedListMultimap.Node next() {
         this.checkForConcurrentModification();
         LinkedListMultimap.access$300(this.next);
         this.previous = this.current = this.next;
         this.next = this.next.next;
         ++this.nextIndex;
         return this.current;
      }

      public void remove() {
         this.checkForConcurrentModification();
         CollectPreconditions.checkRemove(this.current != null);
         if (this.current != this.next) {
            this.previous = this.current.previous;
            --this.nextIndex;
         } else {
            this.next = this.current.next;
         }

         LinkedListMultimap.access$400(this.this$0, this.current);
         this.current = null;
         this.expectedModCount = LinkedListMultimap.access$000(this.this$0);
      }

      public boolean hasPrevious() {
         this.checkForConcurrentModification();
         return this.previous != null;
      }

      public LinkedListMultimap.Node previous() {
         this.checkForConcurrentModification();
         LinkedListMultimap.access$300(this.previous);
         this.next = this.current = this.previous;
         this.previous = this.previous.previous;
         --this.nextIndex;
         return this.current;
      }

      public int nextIndex() {
         return this.nextIndex;
      }

      public int previousIndex() {
         return this.nextIndex - 1;
      }

      public void set(Entry var1) {
         throw new UnsupportedOperationException();
      }

      public void add(Entry var1) {
         throw new UnsupportedOperationException();
      }

      void setValue(Object var1) {
         Preconditions.checkState(this.current != null);
         this.current.value = var1;
      }

      public void add(Object var1) {
         this.add((Entry)var1);
      }

      public void set(Object var1) {
         this.set((Entry)var1);
      }

      public Object previous() {
         return this.previous();
      }

      public Object next() {
         return this.next();
      }
   }

   private static class KeyList {
      LinkedListMultimap.Node head;
      LinkedListMultimap.Node tail;
      int count;

      KeyList(LinkedListMultimap.Node var1) {
         this.head = var1;
         this.tail = var1;
         var1.previousSibling = null;
         var1.nextSibling = null;
         this.count = 1;
      }
   }

   private static final class Node extends AbstractMapEntry {
      final Object key;
      Object value;
      LinkedListMultimap.Node next;
      LinkedListMultimap.Node previous;
      LinkedListMultimap.Node nextSibling;
      LinkedListMultimap.Node previousSibling;

      Node(@Nullable Object var1, @Nullable Object var2) {
         this.key = var1;
         this.value = var2;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(@Nullable Object var1) {
         Object var2 = this.value;
         this.value = var1;
         return var2;
      }
   }
}
