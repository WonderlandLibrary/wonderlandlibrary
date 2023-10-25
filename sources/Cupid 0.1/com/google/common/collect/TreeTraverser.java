package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

@Beta
@GwtCompatible(emulated = true)
public abstract class TreeTraverser<T> {
  public abstract Iterable<T> children(T paramT);
  
  public final FluentIterable<T> preOrderTraversal(final T root) {
    Preconditions.checkNotNull(root);
    return new FluentIterable<T>() {
        public UnmodifiableIterator<T> iterator() {
          return TreeTraverser.this.preOrderIterator((T)root);
        }
      };
  }
  
  UnmodifiableIterator<T> preOrderIterator(T root) {
    return new PreOrderIterator(root);
  }
  
  private final class PreOrderIterator extends UnmodifiableIterator<T> {
    private final Deque<Iterator<T>> stack;
    
    PreOrderIterator(T root) {
      this.stack = new ArrayDeque<Iterator<T>>();
      this.stack.addLast(Iterators.singletonIterator((T)Preconditions.checkNotNull(root)));
    }
    
    public boolean hasNext() {
      return !this.stack.isEmpty();
    }
    
    public T next() {
      Iterator<T> itr = this.stack.getLast();
      T result = (T)Preconditions.checkNotNull(itr.next());
      if (!itr.hasNext())
        this.stack.removeLast(); 
      Iterator<T> childItr = TreeTraverser.this.children(result).iterator();
      if (childItr.hasNext())
        this.stack.addLast(childItr); 
      return result;
    }
  }
  
  public final FluentIterable<T> postOrderTraversal(final T root) {
    Preconditions.checkNotNull(root);
    return new FluentIterable<T>() {
        public UnmodifiableIterator<T> iterator() {
          return TreeTraverser.this.postOrderIterator((T)root);
        }
      };
  }
  
  UnmodifiableIterator<T> postOrderIterator(T root) {
    return new PostOrderIterator(root);
  }
  
  private static final class PostOrderNode<T> {
    final T root;
    
    final Iterator<T> childIterator;
    
    PostOrderNode(T root, Iterator<T> childIterator) {
      this.root = (T)Preconditions.checkNotNull(root);
      this.childIterator = (Iterator<T>)Preconditions.checkNotNull(childIterator);
    }
  }
  
  private final class PostOrderIterator extends AbstractIterator<T> {
    private final ArrayDeque<TreeTraverser.PostOrderNode<T>> stack;
    
    PostOrderIterator(T root) {
      this.stack = new ArrayDeque<TreeTraverser.PostOrderNode<T>>();
      this.stack.addLast(expand(root));
    }
    
    protected T computeNext() {
      while (!this.stack.isEmpty()) {
        TreeTraverser.PostOrderNode<T> top = this.stack.getLast();
        if (top.childIterator.hasNext()) {
          T child = top.childIterator.next();
          this.stack.addLast(expand(child));
          continue;
        } 
        this.stack.removeLast();
        return top.root;
      } 
      return endOfData();
    }
    
    private TreeTraverser.PostOrderNode<T> expand(T t) {
      return new TreeTraverser.PostOrderNode<T>(t, TreeTraverser.this.children(t).iterator());
    }
  }
  
  public final FluentIterable<T> breadthFirstTraversal(final T root) {
    Preconditions.checkNotNull(root);
    return new FluentIterable<T>() {
        public UnmodifiableIterator<T> iterator() {
          return new TreeTraverser.BreadthFirstIterator((T)root);
        }
      };
  }
  
  private final class BreadthFirstIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
    private final Queue<T> queue;
    
    BreadthFirstIterator(T root) {
      this.queue = new ArrayDeque<T>();
      this.queue.add(root);
    }
    
    public boolean hasNext() {
      return !this.queue.isEmpty();
    }
    
    public T peek() {
      return this.queue.element();
    }
    
    public T next() {
      T result = this.queue.remove();
      Iterables.addAll(this.queue, TreeTraverser.this.children(result));
      return result;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\TreeTraverser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */