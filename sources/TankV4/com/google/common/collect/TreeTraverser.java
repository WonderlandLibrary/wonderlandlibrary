package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

@Beta
@GwtCompatible(
   emulated = true
)
public abstract class TreeTraverser {
   public abstract Iterable children(Object var1);

   public final FluentIterable preOrderTraversal(Object var1) {
      Preconditions.checkNotNull(var1);
      return new FluentIterable(this, var1) {
         final Object val$root;
         final TreeTraverser this$0;

         {
            this.this$0 = var1;
            this.val$root = var2;
         }

         public UnmodifiableIterator iterator() {
            return this.this$0.preOrderIterator(this.val$root);
         }

         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   UnmodifiableIterator preOrderIterator(Object var1) {
      return new TreeTraverser.PreOrderIterator(this, var1);
   }

   public final FluentIterable postOrderTraversal(Object var1) {
      Preconditions.checkNotNull(var1);
      return new FluentIterable(this, var1) {
         final Object val$root;
         final TreeTraverser this$0;

         {
            this.this$0 = var1;
            this.val$root = var2;
         }

         public UnmodifiableIterator iterator() {
            return this.this$0.postOrderIterator(this.val$root);
         }

         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   UnmodifiableIterator postOrderIterator(Object var1) {
      return new TreeTraverser.PostOrderIterator(this, var1);
   }

   public final FluentIterable breadthFirstTraversal(Object var1) {
      Preconditions.checkNotNull(var1);
      return new FluentIterable(this, var1) {
         final Object val$root;
         final TreeTraverser this$0;

         {
            this.this$0 = var1;
            this.val$root = var2;
         }

         public UnmodifiableIterator iterator() {
            return this.this$0.new BreadthFirstIterator(this.this$0, this.val$root);
         }

         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   private final class BreadthFirstIterator extends UnmodifiableIterator implements PeekingIterator {
      private final Queue queue;
      final TreeTraverser this$0;

      BreadthFirstIterator(TreeTraverser var1, Object var2) {
         this.this$0 = var1;
         this.queue = new ArrayDeque();
         this.queue.add(var2);
      }

      public boolean hasNext() {
         return !this.queue.isEmpty();
      }

      public Object peek() {
         return this.queue.element();
      }

      public Object next() {
         Object var1 = this.queue.remove();
         Iterables.addAll(this.queue, this.this$0.children(var1));
         return var1;
      }
   }

   private final class PostOrderIterator extends AbstractIterator {
      private final ArrayDeque stack;
      final TreeTraverser this$0;

      PostOrderIterator(TreeTraverser var1, Object var2) {
         this.this$0 = var1;
         this.stack = new ArrayDeque();
         this.stack.addLast(this.expand(var2));
      }

      protected Object computeNext() {
         while(true) {
            if (!this.stack.isEmpty()) {
               TreeTraverser.PostOrderNode var1 = (TreeTraverser.PostOrderNode)this.stack.getLast();
               if (var1.childIterator.hasNext()) {
                  Object var2 = var1.childIterator.next();
                  this.stack.addLast(this.expand(var2));
                  continue;
               }

               this.stack.removeLast();
               return var1.root;
            }

            return this.endOfData();
         }
      }

      private TreeTraverser.PostOrderNode expand(Object var1) {
         return new TreeTraverser.PostOrderNode(var1, this.this$0.children(var1).iterator());
      }
   }

   private static final class PostOrderNode {
      final Object root;
      final Iterator childIterator;

      PostOrderNode(Object var1, Iterator var2) {
         this.root = Preconditions.checkNotNull(var1);
         this.childIterator = (Iterator)Preconditions.checkNotNull(var2);
      }
   }

   private final class PreOrderIterator extends UnmodifiableIterator {
      private final Deque stack;
      final TreeTraverser this$0;

      PreOrderIterator(TreeTraverser var1, Object var2) {
         this.this$0 = var1;
         this.stack = new ArrayDeque();
         this.stack.addLast(Iterators.singletonIterator(Preconditions.checkNotNull(var2)));
      }

      public boolean hasNext() {
         return !this.stack.isEmpty();
      }

      public Object next() {
         Iterator var1 = (Iterator)this.stack.getLast();
         Object var2 = Preconditions.checkNotNull(var1.next());
         if (!var1.hasNext()) {
            this.stack.removeLast();
         }

         Iterator var3 = this.this$0.children(var2).iterator();
         if (var3.hasNext()) {
            this.stack.addLast(var3);
         }

         return var2;
      }
   }
}
