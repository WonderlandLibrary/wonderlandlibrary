package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.SizeConverter;
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
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class StackedBarChart extends XYChart {
   private Map seriesCategoryMap;
   private Legend legend;
   private final Orientation orientation;
   private CategoryAxis categoryAxis;
   private ValueAxis valueAxis;
   private int seriesDefaultColorIndex;
   private Map seriesDefaultColorMap;
   private ListChangeListener categoriesListener;
   private DoubleProperty categoryGap;
   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

   public double getCategoryGap() {
      return this.categoryGap.getValue();
   }

   public void setCategoryGap(double var1) {
      this.categoryGap.setValue((Number)var1);
   }

   public DoubleProperty categoryGapProperty() {
      return this.categoryGap;
   }

   public StackedBarChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2) {
      this(var1, var2, FXCollections.observableArrayList());
   }

   public StackedBarChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2, @NamedArg("data") ObservableList var3) {
      super(var1, var2);
      this.seriesCategoryMap = new HashMap();
      this.legend = new Legend();
      this.seriesDefaultColorIndex = 0;
      this.seriesDefaultColorMap = new HashMap();
      this.categoriesListener = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               Iterator var2 = var1.getRemoved().iterator();

               while(var2.hasNext()) {
                  String var3 = (String)var2.next();
                  Iterator var4 = StackedBarChart.this.getData().iterator();

                  while(var4.hasNext()) {
                     XYChart.Series var5 = (XYChart.Series)var4.next();
                     Iterator var6 = var5.getData().iterator();

                     while(var6.hasNext()) {
                        XYChart.Data var7 = (XYChart.Data)var6.next();
                        Orientation var10001 = StackedBarChart.this.orientation;
                        StackedBarChart.this.orientation;
                        if (var3.equals(var10001 == Orientation.VERTICAL ? var7.getXValue() : var7.getYValue())) {
                           boolean var8 = StackedBarChart.this.getAnimated();
                           StackedBarChart.this.setAnimated(false);
                           StackedBarChart.this.dataItemRemoved(var7, var5);
                           StackedBarChart.this.setAnimated(var8);
                        }
                     }
                  }

                  StackedBarChart.this.requestChartLayout();
               }
            }

         }
      };
      this.categoryGap = new StyleableDoubleProperty(10.0) {
         protected void invalidated() {
            this.get();
            StackedBarChart.this.requestChartLayout();
         }

         public Object getBean() {
            return StackedBarChart.this;
         }

         public String getName() {
            return "categoryGap";
         }

         public CssMetaData getCssMetaData() {
            return StackedBarChart.StyleableProperties.CATEGORY_GAP;
         }
      };
      this.getStyleClass().add("stacked-bar-chart");
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
         this.categoryAxis.getCategories().addListener(this.categoriesListener);
      } else {
         throw new IllegalArgumentException("Axis type incorrect, one of X,Y should be CategoryAxis and the other NumberAxis");
      }
   }

   public StackedBarChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2, @NamedArg("data") ObservableList var3, @NamedArg("categoryGap") double var4) {
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

      Object var6 = ((Map)var5).get(var4) != null ? (List)((Map)var5).get(var4) : new ArrayList();
      ((List)var6).add(var3);
      ((Map)var5).put(var4, var6);
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
         Timeline var4 = this.createDataRemoveTimeline(var1, var3, var2);
         var4.setOnFinished((var3x) -> {
            this.removeDataItemFromDisplay(var2, var1);
         });
         var4.play();
      } else {
         this.getPlotChildren().remove(var3);
         this.removeDataItemFromDisplay(var2, var1);
      }

   }

   protected void dataItemChanged(XYChart.Data var1) {
      double var2;
      double var4;
      if (this.orientation == Orientation.VERTICAL) {
         var2 = ((Number)var1.getYValue()).doubleValue();
         var4 = ((Number)this.getCurrentDisplayedYValue(var1)).doubleValue();
      } else {
         var2 = ((Number)var1.getXValue()).doubleValue();
         var4 = ((Number)this.getCurrentDisplayedXValue(var1)).doubleValue();
      }

      if (var4 > 0.0 && var2 < 0.0) {
         var1.getNode().getStyleClass().add("negative");
      } else if (var4 < 0.0 && var2 > 0.0) {
         var1.getNode().getStyleClass().remove("negative");
      }

   }

   private void animateDataAdd(XYChart.Data var1, Node var2) {
      double var3;
      if (this.orientation == Orientation.VERTICAL) {
         var3 = ((Number)var1.getYValue()).doubleValue();
         if (var3 < 0.0) {
            var2.getStyleClass().add("negative");
         }

         var1.setYValue(this.getYAxis().toRealValue(this.getYAxis().getZeroPosition()));
         this.setCurrentDisplayedYValue(var1, this.getYAxis().toRealValue(this.getYAxis().getZeroPosition()));
         this.getPlotChildren().add(var2);
         var1.setYValue(this.getYAxis().toRealValue(var3));
         this.animate(new Timeline(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(this.currentDisplayedYValueProperty(var1), this.getCurrentDisplayedYValue(var1))}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(this.currentDisplayedYValueProperty(var1), var1.getYValue(), Interpolator.EASE_BOTH)})}));
      } else {
         var3 = ((Number)var1.getXValue()).doubleValue();
         if (var3 < 0.0) {
            var2.getStyleClass().add("negative");
         }

         var1.setXValue(this.getXAxis().toRealValue(this.getXAxis().getZeroPosition()));
         this.setCurrentDisplayedXValue(var1, this.getXAxis().toRealValue(this.getXAxis().getZeroPosition()));
         this.getPlotChildren().add(var2);
         var1.setXValue(this.getXAxis().toRealValue(var3));
         this.animate(new Timeline(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(this.currentDisplayedXValueProperty(var1), this.getCurrentDisplayedXValue(var1))}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(this.currentDisplayedXValueProperty(var1), var1.getXValue(), Interpolator.EASE_BOTH)})}));
      }

   }

   protected void seriesAdded(XYChart.Series var1, int var2) {
      String var3 = "default-color" + this.seriesDefaultColorIndex % 8;
      this.seriesDefaultColorMap.put(var1, var3);
      ++this.seriesDefaultColorIndex;
      HashMap var4 = new HashMap();

      for(int var5 = 0; var5 < var1.getData().size(); ++var5) {
         XYChart.Data var6 = (XYChart.Data)var1.getData().get(var5);
         Node var7 = this.createBar(var1, var2, var6, var5);
         String var8;
         if (this.orientation == Orientation.VERTICAL) {
            var8 = (String)var6.getXValue();
         } else {
            var8 = (String)var6.getYValue();
         }

         Object var9 = var4.get(var8) != null ? (List)var4.get(var8) : new ArrayList();
         ((List)var9).add(var6);
         var4.put(var8, var9);
         if (this.shouldAnimate()) {
            this.animateDataAdd(var6, var7);
         } else {
            double var10 = this.orientation == Orientation.VERTICAL ? ((Number)var6.getYValue()).doubleValue() : ((Number)var6.getXValue()).doubleValue();
            if (var10 < 0.0) {
               var7.getStyleClass().add("negative");
            }

            this.getPlotChildren().add(var7);
         }
      }

      if (var4.size() > 0) {
         this.seriesCategoryMap.put(var1, var4);
      }

   }

   private Timeline createDataRemoveTimeline(XYChart.Data var1, Node var2, XYChart.Series var3) {
      Timeline var4 = new Timeline();
      if (this.orientation == Orientation.VERTICAL) {
         var1.setYValue(this.getYAxis().toRealValue(this.getYAxis().getZeroPosition()));
         var4.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(this.currentDisplayedYValueProperty(var1), this.getCurrentDisplayedYValue(var1))}), new KeyFrame(Duration.millis(700.0), (var2x) -> {
            this.getPlotChildren().remove(var2);
         }, new KeyValue[]{new KeyValue(this.currentDisplayedYValueProperty(var1), var1.getYValue(), Interpolator.EASE_BOTH)}));
      } else {
         var1.setXValue(this.getXAxis().toRealValue(this.getXAxis().getZeroPosition()));
         var4.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(this.currentDisplayedXValueProperty(var1), this.getCurrentDisplayedXValue(var1))}), new KeyFrame(Duration.millis(700.0), (var2x) -> {
            this.getPlotChildren().remove(var2);
         }, new KeyValue[]{new KeyValue(this.currentDisplayedXValueProperty(var1), var1.getXValue(), Interpolator.EASE_BOTH)}));
      }

      return var4;
   }

   protected void seriesRemoved(XYChart.Series var1) {
      --this.seriesDefaultColorIndex;
      if (this.shouldAnimate()) {
         ParallelTransition var2 = new ParallelTransition();
         var2.setOnFinished((var2x) -> {
            this.removeSeriesFromDisplay(var1);
            this.requestChartLayout();
         });
         Iterator var3 = var1.getData().iterator();

         while(true) {
            while(var3.hasNext()) {
               XYChart.Data var4 = (XYChart.Data)var3.next();
               Node var5 = var4.getNode();
               if (this.getSeriesSize() > 1) {
                  for(int var12 = 0; var12 < var1.getData().size(); ++var12) {
                     XYChart.Data var7 = (XYChart.Data)var1.getData().get(var12);
                     Timeline var8 = this.createDataRemoveTimeline(var7, var5, var1);
                     var2.getChildren().add(var8);
                  }
               } else {
                  FadeTransition var6 = new FadeTransition(Duration.millis(700.0), var5);
                  var6.setFromValue(1.0);
                  var6.setToValue(0.0);
                  var6.setOnFinished((var2x) -> {
                     this.getPlotChildren().remove(var5);
                     var5.setOpacity(1.0);
                  });
                  var2.getChildren().add(var6);
               }
            }

            var2.play();
            break;
         }
      } else {
         Iterator var9 = var1.getData().iterator();

         while(var9.hasNext()) {
            XYChart.Data var10 = (XYChart.Data)var9.next();
            Node var11 = var10.getNode();
            this.getPlotChildren().remove(var11);
         }

         this.removeSeriesFromDisplay(var1);
         this.requestChartLayout();
      }

   }

   protected void updateAxisRange() {
      boolean var1 = this.categoryAxis == this.getXAxis();
      ArrayList var2;
      Iterator var3;
      if (this.categoryAxis.isAutoRanging()) {
         var2 = new ArrayList();
         var3 = this.getData().iterator();

         while(var3.hasNext()) {
            XYChart.Series var4 = (XYChart.Series)var3.next();
            Iterator var5 = var4.getData().iterator();

            while(var5.hasNext()) {
               XYChart.Data var6 = (XYChart.Data)var5.next();
               if (var6 != null) {
                  var2.add(var1 ? var6.getXValue() : var6.getYValue());
               }
            }
         }

         this.categoryAxis.invalidateRange(var2);
      }

      if (this.valueAxis.isAutoRanging()) {
         var2 = new ArrayList();
         var3 = this.categoryAxis.getAllDataCategories().iterator();

         while(var3.hasNext()) {
            String var15 = (String)var3.next();
            double var16 = 0.0;
            double var7 = 0.0;
            Iterator var9 = this.getDisplayedSeriesIterator();

            while(var9.hasNext()) {
               XYChart.Series var10 = (XYChart.Series)var9.next();
               Iterator var11 = this.getDataItem(var10, var15).iterator();

               while(var11.hasNext()) {
                  XYChart.Data var12 = (XYChart.Data)var11.next();
                  if (var12 != null) {
                     boolean var13 = var12.getNode().getStyleClass().contains("negative");
                     Number var14 = (Number)((Number)(var1 ? var12.getYValue() : var12.getXValue()));
                     if (!var13) {
                        var7 += this.valueAxis.toNumericValue(var14);
                     } else {
                        var16 += this.valueAxis.toNumericValue(var14);
                     }
                  }
               }
            }

            var2.add(var7);
            var2.add(var16);
         }

         this.valueAxis.invalidateRange(var2);
      }

   }

   protected void layoutPlotChildren() {
      double var1 = this.categoryAxis.getCategorySpacing();
      double var3 = var1 - this.getCategoryGap();
      double var5 = var3;
      double var7 = -((var1 - this.getCategoryGap()) / 2.0);
      double var9 = this.valueAxis.getLowerBound();
      double var11 = this.valueAxis.getUpperBound();
      Iterator var13 = this.categoryAxis.getCategories().iterator();

      while(var13.hasNext()) {
         String var14 = (String)var13.next();
         double var15 = 0.0;
         double var17 = 0.0;
         Iterator var19 = this.getDisplayedSeriesIterator();

         while(var19.hasNext()) {
            XYChart.Series var20 = (XYChart.Series)var19.next();
            Iterator var21 = this.getDataItem(var20, var14).iterator();

            while(var21.hasNext()) {
               XYChart.Data var22 = (XYChart.Data)var21.next();
               if (var22 != null) {
                  Node var23 = var22.getNode();
                  Object var28 = this.getCurrentDisplayedXValue(var22);
                  Object var29 = this.getCurrentDisplayedYValue(var22);
                  double var24;
                  double var26;
                  if (this.orientation == Orientation.VERTICAL) {
                     var24 = this.getXAxis().getDisplayPosition(var28);
                     var26 = this.getYAxis().toNumericValue(var29);
                  } else {
                     var24 = this.getYAxis().getDisplayPosition(var29);
                     var26 = this.getXAxis().toNumericValue(var28);
                  }

                  boolean var34 = var23.getStyleClass().contains("negative");
                  double var32;
                  double var30;
                  if (!var34) {
                     var30 = this.valueAxis.getDisplayPosition((Number)var15);
                     var32 = this.valueAxis.getDisplayPosition((Number)(var15 + var26));
                     var15 += var26;
                  } else {
                     var30 = this.valueAxis.getDisplayPosition((Number)(var17 + var26));
                     var32 = this.valueAxis.getDisplayPosition((Number)var17);
                     var17 += var26;
                  }

                  if (this.orientation == Orientation.VERTICAL) {
                     var23.resizeRelocate(var24 + var7, var32, var5, var30 - var32);
                  } else {
                     var23.resizeRelocate(var30, var24 + var7, var32 - var30, var5);
                  }
               }
            }
         }
      }

   }

   int getSeriesSize() {
      int var1 = 0;

      for(Iterator var2 = this.getDisplayedSeriesIterator(); var2.hasNext(); ++var1) {
         var2.next();
      }

      return var1;
   }

   protected void updateLegend() {
      this.legend.getItems().clear();
      if (this.getData() != null) {
         for(int var1 = 0; var1 < this.getData().size(); ++var1) {
            XYChart.Series var2 = (XYChart.Series)this.getData().get(var1);
            Legend.LegendItem var3 = new Legend.LegendItem(var2.getName());
            String var4 = (String)this.seriesDefaultColorMap.get(var2);
            var3.getSymbol().getStyleClass().addAll("chart-bar", "series" + var1, "bar-legend-symbol", var4);
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

   private Node createBar(XYChart.Series var1, int var2, XYChart.Data var3, int var4) {
      Object var5 = var3.getNode();
      if (var5 == null) {
         var5 = new StackPane();
         ((Node)var5).setAccessibleRole(AccessibleRole.TEXT);
         ((Node)var5).setAccessibleRoleDescription("Bar");
         ((Node)var5).focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
         var3.setNode((Node)var5);
      }

      String var6 = (String)this.seriesDefaultColorMap.get(var1);
      ((Node)var5).getStyleClass().setAll((Object[])("chart-bar", "series" + var2, "data" + var4, var6));
      return (Node)var5;
   }

   private List getDataItem(XYChart.Series var1, String var2) {
      Map var3 = (Map)this.seriesCategoryMap.get(var1);
      return (List)(var3 != null ? (var3.get(var2) != null ? (List)var3.get(var2) : new ArrayList()) : new ArrayList());
   }

   public static List getClassCssMetaData() {
      return StackedBarChart.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData CATEGORY_GAP = new CssMetaData("-fx-category-gap", SizeConverter.getInstance(), 10.0) {
         public boolean isSettable(StackedBarChart var1) {
            return var1.categoryGap == null || !var1.categoryGap.isBound();
         }

         public StyleableProperty getStyleableProperty(StackedBarChart var1) {
            return (StyleableProperty)var1.categoryGapProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(XYChart.getClassCssMetaData());
         var0.add(CATEGORY_GAP);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
