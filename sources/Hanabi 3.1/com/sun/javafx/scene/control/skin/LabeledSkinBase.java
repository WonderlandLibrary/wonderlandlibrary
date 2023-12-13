package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.TextBinding;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public abstract class LabeledSkinBase extends BehaviorSkinBase {
   LabeledText text;
   boolean invalidText = true;
   Node graphic;
   double textWidth = Double.NEGATIVE_INFINITY;
   double ellipsisWidth = Double.NEGATIVE_INFINITY;
   final InvalidationListener graphicPropertyChangedListener = (var1x) -> {
      this.invalidText = true;
      if (this.getSkinnable() != null) {
         ((Labeled)this.getSkinnable()).requestLayout();
      }

   };
   private Rectangle textClip;
   private double wrapWidth;
   private double wrapHeight;
   public TextBinding bindings;
   Line mnemonic_underscore;
   private boolean containsMnemonic = false;
   private Scene mnemonicScene = null;
   private KeyCombination mnemonicCode;
   private Node labeledNode = null;

   public LabeledSkinBase(Labeled var1, BehaviorBase var2) {
      super(var1, var2);
      this.text = new LabeledText(var1);
      this.updateChildren();
      this.registerChangeListener(var1.ellipsisStringProperty(), "ELLIPSIS_STRING");
      this.registerChangeListener(var1.widthProperty(), "WIDTH");
      this.registerChangeListener(var1.heightProperty(), "HEIGHT");
      this.registerChangeListener(var1.textFillProperty(), "TEXT_FILL");
      this.registerChangeListener(var1.fontProperty(), "FONT");
      this.registerChangeListener(var1.graphicProperty(), "GRAPHIC");
      this.registerChangeListener(var1.contentDisplayProperty(), "CONTENT_DISPLAY");
      this.registerChangeListener(var1.labelPaddingProperty(), "LABEL_PADDING");
      this.registerChangeListener(var1.graphicTextGapProperty(), "GRAPHIC_TEXT_GAP");
      this.registerChangeListener(var1.alignmentProperty(), "ALIGNMENT");
      this.registerChangeListener(var1.mnemonicParsingProperty(), "MNEMONIC_PARSING");
      this.registerChangeListener(var1.textProperty(), "TEXT");
      this.registerChangeListener(var1.textAlignmentProperty(), "TEXT_ALIGNMENT");
      this.registerChangeListener(var1.textOverrunProperty(), "TEXT_OVERRUN");
      this.registerChangeListener(var1.wrapTextProperty(), "WRAP_TEXT");
      this.registerChangeListener(var1.underlineProperty(), "UNDERLINE");
      this.registerChangeListener(var1.lineSpacingProperty(), "LINE_SPACING");
      this.registerChangeListener(var1.sceneProperty(), "SCENE");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("WIDTH".equals(var1)) {
         this.updateWrappingWidth();
         this.invalidText = true;
      } else if ("HEIGHT".equals(var1)) {
         this.invalidText = true;
      } else if ("FONT".equals(var1)) {
         this.textMetricsChanged();
         this.invalidateWidths();
         this.ellipsisWidth = Double.NEGATIVE_INFINITY;
      } else if ("GRAPHIC".equals(var1)) {
         this.updateChildren();
         this.textMetricsChanged();
      } else if ("CONTENT_DISPLAY".equals(var1)) {
         this.updateChildren();
         this.textMetricsChanged();
      } else if ("LABEL_PADDING".equals(var1)) {
         this.textMetricsChanged();
      } else if ("GRAPHIC_TEXT_GAP".equals(var1)) {
         this.textMetricsChanged();
      } else if ("ALIGNMENT".equals(var1)) {
         ((Labeled)this.getSkinnable()).requestLayout();
      } else if ("MNEMONIC_PARSING".equals(var1)) {
         this.containsMnemonic = false;
         this.textMetricsChanged();
      } else if ("TEXT".equals(var1)) {
         this.updateChildren();
         this.textMetricsChanged();
         this.invalidateWidths();
      } else if (!"TEXT_ALIGNMENT".equals(var1)) {
         if ("TEXT_OVERRUN".equals(var1)) {
            this.textMetricsChanged();
         } else if ("ELLIPSIS_STRING".equals(var1)) {
            this.textMetricsChanged();
            this.invalidateWidths();
            this.ellipsisWidth = Double.NEGATIVE_INFINITY;
         } else if ("WRAP_TEXT".equals(var1)) {
            this.updateWrappingWidth();
            this.textMetricsChanged();
         } else if ("UNDERLINE".equals(var1)) {
            this.textMetricsChanged();
         } else if ("LINE_SPACING".equals(var1)) {
            this.textMetricsChanged();
         } else if ("SCENE".equals(var1)) {
            this.sceneChanged();
         }
      }

   }

   protected double topLabelPadding() {
      return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getTop());
   }

   protected double bottomLabelPadding() {
      return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getBottom());
   }

   protected double leftLabelPadding() {
      return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getLeft());
   }

   protected double rightLabelPadding() {
      return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getRight());
   }

   private void textMetricsChanged() {
      this.invalidText = true;
      ((Labeled)this.getSkinnable()).requestLayout();
   }

   protected void mnemonicTargetChanged() {
      if (this.containsMnemonic) {
         this.removeMnemonic();
         Control var1 = this.getSkinnable();
         if (var1 instanceof Label) {
            this.labeledNode = ((Label)var1).getLabelFor();
            this.addMnemonic();
         } else {
            this.labeledNode = null;
         }
      }

   }

   private void sceneChanged() {
      Labeled var1 = (Labeled)this.getSkinnable();
      Scene var2 = var1.getScene();
      if (var2 != null && this.containsMnemonic) {
         this.addMnemonic();
      }

   }

   private void invalidateWidths() {
      this.textWidth = Double.NEGATIVE_INFINITY;
   }

   void updateDisplayedText() {
      this.updateDisplayedText(-1.0, -1.0);
   }

   private void updateDisplayedText(double var1, double var3) {
      if (this.invalidText) {
         Labeled var5 = (Labeled)this.getSkinnable();
         String var6 = var5.getText();
         int var7 = -1;
         if (var6 != null && var6.length() > 0) {
            this.bindings = new TextBinding(var6);
            if (!PlatformUtil.isMac() && ((Labeled)this.getSkinnable()).isMnemonicParsing()) {
               if (var5 instanceof Label) {
                  this.labeledNode = ((Label)var5).getLabelFor();
               } else {
                  this.labeledNode = var5;
               }

               if (this.labeledNode == null) {
                  this.labeledNode = var5;
               }

               var7 = this.bindings.getMnemonicIndex();
            }
         }

         if (this.containsMnemonic) {
            if (this.mnemonicScene != null && (var7 == -1 || this.bindings != null && !this.bindings.getMnemonicKeyCombination().equals(this.mnemonicCode))) {
               this.removeMnemonic();
               this.containsMnemonic = false;
            }
         } else {
            this.removeMnemonic();
         }

         if (var6 != null && var6.length() > 0 && var7 >= 0 && !this.containsMnemonic) {
            this.containsMnemonic = true;
            this.mnemonicCode = this.bindings.getMnemonicKeyCombination();
            this.addMnemonic();
         }

         if (this.containsMnemonic) {
            var6 = this.bindings.getText();
            if (this.mnemonic_underscore == null) {
               this.mnemonic_underscore = new Line();
               this.mnemonic_underscore.setStartX(0.0);
               this.mnemonic_underscore.setStartY(0.0);
               this.mnemonic_underscore.setEndY(0.0);
               this.mnemonic_underscore.getStyleClass().clear();
               this.mnemonic_underscore.getStyleClass().setAll((Object[])("mnemonic-underline"));
            }

            if (!this.getChildren().contains(this.mnemonic_underscore)) {
               this.getChildren().add(this.mnemonic_underscore);
            }
         } else {
            if (((Labeled)this.getSkinnable()).isMnemonicParsing() && PlatformUtil.isMac() && this.bindings != null) {
               var6 = this.bindings.getText();
            } else {
               var6 = var5.getText();
            }

            if (this.mnemonic_underscore != null && this.getChildren().contains(this.mnemonic_underscore)) {
               Platform.runLater(() -> {
                  this.getChildren().remove(this.mnemonic_underscore);
                  this.mnemonic_underscore = null;
               });
            }
         }

         int var8 = var6 != null ? var6.length() : 0;
         boolean var9 = false;
         if (var6 != null && var8 > 0) {
            int var10 = var6.indexOf(10);
            if (var10 > -1 && var10 < var8 - 1) {
               var9 = true;
            }
         }

         boolean var11 = var5.getContentDisplay() == ContentDisplay.LEFT || var5.getContentDisplay() == ContentDisplay.RIGHT;
         double var12 = var5.getWidth() - this.snappedLeftInset() - this.leftLabelPadding() - this.snappedRightInset() - this.rightLabelPadding();
         var12 = Math.max(var12, 0.0);
         if (var1 == -1.0) {
            var1 = var12;
         }

         double var14 = Math.min(this.computeMinLabeledPartWidth(-1.0, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset()), var12);
         if (var11 && !this.isIgnoreGraphic()) {
            double var16 = var5.getGraphic().getLayoutBounds().getWidth() + var5.getGraphicTextGap();
            var1 -= var16;
            var14 -= var16;
         }

         this.wrapWidth = Math.max(var14, var1);
         boolean var28 = var5.getContentDisplay() == ContentDisplay.TOP || var5.getContentDisplay() == ContentDisplay.BOTTOM;
         double var17 = var5.getHeight() - this.snappedTopInset() - this.topLabelPadding() - this.snappedBottomInset() - this.bottomLabelPadding();
         var17 = Math.max(var17, 0.0);
         if (var3 == -1.0) {
            var3 = var17;
         }

         double var19 = Math.min(this.computeMinLabeledPartHeight(this.wrapWidth, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset()), var17);
         if (var28 && var5.getGraphic() != null) {
            double var21 = var5.getGraphic().getLayoutBounds().getHeight() + var5.getGraphicTextGap();
            var3 -= var21;
            var19 -= var21;
         }

         this.wrapHeight = Math.max(var19, var3);
         this.updateWrappingWidth();
         Font var29 = this.text.getFont();
         OverrunStyle var22 = var5.getTextOverrun();
         String var23 = var5.getEllipsisString();
         String var27;
         if (var5.isWrapText()) {
            var27 = Utils.computeClippedWrappedText(var29, var6, this.wrapWidth, this.wrapHeight, var22, var23, this.text.getBoundsType());
         } else if (!var9) {
            var27 = Utils.computeClippedText(var29, var6, this.wrapWidth, var22, var23);
         } else {
            StringBuilder var24 = new StringBuilder();
            String[] var25 = var6.split("\n");

            for(int var26 = 0; var26 < var25.length; ++var26) {
               var24.append(Utils.computeClippedText(var29, var25[var26], this.wrapWidth, var22, var23));
               if (var26 < var25.length - 1) {
                  var24.append('\n');
               }
            }

            var27 = var24.toString();
         }

         if (var27 != null && var27.endsWith("\n")) {
            var27 = var27.substring(0, var27.length() - 1);
         }

         this.text.setText(var27);
         this.updateWrappingWidth();
         this.invalidText = false;
      }

   }

   private void addMnemonic() {
      if (this.labeledNode != null) {
         this.mnemonicScene = this.labeledNode.getScene();
         if (this.mnemonicScene != null) {
            this.mnemonicScene.addMnemonic(new Mnemonic(this.labeledNode, this.mnemonicCode));
         }
      }

   }

   private void removeMnemonic() {
      if (this.mnemonicScene != null && this.labeledNode != null) {
         this.mnemonicScene.removeMnemonic(new Mnemonic(this.labeledNode, this.mnemonicCode));
         this.mnemonicScene = null;
      }

   }

   private void updateWrappingWidth() {
      Labeled var1 = (Labeled)this.getSkinnable();
      this.text.setWrappingWidth(0.0);
      if (var1.isWrapText()) {
         double var2 = Math.min(this.text.prefWidth(-1.0), this.wrapWidth);
         this.text.setWrappingWidth(var2);
      }

   }

   protected void updateChildren() {
      Labeled var1 = (Labeled)this.getSkinnable();
      if (this.graphic != null) {
         this.graphic.layoutBoundsProperty().removeListener(this.graphicPropertyChangedListener);
      }

      this.graphic = var1.getGraphic();
      if (this.graphic instanceof ImageView) {
         this.graphic.setMouseTransparent(true);
      }

      if (this.isIgnoreGraphic()) {
         if (var1.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY) {
            this.getChildren().clear();
         } else {
            this.getChildren().setAll((Object[])(this.text));
         }
      } else {
         this.graphic.layoutBoundsProperty().addListener(this.graphicPropertyChangedListener);
         if (this.isIgnoreText()) {
            this.getChildren().setAll((Object[])(this.graphic));
         } else {
            this.getChildren().setAll((Object[])(this.graphic, this.text));
         }

         this.graphic.impl_processCSS(false);
      }

   }

   protected boolean isIgnoreGraphic() {
      return this.graphic == null || !this.graphic.isManaged() || ((Labeled)this.getSkinnable()).getContentDisplay() == ContentDisplay.TEXT_ONLY;
   }

   protected boolean isIgnoreText() {
      Labeled var1 = (Labeled)this.getSkinnable();
      String var2 = var1.getText();
      return var2 == null || var2.equals("") || var1.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY;
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      return this.computeMinLabeledPartWidth(var1, var3, var5, var7, var9);
   }

   private double computeMinLabeledPartWidth(double var1, double var3, double var5, double var7, double var9) {
      Labeled var11 = (Labeled)this.getSkinnable();
      ContentDisplay var12 = var11.getContentDisplay();
      double var13 = var11.getGraphicTextGap();
      double var15 = 0.0;
      Font var17 = this.text.getFont();
      OverrunStyle var18 = var11.getTextOverrun();
      String var19 = var11.getEllipsisString();
      String var20 = var11.getText();
      boolean var21 = var20 == null || var20.isEmpty();
      if (!var21) {
         if (var18 == OverrunStyle.CLIP) {
            if (this.textWidth == Double.NEGATIVE_INFINITY) {
               this.textWidth = Utils.computeTextWidth(var17, var20.substring(0, 1), 0.0);
            }

            var15 = this.textWidth;
         } else {
            if (this.textWidth == Double.NEGATIVE_INFINITY) {
               this.textWidth = Utils.computeTextWidth(var17, var20, 0.0);
            }

            if (this.ellipsisWidth == Double.NEGATIVE_INFINITY) {
               this.ellipsisWidth = Utils.computeTextWidth(var17, var19, 0.0);
            }

            var15 = Math.min(this.textWidth, this.ellipsisWidth);
         }
      }

      Node var22 = var11.getGraphic();
      double var23;
      if (this.isIgnoreGraphic()) {
         var23 = var15;
      } else if (this.isIgnoreText()) {
         var23 = var22.minWidth(-1.0);
      } else if (var12 != ContentDisplay.LEFT && var12 != ContentDisplay.RIGHT) {
         var23 = Math.max(var15, var22.minWidth(-1.0));
      } else {
         var23 = var15 + var22.minWidth(-1.0) + var13;
      }

      return var23 + var9 + this.leftLabelPadding() + var5 + this.rightLabelPadding();
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.computeMinLabeledPartHeight(var1, var3, var5, var7, var9);
   }

   private double computeMinLabeledPartHeight(double var1, double var3, double var5, double var7, double var9) {
      Labeled var11 = (Labeled)this.getSkinnable();
      Font var12 = this.text.getFont();
      String var13 = var11.getText();
      if (var13 != null && var13.length() > 0) {
         int var14 = var13.indexOf(10);
         if (var14 >= 0) {
            var13 = var13.substring(0, var14);
         }
      }

      double var21 = var11.getLineSpacing();
      double var16 = Utils.computeTextHeight(var12, var13, 0.0, var21, this.text.getBoundsType());
      double var18 = var16;
      if (!this.isIgnoreGraphic()) {
         Node var20 = var11.getGraphic();
         if (var11.getContentDisplay() != ContentDisplay.TOP && var11.getContentDisplay() != ContentDisplay.BOTTOM) {
            var18 = Math.max(var16, var20.minHeight(var1));
         } else {
            var18 = var20.minHeight(var1) + var11.getGraphicTextGap() + var16;
         }
      }

      return var3 + var18 + var7 + this.topLabelPadding() - this.bottomLabelPadding();
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      Labeled var11 = (Labeled)this.getSkinnable();
      Font var12 = this.text.getFont();
      String var13 = var11.getText();
      boolean var14 = var13 == null || var13.isEmpty();
      double var15 = var9 + this.leftLabelPadding() + var5 + this.rightLabelPadding();
      double var17 = var14 ? 0.0 : Utils.computeTextWidth(var12, var13, 0.0);
      double var19 = this.graphic == null ? 0.0 : Utils.boundedSize(this.graphic.prefWidth(-1.0), this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
      Node var21 = var11.getGraphic();
      if (this.isIgnoreGraphic()) {
         return var17 + var15;
      } else if (this.isIgnoreText()) {
         return var19 + var15;
      } else {
         return var11.getContentDisplay() != ContentDisplay.LEFT && var11.getContentDisplay() != ContentDisplay.RIGHT ? Math.max(var17, var19) + var15 : var17 + var11.getGraphicTextGap() + var19 + var15;
      }
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      Labeled var11 = (Labeled)this.getSkinnable();
      Font var12 = this.text.getFont();
      ContentDisplay var13 = var11.getContentDisplay();
      double var14 = var11.getGraphicTextGap();
      var1 -= var9 + this.leftLabelPadding() + var5 + this.rightLabelPadding();
      String var16 = var11.getText();
      if (var16 != null && var16.endsWith("\n")) {
         var16 = var16.substring(0, var16.length() - 1);
      }

      double var17 = var1;
      if (!this.isIgnoreGraphic() && (var13 == ContentDisplay.LEFT || var13 == ContentDisplay.RIGHT)) {
         var17 = var1 - (this.graphic.prefWidth(-1.0) + var14);
      }

      double var19 = Utils.computeTextHeight(var12, var16, var11.isWrapText() ? var17 : 0.0, var11.getLineSpacing(), this.text.getBoundsType());
      double var21 = var19;
      if (!this.isIgnoreGraphic()) {
         Node var23 = var11.getGraphic();
         if (var13 != ContentDisplay.TOP && var13 != ContentDisplay.BOTTOM) {
            var21 = Math.max(var19, var23.prefHeight(var1));
         } else {
            var21 = var23.prefHeight(var1) + var14 + var19;
         }
      }

      return var3 + var21 + var7 + this.topLabelPadding() + this.bottomLabelPadding();
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return ((Labeled)this.getSkinnable()).prefWidth(var1);
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((Labeled)this.getSkinnable()).prefHeight(var1);
   }

   public double computeBaselineOffset(double var1, double var3, double var5, double var7) {
      double var9 = this.text.getBaselineOffset();
      double var11 = var9;
      Labeled var13 = (Labeled)this.getSkinnable();
      Node var14 = var13.getGraphic();
      if (!this.isIgnoreGraphic()) {
         ContentDisplay var15 = var13.getContentDisplay();
         if (var15 == ContentDisplay.TOP) {
            var11 = var14.prefHeight(-1.0) + var13.getGraphicTextGap() + var9;
         } else if (var15 == ContentDisplay.LEFT || var15 == ContentDisplay.RIGHT) {
            var11 = var9 + (var14.prefHeight(-1.0) - this.text.prefHeight(-1.0)) / 2.0;
         }
      }

      return var1 + this.topLabelPadding() + var11;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      this.layoutLabelInArea(var1, var3, var5, var7);
   }

   protected void layoutLabelInArea(double var1, double var3, double var5, double var7) {
      this.layoutLabelInArea(var1, var3, var5, var7, (Pos)null);
   }

   protected void layoutLabelInArea(double var1, double var3, double var5, double var7, Pos var9) {
      Labeled var10 = (Labeled)this.getSkinnable();
      ContentDisplay var11 = var10.getContentDisplay();
      if (var9 == null) {
         var9 = var10.getAlignment();
      }

      HPos var12 = var9 == null ? HPos.LEFT : var9.getHpos();
      VPos var13 = var9 == null ? VPos.CENTER : var9.getVpos();
      boolean var14 = this.isIgnoreGraphic();
      boolean var15 = this.isIgnoreText();
      var1 += this.leftLabelPadding();
      var3 += this.topLabelPadding();
      var5 -= this.leftLabelPadding() + this.rightLabelPadding();
      var7 -= this.topLabelPadding() + this.bottomLabelPadding();
      double var16;
      double var18;
      if (var14) {
         var18 = 0.0;
         var16 = 0.0;
      } else if (var15) {
         if (this.graphic.isResizable()) {
            Orientation var24 = this.graphic.getContentBias();
            if (var24 == Orientation.HORIZONTAL) {
               var16 = Utils.boundedSize(var5, this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
               var18 = Utils.boundedSize(var7, this.graphic.minHeight(var16), this.graphic.maxHeight(var16));
            } else if (var24 == Orientation.VERTICAL) {
               var18 = Utils.boundedSize(var7, this.graphic.minHeight(-1.0), this.graphic.maxHeight(-1.0));
               var16 = Utils.boundedSize(var5, this.graphic.minWidth(var18), this.graphic.maxWidth(var18));
            } else {
               var16 = Utils.boundedSize(var5, this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
               var18 = Utils.boundedSize(var7, this.graphic.minHeight(-1.0), this.graphic.maxHeight(-1.0));
            }

            this.graphic.resize(var16, var18);
         } else {
            var16 = this.graphic.getLayoutBounds().getWidth();
            var18 = this.graphic.getLayoutBounds().getHeight();
         }
      } else {
         this.graphic.autosize();
         var16 = this.graphic.getLayoutBounds().getWidth();
         var18 = this.graphic.getLayoutBounds().getHeight();
      }

      double var20;
      double var22;
      if (var15) {
         var22 = 0.0;
         var20 = 0.0;
         this.text.setText("");
      } else {
         this.updateDisplayedText(var5, var7);
         var20 = this.snapSize(Math.min(this.text.getLayoutBounds().getWidth(), this.wrapWidth));
         var22 = this.snapSize(Math.min(this.text.getLayoutBounds().getHeight(), this.wrapHeight));
      }

      double var48 = !var15 && !var14 ? var10.getGraphicTextGap() : 0.0;
      double var26 = Math.max(var16, var20);
      double var28 = Math.max(var18, var22);
      if (var11 != ContentDisplay.TOP && var11 != ContentDisplay.BOTTOM) {
         if (var11 == ContentDisplay.LEFT || var11 == ContentDisplay.RIGHT) {
            var26 = var16 + var48 + var20;
         }
      } else {
         var28 = var18 + var48 + var22;
      }

      double var30;
      if (var12 == HPos.LEFT) {
         var30 = var1;
      } else if (var12 == HPos.RIGHT) {
         var30 = var1 + (var5 - var26);
      } else {
         var30 = var1 + (var5 - var26) / 2.0;
      }

      double var32;
      if (var13 == VPos.TOP) {
         var32 = var3;
      } else if (var13 == VPos.BOTTOM) {
         var32 = var3 + (var7 - var28);
      } else {
         var32 = var3 + (var7 - var28) / 2.0;
      }

      double var34 = 0.0;
      double var36 = 0.0;
      double var38 = 0.0;
      if (this.containsMnemonic) {
         Font var40 = this.text.getFont();
         String var41 = this.bindings.getText();
         var34 = Utils.computeTextWidth(var40, var41.substring(0, this.bindings.getMnemonicIndex()), 0.0);
         var36 = Utils.computeTextWidth(var40, var41.substring(this.bindings.getMnemonicIndex(), this.bindings.getMnemonicIndex() + 1), 0.0);
         var38 = Utils.computeTextHeight(var40, "_", 0.0, this.text.getBoundsType());
      }

      if ((!var14 || !var15) && !this.text.isManaged()) {
         this.text.setManaged(true);
      }

      if (var14 && var15) {
         if (this.text.isManaged()) {
            this.text.setManaged(false);
         }

         this.text.relocate(this.snapPosition(var30), this.snapPosition(var32));
      } else if (var14) {
         this.text.relocate(this.snapPosition(var30), this.snapPosition(var32));
         if (this.containsMnemonic) {
            this.mnemonic_underscore.setEndX(var36 - 2.0);
            this.mnemonic_underscore.relocate(var30 + var34, var32 + var38 - 1.0);
         }
      } else if (var15) {
         this.text.relocate(this.snapPosition(var30), this.snapPosition(var32));
         this.graphic.relocate(this.snapPosition(var30), this.snapPosition(var32));
         if (this.containsMnemonic) {
            this.mnemonic_underscore.setEndX(var36);
            this.mnemonic_underscore.setStrokeWidth(var38 / 10.0);
            this.mnemonic_underscore.relocate(var30 + var34, var32 + var38 - 1.0);
         }
      } else {
         double var49 = 0.0;
         double var42 = 0.0;
         double var44 = 0.0;
         double var46 = 0.0;
         if (var11 == ContentDisplay.TOP) {
            var49 = var30 + (var26 - var16) / 2.0;
            var44 = var30 + (var26 - var20) / 2.0;
            var42 = var32;
            var46 = var32 + var18 + var48;
         } else if (var11 == ContentDisplay.RIGHT) {
            var44 = var30;
            var49 = var30 + var20 + var48;
            var42 = var32 + (var28 - var18) / 2.0;
            var46 = var32 + (var28 - var22) / 2.0;
         } else if (var11 == ContentDisplay.BOTTOM) {
            var49 = var30 + (var26 - var16) / 2.0;
            var44 = var30 + (var26 - var20) / 2.0;
            var46 = var32;
            var42 = var32 + var22 + var48;
         } else if (var11 == ContentDisplay.LEFT) {
            var49 = var30;
            var44 = var30 + var16 + var48;
            var42 = var32 + (var28 - var18) / 2.0;
            var46 = var32 + (var28 - var22) / 2.0;
         } else if (var11 == ContentDisplay.CENTER) {
            var49 = var30 + (var26 - var16) / 2.0;
            var44 = var30 + (var26 - var20) / 2.0;
            var42 = var32 + (var28 - var18) / 2.0;
            var46 = var32 + (var28 - var22) / 2.0;
         }

         this.text.relocate(this.snapPosition(var44), this.snapPosition(var46));
         if (this.containsMnemonic) {
            this.mnemonic_underscore.setEndX(var36);
            this.mnemonic_underscore.setStrokeWidth(var38 / 10.0);
            this.mnemonic_underscore.relocate(this.snapPosition(var44 + var34), this.snapPosition(var46 + var38 - 1.0));
         }

         this.graphic.relocate(this.snapPosition(var49), this.snapPosition(var42));
      }

      if (this.text != null && (this.text.getLayoutBounds().getHeight() > this.wrapHeight || this.text.getLayoutBounds().getWidth() > this.wrapWidth)) {
         if (this.textClip == null) {
            this.textClip = new Rectangle();
         }

         if (var10.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT) {
            this.textClip.setX(this.text.getLayoutBounds().getMinX());
         } else {
            this.textClip.setX(this.text.getLayoutBounds().getMaxX() - this.wrapWidth);
         }

         this.textClip.setY(this.text.getLayoutBounds().getMinY());
         this.textClip.setWidth(this.wrapWidth);
         this.textClip.setHeight(this.wrapHeight);
         if (this.text.getClip() == null) {
            this.text.setClip(this.textClip);
         }
      } else if (this.text.getClip() != null) {
         this.text.setClip((Node)null);
      }

   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case TEXT:
            Labeled var3 = (Labeled)this.getSkinnable();
            String var4 = var3.getAccessibleText();
            if (var4 != null && !var4.isEmpty()) {
               return var4;
            } else {
               String var5;
               if (this.bindings != null) {
                  var5 = this.bindings.getText();
                  if (var5 != null && !var5.isEmpty()) {
                     return var5;
                  }
               }

               if (var3 != null) {
                  var5 = var3.getText();
                  if (var5 != null && !var5.isEmpty()) {
                     return var5;
                  }
               }

               if (this.graphic != null) {
                  Object var6 = this.graphic.queryAccessibleAttribute(AccessibleAttribute.TEXT);
                  if (var6 != null) {
                     return var6;
                  }
               }

               return null;
            }
         case MNEMONIC:
            if (this.bindings != null) {
               return this.bindings.getMnemonic();
            }

            return null;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
