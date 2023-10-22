package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class BubbleChart extends XYChart {
   private Legend legend;

   public BubbleChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2) {
      this(var1, var2, FXCollections.observableArrayList());
   }

   public BubbleChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2, @NamedArg("data") ObservableList var3) {
      super(var1, var2);
      this.legend = new Legend();
      this.setLegend(this.legend);
      if (var1 instanceof ValueAxis && var2 instanceof ValueAxis) {
         this.setData(var3);
      } else {
         throw new IllegalArgumentException("Axis type incorrect, X and Y should both be NumberAxis");
      }
   }

   private static double getDoubleValue(Object var0, double var1) {
      return !(var0 instanceof Number) ? var1 : ((Number)var0).doubleValue();
   }

   protected void layoutPlotChildren() {
      for(int var1 = 0; var1 < this.getDataSize(); ++var1) {
         XYChart.Series var2 = (XYChart.Series)this.getData().get(var1);
         Iterator var3 = this.getDisplayedDataIterator(var2);

         while(var3.hasNext()) {
            XYChart.Data var4 = (XYChart.Data)var3.next();
            double var5 = this.getXAxis().getDisplayPosition(var4.getCurrentX());
            double var7 = this.getYAxis().getDisplayPosition(var4.getCurrentY());
            if (!Double.isNaN(var5) && !Double.isNaN(var7)) {
               Node var9 = var4.getNode();
               if (var9 != null && var9 instanceof StackPane) {
                  StackPane var11 = (StackPane)var4.getNode();
                  Ellipse var10;
                  if (var11.getShape() == null) {
                     var10 = new Ellipse(getDoubleValue(var4.getExtraValue(), 1.0), getDoubleValue(var4.getExtraValue(), 1.0));
                  } else {
                     if (!(var11.getShape() instanceof Ellipse)) {
                        return;
                     }

                     var10 = (Ellipse)var11.getShape();
                  }

                  var10.setRadiusX(getDoubleValue(var4.getExtraValue(), 1.0) * (this.getXAxis() instanceof NumberAxis ? Math.abs(((NumberAxis)this.getXAxis()).getScale()) : 1.0));
                  var10.setRadiusY(getDoubleValue(var4.getExtraValue(), 1.0) * (this.getYAxis() instanceof NumberAxis ? Math.abs(((NumberAxis)this.getYAxis()).getScale()) : 1.0));
                  var11.setShape((Shape)null);
                  var11.setShape(var10);
                  var11.setScaleShape(false);
                  var11.setCenterShape(false);
                  var11.setCacheShape(false);
                  var9.setLayoutX(var5);
                  var9.setLayoutY(var7);
               }
            }
         }
      }

   }

   protected void dataItemAdded(XYChart.Series var1, int var2, XYChart.Data var3) {
      Node var4 = this.createBubble(var1, this.getData().indexOf(var1), var3, var2);
      if (this.shouldAnimate()) {
         var4.setOpacity(0.0);
         this.getPlotChildren().add(var4);
         FadeTransition var5 = new FadeTransition(Duration.millis(500.0), var4);
         var5.setToValue(1.0);
         var5.play();
      } else {
         this.getPlotChildren().add(var4);
      }

   }

   protected void dataItemRemoved(XYChart.Data var1, XYChart.Series var2) {
      Node var3 = var1.getNode();
      if (this.shouldAnimate()) {
         FadeTransition var4 = new FadeTransition(Duration.millis(500.0), var3);
         var4.setToValue(0.0);
         var4.setOnFinished((var4x) -> {
            this.getPlotChildren().remove(var3);
            this.removeDataItemFromDisplay(var2, var1);
            var3.setOpacity(1.0);
         });
         var4.play();
      } else {
         this.getPlotChildren().remove(var3);
         this.removeDataItemFromDisplay(var2, var1);
      }

   }

   protected void dataItemChanged(XYChart.Data var1) {
   }

   protected void seriesAdded(XYChart.Series var1, int var2) {
      for(int var3 = 0; var3 < var1.getData().size(); ++var3) {
         XYChart.Data var4 = (XYChart.Data)var1.getData().get(var3);
         Node var5 = this.createBubble(var1, var2, var4, var3);
         if (this.shouldAnimate()) {
            var5.setOpacity(0.0);
            this.getPlotChildren().add(var5);
            FadeTransition var6 = new FadeTransition(Duration.millis(500.0), var5);
            var6.setToValue(1.0);
            var6.play();
         } else {
            this.getPlotChildren().add(var5);
         }
      }

   }

   protected void seriesRemoved(XYChart.Series var1) {
      if (this.shouldAnimate()) {
         ParallelTransition var2 = new ParallelTransition();
         var2.setOnFinished((var2x) -> {
            this.removeSeriesFromDisplay(var1);
         });
         Iterator var3 = var1.getData().iterator();

         while(var3.hasNext()) {
            XYChart.Data var4 = (XYChart.Data)var3.next();
            Node var5 = var4.getNode();
            FadeTransition var6 = new FadeTransition(Duration.millis(500.0), var5);
            var6.setToValue(0.0);
            var6.setOnFinished((var2x) -> {
               this.getPlotChildren().remove(var5);
               var5.setOpacity(1.0);
            });
            var2.getChildren().add(var6);
         }

         var2.play();
      } else {
         Iterator var7 = var1.getData().iterator();

         while(var7.hasNext()) {
            XYChart.Data var8 = (XYChart.Data)var7.next();
            Node var9 = var8.getNode();
            this.getPlotChildren().remove(var9);
         }

         this.removeSeriesFromDisplay(var1);
      }

   }

   private Node createBubble(XYChart.Series var1, int var2, XYChart.Data var3, int var4) {
      Object var5 = var3.getNode();
      if (var5 == null) {
         var5 = new StackPane();
         var3.setNode((Node)var5);
      }

      ((Node)var5).getStyleClass().setAll((Object[])("chart-bubble", "series" + var2, "data" + var4, var1.defaultColorStyleClass));
      return (Node)var5;
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

      boolean var5 = var1 instanceof CategoryAxis;
      boolean var6 = var2 instanceof CategoryAxis;
      if (var3 != null || var4 != null) {
         Iterator var7 = this.getData().iterator();

         while(var7.hasNext()) {
            XYChart.Series var8 = (XYChart.Series)var7.next();
            Iterator var9 = var8.getData().iterator();

            while(var9.hasNext()) {
               XYChart.Data var10 = (XYChart.Data)var9.next();
               if (var3 != null) {
                  if (var5) {
                     var3.add(var10.getXValue());
                  } else {
                     var3.add(var1.toRealValue(var1.toNumericValue(var10.getXValue()) + getDoubleValue(var10.getExtraValue(), 0.0)));
                     var3.add(var1.toRealValue(var1.toNumericValue(var10.getXValue()) - getDoubleValue(var10.getExtraValue(), 0.0)));
                  }
               }

               if (var4 != null) {
                  if (var6) {
                     var4.add(var10.getYValue());
                  } else {
                     var4.add(var2.toRealValue(var2.toNumericValue(var10.getYValue()) + getDoubleValue(var10.getExtraValue(), 0.0)));
                     var4.add(var2.toRealValue(var2.toNumericValue(var10.getYValue()) - getDoubleValue(var10.getExtraValue(), 0.0)));
                  }
               }
            }
         }

         if (var3 != null) {
            var1.invalidateRange(var3);
         }

         if (var4 != null) {
            var2.invalidateRange(var4);
         }
      }

   }

   protected void updateLegend() {
      this.legend.getItems().clear();
      if (this.getData() != null) {
         for(int var1 = 0; var1 < this.getData().size(); ++var1) {
            XYChart.Series var2 = (XYChart.Series)this.getData().get(var1);
            Legend.LegendItem var3 = new Legend.LegendItem(var2.getName());
            var3.getSymbol().getStyleClass().addAll("series" + var1, "chart-bubble", "bubble-legend-symbol", var2.defaultColorStyleClass);
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
}
