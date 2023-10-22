package javafx.scene;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.CssFlags;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.LayoutFlags;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.TempState;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Window;

public abstract class Parent extends Node {
   static final int DIRTY_CHILDREN_THRESHOLD = 10;
   private static final boolean warnOnAutoMove = PropertyHelper.getBooleanProperty("javafx.sg.warn");
   private static final int REMOVED_CHILDREN_THRESHOLD = 20;
   private boolean removedChildrenOptimizationDisabled = false;
   private final Set childSet = new HashSet();
   private int startIdx = 0;
   private int pgChildrenSize = 0;
   private boolean childrenTriggerPermutation = false;
   private List removed;
   private boolean geomChanged;
   private boolean childSetModified;
   private final ObservableList children = new VetoableListDecorator(new TrackableObservableList() {
      protected void onChanged(ListChangeListener.Change var1) {
         Parent.this.unmodifiableManagedChildren = null;
         boolean var2 = false;
         int var4;
         int var5;
         if (Parent.this.childSetModified) {
            while(true) {
               int var9;
               if (!var1.next()) {
                  if (Parent.this.dirtyChildren == null && Parent.this.children.size() > 10) {
                     Parent.this.dirtyChildren = new ArrayList(20);
                     if (Parent.this.dirtyChildrenCount > 0) {
                        var9 = Parent.this.children.size();

                        for(var4 = 0; var4 < var9; ++var4) {
                           Node var12 = (Node)Parent.this.children.get(var4);
                           if (var12.isVisible() && var12.boundsChanged) {
                              Parent.this.dirtyChildren.add(var12);
                           }
                        }
                     }
                  }
                  break;
               }

               var9 = var1.getFrom();
               var4 = var1.getTo();

               for(var5 = var9; var5 < var4; ++var5) {
                  Node var6 = (Node)Parent.this.children.get(var5);
                  if (var6.getParent() != null && var6.getParent() != Parent.this) {
                     if (Parent.warnOnAutoMove) {
                        System.err.println("WARNING added to a new parent without first removing it from its current");
                        System.err.println("    parent. It will be automatically removed from its current parent.");
                        System.err.println("    node=" + var6 + " oldparent= " + var6.getParent() + " newparent=" + this);
                     }

                     var6.getParent().children.remove(var6);
                     if (Parent.warnOnAutoMove) {
                        Thread.dumpStack();
                     }
                  }
               }

               List var10 = var1.getRemoved();
               int var11 = var10.size();

               int var7;
               Node var8;
               for(var7 = 0; var7 < var11; ++var7) {
                  var8 = (Node)var10.get(var7);
                  if (var8.isManaged()) {
                     var2 = true;
                  }
               }

               for(var7 = var9; var7 < var4; ++var7) {
                  var8 = (Node)Parent.this.children.get(var7);
                  if (var8.isManaged() || var8 instanceof Parent && ((Parent)var8).layoutFlag != LayoutFlags.CLEAN) {
                     var2 = true;
                  }

                  var8.setParent(Parent.this);
                  var8.setScenes(Parent.this.getScene(), Parent.this.getSubScene());
                  if (var8.isVisible()) {
                     Parent.this.geomChanged = true;
                     Parent.this.childIncluded(var8);
                  }
               }
            }
         } else {
            label130:
            while(var1.next()) {
               List var3 = var1.getRemoved();
               var4 = 0;

               for(var5 = var3.size(); var4 < var5; ++var4) {
                  if (((Node)var3.get(var4)).isManaged()) {
                     var2 = true;
                     break label130;
                  }
               }

               var4 = var1.getFrom();

               for(var5 = var1.getTo(); var4 < var5; ++var4) {
                  if (((Node)Parent.this.children.get(var4)).isManaged()) {
                     var2 = true;
                     break label130;
                  }
               }
            }
         }

         if (var2) {
            Parent.this.requestLayout();
         }

         if (Parent.this.geomChanged) {
            Parent.this.impl_geomChanged();
         }

         var1.reset();
         var1.next();
         if (Parent.this.startIdx > var1.getFrom()) {
            Parent.this.startIdx = var1.getFrom();
         }

         Parent.this.impl_markDirty(DirtyBits.PARENT_CHILDREN);
         Parent.this.impl_markDirty(DirtyBits.NODE_FORCE_SYNC);
      }
   }) {
      protected void onProposedChange(List var1, int[] var2) {
         Scene var3 = Parent.this.getScene();
         if (var3 != null) {
            Window var4 = var3.getWindow();
            if (var4 != null && var4.impl_getPeer() != null) {
               Toolkit.getToolkit().checkFxUserThread();
            }
         }

         Parent.this.geomChanged = false;
         long var12 = (long)(Parent.this.children.size() + var1.size());
         int var6 = 0;

         int var7;
         for(var7 = 0; var7 < var2.length; var7 += 2) {
            var6 += var2[var7 + 1] - var2[var7];
         }

         var12 -= (long)var6;
         if (Parent.this.childrenTriggerPermutation) {
            Parent.this.childSetModified = false;
         } else {
            Parent.this.childSetModified = true;
            Node var8;
            if (var12 == (long)Parent.this.childSet.size()) {
               Parent.this.childSetModified = false;

               for(var7 = var1.size() - 1; var7 >= 0; --var7) {
                  var8 = (Node)var1.get(var7);
                  if (!Parent.this.childSet.contains(var8)) {
                     Parent.this.childSetModified = true;
                     break;
                  }
               }
            }

            int var13;
            for(var7 = 0; var7 < var2.length; var7 += 2) {
               for(var13 = var2[var7]; var13 < var2[var7 + 1]; ++var13) {
                  Parent.this.childSet.remove(Parent.this.children.get(var13));
               }
            }

            try {
               if (Parent.this.childSetModified) {
                  for(var7 = var1.size() - 1; var7 >= 0; --var7) {
                     var8 = (Node)var1.get(var7);
                     if (var8 == null) {
                        throw new NullPointerException(this.constructExceptionMessage("child node is null", (Node)null));
                     }

                     if (var8.getClipParent() != null) {
                        throw new IllegalArgumentException(this.constructExceptionMessage("node already used as a clip", var8));
                     }

                     if (Parent.this.wouldCreateCycle(Parent.this, var8)) {
                        throw new IllegalArgumentException(this.constructExceptionMessage("cycle detected", var8));
                     }
                  }
               }

               Parent.this.childSet.addAll(var1);
               if ((long)Parent.this.childSet.size() != var12) {
                  throw new IllegalArgumentException(this.constructExceptionMessage("duplicate children added", (Node)null));
               }
            } catch (RuntimeException var11) {
               Parent.this.childSet.clear();
               Parent.this.childSet.addAll(Parent.this.children);
               throw var11;
            }

            if (Parent.this.childSetModified) {
               if (Parent.this.removed == null) {
                  Parent.this.removed = new ArrayList();
               }

               if (Parent.this.removed.size() + var6 > 20 || !Parent.this.impl_isTreeVisible()) {
                  Parent.this.removedChildrenOptimizationDisabled = true;
               }

               for(var7 = 0; var7 < var2.length; var7 += 2) {
                  for(var13 = var2[var7]; var13 < var2[var7 + 1]; ++var13) {
                     Node var9 = (Node)Parent.this.children.get(var13);
                     Scene var10 = var9.getScene();
                     if (var10 != null) {
                        var10.generateMouseExited(var9);
                     }

                     if (Parent.this.dirtyChildren != null) {
                        Parent.this.dirtyChildren.remove(var9);
                     }

                     if (var9.isVisible()) {
                        Parent.this.geomChanged = true;
                        Parent.this.childExcluded(var9);
                     }

                     if (var9.getParent() == Parent.this) {
                        var9.setParent((Parent)null);
                        var9.setScenes((Scene)null, (SubScene)null);
                     }

                     if (var3 != null && !Parent.this.removedChildrenOptimizationDisabled) {
                        Parent.this.removed.add(var9);
                     }
                  }
               }

            }
         }
      }

      private String constructExceptionMessage(String var1, Node var2) {
         StringBuilder var3 = new StringBuilder("Children: ");
         var3.append(var1);
         var3.append(": parent = ").append(Parent.this);
         if (var2 != null) {
            var3.append(", node = ").append(var2);
         }

         return var3.toString();
      }
   };
   private final ObservableList unmodifiableChildren;
   private List unmodifiableManagedChildren;
   private ObjectProperty impl_traversalEngine;
   private ReadOnlyBooleanWrapper needsLayout;
   LayoutFlags layoutFlag;
   boolean performingLayout;
   private boolean sizeCacheClear;
   private double prefWidthCache;
   private double prefHeightCache;
   private double minWidthCache;
   private double minHeightCache;
   private boolean sceneRoot;
   boolean layoutRoot;
   private final ObservableList stylesheets;
   private BaseBounds tmp;
   private BaseBounds cachedBounds;
   private boolean cachedBoundsInvalid;
   private int dirtyChildrenCount;
   private ArrayList dirtyChildren;
   private Node top;
   private Node left;
   private Node bottom;
   private Node right;
   private Node near;
   private Node far;
   private final int LEFT_INVALID;
   private final int TOP_INVALID;
   private final int NEAR_INVALID;
   private final int RIGHT_INVALID;
   private final int BOTTOM_INVALID;
   private final int FAR_INVALID;
   private Node currentlyProcessedChild;

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      NGGroup var1 = (NGGroup)this.impl_getPeer();
      if (Utils.assertionEnabled()) {
         List var2 = var1.getChildren();
         if (var2.size() != this.pgChildrenSize) {
            System.err.println("*** pgnodes.size() [" + var2.size() + "] != pgChildrenSize [" + this.pgChildrenSize + "]");
         }
      }

      if (this.impl_isDirty(DirtyBits.PARENT_CHILDREN)) {
         var1.clearFrom(this.startIdx);

         int var3;
         for(var3 = this.startIdx; var3 < this.children.size(); ++var3) {
            var1.add(var3, ((Node)this.children.get(var3)).impl_getPeer());
         }

         if (this.removedChildrenOptimizationDisabled) {
            var1.markDirty();
            this.removedChildrenOptimizationDisabled = false;
         } else if (this.removed != null && !this.removed.isEmpty()) {
            for(var3 = 0; var3 < this.removed.size(); ++var3) {
               var1.addToRemoved(((Node)this.removed.get(var3)).impl_getPeer());
            }
         }

         if (this.removed != null) {
            this.removed.clear();
         }

         this.pgChildrenSize = this.children.size();
         this.startIdx = this.pgChildrenSize;
      }

      if (Utils.assertionEnabled()) {
         this.validatePG();
      }

   }

   void validatePG() {
      boolean var1 = false;
      NGGroup var2 = (NGGroup)this.impl_getPeer();
      List var3 = var2.getChildren();
      if (var3.size() != this.children.size()) {
         System.err.println("*** pgnodes.size validatePG() [" + var3.size() + "] != children.size() [" + this.children.size() + "]");
         var1 = true;
      } else {
         for(int var4 = 0; var4 < this.children.size(); ++var4) {
            Node var5 = (Node)this.children.get(var4);
            if (var5.getParent() != this) {
               System.err.println("*** this=" + this + " validatePG children[" + var4 + "].parent= " + var5.getParent());
               var1 = true;
            }

            if (var5.impl_getPeer() != var3.get(var4)) {
               System.err.println("*** pgnodes[" + var4 + "] validatePG != children[" + var4 + "]");
               var1 = true;
            }
         }
      }

      if (var1) {
         throw new AssertionError("validation of PGGroup children failed");
      }
   }

   void printSeq(String var1, List var2) {
      String var3 = var1;

      Node var5;
      for(Iterator var4 = var2.iterator(); var4.hasNext(); var3 = var3 + var5 + " ") {
         var5 = (Node)var4.next();
      }

      System.out.println(var3);
   }

   protected ObservableList getChildren() {
      return this.children;
   }

   public ObservableList getChildrenUnmodifiable() {
      return this.unmodifiableChildren;
   }

   protected List getManagedChildren() {
      if (this.unmodifiableManagedChildren == null) {
         this.unmodifiableManagedChildren = new ArrayList();
         int var1 = 0;

         for(int var2 = this.children.size(); var1 < var2; ++var1) {
            Node var3 = (Node)this.children.get(var1);
            if (var3.isManaged()) {
               this.unmodifiableManagedChildren.add(var3);
            }
         }
      }

      return this.unmodifiableManagedChildren;
   }

   final void managedChildChanged() {
      this.requestLayout();
      this.unmodifiableManagedChildren = null;
   }

   final void impl_toFront(Node var1) {
      if (Utils.assertionEnabled() && !this.childSet.contains(var1)) {
         throw new AssertionError("specified node is not in the list of children");
      } else {
         if (this.children.get(this.children.size() - 1) != var1) {
            this.childrenTriggerPermutation = true;

            try {
               this.children.remove(var1);
               this.children.add(var1);
            } finally {
               this.childrenTriggerPermutation = false;
            }
         }

      }
   }

   final void impl_toBack(Node var1) {
      if (Utils.assertionEnabled() && !this.childSet.contains(var1)) {
         throw new AssertionError("specified node is not in the list of children");
      } else {
         if (this.children.get(0) != var1) {
            this.childrenTriggerPermutation = true;

            try {
               this.children.remove(var1);
               this.children.add(0, var1);
            } finally {
               this.childrenTriggerPermutation = false;
            }
         }

      }
   }

   void scenesChanged(Scene var1, SubScene var2, Scene var3, SubScene var4) {
      if (var3 != null && var1 == null) {
         StyleManager.getInstance().forget(this);
         if (this.removed != null) {
            this.removed.clear();
         }
      }

      for(int var5 = 0; var5 < this.children.size(); ++var5) {
         ((Node)this.children.get(var5)).setScenes(var1, var2);
      }

      boolean var6 = this.layoutFlag != LayoutFlags.CLEAN;
      this.sceneRoot = var2 != null && var2.getRoot() == this || var1 != null && var1.getRoot() == this;
      this.layoutRoot = !this.isManaged() || this.sceneRoot;
      if (var6 && var1 != null && this.layoutRoot && var2 != null) {
         var2.setDirtyLayout(this);
      }

   }

   void setDerivedDepthTest(boolean var1) {
      super.setDerivedDepthTest(var1);
      int var2 = 0;

      for(int var3 = this.children.size(); var2 < var3; ++var2) {
         Node var4 = (Node)this.children.get(var2);
         var4.computeDerivedDepthTest();
      }

   }

   /** @deprecated */
   @Deprecated
   protected void impl_pickNodeLocal(PickRay var1, PickResultChooser var2) {
      double var3 = this.impl_intersectsBounds(var1);
      if (!Double.isNaN(var3)) {
         for(int var5 = this.children.size() - 1; var5 >= 0; --var5) {
            ((Node)this.children.get(var5)).impl_pickNode(var1, var2);
            if (var2.isClosed()) {
               return;
            }
         }

         if (this.isPickOnBounds()) {
            var2.offer(this, var3, PickResultChooser.computePoint(var1, var3));
         }
      }

   }

   boolean isConnected() {
      return super.isConnected() || this.sceneRoot;
   }

   public Node lookup(String var1) {
      Node var2 = super.lookup(var1);
      if (var2 == null) {
         int var3 = 0;

         for(int var4 = this.children.size(); var3 < var4; ++var3) {
            Node var5 = (Node)this.children.get(var3);
            var2 = var5.lookup(var1);
            if (var2 != null) {
               return var2;
            }
         }
      }

      return var2;
   }

   List lookupAll(Selector var1, List var2) {
      var2 = super.lookupAll(var1, var2);
      int var3 = 0;

      for(int var4 = this.children.size(); var3 < var4; ++var3) {
         Node var5 = (Node)this.children.get(var3);
         var2 = var5.lookupAll(var1, var2);
      }

      return var2;
   }

   /** @deprecated */
   @Deprecated
   public final void setImpl_traversalEngine(ParentTraversalEngine var1) {
      this.impl_traversalEngineProperty().set(var1);
   }

   /** @deprecated */
   @Deprecated
   public final ParentTraversalEngine getImpl_traversalEngine() {
      return this.impl_traversalEngine == null ? null : (ParentTraversalEngine)this.impl_traversalEngine.get();
   }

   /** @deprecated */
   @Deprecated
   public final ObjectProperty impl_traversalEngineProperty() {
      if (this.impl_traversalEngine == null) {
         this.impl_traversalEngine = new SimpleObjectProperty(this, "impl_traversalEngine");
      }

      return this.impl_traversalEngine;
   }

   protected final void setNeedsLayout(boolean var1) {
      if (var1) {
         this.markDirtyLayout(true);
      } else if (this.layoutFlag == LayoutFlags.NEEDS_LAYOUT) {
         boolean var2 = false;
         int var3 = 0;

         for(int var4 = this.children.size(); var3 < var4; ++var3) {
            Node var5 = (Node)this.children.get(var3);
            if (var5 instanceof Parent && ((Parent)var5).layoutFlag != LayoutFlags.CLEAN) {
               var2 = true;
               break;
            }
         }

         this.setLayoutFlag(var2 ? LayoutFlags.DIRTY_BRANCH : LayoutFlags.CLEAN);
      }

   }

   public final boolean isNeedsLayout() {
      return this.layoutFlag == LayoutFlags.NEEDS_LAYOUT;
   }

   public final ReadOnlyBooleanProperty needsLayoutProperty() {
      if (this.needsLayout == null) {
         this.needsLayout = new ReadOnlyBooleanWrapper(this, "needsLayout", this.layoutFlag == LayoutFlags.NEEDS_LAYOUT);
      }

      return this.needsLayout;
   }

   void setLayoutFlag(LayoutFlags var1) {
      if (this.needsLayout != null) {
         this.needsLayout.set(var1 == LayoutFlags.NEEDS_LAYOUT);
      }

      this.layoutFlag = var1;
   }

   private void markDirtyLayout(boolean var1) {
      this.setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
      if (!var1 && !this.layoutRoot) {
         this.requestParentLayout();
      } else if (this.sceneRoot) {
         Toolkit.getToolkit().requestNextPulse();
         if (this.getSubScene() != null) {
            this.getSubScene().setDirtyLayout(this);
         }
      } else {
         this.markDirtyLayoutBranch();
      }

   }

   public void requestLayout() {
      this.clearSizeCache();
      this.markDirtyLayout(false);
   }

   protected final void requestParentLayout() {
      if (!this.layoutRoot) {
         Parent var1 = this.getParent();
         if (var1 != null && !var1.performingLayout) {
            var1.requestLayout();
         }
      }

   }

   void clearSizeCache() {
      if (!this.sizeCacheClear) {
         this.sizeCacheClear = true;
         this.prefWidthCache = -1.0;
         this.prefHeightCache = -1.0;
         this.minWidthCache = -1.0;
         this.minHeightCache = -1.0;
      }
   }

   public double prefWidth(double var1) {
      if (var1 == -1.0) {
         if (this.prefWidthCache == -1.0) {
            this.prefWidthCache = this.computePrefWidth(-1.0);
            if (Double.isNaN(this.prefWidthCache) || this.prefWidthCache < 0.0) {
               this.prefWidthCache = 0.0;
            }

            this.sizeCacheClear = false;
         }

         return this.prefWidthCache;
      } else {
         double var3 = this.computePrefWidth(var1);
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   public double prefHeight(double var1) {
      if (var1 == -1.0) {
         if (this.prefHeightCache == -1.0) {
            this.prefHeightCache = this.computePrefHeight(-1.0);
            if (Double.isNaN(this.prefHeightCache) || this.prefHeightCache < 0.0) {
               this.prefHeightCache = 0.0;
            }

            this.sizeCacheClear = false;
         }

         return this.prefHeightCache;
      } else {
         double var3 = this.computePrefHeight(var1);
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   public double minWidth(double var1) {
      if (var1 == -1.0) {
         if (this.minWidthCache == -1.0) {
            this.minWidthCache = this.computeMinWidth(-1.0);
            if (Double.isNaN(this.minWidthCache) || this.minWidthCache < 0.0) {
               this.minWidthCache = 0.0;
            }

            this.sizeCacheClear = false;
         }

         return this.minWidthCache;
      } else {
         double var3 = this.computeMinWidth(var1);
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   public double minHeight(double var1) {
      if (var1 == -1.0) {
         if (this.minHeightCache == -1.0) {
            this.minHeightCache = this.computeMinHeight(-1.0);
            if (Double.isNaN(this.minHeightCache) || this.minHeightCache < 0.0) {
               this.minHeightCache = 0.0;
            }

            this.sizeCacheClear = false;
         }

         return this.minHeightCache;
      } else {
         double var3 = this.computeMinHeight(var1);
         return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
      }
   }

   protected double computePrefWidth(double var1) {
      double var3 = 0.0;
      double var5 = 0.0;
      int var7 = 0;

      for(int var8 = this.children.size(); var7 < var8; ++var7) {
         Node var9 = (Node)this.children.get(var7);
         if (var9.isManaged()) {
            double var10 = var9.getLayoutBounds().getMinX() + var9.getLayoutX();
            var3 = Math.min(var3, var10);
            var5 = Math.max(var5, var10 + this.boundedSize(var9.prefWidth(-1.0), var9.minWidth(-1.0), var9.maxWidth(-1.0)));
         }
      }

      return var5 - var3;
   }

   protected double computePrefHeight(double var1) {
      double var3 = 0.0;
      double var5 = 0.0;
      int var7 = 0;

      for(int var8 = this.children.size(); var7 < var8; ++var7) {
         Node var9 = (Node)this.children.get(var7);
         if (var9.isManaged()) {
            double var10 = var9.getLayoutBounds().getMinY() + var9.getLayoutY();
            var3 = Math.min(var3, var10);
            var5 = Math.max(var5, var10 + this.boundedSize(var9.prefHeight(-1.0), var9.minHeight(-1.0), var9.maxHeight(-1.0)));
         }
      }

      return var5 - var3;
   }

   protected double computeMinWidth(double var1) {
      return this.prefWidth(var1);
   }

   protected double computeMinHeight(double var1) {
      return this.prefHeight(var1);
   }

   public double getBaselineOffset() {
      int var1 = 0;

      for(int var2 = this.children.size(); var1 < var2; ++var1) {
         Node var3 = (Node)this.children.get(var1);
         if (var3.isManaged()) {
            double var4 = var3.getBaselineOffset();
            if (var4 != Double.NEGATIVE_INFINITY) {
               return var3.getLayoutBounds().getMinY() + var3.getLayoutY() + var4;
            }
         }
      }

      return super.getBaselineOffset();
   }

   public final void layout() {
      switch (this.layoutFlag) {
         case CLEAN:
         default:
            break;
         case NEEDS_LAYOUT:
            if (this.performingLayout) {
               break;
            }

            this.performingLayout = true;
            this.layoutChildren();
         case DIRTY_BRANCH:
            int var1 = 0;

            for(int var2 = this.children.size(); var1 < var2; ++var1) {
               Node var3 = (Node)this.children.get(var1);
               if (var3 instanceof Parent) {
                  ((Parent)var3).layout();
               } else if (var3 instanceof SubScene) {
                  ((SubScene)var3).layoutPass();
               }
            }

            this.setLayoutFlag(LayoutFlags.CLEAN);
            this.performingLayout = false;
      }

   }

   protected void layoutChildren() {
      int var1 = 0;

      for(int var2 = this.children.size(); var1 < var2; ++var1) {
         Node var3 = (Node)this.children.get(var1);
         if (var3.isResizable() && var3.isManaged()) {
            var3.autosize();
         }
      }

   }

   final void notifyManagedChanged() {
      this.layoutRoot = !this.isManaged() || this.sceneRoot;
   }

   final boolean isSceneRoot() {
      return this.sceneRoot;
   }

   public final ObservableList getStylesheets() {
      return this.stylesheets;
   }

   /** @deprecated */
   @Deprecated
   public List impl_getAllParentStylesheets() {
      Object var1 = null;
      Parent var2 = this.getParent();
      if (var2 != null) {
         var1 = var2.impl_getAllParentStylesheets();
      }

      if (this.stylesheets != null && !this.stylesheets.isEmpty()) {
         if (var1 == null) {
            var1 = new ArrayList(this.stylesheets.size());
         }

         int var3 = 0;

         for(int var4 = this.stylesheets.size(); var3 < var4; ++var3) {
            ((List)var1).add(this.stylesheets.get(var3));
         }
      }

      return (List)var1;
   }

   /** @deprecated */
   @Deprecated
   protected void impl_processCSS(WritableValue var1) {
      if (this.cssFlag != CssFlags.CLEAN) {
         if (this.cssFlag == CssFlags.DIRTY_BRANCH) {
            super.processCSS();
         } else {
            super.impl_processCSS(var1);
            if (!this.children.isEmpty()) {
               Node[] var2 = (Node[])this.children.toArray(new Node[this.children.size()]);

               for(int var3 = 0; var3 < var2.length; ++var3) {
                  Node var4 = var2[var3];
                  Parent var5 = var4.getParent();
                  if (var5 != null && var5 == this) {
                     if (CssFlags.UPDATE.compareTo(var4.cssFlag) > 0) {
                        var4.cssFlag = CssFlags.UPDATE;
                     }

                     var4.impl_processCSS(var1);
                  }
               }

            }
         }
      }
   }

   protected Parent() {
      this.unmodifiableChildren = FXCollections.unmodifiableObservableList(this.children);
      this.unmodifiableManagedChildren = null;
      this.layoutFlag = LayoutFlags.CLEAN;
      this.performingLayout = false;
      this.sizeCacheClear = true;
      this.prefWidthCache = -1.0;
      this.prefHeightCache = -1.0;
      this.minWidthCache = -1.0;
      this.minHeightCache = -1.0;
      this.sceneRoot = false;
      this.layoutRoot = false;
      this.stylesheets = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            Scene var2 = Parent.this.getScene();
            if (var2 != null) {
               StyleManager.getInstance().stylesheetsChanged(Parent.this, var1);
               var1.reset();

               while(var1.next() && !var1.wasRemoved()) {
               }

               Parent.this.impl_reapplyCSS();
            }

         }
      };
      this.tmp = new RectBounds();
      this.cachedBounds = new RectBounds();
      this.LEFT_INVALID = 1;
      this.TOP_INVALID = 2;
      this.NEAR_INVALID = 4;
      this.RIGHT_INVALID = 8;
      this.BOTTOM_INVALID = 16;
      this.FAR_INVALID = 32;
      this.layoutFlag = LayoutFlags.NEEDS_LAYOUT;
      this.setAccessibleRole(AccessibleRole.PARENT);
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGGroup();
   }

   void nodeResolvedOrientationChanged() {
      int var1 = 0;

      for(int var2 = this.children.size(); var1 < var2; ++var1) {
         ((Node)this.children.get(var1)).parentResolvedOrientationInvalidated();
      }

   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      if (this.children.isEmpty()) {
         return var1.makeEmpty();
      } else if (var2.isTranslateOrIdentity()) {
         if (this.cachedBoundsInvalid) {
            this.recomputeBounds();
            if (this.dirtyChildren != null) {
               this.dirtyChildren.clear();
            }

            this.cachedBoundsInvalid = false;
            this.dirtyChildrenCount = 0;
         }

         if (!var2.isIdentity()) {
            var1 = var1.deriveWithNewBounds((float)((double)this.cachedBounds.getMinX() + var2.getMxt()), (float)((double)this.cachedBounds.getMinY() + var2.getMyt()), (float)((double)this.cachedBounds.getMinZ() + var2.getMzt()), (float)((double)this.cachedBounds.getMaxX() + var2.getMxt()), (float)((double)this.cachedBounds.getMaxY() + var2.getMyt()), (float)((double)this.cachedBounds.getMaxZ() + var2.getMzt()));
         } else {
            var1 = var1.deriveWithNewBounds(this.cachedBounds);
         }

         return var1;
      } else {
         double var3 = Double.MAX_VALUE;
         double var5 = Double.MAX_VALUE;
         double var7 = Double.MAX_VALUE;
         double var9 = Double.MIN_VALUE;
         double var11 = Double.MIN_VALUE;
         double var13 = Double.MIN_VALUE;
         boolean var15 = true;
         int var16 = 0;

         for(int var17 = this.children.size(); var16 < var17; ++var16) {
            Node var18 = (Node)this.children.get(var16);
            if (var18.isVisible()) {
               var1 = this.getChildTransformedBounds(var18, var2, var1);
               if (!var1.isEmpty()) {
                  if (var15) {
                     var3 = (double)var1.getMinX();
                     var5 = (double)var1.getMinY();
                     var7 = (double)var1.getMinZ();
                     var9 = (double)var1.getMaxX();
                     var11 = (double)var1.getMaxY();
                     var13 = (double)var1.getMaxZ();
                     var15 = false;
                  } else {
                     var3 = Math.min((double)var1.getMinX(), var3);
                     var5 = Math.min((double)var1.getMinY(), var5);
                     var7 = Math.min((double)var1.getMinZ(), var7);
                     var9 = Math.max((double)var1.getMaxX(), var9);
                     var11 = Math.max((double)var1.getMaxY(), var11);
                     var13 = Math.max((double)var1.getMaxZ(), var13);
                  }
               }
            }
         }

         if (var15) {
            var1.makeEmpty();
         } else {
            var1 = var1.deriveWithNewBounds((float)var3, (float)var5, (float)var7, (float)var9, (float)var11, (float)var13);
         }

         return var1;
      }
   }

   private void setChildDirty(Node var1, boolean var2) {
      if (var1.boundsChanged != var2) {
         var1.boundsChanged = var2;
         if (var2) {
            if (this.dirtyChildren != null) {
               this.dirtyChildren.add(var1);
            }

            ++this.dirtyChildrenCount;
         } else {
            if (this.dirtyChildren != null) {
               this.dirtyChildren.remove(var1);
            }

            --this.dirtyChildrenCount;
         }

      }
   }

   private void childIncluded(Node var1) {
      this.cachedBoundsInvalid = true;
      this.setChildDirty(var1, true);
   }

   private void childExcluded(Node var1) {
      if (var1 == this.left) {
         this.left = null;
         this.cachedBoundsInvalid = true;
      }

      if (var1 == this.top) {
         this.top = null;
         this.cachedBoundsInvalid = true;
      }

      if (var1 == this.near) {
         this.near = null;
         this.cachedBoundsInvalid = true;
      }

      if (var1 == this.right) {
         this.right = null;
         this.cachedBoundsInvalid = true;
      }

      if (var1 == this.bottom) {
         this.bottom = null;
         this.cachedBoundsInvalid = true;
      }

      if (var1 == this.far) {
         this.far = null;
         this.cachedBoundsInvalid = true;
      }

      this.setChildDirty(var1, false);
   }

   private void recomputeBounds() {
      if (this.children.isEmpty()) {
         this.cachedBounds.makeEmpty();
      } else if (this.children.size() == 1) {
         Node var1 = (Node)this.children.get(0);
         var1.boundsChanged = false;
         if (var1.isVisible()) {
            this.cachedBounds = this.getChildTransformedBounds(var1, BaseTransform.IDENTITY_TRANSFORM, this.cachedBounds);
            this.top = this.left = this.bottom = this.right = this.near = this.far = var1;
         } else {
            this.cachedBounds.makeEmpty();
         }

      } else {
         if (this.dirtyChildrenCount == 0 || !this.updateCachedBounds((List)(this.dirtyChildren != null ? this.dirtyChildren : this.children), this.dirtyChildrenCount)) {
            this.createCachedBounds(this.children);
         }

      }
   }

   private boolean updateCachedBounds(List var1, int var2) {
      if (this.cachedBounds.isEmpty()) {
         this.createCachedBounds(var1);
         return true;
      } else {
         int var3 = 0;
         if (this.left == null || this.left.boundsChanged) {
            var3 |= 1;
         }

         if (this.top == null || this.top.boundsChanged) {
            var3 |= 2;
         }

         if (this.near == null || this.near.boundsChanged) {
            var3 |= 4;
         }

         if (this.right == null || this.right.boundsChanged) {
            var3 |= 8;
         }

         if (this.bottom == null || this.bottom.boundsChanged) {
            var3 |= 16;
         }

         if (this.far == null || this.far.boundsChanged) {
            var3 |= 32;
         }

         float var4 = this.cachedBounds.getMinX();
         float var5 = this.cachedBounds.getMinY();
         float var6 = this.cachedBounds.getMinZ();
         float var7 = this.cachedBounds.getMaxX();
         float var8 = this.cachedBounds.getMaxY();
         float var9 = this.cachedBounds.getMaxZ();

         for(int var10 = var1.size() - 1; var2 > 0; --var10) {
            Node var11 = (Node)var1.get(var10);
            if (var11.boundsChanged) {
               var11.boundsChanged = false;
               --var2;
               this.tmp = this.getChildTransformedBounds(var11, BaseTransform.IDENTITY_TRANSFORM, this.tmp);
               if (!this.tmp.isEmpty()) {
                  float var12 = this.tmp.getMinX();
                  float var13 = this.tmp.getMinY();
                  float var14 = this.tmp.getMinZ();
                  float var15 = this.tmp.getMaxX();
                  float var16 = this.tmp.getMaxY();
                  float var17 = this.tmp.getMaxZ();
                  if (var12 <= var4) {
                     var4 = var12;
                     this.left = var11;
                     var3 &= -2;
                  }

                  if (var13 <= var5) {
                     var5 = var13;
                     this.top = var11;
                     var3 &= -3;
                  }

                  if (var14 <= var6) {
                     var6 = var14;
                     this.near = var11;
                     var3 &= -5;
                  }

                  if (var15 >= var7) {
                     var7 = var15;
                     this.right = var11;
                     var3 &= -9;
                  }

                  if (var16 >= var8) {
                     var8 = var16;
                     this.bottom = var11;
                     var3 &= -17;
                  }

                  if (var17 >= var9) {
                     var9 = var17;
                     this.far = var11;
                     var3 &= -33;
                  }
               }
            }
         }

         if (var3 != 0) {
            return false;
         } else {
            this.cachedBounds = this.cachedBounds.deriveWithNewBounds(var4, var5, var6, var7, var8, var9);
            return true;
         }
      }
   }

   private void createCachedBounds(List var1) {
      int var8 = var1.size();

      int var9;
      Node var10;
      for(var9 = 0; var9 < var8; ++var9) {
         var10 = (Node)var1.get(var9);
         var10.boundsChanged = false;
         if (var10.isVisible()) {
            this.tmp = var10.getTransformedBounds(this.tmp, BaseTransform.IDENTITY_TRANSFORM);
            if (!this.tmp.isEmpty()) {
               this.left = this.top = this.near = this.right = this.bottom = this.far = var10;
               break;
            }
         }
      }

      if (var9 == var8) {
         this.left = this.top = this.near = this.right = this.bottom = this.far = null;
         this.cachedBounds.makeEmpty();
      } else {
         float var2 = this.tmp.getMinX();
         float var3 = this.tmp.getMinY();
         float var4 = this.tmp.getMinZ();
         float var5 = this.tmp.getMaxX();
         float var6 = this.tmp.getMaxY();
         float var7 = this.tmp.getMaxZ();
         ++var9;

         for(; var9 < var8; ++var9) {
            var10 = (Node)var1.get(var9);
            var10.boundsChanged = false;
            if (var10.isVisible()) {
               this.tmp = var10.getTransformedBounds(this.tmp, BaseTransform.IDENTITY_TRANSFORM);
               if (!this.tmp.isEmpty()) {
                  float var11 = this.tmp.getMinX();
                  float var12 = this.tmp.getMinY();
                  float var13 = this.tmp.getMinZ();
                  float var14 = this.tmp.getMaxX();
                  float var15 = this.tmp.getMaxY();
                  float var16 = this.tmp.getMaxZ();
                  if (var11 < var2) {
                     var2 = var11;
                     this.left = var10;
                  }

                  if (var12 < var3) {
                     var3 = var12;
                     this.top = var10;
                  }

                  if (var13 < var4) {
                     var4 = var13;
                     this.near = var10;
                  }

                  if (var14 > var5) {
                     var5 = var14;
                     this.right = var10;
                  }

                  if (var15 > var6) {
                     var6 = var15;
                     this.bottom = var10;
                  }

                  if (var16 > var7) {
                     var7 = var16;
                     this.far = var10;
                  }
               }
            }
         }

         this.cachedBounds = this.cachedBounds.deriveWithNewBounds(var2, var3, var4, var5, var6, var7);
      }
   }

   protected void updateBounds() {
      int var1 = 0;

      for(int var2 = this.children.size(); var1 < var2; ++var1) {
         ((Node)this.children.get(var1)).updateBounds();
      }

      super.updateBounds();
   }

   private BaseBounds getChildTransformedBounds(Node var1, BaseTransform var2, BaseBounds var3) {
      this.currentlyProcessedChild = var1;
      var3 = var1.getTransformedBounds(var3, var2);
      this.currentlyProcessedChild = null;
      return var3;
   }

   void childBoundsChanged(Node var1) {
      if (var1 != this.currentlyProcessedChild) {
         this.cachedBoundsInvalid = true;
         this.setChildDirty(var1, true);
         this.impl_geomChanged();
      }
   }

   void childVisibilityChanged(Node var1) {
      if (var1.isVisible()) {
         this.childIncluded(var1);
      } else {
         this.childExcluded(var1);
      }

      this.impl_geomChanged();
   }

   /** @deprecated */
   @Deprecated
   protected boolean impl_computeContains(double var1, double var3) {
      Point2D var5 = TempState.getInstance().point;
      int var6 = 0;

      for(int var7 = this.children.size(); var6 < var7; ++var6) {
         Node var8 = (Node)this.children.get(var6);
         var5.x = (float)var1;
         var5.y = (float)var3;

         try {
            var8.parentToLocal(var5);
         } catch (NoninvertibleTransformException var10) {
            continue;
         }

         if (var8.contains((double)var5.x, (double)var5.y)) {
            return true;
         }
      }

      return false;
   }

   /** @deprecated */
   @Deprecated
   public Object impl_processMXNode(MXNodeAlgorithm var1, MXNodeAlgorithmContext var2) {
      return var1.processContainerNode(this, var2);
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case CHILDREN:
            return this.getChildrenUnmodifiable();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   void releaseAccessible() {
      int var1 = 0;

      for(int var2 = this.children.size(); var1 < var2; ++var1) {
         Node var3 = (Node)this.children.get(var1);
         var3.releaseAccessible();
      }

      super.releaseAccessible();
   }

   List test_getRemoved() {
      return this.removed;
   }
}
