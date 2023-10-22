package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.SizeConverter;
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
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class BarChart extends XYChart {
   private Map seriesCategoryMap;
   private Legend legend;
   private final Orientation orientation;
   private CategoryAxis categoryAxis;
   private ValueAxis valueAxis;
   private Timeline dataRemoveTimeline;
   private double bottomPos;
   private static String NEGATIVE_STYLE = "negative";
   private ParallelTransition pt;
   private Map XYValueMap;
   private DoubleProperty barGap;
   private DoubleProperty categoryGap;
   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

   public final double getBarGap() {
      return this.barGap.getValue();
   }

   public final void setBarGap(double var1) {
      this.barGap.setValue((Number)var1);
   }

   public final DoubleProperty barGapProperty() {
      return this.barGap;
   }

   public final double getCategoryGap() {
      return this.categoryGap.getValue();
   }

   public final void setCategoryGap(double var1) {
      this.categoryGap.setValue((Number)var1);
   }

   public final DoubleProperty categoryGapProperty() {
      return this.categoryGap;
   }

   public BarChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2) {
      this(var1, var2, FXCollections.observableArrayList());
   }

   public BarChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2, @NamedArg("data") ObservableList var3) {
      super(var1, var2);
      this.seriesCategoryMap = new HashMap();
      this.legend = new Legend();
      this.bottomPos = 0.0;
      this.XYValueMap = new HashMap();
      this.barGap = new StyleableDoubleProperty(4.0) {
         protected void invalidated() {
            this.get();
            BarChart.this.requestChartLayout();
         }

         public Object getBean() {
            return BarChart.this;
         }

         public String getName() {
            return "barGap";
         }

         public CssMetaData getCssMetaData() {
            return BarChart.StyleableProperties.BAR_GAP;
         }
      };
      this.categoryGap = new StyleableDoubleProperty(10.0) {
         protected void invalidated() {
            this.get();
            BarChart.this.requestChartLayout();
         }

         public Object getBean() {
            return BarChart.this;
         }

         public String getName() {
            return "categoryGap";
         }

         public CssMetaData getCssMetaData() {
            return BarChart.StyleableProperties.CATEGORY_GAP;
         }
      };
      this.getStyleClass().add("bar-chart");
      this.setLegend(this.legend);
      if (var1 instanceof ValueAxis && var2 instanceof CategoryAxis || var2 instanceof ValueAxis && var1 instanceof CategoryAxis) {
         if (var1 instanceof CategoryAxis) {
            this.categoryAxis = (CategoryAxis)var1;
            this.valueAxis = (ValueAxis)var2;
            this.orientation = Orientation.VERTICAL;
         } else {
            this.categoryAxis = (CategoryAxis)var2;
            this.valueAxis = (ValueAxis)var1;
            this.orientation = Orientation.HORIZONTAL;
         }

         this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, this.orientation == Orientation.HORIZONTAL);
         this.pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, this.orientation == Orientation.VERTICAL);
         this.setData(var3);
      } else {
         throw new IllegalArgumentException("Axis type incorrect, one of X,Y should be CategoryAxis and the other NumberAxis");
      }
   }

   public BarChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2, @NamedArg("data") ObservableList var3, @NamedArg("categoryGap") double var4) {
      this(var1, var2);
      this.setData(var3);
      this.setCategoryGap(var4);
   }

   protected void dataItemAdded(XYChart.Series var1, int var2, XYChart.Data var3) {
      String var4;
      if (this.orientation == Orientation.VERTICAL) {
         var4 = (String)var3.getXValue();
      } else {
         var4 = (String)var3.getYValue();
      }

      Object var5 = (Map)this.seriesCategoryMap.get(var1);
      if (var5 == null) {
         var5 = new HashMap();
         this.seriesCategoryMap.put(var1, var5);
      }

      if (!this.categoryAxis.getCategories().contains(var4)) {
         this.categoryAxis.getCategories().add(var2, var4);
      } else if (((Map)var5).containsKey(var4)) {
         XYChart.Data var6 = (XYChart.Data)((Map)var5).get(var4);
         this.getPlotChildren().remove(var6.getNode());
         this.removeDataItemFromDisplay(var1, var6);
         this.requestChartLayout();
         ((Map)var5).remove(var4);
      }

      ((Map)var5).put(var4, var3);
      Node var7 = this.createBar(var1, this.getData().indexOf(var1), var3, var2);
      if (this.shouldAnimate()) {
         this.animateDataAdd(var3, var7);
      } else {
         this.getPlotChildren().add(var7);
      }

   }

   protected void dataItemRemoved(XYChart.Data var1, XYChart.Series var2) {
      Node var3 = var1.getNode();
      if (var3 != null) {
         var3.focusTraversableProperty().unbind();
      }

      if (this.shouldAnimate()) {
         this.XYValueMap.clear();
         this.dataRemoveTimeline = this.createDataRemoveTimeline(var1, var3, var2);
         this.dataRemoveTimeline.setOnFinished((var3x) -> {
            var1.setSeries((XYChart.Series)null);
            this.removeDataItemFromDisplay(var2, var1);
         });
         this.dataRemoveTimeline.play();
      } else {
         this.processDataRemove(var2, var1);
         this.removeDataItemFromDisplay(var2, var1);
      }

   }

   protected void dataItemChanged(XYChart.Data var1) {
      double var2;
      double var4;
      if (this.orientation == Orientation.VERTICAL) {
         var2 = ((Number)var1.getYValue()).doubleValue();
         var4 = ((Number)var1.getCurrentY()).doubleValue();
      } else {
         var2 = ((Number)var1.getXValue()).doubleValue();
         var4 = ((Number)var1.getCurrentX()).doubleValue();
      }

      if (var4 > 0.0 && var2 < 0.0) {
         var1.getNode().getStyleClass().add(NEGATIVE_STYLE);
      } else if (var4 < 0.0 && var2 > 0.0) {
         var1.getNode().getStyleClass().remove(NEGATIVE_STYLE);
      }

   }

   protected void seriesAdded(XYChart.Series var1, int var2) {
      HashMap var3 = new HashMap();

      for(int var4 = 0; var4 < var1.getData().size(); ++var4) {
         XYChart.Data var5 = (XYChart.Data)var1.getData().get(var4);
         Node var6 = this.createBar(var1, var2, var5, var4);
         String var7;
         if (this.orientation == Orientation.VERTICAL) {
            var7 = (String)var5.getXValue();
         } else {
            var7 = (String)var5.getYValue();
         }

         var3.put(var7, var5);
         if (this.shouldAnimate()) {
            this.animateDataAdd(var5, var6);
         } else {
            double var8 = this.orientation == Orientation.VERTICAL ? ((Number)var5.getYValue()).doubleValue() : ((Number)var5.getXValue()).doubleValue();
            if (var8 < 0.0) {
               var6.getStyleClass().add(NEGATIVE_STYLE);
            }

            this.getPlotChildren().add(var6);
         }
      }

      if (var3.size() > 0) {
         this.seriesCategoryMap.put(var1, var3);
      }

   }

   protected void seriesRemoved(XYChart.Series var1) {
      this.updateDefaultColorIndex(var1);
      if (this.shouldAnimate()) {
         this.pt = new ParallelTransition();
         this.pt.setOnFinished((var2x) -> {
            this.removeSeriesFromDisplay(var1);
         });
         boolean var2 = this.getSeriesSize() <= 1;
         this.XYValueMap.clear();
         Iterator var3 = var1.getData().iterator();

         while(var3.hasNext()) {
            XYChart.Data var4 = (XYChart.Data)var3.next();
            Node var5 = var4.getNode();
            if (!var2) {
               Timeline var6 = this.createDataRemoveTimeline(var4, var5, var1);
               this.pt.getChildren().add(var6);
            } else {
               FadeTransition var10 = new FadeTransition(Duration.millis(700.0), var5);
               var10.setFromValue(1.0);
               var10.setToValue(0.0);
               var10.setOnFinished((var4x) -> {
                  this.processDataRemove(var1, var4);
                  var5.setOpacity(1.0);
               });
               this.pt.getChildren().add(var10);
            }
         }

         this.pt.play();
      } else {
         Iterator var7 = var1.getData().iterator();

         while(var7.hasNext()) {
            XYChart.Data var8 = (XYChart.Data)var7.next();
            Node var9 = var8.getNode();
            this.getPlotChildren().remove(var9);
            this.updateMap(var1, var8);
         }

         this.removeSeriesFromDisplay(var1);
      }

   }

   protected void layoutPlotChildren() {
      double var1 = this.categoryAxis.getCategorySpacing();
      double var3 = var1 - (this.getCategoryGap() + this.getBarGap());
      double var5 = var3 / (double)this.getSeriesSize() - this.getBarGap();
      double var7 = -((var1 - this.getCategoryGap()) / 2.0);
      double var9 = this.valueAxis.getLowerBound() > 0.0 ? this.valueAxis.getDisplayPosition((Number)this.valueAxis.getLowerBound()) : this.valueAxis.getZeroPosition();
      if (var5 <= 0.0) {
         var5 = 1.0;
      }

      int var11 = 0;

      for(Iterator var12 = this.categoryAxis.getCategories().iterator(); var12.hasNext(); ++var11) {
         String var13 = (String)var12.next();
         int var14 = 0;
         Iterator var15 = this.getDisplayedSeriesIterator();

         while(var15.hasNext()) {
            XYChart.Series var16 = (XYChart.Series)var15.next();
            XYChart.Data var17 = this.getDataItem(var16, var14, var11, var13);
            if (var17 != null) {
               Node var18 = var17.getNode();
               double var19;
               double var21;
               if (this.orientation == Orientation.VERTICAL) {
                  var19 = this.getXAxis().getDisplayPosition(var17.getCurrentX());
                  var21 = this.getYAxis().getDisplayPosition(var17.getCurrentY());
               } else {
                  var19 = this.getYAxis().getDisplayPosition(var17.getCurrentY());
                  var21 = this.getXAxis().getDisplayPosition(var17.getCurrentX());
               }

               if (!Double.isNaN(var19) && !Double.isNaN(var21)) {
                  double var23 = Math.min(var21, var9);
                  double var25 = Math.max(var21, var9);
                  this.bottomPos = var23;
                  if (this.orientation == Orientation.VERTICAL) {
                     var18.resizeRelocate(var19 + var7 + (var5 + this.getBarGap()) * (double)var14, var23, var5, var25 - var23);
                  } else {
                     var18.resizeRelocate(var23, var19 + var7 + (var5 + this.getBarGap()) * (double)var14, var25 - var23, var5);
                  }

                  ++var14;
               }
            }
         }
      }

   }

   protected void updateLegend() {
      this.legend.getItems().clear();
      if (this.getData() != null) {
         for(int var1 = 0; var1 < this.getData().size(); ++var1) {
            XYChart.Series var2 = (XYChart.Series)this.getData().get(var1);
            Legend.LegendItem var3 = new Legend.LegendItem(var2.getName());
            var3.getSymbol().getStyleClass().addAll("chart-bar", "series" + var1, "bar-legend-symbol", var2.defaultColorStyleClass);
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

   private void updateMap(XYChart.Series var1, XYChart.Data var2) {
      String var3 = this.orientation == Orientation.VERTICAL ? (String)var2.getXValue() : (String)var2.getYValue();
      Map var4 = (Map)this.seriesCategoryMap.get(var1);
      if (var4 != null) {
         var4.remove(var3);
         if (var4.isEmpty()) {
            this.seriesCategoryMap.remove(var1);
         }
      }

      if (this.seriesCategoryMap.isEmpty() && this.categoryAxis.isAutoRanging()) {
         this.categoryAxis.getCategories().clear();
      }

   }

   private void processDataRemove(XYChart.Series var1, XYChart.Data var2) {
      Node var3 = var2.getNode();
      this.getPlotChildren().remove(var3);
      this.updateMap(var1, var2);
   }

   private void animateDataAdd(XYChart.Data var1, Node var2) {
      double var3;
      if (this.orientation == Orientation.VERTICAL) {
         var3 = ((Number)var1.getYValue()).doubleValue();
         if (var3 < 0.0) {
            var2.getStyleClass().add(NEGATIVE_STYLE);
         }

         var1.setCurrentY(this.getYAxis().toRealValue(var3 < 0.0 ? -this.bottomPos : this.bottomPos));
         this.getPlotChildren().add(var2);
         var1.setYValue(this.getYAxis().toRealValue(var3));
         this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getCurrentY())}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getYValue(), Interpolator.EASE_BOTH)})});
      } else {
         var3 = ((Number)var1.getXValue()).doubleValue();
         if (var3 < 0.0) {
            var2.getStyleClass().add(NEGATIVE_STYLE);
         }

         var1.setCurrentX(this.getXAxis().toRealValue(var3 < 0.0 ? -this.bottomPos : this.bottomPos));
         this.getPlotChildren().add(var2);
         var1.setXValue(this.getXAxis().toRealValue(var3));
         this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentXProperty(), var1.getCurrentX())}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(var1.currentXProperty(), var1.getXValue(), Interpolator.EASE_BOTH)})});
      }

   }

   private Timeline createDataRemoveTimeline(XYChart.Data var1, Node var2, XYChart.Series var3) {
      Timeline var4 = new Timeline();
      if (this.orientation == Orientation.VERTICAL) {
         this.XYValueMap.put(var1, ((Number)var1.getYValue()).doubleValue());
         var1.setYValue(this.getYAxis().toRealValue(this.bottomPos));
         var4.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getCurrentY())}), new KeyFrame(Duration.millis(700.0), (var3x) -> {
            this.processDataRemove(var3, var1);
            this.XYValueMap.clear();
         }, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getYValue(), Interpolator.EASE_BOTH)}));
      } else {
         this.XYValueMap.put(var1, ((Number)var1.getXValue()).doubleValue());
         var1.setXValue(this.getXAxis().toRealValue(this.getXAxis().getZeroPosition()));
         var4.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentXProperty(), var1.getCurrentX())}), new KeyFrame(Duration.millis(700.0), (var3x) -> {
            this.processDataRemove(var3, var1);
            this.XYValueMap.clear();
         }, new KeyValue[]{new KeyValue(var1.currentXProperty(), var1.getXValue(), Interpolator.EASE_BOTH)}));
      }

      return var4;
   }

   void dataBeingRemovedIsAdded(XYChart.Data var1, XYChart.Series var2) {
      if (this.dataRemoveTimeline != null) {
         this.dataRemoveTimeline.setOnFinished((EventHandler)null);
         this.dataRemoveTimeline.stop();
      }

      this.processDataRemove(var2, var1);
      var1.setSeries((XYChart.Series)null);
      this.removeDataItemFromDisplay(var2, var1);
      this.restoreDataValues(var1);
      this.XYValueMap.clear();
   }

   private void restoreDataValues(XYChart.Data var1) {
      Double var2 = (Double)this.XYValueMap.get(var1);
      if (var2 != null) {
         if (this.orientation.equals(Orientation.VERTICAL)) {
            var1.setYValue(var2);
            var1.setCurrentY(var2);
         } else {
            var1.setXValue(var2);
            var1.setCurrentX(var2);
         }
      }

   }

   void seriesBeingRemovedIsAdded(XYChart.Series var1) {
      boolean var2 = this.pt.getChildren().size() == 1;
      if (this.pt != null) {
         Iterator var3;
         if (!this.pt.getChildren().isEmpty()) {
            var3 = this.pt.getChildren().iterator();

            while(var3.hasNext()) {
               Animation var4 = (Animation)var3.next();
               var4.setOnFinished((EventHandler)null);
            }
         }

         var3 = var1.getData().iterator();

         while(var3.hasNext()) {
            XYChart.Data var5 = (XYChart.Data)var3.next();
            this.processDataRemove(var1, var5);
            if (!var2) {
               this.restoreDataValues(var5);
            }
         }

         this.XYValueMap.clear();
         this.pt.setOnFinished((EventHandler)null);
         this.pt.getChildren().clear();
         this.pt.stop();
         this.removeSeriesFromDisplay(var1);
      }

   }

   private void updateDefaultColorIndex(XYChart.Series var1) {
      int var2 = (Integer)this.seriesColorMap.get(var1);
      Iterator var3 = var1.getData().iterator();

      while(var3.hasNext()) {
         XYChart.Data var4 = (XYChart.Data)var3.next();
         Node var5 = var4.getNode();
         if (var5 != null) {
            var5.getStyleClass().remove(DEFAULT_COLOR + var2);
         }
      }

   }

   private Node createBar(XYChart.Series var1, int var2, XYChart.Data var3, int var4) {
      Object var5 = var3.getNode();
      if (var5 == null) {
         var5 = new StackPane();
         ((Node)var5).setAccessibleRole(AccessibleRole.TEXT);
         ((Node)var5).setAccessibleRoleDescription("Bar");
         ((Node)var5).focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
         var3.setNode((Node)var5);
      }

      ((Node)var5).getStyleClass().addAll("chart-bar", "series" + var2, "data" + var4, var1.defaultColorStyleClass);
      return (Node)var5;
   }

   private XYChart.Data getDataItem(XYChart.Series var1, int var2, int var3, String var4) {
      Map var5 = (Map)this.seriesCategoryMap.get(var1);
      return var5 != null ? (XYChart.Data)var5.get(var4) : null;
   }

   public static List getClassCssMetaData() {
      return BarChart.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData BAR_GAP = new CssMetaData("-fx-bar-gap", SizeConverter.getInstance(), 4.0) {
         public boolean isSettable(BarChart var1) {
            return var1.barGap == null || !var1.barGap.isBound();
         }

         public StyleableProperty getStyleableProperty(BarChart var1) {
            return (StyleableProperty)var1.barGapProperty();
         }
      };
      private static final CssMetaData CATEGORY_GAP = new CssMetaData("-fx-category-gap", SizeConverter.getInstance(), 10.0) {
         public boolean isSettable(BarChart var1) {
            return var1.categoryGap == null || !var1.categoryGap.isBound();
         }

         public StyleableProperty getStyleableProperty(BarChart var1) {
            return (StyleableProperty)var1.categoryGapProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(XYChart.getClassCssMetaData());
         var0.add(BAR_GAP);
         var0.add(CATEGORY_GAP);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
