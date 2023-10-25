package net.minecraft.world.gen.layer;

public class GenLayerFuzzyZoom extends GenLayerZoom {
  public GenLayerFuzzyZoom(long p_i2123_1_, GenLayer p_i2123_3_) {
    super(p_i2123_1_, p_i2123_3_);
  }
  
  protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_) {
    return selectRandom(new int[] { p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_ });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\gen\layer\GenLayerFuzzyZoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */