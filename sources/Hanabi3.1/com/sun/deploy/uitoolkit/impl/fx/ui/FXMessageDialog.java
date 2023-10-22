package com.sun.deploy.uitoolkit.impl.fx.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMessageDialog {
   Stage stage = new Stage();
   Scene scene;
   Group group = new Group();
   Button button1;
   private final Object responseLock = new Object();
   private int response = -1;

   public FXMessageDialog() {
      this.group.getChildren().add(new Text("MessageDialog"));
      this.group.getChildren().add(this.button1 = new Button("Button"));
      this.button1.setOnAction(new DialogEventHandler(1));
      this.scene = new Scene(this.group);
      Platform.runLater(new Runnable() {
         public void run() {
            FXMessageDialog.this.stage.show();
         }
      });
   }

   public int getResponse() {
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

   class DialogEventHandler implements EventHandler {
      int id;

      DialogEventHandler(int var2) {
         this.id = var2;
      }

      public void handle(ActionEvent var1) {
         synchronized(FXMessageDialog.this.responseLock) {
            FXMessageDialog.this.response = this.id;
            FXMessageDialog.this.responseLock.notifyAll();
         }
      }
   }
}
