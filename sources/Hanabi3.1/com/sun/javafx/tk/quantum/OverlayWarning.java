package com.sun.javafx.tk.quantum;

import com.sun.javafx.scene.DirtyBits;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class OverlayWarning extends Group {
   private static final float PAD = 40.0F;
   private static final float RECTW = 600.0F;
   private static final float RECTH = 100.0F;
   private static final float ARC = 20.0F;
   private static final int FONTSIZE = 24;
   private ViewScene view;
   private SequentialTransition overlayTransition;
   private boolean warningTransition;
   private Text text = new Text();
   private Rectangle background;

   public OverlayWarning(ViewScene var1) {
      this.view = var1;
      this.createOverlayGroup();
      PauseTransition var2 = new PauseTransition(Duration.millis(4000.0));
      FadeTransition var3 = new FadeTransition(Duration.millis(1000.0), this);
      var3.setFromValue(1.0);
      var3.setToValue(0.0);
      this.overlayTransition = new SequentialTransition();
      this.overlayTransition.getChildren().add(var2);
      this.overlayTransition.getChildren().add(var3);
      this.overlayTransition.setOnFinished((var1x) -> {
         this.warningTransition = false;
         this.view.getWindowStage().setWarning((OverlayWarning)null);
      });
   }

   protected ViewScene getView() {
      return this.view;
   }

   protected final void setView(ViewScene var1) {
      if (this.view != null) {
         this.view.getWindowStage().setWarning((OverlayWarning)null);
      }

      this.view = var1;
      this.view.entireSceneNeedsRepaint();
   }

   protected void warn(String var1) {
      this.text.setText(var1);
      this.warningTransition = true;
      this.overlayTransition.play();
   }

   protected void cancel() {
      if (this.overlayTransition != null && this.overlayTransition.getStatus() == Animation.Status.RUNNING) {
         this.overlayTransition.stop();
         this.warningTransition = false;
      }

      this.view.getWindowStage().setWarning((OverlayWarning)null);
   }

   protected boolean inWarningTransition() {
      return this.warningTransition;
   }

   private void createOverlayGroup() {
      Font var1 = new Font(Font.getDefault().getFamily(), 24.0);
      Rectangle2D var2 = new Rectangle2D(0.0, 0.0, (double)this.view.getSceneState().getScreenWidth(), (double)this.view.getSceneState().getScreenHeight());
      String var3 = "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.75), 3, 0.0, 0, 2);";
      this.text.setStroke(Color.WHITE);
      this.text.setFill(Color.WHITE);
      this.text.setFont(var1);
      this.text.setWrappingWidth(520.0);
      this.text.setStyle(var3);
      this.text.setTextAlignment(TextAlignment.CENTER);
      this.background = this.createBackground(this.text, var2);
      this.getChildren().add(this.background);
      this.getChildren().add(this.text);
   }

   private Rectangle createBackground(Text var1, Rectangle2D var2) {
      Rectangle var3 = new Rectangle();
      double var4 = var1.getLayoutBounds().getWidth();
      double var6 = var1.getLayoutBounds().getHeight();
      double var8 = (var2.getWidth() - 600.0) / 2.0;
      double var10 = var2.getHeight() / 2.0;
      var3.setWidth(600.0);
      var3.setHeight(100.0);
      var3.setX(var8);
      var3.setY(var10 - 100.0);
      var3.setArcWidth(20.0);
      var3.setArcHeight(20.0);
      var3.setFill(Color.gray(0.0, 0.6));
      var1.setX(var8 + (600.0 - var4) / 2.0);
      var1.setY(var10 - 50.0 + (var6 - var1.getBaselineOffset()) / 2.0);
      return var3;
   }

   public void impl_updatePeer() {
      this.text.impl_updatePeer();
      this.background.impl_updatePeer();
      super.impl_updatePeer();
   }

   protected void updateBounds() {
      super.updateBounds();
   }

   protected void impl_markDirty(DirtyBits var1) {
      super.impl_markDirty(var1);
      this.view.synchroniseOverlayWarning();
   }
}
