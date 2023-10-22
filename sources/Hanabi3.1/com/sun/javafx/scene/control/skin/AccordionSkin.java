package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.AccordionBehavior;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;
import javafx.scene.shape.Rectangle;

public class AccordionSkin extends BehaviorSkinBase {
   private TitledPane firstTitledPane;
   private Rectangle clipRect;
   private boolean forceRelayout = false;
   private boolean relayout = false;
   private double previousHeight = 0.0;
   private TitledPane expandedPane = null;
   private TitledPane previousPane = null;
   private Map listeners = new HashMap();

   public AccordionSkin(Accordion var1) {
      super(var1, new AccordionBehavior(var1));
      var1.getPanes().addListener((var2) -> {
         if (this.firstTitledPane != null) {
            this.firstTitledPane.getStyleClass().remove("first-titled-pane");
         }

         if (!var1.getPanes().isEmpty()) {
            this.firstTitledPane = (TitledPane)var1.getPanes().get(0);
            this.firstTitledPane.getStyleClass().add("first-titled-pane");
         }

         this.getChildren().setAll((Collection)var1.getPanes());

         while(var2.next()) {
            this.removeTitledPaneListeners(var2.getRemoved());
            this.initTitledPaneListeners(var2.getAddedSubList());
         }

         this.forceRelayout = true;
      });
      if (!var1.getPanes().isEmpty()) {
         this.firstTitledPane = (TitledPane)var1.getPanes().get(0);
         this.firstTitledPane.getStyleClass().add("first-titled-pane");
      }

      this.clipRect = new Rectangle(var1.getWidth(), var1.getHeight());
      ((Accordion)this.getSkinnable()).setClip(this.clipRect);
      this.initTitledPaneListeners(var1.getPanes());
      this.getChildren().setAll((Collection)var1.getPanes());
      ((Accordion)this.getSkinnable()).requestLayout();
      this.registerChangeListener(((Accordion)this.getSkinnable()).widthProperty(), "WIDTH");
      this.registerChangeListener(((Accordion)this.getSkinnable()).heightProperty(), "HEIGHT");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("WIDTH".equals(var1)) {
         this.clipRect.setWidth(((Accordion)this.getSkinnable()).getWidth());
      } else if ("HEIGHT".equals(var1)) {
         this.clipRect.setHeight(((Accordion)this.getSkinnable()).getHeight());
         this.relayout = true;
      }

   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      if (this.expandedPane != null) {
         var11 += this.expandedPane.minHeight(var1);
      }

      if (this.previousPane != null && !this.previousPane.equals(this.expandedPane)) {
         var11 += this.previousPane.minHeight(var1);
      }

      Iterator var13 = this.getChildren().iterator();

      while(var13.hasNext()) {
         Node var14 = (Node)var13.next();
         TitledPane var15 = (TitledPane)var14;
         if (!var15.equals(this.expandedPane) && !var15.equals(this.previousPane)) {
            Skin var16 = ((TitledPane)var14).getSkin();
            if (var16 instanceof TitledPaneSkin) {
               TitledPaneSkin var17 = (TitledPaneSkin)var16;
               var11 += var17.getTitleRegionSize(var1);
            } else {
               var11 += var15.minHeight(var1);
            }
         }
      }

      return var11 + var3 + var7;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      if (this.expandedPane != null) {
         var11 += this.expandedPane.prefHeight(var1);
      }

      if (this.previousPane != null && !this.previousPane.equals(this.expandedPane)) {
         var11 += this.previousPane.prefHeight(var1);
      }

      Iterator var13 = this.getChildren().iterator();

      while(var13.hasNext()) {
         Node var14 = (Node)var13.next();
         TitledPane var15 = (TitledPane)var14;
         if (!var15.equals(this.expandedPane) && !var15.equals(this.previousPane)) {
            Skin var16 = ((TitledPane)var14).getSkin();
            if (var16 instanceof TitledPaneSkin) {
               TitledPaneSkin var17 = (TitledPaneSkin)var16;
               var11 += var17.getTitleRegionSize(var1);
            } else {
               var11 += var15.prefHeight(var1);
            }
         }
      }

      return var11 + var3 + var7;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      boolean var9 = this.forceRelayout || this.relayout && this.previousHeight != var7;
      this.forceRelayout = false;
      this.previousHeight = var7;
      double var10 = 0.0;
      Iterator var12 = ((Accordion)this.getSkinnable()).getPanes().iterator();

      while(var12.hasNext()) {
         TitledPane var13 = (TitledPane)var12.next();
         if (!var13.equals(this.expandedPane)) {
            TitledPaneSkin var14 = (TitledPaneSkin)var13.getSkin();
            var10 += this.snapSize(var14.getTitleRegionSize(var5));
         }
      }

      double var24 = var7 - var10;
      Iterator var25 = ((Accordion)this.getSkinnable()).getPanes().iterator();

      while(var25.hasNext()) {
         TitledPane var15 = (TitledPane)var25.next();
         Skin var16 = var15.getSkin();
         double var17;
         if (var16 instanceof TitledPaneSkin) {
            ((TitledPaneSkin)var16).setMaxTitledPaneHeightForAccordion(var24);
            var17 = this.snapSize(((TitledPaneSkin)var16).getTitledPaneHeightForAccordion());
         } else {
            var17 = var15.prefHeight(var5);
         }

         var15.resize(var5, var17);
         boolean var19 = true;
         if (!var9 && this.previousPane != null && this.expandedPane != null) {
            ObservableList var20 = ((Accordion)this.getSkinnable()).getPanes();
            int var21 = var20.indexOf(this.previousPane);
            int var22 = var20.indexOf(this.expandedPane);
            int var23 = var20.indexOf(var15);
            if (var21 < var22) {
               if (var23 <= var22) {
                  var15.relocate(var1, var3);
                  var3 += var17;
                  var19 = false;
               }
            } else if (var21 > var22) {
               if (var23 <= var21) {
                  var15.relocate(var1, var3);
                  var3 += var17;
                  var19 = false;
               }
            } else {
               var15.relocate(var1, var3);
               var3 += var17;
               var19 = false;
            }
         }

         if (var19) {
            var15.relocate(var1, var3);
            var3 += var17;
         }
      }

   }

   private void initTitledPaneListeners(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         TitledPane var3 = (TitledPane)var2.next();
         var3.setExpanded(var3 == ((Accordion)this.getSkinnable()).getExpandedPane());
         if (var3.isExpanded()) {
            this.expandedPane = var3;
         }

         ChangeListener var4 = this.expandedPropertyListener(var3);
         var3.expandedProperty().addListener(var4);
         this.listeners.put(var3, var4);
      }

   }

   private void removeTitledPaneListeners(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         TitledPane var3 = (TitledPane)var2.next();
         if (this.listeners.containsKey(var3)) {
            var3.expandedProperty().removeListener((ChangeListener)this.listeners.get(var3));
            this.listeners.remove(var3);
         }
      }

   }

   private ChangeListener expandedPropertyListener(TitledPane var1) {
      return (var2, var3, var4) -> {
         this.previousPane = this.expandedPane;
         Accordion var5 = (Accordion)this.getSkinnable();
         if (var4) {
            if (this.expandedPane != null) {
               this.expandedPane.setExpanded(false);
            }

            if (var1 != null) {
               var5.setExpandedPane(var1);
            }

            this.expandedPane = var5.getExpandedPane();
         } else {
            this.expandedPane = null;
            var5.setExpandedPane((TitledPane)null);
         }

      };
   }
}
