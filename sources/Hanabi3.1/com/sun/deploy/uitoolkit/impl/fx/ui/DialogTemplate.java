package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.trace.Trace;
import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.javafx.applet.HostServicesImpl;
import com.sun.javafx.application.HostServicesDelegate;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogTemplate {
   int theAnswer = -1;
   final Object responseLock = new Object();
   private static final int RISK_LABEL_WIDTH = 52;
   private static final int RISK_TEXT_WIDTH = 490;
   private EventHandler okHandler = new EventHandler() {
      public void handle(ActionEvent var1) {
         DialogTemplate.this.userAnswer = 0;
         if (DialogTemplate.this.always != null && DialogTemplate.this.always.isSelected()) {
            DialogTemplate.this.userAnswer = 2;
         }

         if (!DialogTemplate.this.stayAliveOnOk) {
            if (DialogTemplate.this.password != null) {
               DialogTemplate.this.pwd = DialogTemplate.this.password.getText().toCharArray();
            }

            if (DialogTemplate.this.pwdName != null) {
               DialogTemplate.this.userName = DialogTemplate.this.pwdName.getText();
            }

            if (DialogTemplate.this.pwdDomain != null) {
               DialogTemplate.this.domain = DialogTemplate.this.pwdDomain.getText();
            }

            if (DialogTemplate.this.scrollList != null) {
               DialogTemplate.this.userAnswer = DialogTemplate.this.scrollList.getSelectionModel().getSelectedIndex();
            }

            DialogTemplate.this.setVisible(false);
         }
      }
   };
   private EventHandler cancelHandler = new EventHandler() {
      public void handle(ActionEvent var1) {
         if (DialogTemplate.this.throwable == null && DialogTemplate.this.detailPanel == null) {
            DialogTemplate.this.userAnswer = 1;
            if (DialogTemplate.this.scrollList != null) {
               DialogTemplate.this.userAnswer = -1;
            }

            DialogTemplate.this.setVisible(false);
         } else {
            DialogTemplate.this.showMoreInfo();
         }
      }
   };
   EventHandler acceptRiskHandler = new EventHandler() {
      public void handle(ActionEvent var1) {
         boolean var2 = DialogTemplate.this.acceptRisk.isSelected();
         DialogTemplate.this.okBtn.setDisable(!var2);
         DialogTemplate.this.okBtn.setDefaultButton(var2);
         DialogTemplate.this.cancelBtn.setDefaultButton(!var2);
         if (DialogTemplate.this.always != null) {
            DialogTemplate.this.always.setSelected(false);
            DialogTemplate.this.always.setDisable(!var2);
         }

      }
   };
   EventHandler expandHandler = new EventHandler() {
      public void handle(ActionEvent var1) {
         if (var1.getSource() == DialogTemplate.this.expandBtn) {
            DialogTemplate.this.expandPanel.setTop(DialogTemplate.this.collapseBtn);
            DialogTemplate.this.expandPanel.setBottom(DialogTemplate.this.always);
            if (DialogTemplate.this.acceptRisk != null) {
               DialogTemplate.this.always.setDisable(!DialogTemplate.this.acceptRisk.isSelected());
            }
         } else if (var1.getSource() == DialogTemplate.this.collapseBtn) {
            DialogTemplate.this.expandPanel.setTop(DialogTemplate.this.expandBtn);
            DialogTemplate.this.expandPanel.setBottom((Node)null);
         }

         DialogTemplate.this.dialog.sizeToScene();
      }
   };
   EventHandler moreInfoHandler = new EventHandler() {
      public void handle(Event var1) {
         DialogTemplate.this.showMoreInfo();
      }
   };
   EventHandler closeHandler = new EventHandler() {
      public void handle(Event var1) {
         DialogTemplate.this.dialog.hide();
      }
   };
   private FXDialog dialog = null;
   private VBox contentPane = null;
   private AppInfo ainfo = null;
   private String topText = null;
   private boolean useErrorIcon = false;
   private boolean useWarningIcon = false;
   private boolean useInfoIcon = false;
   private boolean useBlockedIcon = false;
   private boolean useMixcodeIcon = false;
   private Label progressStatusLabel = null;
   private BorderPane topPanel;
   private Pane centerPanel;
   private BorderPane expandPanel;
   private ImageView topIcon;
   private ImageView securityIcon;
   private Label nameInfo;
   private Label publisherInfo;
   private Label urlInfo;
   private Label mainJNLPInfo;
   private Label documentBaseInfo;
   private Button okBtn;
   private Button cancelBtn;
   private Button expandBtn;
   private Button collapseBtn;
   private CheckBox always;
   private CheckBox acceptRisk;
   private Label mixedCodeLabel;
   private UITextArea masthead1 = null;
   private UITextArea masthead2 = null;
   private static final int ICON_SIZE = 48;
   private int userAnswer = -1;
   static final int DIALOG_WIDTH = 540;
   private final int MAX_LARGE_SCROLL_WIDTH = 600;
   private final String SECURITY_ALERT_HIGH = "security.alert.high.image";
   private final String SECURITY_ALERT_LOW = "security.alert.low.image";
   private static int MAIN_TEXT_WIDTH = 400;
   private final String OK_ACTION = "OK";
   private final int MAX_BUTTONS = 2;
   private int start;
   private int end;
   private Certificate[] certs;
   private String[] alertStrs;
   private String[] infoStrs;
   private int securityInfoCount;
   private Color originalColor;
   private Cursor originalCursor = null;
   protected ProgressBar progressBar = null;
   private boolean stayAliveOnOk = false;
   private String contentString = null;
   private String reason;
   private String cacheUpgradeContentString = null;
   private String contentLabel = null;
   private String alwaysString = null;
   private String mixedCodeString = null;
   private boolean contentScroll = false;
   private boolean includeMasthead = true;
   private boolean includeAppInfo = true;
   private boolean largeScroll = false;
   private Throwable throwable = null;
   private Pane detailPanel = null;
   private char[] pwd = new char[0];
   private String userName;
   private String domain;
   private TextField pwdName;
   private TextField pwdDomain;
   private PasswordField password;
   private ListView scrollList;
   private boolean showDetails = false;
   TreeMap clientAuthCertsMap;
   private boolean majorWarning = false;
   private String okBtnStr;
   private String cancelBtnStr;
   private boolean sandboxApp = false;
   private boolean checkAlways = false;
   private boolean showAlways = false;
   private boolean selfSigned = false;
   private boolean isBlockedDialog;

   DialogTemplate(AppInfo var1, Stage var2, String var3, String var4) {
      this.dialog = new FXDialog(var3, var2, false);
      this.contentPane = new VBox() {
         protected double computePrefHeight(double var1) {
            double var3 = super.computePrefHeight(var1);
            return var3;
         }
      };
      this.dialog.setContentPane(this.contentPane);
      this.ainfo = var1;
      this.topText = var4;
   }

   void setNewSecurityContent(boolean var1, boolean var2, String var3, String var4, String[] var5, String[] var6, int var7, boolean var8, Certificate[] var9, int var10, int var11, boolean var12, boolean var13, boolean var14) {
      this.certs = var9;
      this.start = var10;
      this.end = var11;
      this.alertStrs = var5;
      this.infoStrs = var6;
      this.securityInfoCount = var7;
      this.majorWarning = var12;
      this.okBtnStr = var3;
      this.cancelBtnStr = var4;
      this.sandboxApp = var13;
      this.showAlways = var1;
      this.checkAlways = var2;
      this.selfSigned = var14;
      if (var5 != null && var5.length > 0) {
         this.useWarningIcon = true;
      }

      try {
         this.contentPane.setId("security-content-panel");
         this.dialog.initModality(Modality.APPLICATION_MODAL);
         this.contentPane.getChildren().add(this.createSecurityTopPanel());
         this.contentPane.getChildren().add(this.createSecurityCenterPanel());
         if (!var14) {
            if (var12) {
               if (var1) {
                  this.contentPane.getChildren().add(this.createSecurityBottomExpandPanel());
               }
            } else {
               this.contentPane.getChildren().add(this.createSecurityBottomMoreInfoPanel());
            }
         }

         this.dialog.setResizable(false);
         this.dialog.setIconifiable(false);
         if (this.alertStrs == null) {
            this.dialog.hideWindowTitle();
         }
      } catch (Throwable var16) {
         Trace.ignored(var16);
      }

   }

   private Pane createSecurityTopPanel() {
      BorderPane var1 = new BorderPane();
      var1.setTop(this.createSecurityTopMastheadPanel());
      var1.setBottom(this.createSecurityTopIconLabelsPanel());
      return var1;
   }

   private Pane createSecurityTopMastheadPanel() {
      BorderPane var1 = new BorderPane();
      this.masthead1 = new UITextArea((double)MAIN_TEXT_WIDTH);
      this.masthead1.setId("security-masthead-label");
      this.masthead1.setText(this.topText.trim());
      var1.setLeft(this.masthead1);
      if (this.alertStrs == null) {
         Button var2 = FXDialog.createCloseButton();
         var2.setOnAction(this.closeHandler);
         var1.setRight(var2);
      }

      return var1;
   }

   private Pane createSecurityTopIconLabelsPanel() {
      BorderPane var1 = new BorderPane();
      if (this.alertStrs != null) {
         this.topIcon = ResourceManager.getIcon("warning48s.image");
      } else {
         this.topIcon = ResourceManager.getIcon("java48s.image");
      }

      Label var2 = new Label((String)null, this.topIcon);
      var2.setId("security-top-icon-label");
      var1.setLeft(var2);
      GridPane var3 = new GridPane();
      var3.setId("security-top-labels-grid");
      String var4 = ResourceManager.getMessage("dialog.template.name");
      Label var5 = new Label(var4);
      var5.setId("security-name-label");
      String var6 = ResourceManager.getMessage("dialog.template.publisher");
      Label var7 = new Label(var6);
      var7.setId("security-publisher-label");
      String var8 = ResourceManager.getMessage("deployment.ssv.location");
      Label var9 = new Label(var8);
      var9.setId("security-from-label");
      this.nameInfo = new Label();
      this.nameInfo.setId("security-name-value");
      this.publisherInfo = new Label();
      this.urlInfo = new Label();
      if (this.ainfo.getTitle() != null) {
         GridPane.setConstraints(var5, 0, 0);
         GridPane.setHalignment(var5, HPos.LEFT);
         var3.getChildren().add(var5);
         GridPane.setConstraints(this.nameInfo, 1, 0);
         var3.getChildren().add(this.nameInfo);
      }

      if (this.ainfo.getVendor() != null) {
         GridPane.setConstraints(var7, 0, 1);
         GridPane.setHalignment(var7, HPos.LEFT);
         var3.getChildren().add(var7);
         GridPane.setConstraints(this.publisherInfo, 1, 1);
         var3.getChildren().add(this.publisherInfo);
      }

      if (this.ainfo.getFrom() != null) {
         GridPane.setConstraints(var9, 0, 2);
         GridPane.setHalignment(var9, HPos.LEFT);
         var3.getChildren().add(var9);
         GridPane.setConstraints(this.urlInfo, 1, 2);
         var3.getChildren().add(this.urlInfo);
         int var10 = 3;
         if (this.ainfo.shouldDisplayMainJNLP()) {
            this.mainJNLPInfo = new Label();
            this.mainJNLPInfo.setId("dialog-from-value");
            GridPane.setConstraints(this.mainJNLPInfo, 1, var10++);
            var3.getChildren().add(this.mainJNLPInfo);
         }

         if (this.ainfo.shouldDisplayDocumentBase()) {
            this.documentBaseInfo = new Label();
            this.documentBaseInfo.setId("dialog-from-value");
            GridPane.setConstraints(this.documentBaseInfo, 1, var10);
            var3.getChildren().add(this.documentBaseInfo);
         }
      }

      this.setInfo(this.ainfo);
      var1.setCenter(var3);
      return var1;
   }

   private Pane createSecurityCenterPanel() {
      BorderPane var1 = new BorderPane();
      if (this.majorWarning) {
         BorderPane var2 = new BorderPane();
         String var3 = null;
         if (this.selfSigned) {
            var3 = ResourceManager.getMessage("dialog.selfsigned.security.risk.warning");
         } else {
            var3 = ResourceManager.getMessage("dialog.security.risk.warning");
         }

         String var4 = ResourceManager.getMessage("security.dialog.notverified.subject");
         Text var5 = new Text(var3.replaceAll(var4, var4.toUpperCase()));
         var5.setWrappingWidth((double)(MAIN_TEXT_WIDTH + 120));
         var5.setFill(Color.web("0xCC0000"));
         var5.setFont(Font.font("System", FontWeight.BOLD, 15.0));
         var2.setLeft(var5);
         var2.setPadding(new Insets(8.0, 0.0, 0.0, 0.0));
         var1.setTop(var2);
         var1.setCenter(this.createSecurityRiskPanel());
         var1.setBottom(this.createSecurityAcceptRiskPanel());
      } else {
         var1.setTop(this.createSecurityRiskPanel());
         if (this.showAlways) {
            var1.setBottom(this.createSecurityAlwaysPanel());
         }
      }

      return var1;
   }

   private Pane createSecurityRiskPanel() {
      BorderPane var1 = new BorderPane();
      var1.setId("security-risk-panel");
      String var2;
      if (this.majorWarning) {
         var2 = this.alertStrs[0];
         int var3 = var2.indexOf(" ");
         String var4 = var2.substring(0, var3);
         String var5 = var2.substring(var3 + 1);
         UITextArea var6 = new UITextArea(52.0);
         var6.setText(var4);
         var6.setId("security-risk-label");
         var1.setLeft(var6);
         BorderPane var7 = new BorderPane();
         UITextArea var8 = new UITextArea(490.0);
         var8.setId("security-risk-value");
         var8.setText(var5);
         var7.setTop(var8);
         var7.setBottom(this.createMoreInfoHyperlink());
         var1.setRight(var7);
      } else {
         var2 = ResourceManager.getMessage(this.sandboxApp ? "sandbox.security.dialog.valid.signed.risk" : "security.dialog.valid.signed.risk");
         UITextArea var9 = new UITextArea(450.0);
         var9.setId("security-risk-value");
         var9.setText(var2);
         var1.setLeft(var9);
      }

      return var1;
   }

   private Hyperlink createMoreInfoHyperlink() {
      String var1 = ResourceManager.getMessage("dialog.template.more.info2");
      Hyperlink var2 = new Hyperlink(var1);
      var2.setMnemonicParsing(true);
      var2.setId("security-more-info-link");
      var2.setOnAction(this.moreInfoHandler);
      return var2;
   }

   private Pane createSecurityAcceptRiskPanel() {
      BorderPane var1 = new BorderPane();
      String var2 = ResourceManager.getMessage("security.dialog.accept.title");
      String var3 = ResourceManager.getMessage("security.dialog.accept.text");
      UITextArea var4 = new UITextArea(542.0);
      var4.setId("security-risk-value");
      var4.setText(var2);
      var1.setTop(var4);
      BorderPane var5 = new BorderPane();
      var5.setId("security-accept-risk-panel");
      this.acceptRisk = new CheckBox(var3);
      this.acceptRisk.setSelected(false);
      this.acceptRisk.setOnAction(this.acceptRiskHandler);
      var5.setLeft(this.acceptRisk);
      var5.setRight(this.createSecurityOkCancelButtons());
      var1.setBottom(var5);
      return var1;
   }

   private Pane createSecurityOkCancelButtons() {
      HBox var1 = new HBox();
      var1.getStyleClass().add("security-button-bar");
      this.okBtn = new Button(ResourceManager.getMessage(this.okBtnStr));
      this.okBtn.setMnemonicParsing(true);
      this.okBtn.setOnAction(this.okHandler);
      var1.getChildren().add(this.okBtn);
      this.cancelBtn = new Button(this.cancelBtnStr);
      this.cancelBtn.setOnAction(this.cancelHandler);
      this.cancelBtn.setCancelButton(true);
      var1.getChildren().add(this.cancelBtn);
      if (this.majorWarning) {
         this.okBtn.setDisable(true);
         this.cancelBtn.setDefaultButton(true);
      } else {
         this.okBtn.setDefaultButton(true);
      }

      return var1;
   }

   private Pane createSecurityAlwaysPanel() {
      BorderPane var1 = new BorderPane();
      var1.setLeft(this.createSecurityAlwaysCheckbox());
      return var1;
   }

   private CheckBox createSecurityAlwaysCheckbox() {
      this.alwaysString = ResourceManager.getMessage(this.sandboxApp ? "sandbox.security.dialog.always" : "security.dialog.always");
      this.always = new CheckBox(this.alwaysString);
      if (this.majorWarning) {
         this.always.setId("security-always-trust-checkbox");
      }

      this.always.setSelected(this.sandboxApp && this.checkAlways);
      this.always.setVisible(true);
      return this.always;
   }

   private Pane createSecurityBottomExpandPanel() {
      this.expandPanel = new BorderPane();
      this.expandPanel.setId("security-bottom-panel");
      this.always = this.createSecurityAlwaysCheckbox();
      ImageView var1 = ResourceManager.getIcon("toggle_down2.image");
      String var2 = ResourceManager.getMessage("security.dialog.show.options");
      this.expandBtn = new Button(var2, var1);
      this.expandBtn.setId("security-expand-button");
      this.expandBtn.setOnAction(this.expandHandler);
      ImageView var3 = ResourceManager.getIcon("toggle_up2.image");
      String var4 = ResourceManager.getMessage("security.dialog.hide.options");
      this.collapseBtn = new Button(var4, var3);
      this.collapseBtn.setId("security-expand-button");
      this.collapseBtn.setOnAction(this.expandHandler);
      this.expandPanel.setTop(this.expandBtn);
      return this.expandPanel;
   }

   private Pane createSecurityBottomMoreInfoPanel() {
      BorderPane var1 = new BorderPane();
      var1.setId("security-bottom-panel");
      HBox var2 = new HBox();
      if (this.alertStrs != null) {
         this.securityIcon = ResourceManager.getIcon("yellowShield.image");
      } else {
         this.securityIcon = ResourceManager.getIcon("blueShield.image");
      }

      var2.getChildren().add(this.securityIcon);
      BorderPane var3 = new BorderPane();
      var3.setId("security-more-info-panel");
      var3.setCenter(this.createMoreInfoHyperlink());
      var2.getChildren().add(var3);
      var1.setRight(this.createSecurityOkCancelButtons());
      var1.setLeft(var2);
      return var1;
   }

   void setSecurityContent(boolean var1, boolean var2, String var3, String var4, String[] var5, String[] var6, int var7, boolean var8, Certificate[] var9, int var10, int var11, boolean var12) {
      this.certs = var9;
      this.start = var10;
      this.end = var11;
      this.alertStrs = var5;
      this.infoStrs = var6;
      this.securityInfoCount = var7;
      this.majorWarning = var12;
      if (var5 != null && var5.length > 0) {
         this.useWarningIcon = true;
      }

      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.dialog.initModality(Modality.APPLICATION_MODAL);
         if (var1) {
            this.alwaysString = ResourceManager.getMessage("security.dialog.always");
         }

         this.contentPane.getChildren().add(this.createCenterPanel(var2, var3, var4, -1));
         this.contentPane.getChildren().add(this.createBottomPanel(var8));
         this.dialog.setResizable(false);
         this.dialog.setIconifiable(false);
      } catch (Throwable var14) {
         var14.printStackTrace();
      }

   }

   void setSSVContent(String var1, String var2, URL var3, String var4, String var5, String var6, String var7, String var8) {
      try {
         BorderPane var9 = new BorderPane();
         var9.setId("ssv-content-panel");
         this.dialog.initModality(Modality.APPLICATION_MODAL);
         this.contentPane.getChildren().add(var9);
         var9.setTop(this.createSSVTopPanel(this.topText, this.ainfo.getTitle(), this.ainfo.getDisplayFrom()));
         BorderPane var10 = this.createSSVRiskPanel(var1, var2, var3);
         final SSVChoicePanel var11 = new SSVChoicePanel(var4, var5, var6);
         BorderPane var12 = new BorderPane();
         var12.setTop(var10);
         var12.setBottom(var11);
         var9.setCenter(var12);
         FlowPane var13 = new FlowPane(6.0, 0.0);
         var13.getStyleClass().add("button-bar");
         this.okBtn = new Button(var7);
         this.okBtn.setOnAction(new EventHandler() {
            public void handle(ActionEvent var1) {
               if (var11.getSelection() == 0) {
                  DialogTemplate.this.setUserAnswer(2);
               } else {
                  DialogTemplate.this.setUserAnswer(0);
               }

               DialogTemplate.this.setVisible(false);
            }
         });
         var13.getChildren().add(this.okBtn);
         this.cancelBtn = new Button(var8);
         this.cancelBtn.setOnAction(new EventHandler() {
            public void handle(ActionEvent var1) {
               DialogTemplate.this.cancelAction();
            }
         });
         this.cancelBtn.setCancelButton(true);
         var13.getChildren().add(this.cancelBtn);
         this.okBtn.setDefaultButton(true);
         var9.setBottom(var13);
         this.dialog.setResizable(false);
         this.dialog.setIconifiable(false);
      } catch (Throwable var14) {
      }

   }

   void setSimpleContent(String var1, boolean var2, String var3, String var4, String var5, boolean var6, boolean var7) {
      this.contentString = var1;
      this.contentScroll = var2;
      this.includeMasthead = var6;
      this.includeAppInfo = var6;
      this.largeScroll = !var6;
      this.useWarningIcon = var7;
      if (var3 != null) {
         String[] var8 = new String[]{var3};
         if (var7) {
            this.alertStrs = var8;
         } else {
            this.infoStrs = var8;
         }
      }

      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.contentPane.getChildren().add(this.createCenterPanel(false, var4, var5, -1));
         this.contentPane.getChildren().add(this.createBottomPanel(false));
         this.dialog.setResizable(var2);
      } catch (Throwable var9) {
      }

   }

   void setMixedCodeContent(String var1, boolean var2, String var3, String var4, String var5, String var6, boolean var7, boolean var8) {
      this.contentString = var1;
      this.contentScroll = var2;
      this.includeMasthead = var7;
      this.includeAppInfo = var7;
      this.largeScroll = !var7;
      this.useMixcodeIcon = true;
      this.alertStrs = new String[1];
      String[] var9 = new String[]{var4};
      this.alertStrs = var9;
      this.infoStrs = new String[3];
      String var10 = ResourceManager.getString("security.dialog.mixcode.info1");
      String var11 = ResourceManager.getString("security.dialog.mixcode.info2");
      String var12 = ResourceManager.getString("security.dialog.mixcode.info3");
      String[] var13 = new String[]{var10, var11, var12};
      this.infoStrs = var13;

      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.mixedCodeString = var3;
         this.contentPane.getChildren().add(this.createCenterPanel(false, var5, var6, -1));
         this.contentPane.getChildren().add(this.createBottomPanel(false));
         this.okBtn.requestFocus();
         this.dialog.setResizable(var2);
      } catch (Throwable var15) {
      }

   }

   void setListContent(String var1, ListView var2, boolean var3, String var4, String var5, TreeMap var6) {
      this.useWarningIcon = true;
      this.includeAppInfo = false;
      this.clientAuthCertsMap = var6;
      this.contentLabel = var1;
      this.contentScroll = true;
      this.scrollList = var2;
      this.showDetails = var3;

      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.contentPane.getChildren().add(this.createCenterPanel(false, var4, var5, -1));
         this.contentPane.getChildren().add(this.createBottomPanel(false));
      } catch (Throwable var8) {
      }

   }

   void setApiContent(String var1, String var2, String var3, boolean var4, String var5, String var6) {
      this.contentString = var1;
      this.contentLabel = var2;
      this.contentScroll = var1 != null;
      this.alwaysString = var3;
      if (var2 == null && var1 != null) {
         this.infoStrs = new String[1];
         this.infoStrs[0] = var1;
         this.contentString = null;
      }

      this.includeMasthead = true;
      this.includeAppInfo = this.contentString == null;
      this.largeScroll = false;

      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.contentPane.getChildren().add(this.createCenterPanel(false, var5, var6, -1));
         this.contentPane.getChildren().add(this.createBottomPanel(false));
         this.dialog.setResizable(this.contentScroll);
      } catch (Throwable var8) {
      }

   }

   void setErrorContent(String var1, String var2, String var3, Throwable var4, Object var5, Certificate[] var6, boolean var7) {
      Pane var8 = (Pane)var5;
      this.contentString = var1;
      this.throwable = var4;
      this.detailPanel = var8;
      this.certs = var6;
      if (var7) {
         this.includeAppInfo = false;
      }

      this.useErrorIcon = true;

      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.contentPane.getChildren().add(this.createCenterPanel(false, var2, var3, -1));
         Pane var9 = this.createBottomPanel(false);
         if (var9.getChildren().size() > 0) {
            this.contentPane.getChildren().add(var9);
         }

         this.dialog.setResizable(false);
      } catch (Throwable var10) {
      }

   }

   void setMultiButtonErrorContent(String var1, String var2, String var3, String var4) {
      this.useErrorIcon = true;

      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         BorderPane var5 = new BorderPane();
         var5.setId("error-panel");
         var5.setTop(this.createInfoPanel(var1));
         var5.setBottom(this.createThreeButtonsPanel(var2, var3, var4, false));
         this.contentPane.getChildren().add(var5);
         this.dialog.setResizable(false);
      } catch (Throwable var6) {
      }

   }

   void setInfoContent(String var1, String var2) {
      this.useInfoIcon = true;
      this.contentString = var1;

      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.contentPane.getChildren().add(this.createCenterPanel(false, var2, (String)null, -1));
         this.dialog.setResizable(false);
      } catch (Throwable var4) {
      }

   }

   void setPasswordContent(String var1, boolean var2, boolean var3, String var4, String var5, boolean var6, char[] var7, String var8, String var9) {
      try {
         this.contentPane.getChildren().add(this.createPasswordPanel(var1, var2, var3, var4, var5, var6, var7, var8, var9));
         this.dialog.initModality(Modality.APPLICATION_MODAL);
         this.dialog.setResizable(false);
         this.dialog.setIconifiable(false);
      } catch (Throwable var11) {
      }

   }

   void setUpdateCheckContent(String var1, String var2, String var3, String var4) {
      try {
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.contentPane.getChildren().add(this.createInfoPanel(var1));
         this.contentPane.getChildren().add(this.createThreeButtonsPanel(var2, var3, var4, true));
         this.dialog.setResizable(false);
      } catch (Throwable var6) {
      }

   }

   void setProgressContent(String var1, String var2, String var3, boolean var4, int var5) {
      try {
         this.cacheUpgradeContentString = var3;
         this.contentPane.getChildren().add(this.createTopPanel(false));
         this.contentPane.getChildren().add(this.createCenterPanel(false, var1, var2, var5));
         if (this.cacheUpgradeContentString == null) {
            this.contentPane.getChildren().add(this.createBottomPanel(false));
         }

         this.dialog.setResizable(false);
      } catch (Throwable var7) {
      }

   }

   private Pane createInfoPanel(String var1) {
      StackPane var2 = new StackPane();
      var2.setId("info-panel");
      UITextArea var3 = new UITextArea(508.0);
      var3.setId("info-panel-text");
      var3.setText(var1);
      var2.getChildren().add(var3);
      return var2;
   }

   private Pane createThreeButtonsPanel(String var1, String var2, String var3, boolean var4) {
      FlowPane var5 = new FlowPane(6.0, 0.0);
      var5.getStyleClass().add("button-bar");
      Button var6 = new Button(ResourceManager.getMessage(var1));
      var6.setOnAction(new EventHandler() {
         public void handle(ActionEvent var1) {
            DialogTemplate.this.setUserAnswer(0);
            DialogTemplate.this.setVisible(false);
         }
      });
      var5.getChildren().add(var6);
      Button var7 = new Button(ResourceManager.getMessage(var2));
      var7.setOnAction(new EventHandler() {
         public void handle(ActionEvent var1) {
            DialogTemplate.this.setVisible(false);
            DialogTemplate.this.setUserAnswer(1);
         }
      });
      var5.getChildren().add(var7);
      Button var8 = null;
      if (var3 != null) {
         var8 = new Button(ResourceManager.getMessage(var3));
         var8.setOnAction(new EventHandler() {
            public void handle(ActionEvent var1) {
               DialogTemplate.this.setVisible(false);
               DialogTemplate.this.setUserAnswer(3);
            }
         });
         var5.getChildren().add(var8);
      }

      if (var4) {
         var8.setTooltip(new Tooltip(ResourceManager.getMessage("autoupdatecheck.masthead")));
      }

      if (var8 != null) {
         resizeButtons(var6, var7, var8);
      } else {
         resizeButtons(var6, var7);
      }

      return var5;
   }

   private Pane createTopPanel(boolean var1) {
      this.topPanel = new BorderPane();
      this.topPanel.setId("top-panel");
      if (this.includeMasthead) {
         this.masthead1 = new UITextArea((double)MAIN_TEXT_WIDTH);
         this.masthead1.setId("masthead-label-1");
         String var2 = this.topText;
         String var3 = null;
         String[] var4 = new String[]{"security.dialog.caption.run.question", "security.dialog.caption.continue.question"};
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            String var8 = ResourceManager.getMessage(var7);
            if (var8 != null && var2.endsWith(var8)) {
               var3 = var2.substring(0, var2.indexOf(var8)).trim();
               var2 = var8;
               break;
            }
         }

         VBox var9 = new VBox();
         var9.setAlignment(Pos.CENTER_LEFT);
         this.masthead1.setText(var2);
         this.masthead1.setAlignment(Pos.CENTER_LEFT);
         var9.getChildren().add(this.masthead1);
         if (var3 != null) {
            this.masthead2 = new UITextArea((double)MAIN_TEXT_WIDTH);
            this.masthead2.setId("masthead-label-2");
            this.masthead2.setText(var3);
            this.masthead2.setAlignment(Pos.CENTER_LEFT);
            var9.getChildren().add(this.masthead2);
         }

         this.topPanel.setLeft(var9);
         BorderPane var10000 = this.topPanel;
         BorderPane.setAlignment(var9, Pos.CENTER_LEFT);
         if (var1) {
            ImageView var10 = ResourceManager.getIcon("progress.background.image");
         } else {
            this.topIcon = ResourceManager.getIcon("java48.image");
            if (this.useErrorIcon) {
               this.topIcon = ResourceManager.getIcon("error48.image");
            }

            if (this.useInfoIcon) {
               this.topIcon = ResourceManager.getIcon("info48.image");
            }

            if (this.useMixcodeIcon) {
               this.topIcon = ResourceManager.getIcon("mixcode.image");
            }

            if (this.useBlockedIcon) {
               this.topIcon = ResourceManager.getIcon("cert_error48.image");
            }

            if (this.useWarningIcon) {
               if (this.majorWarning) {
                  this.topIcon = ResourceManager.getIcon("major-warning48.image");
               } else {
                  this.topIcon = ResourceManager.getIcon("warning48.image");
               }
            } else if (this.ainfo.getIconRef() != null) {
               this.topIcon = ResourceManager.getIcon(this.ainfo.getIconRef());
            }

            this.topPanel.setRight(this.topIcon);
         }
      }

      return this.topPanel;
   }

   private Pane createCenterPanel(boolean var1, String var2, String var3, int var4) {
      this.centerPanel = new VBox();
      this.centerPanel.setId("center-panel");
      GridPane var5 = new GridPane();
      var5.setId("center-panel-grid");
      Label var6 = new Label(ResourceManager.getMessage("dialog.template.name"));
      var6.setId("dialog-name-label");
      Label var7 = new Label(ResourceManager.getMessage("dialog.template.publisher"));
      var7.setId("dialog-publisher-label");
      Label var8 = new Label(ResourceManager.getMessage("deployment.ssv.location"));
      var8.setId("dialog-from-label");
      Label var9 = new Label(ResourceManager.getMessage("deployment.ssv.reason"));
      var9.setId("dialog-reason-label");
      this.nameInfo = new Label();
      this.nameInfo.setId("dialog-name-value");
      this.publisherInfo = new Label();
      this.publisherInfo.setId("dialog-publisher-value");
      this.urlInfo = new Label();
      this.urlInfo.setId("dialog-from-value");
      Label var10 = new Label();
      var10.setWrapText(true);
      var10.setId("dialog-reason-value");
      int var11 = 0;
      if (this.ainfo.getTitle() != null) {
         GridPane.setConstraints(var6, 0, var11);
         GridPane.setHalignment(var6, HPos.RIGHT);
         var5.getChildren().add(var6);
         GridPane.setConstraints(this.nameInfo, 1, var11++);
         var5.getChildren().add(this.nameInfo);
      }

      if (this.ainfo.getVendor() != null) {
         GridPane.setConstraints(var7, 0, var11);
         GridPane.setHalignment(var7, HPos.RIGHT);
         var5.getChildren().add(var7);
         GridPane.setConstraints(this.publisherInfo, 1, var11++);
         var5.getChildren().add(this.publisherInfo);
      }

      if (this.ainfo.getFrom() != null) {
         GridPane.setConstraints(var8, 0, var11);
         GridPane.setHalignment(var8, HPos.RIGHT);
         var5.getChildren().add(var8);
         GridPane.setConstraints(this.urlInfo, 1, var11++);
         var5.getChildren().add(this.urlInfo);
         if (this.ainfo.shouldDisplayMainJNLP()) {
            this.mainJNLPInfo = new Label();
            this.mainJNLPInfo.setId("dialog-from-value");
            GridPane.setConstraints(this.mainJNLPInfo, 1, var11++);
            var5.getChildren().add(this.mainJNLPInfo);
         }

         if (this.ainfo.shouldDisplayDocumentBase()) {
            this.documentBaseInfo = new Label();
            this.documentBaseInfo.setId("dialog-from-value");
            GridPane.setConstraints(this.documentBaseInfo, 1, var11++);
            var5.getChildren().add(this.documentBaseInfo);
         }
      }

      if (this.reason != null) {
         GridPane.setConstraints(var9, 0, var11);
         GridPane.setHalignment(var9, HPos.RIGHT);
         var5.getChildren().add(var9);
         var10.setText(this.reason);
         GridPane.setConstraints(var10, 1, var11++);
         var5.getChildren().add(var10);
      }

      this.setInfo(this.ainfo);
      FlowPane var12 = new FlowPane();
      var12.setId("center-checkbox-panel");
      BorderPane var13 = new BorderPane();
      var13.setId("mixed-code-panel");
      BorderPane var14 = new BorderPane();
      var14.setId("center-content-panel");
      VBox.setVgrow(var14, Priority.ALWAYS);
      if (this.alwaysString != null) {
         String var15 = "security.dialog.always";
         this.always = new CheckBox(this.alwaysString);
         this.always.setSelected(var1);
         var12.getChildren().add(this.always);
      }

      Hyperlink var16;
      if (this.mixedCodeString != null) {
         this.mixedCodeLabel = new Label(this.mixedCodeString);
         BorderPane var20 = new BorderPane();
         var20.setId("center-more-info-panel");
         var16 = new Hyperlink(ResourceManager.getMessage("dialog.template.more.info"));
         var16.setMnemonicParsing(true);
         var16.setOnAction(new EventHandler() {
            public void handle(ActionEvent var1) {
               DialogTemplate.this.showMixedcodeMoreInfo();
            }
         });
         var20.setLeft(var16);
         var13.setTop(this.mixedCodeLabel);
         var13.setBottom(var20);
      }

      boolean var21 = var4 >= 0;
      if (var21) {
         this.progressBar = new ProgressBar();
         this.progressBar.setVisible(var4 <= 100);
      }

      BorderPane var22;
      UITextArea var24;
      if (this.contentString != null) {
         if (this.contentLabel != null) {
            var22 = new BorderPane();
            var22.setLeft(new Label(this.contentLabel));
            var14.setTop(var22);
         }

         if (this.contentScroll) {
            boolean var17 = this.largeScroll;
            Label var23;
            if (this.largeScroll) {
               var23 = new Label(this.contentString);
               var23.setPrefWidth(640.0);
               var23.setPrefHeight(240.0);
            } else {
               var23 = new Label(this.contentString);
               var23.setPrefWidth(320.0);
               var23.setPrefHeight(48.0);
            }

            ScrollPane var18 = new ScrollPane();
            var18.setContent(var23);
            var18.setFitToWidth(true);
            VBox.setVgrow(var18, Priority.ALWAYS);
            if (var17) {
               var18.setMaxWidth(600.0);
            }

            var14.setCenter(var18);
         } else if (this.isBlockedDialog) {
            VBox var25 = new VBox();
            var24 = new UITextArea(this.contentString);
            var24.setId("center-content-area");
            var24.setAlignment(Pos.TOP_CENTER);
            var24.setPrefWidth(540.0);
            Hyperlink var28 = new Hyperlink(ResourceManager.getMessage("deployment.blocked.moreinfo.hyperlink.text"));
            final String var19 = ResourceManager.getMessage("deployment.blocked.moreinfo.hyperlink.url");
            var28.setMnemonicParsing(true);
            var28.setOnAction(new EventHandler() {
               public void handle(ActionEvent var1) {
                  HostServicesDelegate var2 = HostServicesImpl.getInstance((Application)null);
                  if (var2 != null && var19 != null) {
                     var2.showDocument(var19);
                  }

               }
            });
            var25.getChildren().add(var24);
            var25.getChildren().add(var28);
            var14.setCenter(var25);
         } else {
            UITextArea var27 = new UITextArea(this.contentString);
            var27.setId("center-content-area");
            var27.setAlignment(Pos.TOP_LEFT);
            var14.setCenter(var27);
         }

         var14.setPadding(new Insets(0.0, 0.0, 12.0, 0.0));
      }

      if (this.scrollList != null) {
         if (this.contentLabel != null) {
            var22 = new BorderPane();
            var22.setLeft(new Label(this.contentLabel));
            var14.setTop(var22);
         }

         if (this.contentScroll) {
            ScrollPane var30 = new ScrollPane();
            var30.setContent(this.scrollList);
            VBox.setVgrow(var30, Priority.ALWAYS);
            var14.setCenter(var30);
         }

         if (this.showDetails) {
            var16 = new Hyperlink(ResourceManager.getMessage("security.more.info.details"));
            var16.setMnemonicParsing(true);
            var16.setOnAction(new EventHandler() {
               public void handle(ActionEvent var1) {
                  DialogTemplate.this.showCertificateDetails();
               }
            });
            FlowPane var26 = new FlowPane();
            var26.setPadding(new Insets(12.0, 0.0, 12.0, 0.0));
            var26.setAlignment(Pos.TOP_LEFT);
            var26.getChildren().add(var16);
            var14.setBottom(var26);
         }
      }

      FlowPane var32 = new FlowPane(6.0, 0.0);
      var32.getStyleClass().add("button-bar");
      var32.setId("center-bottom-button-bar");
      this.okBtn = new Button(var2 == null ? "" : ResourceManager.getMessage(var2));
      this.okBtn.setOnAction(this.okHandler);
      var32.getChildren().add(this.okBtn);
      this.okBtn.setVisible(var2 != null);
      this.cancelBtn = new Button(var3 == null ? "" : ResourceManager.getMessage(var3));
      this.cancelBtn.setOnAction(this.cancelHandler);
      var32.getChildren().add(this.cancelBtn);
      this.cancelBtn.setVisible(var3 != null);
      if (this.okBtn.isVisible()) {
         this.okBtn.setDefaultButton(true);
      } else {
         this.cancelBtn.setCancelButton(true);
      }

      resizeButtons(this.okBtn, this.cancelBtn);
      if (this.isBlockedDialog && var14.getChildren().size() > 0) {
         this.centerPanel.getChildren().add(var14);
      }

      if (this.cacheUpgradeContentString != null) {
         var24 = new UITextArea(this.cacheUpgradeContentString);
         var24.setId("cache-upgrade-content");
         var14.setTop(var24);
      } else {
         if (this.includeAppInfo) {
            this.centerPanel.getChildren().add(var5);
         }

         if (this.alwaysString != null) {
            this.centerPanel.getChildren().add(var12);
         }

         if (this.mixedCodeString != null) {
            this.centerPanel.getChildren().add(var13);
         }
      }

      if (!this.isBlockedDialog && var14.getChildren().size() > 0) {
         this.centerPanel.getChildren().add(var14);
      }

      BorderPane var29 = new BorderPane();
      var29.setId("center-bottom-panel");
      if (var21) {
         this.progressStatusLabel = new Label(" ");
         this.progressStatusLabel.getStyleClass().add("progress-label");
         BorderPane var31 = new BorderPane();
         var31.setId("center-progress-status-panel");
         this.centerPanel.getChildren().add(var31);
         var31.setLeft(this.progressStatusLabel);
         var31.setPadding(new Insets(2.0, 0.0, 2.0, 0.0));
         var29.setCenter(this.progressBar);
      }

      var29.setRight(var32);
      this.centerPanel.getChildren().add(var29);
      return this.centerPanel;
   }

   private Pane createBottomPanel(boolean var1) {
      HBox var2 = new HBox();
      var2.setId("bottom-panel");
      if (this.alertStrs != null || this.infoStrs != null) {
         String var3 = "security.alert.high.image";
         if (this.alertStrs != null && this.alertStrs.length != 0) {
            if (this.mixedCodeString == null) {
               this.okBtn.setDefaultButton(false);
               this.cancelBtn.setCancelButton(true);
            }
         } else {
            var3 = "security.alert.low.image";
            if (this.always != null) {
               this.always.setSelected(true);
            }
         }

         this.securityIcon = ResourceManager.getIcon(var3);
         var2.getChildren().add(this.securityIcon);
         boolean var4 = false;
         Hyperlink var5 = null;
         if (var1) {
            var5 = new Hyperlink(ResourceManager.getMessage("dialog.template.more.info"));
            var5.setMnemonicParsing(true);
            var5.setId("bottom-more-info-link");
            var5.setOnAction(new EventHandler() {
               public void handle(ActionEvent var1) {
                  DialogTemplate.this.showMoreInfo();
               }
            });
         }

         short var6 = 333;
         UITextArea var7 = new UITextArea((double)var6);
         var7.setId("bottom-text");
         if ((this.alertStrs == null || this.alertStrs.length == 0) && this.infoStrs != null && this.infoStrs.length != 0) {
            var7.setText(this.infoStrs[0] != null ? this.infoStrs[0] : " ");
         } else if (this.alertStrs != null && this.alertStrs.length != 0) {
            var7.setText(this.alertStrs[0] != null ? this.alertStrs[0] : " ");
         }

         var2.getChildren().add(var7);
         if (var5 != null) {
            var2.getChildren().add(var5);
         }
      }

      return var2;
   }

   private BorderPane createSSVTopPanel(String var1, String var2, String var3) {
      BorderPane var4 = new BorderPane();
      var4.setPadding(new Insets(16.0, 0.0, 16.0, 16.0));
      Label var5 = new Label(var1);
      var5.getStyleClass().add("ssv-big-bold-label");
      var4.setTop(var5);
      Label var6 = new Label(ResourceManager.getMessage("dialog.template.name"));
      var6.getStyleClass().add("ssv-small-bold-label");
      var6.setId("ssv-top-panel-name-label");
      Label var7 = new Label(ResourceManager.getMessage("deployment.ssv.location"));
      var7.getStyleClass().add("ssv-small-bold-label");
      var7.setId("ssv-top-panel-from-label");
      this.nameInfo = new Label(var2);
      this.nameInfo.getStyleClass().add("ssv-big-bold-label");
      Label var8 = new Label(var3);
      var8.getStyleClass().add("ssv-small-label");
      BorderPane[] var9 = new BorderPane[4];

      for(int var10 = 0; var10 < 4; ++var10) {
         var9[var10] = new BorderPane();
      }

      ImageView var11 = ResourceManager.getIcon("warning48.image");
      var9[2].setTop(var6);
      var9[2].setBottom(var7);
      var9[2].setPadding(new Insets(2.0, 0.0, 0.0, 0.0));
      var9[3].setTop(this.nameInfo);
      var9[3].setBottom(var8);
      var9[1].setLeft(var9[2]);
      var9[1].setCenter(var9[3]);
      var9[1].setPadding(new Insets(12.0, 0.0, 12.0, 0.0));
      var9[0].setLeft(var11);
      var9[0].setRight(var9[1]);
      var9[0].setPadding(new Insets(8.0, 0.0, 0.0, 32.0));
      var4.setBottom(var9[0]);
      return var4;
   }

   private BorderPane createSSVRiskPanel(String var1, String var2, final URL var3) {
      BorderPane var4 = new BorderPane();
      var4.setPadding(new Insets(8.0, 8.0, 0.0, 8.0));
      int var5 = var1.indexOf(" ");
      if (var5 < var1.length() - 2) {
         String var6 = var1.substring(0, var5);
         String var7 = var1.substring(var5 + 1);
         BorderPane var8 = new BorderPane();
         Label var9 = new Label(var6);
         var9.getStyleClass().add("ssv-small-bold-label");
         var8.setTop(var9);
         var8.setPadding(new Insets(0.0, 8.0, 0.0, 8.0));
         BorderPane var10 = new BorderPane();
         Label var11 = new Label(var7);
         var10.setLeft(var11);
         var11.getStyleClass().add("ssv-small-label");
         Hyperlink var12 = new Hyperlink(var2);
         var12.setOnAction(new EventHandler() {
            public void handle(ActionEvent var1) {
               HostServicesDelegate var2 = HostServicesImpl.getInstance((Application)null);
               if (var2 != null && var3 != null) {
                  var2.showDocument(var3.toExternalForm());
               }

            }
         });
         var10.setRight(var12);
         var4.setLeft(var8);
         var4.setCenter(var10);
      }

      return var4;
   }

   private Pane createPasswordPanel(String var1, boolean var2, boolean var3, String var4, String var5, boolean var6, char[] var7, String var8, String var9) {
      Label var10 = new Label();
      Label var11 = new Label();
      ImageView var12 = ResourceManager.getIcon("pwd-masthead.png");
      String var13;
      if (var2) {
         var13 = "password.dialog.username";
         var10.setText(ResourceManager.getMessage(var13));
         var10.setMnemonicParsing(true);
         this.pwdName = new TextField();
         this.pwdName.setId("user-name-field");
         this.pwdName.setText(var4);
         var10.setLabelFor(this.pwdName);
         var10.setId("user-name-label");
      }

      var13 = "password.dialog.password";
      Label var14 = new Label(ResourceManager.getMessage(var13));
      this.password = new PasswordField();
      this.password.setText(String.valueOf(var7));
      var14.setLabelFor(this.password);
      var14.setMnemonicParsing(true);
      var14.setId("password-label");
      if (var3) {
         String var15 = "password.dialog.domain";
         var11.setText(ResourceManager.getMessage(var15));
         this.pwdDomain = new TextField();
         this.pwdDomain.setText(var5);
         var11.setLabelFor(this.pwdDomain);
         var11.setMnemonicParsing(true);
         var11.setId("password-domain-label");
      }

      VBox var24 = new VBox();
      var24.setMaxWidth(var12.getImage().getWidth());
      var24.getChildren().add(var12);
      VBox var16 = new VBox(10.0);
      var16.setId("password-panel");
      Label var17 = new Label();
      var17.setId("password-details");
      var17.setWrapText(true);
      var17.setText(var1);
      var16.getChildren().add(var17);
      GridPane var18 = new GridPane();
      var18.setId("password-panel-grid");
      int var19 = 0;
      if (var2) {
         GridPane.setConstraints(var10, 0, var19);
         GridPane.setHalignment(var10, HPos.RIGHT);
         var18.getChildren().add(var10);
         GridPane.setConstraints(this.pwdName, 1, var19++);
         var18.getChildren().add(this.pwdName);
      }

      GridPane.setConstraints(var14, 0, var19);
      GridPane.setHalignment(var14, HPos.RIGHT);
      var18.getChildren().add(var14);
      GridPane.setConstraints(this.password, 1, var19++);
      var18.getChildren().add(this.password);
      if (var3) {
         GridPane.setConstraints(var11, 0, var19);
         GridPane.setHalignment(var11, HPos.RIGHT);
         var18.getChildren().add(var11);
         GridPane.setConstraints(this.pwdDomain, 1, var19++);
         var18.getChildren().add(this.pwdDomain);
      }

      if (var6) {
         this.always = new CheckBox(ResourceManager.getMessage("password.dialog.save"));
         this.always.setId("password-always-checkbox");
         this.always.setSelected(var7.length > 0);
         GridPane.setConstraints(this.always, 1, var19++);
         var18.getChildren().add(this.always);
      }

      var16.getChildren().add(var18);
      if (var9 != null) {
         Label var20 = new Label();
         var20.setId("password-warning");
         var20.setWrapText(true);
         var20.setText(var9);
         var16.getChildren().add(var20);
      }

      FlowPane var25 = new FlowPane(6.0, 0.0);
      var25.setPrefWrapLength(300.0);
      var25.getStyleClass().add("button-bar");
      var25.setId("password-button-bar");
      this.okBtn = new Button(ResourceManager.getMessage("common.ok_btn"));
      this.okBtn.setOnAction(this.okHandler);
      this.okBtn.setDefaultButton(true);
      this.cancelBtn = new Button(ResourceManager.getMessage("common.cancel_btn"));
      this.cancelBtn.setOnAction(this.cancelHandler);
      resizeButtons(this.okBtn, this.cancelBtn);
      var25.getChildren().addAll(this.okBtn, this.cancelBtn);
      var16.getChildren().add(var25);
      if (var8 != null) {
         MessageFormat var21 = new MessageFormat(ResourceManager.getMessage("password.dialog.scheme"));
         Object[] var22 = new Object[]{var8};
         Label var23 = new Label(var21.format(var22));
         var16.getChildren().add(var23);
      }

      var24.getChildren().add(var16);
      return var24;
   }

   void showMoreInfo() {
      MoreInfoDialog var1;
      if (this.throwable == null && this.detailPanel == null) {
         var1 = new MoreInfoDialog(this.dialog, this.alertStrs, this.infoStrs, this.securityInfoCount, this.certs, this.start, this.end, this.sandboxApp);
      } else {
         var1 = new MoreInfoDialog(this.dialog, this.detailPanel, this.throwable, this.certs);
      }

      var1.show();
   }

   void showMixedcodeMoreInfo() {
      MoreInfoDialog var1 = new MoreInfoDialog(this.dialog, (String[])null, this.infoStrs, 0, (Certificate[])null, 0, 0, this.sandboxApp);
      var1.show();
   }

   void showCertificateDetails() {
      long var1 = (long)this.scrollList.getSelectionModel().getSelectedIndex();
      X509Certificate[] var3 = null;

      for(Iterator var4 = this.clientAuthCertsMap.values().iterator(); var1 >= 0L && var4.hasNext(); --var1) {
         var3 = (X509Certificate[])((X509Certificate[])var4.next());
      }

      if (var3 != null) {
         CertificateDialog.showCertificates(this.dialog, var3, 0, var3.length);
      }

   }

   public void setVisible(boolean var1) {
      if (var1) {
         final FXDialog var2 = this.dialog;
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

   public static void resizeButtons(Button... var0) {
      int var1 = var0.length;
      double var2 = 50.0;

      for(int var4 = 0; var4 < var1; ++var4) {
         if (var0[var4].prefWidth(-1.0) > var2) {
            var2 = var0[var4].prefWidth(-1.0);
         }
      }

   }

   public void cancelAction() {
      this.userAnswer = 1;
      this.setVisible(false);
   }

   int getUserAnswer() {
      return this.userAnswer;
   }

   void setUserAnswer(int var1) {
      this.userAnswer = var1;
   }

   char[] getPassword() {
      return this.pwd;
   }

   String getUserName() {
      return this.userName;
   }

   String getDomain() {
      return this.domain;
   }

   public boolean isPasswordSaved() {
      return this.always != null && this.always.isSelected();
   }

   public void progress(int var1) {
      if (this.progressBar != null) {
         if (var1 <= 100) {
            this.progressBar.setProgress((double)var1 / 100.0);
            this.progressBar.setVisible(true);
         } else {
            this.progressBar.setVisible(false);
         }
      }

   }

   public FXDialog getDialog() {
      return this.dialog;
   }

   static String getDisplayMainJNLPTooltip(AppInfo var0) {
      try {
         Class var1 = AppInfo.class;
         Method var2 = var1.getMethod("getDisplayMainJNLPTooltip", (Class[])null);
         return (String)var2.invoke(var0);
      } catch (Exception var3) {
         var3.printStackTrace();
         return "";
      }
   }

   public void setInfo(AppInfo var1) {
      this.ainfo = var1;
      if (this.nameInfo != null) {
         this.nameInfo.setText(var1.getTitle());
      }

      if (this.publisherInfo != null) {
         this.publisherInfo.setText(var1.getVendor());
      }

      URL var2;
      if (this.urlInfo != null) {
         this.urlInfo.setText(var1.getDisplayFrom());
         var2 = var1.getFrom();
         this.urlInfo.setTooltip(new Tooltip(var2 == null ? "" : var2.toString()));
      }

      if (this.mainJNLPInfo != null) {
         this.mainJNLPInfo.setText(var1.getDisplayMainJNLP());
         this.mainJNLPInfo.setTooltip(new Tooltip(getDisplayMainJNLPTooltip(var1)));
      }

      if (this.documentBaseInfo != null) {
         this.documentBaseInfo.setText(var1.getDisplayDocumentBase());
         var2 = var1.getDocumentBase();
         this.documentBaseInfo.setTooltip(new Tooltip(var2 == null ? "" : var2.toString()));
      }

   }

   void showOk(boolean var1) {
      resizeButtons(this.okBtn, this.cancelBtn);
      this.okBtn.setVisible(var1);
   }

   void stayAlive() {
      this.stayAliveOnOk = true;
   }

   public void setProgressStatusText(String var1) {
      if (this.progressStatusLabel != null) {
         if (var1 == null || var1.length() == 0) {
            var1 = " ";
         }

         this.progressStatusLabel.setText(var1);
      }

   }

   void setPublisherInfo(String var1, String var2, String var3, Object var4, boolean var5) {
      this.detailPanel = (Pane)var4;
      this.contentString = var1;
      this.useInfoIcon = true;
      if (var4 == null) {
         var3 = null;
      }

      if (var5) {
         this.includeAppInfo = false;
      }

      this.okBtnStr = var2;
      this.cancelBtnStr = var3;

      try {
         this.contentPane.getChildren().add(this.createTopPanel(true));
         this.contentPane.getChildren().add(this.createCenterPanel(false, var2, var3, -1));
         this.dialog.setResizable(false);
         this.dialog.setIconifiable(false);
      } catch (Throwable var7) {
         Trace.ignored(var7);
      }

   }

   void setBlockedDialogInfo(String var1, String var2, String var3, String var4, Object var5, boolean var6) {
      this.detailPanel = (Pane)var5;
      this.contentString = var2;
      this.reason = var1;
      this.useBlockedIcon = true;
      this.isBlockedDialog = true;
      if (var5 == null) {
         var4 = null;
      }

      if (var6) {
         this.includeAppInfo = false;
      }

      this.okBtnStr = var3;
      this.cancelBtnStr = var4;

      try {
         this.contentPane.getChildren().add(this.createTopPanel(true));
         this.contentPane.getChildren().add(this.createCenterPanel(false, var3, var4, -1));
         this.dialog.setResizable(false);
         this.dialog.setIconifiable(false);
      } catch (Throwable var8) {
         Trace.ignored(var8);
      }

   }

   static void showDocument(String var0) {
      try {
         Class var1 = Class.forName("com.sun.deploy.config.Platform");
         Method var2 = var1.getMethod("get");
         Object var3 = var2.invoke((Object)null);
         Method var4 = var3.getClass().getMethod("showDocument", String.class);
         var4.invoke(var3, var0);
      } catch (Exception var5) {
         Trace.ignored(var5);
      }

   }

   private class SSVChoicePanel extends BorderPane {
      ToggleGroup group;
      RadioButton button1;
      RadioButton button2;

      public SSVChoicePanel(String var2, String var3, String var4) {
         this.setPadding(new Insets(8.0, 16.0, 0.0, 16.0));
         BorderPane var5 = new BorderPane();
         VBox var6 = new VBox();
         var6.setSpacing(4.0);
         Label var7 = new Label(var2);
         var7.getStyleClass().add("ssv-small-bold-label");
         var5.setCenter(var7);
         this.button1 = new RadioButton(var3);
         this.button1.getStyleClass().add("ssv-small-label");
         this.button2 = new RadioButton(var4);
         this.button2.getStyleClass().add("ssv-small-label");
         this.group = new ToggleGroup();
         this.button1.setToggleGroup(this.group);
         this.button2.setToggleGroup(this.group);
         this.button1.setSelected(true);
         var6.getChildren().addAll(this.button1, this.button2);
         var6.setPadding(new Insets(0.0, 16.0, 0.0, 32.0));
         this.setTop(var5);
         this.setBottom(var6);
         this.button1.requestFocus();
      }

      public int getSelection() {
         return this.button2.isSelected() ? 1 : 0;
      }
   }
}
