package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class FXVKSkin extends BehaviorSkinBase {
   private static final int GAP = 6;
   private List currentBoard;
   private static HashMap boardMap = new HashMap();
   private int numCols;
   private boolean capsDown = false;
   private boolean shiftDown = false;
   private boolean isSymbol = false;
   long lastTime = -1L;
   private static Popup vkPopup;
   private static Popup secondaryPopup;
   private static FXVK primaryVK;
   private static Timeline slideInTimeline = new Timeline();
   private static Timeline slideOutTimeline = new Timeline();
   private static boolean hideAfterSlideOut = false;
   private static FXVK secondaryVK;
   private static Timeline secondaryVKDelay;
   private static CharKey secondaryVKKey;
   private static TextInputKey repeatKey;
   private static Timeline repeatInitialDelay;
   private static Timeline repeatSubsequentDelay;
   private static double KEY_REPEAT_DELAY = 400.0;
   private static double KEY_REPEAT_DELAY_MIN = 100.0;
   private static double KEY_REPEAT_DELAY_MAX = 1000.0;
   private static double KEY_REPEAT_RATE = 25.0;
   private static double KEY_REPEAT_RATE_MIN = 2.0;
   private static double KEY_REPEAT_RATE_MAX = 50.0;
   private Node attachedNode;
   private String vkType = null;
   FXVK fxvk;
   static final double VK_HEIGHT = 243.0;
   static final double VK_SLIDE_MILLIS = 250.0;
   static final double PREF_PORTRAIT_KEY_WIDTH = 40.0;
   static final double PREF_KEY_HEIGHT = 56.0;
   static boolean vkAdjustWindow = false;
   static boolean vkLookup = false;
   private static DoubleProperty winY;
   EventHandler unHideEventHandler;
   private boolean isVKHidden = false;
   private Double origWindowYPos = null;

   void clearShift() {
      if (this.shiftDown && !this.capsDown) {
         this.shiftDown = false;
         this.updateKeys();
      }

      this.lastTime = -1L;
   }

   void pressShift() {
      long var1 = System.currentTimeMillis();
      if (this.shiftDown && !this.capsDown) {
         if (this.lastTime > 0L && var1 - this.lastTime < 400L) {
            this.shiftDown = false;
            this.capsDown = true;
         } else {
            this.shiftDown = false;
            this.capsDown = false;
         }
      } else if (!this.shiftDown && !this.capsDown) {
         this.shiftDown = true;
      } else {
         this.shiftDown = false;
         this.capsDown = false;
      }

      this.updateKeys();
      this.lastTime = var1;
   }

   void clearSymbolABC() {
      this.isSymbol = false;
      this.updateKeys();
   }

   void pressSymbolABC() {
      this.isSymbol = !this.isSymbol;
      this.updateKeys();
   }

   void clearStateKeys() {
      this.capsDown = false;
      this.shiftDown = false;
      this.isSymbol = false;
      this.lastTime = -1L;
      this.updateKeys();
   }

   private void updateKeys() {
      Iterator var1 = this.currentBoard.iterator();

      while(var1.hasNext()) {
         List var2 = (List)var1.next();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Key var4 = (Key)var3.next();
            var4.update(this.capsDown, this.shiftDown, this.isSymbol);
         }
      }

   }

   private static void startSlideIn() {
      slideOutTimeline.stop();
      slideInTimeline.playFromStart();
   }

   private static void startSlideOut(boolean var0) {
      hideAfterSlideOut = var0;
      slideInTimeline.stop();
      slideOutTimeline.playFromStart();
   }

   private void adjustWindowPosition(Node var1) {
      if (var1 instanceof TextInputControl) {
         double var2 = var1.localToScene(0.0, 0.0).getY() + var1.getScene().getY();
         double var4 = ((TextInputControl)var1).getHeight();
         double var6 = var2 + var4;
         double var8 = com.sun.javafx.util.Utils.getScreen(var1).getBounds().getHeight();
         double var10 = var8 - 243.0;
         double var12 = 0.0;
         double var14 = 0.0;
         double var16 = 0.0;
         double var18 = 10.0;
         if (var1 instanceof TextField) {
            var12 = var2 + var4 / 2.0;
            var14 = var6;
            Parent var20 = this.attachedNode.getParent();
            if (var20 instanceof ComboBoxBase) {
               var16 = Math.min(var18 - var2, 0.0);
            } else {
               var16 = Math.min(var10 / 2.0 - var12, 0.0);
            }
         } else if (var1 instanceof TextArea) {
            TextAreaSkin var26 = (TextAreaSkin)((TextArea)var1).getSkin();
            Bounds var21 = var26.getCaretBounds();
            double var22 = var21.getMinY();
            double var24 = var21.getMaxY();
            var12 = var2 + (var22 + var24) / 2.0;
            var14 = var2 + var24;
            if (var4 < var10) {
               var16 = var10 / 2.0 - (var2 + var4 / 2.0);
            } else {
               var16 = var10 / 2.0 - var12;
            }

            var16 = Math.min(var16, 0.0);
         } else {
            var12 = var2 + var4 / 2.0;
            var14 = var6;
            var16 = Math.min(var10 / 2.0 - var12, 0.0);
         }

         Window var27 = var1.getScene().getWindow();
         if (this.origWindowYPos + var14 > var10) {
            var27.setY(var16);
         } else {
            var27.setY(this.origWindowYPos);
         }

      }
   }

   private void saveWindowPosition(Node var1) {
      Window var2 = var1.getScene().getWindow();
      this.origWindowYPos = var2.getY();
   }

   private void restoreWindowPosition(Node var1) {
      if (var1 != null) {
         Scene var2 = var1.getScene();
         if (var2 != null) {
            Window var3 = var2.getWindow();
            if (var3 != null) {
               var3.setY(this.origWindowYPos);
            }
         }
      }

   }

   private void registerUnhideHandler(Node var1) {
      if (this.unHideEventHandler == null) {
         this.unHideEventHandler = (var1x) -> {
            if (this.attachedNode != null && this.isVKHidden) {
               double var2 = com.sun.javafx.util.Utils.getScreen(this.attachedNode).getBounds().getHeight();
               if (this.fxvk.getHeight() > 0.0 && vkPopup.getY() > var2 - this.fxvk.getHeight() && slideInTimeline.getStatus() != Animation.Status.RUNNING) {
                  startSlideIn();
                  if (vkAdjustWindow) {
                     this.adjustWindowPosition(this.attachedNode);
                  }
               }
            }

            this.isVKHidden = false;
         };
      }

      var1.addEventHandler(TouchEvent.TOUCH_PRESSED, this.unHideEventHandler);
      var1.addEventHandler(MouseEvent.MOUSE_PRESSED, this.unHideEventHandler);
   }

   private void unRegisterUnhideHandler(Node var1) {
      if (this.unHideEventHandler != null) {
         var1.removeEventHandler(TouchEvent.TOUCH_PRESSED, this.unHideEventHandler);
         var1.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.unHideEventHandler);
      }

   }

   private String getNodeVKType(Node var1) {
      Object var2 = var1.getProperties().get("vkType");
      String var3 = null;
      if (var2 instanceof String) {
         var3 = ((String)var2).toLowerCase(Locale.ROOT);
      }

      return var3 != null ? var3 : "text";
   }

   private void updateKeyboardType(Node var1) {
      String var2 = this.vkType;
      this.vkType = this.getNodeVKType(var1);
      if (var2 == null || !this.vkType.equals(var2)) {
         this.rebuildPrimaryVK(this.vkType);
      }

   }

   private void closeSecondaryVK() {
      if (secondaryVK != null) {
         secondaryVK.setAttachedNode((Node)null);
         secondaryPopup.hide();
      }

   }

   private void setupPrimaryVK() {
      this.fxvk.setFocusTraversable(false);
      this.fxvk.setVisible(true);
      if (vkPopup == null) {
         vkPopup = new Popup();
         vkPopup.setAutoFix(false);
      }

      vkPopup.getContent().setAll((Object[])(this.fxvk));
      double var1 = com.sun.javafx.util.Utils.getScreen(this.fxvk).getBounds().getHeight();
      double var3 = com.sun.javafx.util.Utils.getScreen(this.fxvk).getBounds().getWidth();
      slideInTimeline.getKeyFrames().setAll((Object[])(new KeyFrame(Duration.millis(250.0), new KeyValue[]{new KeyValue(winY, var1 - 243.0, Interpolator.EASE_BOTH)})));
      slideOutTimeline.getKeyFrames().setAll((Object[])(new KeyFrame(Duration.millis(250.0), (var0) -> {
         if (hideAfterSlideOut && vkPopup.isShowing()) {
            vkPopup.hide();
         }

      }, new KeyValue[]{new KeyValue(winY, var1, Interpolator.EASE_BOTH)})));
      this.fxvk.setPrefWidth(var3);
      this.fxvk.setMinWidth(Double.NEGATIVE_INFINITY);
      this.fxvk.setMaxWidth(Double.NEGATIVE_INFINITY);
      this.fxvk.setPrefHeight(243.0);
      this.fxvk.setMinHeight(Double.NEGATIVE_INFINITY);
      if (secondaryVKDelay == null) {
         secondaryVKDelay = new Timeline();
      }

      KeyFrame var5 = new KeyFrame(Duration.millis(500.0), (var1x) -> {
         if (secondaryVKKey != null) {
            this.showSecondaryVK(secondaryVKKey);
         }

      }, new KeyValue[0]);
      secondaryVKDelay.getKeyFrames().setAll((Object[])(var5));
      if (KEY_REPEAT_RATE > 0.0) {
         repeatInitialDelay = new Timeline(new KeyFrame[]{new KeyFrame(Duration.millis(KEY_REPEAT_DELAY), (var0) -> {
            repeatKey.sendKeyEvents();
            repeatSubsequentDelay.playFromStart();
         }, new KeyValue[0])});
         repeatSubsequentDelay = new Timeline(new KeyFrame[]{new KeyFrame(Duration.millis(1000.0 / KEY_REPEAT_RATE), (var0) -> {
            repeatKey.sendKeyEvents();
         }, new KeyValue[0])});
         repeatSubsequentDelay.setCycleCount(-1);
      }

   }

   void prerender(Node var1) {
      if (this.fxvk == primaryVK) {
         this.loadBoard("text");
         this.loadBoard("numeric");
         this.loadBoard("url");
         this.loadBoard("email");
         this.updateKeyboardType(var1);
         this.fxvk.setVisible(true);
         if (!vkPopup.isShowing()) {
            Rectangle2D var2 = com.sun.javafx.util.Utils.getScreen(var1).getBounds();
            vkPopup.setX((var2.getWidth() - this.fxvk.prefWidth(-1.0)) / 2.0);
            winY.set(var2.getHeight());
            vkPopup.show(var1.getScene().getWindow());
         }

      }
   }

   public FXVKSkin(final FXVK var1) {
      super(var1, new BehaviorBase(var1, Collections.emptyList()));
      this.fxvk = var1;
      if (var1 == FXVK.vk) {
         primaryVK = var1;
      }

      if (var1 == primaryVK) {
         this.setupPrimaryVK();
      }

      var1.attachedNodeProperty().addListener(new InvalidationListener() {
         public void invalidated(Observable var1x) {
            Node var2 = FXVKSkin.this.attachedNode;
            FXVKSkin.this.attachedNode = var1.getAttachedNode();
            if (var1 == FXVKSkin.primaryVK) {
               FXVKSkin.this.closeSecondaryVK();
               if (FXVKSkin.this.attachedNode != null) {
                  if (var2 != null) {
                     FXVKSkin.this.unRegisterUnhideHandler(var2);
                  }

                  FXVKSkin.this.registerUnhideHandler(FXVKSkin.this.attachedNode);
                  FXVKSkin.this.updateKeyboardType(FXVKSkin.this.attachedNode);
                  if ((var2 == null || var2.getScene() == null || var2.getScene().getWindow() != FXVKSkin.this.attachedNode.getScene().getWindow()) && FXVKSkin.vkPopup.isShowing()) {
                     FXVKSkin.vkPopup.hide();
                  }

                  if (!FXVKSkin.vkPopup.isShowing()) {
                     Rectangle2D var3 = com.sun.javafx.util.Utils.getScreen(FXVKSkin.this.attachedNode).getBounds();
                     FXVKSkin.vkPopup.setX((var3.getWidth() - var1.prefWidth(-1.0)) / 2.0);
                     if (var2 != null && !FXVKSkin.this.isVKHidden) {
                        FXVKSkin.winY.set(var3.getHeight() - 243.0);
                     } else {
                        FXVKSkin.winY.set(var3.getHeight());
                     }

                     FXVKSkin.vkPopup.show(FXVKSkin.this.attachedNode.getScene().getWindow());
                  }

                  if (var2 == null || FXVKSkin.this.isVKHidden) {
                     FXVKSkin.startSlideIn();
                  }

                  if (FXVKSkin.vkAdjustWindow) {
                     if (var2 == null || var2.getScene() == null || var2.getScene().getWindow() != FXVKSkin.this.attachedNode.getScene().getWindow()) {
                        FXVKSkin.this.saveWindowPosition(FXVKSkin.this.attachedNode);
                     }

                     FXVKSkin.this.adjustWindowPosition(FXVKSkin.this.attachedNode);
                  }
               } else {
                  if (var2 != null) {
                     FXVKSkin.this.unRegisterUnhideHandler(var2);
                  }

                  FXVKSkin.startSlideOut(true);
                  if (FXVKSkin.vkAdjustWindow) {
                     FXVKSkin.this.restoreWindowPosition(var2);
                  }
               }

               FXVKSkin.this.isVKHidden = false;
            }
         }
      });
   }

   private void rebuildSecondaryVK() {
      if (secondaryVK.chars != null) {
         int var1 = secondaryVK.chars.length;
         int var2 = (int)Math.floor(Math.sqrt((double)Math.max(1, var1 - 2)));
         int var3 = (int)Math.ceil((double)var1 / (double)var2);
         ArrayList var5 = new ArrayList(2);

         for(int var6 = 0; var6 < var2; ++var6) {
            int var7 = var6 * var3;
            int var8 = Math.min(var7 + var3, var1);
            if (var7 >= var8) {
               break;
            }

            ArrayList var9 = new ArrayList(var3);

            for(int var10 = var7; var10 < var8; ++var10) {
               CharKey var4 = new CharKey(secondaryVK.chars[var10], (String)null, (String[])null);
               var4.col = (var10 - var7) * 2;
               var4.colSpan = 2;
               Iterator var11 = var4.getStyleClass().iterator();

               while(var11.hasNext()) {
                  String var12 = (String)var11.next();
                  var4.text.getStyleClass().add(var12 + "-text");
                  var4.altText.getStyleClass().add(var12 + "-alttext");
                  var4.icon.getStyleClass().add(var12 + "-icon");
               }

               if (secondaryVK.chars[var10] != null && secondaryVK.chars[var10].length() > 1) {
                  var4.text.getStyleClass().add("multi-char-text");
               }

               var9.add(var4);
            }

            var5.add(var9);
         }

         this.currentBoard = var5;
         this.getChildren().clear();
         this.numCols = 0;
         Iterator var13 = this.currentBoard.iterator();

         while(var13.hasNext()) {
            List var14 = (List)var13.next();

            Key var16;
            for(Iterator var15 = var14.iterator(); var15.hasNext(); this.numCols = Math.max(this.numCols, var16.col + var16.colSpan)) {
               var16 = (Key)var15.next();
            }

            this.getChildren().addAll(var14);
         }
      }

   }

   private void rebuildPrimaryVK(String var1) {
      this.currentBoard = this.loadBoard(var1);
      this.clearStateKeys();
      this.getChildren().clear();
      this.numCols = 0;
      Iterator var2 = this.currentBoard.iterator();

      while(var2.hasNext()) {
         List var3 = (List)var2.next();

         Key var5;
         for(Iterator var4 = var3.iterator(); var4.hasNext(); this.numCols = Math.max(this.numCols, var5.col + var5.colSpan)) {
            var5 = (Key)var4.next();
         }

         this.getChildren().addAll(var3);
      }

   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      return var9 + (double)(56 * this.numCols) + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      return var3 + 400.0 + var7;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      int var9 = this.currentBoard.size();
      double var10 = (var5 - (double)((this.numCols - 1) * 6)) / (double)this.numCols;
      double var12 = (var7 - (double)((var9 - 1) * 6)) / (double)var9;
      double var14 = var3;

      for(Iterator var16 = this.currentBoard.iterator(); var16.hasNext(); var14 += var12 + 6.0) {
         List var17 = (List)var16.next();
         Iterator var18 = var17.iterator();

         while(var18.hasNext()) {
            Key var19 = (Key)var18.next();
            double var20 = var1 + (double)var19.col * (var10 + 6.0);
            double var22 = (double)var19.colSpan * (var10 + 6.0) - 6.0;
            var19.resizeRelocate((double)((int)(var20 + 0.5)), (double)((int)(var14 + 0.5)), var22, var12);
         }
      }

   }

   private void showSecondaryVK(CharKey var1) {
      if (var1 != null) {
         Node var2 = primaryVK.getAttachedNode();
         if (secondaryVK == null) {
            secondaryVK = new FXVK();
            secondaryVK.setSkin(new FXVKSkin(secondaryVK));
            secondaryVK.getStyleClass().setAll((Object[])("fxvk-secondary"));
            secondaryPopup = new Popup();
            secondaryPopup.setAutoHide(true);
            secondaryPopup.getContent().add(secondaryVK);
         }

         secondaryVK.chars = null;
         ArrayList var3 = new ArrayList();
         if (!this.isSymbol && var1.letterChars != null && var1.letterChars.length() > 0) {
            if (!this.shiftDown && !this.capsDown) {
               var3.add(var1.letterChars);
            } else {
               var3.add(var1.letterChars.toUpperCase());
            }
         }

         if (var1.altChars != null && var1.altChars.length() > 0) {
            if (!this.shiftDown && !this.capsDown) {
               var3.add(var1.altChars);
            } else {
               var3.add(var1.altChars.toUpperCase());
            }
         }

         if (var1.moreChars != null && var1.moreChars.length > 0) {
            String[] var4;
            int var5;
            int var6;
            String var7;
            if (this.isSymbol) {
               var4 = var1.moreChars;
               var5 = var4.length;

               for(var6 = 0; var6 < var5; ++var6) {
                  var7 = var4[var6];
                  if (!Character.isLetter(var7.charAt(0))) {
                     var3.add(var7);
                  }
               }
            } else {
               var4 = var1.moreChars;
               var5 = var4.length;

               for(var6 = 0; var6 < var5; ++var6) {
                  var7 = var4[var6];
                  if (Character.isLetter(var7.charAt(0))) {
                     if (!this.shiftDown && !this.capsDown) {
                        var3.add(var7);
                     } else {
                        var3.add(var7.toUpperCase());
                     }
                  }
               }
            }
         }

         boolean var14 = false;
         Iterator var15 = var3.iterator();

         while(var15.hasNext()) {
            String var17 = (String)var15.next();
            if (var17.length() > 1) {
               var14 = true;
            }
         }

         secondaryVK.chars = (String[])var3.toArray(new String[var3.size()]);
         if (secondaryVK.chars.length > 1) {
            if (secondaryVK.getSkin() != null) {
               ((FXVKSkin)secondaryVK.getSkin()).rebuildSecondaryVK();
            }

            secondaryVK.setAttachedNode(var2);
            FXVKSkin var16 = (FXVKSkin)primaryVK.getSkin();
            FXVKSkin var18 = (FXVKSkin)secondaryVK.getSkin();
            int var19 = secondaryVK.chars.length;
            int var8 = (int)Math.floor(Math.sqrt((double)Math.max(1, var19 - 2)));
            int var9 = (int)Math.ceil((double)var19 / (double)var8);
            double var10 = this.snappedLeftInset() + this.snappedRightInset() + (double)var9 * 40.0 * (double)(var14 ? 2 : 1) + (double)((var9 - 1) * 6);
            double var12 = this.snappedTopInset() + this.snappedBottomInset() + (double)var8 * 56.0 + (double)((var8 - 1) * 6);
            secondaryVK.setPrefWidth(var10);
            secondaryVK.setMinWidth(Double.NEGATIVE_INFINITY);
            secondaryVK.setPrefHeight(var12);
            secondaryVK.setMinHeight(Double.NEGATIVE_INFINITY);
            Platform.runLater(() -> {
               Point2D var5 = com.sun.javafx.util.Utils.pointRelativeTo(var1, var10, var12, HPos.CENTER, VPos.TOP, 5.0, -3.0, true);
               double var6 = var5.getX();
               double var8 = var5.getY();
               Scene var10x = var1.getScene();
               var6 = Math.min(var6, var10x.getWindow().getX() + var10x.getWidth() - var10);
               secondaryPopup.show(var1.getScene().getWindow(), var6, var8);
            });
         }
      } else {
         this.closeSecondaryVK();
      }

   }

   private List loadBoard(String var1) {
      List var2 = (List)boardMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         String var3 = var1.substring(0, 1).toUpperCase() + var1.substring(1).toLowerCase() + "Board.txt";

         try {
            ArrayList var23 = new ArrayList(5);
            ArrayList var4 = new ArrayList(20);
            InputStream var5 = FXVKSkin.class.getResourceAsStream(var3);
            BufferedReader var6 = new BufferedReader(new InputStreamReader(var5));
            int var8 = 0;
            int var9 = 0;
            boolean var10 = true;
            boolean var11 = false;
            ArrayList var12 = new ArrayList(10);

            while(true) {
               String var7;
               do {
                  do {
                     if ((var7 = var6.readLine()) == null) {
                        var6.close();
                        boardMap.put(var1, var23);
                        return var23;
                     }
                  } while(var7.length() == 0);
               } while(var7.charAt(0) == '#');

               for(int var13 = 0; var13 < var7.length(); ++var13) {
                  char var14 = var7.charAt(var13);
                  if (var14 == ' ') {
                     ++var8;
                  } else if (var14 == '[') {
                     var9 = var8;
                     var12 = new ArrayList(10);
                     var11 = false;
                  } else if (var14 == ']') {
                     String var25 = "";
                     String var26 = null;
                     String[] var27 = null;

                     int var18;
                     for(var18 = 0; var18 < var12.size(); ++var18) {
                        var12.set(var18, FXVKCharEntities.get((String)var12.get(var18)));
                     }

                     var18 = var12.size();
                     if (var18 > 0) {
                        var25 = (String)var12.get(0);
                        if (var18 > 1) {
                           var26 = (String)var12.get(1);
                           if (var18 > 2) {
                              var27 = (String[])var12.subList(2, var18).toArray(new String[var18 - 2]);
                           }
                        }
                     }

                     int var24 = var8 - var9;
                     Object var19;
                     if (var11) {
                        if ("$shift".equals(var25)) {
                           var19 = new KeyboardStateKey("", (String)null, "shift") {
                              protected void release() {
                                 FXVKSkin.this.pressShift();
                              }

                              public void update(boolean var1, boolean var2, boolean var3) {
                                 if (var3) {
                                    this.setDisable(true);
                                    this.setVisible(false);
                                 } else {
                                    if (var1) {
                                       this.icon.getStyleClass().remove("shift-icon");
                                       this.icon.getStyleClass().add("capslock-icon");
                                    } else {
                                       this.icon.getStyleClass().remove("capslock-icon");
                                       this.icon.getStyleClass().add("shift-icon");
                                    }

                                    this.setDisable(false);
                                    this.setVisible(true);
                                 }

                              }
                           };
                           ((Key)var19).getStyleClass().add("shift");
                        } else if ("$SymbolABC".equals(var25)) {
                           var19 = new KeyboardStateKey("!#123", "ABC", "symbol") {
                              protected void release() {
                                 FXVKSkin.this.pressSymbolABC();
                              }
                           };
                        } else if ("$backspace".equals(var25)) {
                           var19 = new KeyCodeKey("backspace", "\b", KeyCode.BACK_SPACE) {
                              protected void press() {
                                 if (FXVKSkin.KEY_REPEAT_RATE > 0.0) {
                                    FXVKSkin.this.clearShift();
                                    this.sendKeyEvents();
                                    FXVKSkin.repeatKey = this;
                                    FXVKSkin.repeatInitialDelay.playFromStart();
                                 } else {
                                    super.press();
                                 }

                              }

                              protected void release() {
                                 if (FXVKSkin.KEY_REPEAT_RATE > 0.0) {
                                    FXVKSkin.repeatInitialDelay.stop();
                                    FXVKSkin.repeatSubsequentDelay.stop();
                                 } else {
                                    super.release();
                                 }

                              }
                           };
                           ((Key)var19).getStyleClass().add("backspace");
                        } else if ("$enter".equals(var25)) {
                           var19 = new KeyCodeKey("enter", "\n", KeyCode.ENTER);
                           ((Key)var19).getStyleClass().add("enter");
                        } else if ("$tab".equals(var25)) {
                           var19 = new KeyCodeKey("tab", "\t", KeyCode.TAB);
                        } else if ("$space".equals(var25)) {
                           var19 = new CharKey(" ", " ", (String[])null, "space");
                        } else if ("$clear".equals(var25)) {
                           var19 = new SuperKey("clear", "");
                        } else if ("$.org".equals(var25)) {
                           var19 = new SuperKey(".org", ".org");
                        } else if ("$.com".equals(var25)) {
                           var19 = new SuperKey(".com", ".com");
                        } else if ("$.net".equals(var25)) {
                           var19 = new SuperKey(".net", ".net");
                        } else if ("$oracle.com".equals(var25)) {
                           var19 = new SuperKey("oracle.com", "oracle.com");
                        } else if ("$gmail.com".equals(var25)) {
                           var19 = new SuperKey("gmail.com", "gmail.com");
                        } else if ("$hide".equals(var25)) {
                           var19 = new KeyboardStateKey("hide", (String)null, "hide") {
                              protected void release() {
                                 FXVKSkin.this.isVKHidden = true;
                                 FXVKSkin.startSlideOut(false);
                                 if (FXVKSkin.vkAdjustWindow) {
                                    FXVKSkin.this.restoreWindowPosition(FXVKSkin.this.attachedNode);
                                 }

                              }
                           };
                           ((Key)var19).getStyleClass().add("hide");
                        } else if ("$undo".equals(var25)) {
                           var19 = new SuperKey("undo", "");
                        } else if ("$redo".equals(var25)) {
                           var19 = new SuperKey("redo", "");
                        } else {
                           var19 = null;
                        }
                     } else {
                        var19 = new CharKey(var25, var26, var27);
                     }

                     if (var19 != null) {
                        ((Key)var19).col = var9;
                        ((Key)var19).colSpan = var24;
                        Iterator var20 = ((Key)var19).getStyleClass().iterator();

                        while(var20.hasNext()) {
                           String var21 = (String)var20.next();
                           ((Key)var19).text.getStyleClass().add(var21 + "-text");
                           ((Key)var19).altText.getStyleClass().add(var21 + "-alttext");
                           ((Key)var19).icon.getStyleClass().add(var21 + "-icon");
                        }

                        if (var25 != null && var25.length() > 1) {
                           ((Key)var19).text.getStyleClass().add("multi-char-text");
                        }

                        if (var26 != null && var26.length() > 1) {
                           ((Key)var19).altText.getStyleClass().add("multi-char-text");
                        }

                        var4.add(var19);
                     }
                  } else {
                     for(int var15 = var13; var15 < var7.length(); ++var15) {
                        char var16 = var7.charAt(var15);
                        boolean var17 = false;
                        if (var16 == '\\') {
                           ++var15;
                           ++var13;
                           var17 = true;
                           var16 = var7.charAt(var15);
                        }

                        if (var16 == '$' && !var17) {
                           var11 = true;
                        }

                        if (var16 == '|' && !var17) {
                           var12.add(var7.substring(var13, var15));
                           var13 = var15 + 1;
                        } else if ((var16 == ']' || var16 == ' ') && !var17) {
                           var12.add(var7.substring(var13, var15));
                           var13 = var15 - 1;
                           break;
                        }
                     }

                     ++var8;
                  }
               }

               var8 = 0;
               var9 = 0;
               var23.add(var4);
               var4 = new ArrayList(20);
            }
         } catch (Exception var22) {
            var22.printStackTrace();
            return Collections.emptyList();
         }
      }
   }

   static {
      AccessController.doPrivileged(() -> {
         String var0 = System.getProperty("com.sun.javafx.vk.adjustwindow");
         if (var0 != null) {
            vkAdjustWindow = Boolean.valueOf(var0);
         }

         var0 = System.getProperty("com.sun.javafx.sqe.vk.lookup");
         if (var0 != null) {
            vkLookup = Boolean.valueOf(var0);
         }

         var0 = System.getProperty("com.sun.javafx.virtualKeyboard.backspaceRepeatDelay");
         Double var1;
         if (var0 != null) {
            var1 = Double.valueOf(var0);
            KEY_REPEAT_DELAY = Math.min(Math.max(var1, KEY_REPEAT_DELAY_MIN), KEY_REPEAT_DELAY_MAX);
         }

         var0 = System.getProperty("com.sun.javafx.virtualKeyboard.backspaceRepeatRate");
         if (var0 != null) {
            var1 = Double.valueOf(var0);
            if (var1 <= 0.0) {
               KEY_REPEAT_RATE = 0.0;
            } else {
               KEY_REPEAT_RATE = Math.min(Math.max(var1, KEY_REPEAT_RATE_MIN), KEY_REPEAT_RATE_MAX);
            }
         }

         return null;
      });
      winY = new SimpleDoubleProperty();
      winY.addListener((var0) -> {
         if (vkPopup != null) {
            vkPopup.setY(winY.get());
         }

      });
   }

   private class KeyboardStateKey extends Key {
      private final String defaultText;
      private final String toggledText;

      private KeyboardStateKey(String var2, String var3, String var4) {
         super();
         this.defaultText = var2;
         this.toggledText = var3;
         this.text.setText(this.defaultText);
         if (FXVKSkin.vkLookup && var4 != null) {
            this.setId(var4);
         }

         this.getStyleClass().add("special");
      }

      public void update(boolean var1, boolean var2, boolean var3) {
         if (var3) {
            this.text.setText(this.toggledText);
         } else {
            this.text.setText(this.defaultText);
         }

      }

      // $FF: synthetic method
      KeyboardStateKey(String var2, String var3, String var4, Object var5) {
         this(var2, var3, var4);
      }
   }

   private class KeyCodeKey extends SuperKey {
      private KeyCode code;

      private KeyCodeKey(String var2, String var3, KeyCode var4) {
         super(var2, var3, null);
         this.code = var4;
         if (FXVKSkin.vkLookup) {
            this.setId(var2);
         }

      }

      protected void sendKeyEvents() {
         Node var1 = FXVKSkin.this.fxvk.getAttachedNode();
         if (var1 instanceof EventTarget) {
            var1.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.CHAR_UNDEFINED, this.chars, this.code, FXVKSkin.this.shiftDown, false, false, false));
            var1.fireEvent(new KeyEvent(KeyEvent.KEY_TYPED, this.chars, "", KeyCode.UNDEFINED, FXVKSkin.this.shiftDown, false, false, false));
            var1.fireEvent(new KeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.CHAR_UNDEFINED, this.chars, this.code, FXVKSkin.this.shiftDown, false, false, false));
         }

      }

      // $FF: synthetic method
      KeyCodeKey(String var2, String var3, KeyCode var4, Object var5) {
         this(var2, var3, var4);
      }
   }

   private class SuperKey extends TextInputKey {
      private SuperKey(String var2, String var3) {
         super(null);
         this.chars = var3;
         this.text.setText(var2);
         this.getStyleClass().add("special");
         if (FXVKSkin.vkLookup) {
            this.setId(var2);
         }

      }

      // $FF: synthetic method
      SuperKey(String var2, String var3, Object var4) {
         this(var2, var3);
      }
   }

   private class CharKey extends TextInputKey {
      private final String letterChars;
      private final String altChars;
      private final String[] moreChars;

      private CharKey(String var2, String var3, String[] var4, String var5) {
         super(null);
         this.letterChars = var2;
         this.altChars = var3;
         this.moreChars = var4;
         this.chars = this.letterChars;
         this.text.setText(this.chars);
         this.altText.setText(this.altChars);
         if (FXVKSkin.vkLookup) {
            this.setId((var5 != null ? var5 : this.chars).replaceAll("\\.", ""));
         }

      }

      private CharKey(String var2, String var3, String[] var4) {
         this(var2, var3, var4, (String)null);
      }

      protected void press() {
         super.press();
         if (!this.letterChars.equals(this.altChars) || this.moreChars != null) {
            if (FXVKSkin.this.fxvk == FXVKSkin.primaryVK) {
               FXVKSkin.this.showSecondaryVK((CharKey)null);
               FXVKSkin.secondaryVKKey = this;
               FXVKSkin.secondaryVKDelay.playFromStart();
            }

         }
      }

      protected void release() {
         super.release();
         if (!this.letterChars.equals(this.altChars) || this.moreChars != null) {
            if (FXVKSkin.this.fxvk == FXVKSkin.primaryVK) {
               FXVKSkin.secondaryVKDelay.stop();
            }

         }
      }

      public void update(boolean var1, boolean var2, boolean var3) {
         if (var3) {
            this.chars = this.altChars;
            this.text.setText(this.chars);
            if (this.moreChars != null && this.moreChars.length > 0 && !Character.isLetter(this.moreChars[0].charAt(0))) {
               this.altText.setText(this.moreChars[0]);
            } else {
               this.altText.setText((String)null);
            }
         } else {
            this.chars = !var1 && !var2 ? this.letterChars.toLowerCase() : this.letterChars.toUpperCase();
            this.text.setText(this.chars);
            this.altText.setText(this.altChars);
         }

      }

      // $FF: synthetic method
      CharKey(String var2, String var3, String[] var4, Object var5) {
         this(var2, var3, var4);
      }

      // $FF: synthetic method
      CharKey(String var2, String var3, String[] var4, String var5, Object var6) {
         this(var2, var3, var4, (String)var5);
      }
   }

   private class TextInputKey extends Key {
      String chars;

      private TextInputKey() {
         super();
         this.chars = "";
      }

      protected void press() {
      }

      protected void release() {
         if (FXVKSkin.this.fxvk == FXVKSkin.secondaryVK || FXVKSkin.secondaryPopup == null || !FXVKSkin.secondaryPopup.isShowing()) {
            this.sendKeyEvents();
            if (FXVKSkin.this.fxvk == FXVKSkin.secondaryVK) {
               FXVKSkin.this.showSecondaryVK((CharKey)null);
            }

            super.release();
         }
      }

      protected void sendKeyEvents() {
         Node var1 = FXVKSkin.this.fxvk.getAttachedNode();
         if (var1 instanceof EventTarget && this.chars != null) {
            var1.fireEvent(new KeyEvent(KeyEvent.KEY_TYPED, this.chars, "", KeyCode.UNDEFINED, FXVKSkin.this.shiftDown, false, false, false));
         }

      }

      // $FF: synthetic method
      TextInputKey(Object var2) {
         this();
      }
   }

   private class Key extends Region {
      int col = 0;
      int colSpan = 1;
      protected final Text text = new Text();
      protected final Text altText;
      protected final Region icon = new Region();

      protected Key() {
         this.text.setTextOrigin(VPos.TOP);
         this.altText = new Text();
         this.altText.setTextOrigin(VPos.TOP);
         this.getChildren().setAll((Object[])(this.text, this.altText, this.icon));
         this.getStyleClass().setAll((Object[])("key"));
         this.addEventHandler(MouseEvent.MOUSE_PRESSED, (var1x) -> {
            if (var1x.getButton() == MouseButton.PRIMARY) {
               this.press();
            }

         });
         this.addEventHandler(MouseEvent.MOUSE_RELEASED, (var1x) -> {
            if (var1x.getButton() == MouseButton.PRIMARY) {
               this.release();
            }

         });
      }

      protected void press() {
      }

      protected void release() {
         FXVKSkin.this.clearShift();
      }

      public void update(boolean var1, boolean var2, boolean var3) {
      }

      protected void layoutChildren() {
         double var1 = this.snappedLeftInset();
         double var3 = this.snappedTopInset();
         double var5 = this.getWidth() - var1 - this.snappedRightInset();
         double var7 = this.getHeight() - var3 - this.snappedBottomInset();
         this.text.setVisible(this.icon.getBackground() == null);
         double var9 = this.text.prefWidth(-1.0);
         double var11 = this.text.prefHeight(-1.0);
         this.text.resizeRelocate((double)((int)(var1 + (var5 - var9) / 2.0 + 0.5)), (double)((int)(var3 + (var7 - var11) / 2.0 + 0.5)), (double)((int)var9), (double)((int)var11));
         this.altText.setVisible(this.icon.getBackground() == null && this.altText.getText().length() > 0);
         var9 = this.altText.prefWidth(-1.0);
         var11 = this.altText.prefHeight(-1.0);
         this.altText.resizeRelocate((double)((int)var1) + (var5 - var9) + 0.5, (double)((int)(var3 + (var7 - var11) / 2.0 + 0.5 - var7 / 2.0)), (double)((int)var9), (double)((int)var11));
         this.icon.resizeRelocate(var1 - 8.0, var3 - 8.0, var5 + 16.0, var7 + 16.0);
      }
   }
}
