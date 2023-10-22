package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FXAboutDialog {
   static void showAboutJavaDialog() {
      final FXDialog var0 = new FXDialog(ResourceManager.getMessage("dialogfactory.aboutDialogTitle"));
      var0.setResizable(false);
      VBox var1 = new VBox();
      VBox var2 = new VBox();
      var2.setId("about-dialog-top-panel");
      var1.getChildren().add(var2);
      String var3 = System.getProperty("java.version");
      int var4 = var3.indexOf(".");
      String var5 = var3.substring(var4 + 1, var3.indexOf(".", var4 + 1));
      ImageView var6 = ResourceManager.getIcon("about.java" + ("6".equals(var5) ? "6" : "") + ".image");
      var2.getChildren().add(var6);
      VBox var7 = new VBox();
      var7.setId("about-dialog-center-panel");
      var2.getChildren().add(var7);
      StringBuilder var8 = new StringBuilder();
      var8.append(getVersionStr());
      var8.append("\n");
      var8.append(ResourceManager.getMessage("about.copyright"));
      var8.append("\n \n");
      var8.append(ResourceManager.getMessage("about.prompt.info"));
      Label var9 = new Label();
      var9.setWrapText(true);
      var9.setText(var8.toString());
      var9.setPrefWidth(var6.prefWidth(-1.0) - 16.0);
      var9.setMinWidth(Double.NEGATIVE_INFINITY);
      var7.getChildren().add(var9);
      final String var10 = ResourceManager.getMessage("about.home.link");
      Hyperlink var11 = new Hyperlink(var10);
      var11.setOnAction(new EventHandler() {
         public void handle(Event var1) {
            FXAboutDialog.browserToUrl(var10);
         }
      });
      var7.getChildren().add(var11);
      ImageView var12 = ResourceManager.getIcon("sun.logo.image");
      var7.getChildren().add(var12);
      StackPane var13 = new StackPane();
      var13.getStyleClass().add("button-bar");
      var13.setId("about-dialog-button-bar");
      var1.getChildren().add(var13);
      Button var14 = new Button(ResourceManager.getMessage("about.option.close"));
      var14.setDefaultButton(true);
      var14.setOnAction(new EventHandler() {
         public void handle(Event var1) {
            var0.close();
         }
      });
      var14.setAlignment(Pos.TOP_LEFT);
      var13.getChildren().add(var14);
      var0.setContentPane(var1);
      var0.show();
      var14.requestFocus();
   }

   private static void browserToUrl(String var0) {
      try {
         Desktop.getDesktop().browse(new URI(var0));
      } catch (URISyntaxException var2) {
         var2.printStackTrace();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private static String getVersionStr() {
      String var0 = System.getProperty("java.version");
      int var1 = var0.indexOf(".");
      String var2 = var0.substring(var1 + 1, var0.indexOf(".", var1 + 1));
      int var3 = var0.lastIndexOf("_");
      String var4 = null;
      if (var3 != -1) {
         int var5 = var0.indexOf("-");
         if (var5 != -1) {
            var4 = var0.substring(var3 + 1, var5);
         } else {
            var4 = var0.substring(var3 + 1, var0.length());
         }

         if (var4.startsWith("0")) {
            var4 = var4.substring(1);
         }
      }

      String var7 = null;
      if (var4 != null) {
         var7 = MessageFormat.format(ResourceManager.getMessage("about.java.version.update"), var2, var4);
      } else {
         var7 = MessageFormat.format(ResourceManager.getMessage("about.java.version"), var2);
      }

      String var6 = MessageFormat.format(ResourceManager.getMessage("about.java.build"), System.getProperty("java.runtime.version"));
      return var7 + " " + var6;
   }
}
