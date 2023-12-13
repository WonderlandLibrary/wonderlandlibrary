package com.sun.javafx.scene.control.skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;

public class LabeledImpl extends Label {
   private final Shuttler shuttler;

   public LabeledImpl(Labeled var1) {
      this.shuttler = new Shuttler(this, var1);
   }

   private static void initialize(Shuttler var0, LabeledImpl var1, Labeled var2) {
      var1.setText(var2.getText());
      var2.textProperty().addListener(var0);
      var1.setGraphic(var2.getGraphic());
      var2.graphicProperty().addListener(var0);
      List var3 = LabeledImpl.StyleableProperties.STYLEABLES_TO_MIRROR;
      int var4 = 0;

      for(int var5 = var3.size(); var4 < var5; ++var4) {
         CssMetaData var6 = (CssMetaData)var3.get(var4);
         if (!"-fx-skin".equals(var6.getProperty())) {
            StyleableProperty var7 = var6.getStyleableProperty(var2);
            if (var7 instanceof Observable) {
               ((Observable)var7).addListener(var0);
               StyleOrigin var8 = var7.getStyleOrigin();
               if (var8 != null) {
                  StyleableProperty var9 = var6.getStyleableProperty(var1);
                  var9.applyStyle(var8, var7.getValue());
               }
            }
         }
      }

   }

   static final class StyleableProperties {
      static final List STYLEABLES_TO_MIRROR;

      static {
         List var0 = Labeled.getClassCssMetaData();
         List var1 = Region.getClassCssMetaData();
         ArrayList var2 = new ArrayList(var0);
         var2.removeAll(var1);
         STYLEABLES_TO_MIRROR = Collections.unmodifiableList(var2);
      }
   }

   private static class Shuttler implements InvalidationListener {
      private final LabeledImpl labeledImpl;
      private final Labeled labeled;

      Shuttler(LabeledImpl var1, Labeled var2) {
         this.labeledImpl = var1;
         this.labeled = var2;
         LabeledImpl.initialize(this, var1, var2);
      }

      public void invalidated(Observable var1) {
         if (var1 == this.labeled.textProperty()) {
            this.labeledImpl.setText(this.labeled.getText());
         } else if (var1 == this.labeled.graphicProperty()) {
            StyleOrigin var2 = ((StyleableProperty)this.labeled.graphicProperty()).getStyleOrigin();
            if (var2 == null || var2 == StyleOrigin.USER) {
               this.labeledImpl.setGraphic(this.labeled.getGraphic());
            }
         } else if (var1 instanceof StyleableProperty) {
            StyleableProperty var6 = (StyleableProperty)var1;
            CssMetaData var3 = var6.getCssMetaData();
            if (var3 != null) {
               StyleOrigin var4 = var6.getStyleOrigin();
               StyleableProperty var5 = var3.getStyleableProperty(this.labeledImpl);
               var5.applyStyle(var4, var6.getValue());
            }
         }

      }
   }
}
