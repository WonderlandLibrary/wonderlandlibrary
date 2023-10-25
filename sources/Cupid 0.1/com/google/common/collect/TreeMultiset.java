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

@GwtCompatible(emulated = true)
public final class TreeMultiset<E> extends AbstractSortedMultiset<E> implements Serializable {
  private final transient Reference<AvlNode<E>> rootReference;
  
  private final transient GeneralRange<E> range;
  
  private final transient AvlNode<E> header;
  
  @GwtIncompatible("not needed in emulated source")
  private static final long serialVersionUID = 1L;
  
  public static <E extends Comparable> TreeMultiset<E> create() {
    return new TreeMultiset<E>(Ordering.natural());
  }
  
  public static <E> TreeMultiset<E> create(@Nullable Comparator<? super E> comparator) {
    return (comparator == null) ? new TreeMultiset<E>(Ordering.natural()) : new TreeMultiset<E>(comparator);
  }
  
  public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
    TreeMultiset<E> multiset = create();
    Iterables.addAll(multiset, elements);
    return multiset;
  }
  
  TreeMultiset(Reference<AvlNode<E>> rootReference, GeneralRange<E> range, AvlNode<E> endLink) {
    super(range.comparator());
    this.rootReference = rootReference;
    this.range = range;
    this.header = endLink;
  }
  
  TreeMultiset(Comparator<? super E> comparator) {
    super(comparator);
    this.range = GeneralRange.all(comparator);
    this.header = new AvlNode<E>(null, 1);
    successor(this.header, this.header);
    this.rootReference = new Reference<AvlNode<E>>();
  }
  
  private enum Aggregate {
    SIZE {
      int nodeAggregate(TreeMultiset.AvlNode<?> node) {
        return node.elemCount;
      }
      
      long treeAggregate(@Nullable TreeMultiset.AvlNode<?> root) {
        return (root == null) ? 0L : root.totalCount;
      }
    },
    DISTINCT {
      int nodeAggregate(TreeMultiset.AvlNode<?> node) {
        return 1;
      }
      
      long treeAggregate(@Nullable TreeMultiset.AvlNode<?> root) {
        return (root == null) ? 0L : root.distinctElements;
      }
    };
    
    abstract int nodeAggregate(TreeMultiset.AvlNode<?> param1AvlNode);
    
    abstract long treeAggregate(@Nullable TreeMultiset.AvlNode<?> param1AvlNode);
  }
  
  private long aggregateForEntries(Aggregate aggr) {
    AvlNode<E> root = this.rootReference.get();
    long total = aggr.treeAggregate(root);
    if (this.range.hasLowerBound())
      total -= aggregateBelowRange(aggr, root); 
    if (this.range.hasUpperBound())
      total -= aggregateAboveRange(aggr, root); 
    return total;
  }
  
  private long aggregateBelowRange(Aggregate aggr, @Nullable AvlNode<E> node) {
    if (node == null)
      return 0L; 
    int cmp = comparator().compare(this.range.getLowerEndpoint(), node.elem);
    if (cmp < 0)
      return aggregateBelowRange(aggr, node.left); 
    if (cmp == 0) {
      switch (this.range.getLowerBoundType()) {
        case OPEN:
          return aggr.nodeAggregate(node) + aggr.treeAggregate(node.left);
        case CLOSED:
          return aggr.treeAggregate(node.left);
      } 
      throw new AssertionError();
    } 
    return aggr.treeAggregate(node.left) + aggr.nodeAggregate(node) + aggregateBelowRange(aggr, node.right);
  }
  
  private long aggregateAboveRange(Aggregate aggr, @Nullable AvlNode<E> node) {
    if (node == null)
      return 0L; 
    int cmp = comparator().compare(this.range.getUpperEndpoint(), node.elem);
    if (cmp > 0)
      return aggregateAboveRange(aggr, node.right); 
    if (cmp == 0) {
      switch (this.range.getUpperBoundType()) {
        case OPEN:
          return aggr.nodeAggregate(node) + aggr.treeAggregate(node.right);
        case CLOSED:
          return aggr.treeAggregate(node.right);
      } 
      throw new AssertionError();
    } 
    return aggr.treeAggregate(node.right) + aggr.nodeAggregate(node) + aggregateAboveRange(aggr, node.left);
  }
  
  public int size() {
    return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
  }
  
  int distinctElements() {
    return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
  }
  
  public int count(@Nullable Object element) {
    try {
      E e = (E)element;
      AvlNode<E> root = this.rootReference.get();
      if (!this.range.contains(e) || root == null)
        return 0; 
      return root.count(comparator(), e);
    } catch (ClassCastException e) {
      return 0;
    } catch (NullPointerException e) {
      return 0;
    } 
  }
  
  public int add(@Nullable E element, int occurrences) {
    CollectPreconditions.checkNonnegative(occurrences, "occurrences");
    if (occurrences == 0)
      return count(element); 
    Preconditions.checkArgument(this.range.contains(element));
    AvlNode<E> root = this.rootReference.get();
    if (root == null) {
      comparator().compare(element, element);
      AvlNode<E> avlNode = new AvlNode<E>(element, occurrences);
      successor(this.header, avlNode, this.header);
      this.rootReference.checkAndSet(root, avlNode);
      return 0;
    } 
    int[] result = new int[1];
    AvlNode<E> newRoot = root.add(comparator(), element, occurrences, result);
    this.rootReference.checkAndSet(root, newRoot);
    return result[0];
  }
  
  public int remove(@Nullable Object element, int occurrences) {
    AvlNode<E> newRoot;
    CollectPreconditions.checkNonnegative(occurrences, "occurrences");
    if (occurrences == 0)
      return count(element); 
    AvlNode<E> root = this.rootReference.get();
    int[] result = new int[1];
    try {
      E e = (E)element;
      if (!this.range.contains(e) || root == null)
        return 0; 
      newRoot = root.remove(comparator(), e, occurrences, result);
    } catch (ClassCastException e) {
      return 0;
    } catch (NullPointerException e) {
      return 0;
    } 
    this.rootReference.checkAndSet(root, newRoot);
    return result[0];
  }
  
  public int setCount(@Nullable E element, int count) {
    CollectPreconditions.checkNonnegative(count, "count");
    if (!this.range.contains(element)) {
      Preconditions.checkArgument((count == 0));
      return 0;
    } 
    AvlNode<E> root = this.rootReference.get();
    if (root == null) {
      if (count > 0)
        add(element, count); 
      return 0;
    } 
    int[] result = new int[1];
    AvlNode<E> newRoot = root.setCount(comparator(), element, count, result);
    this.rootReference.checkAndSet(root, newRoot);
    return result[0];
  }
  
  public boolean setCount(@Nullable E element, int oldCount, int newCount) {
    CollectPreconditions.checkNonnegative(newCount, "newCount");
    CollectPreconditions.checkNonnegative(oldCount, "oldCount");
    Preconditions.checkArgument(this.range.contains(element));
    AvlNode<E> root = this.rootReference.get();
    if (root == null) {
      if (oldCount == 0) {
        if (newCount > 0)
          add(element, newCount); 
        return true;
      } 
      return false;
    } 
    int[] result = new int[1];
    AvlNode<E> newRoot = root.setCount(comparator(), element, oldCount, newCount, result);
    this.rootReference.checkAndSet(root, newRoot);
    return (result[0] == oldCount);
  }
  
  private Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
    return new Multisets.AbstractEntry<E>() {
        public E getElement() {
          return baseEntry.getElement();
        }
        
        public int getCount() {
          int result = baseEntry.getCount();
          if (result == 0)
            return TreeMultiset.this.count(getElement()); 
          return result;
        }
      };
  }
  
  @Nullable
  private AvlNode<E> firstNode() {
    AvlNode<E> node, root = this.rootReference.get();
    if (root == null)
      return null; 
    if (this.range.hasLowerBound()) {
      E endpoint = this.range.getLowerEndpoint();
      node = ((AvlNode)this.rootReference.get()).ceiling(comparator(), endpoint);
      if (node == null)
        return null; 
      if (this.range.getLowerBoundType() == BoundType.OPEN && comparator().compare(endpoint, node.getElement()) == 0)
        node = node.succ; 
    } else {
      node = this.header.succ;
    } 
    return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
  }
  
  @Nullable
  private AvlNode<E> lastNode() {
    AvlNode<E> node, root = this.rootReference.get();
    if (root == null)
      return null; 
    if (this.range.hasUpperBound()) {
      E endpoint = this.range.getUpperEndpoint();
      node = ((AvlNode)this.rootReference.get()).floor(comparator(), endpoint);
      if (node == null)
        return null; 
      if (this.range.getUpperBoundType() == BoundType.OPEN && comparator().compare(endpoint, node.getElement()) == 0)
        node = node.pred; 
    } else {
      node = this.header.pred;
    } 
    return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
  }
  
  Iterator<Multiset.Entry<E>> entryIterator() {
    return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
        TreeMultiset.AvlNode<E> current = TreeMultiset.this.firstNode();
        
        Multiset.Entry<E> prevEntry;
        
        public boolean hasNext() {
          if (this.current == null)
            return false; 
          if (TreeMultiset.this.range.tooHigh(this.current.getElement())) {
            this.current = null;
            return false;
          } 
          return true;
        }
        
        public Multiset.Entry<E> next() {
          if (!hasNext())
            throw new NoSuchElementException(); 
          Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
          this.prevEntry = result;
          if (this.current.succ == TreeMultiset.this.header) {
            this.current = null;
          } else {
            this.current = this.current.succ;
          } 
          return result;
        }
        
        public void remove() {
          CollectPreconditions.checkRemove((this.prevEntry != null));
          TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
          this.prevEntry = null;
        }
      };
  }
  
  Iterator<Multiset.Entry<E>> descendingEntryIterator() {
    return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
        TreeMultiset.AvlNode<E> current = TreeMultiset.this.lastNode();
        
        Multiset.Entry<E> prevEntry = null;
        
        public boolean hasNext() {
          if (this.current == null)
            return false; 
          if (TreeMultiset.this.range.tooLow(this.current.getElement())) {
            this.current = null;
            return false;
          } 
          return true;
        }
        
        public Multiset.Entry<E> next() {
          if (!hasNext())
            throw new NoSuchElementException(); 
          Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
          this.prevEntry = result;
          if (this.current.pred == TreeMultiset.this.header) {
            this.current = null;
          } else {
            this.current = this.current.pred;
          } 
          return result;
        }
        
        public void remove() {
          CollectPreconditions.checkRemove((this.prevEntry != null));
          TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
          this.prevEntry = null;
        }
      };
  }
  
  public SortedMultiset<E> headMultiset(@Nullable E upperBound, BoundType boundType) {
    return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.upTo(comparator(), upperBound, boundType)), this.header);
  }
  
  public SortedMultiset<E> tailMultiset(@Nullable E lowerBound, BoundType boundType) {
    return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.downTo(comparator(), lowerBound, boundType)), this.header);
  }
  
  static int distinctElements(@Nullable AvlNode<?> node) {
    return (node == null) ? 0 : node.distinctElements;
  }
  
  private static final class Reference<T> {
    @Nullable
    private T value;
    
    private Reference() {}
    
    @Nullable
    public T get() {
      return this.value;
    }
    
    public void checkAndSet(@Nullable T expected, T newValue) {
      if (this.value != expected)
        throw new ConcurrentModificationException(); 
      this.value = newValue;
    }
  }
  
  private static final class AvlNode<E> extends Multisets.AbstractEntry<E> {
    @Nullable
    private final E elem;
    
    private int elemCount;
    
    private int distinctElements;
    
    private long totalCount;
    
    private int height;
    
    private AvlNode<E> left;
    
    private AvlNode<E> right;
    
    private AvlNode<E> pred;
    
    private AvlNode<E> succ;
    
    AvlNode(@Nullable E elem, int elemCount) {
      Preconditions.checkArgument((elemCount > 0));
      this.elem = elem;
      this.elemCount = elemCount;
      this.totalCount = elemCount;
      this.distinctElements = 1;
      this.height = 1;
      this.left = null;
      this.right = null;
    }
    
    public int count(Comparator<? super E> comparator, E e) {
      int cmp = comparator.compare(e, this.elem);
      if (cmp < 0)
        return (this.left == null) ? 0 : this.left.count(comparator, e); 
      if (cmp > 0)
        return (this.right == null) ? 0 : this.right.count(comparator, e); 
      return this.elemCount;
    }
    
    private AvlNode<E> addRightChild(E e, int count) {
      this.right = new AvlNode(e, count);
      TreeMultiset.successor(this, this.right, this.succ);
      this.height = Math.max(2, this.height);
      this.distinctElements++;
      this.totalCount += count;
      return this;
    }
    
    private AvlNode<E> addLeftChild(E e, int count) {
      this.left = new AvlNode(e, count);
      TreeMultiset.successor(this.pred, this.left, this);
      this.height = Math.max(2, this.height);
      this.distinctElements++;
      this.totalCount += count;
      return this;
    }
    
    AvlNode<E> add(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
      int cmp = comparator.compare(e, this.elem);
      if (cmp < 0) {
        AvlNode<E> initLeft = this.left;
        if (initLeft == null) {
          result[0] = 0;
          return addLeftChild(e, count);
        } 
        int initHeight = initLeft.height;
        this.left = initLeft.add(comparator, e, count, result);
        if (result[0] == 0)
          this.distinctElements++; 
        this.totalCount += count;
        return (this.left.height == initHeight) ? this : rebalance();
      } 
      if (cmp > 0) {
        AvlNode<E> initRight = this.right;
        if (initRight == null) {
          result[0] = 0;
          return addRightChild(e, count);
        } 
        int initHeight = initRight.height;
        this.right = initRight.add(comparator, e, count, result);
        if (result[0] == 0)
          this.distinctElements++; 
        this.totalCount += count;
        return (this.right.height == initHeight) ? this : rebalance();
      } 
      result[0] = this.elemCount;
      long resultCount = this.elemCount + count;
      Preconditions.checkArgument((resultCount <= 2147483647L));
      this.elemCount += count;
      this.totalCount += count;
      return this;
    }
    
    AvlNode<E> remove(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
      int cmp = comparator.compare(e, this.elem);
      if (cmp < 0) {
        AvlNode<E> initLeft = this.left;
        if (initLeft == null) {
          result[0] = 0;
          return this;
        } 
        this.left = initLeft.remove(comparator, e, count, result);
        if (result[0] > 0)
          if (count >= result[0]) {
            this.distinctElements--;
            this.totalCount -= result[0];
          } else {
            this.totalCount -= count;
          }  
        return (result[0] == 0) ? this : rebalance();
      } 
      if (cmp > 0) {
        AvlNode<E> initRight = this.right;
        if (initRight == null) {
          result[0] = 0;
          return this;
        } 
        this.right = initRight.remove(comparator, e, count, result);
        if (result[0] > 0)
          if (count >= result[0]) {
            this.distinctElements--;
            this.totalCount -= result[0];
          } else {
            this.totalCount -= count;
          }  
        return rebalance();
      } 
      result[0] = this.elemCount;
      if (count >= this.elemCount)
        return deleteMe(); 
      this.elemCount -= count;
      this.totalCount -= count;
      return this;
    }
    
    AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
      int cmp = comparator.compare(e, this.elem);
      if (cmp < 0) {
        AvlNode<E> initLeft = this.left;
        if (initLeft == null) {
          result[0] = 0;
          return (count > 0) ? addLeftChild(e, count) : this;
        } 
        this.left = initLeft.setCount(comparator, e, count, result);
        if (count == 0 && result[0] != 0) {
          this.distinctElements--;
        } else if (count > 0 && result[0] == 0) {
          this.distinctElements++;
        } 
        this.totalCount += (count - result[0]);
        return rebalance();
      } 
      if (cmp > 0) {
        AvlNode<E> initRight = this.right;
        if (initRight == null) {
          result[0] = 0;
          return (count > 0) ? addRightChild(e, count) : this;
        } 
        this.right = initRight.setCount(comparator, e, count, result);
        if (count == 0 && result[0] != 0) {
          this.distinctElements--;
        } else if (count > 0 && result[0] == 0) {
          this.distinctElements++;
        } 
        this.totalCount += (count - result[0]);
        return rebalance();
      } 
      result[0] = this.elemCount;
      if (count == 0)
        return deleteMe(); 
      this.totalCount += (count - this.elemCount);
      this.elemCount = count;
      return this;
    }
    
    AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int expectedCount, int newCount, int[] result) {
      int cmp = comparator.compare(e, this.elem);
      if (cmp < 0) {
        AvlNode<E> initLeft = this.left;
        if (initLeft == null) {
          result[0] = 0;
          if (expectedCount == 0 && newCount > 0)
            return addLeftChild(e, newCount); 
          return this;
        } 
        this.left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
        if (result[0] == expectedCount) {
          if (newCount == 0 && result[0] != 0) {
            this.distinctElements--;
          } else if (newCount > 0 && result[0] == 0) {
            this.distinctElements++;
          } 
          this.totalCount += (newCount - result[0]);
        } 
        return rebalance();
      } 
      if (cmp > 0) {
        AvlNode<E> initRight = this.right;
        if (initRight == null) {
          result[0] = 0;
          if (expectedCount == 0 && newCount > 0)
            return addRightChild(e, newCount); 
          return this;
        } 
        this.right = initRight.setCount(comparator, e, expectedCount, newCount, result);
        if (result[0] == expectedCount) {
          if (newCount == 0 && result[0] != 0) {
            this.distinctElements--;
          } else if (newCount > 0 && result[0] == 0) {
            this.distinctElements++;
          } 
          this.totalCount += (newCount - result[0]);
        } 
        return rebalance();
      } 
      result[0] = this.elemCount;
      if (expectedCount == this.elemCount) {
        if (newCount == 0)
          return deleteMe(); 
        this.totalCount += (newCount - this.elemCount);
        this.elemCount = newCount;
      } 
      return this;
    }
    
    private AvlNode<E> deleteMe() {
      int oldElemCount = this.elemCount;
      this.elemCount = 0;
      TreeMultiset.successor(this.pred, this.succ);
      if (this.left == null)
        return this.right; 
      if (this.right == null)
        return this.left; 
      if (this.left.height >= this.right.height) {
        AvlNode<E> avlNode = this.pred;
        avlNode.left = this.left.removeMax(avlNode);
        avlNode.right = this.right;
        this.distinctElements--;
        this.totalCount -= oldElemCount;
        return avlNode.rebalance();
      } 
      AvlNode<E> newTop = this.succ;
      newTop.right = this.right.removeMin(newTop);
      newTop.left = this.left;
      this.distinctElements--;
      this.totalCount -= oldElemCount;
      return newTop.rebalance();
    }
    
    private AvlNode<E> removeMin(AvlNode<E> node) {
      if (this.left == null)
        return this.right; 
      this.left = this.left.removeMin(node);
      this.distinctElements--;
      this.totalCount -= node.elemCount;
      return rebalance();
    }
    
    private AvlNode<E> removeMax(AvlNode<E> node) {
      if (this.right == null)
        return this.left; 
      this.right = this.right.removeMax(node);
      this.distinctElements--;
      this.totalCount -= node.elemCount;
      return rebalance();
    }
    
    private void recomputeMultiset() {
      this.distinctElements = 1 + TreeMultiset.distinctElements(this.left) + TreeMultiset.distinctElements(this.right);
      this.totalCount = this.elemCount + totalCount(this.left) + totalCount(this.right);
    }
    
    private void recomputeHeight() {
      this.height = 1 + Math.max(height(this.left), height(this.right));
    }
    
    private void recompute() {
      recomputeMultiset();
      recomputeHeight();
    }
    
    private AvlNode<E> rebalance() {
      switch (balanceFactor()) {
        case -2:
          if (this.right.balanceFactor() > 0)
            this.right = this.right.rotateRight(); 
          return rotateLeft();
        case 2:
          if (this.left.balanceFactor() < 0)
            this.left = this.left.rotateLeft(); 
          return rotateRight();
      } 
      recomputeHeight();
      return this;
    }
    
    private int balanceFactor() {
      return height(this.left) - height(this.right);
    }
    
    private AvlNode<E> rotateLeft() {
      Preconditions.checkState((this.right != null));
      AvlNode<E> newTop = this.right;
      this.right = newTop.left;
      newTop.left = this;
      newTop.totalCount = this.totalCount;
      newTop.distinctElements = this.distinctElements;
      recompute();
      newTop.recomputeHeight();
      return newTop;
    }
    
    private AvlNode<E> rotateRight() {
      Preconditions.checkState((this.left != null));
      AvlNode<E> newTop = this.left;
      this.left = newTop.right;
      newTop.right = this;
      newTop.totalCount = this.totalCount;
      newTop.distinctElements = this.distinctElements;
      recompute();
      newTop.recomputeHeight();
      return newTop;
    }
    
    private static long totalCount(@Nullable AvlNode<?> node) {
      return (node == null) ? 0L : node.totalCount;
    }
    
    private static int height(@Nullable AvlNode<?> node) {
      return (node == null) ? 0 : node.height;
    }
    
    @Nullable
    private AvlNode<E> ceiling(Comparator<? super E> comparator, E e) {
      int cmp = comparator.compare(e, this.elem);
      if (cmp < 0)
        return (this.left == null) ? this : (AvlNode<E>)Objects.firstNonNull(this.left.ceiling(comparator, e), this); 
      if (cmp == 0)
        return this; 
      return (this.right == null) ? null : this.right.ceiling(comparator, e);
    }
    
    @Nullable
    private AvlNode<E> floor(Comparator<? super E> comparator, E e) {
      int cmp = comparator.compare(e, this.elem);
      if (cmp > 0)
        return (this.right == null) ? this : (AvlNode<E>)Objects.firstNonNull(this.right.floor(comparator, e), this); 
      if (cmp == 0)
        return this; 
      return (this.left == null) ? null : this.left.floor(comparator, e);
    }
    
    public E getElement() {
      return this.elem;
    }
    
    public int getCount() {
      return this.elemCount;
    }
    
    public String toString() {
      return Multisets.<E>immutableEntry(getElement(), getCount()).toString();
    }
  }
  
  private static <T> void successor(AvlNode<T> a, AvlNode<T> b) {
    a.succ = b;
    b.pred = a;
  }
  
  private static <T> void successor(AvlNode<T> a, AvlNode<T> b, AvlNode<T> c) {
    successor(a, b);
    successor(b, c);
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeObject(elementSet().comparator());
    Serialization.writeMultiset(this, stream);
  }
  
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    Comparator<? super E> comparator = (Comparator<? super E>)stream.readObject();
    Serialization.<AbstractSortedMultiset>getFieldSetter(AbstractSortedMultiset.class, "comparator").set(this, comparator);
    Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "range").set(this, GeneralRange.all(comparator));
    Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "rootReference").set(this, new Reference());
    AvlNode<E> header = new AvlNode<E>(null, 1);
    Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "header").set(this, header);
    successor(header, header);
    Serialization.populateMultiset(this, stream);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\TreeMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */