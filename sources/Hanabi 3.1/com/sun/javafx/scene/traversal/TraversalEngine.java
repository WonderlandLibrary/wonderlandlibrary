package com.sun.javafx.scene.traversal;

import com.sun.javafx.application.PlatformImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class TraversalEngine {
   static final Algorithm DEFAULT_ALGORITHM = PlatformImpl.isContextual2DNavigation() ? new Hueristic2D() : new ContainerTabOrder();
   private final TraversalContext context = new EngineContext();
   private final TempEngineContext tempEngineContext = new TempEngineContext();
   protected final Algorithm algorithm;
   private final Bounds initialBounds = new BoundingBox(0.0, 0.0, 1.0, 1.0);
   private final ArrayList listeners = new ArrayList();

   protected TraversalEngine(Algorithm var1) {
      this.algorithm = var1;
   }

   protected TraversalEngine() {
      this.algorithm = null;
   }

   public final void addTraverseListener(TraverseListener var1) {
      this.listeners.add(var1);
   }

   final void notifyTraversedTo(Node var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         TraverseListener var3 = (TraverseListener)var2.next();
         var3.onTraverse(var1, this.getLayoutBounds(var1, this.getRoot()));
      }

   }

   public final Node select(Node var1, Direction var2) {
      return this.algorithm.select(var1, var2, this.context);
   }

   public final Node selectFirst() {
      return this.algorithm.selectFirst(this.context);
   }

   public final Node selectLast() {
      return this.algorithm.selectLast(this.context);
   }

   protected abstract Parent getRoot();

   public final boolean canTraverse() {
      return this.algorithm != null;
   }

   private Bounds getLayoutBounds(Node var1, Parent var2) {
      Bounds var3;
      if (var1 != null) {
         if (var2 == null) {
            var3 = var1.localToScene(var1.getLayoutBounds());
         } else {
            var3 = var2.sceneToLocal(var1.localToScene(var1.getLayoutBounds()));
         }
      } else {
         var3 = this.initialBounds;
      }

      return var3;
   }

   private abstract class BaseEngineContext implements TraversalContext {
      private BaseEngineContext() {
      }

      public List getAllTargetNodes() {
         ArrayList var1 = new ArrayList();
         this.addFocusableChildrenToList(var1, this.getRoot());
         return var1;
      }

      public Bounds getSceneLayoutBounds(Node var1) {
         return TraversalEngine.this.getLayoutBounds(var1, (Parent)null);
      }

      private void addFocusableChildrenToList(List var1, Parent var2) {
         ObservableList var3 = var2.getChildrenUnmodifiable();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            Node var5 = (Node)var4.next();
            if (var5.isFocusTraversable() && !var5.isFocused() && var5.impl_isTreeVisible() && !var5.isDisabled()) {
               var1.add(var5);
            }

            if (var5 instanceof Parent) {
               this.addFocusableChildrenToList(var1, (Parent)var5);
            }
         }

      }

      public Node selectFirstInParent(Parent var1) {
         TraversalEngine.this.tempEngineContext.setRoot(var1);
         return TraversalEngine.DEFAULT_ALGORITHM.selectFirst(TraversalEngine.this.tempEngineContext);
      }

      public Node selectLastInParent(Parent var1) {
         TraversalEngine.this.tempEngineContext.setRoot(var1);
         return TraversalEngine.DEFAULT_ALGORITHM.selectLast(TraversalEngine.this.tempEngineContext);
      }

      public Node selectInSubtree(Parent var1, Node var2, Direction var3) {
         TraversalEngine.this.tempEngineContext.setRoot(var1);
         return TraversalEngine.DEFAULT_ALGORITHM.select(var2, var3, TraversalEngine.this.tempEngineContext);
      }

      // $FF: synthetic method
      BaseEngineContext(Object var2) {
         this();
      }
   }

   private final class TempEngineContext extends BaseEngineContext {
      private Parent root;

      private TempEngineContext() {
         super(null);
      }

      public Parent getRoot() {
         return this.root;
      }

      public void setRoot(Parent var1) {
         this.root = var1;
      }

      // $FF: synthetic method
      TempEngineContext(Object var2) {
         this();
      }
   }

   private final class EngineContext extends BaseEngineContext {
      private EngineContext() {
         super(null);
      }

      public Parent getRoot() {
         return TraversalEngine.this.getRoot();
      }

      // $FF: synthetic method
      EngineContext(Object var2) {
         this();
      }
   }
}
