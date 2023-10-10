package net.minecraft.client.resources.data;

import net.minecraft.util.IChatComponent;

public class PackMetadataSection implements IMetadataSection {
   private final int packFormat;
   private final IChatComponent packDescription;

   public IChatComponent getPackDescription() {
      return this.packDescription;
   }

   public PackMetadataSection(IChatComponent var1, int var2) {
      this.packDescription = var1;
      this.packFormat = var2;
   }

   public int getPackFormat() {
      return this.packFormat;
   }
}
