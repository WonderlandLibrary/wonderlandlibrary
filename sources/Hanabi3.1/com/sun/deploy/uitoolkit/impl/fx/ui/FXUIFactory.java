package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.security.CredentialInfo;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.impl.fx.FXPluginToolkit;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.deploy.uitoolkit.ui.ConsoleController;
import com.sun.deploy.uitoolkit.ui.ConsoleWindow;
import com.sun.deploy.uitoolkit.ui.DialogHook;
import com.sun.deploy.uitoolkit.ui.ModalityHelper;
import com.sun.deploy.uitoolkit.ui.NativeMixedCodeDialog;
import com.sun.deploy.uitoolkit.ui.PluginUIFactory;
import com.sun.deploy.util.DeploySysAction;
import com.sun.deploy.util.DeploySysRun;
import com.sun.javafx.application.PlatformImpl;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class FXUIFactory extends PluginUIFactory {
   public int showMessageDialog(Object var1, AppInfo var2, int var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11) {
      switch (var3) {
         case -1:
         case 6:
         default:
            return showContentDialog(var1, var2, var4, var6, true, var8, var9);
         case 0:
            if (var7 != null) {
               return showErrorDialog(var1, var2, var4, var6, (String)null, var8, var9, (Throwable)null, getDetailPanel(var7), (Certificate[])null);
            }

            return showErrorDialog(var1, var2, var4, var5, var6, var8, var9, var10);
         case 1:
            showInformationDialog(var1, var4, var5, var6);
            return -1;
         case 2:
            return showWarningDialog(var1, var2, var4, var5, var6, var8, var9);
         case 3:
            return showConfirmDialog(var1, var2, var4, var5, var7, var8, var9, true);
         case 4:
            return showMixedCodeDialog(var1, var2, var4, var5, var6, var7, var8, var9, true, var11);
         case 5:
            return showIntegrationDialog(var1, var2);
         case 7:
            return showApiDialog((Object)null, var2, var4, var6, var5, var8, var9, false);
      }
   }

   public void showExceptionDialog(Object var1, AppInfo var2, Throwable var3, String var4, String var5, String var6, Certificate[] var7) {
      if (var7 == null) {
         showExceptionDialog(var1, var3, var5, var6, var4);
      } else {
         showCertificateExceptionDialog(var1, var2, var3, var6, var4, var7);
      }

   }

   public CredentialInfo showPasswordDialog(Object var1, String var2, String var3, boolean var4, boolean var5, CredentialInfo var6, boolean var7, String var8, String var9) {
      return showPasswordDialog0(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public int showSecurityDialog(AppInfo var1, String var2, String var3, String var4, URL var5, boolean var6, boolean var7, String var8, String var9, String[] var10, String[] var11, boolean var12, Certificate[] var13, int var14, int var15, boolean var16) {
      return showSecurityDialog0(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, false, false, false);
   }

   public int showSecurityDialog(AppInfo var1, String var2, String var3, String var4, URL var5, boolean var6, boolean var7, String var8, String var9, String[] var10, String[] var11, boolean var12, Certificate[] var13, int var14, int var15, boolean var16, boolean var17, boolean var18) {
      return showSecurityDialog0(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, false, var18);
   }

   public int showSandboxSecurityDialog(AppInfo var1, String var2, String var3, String var4, URL var5, boolean var6, boolean var7, String var8, String var9, String[] var10, String[] var11, boolean var12, Certificate[] var13, int var14, int var15, boolean var16, boolean var17) {
      return showSecurityDialog0(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, false, true, var17);
   }

   public void showAboutJavaDialog() {
      Platform.runLater(new Runnable() {
         public void run() {
            FXAboutDialog.showAboutJavaDialog();
         }
      });
   }

   public int showListDialog(Object var1, String var2, String var3, String var4, boolean var5, Vector var6, TreeMap var7) {
      ListView var8 = new ListView();
      var8.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      var8.setItems(FXCollections.observableList(var6));
      if (var6.size() > 0) {
         var8.getSelectionModel().select(0);
      }

      return showListDialog0(var1, var2, var3, var4, var5, var8, var7);
   }

   public int showUpdateCheckDialog() {
      return showUpdateCheckDialog0();
   }

   public ConsoleWindow getConsole(ConsoleController var1) {
      return new FXConsole(var1);
   }

   public void setDialogHook(DialogHook var1) {
   }

   public ModalityHelper getModalityHelper() {
      return new FXModalityHelper();
   }

   public static int showErrorDialog(Object var0, final AppInfo var1, final String var2, final String var3, final String var4, final String var5, final String var6, final Throwable var7, final Object var8, final Certificate[] var9) {
      final Stage var10 = beforeDialog((Stage)var0);

      int var12;
      try {
         int var11 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1x = new DialogTemplate(var1, var10, var2, var3);
               var1x.setErrorContent(var4, var5, var6, var7, var8, var9, false);
               var1x.getDialog().centerOnScreen();
               var1x.setVisible(true);
               int var2x = var1x.getUserAnswer();
               return new Integer(var2x);
            }
         });
         System.err.println("FXUIFactory.showErrorDialog: shutting down the FX toolkit");

         try {
            PlatformImpl.tkExit();
         } catch (Exception var17) {
            var17.printStackTrace();
         }

         var12 = var11;
         return var12;
      } catch (Throwable var18) {
         var12 = -1;
      } finally {
         afterDialog();
      }

      return var12;
   }

   public static int showErrorDialog(Object var0, final AppInfo var1, String var2, final String var3, final String var4, String var5, final String var6, final String var7) {
      final String var8 = var5 == null ? ResourceManager.getString("common.ok_btn") : var5;
      if (var2 == null) {
         ResourceManager.getString("error.default.title");
      }

      final Stage var10 = beforeDialog((Stage)var0);

      byte var12;
      try {
         int var11 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               String var1x = ResourceManager.getString("error.default.title");
               DialogTemplate var2 = new DialogTemplate(var1, var10, var1x, var3);
               var2.setMultiButtonErrorContent(var4, var8, var6, var7);
               var2.getDialog().centerOnScreen();
               var2.setVisible(true);
               int var3x = var2.getUserAnswer();
               return new Integer(var3x);
            }
         });
         return var11;
      } catch (Throwable var16) {
         var12 = -1;
      } finally {
         afterDialog();
      }

      return var12;
   }

   public static int showContentDialog(Object var0, final AppInfo var1, final String var2, final String var3, final boolean var4, final String var5, final String var6) {
      final Stage var7 = beforeDialog((Stage)var0);

      byte var9;
      try {
         int var8 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1x = new DialogTemplate(var1, var7, var2, (String)null);
               var1x.setSimpleContent(var3, var4, (String)null, var5, var6, false, false);
               var1x.getDialog().centerOnScreen();
               var1x.setVisible(true);
               int var2x = var1x.getUserAnswer();
               return new Integer(var2x);
            }
         });
         return var8;
      } catch (Throwable var13) {
         var9 = -1;
      } finally {
         afterDialog();
      }

      return var9;
   }

   public static void showInformationDialog(final Object var0, final String var1, final String var2, final String var3) {
      final String var4 = ResourceManager.getString("common.ok_btn");
      final AppInfo var5 = new AppInfo();

      try {
         FXPluginToolkit.callAndWait(new Callable() {
            public Void call() {
               DialogTemplate var1x = new DialogTemplate(var5, (Stage)var0, var1, var2);
               var1x.setInfoContent(var3, var4);
               var1x.getDialog().centerOnScreen();
               var1x.setVisible(true);
               return null;
            }
         });
      } catch (Throwable var7) {
         Trace.ignored(var7);
      }

   }

   public static int showWarningDialog(Object var0, AppInfo var1, final String var2, final String var3, final String var4, String var5, String var6) {
      final AppInfo var7 = var1 == null ? new AppInfo() : var1;
      final String var8 = var5 == null ? ResourceManager.getString("common.ok_btn") : var5;
      final String var9 = var6 == null ? ResourceManager.getString("common.cancel_btn") : var6;
      final Stage var10 = beforeDialog((Stage)var0);

      byte var12;
      try {
         int var11 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1 = new DialogTemplate(var7, var10, var2, var3);
               var1.setSimpleContent(var4, false, (String)null, var8, var9, true, true);
               var1.getDialog().centerOnScreen();
               var1.setVisible(true);
               int var2x = var1.getUserAnswer();
               return new Integer(var2x);
            }
         });
         return var11;
      } catch (Throwable var16) {
         var12 = -1;
      } finally {
         afterDialog();
      }

      return var12;
   }

   public static int showConfirmDialog(Object var0, AppInfo var1, final String var2, final String var3, final String var4, final String var5, final String var6, final boolean var7) {
      final AppInfo var8 = var1 == null ? new AppInfo() : var1;
      final Stage var9 = beforeDialog((Stage)var0);

      byte var11;
      try {
         int var10 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1 = new DialogTemplate(var8, var9, var2, var3);
               var1.setSimpleContent((String)null, false, var4, var5, var6, true, var7);
               var1.getDialog().centerOnScreen();
               var1.setVisible(true);
               int var2x = var1.getUserAnswer();
               return new Integer(var2x);
            }
         });
         return var10;
      } catch (Throwable var15) {
         var11 = -1;
      } finally {
         afterDialog();
      }

      return var11;
   }

   public static int showMixedCodeDialog(Object var0, AppInfo var1, String var2, String var3, String var4, String var5, String var6, String var7, boolean var8, String var9) {
      boolean var10 = var1 != null;
      String var11 = "";
      String var12 = "";
      if (var10) {
         var11 = var1.getVendor();
         var12 = var1.getTitle();
      }

      String var13 = "security.dialog.nativemixcode." + (var10 ? "js." : "");
      String var14 = var10 ? com.sun.deploy.resources.ResourceManager.getString(var13 + "appLabelWebsite") : "";
      String var15 = var10 ? com.sun.deploy.resources.ResourceManager.getString(var13 + "appLabelPublisher") : "";
      String var16 = var10 ? var1.getDisplayFrom() : "";

      try {
         if (NativeMixedCodeDialog.isSupported()) {
            String var17 = com.sun.deploy.resources.ResourceManager.getString("dialog.template.more.info");
            String var18 = com.sun.deploy.resources.ResourceManager.getString("common.close_btn");
            String var19 = com.sun.deploy.resources.ResourceManager.getString("security.more.info.title");
            String var20 = com.sun.deploy.resources.ResourceManager.getString("security.dialog.mixcode.info1") + "\n\n" + com.sun.deploy.resources.ResourceManager.getString("security.dialog.mixcode.info2") + "\n\n" + com.sun.deploy.resources.ResourceManager.getString("security.dialog.mixcode.info3");
            String var21 = com.sun.deploy.resources.ResourceManager.getString("dialog.template.name");
            if (var1 == null) {
               new AppInfo();
            }

            return NativeMixedCodeDialog.show(var2, var3, var4, var5, var6, var7, var17, var18, var19, var20, var21, var12, var14, var16, var15, var11, var9);
         } else {
            return MixedCodeInSwing.show(var0, var1, var2, var3, var4, var5, var6, var7, var8, var10, var9);
         }
      } catch (Throwable var22) {
         Trace.ignored(var22);
         return -1;
      }
   }

   public static int showSecurityDialog0(final AppInfo var0, final String var1, final String var2, final String var3, final URL var4, final boolean var5, final boolean var6, final String var7, final String var8, final String[] var9, final String[] var10, final boolean var11, final Certificate[] var12, final int var13, final int var14, final boolean var15, final boolean var16, final boolean var17, final boolean var18) {
      final Stage var19 = beforeDialog((Stage)null);

      byte var22;
      try {
         int var20 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               String var1x = var7;
               String var2x = var2;
               String var3x = var1;
               String[] var4x = new String[0];
               if (var10 != null) {
                  var4x = var10;
               }

               DialogTemplate var5x = new DialogTemplate(var0, var19, var3x, var2x);
               int var6x = var4x.length;
               var4x = FXUIFactory.addDetail(var4x, var0, true, true);
               var0.setVendor(var3);
               var0.setFrom(var4);
               if (var16) {
                  var5x.setSecurityContent(var5, var6, var7, var8, var9, var4x, var6x, var11, var12, var13, var14, var15);
               } else {
                  var5x.setNewSecurityContent(var5, var6, var1x, var8, var9, var4x, var6x, var11, var12, var13, var14, var15, var17, var18);
               }

               var5x.getDialog().centerOnScreen();
               var5x.setVisible(true);
               int var7x = var5x.getUserAnswer();
               return new Integer(var7x);
            }
         });
         return var20;
      } catch (Throwable var26) {
         var22 = -1;
      } finally {
         afterDialog();
      }

      return var22;
   }

   public static int showIntegrationDialog(Object var0, final AppInfo var1) {
      final Stage var2 = beforeDialog((Stage)var0);

      byte var4;
      try {
         int var3 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               String var1x = ResourceManager.getString("integration.title");
               boolean var2x = var1.getDesktopHint() || var1.getMenuHint();
               String var3 = "integration.text.shortcut";
               if (var2x) {
                  var3 = "integration.text.both";
               } else {
                  var3 = "integration.text.association";
               }

               String var4 = ResourceManager.getString(var3);
               String[] var5 = new String[0];
               String[] var6 = new String[0];
               var6 = FXUIFactory.addDetail(var6, var1, false, true);
               String[] var7 = new String[0];
               var7 = FXUIFactory.addDetail(var7, var1, true, false);
               boolean var8 = var6.length + var7.length > 1;
               String var9 = ResourceManager.getString("common.ok_btn");
               String var10 = ResourceManager.getString("integration.skip.button");
               DialogTemplate var11 = new DialogTemplate(var1, var2, var1x, var4);
               var11.setSecurityContent(false, false, var9, var10, var6, var7, 0, var8, (Certificate[])null, 0, 0, false);
               var11.getDialog().centerOnScreen();
               var11.setVisible(true);
               int var12 = var11.getUserAnswer();
               return new Integer(var12);
            }
         });
         return var3;
      } catch (Throwable var8) {
         var4 = -1;
      } finally {
         afterDialog();
      }

      return var4;
   }

   public static int showUpdateCheckDialog0() {
      final String var3 = ResourceManager.getMessage("autoupdatecheck.caption");
      final String var4 = ResourceManager.getMessage("autoupdatecheck.message");
      final String var5 = ResourceManager.getMessage("autoupdatecheck.masthead");
      final AppInfo var6 = new AppInfo();
      final Stage var7 = beforeDialog((Stage)null);

      byte var9;
      try {
         int var8 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1 = new DialogTemplate(var6, var7, var3, var5);
               var1.setUpdateCheckContent(var4, "autoupdatecheck.buttonYes", "autoupdatecheck.buttonNo", "autoupdatecheck.buttonAskLater");
               var1.getDialog().centerOnScreen();
               var1.setVisible(true);
               int var2 = var1.getUserAnswer();
               return new Integer(var2);
            }
         });
         return var8;
      } catch (Throwable var13) {
         var9 = 3;
      } finally {
         afterDialog();
      }

      return var9;
   }

   public static int showListDialog0(Object var0, final String var1, final String var2, final String var3, final boolean var4, final ListView var5, final TreeMap var6) {
      final String var7 = ResourceManager.getString("common.ok_btn");
      final String var8 = ResourceManager.getString("common.cancel_btn");
      final Stage var9 = beforeDialog((Stage)var0);

      byte var11;
      try {
         int var10 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1x = new DialogTemplate(new AppInfo(), var9, var1, var2);
               var1x.setListContent(var3, var5, var4, var7, var8, var6);
               var1x.getDialog().centerOnScreen();
               var1x.setVisible(true);
               int var2x = var1x.getUserAnswer();
               return new Integer(var2x);
            }
         });
         return var10;
      } catch (Throwable var15) {
         var11 = -1;
      } finally {
         afterDialog();
      }

      return var11;
   }

   public static int showApiDialog(Object var0, AppInfo var1, final String var2, final String var3, final String var4, final String var5, final String var6, final boolean var7) {
      final String var8 = ResourceManager.getString("common.ok_btn");
      final String var9 = ResourceManager.getString("common.cancel_btn");
      final AppInfo var10 = var1 == null ? new AppInfo() : var1;
      final Stage var11 = beforeDialog((Stage)var0);

      byte var13;
      try {
         int var12 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1 = new DialogTemplate(var10, var11, var2, var3);
               var1.setApiContent(var5, var4, var6, var7, var8, var9);
               var1.getDialog().centerOnScreen();
               var1.setVisible(true);
               int var2x = var1.getUserAnswer();
               return new Integer(var2x);
            }
         });
         return var12;
      } catch (Throwable var17) {
         var13 = -1;
      } finally {
         afterDialog();
      }

      return var13;
   }

   public static void showExceptionDialog(Object var0, Throwable var1, String var2, String var3, String var4) {
      String var5 = ResourceManager.getString("common.ok_btn");
      String var6 = ResourceManager.getString("common.detail.button");
      if (var3 == null) {
         var3 = var1.toString();
      }

      if (var4 == null) {
         var4 = ResourceManager.getString("error.default.title");
      }

      showErrorDialog(var0, new AppInfo(), var4, var2, var3, var5, var6, var1, (Object)null, (Certificate[])null);
   }

   public static void showCertificateExceptionDialog(Object var0, AppInfo var1, Throwable var2, String var3, String var4, Certificate[] var5) {
      String var6 = ResourceManager.getString("common.ok_btn");
      String var7 = ResourceManager.getString("common.detail.button");
      if (var3 == null) {
         var3 = var2.toString();
      }

      if (var4 == null) {
         var4 = ResourceManager.getString("error.default.title");
      }

      showErrorDialog(var0, var1, var4, var3, (String)null, var6, var7, var2, (Object)null, var5);
   }

   public static CredentialInfo showPasswordDialog0(Object var0, final String var1, final String var2, final boolean var3, final boolean var4, final CredentialInfo var5, final boolean var6, final String var7, final String var8) {
      final Stage var9 = beforeDialog((Stage)var0);

      Object var11;
      try {
         CredentialInfo var10 = (CredentialInfo)FXPluginToolkit.callAndWait(new Callable() {
            public CredentialInfo call() {
               CredentialInfo var1x = null;
               CredentialInfo var2x = var5;
               DialogTemplate var3x = new DialogTemplate(new AppInfo(), var9, var1, "");
               if (var2x == null) {
                  var2x = new CredentialInfo();
               }

               var3x.setPasswordContent(var2, var3, var4, var2x.getUserName(), var2x.getDomain(), var6, var2x.getPassword(), var7, var8);
               var3x.getDialog().centerOnScreen();
               var3x.setVisible(true);
               int var4x = var3x.getUserAnswer();
               if (var4x == 0 || var4x == 2) {
                  var1x = new CredentialInfo();
                  var1x.setUserName(var3x.getUserName());
                  var1x.setDomain(var3x.getDomain());
                  var1x.setPassword(var3x.getPassword());
                  var1x.setPasswordSaveApproval(var3x.isPasswordSaved());
               }

               return var1x;
            }
         });
         return var10;
      } catch (Throwable var15) {
         var11 = null;
      } finally {
         afterDialog();
      }

      return (CredentialInfo)var11;
   }

   public int showSSVDialog(Object var1, AppInfo var2, String var3, String var4, String var5, String var6, URL var7, String var8, String var9, String var10, String var11, String var12) {
      return showSSVDialog0(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   private File[] showFileChooser_priv(final String var1, final String[] var2, final int var3, final boolean var4, String var5) {
      return (File[])((File[])AccessController.doPrivileged(new PrivilegedAction() {
         public File[] run() {
            FileChooser var1x = new FileChooser();
            File var2x = var1 == null ? null : new File(var1);
            if (var2x != null && !var2x.isDirectory()) {
               var2x = var2x.getParentFile();
            }

            var1x.setInitialDirectory(var2x);
            if (var2 != null) {
               String[] var3x = new String[var2.length];

               for(int var4x = 0; var4x < var2.length; ++var4x) {
                  if (var2[var4x] != null) {
                     var3x[var4x] = "*." + var2[var4x];
                  }
               }

               var1x.getExtensionFilters().setAll((Object[])(new FileChooser.ExtensionFilter(Arrays.toString(var2), Arrays.asList(var3x))));
            }

            if (var4) {
               return (File[])var1x.showOpenMultipleDialog((Window)null).toArray(new File[0]);
            } else {
               return var3 == 8 ? new File[]{var1x.showOpenDialog((Window)null)} : new File[]{var1x.showSaveDialog((Window)null)};
            }
         }
      }));
   }

   public File[] showFileChooser(final String var1, final String[] var2, final int var3, final boolean var4, final String var5) {
      try {
         return (File[])FXPluginToolkit.callAndWait(new Callable() {
            public File[] call() {
               return FXUIFactory.this.showFileChooser_priv(var1, var2, var3, var4, var5);
            }
         });
      } catch (Throwable var7) {
         Trace.ignored(var7);
         return null;
      }
   }

   public static Pane getDetailPanel(String var0) {
      BorderPane var1 = new BorderPane() {
         {
            this.setId("detail-panel");
            this.setPrefWidth(480.0);
            this.setPrefHeight(300.0);
         }
      };
      TabPane var2 = new TabPane();
      var2.setId("detail-panel-tab-pane");
      var2.getStyleClass().add("floating");
      var1.setCenter(var2);
      HBox var3 = new HBox();
      var3.setId("detail-panel-top-pane");
      var3.setAlignment(Pos.BASELINE_LEFT);
      Label var4 = new Label(ResourceManager.getString("launcherrordialog.error.label"));
      var4.setId("error-dialog-error-label");
      var4.setMinWidth(Double.NEGATIVE_INFINITY);
      var3.getChildren().add(var4);
      String[] var5 = var0.split("<split>");
      UITextArea var6 = new UITextArea(var5[0]);
      var6.setId("detail-panel-msg0");
      var6.setPrefWidth(-1.0);
      var3.getChildren().add(var6);
      var1.setTop(var3);

      for(int var7 = 1; var7 + 1 < var5.length; var7 += 2) {
         Label var8 = new Label();
         var8.getStyleClass().add("multiline-text");
         var8.setWrapText(true);
         var8.setText(var5[var7 + 1]);
         Tab var9 = new Tab();
         var9.setText(var5[var7]);
         ScrollPane var10 = new ScrollPane();
         var10.setContent(var8);
         var10.setFitToWidth(true);
         var9.setContent(var10);
         var2.getTabs().add(var9);
      }

      return var1;
   }

   private static Stage beforeDialog(Stage var0) {
      return var0;
   }

   private static void afterDialog() {
   }

   public static String[] addDetail(String[] var0, AppInfo var1, boolean var2, boolean var3) {
      String var4 = var1.getTitle();
      if (var4 == null) {
         var4 = "";
      }

      ArrayList var5 = new ArrayList();

      for(int var6 = 0; var6 < var0.length; ++var6) {
         var5.add(var0[var6]);
      }

      if (var2) {
         String var7 = null;
         if (var1.getDesktopHint() && var1.getMenuHint()) {
            var7 = ResourceManager.getString("install.windows.both.message");
         } else if (var1.getDesktopHint()) {
            var7 = ResourceManager.getString("install.desktop.message");
         } else if (var1.getMenuHint()) {
            var7 = ResourceManager.getString("install.windows.menu.message");
         }

         if (var7 != null) {
            var5.add(var7);
         }
      }

      return (String[])((String[])var5.toArray(var0));
   }

   public static int showSSVDialog0(Object var0, final AppInfo var1, final String var2, final String var3, final String var4, final String var5, final URL var6, final String var7, final String var8, final String var9, final String var10, final String var11) {
      final Stage var12 = beforeDialog((Stage)var0);

      byte var14;
      try {
         int var13 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1x = new DialogTemplate(var1, var12, var2, var3);
               var1x.setSSVContent(var4, var5, var6, var7, var8, var9, var10, var11);
               var1x.getDialog().centerOnScreen();
               var1x.setVisible(true);
               int var2x = var1x.getUserAnswer();
               return new Integer(var2x);
            }
         });
         return var13;
      } catch (Throwable var18) {
         var14 = -1;
      } finally {
         afterDialog();
      }

      return var14;
   }

   public static void placeWindow(Window var0) {
      var0.centerOnScreen();
   }

   private static Object invokeLater(final Runnable var0, Integer var1) throws Exception {
      return var0 != null ? (Integer)DeploySysRun.executePrivileged(new DeploySysAction() {
         public Object execute() {
            Platform.runLater(var0);
            return null;
         }
      }, new Integer(-1)) : -1;
   }

   public int showSSV3Dialog(final Object var1, final AppInfo var2, final int var3, final String var4, final String var5, final String var6, final String var7, final String var8, final String var9, final String var10, final String var11, final String var12, final String var13, final String var14, final URL var15) {
      try {
         Class var16 = Class.forName("com.sun.deploy.config.Config", false, Thread.currentThread().getContextClassLoader());
         Method var17 = var16.getDeclaredMethod("getStringProperty", String.class);
         Object var18 = var17.invoke(var16, "deployment.sqe.automation.run");
         if (var18 instanceof String && "true".equals((String)var18)) {
            return 0;
         }
      } catch (Exception var23) {
         Trace.ignoredException(var23);
      }

      Stage var26 = beforeDialog((Stage)var1);

      byte var28;
      try {
         int var27 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               return FXSSV3Dialog.showSSV3Dialog(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15);
            }
         });
         return var27;
      } catch (Throwable var24) {
         var28 = -1;
      } finally {
         afterDialog();
      }

      return var28;
   }

   public int showPublisherInfo(Object var1, final AppInfo var2, final String var3, final String var4, final String var5, final String var6, final String var7, final String var8) {
      final Stage var9 = beforeDialog((Stage)var1);

      byte var11;
      try {
         int var10 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1 = new DialogTemplate(var2, var9, var3, var4);
               Pane var2x = null;
               if (var8 != null) {
                  var2x = FXUIFactory.getDetailPanel(var8);
               }

               var1.setPublisherInfo(var5, var6, var7, var2x, false);
               var1.getDialog().centerOnScreen();
               var1.setVisible(true);
               int var3x = var1.getUserAnswer();
               return new Integer(var3x);
            }
         });
         return var10;
      } catch (Throwable var15) {
         var11 = -1;
      } finally {
         afterDialog();
      }

      return var11;
   }

   public int showBlockedDialog(Object var1, final AppInfo var2, final String var3, final String var4, final String var5, final String var6, final String var7, final String var8) {
      final Stage var9 = beforeDialog((Stage)var1);

      byte var11;
      try {
         int var10 = (Integer)FXPluginToolkit.callAndWait(new Callable() {
            public Integer call() {
               DialogTemplate var1 = new DialogTemplate(var2, var9, var3, var4);
               Pane var2x = null;
               if (var8 != null) {
                  var2x = FXUIFactory.getDetailPanel(var8);
               }

               String var3x = var2.getBlockedText();
               var1.setBlockedDialogInfo(var5, var3x, var6, var7, var2x, false);
               var1.getDialog().centerOnScreen();
               var1.setVisible(true);
               int var4x = var1.getUserAnswer();
               return new Integer(var4x);
            }
         });
         return var10;
      } catch (Throwable var15) {
         Trace.ignored(var15);
         var11 = -1;
      } finally {
         afterDialog();
      }

      return var11;
   }
}
