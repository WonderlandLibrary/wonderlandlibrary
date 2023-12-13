package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.Point2D;

public class Affine2D extends AffineBase {
   private static final long BASE_HASH;

   private Affine2D(double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      this.mxx = var1;
      this.myx = var3;
      this.mxy = var5;
      this.myy = var7;
      this.mxt = var9;
      this.myt = var11;
      this.state = var13;
      this.type = -1;
   }

   public Affine2D() {
      this.mxx = this.myy = 1.0;
   }

   public Affine2D(BaseTransform var1) {
      this.setTransform(var1);
   }

   public Affine2D(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.mxx = (double)var1;
      this.myx = (double)var2;
      this.mxy = (double)var3;
      this.myy = (double)var4;
      this.mxt = (double)var5;
      this.myt = (double)var6;
      this.updateState2D();
   }

   public Affine2D(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.mxx = var1;
      this.myx = var3;
      this.mxy = var5;
      this.myy = var7;
      this.mxt = var9;
      this.myt = var11;
      this.updateState2D();
   }

   public BaseTransform.Degree getDegree() {
      return BaseTransform.Degree.AFFINE_2D;
   }

   protected void reset3Delements() {
   }

   public void rotate(double var1, double var3, double var5) {
      this.translate(var3, var5);
      this.rotate(var1);
      this.translate(-var3, -var5);
   }

   public void rotate(double var1, double var3) {
      if (var3 == 0.0) {
         if (var1 < 0.0) {
            this.rotate180();
         }
      } else if (var1 == 0.0) {
         if (var3 > 0.0) {
            this.rotate90();
         } else {
            this.rotate270();
         }
      } else {
         double var5 = Math.sqrt(var1 * var1 + var3 * var3);
         double var7 = var3 / var5;
         double var9 = var1 / var5;
         double var11 = this.mxx;
         double var13 = this.mxy;
         this.mxx = var9 * var11 + var7 * var13;
         this.mxy = -var7 * var11 + var9 * var13;
         var11 = this.myx;
         var13 = this.myy;
         this.myx = var9 * var11 + var7 * var13;
         this.myy = -var7 * var11 + var9 * var13;
         this.updateState2D();
      }

   }

   public void rotate(double var1, double var3, double var5, double var7) {
      this.translate(var5, var7);
      this.rotate(var1, var3);
      this.translate(-var5, -var7);
   }

   public void quadrantRotate(int var1) {
      switch (var1 & 3) {
         case 0:
         default:
            break;
         case 1:
            this.rotate90();
            break;
         case 2:
            this.rotate180();
            break;
         case 3:
            this.rotate270();
      }

   }

   public void quadrantRotate(int var1, double var2, double var4) {
      switch (var1 & 3) {
         case 0:
            return;
         case 1:
            this.mxt += var2 * (this.mxx - this.mxy) + var4 * (this.mxy + this.mxx);
            this.myt += var2 * (this.myx - this.myy) + var4 * (this.myy + this.myx);
            this.rotate90();
            break;
         case 2:
            this.mxt += var2 * (this.mxx + this.mxx) + var4 * (this.mxy + this.mxy);
            this.myt += var2 * (this.myx + this.myx) + var4 * (this.myy + this.myy);
            this.rotate180();
            break;
         case 3:
            this.mxt += var2 * (this.mxx + this.mxy) + var4 * (this.mxy - this.mxx);
            this.myt += var2 * (this.myx + this.myy) + var4 * (this.myy - this.myx);
            this.rotate270();
      }

      if (this.mxt == 0.0 && this.myt == 0.0) {
         this.state &= -2;
         if (this.type != -1) {
            this.type &= -2;
         }
      } else {
         this.state |= 1;
         this.type |= 1;
      }

   }

   public void setToTranslation(double var1, double var3) {
      this.mxx = 1.0;
      this.myx = 0.0;
      this.mxy = 0.0;
      this.myy = 1.0;
      this.mxt = var1;
      this.myt = var3;
      if (var1 == 0.0 && var3 == 0.0) {
         this.state = 0;
         this.type = 0;
      } else {
         this.state = 1;
         this.type = 1;
      }

   }

   public void setToRotation(double var1) {
      double var3 = Math.sin(var1);
      double var5;
      if (var3 != 1.0 && var3 != -1.0) {
         var5 = Math.cos(var1);
         if (var5 == -1.0) {
            var3 = 0.0;
            this.state = 2;
            this.type = 8;
         } else if (var5 == 1.0) {
            var3 = 0.0;
            this.state = 0;
            this.type = 0;
         } else {
            this.state = 6;
            this.type = 16;
         }
      } else {
         var5 = 0.0;
         this.state = 4;
         this.type = 8;
      }

      this.mxx = var5;
      this.myx = var3;
      this.mxy = -var3;
      this.myy = var5;
      this.mxt = 0.0;
      this.myt = 0.0;
   }

   public void setToRotation(double var1, double var3, double var5) {
      this.setToRotation(var1);
      double var7 = this.myx;
      double var9 = 1.0 - this.mxx;
      this.mxt = var3 * var9 + var5 * var7;
      this.myt = var5 * var9 - var3 * var7;
      if (this.mxt != 0.0 || this.myt != 0.0) {
         this.state |= 1;
         this.type |= 1;
      }

   }

   public void setToRotation(double var1, double var3) {
      double var5;
      double var7;
      if (var3 == 0.0) {
         var5 = 0.0;
         if (var1 < 0.0) {
            var7 = -1.0;
            this.state = 2;
            this.type = 8;
         } else {
            var7 = 1.0;
            this.state = 0;
            this.type = 0;
         }
      } else if (var1 == 0.0) {
         var7 = 0.0;
         var5 = var3 > 0.0 ? 1.0 : -1.0;
         this.state = 4;
         this.type = 8;
      } else {
         double var9 = Math.sqrt(var1 * var1 + var3 * var3);
         var7 = var1 / var9;
         var5 = var3 / var9;
         this.state = 6;
         this.type = 16;
      }

      this.mxx = var7;
      this.myx = var5;
      this.mxy = -var5;
      this.myy = var7;
      this.mxt = 0.0;
      this.myt = 0.0;
   }

   public void setToRotation(double var1, double var3, double var5, double var7) {
      this.setToRotation(var1, var3);
      double var9 = this.myx;
      double var11 = 1.0 - this.mxx;
      this.mxt = var5 * var11 + var7 * var9;
      this.myt = var7 * var11 - var5 * var9;
      if (this.mxt != 0.0 || this.myt != 0.0) {
         this.state |= 1;
         this.type |= 1;
      }

   }

   public void setToQuadrantRotation(int var1) {
      switch (var1 & 3) {
         case 0:
            this.mxx = 1.0;
            this.myx = 0.0;
            this.mxy = 0.0;
            this.myy = 1.0;
            this.mxt = 0.0;
            this.myt = 0.0;
            this.state = 0;
            this.type = 0;
            break;
         case 1:
            this.mxx = 0.0;
            this.myx = 1.0;
            this.mxy = -1.0;
            this.myy = 0.0;
            this.mxt = 0.0;
            this.myt = 0.0;
            this.state = 4;
            this.type = 8;
            break;
         case 2:
            this.mxx = -1.0;
            this.myx = 0.0;
            this.mxy = 0.0;
            this.myy = -1.0;
            this.mxt = 0.0;
            this.myt = 0.0;
            this.state = 2;
            this.type = 8;
            break;
         case 3:
            this.mxx = 0.0;
            this.myx = -1.0;
            this.mxy = 1.0;
            this.myy = 0.0;
            this.mxt = 0.0;
            this.myt = 0.0;
            this.state = 4;
            this.type = 8;
      }

   }

   public void setToQuadrantRotation(int var1, double var2, double var4) {
      switch (var1 & 3) {
         case 0:
            this.mxx = 1.0;
            this.myx = 0.0;
            this.mxy = 0.0;
            this.myy = 1.0;
            this.mxt = 0.0;
            this.myt = 0.0;
            this.state = 0;
            this.type = 0;
            break;
         case 1:
            this.mxx = 0.0;
            this.myx = 1.0;
            this.mxy = -1.0;
            this.myy = 0.0;
            this.mxt = var2 + var4;
            this.myt = var4 - var2;
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 4;
               this.type = 8;
            } else {
               this.state = 5;
               this.type = 9;
            }
            break;
         case 2:
            this.mxx = -1.0;
            this.myx = 0.0;
            this.mxy = 0.0;
            this.myy = -1.0;
            this.mxt = var2 + var2;
            this.myt = var4 + var4;
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 2;
               this.type = 8;
            } else {
               this.state = 3;
               this.type = 9;
            }
            break;
         case 3:
            this.mxx = 0.0;
            this.myx = -1.0;
            this.mxy = 1.0;
            this.myy = 0.0;
            this.mxt = var2 - var4;
            this.myt = var4 + var2;
            if (this.mxt == 0.0 && this.myt == 0.0) {
               this.state = 4;
               this.type = 8;
            } else {
               this.state = 5;
               this.type = 9;
            }
      }

   }

   public void setToScale(double var1, double var3) {
      this.mxx = var1;
      this.myx = 0.0;
      this.mxy = 0.0;
      this.myy = var3;
      this.mxt = 0.0;
      this.myt = 0.0;
      if (var1 == 1.0 && var3 == 1.0) {
         this.state = 0;
         this.type = 0;
      } else {
         this.state = 2;
         this.type = -1;
      }

   }

   public void setTransform(BaseTransform var1) {
      switch (var1.getDegree()) {
         case IDENTITY:
            this.setToIdentity();
            break;
         case TRANSLATE_2D:
            this.setToTranslation(var1.getMxt(), var1.getMyt());
            break;
         default:
            if (var1.getType() > 127) {
               System.out.println(var1 + " is " + var1.getType());
               System.out.print("  " + var1.getMxx());
               System.out.print(", " + var1.getMxy());
               System.out.print(", " + var1.getMxz());
               System.out.print(", " + var1.getMxt());
               System.out.println();
               System.out.print("  " + var1.getMyx());
               System.out.print(", " + var1.getMyy());
               System.out.print(", " + var1.getMyz());
               System.out.print(", " + var1.getMyt());
               System.out.println();
               System.out.print("  " + var1.getMzx());
               System.out.print(", " + var1.getMzy());
               System.out.print(", " + var1.getMzz());
               System.out.print(", " + var1.getMzt());
               System.out.println();
               degreeError(BaseTransform.Degree.AFFINE_2D);
            }
         case AFFINE_2D:
            this.mxx = var1.getMxx();
            this.myx = var1.getMyx();
            this.mxy = var1.getMxy();
            this.myy = var1.getMyy();
            this.mxt = var1.getMxt();
            this.myt = var1.getMyt();
            if (var1 instanceof AffineBase) {
               this.state = ((AffineBase)var1).state;
               this.type = ((AffineBase)var1).type;
            } else {
               this.updateState2D();
            }
      }

   }

   public void preConcatenate(BaseTransform var1) {
      switch (var1.getDegree()) {
         case IDENTITY:
            return;
         case TRANSLATE_2D:
            this.translate(var1.getMxt(), var1.getMyt());
            return;
         default:
            degreeError(BaseTransform.Degree.AFFINE_2D);
         case AFFINE_2D:
            int var18 = this.state;
            Affine2D var19 = (Affine2D)var1;
            int var20 = var19.state;
            double var2;
            double var6;
            double var8;
            double var10;
            double var12;
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
               case 48:
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
               default:
                  var6 = var19.mxx;
                  var8 = var19.mxy;
                  double var14 = var19.mxt;
                  var10 = var19.myx;
                  var12 = var19.myy;
                  double var16 = var19.myt;
                  double var4;
                  switch (var18) {
                     case 1:
                        var2 = this.mxt;
                        var4 = this.myt;
                        var14 += var2 * var6 + var4 * var8;
                        var16 += var2 * var10 + var4 * var12;
                     case 0:
                        this.mxt = var14;
                        this.myt = var16;
                        this.mxx = var6;
                        this.myx = var10;
                        this.mxy = var8;
                        this.myy = var12;
                        this.state = var18 | var20;
                        this.type = -1;
                        return;
                     case 3:
                        var2 = this.mxt;
                        var4 = this.myt;
                        var14 += var2 * var6 + var4 * var8;
                        var16 += var2 * var10 + var4 * var12;
                     case 2:
                        this.mxt = var14;
                        this.myt = var16;
                        var2 = this.mxx;
                        this.mxx = var2 * var6;
                        this.myx = var2 * var10;
                        var2 = this.myy;
                        this.mxy = var2 * var8;
                        this.myy = var2 * var12;
                        break;
                     case 5:
                        var2 = this.mxt;
                        var4 = this.myt;
                        var14 += var2 * var6 + var4 * var8;
                        var16 += var2 * var10 + var4 * var12;
                     case 4:
                        this.mxt = var14;
                        this.myt = var16;
                        var2 = this.myx;
                        this.mxx = var2 * var8;
                        this.myx = var2 * var12;
                        var2 = this.mxy;
                        this.mxy = var2 * var6;
                        this.myy = var2 * var10;
                        break;
                     default:
                        stateError();
                     case 7:
                        var2 = this.mxt;
                        var4 = this.myt;
                        var14 += var2 * var6 + var4 * var8;
                        var16 += var2 * var10 + var4 * var12;
                     case 6:
                        this.mxt = var14;
                        this.myt = var16;
                        var2 = this.mxx;
                        var4 = this.myx;
                        this.mxx = var2 * var6 + var4 * var8;
                        this.myx = var2 * var10 + var4 * var12;
                        var2 = this.mxy;
                        var4 = this.myy;
                        this.mxy = var2 * var6 + var4 * var8;
                        this.myy = var2 * var10 + var4 * var12;
                  }

                  this.updateState2D();
                  return;
               case 16:
               case 18:
               case 20:
               case 22:
                  this.mxt = var19.mxt;
                  this.myt = var19.myt;
                  this.state = var18 | 1;
                  this.type |= 1;
                  return;
               case 17:
               case 19:
               case 21:
               case 23:
                  this.mxt += var19.mxt;
                  this.myt += var19.myt;
                  return;
               case 32:
               case 33:
                  this.state = var18 | 2;
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 39:
                  var6 = var19.mxx;
                  var12 = var19.myy;
                  if ((var18 & 4) != 0) {
                     this.mxy *= var6;
                     this.myx *= var12;
                     if ((var18 & 2) != 0) {
                        this.mxx *= var6;
                        this.myy *= var12;
                     }
                  } else {
                     this.mxx *= var6;
                     this.myy *= var12;
                  }

                  if ((var18 & 1) != 0) {
                     this.mxt *= var6;
                     this.myt *= var12;
                  }

                  this.type = -1;
                  return;
               case 68:
               case 69:
                  var18 |= 2;
               case 64:
               case 65:
               case 66:
               case 67:
                  this.state = var18 ^ 4;
               case 70:
               case 71:
                  var8 = var19.mxy;
                  var10 = var19.myx;
                  var2 = this.mxx;
                  this.mxx = this.myx * var8;
                  this.myx = var2 * var10;
                  var2 = this.mxy;
                  this.mxy = this.myy * var8;
                  this.myy = var2 * var10;
                  var2 = this.mxt;
                  this.mxt = this.myt * var8;
                  this.myt = var2 * var10;
                  this.type = -1;
            }
      }
   }

   public Affine2D createInverse() throws NoninvertibleTransformException {
      double var1;
      switch (this.state) {
         case 0:
            return new Affine2D();
         case 1:
            return new Affine2D(1.0, 0.0, 0.0, 1.0, -this.mxt, -this.myt, 1);
         case 2:
            if (this.mxx != 0.0 && this.myy != 0.0) {
               return new Affine2D(1.0 / this.mxx, 0.0, 0.0, 1.0 / this.myy, 0.0, 0.0, 2);
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 3:
            if (this.mxx != 0.0 && this.myy != 0.0) {
               return new Affine2D(1.0 / this.mxx, 0.0, 0.0, 1.0 / this.myy, -this.mxt / this.mxx, -this.myt / this.myy, 3);
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 4:
            if (this.mxy != 0.0 && this.myx != 0.0) {
               return new Affine2D(0.0, 1.0 / this.mxy, 1.0 / this.myx, 0.0, 0.0, 0.0, 4);
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 5:
            if (this.mxy != 0.0 && this.myx != 0.0) {
               return new Affine2D(0.0, 1.0 / this.mxy, 1.0 / this.myx, 0.0, -this.myt / this.myx, -this.mxt / this.mxy, 5);
            }

            throw new NoninvertibleTransformException("Determinant is 0");
         case 6:
            var1 = this.mxx * this.myy - this.mxy * this.myx;
            if (var1 != 0.0 && !(Math.abs(var1) <= Double.MIN_VALUE)) {
               return new Affine2D(this.myy / var1, -this.myx / var1, -this.mxy / var1, this.mxx / var1, 0.0, 0.0, 6);
            }

            throw new NoninvertibleTransformException("Determinant is " + var1);
         default:
            stateError();
         case 7:
            var1 = this.mxx * this.myy - this.mxy * this.myx;
            if (var1 != 0.0 && !(Math.abs(var1) <= Double.MIN_VALUE)) {
               return new Affine2D(this.myy / var1, -this.myx / var1, -this.mxy / var1, this.mxx / var1, (this.mxy * this.myt - this.myy * this.mxt) / var1, (this.myx * this.mxt - this.mxx * this.myt) / var1, 7);
            } else {
               throw new NoninvertibleTransformException("Determinant is " + var1);
            }
      }
   }

   public void transform(Point2D[] var1, int var2, Point2D[] var3, int var4, int var5) {
      int var6 = this.state;

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         Point2D var7 = var1[var2++];
         double var8 = (double)var7.x;
         double var10 = (double)var7.y;
         Point2D var12 = var3[var4++];
         if (var12 == null) {
            var12 = new Point2D();
            var3[var4 - 1] = var12;
         }

         switch (var6) {
            case 0:
               var12.setLocation((float)var8, (float)var10);
               break;
            case 1:
               var12.setLocation((float)(var8 + this.mxt), (float)(var10 + this.myt));
               break;
            case 2:
               var12.setLocation((float)(var8 * this.mxx), (float)(var10 * this.myy));
               break;
            case 3:
               var12.setLocation((float)(var8 * this.mxx + this.mxt), (float)(var10 * this.myy + this.myt));
               break;
            case 4:
               var12.setLocation((float)(var10 * this.mxy), (float)(var8 * this.myx));
               break;
            case 5:
               var12.setLocation((float)(var10 * this.mxy + this.mxt), (float)(var8 * this.myx + this.myt));
               break;
            case 6:
               var12.setLocation((float)(var8 * this.mxx + var10 * this.mxy), (float)(var8 * this.myx + var10 * this.myy));
               break;
            default:
               stateError();
            case 7:
               var12.setLocation((float)(var8 * this.mxx + var10 * this.mxy + this.mxt), (float)(var8 * this.myx + var10 * this.myy + this.myt));
         }
      }
   }

   public Point2D deltaTransform(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = new Point2D();
      }

      double var3 = (double)var1.x;
      double var5 = (double)var1.y;
      switch (this.state) {
         case 0:
         case 1:
            var2.setLocation((float)var3, (float)var5);
            return var2;
         case 2:
         case 3:
            var2.setLocation((float)(var3 * this.mxx), (float)(var5 * this.myy));
            return var2;
         case 4:
         case 5:
            var2.setLocation((float)(var5 * this.mxy), (float)(var3 * this.myx));
            return var2;
         default:
            stateError();
         case 6:
         case 7:
            var2.setLocation((float)(var3 * this.mxx + var5 * this.mxy), (float)(var3 * this.myx + var5 * this.myy));
            return var2;
      }
   }

   private static double _matround(double var0) {
      return Math.rint(var0 * 1.0E15) / 1.0E15;
   }

   public String toString() {
      return "Affine2D[[" + _matround(this.mxx) + ", " + _matround(this.mxy) + ", " + _matround(this.mxt) + "], [" + _matround(this.myx) + ", " + _matround(this.myy) + ", " + _matround(this.myt) + "]]";
   }

   public boolean is2D() {
      return true;
   }

   public void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.setTransform(var1, var3, var5, var7, var9, var11);
   }

   public void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (var5 != 0.0 || var13 != 0.0 || var17 != 0.0 || var19 != 0.0 || var21 != 1.0 || var23 != 0.0) {
         degreeError(BaseTransform.Degree.AFFINE_2D);
      }

      this.setTransform(var1, var9, var3, var11, var7, var15);
   }

   public BaseTransform deriveWithTranslation(double var1, double var3) {
      this.translate(var1, var3);
      return this;
   }

   public BaseTransform deriveWithTranslation(double var1, double var3, double var5) {
      if (var5 == 0.0) {
         this.translate(var1, var3);
         return this;
      } else {
         Affine3D var7 = new Affine3D(this);
         var7.translate(var1, var3, var5);
         return var7;
      }
   }

   public BaseTransform deriveWithScale(double var1, double var3, double var5) {
      if (var5 == 1.0) {
         this.scale(var1, var3);
         return this;
      } else {
         Affine3D var7 = new Affine3D(this);
         var7.scale(var1, var3, var5);
         return var7;
      }
   }

   public BaseTransform deriveWithRotation(double var1, double var3, double var5, double var7) {
      if (var1 == 0.0) {
         return this;
      } else if (almostZero(var3) && almostZero(var5)) {
         if (var7 > 0.0) {
            this.rotate(var1);
         } else if (var7 < 0.0) {
            this.rotate(-var1);
         }

         return this;
      } else {
         Affine3D var9 = new Affine3D(this);
         var9.rotate(var1, var3, var5, var7);
         return var9;
      }
   }

   public BaseTransform deriveWithPreTranslation(double var1, double var3) {
      this.mxt += var1;
      this.myt += var3;
      if (this.mxt == 0.0 && this.myt == 0.0) {
         this.state &= -2;
         if (this.type != -1) {
            this.type &= -2;
         }
      } else {
         this.state |= 1;
         this.type |= 1;
      }

      return this;
   }

   public BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11) {
      BaseTransform var13 = getInstance(var1, var3, var5, var7, var9, var11);
      this.concatenate(var13);
      return this;
   }

   public BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (var5 == 0.0 && var13 == 0.0 && var17 == 0.0 && var19 == 0.0 && var21 == 1.0 && var23 == 0.0) {
         this.concatenate(var1, var3, var7, var9, var11, var15);
         return this;
      } else {
         Affine3D var25 = new Affine3D(this);
         var25.concatenate(var1, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23);
         return var25;
      }
   }

   public BaseTransform deriveWithConcatenation(BaseTransform var1) {
      if (var1.is2D()) {
         this.concatenate(var1);
         return this;
      } else {
         Affine3D var2 = new Affine3D(this);
         var2.concatenate(var1);
         return var2;
      }
   }

   public BaseTransform deriveWithPreConcatenation(BaseTransform var1) {
      if (var1.is2D()) {
         this.preConcatenate(var1);
         return this;
      } else {
         Affine3D var2 = new Affine3D(this);
         var2.preConcatenate(var1);
         return var2;
      }
   }

   public BaseTransform deriveWithNewTransform(BaseTransform var1) {
      if (var1.is2D()) {
         this.setTransform(var1);
         return this;
      } else {
         return getInstance(var1);
      }
   }

   public BaseTransform copy() {
      return new Affine2D(this);
   }

   public int hashCode() {
      if (this.isIdentity()) {
         return 0;
      } else {
         long var1 = BASE_HASH;
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMyy());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMyx());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMxy());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMxx());
         var1 = var1 * 31L + Double.doubleToLongBits(0.0);
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMyt());
         var1 = var1 * 31L + Double.doubleToLongBits(this.getMxt());
         return (int)var1 ^ (int)(var1 >> 32);
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof BaseTransform)) {
         return false;
      } else {
         BaseTransform var2 = (BaseTransform)var1;
         return var2.getType() <= 127 && var2.getMxx() == this.mxx && var2.getMxy() == this.mxy && var2.getMxt() == this.mxt && var2.getMyx() == this.myx && var2.getMyy() == this.myy && var2.getMyt() == this.myt;
      }
   }

   static {
      long var0 = 0L;
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzz());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzy());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMzx());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMyz());
      var0 = var0 * 31L + Double.doubleToLongBits(IDENTITY_TRANSFORM.getMxz());
      BASE_HASH = var0;
   }
}
