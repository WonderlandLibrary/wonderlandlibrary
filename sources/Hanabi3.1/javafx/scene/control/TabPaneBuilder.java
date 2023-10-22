package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TabPaneBuilder extends ControlBuilder implements Builder {
   private int __set;
   private boolean rotateGraphic;
   private SingleSelectionModel selectionModel;
   private Side side;
   private TabPane.TabClosingPolicy tabClosingPolicy;
   private double tabMaxHeight;
   private double tabMaxWidth;
   private double tabMinHeight;
   private double tabMinWidth;
   private Collection tabs;

   protected TabPaneBuilder() {
   }

   public static TabPaneBuilder create() {
      return new TabPaneBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(TabPane var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setRotateGraphic(this.rotateGraphic);
               break;
            case 1:
               var1.setSelectionModel(this.selectionModel);
               break;
            case 2:
               var1.setSide(this.side);
               break;
            case 3:
               var1.setTabClosingPolicy(this.tabClosingPolicy);
               break;
            case 4:
               var1.setTabMaxHeight(this.tabMaxHeight);
               break;
            case 5:
               var1.setTabMaxWidth(this.tabMaxWidth);
               break;
            case 6:
               var1.setTabMinHeight(this.tabMinHeight);
               break;
            case 7:
               var1.setTabMinWidth(this.tabMinWidth);
               break;
            case 8:
               var1.getTabs().addAll(this.tabs);
         }
      }

   }

   public TabPaneBuilder rotateGraphic(boolean var1) {
      this.rotateGraphic = var1;
      this.__set(0);
      return this;
   }

   public TabPaneBuilder selectionModel(SingleSelectionModel var1) {
      this.selectionModel = var1;
      this.__set(1);
      return this;
   }

   public TabPaneBuilder side(Side var1) {
      this.side = var1;
      this.__set(2);
      return this;
   }

   public TabPaneBuilder tabClosingPolicy(TabPane.TabClosingPolicy var1) {
      this.tabClosingPolicy = var1;
      this.__set(3);
      return this;
   }

   public TabPaneBuilder tabMaxHeight(double var1) {
      this.tabMaxHeight = var1;
      this.__set(4);
      return this;
   }

   public TabPaneBuilder tabMaxWidth(double var1) {
      this.tabMaxWidth = var1;
      this.__set(5);
      return this;
   }

   public TabPaneBuilder tabMinHeight(double var1) {
      this.tabMinHeight = var1;
      this.__set(6);
      return this;
   }

   public TabPaneBuilder tabMinWidth(double var1) {
      this.tabMinWidth = var1;
      this.__set(7);
      return this;
   }

   public TabPaneBuilder tabs(Collection var1) {
      this.tabs = var1;
      this.__set(8);
      return this;
   }

   public TabPaneBuilder tabs(Tab... var1) {
      return this.tabs((Collection)Arrays.asList(var1));
   }

   public TabPane build() {
      TabPane var1 = new TabPane();
      this.applyTo(var1);
      return var1;
   }
}
