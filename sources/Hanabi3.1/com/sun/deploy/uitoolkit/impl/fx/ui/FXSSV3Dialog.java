package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.net.URL;
import java.security.cert.Certificate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXSSV3Dialog {
   private static final int MAIN_TEXT_WIDTH = 460;
   private static final int RISK_TEXT_WIDTH = 540;
   private static final int MAX_URL_WIDTH = 360;
   private AppInfo ainfo;
   private String masthead;
   private String mainText;
   private String location;
   private String prompt;
   private String multiPrompt;
   private String multiText;
   private String runKey;
   private String updateText;
   private String cancelText;
   private URL updateURL;
   private String locationURL = "";
   private String mainJNLPURL;
   private String documentBaseURL;
   private String locationTooltip = "";
   private String mainJNLPTooltip;
   private String documentBaseTooltip;
   private int userAnswer = 1;
   private FXDialog dialog;
   private Button runButton;
   private Button updateButton;
   private Button cancelButton;
   private CheckBox multiClickCheckBox;

   public static int showSSV3Dialog(Object var0, AppInfo var1, int var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, URL var14) {
      FXSSV3Dialog var15 = new FXSSV3Dialog(var0, getMessage(var3));
      var15.ainfo = var1;
      var15.masthead = getMessage(var4);
      var15.mainText = getMessage(var5);
      var15.location = getMessage(var6);
      var15.prompt = getMessage(var7);
      var15.multiPrompt = getMessage(var8);
      var15.multiText = getMessage(var9);
      var15.runKey = var10;
      var15.updateText = getMessage(var11);
      var15.cancelText = getMessage(var12);
      var15.updateURL = var14;
      var15.initComponents();
      var15.setVisible(true);
      return var15.getAnswer();
   }

   private FXSSV3Dialog(Object var1, String var2) {
      Stage var3 = null;
      if (var1 instanceof Stage) {
         var3 = (Stage)var1;
      }

      this.dialog = new FXDialog(var2, var3, true);
   }

   private void initComponents() {
      try {
         try {
            this.locationURL = this.ainfo.getDisplayFrom();
            this.locationTooltip = this.ainfo.getFrom().toString();
         } catch (Exception var2) {
            this.locationURL = "";
         }

         if (this.ainfo.shouldDisplayMainJNLP()) {
            this.mainJNLPURL = this.ainfo.getDisplayMainJNLP();
            this.mainJNLPTooltip = DialogTemplate.getDisplayMainJNLPTooltip(this.ainfo);
         }

         if (this.ainfo.shouldDisplayDocumentBase()) {
            this.documentBaseURL = this.ainfo.getDisplayDocumentBase();
            this.documentBaseTooltip = this.ainfo.getDocumentBase().toString();
         }

         this.dialog.setResizable(false);
         this.dialog.setIconifiable(false);
         Pane var1 = this.createContentPane();
         var1.getChildren().add(this.createMastHead());
         var1.getChildren().add(this.createMainContent());
         var1.getChildren().add(this.createOkCancelPanel());
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   private Pane createContentPane() {
      VBox var1 = new VBox() {
         protected double computePrefHeight(double var1) {
            double var3 = super.computePrefHeight(var1);
            return var3;
         }
      };
      var1.setId("ssv3-content-panel");
      this.dialog.setContentPane(var1);
      return var1;
   }

   private Node createMastHead() {
      UITextArea var1 = new UITextArea(this.masthead);
      var1.setId("security-masthead-label");
      return var1;
   }

   private Pane createMainContent() {
      Pane var1 = this.createWarningPanel();
      BorderPane var2 = new BorderPane();
      var2.setTop(var1);
      if (this.multiText == null) {
         UITextArea var3 = new UITextArea(460.0);
         var3.setText(this.prompt);
         var3.setId("ssv3-prompt");
         var2.setBottom(this.createWarningMorePrompt(var3));
      }

      return var2;
   }

   private Pane createWarningPanel() {
      BorderPane var1 = new BorderPane();
      var1.setLeft(this.createShieldIcon());
      VBox var2 = this.createLocationPanel();
      var1.setCenter(var2);
      return var1;
   }

   private VBox createLocationPanel() {
      VBox var1 = new VBox();
      Text var2 = new Text(this.mainText);
      var2.setId("ssv3-main-text");
      var2.setWrappingWidth(460.0);
      var1.getChildren().add(var2);
      Label var3 = new Label(this.location);
      var3.setText(this.location);
      var3.setId("ssv3-location-label");
      Label var4 = new Label(this.locationURL);
      var4.setTooltip(new Tooltip(this.locationTooltip));
      var4.setId("ssv3-location-url");
      GridPane var5 = new GridPane();
      var5.setId("ssv3-location-label-url");
      var5.add(var3, 0, 0);
      var5.add(var4, 1, 0);
      int var6 = 1;
      if (this.mainJNLPURL != null) {
         var4 = new Label(this.mainJNLPURL);
         var4.setTooltip(new Tooltip(this.mainJNLPTooltip));
         var4.setMaxWidth(360.0);
         var4.setId("ssv3-location-url");
         var5.add(var4, 1, var6++);
      }

      if (this.documentBaseURL != null) {
         var4 = new Label(this.documentBaseURL);
         var4.setTooltip(new Tooltip(this.documentBaseTooltip));
         var4.setMaxWidth(360.0);
         var4.setId("ssv3-location-url");
         var5.add(var4, 1, var6);
      }

      var1.getChildren().add(var5);
      return var1;
   }

   private Pane createShieldIcon() {
      Label var1 = new Label((String)null, ResourceManager.getIcon("warning48s.image"));
      var1.setId("ssv3-shield");
      VBox var2 = new VBox();
      var2.getChildren().add(var1);
      return var2;
   }

   private Pane createOkCancelPanel() {
      HBox var1 = new HBox();
      var1.getStyleClass().add("security-button-bar");
      this.runButton = new Button(getMessage(this.runKey));
      this.runButton.setMnemonicParsing(true);
      Button var2 = null;
      var1.getChildren().add(this.runButton);
      this.runButton.setOnAction(new EventHandler() {
         public void handle(ActionEvent var1) {
            FXSSV3Dialog.this.runAction();
         }
      });
      this.cancelButton = new Button(this.cancelText);
      this.cancelButton.setOnAction(new EventHandler() {
         public void handle(ActionEvent var1) {
            FXSSV3Dialog.this.userAnswer = 1;
            FXSSV3Dialog.this.closeDialog();
         }
      });
      this.cancelButton.setCancelButton(true);
      if (this.updateText != null) {
         this.updateButton = new Button(this.updateText);
         this.updateButton.setOnAction(new EventHandler() {
            public void handle(ActionEvent var1) {
               FXSSV3Dialog.this.updateAction();
            }
         });
         var1.getChildren().add(this.updateButton);
         var2 = this.updateButton;
      } else {
         var2 = this.cancelButton;
      }

      var1.getChildren().add(this.cancelButton);
      VBox var3 = new VBox();
      this.createMultSelectionBox(var3);
      var3.getChildren().add(var1);
      this.setDefaultButton(var2);
      return var3;
   }

   private void createMultSelectionBox(VBox var1) {
      if (this.multiPrompt != null && this.multiText != null) {
         this.runButton.setDisable(true);
         Label var2 = new Label(this.multiPrompt);
         var2.setId("ssv3-multi-click");
         VBox var3 = this.createWarningMorePrompt(var2);
         VBox var4 = new VBox(8.0);
         var4.getChildren().add(var3);
         var4.getChildren().add(var2);
         HBox var5 = new HBox();
         var5.getChildren().add(var4);
         var1.getChildren().add(var5);
         this.multiClickCheckBox = new CheckBox(this.multiText);
         this.multiClickCheckBox.setId("ssv3-checkbox");
         var1.getChildren().add(this.multiClickCheckBox);
         this.multiClickCheckBox.setOnAction(new EventHandler() {
            public void handle(ActionEvent var1) {
               FXSSV3Dialog.this.runButton.setDisable(!FXSSV3Dialog.this.multiClickCheckBox.isSelected());
               if (FXSSV3Dialog.this.multiClickCheckBox.isSelected()) {
                  FXSSV3Dialog.this.setDefaultButton(FXSSV3Dialog.this.runButton);
               } else if (FXSSV3Dialog.this.updateButton != null) {
                  FXSSV3Dialog.this.setDefaultButton(FXSSV3Dialog.this.updateButton);
               } else if (FXSSV3Dialog.this.cancelButton != null) {
                  FXSSV3Dialog.this.setDefaultButton(FXSSV3Dialog.this.cancelButton);
               }

            }
         });
      }

   }

   private void setDefaultButton(Button var1) {
      this.runButton.setDefaultButton(false);
      if (this.updateButton != null) {
         this.updateButton.setDefaultButton(false);
      }

      this.cancelButton.setDefaultButton(false);
      var1.setDefaultButton(true);
   }

   private void runAction() {
      this.userAnswer = 0;
      this.closeDialog();
   }

   private void updateAction() {
      DialogTemplate.showDocument(this.updateURL.toExternalForm());
   }

   private void closeDialog() {
      this.setVisible(false);
   }

   public void setVisible(boolean var1) {
      if (var1) {
         final FXDialog var2 = this.dialog;
         this.dialog.centerOnScreen();
         Runnable var3 = new Runnable() {
            public void run() {
               var2.showAndWait();
            }
         };
         var3.run();
      } else {
         this.dialog.hide();
      }

   }

   private int getAnswer() {
      return this.userAnswer;
   }

   private VBox createWarningMorePrompt(Node var1) {
      VBox var2 = new VBox();
      var2.getChildren().add(this.getSecurityWarning());
      var2.getChildren().add(this.getMoreInfoButton());
      var2.getChildren().add(var1);
      return var2;
   }

   private Hyperlink getMoreInfoButton() {
      Hyperlink var1 = null;
      var1 = new Hyperlink(getMessage("dialog.template.more.info2"));
      var1.setMnemonicParsing(true);
      var1.setId("bottom-more-info-link");
      var1.setOnAction(new EventHandler() {
         public void handle(ActionEvent var1) {
            FXSSV3Dialog.this.showMoreInfo();
         }
      });
      return var1;
   }

   private static String getMessage(String var0) {
      return var0 == null ? null : ResourceManager.getMessage(var0);
   }

   private BorderPane getSecurityWarning() {
      BorderPane var1 = new BorderPane();
      Text var2 = new Text(getMessage("dialog.unsigned.security.risk.warning"));
      var2.setFill(Color.web("0xCC0000"));
      var2.setFont(Font.font("System", FontWeight.BOLD, 15.0));
      var2.setWrappingWidth(540.0);
      var1.setLeft(var2);
      var1.setPadding(new Insets(8.0, 0.0, 0.0, 0.0));
      return var1;
   }

   private void showMoreInfo() {
      StringBuilder var1 = new StringBuilder();
      if (isLocalApp(this.ainfo)) {
         var1.append(getMessage("sandbox.security.info.local.description"));
      } else {
         var1.append(getMessage("sandbox.security.info.description"));
      }

      var1.append("\n\n");
      if (this.updateText != null) {
         var1.append(getMessage("deployment.dialog.ssv3.more.insecure"));
         var1.append("\n\n");
      }

      if (isLocalApp(this.ainfo)) {
         var1.append(getMessage("deployment.dialog.ssv3.more.local"));
         var1.append("\n\n");
      }

      if (this.multiText != null) {
         var1.append(getMessage("deployment.dialog.ssv3.more.multi"));
         var1.append("\n\n");
      }

      var1.append(getMessage("deployment.dialog.ssv3.more.general"));
      MoreInfoDialog var2 = new MoreInfoDialog(this.dialog, new String[]{var1.toString()}, (String[])null, 0, (Certificate[])null, 0, 0, false);
      var2.showAndWait();
   }

   private static boolean isLocalApp(AppInfo var0) {
      URL var1 = var0.getFrom();
      return var1 != null && var1.getProtocol().equals("file");
   }
}
