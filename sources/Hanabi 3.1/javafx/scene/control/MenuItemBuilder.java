package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class MenuItemBuilder implements Builder {
   private int __set;
   private KeyCombination accelerator;
   private boolean disable;
   private Node graphic;
   private String id;
   private boolean mnemonicParsing;
   private EventHandler onAction;
   private EventHandler onMenuValidation;
   private String style;
   private Collection styleClass;
   private String text;
   private Object userData;
   private boolean visible;

   protected MenuItemBuilder() {
   }

   public static MenuItemBuilder create() {
      return new MenuItemBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(MenuItem var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setAccelerator(this.accelerator);
               break;
            case 1:
               var1.setDisable(this.disable);
               break;
            case 2:
               var1.setGraphic(this.graphic);
               break;
            case 3:
               var1.setId(this.id);
               break;
            case 4:
               var1.setMnemonicParsing(this.mnemonicParsing);
               break;
            case 5:
               var1.setOnAction(this.onAction);
               break;
            case 6:
               var1.setOnMenuValidation(this.onMenuValidation);
               break;
            case 7:
               var1.setStyle(this.style);
               break;
            case 8:
               var1.getStyleClass().addAll(this.styleClass);
               break;
            case 9:
               var1.setText(this.text);
               break;
            case 10:
               var1.setUserData(this.userData);
               break;
            case 11:
               var1.setVisible(this.visible);
         }
      }

   }

   public MenuItemBuilder accelerator(KeyCombination var1) {
      this.accelerator = var1;
      this.__set(0);
      return this;
   }

   public MenuItemBuilder disable(boolean var1) {
      this.disable = var1;
      this.__set(1);
      return this;
   }

   public MenuItemBuilder graphic(Node var1) {
      this.graphic = var1;
      this.__set(2);
      return this;
   }

   public MenuItemBuilder id(String var1) {
      this.id = var1;
      this.__set(3);
      return this;
   }

   public MenuItemBuilder mnemonicParsing(boolean var1) {
      this.mnemonicParsing = var1;
      this.__set(4);
      return this;
   }

   public MenuItemBuilder onAction(EventHandler var1) {
      this.onAction = var1;
      this.__set(5);
      return this;
   }

   public MenuItemBuilder onMenuValidation(EventHandler var1) {
      this.onMenuValidation = var1;
      this.__set(6);
      return this;
   }

   public MenuItemBuilder style(String var1) {
      this.style = var1;
      this.__set(7);
      return this;
   }

   public MenuItemBuilder styleClass(Collection var1) {
      this.styleClass = var1;
      this.__set(8);
      return this;
   }

   public MenuItemBuilder styleClass(String... var1) {
      return this.styleClass((Collection)Arrays.asList(var1));
   }

   public MenuItemBuilder text(String var1) {
      this.text = var1;
      this.__set(9);
      return this;
   }

   public MenuItemBuilder userData(Object var1) {
      this.userData = var1;
      this.__set(10);
      return this;
   }

   public MenuItemBuilder visible(boolean var1) {
      this.visible = var1;
      this.__set(11);
      return this;
   }

   public MenuItem build() {
      MenuItem var1 = new MenuItem();
      this.applyTo(var1);
      return var1;
   }
}
