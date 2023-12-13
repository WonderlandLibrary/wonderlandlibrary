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
@GwtCompatible(
   emulated = true
)
public abstract class BinaryTreeTraverser extends TreeTraverser {
   public abstract Optional leftChild(Object var1);

   public abstract Optional rightChild(Object var1);

   public final Iterable children(Object var1) {
      Preconditions.checkNotNull(var1);
      return new FluentIterable(this, var1) {
         final Object val$root;
         final BinaryTreeTraverser this$0;

         {
            this.this$0 = var1;
            this.val$root = var2;
         }

         public Iterator iterator() {
            return new AbstractIterator(this) {
               boolean doneLeft;
               boolean doneRight;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
               }

               protected Object computeNext() {
                  Optional var1;
                  if (!this.doneLeft) {
                     this.doneLeft = true;
                     var1 = this.this$1.this$0.leftChild(this.this$1.val$root);
                     if (var1.isPresent()) {
                        return var1.get();
                     }
                  }

                  if (!this.doneRight) {
                     this.doneRight = true;
                     var1 = this.this$1.this$0.rightChild(this.this$1.val$root);
                     if (var1.isPresent()) {
                        return var1.get();
                     }
                  }

                  return this.endOfData();
               }
            };
         }
      };
   }

   UnmodifiableIterator preOrderIterator(Object var1) {
      return new BinaryTreeTraverser.PreOrderIterator(this, var1);
   }

   UnmodifiableIterator postOrderIterator(Object var1) {
      return new BinaryTreeTraverser.PostOrderIterator(this, var1);
   }

   public final FluentIterable inOrderTraversal(Object var1) {
      Preconditions.checkNotNull(var1);
      return new FluentIterable(this, var1) {
         final Object val$root;
         final BinaryTreeTraverser this$0;

         {
            this.this$0 = var1;
            this.val$root = var2;
         }

         public UnmodifiableIterator iterator() {
            return this.this$0.new InOrderIterator(this.this$0, this.val$root);
         }

         public Iterator iterator() {
            return this.iterator();
         }
      };
   }

   private static void pushIfPresent(Deque var0, Optional var1) {
      if (var1.isPresent()) {
         var0.addLast(var1.get());
      }

   }

   static void access$000(Deque var0, Optional var1) {
      pushIfPresent(var0, var1);
   }

   private final class InOrderIterator extends AbstractIterator {
      private final Deque stack;
      private final BitSet hasExpandedLeft;
      final BinaryTreeTraverser this$0;

      InOrderIterator(BinaryTreeTraverser var1, Object var2) {
         this.this$0 = var1;
         this.stack = new ArrayDeque();
         this.hasExpandedLeft = new BitSet();
         this.stack.addLast(var2);
      }

      protected Object computeNext() {
         while(!this.stack.isEmpty()) {
            Object var1 = this.stack.getLast();
            if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
               this.stack.removeLast();
               this.hasExpandedLeft.clear(this.stack.size());
               BinaryTreeTraverser.access$000(this.stack, this.this$0.rightChild(var1));
               return var1;
            }

            this.hasExpandedLeft.set(this.stack.size() - 1);
            BinaryTreeTraverser.access$000(this.stack, this.this$0.leftChild(var1));
         }

         return this.endOfData();
      }
   }

   private final class PostOrderIterator extends UnmodifiableIterator {
      private final Deque stack;
      private final BitSet hasExpanded;
      final BinaryTreeTraverser this$0;

      PostOrderIterator(BinaryTreeTraverser var1, Object var2) {
         this.this$0 = var1;
         this.stack = new ArrayDeque();
         this.stack.addLast(var2);
         this.hasExpanded = new BitSet();
      }

      public boolean hasNext() {
         return !this.stack.isEmpty();
      }

      public Object next() {
         while(true) {
            Object var1 = this.stack.getLast();
            boolean var2 = this.hasExpanded.get(this.stack.size() - 1);
            if (var2) {
               this.stack.removeLast();
               this.hasExpanded.clear(this.stack.size());
               return var1;
            }

            this.hasExpanded.set(this.stack.size() - 1);
            BinaryTreeTraverser.access$000(this.stack, this.this$0.rightChild(var1));
            BinaryTreeTraverser.access$000(this.stack, this.this$0.leftChild(var1));
         }
      }
   }

   private final class PreOrderIterator extends UnmodifiableIterator implements PeekingIterator {
      private final Deque stack;
      final BinaryTreeTraverser this$0;

      PreOrderIterator(BinaryTreeTraverser var1, Object var2) {
         this.this$0 = var1;
         this.stack = new ArrayDeque();
         this.stack.addLast(var2);
      }

      public boolean hasNext() {
         return !this.stack.isEmpty();
      }

      public Object next() {
         Object var1 = this.stack.removeLast();
         BinaryTreeTraverser.access$000(this.stack, this.this$0.rightChild(var1));
         BinaryTreeTraverser.access$000(this.stack, this.this$0.leftChild(var1));
         return var1;
      }

      public Object peek() {
         return this.stack.getLast();
      }
   }
}
