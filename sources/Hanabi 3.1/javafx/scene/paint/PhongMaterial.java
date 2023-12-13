package javafx.scene.paint;

import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.sg.prism.NGPhongMaterial;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class PhongMaterial extends Material {
   private boolean diffuseColorDirty = true;
   private boolean specularColorDirty = true;
   private boolean specularPowerDirty = true;
   private boolean diffuseMapDirty = true;
   private boolean specularMapDirty = true;
   private boolean bumpMapDirty = true;
   private boolean selfIlluminationMapDirty = true;
   private ObjectProperty diffuseColor;
   private ObjectProperty specularColor;
   private DoubleProperty specularPower;
   private final AbstractNotifyListener platformImageChangeListener = new AbstractNotifyListener() {
      public void invalidated(Observable var1) {
         if (PhongMaterial.this.oldDiffuseMap != null && var1 == Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldDiffuseMap)) {
            PhongMaterial.this.diffuseMapDirty = true;
         } else if (PhongMaterial.this.oldSpecularMap != null && var1 == Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldSpecularMap)) {
            PhongMaterial.this.specularMapDirty = true;
         } else if (PhongMaterial.this.oldBumpMap != null && var1 == Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldBumpMap)) {
            PhongMaterial.this.bumpMapDirty = true;
         } else if (PhongMaterial.this.oldSelfIlluminationMap != null && var1 == Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldSelfIlluminationMap)) {
            PhongMaterial.this.selfIlluminationMapDirty = true;
         }

         PhongMaterial.this.setDirty(true);
      }
   };
   private ObjectProperty diffuseMap;
   private Image oldDiffuseMap;
   private ObjectProperty specularMap;
   private Image oldSpecularMap;
   private ObjectProperty bumpMap;
   private Image oldBumpMap;
   private ObjectProperty selfIlluminationMap;
   private Image oldSelfIlluminationMap;
   private NGPhongMaterial peer;

   public PhongMaterial() {
      this.setDiffuseColor(Color.WHITE);
   }

   public PhongMaterial(Color var1) {
      this.setDiffuseColor(var1);
   }

   public PhongMaterial(Color var1, Image var2, Image var3, Image var4, Image var5) {
      this.setDiffuseColor(var1);
      this.setDiffuseMap(var2);
      this.setSpecularMap(var3);
      this.setBumpMap(var4);
      this.setSelfIlluminationMap(var5);
   }

   public final void setDiffuseColor(Color var1) {
      this.diffuseColorProperty().set(var1);
   }

   public final Color getDiffuseColor() {
      return this.diffuseColor == null ? null : (Color)this.diffuseColor.get();
   }

   public final ObjectProperty diffuseColorProperty() {
      if (this.diffuseColor == null) {
         this.diffuseColor = new SimpleObjectProperty(this, "diffuseColor") {
            protected void invalidated() {
               PhongMaterial.this.diffuseColorDirty = true;
               PhongMaterial.this.setDirty(true);
            }
         };
      }

      return this.diffuseColor;
   }

   public final void setSpecularColor(Color var1) {
      this.specularColorProperty().set(var1);
   }

   public final Color getSpecularColor() {
      return this.specularColor == null ? null : (Color)this.specularColor.get();
   }

   public final ObjectProperty specularColorProperty() {
      if (this.specularColor == null) {
         this.specularColor = new SimpleObjectProperty(this, "specularColor") {
            protected void invalidated() {
               PhongMaterial.this.specularColorDirty = true;
               PhongMaterial.this.setDirty(true);
            }
         };
      }

      return this.specularColor;
   }

   public final void setSpecularPower(double var1) {
      this.specularPowerProperty().set(var1);
   }

   public final double getSpecularPower() {
      return this.specularPower == null ? 32.0 : this.specularPower.get();
   }

   public final DoubleProperty specularPowerProperty() {
      if (this.specularPower == null) {
         this.specularPower = new SimpleDoubleProperty(this, "specularPower", 32.0) {
            public void invalidated() {
               PhongMaterial.this.specularPowerDirty = true;
               PhongMaterial.this.setDirty(true);
            }
         };
      }

      return this.specularPower;
   }

   public final void setDiffuseMap(Image var1) {
      this.diffuseMapProperty().set(var1);
   }

   public final Image getDiffuseMap() {
      return this.diffuseMap == null ? null : (Image)this.diffuseMap.get();
   }

   public final ObjectProperty diffuseMapProperty() {
      if (this.diffuseMap == null) {
         this.diffuseMap = new SimpleObjectProperty(this, "diffuseMap") {
            private boolean needsListeners = false;

            public void invalidated() {
               Image var1 = (Image)this.get();
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldDiffuseMap).removeListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
               }

               this.needsListeners = var1 != null && (Toolkit.getImageAccessor().isAnimation(var1) || var1.getProgress() < 1.0);
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(var1).addListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
               }

               PhongMaterial.this.oldDiffuseMap = var1;
               PhongMaterial.this.diffuseMapDirty = true;
               PhongMaterial.this.setDirty(true);
            }
         };
      }

      return this.diffuseMap;
   }

   public final void setSpecularMap(Image var1) {
      this.specularMapProperty().set(var1);
   }

   public final Image getSpecularMap() {
      return this.specularMap == null ? null : (Image)this.specularMap.get();
   }

   public final ObjectProperty specularMapProperty() {
      if (this.specularMap == null) {
         this.specularMap = new SimpleObjectProperty(this, "specularMap") {
            private boolean needsListeners = false;

            public void invalidated() {
               Image var1 = (Image)this.get();
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldSpecularMap).removeListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
               }

               this.needsListeners = var1 != null && (Toolkit.getImageAccessor().isAnimation(var1) || var1.getProgress() < 1.0);
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(var1).addListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
               }

               PhongMaterial.this.oldSpecularMap = var1;
               PhongMaterial.this.specularMapDirty = true;
               PhongMaterial.this.setDirty(true);
            }
         };
      }

      return this.specularMap;
   }

   public final void setBumpMap(Image var1) {
      this.bumpMapProperty().set(var1);
   }

   public final Image getBumpMap() {
      return this.bumpMap == null ? null : (Image)this.bumpMap.get();
   }

   public final ObjectProperty bumpMapProperty() {
      if (this.bumpMap == null) {
         this.bumpMap = new SimpleObjectProperty(this, "bumpMap") {
            private boolean needsListeners = false;

            public void invalidated() {
               Image var1 = (Image)this.get();
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldBumpMap).removeListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
               }

               this.needsListeners = var1 != null && (Toolkit.getImageAccessor().isAnimation(var1) || var1.getProgress() < 1.0);
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(var1).addListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
               }

               PhongMaterial.this.oldBumpMap = var1;
               PhongMaterial.this.bumpMapDirty = true;
               PhongMaterial.this.setDirty(true);
            }
         };
      }

      return this.bumpMap;
   }

   public final void setSelfIlluminationMap(Image var1) {
      this.selfIlluminationMapProperty().set(var1);
   }

   public final Image getSelfIlluminationMap() {
      return this.selfIlluminationMap == null ? null : (Image)this.selfIlluminationMap.get();
   }

   public final ObjectProperty selfIlluminationMapProperty() {
      if (this.selfIlluminationMap == null) {
         this.selfIlluminationMap = new SimpleObjectProperty(this, "selfIlluminationMap") {
            private boolean needsListeners = false;

            public void invalidated() {
               Image var1 = (Image)this.get();
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(PhongMaterial.this.oldSelfIlluminationMap).removeListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
               }

               this.needsListeners = var1 != null && (Toolkit.getImageAccessor().isAnimation(var1) || var1.getProgress() < 1.0);
               if (this.needsListeners) {
                  Toolkit.getImageAccessor().getImageProperty(var1).addListener(PhongMaterial.this.platformImageChangeListener.getWeakListener());
               }

               PhongMaterial.this.oldSelfIlluminationMap = var1;
               PhongMaterial.this.selfIlluminationMapDirty = true;
               PhongMaterial.this.setDirty(true);
            }
         };
      }

      return this.selfIlluminationMap;
   }

   void setDirty(boolean var1) {
      super.setDirty(var1);
      if (!var1) {
         this.diffuseColorDirty = false;
         this.specularColorDirty = false;
         this.specularPowerDirty = false;
         this.diffuseMapDirty = false;
         this.specularMapDirty = false;
         this.bumpMapDirty = false;
         this.selfIlluminationMapDirty = false;
      }

   }

   /** @deprecated */
   @Deprecated
   public NGPhongMaterial impl_getNGMaterial() {
      if (this.peer == null) {
         this.peer = new NGPhongMaterial();
      }

      return this.peer;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePG() {
      if (this.isDirty()) {
         NGPhongMaterial var1 = this.impl_getNGMaterial();
         if (this.diffuseColorDirty) {
            var1.setDiffuseColor(this.getDiffuseColor() == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(this.getDiffuseColor()));
         }

         if (this.specularColorDirty) {
            var1.setSpecularColor(this.getSpecularColor() == null ? null : Toolkit.getPaintAccessor().getPlatformPaint(this.getSpecularColor()));
         }

         if (this.specularPowerDirty) {
            var1.setSpecularPower((float)this.getSpecularPower());
         }

         if (this.diffuseMapDirty) {
            var1.setDiffuseMap(this.getDiffuseMap() == null ? null : this.getDiffuseMap().impl_getPlatformImage());
         }

         if (this.specularMapDirty) {
            var1.setSpecularMap(this.getSpecularMap() == null ? null : this.getSpecularMap().impl_getPlatformImage());
         }

         if (this.bumpMapDirty) {
            var1.setBumpMap(this.getBumpMap() == null ? null : this.getBumpMap().impl_getPlatformImage());
         }

         if (this.selfIlluminationMapDirty) {
            var1.setSelfIllumMap(this.getSelfIlluminationMap() == null ? null : this.getSelfIlluminationMap().impl_getPlatformImage());
         }

         this.setDirty(false);
      }
   }

   public String toString() {
      return "PhongMaterial[diffuseColor=" + this.getDiffuseColor() + ", specularColor=" + this.getSpecularColor() + ", specularPower=" + this.getSpecularPower() + ", diffuseMap=" + this.getDiffuseMap() + ", specularMap=" + this.getSpecularMap() + ", bumpMap=" + this.getBumpMap() + ", selfIlluminationMap=" + this.getSelfIlluminationMap() + "]";
   }
}
