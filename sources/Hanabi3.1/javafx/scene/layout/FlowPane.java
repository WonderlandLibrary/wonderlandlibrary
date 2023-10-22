package javafx.scene.layout;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.util.Callback;

public class FlowPane extends Pane {
   private static final String MARGIN_CONSTRAINT = "flowpane-margin";
   private static final Callback marginAccessor = (var0) -> {
      return getMargin(var0);
   };
   private ObjectProperty orientation;
   private DoubleProperty hgap;
   private DoubleProperty vgap;
   private DoubleProperty prefWrapLength;
   private ObjectProperty alignment;
   private ObjectProperty columnHalignment;
   private ObjectProperty rowValignment;
   private List runs;
   private double lastMaxRunLength;
   boolean computingRuns;

   public static void setMargin(Node var0, Insets var1) {
      setConstraint(var0, "flowpane-margin", var1);
   }

   public static Insets getMargin(Node var0) {
      return (Insets)getConstraint(var0, "flowpane-margin");
   }

   public static void clearConstraints(Node var0) {
      setMargin(var0, (Insets)null);
   }

   public FlowPane() {
      this.runs = null;
      this.lastMaxRunLength = -1.0;
      this.computingRuns = false;
   }

   public FlowPane(Orientation var1) {
      this();
      this.setOrientation(var1);
   }

   public FlowPane(double var1, double var3) {
      this();
      this.setHgap(var1);
      this.setVgap(var3);
   }

   public FlowPane(Orientation var1, double var2, double var4) {
      this();
      this.setOrientation(var1);
      this.setHgap(var2);
      this.setVgap(var4);
   }

   public FlowPane(Node... var1) {
      this.runs = null;
      this.lastMaxRunLength = -1.0;
      this.computingRuns = false;
      this.getChildren().addAll(var1);
   }

   public FlowPane(Orientation var1, Node... var2) {
      this();
      this.setOrientation(var1);
      this.getChildren().addAll(var2);
   }

   public FlowPane(double var1, double var3, Node... var5) {
      this();
      this.setHgap(var1);
      this.setVgap(var3);
      this.getChildren().addAll(var5);
   }

   public FlowPane(Orientation var1, double var2, double var4, Node... var6) {
      this();
      this.setOrientation(var1);
      this.setHgap(var2);
      this.setVgap(var4);
      this.getChildren().addAll(var6);
   }

   public final ObjectProperty orientationProperty() {
      if (this.orientation == null) {
         this.orientation = new StyleableObjectProperty(Orientation.HORIZONTAL) {
            public void invalidated() {
               FlowPane.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return FlowPane.StyleableProperties.ORIENTATION;
            }

            public Object getBean() {
               return FlowPane.this;
            }

            public String getName() {
               return "orientation";
            }
         };
      }

      return this.orientation;
   }

   public final void setOrientation(Orientation var1) {
      this.orientationProperty().set(var1);
   }

   public final Orientation getOrientation() {
      return this.orientation == null ? Orientation.HORIZONTAL : (Orientation)this.orientation.get();
   }

   public final DoubleProperty hgapProperty() {
      if (this.hgap == null) {
         this.hgap = new StyleableDoubleProperty() {
            public void invalidated() {
               FlowPane.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return FlowPane.StyleableProperties.HGAP;
            }

            public Object getBean() {
               return FlowPane.this;
            }

            public String getName() {
               return "hgap";
            }
         };
      }

      return this.hgap;
   }

   public final void setHgap(double var1) {
      this.hgapProperty().set(var1);
   }

   public final double getHgap() {
      return this.hgap == null ? 0.0 : this.hgap.get();
   }

   public final DoubleProperty vgapProperty() {
      if (this.vgap == null) {
         this.vgap = new StyleableDoubleProperty() {
            public void invalidated() {
               FlowPane.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return FlowPane.StyleableProperties.VGAP;
            }

            public Object getBean() {
               return FlowPane.this;
            }

            public String getName() {
               return "vgap";
            }
         };
      }

      return this.vgap;
   }

   public final void setVgap(double var1) {
      this.vgapProperty().set(var1);
   }

   public final double getVgap() {
      return this.vgap == null ? 0.0 : this.vgap.get();
   }

   public final DoubleProperty prefWrapLengthProperty() {
      if (this.prefWrapLength == null) {
         this.prefWrapLength = new DoublePropertyBase(400.0) {
            protected void invalidated() {
               FlowPane.this.requestLayout();
            }

            public Object getBean() {
               return FlowPane.this;
            }

            public String getName() {
               return "prefWrapLength";
            }
         };
      }

      return this.prefWrapLength;
   }

   public final void setPrefWrapLength(double var1) {
      this.prefWrapLengthProperty().set(var1);
   }

   public final double getPrefWrapLength() {
      return this.prefWrapLength == null ? 400.0 : this.prefWrapLength.get();
   }

   public final ObjectProperty alignmentProperty() {
      if (this.alignment == null) {
         this.alignment = new StyleableObjectProperty(Pos.TOP_LEFT) {
            public void invalidated() {
               FlowPane.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return FlowPane.StyleableProperties.ALIGNMENT;
            }

            public Object getBean() {
               return FlowPane.this;
            }

            public String getName() {
               return "alignment";
            }
         };
      }

      return this.alignment;
   }

   public final void setAlignment(Pos var1) {
      this.alignmentProperty().set(var1);
   }

   public final Pos getAlignment() {
      return this.alignment == null ? Pos.TOP_LEFT : (Pos)this.alignment.get();
   }

   private Pos getAlignmentInternal() {
      Pos var1 = this.getAlignment();
      return var1 == null ? Pos.TOP_LEFT : var1;
   }

   public final ObjectProperty columnHalignmentProperty() {
      if (this.columnHalignment == null) {
         this.columnHalignment = new StyleableObjectProperty(HPos.LEFT) {
            public void invalidated() {
               FlowPane.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return FlowPane.StyleableProperties.COLUMN_HALIGNMENT;
            }

            public Object getBean() {
               return FlowPane.this;
            }

            public String getName() {
               return "columnHalignment";
            }
         };
      }

      return this.columnHalignment;
   }

   public final void setColumnHalignment(HPos var1) {
      this.columnHalignmentProperty().set(var1);
   }

   public final HPos getColumnHalignment() {
      return this.columnHalignment == null ? HPos.LEFT : (HPos)this.columnHalignment.get();
   }

   private HPos getColumnHalignmentInternal() {
      HPos var1 = this.getColumnHalignment();
      return var1 == null ? HPos.LEFT : var1;
   }

   public final ObjectProperty rowValignmentProperty() {
      if (this.rowValignment == null) {
         this.rowValignment = new StyleableObjectProperty(VPos.CENTER) {
            public void invalidated() {
               FlowPane.this.requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return FlowPane.StyleableProperties.ROW_VALIGNMENT;
            }

            public Object getBean() {
               return FlowPane.this;
            }

            public String getName() {
               return "rowValignment";
            }
         };
      }

      return this.rowValignment;
   }

   public final void setRowValignment(VPos var1) {
      this.rowValignmentProperty().set(var1);
   }

   public final VPos getRowValignment() {
      return this.rowValignment == null ? VPos.CENTER : (VPos)this.rowValignment.get();
   }

   private VPos getRowValignmentInternal() {
      VPos var1 = this.getRowValignment();
      return var1 == null ? VPos.CENTER : var1;
   }

   public Orientation getContentBias() {
      return this.getOrientation();
   }

   protected double computeMinWidth(double var1) {
      if (this.getContentBias() == Orientation.HORIZONTAL) {
         double var3 = 0.0;
         ObservableList var5 = this.getChildren();
         int var6 = 0;

         for(int var7 = var5.size(); var6 < var7; ++var6) {
            Node var8 = (Node)var5.get(var6);
            if (var8.isManaged()) {
               var3 = Math.max(var3, var8.prefWidth(-1.0));
            }
         }

         Insets var9 = this.getInsets();
         return var9.getLeft() + this.snapSize(var3) + var9.getRight();
      } else {
         return this.computePrefWidth(var1);
      }
   }

   protected double computeMinHeight(double var1) {
      if (this.getContentBias() == Orientation.VERTICAL) {
         double var3 = 0.0;
         ObservableList var5 = this.getChildren();
         int var6 = 0;

         for(int var7 = var5.size(); var6 < var7; ++var6) {
            Node var8 = (Node)var5.get(var6);
            if (var8.isManaged()) {
               var3 = Math.max(var3, var8.prefHeight(-1.0));
            }
         }

         Insets var9 = this.getInsets();
         return var9.getTop() + this.snapSize(var3) + var9.getBottom();
      } else {
         return this.computePrefHeight(var1);
      }
   }

   protected double computePrefWidth(double var1) {
      Insets var3 = this.getInsets();
      double var4;
      List var6;
      if (this.getOrientation() == Orientation.HORIZONTAL) {
         var4 = this.getPrefWrapLength();
         var6 = this.getRuns(var4);
         double var7 = this.computeContentWidth(var6);
         var7 = this.getPrefWrapLength() > var7 ? this.getPrefWrapLength() : var7;
         return var3.getLeft() + this.snapSize(var7) + var3.getRight();
      } else {
         var4 = var1 != -1.0 ? var1 - var3.getTop() - var3.getBottom() : this.getPrefWrapLength();
         var6 = this.getRuns(var4);
         return var3.getLeft() + this.computeContentWidth(var6) + var3.getRight();
      }
   }

   protected double computePrefHeight(double var1) {
      Insets var3 = this.getInsets();
      double var4;
      List var6;
      if (this.getOrientation() == Orientation.HORIZONTAL) {
         var4 = var1 != -1.0 ? var1 - var3.getLeft() - var3.getRight() : this.getPrefWrapLength();
         var6 = this.getRuns(var4);
         return var3.getTop() + this.computeContentHeight(var6) + var3.getBottom();
      } else {
         var4 = this.getPrefWrapLength();
         var6 = this.getRuns(var4);
         double var7 = this.computeContentHeight(var6);
         var7 = this.getPrefWrapLength() > var7 ? this.getPrefWrapLength() : var7;
         return var3.getTop() + this.snapSize(var7) + var3.getBottom();
      }
   }

   public void requestLayout() {
      if (!this.computingRuns) {
         this.runs = null;
      }

      super.requestLayout();
   }

   private List getRuns(double var1) {
      if (this.runs == null || var1 != this.lastMaxRunLength) {
         this.computingRuns = true;
         this.lastMaxRunLength = var1;
         this.runs = new ArrayList();
         double var3 = 0.0;
         double var5 = 0.0;
         Run var7 = new Run();
         double var8 = this.snapSpace(this.getVgap());
         double var10 = this.snapSpace(this.getHgap());
         ObservableList var12 = this.getChildren();
         int var13 = 0;

         for(int var14 = var12.size(); var13 < var14; ++var13) {
            Node var15 = (Node)var12.get(var13);
            if (var15.isManaged()) {
               LayoutRect var16 = new LayoutRect();
               var16.node = var15;
               Insets var17 = getMargin(var15);
               var16.width = this.computeChildPrefAreaWidth(var15, var17);
               var16.height = this.computeChildPrefAreaHeight(var15, var17);
               double var18 = this.getOrientation() == Orientation.HORIZONTAL ? var16.width : var16.height;
               if (var3 + var18 > var1 && var3 > 0.0) {
                  this.normalizeRun(var7, var5);
                  if (this.getOrientation() == Orientation.HORIZONTAL) {
                     var5 += var7.height + var8;
                  } else {
                     var5 += var7.width + var10;
                  }

                  this.runs.add(var7);
                  var3 = 0.0;
                  var7 = new Run();
               }

               if (this.getOrientation() == Orientation.HORIZONTAL) {
                  var16.x = var3;
                  var3 += var16.width + var10;
               } else {
                  var16.y = var3;
                  var3 += var16.height + var8;
               }

               var7.rects.add(var16);
            }
         }

         this.normalizeRun(var7, var5);
         this.runs.add(var7);
         this.computingRuns = false;
      }

      return this.runs;
   }

   private void normalizeRun(Run var1, double var2) {
      int var6;
      if (this.getOrientation() == Orientation.HORIZONTAL) {
         ArrayList var4 = new ArrayList();
         var1.width = (double)(var1.rects.size() - 1) * this.snapSpace(this.getHgap());
         int var5 = 0;

         for(var6 = var1.rects.size(); var5 < var6; ++var5) {
            LayoutRect var7 = (LayoutRect)var1.rects.get(var5);
            var4.add(var7.node);
            var1.width += var7.width;
            var7.y = var2;
         }

         var1.height = this.computeMaxPrefAreaHeight(var4, marginAccessor, this.getRowValignment());
         var1.baselineOffset = this.getRowValignment() == VPos.BASELINE ? this.getAreaBaselineOffset(var4, marginAccessor, (var1x) -> {
            return ((LayoutRect)var1.rects.get(var1x)).width;
         }, var1.height, true) : 0.0;
      } else {
         var1.height = (double)(var1.rects.size() - 1) * this.snapSpace(this.getVgap());
         double var9 = 0.0;
         var6 = 0;

         for(int var10 = var1.rects.size(); var6 < var10; ++var6) {
            LayoutRect var8 = (LayoutRect)var1.rects.get(var6);
            var1.height += var8.height;
            var8.x = var2;
            var9 = Math.max(var9, var8.width);
         }

         var1.width = var9;
         var1.baselineOffset = var1.height;
      }

   }

   private double computeContentWidth(List var1) {
      double var2 = this.getOrientation() == Orientation.HORIZONTAL ? 0.0 : (double)(var1.size() - 1) * this.snapSpace(this.getHgap());
      int var4 = 0;

      for(int var5 = var1.size(); var4 < var5; ++var4) {
         Run var6 = (Run)var1.get(var4);
         if (this.getOrientation() == Orientation.HORIZONTAL) {
            var2 = Math.max(var2, var6.width);
         } else {
            var2 += var6.width;
         }
      }

      return var2;
   }

   private double computeContentHeight(List var1) {
      double var2 = this.getOrientation() == Orientation.VERTICAL ? 0.0 : (double)(var1.size() - 1) * this.snapSpace(this.getVgap());
      int var4 = 0;

      for(int var5 = var1.size(); var4 < var5; ++var4) {
         Run var6 = (Run)var1.get(var4);
         if (this.getOrientation() == Orientation.VERTICAL) {
            var2 = Math.max(var2, var6.height);
         } else {
            var2 += var6.height;
         }
      }

      return var2;
   }

   protected void layoutChildren() {
      Insets var1 = this.getInsets();
      double var2 = this.getWidth();
      double var4 = this.getHeight();
      double var6 = var1.getTop();
      double var8 = var1.getLeft();
      double var10 = var1.getBottom();
      double var12 = var1.getRight();
      double var14 = var2 - var8 - var12;
      double var16 = var4 - var6 - var10;
      List var18 = this.getRuns(this.getOrientation() == Orientation.HORIZONTAL ? var14 : var16);
      int var19 = 0;

      for(int var20 = var18.size(); var19 < var20; ++var19) {
         Run var21 = (Run)var18.get(var19);
         double var22 = var8 + computeXOffset(var14, this.getOrientation() == Orientation.HORIZONTAL ? var21.width : this.computeContentWidth(var18), this.getAlignmentInternal().getHpos());
         double var24 = var6 + computeYOffset(var16, this.getOrientation() == Orientation.VERTICAL ? var21.height : this.computeContentHeight(var18), this.getAlignmentInternal().getVpos());

         for(int var26 = 0; var26 < var21.rects.size(); ++var26) {
            LayoutRect var27 = (LayoutRect)var21.rects.get(var26);
            double var28 = var22 + var27.x;
            double var30 = var24 + var27.y;
            this.layoutInArea(var27.node, var28, var30, this.getOrientation() == Orientation.HORIZONTAL ? var27.width : var21.width, this.getOrientation() == Orientation.VERTICAL ? var27.height : var21.height, var21.baselineOffset, getMargin(var27.node), this.getColumnHalignmentInternal(), this.getRowValignmentInternal());
         }
      }

   }

   public static List getClassCssMetaData() {
      return FlowPane.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class Run {
      ArrayList rects;
      double width;
      double height;
      double baselineOffset;

      private Run() {
         this.rects = new ArrayList();
      }

      // $FF: synthetic method
      Run(Object var1) {
         this();
      }
   }

   private static class LayoutRect {
      public Node node;
      double x;
      double y;
      double width;
      double height;

      private LayoutRect() {
      }

      public String toString() {
         return "LayoutRect node id=" + this.node.getId() + " " + this.x + "," + this.y + " " + this.width + "x" + this.height;
      }

      // $FF: synthetic method
      LayoutRect(Object var1) {
         this();
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData ALIGNMENT;
      private static final CssMetaData COLUMN_HALIGNMENT;
      private static final CssMetaData HGAP;
      private static final CssMetaData ROW_VALIGNMENT;
      private static final CssMetaData ORIENTATION;
      private static final CssMetaData VGAP;
      private static final List STYLEABLES;

      static {
         ALIGNMENT = new CssMetaData("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) {
            public boolean isSettable(FlowPane var1) {
               return var1.alignment == null || !var1.alignment.isBound();
            }

            public StyleableProperty getStyleableProperty(FlowPane var1) {
               return (StyleableProperty)var1.alignmentProperty();
            }
         };
         COLUMN_HALIGNMENT = new CssMetaData("-fx-column-halignment", new EnumConverter(HPos.class), HPos.LEFT) {
            public boolean isSettable(FlowPane var1) {
               return var1.columnHalignment == null || !var1.columnHalignment.isBound();
            }

            public StyleableProperty getStyleableProperty(FlowPane var1) {
               return (StyleableProperty)var1.columnHalignmentProperty();
            }
         };
         HGAP = new CssMetaData("-fx-hgap", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(FlowPane var1) {
               return var1.hgap == null || !var1.hgap.isBound();
            }

            public StyleableProperty getStyleableProperty(FlowPane var1) {
               return (StyleableProperty)var1.hgapProperty();
            }
         };
         ROW_VALIGNMENT = new CssMetaData("-fx-row-valignment", new EnumConverter(VPos.class), VPos.CENTER) {
            public boolean isSettable(FlowPane var1) {
               return var1.rowValignment == null || !var1.rowValignment.isBound();
            }

            public StyleableProperty getStyleableProperty(FlowPane var1) {
               return (StyleableProperty)var1.rowValignmentProperty();
            }
         };
         ORIENTATION = new CssMetaData("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) {
            public Orientation getInitialValue(FlowPane var1) {
               return var1.getOrientation();
            }

            public boolean isSettable(FlowPane var1) {
               return var1.orientation == null || !var1.orientation.isBound();
            }

            public StyleableProperty getStyleableProperty(FlowPane var1) {
               return (StyleableProperty)var1.orientationProperty();
            }
         };
         VGAP = new CssMetaData("-fx-vgap", SizeConverter.getInstance(), 0.0) {
            public boolean isSettable(FlowPane var1) {
               return var1.vgap == null || !var1.vgap.isBound();
            }

            public StyleableProperty getStyleableProperty(FlowPane var1) {
               return (StyleableProperty)var1.vgapProperty();
            }
         };
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         var0.add(ALIGNMENT);
         var0.add(COLUMN_HALIGNMENT);
         var0.add(HGAP);
         var0.add(ROW_VALIGNMENT);
         var0.add(ORIENTATION);
         var0.add(VGAP);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
