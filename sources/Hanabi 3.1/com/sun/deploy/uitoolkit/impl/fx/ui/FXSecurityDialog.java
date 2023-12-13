package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.ui.AppInfo;
import com.sun.javafx.stage.StageHelper;
import java.net.URL;
import java.security.cert.Certificate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class FXSecurityDialog {
   private Stage stage;
   private Scene scene;
   private Button okButton;
   private Button cancelButton;
   private final Object responseLock = new Object();
   private int response = -1;

   FXSecurityDialog(AppInfo var1, final String var2, final String var3, String var4, URL var5, boolean var6, boolean var7, final String var8, final String var9, String[] var10, String[] var11, boolean var12, Certificate[] var13, int var14, int var15, boolean var16) {
      Platform.runLater(new Runnable() {
         public void run() {
            FXSecurityDialog.this.stage = new Stage();
            FXSecurityDialog.this.stage.setTitle(var2);
            StageHelper.initSecurityDialog(FXSecurityDialog.this.stage, true);
            VBox var1 = new VBox();
            Text var2x = new Text(var3);
            var1.getChildren().add(var2x);
            HBox var3x = new HBox();
            FXSecurityDialog.this.okButton = new Button(var8);
            FXSecurityDialog.this.cancelButton = new Button(var9);
            var3x.getChildren().add(FXSecurityDialog.this.okButton);
            var3x.getChildren().add(FXSecurityDialog.this.cancelButton);
            var1.getChildren().add(var3x);
            DialogEventHandler var4 = FXSecurityDialog.this.new DialogEventHandler();
            FXSecurityDialog.this.okButton.setOnAction(var4);
            FXSecurityDialog.this.cancelButton.setOnAction(var4);
            FXSecurityDialog.this.scene = new Scene(var1, 640.0, 480.0);
            FXSecurityDialog.this.stage.setScene(FXSecurityDialog.this.scene);
         }
      });
   }

   void setVisible(boolean var1) {
      Platform.runLater(new Runnable() {
         public void run() {
            FXSecurityDialog.this.stage.show();
         }
      });
   }

   int getResponse() {
      synchronized(this.responseLock) {
         if (this.response == -1) {
            try {
               this.responseLock.wait();
            } catch (InterruptedException var4) {
               System.out.println("interupted " + var4);
            }
         }

         return this.response;
      }
   }

   private class DialogEventHandler implements EventHandler {
      private DialogEventHandler() {
      }

      public void handle(ActionEvent var1) {
         synchronized(FXSecurityDialog.this.responseLock) {
            if (var1.getSource() == FXSecurityDialog.this.okButton) {
               FXSecurityDialog.this.response = 0;
            } else {
               FXSecurityDialog.this.response = 1;
            }

            FXSecurityDialog.this.responseLock.notifyAll();
            FXSecurityDialog.this.stage.close();
         }
      }

      // $FF: synthetic method
      DialogEventHandler(Object var2) {
         this();
      }
   }
}
