package javafx.scene.transform;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class AffineBuilder implements Builder {
   private int __set;
   private double mxx;
   private double mxy;
   private double mxz;
   private double myx;
   private double myy;
   private double myz;
   private double mzx;
   private double mzy;
   private double mzz;
   private double tx;
   private double ty;
   private double tz;

   protected AffineBuilder() {
   }

   public static AffineBuilder create() {
      return new AffineBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Affine var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setMxx(this.mxx);
               break;
            case 1:
               var1.setMxy(this.mxy);
               break;
            case 2:
               var1.setMxz(this.mxz);
               break;
            case 3:
               var1.setMyx(this.myx);
               break;
            case 4:
               var1.setMyy(this.myy);
               break;
            case 5:
               var1.setMyz(this.myz);
               break;
            case 6:
               var1.setMzx(this.mzx);
               break;
            case 7:
               var1.setMzy(this.mzy);
               break;
            case 8:
               var1.setMzz(this.mzz);
               break;
            case 9:
               var1.setTx(this.tx);
               break;
            case 10:
               var1.setTy(this.ty);
               break;
            case 11:
               var1.setTz(this.tz);
         }
      }

   }

   public AffineBuilder mxx(double var1) {
      this.mxx = var1;
      this.__set(0);
      return this;
   }

   public AffineBuilder mxy(double var1) {
      this.mxy = var1;
      this.__set(1);
      return this;
   }

   public AffineBuilder mxz(double var1) {
      this.mxz = var1;
      this.__set(2);
      return this;
   }

   public AffineBuilder myx(double var1) {
      this.myx = var1;
      this.__set(3);
      return this;
   }

   public AffineBuilder myy(double var1) {
      this.myy = var1;
      this.__set(4);
      return this;
   }

   public AffineBuilder myz(double var1) {
      this.myz = var1;
      this.__set(5);
      return this;
   }

   public AffineBuilder mzx(double var1) {
      this.mzx = var1;
      this.__set(6);
      return this;
   }

   public AffineBuilder mzy(double var1) {
      this.mzy = var1;
      this.__set(7);
      return this;
   }

   public AffineBuilder mzz(double var1) {
      this.mzz = var1;
      this.__set(8);
      return this;
   }

   public AffineBuilder tx(double var1) {
      this.tx = var1;
      this.__set(9);
      return this;
   }

   public AffineBuilder ty(double var1) {
      this.ty = var1;
      this.__set(10);
      return this;
   }

   public AffineBuilder tz(double var1) {
      this.tz = var1;
      this.__set(11);
      return this;
   }

   public Affine build() {
      Affine var1 = new Affine();
      this.applyTo(var1);
      return var1;
   }
}
