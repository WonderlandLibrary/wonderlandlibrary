package javafx.scene.control;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ScrollPaneBuilder extends ControlBuilder implements Builder {
   private int __set;
   private Node content;
   private boolean fitToHeight;
   private boolean fitToWidth;
   private ScrollPane.ScrollBarPolicy hbarPolicy;
   private double hmax;
   private double hmin;
   private double hvalue;
   private boolean pannable;
   private double prefViewportHeight;
   private double prefViewportWidth;
   private ScrollPane.ScrollBarPolicy vbarPolicy;
   private Bounds viewportBounds;
   private double vmax;
   private double vmin;
   private double vvalue;

   protected ScrollPaneBuilder() {
   }

   public static ScrollPaneBuilder create() {
      return new ScrollPaneBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(ScrollPane var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setContent(this.content);
               break;
            case 1:
               var1.setFitToHeight(this.fitToHeight);
               break;
            case 2:
               var1.setFitToWidth(this.fitToWidth);
               break;
            case 3:
               var1.setHbarPolicy(this.hbarPolicy);
               break;
            case 4:
               var1.setHmax(this.hmax);
               break;
            case 5:
               var1.setHmin(this.hmin);
               break;
            case 6:
               var1.setHvalue(this.hvalue);
               break;
            case 7:
               var1.setPannable(this.pannable);
               break;
            case 8:
               var1.setPrefViewportHeight(this.prefViewportHeight);
               break;
            case 9:
               var1.setPrefViewportWidth(this.prefViewportWidth);
               break;
            case 10:
               var1.setVbarPolicy(this.vbarPolicy);
               break;
            case 11:
               var1.setViewportBounds(this.viewportBounds);
               break;
            case 12:
               var1.setVmax(this.vmax);
               break;
            case 13:
               var1.setVmin(this.vmin);
               break;
            case 14:
               var1.setVvalue(this.vvalue);
         }
      }

   }

   public ScrollPaneBuilder content(Node var1) {
      this.content = var1;
      this.__set(0);
      return this;
   }

   public ScrollPaneBuilder fitToHeight(boolean var1) {
      this.fitToHeight = var1;
      this.__set(1);
      return this;
   }

   public ScrollPaneBuilder fitToWidth(boolean var1) {
      this.fitToWidth = var1;
      this.__set(2);
      return this;
   }

   public ScrollPaneBuilder hbarPolicy(ScrollPane.ScrollBarPolicy var1) {
      this.hbarPolicy = var1;
      this.__set(3);
      return this;
   }

   public ScrollPaneBuilder hmax(double var1) {
      this.hmax = var1;
      this.__set(4);
      return this;
   }

   public ScrollPaneBuilder hmin(double var1) {
      this.hmin = var1;
      this.__set(5);
      return this;
   }

   public ScrollPaneBuilder hvalue(double var1) {
      this.hvalue = var1;
      this.__set(6);
      return this;
   }

   public ScrollPaneBuilder pannable(boolean var1) {
      this.pannable = var1;
      this.__set(7);
      return this;
   }

   public ScrollPaneBuilder prefViewportHeight(double var1) {
      this.prefViewportHeight = var1;
      this.__set(8);
      return this;
   }

   public ScrollPaneBuilder prefViewportWidth(double var1) {
      this.prefViewportWidth = var1;
      this.__set(9);
      return this;
   }

   public ScrollPaneBuilder vbarPolicy(ScrollPane.ScrollBarPolicy var1) {
      this.vbarPolicy = var1;
      this.__set(10);
      return this;
   }

   public ScrollPaneBuilder viewportBounds(Bounds var1) {
      this.viewportBounds = var1;
      this.__set(11);
      return this;
   }

   public ScrollPaneBuilder vmax(double var1) {
      this.vmax = var1;
      this.__set(12);
      return this;
   }

   public ScrollPaneBuilder vmin(double var1) {
      this.vmin = var1;
      this.__set(13);
      return this;
   }

   public ScrollPaneBuilder vvalue(double var1) {
      this.vvalue = var1;
      this.__set(14);
      return this;
   }

   public ScrollPane build() {
      ScrollPane var1 = new ScrollPane();
      this.applyTo(var1);
      return var1;
   }
}
