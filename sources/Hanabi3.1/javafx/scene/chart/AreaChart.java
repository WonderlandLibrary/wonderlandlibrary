package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

public class AreaChart extends XYChart {
   private Map seriesYMultiplierMap;
   private Legend legend;
   private BooleanProperty createSymbols;

   public final boolean getCreateSymbols() {
      return this.createSymbols.getValue();
   }

   public final void setCreateSymbols(boolean var1) {
      this.createSymbols.setValue(var1);
   }

   public final BooleanProperty createSymbolsProperty() {
      return this.createSymbols;
   }

   public AreaChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2) {
      this(var1, var2, FXCollections.observableArrayList());
   }

   public AreaChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2, @NamedArg("data") ObservableList var3) {
      super(var1, var2);
      this.seriesYMultiplierMap = new HashMap();
      this.legend = new Legend();
      this.createSymbols = new StyleableBooleanProperty(true) {
         protected void invalidated() {
            for(int var1 = 0; var1 < AreaChart.this.getData().size(); ++var1) {
               XYChart.Series var2 = (XYChart.Series)AreaChart.this.getData().get(var1);

               for(int var3 = 0; var3 < var2.getData().size(); ++var3) {
                  XYChart.Data var4 = (XYChart.Data)var2.getData().get(var3);
                  Node var5 = var4.getNode();
                  if (this.get() && var5 == null) {
                     var5 = AreaChart.this.createSymbol(var2, AreaChart.this.getData().indexOf(var2), var4, var3);
                     if (null != var5) {
                        AreaChart.this.getPlotChildren().add(var5);
                     }
                  } else if (!this.get() && var5 != null) {
                     AreaChart.this.getPlotChildren().remove(var5);
                     var5 = null;
                     var4.setNode((Node)null);
                  }
               }
            }

            AreaChart.this.requestChartLayout();
         }

         public Object getBean() {
            return this;
         }

         public String getName() {
            return "createSymbols";
         }

         public CssMetaData getCssMetaData() {
            return AreaChart.StyleableProperties.CREATE_SYMBOLS;
         }
      };
      this.setLegend(this.legend);
      this.setData(var3);
   }

   private static double doubleValue(Number var0) {
      return doubleValue(var0, 0.0);
   }

   private static double doubleValue(Number var0, double var1) {
      return var0 == null ? var1 : var0.doubleValue();
   }

   protected void updateAxisRange() {
      Axis var1 = this.getXAxis();
      Axis var2 = this.getYAxis();
      ArrayList var3 = null;
      ArrayList var4 = null;
      if (var1.isAutoRanging()) {
         var3 = new ArrayList();
      }

      if (var2.isAutoRanging()) {
         var4 = new ArrayList();
      }

      if (var3 != null || var4 != null) {
         Iterator var5 = this.getData().iterator();

         while(var5.hasNext()) {
            XYChart.Series var6 = (XYChart.Series)var5.next();
            Iterator var7 = var6.getData().iterator();

            while(var7.hasNext()) {
               XYChart.Data var8 = (XYChart.Data)var7.next();
               if (var3 != null) {
                  var3.add(var8.getXValue());
               }

               if (var4 != null) {
                  var4.add(var8.getYValue());
               }
            }
         }

         if (var3 != null && (var3.size() != 1 || this.getXAxis().toNumericValue(var3.get(0)) != 0.0)) {
            var1.invalidateRange(var3);
         }

         if (var4 != null && (var4.size() != 1 || this.getYAxis().toNumericValue(var4.get(0)) != 0.0)) {
            var2.invalidateRange(var4);
         }
      }

   }

   protected void dataItemAdded(XYChart.Series var1, int var2, XYChart.Data var3) {
      Node var4 = this.createSymbol(var1, this.getData().indexOf(var1), var3, var2);
      if (this.shouldAnimate()) {
         boolean var5 = false;
         if (var2 > 0 && var2 < var1.getData().size() - 1) {
            var5 = true;
            XYChart.Data var22 = (XYChart.Data)var1.getData().get(var2 - 1);
            XYChart.Data var7 = (XYChart.Data)var1.getData().get(var2 + 1);
            double var8 = this.getXAxis().toNumericValue(var22.getXValue());
            double var10 = this.getYAxis().toNumericValue(var22.getYValue());
            double var12 = this.getXAxis().toNumericValue(var7.getXValue());
            double var14 = this.getYAxis().toNumericValue(var7.getYValue());
            double var16 = this.getXAxis().toNumericValue(var3.getXValue());
            double var18 = this.getYAxis().toNumericValue(var3.getYValue());
            double var20 = (var14 - var10) / (var12 - var8) * var16 + (var12 * var10 - var14 * var8) / (var12 - var8);
            var3.setCurrentY(this.getYAxis().toRealValue(var20));
            var3.setCurrentX(this.getXAxis().toRealValue(var16));
         } else if (var2 == 0 && var1.getData().size() > 1) {
            var5 = true;
            var3.setCurrentX(((XYChart.Data)var1.getData().get(1)).getXValue());
            var3.setCurrentY(((XYChart.Data)var1.getData().get(1)).getYValue());
         } else if (var2 == var1.getData().size() - 1 && var1.getData().size() > 1) {
            var5 = true;
            int var6 = var1.getData().size() - 2;
            var3.setCurrentX(((XYChart.Data)var1.getData().get(var6)).getXValue());
            var3.setCurrentY(((XYChart.Data)var1.getData().get(var6)).getYValue());
         }

         if (var4 != null) {
            var4.setOpacity(0.0);
            this.getPlotChildren().add(var4);
            FadeTransition var23 = new FadeTransition(Duration.millis(500.0), var4);
            var23.setToValue(1.0);
            var23.play();
         }

         if (var5) {
            this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, (var2x) -> {
               if (var4 != null && !this.getPlotChildren().contains(var4)) {
                  this.getPlotChildren().add(var4);
               }

            }, new KeyValue[]{new KeyValue(var3.currentYProperty(), var3.getCurrentY()), new KeyValue(var3.currentXProperty(), var3.getCurrentX())}), new KeyFrame(Duration.millis(800.0), new KeyValue[]{new KeyValue(var3.currentYProperty(), var3.getYValue(), Interpolator.EASE_BOTH), new KeyValue(var3.currentXProperty(), var3.getXValue(), Interpolator.EASE_BOTH)})});
         }
      } else if (var4 != null) {
         this.getPlotChildren().add(var4);
      }

   }

   protected void dataItemRemoved(XYChart.Data var1, XYChart.Series var2) {
      Node var3 = var1.getNode();
      if (var3 != null) {
         var3.focusTraversableProperty().unbind();
      }

      int var4 = var2.getItemIndex(var1);
      if (this.shouldAnimate()) {
         boolean var5 = false;
         int var6 = var2.getDataSize();
         int var7 = var2.getData().size();
         if (var4 > 0 && var4 < var6 - 1) {
            var5 = true;
            XYChart.Data var25 = var2.getItem(var4 - 1);
            XYChart.Data var9 = var2.getItem(var4 + 1);
            double var10 = this.getXAxis().toNumericValue(var25.getXValue());
            double var12 = this.getYAxis().toNumericValue(var25.getYValue());
            double var14 = this.getXAxis().toNumericValue(var9.getXValue());
            double var16 = this.getYAxis().toNumericValue(var9.getYValue());
            double var18 = this.getXAxis().toNumericValue(var1.getXValue());
            double var20 = this.getYAxis().toNumericValue(var1.getYValue());
            double var22 = (var16 - var12) / (var14 - var10) * var18 + (var14 * var12 - var16 * var10) / (var14 - var10);
            var1.setCurrentX(this.getXAxis().toRealValue(var18));
            var1.setCurrentY(this.getYAxis().toRealValue(var20));
            var1.setXValue(this.getXAxis().toRealValue(var18));
            var1.setYValue(this.getYAxis().toRealValue(var22));
         } else if (var4 == 0 && var7 > 1) {
            var5 = true;
            var1.setXValue(((XYChart.Data)var2.getData().get(0)).getXValue());
            var1.setYValue(((XYChart.Data)var2.getData().get(0)).getYValue());
         } else if (var4 == var6 - 1 && var7 > 1) {
            var5 = true;
            int var24 = var7 - 1;
            var1.setXValue(((XYChart.Data)var2.getData().get(var24)).getXValue());
            var1.setYValue(((XYChart.Data)var2.getData().get(var24)).getYValue());
         } else if (var3 != null) {
            var3.setOpacity(0.0);
            FadeTransition var8 = new FadeTransition(Duration.millis(500.0), var3);
            var8.setToValue(0.0);
            var8.setOnFinished((var4x) -> {
               this.getPlotChildren().remove(var3);
               this.removeDataItemFromDisplay(var2, var1);
            });
            var8.play();
         }

         if (var5) {
            this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getCurrentY()), new KeyValue(var1.currentXProperty(), var1.getCurrentX())}), new KeyFrame(Duration.millis(800.0), (var4x) -> {
               var1.setSeries((XYChart.Series)null);
               this.getPlotChildren().remove(var3);
               this.removeDataItemFromDisplay(var2, var1);
            }, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getYValue(), Interpolator.EASE_BOTH), new KeyValue(var1.currentXProperty(), var1.getXValue(), Interpolator.EASE_BOTH)})});
         }
      } else {
         var1.setSeries((XYChart.Series)null);
         this.getPlotChildren().remove(var3);
         this.removeDataItemFromDisplay(var2, var1);
      }

   }

   protected void dataItemChanged(XYChart.Data var1) {
   }

   protected void seriesChanged(ListChangeListener.Change var1) {
      for(int var2 = 0; var2 < this.getDataSize(); ++var2) {
         XYChart.Series var3 = (XYChart.Series)this.getData().get(var2);
         Path var4 = (Path)((Group)var3.getNode()).getChildren().get(1);
         Path var5 = (Path)((Group)var3.getNode()).getChildren().get(0);
         var4.getStyleClass().setAll((Object[])("chart-series-area-line", "series" + var2, var3.defaultColorStyleClass));
         var5.getStyleClass().setAll((Object[])("chart-series-area-fill", "series" + var2, var3.defaultColorStyleClass));

         for(int var6 = 0; var6 < var3.getData().size(); ++var6) {
            XYChart.Data var7 = (XYChart.Data)var3.getData().get(var6);
            Node var8 = var7.getNode();
            if (var8 != null) {
               var8.getStyleClass().setAll((Object[])("chart-area-symbol", "series" + var2, "data" + var6, var3.defaultColorStyleClass));
            }
         }
      }

   }

   protected void seriesAdded(XYChart.Series var1, int var2) {
      Path var3 = new Path();
      Path var4 = new Path();
      var3.setStrokeLineJoin(StrokeLineJoin.BEVEL);
      Group var5 = new Group(new Node[]{var4, var3});
      var1.setNode(var5);
      SimpleDoubleProperty var6 = new SimpleDoubleProperty(this, "seriesYMultiplier");
      this.seriesYMultiplierMap.put(var1, var6);
      if (this.shouldAnimate()) {
         var6.setValue((Number)0.0);
      } else {
         var6.setValue((Number)1.0);
      }

      this.getPlotChildren().add(var5);
      ArrayList var7 = new ArrayList();
      if (this.shouldAnimate()) {
         var7.add(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var5.opacityProperty(), 0), new KeyValue(var6, 0)}));
         var7.add(new KeyFrame(Duration.millis(200.0), new KeyValue[]{new KeyValue(var5.opacityProperty(), 1)}));
         var7.add(new KeyFrame(Duration.millis(500.0), new KeyValue[]{new KeyValue(var6, 1)}));
      }

      for(int var8 = 0; var8 < var1.getData().size(); ++var8) {
         XYChart.Data var9 = (XYChart.Data)var1.getData().get(var8);
         Node var10 = this.createSymbol(var1, var2, var9, var8);
         if (var10 != null) {
            if (this.shouldAnimate()) {
               var10.setOpacity(0.0);
               this.getPlotChildren().add(var10);
               var7.add(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var10.opacityProperty(), 0)}));
               var7.add(new KeyFrame(Duration.millis(200.0), new KeyValue[]{new KeyValue(var10.opacityProperty(), 1)}));
            } else {
               this.getPlotChildren().add(var10);
            }
         }
      }

      if (this.shouldAnimate()) {
         this.animate((KeyFrame[])var7.toArray(new KeyFrame[var7.size()]));
      }

   }

   private void updateDefaultColorIndex(XYChart.Series var1) {
      int var2 = (Integer)this.seriesColorMap.get(var1);
      Path var3 = (Path)((Group)var1.getNode()).getChildren().get(1);
      Path var4 = (Path)((Group)var1.getNode()).getChildren().get(0);
      if (var3 != null) {
         var3.getStyleClass().remove(DEFAULT_COLOR + var2);
      }

      if (var4 != null) {
         var4.getStyleClass().remove(DEFAULT_COLOR + var2);
      }

      for(int var5 = 0; var5 < var1.getData().size(); ++var5) {
         Node var6 = ((XYChart.Data)var1.getData().get(var5)).getNode();
         if (var6 != null) {
            var6.getStyleClass().remove(DEFAULT_COLOR + var2);
         }
      }

   }

   protected void seriesRemoved(XYChart.Series var1) {
      this.updateDefaultColorIndex(var1);
      this.seriesYMultiplierMap.remove(var1);
      if (this.shouldAnimate()) {
         Timeline var2 = new Timeline(this.createSeriesRemoveTimeLine(var1, 400L));
         var2.play();
      } else {
         this.getPlotChildren().remove(var1.getNode());
         Iterator var4 = var1.getData().iterator();

         while(var4.hasNext()) {
            XYChart.Data var3 = (XYChart.Data)var4.next();
            this.getPlotChildren().remove(var3.getNode());
         }

         this.removeSeriesFromDisplay(var1);
      }

   }

   protected void layoutPlotChildren() {
      ArrayList var1 = new ArrayList(this.getDataSize());

      for(int var2 = 0; var2 < this.getDataSize(); ++var2) {
         XYChart.Series var3 = (XYChart.Series)this.getData().get(var2);
         DoubleProperty var4 = (DoubleProperty)this.seriesYMultiplierMap.get(var3);
         double var5 = 0.0;
         ObservableList var7 = ((Group)var3.getNode()).getChildren();
         ObservableList var8 = ((Path)var7.get(1)).getElements();
         ObservableList var9 = ((Path)var7.get(0)).getElements();
         var8.clear();
         var9.clear();
         var1.clear();
         Iterator var10 = this.getDisplayedDataIterator(var3);

         while(var10.hasNext()) {
            XYChart.Data var11 = (XYChart.Data)var10.next();
            double var12 = this.getXAxis().getDisplayPosition(var11.getCurrentX());
            double var14 = this.getYAxis().getDisplayPosition(this.getYAxis().toRealValue(this.getYAxis().toNumericValue(var11.getCurrentY()) * var4.getValue()));
            var1.add(new LineTo(var12, var14));
            if (!Double.isNaN(var12) && !Double.isNaN(var14)) {
               var5 = var12;
               Node var16 = var11.getNode();
               if (var16 != null) {
                  double var17 = var16.prefWidth(-1.0);
                  double var19 = var16.prefHeight(-1.0);
                  var16.resizeRelocate(var12 - var17 / 2.0, var14 - var19 / 2.0, var17, var19);
               }
            }
         }

         if (!var1.isEmpty()) {
            Collections.sort(var1, (var0, var1x) -> {
               return Double.compare(var0.getX(), var1x.getX());
            });
            LineTo var22 = (LineTo)var1.get(0);
            double var23 = var22.getY();
            double var13 = this.getYAxis().toNumericValue(this.getYAxis().getValueForDisplay(var23));
            double var15 = this.getYAxis().getZeroPosition();
            boolean var24 = !Double.isNaN(var15);
            double var18 = this.getYAxis().getHeight();
            double var20 = var24 ? var15 : (var13 < 0.0 ? var13 - var18 : var18);
            var8.add(new MoveTo(var22.getX(), var23));
            var9.add(new MoveTo(var22.getX(), var20));
            var8.addAll(var1);
            var9.addAll(var1);
            var9.add(new LineTo(var5, var20));
            var9.add(new ClosePath());
         }
      }

   }

   private Node createSymbol(XYChart.Series var1, int var2, XYChart.Data var3, int var4) {
      Object var5 = var3.getNode();
      if (var5 == null && this.getCreateSymbols()) {
         var5 = new StackPane();
         ((Node)var5).setAccessibleRole(AccessibleRole.TEXT);
         ((Node)var5).setAccessibleRoleDescription("Point");
         ((Node)var5).focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
         var3.setNode((Node)var5);
      }

      if (var5 != null) {
         ((Node)var5).getStyleClass().setAll((Object[])("chart-area-symbol", "series" + var2, "data" + var4, var1.defaultColorStyleClass));
      }

      return (Node)var5;
   }

   protected void updateLegend() {
      this.legend.getItems().clear();
      if (this.getData() != null) {
         for(int var1 = 0; var1 < this.getData().size(); ++var1) {
            XYChart.Series var2 = (XYChart.Series)this.getData().get(var1);
            Legend.LegendItem var3 = new Legend.LegendItem(var2.getName());
            var3.getSymbol().getStyleClass().addAll("chart-area-symbol", "series" + var1, "area-legend-symbol", var2.defaultColorStyleClass);
            this.legend.getItems().add(var3);
         }
      }

      if (this.legend.getItems().size() > 0) {
         if (this.getLegend() == null) {
            this.setLegend(this.legend);
         }
      } else {
         this.setLegend((Node)null);
      }

   }

   public static List getClassCssMetaData() {
      return AreaChart.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData CREATE_SYMBOLS;
      private static final List STYLEABLES;

      static {
         CREATE_SYMBOLS = new CssMetaData("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(AreaChart var1) {
               return var1.createSymbols == null || !var1.createSymbols.isBound();
            }

            public StyleableProperty getStyleableProperty(AreaChart var1) {
               return (StyleableProperty)var1.createSymbolsProperty();
            }
         };
         ArrayList var0 = new ArrayList(XYChart.getClassCssMetaData());
         var0.add(CREATE_SYMBOLS);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
