package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.cert.Certificate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class MoreInfoDialog extends FXDialog {
   private Hyperlink details;
   private String[] alerts;
   private String[] infos;
   private int securityInfoCount;
   private Certificate[] certs;
   private int start;
   private int end;
   private boolean sandboxApp = false;
   private final String WARNING_ICON = "warning16.image";
   private final String INFO_ICON = "info16.image";
   private final int VERTICAL_STRUT = 18;
   private final int HORIZONTAL_STRUT = 12;
   private final int TEXT_WIDTH = 326;

   MoreInfoDialog(Stage var1, String[] var2, String[] var3, int var4, Certificate[] var5, int var6, int var7, boolean var8) {
      super(ResourceManager.getMessage("security.more.info.title"), var1, true);
      this.alerts = var2;
      this.infos = var3;
      this.securityInfoCount = var4;
      this.certs = var5;
      this.start = var6;
      this.end = var7;
      this.sandboxApp = var8;
      this.initComponents((Pane)null, (Throwable)null);
      this.setResizable(false);
   }

   MoreInfoDialog(Stage var1, Pane var2, Throwable var3, Certificate[] var4) {
      super(ResourceManager.getMessage("security.more.info.title"));
      this.certs = var4;
      this.start = 0;
      this.end = var4 == null ? 0 : var4.length;
      this.initComponents(var2, var3);
   }

   private void initComponents(Pane var1, Throwable var2) {
      VBox var3 = new VBox();
      var3.setId("more-info-dialog");
      if (var1 != null) {
         VBox.setVgrow(var1, Priority.ALWAYS);
         var3.getChildren().add(var1);
      } else if (var2 != null) {
         BorderPane var4 = new BorderPane();
         Label var5 = new Label(ResourceManager.getString("exception.details.label"));
         var4.setLeft(var5);
         var3.getChildren().add(var4);
         StringWriter var6 = new StringWriter();
         PrintWriter var7 = new PrintWriter(var6);
         var2.printStackTrace(var7);
         TextArea var8 = new TextArea(var6.toString());
         var8.setEditable(false);
         var8.setWrapText(true);
         var8.setPrefWidth(480.0);
         var8.setPrefHeight(240.0);
         ScrollPane var9 = new ScrollPane();
         var9.setContent(var8);
         var9.setFitToWidth(true);
         VBox.setVgrow(var9, Priority.ALWAYS);
         var3.getChildren().add(var9);
         if (this.certs != null) {
            var3.getChildren().add(this.getLinkPanel());
         }
      } else {
         Pane var10 = this.getSecurityPanel();
         if (var10.getChildren().size() > 0) {
            VBox.setVgrow(var10, Priority.ALWAYS);
            var3.getChildren().add(var10);
         }

         var3.getChildren().add(this.getIntegrationPanel());
      }

      var3.getChildren().add(this.getBtnPanel());
      this.setContentPane(var3);
   }

   private Pane getSecurityPanel() {
      VBox var1 = new VBox();
      boolean var4 = this.certs == null;
      int var2 = !var4 && this.alerts != null ? 1 : 0;
      int var3 = this.alerts == null ? 0 : this.alerts.length;
      if (var3 > var2) {
         var1.getChildren().add(this.blockPanel("warning16.image", this.alerts, var2, var3));
      }

      var3 = this.securityInfoCount;
      if (var3 > var2) {
         var1.getChildren().add(this.blockPanel("info16.image", this.infos, var2, var3));
      }

      if (this.certs != null) {
         var1.getChildren().add(this.getLinkPanel());
      }

      return var1;
   }

   private Pane getLinkPanel() {
      HBox var1 = new HBox();
      var1.setPadding(new Insets(8.0, 0.0, 0.0, 0.0));
      var1.setAlignment(Pos.TOP_RIGHT);
      String var2 = this.sandboxApp ? "sandbox.security.more.info.details" : "security.more.info.details";
      this.details = new Hyperlink(ResourceManager.getMessage(var2));
      this.details.setMnemonicParsing(true);
      this.details.setOnAction(new EventHandler() {
         public void handle(ActionEvent var1) {
            MoreInfoDialog.this.showCertDetails();
         }
      });
      var1.getChildren().add(this.details);
      return var1;
   }

   private Pane getIntegrationPanel() {
      int var1 = this.securityInfoCount;
      int var2 = this.infos == null ? 0 : this.infos.length;
      return this.blockPanel("info16.image", this.infos, var1, var2);
   }

   private Pane getBtnPanel() {
      HBox var1 = new HBox();
      var1.setId("more-info-dialog-button-panel");
      Button var2 = new Button(ResourceManager.getMessage("common.close_btn"));
      var2.setCancelButton(true);
      var2.setOnAction(new EventHandler() {
         public void handle(ActionEvent var1) {
            MoreInfoDialog.this.dismissAction();
         }
      });
      var2.setDefaultButton(true);
      var1.getChildren().add(var2);
      return var1;
   }

   private Pane blockPanel(String var1, String[] var2, int var3, int var4) {
      VBox var5 = new VBox(5.0);
      if (var2 != null) {
         for(int var6 = var3; var6 < var4; ++var6) {
            HBox var7 = new HBox(12.0);
            var7.setAlignment(Pos.TOP_LEFT);
            ImageView var8 = ResourceManager.getIcon(var1);
            UITextArea var9 = new UITextArea(326.0);
            var9.setWrapText(true);
            var9.setId("more-info-text-block");
            var9.setText(var2[var6]);
            if (var6 > var3) {
               var8.setVisible(false);
            }

            var7.getChildren().add(var8);
            var7.getChildren().add(var9);
            var5.getChildren().add(var7);
            if (var6 < var4 - 1) {
               var5.getChildren().add(new Separator());
            }
         }
      }

      return var5;
   }

   private void showCertDetails() {
      CertificateDialog.showCertificates(this, this.certs, this.start, this.end);
   }

   private void dismissAction() {
      this.hide();
   }
}
