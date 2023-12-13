package javafx.stage;

import javafx.event.EventDispatcher;
import javafx.event.EventHandler;

/** @deprecated */
@Deprecated
public abstract class WindowBuilder {
   private int __set;
   private EventDispatcher eventDispatcher;
   private boolean focused;
   private double height;
   private EventHandler onCloseRequest;
   private EventHandler onHidden;
   private EventHandler onHiding;
   private EventHandler onShowing;
   private EventHandler onShown;
   private double opacity;
   private double width;
   private double x;
   private double y;

   protected WindowBuilder() {
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Window var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setEventDispatcher(this.eventDispatcher);
               break;
            case 1:
               var1.setFocused(this.focused);
               break;
            case 2:
               var1.setHeight(this.height);
               break;
            case 3:
               var1.setOnCloseRequest(this.onCloseRequest);
               break;
            case 4:
               var1.setOnHidden(this.onHidden);
               break;
            case 5:
               var1.setOnHiding(this.onHiding);
               break;
            case 6:
               var1.setOnShowing(this.onShowing);
               break;
            case 7:
               var1.setOnShown(this.onShown);
               break;
            case 8:
               var1.setOpacity(this.opacity);
               break;
            case 9:
               var1.setWidth(this.width);
               break;
            case 10:
               var1.setX(this.x);
               break;
            case 11:
               var1.setY(this.y);
         }
      }

   }

   public WindowBuilder eventDispatcher(EventDispatcher var1) {
      this.eventDispatcher = var1;
      this.__set(0);
      return this;
   }

   public WindowBuilder focused(boolean var1) {
      this.focused = var1;
      this.__set(1);
      return this;
   }

   public WindowBuilder height(double var1) {
      this.height = var1;
      this.__set(2);
      return this;
   }

   public WindowBuilder onCloseRequest(EventHandler var1) {
      this.onCloseRequest = var1;
      this.__set(3);
      return this;
   }

   public WindowBuilder onHidden(EventHandler var1) {
      this.onHidden = var1;
      this.__set(4);
      return this;
   }

   public WindowBuilder onHiding(EventHandler var1) {
      this.onHiding = var1;
      this.__set(5);
      return this;
   }

   public WindowBuilder onShowing(EventHandler var1) {
      this.onShowing = var1;
      this.__set(6);
      return this;
   }

   public WindowBuilder onShown(EventHandler var1) {
      this.onShown = var1;
      this.__set(7);
      return this;
   }

   public WindowBuilder opacity(double var1) {
      this.opacity = var1;
      this.__set(8);
      return this;
   }

   public WindowBuilder width(double var1) {
      this.width = var1;
      this.__set(9);
      return this;
   }

   public WindowBuilder x(double var1) {
      this.x = var1;
      this.__set(10);
      return this;
   }

   public WindowBuilder y(double var1) {
      this.y = var1;
      this.__set(11);
      return this;
   }
}
