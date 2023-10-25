package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Iterator;

@Beta
@GwtCompatible(emulated = true)
public abstract class BinaryTreeTraverser<T> extends TreeTraverser<T> {
  public final Iterable<T> children(final T root) {
    Preconditions.checkNotNull(root);
    return new FluentIterable<T>() {
        public Iterator<T> iterator() {
          return new AbstractIterator<T>() {
              boolean doneLeft;
              
              boolean doneRight;
              
              protected T computeNext() {
                if (!this.doneLeft) {
                  this.doneLeft = true;
                  Optional<T> left = BinaryTreeTraverser.this.leftChild((T)root);
                  if (left.isPresent())
                    return (T)left.get(); 
                } 
                if (!this.doneRight) {
                  this.doneRight = true;
                  Optional<T> right = BinaryTreeTraverser.this.rightChild((T)root);
                  if (right.isPresent())
                    return (T)right.get(); 
                } 
                return endOfData();
              }
            };
        }
      };
  }
  
  UnmodifiableIterator<T> preOrderIterator(T root) {
    return new PreOrderIterator(root);
  }
  
  private final class PreOrderIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
    private final Deque<T> stack;
    
    PreOrderIterator(T root) {
      this.stack = new ArrayDeque<T>();
      this.stack.addLast(root);
    }
    
    public boolean hasNext() {
      return !this.stack.isEmpty();
    }
    
    public T next() {
      T result = this.stack.removeLast();
      BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(result));
      BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(result));
      return result;
    }
    
    public T peek() {
      return this.stack.getLast();
    }
  }
  
  UnmodifiableIterator<T> postOrderIterator(T root) {
    return new PostOrderIterator(root);
  }
  
  private final class PostOrderIterator extends UnmodifiableIterator<T> {
    private final Deque<T> stack;
    
    private final BitSet hasExpanded;
    
    PostOrderIterator(T root) {
      this.stack = new ArrayDeque<T>();
      this.stack.addLast(root);
      this.hasExpanded = new BitSet();
    }
    
    public boolean hasNext() {
      return !this.stack.isEmpty();
    }
    
    public T next() {
      while (true) {
        T node = this.stack.getLast();
        boolean expandedNode = this.hasExpanded.get(this.stack.size() - 1);
        if (expandedNode) {
          this.stack.removeLast();
          this.hasExpanded.clear(this.stack.size());
          return node;
        } 
        this.hasExpanded.set(this.stack.size() - 1);
        BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
        BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
      } 
    }
  }
  
  public final FluentIterable<T> inOrderTraversal(final T root) {
    Preconditions.checkNotNull(root);
    return new FluentIterable<T>() {
        public UnmodifiableIterator<T> iterator() {
          return new BinaryTreeTraverser.InOrderIterator((T)root);
        }
      };
  }
  
  private final class InOrderIterator extends AbstractIterator<T> {
    private final Deque<T> stack;
    
    private final BitSet hasExpandedLeft;
    
    InOrderIterator(T root) {
      this.stack = new ArrayDeque<T>();
      this.hasExpandedLeft = new BitSet();
      this.stack.addLast(root);
    }
    
    protected T computeNext() {
      while (!this.stack.isEmpty()) {
        T node = this.stack.getLast();
        if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
          this.stack.removeLast();
          this.hasExpandedLeft.clear(this.stack.size());
          BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
          return node;
        } 
        this.hasExpandedLeft.set(this.stack.size() - 1);
        BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
      } 
      return endOfData();
    }
  }
  
  private static <T> void pushIfPresent(Deque<T> stack, Optional<T> node) {
    if (node.isPresent())
      stack.addLast((T)node.get()); 
  }
  
  public abstract Optional<T> leftChild(T paramT);
  
  public abstract Optional<T> rightChild(T paramT);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\BinaryTreeTraverser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */