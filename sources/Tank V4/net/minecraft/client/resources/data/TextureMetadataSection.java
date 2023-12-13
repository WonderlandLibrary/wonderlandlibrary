package net.minecraft.client.resources.data;

import java.util.Collections;
import java.util.List;

public class TextureMetadataSection implements IMetadataSection {
   private final List listMipmaps;
   private final boolean textureClamp;
   private final boolean textureBlur;

   public TextureMetadataSection(boolean var1, boolean var2, List var3) {
      this.textureBlur = var1;
      this.textureClamp = var2;
      this.listMipmaps = var3;
   }

   public boolean getTextureClamp() {
      return this.textureClamp;
   }

   public List getListMipmaps() {
      return Collections.unmodifiableList(this.listMipmaps);
   }

   public boolean getTextureBlur() {
      return this.textureBlur;
   }
}
