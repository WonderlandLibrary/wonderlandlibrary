package javafx.scene.effect;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import com.sun.scenario.effect.light.DistantLight;
import com.sun.scenario.effect.light.PointLight;
import com.sun.scenario.effect.light.SpotLight;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

public abstract class Light {
   private com.sun.scenario.effect.light.Light peer;
   private ObjectProperty color;
   private BooleanProperty effectDirty;

   protected Light() {
      this.impl_markDirty();
   }

   abstract com.sun.scenario.effect.light.Light impl_createImpl();

   com.sun.scenario.effect.light.Light impl_getImpl() {
      if (this.peer == null) {
         this.peer = this.impl_createImpl();
      }

      return this.peer;
   }

   public final void setColor(Color var1) {
      this.colorProperty().set(var1);
   }

   public final Color getColor() {
      return this.color == null ? Color.WHITE : (Color)this.color.get();
   }

   public final ObjectProperty colorProperty() {
      if (this.color == null) {
         this.color = new ObjectPropertyBase(Color.WHITE) {
            public void invalidated() {
               Light.this.impl_markDirty();
            }

            public Object getBean() {
               return Light.this;
            }

            public String getName() {
               return "color";
            }
         };
      }

      return this.color;
   }

   void impl_sync() {
      if (this.impl_isEffectDirty()) {
         this.impl_update();
         this.impl_clearDirty();
      }

   }

   private Color getColorInternal() {
      Color var1 = this.getColor();
      return var1 == null ? Color.WHITE : var1;
   }

   void impl_update() {
      this.impl_getImpl().setColor(Toolkit.getToolkit().toColor4f(this.getColorInternal()));
   }

   private void setEffectDirty(boolean var1) {
      this.effectDirtyProperty().set(var1);
   }

   final BooleanProperty effectDirtyProperty() {
      if (this.effectDirty == null) {
         this.effectDirty = new SimpleBooleanProperty(this, "effectDirty");
      }

      return this.effectDirty;
   }

   boolean impl_isEffectDirty() {
      return this.effectDirty == null ? false : this.effectDirty.get();
   }

   final void impl_markDirty() {
      this.setEffectDirty(true);
   }

   final void impl_clearDirty() {
      this.setEffectDirty(false);
   }

   public static class Spot extends Point {
      private DoubleProperty pointsAtX;
      private DoubleProperty pointsAtY;
      private DoubleProperty pointsAtZ;
      private DoubleProperty specularExponent;

      public Spot() {
      }

      public Spot(double var1, double var3, double var5, double var7, Color var9) {
         this.setX(var1);
         this.setY(var3);
         this.setZ(var5);
         this.setSpecularExponent(var7);
         this.setColor(var9);
      }

      /** @deprecated */
      @Deprecated
      SpotLight impl_createImpl() {
         return new SpotLight();
      }

      public final void setPointsAtX(double var1) {
         this.pointsAtXProperty().set(var1);
      }

      public final double getPointsAtX() {
         return this.pointsAtX == null ? 0.0 : this.pointsAtX.get();
      }

      public final DoubleProperty pointsAtXProperty() {
         if (this.pointsAtX == null) {
            this.pointsAtX = new DoublePropertyBase() {
               public void invalidated() {
                  Spot.this.impl_markDirty();
               }

               public Object getBean() {
                  return Spot.this;
               }

               public String getName() {
                  return "pointsAtX";
               }
            };
         }

         return this.pointsAtX;
      }

      public final void setPointsAtY(double var1) {
         this.pointsAtYProperty().set(var1);
      }

      public final double getPointsAtY() {
         return this.pointsAtY == null ? 0.0 : this.pointsAtY.get();
      }

      public final DoubleProperty pointsAtYProperty() {
         if (this.pointsAtY == null) {
            this.pointsAtY = new DoublePropertyBase() {
               public void invalidated() {
                  Spot.this.impl_markDirty();
               }

               public Object getBean() {
                  return Spot.this;
               }

               public String getName() {
                  return "pointsAtY";
               }
            };
         }

         return this.pointsAtY;
      }

      public final void setPointsAtZ(double var1) {
         this.pointsAtZProperty().set(var1);
      }

      public final double getPointsAtZ() {
         return this.pointsAtZ == null ? 0.0 : this.pointsAtZ.get();
      }

      public final DoubleProperty pointsAtZProperty() {
         if (this.pointsAtZ == null) {
            this.pointsAtZ = new DoublePropertyBase() {
               public void invalidated() {
                  Spot.this.impl_markDirty();
               }

               public Object getBean() {
                  return Spot.this;
               }

               public String getName() {
                  return "pointsAtZ";
               }
            };
         }

         return this.pointsAtZ;
      }

      public final void setSpecularExponent(double var1) {
         this.specularExponentProperty().set(var1);
      }

      public final double getSpecularExponent() {
         return this.specularExponent == null ? 1.0 : this.specularExponent.get();
      }

      public final DoubleProperty specularExponentProperty() {
         if (this.specularExponent == null) {
            this.specularExponent = new DoublePropertyBase(1.0) {
               public void invalidated() {
                  Spot.this.impl_markDirty();
               }

               public Object getBean() {
                  return Spot.this;
               }

               public String getName() {
                  return "specularExponent";
               }
            };
         }

         return this.specularExponent;
      }

      void impl_update() {
         super.impl_update();
         SpotLight var1 = (SpotLight)this.impl_getImpl();
         var1.setPointsAtX((float)this.getPointsAtX());
         var1.setPointsAtY((float)this.getPointsAtY());
         var1.setPointsAtZ((float)this.getPointsAtZ());
         var1.setSpecularExponent((float)Utils.clamp(0.0, this.getSpecularExponent(), 4.0));
      }
   }

   public static class Point extends Light {
      private DoubleProperty x;
      private DoubleProperty y;
      private DoubleProperty z;

      public Point() {
      }

      public Point(double var1, double var3, double var5, Color var7) {
         this.setX(var1);
         this.setY(var3);
         this.setZ(var5);
         this.setColor(var7);
      }

      /** @deprecated */
      @Deprecated
      PointLight impl_createImpl() {
         return new PointLight();
      }

      public final void setX(double var1) {
         this.xProperty().set(var1);
      }

      public final double getX() {
         return this.x == null ? 0.0 : this.x.get();
      }

      public final DoubleProperty xProperty() {
         if (this.x == null) {
            this.x = new DoublePropertyBase() {
               public void invalidated() {
                  Point.this.impl_markDirty();
               }

               public Object getBean() {
                  return Point.this;
               }

               public String getName() {
                  return "x";
               }
            };
         }

         return this.x;
      }

      public final void setY(double var1) {
         this.yProperty().set(var1);
      }

      public final double getY() {
         return this.y == null ? 0.0 : this.y.get();
      }

      public final DoubleProperty yProperty() {
         if (this.y == null) {
            this.y = new DoublePropertyBase() {
               public void invalidated() {
                  Point.this.impl_markDirty();
               }

               public Object getBean() {
                  return Point.this;
               }

               public String getName() {
                  return "y";
               }
            };
         }

         return this.y;
      }

      public final void setZ(double var1) {
         this.zProperty().set(var1);
      }

      public final double getZ() {
         return this.z == null ? 0.0 : this.z.get();
      }

      public final DoubleProperty zProperty() {
         if (this.z == null) {
            this.z = new DoublePropertyBase() {
               public void invalidated() {
                  Point.this.impl_markDirty();
               }

               public Object getBean() {
                  return Point.this;
               }

               public String getName() {
                  return "z";
               }
            };
         }

         return this.z;
      }

      void impl_update() {
         super.impl_update();
         PointLight var1 = (PointLight)this.impl_getImpl();
         var1.setX((float)this.getX());
         var1.setY((float)this.getY());
         var1.setZ((float)this.getZ());
      }
   }

   public static class Distant extends Light {
      private DoubleProperty azimuth;
      private DoubleProperty elevation;

      public Distant() {
      }

      public Distant(double var1, double var3, Color var5) {
         this.setAzimuth(var1);
         this.setElevation(var3);
         this.setColor(var5);
      }

      DistantLight impl_createImpl() {
         return new DistantLight();
      }

      public final void setAzimuth(double var1) {
         this.azimuthProperty().set(var1);
      }

      public final double getAzimuth() {
         return this.azimuth == null ? 45.0 : this.azimuth.get();
      }

      public final DoubleProperty azimuthProperty() {
         if (this.azimuth == null) {
            this.azimuth = new DoublePropertyBase(45.0) {
               public void invalidated() {
                  Distant.this.impl_markDirty();
               }

               public Object getBean() {
                  return Distant.this;
               }

               public String getName() {
                  return "azimuth";
               }
            };
         }

         return this.azimuth;
      }

      public final void setElevation(double var1) {
         this.elevationProperty().set(var1);
      }

      public final double getElevation() {
         return this.elevation == null ? 45.0 : this.elevation.get();
      }

      public final DoubleProperty elevationProperty() {
         if (this.elevation == null) {
            this.elevation = new DoublePropertyBase(45.0) {
               public void invalidated() {
                  Distant.this.impl_markDirty();
               }

               public Object getBean() {
                  return Distant.this;
               }

               public String getName() {
                  return "elevation";
               }
            };
         }

         return this.elevation;
      }

      void impl_update() {
         super.impl_update();
         DistantLight var1 = (DistantLight)this.impl_getImpl();
         var1.setAzimuth((float)this.getAzimuth());
         var1.setElevation((float)this.getElevation());
      }
   }
}
