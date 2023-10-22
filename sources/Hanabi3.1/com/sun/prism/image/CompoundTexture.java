package com.sun.prism.image;

import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;

public class CompoundTexture extends CompoundImage implements GraphicsResource {
   protected Texture[] texTiles;

   public CompoundTexture(Image var1, int var2) {
      super(var1, var2);
      this.texTiles = new Texture[this.tiles.length];
   }

   public Texture getTile(int var1, int var2, ResourceFactory var3) {
      int var4 = var1 + var2 * this.uSections;
      Texture var5 = this.texTiles[var4];
      if (var5 != null) {
         var5.lock();
         if (var5.isSurfaceLost()) {
            var5 = null;
            this.texTiles[var4] = null;
         }
      }

      if (var5 == null) {
         var5 = var3.createTexture(this.tiles[var4], Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
         this.texTiles[var4] = var5;
      }

      return var5;
   }

   public void dispose() {
      for(int var1 = 0; var1 != this.texTiles.length; ++var1) {
         if (this.texTiles[var1] != null) {
            this.texTiles[var1].dispose();
            this.texTiles[var1] = null;
         }
      }

   }
}
