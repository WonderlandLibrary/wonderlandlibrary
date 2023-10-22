package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ToolBarBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ToolBarSkin extends BehaviorSkinBase {
   private Pane box;
   private ToolBarOverflowMenu overflowMenu;
   private boolean overflow = false;
   private double previousWidth = 0.0;
   private double previousHeight = 0.0;
   private double savedPrefWidth = 0.0;
   private double savedPrefHeight = 0.0;
   private ObservableList overflowMenuItems = FXCollections.observableArrayList();
   private boolean needsUpdate = false;
   private final ParentTraversalEngine engine;
   private DoubleProperty spacing;
   private ObjectProperty boxAlignment;

   public ToolBarSkin(ToolBar var1) {
      super(var1, new ToolBarBehavior(var1));
      this.initialize();
      this.registerChangeListener(var1.orientationProperty(), "ORIENTATION");
      this.engine = new ParentTraversalEngine(this.getSkinnable(), new Algorithm() {
         private Node selectPrev(int var1, TraversalContext var2) {
            for(int var3 = var1; var3 >= 0; --var3) {
               Node var4 = (Node)ToolBarSkin.this.box.getChildren().get(var3);
               if (!var4.isDisabled() && var4.impl_isTreeVisible()) {
                  if (var4 instanceof Parent) {
                     Node var5 = var2.selectLastInParent((Parent)var4);
                     if (var5 != null) {
                        return var5;
                     }
                  }

                  if (var4.isFocusTraversable()) {
                     return var4;
                  }
               }
            }

            return null;
         }

         private Node selectNext(int var1, TraversalContext var2) {
            int var3 = var1;

            for(int var4 = ToolBarSkin.this.box.getChildren().size(); var3 < var4; ++var3) {
               Node var5 = (Node)ToolBarSkin.this.box.getChildren().get(var3);
               if (!var5.isDisabled() && var5.impl_isTreeVisible()) {
                  if (var5.isFocusTraversable()) {
                     return var5;
                  }

                  if (var5 instanceof Parent) {
                     Node var6 = var2.selectFirstInParent((Parent)var5);
                     if (var6 != null) {
                        return var6;
                     }
                  }
               }
            }

            return null;
         }

         public Node select(Node var1, Direction var2, TraversalContext var3) {
            ObservableList var4 = ToolBarSkin.this.box.getChildren();
            if (var1 == ToolBarSkin.this.overflowMenu) {
               if (var2.isForward()) {
                  return null;
               }

               Node var5 = this.selectPrev(var4.size() - 1, var3);
               if (var5 != null) {
                  return var5;
               }
            }

            int var8 = var4.indexOf(var1);
            if (var8 < 0) {
               Parent var6;
               for(var6 = var1.getParent(); !var4.contains(var6); var6 = var6.getParent()) {
               }

               Node var7 = var3.selectInSubtree(var6, var1, var2);
               if (var7 != null) {
                  return var7;
               }

               var8 = var4.indexOf(var1);
               if (var2 == Direction.NEXT) {
                  var2 = Direction.NEXT_IN_LINE;
               }
            }

            if (var8 >= 0) {
               Node var9;
               if (var2.isForward()) {
                  var9 = this.selectNext(var8 + 1, var3);
                  if (var9 != null) {
                     return var9;
                  }

                  if (ToolBarSkin.this.overflow) {
                     ToolBarSkin.this.overflowMenu.requestFocus();
                     return ToolBarSkin.this.overflowMenu;
                  }
               } else {
                  var9 = this.selectPrev(var8 - 1, var3);
                  if (var9 != null) {
                     return var9;
                  }
               }
            }

            return null;
         }

         public Node selectFirst(TraversalContext var1) {
            Node var2 = this.selectNext(0, var1);
            if (var2 != null) {
               return var2;
            } else {
               return ToolBarSkin.this.overflow ? ToolBarSkin.this.overflowMenu : null;
            }
         }

         public Node selectLast(TraversalContext var1) {
            return (Node)(ToolBarSkin.this.overflow ? ToolBarSkin.this.overflowMenu : this.selectPrev(ToolBarSkin.this.box.getChildren().size() - 1, var1));
         }
      });
      ((ToolBar)this.getSkinnable()).setImpl_traversalEngine(this.engine);
      var1.focusedProperty().addListener((var1x, var2, var3) -> {
         if (var3) {
            if (!this.box.getChildren().isEmpty()) {
               ((Node)this.box.getChildren().get(0)).requestFocus();
            } else {
               this.overflowMenu.requestFocus();
            }
         }

      });
      var1.getItems().addListener((var1x) -> {
         while(var1x.next()) {
            Iterator var2 = var1x.getRemoved().iterator();

            while(var2.hasNext()) {
               Node var3 = (Node)var2.next();
               this.box.getChildren().remove(var3);
            }

            this.box.getChildren().addAll(var1x.getAddedSubList());
         }

         this.needsUpdate = true;
         ((ToolBar)this.getSkinnable()).requestLayout();
      });
   }

   public final void setSpacing(double var1) {
      this.spacingProperty().set(this.snapSpace(var1));
   }

   public final double getSpacing() {
      return this.spacing == null ? 0.0 : this.snapSpace(this.spacing.get());
   }

   public final DoubleProperty spacingProperty() {
      if (this.spacing == null) {
         this.spacing = new StyleableDoubleProperty() {
            protected void invalidated() {
               double var1 = this.get();
               if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                  ((VBox)ToolBarSkin.this.box).setSpacing(var1);
               } else {
                  ((HBox)ToolBarSkin.this.box).setSpacing(var1);
               }

            }

            public Object getBean() {
               return ToolBarSkin.this;
            }

            public String getName() {
               return "spacing";
            }

            public CssMetaData getCssMetaData() {
               return ToolBarSkin.StyleableProperties.SPACING;
            }
         };
      }

      return this.spacing;
   }

   public final void setBoxAlignment(Pos var1) {
      this.boxAlignmentProperty().set(var1);
   }

   public final Pos getBoxAlignment() {
      return this.boxAlignment == null ? Pos.TOP_LEFT : (Pos)this.boxAlignment.get();
   }

   public final ObjectProperty boxAlignmentProperty() {
      if (this.boxAlignment == null) {
         this.boxAlignment = new StyleableObjectProperty(Pos.TOP_LEFT) {
            public void invalidated() {
               Pos var1 = (Pos)this.get();
               if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                  ((VBox)ToolBarSkin.this.box).setAlignment(var1);
               } else {
                  ((HBox)ToolBarSkin.this.box).setAlignment(var1);
               }

            }

            public Object getBean() {
               return ToolBarSkin.this;
            }

            public String getName() {
               return "boxAlignment";
            }

            public CssMetaData getCssMetaData() {
               return ToolBarSkin.StyleableProperties.ALIGNMENT;
            }
         };
      }

      return this.boxAlignment;
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("ORIENTATION".equals(var1)) {
         this.initialize();
      }

   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      ToolBar var11 = (ToolBar)this.getSkinnable();
      return var11.getOrientation() == Orientation.VERTICAL ? this.computePrefWidth(-1.0, var3, var5, var7, var9) : this.snapSize(this.overflowMenu.prefWidth(-1.0)) + var9 + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      ToolBar var11 = (ToolBar)this.getSkinnable();
      return var11.getOrientation() == Orientation.VERTICAL ? this.snapSize(this.overflowMenu.prefHeight(-1.0)) + var3 + var7 : this.computePrefHeight(-1.0, var3, var5, var7, var9);
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      ToolBar var13 = (ToolBar)this.getSkinnable();
      Iterator var14;
      Node var15;
      if (var13.getOrientation() == Orientation.HORIZONTAL) {
         for(var14 = var13.getItems().iterator(); var14.hasNext(); var11 += this.snapSize(var15.prefWidth(-1.0)) + this.getSpacing()) {
            var15 = (Node)var14.next();
         }

         var11 -= this.getSpacing();
      } else {
         for(var14 = var13.getItems().iterator(); var14.hasNext(); var11 = Math.max(var11, this.snapSize(var15.prefWidth(-1.0)))) {
            var15 = (Node)var14.next();
         }

         if (var13.getItems().size() > 0) {
            this.savedPrefWidth = var11;
         } else {
            var11 = this.savedPrefWidth;
         }
      }

      return var9 + var11 + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      ToolBar var13 = (ToolBar)this.getSkinnable();
      Iterator var14;
      Node var15;
      if (var13.getOrientation() == Orientation.VERTICAL) {
         for(var14 = var13.getItems().iterator(); var14.hasNext(); var11 += this.snapSize(var15.prefHeight(-1.0)) + this.getSpacing()) {
            var15 = (Node)var14.next();
         }

         var11 -= this.getSpacing();
      } else {
         for(var14 = var13.getItems().iterator(); var14.hasNext(); var11 = Math.max(var11, this.snapSize(var15.prefHeight(-1.0)))) {
            var15 = (Node)var14.next();
         }

         if (var13.getItems().size() > 0) {
            this.savedPrefHeight = var11;
         } else {
            var11 = this.savedPrefHeight;
         }
      }

      return var3 + var11 + var7;
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? this.snapSize(((ToolBar)this.getSkinnable()).prefWidth(-1.0)) : Double.MAX_VALUE;
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL ? Double.MAX_VALUE : this.snapSize(((ToolBar)this.getSkinnable()).prefHeight(-1.0));
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      ToolBar var9 = (ToolBar)this.getSkinnable();
      if (var9.getOrientation() == Orientation.VERTICAL) {
         if (this.snapSize(var9.getHeight()) != this.previousHeight || this.needsUpdate) {
            ((VBox)this.box).setSpacing(this.getSpacing());
            ((VBox)this.box).setAlignment(this.getBoxAlignment());
            this.previousHeight = this.snapSize(var9.getHeight());
            this.addNodesToToolBar();
         }
      } else if (this.snapSize(var9.getWidth()) != this.previousWidth || this.needsUpdate) {
         ((HBox)this.box).setSpacing(this.getSpacing());
         ((HBox)this.box).setAlignment(this.getBoxAlignment());
         this.previousWidth = this.snapSize(var9.getWidth());
         this.addNodesToToolBar();
      }

      this.needsUpdate = false;
      double var10 = var5;
      double var12 = var7;
      if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
         var12 = var7 - (this.overflow ? this.snapSize(this.overflowMenu.prefHeight(-1.0)) : 0.0);
      } else {
         var10 = var5 - (this.overflow ? this.snapSize(this.overflowMenu.prefWidth(-1.0)) : 0.0);
      }

      this.box.resize(var10, var12);
      this.positionInArea(this.box, var1, var3, var10, var12, 0.0, HPos.CENTER, VPos.CENTER);
      if (this.overflow) {
         double var14 = this.snapSize(this.overflowMenu.prefWidth(-1.0));
         double var16 = this.snapSize(this.overflowMenu.prefHeight(-1.0));
         double var18;
         double var20;
         if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            if (var10 == 0.0) {
               var10 = this.savedPrefWidth;
            }

            HPos var22 = ((VBox)this.box).getAlignment().getHpos();
            if (HPos.LEFT.equals(var22)) {
               var18 = var1 + Math.abs((var10 - var14) / 2.0);
            } else if (HPos.RIGHT.equals(var22)) {
               var18 = this.snapSize(var9.getWidth()) - this.snappedRightInset() - var10 + Math.abs((var10 - var14) / 2.0);
            } else {
               var18 = var1 + Math.abs((this.snapSize(var9.getWidth()) - var1 + this.snappedRightInset() - var14) / 2.0);
            }

            var20 = this.snapSize(var9.getHeight()) - var16 - var3;
         } else {
            if (var12 == 0.0) {
               var12 = this.savedPrefHeight;
            }

            VPos var23 = ((HBox)this.box).getAlignment().getVpos();
            if (VPos.TOP.equals(var23)) {
               var20 = var3 + Math.abs((var12 - var16) / 2.0);
            } else if (VPos.BOTTOM.equals(var23)) {
               var20 = this.snapSize(var9.getHeight()) - this.snappedBottomInset() - var12 + Math.abs((var12 - var16) / 2.0);
            } else {
               var20 = var3 + Math.abs((var12 - var16) / 2.0);
            }

            var18 = this.snapSize(var9.getWidth()) - var14 - this.snappedRightInset();
         }

         this.overflowMenu.resize(var14, var16);
         this.positionInArea(this.overflowMenu, var18, var20, var14, var16, 0.0, HPos.CENTER, VPos.CENTER);
      }

   }

   private void initialize() {
      if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
         this.box = new VBox();
      } else {
         this.box = new HBox();
      }

      this.box.getStyleClass().add("container");
      this.box.getChildren().addAll(((ToolBar)this.getSkinnable()).getItems());
      this.overflowMenu = new ToolBarOverflowMenu(this.overflowMenuItems);
      this.overflowMenu.setVisible(false);
      this.overflowMenu.setManaged(false);
      this.getChildren().clear();
      this.getChildren().add(this.box);
      this.getChildren().add(this.overflowMenu);
      this.previousWidth = 0.0;
      this.previousHeight = 0.0;
      this.savedPrefWidth = 0.0;
      this.savedPrefHeight = 0.0;
      this.needsUpdate = true;
      ((ToolBar)this.getSkinnable()).requestLayout();
   }

   private void addNodesToToolBar() {
      ToolBar var1 = (ToolBar)this.getSkinnable();
      double var2 = 0.0;
      if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
         var2 = this.snapSize(var1.getHeight()) - this.snappedTopInset() - this.snappedBottomInset() + this.getSpacing();
      } else {
         var2 = this.snapSize(var1.getWidth()) - this.snappedLeftInset() - this.snappedRightInset() + this.getSpacing();
      }

      double var4 = 0.0;
      boolean var6 = false;
      Iterator var7 = ((ToolBar)this.getSkinnable()).getItems().iterator();

      Node var8;
      while(var7.hasNext()) {
         var8 = (Node)var7.next();
         if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            var4 += this.snapSize(var8.prefHeight(-1.0)) + this.getSpacing();
         } else {
            var4 += this.snapSize(var8.prefWidth(-1.0)) + this.getSpacing();
         }

         if (var4 > var2) {
            var6 = true;
            break;
         }
      }

      if (var6) {
         if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            var2 -= this.snapSize(this.overflowMenu.prefHeight(-1.0));
         } else {
            var2 -= this.snapSize(this.overflowMenu.prefWidth(-1.0));
         }

         var2 -= this.getSpacing();
      }

      var4 = 0.0;
      this.overflowMenuItems.clear();
      this.box.getChildren().clear();
      var7 = ((ToolBar)this.getSkinnable()).getItems().iterator();

      while(var7.hasNext()) {
         var8 = (Node)var7.next();
         var8.getStyleClass().remove("menu-item");
         var8.getStyleClass().remove("custom-menu-item");
         if (((ToolBar)this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            var4 += this.snapSize(var8.prefHeight(-1.0)) + this.getSpacing();
         } else {
            var4 += this.snapSize(var8.prefWidth(-1.0)) + this.getSpacing();
         }

         if (var4 <= var2) {
            this.box.getChildren().add(var8);
         } else {
            if (var8.isFocused()) {
               if (!this.box.getChildren().isEmpty()) {
                  Node var9 = this.engine.selectLast();
                  if (var9 != null) {
                     var9.requestFocus();
                  }
               } else {
                  this.overflowMenu.requestFocus();
               }
            }

            if (var8 instanceof Separator) {
               this.overflowMenuItems.add(new SeparatorMenuItem());
            } else {
               CustomMenuItem var14 = new CustomMenuItem(var8);
               switch (var8.getTypeSelector()) {
                  case "Button":
                  case "Hyperlink":
                  case "Label":
                     var14.setHideOnClick(true);
                     break;
                  case "CheckBox":
                  case "ChoiceBox":
                  case "ColorPicker":
                  case "ComboBox":
                  case "DatePicker":
                  case "MenuButton":
                  case "PasswordField":
                  case "RadioButton":
                  case "ScrollBar":
                  case "ScrollPane":
                  case "Slider":
                  case "SplitMenuButton":
                  case "SplitPane":
                  case "TextArea":
                  case "TextField":
                  case "ToggleButton":
                  case "ToolBar":
                     var14.setHideOnClick(false);
               }

               this.overflowMenuItems.add(var14);
            }
         }
      }

      this.overflow = this.overflowMenuItems.size() > 0;
      if (!this.overflow && this.overflowMenu.isFocused()) {
         Node var13 = this.engine.selectLast();
         if (var13 != null) {
            var13.requestFocus();
         }
      }

      this.overflowMenu.setVisible(this.overflow);
      this.overflowMenu.setManaged(this.overflow);
   }

   public static List getClassCssMetaData() {
      return ToolBarSkin.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case OVERFLOW_BUTTON:
            return this.overflowMenu;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   protected void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case SHOW_MENU:
            this.overflowMenu.fire();
            break;
         default:
            super.executeAccessibleAction(var1, var2);
      }

   }

   private static class StyleableProperties {
      private static final CssMetaData SPACING = new CssMetaData("-fx-spacing", SizeConverter.getInstance(), 0.0) {
         public boolean isSettable(ToolBar var1) {
            ToolBarSkin var2 = (ToolBarSkin)var1.getSkin();
            return var2.spacing == null || !var2.spacing.isBound();
         }

         public StyleableProperty getStyleableProperty(ToolBar var1) {
            ToolBarSkin var2 = (ToolBarSkin)var1.getSkin();
            return (StyleableProperty)var2.spacingProperty();
         }
      };
      private static final CssMetaData ALIGNMENT;
      private static final List STYLEABLES;

      static {
         ALIGNMENT = new CssMetaData("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) {
            public boolean isSettable(ToolBar var1) {
               ToolBarSkin var2 = (ToolBarSkin)var1.getSkin();
               return var2.boxAlignment == null || !var2.boxAlignment.isBound();
            }

            public StyleableProperty getStyleableProperty(ToolBar var1) {
               ToolBarSkin var2 = (ToolBarSkin)var1.getSkin();
               return (StyleableProperty)var2.boxAlignmentProperty();
            }
         };
         ArrayList var0 = new ArrayList(SkinBase.getClassCssMetaData());
         String var1 = ALIGNMENT.getProperty();
         int var2 = 0;

         for(int var3 = var0.size(); var2 < var3; ++var2) {
            CssMetaData var4 = (CssMetaData)var0.get(var2);
            if (var1.equals(var4.getProperty())) {
               var0.remove(var4);
            }
         }

         var0.add(SPACING);
         var0.add(ALIGNMENT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   class ToolBarOverflowMenu extends StackPane {
      private StackPane downArrow;
      private ContextMenu popup;
      private ObservableList menuItems;

      public ToolBarOverflowMenu(ObservableList var2) {
         this.getStyleClass().setAll((Object[])("tool-bar-overflow-button"));
         this.setAccessibleRole(AccessibleRole.BUTTON);
         this.setAccessibleText(ControlResources.getString("Accessibility.title.ToolBar.OverflowButton"));
         this.setFocusTraversable(true);
         this.menuItems = var2;
         this.downArrow = new StackPane();
         this.downArrow.getStyleClass().setAll((Object[])("arrow"));
         this.downArrow.setOnMousePressed((var1x) -> {
            this.fire();
         });
         this.setOnKeyPressed((var1x) -> {
            if (KeyCode.SPACE.equals(var1x.getCode())) {
               if (!this.popup.isShowing()) {
                  this.popup.getItems().clear();
                  this.popup.getItems().addAll(this.menuItems);
                  this.popup.show(this.downArrow, Side.BOTTOM, 0.0, 0.0);
               }

               var1x.consume();
            } else if (KeyCode.ESCAPE.equals(var1x.getCode())) {
               if (this.popup.isShowing()) {
                  this.popup.hide();
               }

               var1x.consume();
            } else if (KeyCode.ENTER.equals(var1x.getCode())) {
               this.fire();
               var1x.consume();
            }

         });
         this.visibleProperty().addListener((var1x, var2x, var3) -> {
            if (var3 && ToolBarSkin.this.box.getChildren().isEmpty()) {
               this.setFocusTraversable(true);
            }

         });
         this.popup = new ContextMenu();
         this.setVisible(false);
         this.setManaged(false);
         this.getChildren().add(this.downArrow);
      }

      private void fire() {
         if (this.popup.isShowing()) {
            this.popup.hide();
         } else {
            this.popup.getItems().clear();
            this.popup.getItems().addAll(this.menuItems);
            this.popup.show(this.downArrow, Side.BOTTOM, 0.0, 0.0);
         }

      }

      protected double computePrefWidth(double var1) {
         return this.snappedLeftInset() + this.snappedRightInset();
      }

      protected double computePrefHeight(double var1) {
         return this.snappedTopInset() + this.snappedBottomInset();
      }

      protected void layoutChildren() {
         double var1 = this.snapSize(this.downArrow.prefWidth(-1.0));
         double var3 = this.snapSize(this.downArrow.prefHeight(-1.0));
         double var5 = (this.snapSize(this.getWidth()) - var1) / 2.0;
         double var7 = (this.snapSize(this.getHeight()) - var3) / 2.0;
         if (((ToolBar)ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            this.downArrow.setRotate(0.0);
         }

         this.downArrow.resize(var1, var3);
         this.positionInArea(this.downArrow, var5, var7, var1, var3, 0.0, HPos.CENTER, VPos.CENTER);
      }

      public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
         switch (var1) {
            case FIRE:
               this.fire();
               break;
            default:
               super.executeAccessibleAction(var1, new Object[0]);
         }

      }
   }
}
