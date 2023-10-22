package javafx.scene.control;

import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public abstract class SkinBase implements Skin {
   private Control control;
   private ObservableList children;
   private static final EventHandler mouseEventConsumer = (var0) -> {
      var0.consume();
   };

   protected SkinBase(Control var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot pass null for control");
      } else {
         this.control = var1;
         this.children = var1.getControlChildren();
         this.consumeMouseEvents(true);
      }
   }

   public final Control getSkinnable() {
      return this.control;
   }

   public final Node getNode() {
      return this.control;
   }

   public void dispose() {
      this.control = null;
   }

   public final ObservableList getChildren() {
      return this.children;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      int var9 = 0;

      for(int var10 = this.children.size(); var9 < var10; ++var9) {
         Node var11 = (Node)this.children.get(var9);
         if (var11.isManaged()) {
            this.layoutInArea(var11, var1, var3, var5, var7, -1.0, HPos.CENTER, VPos.CENTER);
         }
      }

   }

   protected final void consumeMouseEvents(boolean var1) {
      if (var1) {
         this.control.addEventHandler(MouseEvent.ANY, mouseEventConsumer);
      } else {
         this.control.removeEventHandler(MouseEvent.ANY, mouseEventConsumer);
      }

   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      double var13 = 0.0;
      boolean var15 = true;

      for(int var16 = 0; var16 < this.children.size(); ++var16) {
         Node var17 = (Node)this.children.get(var16);
         if (var17.isManaged()) {
            double var18 = var17.getLayoutBounds().getMinX() + var17.getLayoutX();
            if (!var15) {
               var11 = Math.min(var11, var18);
               var13 = Math.max(var13, var18 + var17.minWidth(-1.0));
            } else {
               var11 = var18;
               var13 = var18 + var17.minWidth(-1.0);
               var15 = false;
            }
         }
      }

      double var20 = var13 - var11;
      return var9 + var20 + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      double var13 = 0.0;
      boolean var15 = true;

      for(int var16 = 0; var16 < this.children.size(); ++var16) {
         Node var17 = (Node)this.children.get(var16);
         if (var17.isManaged()) {
            double var18 = var17.getLayoutBounds().getMinY() + var17.getLayoutY();
            if (!var15) {
               var11 = Math.min(var11, var18);
               var13 = Math.max(var13, var18 + var17.minHeight(-1.0));
            } else {
               var11 = var18;
               var13 = var18 + var17.minHeight(-1.0);
               var15 = false;
            }
         }
      }

      double var20 = var13 - var11;
      return var3 + var20 + var7;
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return Double.MAX_VALUE;
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return Double.MAX_VALUE;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      double var13 = 0.0;
      boolean var15 = true;

      for(int var16 = 0; var16 < this.children.size(); ++var16) {
         Node var17 = (Node)this.children.get(var16);
         if (var17.isManaged()) {
            double var18 = var17.getLayoutBounds().getMinX() + var17.getLayoutX();
            if (!var15) {
               var11 = Math.min(var11, var18);
               var13 = Math.max(var13, var18 + var17.prefWidth(-1.0));
            } else {
               var11 = var18;
               var13 = var18 + var17.prefWidth(-1.0);
               var15 = false;
            }
         }
      }

      return var13 - var11;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = 0.0;
      double var13 = 0.0;
      boolean var15 = true;

      for(int var16 = 0; var16 < this.children.size(); ++var16) {
         Node var17 = (Node)this.children.get(var16);
         if (var17.isManaged()) {
            double var18 = var17.getLayoutBounds().getMinY() + var17.getLayoutY();
            if (!var15) {
               var11 = Math.min(var11, var18);
               var13 = Math.max(var13, var18 + var17.prefHeight(-1.0));
            } else {
               var11 = var18;
               var13 = var18 + var17.prefHeight(-1.0);
               var15 = false;
            }
         }
      }

      return var13 - var11;
   }

   protected double computeBaselineOffset(double var1, double var3, double var5, double var7) {
      int var9 = this.children.size();

      for(int var10 = 0; var10 < var9; ++var10) {
         Node var11 = (Node)this.children.get(var10);
         if (var11.isManaged()) {
            double var12 = var11.getBaselineOffset();
            if (var12 != Double.NEGATIVE_INFINITY) {
               return var11.getLayoutBounds().getMinY() + var11.getLayoutY() + var12;
            }
         }
      }

      return Double.NEGATIVE_INFINITY;
   }

   protected double snappedTopInset() {
      return this.control.snappedTopInset();
   }

   protected double snappedBottomInset() {
      return this.control.snappedBottomInset();
   }

   protected double snappedLeftInset() {
      return this.control.snappedLeftInset();
   }

   protected double snappedRightInset() {
      return this.control.snappedRightInset();
   }

   protected double snapSpace(double var1) {
      return this.control.isSnapToPixel() ? (double)Math.round(var1) : var1;
   }

   protected double snapSize(double var1) {
      return this.control.isSnapToPixel() ? Math.ceil(var1) : var1;
   }

   protected double snapPosition(double var1) {
      return this.control.isSnapToPixel() ? (double)Math.round(var1) : var1;
   }

   protected void positionInArea(Node var1, double var2, double var4, double var6, double var8, double var10, HPos var12, VPos var13) {
      this.positionInArea(var1, var2, var4, var6, var8, var10, Insets.EMPTY, var12, var13);
   }

   protected void positionInArea(Node var1, double var2, double var4, double var6, double var8, double var10, Insets var12, HPos var13, VPos var14) {
      Region.positionInArea(var1, var2, var4, var6, var8, var10, var12, var13, var14, this.control.isSnapToPixel());
   }

   protected void layoutInArea(Node var1, double var2, double var4, double var6, double var8, double var10, HPos var12, VPos var13) {
      this.layoutInArea(var1, var2, var4, var6, var8, var10, Insets.EMPTY, true, true, var12, var13);
   }

   protected void layoutInArea(Node var1, double var2, double var4, double var6, double var8, double var10, Insets var12, HPos var13, VPos var14) {
      this.layoutInArea(var1, var2, var4, var6, var8, var10, var12, true, true, var13, var14);
   }

   protected void layoutInArea(Node var1, double var2, double var4, double var6, double var8, double var10, Insets var12, boolean var13, boolean var14, HPos var15, VPos var16) {
      Region.layoutInArea(var1, var2, var4, var6, var8, var10, var12, var13, var14, var15, var16, this.control.isSnapToPixel());
   }

   public static List getClassCssMetaData() {
      return SkinBase.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public final void pseudoClassStateChanged(PseudoClass var1, boolean var2) {
      Control var3 = this.getSkinnable();
      if (var3 != null) {
         var3.pseudoClassStateChanged(var1, var2);
      }

   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      return null;
   }

   protected void executeAccessibleAction(AccessibleAction var1, Object... var2) {
   }

   private static class StyleableProperties {
      private static final List STYLEABLES = Collections.unmodifiableList(Control.getClassCssMetaData());
   }
}
