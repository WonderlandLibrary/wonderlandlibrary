package com.sun.prism.image;

import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;

public class CachingCompoundImage extends CompoundImage {
   public CachingCompoundImage(Image var1, int var2) {
      super(var1, var2);
   }

   public Texture getTile(int var1, int var2, ResourceFactory var3) {
      return var3.getCachedTexture(this.tiles[var1 + var2 * this.uSections], Texture.WrapMode.CLAMP_TO_EDGE);
   }
}
