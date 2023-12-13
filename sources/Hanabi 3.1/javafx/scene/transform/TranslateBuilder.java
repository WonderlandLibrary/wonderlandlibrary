package javafx.scene.transform;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TranslateBuilder implements Builder {
   private int __set;
   private double x;
   private double y;
   private double z;

   protected TranslateBuilder() {
   }

   public static TranslateBuilder create() {
      return new TranslateBuilder();
   }

   public void applyTo(Translate var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 2) != 0) {
         var1.setY(this.y);
      }

      if ((var2 & 4) != 0) {
         var1.setZ(this.z);
      }

   }

   public TranslateBuilder x(double var1) {
      this.x = var1;
      this.__set |= 1;
      return this;
   }

   public TranslateBuilder y(double var1) {
      this.y = var1;
      this.__set |= 2;
      return this;
   }

   public TranslateBuilder z(double var1) {
      this.z = var1;
      this.__set |= 4;
      return this;
   }

   public Translate build() {
      Translate var1 = new Translate();
      this.applyTo(var1);
      return var1;
   }
}
