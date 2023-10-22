package javafx.scene.chart;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

public class StackedAreaChart extends XYChart {
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

   public StackedAreaChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2) {
      this(var1, var2, FXCollections.observableArrayList());
   }

   public StackedAreaChart(@NamedArg("xAxis") Axis var1, @NamedArg("yAxis") Axis var2, @NamedArg("data") ObservableList var3) {
      super(var1, var2);
      this.seriesYMultiplierMap = new HashMap();
      this.legend = new Legend();
      this.createSymbols = new StyleableBooleanProperty(true) {
         protected void invalidated() {
            for(int var1 = 0; var1 < StackedAreaChart.this.getData().size(); ++var1) {
               XYChart.Series var2 = (XYChart.Series)StackedAreaChart.this.getData().get(var1);

               for(int var3 = 0; var3 < var2.getData().size(); ++var3) {
                  XYChart.Data var4 = (XYChart.Data)var2.getData().get(var3);
                  Node var5 = var4.getNode();
                  if (this.get() && var5 == null) {
                     var5 = StackedAreaChart.this.createSymbol(var2, StackedAreaChart.this.getData().indexOf(var2), var4, var3);
                     if (null != var5) {
                        StackedAreaChart.this.getPlotChildren().add(var5);
                     }
                  } else if (!this.get() && var5 != null) {
                     StackedAreaChart.this.getPlotChildren().remove(var5);
                     var5 = null;
                     var4.setNode((Node)null);
                  }
               }
            }

            StackedAreaChart.this.requestChartLayout();
         }

         public Object getBean() {
            return this;
         }

         public String getName() {
            return "createSymbols";
         }

         public CssMetaData getCssMetaData() {
            return StackedAreaChart.StyleableProperties.CREATE_SYMBOLS;
         }
      };
      if (!(var2 instanceof ValueAxis)) {
         throw new IllegalArgumentException("Axis type incorrect, yAxis must be of ValueAxis type.");
      } else {
         this.setLegend(this.legend);
         this.setData(var3);
      }
   }

   private static double doubleValue(Number var0) {
      return doubleValue(var0, 0.0);
   }

   private static double doubleValue(Number var0, double var1) {
      return var0 == null ? var1 : var0.doubleValue();
   }

   protected void dataItemAdded(XYChart.Series var1, int var2, XYChart.Data var3) {
      Node var4 = this.createSymbol(var1, this.getData().indexOf(var1), var3, var2);
      if (this.shouldAnimate()) {
         boolean var5 = false;
         if (var2 > 0 && var2 < var1.getData().size() - 1) {
            var5 = true;
            XYChart.Data var23 = (XYChart.Data)var1.getData().get(var2 - 1);
            XYChart.Data var7 = (XYChart.Data)var1.getData().get(var2 + 1);
            double var8 = this.getXAxis().toNumericValue(var23.getXValue());
            double var10 = this.getYAxis().toNumericValue(var23.getYValue());
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
               var3.setOpacity(1.0);
            });
            var8.play();
         }

         if (var5) {
            this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getCurrentY()), new KeyValue(var1.currentXProperty(), var1.getCurrentX())}), new KeyFrame(Duration.millis(800.0), (var4x) -> {
               this.getPlotChildren().remove(var3);
               this.removeDataItemFromDisplay(var2, var1);
            }, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getYValue(), Interpolator.EASE_BOTH), new KeyValue(var1.currentXProperty(), var1.getXValue(), Interpolator.EASE_BOTH)})});
         }
      } else {
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
      var4.setStrokeLineJoin(StrokeLineJoin.BEVEL);
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
            }

            this.getPlotChildren().add(var10);
            if (this.shouldAnimate()) {
               var7.add(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var10.opacityProperty(), 0)}));
               var7.add(new KeyFrame(Duration.millis(200.0), new KeyValue[]{new KeyValue(var10.opacityProperty(), 1)}));
            }
         }
      }

      if (this.shouldAnimate()) {
         this.animate((KeyFrame[])var7.toArray(new KeyFrame[var7.size()]));
      }

   }

   protected void seriesRemoved(XYChart.Series var1) {
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

   protected void updateAxisRange() {
      Axis var1 = this.getXAxis();
      Axis var2 = this.getYAxis();
      if (var1.isAutoRanging()) {
         ArrayList var3 = new ArrayList();
         Iterator var4 = this.getData().iterator();

         while(var4.hasNext()) {
            XYChart.Series var5 = (XYChart.Series)var4.next();
            Iterator var6 = var5.getData().iterator();

            while(var6.hasNext()) {
               XYChart.Data var7 = (XYChart.Data)var6.next();
               var3.add(var7.getXValue());
            }
         }

         var1.invalidateRange(var3);
      }

      if (var2.isAutoRanging()) {
         double var19 = Double.MAX_VALUE;
         Iterator var20 = this.getDisplayedSeriesIterator();
         boolean var21 = true;
         TreeMap var22 = new TreeMap();
         TreeMap var8 = new TreeMap();

         label97:
         for(TreeMap var9 = new TreeMap(); var20.hasNext(); var21 = var19 == Double.MAX_VALUE) {
            var9.clear();
            XYChart.Series var10 = (XYChart.Series)var20.next();
            Iterator var11 = var10.getData().iterator();

            while(true) {
               while(true) {
                  XYChart.Data var12;
                  do {
                     if (!var11.hasNext()) {
                        var11 = var8.entrySet().iterator();

                        while(true) {
                           while(true) {
                              Map.Entry var23;
                              do {
                                 if (!var11.hasNext()) {
                                    var8.clear();
                                    var8.putAll(var22);
                                    var22.clear();
                                    continue label97;
                                 }

                                 var23 = (Map.Entry)var11.next();
                              } while(var22.keySet().contains(var23.getKey()));

                              Double var24 = (Double)var23.getKey();
                              Double var14 = (Double)var23.getValue();
                              Map.Entry var25 = var9.higherEntry(var24);
                              Map.Entry var16 = var9.lowerEntry(var24);
                              if (var25 != null && var16 != null) {
                                 var22.put(var24, (var24 - (Double)var16.getKey()) / ((Double)var25.getKey() - (Double)var16.getKey()) * ((Double)var16.getValue() + (Double)var25.getValue()) + var14);
                              } else if (var25 != null) {
                                 var22.put(var24, (Double)var25.getValue() + var14);
                              } else if (var16 != null) {
                                 var22.put(var24, (Double)var16.getValue() + var14);
                              } else {
                                 var22.put(var24, var14);
                              }
                           }
                        }
                     }

                     var12 = (XYChart.Data)var11.next();
                  } while(var12 == null);

                  double var13 = var1.toNumericValue(var12.getXValue());
                  double var15 = var2.toNumericValue(var12.getYValue());
                  var9.put(var13, var15);
                  if (var21) {
                     var22.put(var13, var15);
                     var19 = Math.min(var19, var15);
                  } else if (var8.containsKey(var13)) {
                     var22.put(var13, (Double)var8.get(var13) + var15);
                  } else {
                     Map.Entry var17 = var8.higherEntry(var13);
                     Map.Entry var18 = var8.lowerEntry(var13);
                     if (var17 != null && var18 != null) {
                        var22.put(var13, (var13 - (Double)var18.getKey()) / ((Double)var17.getKey() - (Double)var18.getKey()) * ((Double)var18.getValue() + (Double)var17.getValue()) + var15);
                     } else if (var17 != null) {
                        var22.put(var13, (Double)var17.getValue() + var15);
                     } else if (var18 != null) {
                        var22.put(var13, (Double)var18.getValue() + var15);
                     } else {
                        var22.put(var13, var15);
                     }
                  }
               }
            }
         }

         if (var19 != Double.MAX_VALUE) {
            var2.invalidateRange(Arrays.asList(var2.toRealValue(var19), var2.toRealValue((Double)Collections.max(var8.values()))));
         }
      }

   }

   protected void layoutPlotChildren() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.getDataSize(); ++var3) {
         XYChart.Series var4 = (XYChart.Series)this.getData().get(var3);
         var2.clear();
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            DataPointInfo var6 = (DataPointInfo)var5.next();
            var6.partOf = StackedAreaChart.PartOf.PREVIOUS;
            var2.add(var6);
         }

         var1.clear();
         var5 = this.getDisplayedDataIterator(var4);

         while(var5.hasNext()) {
            XYChart.Data var36 = (XYChart.Data)var5.next();
            DataPointInfo var7 = new DataPointInfo(var36, var36.getXValue(), var36.getYValue(), StackedAreaChart.PartOf.CURRENT);
            var2.add(var7);
         }

         DoubleProperty var35 = (DoubleProperty)this.seriesYMultiplierMap.get(var4);
         Path var37 = (Path)((Group)var4.getNode()).getChildren().get(1);
         Path var38 = (Path)((Group)var4.getNode()).getChildren().get(0);
         var37.getElements().clear();
         var38.getElements().clear();
         int var8 = 0;
         this.sortAggregateList(var2);
         Axis var9 = this.getYAxis();
         Axis var10 = this.getXAxis();
         boolean var11 = false;
         boolean var12 = false;
         int var13 = this.findNextCurrent(var2, -1);
         int var14 = this.findPreviousCurrent(var2, var2.size());
         double var15 = var9.getZeroPosition();
         if (Double.isNaN(var15)) {
            ValueAxis var17 = (ValueAxis)var9;
            if (var17.getLowerBound() > 0.0) {
               var15 = var17.getDisplayPosition((Number)var17.getLowerBound());
            } else {
               var15 = var17.getDisplayPosition((Number)var17.getUpperBound());
            }
         }

         Iterator var39 = var2.iterator();

         DataPointInfo var18;
         while(var39.hasNext()) {
            var18 = (DataPointInfo)var39.next();
            if (var8 == var14) {
               var12 = true;
            }

            if (var8 == var13) {
               var11 = true;
            }

            XYChart.Data var19 = var18.dataItem;
            int var20;
            int var21;
            DataPointInfo var22;
            DataPointInfo var23;
            double var24;
            double var26;
            double var28;
            double var30;
            if (!var18.partOf.equals(StackedAreaChart.PartOf.CURRENT)) {
               var20 = this.findPreviousCurrent(var2, var8);
               var21 = this.findNextCurrent(var2, var8);
               if (var18.dropDown) {
                  if (var10.toNumericValue(var18.x) <= var10.toNumericValue(((DataPointInfo)var2.get(var13)).x) || var10.toNumericValue(var18.x) > var10.toNumericValue(((DataPointInfo)var2.get(var14)).x)) {
                     this.addDropDown(var1, var19, var18.x, var18.y, var18.displayX, var18.displayY);
                  }
               } else if (var20 != -1 && var21 != -1) {
                  var23 = (DataPointInfo)var2.get(var21);
                  if (!var23.x.equals(var18.x)) {
                     var22 = (DataPointInfo)var2.get(var20);
                     var24 = var10.getDisplayPosition(var19.getCurrentX());
                     var26 = this.interpolate(var10.toNumericValue(var22.x), var9.toNumericValue(var22.y), var10.toNumericValue(var23.x), var9.toNumericValue(var23.y), var10.toNumericValue(var18.x));
                     var28 = var9.toNumericValue(var18.y) + var26;
                     var30 = var9.getDisplayPosition(var9.toRealValue(var28 * var35.getValue()));
                     this.addPoint(var1, new XYChart.Data(var18.x, var26), var18.x, var9.toRealValue(var28), var24, var30, StackedAreaChart.PartOf.CURRENT, true, true);
                  }
               } else {
                  this.addPoint(var1, var19, var18.x, var18.y, var18.displayX, var18.displayY, StackedAreaChart.PartOf.CURRENT, true, false);
               }
            } else {
               var20 = this.findPreviousPrevious(var2, var8);
               var21 = this.findNextPrevious(var2, var8);
               if (var20 == -1 || var21 == -1 && !((DataPointInfo)var2.get(var20)).x.equals(var18.x)) {
                  if (var11) {
                     XYChart.Data var44 = new XYChart.Data(var18.x, 0);
                     this.addDropDown(var1, var44, var44.getXValue(), var44.getYValue(), var10.getDisplayPosition(var44.getCurrentX()), var15);
                  }

                  var24 = var10.getDisplayPosition(var19.getCurrentX());
                  var26 = var9.getDisplayPosition(var9.toRealValue(var9.toNumericValue(var19.getCurrentY()) * var35.getValue()));
                  this.addPoint(var1, var19, var19.getXValue(), var19.getYValue(), var24, var26, StackedAreaChart.PartOf.CURRENT, false, !var11);
                  if (var8 == var14) {
                     XYChart.Data var45 = new XYChart.Data(var18.x, 0);
                     this.addDropDown(var1, var45, var45.getXValue(), var45.getYValue(), var10.getDisplayPosition(var45.getCurrentX()), var15);
                  }
               } else {
                  var22 = (DataPointInfo)var2.get(var20);
                  if (var22.x.equals(var18.x)) {
                     if (var22.dropDown) {
                        var20 = this.findPreviousPrevious(var2, var20);
                        var22 = (DataPointInfo)var2.get(var20);
                     }

                     if (var22.x.equals(var18.x)) {
                        var24 = var10.getDisplayPosition(var19.getCurrentX());
                        var26 = var9.toNumericValue(var19.getCurrentY()) + var9.toNumericValue(var22.y);
                        var28 = var9.getDisplayPosition(var9.toRealValue(var26 * var35.getValue()));
                        this.addPoint(var1, var19, var18.x, var9.toRealValue(var26), var24, var28, StackedAreaChart.PartOf.CURRENT, false, !var11);
                     }

                     if (var12) {
                        this.addDropDown(var1, var19, var22.x, var22.y, var22.displayX, var22.displayY);
                     }
                  } else {
                     var23 = var21 == -1 ? null : (DataPointInfo)var2.get(var21);
                     var22 = var20 == -1 ? null : (DataPointInfo)var2.get(var20);
                     var24 = var9.toNumericValue(var19.getCurrentY());
                     if (var22 != null && var23 != null) {
                        var26 = var10.getDisplayPosition(var19.getCurrentX());
                        var28 = this.interpolate(var22.displayX, var22.displayY, var23.displayX, var23.displayY, var26);
                        var30 = this.interpolate(var10.toNumericValue(var22.x), var9.toNumericValue(var22.y), var10.toNumericValue(var23.x), var9.toNumericValue(var23.y), var10.toNumericValue(var18.x));
                        if (var11) {
                           XYChart.Data var32 = new XYChart.Data(var18.x, var30);
                           this.addDropDown(var1, var32, var18.x, var9.toRealValue(var30), var26, var28);
                        }

                        double var46 = var9.getDisplayPosition(var9.toRealValue((var24 + var30) * var35.getValue()));
                        this.addPoint(var1, var19, var18.x, var9.toRealValue(var24 + var30), var26, var46, StackedAreaChart.PartOf.CURRENT, false, !var11);
                        if (var8 == var14) {
                           XYChart.Data var34 = new XYChart.Data(var18.x, var30);
                           this.addDropDown(var1, var34, var18.x, var9.toRealValue(var30), var26, var28);
                        }
                     }
                  }
               }
            }

            ++var8;
            if (var11) {
               var11 = false;
            }

            if (var12) {
               var12 = false;
            }
         }

         if (!var1.isEmpty()) {
            var37.getElements().add(new MoveTo(((DataPointInfo)var1.get(0)).displayX, ((DataPointInfo)var1.get(0)).displayY));
            var38.getElements().add(new MoveTo(((DataPointInfo)var1.get(0)).displayX, ((DataPointInfo)var1.get(0)).displayY));
         }

         var39 = var1.iterator();

         while(var39.hasNext()) {
            var18 = (DataPointInfo)var39.next();
            if (var18.lineTo) {
               var37.getElements().add(new LineTo(var18.displayX, var18.displayY));
            } else {
               var37.getElements().add(new MoveTo(var18.displayX, var18.displayY));
            }

            var38.getElements().add(new LineTo(var18.displayX, var18.displayY));
            if (!var18.skipSymbol) {
               Node var41 = var18.dataItem.getNode();
               if (var41 != null) {
                  double var42 = var41.prefWidth(-1.0);
                  double var43 = var41.prefHeight(-1.0);
                  var41.resizeRelocate(var18.displayX - var42 / 2.0, var18.displayY - var43 / 2.0, var42, var43);
               }
            }
         }

         for(int var40 = var2.size() - 1; var40 > 0; --var40) {
            var18 = (DataPointInfo)var2.get(var40);
            if (StackedAreaChart.PartOf.PREVIOUS.equals(var18.partOf)) {
               var38.getElements().add(new LineTo(var18.displayX, var18.displayY));
            }
         }

         if (!var38.getElements().isEmpty()) {
            var38.getElements().add(new ClosePath());
         }
      }

   }

   private void addDropDown(ArrayList var1, XYChart.Data var2, Object var3, Object var4, double var5, double var7) {
      DataPointInfo var9 = new DataPointInfo(true);
      var9.setValues(var2, var3, var4, var5, var7, StackedAreaChart.PartOf.CURRENT, true, false);
      var1.add(var9);
   }

   private void addPoint(ArrayList var1, XYChart.Data var2, Object var3, Object var4, double var5, double var7, PartOf var9, boolean var10, boolean var11) {
      DataPointInfo var12 = new DataPointInfo();
      var12.setValues(var2, var3, var4, var5, var7, var9, var10, var11);
      var1.add(var12);
   }

   private int findNextCurrent(ArrayList var1, int var2) {
      for(int var3 = var2 + 1; var3 < var1.size(); ++var3) {
         if (((DataPointInfo)var1.get(var3)).partOf.equals(StackedAreaChart.PartOf.CURRENT)) {
            return var3;
         }
      }

      return -1;
   }

   private int findPreviousCurrent(ArrayList var1, int var2) {
      for(int var3 = var2 - 1; var3 >= 0; --var3) {
         if (((DataPointInfo)var1.get(var3)).partOf.equals(StackedAreaChart.PartOf.CURRENT)) {
            return var3;
         }
      }

      return -1;
   }

   private int findPreviousPrevious(ArrayList var1, int var2) {
      for(int var3 = var2 - 1; var3 >= 0; --var3) {
         if (((DataPointInfo)var1.get(var3)).partOf.equals(StackedAreaChart.PartOf.PREVIOUS)) {
            return var3;
         }
      }

      return -1;
   }

   private int findNextPrevious(ArrayList var1, int var2) {
      for(int var3 = var2 + 1; var3 < var1.size(); ++var3) {
         if (((DataPointInfo)var1.get(var3)).partOf.equals(StackedAreaChart.PartOf.PREVIOUS)) {
            return var3;
         }
      }

      return -1;
   }

   private void sortAggregateList(ArrayList var1) {
      Collections.sort(var1, (var1x, var2) -> {
         XYChart.Data var3 = var1x.dataItem;
         XYChart.Data var4 = var2.dataItem;
         double var5 = this.getXAxis().toNumericValue(var3.getXValue());
         double var7 = this.getXAxis().toNumericValue(var4.getXValue());
         return var5 < var7 ? -1 : (var5 == var7 ? 0 : 1);
      });
   }

   private double interpolate(double var1, double var3, double var5, double var7, double var9) {
      return (var7 - var3) / (var5 - var1) * (var9 - var1) + var3;
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
      return StackedAreaChart.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData CREATE_SYMBOLS;
      private static final List STYLEABLES;

      static {
         CREATE_SYMBOLS = new CssMetaData("-fx-create-symbols", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(StackedAreaChart var1) {
               return var1.createSymbols == null || !var1.createSymbols.isBound();
            }

            public StyleableProperty getStyleableProperty(StackedAreaChart var1) {
               return (StyleableProperty)var1.createSymbolsProperty();
            }
         };
         ArrayList var0 = new ArrayList(XYChart.getClassCssMetaData());
         var0.add(CREATE_SYMBOLS);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   private static enum PartOf {
      CURRENT,
      PREVIOUS;
   }

   static final class DataPointInfo {
      Object x;
      Object y;
      double displayX;
      double displayY;
      XYChart.Data dataItem;
      PartOf partOf;
      boolean skipSymbol = false;
      boolean lineTo = false;
      boolean dropDown = false;

      DataPointInfo() {
      }

      DataPointInfo(XYChart.Data var1, Object var2, Object var3, PartOf var4) {
         this.dataItem = var1;
         this.x = var2;
         this.y = var3;
         this.partOf = var4;
      }

      DataPointInfo(boolean var1) {
         this.dropDown = var1;
      }

      void setValues(XYChart.Data var1, Object var2, Object var3, double var4, double var6, PartOf var8, boolean var9, boolean var10) {
         this.dataItem = var1;
         this.x = var2;
         this.y = var3;
         this.displayX = var4;
         this.displayY = var6;
         this.partOf = var8;
         this.skipSymbol = var9;
         this.lineTo = var10;
      }

      public final Object getX() {
         return this.x;
      }

      public final Object getY() {
         return this.y;
      }
   }
}
