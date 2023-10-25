package net.minecraft.client.resources.data;

public class FontMetadataSection implements IMetadataSection {
  private final float[] charWidths;
  
  private final float[] charLefts;
  
  private final float[] charSpacings;
  
  public FontMetadataSection(float[] p_i1310_1_, float[] p_i1310_2_, float[] p_i1310_3_) {
    this.charWidths = p_i1310_1_;
    this.charLefts = p_i1310_2_;
    this.charSpacings = p_i1310_3_;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\resources\data\FontMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */