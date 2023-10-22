package javafx.scene.chart;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.BooleanConverter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public abstract class XYChart extends Chart {
   private final BitSet colorBits = new BitSet(8);
   static String DEFAULT_COLOR = "default-color";
   final Map seriesColorMap = new HashMap();
   private boolean rangeValid = false;
   private final Line verticalZeroLine = new Line();
   private final Line horizontalZeroLine = new Line();
   private final Path verticalGridLines = new Path();
   private final Path horizontalGridLines = new Path();
   private final Path horizontalRowFill = new Path();
   private final Path verticalRowFill = new Path();
   private final Region plotBackground = new Region();
   private final Group plotArea = new Group() {
      public void requestLayout() {
      }
   };
   private final Group plotContent = new Group();
   private final Rectangle plotAreaClip = new Rectangle();
   private final List displayedSeries = new ArrayList();
   private final ListChangeListener seriesChanged = (var1x) -> {
      for(ObservableList var2 = var1x.getList(); var1x.next(); this.seriesChanged(var1x)) {
         if (var1x.wasPermutated()) {
            this.displayedSeries.sort((var1, var2x) -> {
               return var2.indexOf(var2x) - var2.indexOf(var1);
            });
         }

         if (var1x.getRemoved().size() > 0) {
            this.updateLegend();
         }

         HashSet var3 = new HashSet(this.displayedSeries);
         var3.removeAll(var1x.getRemoved());
         Iterator var4 = var1x.getAddedSubList().iterator();

         Series var5;
         while(var4.hasNext()) {
            var5 = (Series)var4.next();
            if (!var3.add(var5)) {
               throw new IllegalArgumentException("Duplicate series added");
            }
         }

         var4 = var1x.getRemoved().iterator();

         int var6;
         while(var4.hasNext()) {
            var5 = (Series)var4.next();
            var5.setToRemove = true;
            this.seriesRemoved(var5);
            var6 = (Integer)this.seriesColorMap.remove(var5);
            this.colorBits.clear(var6);
         }

         for(int var7 = var1x.getFrom(); var7 < var1x.getTo() && !var1x.wasPermutated(); ++var7) {
            var5 = (Series)var1x.getList().get(var7);
            var5.setChart(this);
            if (var5.setToRemove) {
               var5.setToRemove = false;
               var5.getChart().seriesBeingRemovedIsAdded(var5);
            }

            this.displayedSeries.add(var5);
            var6 = this.colorBits.nextClearBit(0);
            this.colorBits.set(var6, true);
            var5.defaultColorStyleClass = DEFAULT_COLOR + var6 % 8;
            this.seriesColorMap.put(var5, var6 % 8);
            this.seriesAdded(var5, var7);
         }

         if (var1x.getFrom() < var1x.getTo()) {
            this.updateLegend();
         }
      }

      this.invalidateRange();
      this.requestChartLayout();
   };
   private final Axis xAxis;
   private final Axis yAxis;
   private ObjectProperty data = new ObjectPropertyBase() {
      private ObservableList old;

      protected void invalidated() {
         ObservableList var1 = (ObservableList)this.getValue();
         int var2 = -1;
         if (this.old != null) {
            this.old.removeListener(XYChart.this.seriesChanged);
            if (var1 != null && this.old.size() > 0) {
               var2 = ((Series)this.old.get(0)).getChart().getAnimated() ? 1 : 2;
               ((Series)this.old.get(0)).getChart().setAnimated(false);
            }
         }

         if (var1 != null) {
            var1.addListener(XYChart.this.seriesChanged);
         }

         if (this.old == null && var1 == null) {
            if (this.old != null && this.old.size() > 0) {
               XYChart.this.seriesChanged.onChanged(new NonIterableChange(0, 0, var1) {
                  public List getRemoved() {
                     return old;
                  }

                  protected int[] getPermutation() {
                     return new int[0];
                  }
               });
            }
         } else {
            final Object var3 = this.old != null ? this.old : Collections.emptyList();
            int var4 = var1 != null ? var1.size() : 0;
            if (var4 > 0 || !((List)var3).isEmpty()) {
               XYChart.this.seriesChanged.onChanged(new NonIterableChange(0, var4, var1) {
                  public List getRemoved() {
                     return (List)var3;
                  }

                  protected int[] getPermutation() {
                     return new int[0];
                  }
               });
            }
         }

         if (var1 != null && var1.size() > 0 && var2 != -1) {
            ((Series)var1.get(0)).getChart().setAnimated(var2 == 1);
         }

         this.old = var1;
      }

      public Object getBean() {
         return XYChart.this;
      }

      public String getName() {
         return "data";
      }
   };
   private BooleanProperty verticalGridLinesVisible = new StyleableBooleanProperty(true) {
      protected void invalidated() {
         XYChart.this.requestChartLayout();
      }

      public Object getBean() {
         return XYChart.this;
      }

      public String getName() {
         return "verticalGridLinesVisible";
      }

      public CssMetaData getCssMetaData() {
         return XYChart.StyleableProperties.VERTICAL_GRID_LINE_VISIBLE;
      }
   };
   private BooleanProperty horizontalGridLinesVisible = new StyleableBooleanProperty(true) {
      protected void invalidated() {
         XYChart.this.requestChartLayout();
      }

      public Object getBean() {
         return XYChart.this;
      }

      public String getName() {
         return "horizontalGridLinesVisible";
      }

      public CssMetaData getCssMetaData() {
         return XYChart.StyleableProperties.HORIZONTAL_GRID_LINE_VISIBLE;
      }
   };
   private BooleanProperty alternativeColumnFillVisible = new StyleableBooleanProperty(false) {
      protected void invalidated() {
         XYChart.this.requestChartLayout();
      }

      public Object getBean() {
         return XYChart.this;
      }

      public String getName() {
         return "alternativeColumnFillVisible";
      }

      public CssMetaData getCssMetaData() {
         return XYChart.StyleableProperties.ALTERNATIVE_COLUMN_FILL_VISIBLE;
      }
   };
   private BooleanProperty alternativeRowFillVisible = new StyleableBooleanProperty(true) {
      protected void invalidated() {
         XYChart.this.requestChartLayout();
      }

      public Object getBean() {
         return XYChart.this;
      }

      public String getName() {
         return "alternativeRowFillVisible";
      }

      public CssMetaData getCssMetaData() {
         return XYChart.StyleableProperties.ALTERNATIVE_ROW_FILL_VISIBLE;
      }
   };
   private BooleanProperty verticalZeroLineVisible = new StyleableBooleanProperty(true) {
      protected void invalidated() {
         XYChart.this.requestChartLayout();
      }

      public Object getBean() {
         return XYChart.this;
      }

      public String getName() {
         return "verticalZeroLineVisible";
      }

      public CssMetaData getCssMetaData() {
         return XYChart.StyleableProperties.VERTICAL_ZERO_LINE_VISIBLE;
      }
   };
   private BooleanProperty horizontalZeroLineVisible = new StyleableBooleanProperty(true) {
      protected void invalidated() {
         XYChart.this.requestChartLayout();
      }

      public Object getBean() {
         return XYChart.this;
      }

      public String getName() {
         return "horizontalZeroLineVisible";
      }

      public CssMetaData getCssMetaData() {
         return XYChart.StyleableProperties.HORIZONTAL_ZERO_LINE_VISIBLE;
      }
   };

   public Axis getXAxis() {
      return this.xAxis;
   }

   public Axis getYAxis() {
      return this.yAxis;
   }

   public final ObservableList getData() {
      return (ObservableList)this.data.getValue();
   }

   public final void setData(ObservableList var1) {
      this.data.setValue(var1);
   }

   public final ObjectProperty dataProperty() {
      return this.data;
   }

   public final boolean getVerticalGridLinesVisible() {
      return this.verticalGridLinesVisible.get();
   }

   public final void setVerticalGridLinesVisible(boolean var1) {
      this.verticalGridLinesVisible.set(var1);
   }

   public final BooleanProperty verticalGridLinesVisibleProperty() {
      return this.verticalGridLinesVisible;
   }

   public final boolean isHorizontalGridLinesVisible() {
      return this.horizontalGridLinesVisible.get();
   }

   public final void setHorizontalGridLinesVisible(boolean var1) {
      this.horizontalGridLinesVisible.set(var1);
   }

   public final BooleanProperty horizontalGridLinesVisibleProperty() {
      return this.horizontalGridLinesVisible;
   }

   public final boolean isAlternativeColumnFillVisible() {
      return this.alternativeColumnFillVisible.getValue();
   }

   public final void setAlternativeColumnFillVisible(boolean var1) {
      this.alternativeColumnFillVisible.setValue(var1);
   }

   public final BooleanProperty alternativeColumnFillVisibleProperty() {
      return this.alternativeColumnFillVisible;
   }

   public final boolean isAlternativeRowFillVisible() {
      return this.alternativeRowFillVisible.getValue();
   }

   public final void setAlternativeRowFillVisible(boolean var1) {
      this.alternativeRowFillVisible.setValue(var1);
   }

   public final BooleanProperty alternativeRowFillVisibleProperty() {
      return this.alternativeRowFillVisible;
   }

   public final boolean isVerticalZeroLineVisible() {
      return this.verticalZeroLineVisible.get();
   }

   public final void setVerticalZeroLineVisible(boolean var1) {
      this.verticalZeroLineVisible.set(var1);
   }

   public final BooleanProperty verticalZeroLineVisibleProperty() {
      return this.verticalZeroLineVisible;
   }

   public final boolean isHorizontalZeroLineVisible() {
      return this.horizontalZeroLineVisible.get();
   }

   public final void setHorizontalZeroLineVisible(boolean var1) {
      this.horizontalZeroLineVisible.set(var1);
   }

   public final BooleanProperty horizontalZeroLineVisibleProperty() {
      return this.horizontalZeroLineVisible;
   }

   protected ObservableList getPlotChildren() {
      return this.plotContent.getChildren();
   }

   public XYChart(Axis var1, Axis var2) {
      this.xAxis = var1;
      if (var1.getSide() == null) {
         var1.setSide(Side.BOTTOM);
      }

      var1.setEffectiveOrientation(Orientation.HORIZONTAL);
      this.yAxis = var2;
      if (var2.getSide() == null) {
         var2.setSide(Side.LEFT);
      }

      var2.setEffectiveOrientation(Orientation.VERTICAL);
      var1.autoRangingProperty().addListener((var1x, var2x, var3) -> {
         this.updateAxisRange();
      });
      var2.autoRangingProperty().addListener((var1x, var2x, var3) -> {
         this.updateAxisRange();
      });
      this.getChartChildren().addAll(this.plotBackground, this.plotArea, var1, var2);
      this.plotArea.setAutoSizeChildren(false);
      this.plotContent.setAutoSizeChildren(false);
      this.plotAreaClip.setSmooth(false);
      this.plotArea.setClip(this.plotAreaClip);
      this.plotArea.getChildren().addAll(this.verticalRowFill, this.horizontalRowFill, this.verticalGridLines, this.horizontalGridLines, this.verticalZeroLine, this.horizontalZeroLine, this.plotContent);
      this.plotContent.getStyleClass().setAll((Object[])("plot-content"));
      this.plotBackground.getStyleClass().setAll((Object[])("chart-plot-background"));
      this.verticalRowFill.getStyleClass().setAll((Object[])("chart-alternative-column-fill"));
      this.horizontalRowFill.getStyleClass().setAll((Object[])("chart-alternative-row-fill"));
      this.verticalGridLines.getStyleClass().setAll((Object[])("chart-vertical-grid-lines"));
      this.horizontalGridLines.getStyleClass().setAll((Object[])("chart-horizontal-grid-lines"));
      this.verticalZeroLine.getStyleClass().setAll((Object[])("chart-vertical-zero-line"));
      this.horizontalZeroLine.getStyleClass().setAll((Object[])("chart-horizontal-zero-line"));
      this.plotContent.setManaged(false);
      this.plotArea.setManaged(false);
      this.animatedProperty().addListener((var1x, var2x, var3) -> {
         if (this.getXAxis() != null) {
            this.getXAxis().setAnimated(var3);
         }

         if (this.getYAxis() != null) {
            this.getYAxis().setAnimated(var3);
         }

      });
   }

   final int getDataSize() {
      ObservableList var1 = this.getData();
      return var1 != null ? var1.size() : 0;
   }

   private void seriesNameChanged() {
      this.updateLegend();
      this.requestChartLayout();
   }

   private void dataItemsChanged(Series var1, List var2, int var3, int var4, boolean var5) {
      Iterator var6 = var2.iterator();

      Data var7;
      while(var6.hasNext()) {
         var7 = (Data)var6.next();
         this.dataItemRemoved(var7, var1);
      }

      for(int var8 = var3; var8 < var4; ++var8) {
         var7 = (Data)var1.getData().get(var8);
         this.dataItemAdded(var1, var8, var7);
      }

      this.invalidateRange();
      this.requestChartLayout();
   }

   private void dataXValueChanged(Data var1) {
      if (var1.getCurrentX() != var1.getXValue()) {
         this.invalidateRange();
      }

      this.dataItemChanged(var1);
      if (this.shouldAnimate()) {
         this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentXProperty(), var1.getCurrentX())}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(var1.currentXProperty(), var1.getXValue(), Interpolator.EASE_BOTH)})});
      } else {
         var1.setCurrentX(var1.getXValue());
         this.requestChartLayout();
      }

   }

   private void dataYValueChanged(Data var1) {
      if (var1.getCurrentY() != var1.getYValue()) {
         this.invalidateRange();
      }

      this.dataItemChanged(var1);
      if (this.shouldAnimate()) {
         this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getCurrentY())}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getYValue(), Interpolator.EASE_BOTH)})});
      } else {
         var1.setCurrentY(var1.getYValue());
         this.requestChartLayout();
      }

   }

   private void dataExtraValueChanged(Data var1) {
      if (var1.getCurrentY() != var1.getYValue()) {
         this.invalidateRange();
      }

      this.dataItemChanged(var1);
      if (this.shouldAnimate()) {
         this.animate(new KeyFrame[]{new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getCurrentY())}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(var1.currentYProperty(), var1.getYValue(), Interpolator.EASE_BOTH)})});
      } else {
         var1.setCurrentY(var1.getYValue());
         this.requestChartLayout();
      }

   }

   protected void updateLegend() {
   }

   void seriesBeingRemovedIsAdded(Series var1) {
   }

   void dataBeingRemovedIsAdded(Data var1, Series var2) {
   }

   protected abstract void dataItemAdded(Series var1, int var2, Data var3);

   protected abstract void dataItemRemoved(Data var1, Series var2);

   protected abstract void dataItemChanged(Data var1);

   protected abstract void seriesAdded(Series var1, int var2);

   protected abstract void seriesRemoved(Series var1);

   protected void seriesChanged(ListChangeListener.Change var1) {
   }

   private void invalidateRange() {
      this.rangeValid = false;
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
            Series var6 = (Series)var5.next();
            Iterator var7 = var6.getData().iterator();

            while(var7.hasNext()) {
               Data var8 = (Data)var7.next();
               if (var3 != null) {
                  var3.add(var8.getXValue());
               }

               if (var4 != null) {
                  var4.add(var8.getYValue());
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

   protected abstract void layoutPlotChildren();

   protected final void layoutChartChildren(double var1, double var3, double var5, double var7) {
      if (this.getData() != null) {
         if (!this.rangeValid) {
            this.rangeValid = true;
            if (this.getData() != null) {
               this.updateAxisRange();
            }
         }

         var1 = this.snapPosition(var1);
         var3 = this.snapPosition(var3);
         Axis var9 = this.getXAxis();
         ObservableList var10 = var9.getTickMarks();
         Axis var11 = this.getYAxis();
         ObservableList var12 = var11.getTickMarks();
         if (var9 != null && var11 != null) {
            double var13 = 0.0;
            double var15 = 30.0;
            double var17 = 0.0;
            double var19 = 0.0;

            for(int var21 = 0; var21 < 5; ++var21) {
               var19 = this.snapSize(var7 - var15);
               if (var19 < 0.0) {
                  var19 = 0.0;
               }

               var17 = var11.prefWidth(var19);
               var13 = this.snapSize(var5 - var17);
               if (var13 < 0.0) {
                  var13 = 0.0;
               }

               double var22 = var9.prefHeight(var13);
               if (var22 == var15) {
                  break;
               }

               var15 = var22;
            }

            var13 = Math.ceil(var13);
            var15 = Math.ceil(var15);
            var17 = Math.ceil(var17);
            var19 = Math.ceil(var19);
            double var36 = 0.0;
            switch (var9.getEffectiveSide()) {
               case TOP:
                  var9.setVisible(true);
                  var36 = var1 + 1.0;
                  var1 += var15;
                  break;
               case BOTTOM:
                  var9.setVisible(true);
                  var36 = var1 + var19;
            }

            double var23 = 0.0;
            switch (var11.getEffectiveSide()) {
               case LEFT:
                  var11.setVisible(true);
                  var23 = var3 + 1.0;
                  var3 += var17;
                  break;
               case RIGHT:
                  var11.setVisible(true);
                  var23 = var3 + var13;
            }

            var9.resizeRelocate(var3, var36, var13, var15);
            var11.resizeRelocate(var23, var1, var17, var19);
            var9.requestAxisLayout();
            var9.layout();
            var11.requestAxisLayout();
            var11.layout();
            this.layoutPlotChildren();
            double var25 = var9.getZeroPosition();
            double var27 = var11.getZeroPosition();
            if (!Double.isNaN(var25) && this.isVerticalZeroLineVisible()) {
               this.verticalZeroLine.setStartX(var3 + var25 + 0.5);
               this.verticalZeroLine.setStartY(var1);
               this.verticalZeroLine.setEndX(var3 + var25 + 0.5);
               this.verticalZeroLine.setEndY(var1 + var19);
               this.verticalZeroLine.setVisible(true);
            } else {
               this.verticalZeroLine.setVisible(false);
            }

            if (!Double.isNaN(var27) && this.isHorizontalZeroLineVisible()) {
               this.horizontalZeroLine.setStartX(var3);
               this.horizontalZeroLine.setStartY(var1 + var27 + 0.5);
               this.horizontalZeroLine.setEndX(var3 + var13);
               this.horizontalZeroLine.setEndY(var1 + var27 + 0.5);
               this.horizontalZeroLine.setVisible(true);
            } else {
               this.horizontalZeroLine.setVisible(false);
            }

            this.plotBackground.resizeRelocate(var3, var1, var13, var19);
            this.plotAreaClip.setX(var3);
            this.plotAreaClip.setY(var1);
            this.plotAreaClip.setWidth(var13 + 1.0);
            this.plotAreaClip.setHeight(var19 + 1.0);
            this.plotContent.setLayoutX(var3);
            this.plotContent.setLayoutY(var1);
            this.plotContent.requestLayout();
            this.verticalGridLines.getElements().clear();
            int var29;
            Axis.TickMark var30;
            double var31;
            if (this.getVerticalGridLinesVisible()) {
               for(var29 = 0; var29 < var10.size(); ++var29) {
                  var30 = (Axis.TickMark)var10.get(var29);
                  var31 = var9.getDisplayPosition(var30.getValue());
                  if ((var31 != var25 || !this.isVerticalZeroLineVisible()) && var31 > 0.0 && var31 <= var13) {
                     this.verticalGridLines.getElements().add(new MoveTo(var3 + var31 + 0.5, var1));
                     this.verticalGridLines.getElements().add(new LineTo(var3 + var31 + 0.5, var1 + var19));
                  }
               }
            }

            this.horizontalGridLines.getElements().clear();
            if (this.isHorizontalGridLinesVisible()) {
               for(var29 = 0; var29 < var12.size(); ++var29) {
                  var30 = (Axis.TickMark)var12.get(var29);
                  var31 = var11.getDisplayPosition(var30.getValue());
                  if ((var31 != var27 || !this.isHorizontalZeroLineVisible()) && var31 >= 0.0 && var31 < var19) {
                     this.horizontalGridLines.getElements().add(new MoveTo(var3, var1 + var31 + 0.5));
                     this.horizontalGridLines.getElements().add(new LineTo(var3 + var13, var1 + var31 + 0.5));
                  }
               }
            }

            this.verticalRowFill.getElements().clear();
            double var32;
            double var34;
            ArrayList var37;
            ArrayList var38;
            int var39;
            if (this.isAlternativeColumnFillVisible()) {
               var37 = new ArrayList();
               var38 = new ArrayList();

               for(var39 = 0; var39 < var10.size(); ++var39) {
                  var32 = var9.getDisplayPosition(((Axis.TickMark)var10.get(var39)).getValue());
                  if (var32 == var25) {
                     var37.add(var32);
                     var38.add(var32);
                  } else if (var32 < var25) {
                     var37.add(var32);
                  } else {
                     var38.add(var32);
                  }
               }

               Collections.sort(var37);
               Collections.sort(var38);

               for(var39 = 1; var39 < var37.size(); var39 += 2) {
                  if (var39 + 1 < var37.size()) {
                     var32 = (Double)var37.get(var39);
                     var34 = (Double)var37.get(var39 + 1);
                     this.verticalRowFill.getElements().addAll(new MoveTo(var3 + var32, var1), new LineTo(var3 + var32, var1 + var19), new LineTo(var3 + var34, var1 + var19), new LineTo(var3 + var34, var1), new ClosePath());
                  }
               }

               for(var39 = 0; var39 < var38.size(); var39 += 2) {
                  if (var39 + 1 < var38.size()) {
                     var32 = (Double)var38.get(var39);
                     var34 = (Double)var38.get(var39 + 1);
                     this.verticalRowFill.getElements().addAll(new MoveTo(var3 + var32, var1), new LineTo(var3 + var32, var1 + var19), new LineTo(var3 + var34, var1 + var19), new LineTo(var3 + var34, var1), new ClosePath());
                  }
               }
            }

            this.horizontalRowFill.getElements().clear();
            if (this.isAlternativeRowFillVisible()) {
               var37 = new ArrayList();
               var38 = new ArrayList();

               for(var39 = 0; var39 < var12.size(); ++var39) {
                  var32 = var11.getDisplayPosition(((Axis.TickMark)var12.get(var39)).getValue());
                  if (var32 == var27) {
                     var37.add(var32);
                     var38.add(var32);
                  } else if (var32 < var27) {
                     var37.add(var32);
                  } else {
                     var38.add(var32);
                  }
               }

               Collections.sort(var37);
               Collections.sort(var38);

               for(var39 = 1; var39 < var37.size(); var39 += 2) {
                  if (var39 + 1 < var37.size()) {
                     var32 = (Double)var37.get(var39);
                     var34 = (Double)var37.get(var39 + 1);
                     this.horizontalRowFill.getElements().addAll(new MoveTo(var3, var1 + var32), new LineTo(var3 + var13, var1 + var32), new LineTo(var3 + var13, var1 + var34), new LineTo(var3, var1 + var34), new ClosePath());
                  }
               }

               for(var39 = 0; var39 < var38.size(); var39 += 2) {
                  if (var39 + 1 < var38.size()) {
                     var32 = (Double)var38.get(var39);
                     var34 = (Double)var38.get(var39 + 1);
                     this.horizontalRowFill.getElements().addAll(new MoveTo(var3, var1 + var32), new LineTo(var3 + var13, var1 + var32), new LineTo(var3 + var13, var1 + var34), new LineTo(var3, var1 + var34), new ClosePath());
                  }
               }
            }

         }
      }
   }

   int getSeriesIndex(Series var1) {
      return this.displayedSeries.indexOf(var1);
   }

   int getSeriesSize() {
      return this.displayedSeries.size();
   }

   protected final void removeSeriesFromDisplay(Series var1) {
      if (var1 != null) {
         var1.setToRemove = false;
      }

      var1.setChart((XYChart)null);
      this.displayedSeries.remove(var1);
   }

   protected final Iterator getDisplayedSeriesIterator() {
      return Collections.unmodifiableList(this.displayedSeries).iterator();
   }

   final KeyFrame[] createSeriesRemoveTimeLine(Series var1, long var2) {
      ArrayList var4 = new ArrayList();
      var4.add(var1.getNode());
      Iterator var5 = var1.getData().iterator();

      while(var5.hasNext()) {
         Data var6 = (Data)var5.next();
         if (var6.getNode() != null) {
            var4.add(var6.getNode());
         }
      }

      KeyValue[] var8 = new KeyValue[var4.size()];
      KeyValue[] var9 = new KeyValue[var4.size()];

      for(int var7 = 0; var7 < var4.size(); ++var7) {
         var8[var7] = new KeyValue(((Node)var4.get(var7)).opacityProperty(), 1);
         var9[var7] = new KeyValue(((Node)var4.get(var7)).opacityProperty(), 0);
      }

      return new KeyFrame[]{new KeyFrame(Duration.ZERO, var8), new KeyFrame(Duration.millis((double)var2), (var3) -> {
         this.getPlotChildren().removeAll(var4);
         this.removeSeriesFromDisplay(var1);
      }, var9)};
   }

   protected final Object getCurrentDisplayedXValue(Data var1) {
      return var1.getCurrentX();
   }

   protected final void setCurrentDisplayedXValue(Data var1, Object var2) {
      var1.setCurrentX(var2);
   }

   protected final ObjectProperty currentDisplayedXValueProperty(Data var1) {
      return var1.currentXProperty();
   }

   protected final Object getCurrentDisplayedYValue(Data var1) {
      return var1.getCurrentY();
   }

   protected final void setCurrentDisplayedYValue(Data var1, Object var2) {
      var1.setCurrentY(var2);
   }

   protected final ObjectProperty currentDisplayedYValueProperty(Data var1) {
      return var1.currentYProperty();
   }

   protected final Object getCurrentDisplayedExtraValue(Data var1) {
      return var1.getCurrentExtraValue();
   }

   protected final void setCurrentDisplayedExtraValue(Data var1, Object var2) {
      var1.setCurrentExtraValue(var2);
   }

   protected final ObjectProperty currentDisplayedExtraValueProperty(Data var1) {
      return var1.currentExtraValueProperty();
   }

   protected final Iterator getDisplayedDataIterator(Series var1) {
      return Collections.unmodifiableList(var1.displayedData).iterator();
   }

   protected final void removeDataItemFromDisplay(Series var1, Data var2) {
      var1.removeDataItemRef(var2);
   }

   public static List getClassCssMetaData() {
      return XYChart.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public static final class Series {
      String defaultColorStyleClass;
      boolean setToRemove;
      private List displayedData;
      private final ListChangeListener dataChangeListener;
      private final ReadOnlyObjectWrapper chart;
      private final StringProperty name;
      private ObjectProperty node;
      private final ObjectProperty data;

      public final XYChart getChart() {
         return (XYChart)this.chart.get();
      }

      private void setChart(XYChart var1) {
         this.chart.set(var1);
      }

      public final ReadOnlyObjectProperty chartProperty() {
         return this.chart.getReadOnlyProperty();
      }

      public final String getName() {
         return (String)this.name.get();
      }

      public final void setName(String var1) {
         this.name.set(var1);
      }

      public final StringProperty nameProperty() {
         return this.name;
      }

      public final Node getNode() {
         return (Node)this.node.get();
      }

      public final void setNode(Node var1) {
         this.node.set(var1);
      }

      public final ObjectProperty nodeProperty() {
         return this.node;
      }

      public final ObservableList getData() {
         return (ObservableList)this.data.getValue();
      }

      public final void setData(ObservableList var1) {
         this.data.setValue(var1);
      }

      public final ObjectProperty dataProperty() {
         return this.data;
      }

      public Series() {
         this(FXCollections.observableArrayList());
      }

      public Series(ObservableList var1) {
         this.setToRemove = false;
         this.displayedData = new ArrayList();
         this.dataChangeListener = new ListChangeListener() {
            public void onChanged(ListChangeListener.Change var1) {
               ObservableList var2 = var1.getList();
               XYChart var3 = Series.this.getChart();

               while(true) {
                  while(var1.next()) {
                     HashSet var4;
                     Iterator var5;
                     Data var6;
                     if (var3 != null) {
                        if (var1.wasPermutated()) {
                           Series.this.displayedData.sort((var1x, var2x) -> {
                              return var2.indexOf(var2x) - var2.indexOf(var1x);
                           });
                           return;
                        }

                        var4 = new HashSet(Series.this.displayedData);
                        var4.removeAll(var1.getRemoved());
                        var5 = var1.getAddedSubList().iterator();

                        while(var5.hasNext()) {
                           var6 = (Data)var5.next();
                           if (!var4.add(var6)) {
                              throw new IllegalArgumentException("Duplicate data added");
                           }
                        }

                        var5 = var1.getRemoved().iterator();

                        while(var5.hasNext()) {
                           var6 = (Data)var5.next();
                           var6.setToRemove = true;
                        }

                        if (var1.getAddedSize() > 0) {
                           var5 = var1.getAddedSubList().iterator();

                           while(var5.hasNext()) {
                              var6 = (Data)var5.next();
                              if (var6.setToRemove) {
                                 if (var3 != null) {
                                    var3.dataBeingRemovedIsAdded(var6, Series.this);
                                 }

                                 var6.setToRemove = false;
                              }
                           }

                           var5 = var1.getAddedSubList().iterator();

                           while(var5.hasNext()) {
                              var6 = (Data)var5.next();
                              var6.setSeries(Series.this);
                           }

                           if (var1.getFrom() == 0) {
                              Series.this.displayedData.addAll(0, var1.getAddedSubList());
                           } else {
                              Series.this.displayedData.addAll(Series.this.displayedData.indexOf(var2.get(var1.getFrom() - 1)) + 1, var1.getAddedSubList());
                           }
                        }

                        var3.dataItemsChanged(Series.this, var1.getRemoved(), var1.getFrom(), var1.getTo(), var1.wasPermutated());
                     } else {
                        var4 = new HashSet();
                        var5 = var2.iterator();

                        while(var5.hasNext()) {
                           var6 = (Data)var5.next();
                           if (!var4.add(var6)) {
                              throw new IllegalArgumentException("Duplicate data added");
                           }
                        }

                        var5 = var1.getAddedSubList().iterator();

                        while(var5.hasNext()) {
                           var6 = (Data)var5.next();
                           var6.setSeries(Series.this);
                        }
                     }
                  }

                  return;
               }
            }
         };
         this.chart = new ReadOnlyObjectWrapper(this, "chart") {
            protected void invalidated() {
               if (this.get() == null) {
                  Series.this.displayedData.clear();
               } else {
                  Series.this.displayedData.addAll(Series.this.getData());
               }

            }
         };
         this.name = new StringPropertyBase() {
            protected void invalidated() {
               this.get();
               if (Series.this.getChart() != null) {
                  Series.this.getChart().seriesNameChanged();
               }

            }

            public Object getBean() {
               return Series.this;
            }

            public String getName() {
               return "name";
            }
         };
         this.node = new SimpleObjectProperty(this, "node");
         this.data = new ObjectPropertyBase() {
            private ObservableList old;

            protected void invalidated() {
               ObservableList var1 = (ObservableList)this.getValue();
               if (this.old != null) {
                  this.old.removeListener(Series.this.dataChangeListener);
               }

               if (var1 != null) {
                  var1.addListener(Series.this.dataChangeListener);
               }

               if (this.old == null && var1 == null) {
                  if (this.old != null && this.old.size() > 0) {
                     Series.this.dataChangeListener.onChanged(new NonIterableChange(0, 0, var1) {
                        public List getRemoved() {
                           return old;
                        }

                        protected int[] getPermutation() {
                           return new int[0];
                        }
                     });
                  }
               } else {
                  final Object var2 = this.old != null ? this.old : Collections.emptyList();
                  int var3 = var1 != null ? var1.size() : 0;
                  if (var3 > 0 || !((List)var2).isEmpty()) {
                     Series.this.dataChangeListener.onChanged(new NonIterableChange(0, var3, var1) {
                        public List getRemoved() {
                           return (List)var2;
                        }

                        protected int[] getPermutation() {
                           return new int[0];
                        }
                     });
                  }
               }

               this.old = var1;
            }

            public Object getBean() {
               return Series.this;
            }

            public String getName() {
               return "data";
            }
         };
         this.setData(var1);
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Data var3 = (Data)var2.next();
            var3.setSeries(this);
         }

      }

      public Series(String var1, ObservableList var2) {
         this(var2);
         this.setName(var1);
      }

      public String toString() {
         return "Series[" + this.getName() + "]";
      }

      private void removeDataItemRef(Data var1) {
         if (var1 != null) {
            var1.setToRemove = false;
         }

         this.displayedData.remove(var1);
      }

      int getItemIndex(Data var1) {
         return this.displayedData.indexOf(var1);
      }

      Data getItem(int var1) {
         return (Data)this.displayedData.get(var1);
      }

      int getDataSize() {
         return this.displayedData.size();
      }
   }

   public static final class Data {
      private boolean setToRemove = false;
      private Series series;
      private ObjectProperty xValue = new ObjectPropertyBase() {
         protected void invalidated() {
            this.get();
            if (Data.this.series != null) {
               XYChart var1 = Data.this.series.getChart();
               if (var1 != null) {
                  var1.dataXValueChanged(Data.this);
               }
            } else {
               Data.this.setCurrentX(this.get());
            }

         }

         public Object getBean() {
            return Data.this;
         }

         public String getName() {
            return "XValue";
         }
      };
      private ObjectProperty yValue = new ObjectPropertyBase() {
         protected void invalidated() {
            this.get();
            if (Data.this.series != null) {
               XYChart var1 = Data.this.series.getChart();
               if (var1 != null) {
                  var1.dataYValueChanged(Data.this);
               }
            } else {
               Data.this.setCurrentY(this.get());
            }

         }

         public Object getBean() {
            return Data.this;
         }

         public String getName() {
            return "YValue";
         }
      };
      private ObjectProperty extraValue = new ObjectPropertyBase() {
         protected void invalidated() {
            this.get();
            if (Data.this.series != null) {
               XYChart var1 = Data.this.series.getChart();
               if (var1 != null) {
                  var1.dataExtraValueChanged(Data.this);
               }
            }

         }

         public Object getBean() {
            return Data.this;
         }

         public String getName() {
            return "extraValue";
         }
      };
      private ObjectProperty node = new SimpleObjectProperty(this, "node") {
         protected void invalidated() {
            Node var1 = (Node)this.get();
            if (var1 != null) {
               var1.accessibleTextProperty().unbind();
               var1.accessibleTextProperty().bind(new StringBinding() {
                  {
                     this.bind(new Observable[]{Data.this.currentXProperty(), Data.this.currentYProperty()});
                  }

                  protected String computeValue() {
                     String var1 = Data.this.series != null ? Data.this.series.getName() : "";
                     return var1 + " X Axis is " + Data.this.getCurrentX() + " Y Axis is " + Data.this.getCurrentY();
                  }
               });
            }

         }
      };
      private ObjectProperty currentX = new SimpleObjectProperty(this, "currentX");
      private ObjectProperty currentY = new SimpleObjectProperty(this, "currentY");
      private ObjectProperty currentExtraValue = new SimpleObjectProperty(this, "currentExtraValue");

      void setSeries(Series var1) {
         this.series = var1;
      }

      public final Object getXValue() {
         return this.xValue.get();
      }

      public final void setXValue(Object var1) {
         this.xValue.set(var1);
         if (this.currentX.get() == null || this.series != null && this.series.getChart() == null) {
            this.currentX.setValue(var1);
         }

      }

      public final ObjectProperty XValueProperty() {
         return this.xValue;
      }

      public final Object getYValue() {
         return this.yValue.get();
      }

      public final void setYValue(Object var1) {
         this.yValue.set(var1);
         if (this.currentY.get() == null || this.series != null && this.series.getChart() == null) {
            this.currentY.setValue(var1);
         }

      }

      public final ObjectProperty YValueProperty() {
         return this.yValue;
      }

      public final Object getExtraValue() {
         return this.extraValue.get();
      }

      public final void setExtraValue(Object var1) {
         this.extraValue.set(var1);
      }

      public final ObjectProperty extraValueProperty() {
         return this.extraValue;
      }

      public final Node getNode() {
         return (Node)this.node.get();
      }

      public final void setNode(Node var1) {
         this.node.set(var1);
      }

      public final ObjectProperty nodeProperty() {
         return this.node;
      }

      final Object getCurrentX() {
         return this.currentX.get();
      }

      final void setCurrentX(Object var1) {
         this.currentX.set(var1);
      }

      final ObjectProperty currentXProperty() {
         return this.currentX;
      }

      final Object getCurrentY() {
         return this.currentY.get();
      }

      final void setCurrentY(Object var1) {
         this.currentY.set(var1);
      }

      final ObjectProperty currentYProperty() {
         return this.currentY;
      }

      final Object getCurrentExtraValue() {
         return this.currentExtraValue.getValue();
      }

      final void setCurrentExtraValue(Object var1) {
         this.currentExtraValue.setValue(var1);
      }

      final ObjectProperty currentExtraValueProperty() {
         return this.currentExtraValue;
      }

      public Data() {
      }

      public Data(Object var1, Object var2) {
         this.setXValue(var1);
         this.setYValue(var2);
         this.setCurrentX(var1);
         this.setCurrentY(var2);
      }

      public Data(Object var1, Object var2, Object var3) {
         this.setXValue(var1);
         this.setYValue(var2);
         this.setExtraValue(var3);
         this.setCurrentX(var1);
         this.setCurrentY(var2);
         this.setCurrentExtraValue(var3);
      }

      public String toString() {
         return "Data[" + this.getXValue() + "," + this.getYValue() + "," + this.getExtraValue() + "]";
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData HORIZONTAL_GRID_LINE_VISIBLE;
      private static final CssMetaData HORIZONTAL_ZERO_LINE_VISIBLE;
      private static final CssMetaData ALTERNATIVE_ROW_FILL_VISIBLE;
      private static final CssMetaData VERTICAL_GRID_LINE_VISIBLE;
      private static final CssMetaData VERTICAL_ZERO_LINE_VISIBLE;
      private static final CssMetaData ALTERNATIVE_COLUMN_FILL_VISIBLE;
      private static final List STYLEABLES;

      static {
         HORIZONTAL_GRID_LINE_VISIBLE = new CssMetaData("-fx-horizontal-grid-lines-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(XYChart var1) {
               return var1.horizontalGridLinesVisible == null || !var1.horizontalGridLinesVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(XYChart var1) {
               return (StyleableProperty)var1.horizontalGridLinesVisibleProperty();
            }
         };
         HORIZONTAL_ZERO_LINE_VISIBLE = new CssMetaData("-fx-horizontal-zero-line-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(XYChart var1) {
               return var1.horizontalZeroLineVisible == null || !var1.horizontalZeroLineVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(XYChart var1) {
               return (StyleableProperty)var1.horizontalZeroLineVisibleProperty();
            }
         };
         ALTERNATIVE_ROW_FILL_VISIBLE = new CssMetaData("-fx-alternative-row-fill-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(XYChart var1) {
               return var1.alternativeRowFillVisible == null || !var1.alternativeRowFillVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(XYChart var1) {
               return (StyleableProperty)var1.alternativeRowFillVisibleProperty();
            }
         };
         VERTICAL_GRID_LINE_VISIBLE = new CssMetaData("-fx-vertical-grid-lines-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(XYChart var1) {
               return var1.verticalGridLinesVisible == null || !var1.verticalGridLinesVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(XYChart var1) {
               return (StyleableProperty)var1.verticalGridLinesVisibleProperty();
            }
         };
         VERTICAL_ZERO_LINE_VISIBLE = new CssMetaData("-fx-vertical-zero-line-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(XYChart var1) {
               return var1.verticalZeroLineVisible == null || !var1.verticalZeroLineVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(XYChart var1) {
               return (StyleableProperty)var1.verticalZeroLineVisibleProperty();
            }
         };
         ALTERNATIVE_COLUMN_FILL_VISIBLE = new CssMetaData("-fx-alternative-column-fill-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(XYChart var1) {
               return var1.alternativeColumnFillVisible == null || !var1.alternativeColumnFillVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(XYChart var1) {
               return (StyleableProperty)var1.alternativeColumnFillVisibleProperty();
            }
         };
         ArrayList var0 = new ArrayList(Chart.getClassCssMetaData());
         var0.add(HORIZONTAL_GRID_LINE_VISIBLE);
         var0.add(HORIZONTAL_ZERO_LINE_VISIBLE);
         var0.add(ALTERNATIVE_ROW_FILL_VISIBLE);
         var0.add(VERTICAL_GRID_LINE_VISIBLE);
         var0.add(VERTICAL_ZERO_LINE_VISIBLE);
         var0.add(ALTERNATIVE_COLUMN_FILL_VISIBLE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
