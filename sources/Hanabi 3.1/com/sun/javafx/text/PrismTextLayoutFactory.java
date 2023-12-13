package com.sun.javafx.text;

import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.scene.text.TextSpan;

public class PrismTextLayoutFactory implements TextLayoutFactory {
   private static final PrismTextLayout reusableTL = new PrismTextLayout();
   private static boolean inUse;
   private static final PrismTextLayoutFactory factory = new PrismTextLayoutFactory();

   private PrismTextLayoutFactory() {
   }

   public TextLayout createLayout() {
      return new PrismTextLayout();
   }

   public TextLayout getLayout() {
      if (inUse) {
         return new PrismTextLayout();
      } else {
         Class var1 = PrismTextLayoutFactory.class;
         synchronized(PrismTextLayoutFactory.class) {
            if (inUse) {
               return new PrismTextLayout();
            } else {
               inUse = true;
               reusableTL.setAlignment(0);
               reusableTL.setWrapWidth(0.0F);
               reusableTL.setDirection(0);
               reusableTL.setContent((TextSpan[])null);
               return reusableTL;
            }
         }
      }
   }

   public void disposeLayout(TextLayout var1) {
      if (var1 == reusableTL) {
         inUse = false;
      }

   }

   public static PrismTextLayoutFactory getFactory() {
      return factory;
   }
}
