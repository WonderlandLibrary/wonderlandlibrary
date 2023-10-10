package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class TreeMultiset extends AbstractSortedMultiset implements Serializable {
   private final transient TreeMultiset.Reference rootReference;
   private final transient GeneralRange range;
   private final transient TreeMultiset.AvlNode header;
   @GwtIncompatible("not needed in emulated source")
   private static final long serialVersionUID = 1L;

   public static TreeMultiset create() {
      return new TreeMultiset(Ordering.natural());
   }

   public static TreeMultiset create(@Nullable Comparator var0) {
      return var0 == null ? new TreeMultiset(Ordering.natural()) : new TreeMultiset(var0);
   }

   public static TreeMultiset create(Iterable var0) {
      TreeMultiset var1 = create();
      Iterables.addAll(var1, var0);
      return var1;
   }

   TreeMultiset(TreeMultiset.Reference var1, GeneralRange var2, TreeMultiset.AvlNode var3) {
      super(var2.comparator());
      this.rootReference = var1;
      this.range = var2;
      this.header = var3;
   }

   TreeMultiset(Comparator var1) {
      super(var1);
      this.range = GeneralRange.all(var1);
      this.header = new TreeMultiset.AvlNode((Object)null, 1);
      successor(this.header, this.header);
      this.rootReference = new TreeMultiset.Reference();
   }

   private long aggregateForEntries(TreeMultiset.Aggregate var1) {
      TreeMultiset.AvlNode var2 = (TreeMultiset.AvlNode)this.rootReference.get();
      long var3 = var1.treeAggregate(var2);
      if (this.range.hasLowerBound()) {
         var3 -= this.aggregateBelowRange(var1, var2);
      }

      if (this.range.hasUpperBound()) {
         var3 -= this.aggregateAboveRange(var1, var2);
      }

      return var3;
   }

   private long aggregateBelowRange(TreeMultiset.Aggregate var1, @Nullable TreeMultiset.AvlNode var2) {
      if (var2 == null) {
         return 0L;
      } else {
         int var3 = this.comparator().compare(this.range.getLowerEndpoint(), TreeMultiset.AvlNode.access$500(var2));
         if (var3 < 0) {
            return this.aggregateBelowRange(var1, TreeMultiset.AvlNode.access$600(var2));
         } else if (var3 == 0) {
            switch(this.range.getLowerBoundType()) {
            case OPEN:
               return (long)var1.nodeAggregate(var2) + var1.treeAggregate(TreeMultiset.AvlNode.access$600(var2));
            case CLOSED:
               return var1.treeAggregate(TreeMultiset.AvlNode.access$600(var2));
            default:
               throw new AssertionError();
            }
         } else {
            return var1.treeAggregate(TreeMultiset.AvlNode.access$600(var2)) + (long)var1.nodeAggregate(var2) + this.aggregateBelowRange(var1, TreeMultiset.AvlNode.access$700(var2));
         }
      }
   }

   private long aggregateAboveRange(TreeMultiset.Aggregate var1, @Nullable TreeMultiset.AvlNode var2) {
      if (var2 == null) {
         return 0L;
      } else {
         int var3 = this.comparator().compare(this.range.getUpperEndpoint(), TreeMultiset.AvlNode.access$500(var2));
         if (var3 > 0) {
            return this.aggregateAboveRange(var1, TreeMultiset.AvlNode.access$700(var2));
         } else if (var3 == 0) {
            switch(this.range.getUpperBoundType()) {
            case OPEN:
               return (long)var1.nodeAggregate(var2) + var1.treeAggregate(TreeMultiset.AvlNode.access$700(var2));
            case CLOSED:
               return var1.treeAggregate(TreeMultiset.AvlNode.access$700(var2));
            default:
               throw new AssertionError();
            }
         } else {
            return var1.treeAggregate(TreeMultiset.AvlNode.access$700(var2)) + (long)var1.nodeAggregate(var2) + this.aggregateAboveRange(var1, TreeMultiset.AvlNode.access$600(var2));
         }
      }
   }

   public int size() {
      return Ints.saturatedCast(this.aggregateForEntries(TreeMultiset.Aggregate.SIZE));
   }

   int distinctElements() {
      return Ints.saturatedCast(this.aggregateForEntries(TreeMultiset.Aggregate.DISTINCT));
   }

   public int count(@Nullable Object var1) {
      try {
         TreeMultiset.AvlNode var3 = (TreeMultiset.AvlNode)this.rootReference.get();
         return this.range.contains(var1) && var3 != null ? var3.count(this.comparator(), var1) : 0;
      } catch (ClassCastException var4) {
         return 0;
      } catch (NullPointerException var5) {
         return 0;
      }
   }

   public int add(@Nullable Object var1, int var2) {
      CollectPreconditions.checkNonnegative(var2, "occurrences");
      if (var2 == 0) {
         return this.count(var1);
      } else {
         Preconditions.checkArgument(this.range.contains(var1));
         TreeMultiset.AvlNode var3 = (TreeMultiset.AvlNode)this.rootReference.get();
         if (var3 == null) {
            this.comparator().compare(var1, var1);
            TreeMultiset.AvlNode var6 = new TreeMultiset.AvlNode(var1, var2);
            successor(this.header, var6, this.header);
            this.rootReference.checkAndSet(var3, var6);
            return 0;
         } else {
            int[] var4 = new int[1];
            TreeMultiset.AvlNode var5 = var3.add(this.comparator(), var1, var2, var4);
            this.rootReference.checkAndSet(var3, var5);
            return var4[0];
         }
      }
   }

   public int remove(@Nullable Object var1, int var2) {
      CollectPreconditions.checkNonnegative(var2, "occurrences");
      if (var2 == 0) {
         return this.count(var1);
      } else {
         TreeMultiset.AvlNode var3 = (TreeMultiset.AvlNode)this.rootReference.get();
         int[] var4 = new int[1];

         TreeMultiset.AvlNode var5;
         try {
            if (!this.range.contains(var1) || var3 == null) {
               return 0;
            }

            var5 = var3.remove(this.comparator(), var1, var2, var4);
         } catch (ClassCastException var7) {
            return 0;
         } catch (NullPointerException var8) {
            return 0;
         }

         this.rootReference.checkAndSet(var3, var5);
         return var4[0];
      }
   }

   public int setCount(@Nullable Object var1, int var2) {
      CollectPreconditions.checkNonnegative(var2, "count");
      if (!this.range.contains(var1)) {
         Preconditions.checkArgument(var2 == 0);
         return 0;
      } else {
         TreeMultiset.AvlNode var3 = (TreeMultiset.AvlNode)this.rootReference.get();
         if (var3 == null) {
            if (var2 > 0) {
               this.add(var1, var2);
            }

            return 0;
         } else {
            int[] var4 = new int[1];
            TreeMultiset.AvlNode var5 = var3.setCount(this.comparator(), var1, var2, var4);
            this.rootReference.checkAndSet(var3, var5);
            return var4[0];
         }
      }
   }

   public boolean setCount(@Nullable Object var1, int var2, int var3) {
      CollectPreconditions.checkNonnegative(var3, "newCount");
      CollectPreconditions.checkNonnegative(var2, "oldCount");
      Preconditions.checkArgument(this.range.contains(var1));
      TreeMultiset.AvlNode var4 = (TreeMultiset.AvlNode)this.rootReference.get();
      if (var4 == null) {
         if (var2 == 0) {
            if (var3 > 0) {
               this.add(var1, var3);
            }

            return true;
         } else {
            return false;
         }
      } else {
         int[] var5 = new int[1];
         TreeMultiset.AvlNode var6 = var4.setCount(this.comparator(), var1, var2, var3, var5);
         this.rootReference.checkAndSet(var4, var6);
         return var5[0] == var2;
      }
   }

   private Multiset.Entry wrapEntry(TreeMultiset.AvlNode var1) {
      return new Multisets.AbstractEntry(this, var1) {
         final TreeMultiset.AvlNode val$baseEntry;
         final TreeMultiset this$0;

         {
            this.this$0 = var1;
            this.val$baseEntry = var2;
         }

         public Object getElement() {
            return this.val$baseEntry.getElement();
         }

         public int getCount() {
            int var1 = this.val$baseEntry.getCount();
            return var1 == 0 ? this.this$0.count(this.getElement()) : var1;
         }
      };
   }

   @Nullable
   private TreeMultiset.AvlNode firstNode() {
      TreeMultiset.AvlNode var1 = (TreeMultiset.AvlNode)this.rootReference.get();
      if (var1 == null) {
         return null;
      } else {
         TreeMultiset.AvlNode var2;
         if (this.range.hasLowerBound()) {
            Object var3 = this.range.getLowerEndpoint();
            var2 = TreeMultiset.AvlNode.access$800((TreeMultiset.AvlNode)this.rootReference.get(), this.comparator(), var3);
            if (var2 == null) {
               return null;
            }

            if (this.range.getLowerBoundType() == BoundType.OPEN && this.comparator().compare(var3, var2.getElement()) == 0) {
               var2 = TreeMultiset.AvlNode.access$900(var2);
            }
         } else {
            var2 = TreeMultiset.AvlNode.access$900(this.header);
         }

         return var2 != this.header && this.range.contains(var2.getElement()) ? var2 : null;
      }
   }

   @Nullable
   private TreeMultiset.AvlNode lastNode() {
      TreeMultiset.AvlNode var1 = (TreeMultiset.AvlNode)this.rootReference.get();
      if (var1 == null) {
         return null;
      } else {
         TreeMultiset.AvlNode var2;
         if (this.range.hasUpperBound()) {
            Object var3 = this.range.getUpperEndpoint();
            var2 = TreeMultiset.AvlNode.access$1000((TreeMultiset.AvlNode)this.rootReference.get(), this.comparator(), var3);
            if (var2 == null) {
               return null;
            }

            if (this.range.getUpperBoundType() == BoundType.OPEN && this.comparator().compare(var3, var2.getElement()) == 0) {
               var2 = TreeMultiset.AvlNode.access$1100(var2);
            }
         } else {
            var2 = TreeMultiset.AvlNode.access$1100(this.header);
         }

         return var2 != this.header && this.range.contains(var2.getElement()) ? var2 : null;
      }
   }

   Iterator entryIterator() {
      return new Iterator(this) {
         TreeMultiset.AvlNode current;
         Multiset.Entry prevEntry;
         final TreeMultiset this$0;

         {
            this.this$0 = var1;
            this.current = TreeMultiset.access$1200(this.this$0);
         }

         public Multiset.Entry next() {
            if (this == null) {
               throw new NoSuchElementException();
            } else {
               Multiset.Entry var1 = TreeMultiset.access$1400(this.this$0, this.current);
               this.prevEntry = var1;
               if (TreeMultiset.AvlNode.access$900(this.current) == TreeMultiset.access$1500(this.this$0)) {
                  this.current = null;
               } else {
                  this.current = TreeMultiset.AvlNode.access$900(this.current);
               }

               return var1;
            }
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.prevEntry != null);
            this.this$0.setCount(this.prevEntry.getElement(), 0);
            this.prevEntry = null;
         }

         public Object next() {
            return this.next();
         }
      };
   }

   Iterator descendingEntryIterator() {
      return new Iterator(this) {
         TreeMultiset.AvlNode current;
         Multiset.Entry prevEntry;
         final TreeMultiset this$0;

         {
            this.this$0 = var1;
            this.current = TreeMultiset.access$1600(this.this$0);
            this.prevEntry = null;
         }

         public Multiset.Entry next() {
            if (this == null) {
               throw new NoSuchElementException();
            } else {
               Multiset.Entry var1 = TreeMultiset.access$1400(this.this$0, this.current);
               this.prevEntry = var1;
               if (TreeMultiset.AvlNode.access$1100(this.current) == TreeMultiset.access$1500(this.this$0)) {
                  this.current = null;
               } else {
                  this.current = TreeMultiset.AvlNode.access$1100(this.current);
               }

               return var1;
            }
         }

         public void remove() {
            CollectPreconditions.checkRemove(this.prevEntry != null);
            this.this$0.setCount(this.prevEntry.getElement(), 0);
            this.prevEntry = null;
         }

         public Object next() {
            return this.next();
         }
      };
   }

   public SortedMultiset headMultiset(@Nullable Object var1, BoundType var2) {
      return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.upTo(this.comparator(), var1, var2)), this.header);
   }

   public SortedMultiset tailMultiset(@Nullable Object var1, BoundType var2) {
      return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.downTo(this.comparator(), var1, var2)), this.header);
   }

   static int distinctElements(@Nullable TreeMultiset.AvlNode var0) {
      return var0 == null ? 0 : TreeMultiset.AvlNode.access$400(var0);
   }

   private static void successor(TreeMultiset.AvlNode var0, TreeMultiset.AvlNode var1) {
      TreeMultiset.AvlNode.access$902(var0, var1);
      TreeMultiset.AvlNode.access$1102(var1, var0);
   }

   private static void successor(TreeMultiset.AvlNode var0, TreeMultiset.AvlNode var1, TreeMultiset.AvlNode var2) {
      successor(var0, var1);
      successor(var1, var2);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.elementSet().comparator());
      Serialization.writeMultiset(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      Comparator var2 = (Comparator)var1.readObject();
      Serialization.getFieldSetter(AbstractSortedMultiset.class, "comparator").set(this, var2);
      Serialization.getFieldSetter(TreeMultiset.class, "range").set(this, GeneralRange.all(var2));
      Serialization.getFieldSetter(TreeMultiset.class, "rootReference").set(this, new TreeMultiset.Reference());
      TreeMultiset.AvlNode var3 = new TreeMultiset.AvlNode((Object)null, 1);
      Serialization.getFieldSetter(TreeMultiset.class, "header").set(this, var3);
      successor(var3, var3);
      Serialization.populateMultiset(this, var1);
   }

   public SortedMultiset descendingMultiset() {
      return super.descendingMultiset();
   }

   public SortedMultiset subMultiset(Object var1, BoundType var2, Object var3, BoundType var4) {
      return super.subMultiset(var1, var2, var3, var4);
   }

   public Multiset.Entry pollLastEntry() {
      return super.pollLastEntry();
   }

   public Multiset.Entry pollFirstEntry() {
      return super.pollFirstEntry();
   }

   public Multiset.Entry lastEntry() {
      return super.lastEntry();
   }

   public Multiset.Entry firstEntry() {
      return super.firstEntry();
   }

   public Comparator comparator() {
      return super.comparator();
   }

   public NavigableSet elementSet() {
      return super.elementSet();
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

   public Set entrySet() {
      return super.entrySet();
   }

   public void clear() {
      super.clear();
   }

   public boolean retainAll(Collection var1) {
      return super.retainAll(var1);
   }

   public boolean removeAll(Collection var1) {
      return super.removeAll(var1);
   }

   public boolean addAll(Collection var1) {
      return super.addAll(var1);
   }

   public boolean remove(Object var1) {
      return super.remove(var1);
   }

   public boolean add(Object var1) {
      return super.add(var1);
   }

   public Iterator iterator() {
      return super.iterator();
   }

   public boolean contains(Object var1) {
      return super.contains(var1);
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }

   static TreeMultiset.AvlNode access$1200(TreeMultiset var0) {
      return var0.firstNode();
   }

   static GeneralRange access$1300(TreeMultiset var0) {
      return var0.range;
   }

   static Multiset.Entry access$1400(TreeMultiset var0, TreeMultiset.AvlNode var1) {
      return var0.wrapEntry(var1);
   }

   static TreeMultiset.AvlNode access$1500(TreeMultiset var0) {
      return var0.header;
   }

   static TreeMultiset.AvlNode access$1600(TreeMultiset var0) {
      return var0.lastNode();
   }

   static void access$1700(TreeMultiset.AvlNode var0, TreeMultiset.AvlNode var1, TreeMultiset.AvlNode var2) {
      successor(var0, var1, var2);
   }

   static void access$1800(TreeMultiset.AvlNode var0, TreeMultiset.AvlNode var1) {
      successor(var0, var1);
   }

   private static final class AvlNode extends Multisets.AbstractEntry {
      @Nullable
      private final Object elem;
      private int elemCount;
      private int distinctElements;
      private long totalCount;
      private int height;
      private TreeMultiset.AvlNode left;
      private TreeMultiset.AvlNode right;
      private TreeMultiset.AvlNode pred;
      private TreeMultiset.AvlNode succ;

      AvlNode(@Nullable Object var1, int var2) {
         Preconditions.checkArgument(var2 > 0);
         this.elem = var1;
         this.elemCount = var2;
         this.totalCount = (long)var2;
         this.distinctElements = 1;
         this.height = 1;
         this.left = null;
         this.right = null;
      }

      public int count(Comparator var1, Object var2) {
         int var3 = var1.compare(var2, this.elem);
         if (var3 < 0) {
            return this.left == null ? 0 : this.left.count(var1, var2);
         } else if (var3 > 0) {
            return this.right == null ? 0 : this.right.count(var1, var2);
         } else {
            return this.elemCount;
         }
      }

      private TreeMultiset.AvlNode addRightChild(Object var1, int var2) {
         this.right = new TreeMultiset.AvlNode(var1, var2);
         TreeMultiset.access$1700(this, this.right, this.succ);
         this.height = Math.max(2, this.height);
         ++this.distinctElements;
         this.totalCount += (long)var2;
         return this;
      }

      private TreeMultiset.AvlNode addLeftChild(Object var1, int var2) {
         this.left = new TreeMultiset.AvlNode(var1, var2);
         TreeMultiset.access$1700(this.pred, this.left, this);
         this.height = Math.max(2, this.height);
         ++this.distinctElements;
         this.totalCount += (long)var2;
         return this;
      }

      TreeMultiset.AvlNode add(Comparator var1, @Nullable Object var2, int var3, int[] var4) {
         int var5 = var1.compare(var2, this.elem);
         int var7;
         TreeMultiset.AvlNode var8;
         if (var5 < 0) {
            var8 = this.left;
            if (var8 == null) {
               var4[0] = 0;
               return this.addLeftChild(var2, var3);
            } else {
               var7 = var8.height;
               this.left = var8.add(var1, var2, var3, var4);
               if (var4[0] == 0) {
                  ++this.distinctElements;
               }

               this.totalCount += (long)var3;
               return this.left.height == var7 ? this : this.rebalance();
            }
         } else if (var5 > 0) {
            var8 = this.right;
            if (var8 == null) {
               var4[0] = 0;
               return this.addRightChild(var2, var3);
            } else {
               var7 = var8.height;
               this.right = var8.add(var1, var2, var3, var4);
               if (var4[0] == 0) {
                  ++this.distinctElements;
               }

               this.totalCount += (long)var3;
               return this.right.height == var7 ? this : this.rebalance();
            }
         } else {
            var4[0] = this.elemCount;
            long var6 = (long)this.elemCount + (long)var3;
            Preconditions.checkArgument(var6 <= 2147483647L);
            this.elemCount += var3;
            this.totalCount += (long)var3;
            return this;
         }
      }

      TreeMultiset.AvlNode remove(Comparator var1, @Nullable Object var2, int var3, int[] var4) {
         int var5 = var1.compare(var2, this.elem);
         TreeMultiset.AvlNode var6;
         if (var5 < 0) {
            var6 = this.left;
            if (var6 == null) {
               var4[0] = 0;
               return this;
            } else {
               this.left = var6.remove(var1, var2, var3, var4);
               if (var4[0] > 0) {
                  if (var3 >= var4[0]) {
                     --this.distinctElements;
                     this.totalCount -= (long)var4[0];
                  } else {
                     this.totalCount -= (long)var3;
                  }
               }

               return var4[0] == 0 ? this : this.rebalance();
            }
         } else if (var5 > 0) {
            var6 = this.right;
            if (var6 == null) {
               var4[0] = 0;
               return this;
            } else {
               this.right = var6.remove(var1, var2, var3, var4);
               if (var4[0] > 0) {
                  if (var3 >= var4[0]) {
                     --this.distinctElements;
                     this.totalCount -= (long)var4[0];
                  } else {
                     this.totalCount -= (long)var3;
                  }
               }

               return this.rebalance();
            }
         } else {
            var4[0] = this.elemCount;
            if (var3 >= this.elemCount) {
               return this.deleteMe();
            } else {
               this.elemCount -= var3;
               this.totalCount -= (long)var3;
               return this;
            }
         }
      }

      TreeMultiset.AvlNode setCount(Comparator var1, @Nullable Object var2, int var3, int[] var4) {
         int var5 = var1.compare(var2, this.elem);
         TreeMultiset.AvlNode var6;
         if (var5 < 0) {
            var6 = this.left;
            if (var6 == null) {
               var4[0] = 0;
               return var3 > 0 ? this.addLeftChild(var2, var3) : this;
            } else {
               this.left = var6.setCount(var1, var2, var3, var4);
               if (var3 == 0 && var4[0] != 0) {
                  --this.distinctElements;
               } else if (var3 > 0 && var4[0] == 0) {
                  ++this.distinctElements;
               }

               this.totalCount += (long)(var3 - var4[0]);
               return this.rebalance();
            }
         } else if (var5 > 0) {
            var6 = this.right;
            if (var6 == null) {
               var4[0] = 0;
               return var3 > 0 ? this.addRightChild(var2, var3) : this;
            } else {
               this.right = var6.setCount(var1, var2, var3, var4);
               if (var3 == 0 && var4[0] != 0) {
                  --this.distinctElements;
               } else if (var3 > 0 && var4[0] == 0) {
                  ++this.distinctElements;
               }

               this.totalCount += (long)(var3 - var4[0]);
               return this.rebalance();
            }
         } else {
            var4[0] = this.elemCount;
            if (var3 == 0) {
               return this.deleteMe();
            } else {
               this.totalCount += (long)(var3 - this.elemCount);
               this.elemCount = var3;
               return this;
            }
         }
      }

      TreeMultiset.AvlNode setCount(Comparator var1, @Nullable Object var2, int var3, int var4, int[] var5) {
         int var6 = var1.compare(var2, this.elem);
         TreeMultiset.AvlNode var7;
         if (var6 < 0) {
            var7 = this.left;
            if (var7 == null) {
               var5[0] = 0;
               return var3 == 0 && var4 > 0 ? this.addLeftChild(var2, var4) : this;
            } else {
               this.left = var7.setCount(var1, var2, var3, var4, var5);
               if (var5[0] == var3) {
                  if (var4 == 0 && var5[0] != 0) {
                     --this.distinctElements;
                  } else if (var4 > 0 && var5[0] == 0) {
                     ++this.distinctElements;
                  }

                  this.totalCount += (long)(var4 - var5[0]);
               }

               return this.rebalance();
            }
         } else if (var6 > 0) {
            var7 = this.right;
            if (var7 == null) {
               var5[0] = 0;
               return var3 == 0 && var4 > 0 ? this.addRightChild(var2, var4) : this;
            } else {
               this.right = var7.setCount(var1, var2, var3, var4, var5);
               if (var5[0] == var3) {
                  if (var4 == 0 && var5[0] != 0) {
                     --this.distinctElements;
                  } else if (var4 > 0 && var5[0] == 0) {
                     ++this.distinctElements;
                  }

                  this.totalCount += (long)(var4 - var5[0]);
               }

               return this.rebalance();
            }
         } else {
            var5[0] = this.elemCount;
            if (var3 == this.elemCount) {
               if (var4 == 0) {
                  return this.deleteMe();
               }

               this.totalCount += (long)(var4 - this.elemCount);
               this.elemCount = var4;
            }

            return this;
         }
      }

      private TreeMultiset.AvlNode deleteMe() {
         int var1 = this.elemCount;
         this.elemCount = 0;
         TreeMultiset.access$1800(this.pred, this.succ);
         if (this.left == null) {
            return this.right;
         } else if (this.right == null) {
            return this.left;
         } else {
            TreeMultiset.AvlNode var2;
            if (this.left.height >= this.right.height) {
               var2 = this.pred;
               var2.left = this.left.removeMax(var2);
               var2.right = this.right;
               var2.distinctElements = this.distinctElements - 1;
               var2.totalCount = this.totalCount - (long)var1;
               return var2.rebalance();
            } else {
               var2 = this.succ;
               var2.right = this.right.removeMin(var2);
               var2.left = this.left;
               var2.distinctElements = this.distinctElements - 1;
               var2.totalCount = this.totalCount - (long)var1;
               return var2.rebalance();
            }
         }
      }

      private TreeMultiset.AvlNode removeMin(TreeMultiset.AvlNode var1) {
         if (this.left == null) {
            return this.right;
         } else {
            this.left = this.left.removeMin(var1);
            --this.distinctElements;
            this.totalCount -= (long)var1.elemCount;
            return this.rebalance();
         }
      }

      private TreeMultiset.AvlNode removeMax(TreeMultiset.AvlNode var1) {
         if (this.right == null) {
            return this.left;
         } else {
            this.right = this.right.removeMax(var1);
            --this.distinctElements;
            this.totalCount -= (long)var1.elemCount;
            return this.rebalance();
         }
      }

      private void recomputeMultiset() {
         this.distinctElements = 1 + TreeMultiset.distinctElements(this.left) + TreeMultiset.distinctElements(this.right);
         this.totalCount = (long)this.elemCount + totalCount(this.left) + totalCount(this.right);
      }

      private void recomputeHeight() {
         this.height = 1 + Math.max(height(this.left), height(this.right));
      }

      private void recompute() {
         this.recomputeMultiset();
         this.recomputeHeight();
      }

      private TreeMultiset.AvlNode rebalance() {
         switch(this.balanceFactor()) {
         case -2:
            if (this.right.balanceFactor() > 0) {
               this.right = this.right.rotateRight();
            }

            return this.rotateLeft();
         case 2:
            if (this.left.balanceFactor() < 0) {
               this.left = this.left.rotateLeft();
            }

            return this.rotateRight();
         default:
            this.recomputeHeight();
            return this;
         }
      }

      private int balanceFactor() {
         return height(this.left) - height(this.right);
      }

      private TreeMultiset.AvlNode rotateLeft() {
         Preconditions.checkState(this.right != null);
         TreeMultiset.AvlNode var1 = this.right;
         this.right = var1.left;
         var1.left = this;
         var1.totalCount = this.totalCount;
         var1.distinctElements = this.distinctElements;
         this.recompute();
         var1.recomputeHeight();
         return var1;
      }

      private TreeMultiset.AvlNode rotateRight() {
         Preconditions.checkState(this.left != null);
         TreeMultiset.AvlNode var1 = this.left;
         this.left = var1.right;
         var1.right = this;
         var1.totalCount = this.totalCount;
         var1.distinctElements = this.distinctElements;
         this.recompute();
         var1.recomputeHeight();
         return var1;
      }

      private static long totalCount(@Nullable TreeMultiset.AvlNode var0) {
         return var0 == null ? 0L : var0.totalCount;
      }

      private static int height(@Nullable TreeMultiset.AvlNode var0) {
         return var0 == null ? 0 : var0.height;
      }

      @Nullable
      private TreeMultiset.AvlNode ceiling(Comparator var1, Object var2) {
         int var3 = var1.compare(var2, this.elem);
         if (var3 < 0) {
            return this.left == null ? this : (TreeMultiset.AvlNode)Objects.firstNonNull(this.left.ceiling(var1, var2), this);
         } else if (var3 == 0) {
            return this;
         } else {
            return this.right == null ? null : this.right.ceiling(var1, var2);
         }
      }

      @Nullable
      private TreeMultiset.AvlNode floor(Comparator var1, Object var2) {
         int var3 = var1.compare(var2, this.elem);
         if (var3 > 0) {
            return this.right == null ? this : (TreeMultiset.AvlNode)Objects.firstNonNull(this.right.floor(var1, var2), this);
         } else if (var3 == 0) {
            return this;
         } else {
            return this.left == null ? null : this.left.floor(var1, var2);
         }
      }

      public Object getElement() {
         return this.elem;
      }

      public int getCount() {
         return this.elemCount;
      }

      public String toString() {
         return Multisets.immutableEntry(this.getElement(), this.getCount()).toString();
      }

      static int access$200(TreeMultiset.AvlNode var0) {
         return var0.elemCount;
      }

      static long access$300(TreeMultiset.AvlNode var0) {
         return var0.totalCount;
      }

      static int access$400(TreeMultiset.AvlNode var0) {
         return var0.distinctElements;
      }

      static Object access$500(TreeMultiset.AvlNode var0) {
         return var0.elem;
      }

      static TreeMultiset.AvlNode access$600(TreeMultiset.AvlNode var0) {
         return var0.left;
      }

      static TreeMultiset.AvlNode access$700(TreeMultiset.AvlNode var0) {
         return var0.right;
      }

      static TreeMultiset.AvlNode access$800(TreeMultiset.AvlNode var0, Comparator var1, Object var2) {
         return var0.ceiling(var1, var2);
      }

      static TreeMultiset.AvlNode access$900(TreeMultiset.AvlNode var0) {
         return var0.succ;
      }

      static TreeMultiset.AvlNode access$1000(TreeMultiset.AvlNode var0, Comparator var1, Object var2) {
         return var0.floor(var1, var2);
      }

      static TreeMultiset.AvlNode access$1100(TreeMultiset.AvlNode var0) {
         return var0.pred;
      }

      static TreeMultiset.AvlNode access$902(TreeMultiset.AvlNode var0, TreeMultiset.AvlNode var1) {
         return var0.succ = var1;
      }

      static TreeMultiset.AvlNode access$1102(TreeMultiset.AvlNode var0, TreeMultiset.AvlNode var1) {
         return var0.pred = var1;
      }
   }

   private static final class Reference {
      @Nullable
      private Object value;

      private Reference() {
      }

      @Nullable
      public Object get() {
         return this.value;
      }

      public void checkAndSet(@Nullable Object var1, Object var2) {
         if (this.value != var1) {
            throw new ConcurrentModificationException();
         } else {
            this.value = var2;
         }
      }

      Reference(Object var1) {
         this();
      }
   }

   private static enum Aggregate {
      SIZE {
         int nodeAggregate(TreeMultiset.AvlNode var1) {
            return TreeMultiset.AvlNode.access$200(var1);
         }

         long treeAggregate(@Nullable TreeMultiset.AvlNode var1) {
            return var1 == null ? 0L : TreeMultiset.AvlNode.access$300(var1);
         }
      },
      DISTINCT {
         int nodeAggregate(TreeMultiset.AvlNode var1) {
            return 1;
         }

         long treeAggregate(@Nullable TreeMultiset.AvlNode var1) {
            return var1 == null ? 0L : (long)TreeMultiset.AvlNode.access$400(var1);
         }
      };

      private static final TreeMultiset.Aggregate[] $VALUES = new TreeMultiset.Aggregate[]{SIZE, DISTINCT};

      private Aggregate() {
      }

      abstract int nodeAggregate(TreeMultiset.AvlNode var1);

      abstract long treeAggregate(@Nullable TreeMultiset.AvlNode var1);

      Aggregate(Object var3) {
         this();
      }
   }
}
