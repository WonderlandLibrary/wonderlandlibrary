package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;

public class LineChart extends XYChart {
   private Map seriesYMultiplierMap;
   private Legend legend;
   private Timeline dataRemoveTimeline;
   private XYChart.Series seriesOfDataRemoved;
   private XYChart.Data dataItemBeingRemoved;
   private FadeTransition fadeSymbolTransition;
   private Map XYValueMap;
   private Timeline seriesRemoveTimeline;
   private BooleanProperty createSymbols;
   private ObjectProperty axisSortingPolicy;

   public final boolean getCreateSymbols() {
      return this.createSymbols.getValue();
   }

   public final void setCreateSymbols(boolean var1) {
      this.createSymbols.setValue(var1);
   }

   public final BooleanProperty createSymbolsProperty() {
      return this.createSymbols;
   }

   public final SortingPolicy getAxisSortingPolicy() {
      return (SortingPolicy)this.axisSortingPolicy.getValue();
   }

   public final void setAxisSortingPolicy(SortingPolicy var1) {
      this.axisSortingPolicy.setValue(var1);
   }

   public final ObjectProperty axisSortingPolicyProperty() {
      return this.axisSortingPolicy;
   }

   public LineChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2) {
      this(var1, var2, FXCollections.observableArrayList());
   }

   public LineChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2, @NamedArg("data") ObservableList var3) {
      super(var1, var2);
      this.seriesYMultiplierMap = new HashMap();
      this.legend = new Legend();
      this.seriesOfDataRemoved = null;
      this.dataItemBeingRemoved = null;
      this.fadeSymbolTransition = null;
      this.XYValueMap = new HashMap();
      this.seriesRemoveTimeline = null;
      this.createSymbols = new StyleableBooleanProperty(true) {
         protected void invalidated() {
            for(int var1 = 0; var1 < LineChart.this.getData().size(); ++var1) {
               XYChart.Series var2 = (XYChart.Series)LineChart.this.getData().get(var1);

               for(int var3 = 0; var3 < var2.getData().size(); ++var3) {
                  XYChart.Data var4 = (XYChart.Data)var2.getData().get(var3);
                  Node var5 = var4.getNode();
                  if (this.get() && var5 == null) {
                     var5 = LineChart.this.createSymbol(var2, LineChart.this.getData().indexOf(var2), var4, var3);
                     LineChart.this.getPlotChildren().add(var5);
                  } else if (!this.get() && var5 != null) {
                     LineChart.this.getPlotChildren().remove(var5);
                     var5 = null;
                     var4.setNode((Node)null);
                  }
               }
            }

            LineChart.this.requestChartLayout();
         }

         public Object getBean() {
            return LineChart.this;
         }

         public String getName() {
            return "createSymbols";
         }

         public CssMetaData getCssMetaData() {
            return LineChart.StyleableProperties.CREATE_SYMBOLS;
         }
      };
      this.axisSortingPolicy = new ObjectPropertyBase(LineChart.SortingPolicy.X_AXIS) {
         protected void invalidated() {
            LineChart.this.requestChartLayout();
         }

         public Object getBean() {
            return LineChart.this;
         }

         public String getName() {
            return "axisSortingPolicy";
         }
      };
      this.setLegend(this.legend);
      this.setData(var3);
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
         if (this.dataRemoveTimeline != null && this.dataRemoveTimeline.getStatus().equals(Animation.Status.RUNNING) && this.seriesOfDataRemoved == var1) {
            this.dataRemoveTimeline.stop();
            this.dataRemoveTimeline = null;
            this.getPlotChildren().remove(this.dataItemBeingRemoved.getNode());
            this.removeDataItemFromDisplay(this.seriesOfDataRemoved, this.dataItemBeingRemoved);
            this.seriesOfDataRemoved = null;
            this.dataItemBeingRemoved = null;
         }

         boolean var5 = false;
         if (var2 > 0 && var2 < var1.getData().size() - 1) {
            var5 = true;
            XYChart.Data var23 = (XYChart.Data)var1.getData().get(var2 - 1);
            XYChart.Data var7 = (XYChart.Data)var1.getData().get(var2 + 1);
            if (var23 != null && var7 != null) {
               double var8 = this.getXAxis().toNumericValue(var23.getXValue());
               double var10 = this.getYAxis().toNumericValue(var23.getYValue());
               double var12 = this.getXAxis().toNumericValue(var7.getXValue());
               double var14 = this.getYAxis().toNumericValue(var7.getYValue());
               double var16 = this.getXAxis().toNumericValue(var3.getXValue());
               double var18;
               if (var16 > var8 && var16 < var12) {
                  var18 = (var14 - var10) / (var12 - var8) * var16 + (var12 * var10 - var14 * var8) / (var12 - var8);
                  var3.setCurrentY(this.getYAxis().toRealValue(var18));
                  var3.setCurrentX(this.getXAxis().toRealValue(var16));
               } else {
                  var18 = (var12 + var8) / 2.0;
                  double var20 = (var14 + var10) / 2.0;
                  var3.setCurrentX(this.getXAxis().toRealValue(var18));
                  var3.setCurrentY(this.getYAxis().toRealValue(var20));
               }
            }
         } else if (var2 == 0 && var1.getData().size() > 1) {
            var5 = true;
            var3.setCurrentX(((XYChart.Data)var1.getData().get(1)).getXValue());
            var3.setCurrentY(((XYChart.Data)var1.getData().get(1)).getYValue());
         } else if (var2 == var1.getData().size() - 1 && var1.getData().size() > 1) {
            var5 = true;
            int var22 = var1.getData().size() - 2;
            var3.setCurrentX(((XYChart.Data)var1.getData().get(var22)).getXValue());
            var3.setCurrentY(((XYChart.Data)var1.getData().get(var22)).getYValue());
         } else if (var4 != null) {
            var4.setOpacity(0.0);
            this.getPlotChildren().add(var4);
            FadeTransition var6 = new FadeTransition(Duration.millis(500.0), var4);
            var6.setToValue(1.0);
            var6.play();
         }

         if (var5) {
            this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, (var2x) -> {
               if (var4 != null && !this.getPlotChildren().contains(var4)) {
                  this.getPlotChildren().add(var4);
               }

            }, new KeyValue[]{new KeyValue(var3.currentYProperty(), var3.getCurrentY()), new KeyValue(var3.currentXProperty(), var3.getCurrentX())}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(var3.currentYProperty(), var3.getYValue(), Interpolator.EASE_BOTH), new KeyValue(var3.currentXProperty(), var3.getXValue(), Interpolator.EASE_BOTH)})});
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
         this.XYValueMap.clear();
         boolean var5 = false;
         int var6 = var2.getDataSize();
         int var7 = var2.getData().size();
         if (var4 > 0 && var4 < var6 - 1) {
            var5 = true;
            XYChart.Data var26 = var2.getItem(var4 - 1);
            XYChart.Data var9 = var2.getItem(var4 + 1);
            double var10 = this.getXAxis().toNumericValue(var26.getXValue());
            double var12 = this.getYAxis().toNumericValue(var26.getYValue());
            double var14 = this.getXAxis().toNumericValue(var9.getXValue());
            double var16 = this.getYAxis().toNumericValue(var9.getYValue());
            double var18 = this.getXAxis().toNumericValue(var1.getXValue());
            double var20 = this.getYAxis().toNumericValue(var1.getYValue());
            double var22;
            if (var18 > var10 && var18 < var14) {
               var22 = (var16 - var12) / (var14 - var10) * var18 + (var14 * var12 - var16 * var10) / (var14 - var10);
               var1.setCurrentX(this.getXAxis().toRealValue(var18));
               var1.setCurrentY(this.getYAxis().toRealValue(var20));
               var1.setXValue(this.getXAxis().toRealValue(var18));
               var1.setYValue(this.getYAxis().toRealValue(var22));
            } else {
               var22 = (var14 + var10) / 2.0;
               double var24 = (var16 + var12) / 2.0;
               var1.setCurrentX(this.getXAxis().toRealValue(var22));
               var1.setCurrentY(this.getYAxis().toRealValue(var24));
            }
         } else if (var4 == 0 && var7 > 1) {
            var5 = true;
            var1.setXValue(((XYChart.Data)var2.getData().get(0)).getXValue());
            var1.setYValue(((XYChart.Data)var2.getData().get(0)).getYValue());
         } else if (var4 == var6 - 1 && var7 > 1) {
            var5 = true;
            int var8 = var7 - 1;
            var1.setXValue(((XYChart.Data)var2.getData().get(var8)).getXValue());
            var1.setYValue(((XYChart.Data)var2.getData().get(var8)).getYValue());
         } else if (var3 != null) {
            this.fadeSymbolTransition = new FadeTransition(Duration.millis(500.0), var3);
            this.fadeSymbolTransition.setToValue(0.0);
            this.fadeSymbolTransition.setOnFinished((var4x) -> {
               var1.setSeries((XYChart.Series)null);
               this.getPlotChildren().remove(var3);
               this.removeDataItemFromDisplay(var2, var1);
               var3.setOpacity(1.0);
            });
            this.fadeSymbolTransition.play();
         }

         if (var5) {
            this.dataRemoveTimeline = this.createDataRemoveTimeline(var1, var3, var2);
            this.seriesOfDataRemoved = var2;
            this.dataItemBeingRemoved = var1;
            this.dataRemoveTimeline.play();
         }
      } else {
         var1.setSeries((XYChart.Series)null);
         if (var3 != null) {
            this.getPlotChildren().remove(var3);
         }

         this.removeDataItemFromDisplay(var2, var1);
      }

   }

   protected void dataItemChanged(XYChart.Data var1) {
   }

   protected void seriesChanged(ListChangeListener.Change var1) {
      for(int var2 = 0; var2 < this.getDataSize(); ++var2) {
         XYChart.Series var3 = (XYChart.Series)this.getData().get(var2);
         Node var4 = var3.getNode();
         if (var4 != null) {
            var4.getStyleClass().setAll((Object[])("chart-series-line", "series" + var2, var3.defaultColorStyleClass));
         }
      }

   }

   protected void seriesAdded(XYChart.Series var1, int var2) {
      Path var3 = new Path();
      var3.setStrokeLineJoin(StrokeLineJoin.BEVEL);
      var1.setNode(var3);
      SimpleDoubleProperty var4 = new SimpleDoubleProperty(this, "seriesYMultiplier");
      this.seriesYMultiplierMap.put(var1, var4);
      if (this.shouldAnimate()) {
         var3.setOpacity(0.0);
         var4.setValue((Number)0.0);
      } else {
         var4.setValue((Number)1.0);
      }

      this.getPlotChildren().add(var3);
      ArrayList var5 = new ArrayList();
      if (this.shouldAnimate()) {
         var5.add(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var3.opacityProperty(), 0), new KeyValue(var4, 0)}));
         var5.add(new KeyFrame(Duration.millis(200.0), new KeyValue[]{new KeyValue(var3.opacityProperty(), 1)}));
         var5.add(new KeyFrame(Duration.millis(500.0), new KeyValue[]{new KeyValue(var4, 1)}));
      }

      for(int var6 = 0; var6 < var1.getData().size(); ++var6) {
         XYChart.Data var7 = (XYChart.Data)var1.getData().get(var6);
         Node var8 = this.createSymbol(var1, var2, var7, var6);
         if (var8 != null) {
            if (this.shouldAnimate()) {
               var8.setOpacity(0.0);
            }

            this.getPlotChildren().add(var8);
            if (this.shouldAnimate()) {
               var5.add(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var8.opacityProperty(), 0)}));
               var5.add(new KeyFrame(Duration.millis(200.0), new KeyValue[]{new KeyValue(var8.opacityProperty(), 1)}));
            }
         }
      }

      if (this.shouldAnimate()) {
         this.animate((KeyFrame[])var5.toArray(new KeyFrame[var5.size()]));
      }

   }

   private void updateDefaultColorIndex(XYChart.Series var1) {
      int var2 = (Integer)this.seriesColorMap.get(var1);
      var1.getNode().getStyleClass().remove(DEFAULT_COLOR + var2);

      for(int var3 = 0; var3 < var1.getData().size(); ++var3) {
         Node var4 = ((XYChart.Data)var1.getData().get(var3)).getNode();
         if (var4 != null) {
            var4.getStyleClass().remove(DEFAULT_COLOR + var2);
         }
      }

   }

   protected void seriesRemoved(XYChart.Series var1) {
      this.updateDefaultColorIndex(var1);
      this.seriesYMultiplierMap.remove(var1);
      if (this.shouldAnimate()) {
         this.seriesRemoveTimeline = new Timeline(this.createSeriesRemoveTimeLine(var1, 900L));
         this.seriesRemoveTimeline.play();
      } else {
         this.getPlotChildren().remove(var1.getNode());
         Iterator var2 = var1.getData().iterator();

         while(var2.hasNext()) {
            XYChart.Data var3 = (XYChart.Data)var2.next();
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
         if (var3.getNode() instanceof Path) {
            ObservableList var5 = ((Path)var3.getNode()).getElements();
            var5.clear();
            var1.clear();
            Iterator var6 = this.getDisplayedDataIterator(var3);

            while(var6.hasNext()) {
               XYChart.Data var7 = (XYChart.Data)var6.next();
               double var8 = this.getXAxis().getDisplayPosition(var7.getCurrentX());
               double var10 = this.getYAxis().getDisplayPosition(this.getYAxis().toRealValue(this.getYAxis().toNumericValue(var7.getCurrentY()) * var4.getValue()));
               if (!Double.isNaN(var8) && !Double.isNaN(var10)) {
                  var1.add(new LineTo(var8, var10));
                  Node var12 = var7.getNode();
                  if (var12 != null) {
                     double var13 = var12.prefWidth(-1.0);
                     double var15 = var12.prefHeight(-1.0);
                     var12.resizeRelocate(var8 - var13 / 2.0, var10 - var15 / 2.0, var13, var15);
                  }
               }
            }

            switch (this.getAxisSortingPolicy()) {
               case X_AXIS:
                  Collections.sort(var1, (var0, var1x) -> {
                     return Double.compare(var0.getX(), var1x.getX());
                  });
                  break;
               case Y_AXIS:
                  Collections.sort(var1, (var0, var1x) -> {
                     return Double.compare(var0.getY(), var1x.getY());
                  });
            }

            if (!var1.isEmpty()) {
               LineTo var17 = (LineTo)var1.get(0);
               var5.add(new MoveTo(var17.getX(), var17.getY()));
               var5.addAll(var1);
            }
         }
      }

   }

   void dataBeingRemovedIsAdded(XYChart.Data var1, XYChart.Series var2) {
      if (this.fadeSymbolTransition != null) {
         this.fadeSymbolTransition.setOnFinished((EventHandler)null);
         this.fadeSymbolTransition.stop();
      }

      if (this.dataRemoveTimeline != null) {
         this.dataRemoveTimeline.setOnFinished((EventHandler)null);
         this.dataRemoveTimeline.stop();
      }

      Node var3 = var1.getNode();
      if (var3 != null) {
         this.getPlotChildren().remove(var3);
      }

      var1.setSeries((XYChart.Series)null);
      this.removeDataItemFromDisplay(var2, var1);
      Double var4 = (Double)this.XYValueMap.get(var1);
      if (var4 != null) {
         var1.setYValue(var4);
         var1.setCurrentY(var4);
      }

      this.XYValueMap.clear();
   }

   void seriesBeingRemovedIsAdded(XYChart.Series var1) {
      if (this.seriesRemoveTimeline != null) {
         this.seriesRemoveTimeline.setOnFinished((EventHandler)null);
         this.seriesRemoveTimeline.stop();
         this.getPlotChildren().remove(var1.getNode());
         Iterator var2 = var1.getData().iterator();

         while(var2.hasNext()) {
            XYChart.Data var3 = (XYChart.Data)var2.next();
            this.getPlotChildren().remove(var3.getNode());
         }

         this.removeSeriesFromDisplay(var1);
      }

   }

   private Timeline createDataRemoveTimeline(XYChart.Data var1, Node var2, XYChart.Series var3) {
      Timeline var4 = new Timeline();
      this.XYValueMap.put(var1, ((Number)var1.getYValue()).doubleValue());
      var4.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getCurrentY()), new KeyValue(var1.currentXProperty(), var1.getCurrentX())}), new KeyFrame(Duration.millis(500.0), (var4x) -> {
         if (var2 != null) {
            this.getPlotChildren().remove(var2);
         }

         this.removeDataItemFromDisplay(var3, var1);
         this.XYValueMap.clear();
      }, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getYValue(), Interpolator.EASE_BOTH), new KeyValue(var1.currentXProperty(), var1.getXValue(), Interpolator.EASE_BOTH)}));
      return var4;
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
         ((Node)var5).getStyleClass().addAll("chart-line-symbol", "series" + var2, "data" + var4, var1.defaultColorStyleClass);
      }

      return (Node)var5;
   }

   protected void updateLegend() {
      this.legend.getItems().clear();
      if (this.getData() != null) {
         for(int var1 = 0; var1 < this.getData().size(); ++var1) {
            XYChart.Series var2 = (XYChart.Series)this.getData().get(var1);
            Legend.LegendItem var3 = new Legend.LegendItem(var2.getName());
            var3.getSymbol().getStyleClass().addAll("chart-line-symbol", "series" + var1, var2.defaultColorStyleClass);
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
      return LineChart.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public static enum SortingPolicy {
      NONE,
      X_AXIS,
      Y_AXIS;
   }

   private static class StyleableProperties {
      private static final CssMetaData CREATE_SYMBOLS;
      private static final List STYLEABLES;

      static {
         CREATE_SYMBOLS = new CssMetaData("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(LineChart var1) {
               return var1.createSymbols == null || !var1.createSymbols.isBound();
            }

            public StyleableProperty getStyleableProperty(LineChart var1) {
               return (StyleableProperty)var1.createSymbolsProperty();
            }
         };
         ArrayList var0 = new ArrayList(XYChart.getClassCssMetaData());
         var0.add(CREATE_SYMBOLS);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
