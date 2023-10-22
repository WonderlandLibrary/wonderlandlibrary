package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class ProgressIndicatorSkin extends BehaviorSkinBase {
   private ObjectProperty progressColor = new StyleableObjectProperty((Paint)null) {
      protected void invalidated() {
         Paint var1 = (Paint)this.get();
         if (var1 != null && !(var1 instanceof Color)) {
            if (this.isBound()) {
               this.unbind();
            }

            this.set((Object)null);
            throw new IllegalArgumentException("Only Color objects are supported");
         } else {
            if (ProgressIndicatorSkin.this.spinner != null) {
               ProgressIndicatorSkin.this.spinner.setFillOverride(var1);
            }

            if (ProgressIndicatorSkin.this.determinateIndicator != null) {
               ProgressIndicatorSkin.this.determinateIndicator.setFillOverride(var1);
            }

         }
      }

      public Object getBean() {
         return ProgressIndicatorSkin.this;
      }

      public String getName() {
         return "progressColorProperty";
      }

      public CssMetaData getCssMetaData() {
         return ProgressIndicatorSkin.PROGRESS_COLOR;
      }
   };
   private IntegerProperty indeterminateSegmentCount = new StyleableIntegerProperty(8) {
      protected void invalidated() {
         if (ProgressIndicatorSkin.this.spinner != null) {
            ProgressIndicatorSkin.this.spinner.rebuild();
         }

      }

      public Object getBean() {
         return ProgressIndicatorSkin.this;
      }

      public String getName() {
         return "indeterminateSegmentCount";
      }

      public CssMetaData getCssMetaData() {
         return ProgressIndicatorSkin.INDETERMINATE_SEGMENT_COUNT;
      }
   };
   private final BooleanProperty spinEnabled = new StyleableBooleanProperty(false) {
      protected void invalidated() {
         if (ProgressIndicatorSkin.this.spinner != null) {
            ProgressIndicatorSkin.this.spinner.setSpinEnabled(this.get());
         }

      }

      public CssMetaData getCssMetaData() {
         return ProgressIndicatorSkin.SPIN_ENABLED;
      }

      public Object getBean() {
         return ProgressIndicatorSkin.this;
      }

      public String getName() {
         return "spinEnabled";
      }
   };
   private static final String DONE = ControlResources.getString("ProgressIndicator.doneString");
   private static final Text doneText;
   private IndeterminateSpinner spinner;
   private DeterminateIndicator determinateIndicator;
   private ProgressIndicator control;
   protected Animation indeterminateTransition;
   private ReadOnlyObjectProperty windowProperty = null;
   private ReadOnlyBooleanProperty windowShowingProperty = null;
   protected final Duration CLIPPED_DELAY = new Duration(300.0);
   protected final Duration UNCLIPPED_DELAY = new Duration(0.0);
   private static final CssMetaData PROGRESS_COLOR;
   private static final CssMetaData INDETERMINATE_SEGMENT_COUNT;
   private static final CssMetaData SPIN_ENABLED;
   public static final List STYLEABLES;

   Paint getProgressColor() {
      return (Paint)this.progressColor.get();
   }

   public ProgressIndicatorSkin(ProgressIndicator var1) {
      super(var1, new BehaviorBase(var1, Collections.emptyList()));
      this.control = var1;
      this.registerChangeListener(var1.indeterminateProperty(), "INDETERMINATE");
      this.registerChangeListener(var1.progressProperty(), "PROGRESS");
      this.registerChangeListener(var1.visibleProperty(), "VISIBLE");
      this.registerChangeListener(var1.parentProperty(), "PARENT");
      this.registerChangeListener(var1.sceneProperty(), "SCENE");
      this.updateWindowListeners();
      this.initialize();
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("INDETERMINATE".equals(var1)) {
         this.initialize();
      } else if ("PROGRESS".equals(var1)) {
         this.updateProgress();
      } else if ("VISIBLE".equals(var1)) {
         this.updateAnimation();
      } else if ("PARENT".equals(var1)) {
         this.updateAnimation();
      } else if ("SCENE".equals(var1)) {
         this.updateWindowListeners();
         this.updateAnimation();
      } else if ("WINDOW".equals(var1)) {
         this.updateWindowListeners();
         this.updateAnimation();
      } else if ("WINDOWSHOWING".equals(var1)) {
         this.updateAnimation();
      }

   }

   protected void initialize() {
      boolean var1 = this.control.isIndeterminate();
      if (var1) {
         this.determinateIndicator = null;
         this.spinner = new IndeterminateSpinner(this.spinEnabled.get(), (Paint)this.progressColor.get());
         this.getChildren().setAll((Object[])(this.spinner));
         if (this.control.impl_isTreeVisible() && this.indeterminateTransition != null) {
            this.indeterminateTransition.play();
         }
      } else {
         if (this.spinner != null) {
            if (this.indeterminateTransition != null) {
               this.indeterminateTransition.stop();
            }

            this.spinner = null;
         }

         this.determinateIndicator = new DeterminateIndicator(this.control, this, (Paint)this.progressColor.get());
         this.getChildren().setAll((Object[])(this.determinateIndicator));
      }

   }

   public void dispose() {
      super.dispose();
      if (this.indeterminateTransition != null) {
         this.indeterminateTransition.stop();
         this.indeterminateTransition = null;
      }

      if (this.spinner != null) {
         this.spinner = null;
      }

      this.control = null;
   }

   protected void updateProgress() {
      if (this.determinateIndicator != null) {
         this.determinateIndicator.updateProgress(this.control.getProgress());
      }

   }

   protected void createIndeterminateTimeline() {
      if (this.spinner != null) {
         this.spinner.rebuildTimeline();
      }

   }

   protected void pauseTimeline(boolean var1) {
      if (((ProgressIndicator)this.getSkinnable()).isIndeterminate()) {
         if (this.indeterminateTransition == null) {
            this.createIndeterminateTimeline();
         }

         if (var1) {
            this.indeterminateTransition.pause();
         } else {
            this.indeterminateTransition.play();
         }
      }

   }

   protected void updateAnimation() {
      ProgressIndicator var1 = (ProgressIndicator)this.getSkinnable();
      boolean var2 = var1.isVisible() && var1.getParent() != null && var1.getScene() != null && var1.getScene().getWindow() != null && var1.getScene().getWindow().isShowing();
      if (this.indeterminateTransition != null) {
         this.pauseTimeline(!var2);
      } else if (var2) {
         this.createIndeterminateTimeline();
      }

   }

   private void updateWindowListeners() {
      if (this.windowProperty != null) {
         this.unregisterChangeListener(this.windowProperty);
         this.windowProperty = null;
         this.unregisterChangeListener(this.windowShowingProperty);
         this.windowShowingProperty = null;
      }

      if (this.control.getScene() != null && this.control.getScene().getWindow() != null) {
         this.windowProperty = this.control.getScene().windowProperty();
         this.windowShowingProperty = this.control.getScene().getWindow().showingProperty();
         this.registerChangeListener(this.windowProperty, "WINDOW");
         this.registerChangeListener(this.windowShowingProperty, "WINDOWSHOWING");
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      if (this.spinner != null && this.control.isIndeterminate()) {
         this.spinner.layoutChildren();
         this.spinner.resizeRelocate(0.0, 0.0, var5, var7);
      } else if (this.determinateIndicator != null) {
         this.determinateIndicator.layoutChildren();
         this.determinateIndicator.resizeRelocate(0.0, 0.0, var5, var7);
      }

   }

   public static List getClassCssMetaData() {
      return STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   static {
      doneText = new Text(DONE);
      doneText.getStyleClass().add("text");
      PROGRESS_COLOR = new CssMetaData("-fx-progress-color", PaintConverter.getInstance(), (Paint)null) {
         public boolean isSettable(ProgressIndicator var1) {
            ProgressIndicatorSkin var2 = (ProgressIndicatorSkin)var1.getSkin();
            return var2.progressColor == null || !var2.progressColor.isBound();
         }

         public StyleableProperty getStyleableProperty(ProgressIndicator var1) {
            ProgressIndicatorSkin var2 = (ProgressIndicatorSkin)var1.getSkin();
            return (StyleableProperty)var2.progressColor;
         }
      };
      INDETERMINATE_SEGMENT_COUNT = new CssMetaData("-fx-indeterminate-segment-count", SizeConverter.getInstance(), 8) {
         public boolean isSettable(ProgressIndicator var1) {
            ProgressIndicatorSkin var2 = (ProgressIndicatorSkin)var1.getSkin();
            return var2.indeterminateSegmentCount == null || !var2.indeterminateSegmentCount.isBound();
         }

         public StyleableProperty getStyleableProperty(ProgressIndicator var1) {
            ProgressIndicatorSkin var2 = (ProgressIndicatorSkin)var1.getSkin();
            return (StyleableProperty)var2.indeterminateSegmentCount;
         }
      };
      SPIN_ENABLED = new CssMetaData("-fx-spin-enabled", BooleanConverter.getInstance(), Boolean.FALSE) {
         public boolean isSettable(ProgressIndicator var1) {
            ProgressIndicatorSkin var2 = (ProgressIndicatorSkin)var1.getSkin();
            return var2.spinEnabled == null || !var2.spinEnabled.isBound();
         }

         public StyleableProperty getStyleableProperty(ProgressIndicator var1) {
            ProgressIndicatorSkin var2 = (ProgressIndicatorSkin)var1.getSkin();
            return (StyleableProperty)var2.spinEnabled;
         }
      };
      ArrayList var0 = new ArrayList(SkinBase.getClassCssMetaData());
      var0.add(PROGRESS_COLOR);
      var0.add(INDETERMINATE_SEGMENT_COUNT);
      var0.add(SPIN_ENABLED);
      STYLEABLES = Collections.unmodifiableList(var0);
   }

   private final class IndeterminateSpinner extends Region {
      private IndicatorPaths pathsG;
      private final List opacities;
      private boolean spinEnabled;
      private Paint fillOverride;

      private IndeterminateSpinner(boolean var2, Paint var3) {
         this.opacities = new ArrayList();
         this.spinEnabled = false;
         this.fillOverride = null;
         this.spinEnabled = var2;
         this.fillOverride = var3;
         this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
         this.getStyleClass().setAll((Object[])("spinner"));
         this.pathsG = new IndicatorPaths();
         this.getChildren().add(this.pathsG);
         this.rebuild();
         this.rebuildTimeline();
      }

      public void setFillOverride(Paint var1) {
         this.fillOverride = var1;
         this.rebuild();
      }

      public void setSpinEnabled(boolean var1) {
         this.spinEnabled = var1;
         this.rebuildTimeline();
      }

      private void rebuildTimeline() {
         if (this.spinEnabled) {
            if (ProgressIndicatorSkin.this.indeterminateTransition == null) {
               ProgressIndicatorSkin.this.indeterminateTransition = new Timeline();
               ProgressIndicatorSkin.this.indeterminateTransition.setCycleCount(-1);
               ProgressIndicatorSkin.this.indeterminateTransition.setDelay(ProgressIndicatorSkin.this.UNCLIPPED_DELAY);
            } else {
               ProgressIndicatorSkin.this.indeterminateTransition.stop();
               ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().clear();
            }

            ObservableList var1 = FXCollections.observableArrayList();
            var1.add(new KeyFrame(Duration.millis(1.0), new KeyValue[]{new KeyValue(this.pathsG.rotateProperty(), 360)}));
            var1.add(new KeyFrame(Duration.millis(3900.0), new KeyValue[]{new KeyValue(this.pathsG.rotateProperty(), 0)}));

            for(int var2 = 100; var2 <= 3900; var2 += 100) {
               var1.add(new KeyFrame(Duration.millis((double)var2), (var1x) -> {
                  this.shiftColors();
               }, new KeyValue[0]));
            }

            ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().setAll((Collection)var1);
            ProgressIndicatorSkin.this.indeterminateTransition.playFromStart();
         } else if (ProgressIndicatorSkin.this.indeterminateTransition != null) {
            ProgressIndicatorSkin.this.indeterminateTransition.stop();
            ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().clear();
            ProgressIndicatorSkin.this.indeterminateTransition = null;
         }

      }

      protected void layoutChildren() {
         double var1 = ProgressIndicatorSkin.this.control.getWidth() - ProgressIndicatorSkin.this.control.snappedLeftInset() - ProgressIndicatorSkin.this.control.snappedRightInset();
         double var3 = ProgressIndicatorSkin.this.control.getHeight() - ProgressIndicatorSkin.this.control.snappedTopInset() - ProgressIndicatorSkin.this.control.snappedBottomInset();
         double var5 = this.pathsG.prefWidth(-1.0);
         double var7 = this.pathsG.prefHeight(-1.0);
         double var9 = var1 / var5;
         double var11 = var9;
         if (var9 * var7 > var3) {
            var11 = var3 / var7;
         }

         double var13 = var5 * var11;
         double var15 = var7 * var11;
         this.pathsG.resizeRelocate((var1 - var13) / 2.0, (var3 - var15) / 2.0, var13, var15);
      }

      private void rebuild() {
         int var1 = ProgressIndicatorSkin.this.indeterminateSegmentCount.get();
         this.opacities.clear();
         this.pathsG.getChildren().clear();
         double var2 = 0.8 / (double)(var1 - 1);

         for(int var4 = 0; var4 < var1; ++var4) {
            Region var5 = new Region();
            var5.setScaleShape(false);
            var5.setCenterShape(false);
            var5.getStyleClass().addAll("segment", "segment" + var4);
            if (this.fillOverride instanceof Color) {
               Color var6 = (Color)this.fillOverride;
               var5.setStyle("-fx-background-color: rgba(" + (int)(255.0 * var6.getRed()) + "," + (int)(255.0 * var6.getGreen()) + "," + (int)(255.0 * var6.getBlue()) + "," + var6.getOpacity() + ");");
            } else {
               var5.setStyle((String)null);
            }

            this.pathsG.getChildren().add(var5);
            this.opacities.add(Math.max(0.1, 1.0 - var2 * (double)var4));
         }

      }

      private void shiftColors() {
         if (this.opacities.size() > 0) {
            int var1 = ProgressIndicatorSkin.this.indeterminateSegmentCount.get();
            Collections.rotate(this.opacities, -1);

            for(int var2 = 0; var2 < var1; ++var2) {
               ((Node)this.pathsG.getChildren().get(var2)).setOpacity((Double)this.opacities.get(var2));
            }

         }
      }

      // $FF: synthetic method
      IndeterminateSpinner(boolean var2, Paint var3, Object var4) {
         this(var2, var3);
      }

      private class IndicatorPaths extends Pane {
         private IndicatorPaths() {
         }

         protected double computePrefWidth(double var1) {
            double var3 = 0.0;
            Iterator var5 = this.getChildren().iterator();

            while(var5.hasNext()) {
               Node var6 = (Node)var5.next();
               if (var6 instanceof Region) {
                  Region var7 = (Region)var6;
                  if (var7.getShape() != null) {
                     var3 = Math.max(var3, var7.getShape().getLayoutBounds().getMaxX());
                  } else {
                     var3 = Math.max(var3, var7.prefWidth(var1));
                  }
               }
            }

            return var3;
         }

         protected double computePrefHeight(double var1) {
            double var3 = 0.0;
            Iterator var5 = this.getChildren().iterator();

            while(var5.hasNext()) {
               Node var6 = (Node)var5.next();
               if (var6 instanceof Region) {
                  Region var7 = (Region)var6;
                  if (var7.getShape() != null) {
                     var3 = Math.max(var3, var7.getShape().getLayoutBounds().getMaxY());
                  } else {
                     var3 = Math.max(var3, var7.prefHeight(var1));
                  }
               }
            }

            return var3;
         }

         protected void layoutChildren() {
            double var1 = this.getWidth() / this.computePrefWidth(-1.0);
            Iterator var3 = this.getChildren().iterator();

            while(var3.hasNext()) {
               Node var4 = (Node)var3.next();
               if (var4 instanceof Region) {
                  Region var5 = (Region)var4;
                  if (var5.getShape() != null) {
                     var5.resize(var5.getShape().getLayoutBounds().getMaxX(), var5.getShape().getLayoutBounds().getMaxY());
                     var5.getTransforms().setAll((Object[])(new Scale(var1, var1, 0.0, 0.0)));
                  } else {
                     var5.autosize();
                  }
               }
            }

         }

         // $FF: synthetic method
         IndicatorPaths(Object var2) {
            this();
         }
      }
   }

   private class DeterminateIndicator extends Region {
      private double textGap = 2.0;
      private int intProgress;
      private int degProgress;
      private Text text;
      private StackPane indicator;
      private StackPane progress;
      private StackPane tick;
      private Arc arcShape;
      private Circle indicatorCircle;

      public DeterminateIndicator(ProgressIndicator var2, ProgressIndicatorSkin var3, Paint var4) {
         this.getStyleClass().add("determinate-indicator");
         this.intProgress = (int)Math.round(var2.getProgress() * 100.0);
         this.degProgress = (int)(360.0 * var2.getProgress());
         this.getChildren().clear();
         this.text = new Text(var2.getProgress() >= 1.0 ? ProgressIndicatorSkin.DONE : "" + this.intProgress + "%");
         this.text.setTextOrigin(VPos.TOP);
         this.text.getStyleClass().setAll((Object[])("text", "percentage"));
         this.indicator = new StackPane();
         this.indicator.setScaleShape(false);
         this.indicator.setCenterShape(false);
         this.indicator.getStyleClass().setAll((Object[])("indicator"));
         this.indicatorCircle = new Circle();
         this.indicator.setShape(this.indicatorCircle);
         this.arcShape = new Arc();
         this.arcShape.setType(ArcType.ROUND);
         this.arcShape.setStartAngle(90.0);
         this.progress = new StackPane();
         this.progress.getStyleClass().setAll((Object[])("progress"));
         this.progress.setScaleShape(false);
         this.progress.setCenterShape(false);
         this.progress.setShape(this.arcShape);
         this.progress.getChildren().clear();
         this.setFillOverride(var4);
         this.tick = new StackPane();
         this.tick.getStyleClass().setAll((Object[])("tick"));
         this.getChildren().setAll((Object[])(this.indicator, this.progress, this.text, this.tick));
         this.updateProgress(var2.getProgress());
      }

      private void setFillOverride(Paint var1) {
         if (var1 instanceof Color) {
            Color var2 = (Color)var1;
            this.progress.setStyle("-fx-background-color: rgba(" + (int)(255.0 * var2.getRed()) + "," + (int)(255.0 * var2.getGreen()) + "," + (int)(255.0 * var2.getBlue()) + "," + var2.getOpacity() + ");");
         } else {
            this.progress.setStyle((String)null);
         }

      }

      public boolean usesMirroring() {
         return false;
      }

      private void updateProgress(double var1) {
         this.intProgress = (int)Math.round(var1 * 100.0);
         this.text.setText(var1 >= 1.0 ? ProgressIndicatorSkin.DONE : "" + this.intProgress + "%");
         this.degProgress = (int)(360.0 * var1);
         this.arcShape.setLength((double)(-this.degProgress));
         this.requestLayout();
      }

      protected void layoutChildren() {
         double var1 = ProgressIndicatorSkin.doneText.getLayoutBounds().getHeight();
         double var3 = ProgressIndicatorSkin.this.control.snappedLeftInset();
         double var5 = ProgressIndicatorSkin.this.control.snappedRightInset();
         double var7 = ProgressIndicatorSkin.this.control.snappedTopInset();
         double var9 = ProgressIndicatorSkin.this.control.snappedBottomInset();
         double var11 = ProgressIndicatorSkin.this.control.getWidth() - var3 - var5;
         double var13 = ProgressIndicatorSkin.this.control.getHeight() - var7 - var9 - this.textGap - var1;
         double var15 = var11 / 2.0;
         double var17 = var13 / 2.0;
         double var19 = Math.floor(Math.min(var15, var17));
         double var21 = this.snapPosition(var3 + var15);
         double var23 = this.snapPosition(var7 + var19);
         double var25 = this.indicator.snappedLeftInset();
         double var27 = this.indicator.snappedRightInset();
         double var29 = this.indicator.snappedTopInset();
         double var31 = this.indicator.snappedBottomInset();
         double var33 = this.snapSize(Math.min(Math.min(var19 - var25, var19 - var27), Math.min(var19 - var29, var19 - var31)));
         this.indicatorCircle.setRadius(var19);
         this.indicator.setLayoutX(var21);
         this.indicator.setLayoutY(var23);
         this.arcShape.setRadiusX(var33);
         this.arcShape.setRadiusY(var33);
         this.progress.setLayoutX(var21);
         this.progress.setLayoutY(var23);
         double var35 = this.progress.snappedLeftInset();
         double var37 = this.progress.snappedRightInset();
         double var39 = this.progress.snappedTopInset();
         double var41 = this.progress.snappedBottomInset();
         double var43 = this.snapSize(Math.min(Math.min(var33 - var35, var33 - var37), Math.min(var33 - var39, var33 - var41)));
         double var45 = Math.ceil(Math.sqrt(var43 * var43 / 2.0));
         double var10000 = var43 * (Math.sqrt(2.0) / 2.0);
         this.tick.setLayoutX(var21 - var45);
         this.tick.setLayoutY(var23 - var45);
         this.tick.resize(var45 + var45, var45 + var45);
         this.tick.setVisible(ProgressIndicatorSkin.this.control.getProgress() >= 1.0);
         double var49 = this.text.getLayoutBounds().getWidth();
         double var51 = this.text.getLayoutBounds().getHeight();
         if (ProgressIndicatorSkin.this.control.getWidth() >= var49 && ProgressIndicatorSkin.this.control.getHeight() >= var51) {
            if (!this.text.isVisible()) {
               this.text.setVisible(true);
            }

            this.text.setLayoutY(this.snapPosition(var23 + var19 + this.textGap));
            this.text.setLayoutX(this.snapPosition(var21 - var49 / 2.0));
         } else if (this.text.isVisible()) {
            this.text.setVisible(false);
         }

      }

      protected double computePrefWidth(double var1) {
         double var3 = ProgressIndicatorSkin.this.control.snappedLeftInset();
         double var5 = ProgressIndicatorSkin.this.control.snappedRightInset();
         double var7 = this.indicator.snappedLeftInset();
         double var9 = this.indicator.snappedRightInset();
         double var11 = this.indicator.snappedTopInset();
         double var13 = this.indicator.snappedBottomInset();
         double var15 = this.snapSize(Math.max(Math.max(var7, var9), Math.max(var11, var13)));
         double var17 = this.progress.snappedLeftInset();
         double var19 = this.progress.snappedRightInset();
         double var21 = this.progress.snappedTopInset();
         double var23 = this.progress.snappedBottomInset();
         double var25 = this.snapSize(Math.max(Math.max(var17, var19), Math.max(var21, var23)));
         double var27 = this.tick.snappedLeftInset();
         double var29 = this.tick.snappedRightInset();
         double var31 = var15 + var25 + var27 + var29 + var25 + var15;
         return var3 + Math.max(var31, ProgressIndicatorSkin.doneText.getLayoutBounds().getWidth()) + var5;
      }

      protected double computePrefHeight(double var1) {
         double var3 = ProgressIndicatorSkin.this.control.snappedTopInset();
         double var5 = ProgressIndicatorSkin.this.control.snappedBottomInset();
         double var7 = this.indicator.snappedLeftInset();
         double var9 = this.indicator.snappedRightInset();
         double var11 = this.indicator.snappedTopInset();
         double var13 = this.indicator.snappedBottomInset();
         double var15 = this.snapSize(Math.max(Math.max(var7, var9), Math.max(var11, var13)));
         double var17 = this.progress.snappedLeftInset();
         double var19 = this.progress.snappedRightInset();
         double var21 = this.progress.snappedTopInset();
         double var23 = this.progress.snappedBottomInset();
         double var25 = this.snapSize(Math.max(Math.max(var17, var19), Math.max(var21, var23)));
         double var27 = this.tick.snappedTopInset();
         double var29 = this.tick.snappedBottomInset();
         double var31 = var15 + var25 + var27 + var29 + var25 + var15;
         return var3 + var31 + this.textGap + ProgressIndicatorSkin.doneText.getLayoutBounds().getHeight() + var5;
      }

      protected double computeMaxWidth(double var1) {
         return this.computePrefWidth(var1);
      }

      protected double computeMaxHeight(double var1) {
         return this.computePrefHeight(var1);
      }
   }
}
