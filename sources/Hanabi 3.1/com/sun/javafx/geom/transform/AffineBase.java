package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.Vec3d;

public abstract class AffineBase extends BaseTransform {
   protected static final int APPLY_IDENTITY = 0;
   protected static final int APPLY_TRANSLATE = 1;
   protected static final int APPLY_SCALE = 2;
   protected static final int APPLY_SHEAR = 4;
   protected static final int APPLY_3D = 8;
   protected static final int APPLY_2D_MASK = 7;
   protected static final int APPLY_2D_DELTA_MASK = 6;
   protected static final int HI_SHIFT = 4;
   protected static final int HI_IDENTITY = 0;
   protected static final int HI_TRANSLATE = 16;
   protected static final int HI_SCALE = 32;
   protected static final int HI_SHEAR = 64;
   protected static final int HI_3D = 128;
   protected double mxx;
   protected double myx;
   protected double mxy;
   protected double myy;
   protected double mxt;
   protected double myt;
   protected transient int state;
   protected transient int type;
   private static final int[] rot90conversion = new int[]{4, 5, 4, 5, 2, 3, 6, 7};

   protected static void stateError() {
      throw new InternalError("missing case in transform state switch");
   }

   protected void updateState() {
      this.updateState2D();
   }

   protected void updateState2D() {
      if (this.mxy == 0.0 && this.myx == 0.0) {
         if (this.mxx == 1.0 && this.myy == 1.0) {
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 0;
               this.type = 0;
            } else {
               this.state = 1;
               this.type = 1;
            }
         } else {
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 2;
            } else {
               this.state = 3;
            }

            this.type = -1;
         }
      } else {
         if (this.mxx == 0.0 && this.myy == 0.0) {
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 4;
            } else {
               this.state = 5;
            }
         } else if (this.mxt == 0.0 && this.myt == 0.0) {
            this.state = 6;
         } else {
            this.state = 7;
         }

         this.type = -1;
      }

   }

   public int getType() {
      if (this.type == -1) {
         this.updateState();
         if (this.type == -1) {
            this.type = this.calculateType();
         }
      }

      return this.type;
   }

   protected int calculateType() {
      int var1 = (this.state & 8) == 0 ? 0 : 128;
      boolean var2;
      boolean var3;
      switch (this.state & 7) {
         case 0:
            break;
         case 1:
            var1 |= 1;
            break;
         case 3:
            var1 |= 1;
         case 2:
            var2 = this.mxx >= 0.0;
            var3 = this.myy >= 0.0;
            if (var2 == var3) {
               if (var2) {
                  if (this.mxx == this.myy) {
                     var1 |= 2;
                  } else {
                     var1 |= 4;
                  }
               } else if (this.mxx != this.myy) {
                  var1 |= 12;
               } else if (this.mxx != -1.0) {
                  var1 |= 10;
               } else {
                  var1 |= 8;
               }
            } else if (this.mxx == -this.myy) {
               if (this.mxx != 1.0 && this.mxx != -1.0) {
                  var1 |= 66;
               } else {
                  var1 |= 64;
               }
            } else {
               var1 |= 68;
            }
            break;
         case 5:
            var1 |= 1;
         case 4:
            var2 = this.mxy >= 0.0;
            var3 = this.myx >= 0.0;
            if (var2 != var3) {
               if (this.mxy != -this.myx) {
                  var1 |= 12;
               } else if (this.mxy != 1.0 && this.mxy != -1.0) {
                  var1 |= 10;
               } else {
                  var1 |= 8;
               }
            } else if (this.mxy == this.myx) {
               var1 |= 74;
            } else {
               var1 |= 76;
            }
            break;
         default:
            stateError();
         case 7:
            var1 |= 1;
         case 6:
            if (this.mxx * this.mxy + this.myx * this.myy != 0.0) {
               var1 |= 32;
            } else {
               var2 = this.mxx >= 0.0;
               var3 = this.myy >= 0.0;
               if (var2 == var3) {
                  if (this.mxx == this.myy && this.mxy == -this.myx) {
                     if (this.mxx * this.myy - this.mxy * this.myx != 1.0) {
                        var1 |= 18;
                     } else {
                        var1 |= 16;
                     }
                  } else {
                     var1 |= 20;
                  }
               } else if (this.mxx == -this.myy && this.mxy == this.myx) {
                  if (this.mxx * this.myy - this.mxy * this.myx != 1.0) {
                     var1 |= 82;
                  } else {
                     var1 |= 80;
                  }
               } else {
                  var1 |= 84;
               }
            }
      }

      return var1;
   }

   public double getMxx() {
      return this.mxx;
   }

   public double getMyy() {
      return this.myy;
   }

   public double getMxy() {
      return this.mxy;
   }

   public double getMyx() {
      return this.myx;
   }

   public double getMxt() {
      return this.mxt;
   }

   public double getMyt() {
      return this.myt;
   }

   public boolean isIdentity() {
      return this.state == 0 || this.getType() == 0;
   }

   public boolean isTranslateOrIdentity() {
      return this.state <= 1 || this.getType() <= 1;
   }

   public boolean is2D() {
      return this.state < 8 || this.getType() <= 127;
   }

   public double getDeterminant() {
      switch (this.state) {
         case 0:
         case 1:
            return 1.0;
         case 2:
         case 3:
            return this.mxx * this.myy;
         case 4:
         case 5:
            return -(this.mxy * this.myx);
         default:
            stateError();
         case 6:
         case 7:
            return this.mxx * this.myy - this.mxy * this.myx;
      }
   }

   protected abstract void reset3Delements();

   public void setToIdentity() {
      this.mxx = this.myy = 1.0;
      this.myx = this.mxy = this.mxt = this.myt = 0.0;
      this.reset3Delements();
      this.state = 0;
      this.type = 0;
   }

   public void setTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.mxx = var1;
      this.myx = var3;
      this.mxy = var5;
      this.myy = var7;
      this.mxt = var9;
      this.myt = var11;
      this.reset3Delements();
      this.updateState2D();
   }

   public void setToShear(double var1, double var3) {
      this.mxx = 1.0;
      this.mxy = var1;
      this.myx = var3;
      this.myy = 1.0;
      this.mxt = 0.0;
      this.myt = 0.0;
      this.reset3Delements();
      if (var1 == 0.0 && var3 == 0.0) {
         this.state = 0;
         this.type = 0;
      } else {
         this.state = 6;
         this.type = -1;
      }

   }

   public Point2D transform(Point2D var1) {
      return this.transform(var1, var1);
   }

   public Point2D transform(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = new Point2D();
      }

      double var3 = (double)var1.x;
      double var5 = (double)var1.y;
      switch (this.state & 7) {
         case 0:
            var2.setLocation((float)var3, (float)var5);
            return var2;
         case 1:
            var2.setLocation((float)(var3 + this.mxt), (float)(var5 + this.myt));
            return var2;
         case 2:
            var2.setLocation((float)(var3 * this.mxx), (float)(var5 * this.myy));
            return var2;
         case 3:
            var2.setLocation((float)(var3 * this.mxx + this.mxt), (float)(var5 * this.myy + this.myt));
            return var2;
         case 4:
            var2.setLocation((float)(var5 * this.mxy), (float)(var3 * this.myx));
            return var2;
         case 5:
            var2.setLocation((float)(var5 * this.mxy + this.mxt), (float)(var3 * this.myx + this.myt));
            return var2;
         case 6:
            var2.setLocation((float)(var3 * this.mxx + var5 * this.mxy), (float)(var3 * this.myx + var5 * this.myy));
            return var2;
         default:
            stateError();
         case 7:
            var2.setLocation((float)(var3 * this.mxx + var5 * this.mxy + this.mxt), (float)(var3 * this.myx + var5 * this.myy + this.myt));
            return var2;
      }
   }

   public Vec3d transform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      double var3 = var1.x;
      double var5 = var1.y;
      double var7 = var1.z;
      switch (this.state) {
         case 0:
            var2.x = var3;
            var2.y = var5;
            var2.z = var7;
            return var2;
         case 1:
            var2.x = var3 + this.mxt;
            var2.y = var5 + this.myt;
            var2.z = var7;
            return var2;
         case 2:
            var2.x = var3 * this.mxx;
            var2.y = var5 * this.myy;
            var2.z = var7;
            return var2;
         case 3:
            var2.x = var3 * this.mxx + this.mxt;
            var2.y = var5 * this.myy + this.myt;
            var2.z = var7;
            return var2;
         case 4:
            var2.x = var5 * this.mxy;
            var2.y = var3 * this.myx;
            var2.z = var7;
            return var2;
         case 5:
            var2.x = var5 * this.mxy + this.mxt;
            var2.y = var3 * this.myx + this.myt;
            var2.z = var7;
            return var2;
         case 6:
            var2.x = var3 * this.mxx + var5 * this.mxy;
            var2.y = var3 * this.myx + var5 * this.myy;
            var2.z = var7;
            return var2;
         default:
            stateError();
         case 7:
            var2.x = var3 * this.mxx + var5 * this.mxy + this.mxt;
            var2.y = var3 * this.myx + var5 * this.myy + this.myt;
            var2.z = var7;
            return var2;
      }
   }

   public Vec3d deltaTransform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      double var3 = var1.x;
      double var5 = var1.y;
      double var7 = var1.z;
      switch (this.state) {
         case 0:
         case 1:
            var2.x = var3;
            var2.y = var5;
            var2.z = var7;
            return var2;
         case 2:
         case 3:
            var2.x = var3 * this.mxx;
            var2.y = var5 * this.myy;
            var2.z = var7;
            return var2;
         case 4:
         case 5:
            var2.x = var5 * this.mxy;
            var2.y = var3 * this.myx;
            var2.z = var7;
            return var2;
         default:
            stateError();
         case 6:
         case 7:
            var2.x = var3 * this.mxx + var5 * this.mxy;
            var2.y = var3 * this.myx + var5 * this.myy;
            var2.z = var7;
            return var2;
      }
   }

   private BaseBounds transform2DBounds(RectBounds var1, RectBounds var2) {
      switch (this.state & 7) {
         case 0:
            if (var1 != var2) {
               var2.setBounds(var1);
            }
            break;
         case 1:
            var2.setBounds((float)((double)var1.getMinX() + this.mxt), (float)((double)var1.getMinY() + this.myt), (float)((double)var1.getMaxX() + this.mxt), (float)((double)var1.getMaxY() + this.myt));
            break;
         case 2:
            var2.setBoundsAndSort((float)((double)var1.getMinX() * this.mxx), (float)((double)var1.getMinY() * this.myy), (float)((double)var1.getMaxX() * this.mxx), (float)((double)var1.getMaxY() * this.myy));
            break;
         case 3:
            var2.setBoundsAndSort((float)((double)var1.getMinX() * this.mxx + this.mxt), (float)((double)var1.getMinY() * this.myy + this.myt), (float)((double)var1.getMaxX() * this.mxx + this.mxt), (float)((double)var1.getMaxY() * this.myy + this.myt));
            break;
         case 4:
            var2.setBoundsAndSort((float)((double)var1.getMinY() * this.mxy), (float)((double)var1.getMinX() * this.myx), (float)((double)var1.getMaxY() * this.mxy), (float)((double)var1.getMaxX() * this.myx));
            break;
         case 5:
            var2.setBoundsAndSort((float)((double)var1.getMinY() * this.mxy + this.mxt), (float)((double)var1.getMinX() * this.myx + this.myt), (float)((double)var1.getMaxY() * this.mxy + this.mxt), (float)((double)var1.getMaxX() * this.myx + this.myt));
            break;
         default:
            stateError();
         case 6:
         case 7:
            double var3 = (double)var1.getMinX();
            double var5 = (double)var1.getMinY();
            double var7 = (double)var1.getMaxX();
            double var9 = (double)var1.getMaxY();
            var2.setBoundsAndSort((float)(var3 * this.mxx + var5 * this.mxy), (float)(var3 * this.myx + var5 * this.myy), (float)(var7 * this.mxx + var9 * this.mxy), (float)(var7 * this.myx + var9 * this.myy));
            var2.add((float)(var3 * this.mxx + var9 * this.mxy), (float)(var3 * this.myx + var9 * this.myy));
            var2.add((float)(var7 * this.mxx + var5 * this.mxy), (float)(var7 * this.myx + var5 * this.myy));
            var2.setBounds((float)((double)var2.getMinX() + this.mxt), (float)((double)var2.getMinY() + this.myt), (float)((double)var2.getMaxX() + this.mxt), (float)((double)var2.getMaxY() + this.myt));
      }

      return var2;
   }

   private BaseBounds transform3DBounds(BaseBounds var1, BaseBounds var2) {
      switch (this.state & 7) {
         case 0:
            if (var1 != var2) {
               var2 = var2.deriveWithNewBounds(var1);
            }
            break;
         case 1:
            var2 = var2.deriveWithNewBounds((float)((double)var1.getMinX() + this.mxt), (float)((double)var1.getMinY() + this.myt), var1.getMinZ(), (float)((double)var1.getMaxX() + this.mxt), (float)((double)var1.getMaxY() + this.myt), var1.getMaxZ());
            break;
         case 2:
            var2 = var2.deriveWithNewBoundsAndSort((float)((double)var1.getMinX() * this.mxx), (float)((double)var1.getMinY() * this.myy), var1.getMinZ(), (float)((double)var1.getMaxX() * this.mxx), (float)((double)var1.getMaxY() * this.myy), var1.getMaxZ());
            break;
         case 3:
            var2 = var2.deriveWithNewBoundsAndSort((float)((double)var1.getMinX() * this.mxx + this.mxt), (float)((double)var1.getMinY() * this.myy + this.myt), var1.getMinZ(), (float)((double)var1.getMaxX() * this.mxx + this.mxt), (float)((double)var1.getMaxY() * this.myy + this.myt), var1.getMaxZ());
            break;
         case 4:
            var2 = var2.deriveWithNewBoundsAndSort((float)((double)var1.getMinY() * this.mxy), (float)((double)var1.getMinX() * this.myx), var1.getMinZ(), (float)((double)var1.getMaxY() * this.mxy), (float)((double)var1.getMaxX() * this.myx), var1.getMaxZ());
            break;
         case 5:
            var2 = var2.deriveWithNewBoundsAndSort((float)((double)var1.getMinY() * this.mxy + this.mxt), (float)((double)var1.getMinX() * this.myx + this.myt), var1.getMinZ(), (float)((double)var1.getMaxY() * this.mxy + this.mxt), (float)((double)var1.getMaxX() * this.myx + this.myt), var1.getMaxZ());
            break;
         default:
            stateError();
         case 6:
         case 7:
            double var3 = (double)var1.getMinX();
            double var5 = (double)var1.getMinY();
            double var7 = (double)var1.getMinZ();
            double var9 = (double)var1.getMaxX();
            double var11 = (double)var1.getMaxY();
            double var13 = (double)var1.getMaxZ();
            var2.setBoundsAndSort((float)(var3 * this.mxx + var5 * this.mxy), (float)(var3 * this.myx + var5 * this.myy), (float)var7, (float)(var9 * this.mxx + var11 * this.mxy), (float)(var9 * this.myx + var11 * this.myy), (float)var13);
            var2.add((float)(var3 * this.mxx + var11 * this.mxy), (float)(var3 * this.myx + var11 * this.myy), 0.0F);
            var2.add((float)(var9 * this.mxx + var5 * this.mxy), (float)(var9 * this.myx + var5 * this.myy), 0.0F);
            var2.deriveWithNewBounds((float)((double)var2.getMinX() + this.mxt), (float)((double)var2.getMinY() + this.myt), var2.getMinZ(), (float)((double)var2.getMaxX() + this.mxt), (float)((double)var2.getMaxY() + this.myt), var2.getMaxZ());
      }

      return var2;
   }

   public BaseBounds transform(BaseBounds var1, BaseBounds var2) {
      return var1.is2D() && var2.is2D() ? this.transform2DBounds((RectBounds)var1, (RectBounds)var2) : this.transform3DBounds(var1, var2);
   }

   public void transform(Rectangle var1, Rectangle var2) {
      switch (this.state & 7) {
         case 0:
            if (var2 != var1) {
               var2.setBounds(var1);
            }

            return;
         case 1:
            Translate2D.transform(var1, var2, this.mxt, this.myt);
            return;
         default:
            stateError();
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            RectBounds var3 = new RectBounds(var1);
            var3 = (RectBounds)this.transform((BaseBounds)var3, (BaseBounds)var3);
            var2.setBounds((BaseBounds)var3);
      }
   }

   public void transform(float[] var1, int var2, float[] var3, int var4, int var5) {
      this.doTransform(var1, var2, var3, var4, var5, this.state & 7);
   }

   public void deltaTransform(float[] var1, int var2, float[] var3, int var4, int var5) {
      this.doTransform(var1, var2, var3, var4, var5, this.state & 6);
   }

   private void doTransform(float[] var1, int var2, float[] var3, int var4, int var5, int var6) {
      if (var3 == var1 && var4 > var2 && var4 < var2 + var5 * 2) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
         var2 = var4;
      }

      double var7;
      double var9;
      double var11;
      double var13;
      double var15;
      double var17;
      double var19;
      double var21;
      switch (var6) {
         case 0:
            if (var1 != var3 || var2 != var4) {
               System.arraycopy(var1, var2, var3, var4, var5 * 2);
            }

            return;
         case 1:
            var11 = this.mxt;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (float)((double)var1[var2++] + var11);
               var3[var4++] = (float)((double)var1[var2++] + var17);
            }
         case 2:
            var7 = this.mxx;
            var15 = this.myy;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (float)(var7 * (double)var1[var2++]);
               var3[var4++] = (float)(var15 * (double)var1[var2++]);
            }
         case 3:
            var7 = this.mxx;
            var11 = this.mxt;
            var15 = this.myy;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (float)(var7 * (double)var1[var2++] + var11);
               var3[var4++] = (float)(var15 * (double)var1[var2++] + var17);
            }
         case 4:
            var9 = this.mxy;
            var13 = this.myx;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var19 = (double)var1[var2++];
               var3[var4++] = (float)(var9 * (double)var1[var2++]);
               var3[var4++] = (float)(var13 * var19);
            }
         case 5:
            var9 = this.mxy;
            var11 = this.mxt;
            var13 = this.myx;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var19 = (double)var1[var2++];
               var3[var4++] = (float)(var9 * (double)var1[var2++] + var11);
               var3[var4++] = (float)(var13 * var19 + var17);
            }
         case 6:
            var7 = this.mxx;
            var9 = this.mxy;
            var13 = this.myx;
            var15 = this.myy;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var19 = (double)var1[var2++];
               var21 = (double)var1[var2++];
               var3[var4++] = (float)(var7 * var19 + var9 * var21);
               var3[var4++] = (float)(var13 * var19 + var15 * var21);
            }
         default:
            stateError();
         case 7:
            var7 = this.mxx;
            var9 = this.mxy;
            var11 = this.mxt;
            var13 = this.myx;
            var15 = this.myy;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var19 = (double)var1[var2++];
               var21 = (double)var1[var2++];
               var3[var4++] = (float)(var7 * var19 + var9 * var21 + var11);
               var3[var4++] = (float)(var13 * var19 + var15 * var21 + var17);
            }
      }
   }

   public void transform(double[] var1, int var2, double[] var3, int var4, int var5) {
      this.doTransform(var1, var2, var3, var4, var5, this.state & 7);
   }

   public void deltaTransform(double[] var1, int var2, double[] var3, int var4, int var5) {
      this.doTransform(var1, var2, var3, var4, var5, this.state & 6);
   }

   private void doTransform(double[] var1, int var2, double[] var3, int var4, int var5, int var6) {
      if (var3 == var1 && var4 > var2 && var4 < var2 + var5 * 2) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
         var2 = var4;
      }

      double var7;
      double var9;
      double var11;
      double var13;
      double var15;
      double var17;
      double var19;
      double var21;
      switch (var6) {
         case 0:
            if (var1 != var3 || var2 != var4) {
               System.arraycopy(var1, var2, var3, var4, var5 * 2);
            }

            return;
         case 1:
            var11 = this.mxt;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var1[var2++] + var11;
               var3[var4++] = var1[var2++] + var17;
            }
         case 2:
            var7 = this.mxx;
            var15 = this.myy;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var7 * var1[var2++];
               var3[var4++] = var15 * var1[var2++];
            }
         case 3:
            var7 = this.mxx;
            var11 = this.mxt;
            var15 = this.myy;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var7 * var1[var2++] + var11;
               var3[var4++] = var15 * var1[var2++] + var17;
            }
         case 4:
            var9 = this.mxy;
            var13 = this.myx;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var19 = var1[var2++];
               var3[var4++] = var9 * var1[var2++];
               var3[var4++] = var13 * var19;
            }
         case 5:
            var9 = this.mxy;
            var11 = this.mxt;
            var13 = this.myx;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var19 = var1[var2++];
               var3[var4++] = var9 * var1[var2++] + var11;
               var3[var4++] = var13 * var19 + var17;
            }
         case 6:
            var7 = this.mxx;
            var9 = this.mxy;
            var13 = this.myx;
            var15 = this.myy;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var19 = var1[var2++];
               var21 = var1[var2++];
               var3[var4++] = var7 * var19 + var9 * var21;
               var3[var4++] = var13 * var19 + var15 * var21;
            }
         default:
            stateError();
         case 7:
            var7 = this.mxx;
            var9 = this.mxy;
            var11 = this.mxt;
            var13 = this.myx;
            var15 = this.myy;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var19 = var1[var2++];
               var21 = var1[var2++];
               var3[var4++] = var7 * var19 + var9 * var21 + var11;
               var3[var4++] = var13 * var19 + var15 * var21 + var17;
            }
      }
   }

   public void transform(float[] var1, int var2, double[] var3, int var4, int var5) {
      double var6;
      double var8;
      double var10;
      double var12;
      double var14;
      double var16;
      double var18;
      double var20;
      switch (this.state & 7) {
         case 0:
            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (double)var1[var2++];
               var3[var4++] = (double)var1[var2++];
            }
         case 1:
            var10 = this.mxt;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (double)var1[var2++] + var10;
               var3[var4++] = (double)var1[var2++] + var16;
            }
         case 2:
            var6 = this.mxx;
            var14 = this.myy;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var6 * (double)var1[var2++];
               var3[var4++] = var14 * (double)var1[var2++];
            }
         case 3:
            var6 = this.mxx;
            var10 = this.mxt;
            var14 = this.myy;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var6 * (double)var1[var2++] + var10;
               var3[var4++] = var14 * (double)var1[var2++] + var16;
            }
         case 4:
            var8 = this.mxy;
            var12 = this.myx;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = (double)var1[var2++];
               var3[var4++] = var8 * (double)var1[var2++];
               var3[var4++] = var12 * var18;
            }
         case 5:
            var8 = this.mxy;
            var10 = this.mxt;
            var12 = this.myx;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = (double)var1[var2++];
               var3[var4++] = var8 * (double)var1[var2++] + var10;
               var3[var4++] = var12 * var18 + var16;
            }
         case 6:
            var6 = this.mxx;
            var8 = this.mxy;
            var12 = this.myx;
            var14 = this.myy;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = (double)var1[var2++];
               var20 = (double)var1[var2++];
               var3[var4++] = var6 * var18 + var8 * var20;
               var3[var4++] = var12 * var18 + var14 * var20;
            }
         default:
            stateError();
         case 7:
            var6 = this.mxx;
            var8 = this.mxy;
            var10 = this.mxt;
            var12 = this.myx;
            var14 = this.myy;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = (double)var1[var2++];
               var20 = (double)var1[var2++];
               var3[var4++] = var6 * var18 + var8 * var20 + var10;
               var3[var4++] = var12 * var18 + var14 * var20 + var16;
            }
      }
   }

   public void transform(double[] var1, int var2, float[] var3, int var4, int var5) {
      double var6;
      double var8;
      double var10;
      double var12;
      double var14;
      double var16;
      double var18;
      double var20;
      switch (this.state & 7) {
         case 0:
            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (float)var1[var2++];
               var3[var4++] = (float)var1[var2++];
            }
         case 1:
            var10 = this.mxt;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (float)(var1[var2++] + var10);
               var3[var4++] = (float)(var1[var2++] + var16);
            }
         case 2:
            var6 = this.mxx;
            var14 = this.myy;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (float)(var6 * var1[var2++]);
               var3[var4++] = (float)(var14 * var1[var2++]);
            }
         case 3:
            var6 = this.mxx;
            var10 = this.mxt;
            var14 = this.myy;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (float)(var6 * var1[var2++] + var10);
               var3[var4++] = (float)(var14 * var1[var2++] + var16);
            }
         case 4:
            var8 = this.mxy;
            var12 = this.myx;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = var1[var2++];
               var3[var4++] = (float)(var8 * var1[var2++]);
               var3[var4++] = (float)(var12 * var18);
            }
         case 5:
            var8 = this.mxy;
            var10 = this.mxt;
            var12 = this.myx;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = var1[var2++];
               var3[var4++] = (float)(var8 * var1[var2++] + var10);
               var3[var4++] = (float)(var12 * var18 + var16);
            }
         case 6:
            var6 = this.mxx;
            var8 = this.mxy;
            var12 = this.myx;
            var14 = this.myy;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = var1[var2++];
               var20 = var1[var2++];
               var3[var4++] = (float)(var6 * var18 + var8 * var20);
               var3[var4++] = (float)(var12 * var18 + var14 * var20);
            }
         default:
            stateError();
         case 7:
            var6 = this.mxx;
            var8 = this.mxy;
            var10 = this.mxt;
            var12 = this.myx;
            var14 = this.myy;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = var1[var2++];
               var20 = var1[var2++];
               var3[var4++] = (float)(var6 * var18 + var8 * var20 + var10);
               var3[var4++] = (float)(var12 * var18 + var14 * var20 + var16);
            }
      }
   }

   public Point2D inverseTransform(Point2D var1, Point2D var2) throws NoninvertibleTransformException {
      if (var2 == null) {
         var2 = new Point2D();
      }

      double var3 = (double)var1.x;
      double var5 = (double)var1.y;
      switch (this.state) {
         case 0:
            var2.setLocation((float)var3, (float)var5);
            return var2;
         case 1:
            var2.setLocation((float)(var3 - this.mxt), (float)(var5 - this.myt));
            return var2;
         case 3:
            var3 -= this.mxt;
            var5 -= this.myt;
         case 2:
            if (this.mxx != 0.0 && this.myy != 0.0) {
               var2.setLocation((float)(var3 / this.mxx), (float)(var5 / this.myy));
               return var2;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 5:
            var3 -= this.mxt;
            var5 -= this.myt;
         case 4:
            if (this.mxy != 0.0 && this.myx != 0.0) {
               var2.setLocation((float)(var5 / this.myx), (float)(var3 / this.mxy));
               return var2;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         default:
            stateError();
         case 7:
            var3 -= this.mxt;
            var5 -= this.myt;
         case 6:
            double var7 = this.mxx * this.myy - this.mxy * this.myx;
            if (var7 != 0.0 && !(Math.abs(var7) <= Double.MIN_VALUE)) {
               var2.setLocation((float)((var3 * this.myy - var5 * this.mxy) / var7), (float)((var5 * this.mxx - var3 * this.myx) / var7));
               return var2;
            } else {
               throw new NoninvertibleTransformException("Determinant is " + var7);
            }
      }
   }

   public Vec3d inverseTransform(Vec3d var1, Vec3d var2) throws NoninvertibleTransformException {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      double var3 = var1.x;
      double var5 = var1.y;
      double var7 = var1.z;
      switch (this.state) {
         case 0:
            var2.set(var3, var5, var7);
            return var2;
         case 1:
            var2.set(var3 - this.mxt, var5 - this.myt, var7);
            return var2;
         case 3:
            var3 -= this.mxt;
            var5 -= this.myt;
         case 2:
            if (this.mxx != 0.0 && this.myy != 0.0) {
               var2.set(var3 / this.mxx, var5 / this.myy, var7);
               return var2;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 5:
            var3 -= this.mxt;
            var5 -= this.myt;
         case 4:
            if (this.mxy != 0.0 && this.myx != 0.0) {
               var2.set(var5 / this.myx, var3 / this.mxy, var7);
               return var2;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         default:
            stateError();
         case 7:
            var3 -= this.mxt;
            var5 -= this.myt;
         case 6:
            double var9 = this.mxx * this.myy - this.mxy * this.myx;
            if (var9 != 0.0 && !(Math.abs(var9) <= Double.MIN_VALUE)) {
               var2.set((var3 * this.myy - var5 * this.mxy) / var9, (var5 * this.mxx - var3 * this.myx) / var9, var7);
               return var2;
            } else {
               throw new NoninvertibleTransformException("Determinant is " + var9);
            }
      }
   }

   public Vec3d inverseDeltaTransform(Vec3d var1, Vec3d var2) throws NoninvertibleTransformException {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      double var3 = var1.x;
      double var5 = var1.y;
      double var7 = var1.z;
      switch (this.state) {
         case 0:
         case 1:
            var2.set(var3, var5, var7);
            return var2;
         case 2:
         case 3:
            if (this.mxx != 0.0 && this.myy != 0.0) {
               var2.set(var3 / this.mxx, var5 / this.myy, var7);
               return var2;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 4:
         case 5:
            if (this.mxy != 0.0 && this.myx != 0.0) {
               var2.set(var5 / this.myx, var3 / this.mxy, var7);
               return var2;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         default:
            stateError();
         case 6:
         case 7:
            double var9 = this.mxx * this.myy - this.mxy * this.myx;
            if (var9 != 0.0 && !(Math.abs(var9) <= Double.MIN_VALUE)) {
               var2.set((var3 * this.myy - var5 * this.mxy) / var9, (var5 * this.mxx - var3 * this.myx) / var9, var7);
               return var2;
            } else {
               throw new NoninvertibleTransformException("Determinant is " + var9);
            }
      }
   }

   private BaseBounds inversTransform2DBounds(RectBounds var1, RectBounds var2) throws NoninvertibleTransformException {
      label57: {
         switch (this.state) {
            case 0:
               if (var2 != var1) {
                  var2.setBounds(var1);
               }
               break;
            case 1:
               var2.setBounds((float)((double)var1.getMinX() - this.mxt), (float)((double)var1.getMinY() - this.myt), (float)((double)var1.getMaxX() - this.mxt), (float)((double)var1.getMaxY() - this.myt));
               break;
            case 2:
               if (this.mxx == 0.0 || this.myy == 0.0) {
                  throw new NoninvertibleTransformException("Determinant is 0");
               }

               var2.setBoundsAndSort((float)((double)var1.getMinX() / this.mxx), (float)((double)var1.getMinY() / this.myy), (float)((double)var1.getMaxX() / this.mxx), (float)((double)var1.getMaxY() / this.myy));
               break;
            case 3:
               if (this.mxx == 0.0 || this.myy == 0.0) {
                  throw new NoninvertibleTransformException("Determinant is 0");
               }

               var2.setBoundsAndSort((float)(((double)var1.getMinX() - this.mxt) / this.mxx), (float)(((double)var1.getMinY() - this.myt) / this.myy), (float)(((double)var1.getMaxX() - this.mxt) / this.mxx), (float)(((double)var1.getMaxY() - this.myt) / this.myy));
               break;
            case 4:
               if (this.mxy != 0.0 && this.myx != 0.0) {
                  var2.setBoundsAndSort((float)((double)var1.getMinY() / this.myx), (float)((double)var1.getMinX() / this.mxy), (float)((double)var1.getMaxY() / this.myx), (float)((double)var1.getMaxX() / this.mxy));
                  break;
               }

               throw new NoninvertibleTransformException("Determinant is 0");
            case 5:
               if (this.mxy == 0.0 || this.myx == 0.0) {
                  throw new NoninvertibleTransformException("Determinant is 0");
               }

               var2.setBoundsAndSort((float)(((double)var1.getMinY() - this.myt) / this.myx), (float)(((double)var1.getMinX() - this.mxt) / this.mxy), (float)(((double)var1.getMaxY() - this.myt) / this.myx), (float)(((double)var1.getMaxX() - this.mxt) / this.mxy));
               break;
            case 6:
            case 7:
               break label57;
            default:
               stateError();
               break label57;
         }

         return var2;
      }

      double var3 = this.mxx * this.myy - this.mxy * this.myx;
      if (var3 != 0.0 && !(Math.abs(var3) <= Double.MIN_VALUE)) {
         double var5 = (double)var1.getMinX() - this.mxt;
         double var7 = (double)var1.getMinY() - this.myt;
         double var9 = (double)var1.getMaxX() - this.mxt;
         double var11 = (double)var1.getMaxY() - this.myt;
         var2.setBoundsAndSort((float)((var5 * this.myy - var7 * this.mxy) / var3), (float)((var7 * this.mxx - var5 * this.myx) / var3), (float)((var9 * this.myy - var11 * this.mxy) / var3), (float)((var11 * this.mxx - var9 * this.myx) / var3));
         var2.add((float)((var9 * this.myy - var7 * this.mxy) / var3), (float)((var7 * this.mxx - var9 * this.myx) / var3));
         var2.add((float)((var5 * this.myy - var11 * this.mxy) / var3), (float)((var11 * this.mxx - var5 * this.myx) / var3));
         return var2;
      } else {
         throw new NoninvertibleTransformException("Determinant is " + var3);
      }
   }

   private BaseBounds inversTransform3DBounds(BaseBounds var1, BaseBounds var2) throws NoninvertibleTransformException {
      switch (this.state) {
         case 0:
            if (var2 != var1) {
               var2 = var2.deriveWithNewBounds(var1);
            }
            break;
         case 1:
            var2 = var2.deriveWithNewBounds((float)((double)var1.getMinX() - this.mxt), (float)((double)var1.getMinY() - this.myt), var1.getMinZ(), (float)((double)var1.getMaxX() - this.mxt), (float)((double)var1.getMaxY() - this.myt), var1.getMaxZ());
            break;
         case 2:
            if (this.mxx != 0.0 && this.myy != 0.0) {
               var2 = var2.deriveWithNewBoundsAndSort((float)((double)var1.getMinX() / this.mxx), (float)((double)var1.getMinY() / this.myy), var1.getMinZ(), (float)((double)var1.getMaxX() / this.mxx), (float)((double)var1.getMaxY() / this.myy), var1.getMaxZ());
               break;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 3:
            if (this.mxx != 0.0 && this.myy != 0.0) {
               var2 = var2.deriveWithNewBoundsAndSort((float)(((double)var1.getMinX() - this.mxt) / this.mxx), (float)(((double)var1.getMinY() - this.myt) / this.myy), var1.getMinZ(), (float)(((double)var1.getMaxX() - this.mxt) / this.mxx), (float)(((double)var1.getMaxY() - this.myt) / this.myy), var1.getMaxZ());
               break;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         default:
            stateError();
         case 4:
         case 5:
         case 6:
         case 7:
            double var3 = this.mxx * this.myy - this.mxy * this.myx;
            if (var3 != 0.0 && !(Math.abs(var3) <= Double.MIN_VALUE)) {
               double var5 = (double)var1.getMinX() - this.mxt;
               double var7 = (double)var1.getMinY() - this.myt;
               double var9 = (double)var1.getMinZ();
               double var11 = (double)var1.getMaxX() - this.mxt;
               double var13 = (double)var1.getMaxY() - this.myt;
               double var15 = (double)var1.getMaxZ();
               var2 = var2.deriveWithNewBoundsAndSort((float)((var5 * this.myy - var7 * this.mxy) / var3), (float)((var7 * this.mxx - var5 * this.myx) / var3), (float)(var9 / var3), (float)((var11 * this.myy - var13 * this.mxy) / var3), (float)((var13 * this.mxx - var11 * this.myx) / var3), (float)(var15 / var3));
               var2.add((float)((var11 * this.myy - var7 * this.mxy) / var3), (float)((var7 * this.mxx - var11 * this.myx) / var3), 0.0F);
               var2.add((float)((var5 * this.myy - var13 * this.mxy) / var3), (float)((var13 * this.mxx - var5 * this.myx) / var3), 0.0F);
               return var2;
            }

            throw new NoninvertibleTransformException("Determinant is " + var3);
      }

      return var2;
   }

   public BaseBounds inverseTransform(BaseBounds var1, BaseBounds var2) throws NoninvertibleTransformException {
      return var1.is2D() && var2.is2D() ? this.inversTransform2DBounds((RectBounds)var1, (RectBounds)var2) : this.inversTransform3DBounds(var1, var2);
   }

   public void inverseTransform(Rectangle var1, Rectangle var2) throws NoninvertibleTransformException {
      switch (this.state) {
         case 0:
            if (var2 != var1) {
               var2.setBounds(var1);
            }

            return;
         case 1:
            Translate2D.transform(var1, var2, -this.mxt, -this.myt);
            return;
         default:
            stateError();
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            RectBounds var3 = new RectBounds(var1);
            var3 = (RectBounds)this.inverseTransform((BaseBounds)var3, (BaseBounds)var3);
            var2.setBounds((BaseBounds)var3);
      }
   }

   public void inverseTransform(float[] var1, int var2, float[] var3, int var4, int var5) throws NoninvertibleTransformException {
      this.doInverseTransform(var1, var2, var3, var4, var5, this.state);
   }

   public void inverseDeltaTransform(float[] var1, int var2, float[] var3, int var4, int var5) throws NoninvertibleTransformException {
      this.doInverseTransform(var1, var2, var3, var4, var5, this.state & -2);
   }

   private void doInverseTransform(float[] var1, int var2, float[] var3, int var4, int var5, int var6) throws NoninvertibleTransformException {
      if (var3 == var1 && var4 > var2 && var4 < var2 + var5 * 2) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
         var2 = var4;
      }

      double var7;
      double var9;
      double var11;
      double var13;
      double var15;
      double var17;
      double var19;
      double var21;
      double var23;
      switch (var6) {
         case 0:
            if (var1 != var3 || var2 != var4) {
               System.arraycopy(var1, var2, var3, var4, var5 * 2);
            }

            return;
         case 1:
            var11 = this.mxt;
            var17 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = (float)((double)var1[var2++] - var11);
               var3[var4++] = (float)((double)var1[var2++] - var17);
            }
         case 2:
            var7 = this.mxx;
            var15 = this.myy;
            if (var7 != 0.0 && var15 != 0.0) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = (float)((double)var1[var2++] / var7);
                  var3[var4++] = (float)((double)var1[var2++] / var15);
               }
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 3:
            var7 = this.mxx;
            var11 = this.mxt;
            var15 = this.myy;
            var17 = this.myt;
            if (var7 != 0.0 && var15 != 0.0) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = (float)(((double)var1[var2++] - var11) / var7);
                  var3[var4++] = (float)(((double)var1[var2++] - var17) / var15);
               }
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 4:
            var9 = this.mxy;
            var13 = this.myx;
            if (var9 != 0.0 && var13 != 0.0) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var21 = (double)var1[var2++];
                  var3[var4++] = (float)((double)var1[var2++] / var13);
                  var3[var4++] = (float)(var21 / var9);
               }
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 5:
            var9 = this.mxy;
            var11 = this.mxt;
            var13 = this.myx;
            var17 = this.myt;
            if (var9 != 0.0 && var13 != 0.0) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var21 = (double)var1[var2++] - var11;
                  var3[var4++] = (float)(((double)var1[var2++] - var17) / var13);
                  var3[var4++] = (float)(var21 / var9);
               }
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 6:
            var7 = this.mxx;
            var9 = this.mxy;
            var13 = this.myx;
            var15 = this.myy;
            var19 = var7 * var15 - var9 * var13;
            if (var19 != 0.0 && !(Math.abs(var19) <= Double.MIN_VALUE)) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var21 = (double)var1[var2++];
                  var23 = (double)var1[var2++];
                  var3[var4++] = (float)((var21 * var15 - var23 * var9) / var19);
                  var3[var4++] = (float)((var23 * var7 - var21 * var13) / var19);
               }
            }

            throw new NoninvertibleTransformException("Determinant is " + var19);
         case 7:
            break;
         default:
            stateError();
      }

      var7 = this.mxx;
      var9 = this.mxy;
      var11 = this.mxt;
      var13 = this.myx;
      var15 = this.myy;
      var17 = this.myt;
      var19 = var7 * var15 - var9 * var13;
      if (var19 != 0.0 && !(Math.abs(var19) <= Double.MIN_VALUE)) {
         while(true) {
            --var5;
            if (var5 < 0) {
               return;
            }

            var21 = (double)var1[var2++] - var11;
            var23 = (double)var1[var2++] - var17;
            var3[var4++] = (float)((var21 * var15 - var23 * var9) / var19);
            var3[var4++] = (float)((var23 * var7 - var21 * var13) / var19);
         }
      } else {
         throw new NoninvertibleTransformException("Determinant is " + var19);
      }
   }

   public void inverseTransform(double[] var1, int var2, double[] var3, int var4, int var5) throws NoninvertibleTransformException {
      if (var3 == var1 && var4 > var2 && var4 < var2 + var5 * 2) {
         System.arraycopy(var1, var2, var3, var4, var5 * 2);
         var2 = var4;
      }

      double var6;
      double var8;
      double var10;
      double var12;
      double var14;
      double var16;
      double var18;
      double var20;
      double var22;
      switch (this.state) {
         case 0:
            if (var1 != var3 || var2 != var4) {
               System.arraycopy(var1, var2, var3, var4, var5 * 2);
            }

            return;
         case 1:
            var10 = this.mxt;
            var16 = this.myt;

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var1[var2++] - var10;
               var3[var4++] = var1[var2++] - var16;
            }
         case 2:
            var6 = this.mxx;
            var14 = this.myy;
            if (var6 != 0.0 && var14 != 0.0) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = var1[var2++] / var6;
                  var3[var4++] = var1[var2++] / var14;
               }
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 3:
            var6 = this.mxx;
            var10 = this.mxt;
            var14 = this.myy;
            var16 = this.myt;
            if (var6 != 0.0 && var14 != 0.0) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = (var1[var2++] - var10) / var6;
                  var3[var4++] = (var1[var2++] - var16) / var14;
               }
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 4:
            var8 = this.mxy;
            var12 = this.myx;
            if (var8 != 0.0 && var12 != 0.0) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var20 = var1[var2++];
                  var3[var4++] = var1[var2++] / var12;
                  var3[var4++] = var20 / var8;
               }
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 5:
            var8 = this.mxy;
            var10 = this.mxt;
            var12 = this.myx;
            var16 = this.myt;
            if (var8 != 0.0 && var12 != 0.0) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var20 = var1[var2++] - var10;
                  var3[var4++] = (var1[var2++] - var16) / var12;
                  var3[var4++] = var20 / var8;
               }
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 6:
            var6 = this.mxx;
            var8 = this.mxy;
            var12 = this.myx;
            var14 = this.myy;
            var18 = var6 * var14 - var8 * var12;
            if (var18 != 0.0 && !(Math.abs(var18) <= Double.MIN_VALUE)) {
               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var20 = var1[var2++];
                  var22 = var1[var2++];
                  var3[var4++] = (var20 * var14 - var22 * var8) / var18;
                  var3[var4++] = (var22 * var6 - var20 * var12) / var18;
               }
            }

            throw new NoninvertibleTransformException("Determinant is " + var18);
         case 7:
            break;
         default:
            stateError();
      }

      var6 = this.mxx;
      var8 = this.mxy;
      var10 = this.mxt;
      var12 = this.myx;
      var14 = this.myy;
      var16 = this.myt;
      var18 = var6 * var14 - var8 * var12;
      if (var18 != 0.0 && !(Math.abs(var18) <= Double.MIN_VALUE)) {
         while(true) {
            --var5;
            if (var5 < 0) {
               return;
            }

            var20 = var1[var2++] - var10;
            var22 = var1[var2++] - var16;
            var3[var4++] = (var20 * var14 - var22 * var8) / var18;
            var3[var4++] = (var22 * var6 - var20 * var12) / var18;
         }
      } else {
         throw new NoninvertibleTransformException("Determinant is " + var18);
      }
   }

   public Shape createTransformedShape(Shape var1) {
      return var1 == null ? null : new Path2D(var1, this);
   }

   public void translate(double var1, double var3) {
      switch (this.state) {
         case 0:
            this.mxt = var1;
            this.myt = var3;
            if (var1 != 0.0 || var3 != 0.0) {
               this.state = 1;
               this.type = 1;
            }

            return;
         case 1:
            this.mxt += var1;
            this.myt += var3;
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 0;
               this.type = 0;
            }

            return;
         case 2:
            this.mxt = var1 * this.mxx;
            this.myt = var3 * this.myy;
            if (this.mxt != 0.0 || this.myt != 0.0) {
               this.state = 3;
               this.type |= 1;
            }

            return;
         case 3:
            this.mxt += var1 * this.mxx;
            this.myt += var3 * this.myy;
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 2;
               if (this.type != -1) {
                  this.type &= -2;
               }
            }

            return;
         case 4:
            this.mxt = var3 * this.mxy;
            this.myt = var1 * this.myx;
            if (this.mxt != 0.0 || this.myt != 0.0) {
               this.state = 5;
               this.type |= 1;
            }

            return;
         case 5:
            this.mxt += var3 * this.mxy;
            this.myt += var1 * this.myx;
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 4;
               if (this.type != -1) {
                  this.type &= -2;
               }
            }

            return;
         case 6:
            this.mxt = var1 * this.mxx + var3 * this.mxy;
            this.myt = var1 * this.myx + var3 * this.myy;
            if (this.mxt != 0.0 || this.myt != 0.0) {
               this.state = 7;
               this.type |= 1;
            }

            return;
         default:
            stateError();
         case 7:
            this.mxt += var1 * this.mxx + var3 * this.mxy;
            this.myt += var1 * this.myx + var3 * this.myy;
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 6;
               if (this.type != -1) {
                  this.type &= -2;
               }
            }

      }
   }

   protected final void rotate90() {
      double var1 = this.mxx;
      this.mxx = this.mxy;
      this.mxy = -var1;
      var1 = this.myx;
      this.myx = this.myy;
      this.myy = -var1;
      int var3 = rot90conversion[this.state];
      if ((var3 & 6) == 2 && this.mxx == 1.0 && this.myy == 1.0) {
         var3 -= 2;
      }

      this.state = var3;
      this.type = -1;
   }

   protected final void rotate180() {
      this.mxx = -this.mxx;
      this.myy = -this.myy;
      int var1 = this.state;
      if ((var1 & 4) != 0) {
         this.mxy = -this.mxy;
         this.myx = -this.myx;
      } else if (this.mxx == 1.0 && this.myy == 1.0) {
         this.state = var1 & -3;
      } else {
         this.state = var1 | 2;
      }

      this.type = -1;
   }

   protected final void rotate270() {
      double var1 = this.mxx;
      this.mxx = -this.mxy;
      this.mxy = var1;
      var1 = this.myx;
      this.myx = -this.myy;
      this.myy = var1;
      int var3 = rot90conversion[this.state];
      if ((var3 & 6) == 2 && this.mxx == 1.0 && this.myy == 1.0) {
         var3 -= 2;
      }

      this.state = var3;
      this.type = -1;
   }

   public void rotate(double var1) {
      double var3 = Math.sin(var1);
      if (var3 == 1.0) {
         this.rotate90();
      } else if (var3 == -1.0) {
         this.rotate270();
      } else {
         double var5 = Math.cos(var1);
         if (var5 == -1.0) {
            this.rotate180();
         } else if (var5 != 1.0) {
            double var7 = this.mxx;
            double var9 = this.mxy;
            this.mxx = var5 * var7 + var3 * var9;
            this.mxy = -var3 * var7 + var5 * var9;
            var7 = this.myx;
            var9 = this.myy;
            this.myx = var5 * var7 + var3 * var9;
            this.myy = -var3 * var7 + var5 * var9;
            this.updateState2D();
         }
      }

   }

   public void scale(double var1, double var3) {
      int var5 = this.state;
      switch (var5) {
         case 0:
         case 1:
            this.mxx = var1;
            this.myy = var3;
            if (var1 != 1.0 || var3 != 1.0) {
               this.state = var5 | 2;
               this.type = -1;
            }

            return;
         case 2:
         case 3:
            this.mxx *= var1;
            this.myy *= var3;
            if (this.mxx == 1.0 && this.myy == 1.0) {
               this.state = var5 &= 1;
               this.type = var5 == 0 ? 0 : 1;
            } else {
               this.type = -1;
            }

            return;
         case 4:
         case 5:
            break;
         default:
            stateError();
         case 6:
         case 7:
            this.mxx *= var1;
            this.myy *= var3;
      }

      this.mxy *= var3;
      this.myx *= var1;
      if (this.mxy == 0.0 && this.myx == 0.0) {
         var5 &= 1;
         if (this.mxx == 1.0 && this.myy == 1.0) {
            this.type = var5 == 0 ? 0 : 1;
         } else {
            var5 |= 2;
            this.type = -1;
         }

         this.state = var5;
      }

   }

   public void shear(double var1, double var3) {
      int var5 = this.state;
      switch (var5) {
         case 0:
         case 1:
            this.mxy = var1;
            this.myx = var3;
            if (this.mxy != 0.0 || this.myx != 0.0) {
               this.state = var5 | 2 | 4;
               this.type = -1;
            }

            return;
         case 2:
         case 3:
            this.mxy = this.mxx * var1;
            this.myx = this.myy * var3;
            if (this.mxy != 0.0 || this.myx != 0.0) {
               this.state = var5 | 4;
            }

            this.type = -1;
            return;
         case 4:
         case 5:
            this.mxx = this.mxy * var3;
            this.myy = this.myx * var1;
            if (this.mxx != 0.0 || this.myy != 0.0) {
               this.state = var5 | 2;
            }

            this.type = -1;
            return;
         default:
            stateError();
         case 6:
         case 7:
            double var6 = this.mxx;
            double var8 = this.mxy;
            this.mxx = var6 + var8 * var3;
            this.mxy = var6 * var1 + var8;
            var6 = this.myx;
            var8 = this.myy;
            this.myx = var6 + var8 * var3;
            this.myy = var6 * var1 + var8;
            this.updateState2D();
      }
   }

   public void concatenate(BaseTransform var1) {
      switch (((BaseTransform)var1).getDegree()) {
         case IDENTITY:
            return;
         case TRANSLATE_2D:
            this.translate(((BaseTransform)var1).getMxt(), ((BaseTransform)var1).getMyt());
            return;
         default:
            if (!((BaseTransform)var1).is2D()) {
               degreeError(BaseTransform.Degree.AFFINE_2D);
            }

            if (!(var1 instanceof AffineBase)) {
               var1 = new Affine2D((BaseTransform)var1);
            }
         case AFFINE_2D:
            int var18 = this.state;
            AffineBase var19 = (AffineBase)var1;
            int var20 = var19.state;
            double var2;
            double var8;
            double var10;
            switch (var20 << 4 | var18) {
               case 0:
               case 1:
               case 2:
               case 3:
               case 4:
               case 5:
               case 6:
               case 7:
                  return;
               case 8:
               case 9:
               case 10:
               case 11:
               case 12:
               case 13:
               case 14:
               case 15:
               case 24:
               case 25:
               case 26:
               case 27:
               case 28:
               case 29:
               case 30:
               case 31:
               case 40:
               case 41:
               case 42:
               case 43:
               case 44:
               case 45:
               case 46:
               case 47:
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
               case 58:
               case 59:
               case 60:
               case 61:
               case 62:
               case 63:
               case 72:
               case 73:
               case 74:
               case 75:
               case 76:
               case 77:
               case 78:
               case 79:
               case 81:
               case 82:
               case 83:
               case 84:
               case 85:
               case 86:
               case 87:
               case 88:
               case 89:
               case 90:
               case 91:
               case 92:
               case 93:
               case 94:
               case 95:
               case 97:
               case 98:
               case 99:
               case 100:
               case 101:
               case 102:
               case 103:
               case 104:
               case 105:
               case 106:
               case 107:
               case 108:
               case 109:
               case 110:
               case 111:
               default:
                  double var6 = var19.mxx;
                  var8 = var19.mxy;
                  double var14 = var19.mxt;
                  var10 = var19.myx;
                  double var12 = var19.myy;
                  double var16 = var19.myt;
                  switch (var18) {
                     case 1:
                        this.mxx = var6;
                        this.mxy = var8;
                        this.mxt += var14;
                        this.myx = var10;
                        this.myy = var12;
                        this.myt += var16;
                        this.state = var20 | 1;
                        this.type = -1;
                        return;
                     case 2:
                     case 3:
                        var2 = this.mxx;
                        this.mxx = var6 * var2;
                        this.mxy = var8 * var2;
                        this.mxt += var14 * var2;
                        var2 = this.myy;
                        this.myx = var10 * var2;
                        this.myy = var12 * var2;
                        this.myt += var16 * var2;
                        break;
                     case 4:
                     case 5:
                        var2 = this.mxy;
                        this.mxx = var10 * var2;
                        this.mxy = var12 * var2;
                        this.mxt += var16 * var2;
                        var2 = this.myx;
                        this.myx = var6 * var2;
                        this.myy = var8 * var2;
                        this.myt += var14 * var2;
                        break;
                     default:
                        stateError();
                     case 6:
                        this.state = var18 | var20;
                     case 7:
                        var2 = this.mxx;
                        double var4 = this.mxy;
                        this.mxx = var6 * var2 + var10 * var4;
                        this.mxy = var8 * var2 + var12 * var4;
                        this.mxt += var14 * var2 + var16 * var4;
                        var2 = this.myx;
                        var4 = this.myy;
                        this.myx = var6 * var2 + var10 * var4;
                        this.myy = var8 * var2 + var12 * var4;
                        this.myt += var14 * var2 + var16 * var4;
                        this.type = -1;
                        return;
                  }

                  this.updateState2D();
                  return;
               case 17:
               case 18:
               case 19:
               case 20:
               case 21:
               case 22:
               case 23:
                  this.translate(var19.mxt, var19.myt);
                  return;
               case 33:
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 39:
                  this.scale(var19.mxx, var19.myy);
                  return;
               case 65:
                  this.mxx = 0.0;
                  this.mxy = var19.mxy;
                  this.myx = var19.myx;
                  this.myy = 0.0;
                  this.state = 5;
                  this.type = -1;
                  return;
               case 66:
               case 67:
                  this.mxy = this.mxx * var19.mxy;
                  this.mxx = 0.0;
                  this.myx = this.myy * var19.myx;
                  this.myy = 0.0;
                  this.state = var18 ^ 6;
                  this.type = -1;
                  return;
               case 68:
               case 69:
                  this.mxx = this.mxy * var19.myx;
                  this.mxy = 0.0;
                  this.myy = this.myx * var19.mxy;
                  this.myx = 0.0;
                  this.state = var18 ^ 6;
                  this.type = -1;
                  return;
               case 70:
               case 71:
                  var8 = var19.mxy;
                  var10 = var19.myx;
                  var2 = this.mxx;
                  this.mxx = this.mxy * var10;
                  this.mxy = var2 * var8;
                  var2 = this.myx;
                  this.myx = this.myy * var10;
                  this.myy = var2 * var8;
                  this.type = -1;
                  return;
               case 80:
                  this.mxt = var19.mxt;
                  this.myt = var19.myt;
               case 64:
                  this.mxy = var19.mxy;
                  this.myx = var19.myx;
                  this.mxx = this.myy = 0.0;
                  this.state = var20;
                  this.type = var19.type;
                  return;
               case 96:
                  this.mxy = var19.mxy;
                  this.myx = var19.myx;
               case 32:
                  this.mxx = var19.mxx;
                  this.myy = var19.myy;
                  this.state = var20;
                  this.type = var19.type;
                  return;
               case 112:
                  this.mxy = var19.mxy;
                  this.myx = var19.myx;
               case 48:
                  this.mxx = var19.mxx;
                  this.myy = var19.myy;
               case 16:
                  this.mxt = var19.mxt;
                  this.myt = var19.myt;
                  this.state = var20;
                  this.type = var19.type;
            }
      }
   }

   public void concatenate(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = this.mxx * var1 + this.mxy * var7;
      double var15 = this.mxx * var3 + this.mxy * var9;
      double var17 = this.mxx * var5 + this.mxy * var11 + this.mxt;
      double var19 = this.myx * var1 + this.myy * var7;
      double var21 = this.myx * var3 + this.myy * var9;
      double var23 = this.myx * var5 + this.myy * var11 + this.myt;
      this.mxx = var13;
      this.mxy = var15;
      this.mxt = var17;
      this.myx = var19;
      this.myy = var21;
      this.myt = var23;
      this.updateState();
   }

   public void invert() throws NoninvertibleTransformException {
      double var1;
      double var3;
      double var5;
      double var7;
      double var9;
      double var11;
      double var13;
      switch (this.state) {
         case 0:
            break;
         case 1:
            this.mxt = -this.mxt;
            this.myt = -this.myt;
            break;
         case 2:
            var1 = this.mxx;
            var9 = this.myy;
            if (var1 == 0.0 || var9 == 0.0) {
               throw new NoninvertibleTransformException("Determinant is 0");
            }

            this.mxx = 1.0 / var1;
            this.myy = 1.0 / var9;
            break;
         case 3:
            var1 = this.mxx;
            var5 = this.mxt;
            var9 = this.myy;
            var11 = this.myt;
            if (var1 == 0.0 || var9 == 0.0) {
               throw new NoninvertibleTransformException("Determinant is 0");
            }

            this.mxx = 1.0 / var1;
            this.myy = 1.0 / var9;
            this.mxt = -var5 / var1;
            this.myt = -var11 / var9;
            break;
         case 4:
            var3 = this.mxy;
            var7 = this.myx;
            if (var3 == 0.0 || var7 == 0.0) {
               throw new NoninvertibleTransformException("Determinant is 0");
            }

            this.myx = 1.0 / var3;
            this.mxy = 1.0 / var7;
            break;
         case 5:
            var3 = this.mxy;
            var5 = this.mxt;
            var7 = this.myx;
            var11 = this.myt;
            if (var3 != 0.0 && var7 != 0.0) {
               this.myx = 1.0 / var3;
               this.mxy = 1.0 / var7;
               this.mxt = -var11 / var7;
               this.myt = -var5 / var3;
               break;
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 6:
            var1 = this.mxx;
            var3 = this.mxy;
            var7 = this.myx;
            var9 = this.myy;
            var13 = var1 * var9 - var3 * var7;
            if (var13 == 0.0 || Math.abs(var13) <= Double.MIN_VALUE) {
               throw new NoninvertibleTransformException("Determinant is " + var13);
            }

            this.mxx = var9 / var13;
            this.myx = -var7 / var13;
            this.mxy = -var3 / var13;
            this.myy = var1 / var13;
            break;
         default:
            stateError();
         case 7:
            var1 = this.mxx;
            var3 = this.mxy;
            var5 = this.mxt;
            var7 = this.myx;
            var9 = this.myy;
            var11 = this.myt;
            var13 = var1 * var9 - var3 * var7;
            if (var13 == 0.0 || Math.abs(var13) <= Double.MIN_VALUE) {
               throw new NoninvertibleTransformException("Determinant is " + var13);
            }

            this.mxx = var9 / var13;
            this.myx = -var7 / var13;
            this.mxy = -var3 / var13;
            this.myy = var1 / var13;
            this.mxt = (var3 * var11 - var9 * var5) / var13;
            this.myt = (var7 * var5 - var1 * var11) / var13;
      }

   }
}
