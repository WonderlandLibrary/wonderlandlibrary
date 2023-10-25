package net.minecraft.client.resources.data;

import net.minecraft.util.IChatComponent;

public class PackMetadataSection implements IMetadataSection {
  private final IChatComponent packDescription;
  
  private final int packFormat;
  
  public PackMetadataSection(IChatComponent p_i1034_1_, int p_i1034_2_) {
    this.packDescription = p_i1034_1_;
    this.packFormat = p_i1034_2_;
  }
  
  public IChatComponent getPackDescription() {
    return this.packDescription;
  }
  
  public int getPackFormat() {
    return this.packFormat;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\resources\data\PackMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */