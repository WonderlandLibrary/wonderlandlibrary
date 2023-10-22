package com.sun.javafx.scene.traversal;

import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class TopMostTraversalEngine extends TraversalEngine {
   protected TopMostTraversalEngine() {
      super(DEFAULT_ALGORITHM);
   }

   TopMostTraversalEngine(Algorithm var1) {
      super(var1);
   }

   public final Node trav(Node var1, Direction var2) {
      Node var3 = null;
      Parent var4 = var1.getParent();

      Object var5;
      for(var5 = var1; var4 != null; var4 = var4.getParent()) {
         ParentTraversalEngine var6 = var4.getImpl_traversalEngine();
         if (var6 != null && var6.canTraverse()) {
            var3 = var6.select(var1, var2);
            if (var3 != null) {
               break;
            }

            var5 = var4;
            if (var2 == Direction.NEXT) {
               var2 = Direction.NEXT_IN_LINE;
            }
         }
      }

      if (var3 == null) {
         var3 = this.select((Node)var5, var2);
      }

      if (var3 == null) {
         if (var2 != Direction.NEXT && var2 != Direction.NEXT_IN_LINE) {
            if (var2 == Direction.PREVIOUS) {
               var3 = this.selectLast();
            }
         } else {
            var3 = this.selectFirst();
         }
      }

      if (var3 != null) {
         this.focusAndNotify(var3);
      }

      return var3;
   }

   private void focusAndNotify(Node var1) {
      var1.requestFocus();
      this.notifyTreeTraversedTo(var1);
   }

   private void notifyTreeTraversedTo(Node var1) {
      for(Parent var2 = var1.getParent(); var2 != null; var2 = var2.getParent()) {
         ParentTraversalEngine var3 = var2.getImpl_traversalEngine();
         if (var3 != null) {
            var3.notifyTraversedTo(var1);
         }
      }

      this.notifyTraversedTo(var1);
   }

   public final Node traverseToFirst() {
      Node var1 = this.selectFirst();
      if (var1 != null) {
         this.focusAndNotify(var1);
      }

      return var1;
   }

   public final Node traverseToLast() {
      Node var1 = this.selectLast();
      if (var1 != null) {
         this.focusAndNotify(var1);
      }

      return var1;
   }
}
