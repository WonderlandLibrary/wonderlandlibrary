// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;
import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BreakingFour extends BakedQuad
{
    private final TextureAtlasSprite texture;
    private static final String __OBFID = "CL_00002492";
    
    public BreakingFour(final BakedQuad p_i46217_1_, final TextureAtlasSprite p_i46217_2_) {
        super(Arrays.copyOf(p_i46217_1_.func_178209_a(), p_i46217_1_.func_178209_a().length), p_i46217_1_.field_178213_b, FaceBakery.func_178410_a(p_i46217_1_.func_178209_a()));
        this.texture = p_i46217_2_;
        this.func_178217_e();
    }
    
    private void func_178217_e() {
        for (int var1 = 0; var1 < 4; ++var1) {
            this.func_178216_a(var1);
        }
    }
    
    private void func_178216_a(final int p_178216_1_) {
        final int var2 = 7 * p_178216_1_;
        final float var3 = Float.intBitsToFloat(this.field_178215_a[var2]);
        final float var4 = Float.intBitsToFloat(this.field_178215_a[var2 + 1]);
        final float var5 = Float.intBitsToFloat(this.field_178215_a[var2 + 2]);
        float var6 = 0.0f;
        float var7 = 0.0f;
        switch (SwitchEnumFacing.field_178419_a[this.face.ordinal()]) {
            case 1: {
                var6 = var3 * 16.0f;
                var7 = (1.0f - var5) * 16.0f;
                break;
            }
            case 2: {
                var6 = var3 * 16.0f;
                var7 = var5 * 16.0f;
                break;
            }
            case 3: {
                var6 = (1.0f - var3) * 16.0f;
                var7 = (1.0f - var4) * 16.0f;
                break;
            }
            case 4: {
                var6 = var3 * 16.0f;
                var7 = (1.0f - var4) * 16.0f;
                break;
            }
            case 5: {
                var6 = var5 * 16.0f;
                var7 = (1.0f - var4) * 16.0f;
                break;
            }
            case 6: {
                var6 = (1.0f - var5) * 16.0f;
                var7 = (1.0f - var4) * 16.0f;
                break;
            }
        }
        this.field_178215_a[var2 + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU(var6));
        this.field_178215_a[var2 + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(var7));
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_178419_a;
        private static final String __OBFID = "CL_00002491";
        
        static {
            field_178419_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_178419_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_178419_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_178419_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_178419_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_178419_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumFacing.field_178419_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
