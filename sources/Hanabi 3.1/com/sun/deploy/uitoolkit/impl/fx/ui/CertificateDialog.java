package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import sun.misc.HexDumpEncoder;
import sun.security.x509.SerialNumber;

public class CertificateDialog {
   public static void showCertificates(Stage var0, Certificate[] var1, int var2, int var3) {
      final FXDialog var4 = new FXDialog(ResourceManager.getMessage("cert.dialog.caption"), var0, true);
      var4.setWidth(800.0);
      var4.setHeight(600.0);
      BorderPane var5 = new BorderPane();
      var4.setContentPane(var5);
      var5.setCenter(getComponents(var0, var1, var2, var3));
      FlowPane var6 = new FlowPane();
      var6.getStyleClass().add("button-bar");
      Button var7 = new Button(ResourceManager.getMessage("cert.dialog.close"));
      var7.setDefaultButton(true);
      var7.setOnAction(new EventHandler() {
         public void handle(ActionEvent var1) {
            var4.hide();
         }
      });
      var6.getChildren().add(var7);
      var5.setBottom(var6);
      var4.show();
   }

   private static Node getComponents(Stage var0, Certificate[] var1, int var2, int var3) {
      SplitPane var4 = new SplitPane();
      if (var1.length > var2 && var1[var2] instanceof X509Certificate) {
         TreeView var5 = buildCertChainTree(var1, var2, var3);
         final TableView var6 = new TableView();
         final TextArea var7 = new TextArea();
         var7.setEditable(false);
         final MultipleSelectionModel var8 = var5.getSelectionModel();
         var8.getSelectedItems().addListener(new ListChangeListener() {
            public void onChanged(ListChangeListener.Change var1) {
               ObservableList var2 = var8.getSelectedItems();
               if (var2 != null && var2.size() == 1) {
                  TreeItem var3 = (TreeItem)var2.get(0);
                  CertificateInfo var4 = (CertificateInfo)var3.getValue();
                  CertificateDialog.showCertificateInfo(var4.getCertificate(), var6, var7);
               }

            }
         });
         TableColumn var9 = new TableColumn();
         var9.setText(ResourceManager.getMessage("cert.dialog.field"));
         var9.setCellValueFactory(new Callback() {
            public ObservableValue call(TableColumn.CellDataFeatures var1) {
               return new ReadOnlyObjectWrapper(((Row)var1.getValue()).field);
            }
         });
         TableColumn var10 = new TableColumn();
         var10.setText(ResourceManager.getMessage("cert.dialog.value"));
         var10.setCellValueFactory(new Callback() {
            public ObservableValue call(TableColumn.CellDataFeatures var1) {
               return new ReadOnlyObjectWrapper(((Row)var1.getValue()).value);
            }
         });
         var6.getColumns().addAll(var9, var10);
         final TableView.TableViewSelectionModel var11 = var6.getSelectionModel();
         var11.setSelectionMode(SelectionMode.SINGLE);
         var11.getSelectedItems().addListener(new ListChangeListener() {
            public void onChanged(ListChangeListener.Change var1) {
               ObservableList var2 = var11.getSelectedItems();
               if (var2 != null && var2.size() == 1) {
                  String var3 = ((Row)var2.get(0)).value;
                  var7.setText(var3);
               }

            }
         });
         var5.setMinWidth(Double.NEGATIVE_INFINITY);
         var5.setMinHeight(Double.NEGATIVE_INFINITY);
         ScrollPane var12 = makeScrollPane(var5);
         var12.setFitToWidth(true);
         var12.setFitToHeight(true);
         var4.getItems().add(var12);
         SplitPane var13 = new SplitPane();
         var13.setOrientation(Orientation.VERTICAL);
         var13.getItems().add(var6);
         var7.setPrefWidth(320.0);
         var7.setPrefHeight(120.0);
         var13.getItems().add(var7);
         var13.setDividerPosition(0, 0.8);
         var4.getItems().add(var13);
         var4.setDividerPosition(0, 0.4);
         var8.select(0);
      }

      return var4;
   }

   private static ScrollPane makeScrollPane(Node var0) {
      ScrollPane var1 = new ScrollPane();
      var1.setContent(var0);
      if (var0 instanceof Label) {
         var1.setFitToWidth(true);
      }

      return var1;
   }

   private static TreeView buildCertChainTree(Certificate[] var0, int var1, int var2) {
      TreeItem var3 = null;
      TreeItem var4 = null;

      for(int var5 = var1; var5 < var0.length && var5 < var2; ++var5) {
         TreeItem var6 = new TreeItem(new CertificateInfo((X509Certificate)var0[var5]));
         if (var3 == null) {
            var3 = var6;
         } else {
            var4.getChildren().add(var6);
         }

         var4 = var6;
      }

      TreeView var7 = new TreeView();
      var7.setShowRoot(true);
      var7.setRoot(var3);
      var7.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      return var7;
   }

   private static void showCertificateInfo(X509Certificate var0, TableView var1, TextArea var2) {
      String var3 = "V" + var0.getVersion();
      String var4 = "[xxxxx-xxxxx]";
      String var5 = null;
      String var6 = null;

      try {
         SerialNumber var7 = new SerialNumber(var0.getSerialNumber());
         var4 = "[" + var7.getNumber() + "]";
         var5 = getCertFingerPrint("MD5", var0);
         var6 = getCertFingerPrint("SHA1", var0);
      } catch (Throwable var14) {
      }

      String var15 = "[" + var0.getSigAlgName() + "]";
      String var8 = formatDNString(var0.getIssuerDN().toString());
      String var9 = "[From: " + var0.getNotBefore() + ",\n To: " + var0.getNotAfter() + "]";
      String var10 = formatDNString(var0.getSubjectDN().toString());
      HexDumpEncoder var11 = new HexDumpEncoder();
      String var12 = var11.encodeBuffer(var0.getSignature());
      ObservableList var13 = FXCollections.observableArrayList((Object[])(new Row(ResourceManager.getMessage("cert.dialog.field.Version"), var3), new Row(ResourceManager.getMessage("cert.dialog.field.SerialNumber"), var4), new Row(ResourceManager.getMessage("cert.dialog.field.SignatureAlg"), var15), new Row(ResourceManager.getMessage("cert.dialog.field.Issuer"), var8), new Row(ResourceManager.getMessage("cert.dialog.field.Validity"), var9), new Row(ResourceManager.getMessage("cert.dialog.field.Subject"), var10), new Row(ResourceManager.getMessage("cert.dialog.field.Signature"), var12), new Row(ResourceManager.getMessage("cert.dialog.field.md5Fingerprint"), var5), new Row(ResourceManager.getMessage("cert.dialog.field.sha1Fingerprint"), var6)));
      var1.setItems(var13);
      var1.getSelectionModel().select(8, (TableColumn)null);
   }

   public static String formatDNString(String var0) {
      int var1 = var0.length();
      boolean var2 = false;
      boolean var3 = false;
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < var1; ++var5) {
         char var6 = var0.charAt(var5);
         if (var6 == '"' || var6 == '\'') {
            var3 = !var3;
         }

         if (var6 == ',' && !var3) {
            var4.append(",\n");
         } else {
            var4.append(var6);
         }
      }

      return var4.toString();
   }

   public static String getCertFingerPrint(String var0, X509Certificate var1) throws Exception {
      byte[] var2 = var1.getEncoded();
      MessageDigest var3 = MessageDigest.getInstance(var0);
      byte[] var4 = var3.digest(var2);
      return toHexString(var4);
   }

   private static void byte2hex(byte var0, StringBuffer var1) {
      char[] var2 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      int var3 = (var0 & 240) >> 4;
      int var4 = var0 & 15;
      var1.append(var2[var3]);
      var1.append(var2[var4]);
   }

   private static String toHexString(byte[] var0) {
      StringBuffer var1 = new StringBuffer();
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         byte2hex(var0[var3], var1);
         if (var3 < var2 - 1) {
            var1.append(":");
         }
      }

      return var1.toString();
   }

   public static class CertificateInfo {
      X509Certificate cert;

      public CertificateInfo(X509Certificate var1) {
         this.cert = var1;
      }

      public X509Certificate getCertificate() {
         return this.cert;
      }

      private String extractAliasName(X509Certificate var1) {
         String var2 = ResourceManager.getMessage("security.dialog.unknown.subject");
         String var3 = ResourceManager.getMessage("security.dialog.unknown.issuer");

         try {
            Principal var4 = var1.getSubjectDN();
            Principal var5 = var1.getIssuerDN();
            String var6 = var4.getName();
            String var7 = var5.getName();
            var2 = this.extractFromQuote(var6, "CN=");
            if (var2 == null) {
               var2 = this.extractFromQuote(var6, "O=");
            }

            if (var2 == null) {
               var2 = ResourceManager.getMessage("security.dialog.unknown.subject");
            }

            var3 = this.extractFromQuote(var7, "CN=");
            if (var3 == null) {
               var3 = this.extractFromQuote(var7, "O=");
            }

            if (var3 == null) {
               var3 = ResourceManager.getMessage("security.dialog.unknown.issuer");
            }
         } catch (Exception var8) {
         }

         MessageFormat var10 = new MessageFormat(ResourceManager.getMessage("security.dialog.certShowName"));
         Object[] var9 = new Object[]{var2, var3};
         return var10.format(var9);
      }

      private String extractFromQuote(String var1, String var2) {
         if (var1 == null) {
            return null;
         } else {
            int var3 = var1.indexOf(var2);
            boolean var4 = false;
            if (var3 >= 0) {
               var3 += var2.length();
               int var5;
               if (var1.charAt(var3) == '"') {
                  ++var3;
                  var5 = var1.indexOf(34, var3);
               } else {
                  var5 = var1.indexOf(44, var3);
               }

               return var5 < 0 ? var1.substring(var3) : var1.substring(var3, var5);
            } else {
               return null;
            }
         }
      }

      public String toString() {
         return this.extractAliasName(this.cert);
      }
   }

   private static class Row {
      public String field;
      public String value;

      Row(String var1, String var2) {
         this.field = var1;
         this.value = var2;
      }
   }
}
