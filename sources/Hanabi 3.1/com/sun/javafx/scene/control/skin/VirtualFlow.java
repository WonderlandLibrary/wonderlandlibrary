package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.Logging;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.IndexedCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.Duration;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class VirtualFlow extends Region {
   private static final int MIN_SCROLLING_LINES_PER_PAGE = 8;
   private boolean touchDetected = false;
   private boolean mouseDown = false;
   private BooleanProperty vertical;
   private boolean pannable = true;
   private int cellCount;
   private double position;
   private double fixedCellSize = 0.0;
   private boolean fixedCellSizeEnabled = false;
   private Callback createCell;
   private double maxPrefBreadth;
   private double viewportBreadth;
   private double viewportLength;
   double lastWidth = -1.0;
   double lastHeight = -1.0;
   int lastCellCount = 0;
   boolean lastVertical;
   double lastPosition;
   double lastCellBreadth = -1.0;
   double lastCellLength = -1.0;
   final ArrayLinkedList cells = new ArrayLinkedList();
   final ArrayLinkedList pile = new ArrayLinkedList();
   IndexedCell accumCell;
   Group accumCellParent;
   final Group sheet;
   final ObservableList sheetChildren;
   private VirtualScrollBar hbar = new VirtualScrollBar(this);
   private VirtualScrollBar vbar = new VirtualScrollBar(this);
   ClippedContainer clipView;
   StackPane corner;
   private double lastX;
   private double lastY;
   private boolean isPanning = false;
   private final List privateCells = new ArrayList();
   private static final String NEW_CELL = "newcell";
   private boolean needsReconfigureCells = false;
   private boolean needsRecreateCells = false;
   private boolean needsRebuildCells = false;
   private boolean needsCellsLayout = false;
   private boolean sizeChanged = false;
   private final BitSet dirtyCells = new BitSet();
   private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987;
   Timeline sbTouchTimeline;
   KeyFrame sbTouchKF1;
   KeyFrame sbTouchKF2;
   private boolean needBreadthBar;
   private boolean needLengthBar;
   private boolean tempVisibility = false;

   public final void setVertical(boolean var1) {
      this.verticalProperty().set(var1);
   }

   public final boolean isVertical() {
      return this.vertical == null ? true : this.vertical.get();
   }

   public final BooleanProperty verticalProperty() {
      if (this.vertical == null) {
         this.vertical = new BooleanPropertyBase(true) {
            protected void invalidated() {
               VirtualFlow.this.pile.clear();
               VirtualFlow.this.sheetChildren.clear();
               VirtualFlow.this.cells.clear();
               VirtualFlow.this.lastWidth = VirtualFlow.this.lastHeight = -1.0;
               VirtualFlow.this.setMaxPrefBreadth(-1.0);
               VirtualFlow.this.setViewportBreadth(0.0);
               VirtualFlow.this.setViewportLength(0.0);
               VirtualFlow.this.lastPosition = 0.0;
               VirtualFlow.this.hbar.setValue(0.0);
               VirtualFlow.this.vbar.setValue(0.0);
               VirtualFlow.this.setPosition(0.0);
               VirtualFlow.this.setNeedsLayout(true);
               VirtualFlow.this.requestLayout();
            }

            public Object getBean() {
               return VirtualFlow.this;
            }

            public String getName() {
               return "vertical";
            }
         };
      }

      return this.vertical;
   }

   public boolean isPannable() {
      return this.pannable;
   }

   public void setPannable(boolean var1) {
      this.pannable = var1;
   }

   public int getCellCount() {
      return this.cellCount;
   }

   public void setCellCount(int var1) {
      int var2 = this.cellCount;
      this.cellCount = var1;
      boolean var3 = var2 != this.cellCount;
      if (var3) {
         VirtualScrollBar var4 = this.isVertical() ? this.vbar : this.hbar;
         var4.setMax((double)var1);
      }

      if (var3) {
         this.layoutChildren();
         this.sheetChildren.clear();
         Parent var5 = this.getParent();
         if (var5 != null) {
            var5.requestLayout();
         }
      }

   }

   public double getPosition() {
      return this.position;
   }

   public void setPosition(double var1) {
      boolean var3 = this.position != var1;
      this.position = com.sun.javafx.util.Utils.clamp(0.0, var1, 1.0);
      if (var3) {
         this.requestLayout();
      }

   }

   public void setFixedCellSize(double var1) {
      this.fixedCellSize = var1;
      this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
      this.needsCellsLayout = true;
      this.layoutChildren();
   }

   public Callback getCreateCell() {
      return this.createCell;
   }

   public void setCreateCell(Callback var1) {
      this.createCell = var1;
      if (this.createCell != null) {
         this.accumCell = null;
         this.setNeedsLayout(true);
         this.recreateCells();
         if (this.getParent() != null) {
            this.getParent().requestLayout();
         }
      }

   }

   protected final void setMaxPrefBreadth(double var1) {
      this.maxPrefBreadth = var1;
   }

   protected final double getMaxPrefBreadth() {
      return this.maxPrefBreadth;
   }

   protected final void setViewportBreadth(double var1) {
      this.viewportBreadth = var1;
   }

   protected final double getViewportBreadth() {
      return this.viewportBreadth;
   }

   void setViewportLength(double var1) {
      this.viewportLength = var1;
   }

   protected double getViewportLength() {
      return this.viewportLength;
   }

   protected List getCells() {
      return this.cells;
   }

   protected final VirtualScrollBar getHbar() {
      return this.hbar;
   }

   protected final VirtualScrollBar getVbar() {
      return this.vbar;
   }

   public VirtualFlow() {
      this.getStyleClass().add("virtual-flow");
      this.setId("virtual-flow");
      this.sheet = new Group();
      this.sheet.getStyleClass().add("sheet");
      this.sheet.setAutoSizeChildren(false);
      this.sheetChildren = this.sheet.getChildren();
      this.clipView = new ClippedContainer(this);
      this.clipView.setNode(this.sheet);
      this.getChildren().add(this.clipView);
      this.accumCellParent = new Group();
      this.accumCellParent.setVisible(false);
      this.getChildren().add(this.accumCellParent);
      EventDispatcher var1 = (var0, var1x) -> {
         return var0;
      };
      EventDispatcher var2 = this.hbar.getEventDispatcher();
      this.hbar.setEventDispatcher((var2x, var3x) -> {
         if (var2x.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent)var2x).isDirect()) {
            var3x = var3x.prepend(var1);
            var3x = var3x.prepend(var2);
            return var3x.dispatchEvent(var2x);
         } else {
            return var2.dispatchEvent(var2x, var3x);
         }
      });
      EventDispatcher var3 = this.vbar.getEventDispatcher();
      this.vbar.setEventDispatcher((var2x, var3x) -> {
         if (var2x.getEventType() == ScrollEvent.SCROLL && !((ScrollEvent)var2x).isDirect()) {
            var3x = var3x.prepend(var1);
            var3x = var3x.prepend(var3);
            return var3x.dispatchEvent(var2x);
         } else {
            return var3.dispatchEvent(var2x, var3x);
         }
      });
      this.setOnScroll(new EventHandler() {
         public void handle(ScrollEvent var1) {
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && !VirtualFlow.this.touchDetected && !VirtualFlow.this.mouseDown) {
               VirtualFlow.this.startSBReleasedAnimation();
            }

            double var2 = 0.0;
            double var4;
            if (VirtualFlow.this.isVertical()) {
               switch (var1.getTextDeltaYUnits()) {
                  case PAGES:
                     var2 = var1.getTextDeltaY() * VirtualFlow.this.lastHeight;
                     break;
                  case LINES:
                     if (VirtualFlow.this.fixedCellSizeEnabled) {
                        var4 = VirtualFlow.this.fixedCellSize;
                     } else {
                        IndexedCell var6 = (IndexedCell)VirtualFlow.this.cells.getLast();
                        var4 = (VirtualFlow.this.getCellPosition(var6) + VirtualFlow.this.getCellLength(var6) - VirtualFlow.this.getCellPosition((IndexedCell)VirtualFlow.this.cells.getFirst())) / (double)VirtualFlow.this.cells.size();
                     }

                     if (VirtualFlow.this.lastHeight / var4 < 8.0) {
                        var4 = VirtualFlow.this.lastHeight / 8.0;
                     }

                     var2 = var1.getTextDeltaY() * var4;
                     break;
                  case NONE:
                     var2 = var1.getDeltaY();
               }
            } else {
               switch (var1.getTextDeltaXUnits()) {
                  case CHARACTERS:
                  case NONE:
                     var4 = var1.getDeltaX();
                     double var9 = var1.getDeltaY();
                     var2 = Math.abs(var4) > Math.abs(var9) ? var4 : var9;
               }
            }

            if (var2 != 0.0) {
               var4 = VirtualFlow.this.adjustPixels(-var2);
               if (var4 != 0.0) {
                  var1.consume();
               }
            }

            VirtualScrollBar var10 = VirtualFlow.this.isVertical() ? VirtualFlow.this.hbar : VirtualFlow.this.vbar;
            if (VirtualFlow.this.needBreadthBar) {
               double var5 = VirtualFlow.this.isVertical() ? var1.getDeltaX() : var1.getDeltaY();
               if (var5 != 0.0) {
                  double var7 = var10.getValue() - var5;
                  if (var7 < var10.getMin()) {
                     var10.setValue(var10.getMin());
                  } else if (var7 > var10.getMax()) {
                     var10.setValue(var10.getMax());
                  } else {
                     var10.setValue(var7);
                  }

                  var1.consume();
               }
            }

         }
      });
      this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler() {
         public void handle(MouseEvent var1) {
            VirtualFlow.this.mouseDown = true;
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
               VirtualFlow.this.scrollBarOn();
            }

            if (VirtualFlow.this.isFocusTraversable()) {
               boolean var2 = true;
               Node var3 = VirtualFlow.this.getScene().getFocusOwner();
               if (var3 != null) {
                  for(Parent var4 = var3.getParent(); var4 != null; var4 = var4.getParent()) {
                     if (var4.equals(VirtualFlow.this)) {
                        var2 = false;
                        break;
                     }
                  }
               }

               if (var2) {
                  VirtualFlow.this.requestFocus();
               }
            }

            VirtualFlow.this.lastX = var1.getX();
            VirtualFlow.this.lastY = var1.getY();
            VirtualFlow.this.isPanning = !VirtualFlow.this.vbar.getBoundsInParent().contains(var1.getX(), var1.getY()) && !VirtualFlow.this.hbar.getBoundsInParent().contains(var1.getX(), var1.getY());
         }
      });
      this.addEventFilter(MouseEvent.MOUSE_RELEASED, (var1x) -> {
         this.mouseDown = false;
         if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            this.startSBReleasedAnimation();
         }

      });
      this.addEventFilter(MouseEvent.MOUSE_DRAGGED, (var1x) -> {
         if (BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            this.scrollBarOn();
         }

         if (this.isPanning && this.isPannable()) {
            double var2 = this.lastX - var1x.getX();
            double var4 = this.lastY - var1x.getY();
            double var6 = this.isVertical() ? var4 : var2;
            double var8 = this.adjustPixels(var6);
            if (var8 != 0.0) {
               if (this.isVertical()) {
                  this.lastY = var1x.getY();
               } else {
                  this.lastX = var1x.getX();
               }
            }

            double var10 = this.isVertical() ? var2 : var4;
            VirtualScrollBar var12 = this.isVertical() ? this.hbar : this.vbar;
            if (var12.isVisible()) {
               double var13 = var12.getValue() + var10;
               if (var13 < var12.getMin()) {
                  var12.setValue(var12.getMin());
               } else if (var13 > var12.getMax()) {
                  var12.setValue(var12.getMax());
               } else {
                  var12.setValue(var13);
                  if (this.isVertical()) {
                     this.lastX = var1x.getX();
                  } else {
                     this.lastY = var1x.getY();
                  }
               }
            }

         }
      });
      this.vbar.setOrientation(Orientation.VERTICAL);
      this.vbar.addEventHandler(MouseEvent.ANY, (var0) -> {
         var0.consume();
      });
      this.getChildren().add(this.vbar);
      this.hbar.setOrientation(Orientation.HORIZONTAL);
      this.hbar.addEventHandler(MouseEvent.ANY, (var0) -> {
         var0.consume();
      });
      this.getChildren().add(this.hbar);
      this.corner = new StackPane();
      this.corner.getStyleClass().setAll((Object[])("corner"));
      this.getChildren().add(this.corner);
      InvalidationListener var4 = (var1x) -> {
         this.updateHbar();
      };
      this.verticalProperty().addListener(var4);
      this.hbar.valueProperty().addListener(var4);
      this.hbar.visibleProperty().addListener(var4);
      ChangeListener var5 = (var1x, var2x, var3x) -> {
         this.clipView.setClipY(this.isVertical() ? 0.0 : this.vbar.getValue());
      };
      this.vbar.valueProperty().addListener(var5);
      super.heightProperty().addListener((var1x, var2x, var3x) -> {
         if (var2x.doubleValue() == 0.0 && var3x.doubleValue() > 0.0) {
            this.recreateCells();
         }

      });
      this.setOnTouchPressed((var1x) -> {
         this.touchDetected = true;
         this.scrollBarOn();
      });
      this.setOnTouchReleased((var1x) -> {
         this.touchDetected = false;
         this.startSBReleasedAnimation();
      });
      this.setImpl_traversalEngine(new ParentTraversalEngine(this, new Algorithm() {
         Node selectNextAfterIndex(int var1, TraversalContext var2) {
            while(true) {
               ++var1;
               IndexedCell var3;
               if ((var3 = VirtualFlow.this.getVisibleCell(var1)) != null) {
                  if (var3.isFocusTraversable()) {
                     return var3;
                  }

                  Node var4 = var2.selectFirstInParent(var3);
                  if (var4 == null) {
                     continue;
                  }

                  return var4;
               }

               return null;
            }
         }

         Node selectPreviousBeforeIndex(int var1, TraversalContext var2) {
            while(true) {
               --var1;
               IndexedCell var3;
               if ((var3 = VirtualFlow.this.getVisibleCell(var1)) != null) {
                  Node var4 = var2.selectLastInParent(var3);
                  if (var4 != null) {
                     return var4;
                  }

                  if (!var3.isFocusTraversable()) {
                     continue;
                  }

                  return var3;
               }

               return null;
            }
         }

         public Node select(Node var1, Direction var2, TraversalContext var3) {
            if (VirtualFlow.this.cells.isEmpty()) {
               return null;
            } else {
               IndexedCell var4;
               if (VirtualFlow.this.cells.contains(var1)) {
                  var4 = (IndexedCell)var1;
               } else {
                  var4 = this.findOwnerCell(var1);
                  Node var5 = var3.selectInSubtree(var4, var1, var2);
                  if (var5 != null) {
                     return var5;
                  }

                  if (var2 == Direction.NEXT) {
                     var2 = Direction.NEXT_IN_LINE;
                  }
               }

               int var7 = var4.getIndex();
               switch (var2) {
                  case PREVIOUS:
                     return this.selectPreviousBeforeIndex(var7, var3);
                  case NEXT:
                     Node var6 = var3.selectFirstInParent(var4);
                     if (var6 != null) {
                        return var6;
                     }
                  case NEXT_IN_LINE:
                     return this.selectNextAfterIndex(var7, var3);
                  default:
                     return null;
               }
            }
         }

         private IndexedCell findOwnerCell(Node var1) {
            Parent var2;
            for(var2 = var1.getParent(); !VirtualFlow.this.cells.contains(var2); var2 = var2.getParent()) {
            }

            return (IndexedCell)var2;
         }

         public Node selectFirst(TraversalContext var1) {
            IndexedCell var2 = (IndexedCell)VirtualFlow.this.cells.getFirst();
            if (var2 == null) {
               return null;
            } else if (var2.isFocusTraversable()) {
               return var2;
            } else {
               Node var3 = var1.selectFirstInParent(var2);
               return var3 != null ? var3 : this.selectNextAfterIndex(var2.getIndex(), var1);
            }
         }

         public Node selectLast(TraversalContext var1) {
            IndexedCell var2 = (IndexedCell)VirtualFlow.this.cells.getLast();
            if (var2 == null) {
               return null;
            } else {
               Node var3 = var1.selectLastInParent(var2);
               if (var3 != null) {
                  return var3;
               } else {
                  return (Node)(var2.isFocusTraversable() ? var2 : this.selectPreviousBeforeIndex(var2.getIndex(), var1));
               }
            }
         }
      }));
   }

   void updateHbar() {
      if (this.isVisible() && this.getScene() != null) {
         if (this.isVertical()) {
            if (this.hbar.isVisible()) {
               this.clipView.setClipX(this.hbar.getValue());
            } else {
               this.clipView.setClipX(0.0);
               this.hbar.setValue(0.0);
            }
         }

      }
   }

   public void requestLayout() {
      this.setNeedsLayout(true);
   }

   protected void layoutChildren() {
      int var1;
      int var2;
      if (this.needsRecreateCells) {
         this.lastWidth = -1.0;
         this.lastHeight = -1.0;
         this.releaseCell(this.accumCell);
         this.sheet.getChildren().clear();
         var1 = 0;

         for(var2 = this.cells.size(); var1 < var2; ++var1) {
            ((IndexedCell)this.cells.get(var1)).updateIndex(-1);
         }

         this.cells.clear();
         this.pile.clear();
         this.releaseAllPrivateCells();
      } else if (this.needsRebuildCells) {
         this.lastWidth = -1.0;
         this.lastHeight = -1.0;
         this.releaseCell(this.accumCell);

         for(var1 = 0; var1 < this.cells.size(); ++var1) {
            ((IndexedCell)this.cells.get(var1)).updateIndex(-1);
         }

         this.addAllToPile();
         this.releaseAllPrivateCells();
      } else if (this.needsReconfigureCells) {
         this.setMaxPrefBreadth(-1.0);
         this.lastWidth = -1.0;
         this.lastHeight = -1.0;
      }

      if (!this.dirtyCells.isEmpty()) {
         for(var2 = this.cells.size(); (var1 = this.dirtyCells.nextSetBit(0)) != -1 && var1 < var2; this.dirtyCells.clear(var1)) {
            IndexedCell var3 = (IndexedCell)this.cells.get(var1);
            if (var3 != null) {
               var3.requestLayout();
            }
         }

         this.setMaxPrefBreadth(-1.0);
         this.lastWidth = -1.0;
         this.lastHeight = -1.0;
      }

      boolean var21 = this.sizeChanged;
      boolean var22 = this.needsRebuildCells || this.needsRecreateCells || this.sizeChanged;
      this.needsRecreateCells = false;
      this.needsReconfigureCells = false;
      this.needsRebuildCells = false;
      this.sizeChanged = false;
      if (this.needsCellsLayout) {
         int var24 = 0;

         for(int var4 = this.cells.size(); var24 < var4; ++var24) {
            Cell var25 = (Cell)this.cells.get(var24);
            if (var25 != null) {
               var25.requestLayout();
            }
         }

         this.needsCellsLayout = false;
      } else {
         double var23 = this.getWidth();
         double var5 = this.getHeight();
         boolean var7 = this.isVertical();
         double var8 = this.getPosition();
         if (!(var23 <= 0.0) && !(var5 <= 0.0)) {
            boolean var10 = false;
            boolean var11 = false;
            if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && (this.tempVisibility && (!this.hbar.isVisible() || !this.vbar.isVisible()) || !this.tempVisibility && (this.hbar.isVisible() || this.vbar.isVisible()))) {
               var11 = true;
            }

            if (!var10) {
               for(int var12 = 0; var12 < this.cells.size(); ++var12) {
                  Cell var13 = (Cell)this.cells.get(var12);
                  var10 = var13.isNeedsLayout();
                  if (var10) {
                     break;
                  }
               }
            }

            IndexedCell var26 = this.getFirstVisibleCell();
            double var16;
            boolean var27;
            if (!var10 && !var11) {
               var27 = false;
               if (var26 != null) {
                  double var14 = this.getCellBreadth(var26);
                  var16 = this.getCellLength(var26);
                  var27 = var14 != this.lastCellBreadth || var16 != this.lastCellLength;
                  this.lastCellBreadth = var14;
                  this.lastCellLength = var16;
               }

               if (var23 == this.lastWidth && var5 == this.lastHeight && this.cellCount == this.lastCellCount && var7 == this.lastVertical && var8 == this.lastPosition && !var27) {
                  return;
               }
            }

            var27 = false;
            boolean var28 = var10 || var7 != this.lastVertical || this.cells.isEmpty() || this.getMaxPrefBreadth() == -1.0 || var8 != this.lastPosition || this.cellCount != this.lastCellCount || var21 || var7 && var5 < this.lastHeight || !var7 && var23 < this.lastWidth;
            int var18;
            double var19;
            if (!var28) {
               double var15 = this.getMaxPrefBreadth();
               boolean var17 = false;

               for(var18 = 0; var18 < this.cells.size(); ++var18) {
                  var19 = this.getCellBreadth((Cell)this.cells.get(var18));
                  if (var15 == var19) {
                     var17 = true;
                  } else if (var19 > var15) {
                     var28 = true;
                     break;
                  }
               }

               if (!var17) {
                  var28 = true;
               }
            }

            if (!var28 && (var7 && var5 > this.lastHeight || !var7 && var23 > this.lastWidth)) {
               var27 = true;
            }

            this.initViewport();
            int var29 = this.computeCurrentIndex();
            if (this.lastCellCount != this.cellCount) {
               if (var8 != 0.0 && var8 != 1.0) {
                  if (var29 >= this.cellCount) {
                     this.setPosition(1.0);
                  } else if (var26 != null) {
                     var16 = this.getCellPosition(var26);
                     var18 = this.getCellIndex(var26);
                     this.adjustPositionToIndex(var18);
                     var19 = -this.computeOffsetForCell(var18);
                     this.adjustByPixelAmount(var19 - var16);
                  }
               }

               var29 = this.computeCurrentIndex();
            }

            if (var28) {
               this.setMaxPrefBreadth(-1.0);
               this.addAllToPile();
               var16 = -this.computeViewportOffset(this.getPosition());
               this.addLeadingCells(var29, var16);
               this.addTrailingCells(true);
            } else if (var27) {
               this.addTrailingCells(true);
            }

            this.computeBarVisiblity();
            this.updateScrollBarsAndCells(var22);
            this.lastWidth = this.getWidth();
            this.lastHeight = this.getHeight();
            this.lastCellCount = this.getCellCount();
            this.lastVertical = this.isVertical();
            this.lastPosition = this.getPosition();
            this.cleanPile();
         } else {
            this.addAllToPile();
            this.lastWidth = var23;
            this.lastHeight = var5;
            this.hbar.setVisible(false);
            this.vbar.setVisible(false);
            this.corner.setVisible(false);
         }
      }
   }

   protected void addLeadingCells(int var1, double var2) {
      double var4 = var2;
      int var6 = var1;
      boolean var7 = true;
      IndexedCell var8 = null;
      if (var1 == this.cellCount && var2 == this.getViewportLength()) {
         var6 = var1 - 1;
         var7 = false;
      }

      while(var6 >= 0 && (var4 > 0.0 || var7)) {
         var8 = this.getAvailableCell(var6);
         this.setCellIndex(var8, var6);
         this.resizeCellSize(var8);
         this.cells.addFirst(var8);
         if (var7) {
            var7 = false;
         } else {
            var4 -= this.getCellLength(var8);
         }

         this.positionCell(var8, var4);
         this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth(var8)));
         var8.setVisible(true);
         --var6;
      }

      if (this.cells.size() > 0) {
         var8 = (IndexedCell)this.cells.getFirst();
         int var9 = this.getCellIndex(var8);
         double var10 = this.getCellPosition(var8);
         if (var9 == 0 && var10 > 0.0) {
            this.setPosition(0.0);
            var4 = 0.0;

            for(int var12 = 0; var12 < this.cells.size(); ++var12) {
               var8 = (IndexedCell)this.cells.get(var12);
               this.positionCell(var8, var4);
               var4 += this.getCellLength(var8);
            }
         }
      } else {
         this.vbar.setValue(0.0);
         this.hbar.setValue(0.0);
      }

   }

   protected boolean addTrailingCells(boolean var1) {
      if (this.cells.isEmpty()) {
         return false;
      } else {
         IndexedCell var2 = (IndexedCell)this.cells.getLast();
         double var3 = this.getCellPosition(var2) + this.getCellLength(var2);
         int var5 = this.getCellIndex(var2) + 1;
         boolean var6 = var5 <= this.cellCount;
         double var7 = this.getViewportLength();
         if (var3 < 0.0 && !var1) {
            return false;
         } else {
            IndexedCell var11;
            for(double var9 = var7 - var3; var3 < var7; ++var5) {
               if (var5 >= this.cellCount) {
                  if (var3 < var7) {
                     var6 = false;
                  }

                  if (!var1) {
                     return var6;
                  }

                  if ((double)var5 > var9) {
                     PlatformLogger var25 = Logging.getControlsLogger();
                     if (var25.isLoggable(Level.INFO)) {
                        if (var2 != null) {
                           var25.info("index exceeds maxCellCount. Check size calculations for " + var2.getClass());
                        } else {
                           var25.info("index exceeds maxCellCount");
                        }
                     }

                     return var6;
                  }
               }

               var11 = this.getAvailableCell(var5);
               this.setCellIndex(var11, var5);
               this.resizeCellSize(var11);
               this.cells.addLast(var11);
               this.positionCell(var11, var3);
               this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth(var11)));
               var3 += this.getCellLength(var11);
               var11.setVisible(true);
            }

            var11 = (IndexedCell)this.cells.getFirst();
            var5 = this.getCellIndex(var11);
            IndexedCell var12 = this.getLastVisibleCell();
            double var13 = this.getCellPosition(var11);
            double var15 = this.getCellPosition(var12) + this.getCellLength(var12);
            if ((var5 != 0 || var5 == 0 && var13 < 0.0) && var1 && var12 != null && this.getCellIndex(var12) == this.cellCount - 1 && var15 < var7) {
               double var17 = var15;
               double var19 = var7 - var15;

               while(var17 < var7 && var5 != 0 && -var13 < var19) {
                  --var5;
                  IndexedCell var21 = this.getAvailableCell(var5);
                  this.setCellIndex(var21, var5);
                  this.resizeCellSize(var21);
                  this.cells.addFirst(var21);
                  double var22 = this.getCellLength(var21);
                  var13 -= var22;
                  var17 += var22;
                  this.positionCell(var21, var13);
                  this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth(var21)));
                  var21.setVisible(true);
               }

               var11 = (IndexedCell)this.cells.getFirst();
               var13 = this.getCellPosition(var11);
               double var26 = var7 - var15;
               if (this.getCellIndex(var11) == 0 && var26 > -var13) {
                  var26 = -var13;
               }

               for(int var23 = 0; var23 < this.cells.size(); ++var23) {
                  IndexedCell var24 = (IndexedCell)this.cells.get(var23);
                  this.positionCell(var24, this.getCellPosition(var24) + var26);
               }

               var13 = this.getCellPosition(var11);
               if (this.getCellIndex(var11) == 0 && var13 == 0.0) {
                  this.setPosition(0.0);
               } else if (this.getPosition() != 1.0) {
                  this.setPosition(1.0);
               }
            }

            return var6;
         }
      }
   }

   private boolean computeBarVisiblity() {
      if (this.cells.isEmpty()) {
         this.needLengthBar = false;
         this.needBreadthBar = false;
         return true;
      } else {
         boolean var1 = this.isVertical();
         boolean var2 = false;
         VirtualScrollBar var3 = var1 ? this.hbar : this.vbar;
         VirtualScrollBar var4 = var1 ? this.vbar : this.hbar;
         double var5 = this.getViewportBreadth();
         int var7 = this.cells.size();

         for(int var8 = 0; var8 < 2; ++var8) {
            boolean var9 = this.getPosition() > 0.0 || this.cellCount > var7 || this.cellCount == var7 && this.getCellPosition((IndexedCell)this.cells.getLast()) + this.getCellLength((IndexedCell)this.cells.getLast()) > this.getViewportLength() || this.cellCount == var7 - 1 && var2 && this.needBreadthBar;
            if (var9 ^ this.needLengthBar) {
               this.needLengthBar = var9;
               var2 = true;
            }

            boolean var10 = this.maxPrefBreadth > var5;
            if (var10 ^ this.needBreadthBar) {
               this.needBreadthBar = var10;
               var2 = true;
            }
         }

         if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            this.updateViewportDimensions();
            var3.setVisible(this.needBreadthBar);
            var4.setVisible(this.needLengthBar);
         } else {
            var3.setVisible(this.needBreadthBar && this.tempVisibility);
            var4.setVisible(this.needLengthBar && this.tempVisibility);
         }

         return var2;
      }
   }

   private void updateViewportDimensions() {
      boolean var1 = this.isVertical();
      double var2 = this.snapSize(var1 ? this.hbar.prefHeight(-1.0) : this.vbar.prefWidth(-1.0));
      double var4 = this.snapSize(var1 ? this.vbar.prefWidth(-1.0) : this.hbar.prefHeight(-1.0));
      this.setViewportBreadth((var1 ? this.getWidth() : this.getHeight()) - (this.needLengthBar ? var4 : 0.0));
      this.setViewportLength((var1 ? this.getHeight() : this.getWidth()) - (this.needBreadthBar ? var2 : 0.0));
   }

   private void initViewport() {
      boolean var1 = this.isVertical();
      this.updateViewportDimensions();
      VirtualScrollBar var2 = var1 ? this.hbar : this.vbar;
      VirtualScrollBar var3 = var1 ? this.vbar : this.hbar;
      var2.setVirtual(false);
      var3.setVirtual(true);
   }

   protected void setWidth(double var1) {
      if (var1 != this.lastWidth) {
         super.setWidth(var1);
         this.sizeChanged = true;
         this.setNeedsLayout(true);
         this.requestLayout();
      }

   }

   protected void setHeight(double var1) {
      if (var1 != this.lastHeight) {
         super.setHeight(var1);
         this.sizeChanged = true;
         this.setNeedsLayout(true);
         this.requestLayout();
      }

   }

   private void updateScrollBarsAndCells(boolean var1) {
      boolean var2 = this.isVertical();
      VirtualScrollBar var3 = var2 ? this.hbar : this.vbar;
      VirtualScrollBar var4 = var2 ? this.vbar : this.hbar;
      this.fitCells();
      double var5;
      double var9;
      if (!this.cells.isEmpty()) {
         var5 = -this.computeViewportOffset(this.getPosition());
         int var7 = this.computeCurrentIndex() - ((IndexedCell)this.cells.getFirst()).getIndex();
         int var8 = this.cells.size();
         var9 = var5;

         int var11;
         IndexedCell var12;
         for(var11 = var7 - 1; var11 >= 0 && var11 < var8; --var11) {
            var12 = (IndexedCell)this.cells.get(var11);
            var9 -= this.getCellLength(var12);
            this.positionCell(var12, var9);
         }

         var9 = var5;

         for(var11 = var7; var11 >= 0 && var11 < var8; ++var11) {
            var12 = (IndexedCell)this.cells.get(var11);
            this.positionCell(var12, var9);
            var9 += this.getCellLength(var12);
         }
      }

      this.corner.setVisible(var3.isVisible() && var4.isVisible());
      var5 = 0.0;
      double var18 = (var2 ? this.getHeight() : this.getWidth()) - (var3.isVisible() ? var3.prefHeight(-1.0) : 0.0);
      var9 = this.getViewportBreadth();
      double var19 = this.getViewportLength();
      if (var3.isVisible()) {
         if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            if (var2) {
               this.hbar.resizeRelocate(0.0, var19, var9, this.hbar.prefHeight(var9));
            } else {
               this.vbar.resizeRelocate(var19, 0.0, this.vbar.prefWidth(var9), var9);
            }
         } else if (var2) {
            this.hbar.resizeRelocate(0.0, var19 - this.hbar.getHeight(), var9, this.hbar.prefHeight(var9));
         } else {
            this.vbar.resizeRelocate(var19 - this.vbar.getWidth(), 0.0, this.vbar.prefWidth(var9), var9);
         }

         if (this.getMaxPrefBreadth() != -1.0) {
            double var13 = Math.max(1.0, this.getMaxPrefBreadth() - var9);
            if (var13 != var3.getMax()) {
               var3.setMax(var13);
               double var15 = var3.getValue();
               boolean var17 = var15 != 0.0 && var13 == var15;
               if (var17 || var15 > var13) {
                  var3.setValue(var13);
               }

               var3.setVisibleAmount(var9 / this.getMaxPrefBreadth() * var13);
            }
         }
      }

      if (var1 && (var4.isVisible() || BehaviorSkinBase.IS_TOUCH_SUPPORTED)) {
         int var20 = 0;
         int var14 = 0;

         for(int var21 = this.cells.size(); var14 < var21; ++var14) {
            IndexedCell var16 = (IndexedCell)this.cells.get(var14);
            if (var16 != null && !var16.isEmpty()) {
               var5 += var2 ? var16.getHeight() : var16.getWidth();
               if (var5 > var18) {
                  break;
               }

               ++var20;
            }
         }

         var4.setMax(1.0);
         if (var20 == 0 && this.cellCount == 1) {
            var4.setVisibleAmount(var18 / var5);
         } else {
            var4.setVisibleAmount((double)((float)var20 / (float)this.cellCount));
         }
      }

      if (var4.isVisible()) {
         if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            if (var2) {
               this.vbar.resizeRelocate(var9, 0.0, this.vbar.prefWidth(var19), var19);
            } else {
               this.hbar.resizeRelocate(0.0, var9, var19, this.hbar.prefHeight(-1.0));
            }
         } else if (var2) {
            this.vbar.resizeRelocate(var9 - this.vbar.getWidth(), 0.0, this.vbar.prefWidth(var19), var19);
         } else {
            this.hbar.resizeRelocate(0.0, var9 - this.hbar.getHeight(), var19, this.hbar.prefHeight(-1.0));
         }
      }

      if (this.corner.isVisible()) {
         if (!BehaviorSkinBase.IS_TOUCH_SUPPORTED) {
            this.corner.resize(this.vbar.getWidth(), this.hbar.getHeight());
            this.corner.relocate(this.hbar.getLayoutX() + this.hbar.getWidth(), this.vbar.getLayoutY() + this.vbar.getHeight());
         } else {
            this.corner.resize(this.vbar.getWidth(), this.hbar.getHeight());
            this.corner.relocate(this.hbar.getLayoutX() + (this.hbar.getWidth() - this.vbar.getWidth()), this.vbar.getLayoutY() + (this.vbar.getHeight() - this.hbar.getHeight()));
            this.hbar.resize(this.hbar.getWidth() - this.vbar.getWidth(), this.hbar.getHeight());
            this.vbar.resize(this.vbar.getWidth(), this.vbar.getHeight() - this.hbar.getHeight());
         }
      }

      this.clipView.resize(this.snapSize(var2 ? var9 : var19), this.snapSize(var2 ? var19 : var9));
      if (this.getPosition() != var4.getValue()) {
         var4.setValue(this.getPosition());
      }

   }

   private void fitCells() {
      double var1 = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
      boolean var3 = this.isVertical();

      for(int var4 = 0; var4 < this.cells.size(); ++var4) {
         Cell var5 = (Cell)this.cells.get(var4);
         if (var3) {
            var5.resize(var1, var5.prefHeight(var1));
         } else {
            var5.resize(var5.prefWidth(var1), var1);
         }
      }

   }

   private void cull() {
      double var1 = this.getViewportLength();

      for(int var3 = this.cells.size() - 1; var3 >= 0; --var3) {
         IndexedCell var4 = (IndexedCell)this.cells.get(var3);
         double var5 = this.getCellLength(var4);
         double var7 = this.getCellPosition(var4);
         double var9 = var7 + var5;
         if (var7 >= var1 || var9 < 0.0) {
            this.addToPile((IndexedCell)this.cells.remove(var3));
         }
      }

   }

   protected int getCellIndex(IndexedCell var1) {
      return var1.getIndex();
   }

   public IndexedCell getCell(int var1) {
      if (!this.cells.isEmpty()) {
         IndexedCell var2 = this.getVisibleCell(var1);
         if (var2 != null) {
            return var2;
         }
      }

      for(int var4 = 0; var4 < this.pile.size(); ++var4) {
         IndexedCell var3 = (IndexedCell)this.pile.get(var4);
         if (this.getCellIndex(var3) == var1) {
            return var3;
         }
      }

      if (this.pile.size() > 0) {
         return (IndexedCell)this.pile.get(0);
      } else {
         if (this.accumCell == null) {
            Callback var5 = this.getCreateCell();
            if (var5 != null) {
               this.accumCell = (IndexedCell)var5.call(this);
               this.accumCell.getProperties().put("newcell", (Object)null);
               this.accumCellParent.getChildren().setAll((Object[])(this.accumCell));
               this.accumCell.setAccessibleRole(AccessibleRole.NODE);
               this.accumCell.getChildrenUnmodifiable().addListener((var1x) -> {
                  Iterator var2 = this.accumCell.getChildrenUnmodifiable().iterator();

                  while(var2.hasNext()) {
                     Node var3 = (Node)var2.next();
                     var3.setAccessibleRole(AccessibleRole.NODE);
                  }

               });
            }
         }

         this.setCellIndex(this.accumCell, var1);
         this.resizeCellSize(this.accumCell);
         return this.accumCell;
      }
   }

   private void releaseCell(IndexedCell var1) {
      if (this.accumCell != null && var1 == this.accumCell) {
         this.accumCell.updateIndex(-1);
      }

   }

   IndexedCell getPrivateCell(int var1) {
      IndexedCell var2 = null;
      if (!this.cells.isEmpty()) {
         var2 = this.getVisibleCell(var1);
         if (var2 != null) {
            var2.layout();
            return var2;
         }
      }

      if (var2 == null) {
         for(int var3 = 0; var3 < this.sheetChildren.size(); ++var3) {
            IndexedCell var4 = (IndexedCell)this.sheetChildren.get(var3);
            if (this.getCellIndex(var4) == var1) {
               return var4;
            }
         }
      }

      if (var2 == null) {
         Callback var5 = this.getCreateCell();
         if (var5 != null) {
            var2 = (IndexedCell)var5.call(this);
         }
      }

      if (var2 != null) {
         this.setCellIndex(var2, var1);
         this.resizeCellSize(var2);
         var2.setVisible(false);
         this.sheetChildren.add(var2);
         this.privateCells.add(var2);
      }

      return var2;
   }

   private void releaseAllPrivateCells() {
      this.sheetChildren.removeAll(this.privateCells);
   }

   protected double getCellLength(int var1) {
      if (this.fixedCellSizeEnabled) {
         return this.fixedCellSize;
      } else {
         IndexedCell var2 = this.getCell(var1);
         double var3 = this.getCellLength(var2);
         this.releaseCell(var2);
         return var3;
      }
   }

   protected double getCellBreadth(int var1) {
      IndexedCell var2 = this.getCell(var1);
      double var3 = this.getCellBreadth(var2);
      this.releaseCell(var2);
      return var3;
   }

   protected double getCellLength(IndexedCell var1) {
      if (var1 == null) {
         return 0.0;
      } else if (this.fixedCellSizeEnabled) {
         return this.fixedCellSize;
      } else {
         return this.isVertical() ? var1.getLayoutBounds().getHeight() : var1.getLayoutBounds().getWidth();
      }
   }

   protected double getCellBreadth(Cell var1) {
      return this.isVertical() ? var1.prefWidth(-1.0) : var1.prefHeight(-1.0);
   }

   protected double getCellPosition(IndexedCell var1) {
      if (var1 == null) {
         return 0.0;
      } else {
         return this.isVertical() ? var1.getLayoutY() : var1.getLayoutX();
      }
   }

   protected void positionCell(IndexedCell var1, double var2) {
      if (this.isVertical()) {
         var1.setLayoutX(0.0);
         var1.setLayoutY(this.snapSize(var2));
      } else {
         var1.setLayoutX(this.snapSize(var2));
         var1.setLayoutY(0.0);
      }

   }

   protected void resizeCellSize(IndexedCell var1) {
      if (var1 != null) {
         double var2;
         if (this.isVertical()) {
            var2 = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
            var1.resize(var2, this.fixedCellSizeEnabled ? this.fixedCellSize : Utils.boundedSize(var1.prefHeight(var2), var1.minHeight(var2), var1.maxHeight(var2)));
         } else {
            var2 = Math.max(this.getMaxPrefBreadth(), this.getViewportBreadth());
            var1.resize(this.fixedCellSizeEnabled ? this.fixedCellSize : Utils.boundedSize(var1.prefWidth(var2), var1.minWidth(var2), var1.maxWidth(var2)), var2);
         }

      }
   }

   protected void setCellIndex(IndexedCell var1, int var2) {
      assert var1 != null;

      var1.updateIndex(var2);
      if (var1.isNeedsLayout() && var1.getScene() != null || var1.getProperties().containsKey("newcell")) {
         var1.applyCss();
         var1.getProperties().remove("newcell");
      }

   }

   protected IndexedCell getAvailableCell(int var1) {
      IndexedCell var2 = null;
      int var3 = 0;

      int var4;
      for(var4 = this.pile.size(); var3 < var4; ++var3) {
         IndexedCell var5 = (IndexedCell)this.pile.get(var3);

         assert var5 != null;

         if (this.getCellIndex(var5) == var1) {
            var2 = var5;
            this.pile.remove(var3);
            break;
         }

         var2 = null;
      }

      if (var2 == null) {
         if (this.pile.size() > 0) {
            boolean var8 = (var1 & 1) == 0;
            var4 = 0;

            for(int var9 = this.pile.size(); var4 < var9; ++var4) {
               IndexedCell var6 = (IndexedCell)this.pile.get(var4);
               int var7 = this.getCellIndex(var6);
               if ((var7 & 1) == 0 && var8) {
                  var2 = var6;
                  this.pile.remove(var4);
                  break;
               }

               if ((var7 & 1) == 1 && !var8) {
                  var2 = var6;
                  this.pile.remove(var4);
                  break;
               }
            }

            if (var2 == null) {
               var2 = (IndexedCell)this.pile.removeFirst();
            }
         } else {
            var2 = (IndexedCell)this.getCreateCell().call(this);
            var2.getProperties().put("newcell", (Object)null);
         }
      }

      if (var2.getParent() == null) {
         this.sheetChildren.add(var2);
      }

      return var2;
   }

   protected void addAllToPile() {
      int var1 = 0;

      for(int var2 = this.cells.size(); var1 < var2; ++var1) {
         this.addToPile((IndexedCell)this.cells.removeFirst());
      }

   }

   private void addToPile(IndexedCell var1) {
      assert var1 != null;

      this.pile.addLast(var1);
   }

   private void cleanPile() {
      boolean var1 = false;
      int var2 = 0;

      for(int var3 = this.pile.size(); var2 < var3; ++var2) {
         IndexedCell var4 = (IndexedCell)this.pile.get(var2);
         var1 = var1 || this.doesCellContainFocus(var4);
         var4.setVisible(false);
      }

      if (var1) {
         this.requestFocus();
      }

   }

   private boolean doesCellContainFocus(Cell var1) {
      Scene var2 = var1.getScene();
      Node var3 = var2 == null ? null : var2.getFocusOwner();
      if (var3 != null) {
         if (var1.equals(var3)) {
            return true;
         }

         for(Parent var4 = var3.getParent(); var4 != null && !(var4 instanceof VirtualFlow); var4 = var4.getParent()) {
            if (var1.equals(var4)) {
               return true;
            }
         }
      }

      return false;
   }

   public IndexedCell getVisibleCell(int var1) {
      if (this.cells.isEmpty()) {
         return null;
      } else {
         IndexedCell var2 = (IndexedCell)this.cells.getLast();
         int var3 = this.getCellIndex(var2);
         if (var1 == var3) {
            return var2;
         } else {
            IndexedCell var4 = (IndexedCell)this.cells.getFirst();
            int var5 = this.getCellIndex(var4);
            if (var1 == var5) {
               return var4;
            } else {
               if (var1 > var5 && var1 < var3) {
                  IndexedCell var6 = (IndexedCell)this.cells.get(var1 - var5);
                  if (this.getCellIndex(var6) == var1) {
                     return var6;
                  }
               }

               return null;
            }
         }
      }
   }

   public IndexedCell getLastVisibleCell() {
      if (!this.cells.isEmpty() && !(this.getViewportLength() <= 0.0)) {
         for(int var2 = this.cells.size() - 1; var2 >= 0; --var2) {
            IndexedCell var1 = (IndexedCell)this.cells.get(var2);
            if (!var1.isEmpty()) {
               return var1;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public IndexedCell getFirstVisibleCell() {
      if (!this.cells.isEmpty() && !(this.getViewportLength() <= 0.0)) {
         IndexedCell var1 = (IndexedCell)this.cells.getFirst();
         return var1.isEmpty() ? null : var1;
      } else {
         return null;
      }
   }

   public IndexedCell getLastVisibleCellWithinViewPort() {
      if (!this.cells.isEmpty() && !(this.getViewportLength() <= 0.0)) {
         double var2 = this.getViewportLength();

         for(int var4 = this.cells.size() - 1; var4 >= 0; --var4) {
            IndexedCell var1 = (IndexedCell)this.cells.get(var4);
            if (!var1.isEmpty()) {
               double var5 = this.getCellPosition(var1);
               double var7 = var5 + this.getCellLength(var1);
               if (var7 <= var2 + 2.0) {
                  return var1;
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public IndexedCell getFirstVisibleCellWithinViewPort() {
      if (!this.cells.isEmpty() && !(this.getViewportLength() <= 0.0)) {
         for(int var2 = 0; var2 < this.cells.size(); ++var2) {
            IndexedCell var1 = (IndexedCell)this.cells.get(var2);
            if (!var1.isEmpty()) {
               double var3 = this.getCellPosition(var1);
               if (var3 >= 0.0) {
                  return var1;
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public void showAsFirst(IndexedCell var1) {
      if (var1 != null) {
         this.adjustPixels(this.getCellPosition(var1));
      }

   }

   public void showAsLast(IndexedCell var1) {
      if (var1 != null) {
         this.adjustPixels(this.getCellPosition(var1) + this.getCellLength(var1) - this.getViewportLength());
      }

   }

   public void show(IndexedCell var1) {
      if (var1 != null) {
         double var2 = this.getCellPosition(var1);
         double var4 = this.getCellLength(var1);
         double var6 = var2 + var4;
         double var8 = this.getViewportLength();
         if (var2 < 0.0) {
            this.adjustPixels(var2);
         } else if (var6 > var8) {
            this.adjustPixels(var6 - var8);
         }
      }

   }

   public void show(int var1) {
      IndexedCell var2 = this.getVisibleCell(var1);
      if (var2 != null) {
         this.show(var2);
      } else {
         IndexedCell var3 = this.getVisibleCell(var1 - 1);
         if (var3 != null) {
            var2 = this.getAvailableCell(var1);
            this.setCellIndex(var2, var1);
            this.resizeCellSize(var2);
            this.cells.addLast(var2);
            this.positionCell(var2, this.getCellPosition(var3) + this.getCellLength(var3));
            this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth(var2)));
            var2.setVisible(true);
            this.show(var2);
            return;
         }

         IndexedCell var4 = this.getVisibleCell(var1 + 1);
         if (var4 != null) {
            var2 = this.getAvailableCell(var1);
            this.setCellIndex(var2, var1);
            this.resizeCellSize(var2);
            this.cells.addFirst(var2);
            this.positionCell(var2, this.getCellPosition(var4) - this.getCellLength(var2));
            this.setMaxPrefBreadth(Math.max(this.getMaxPrefBreadth(), this.getCellBreadth(var2)));
            var2.setVisible(true);
            this.show(var2);
            return;
         }

         this.adjustPositionToIndex(var1);
         this.addAllToPile();
         this.requestLayout();
      }

   }

   public void scrollTo(int var1) {
      boolean var2 = false;
      if (var1 >= this.cellCount - 1) {
         this.setPosition(1.0);
         var2 = true;
      } else if (var1 < 0) {
         this.setPosition(0.0);
         var2 = true;
      }

      if (!var2) {
         this.adjustPositionToIndex(var1);
         double var3 = -this.computeOffsetForCell(var1);
         this.adjustByPixelAmount(var3);
      }

      this.requestLayout();
   }

   public void scrollToOffset(int var1) {
      this.adjustPixels((double)var1 * this.getCellLength(0));
   }

   public double adjustPixels(double var1) {
      if (var1 == 0.0) {
         return 0.0;
      } else {
         boolean var3 = this.isVertical();
         if (var3) {
            if (this.tempVisibility) {
               if (!this.needLengthBar) {
                  return 0.0;
               }
            } else if (!this.vbar.isVisible()) {
               return 0.0;
            }
         }

         if (!var3) {
            if (this.tempVisibility) {
               if (!this.needLengthBar) {
                  return 0.0;
               }
            } else if (!this.hbar.isVisible()) {
               return 0.0;
            }
         }

         double var4 = this.getPosition();
         if (var4 == 0.0 && var1 < 0.0) {
            return 0.0;
         } else if (var4 == 1.0 && var1 > 0.0) {
            return 0.0;
         } else {
            this.adjustByPixelAmount(var1);
            if (var4 == this.getPosition()) {
               return 0.0;
            } else {
               if (this.cells.size() > 0) {
                  for(int var6 = 0; var6 < this.cells.size(); ++var6) {
                     IndexedCell var7 = (IndexedCell)this.cells.get(var6);

                     assert var7 != null;

                     this.positionCell(var7, this.getCellPosition(var7) - var1);
                  }

                  IndexedCell var21 = (IndexedCell)this.cells.getFirst();
                  double var22 = var21 == null ? 0.0 : this.getCellPosition(var21);

                  int var9;
                  for(var9 = 0; var9 < this.cells.size(); ++var9) {
                     IndexedCell var10 = (IndexedCell)this.cells.get(var9);

                     assert var10 != null;

                     double var11 = this.getCellPosition(var10);
                     if (var11 != var22) {
                        this.positionCell(var10, var22);
                     }

                     var22 += this.getCellLength(var10);
                  }

                  this.cull();
                  var21 = (IndexedCell)this.cells.getFirst();
                  double var23;
                  if (var21 != null) {
                     var9 = this.getCellIndex(var21);
                     var23 = this.getCellLength(var9 - 1);
                     this.addLeadingCells(var9 - 1, this.getCellPosition(var21) - var23);
                  } else {
                     var9 = this.computeCurrentIndex();
                     var23 = -this.computeViewportOffset(this.getPosition());
                     this.addLeadingCells(var9, var23);
                  }

                  if (!this.addTrailingCells(false)) {
                     IndexedCell var24 = this.getLastVisibleCell();
                     var23 = this.getCellLength(var24);
                     double var12 = this.getCellPosition(var24) + var23;
                     double var14 = this.getViewportLength();
                     if (var12 < var14) {
                        double var16 = var14 - var12;

                        int var18;
                        for(var18 = 0; var18 < this.cells.size(); ++var18) {
                           IndexedCell var19 = (IndexedCell)this.cells.get(var18);
                           this.positionCell(var19, this.getCellPosition(var19) + var16);
                        }

                        this.setPosition(1.0);
                        var21 = (IndexedCell)this.cells.getFirst();
                        var18 = this.getCellIndex(var21);
                        double var25 = this.getCellLength(var18 - 1);
                        this.addLeadingCells(var18 - 1, this.getCellPosition(var21) - var25);
                     }
                  }
               }

               this.cull();
               this.updateScrollBarsAndCells(false);
               this.lastPosition = this.getPosition();
               return var1;
            }
         }
      }
   }

   public void reconfigureCells() {
      this.needsReconfigureCells = true;
      this.requestLayout();
   }

   public void recreateCells() {
      this.needsRecreateCells = true;
      this.requestLayout();
   }

   public void rebuildCells() {
      this.needsRebuildCells = true;
      this.requestLayout();
   }

   public void requestCellLayout() {
      this.needsCellsLayout = true;
      this.requestLayout();
   }

   public void setCellDirty(int var1) {
      this.dirtyCells.set(var1);
      this.requestLayout();
   }

   private double getPrefBreadth(double var1) {
      double var3 = this.getMaxCellWidth(10);
      if (var1 > -1.0) {
         double var5 = this.getPrefLength();
         var3 = Math.max(var3, var5 * 0.618033987);
      }

      return var3;
   }

   private double getPrefLength() {
      double var1 = 0.0;
      int var3 = Math.min(10, this.cellCount);

      for(int var4 = 0; var4 < var3; ++var4) {
         var1 += this.getCellLength(var4);
      }

      return var1;
   }

   protected double computePrefWidth(double var1) {
      double var3 = this.isVertical() ? this.getPrefBreadth(var1) : this.getPrefLength();
      return var3 + this.vbar.prefWidth(-1.0);
   }

   protected double computePrefHeight(double var1) {
      double var3 = this.isVertical() ? this.getPrefLength() : this.getPrefBreadth(var1);
      return var3 + this.hbar.prefHeight(-1.0);
   }

   double getMaxCellWidth(int var1) {
      double var2 = 0.0;
      int var4 = Math.max(1, var1 == -1 ? this.cellCount : var1);

      for(int var5 = 0; var5 < var4; ++var5) {
         var2 = Math.max(var2, this.getCellBreadth(var5));
      }

      return var2;
   }

   private double computeViewportOffset(double var1) {
      double var3 = com.sun.javafx.util.Utils.clamp(0.0, var1, 1.0);
      double var5 = var3 * (double)this.getCellCount();
      int var7 = (int)var5;
      double var8 = var5 - (double)var7;
      double var10 = this.getCellLength(var7);
      double var12 = var10 * var8;
      double var14 = this.getViewportLength() * var3;
      return var12 - var14;
   }

   private void adjustPositionToIndex(int var1) {
      int var2 = this.getCellCount();
      if (var2 <= 0) {
         this.setPosition(0.0);
      } else {
         this.setPosition((double)var1 / (double)var2);
      }

   }

   private void adjustByPixelAmount(double var1) {
      if (var1 != 0.0) {
         boolean var3 = var1 > 0.0;
         int var4 = this.getCellCount();
         double var5 = this.getPosition() * (double)var4;
         int var7 = (int)var5;
         if (!var3 || var7 != var4) {
            double var8 = this.getCellLength(var7);
            double var10 = var5 - (double)var7;
            double var12 = var8 * var10;
            double var14 = 1.0 / (double)var4;
            double var16 = this.computeOffsetForCell(var7);
            double var18 = var8 + this.computeOffsetForCell(var7 + 1);
            double var20 = var18 - var16;
            double var22 = var3 ? var1 + var12 - this.getViewportLength() * this.getPosition() - var16 : -var1 + var18 - (var12 - this.getViewportLength() * this.getPosition());

            double var24;
            for(var24 = var14 * (double)var7; var22 > var20 && (var3 && var7 < var4 - 1 || !var3 && var7 > 0); var24 = var14 * (double)var7) {
               if (var3) {
                  ++var7;
               } else {
                  --var7;
               }

               var22 -= var20;
               var8 = this.getCellLength(var7);
               var16 = this.computeOffsetForCell(var7);
               var18 = var8 + this.computeOffsetForCell(var7 + 1);
               var20 = var18 - var16;
            }

            if (var22 > var20) {
               this.setPosition(var3 ? 1.0 : 0.0);
            } else {
               double var26;
               if (var3) {
                  var26 = var14 / Math.abs(var18 - var16);
                  this.setPosition(var24 + var26 * var22);
               } else {
                  var26 = var14 / Math.abs(var18 - var16);
                  this.setPosition(var24 + var14 - var26 * var22);
               }
            }

         }
      }
   }

   private int computeCurrentIndex() {
      return (int)(this.getPosition() * (double)this.getCellCount());
   }

   private double computeOffsetForCell(int var1) {
      double var2 = (double)this.getCellCount();
      double var4 = com.sun.javafx.util.Utils.clamp(0.0, (double)var1, var2) / var2;
      return -(this.getViewportLength() * var4);
   }

   protected void startSBReleasedAnimation() {
      if (this.sbTouchTimeline == null) {
         this.sbTouchTimeline = new Timeline();
         this.sbTouchKF1 = new KeyFrame(Duration.millis(0.0), (var1) -> {
            this.tempVisibility = true;
            this.requestLayout();
         }, new KeyValue[0]);
         this.sbTouchKF2 = new KeyFrame(Duration.millis(1000.0), (var1) -> {
            if (!this.touchDetected && !this.mouseDown) {
               this.tempVisibility = false;
               this.requestLayout();
            }

         }, new KeyValue[0]);
         this.sbTouchTimeline.getKeyFrames().addAll(this.sbTouchKF1, this.sbTouchKF2);
      }

      this.sbTouchTimeline.playFromStart();
   }

   protected void scrollBarOn() {
      this.tempVisibility = true;
      this.requestLayout();
   }

   public static class ArrayLinkedList extends AbstractList {
      private final ArrayList array = new ArrayList(50);
      private int firstIndex = -1;
      private int lastIndex = -1;

      public ArrayLinkedList() {
         for(int var1 = 0; var1 < 50; ++var1) {
            this.array.add((Object)null);
         }

      }

      public Object getFirst() {
         return this.firstIndex == -1 ? null : this.array.get(this.firstIndex);
      }

      public Object getLast() {
         return this.lastIndex == -1 ? null : this.array.get(this.lastIndex);
      }

      public void addFirst(Object var1) {
         if (this.firstIndex == -1) {
            this.firstIndex = this.lastIndex = this.array.size() / 2;
            this.array.set(this.firstIndex, var1);
         } else if (this.firstIndex == 0) {
            this.array.add(0, var1);
            ++this.lastIndex;
         } else {
            this.array.set(--this.firstIndex, var1);
         }

      }

      public void addLast(Object var1) {
         if (this.firstIndex == -1) {
            this.firstIndex = this.lastIndex = this.array.size() / 2;
            this.array.set(this.lastIndex, var1);
         } else if (this.lastIndex == this.array.size() - 1) {
            this.array.add(++this.lastIndex, var1);
         } else {
            this.array.set(++this.lastIndex, var1);
         }

      }

      public int size() {
         return this.firstIndex == -1 ? 0 : this.lastIndex - this.firstIndex + 1;
      }

      public boolean isEmpty() {
         return this.firstIndex == -1;
      }

      public Object get(int var1) {
         return var1 <= this.lastIndex - this.firstIndex && var1 >= 0 ? this.array.get(this.firstIndex + var1) : null;
      }

      public void clear() {
         for(int var1 = 0; var1 < this.array.size(); ++var1) {
            this.array.set(var1, (Object)null);
         }

         this.firstIndex = this.lastIndex = -1;
      }

      public Object removeFirst() {
         return this.isEmpty() ? null : this.remove(0);
      }

      public Object removeLast() {
         return this.isEmpty() ? null : this.remove(this.lastIndex - this.firstIndex);
      }

      public Object remove(int var1) {
         if (var1 <= this.lastIndex - this.firstIndex && var1 >= 0) {
            Object var2;
            if (var1 == 0) {
               var2 = this.array.get(this.firstIndex);
               this.array.set(this.firstIndex, (Object)null);
               if (this.firstIndex == this.lastIndex) {
                  this.firstIndex = this.lastIndex = -1;
               } else {
                  ++this.firstIndex;
               }

               return var2;
            } else if (var1 == this.lastIndex - this.firstIndex) {
               var2 = this.array.get(this.lastIndex);
               this.array.set(this.lastIndex--, (Object)null);
               return var2;
            } else {
               var2 = this.array.get(this.firstIndex + var1);
               this.array.set(this.firstIndex + var1, (Object)null);

               for(int var3 = this.firstIndex + var1 + 1; var3 <= this.lastIndex; ++var3) {
                  this.array.set(var3 - 1, this.array.get(var3));
               }

               this.array.set(this.lastIndex--, (Object)null);
               return var2;
            }
         } else {
            throw new ArrayIndexOutOfBoundsException();
         }
      }
   }

   static class ClippedContainer extends Region {
      private Node node;
      private final Rectangle clipRect;

      public Node getNode() {
         return this.node;
      }

      public void setNode(Node var1) {
         this.node = var1;
         this.getChildren().clear();
         this.getChildren().add(this.node);
      }

      public void setClipX(double var1) {
         this.setLayoutX(-var1);
         this.clipRect.setLayoutX(var1);
      }

      public void setClipY(double var1) {
         this.setLayoutY(-var1);
         this.clipRect.setLayoutY(var1);
      }

      public ClippedContainer(VirtualFlow var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("VirtualFlow can not be null");
         } else {
            this.getStyleClass().add("clipped-container");
            this.clipRect = new Rectangle();
            this.clipRect.setSmooth(false);
            this.setClip(this.clipRect);
            super.widthProperty().addListener((var1x) -> {
               this.clipRect.setWidth(this.getWidth());
            });
            super.heightProperty().addListener((var1x) -> {
               this.clipRect.setHeight(this.getHeight());
            });
         }
      }
   }
}
