package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TabBuilder implements Builder {
   private int __set;
   private boolean closable;
   private Node content;
   private ContextMenu contextMenu;
   private boolean disable;
   private Node graphic;
   private String id;
   private EventHandler onClosed;
   private EventHandler onSelectionChanged;
   private String style;
   private Collection styleClass;
   private String text;
   private Tooltip tooltip;
   private Object userData;

   protected TabBuilder() {
   }

   public static TabBuilder create() {
      return new TabBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Tab var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setClosable(this.closable);
               break;
            case 1:
               var1.setContent(this.content);
               break;
            case 2:
               var1.setContextMenu(this.contextMenu);
               break;
            case 3:
               var1.setDisable(this.disable);
               break;
            case 4:
               var1.setGraphic(this.graphic);
               break;
            case 5:
               var1.setId(this.id);
               break;
            case 6:
               var1.setOnClosed(this.onClosed);
               break;
            case 7:
               var1.setOnSelectionChanged(this.onSelectionChanged);
               break;
            case 8:
               var1.setStyle(this.style);
               break;
            case 9:
               var1.getStyleClass().addAll(this.styleClass);
               break;
            case 10:
               var1.setText(this.text);
               break;
            case 11:
               var1.setTooltip(this.tooltip);
               break;
            case 12:
               var1.setUserData(this.userData);
         }
      }

   }

   public TabBuilder closable(boolean var1) {
      this.closable = var1;
      this.__set(0);
      return this;
   }

   public TabBuilder content(Node var1) {
      this.content = var1;
      this.__set(1);
      return this;
   }

   public TabBuilder contextMenu(ContextMenu var1) {
      this.contextMenu = var1;
      this.__set(2);
      return this;
   }

   public TabBuilder disable(boolean var1) {
      this.disable = var1;
      this.__set(3);
      return this;
   }

   public TabBuilder graphic(Node var1) {
      this.graphic = var1;
      this.__set(4);
      return this;
   }

   public TabBuilder id(String var1) {
      this.id = var1;
      this.__set(5);
      return this;
   }

   public TabBuilder onClosed(EventHandler var1) {
      this.onClosed = var1;
      this.__set(6);
      return this;
   }

   public TabBuilder onSelectionChanged(EventHandler var1) {
      this.onSelectionChanged = var1;
      this.__set(7);
      return this;
   }

   public TabBuilder style(String var1) {
      this.style = var1;
      this.__set(8);
      return this;
   }

   public TabBuilder styleClass(Collection var1) {
      this.styleClass = var1;
      this.__set(9);
      return this;
   }

   public TabBuilder styleClass(String... var1) {
      return this.styleClass((Collection)Arrays.asList(var1));
   }

   public TabBuilder text(String var1) {
      this.text = var1;
      this.__set(10);
      return this;
   }

   public TabBuilder tooltip(Tooltip var1) {
      this.tooltip = var1;
      this.__set(11);
      return this;
   }

   public TabBuilder userData(Object var1) {
      this.userData = var1;
      this.__set(12);
      return this;
   }

   public Tab build() {
      Tab var1 = new Tab();
      this.applyTo(var1);
      return var1;
   }
}
