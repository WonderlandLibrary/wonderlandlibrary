package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.FXPreloader;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.parser.CSSParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.application.HostServices;
import javafx.application.Preloader;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXDefaultPreloader extends Preloader {
   static final double FADE_DURATION = 2000.0;
   Stage stage;
   FXPreloaderPane pane;
   double currentWidth = 0.0;
   double currentHeight = 0.0;
   boolean isEmbedded;

   public void init() {
      this.pane = new FXPreloaderPane();
   }

   public void start(Stage var1) {
      this.stage = var1;
      this.stage.setTitle("JavaFX Application Preview");
      this.isEmbedded = var1.getWidth() >= 0.0 && var1.getHeight() >= 0.0;
   }

   private void showIfNeeded() {
      if (!this.stage.isShowing()) {
         this.stage.setScene(new FXPreloaderScene(this.pane));
         this.stage.show();
         ((FXPreloaderScene)this.stage.getScene()).loadStylesheets();
         this.stage.toFront();
         FXPreloader.hideSplash();
      }

   }

   public void stop() {
   }

   public void handleStateChangeNotification(Preloader.StateChangeNotification var1) {
      switch (var1.getType()) {
         case BEFORE_START:
            if (this.stage.isShowing()) {
               if (this.isEmbedded) {
                  FadeTransition var2 = new FadeTransition(Duration.millis(2000.0), this.pane);
                  var2.setFromValue(1.0);
                  var2.setToValue(0.0);
                  var2.setOnFinished(new FadeOutFinisher(this.stage));
                  var2.play();
               } else {
                  this.stage.hide();
               }
            } else {
               FXPreloader.hideSplash();
            }
         default:
      }
   }

   public boolean handleErrorNotification(Preloader.ErrorNotification var1) {
      if (this.stage != null && this.stage.isShowing() && !this.isEmbedded) {
         this.stage.hide();
      } else if (this.isEmbedded) {
         FXPreloader.hideSplash();
      }

      return false;
   }

   public void handleProgressNotification(Preloader.ProgressNotification var1) {
      if (var1.getProgress() != 1.0) {
         this.showIfNeeded();
      }

      if (this.stage.isShowing()) {
         this.pane.setProgress(var1.getProgress());
      }

   }

   public List impl_CSS_STYLEABLES() {
      return FXDefaultPreloader.CSSProperties.STYLEABLES;
   }

   private static class CSSProperties {
      private static CssMetaData PRELOADER_TEXT = new CssMetaData("-fx-preloader-text", StringConverter.getInstance()) {
         public boolean isSettable(FXPreloaderPane var1) {
            return true;
         }

         public StyleableProperty getStyleableProperty(FXPreloaderPane var1) {
            return (StyleableProperty)var1.preloaderText;
         }
      };
      private static CssMetaData PRELOADER_GRAPHIC = new CssMetaData("-fx-preloader-graphic", StringConverter.getInstance()) {
         public boolean isSettable(FXPreloaderPane var1) {
            return true;
         }

         public StyleableProperty getStyleableProperty(FXPreloaderPane var1) {
            return (StyleableProperty)var1.preloaderGraphicUrl;
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList();
         Collections.addAll(var0, new CssMetaData[]{PRELOADER_TEXT, PRELOADER_GRAPHIC});
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   class FXPreloaderPane extends Pane {
      ProgressBar progressBar;
      Label status;
      Label percent;
      Label graphic;
      private StringProperty preloaderText = new StyleableStringProperty() {
         public Object getBean() {
            return FXPreloaderPane.this;
         }

         public String getName() {
            return "preloaderText";
         }

         public CssMetaData getCssMetaData() {
            return FXDefaultPreloader.CSSProperties.PRELOADER_TEXT;
         }
      };
      private StringProperty preloaderGraphicUrl = new StyleableStringProperty() {
         protected void invalidated() {
            if (this.getValue() != null) {
               if (FXPreloaderPane.this.graphic == null) {
                  FXPreloaderPane.this.graphic = new Label();
                  FXPreloaderPane.this.getChildren().add(FXPreloaderPane.this.graphic);
               }

               FXPreloaderPane.this.graphic.setGraphic(new ImageView(new Image(this.getValue())));
            }

         }

         public Object getBean() {
            return FXPreloaderPane.this;
         }

         public String getName() {
            return "preloaderGraphicUrl";
         }

         public CssMetaData getCssMetaData() {
            return FXDefaultPreloader.CSSProperties.PRELOADER_GRAPHIC;
         }
      };

      FXPreloaderPane() {
         this.getStyleClass().setAll((Object[])("default-preloader"));
         String var2 = (String)FXDefaultPreloader.this.getParameters().getNamed().get("javafx.default.preloader.style");
         if (var2 != null) {
            this.setStyle(var2);
         }

         this.progressBar = new ProgressBar();
         this.status = new Label(ResourceManager.getMessage("preloader.loading"));
         this.status.setId("preloader-status-label");
         this.percent = new Label("");
         this.percent.setId("preloader-percent-label");
         this.getChildren().addAll(this.progressBar, this.status, this.percent);
      }

      public void layoutChildren() {
         if (FXDefaultPreloader.this.currentWidth != this.getWidth() || FXDefaultPreloader.this.currentHeight != this.getHeight()) {
            FXDefaultPreloader.this.currentWidth = this.getWidth();
            FXDefaultPreloader.this.currentHeight = this.getHeight();
            this.setPrefHeight(FXDefaultPreloader.this.currentHeight);
            this.setPrefWidth(FXDefaultPreloader.this.currentWidth);
            if (FXDefaultPreloader.this.currentWidth > 40.0 && FXDefaultPreloader.this.currentHeight > 21.0) {
               this.percent.autosize();
               if (!(FXDefaultPreloader.this.currentWidth < 100.0) && !(FXDefaultPreloader.this.currentHeight < 100.0)) {
                  if (this.graphic != null) {
                     this.graphic.setVisible(true);
                     this.graphic.autosize();
                     this.graphic.relocate((FXDefaultPreloader.this.currentWidth - this.graphic.getWidth()) / 2.0, (FXDefaultPreloader.this.currentHeight / 2.0 - this.graphic.getHeight()) / 2.0);
                  }

                  this.status.setVisible(true);
                  this.status.autosize();
                  float var1 = FXDefaultPreloader.this.currentWidth < 240.0 ? 0.75F : 0.65F;
                  this.progressBar.setVisible(true);
                  this.progressBar.setPrefWidth(FXDefaultPreloader.this.currentWidth * (double)var1);
                  this.progressBar.resize(this.progressBar.prefWidth(-1.0), this.progressBar.prefHeight(-1.0));
                  this.progressBar.relocate((FXDefaultPreloader.this.currentWidth - this.progressBar.getWidth()) / 2.0, FXDefaultPreloader.this.currentHeight / 2.0 - this.progressBar.getHeight());
                  this.status.relocate(this.progressBar.getLayoutX(), this.progressBar.getLayoutY() + this.progressBar.getHeight() + 4.0);
                  this.percent.relocate(this.progressBar.getLayoutX() + this.progressBar.getWidth() - this.percent.getWidth(), this.progressBar.getLayoutY() - this.percent.getHeight() - 4.0);
               } else {
                  this.graphic.setVisible(false);
                  this.progressBar.setVisible(false);
                  this.status.setVisible(false);
                  this.percent.relocate((FXDefaultPreloader.this.currentWidth - this.percent.getWidth()) / 2.0, (FXDefaultPreloader.this.currentHeight - this.percent.getHeight()) / 2.0);
               }
            }

         }
      }

      void setProgress(double var1) {
         this.progressBar.setProgress(var1);
         this.percent.setText(String.format("%.0f%%", var1 * 100.0));
         this.percent.autosize();
         this.percent.setLayoutX(this.progressBar.getLayoutX() + this.progressBar.getPrefWidth() - this.percent.getWidth());
      }
   }

   private class FXPreloaderScene extends Scene {
      FXPreloaderScene(Parent var2) {
         super(var2, 600.0, 400.0);
         new Scene(new Pane(), 0.0, 0.0);
         this.setRoot(var2);
         this.getStylesheets().addAll(FXDefaultPreloader.class.getResource("deploydialogs.css").toExternalForm());
      }

      private void loadStylesheets() {
         String var1 = (String)FXDefaultPreloader.this.getParameters().getNamed().get("javafx.default.preloader.stylesheet");
         if (var1 != null) {
            HostServices var2 = FXDefaultPreloader.this.getHostServices();
            String var3 = var2.getDocumentBase();
            if (var1.matches(".*\\.[bc]ss$")) {
               String var4;
               if (var1.startsWith("jar:")) {
                  var4 = "jar:" + var2.resolveURI(var3, var1.substring(4));
               } else {
                  var4 = var2.resolveURI(var3, var1);
               }

               this.getStylesheets().add(var4);
            } else {
               try {
                  Stylesheet var6 = CSSParser.getInstance().parse(var3, var1);
                  StyleManager.getInstance().addUserAgentStylesheet(this, (Stylesheet)var6);
                  var6.setOrigin(StyleOrigin.AUTHOR);
               } catch (IOException var5) {
                  var5.printStackTrace();
               }
            }
         }

      }
   }

   private class FadeOutFinisher implements EventHandler {
      Stage stage;

      FadeOutFinisher(Stage var2) {
         this.stage = var2;
      }

      public void handle(ActionEvent var1) {
         if (this.stage.isShowing()) {
            this.stage.hide();
         }

      }
   }
}
