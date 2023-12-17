// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

public class ColorizerFoliage
{
    private static int[] foliageBuffer;
    
    public static void setFoliageBiomeColorizer(final int[] foliageBufferIn) {
        ColorizerFoliage.foliageBuffer = foliageBufferIn;
    }
    
    public static int getFoliageColor(final double temperature, double humidity) {
        humidity *= temperature;
        final int i = (int)((1.0 - temperature) * 255.0);
        final int j = (int)((1.0 - humidity) * 255.0);
        return ColorizerFoliage.foliageBuffer[j << 8 | i];
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
