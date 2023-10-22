package com.sun.prism;

public class TextureMap {
   private final PhongMaterial.MapType type;
   private Image image;
   private Texture texture;
   private boolean dirty;

   public TextureMap(PhongMaterial.MapType var1) {
      this.type = var1;
   }

   public PhongMaterial.MapType getType() {
      return this.type;
   }

   public Image getImage() {
      return this.image;
   }

   public void setImage(Image var1) {
      this.image = var1;
   }

   public Texture getTexture() {
      return this.texture;
   }

   public void setTexture(Texture var1) {
      this.texture = var1;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(boolean var1) {
      this.dirty = var1;
   }
}
