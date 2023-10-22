package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class Affine extends Transform {
   AffineAtomicChange atomicChange;
   private static final int APPLY_IDENTITY = 0;
   private static final int APPLY_TRANSLATE = 1;
   private static final int APPLY_SCALE = 2;
   private static final int APPLY_SHEAR = 4;
   private static final int APPLY_NON_3D = 0;
   private static final int APPLY_3D_COMPLEX = 4;
   private transient int state2d;
   private transient int state3d;
   private double xx;
   private double xy;
   private double xz;
   private double yx;
   private double yy;
   private double yz;
   private double zx;
   private double zy;
   private double zz;
   private double xt;
   private double yt;
   private double zt;
   private AffineElementProperty mxx;
   private AffineElementProperty mxy;
   private AffineElementProperty mxz;
   private AffineElementProperty tx;
   private AffineElementProperty myx;
   private AffineElementProperty myy;
   private AffineElementProperty myz;
   private AffineElementProperty ty;
   private AffineElementProperty mzx;
   private AffineElementProperty mzy;
   private AffineElementProperty mzz;
   private AffineElementProperty tz;
   private static final int[] rot90conversion = new int[]{4, 5, 4, 5, 2, 3, 6, 7};

   public Affine() {
      this.atomicChange = new AffineAtomicChange();
      this.xx = this.yy = this.zz = 1.0;
   }

   public Affine(Transform var1) {
      this(var1.getMxx(), var1.getMxy(), var1.getMxz(), var1.getTx(), var1.getMyx(), var1.getMyy(), var1.getMyz(), var1.getTy(), var1.getMzx(), var1.getMzy(), var1.getMzz(), var1.getTz());
   }

   public Affine(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.atomicChange = new AffineAtomicChange();
      this.xx = var1;
      this.xy = var3;
      this.xt = var5;
      this.yx = var7;
      this.yy = var9;
      this.yt = var11;
      this.zz = 1.0;
      this.updateState2D();
   }

   public Affine(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.atomicChange = new AffineAtomicChange();
      this.xx = var1;
      this.xy = var3;
      this.xz = var5;
      this.xt = var7;
      this.yx = var9;
      this.yy = var11;
      this.yz = var13;
      this.yt = var15;
      this.zx = var17;
      this.zy = var19;
      this.zz = var21;
      this.zt = var23;
      this.updateState();
   }

   public Affine(double[] var1, MatrixType var2, int var3) {
      this.atomicChange = new AffineAtomicChange();
      if (var1.length < var3 + var2.elements()) {
         throw new IndexOutOfBoundsException("The array is too short.");
      } else {
         switch (var2) {
            case MT_2D_2x3:
               break;
            case MT_3D_4x4:
               if (var1[var3 + 12] != 0.0 || var1[var3 + 13] != 0.0 || var1[var3 + 14] != 0.0 || var1[var3 + 15] != 1.0) {
                  throw new IllegalArgumentException("The matrix is not affine");
               }
            case MT_3D_3x4:
               this.xx = var1[var3++];
               this.xy = var1[var3++];
               this.xz = var1[var3++];
               this.xt = var1[var3++];
               this.yx = var1[var3++];
               this.yy = var1[var3++];
               this.yz = var1[var3++];
               this.yt = var1[var3++];
               this.zx = var1[var3++];
               this.zy = var1[var3++];
               this.zz = var1[var3++];
               this.zt = var1[var3];
               this.updateState();
               return;
            default:
               stateError();
            case MT_2D_3x3:
               if (var1[var3 + 6] != 0.0 || var1[var3 + 7] != 0.0 || var1[var3 + 8] != 1.0) {
                  throw new IllegalArgumentException("The matrix is not affine");
               }
         }

         this.xx = var1[var3++];
         this.xy = var1[var3++];
         this.xt = var1[var3++];
         this.yx = var1[var3++];
         this.yy = var1[var3++];
         this.yt = var1[var3];
         this.zz = 1.0;
         this.updateState2D();
      }
   }

   public final void setMxx(double var1) {
      if (this.mxx == null) {
         if (this.xx != var1) {
            this.xx = var1;
            this.postProcessChange();
         }
      } else {
         this.mxxProperty().set(var1);
      }

   }

   public final double getMxx() {
      return this.mxx == null ? this.xx : this.mxx.get();
   }

   public final DoubleProperty mxxProperty() {
      if (this.mxx == null) {
         this.mxx = new AffineElementProperty(this.xx) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "mxx";
            }
         };
      }

      return this.mxx;
   }

   public final void setMxy(double var1) {
      if (this.mxy == null) {
         if (this.xy != var1) {
            this.xy = var1;
            this.postProcessChange();
         }
      } else {
         this.mxyProperty().set(var1);
      }

   }

   public final double getMxy() {
      return this.mxy == null ? this.xy : this.mxy.get();
   }

   public final DoubleProperty mxyProperty() {
      if (this.mxy == null) {
         this.mxy = new AffineElementProperty(this.xy) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "mxy";
            }
         };
      }

      return this.mxy;
   }

   public final void setMxz(double var1) {
      if (this.mxz == null) {
         if (this.xz != var1) {
            this.xz = var1;
            this.postProcessChange();
         }
      } else {
         this.mxzProperty().set(var1);
      }

   }

   public final double getMxz() {
      return this.mxz == null ? this.xz : this.mxz.get();
   }

   public final DoubleProperty mxzProperty() {
      if (this.mxz == null) {
         this.mxz = new AffineElementProperty(this.xz) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "mxz";
            }
         };
      }

      return this.mxz;
   }

   public final void setTx(double var1) {
      if (this.tx == null) {
         if (this.xt != var1) {
            this.xt = var1;
            this.postProcessChange();
         }
      } else {
         this.txProperty().set(var1);
      }

   }

   public final double getTx() {
      return this.tx == null ? this.xt : this.tx.get();
   }

   public final DoubleProperty txProperty() {
      if (this.tx == null) {
         this.tx = new AffineElementProperty(this.xt) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "tx";
            }
         };
      }

      return this.tx;
   }

   public final void setMyx(double var1) {
      if (this.myx == null) {
         if (this.yx != var1) {
            this.yx = var1;
            this.postProcessChange();
         }
      } else {
         this.myxProperty().set(var1);
      }

   }

   public final double getMyx() {
      return this.myx == null ? this.yx : this.myx.get();
   }

   public final DoubleProperty myxProperty() {
      if (this.myx == null) {
         this.myx = new AffineElementProperty(this.yx) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "myx";
            }
         };
      }

      return this.myx;
   }

   public final void setMyy(double var1) {
      if (this.myy == null) {
         if (this.yy != var1) {
            this.yy = var1;
            this.postProcessChange();
         }
      } else {
         this.myyProperty().set(var1);
      }

   }

   public final double getMyy() {
      return this.myy == null ? this.yy : this.myy.get();
   }

   public final DoubleProperty myyProperty() {
      if (this.myy == null) {
         this.myy = new AffineElementProperty(this.yy) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "myy";
            }
         };
      }

      return this.myy;
   }

   public final void setMyz(double var1) {
      if (this.myz == null) {
         if (this.yz != var1) {
            this.yz = var1;
            this.postProcessChange();
         }
      } else {
         this.myzProperty().set(var1);
      }

   }

   public final double getMyz() {
      return this.myz == null ? this.yz : this.myz.get();
   }

   public final DoubleProperty myzProperty() {
      if (this.myz == null) {
         this.myz = new AffineElementProperty(this.yz) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "myz";
            }
         };
      }

      return this.myz;
   }

   public final void setTy(double var1) {
      if (this.ty == null) {
         if (this.yt != var1) {
            this.yt = var1;
            this.postProcessChange();
         }
      } else {
         this.tyProperty().set(var1);
      }

   }

   public final double getTy() {
      return this.ty == null ? this.yt : this.ty.get();
   }

   public final DoubleProperty tyProperty() {
      if (this.ty == null) {
         this.ty = new AffineElementProperty(this.yt) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "ty";
            }
         };
      }

      return this.ty;
   }

   public final void setMzx(double var1) {
      if (this.mzx == null) {
         if (this.zx != var1) {
            this.zx = var1;
            this.postProcessChange();
         }
      } else {
         this.mzxProperty().set(var1);
      }

   }

   public final double getMzx() {
      return this.mzx == null ? this.zx : this.mzx.get();
   }

   public final DoubleProperty mzxProperty() {
      if (this.mzx == null) {
         this.mzx = new AffineElementProperty(this.zx) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "mzx";
            }
         };
      }

      return this.mzx;
   }

   public final void setMzy(double var1) {
      if (this.mzy == null) {
         if (this.zy != var1) {
            this.zy = var1;
            this.postProcessChange();
         }
      } else {
         this.mzyProperty().set(var1);
      }

   }

   public final double getMzy() {
      return this.mzy == null ? this.zy : this.mzy.get();
   }

   public final DoubleProperty mzyProperty() {
      if (this.mzy == null) {
         this.mzy = new AffineElementProperty(this.zy) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "mzy";
            }
         };
      }

      return this.mzy;
   }

   public final void setMzz(double var1) {
      if (this.mzz == null) {
         if (this.zz != var1) {
            this.zz = var1;
            this.postProcessChange();
         }
      } else {
         this.mzzProperty().set(var1);
      }

   }

   public final double getMzz() {
      return this.mzz == null ? this.zz : this.mzz.get();
   }

   public final DoubleProperty mzzProperty() {
      if (this.mzz == null) {
         this.mzz = new AffineElementProperty(this.zz) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "mzz";
            }
         };
      }

      return this.mzz;
   }

   public final void setTz(double var1) {
      if (this.tz == null) {
         if (this.zt != var1) {
            this.zt = var1;
            this.postProcessChange();
         }
      } else {
         this.tzProperty().set(var1);
      }

   }

   public final double getTz() {
      return this.tz == null ? this.zt : this.tz.get();
   }

   public final DoubleProperty tzProperty() {
      if (this.tz == null) {
         this.tz = new AffineElementProperty(this.zt) {
            public Object getBean() {
               return Affine.this;
            }

            public String getName() {
               return "tz";
            }
         };
      }

      return this.tz;
   }

   public void setElement(MatrixType var1, int var2, int var3, double var4) {
      if (var2 >= 0 && var2 < var1.rows() && var3 >= 0 && var3 < var1.columns()) {
         switch (var1) {
            case MT_3D_4x4:
            case MT_3D_3x4:
               switch (var2) {
                  case 0:
                     switch (var3) {
                        case 0:
                           this.setMxx(var4);
                           return;
                        case 1:
                           this.setMxy(var4);
                           return;
                        case 2:
                           this.setMxz(var4);
                           return;
                        case 3:
                           this.setTx(var4);
                           return;
                     }
                  case 1:
                     switch (var3) {
                        case 0:
                           this.setMyx(var4);
                           return;
                        case 1:
                           this.setMyy(var4);
                           return;
                        case 2:
                           this.setMyz(var4);
                           return;
                        case 3:
                           this.setTy(var4);
                           return;
                     }
                  case 2:
                     switch (var3) {
                        case 0:
                           this.setMzx(var4);
                           return;
                        case 1:
                           this.setMzy(var4);
                           return;
                        case 2:
                           this.setMzz(var4);
                           return;
                        case 3:
                           this.setTz(var4);
                           return;
                     }
                  case 3:
                     switch (var3) {
                        case 0:
                           if (var4 == 0.0) {
                              return;
                           }

                           throw new IllegalArgumentException("Cannot set affine matrix " + var1 + " element [" + var2 + ", " + var3 + "] to " + var4);
                        case 1:
                           if (var4 == 0.0) {
                              return;
                           }

                           throw new IllegalArgumentException("Cannot set affine matrix " + var1 + " element [" + var2 + ", " + var3 + "] to " + var4);
                        case 2:
                           if (var4 == 0.0) {
                              return;
                           }

                           throw new IllegalArgumentException("Cannot set affine matrix " + var1 + " element [" + var2 + ", " + var3 + "] to " + var4);
                        case 3:
                           if (var4 == 1.0) {
                              return;
                           }

                           throw new IllegalArgumentException("Cannot set affine matrix " + var1 + " element [" + var2 + ", " + var3 + "] to " + var4);
                     }
                  default:
                     throw new IllegalArgumentException("Cannot set affine matrix " + var1 + " element [" + var2 + ", " + var3 + "] to " + var4);
               }
            default:
               stateError();
            case MT_2D_3x3:
            case MT_2D_2x3:
               if (!this.isType2D()) {
                  throw new IllegalArgumentException("Cannot access 2D matrix of a 3D transform");
               }

               switch (var2) {
                  case 0:
                     switch (var3) {
                        case 0:
                           this.setMxx(var4);
                           return;
                        case 1:
                           this.setMxy(var4);
                           return;
                        case 2:
                           this.setTx(var4);
                           return;
                     }
                  case 1:
                     switch (var3) {
                        case 0:
                           this.setMyx(var4);
                           return;
                        case 1:
                           this.setMyy(var4);
                           return;
                        case 2:
                           this.setTy(var4);
                           return;
                     }
                  case 2:
                     switch (var3) {
                        case 0:
                           if (var4 == 0.0) {
                              return;
                           }
                           break;
                        case 1:
                           if (var4 == 0.0) {
                              return;
                           }
                           break;
                        case 2:
                           if (var4 == 1.0) {
                              return;
                           }
                     }
               }
         }

         throw new IllegalArgumentException("Cannot set affine matrix " + var1 + " element [" + var2 + ", " + var3 + "] to " + var4);
      } else {
         throw new IndexOutOfBoundsException("Index outside of affine matrix " + var1 + ": [" + var2 + ", " + var3 + "]");
      }
   }

   private void postProcessChange() {
      if (!this.atomicChange.runs()) {
         this.updateState();
         this.transformChanged();
      }

   }

   boolean computeIs2D() {
      return this.state3d == 0;
   }

   boolean computeIsIdentity() {
      return this.state3d == 0 && this.state2d == 0;
   }

   public double determinant() {
      return this.state3d == 0 ? this.getDeterminant2D() : this.getDeterminant3D();
   }

   private double getDeterminant2D() {
      switch (this.state2d) {
         case 0:
         case 1:
            return 1.0;
         case 2:
         case 3:
            return this.getMxx() * this.getMyy();
         case 4:
         case 5:
            return -(this.getMxy() * this.getMyx());
         default:
            stateError();
         case 6:
         case 7:
            return this.getMxx() * this.getMyy() - this.getMxy() * this.getMyx();
      }
   }

   private double getDeterminant3D() {
      switch (this.state3d) {
         case 2:
         case 3:
            return this.getMxx() * this.getMyy() * this.getMzz();
         case 4:
            double var1 = this.getMyx();
            double var3 = this.getMyy();
            double var5 = this.getMyz();
            double var7 = this.getMzx();
            double var9 = this.getMzy();
            double var11 = this.getMzz();
            return this.getMxx() * (var3 * var11 - var9 * var5) + this.getMxy() * (var5 * var7 - var11 * var1) + this.getMxz() * (var1 * var9 - var7 * var3);
         default:
            stateError();
         case 1:
            return 1.0;
      }
   }

   public Transform createConcatenation(Transform var1) {
      Affine var2 = this.clone();
      var2.append(var1);
      return var2;
   }

   public Affine createInverse() throws NonInvertibleTransformException {
      Affine var1 = this.clone();
      var1.invert();
      return var1;
   }

   public Affine clone() {
      return new Affine(this);
   }

   public void setToTransform(Transform var1) {
      this.setToTransform(var1.getMxx(), var1.getMxy(), var1.getMxz(), var1.getTx(), var1.getMyx(), var1.getMyy(), var1.getMyz(), var1.getTy(), var1.getMzx(), var1.getMzy(), var1.getMzz(), var1.getTz());
   }

   public void setToTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.setToTransform(var1, var3, 0.0, var5, var7, var9, 0.0, var11, 0.0, 0.0, 1.0, 0.0);
   }

   public void setToTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.atomicChange.start();
      this.setMxx(var1);
      this.setMxy(var3);
      this.setMxz(var5);
      this.setTx(var7);
      this.setMyx(var9);
      this.setMyy(var11);
      this.setMyz(var13);
      this.setTy(var15);
      this.setMzx(var17);
      this.setMzy(var19);
      this.setMzz(var21);
      this.setTz(var23);
      this.updateState();
      this.atomicChange.end();
   }

   public void setToTransform(double[] var1, MatrixType var2, int var3) {
      if (var1.length < var3 + var2.elements()) {
         throw new IndexOutOfBoundsException("The array is too short.");
      } else {
         switch (var2) {
            case MT_2D_2x3:
               break;
            case MT_3D_4x4:
               if (var1[var3 + 12] != 0.0 || var1[var3 + 13] != 0.0 || var1[var3 + 14] != 0.0 || var1[var3 + 15] != 1.0) {
                  throw new IllegalArgumentException("The matrix is not affine");
               }
            case MT_3D_3x4:
               this.setToTransform(var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++]);
               return;
            default:
               stateError();
            case MT_2D_3x3:
               if (var1[var3 + 6] != 0.0 || var1[var3 + 7] != 0.0 || var1[var3 + 8] != 1.0) {
                  throw new IllegalArgumentException("The matrix is not affine");
               }
         }

         this.setToTransform(var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++]);
      }
   }

   public void setToIdentity() {
      this.atomicChange.start();
      if (this.state3d != 0) {
         this.setMxx(1.0);
         this.setMxy(0.0);
         this.setMxz(0.0);
         this.setTx(0.0);
         this.setMyx(0.0);
         this.setMyy(1.0);
         this.setMyz(0.0);
         this.setTy(0.0);
         this.setMzx(0.0);
         this.setMzy(0.0);
         this.setMzz(1.0);
         this.setTz(0.0);
         this.state3d = 0;
         this.state2d = 0;
      } else if (this.state2d != 0) {
         this.setMxx(1.0);
         this.setMxy(0.0);
         this.setTx(0.0);
         this.setMyx(0.0);
         this.setMyy(1.0);
         this.setTy(0.0);
         this.state2d = 0;
      }

      this.atomicChange.end();
   }

   public void invert() throws NonInvertibleTransformException {
      this.atomicChange.start();
      if (this.state3d == 0) {
         this.invert2D();
         this.updateState2D();
      } else {
         this.invert3D();
         this.updateState();
      }

      this.atomicChange.end();
   }

   private void invert2D() throws NonInvertibleTransformException {
      double var1;
      double var3;
      double var5;
      double var7;
      double var9;
      double var11;
      double var13;
      switch (this.state2d) {
         case 0:
            return;
         case 1:
            this.setTx(-this.getTx());
            this.setTy(-this.getTy());
            return;
         case 2:
            var1 = this.getMxx();
            var9 = this.getMyy();
            if (var1 != 0.0 && var9 != 0.0) {
               this.setMxx(1.0 / var1);
               this.setMyy(1.0 / var9);
               return;
            }

            this.atomicChange.cancel();
            throw new NonInvertibleTransformException("Determinant is 0");
         case 3:
            var1 = this.getMxx();
            var5 = this.getTx();
            var9 = this.getMyy();
            var11 = this.getTy();
            if (var1 != 0.0 && var9 != 0.0) {
               this.setMxx(1.0 / var1);
               this.setMyy(1.0 / var9);
               this.setTx(-var5 / var1);
               this.setTy(-var11 / var9);
               return;
            }

            this.atomicChange.cancel();
            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
            var3 = this.getMxy();
            var7 = this.getMyx();
            if (var3 != 0.0 && var7 != 0.0) {
               this.setMyx(1.0 / var3);
               this.setMxy(1.0 / var7);
               return;
            }

            this.atomicChange.cancel();
            throw new NonInvertibleTransformException("Determinant is 0");
         case 5:
            var3 = this.getMxy();
            var5 = this.getTx();
            var7 = this.getMyx();
            var11 = this.getTy();
            if (var3 != 0.0 && var7 != 0.0) {
               this.setMyx(1.0 / var3);
               this.setMxy(1.0 / var7);
               this.setTx(-var11 / var7);
               this.setTy(-var5 / var3);
               return;
            }

            this.atomicChange.cancel();
            throw new NonInvertibleTransformException("Determinant is 0");
         case 6:
            var1 = this.getMxx();
            var3 = this.getMxy();
            var7 = this.getMyx();
            var9 = this.getMyy();
            var13 = this.getDeterminant2D();
            if (var13 == 0.0) {
               this.atomicChange.cancel();
               throw new NonInvertibleTransformException("Determinant is 0");
            }

            this.setMxx(var9 / var13);
            this.setMyx(-var7 / var13);
            this.setMxy(-var3 / var13);
            this.setMyy(var1 / var13);
            return;
         default:
            stateError();
         case 7:
            var1 = this.getMxx();
            var3 = this.getMxy();
            var5 = this.getTx();
            var7 = this.getMyx();
            var9 = this.getMyy();
            var11 = this.getTy();
            var13 = this.getDeterminant2D();
            if (var13 == 0.0) {
               this.atomicChange.cancel();
               throw new NonInvertibleTransformException("Determinant is 0");
            } else {
               this.setMxx(var9 / var13);
               this.setMyx(-var7 / var13);
               this.setMxy(-var3 / var13);
               this.setMyy(var1 / var13);
               this.setTx((var3 * var11 - var9 * var5) / var13);
               this.setTy((var7 * var5 - var1 * var11) / var13);
            }
      }
   }

   private void invert3D() throws NonInvertibleTransformException {
      switch (this.state3d) {
         case 2:
            double var1 = this.getMxx();
            double var3 = this.getMyy();
            double var5 = this.getMzz();
            if (var1 != 0.0 && var3 != 0.0 && var5 != 0.0) {
               this.setMxx(1.0 / var1);
               this.setMyy(1.0 / var3);
               this.setMzz(1.0 / var5);
               return;
            }

            this.atomicChange.cancel();
            throw new NonInvertibleTransformException("Determinant is 0");
         case 3:
            double var7 = this.getMxx();
            double var9 = this.getTx();
            double var11 = this.getMyy();
            double var13 = this.getTy();
            double var15 = this.getMzz();
            double var17 = this.getTz();
            if (var7 != 0.0 && var11 != 0.0 && var15 != 0.0) {
               this.setMxx(1.0 / var7);
               this.setMyy(1.0 / var11);
               this.setMzz(1.0 / var15);
               this.setTx(-var9 / var7);
               this.setTy(-var13 / var11);
               this.setTz(-var17 / var15);
               return;
            }

            this.atomicChange.cancel();
            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
            double var19 = this.getMxx();
            double var21 = this.getMxy();
            double var23 = this.getMxz();
            double var25 = this.getTx();
            double var27 = this.getMyx();
            double var29 = this.getMyy();
            double var31 = this.getMyz();
            double var33 = this.getTy();
            double var35 = this.getMzy();
            double var37 = this.getMzx();
            double var39 = this.getMzz();
            double var41 = this.getTz();
            double var43 = var19 * (var29 * var39 - var35 * var31) + var21 * (var31 * var37 - var39 * var27) + var23 * (var27 * var35 - var37 * var29);
            if (var43 == 0.0) {
               this.atomicChange.cancel();
               throw new NonInvertibleTransformException("Determinant is 0");
            }

            double var45 = var29 * var39 - var31 * var35;
            double var47 = -var27 * var39 + var31 * var37;
            double var49 = var27 * var35 - var29 * var37;
            double var51 = -var21 * (var31 * var41 - var39 * var33) - var23 * (var33 * var35 - var41 * var29) - var25 * (var29 * var39 - var35 * var31);
            double var53 = -var21 * var39 + var23 * var35;
            double var55 = var19 * var39 - var23 * var37;
            double var57 = -var19 * var35 + var21 * var37;
            double var59 = var19 * (var31 * var41 - var39 * var33) + var23 * (var33 * var37 - var41 * var27) + var25 * (var27 * var39 - var37 * var31);
            double var61 = var21 * var31 - var23 * var29;
            double var63 = -var19 * var31 + var23 * var27;
            double var65 = var19 * var29 - var21 * var27;
            double var67 = -var19 * (var29 * var41 - var35 * var33) - var21 * (var33 * var37 - var41 * var27) - var25 * (var27 * var35 - var37 * var29);
            this.setMxx(var45 / var43);
            this.setMxy(var53 / var43);
            this.setMxz(var61 / var43);
            this.setTx(var51 / var43);
            this.setMyx(var47 / var43);
            this.setMyy(var55 / var43);
            this.setMyz(var63 / var43);
            this.setTy(var59 / var43);
            this.setMzx(var49 / var43);
            this.setMzy(var57 / var43);
            this.setMzz(var65 / var43);
            this.setTz(var67 / var43);
            return;
         default:
            stateError();
         case 1:
            this.setTx(-this.getTx());
            this.setTy(-this.getTy());
            this.setTz(-this.getTz());
      }
   }

   public void append(Transform var1) {
      var1.appendTo(this);
   }

   public void append(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (this.state3d == 0) {
         this.atomicChange.start();
         double var13 = this.getMxx();
         double var15 = this.getMxy();
         double var17 = this.getMyx();
         double var19 = this.getMyy();
         this.setMxx(var13 * var1 + var15 * var7);
         this.setMxy(var13 * var3 + var15 * var9);
         this.setTx(var13 * var5 + var15 * var11 + this.getTx());
         this.setMyx(var17 * var1 + var19 * var7);
         this.setMyy(var17 * var3 + var19 * var9);
         this.setTy(var17 * var5 + var19 * var11 + this.getTy());
         this.updateState();
         this.atomicChange.end();
      } else {
         this.append(var1, var3, 0.0, var5, var7, var9, 0.0, var11, 0.0, 0.0, 1.0, 0.0);
      }

   }

   public void append(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.atomicChange.start();
      double var25 = this.getMxx();
      double var27 = this.getMxy();
      double var29 = this.getMxz();
      double var31 = this.getTx();
      double var33 = this.getMyx();
      double var35 = this.getMyy();
      double var37 = this.getMyz();
      double var39 = this.getTy();
      double var41 = this.getMzx();
      double var43 = this.getMzy();
      double var45 = this.getMzz();
      double var47 = this.getTz();
      this.setMxx(var25 * var1 + var27 * var9 + var29 * var17);
      this.setMxy(var25 * var3 + var27 * var11 + var29 * var19);
      this.setMxz(var25 * var5 + var27 * var13 + var29 * var21);
      this.setTx(var25 * var7 + var27 * var15 + var29 * var23 + var31);
      this.setMyx(var33 * var1 + var35 * var9 + var37 * var17);
      this.setMyy(var33 * var3 + var35 * var11 + var37 * var19);
      this.setMyz(var33 * var5 + var35 * var13 + var37 * var21);
      this.setTy(var33 * var7 + var35 * var15 + var37 * var23 + var39);
      this.setMzx(var41 * var1 + var43 * var9 + var45 * var17);
      this.setMzy(var41 * var3 + var43 * var11 + var45 * var19);
      this.setMzz(var41 * var5 + var43 * var13 + var45 * var21);
      this.setTz(var41 * var7 + var43 * var15 + var45 * var23 + var47);
      this.updateState();
      this.atomicChange.end();
   }

   public void append(double[] var1, MatrixType var2, int var3) {
      if (var1.length < var3 + var2.elements()) {
         throw new IndexOutOfBoundsException("The array is too short.");
      } else {
         switch (var2) {
            case MT_2D_2x3:
               break;
            case MT_3D_4x4:
               if (var1[var3 + 12] != 0.0 || var1[var3 + 13] != 0.0 || var1[var3 + 14] != 0.0 || var1[var3 + 15] != 1.0) {
                  throw new IllegalArgumentException("The matrix is not affine");
               }
            case MT_3D_3x4:
               this.append(var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++]);
               return;
            default:
               stateError();
            case MT_2D_3x3:
               if (var1[var3 + 6] != 0.0 || var1[var3 + 7] != 0.0 || var1[var3 + 8] != 1.0) {
                  throw new IllegalArgumentException("The matrix is not affine");
               }
         }

         this.append(var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++]);
      }
   }

   void appendTo(Affine var1) {
      switch (this.state3d) {
         case 1:
            var1.appendTranslation(this.getTx(), this.getTy(), this.getTz());
            return;
         case 2:
            var1.appendScale(this.getMxx(), this.getMyy(), this.getMzz());
            return;
         case 3:
            var1.appendTranslation(this.getTx(), this.getTy(), this.getTz());
            var1.appendScale(this.getMxx(), this.getMyy(), this.getMzz());
            return;
         case 4:
            var1.append(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
            return;
         default:
            stateError();
         case 0:
            switch (this.state2d) {
               case 0:
                  return;
               case 1:
                  var1.appendTranslation(this.getTx(), this.getTy());
                  return;
               case 2:
                  var1.appendScale(this.getMxx(), this.getMyy());
                  return;
               case 3:
                  var1.appendTranslation(this.getTx(), this.getTy());
                  var1.appendScale(this.getMxx(), this.getMyy());
                  return;
               default:
                  var1.append(this.getMxx(), this.getMxy(), this.getTx(), this.getMyx(), this.getMyy(), this.getTy());
            }
      }
   }

   public void prepend(Transform var1) {
      var1.prependTo(this);
   }

   public void prepend(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (this.state3d == 0) {
         this.atomicChange.start();
         double var13 = this.getMxx();
         double var15 = this.getMxy();
         double var17 = this.getTx();
         double var19 = this.getMyx();
         double var21 = this.getMyy();
         double var23 = this.getTy();
         this.setMxx(var1 * var13 + var3 * var19);
         this.setMxy(var1 * var15 + var3 * var21);
         this.setTx(var1 * var17 + var3 * var23 + var5);
         this.setMyx(var7 * var13 + var9 * var19);
         this.setMyy(var7 * var15 + var9 * var21);
         this.setTy(var7 * var17 + var9 * var23 + var11);
         this.updateState2D();
         this.atomicChange.end();
      } else {
         this.prepend(var1, var3, 0.0, var5, var7, var9, 0.0, var11, 0.0, 0.0, 1.0, 0.0);
      }

   }

   public void prepend(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.atomicChange.start();
      double var25 = this.getMxx();
      double var27 = this.getMxy();
      double var29 = this.getMxz();
      double var31 = this.getTx();
      double var33 = this.getMyx();
      double var35 = this.getMyy();
      double var37 = this.getMyz();
      double var39 = this.getTy();
      double var41 = this.getMzx();
      double var43 = this.getMzy();
      double var45 = this.getMzz();
      double var47 = this.getTz();
      this.setMxx(var1 * var25 + var3 * var33 + var5 * var41);
      this.setMxy(var1 * var27 + var3 * var35 + var5 * var43);
      this.setMxz(var1 * var29 + var3 * var37 + var5 * var45);
      this.setTx(var1 * var31 + var3 * var39 + var5 * var47 + var7);
      this.setMyx(var9 * var25 + var11 * var33 + var13 * var41);
      this.setMyy(var9 * var27 + var11 * var35 + var13 * var43);
      this.setMyz(var9 * var29 + var11 * var37 + var13 * var45);
      this.setTy(var9 * var31 + var11 * var39 + var13 * var47 + var15);
      this.setMzx(var17 * var25 + var19 * var33 + var21 * var41);
      this.setMzy(var17 * var27 + var19 * var35 + var21 * var43);
      this.setMzz(var17 * var29 + var19 * var37 + var21 * var45);
      this.setTz(var17 * var31 + var19 * var39 + var21 * var47 + var23);
      this.updateState();
      this.atomicChange.end();
   }

   public void prepend(double[] var1, MatrixType var2, int var3) {
      if (var1.length < var3 + var2.elements()) {
         throw new IndexOutOfBoundsException("The array is too short.");
      } else {
         switch (var2) {
            case MT_2D_2x3:
               break;
            case MT_3D_4x4:
               if (var1[var3 + 12] != 0.0 || var1[var3 + 13] != 0.0 || var1[var3 + 14] != 0.0 || var1[var3 + 15] != 1.0) {
                  throw new IllegalArgumentException("The matrix is not affine");
               }
            case MT_3D_3x4:
               this.prepend(var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++]);
               return;
            default:
               stateError();
            case MT_2D_3x3:
               if (var1[var3 + 6] != 0.0 || var1[var3 + 7] != 0.0 || var1[var3 + 8] != 1.0) {
                  throw new IllegalArgumentException("The matrix is not affine");
               }
         }

         this.prepend(var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++], var1[var3++]);
      }
   }

   void prependTo(Affine var1) {
      switch (this.state3d) {
         case 1:
            var1.prependTranslation(this.getTx(), this.getTy(), this.getTz());
            return;
         case 2:
            var1.prependScale(this.getMxx(), this.getMyy(), this.getMzz());
            return;
         case 3:
            var1.prependScale(this.getMxx(), this.getMyy(), this.getMzz());
            var1.prependTranslation(this.getTx(), this.getTy(), this.getTz());
            return;
         case 4:
            var1.prepend(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
            return;
         default:
            stateError();
         case 0:
            switch (this.state2d) {
               case 0:
                  return;
               case 1:
                  var1.prependTranslation(this.getTx(), this.getTy());
                  return;
               case 2:
                  var1.prependScale(this.getMxx(), this.getMyy());
                  return;
               case 3:
                  var1.prependScale(this.getMxx(), this.getMyy());
                  var1.prependTranslation(this.getTx(), this.getTy());
                  return;
               default:
                  var1.prepend(this.getMxx(), this.getMxy(), this.getTx(), this.getMyx(), this.getMyy(), this.getTy());
            }
      }
   }

   public void appendTranslation(double var1, double var3) {
      this.atomicChange.start();
      this.translate2D(var1, var3);
      this.atomicChange.end();
   }

   public void appendTranslation(double var1, double var3, double var5) {
      this.atomicChange.start();
      this.translate3D(var1, var3, var5);
      this.atomicChange.end();
   }

   private void translate2D(double var1, double var3) {
      if (this.state3d != 0) {
         this.translate3D(var1, var3, 0.0);
      } else {
         switch (this.state2d) {
            case 0:
               this.setTx(var1);
               this.setTy(var3);
               if (var1 != 0.0 || var3 != 0.0) {
                  this.state2d = 1;
               }

               return;
            case 1:
               this.setTx(var1 + this.getTx());
               this.setTy(var3 + this.getTy());
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state2d = 0;
               }

               return;
            case 2:
               this.setTx(var1 * this.getMxx());
               this.setTy(var3 * this.getMyy());
               if (this.getTx() != 0.0 || this.getTy() != 0.0) {
                  this.state2d = 3;
               }

               return;
            case 3:
               this.setTx(var1 * this.getMxx() + this.getTx());
               this.setTy(var3 * this.getMyy() + this.getTy());
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state2d = 2;
               }

               return;
            case 4:
               this.setTx(var3 * this.getMxy());
               this.setTy(var1 * this.getMyx());
               if (this.getTx() != 0.0 || this.getTy() != 0.0) {
                  this.state2d = 5;
               }

               return;
            case 5:
               this.setTx(var3 * this.getMxy() + this.getTx());
               this.setTy(var1 * this.getMyx() + this.getTy());
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state2d = 4;
               }

               return;
            case 6:
               this.setTx(var1 * this.getMxx() + var3 * this.getMxy());
               this.setTy(var1 * this.getMyx() + var3 * this.getMyy());
               if (this.getTx() != 0.0 || this.getTy() != 0.0) {
                  this.state2d = 7;
               }

               return;
            default:
               stateError();
            case 7:
               this.setTx(var1 * this.getMxx() + var3 * this.getMxy() + this.getTx());
               this.setTy(var1 * this.getMyx() + var3 * this.getMyy() + this.getTy());
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state2d = 6;
               }

         }
      }
   }

   private void translate3D(double var1, double var3, double var5) {
      switch (this.state3d) {
         case 1:
            this.setTx(var1 + this.getTx());
            this.setTy(var3 + this.getTy());
            this.setTz(var5 + this.getTz());
            if (this.getTz() == 0.0) {
               this.state3d = 0;
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state2d = 0;
               } else {
                  this.state2d = 1;
               }
            }

            return;
         case 2:
            this.setTx(var1 * this.getMxx());
            this.setTy(var3 * this.getMyy());
            this.setTz(var5 * this.getMzz());
            if (this.getTx() != 0.0 || this.getTy() != 0.0 || this.getTz() != 0.0) {
               this.state3d |= 1;
            }

            return;
         case 3:
            this.setTx(var1 * this.getMxx() + this.getTx());
            this.setTy(var3 * this.getMyy() + this.getTy());
            this.setTz(var5 * this.getMzz() + this.getTz());
            if (this.getTz() == 0.0) {
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state3d = 2;
               }

               if (this.getMzz() == 1.0) {
                  this.state2d = this.state3d;
                  this.state3d = 0;
               }
            }

            return;
         case 4:
            this.setTx(var1 * this.getMxx() + var3 * this.getMxy() + var5 * this.getMxz() + this.getTx());
            this.setTy(var1 * this.getMyx() + var3 * this.getMyy() + var5 * this.getMyz() + this.getTy());
            this.setTz(var1 * this.getMzx() + var3 * this.getMzy() + var5 * this.getMzz() + this.getTz());
            this.updateState();
            return;
         default:
            stateError();
         case 0:
            this.translate2D(var1, var3);
            if (var5 != 0.0) {
               this.setTz(var5);
               if ((this.state2d & 4) == 0) {
                  this.state3d = this.state2d & 2 | 1;
               } else {
                  this.state3d = 4;
               }
            }

      }
   }

   public void prependTranslation(double var1, double var3, double var5) {
      this.atomicChange.start();
      this.preTranslate3D(var1, var3, var5);
      this.atomicChange.end();
   }

   public void prependTranslation(double var1, double var3) {
      this.atomicChange.start();
      this.preTranslate2D(var1, var3);
      this.atomicChange.end();
   }

   private void preTranslate2D(double var1, double var3) {
      if (this.state3d != 0) {
         this.preTranslate3D(var1, var3, 0.0);
      } else {
         this.setTx(this.getTx() + var1);
         this.setTy(this.getTy() + var3);
         if (this.getTx() == 0.0 && this.getTy() == 0.0) {
            this.state2d &= -2;
         } else {
            this.state2d |= 1;
         }

      }
   }

   private void preTranslate3D(double var1, double var3, double var5) {
      switch (this.state3d) {
         case 1:
            this.setTx(this.getTx() + var1);
            this.setTy(this.getTy() + var3);
            this.setTz(this.getTz() + var5);
            if (this.getTz() == 0.0) {
               this.state3d = 0;
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state2d = 0;
               } else {
                  this.state2d = 1;
               }
            }

            return;
         case 2:
            this.setTx(var1);
            this.setTy(var3);
            this.setTz(var5);
            if (var1 != 0.0 || var3 != 0.0 || var5 != 0.0) {
               this.state3d |= 1;
            }

            return;
         case 3:
            this.setTx(this.getTx() + var1);
            this.setTy(this.getTy() + var3);
            this.setTz(this.getTz() + var5);
            if (this.getTz() == 0.0) {
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state3d = 2;
               }

               if (this.getMzz() == 1.0) {
                  this.state2d = this.state3d;
                  this.state3d = 0;
               }
            }

            return;
         case 4:
            this.setTx(this.getTx() + var1);
            this.setTy(this.getTy() + var3);
            this.setTz(this.getTz() + var5);
            if (this.getTz() == 0.0 && this.getMxz() == 0.0 && this.getMyz() == 0.0 && this.getMzx() == 0.0 && this.getMzy() == 0.0 && this.getMzz() == 1.0) {
               this.state3d = 0;
               this.updateState2D();
            }

            return;
         default:
            stateError();
         case 0:
            this.preTranslate2D(var1, var3);
            if (var5 != 0.0) {
               this.setTz(var5);
               if ((this.state2d & 4) == 0) {
                  this.state3d = this.state2d & 2 | 1;
               } else {
                  this.state3d = 4;
               }
            }

      }
   }

   public void appendScale(double var1, double var3) {
      this.atomicChange.start();
      this.scale2D(var1, var3);
      this.atomicChange.end();
   }

   public void appendScale(double var1, double var3, double var5, double var7) {
      this.atomicChange.start();
      if (var5 == 0.0 && var7 == 0.0) {
         this.scale2D(var1, var3);
      } else {
         this.translate2D(var5, var7);
         this.scale2D(var1, var3);
         this.translate2D(-var5, -var7);
      }

      this.atomicChange.end();
   }

   public void appendScale(double var1, double var3, Point2D var5) {
      this.appendScale(var1, var3, var5.getX(), var5.getY());
   }

   public void appendScale(double var1, double var3, double var5) {
      this.atomicChange.start();
      this.scale3D(var1, var3, var5);
      this.atomicChange.end();
   }

   public void appendScale(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.atomicChange.start();
      if (var7 == 0.0 && var9 == 0.0 && var11 == 0.0) {
         this.scale3D(var1, var3, var5);
      } else {
         this.translate3D(var7, var9, var11);
         this.scale3D(var1, var3, var5);
         this.translate3D(-var7, -var9, -var11);
      }

      this.atomicChange.end();
   }

   public void appendScale(double var1, double var3, double var5, Point3D var7) {
      this.appendScale(var1, var3, var5, var7.getX(), var7.getY(), var7.getZ());
   }

   private void scale2D(double var1, double var3) {
      if (this.state3d != 0) {
         this.scale3D(var1, var3, 1.0);
      } else {
         int var5 = this.state2d;
         switch (var5) {
            case 0:
            case 1:
               this.setMxx(var1);
               this.setMyy(var3);
               if (var1 != 1.0 || var3 != 1.0) {
                  this.state2d = var5 | 2;
               }

               return;
            case 2:
            case 3:
               this.setMxx(this.getMxx() * var1);
               this.setMyy(this.getMyy() * var3);
               if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
                  this.state2d = var5 &= 1;
               }

               return;
            case 4:
            case 5:
               break;
            default:
               stateError();
            case 6:
            case 7:
               this.setMxx(this.getMxx() * var1);
               this.setMyy(this.getMyy() * var3);
         }

         this.setMxy(this.getMxy() * var3);
         this.setMyx(this.getMyx() * var1);
         if (this.getMxy() == 0.0 && this.getMyx() == 0.0) {
            var5 &= 1;
            if (this.getMxx() != 1.0 || this.getMyy() != 1.0) {
               var5 |= 2;
            }

            this.state2d = var5;
         } else if (this.getMxx() == 0.0 && this.getMyy() == 0.0) {
            this.state2d &= -3;
         }

      }
   }

   private void scale3D(double var1, double var3, double var5) {
      switch (this.state3d) {
         case 1:
            this.setMxx(var1);
            this.setMyy(var3);
            this.setMzz(var5);
            if (var1 != 1.0 || var3 != 1.0 || var5 != 1.0) {
               this.state3d |= 2;
            }

            return;
         case 2:
            this.setMxx(this.getMxx() * var1);
            this.setMyy(this.getMyy() * var3);
            this.setMzz(this.getMzz() * var5);
            if (this.getMzz() == 1.0) {
               this.state3d = 0;
               if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
                  this.state2d = 0;
               } else {
                  this.state2d = 2;
               }
            }

            return;
         case 3:
            this.setMxx(this.getMxx() * var1);
            this.setMyy(this.getMyy() * var3);
            this.setMzz(this.getMzz() * var5);
            if (this.getMxx() == 1.0 && this.getMyy() == 1.0 && this.getMzz() == 1.0) {
               this.state3d &= -3;
            }

            if (this.getTz() == 0.0 && this.getMzz() == 1.0) {
               this.state2d = this.state3d;
               this.state3d = 0;
            }

            return;
         case 4:
            this.setMxx(this.getMxx() * var1);
            this.setMxy(this.getMxy() * var3);
            this.setMxz(this.getMxz() * var5);
            this.setMyx(this.getMyx() * var1);
            this.setMyy(this.getMyy() * var3);
            this.setMyz(this.getMyz() * var5);
            this.setMzx(this.getMzx() * var1);
            this.setMzy(this.getMzy() * var3);
            this.setMzz(this.getMzz() * var5);
            if (var1 == 0.0 || var3 == 0.0 || var5 == 0.0) {
               this.updateState();
            }

            return;
         default:
            stateError();
         case 0:
            this.scale2D(var1, var3);
            if (var5 != 1.0) {
               this.setMzz(var5);
               if ((this.state2d & 4) == 0) {
                  this.state3d = this.state2d & 1 | 2;
               } else {
                  this.state3d = 4;
               }
            }

      }
   }

   public void prependScale(double var1, double var3) {
      this.atomicChange.start();
      this.preScale2D(var1, var3);
      this.atomicChange.end();
   }

   public void prependScale(double var1, double var3, double var5, double var7) {
      this.atomicChange.start();
      if (var5 == 0.0 && var7 == 0.0) {
         this.preScale2D(var1, var3);
      } else {
         this.preTranslate2D(-var5, -var7);
         this.preScale2D(var1, var3);
         this.preTranslate2D(var5, var7);
      }

      this.atomicChange.end();
   }

   public void prependScale(double var1, double var3, Point2D var5) {
      this.prependScale(var1, var3, var5.getX(), var5.getY());
   }

   public void prependScale(double var1, double var3, double var5) {
      this.atomicChange.start();
      this.preScale3D(var1, var3, var5);
      this.atomicChange.end();
   }

   public void prependScale(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.atomicChange.start();
      if (var7 == 0.0 && var9 == 0.0 && var11 == 0.0) {
         this.preScale3D(var1, var3, var5);
      } else {
         this.preTranslate3D(-var7, -var9, -var11);
         this.preScale3D(var1, var3, var5);
         this.preTranslate3D(var7, var9, var11);
      }

      this.atomicChange.end();
   }

   public void prependScale(double var1, double var3, double var5, Point3D var7) {
      this.prependScale(var1, var3, var5, var7.getX(), var7.getY(), var7.getZ());
   }

   private void preScale2D(double var1, double var3) {
      if (this.state3d != 0) {
         this.preScale3D(var1, var3, 1.0);
      } else {
         int var5 = this.state2d;
         switch (var5) {
            case 1:
               this.setTx(this.getTx() * var1);
               this.setTy(this.getTy() * var3);
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  var5 &= -2;
                  this.state2d = var5;
               }
            case 0:
               this.setMxx(var1);
               this.setMyy(var3);
               if (var1 != 1.0 || var3 != 1.0) {
                  this.state2d = var5 | 2;
               }

               return;
            case 3:
               this.setTx(this.getTx() * var1);
               this.setTy(this.getTy() * var3);
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  var5 &= -2;
                  this.state2d = var5;
               }
            case 2:
               this.setMxx(this.getMxx() * var1);
               this.setMyy(this.getMyy() * var3);
               if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
                  this.state2d = var5 &= 1;
               }

               return;
            case 4:
               break;
            case 5:
               this.setTx(this.getTx() * var1);
               this.setTy(this.getTy() * var3);
               this.setMxy(this.getMxy() * var1);
               this.setMyx(this.getMyx() * var3);
               if (this.getMxy() == 0.0 && this.getMyx() == 0.0) {
                  if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                     this.state2d = 2;
                  } else {
                     this.state2d = 3;
                  }
               } else if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  this.state2d = 4;
               }

               return;
            default:
               stateError();
            case 7:
               this.setTx(this.getTx() * var1);
               this.setTy(this.getTy() * var3);
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  var5 &= -2;
                  this.state2d = var5;
               }
            case 6:
               this.setMxx(this.getMxx() * var1);
               this.setMyy(this.getMyy() * var3);
         }

         this.setMxy(this.getMxy() * var1);
         this.setMyx(this.getMyx() * var3);
         if (this.getMxy() == 0.0 && this.getMyx() == 0.0) {
            var5 &= 1;
            if (this.getMxx() != 1.0 || this.getMyy() != 1.0) {
               var5 |= 2;
            }

            this.state2d = var5;
         }

      }
   }

   private void preScale3D(double var1, double var3, double var5) {
      switch (this.state3d) {
         case 1:
            this.setTx(this.getTx() * var1);
            this.setTy(this.getTy() * var3);
            this.setTz(this.getTz() * var5);
            this.setMxx(var1);
            this.setMyy(var3);
            this.setMzz(var5);
            if (this.getTx() == 0.0 && this.getTy() == 0.0 && this.getTz() == 0.0) {
               this.state3d &= -2;
            }

            if (var1 != 1.0 || var3 != 1.0 || var5 != 1.0) {
               this.state3d |= 2;
            }

            return;
         case 2:
            this.setMxx(this.getMxx() * var1);
            this.setMyy(this.getMyy() * var3);
            this.setMzz(this.getMzz() * var5);
            if (this.getMzz() == 1.0) {
               this.state3d = 0;
               if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
                  this.state2d = 0;
               } else {
                  this.state2d = 2;
               }
            }

            return;
         case 3:
            this.setTx(this.getTx() * var1);
            this.setTy(this.getTy() * var3);
            this.setTz(this.getTz() * var5);
            this.setMxx(this.getMxx() * var1);
            this.setMyy(this.getMyy() * var3);
            this.setMzz(this.getMzz() * var5);
            if (this.getTx() == 0.0 && this.getTy() == 0.0 && this.getTz() == 0.0) {
               this.state3d &= -2;
            }

            if (this.getMxx() == 1.0 && this.getMyy() == 1.0 && this.getMzz() == 1.0) {
               this.state3d &= -3;
            }

            if (this.getTz() == 0.0 && this.getMzz() == 1.0) {
               this.state2d = this.state3d;
               this.state3d = 0;
            }

            return;
         case 4:
            this.setMxx(this.getMxx() * var1);
            this.setMxy(this.getMxy() * var1);
            this.setMxz(this.getMxz() * var1);
            this.setTx(this.getTx() * var1);
            this.setMyx(this.getMyx() * var3);
            this.setMyy(this.getMyy() * var3);
            this.setMyz(this.getMyz() * var3);
            this.setTy(this.getTy() * var3);
            this.setMzx(this.getMzx() * var5);
            this.setMzy(this.getMzy() * var5);
            this.setMzz(this.getMzz() * var5);
            this.setTz(this.getTz() * var5);
            if (var1 == 0.0 || var3 == 0.0 || var5 == 0.0) {
               this.updateState();
            }

            return;
         default:
            stateError();
         case 0:
            this.preScale2D(var1, var3);
            if (var5 != 1.0) {
               this.setMzz(var5);
               if ((this.state2d & 4) == 0) {
                  this.state3d = this.state2d & 1 | 2;
               } else {
                  this.state3d = 4;
               }
            }

      }
   }

   public void appendShear(double var1, double var3) {
      this.atomicChange.start();
      this.shear2D(var1, var3);
      this.atomicChange.end();
   }

   public void appendShear(double var1, double var3, double var5, double var7) {
      this.atomicChange.start();
      if (var5 == 0.0 && var7 == 0.0) {
         this.shear2D(var1, var3);
      } else {
         this.translate2D(var5, var7);
         this.shear2D(var1, var3);
         this.translate2D(-var5, -var7);
      }

      this.atomicChange.end();
   }

   public void appendShear(double var1, double var3, Point2D var5) {
      this.appendShear(var1, var3, var5.getX(), var5.getY());
   }

   private void shear2D(double var1, double var3) {
      if (this.state3d != 0) {
         this.shear3D(var1, var3);
      } else {
         int var5 = this.state2d;
         switch (var5) {
            case 0:
            case 1:
               this.setMxy(var1);
               this.setMyx(var3);
               if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                  this.state2d = var5 | 2 | 4;
               }

               return;
            case 2:
            case 3:
               this.setMxy(this.getMxx() * var1);
               this.setMyx(this.getMyy() * var3);
               if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                  this.state2d = var5 | 4;
               }

               return;
            case 4:
            case 5:
               this.setMxx(this.getMxy() * var3);
               this.setMyy(this.getMyx() * var1);
               if (this.getMxx() != 0.0 || this.getMyy() != 0.0) {
                  this.state2d = var5 | 2;
               }

               return;
            default:
               stateError();
            case 6:
            case 7:
               double var6 = this.getMxx();
               double var8 = this.getMxy();
               this.setMxx(var6 + var8 * var3);
               this.setMxy(var6 * var1 + var8);
               var6 = this.getMyx();
               var8 = this.getMyy();
               this.setMyx(var6 + var8 * var3);
               this.setMyy(var6 * var1 + var8);
               this.updateState2D();
         }
      }
   }

   private void shear3D(double var1, double var3) {
      switch (this.state3d) {
         case 1:
            this.setMxy(var1);
            this.setMyx(var3);
            if (var1 != 0.0 || var3 != 0.0) {
               this.state3d = 4;
            }

            return;
         case 2:
         case 3:
            this.setMxy(this.getMxx() * var1);
            this.setMyx(this.getMyy() * var3);
            if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
               this.state3d = 4;
            }

            return;
         case 4:
            double var5 = this.getMxx();
            double var7 = this.getMxy();
            double var9 = this.getMyx();
            double var11 = this.getMyy();
            double var13 = this.getMzx();
            double var15 = this.getMzy();
            this.setMxx(var5 + var7 * var3);
            this.setMxy(var7 + var5 * var1);
            this.setMyx(var9 + var11 * var3);
            this.setMyy(var11 + var9 * var1);
            this.setMzx(var13 + var15 * var3);
            this.setMzy(var15 + var13 * var1);
            this.updateState();
            return;
         default:
            stateError();
         case 0:
            this.shear2D(var1, var3);
      }
   }

   public void prependShear(double var1, double var3) {
      this.atomicChange.start();
      this.preShear2D(var1, var3);
      this.atomicChange.end();
   }

   public void prependShear(double var1, double var3, double var5, double var7) {
      this.atomicChange.start();
      if (var5 == 0.0 && var7 == 0.0) {
         this.preShear2D(var1, var3);
      } else {
         this.preTranslate2D(-var5, -var7);
         this.preShear2D(var1, var3);
         this.preTranslate2D(var5, var7);
      }

      this.atomicChange.end();
   }

   public void prependShear(double var1, double var3, Point2D var5) {
      this.prependShear(var1, var3, var5.getX(), var5.getY());
   }

   private void preShear2D(double var1, double var3) {
      if (this.state3d != 0) {
         this.preShear3D(var1, var3);
      } else {
         int var5 = this.state2d;
         switch (var5) {
            case 0:
               break;
            case 1:
               double var22 = this.getTx();
               double var24 = this.getTy();
               this.setTx(var22 + var1 * var24);
               this.setTy(var24 + var3 * var22);
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  var5 &= -2;
                  this.state2d = var5;
               }
               break;
            case 3:
               double var18 = this.getTx();
               double var20 = this.getTy();
               this.setTx(var18 + var1 * var20);
               this.setTy(var20 + var3 * var18);
               if (this.getTx() == 0.0 && this.getTy() == 0.0) {
                  var5 &= -2;
                  this.state2d = var5;
               }
            case 2:
               this.setMxy(var1 * this.getMyy());
               this.setMyx(var3 * this.getMxx());
               if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
                  this.state2d = var5 | 4;
               }

               return;
            default:
               stateError();
            case 5:
            case 7:
               double var6 = this.getTx();
               double var8 = this.getTy();
               this.setTx(var6 + var1 * var8);
               this.setTy(var8 + var3 * var6);
            case 4:
            case 6:
               double var10 = this.getMxx();
               double var12 = this.getMxy();
               double var14 = this.getMyx();
               double var16 = this.getMyy();
               this.setMxx(var10 + var1 * var14);
               this.setMxy(var12 + var1 * var16);
               this.setMyx(var3 * var10 + var14);
               this.setMyy(var3 * var12 + var16);
               this.updateState2D();
               return;
         }

         this.setMxy(var1);
         this.setMyx(var3);
         if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
            this.state2d = var5 | 2 | 4;
         }

      }
   }

   private void preShear3D(double var1, double var3) {
      switch (this.state3d) {
         case 1:
            double var5 = this.getTx();
            this.setMxy(var1);
            this.setTx(var5 + this.getTy() * var1);
            this.setMyx(var3);
            this.setTy(var5 * var3 + this.getTy());
            if (var1 != 0.0 || var3 != 0.0) {
               this.state3d = 4;
            }

            return;
         case 2:
            this.setMxy(this.getMyy() * var1);
            this.setMyx(this.getMxx() * var3);
            if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
               this.state3d = 4;
            }

            return;
         case 3:
            double var7 = this.getTx();
            this.setMxy(this.getMyy() * var1);
            this.setTx(var7 + this.getTy() * var1);
            this.setMyx(this.getMxx() * var3);
            this.setTy(var7 * var3 + this.getTy());
            if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
               this.state3d = 4;
            }

            return;
         case 4:
            double var9 = this.getMxx();
            double var11 = this.getMxy();
            double var13 = this.getMyx();
            double var15 = this.getTx();
            double var17 = this.getMyy();
            double var19 = this.getMxz();
            double var21 = this.getMyz();
            double var23 = this.getTy();
            this.setMxx(var9 + var13 * var1);
            this.setMxy(var11 + var17 * var1);
            this.setMxz(var19 + var21 * var1);
            this.setTx(var15 + var23 * var1);
            this.setMyx(var9 * var3 + var13);
            this.setMyy(var11 * var3 + var17);
            this.setMyz(var19 * var3 + var21);
            this.setTy(var15 * var3 + var23);
            this.updateState();
            return;
         default:
            stateError();
         case 0:
            this.preShear2D(var1, var3);
      }
   }

   public void appendRotation(double var1) {
      this.atomicChange.start();
      this.rotate2D(var1);
      this.atomicChange.end();
   }

   public void appendRotation(double var1, double var3, double var5) {
      this.atomicChange.start();
      if (var3 == 0.0 && var5 == 0.0) {
         this.rotate2D(var1);
      } else {
         this.translate2D(var3, var5);
         this.rotate2D(var1);
         this.translate2D(-var3, -var5);
      }

      this.atomicChange.end();
   }

   public void appendRotation(double var1, Point2D var3) {
      this.appendRotation(var1, var3.getX(), var3.getY());
   }

   public void appendRotation(double var1, double var3, double var5, double var7, double var9, double var11, double var13) {
      this.atomicChange.start();
      if (var3 == 0.0 && var5 == 0.0 && var7 == 0.0) {
         this.rotate3D(var1, var9, var11, var13);
      } else {
         this.translate3D(var3, var5, var7);
         this.rotate3D(var1, var9, var11, var13);
         this.translate3D(-var3, -var5, -var7);
      }

      this.atomicChange.end();
   }

   public void appendRotation(double var1, double var3, double var5, double var7, Point3D var9) {
      this.appendRotation(var1, var3, var5, var7, var9.getX(), var9.getY(), var9.getZ());
   }

   public void appendRotation(double var1, Point3D var3, Point3D var4) {
      this.appendRotation(var1, var3.getX(), var3.getY(), var3.getZ(), var4.getX(), var4.getY(), var4.getZ());
   }

   private void rotate3D(double var1, double var3, double var5, double var7) {
      if (var3 == 0.0 && var5 == 0.0) {
         if (var7 > 0.0) {
            this.rotate3D(var1);
         } else if (var7 < 0.0) {
            this.rotate3D(-var1);
         }

      } else {
         double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
         if (var9 != 0.0) {
            var9 = 1.0 / var9;
            double var11 = var3 * var9;
            double var13 = var5 * var9;
            double var15 = var7 * var9;
            double var17 = Math.sin(Math.toRadians(var1));
            double var19 = Math.cos(Math.toRadians(var1));
            double var21 = 1.0 - var19;
            double var23 = var11 * var15;
            double var25 = var11 * var13;
            double var27 = var13 * var15;
            double var29 = var21 * var11 * var11 + var19;
            double var31 = var21 * var25 - var17 * var15;
            double var33 = var21 * var23 + var17 * var13;
            double var35 = var21 * var25 + var17 * var15;
            double var37 = var21 * var13 * var13 + var19;
            double var39 = var21 * var27 - var17 * var11;
            double var41 = var21 * var23 - var17 * var13;
            double var43 = var21 * var27 + var17 * var11;
            double var45 = var21 * var15 * var15 + var19;
            double var47;
            double var49;
            double var51;
            double var53;
            double var55;
            double var57;
            double var59;
            double var61;
            switch (this.state3d) {
               case 1:
                  this.setMxx(var29);
                  this.setMxy(var31);
                  this.setMxz(var33);
                  this.setMyx(var35);
                  this.setMyy(var37);
                  this.setMyz(var39);
                  this.setMzx(var41);
                  this.setMzy(var43);
                  this.setMzz(var45);
                  break;
               case 2:
               case 3:
                  var47 = this.getMxx();
                  var49 = this.getMyy();
                  var51 = this.getMzz();
                  this.setMxx(var47 * var29);
                  this.setMxy(var47 * var31);
                  this.setMxz(var47 * var33);
                  this.setMyx(var49 * var35);
                  this.setMyy(var49 * var37);
                  this.setMyz(var49 * var39);
                  this.setMzx(var51 * var41);
                  this.setMzy(var51 * var43);
                  this.setMzz(var51 * var45);
                  break;
               case 4:
                  var53 = this.getMxx();
                  var55 = this.getMxy();
                  var57 = this.getMxz();
                  var59 = this.getMyx();
                  var61 = this.getMyy();
                  double var63 = this.getMyz();
                  double var65 = this.getMzx();
                  double var67 = this.getMzy();
                  double var69 = this.getMzz();
                  this.setMxx(var53 * var29 + var55 * var35 + var57 * var41);
                  this.setMxy(var53 * var31 + var55 * var37 + var57 * var43);
                  this.setMxz(var53 * var33 + var55 * var39 + var57 * var45);
                  this.setMyx(var59 * var29 + var61 * var35 + var63 * var41);
                  this.setMyy(var59 * var31 + var61 * var37 + var63 * var43);
                  this.setMyz(var59 * var33 + var61 * var39 + var63 * var45);
                  this.setMzx(var65 * var29 + var67 * var35 + var69 * var41);
                  this.setMzy(var65 * var31 + var67 * var37 + var69 * var43);
                  this.setMzz(var65 * var33 + var67 * var39 + var69 * var45);
                  break;
               default:
                  stateError();
               case 0:
                  switch (this.state2d) {
                     case 0:
                     case 1:
                        this.setMxx(var29);
                        this.setMxy(var31);
                        this.setMxz(var33);
                        this.setMyx(var35);
                        this.setMyy(var37);
                        this.setMyz(var39);
                        this.setMzx(var41);
                        this.setMzy(var43);
                        this.setMzz(var45);
                        break;
                     case 2:
                     case 3:
                        var59 = this.getMxx();
                        var61 = this.getMyy();
                        this.setMxx(var59 * var29);
                        this.setMxy(var59 * var31);
                        this.setMxz(var59 * var33);
                        this.setMyx(var61 * var35);
                        this.setMyy(var61 * var37);
                        this.setMyz(var61 * var39);
                        this.setMzx(var41);
                        this.setMzy(var43);
                        this.setMzz(var45);
                        break;
                     case 4:
                     case 5:
                        var55 = this.getMxy();
                        var57 = this.getMyx();
                        this.setMxx(var55 * var35);
                        this.setMxy(var55 * var37);
                        this.setMxz(var55 * var39);
                        this.setMyx(var57 * var29);
                        this.setMyy(var57 * var31);
                        this.setMyz(var57 * var33);
                        this.setMzx(var41);
                        this.setMzy(var43);
                        this.setMzz(var45);
                        break;
                     default:
                        stateError();
                     case 6:
                     case 7:
                        var47 = this.getMxx();
                        var49 = this.getMxy();
                        var51 = this.getMyx();
                        var53 = this.getMyy();
                        this.setMxx(var47 * var29 + var49 * var35);
                        this.setMxy(var47 * var31 + var49 * var37);
                        this.setMxz(var47 * var33 + var49 * var39);
                        this.setMyx(var51 * var29 + var53 * var35);
                        this.setMyy(var51 * var31 + var53 * var37);
                        this.setMyz(var51 * var33 + var53 * var39);
                        this.setMzx(var41);
                        this.setMzy(var43);
                        this.setMzz(var45);
                  }
            }

            this.updateState();
         }
      }
   }

   private void rotate2D(double var1) {
      if (this.state3d != 0) {
         this.rotate3D(var1);
      } else {
         double var3 = Math.sin(Math.toRadians(var1));
         if (var3 == 1.0) {
            this.rotate2D_90();
         } else if (var3 == -1.0) {
            this.rotate2D_270();
         } else {
            double var5 = Math.cos(Math.toRadians(var1));
            if (var5 == -1.0) {
               this.rotate2D_180();
            } else if (var5 != 1.0) {
               double var7 = this.getMxx();
               double var9 = this.getMxy();
               this.setMxx(var5 * var7 + var3 * var9);
               this.setMxy(-var3 * var7 + var5 * var9);
               var7 = this.getMyx();
               var9 = this.getMyy();
               this.setMyx(var5 * var7 + var3 * var9);
               this.setMyy(-var3 * var7 + var5 * var9);
               this.updateState2D();
            }
         }

      }
   }

   private void rotate2D_90() {
      double var1 = this.getMxx();
      this.setMxx(this.getMxy());
      this.setMxy(-var1);
      var1 = this.getMyx();
      this.setMyx(this.getMyy());
      this.setMyy(-var1);
      int var3 = rot90conversion[this.state2d];
      if ((var3 & 6) == 2 && this.getMxx() == 1.0 && this.getMyy() == 1.0) {
         var3 -= 2;
      } else if ((var3 & 6) == 4 && this.getMxy() == 0.0 && this.getMyx() == 0.0) {
         var3 = var3 & -5 | 2;
      }

      this.state2d = var3;
   }

   private void rotate2D_180() {
      this.setMxx(-this.getMxx());
      this.setMyy(-this.getMyy());
      int var1 = this.state2d;
      if ((var1 & 4) != 0) {
         this.setMxy(-this.getMxy());
         this.setMyx(-this.getMyx());
      } else if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
         this.state2d = var1 & -3;
      } else {
         this.state2d = var1 | 2;
      }

   }

   private void rotate2D_270() {
      double var1 = this.getMxx();
      this.setMxx(-this.getMxy());
      this.setMxy(var1);
      var1 = this.getMyx();
      this.setMyx(-this.getMyy());
      this.setMyy(var1);
      int var3 = rot90conversion[this.state2d];
      if ((var3 & 6) == 2 && this.getMxx() == 1.0 && this.getMyy() == 1.0) {
         var3 -= 2;
      } else if ((var3 & 6) == 4 && this.getMxy() == 0.0 && this.getMyx() == 0.0) {
         var3 = var3 & -5 | 2;
      }

      this.state2d = var3;
   }

   private void rotate3D(double var1) {
      if (this.state3d == 0) {
         this.rotate2D(var1);
      } else {
         double var3 = Math.sin(Math.toRadians(var1));
         if (var3 == 1.0) {
            this.rotate3D_90();
         } else if (var3 == -1.0) {
            this.rotate3D_270();
         } else {
            double var5 = Math.cos(Math.toRadians(var1));
            if (var5 == -1.0) {
               this.rotate3D_180();
            } else if (var5 != 1.0) {
               double var7 = this.getMxx();
               double var9 = this.getMxy();
               this.setMxx(var5 * var7 + var3 * var9);
               this.setMxy(-var3 * var7 + var5 * var9);
               var7 = this.getMyx();
               var9 = this.getMyy();
               this.setMyx(var5 * var7 + var3 * var9);
               this.setMyy(-var3 * var7 + var5 * var9);
               var7 = this.getMzx();
               var9 = this.getMzy();
               this.setMzx(var5 * var7 + var3 * var9);
               this.setMzy(-var3 * var7 + var5 * var9);
               this.updateState();
            }
         }

      }
   }

   private void rotate3D_90() {
      double var1 = this.getMxx();
      this.setMxx(this.getMxy());
      this.setMxy(-var1);
      var1 = this.getMyx();
      this.setMyx(this.getMyy());
      this.setMyy(-var1);
      var1 = this.getMzx();
      this.setMzx(this.getMzy());
      this.setMzy(-var1);
      switch (this.state3d) {
         case 2:
         case 3:
            if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
               this.state3d = 4;
            }

            return;
         case 4:
            this.updateState();
            return;
         default:
            stateError();
         case 1:
            this.state3d = 4;
      }
   }

   private void rotate3D_180() {
      double var1 = this.getMxx();
      double var3 = this.getMyy();
      this.setMxx(-var1);
      this.setMyy(-var3);
      if (this.state3d == 4) {
         this.setMxy(-this.getMxy());
         this.setMyx(-this.getMyx());
         this.setMzx(-this.getMzx());
         this.setMzy(-this.getMzy());
         this.updateState();
      } else {
         if (var1 == -1.0 && var3 == -1.0 && this.getMzz() == 1.0) {
            this.state3d &= -3;
         } else {
            this.state3d |= 2;
         }

      }
   }

   private void rotate3D_270() {
      double var1 = this.getMxx();
      this.setMxx(-this.getMxy());
      this.setMxy(var1);
      var1 = this.getMyx();
      this.setMyx(-this.getMyy());
      this.setMyy(var1);
      var1 = this.getMzx();
      this.setMzx(-this.getMzy());
      this.setMzy(var1);
      switch (this.state3d) {
         case 2:
         case 3:
            if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
               this.state3d = 4;
            }

            return;
         case 4:
            this.updateState();
            return;
         default:
            stateError();
         case 1:
            this.state3d = 4;
      }
   }

   public void prependRotation(double var1) {
      this.atomicChange.start();
      this.preRotate2D(var1);
      this.atomicChange.end();
   }

   public void prependRotation(double var1, double var3, double var5) {
      this.atomicChange.start();
      if (var3 == 0.0 && var5 == 0.0) {
         this.preRotate2D(var1);
      } else {
         this.preTranslate2D(-var3, -var5);
         this.preRotate2D(var1);
         this.preTranslate2D(var3, var5);
      }

      this.atomicChange.end();
   }

   public void prependRotation(double var1, Point2D var3) {
      this.prependRotation(var1, var3.getX(), var3.getY());
   }

   public void prependRotation(double var1, double var3, double var5, double var7, double var9, double var11, double var13) {
      this.atomicChange.start();
      if (var3 == 0.0 && var5 == 0.0 && var7 == 0.0) {
         this.preRotate3D(var1, var9, var11, var13);
      } else {
         this.preTranslate3D(-var3, -var5, -var7);
         this.preRotate3D(var1, var9, var11, var13);
         this.preTranslate3D(var3, var5, var7);
      }

      this.atomicChange.end();
   }

   public void prependRotation(double var1, double var3, double var5, double var7, Point3D var9) {
      this.prependRotation(var1, var3, var5, var7, var9.getX(), var9.getY(), var9.getZ());
   }

   public void prependRotation(double var1, Point3D var3, Point3D var4) {
      this.prependRotation(var1, var3.getX(), var3.getY(), var3.getZ(), var4.getX(), var4.getY(), var4.getZ());
   }

   private void preRotate3D(double var1, double var3, double var5, double var7) {
      if (var3 == 0.0 && var5 == 0.0) {
         if (var7 > 0.0) {
            this.preRotate3D(var1);
         } else if (var7 < 0.0) {
            this.preRotate3D(-var1);
         }

      } else {
         double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
         if (var9 != 0.0) {
            var9 = 1.0 / var9;
            double var11 = var3 * var9;
            double var13 = var5 * var9;
            double var15 = var7 * var9;
            double var17 = Math.sin(Math.toRadians(var1));
            double var19 = Math.cos(Math.toRadians(var1));
            double var21 = 1.0 - var19;
            double var23 = var11 * var15;
            double var25 = var11 * var13;
            double var27 = var13 * var15;
            double var29 = var21 * var11 * var11 + var19;
            double var31 = var21 * var25 - var17 * var15;
            double var33 = var21 * var23 + var17 * var13;
            double var35 = var21 * var25 + var17 * var15;
            double var37 = var21 * var13 * var13 + var19;
            double var39 = var21 * var27 - var17 * var11;
            double var41 = var21 * var23 - var17 * var13;
            double var43 = var21 * var27 + var17 * var11;
            double var45 = var21 * var15 * var15 + var19;
            double var65;
            double var67;
            double var69;
            double var71;
            double var73;
            double var75;
            double var77;
            double var79;
            double var81;
            double var83;
            double var85;
            double var87;
            double var89;
            double var91;
            double var93;
            double var47;
            double var49;
            double var51;
            double var53;
            double var55;
            double var57;
            double var59;
            double var61;
            double var63;
            switch (this.state3d) {
               case 1:
                  var47 = this.getTx();
                  var49 = this.getTy();
                  var51 = this.getTz();
                  this.setMxx(var29);
                  this.setMxy(var31);
                  this.setMxz(var33);
                  this.setMyx(var35);
                  this.setMyy(var37);
                  this.setMyz(var39);
                  this.setMzx(var41);
                  this.setMzy(var43);
                  this.setMzz(var45);
                  this.setTx(var29 * var47 + var31 * var49 + var33 * var51);
                  this.setTy(var35 * var47 + var37 * var49 + var39 * var51);
                  this.setTz(var41 * var47 + var43 * var49 + var45 * var51);
                  break;
               case 2:
                  var53 = this.getMxx();
                  var55 = this.getMyy();
                  var57 = this.getMzz();
                  this.setMxx(var29 * var53);
                  this.setMxy(var31 * var55);
                  this.setMxz(var33 * var57);
                  this.setMyx(var35 * var53);
                  this.setMyy(var37 * var55);
                  this.setMyz(var39 * var57);
                  this.setMzx(var41 * var53);
                  this.setMzy(var43 * var55);
                  this.setMzz(var45 * var57);
                  break;
               case 3:
                  var59 = this.getMxx();
                  var61 = this.getTx();
                  var63 = this.getMyy();
                  var65 = this.getTy();
                  var67 = this.getMzz();
                  var69 = this.getTz();
                  this.setMxx(var29 * var59);
                  this.setMxy(var31 * var63);
                  this.setMxz(var33 * var67);
                  this.setTx(var29 * var61 + var31 * var65 + var33 * var69);
                  this.setMyx(var35 * var59);
                  this.setMyy(var37 * var63);
                  this.setMyz(var39 * var67);
                  this.setTy(var35 * var61 + var37 * var65 + var39 * var69);
                  this.setMzx(var41 * var59);
                  this.setMzy(var43 * var63);
                  this.setMzz(var45 * var67);
                  this.setTz(var41 * var61 + var43 * var65 + var45 * var69);
                  break;
               case 4:
                  var71 = this.getMxx();
                  var73 = this.getMxy();
                  var75 = this.getMxz();
                  var77 = this.getTx();
                  var79 = this.getMyx();
                  var81 = this.getMyy();
                  var83 = this.getMyz();
                  var85 = this.getTy();
                  var87 = this.getMzx();
                  var89 = this.getMzy();
                  var91 = this.getMzz();
                  var93 = this.getTz();
                  this.setMxx(var29 * var71 + var31 * var79 + var33 * var87);
                  this.setMxy(var29 * var73 + var31 * var81 + var33 * var89);
                  this.setMxz(var29 * var75 + var31 * var83 + var33 * var91);
                  this.setTx(var29 * var77 + var31 * var85 + var33 * var93);
                  this.setMyx(var35 * var71 + var37 * var79 + var39 * var87);
                  this.setMyy(var35 * var73 + var37 * var81 + var39 * var89);
                  this.setMyz(var35 * var75 + var37 * var83 + var39 * var91);
                  this.setTy(var35 * var77 + var37 * var85 + var39 * var93);
                  this.setMzx(var41 * var71 + var43 * var79 + var45 * var87);
                  this.setMzy(var41 * var73 + var43 * var81 + var45 * var89);
                  this.setMzz(var41 * var75 + var43 * var83 + var45 * var91);
                  this.setTz(var41 * var77 + var43 * var85 + var45 * var93);
                  break;
               default:
                  stateError();
               case 0:
                  switch (this.state2d) {
                     case 0:
                        this.setMxx(var29);
                        this.setMxy(var31);
                        this.setMxz(var33);
                        this.setMyx(var35);
                        this.setMyy(var37);
                        this.setMyz(var39);
                        this.setMzx(var41);
                        this.setMzy(var43);
                        this.setMzz(var45);
                        break;
                     case 1:
                        var91 = this.getTx();
                        var93 = this.getTy();
                        this.setMxx(var29);
                        this.setMxy(var31);
                        this.setMxz(var33);
                        this.setTx(var29 * var91 + var31 * var93);
                        this.setMyx(var35);
                        this.setMyy(var37);
                        this.setMyz(var39);
                        this.setTy(var35 * var91 + var37 * var93);
                        this.setMzx(var41);
                        this.setMzy(var43);
                        this.setMzz(var45);
                        this.setTz(var41 * var91 + var43 * var93);
                        break;
                     case 2:
                        var87 = this.getMxx();
                        var89 = this.getMyy();
                        this.setMxx(var29 * var87);
                        this.setMxy(var31 * var89);
                        this.setMxz(var33);
                        this.setMyx(var35 * var87);
                        this.setMyy(var37 * var89);
                        this.setMyz(var39);
                        this.setMzx(var41 * var87);
                        this.setMzy(var43 * var89);
                        this.setMzz(var45);
                        break;
                     case 3:
                        var79 = this.getMxx();
                        var81 = this.getTx();
                        var83 = this.getMyy();
                        var85 = this.getTy();
                        this.setMxx(var29 * var79);
                        this.setMxy(var31 * var83);
                        this.setMxz(var33);
                        this.setTx(var29 * var81 + var31 * var85);
                        this.setMyx(var35 * var79);
                        this.setMyy(var37 * var83);
                        this.setMyz(var39);
                        this.setTy(var35 * var81 + var37 * var85);
                        this.setMzx(var41 * var79);
                        this.setMzy(var43 * var83);
                        this.setMzz(var45);
                        this.setTz(var41 * var81 + var43 * var85);
                        break;
                     case 4:
                        var75 = this.getMxy();
                        var77 = this.getMyx();
                        this.setMxx(var31 * var77);
                        this.setMxy(var29 * var75);
                        this.setMxz(var33);
                        this.setMyx(var37 * var77);
                        this.setMyy(var35 * var75);
                        this.setMyz(var39);
                        this.setMzx(var43 * var77);
                        this.setMzy(var41 * var75);
                        this.setMzz(var45);
                        break;
                     case 5:
                        var67 = this.getMxy();
                        var69 = this.getTx();
                        var71 = this.getMyx();
                        var73 = this.getTy();
                        this.setMxx(var31 * var71);
                        this.setMxy(var29 * var67);
                        this.setMxz(var33);
                        this.setTx(var29 * var69 + var31 * var73);
                        this.setMyx(var37 * var71);
                        this.setMyy(var35 * var67);
                        this.setMyz(var39);
                        this.setTy(var35 * var69 + var37 * var73);
                        this.setMzx(var43 * var71);
                        this.setMzy(var41 * var67);
                        this.setMzz(var45);
                        this.setTz(var41 * var69 + var43 * var73);
                        break;
                     case 6:
                        var59 = this.getMxx();
                        var61 = this.getMxy();
                        var63 = this.getMyx();
                        var65 = this.getMyy();
                        this.setMxx(var29 * var59 + var31 * var63);
                        this.setMxy(var29 * var61 + var31 * var65);
                        this.setMxz(var33);
                        this.setMyx(var35 * var59 + var37 * var63);
                        this.setMyy(var35 * var61 + var37 * var65);
                        this.setMyz(var39);
                        this.setMzx(var41 * var59 + var43 * var63);
                        this.setMzy(var41 * var61 + var43 * var65);
                        this.setMzz(var45);
                        break;
                     default:
                        stateError();
                     case 7:
                        var47 = this.getMxx();
                        var49 = this.getMxy();
                        var51 = this.getTx();
                        var53 = this.getMyx();
                        var55 = this.getMyy();
                        var57 = this.getTy();
                        this.setMxx(var29 * var47 + var31 * var53);
                        this.setMxy(var29 * var49 + var31 * var55);
                        this.setMxz(var33);
                        this.setTx(var29 * var51 + var31 * var57);
                        this.setMyx(var35 * var47 + var37 * var53);
                        this.setMyy(var35 * var49 + var37 * var55);
                        this.setMyz(var39);
                        this.setTy(var35 * var51 + var37 * var57);
                        this.setMzx(var41 * var47 + var43 * var53);
                        this.setMzy(var41 * var49 + var43 * var55);
                        this.setMzz(var45);
                        this.setTz(var41 * var51 + var43 * var57);
                  }
            }

            this.updateState();
         }
      }
   }

   private void preRotate2D(double var1) {
      if (this.state3d != 0) {
         this.preRotate3D(var1);
      } else {
         double var3 = Math.sin(Math.toRadians(var1));
         if (var3 == 1.0) {
            this.preRotate2D_90();
         } else if (var3 == -1.0) {
            this.preRotate2D_270();
         } else {
            double var5 = Math.cos(Math.toRadians(var1));
            if (var5 == -1.0) {
               this.preRotate2D_180();
            } else if (var5 != 1.0) {
               double var7 = this.getMxx();
               double var9 = this.getMyx();
               this.setMxx(var5 * var7 - var3 * var9);
               this.setMyx(var3 * var7 + var5 * var9);
               var7 = this.getMxy();
               var9 = this.getMyy();
               this.setMxy(var5 * var7 - var3 * var9);
               this.setMyy(var3 * var7 + var5 * var9);
               var7 = this.getTx();
               var9 = this.getTy();
               this.setTx(var5 * var7 - var3 * var9);
               this.setTy(var3 * var7 + var5 * var9);
               this.updateState2D();
            }
         }

      }
   }

   private void preRotate2D_90() {
      double var1 = this.getMxx();
      this.setMxx(-this.getMyx());
      this.setMyx(var1);
      var1 = this.getMxy();
      this.setMxy(-this.getMyy());
      this.setMyy(var1);
      var1 = this.getTx();
      this.setTx(-this.getTy());
      this.setTy(var1);
      int var3 = rot90conversion[this.state2d];
      if ((var3 & 6) == 2 && this.getMxx() == 1.0 && this.getMyy() == 1.0) {
         var3 -= 2;
      } else if ((var3 & 6) == 4 && this.getMxy() == 0.0 && this.getMyx() == 0.0) {
         var3 = var3 & -5 | 2;
      }

      this.state2d = var3;
   }

   private void preRotate2D_180() {
      this.setMxx(-this.getMxx());
      this.setMxy(-this.getMxy());
      this.setTx(-this.getTx());
      this.setMyx(-this.getMyx());
      this.setMyy(-this.getMyy());
      this.setTy(-this.getTy());
      if ((this.state2d & 4) != 0) {
         if (this.getMxx() == 0.0 && this.getMyy() == 0.0) {
            this.state2d &= -3;
         } else {
            this.state2d |= 2;
         }
      } else if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
         this.state2d &= -3;
      } else {
         this.state2d |= 2;
      }

   }

   private void preRotate2D_270() {
      double var1 = this.getMxx();
      this.setMxx(this.getMyx());
      this.setMyx(-var1);
      var1 = this.getMxy();
      this.setMxy(this.getMyy());
      this.setMyy(-var1);
      var1 = this.getTx();
      this.setTx(this.getTy());
      this.setTy(-var1);
      int var3 = rot90conversion[this.state2d];
      if ((var3 & 6) == 2 && this.getMxx() == 1.0 && this.getMyy() == 1.0) {
         var3 -= 2;
      } else if ((var3 & 6) == 4 && this.getMxy() == 0.0 && this.getMyx() == 0.0) {
         var3 = var3 & -5 | 2;
      }

      this.state2d = var3;
   }

   private void preRotate3D(double var1) {
      if (this.state3d == 0) {
         this.preRotate2D(var1);
      } else {
         double var3 = Math.sin(Math.toRadians(var1));
         if (var3 == 1.0) {
            this.preRotate3D_90();
         } else if (var3 == -1.0) {
            this.preRotate3D_270();
         } else {
            double var5 = Math.cos(Math.toRadians(var1));
            if (var5 == -1.0) {
               this.preRotate3D_180();
            } else if (var5 != 1.0) {
               double var7 = this.getMxx();
               double var9 = this.getMyx();
               this.setMxx(var5 * var7 - var3 * var9);
               this.setMyx(var3 * var7 + var5 * var9);
               var7 = this.getMxy();
               var9 = this.getMyy();
               this.setMxy(var5 * var7 - var3 * var9);
               this.setMyy(var3 * var7 + var5 * var9);
               var7 = this.getMxz();
               var9 = this.getMyz();
               this.setMxz(var5 * var7 - var3 * var9);
               this.setMyz(var3 * var7 + var5 * var9);
               var7 = this.getTx();
               var9 = this.getTy();
               this.setTx(var5 * var7 - var3 * var9);
               this.setTy(var3 * var7 + var5 * var9);
               this.updateState();
            }
         }

      }
   }

   private void preRotate3D_90() {
      double var1 = this.getMxx();
      this.setMxx(-this.getMyx());
      this.setMyx(var1);
      var1 = this.getMxy();
      this.setMxy(-this.getMyy());
      this.setMyy(var1);
      var1 = this.getMxz();
      this.setMxz(-this.getMyz());
      this.setMyz(var1);
      var1 = this.getTx();
      this.setTx(-this.getTy());
      this.setTy(var1);
      switch (this.state3d) {
         case 2:
         case 3:
            if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
               this.state3d = 4;
            }

            return;
         case 4:
            this.updateState();
            return;
         default:
            stateError();
         case 1:
            this.state3d = 4;
      }
   }

   private void preRotate3D_180() {
      double var1 = this.getMxx();
      double var3 = this.getMyy();
      this.setMxx(-var1);
      this.setMyy(-var3);
      this.setTx(-this.getTx());
      this.setTy(-this.getTy());
      if (this.state3d == 4) {
         this.setMxy(-this.getMxy());
         this.setMxz(-this.getMxz());
         this.setMyx(-this.getMyx());
         this.setMyz(-this.getMyz());
         this.updateState();
      } else {
         if (var1 == -1.0 && var3 == -1.0 && this.getMzz() == 1.0) {
            this.state3d &= -3;
         } else {
            this.state3d |= 2;
         }

      }
   }

   private void preRotate3D_270() {
      double var1 = this.getMxx();
      this.setMxx(this.getMyx());
      this.setMyx(-var1);
      var1 = this.getMxy();
      this.setMxy(this.getMyy());
      this.setMyy(-var1);
      var1 = this.getMxz();
      this.setMxz(this.getMyz());
      this.setMyz(-var1);
      var1 = this.getTx();
      this.setTx(this.getTy());
      this.setTy(-var1);
      switch (this.state3d) {
         case 2:
         case 3:
            if (this.getMxy() != 0.0 || this.getMyx() != 0.0) {
               this.state3d = 4;
            }

            return;
         case 4:
            this.updateState();
            return;
         default:
            stateError();
         case 1:
            this.state3d = 4;
      }
   }

   public Point2D transform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      switch (this.state2d) {
         case 0:
            return new Point2D(var1, var3);
         case 1:
            return new Point2D(var1 + this.getTx(), var3 + this.getTy());
         case 2:
            return new Point2D(this.getMxx() * var1, this.getMyy() * var3);
         case 3:
            return new Point2D(this.getMxx() * var1 + this.getTx(), this.getMyy() * var3 + this.getTy());
         case 4:
            return new Point2D(this.getMxy() * var3, this.getMyx() * var1);
         case 5:
            return new Point2D(this.getMxy() * var3 + this.getTx(), this.getMyx() * var1 + this.getTy());
         case 6:
            return new Point2D(this.getMxx() * var1 + this.getMxy() * var3, this.getMyx() * var1 + this.getMyy() * var3);
         default:
            stateError();
         case 7:
            return new Point2D(this.getMxx() * var1 + this.getMxy() * var3 + this.getTx(), this.getMyx() * var1 + this.getMyy() * var3 + this.getTy());
      }
   }

   public Point3D transform(double var1, double var3, double var5) {
      switch (this.state3d) {
         case 1:
            return new Point3D(var1 + this.getTx(), var3 + this.getTy(), var5 + this.getTz());
         case 2:
            return new Point3D(this.getMxx() * var1, this.getMyy() * var3, this.getMzz() * var5);
         case 3:
            return new Point3D(this.getMxx() * var1 + this.getTx(), this.getMyy() * var3 + this.getTy(), this.getMzz() * var5 + this.getTz());
         case 4:
            return new Point3D(this.getMxx() * var1 + this.getMxy() * var3 + this.getMxz() * var5 + this.getTx(), this.getMyx() * var1 + this.getMyy() * var3 + this.getMyz() * var5 + this.getTy(), this.getMzx() * var1 + this.getMzy() * var3 + this.getMzz() * var5 + this.getTz());
         default:
            stateError();
         case 0:
            switch (this.state2d) {
               case 0:
                  return new Point3D(var1, var3, var5);
               case 1:
                  return new Point3D(var1 + this.getTx(), var3 + this.getTy(), var5);
               case 2:
                  return new Point3D(this.getMxx() * var1, this.getMyy() * var3, var5);
               case 3:
                  return new Point3D(this.getMxx() * var1 + this.getTx(), this.getMyy() * var3 + this.getTy(), var5);
               case 4:
                  return new Point3D(this.getMxy() * var3, this.getMyx() * var1, var5);
               case 5:
                  return new Point3D(this.getMxy() * var3 + this.getTx(), this.getMyx() * var1 + this.getTy(), var5);
               case 6:
                  return new Point3D(this.getMxx() * var1 + this.getMxy() * var3, this.getMyx() * var1 + this.getMyy() * var3, var5);
               default:
                  stateError();
               case 7:
                  return new Point3D(this.getMxx() * var1 + this.getMxy() * var3 + this.getTx(), this.getMyx() * var1 + this.getMyy() * var3 + this.getTy(), var5);
            }
      }
   }

   void transform2DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6;
      double var8;
      double var10;
      double var12;
      double var14;
      double var16;
      double var18;
      double var20;
      switch (this.state2d) {
         case 0:
            if (var1 != var3 || var2 != var4) {
               System.arraycopy(var1, var2, var3, var4, var5 * 2);
            }

            return;
         case 1:
            var10 = this.getTx();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var1[var2++] + var10;
               var3[var4++] = var1[var2++] + var16;
            }
         case 2:
            var6 = this.getMxx();
            var14 = this.getMyy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var6 * var1[var2++];
               var3[var4++] = var14 * var1[var2++];
            }
         case 3:
            var6 = this.getMxx();
            var10 = this.getTx();
            var14 = this.getMyy();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var6 * var1[var2++] + var10;
               var3[var4++] = var14 * var1[var2++] + var16;
            }
         case 4:
            var8 = this.getMxy();
            var12 = this.getMyx();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = var1[var2++];
               var3[var4++] = var8 * var1[var2++];
               var3[var4++] = var12 * var18;
            }
         case 5:
            var8 = this.getMxy();
            var10 = this.getTx();
            var12 = this.getMyx();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = var1[var2++];
               var3[var4++] = var8 * var1[var2++] + var10;
               var3[var4++] = var12 * var18 + var16;
            }
         case 6:
            var6 = this.getMxx();
            var8 = this.getMxy();
            var12 = this.getMyx();
            var14 = this.getMyy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = var1[var2++];
               var20 = var1[var2++];
               var3[var4++] = var6 * var18 + var8 * var20;
               var3[var4++] = var12 * var18 + var14 * var20;
            }
         default:
            stateError();
         case 7:
            var6 = this.getMxx();
            var8 = this.getMxy();
            var10 = this.getTx();
            var12 = this.getMyx();
            var14 = this.getMyy();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var18 = var1[var2++];
               var20 = var1[var2++];
               var3[var4++] = var6 * var18 + var8 * var20 + var10;
               var3[var4++] = var12 * var18 + var14 * var20 + var16;
            }
      }
   }

   void transform3DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6;
      double var8;
      double var10;
      double var12;
      double var14;
      double var16;
      double var18;
      double var20;
      double var22;
      double var24;
      switch (this.state3d) {
         case 0:
            break;
         case 1:
            var10 = this.getTx();
            var16 = this.getTy();
            var20 = this.getTz();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var1[var2++] + var10;
               var3[var4++] = var1[var2++] + var16;
               var3[var4++] = var1[var2++] + var20;
            }
         case 2:
            var6 = this.getMxx();
            var14 = this.getMyy();
            var18 = this.getMzz();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var6 * var1[var2++];
               var3[var4++] = var14 * var1[var2++];
               var3[var4++] = var18 * var1[var2++];
            }
         case 3:
            var6 = this.getMxx();
            var10 = this.getTx();
            var14 = this.getMyy();
            var16 = this.getTy();
            var18 = this.getMzz();
            var20 = this.getTz();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var6 * var1[var2++] + var10;
               var3[var4++] = var14 * var1[var2++] + var16;
               var3[var4++] = var18 * var1[var2++] + var20;
            }
         case 4:
            var6 = this.getMxx();
            var8 = this.getMxy();
            var22 = this.getMxz();
            var10 = this.getTx();
            var12 = this.getMyx();
            var14 = this.getMyy();
            var24 = this.getMyz();
            var16 = this.getTy();
            double var26 = this.getMzx();
            double var28 = this.getMzy();
            var18 = this.getMzz();
            var20 = this.getTz();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               double var30 = var1[var2++];
               double var32 = var1[var2++];
               double var34 = var1[var2++];
               var3[var4++] = var6 * var30 + var8 * var32 + var22 * var34 + var10;
               var3[var4++] = var12 * var30 + var14 * var32 + var24 * var34 + var16;
               var3[var4++] = var26 * var30 + var28 * var32 + var18 * var34 + var20;
            }
         default:
            stateError();
      }

      switch (this.state2d) {
         case 0:
            if (var1 != var3 || var2 != var4) {
               System.arraycopy(var1, var2, var3, var4, var5 * 3);
            }

            return;
         case 1:
            var10 = this.getTx();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var1[var2++] + var10;
               var3[var4++] = var1[var2++] + var16;
               var3[var4++] = var1[var2++];
            }
         case 2:
            var6 = this.getMxx();
            var14 = this.getMyy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var6 * var1[var2++];
               var3[var4++] = var14 * var1[var2++];
               var3[var4++] = var1[var2++];
            }
         case 3:
            var6 = this.getMxx();
            var10 = this.getTx();
            var14 = this.getMyy();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var6 * var1[var2++] + var10;
               var3[var4++] = var14 * var1[var2++] + var16;
               var3[var4++] = var1[var2++];
            }
         case 4:
            var8 = this.getMxy();
            var12 = this.getMyx();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var22 = var1[var2++];
               var3[var4++] = var8 * var1[var2++];
               var3[var4++] = var12 * var22;
               var3[var4++] = var1[var2++];
            }
         case 5:
            var8 = this.getMxy();
            var10 = this.getTx();
            var12 = this.getMyx();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var22 = var1[var2++];
               var3[var4++] = var8 * var1[var2++] + var10;
               var3[var4++] = var12 * var22 + var16;
               var3[var4++] = var1[var2++];
            }
         case 6:
            var6 = this.getMxx();
            var8 = this.getMxy();
            var12 = this.getMyx();
            var14 = this.getMyy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var22 = var1[var2++];
               var24 = var1[var2++];
               var3[var4++] = var6 * var22 + var8 * var24;
               var3[var4++] = var12 * var22 + var14 * var24;
               var3[var4++] = var1[var2++];
            }
         default:
            stateError();
         case 7:
            var6 = this.getMxx();
            var8 = this.getMxy();
            var10 = this.getTx();
            var12 = this.getMyx();
            var14 = this.getMyy();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var22 = var1[var2++];
               var24 = var1[var2++];
               var3[var4++] = var6 * var22 + var8 * var24 + var10;
               var3[var4++] = var12 * var22 + var14 * var24 + var16;
               var3[var4++] = var1[var2++];
            }
      }
   }

   public Point2D deltaTransform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      switch (this.state2d) {
         case 0:
         case 1:
            return new Point2D(var1, var3);
         case 2:
         case 3:
            return new Point2D(this.getMxx() * var1, this.getMyy() * var3);
         case 4:
         case 5:
            return new Point2D(this.getMxy() * var3, this.getMyx() * var1);
         default:
            stateError();
         case 6:
         case 7:
            return new Point2D(this.getMxx() * var1 + this.getMxy() * var3, this.getMyx() * var1 + this.getMyy() * var3);
      }
   }

   public Point3D deltaTransform(double var1, double var3, double var5) {
      switch (this.state3d) {
         case 1:
            return new Point3D(var1, var3, var5);
         case 2:
         case 3:
            return new Point3D(this.getMxx() * var1, this.getMyy() * var3, this.getMzz() * var5);
         case 4:
            return new Point3D(this.getMxx() * var1 + this.getMxy() * var3 + this.getMxz() * var5, this.getMyx() * var1 + this.getMyy() * var3 + this.getMyz() * var5, this.getMzx() * var1 + this.getMzy() * var3 + this.getMzz() * var5);
         default:
            stateError();
         case 0:
            switch (this.state2d) {
               case 0:
               case 1:
                  return new Point3D(var1, var3, var5);
               case 2:
               case 3:
                  return new Point3D(this.getMxx() * var1, this.getMyy() * var3, var5);
               case 4:
               case 5:
                  return new Point3D(this.getMxy() * var3, this.getMyx() * var1, var5);
               default:
                  stateError();
               case 6:
               case 7:
                  return new Point3D(this.getMxx() * var1 + this.getMxy() * var3, this.getMyx() * var1 + this.getMyy() * var3, var5);
            }
      }
   }

   public Point2D inverseTransform(double var1, double var3) throws NonInvertibleTransformException {
      this.ensureCanTransform2DPoint();
      switch (this.state2d) {
         case 0:
            return new Point2D(var1, var3);
         case 1:
            return new Point2D(var1 - this.getTx(), var3 - this.getTy());
         case 2:
            double var17 = this.getMxx();
            double var19 = this.getMyy();
            if (var17 != 0.0 && var19 != 0.0) {
               return new Point2D(1.0 / var17 * var1, 1.0 / var19 * var3);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 3:
            double var13 = this.getMxx();
            double var15 = this.getMyy();
            if (var13 != 0.0 && var15 != 0.0) {
               return new Point2D(1.0 / var13 * var1 - this.getTx() / var13, 1.0 / var15 * var3 - this.getTy() / var15);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
            double var9 = this.getMxy();
            double var11 = this.getMyx();
            if (var9 != 0.0 && var11 != 0.0) {
               return new Point2D(1.0 / var11 * var3, 1.0 / var9 * var1);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 5:
            double var5 = this.getMxy();
            double var7 = this.getMyx();
            if (var5 != 0.0 && var7 != 0.0) {
               return new Point2D(1.0 / var7 * var3 - this.getTy() / var7, 1.0 / var5 * var1 - this.getTx() / var5);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         default:
            return super.inverseTransform(var1, var3);
      }
   }

   public Point3D inverseTransform(double var1, double var3, double var5) throws NonInvertibleTransformException {
      double var17;
      double var7;
      double var9;
      double var11;
      double var13;
      double var15;
      switch (this.state3d) {
         case 1:
            return new Point3D(var1 - this.getTx(), var3 - this.getTy(), var5 - this.getTz());
         case 2:
            var7 = this.getMxx();
            var9 = this.getMyy();
            var11 = this.getMzz();
            if (var7 != 0.0 && var9 != 0.0 && var11 != 0.0) {
               return new Point3D(1.0 / var7 * var1, 1.0 / var9 * var3, 1.0 / var11 * var5);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 3:
            var13 = this.getMxx();
            var15 = this.getMyy();
            var17 = this.getMzz();
            if (var13 != 0.0 && var15 != 0.0 && var17 != 0.0) {
               return new Point3D(1.0 / var13 * var1 - this.getTx() / var13, 1.0 / var15 * var3 - this.getTy() / var15, 1.0 / var17 * var5 - this.getTz() / var17);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
            return super.inverseTransform(var1, var3, var5);
         default:
            stateError();
         case 0:
            switch (this.state2d) {
               case 0:
                  return new Point3D(var1, var3, var5);
               case 1:
                  return new Point3D(var1 - this.getTx(), var3 - this.getTy(), var5);
               case 2:
                  double var19 = this.getMxx();
                  double var21 = this.getMyy();
                  if (var19 != 0.0 && var21 != 0.0) {
                     return new Point3D(1.0 / var19 * var1, 1.0 / var21 * var3, var5);
                  }

                  throw new NonInvertibleTransformException("Determinant is 0");
               case 3:
                  var15 = this.getMxx();
                  var17 = this.getMyy();
                  if (var15 != 0.0 && var17 != 0.0) {
                     return new Point3D(1.0 / var15 * var1 - this.getTx() / var15, 1.0 / var17 * var3 - this.getTy() / var17, var5);
                  }

                  throw new NonInvertibleTransformException("Determinant is 0");
               case 4:
                  var11 = this.getMxy();
                  var13 = this.getMyx();
                  if (var11 != 0.0 && var13 != 0.0) {
                     return new Point3D(1.0 / var13 * var3, 1.0 / var11 * var1, var5);
                  }

                  throw new NonInvertibleTransformException("Determinant is 0");
               case 5:
                  var7 = this.getMxy();
                  var9 = this.getMyx();
                  if (var7 != 0.0 && var9 != 0.0) {
                     return new Point3D(1.0 / var9 * var3 - this.getTy() / var9, 1.0 / var7 * var1 - this.getTx() / var7, var5);
                  }

                  throw new NonInvertibleTransformException("Determinant is 0");
               default:
                  return super.inverseTransform(var1, var3, var5);
            }
      }
   }

   void inverseTransform2DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) throws NonInvertibleTransformException {
      double var6;
      double var8;
      double var10;
      double var12;
      double var14;
      double var16;
      double var18;
      double var20;
      switch (this.state2d) {
         case 0:
            if (var1 != var3 || var2 != var4) {
               System.arraycopy(var1, var2, var3, var4, var5 * 2);
            }

            return;
         case 1:
            var10 = this.getTx();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var1[var2++] - var10;
               var3[var4++] = var1[var2++] - var16;
            }
         case 2:
            var6 = this.getMxx();
            var14 = this.getMyy();
            if (var6 != 0.0 && var14 != 0.0) {
               var6 = 1.0 / var6;
               var14 = 1.0 / var14;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = var6 * var1[var2++];
                  var3[var4++] = var14 * var1[var2++];
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 3:
            var6 = this.getMxx();
            var10 = this.getTx();
            var14 = this.getMyy();
            var16 = this.getTy();
            if (var6 != 0.0 && var14 != 0.0) {
               var10 = -var10 / var6;
               var16 = -var16 / var14;
               var6 = 1.0 / var6;
               var14 = 1.0 / var14;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = var6 * var1[var2++] + var10;
                  var3[var4++] = var14 * var1[var2++] + var16;
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
            var8 = this.getMxy();
            var12 = this.getMyx();
            if (var8 != 0.0 && var12 != 0.0) {
               var18 = var12;
               var12 = 1.0 / var8;
               var8 = 1.0 / var18;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var20 = var1[var2++];
                  var3[var4++] = var8 * var1[var2++];
                  var3[var4++] = var12 * var20;
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 5:
            var8 = this.getMxy();
            var10 = this.getTx();
            var12 = this.getMyx();
            var16 = this.getTy();
            if (var8 != 0.0 && var12 != 0.0) {
               var18 = var10;
               var10 = -var16 / var12;
               var16 = -var18 / var8;
               var18 = var12;
               var12 = 1.0 / var8;
               var8 = 1.0 / var18;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var20 = var1[var2++];
                  var3[var4++] = var8 * var1[var2++] + var10;
                  var3[var4++] = var12 * var20 + var16;
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         default:
            super.inverseTransform2DPointsImpl(var1, var2, var3, var4, var5);
      }
   }

   void inverseTransform3DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) throws NonInvertibleTransformException {
      double var6;
      double var10;
      double var14;
      double var16;
      double var18;
      double var20;
      switch (this.state3d) {
         case 0:
            break;
         case 1:
            var10 = this.getTx();
            var16 = this.getTy();
            var20 = this.getTz();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var1[var2++] - var10;
               var3[var4++] = var1[var2++] - var16;
               var3[var4++] = var1[var2++] - var20;
            }
         case 2:
            var6 = this.getMxx();
            var14 = this.getMyy();
            var18 = this.getMzz();
            if (var6 != 0.0 && !(var14 == 0.0 | var18 == 0.0)) {
               var6 = 1.0 / var6;
               var14 = 1.0 / var14;
               var18 = 1.0 / var18;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = var6 * var1[var2++];
                  var3[var4++] = var14 * var1[var2++];
                  var3[var4++] = var18 * var1[var2++];
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 3:
            var6 = this.getMxx();
            var10 = this.getTx();
            var14 = this.getMyy();
            var16 = this.getTy();
            var18 = this.getMzz();
            var20 = this.getTz();
            if (var6 != 0.0 && var14 != 0.0 && var18 != 0.0) {
               var10 = -var10 / var6;
               var16 = -var16 / var14;
               var20 = -var20 / var18;
               var6 = 1.0 / var6;
               var14 = 1.0 / var14;
               var18 = 1.0 / var18;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = var6 * var1[var2++] + var10;
                  var3[var4++] = var14 * var1[var2++] + var16;
                  var3[var4++] = var18 * var1[var2++] + var20;
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
            super.inverseTransform3DPointsImpl(var1, var2, var3, var4, var5);
            return;
         default:
            stateError();
      }

      double var8;
      double var12;
      double var22;
      double var24;
      switch (this.state2d) {
         case 0:
            if (var1 != var3 || var2 != var4) {
               System.arraycopy(var1, var2, var3, var4, var5 * 3);
            }

            return;
         case 1:
            var10 = this.getTx();
            var16 = this.getTy();

            while(true) {
               --var5;
               if (var5 < 0) {
                  return;
               }

               var3[var4++] = var1[var2++] - var10;
               var3[var4++] = var1[var2++] - var16;
               var3[var4++] = var1[var2++];
            }
         case 2:
            var6 = this.getMxx();
            var14 = this.getMyy();
            if (var6 != 0.0 && var14 != 0.0) {
               var6 = 1.0 / var6;
               var14 = 1.0 / var14;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = var6 * var1[var2++];
                  var3[var4++] = var14 * var1[var2++];
                  var3[var4++] = var1[var2++];
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 3:
            var6 = this.getMxx();
            var10 = this.getTx();
            var14 = this.getMyy();
            var16 = this.getTy();
            if (var6 != 0.0 && var14 != 0.0) {
               var10 = -var10 / var6;
               var16 = -var16 / var14;
               var6 = 1.0 / var6;
               var14 = 1.0 / var14;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var3[var4++] = var6 * var1[var2++] + var10;
                  var3[var4++] = var14 * var1[var2++] + var16;
                  var3[var4++] = var1[var2++];
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
            var8 = this.getMxy();
            var12 = this.getMyx();
            if (var8 != 0.0 && var12 != 0.0) {
               var22 = var12;
               var12 = 1.0 / var8;
               var8 = 1.0 / var22;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var24 = var1[var2++];
                  var3[var4++] = var8 * var1[var2++];
                  var3[var4++] = var12 * var24;
                  var3[var4++] = var1[var2++];
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 5:
            var8 = this.getMxy();
            var10 = this.getTx();
            var12 = this.getMyx();
            var16 = this.getTy();
            if (var8 != 0.0 && var12 != 0.0) {
               var22 = var10;
               var10 = -var16 / var12;
               var16 = -var22 / var8;
               var22 = var12;
               var12 = 1.0 / var8;
               var8 = 1.0 / var22;

               while(true) {
                  --var5;
                  if (var5 < 0) {
                     return;
                  }

                  var24 = var1[var2++];
                  var3[var4++] = var8 * var1[var2++] + var10;
                  var3[var4++] = var12 * var24 + var16;
                  var3[var4++] = var1[var2++];
               }
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         default:
            super.inverseTransform3DPointsImpl(var1, var2, var3, var4, var5);
      }
   }

   public Point2D inverseDeltaTransform(double var1, double var3) throws NonInvertibleTransformException {
      this.ensureCanTransform2DPoint();
      switch (this.state2d) {
         case 0:
         case 1:
            return new Point2D(var1, var3);
         case 2:
         case 3:
            double var9 = this.getMxx();
            double var11 = this.getMyy();
            if (var9 != 0.0 && var11 != 0.0) {
               return new Point2D(1.0 / var9 * var1, 1.0 / var11 * var3);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
         case 5:
            double var5 = this.getMxy();
            double var7 = this.getMyx();
            if (var5 != 0.0 && var7 != 0.0) {
               return new Point2D(1.0 / var7 * var3, 1.0 / var5 * var1);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         default:
            return super.inverseDeltaTransform(var1, var3);
      }
   }

   public Point3D inverseDeltaTransform(double var1, double var3, double var5) throws NonInvertibleTransformException {
      double var7;
      double var9;
      double var11;
      switch (this.state3d) {
         case 1:
            return new Point3D(var1, var3, var5);
         case 2:
         case 3:
            var7 = this.getMxx();
            var9 = this.getMyy();
            var11 = this.getMzz();
            if (var7 != 0.0 && var9 != 0.0 && var11 != 0.0) {
               return new Point3D(1.0 / var7 * var1, 1.0 / var9 * var3, 1.0 / var11 * var5);
            }

            throw new NonInvertibleTransformException("Determinant is 0");
         case 4:
            return super.inverseDeltaTransform(var1, var3, var5);
         default:
            stateError();
         case 0:
            switch (this.state2d) {
               case 0:
               case 1:
                  return new Point3D(var1, var3, var5);
               case 2:
               case 3:
                  var11 = this.getMxx();
                  double var13 = this.getMyy();
                  if (var11 != 0.0 && var13 != 0.0) {
                     return new Point3D(1.0 / var11 * var1, 1.0 / var13 * var3, var5);
                  }

                  throw new NonInvertibleTransformException("Determinant is 0");
               case 4:
               case 5:
                  var7 = this.getMxy();
                  var9 = this.getMyx();
                  if (var7 != 0.0 && var9 != 0.0) {
                     return new Point3D(1.0 / var9 * var3, 1.0 / var7 * var1, var5);
                  }

                  throw new NonInvertibleTransformException("Determinant is 0");
               default:
                  return super.inverseDeltaTransform(var1, var3, var5);
            }
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Affine [\n");
      var1.append("\t").append(this.getMxx());
      var1.append(", ").append(this.getMxy());
      var1.append(", ").append(this.getMxz());
      var1.append(", ").append(this.getTx());
      var1.append('\n');
      var1.append("\t").append(this.getMyx());
      var1.append(", ").append(this.getMyy());
      var1.append(", ").append(this.getMyz());
      var1.append(", ").append(this.getTy());
      var1.append('\n');
      var1.append("\t").append(this.getMzx());
      var1.append(", ").append(this.getMzy());
      var1.append(", ").append(this.getMzz());
      var1.append(", ").append(this.getTz());
      return var1.append("\n]").toString();
   }

   private void updateState() {
      this.updateState2D();
      this.state3d = 0;
      if (this.getMxz() == 0.0 && this.getMyz() == 0.0 && this.getMzx() == 0.0 && this.getMzy() == 0.0) {
         if ((this.state2d & 4) == 0) {
            if (this.getTz() != 0.0) {
               this.state3d |= 1;
            }

            if (this.getMzz() != 1.0) {
               this.state3d |= 2;
            }

            if (this.state3d != 0) {
               this.state3d |= this.state2d & 3;
            }
         } else if (this.getMzz() != 1.0 || this.getTz() != 0.0) {
            this.state3d = 4;
         }
      } else {
         this.state3d = 4;
      }

   }

   private void updateState2D() {
      if (this.getMxy() == 0.0 && this.getMyx() == 0.0) {
         if (this.getMxx() == 1.0 && this.getMyy() == 1.0) {
            if (this.getTx() == 0.0 && this.getTy() == 0.0) {
               this.state2d = 0;
            } else {
               this.state2d = 1;
            }
         } else if (this.getTx() == 0.0 && this.getTy() == 0.0) {
            this.state2d = 2;
         } else {
            this.state2d = 3;
         }
      } else if (this.getMxx() == 0.0 && this.getMyy() == 0.0) {
         if (this.getTx() == 0.0 && this.getTy() == 0.0) {
            this.state2d = 4;
         } else {
            this.state2d = 5;
         }
      } else if (this.getTx() == 0.0 && this.getTy() == 0.0) {
         this.state2d = 6;
      } else {
         this.state2d = 7;
      }

   }

   private static void stateError() {
      throw new InternalError("missing case in a switch");
   }

   /** @deprecated */
   @Deprecated
   public void impl_apply(Affine3D var1) {
      var1.concatenate(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
   }

   /** @deprecated */
   @Deprecated
   public BaseTransform impl_derive(BaseTransform var1) {
      switch (this.state3d) {
         case 1:
            return var1.deriveWithTranslation(this.getTx(), this.getTy(), this.getTz());
         case 2:
            return var1.deriveWithScale(this.getMxx(), this.getMyy(), this.getMzz());
         case 3:
         case 4:
            return var1.deriveWithConcatenation(this.getMxx(), this.getMxy(), this.getMxz(), this.getTx(), this.getMyx(), this.getMyy(), this.getMyz(), this.getTy(), this.getMzx(), this.getMzy(), this.getMzz(), this.getTz());
         default:
            stateError();
         case 0:
            switch (this.state2d) {
               case 0:
                  return var1;
               case 1:
                  return var1.deriveWithTranslation(this.getTx(), this.getTy());
               case 2:
                  return var1.deriveWithScale(this.getMxx(), this.getMyy(), 1.0);
               case 3:
               default:
                  return var1.deriveWithConcatenation(this.getMxx(), this.getMyx(), this.getMxy(), this.getMyy(), this.getTx(), this.getTy());
            }
      }
   }

   int getState2d() {
      return this.state2d;
   }

   int getState3d() {
      return this.state3d;
   }

   boolean atomicChangeRuns() {
      return this.atomicChange.runs();
   }

   private class AffineAtomicChange {
      private boolean running;

      private AffineAtomicChange() {
         this.running = false;
      }

      private void start() {
         if (this.running) {
            throw new InternalError("Affine internal error: trying to run inner atomic operation");
         } else {
            if (Affine.this.mxx != null) {
               Affine.this.mxx.preProcessAtomicChange();
            }

            if (Affine.this.mxy != null) {
               Affine.this.mxy.preProcessAtomicChange();
            }

            if (Affine.this.mxz != null) {
               Affine.this.mxz.preProcessAtomicChange();
            }

            if (Affine.this.tx != null) {
               Affine.this.tx.preProcessAtomicChange();
            }

            if (Affine.this.myx != null) {
               Affine.this.myx.preProcessAtomicChange();
            }

            if (Affine.this.myy != null) {
               Affine.this.myy.preProcessAtomicChange();
            }

            if (Affine.this.myz != null) {
               Affine.this.myz.preProcessAtomicChange();
            }

            if (Affine.this.ty != null) {
               Affine.this.ty.preProcessAtomicChange();
            }

            if (Affine.this.mzx != null) {
               Affine.this.mzx.preProcessAtomicChange();
            }

            if (Affine.this.mzy != null) {
               Affine.this.mzy.preProcessAtomicChange();
            }

            if (Affine.this.mzz != null) {
               Affine.this.mzz.preProcessAtomicChange();
            }

            if (Affine.this.tz != null) {
               Affine.this.tz.preProcessAtomicChange();
            }

            this.running = true;
         }
      }

      private void end() {
         this.running = false;
         Affine.this.transformChanged();
         if (Affine.this.mxx != null) {
            Affine.this.mxx.postProcessAtomicChange();
         }

         if (Affine.this.mxy != null) {
            Affine.this.mxy.postProcessAtomicChange();
         }

         if (Affine.this.mxz != null) {
            Affine.this.mxz.postProcessAtomicChange();
         }

         if (Affine.this.tx != null) {
            Affine.this.tx.postProcessAtomicChange();
         }

         if (Affine.this.myx != null) {
            Affine.this.myx.postProcessAtomicChange();
         }

         if (Affine.this.myy != null) {
            Affine.this.myy.postProcessAtomicChange();
         }

         if (Affine.this.myz != null) {
            Affine.this.myz.postProcessAtomicChange();
         }

         if (Affine.this.ty != null) {
            Affine.this.ty.postProcessAtomicChange();
         }

         if (Affine.this.mzx != null) {
            Affine.this.mzx.postProcessAtomicChange();
         }

         if (Affine.this.mzy != null) {
            Affine.this.mzy.postProcessAtomicChange();
         }

         if (Affine.this.mzz != null) {
            Affine.this.mzz.postProcessAtomicChange();
         }

         if (Affine.this.tz != null) {
            Affine.this.tz.postProcessAtomicChange();
         }

      }

      private void cancel() {
         this.running = false;
      }

      private boolean runs() {
         return this.running;
      }

      // $FF: synthetic method
      AffineAtomicChange(Object var2) {
         this();
      }
   }

   private class AffineElementProperty extends SimpleDoubleProperty {
      private boolean needsValueChangedEvent = false;
      private double oldValue;

      public AffineElementProperty(double var2) {
         super(var2);
      }

      public void invalidated() {
         if (!Affine.this.atomicChange.runs()) {
            Affine.this.updateState();
            Affine.this.transformChanged();
         }

      }

      protected void fireValueChangedEvent() {
         if (!Affine.this.atomicChange.runs()) {
            super.fireValueChangedEvent();
         } else {
            this.needsValueChangedEvent = true;
         }

      }

      private void preProcessAtomicChange() {
         this.oldValue = this.get();
      }

      private void postProcessAtomicChange() {
         if (this.needsValueChangedEvent) {
            this.needsValueChangedEvent = false;
            if (this.oldValue != this.get()) {
               super.fireValueChangedEvent();
            }
         }

      }
   }
}
