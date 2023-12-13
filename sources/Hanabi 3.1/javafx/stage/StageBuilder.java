package javafx.stage;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class StageBuilder extends WindowBuilder implements Builder {
   private int __set;
   private boolean fullScreen;
   private boolean iconified;
   private Collection icons;
   private double maxHeight;
   private double maxWidth;
   private double minHeight;
   private double minWidth;
   private boolean resizable;
   private Scene scene;
   private StageStyle style;
   private String title;

   protected StageBuilder() {
      this.style = StageStyle.DECORATED;
   }

   public static StageBuilder create() {
      return new StageBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Stage var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setFullScreen(this.fullScreen);
               break;
            case 1:
               var1.setIconified(this.iconified);
               break;
            case 2:
               var1.getIcons().addAll(this.icons);
               break;
            case 3:
               var1.setMaxHeight(this.maxHeight);
               break;
            case 4:
               var1.setMaxWidth(this.maxWidth);
               break;
            case 5:
               var1.setMinHeight(this.minHeight);
               break;
            case 6:
               var1.setMinWidth(this.minWidth);
               break;
            case 7:
               var1.setResizable(this.resizable);
               break;
            case 8:
               var1.setScene(this.scene);
               break;
            case 9:
               var1.setTitle(this.title);
         }
      }

   }

   public StageBuilder fullScreen(boolean var1) {
      this.fullScreen = var1;
      this.__set(0);
      return this;
   }

   public StageBuilder iconified(boolean var1) {
      this.iconified = var1;
      this.__set(1);
      return this;
   }

   public StageBuilder icons(Collection var1) {
      this.icons = var1;
      this.__set(2);
      return this;
   }

   public StageBuilder icons(Image... var1) {
      return this.icons((Collection)Arrays.asList(var1));
   }

   public StageBuilder maxHeight(double var1) {
      this.maxHeight = var1;
      this.__set(3);
      return this;
   }

   public StageBuilder maxWidth(double var1) {
      this.maxWidth = var1;
      this.__set(4);
      return this;
   }

   public StageBuilder minHeight(double var1) {
      this.minHeight = var1;
      this.__set(5);
      return this;
   }

   public StageBuilder minWidth(double var1) {
      this.minWidth = var1;
      this.__set(6);
      return this;
   }

   public StageBuilder resizable(boolean var1) {
      this.resizable = var1;
      this.__set(7);
      return this;
   }

   public StageBuilder scene(Scene var1) {
      this.scene = var1;
      this.__set(8);
      return this;
   }

   public StageBuilder style(StageStyle var1) {
      this.style = var1;
      return this;
   }

   public StageBuilder title(String var1) {
      this.title = var1;
      this.__set(9);
      return this;
   }

   public Stage build() {
      Stage var1 = new Stage(this.style);
      this.applyTo(var1);
      return var1;
   }
}
