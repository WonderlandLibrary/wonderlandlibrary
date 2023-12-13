// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

public class ColorizerFoliage
{
    private static int[] foliageBuffer;
    private static final String __OBFID = "CL_00000135";
    
    public static void setFoliageBiomeColorizer(final int[] p_77467_0_) {
        ColorizerFoliage.foliageBuffer = p_77467_0_;
    }
    
    public static int getFoliageColor(final double p_77470_0_, double p_77470_2_) {
        p_77470_2_ *= p_77470_0_;
        final int var4 = (int)((1.0 - p_77470_0_) * 255.0);
        final int var5 = (int)((1.0 - p_77470_2_) * 255.0);
        return ColorizerFoliage.foliageBuffer[var5 << 8 | var4];
    }
    
    public static int getFoliageColorPine() {
        return 6396257;
    }
    
    public static int getFoliageColorBirch() {
        return 8431445;
    }
    
    public static int getFoliageColorBasic() {
        return 4764952;
    }
    
    static {
        ColorizerFoliage.foliageBuffer = new int[65536];
    }
}
