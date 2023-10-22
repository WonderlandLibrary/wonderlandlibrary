package javafx.scene.chart;

import com.sun.javafx.charts.ChartLayoutAnimator;
import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public abstract class Chart extends Region {
   private static final int MIN_WIDTH_TO_LEAVE_FOR_CHART_CONTENT = 200;
   private static final int MIN_HEIGHT_TO_LEAVE_FOR_CHART_CONTENT = 150;
   private final Label titleLabel = new Label();
   private final Pane chartContent = new Pane() {
      protected void layoutChildren() {
         double var1 = this.snappedTopInset();
         double var3 = this.snappedLeftInset();
         double var5 = this.snappedBottomInset();
         double var7 = this.snappedRightInset();
         double var9 = this.getWidth();
         double var11 = this.getHeight();
         double var13 = this.snapSize(var9 - (var3 + var7));
         double var15 = this.snapSize(var11 - (var1 + var5));
         Chart.this.layoutChartChildren(this.snapPosition(var1), this.snapPosition(var3), var13, var15);
      }

      public boolean usesMirroring() {
         return Chart.this.useChartContentMirroring;
      }
   };
   boolean useChartContentMirroring = true;
   private final ChartLayoutAnimator animator;
   private StringProperty title;
   private ObjectProperty titleSide;
   private final ObjectProperty legend;
   private final BooleanProperty legendVisible;
   private ObjectProperty legendSide;
   private BooleanProperty animated;

   public final String getTitle() {
      return (String)this.title.get();
   }

   public final void setTitle(String var1) {
      this.title.set(var1);
   }

   public final StringProperty titleProperty() {
      return this.title;
   }

   public final Side getTitleSide() {
      return (Side)this.titleSide.get();
   }

   public final void setTitleSide(Side var1) {
      this.titleSide.set(var1);
   }

   public final ObjectProperty titleSideProperty() {
      return this.titleSide;
   }

   protected final Node getLegend() {
      return (Node)this.legend.getValue();
   }

   protected final void setLegend(Node var1) {
      this.legend.setValue(var1);
   }

   protected final ObjectProperty legendProperty() {
      return this.legend;
   }

   public final boolean isLegendVisible() {
      return this.legendVisible.getValue();
   }

   public final void setLegendVisible(boolean var1) {
      this.legendVisible.setValue(var1);
   }

   public final BooleanProperty legendVisibleProperty() {
      return this.legendVisible;
   }

   public final Side getLegendSide() {
      return (Side)this.legendSide.get();
   }

   public final void setLegendSide(Side var1) {
      this.legendSide.set(var1);
   }

   public final ObjectProperty legendSideProperty() {
      return this.legendSide;
   }

   public final boolean getAnimated() {
      return this.animated.get();
   }

   public final void setAnimated(boolean var1) {
      this.animated.set(var1);
   }

   public final BooleanProperty animatedProperty() {
      return this.animated;
   }

   protected ObservableList getChartChildren() {
      return this.chartContent.getChildren();
   }

   public Chart() {
      this.animator = new ChartLayoutAnimator(this.chartContent);
      this.title = new StringPropertyBase() {
         protected void invalidated() {
            Chart.this.titleLabel.setText(this.get());
         }

         public Object getBean() {
            return Chart.this;
         }

         public String getName() {
            return "title";
         }
      };
      this.titleSide = new StyleableObjectProperty(Side.TOP) {
         protected void invalidated() {
            Chart.this.requestLayout();
         }

         public CssMetaData getCssMetaData() {
            return Chart.StyleableProperties.TITLE_SIDE;
         }

         public Object getBean() {
            return Chart.this;
         }

         public String getName() {
            return "titleSide";
         }
      };
      this.legend = new ObjectPropertyBase() {
         private Node old = null;

         protected void invalidated() {
            Node var1 = (Node)this.get();
            if (this.old != null) {
               Chart.this.getChildren().remove(this.old);
            }

            if (var1 != null) {
               Chart.this.getChildren().add(var1);
               var1.setVisible(Chart.this.isLegendVisible());
            }

            this.old = var1;
         }

         public Object getBean() {
            return Chart.this;
         }

         public String getName() {
            return "legend";
         }
      };
      this.legendVisible = new StyleableBooleanProperty(true) {
         protected void invalidated() {
            Chart.this.requestLayout();
         }

         public CssMetaData getCssMetaData() {
            return Chart.StyleableProperties.LEGEND_VISIBLE;
         }

         public Object getBean() {
            return Chart.this;
         }

         public String getName() {
            return "legendVisible";
         }
      };
      this.legendSide = new StyleableObjectProperty(Side.BOTTOM) {
         protected void invalidated() {
            Side var1 = (Side)this.get();
            Node var2 = Chart.this.getLegend();
            if (var2 instanceof Legend) {
               ((Legend)var2).setVertical(Side.LEFT.equals(var1) || Side.RIGHT.equals(var1));
            }

            Chart.this.requestLayout();
         }

         public CssMetaData getCssMetaData() {
            return Chart.StyleableProperties.LEGEND_SIDE;
         }

         public Object getBean() {
            return Chart.this;
         }

         public String getName() {
            return "legendSide";
         }
      };
      this.animated = new SimpleBooleanProperty(this, "animated", true);
      this.titleLabel.setAlignment(Pos.CENTER);
      this.titleLabel.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
      this.getChildren().addAll(this.titleLabel, this.chartContent);
      this.getStyleClass().add("chart");
      this.titleLabel.getStyleClass().add("chart-title");
      this.chartContent.getStyleClass().add("chart-content");
      this.chartContent.setManaged(false);
   }

   void animate(KeyFrame... var1) {
      this.animator.animate(var1);
   }

   protected void animate(Animation var1) {
      this.animator.animate(var1);
   }

   protected void requestChartLayout() {
      this.chartContent.requestLayout();
   }

   protected final boolean shouldAnimate() {
      return this.getAnimated() && this.impl_isTreeVisible() && this.getScene() != null;
   }

   protected abstract void layoutChartChildren(double var1, double var3, double var5, double var7);

   protected void layoutChildren() {
      double var1 = this.snappedTopInset();
      double var3 = this.snappedLeftInset();
      double var5 = this.snappedBottomInset();
      double var7 = this.snappedRightInset();
      double var9 = this.getWidth();
      double var11 = this.getHeight();
      if (this.getTitle() != null) {
         this.titleLabel.setVisible(true);
         double var13;
         if (this.getTitleSide().equals(Side.TOP)) {
            var13 = this.snapSize(this.titleLabel.prefHeight(var9 - var3 - var7));
            this.titleLabel.resizeRelocate(var3, var1, var9 - var3 - var7, var13);
            var1 += var13;
         } else if (this.getTitleSide().equals(Side.BOTTOM)) {
            var13 = this.snapSize(this.titleLabel.prefHeight(var9 - var3 - var7));
            this.titleLabel.resizeRelocate(var3, var11 - var5 - var13, var9 - var3 - var7, var13);
            var5 += var13;
         } else if (this.getTitleSide().equals(Side.LEFT)) {
            var13 = this.snapSize(this.titleLabel.prefWidth(var11 - var1 - var5));
            this.titleLabel.resizeRelocate(var3, var1, var13, var11 - var1 - var5);
            var3 += var13;
         } else if (this.getTitleSide().equals(Side.RIGHT)) {
            var13 = this.snapSize(this.titleLabel.prefWidth(var11 - var1 - var5));
            this.titleLabel.resizeRelocate(var9 - var7 - var13, var1, var13, var11 - var1 - var5);
            var7 += var13;
         }
      } else {
         this.titleLabel.setVisible(false);
      }

      Node var19 = this.getLegend();
      if (var19 != null) {
         boolean var14 = this.isLegendVisible();
         if (var14) {
            double var17;
            double var15;
            if (this.getLegendSide() == Side.TOP) {
               var15 = this.snapSize(var19.prefHeight(var9 - var3 - var7));
               var17 = Utils.boundedSize(this.snapSize(var19.prefWidth(var15)), 0.0, var9 - var3 - var7);
               var19.resizeRelocate(var3 + (var9 - var3 - var7 - var17) / 2.0, var1, var17, var15);
               if (var11 - var5 - var1 - var15 < 150.0) {
                  var14 = false;
               } else {
                  var1 += var15;
               }
            } else if (this.getLegendSide() == Side.BOTTOM) {
               var15 = this.snapSize(var19.prefHeight(var9 - var3 - var7));
               var17 = Utils.boundedSize(this.snapSize(var19.prefWidth(var15)), 0.0, var9 - var3 - var7);
               var19.resizeRelocate(var3 + (var9 - var3 - var7 - var17) / 2.0, var11 - var5 - var15, var17, var15);
               if (var11 - var5 - var1 - var15 < 150.0) {
                  var14 = false;
               } else {
                  var5 += var15;
               }
            } else if (this.getLegendSide() == Side.LEFT) {
               var15 = this.snapSize(var19.prefWidth(var11 - var1 - var5));
               var17 = Utils.boundedSize(this.snapSize(var19.prefHeight(var15)), 0.0, var11 - var1 - var5);
               var19.resizeRelocate(var3, var1 + (var11 - var1 - var5 - var17) / 2.0, var15, var17);
               if (var9 - var3 - var7 - var15 < 200.0) {
                  var14 = false;
               } else {
                  var3 += var15;
               }
            } else if (this.getLegendSide() == Side.RIGHT) {
               var15 = this.snapSize(var19.prefWidth(var11 - var1 - var5));
               var17 = Utils.boundedSize(this.snapSize(var19.prefHeight(var15)), 0.0, var11 - var1 - var5);
               var19.resizeRelocate(var9 - var7 - var15, var1 + (var11 - var1 - var5 - var17) / 2.0, var15, var17);
               if (var9 - var3 - var7 - var15 < 200.0) {
                  var14 = false;
               } else {
                  var7 += var15;
               }
            }
         }

         var19.setVisible(var14);
      }

      this.chartContent.resizeRelocate(var3, var1, var9 - var3 - var7, var11 - var1 - var5);
   }

   protected double computeMinHeight(double var1) {
      return 150.0;
   }

   protected double computeMinWidth(double var1) {
      return 200.0;
   }

   protected double computePrefWidth(double var1) {
      return 500.0;
   }

   protected double computePrefHeight(double var1) {
      return 400.0;
   }

   public static List getClassCssMetaData() {
      return Chart.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData TITLE_SIDE;
      private static final CssMetaData LEGEND_SIDE;
      private static final CssMetaData LEGEND_VISIBLE;
      private static final List STYLEABLES;

      static {
         TITLE_SIDE = new CssMetaData("-fx-title-side", new EnumConverter(Side.class), Side.TOP) {
            public boolean isSettable(Chart var1) {
               return var1.titleSide == null || !var1.titleSide.isBound();
            }

            public StyleableProperty getStyleableProperty(Chart var1) {
               return (StyleableProperty)var1.titleSideProperty();
            }
         };
         LEGEND_SIDE = new CssMetaData("-fx-legend-side", new EnumConverter(Side.class), Side.BOTTOM) {
            public boolean isSettable(Chart var1) {
               return var1.legendSide == null || !var1.legendSide.isBound();
            }

            public StyleableProperty getStyleableProperty(Chart var1) {
               return (StyleableProperty)var1.legendSideProperty();
            }
         };
         LEGEND_VISIBLE = new CssMetaData("-fx-legend-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(Chart var1) {
               return var1.legendVisible == null || !var1.legendVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(Chart var1) {
               return (StyleableProperty)var1.legendVisibleProperty();
            }
         };
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         var0.add(TITLE_SIDE);
         var0.add(LEGEND_VISIBLE);
         var0.add(LEGEND_SIDE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
