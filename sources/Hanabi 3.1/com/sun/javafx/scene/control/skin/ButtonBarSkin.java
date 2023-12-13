package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ButtonBarSkin extends BehaviorSkinBase {
   private static final double GAP_SIZE = 10.0;
   private static final String CATEGORIZED_TYPES = "LRHEYNXBIACO";
   public static final String BUTTON_DATA_PROPERTY = "javafx.scene.control.ButtonBar.ButtonData";
   public static final String BUTTON_SIZE_INDEPENDENCE = "javafx.scene.control.ButtonBar.independentSize";
   private static final double DO_NOT_CHANGE_SIZE = Double.MAX_VALUE;
   private HBox layout = new HBox(10.0) {
      protected void layoutChildren() {
         ButtonBarSkin.this.resizeButtons();
         super.layoutChildren();
      }
   };
   private InvalidationListener buttonDataListener = (var1x) -> {
      this.layoutButtons();
   };

   public ButtonBarSkin(ButtonBar var1) {
      super(var1, new BehaviorBase(var1, Collections.emptyList()));
      this.layout.setAlignment(Pos.CENTER);
      this.layout.getStyleClass().add("container");
      this.getChildren().add(this.layout);
      this.layoutButtons();
      this.updateButtonListeners(var1.getButtons(), true);
      var1.getButtons().addListener((var1x) -> {
         while(var1x.next()) {
            this.updateButtonListeners(var1x.getRemoved(), false);
            this.updateButtonListeners(var1x.getAddedSubList(), true);
         }

         this.layoutButtons();
      });
      this.registerChangeListener(var1.buttonOrderProperty(), "BUTTON_ORDER");
      this.registerChangeListener(var1.buttonMinWidthProperty(), "BUTTON_MIN_WIDTH");
   }

   private void updateButtonListeners(List var1, boolean var2) {
      if (var1 != null) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Node var4 = (Node)var3.next();
            ObservableMap var5 = var4.getProperties();
            if (var5.containsKey("javafx.scene.control.ButtonBar.ButtonData")) {
               ObjectProperty var6 = (ObjectProperty)var5.get("javafx.scene.control.ButtonBar.ButtonData");
               if (var6 != null) {
                  if (var2) {
                     var6.addListener(this.buttonDataListener);
                  } else {
                     var6.removeListener(this.buttonDataListener);
                  }
               }
            }
         }
      }

   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("BUTTON_ORDER".equals(var1)) {
         this.layoutButtons();
      } else if ("BUTTON_MIN_WIDTH".equals(var1)) {
         this.resizeButtons();
      }

   }

   private void layoutButtons() {
      ButtonBar var1 = (ButtonBar)this.getSkinnable();
      ObservableList var2 = var1.getButtons();
      double var3 = var1.getButtonMinWidth();
      String var5 = ((ButtonBar)this.getSkinnable()).getButtonOrder();
      this.layout.getChildren().clear();
      if (var5 == null) {
         throw new IllegalStateException("ButtonBar buttonOrder string can not be null");
      } else {
         if (var5 == "") {
            ButtonBarSkin.Spacer.DYNAMIC.add(this.layout, true);
            Iterator var6 = var2.iterator();

            while(var6.hasNext()) {
               Node var7 = (Node)var6.next();
               this.sizeButton(var7, var3, Double.MAX_VALUE, Double.MAX_VALUE);
               this.layout.getChildren().add(var7);
               HBox.setHgrow(var7, Priority.NEVER);
            }
         } else {
            this.doButtonOrderLayout(var5);
         }

      }
   }

   private void doButtonOrderLayout(String var1) {
      ButtonBar var2 = (ButtonBar)this.getSkinnable();
      ObservableList var3 = var2.getButtons();
      double var4 = var2.getButtonMinWidth();
      Map var6 = this.buildButtonMap(var3);
      char[] var7 = var1.toCharArray();
      int var8 = 0;
      Spacer var9 = ButtonBarSkin.Spacer.NONE;

      int var11;
      for(int var10 = 0; var10 < var7.length; ++var10) {
         var11 = var7[var10];
         boolean var12 = var8 <= 0 && var8 >= var3.size() - 1;
         boolean var13 = !this.layout.getChildren().isEmpty();
         if (var11 == 43) {
            var9 = var9.replace(ButtonBarSkin.Spacer.DYNAMIC);
         } else if (var11 == 95 && var13) {
            var9 = var9.replace(ButtonBarSkin.Spacer.FIXED);
         } else {
            List var14 = (List)var6.get(String.valueOf((char)var11).toUpperCase());
            if (var14 != null) {
               var9.add(this.layout, var12);

               for(Iterator var15 = var14.iterator(); var15.hasNext(); ++var8) {
                  Node var16 = (Node)var15.next();
                  this.sizeButton(var16, var4, Double.MAX_VALUE, Double.MAX_VALUE);
                  this.layout.getChildren().add(var16);
                  HBox.setHgrow(var16, Priority.NEVER);
               }

               var9 = var9.replace(ButtonBarSkin.Spacer.NONE);
            }
         }
      }

      boolean var17 = false;
      var11 = var3.size();

      int var18;
      Node var19;
      for(var18 = 0; var18 < var11; ++var18) {
         var19 = (Node)var3.get(var18);
         if (var19 instanceof Button && ((Button)var19).isDefaultButton()) {
            var19.requestFocus();
            var17 = true;
            break;
         }
      }

      if (!var17) {
         for(var18 = 0; var18 < var11; ++var18) {
            var19 = (Node)var3.get(var18);
            ButtonBar.ButtonData var20 = ButtonBar.getButtonData(var19);
            if (var20 != null && var20.isDefaultButton()) {
               var19.requestFocus();
               var17 = true;
               break;
            }
         }
      }

   }

   private void resizeButtons() {
      ButtonBar var1 = (ButtonBar)this.getSkinnable();
      double var2 = var1.getButtonMinWidth();
      ObservableList var4 = var1.getButtons();
      double var5 = var2;
      Iterator var7 = var4.iterator();

      Node var8;
      while(var7.hasNext()) {
         var8 = (Node)var7.next();
         if (ButtonBar.isButtonUniformSize(var8)) {
            var5 = Math.max(var8.prefWidth(-1.0), var5);
         }
      }

      var7 = var4.iterator();

      while(var7.hasNext()) {
         var8 = (Node)var7.next();
         if (ButtonBar.isButtonUniformSize(var8)) {
            this.sizeButton(var8, Double.MAX_VALUE, var5, Double.MAX_VALUE);
         }
      }

   }

   private void sizeButton(Node var1, double var2, double var4, double var6) {
      if (var1 instanceof Region) {
         Region var8 = (Region)var1;
         if (var2 != Double.MAX_VALUE) {
            var8.setMinWidth(var2);
         }

         if (var4 != Double.MAX_VALUE) {
            var8.setPrefWidth(var4);
         }

         if (var6 != Double.MAX_VALUE) {
            var8.setMaxWidth(var6);
         }
      }

   }

   private String getButtonType(Node var1) {
      ButtonBar.ButtonData var2 = ButtonBar.getButtonData(var1);
      if (var2 == null) {
         var2 = ButtonBar.ButtonData.OTHER;
      }

      String var3 = var2.getTypeCode();
      var3 = var3.length() > 0 ? var3.substring(0, 1) : "";
      return "LRHEYNXBIACO".contains(var3.toUpperCase()) ? var3 : ButtonBar.ButtonData.OTHER.getTypeCode();
   }

   private Map buildButtonMap(List var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Node var4 = (Node)var3.next();
         if (var4 != null) {
            String var5 = this.getButtonType(var4);
            Object var6 = (List)var2.get(var5);
            if (var6 == null) {
               var6 = new ArrayList();
               var2.put(var5, var6);
            }

            ((List)var6).add(var4);
         }
      }

      return var2;
   }

   private static enum Spacer {
      FIXED {
         protected Node create(boolean var1) {
            if (var1) {
               return null;
            } else {
               Region var2 = new Region();
               ButtonBar.setButtonData(var2, ButtonBar.ButtonData.SMALL_GAP);
               var2.setMinWidth(10.0);
               HBox.setHgrow(var2, Priority.NEVER);
               return var2;
            }
         }
      },
      DYNAMIC {
         protected Node create(boolean var1) {
            Region var2 = new Region();
            ButtonBar.setButtonData(var2, ButtonBar.ButtonData.BIG_GAP);
            var2.setMinWidth(var1 ? 0.0 : 10.0);
            HBox.setHgrow(var2, Priority.ALWAYS);
            return var2;
         }

         public Spacer replace(Spacer var1) {
            return (Spacer)(FIXED == var1 ? this : var1);
         }
      },
      NONE;

      private Spacer() {
      }

      protected Node create(boolean var1) {
         return null;
      }

      public Spacer replace(Spacer var1) {
         return var1;
      }

      public void add(Pane var1, boolean var2) {
         Node var3 = this.create(var2);
         if (var3 != null) {
            var1.getChildren().add(var3);
         }

      }

      // $FF: synthetic method
      Spacer(Object var3) {
         this();
      }
   }
}
