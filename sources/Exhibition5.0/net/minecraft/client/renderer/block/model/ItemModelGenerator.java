// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.model;

import java.util.Iterator;
import javax.vecmath.Vector3f;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.List;

public class ItemModelGenerator
{
    public static final List LAYERS;
    private static final String __OBFID = "CL_00002488";
    
    public ModelBlock func_178392_a(final TextureMap p_178392_1_, final ModelBlock p_178392_2_) {
        final HashMap var3 = Maps.newHashMap();
        final ArrayList var4 = Lists.newArrayList();
        for (int var5 = 0; var5 < ItemModelGenerator.LAYERS.size(); ++var5) {
            final String var6 = ItemModelGenerator.LAYERS.get(var5);
            if (!p_178392_2_.isTexturePresent(var6)) {
                break;
            }
            final String var7 = p_178392_2_.resolveTextureName(var6);
            var3.put(var6, var7);
            final TextureAtlasSprite var8 = p_178392_1_.getAtlasSprite(new ResourceLocation(var7).toString());
            var4.addAll(this.func_178394_a(var5, var6, var8));
        }
        if (var4.isEmpty()) {
            return null;
        }
        var3.put("particle", p_178392_2_.isTexturePresent("particle") ? p_178392_2_.resolveTextureName("particle") : var3.get("layer0"));
        return new ModelBlock(var4, var3, false, false, new ItemCameraTransforms(p_178392_2_.getThirdPersonTransform(), p_178392_2_.getFirstPersonTransform(), p_178392_2_.getHeadTransform(), p_178392_2_.getInGuiTransform()));
    }
    
    private List func_178394_a(final int p_178394_1_, final String p_178394_2_, final TextureAtlasSprite p_178394_3_) {
        final HashMap var4 = Maps.newHashMap();
        var4.put(EnumFacing.SOUTH, new BlockPartFace(null, p_178394_1_, p_178394_2_, new BlockFaceUV(new float[] { 0.0f, 0.0f, 16.0f, 16.0f }, 0)));
        var4.put(EnumFacing.NORTH, new BlockPartFace(null, p_178394_1_, p_178394_2_, new BlockFaceUV(new float[] { 16.0f, 0.0f, 0.0f, 16.0f }, 0)));
        final ArrayList var5 = Lists.newArrayList();
        var5.add(new BlockPart(new Vector3f(0.0f, 0.0f, 7.5f), new Vector3f(16.0f, 16.0f, 8.5f), var4, null, true));
        var5.addAll(this.func_178397_a(p_178394_3_, p_178394_2_, p_178394_1_));
        return var5;
    }
    
    private List func_178397_a(final TextureAtlasSprite p_178397_1_, final String p_178397_2_, final int p_178397_3_) {
        final float var4 = p_178397_1_.getIconWidth();
        final float var5 = p_178397_1_.getIconHeight();
        final ArrayList var6 = Lists.newArrayList();
        for (final Span var8 : this.func_178393_a(p_178397_1_)) {
            float var9 = 0.0f;
            float var10 = 0.0f;
            float var11 = 0.0f;
            float var12 = 0.0f;
            float var13 = 0.0f;
            float var14 = 0.0f;
            float var15 = 0.0f;
            float var16 = 0.0f;
            float var17 = 0.0f;
            float var18 = 0.0f;
            final float var19 = var8.func_178385_b();
            final float var20 = var8.func_178384_c();
            final float var21 = var8.func_178381_d();
            final SpanFacing var22 = var8.func_178383_a();
            switch (SwitchSpanFacing.field_178390_a[var22.ordinal()]) {
                case 1: {
                    var13 = var19;
                    var9 = var19;
                    var14 = (var11 = var20 + 1.0f);
                    var15 = var21;
                    var10 = var21;
                    var16 = var21;
                    var12 = var21;
                    var17 = 16.0f / var4;
                    var18 = 16.0f / (var5 - 1.0f);
                    break;
                }
                case 2: {
                    var16 = var21;
                    var15 = var21;
                    var13 = var19;
                    var9 = var19;
                    var14 = (var11 = var20 + 1.0f);
                    var10 = var21 + 1.0f;
                    var12 = var21 + 1.0f;
                    var17 = 16.0f / var4;
                    var18 = 16.0f / (var5 - 1.0f);
                    break;
                }
                case 3: {
                    var13 = var21;
                    var9 = var21;
                    var14 = var21;
                    var11 = var21;
                    var16 = var19;
                    var10 = var19;
                    var15 = (var12 = var20 + 1.0f);
                    var17 = 16.0f / (var4 - 1.0f);
                    var18 = 16.0f / var5;
                    break;
                }
                case 4: {
                    var14 = var21;
                    var13 = var21;
                    var9 = var21 + 1.0f;
                    var11 = var21 + 1.0f;
                    var16 = var19;
                    var10 = var19;
                    var15 = (var12 = var20 + 1.0f);
                    var17 = 16.0f / (var4 - 1.0f);
                    var18 = 16.0f / var5;
                    break;
                }
            }
            final float var23 = 16.0f / var4;
            final float var24 = 16.0f / var5;
            var9 *= var23;
            var11 *= var23;
            var10 *= var24;
            var12 *= var24;
            var10 = 16.0f - var10;
            var12 = 16.0f - var12;
            var13 *= var17;
            var14 *= var17;
            var15 *= var18;
            var16 *= var18;
            final HashMap var25 = Maps.newHashMap();
            var25.put(var22.func_178367_a(), new BlockPartFace(null, p_178397_3_, p_178397_2_, new BlockFaceUV(new float[] { var13, var15, var14, var16 }, 0)));
            switch (SwitchSpanFacing.field_178390_a[var22.ordinal()]) {
                case 1: {
                    var6.add(new BlockPart(new Vector3f(var9, var10, 7.5f), new Vector3f(var11, var10, 8.5f), var25, null, true));
                    continue;
                }
                case 2: {
                    var6.add(new BlockPart(new Vector3f(var9, var12, 7.5f), new Vector3f(var11, var12, 8.5f), var25, null, true));
                    continue;
                }
                case 3: {
                    var6.add(new BlockPart(new Vector3f(var9, var10, 7.5f), new Vector3f(var9, var12, 8.5f), var25, null, true));
                    continue;
                }
                case 4: {
                    var6.add(new BlockPart(new Vector3f(var11, var10, 7.5f), new Vector3f(var11, var12, 8.5f), var25, null, true));
                    continue;
                }
            }
        }
        return var6;
    }
    
    private List func_178393_a(final TextureAtlasSprite p_178393_1_) {
        final int var2 = p_178393_1_.getIconWidth();
        final int var3 = p_178393_1_.getIconHeight();
        final ArrayList var4 = Lists.newArrayList();
        for (int var5 = 0; var5 < p_178393_1_.getFrameCount(); ++var5) {
            final int[] var6 = p_178393_1_.getFrameTextureData(var5)[0];
            for (int var7 = 0; var7 < var3; ++var7) {
                for (int var8 = 0; var8 < var2; ++var8) {
                    final boolean var9 = !this.func_178391_a(var6, var8, var7, var2, var3);
                    this.func_178396_a(SpanFacing.UP, var4, var6, var8, var7, var2, var3, var9);
                    this.func_178396_a(SpanFacing.DOWN, var4, var6, var8, var7, var2, var3, var9);
                    this.func_178396_a(SpanFacing.LEFT, var4, var6, var8, var7, var2, var3, var9);
                    this.func_178396_a(SpanFacing.RIGHT, var4, var6, var8, var7, var2, var3, var9);
                }
            }
        }
        return var4;
    }
    
    private void func_178396_a(final SpanFacing p_178396_1_, final List p_178396_2_, final int[] p_178396_3_, final int p_178396_4_, final int p_178396_5_, final int p_178396_6_, final int p_178396_7_, final boolean p_178396_8_) {
        final boolean var9 = this.func_178391_a(p_178396_3_, p_178396_4_ + p_178396_1_.func_178372_b(), p_178396_5_ + p_178396_1_.func_178371_c(), p_178396_6_, p_178396_7_) && p_178396_8_;
        if (var9) {
            this.func_178395_a(p_178396_2_, p_178396_1_, p_178396_4_, p_178396_5_);
        }
    }
    
    private void func_178395_a(final List p_178395_1_, final SpanFacing p_178395_2_, final int p_178395_3_, final int p_178395_4_) {
        Span var5 = null;
        for (final Span var7 : p_178395_1_) {
            if (var7.func_178383_a() == p_178395_2_) {
                final int var8 = p_178395_2_.func_178369_d() ? p_178395_4_ : p_178395_3_;
                if (var7.func_178381_d() == var8) {
                    var5 = var7;
                    break;
                }
                continue;
            }
        }
        final int var9 = p_178395_2_.func_178369_d() ? p_178395_4_ : p_178395_3_;
        final int var10 = p_178395_2_.func_178369_d() ? p_178395_3_ : p_178395_4_;
        if (var5 == null) {
            p_178395_1_.add(new Span(p_178395_2_, var10, var9));
        }
        else {
            var5.func_178382_a(var10);
        }
    }
    
    private boolean func_178391_a(final int[] p_178391_1_, final int p_178391_2_, final int p_178391_3_, final int p_178391_4_, final int p_178391_5_) {
        return p_178391_2_ < 0 || p_178391_3_ < 0 || p_178391_2_ >= p_178391_4_ || p_178391_3_ >= p_178391_5_ || (p_178391_1_[p_178391_3_ * p_178391_4_ + p_178391_2_] >> 24 & 0xFF) == 0x0;
    }
    
    static {
        LAYERS = Lists.newArrayList((Object[])new String[] { "layer0", "layer1", "layer2", "layer3", "layer4" });
    }
    
    static class Span
    {
        private final SpanFacing field_178389_a;
        private int field_178387_b;
        private int field_178388_c;
        private final int field_178386_d;
        private static final String __OBFID = "CL_00002486";
        
        public Span(final SpanFacing p_i46216_1_, final int p_i46216_2_, final int p_i46216_3_) {
            this.field_178389_a = p_i46216_1_;
            this.field_178387_b = p_i46216_2_;
            this.field_178388_c = p_i46216_2_;
            this.field_178386_d = p_i46216_3_;
        }
        
        public void func_178382_a(final int p_178382_1_) {
            if (p_178382_1_ < this.field_178387_b) {
                this.field_178387_b = p_178382_1_;
            }
            else if (p_178382_1_ > this.field_178388_c) {
                this.field_178388_c = p_178382_1_;
            }
        }
        
        public SpanFacing func_178383_a() {
            return this.field_178389_a;
        }
        
        public int func_178385_b() {
            return this.field_178387_b;
        }
        
        public int func_178384_c() {
            return this.field_178388_c;
        }
        
        public int func_178381_d() {
            return this.field_178386_d;
        }
    }
    
    enum SpanFacing
    {
        UP("UP", 0, EnumFacing.UP, 0, -1), 
        DOWN("DOWN", 1, EnumFacing.DOWN, 0, 1), 
        LEFT("LEFT", 2, EnumFacing.EAST, -1, 0), 
        RIGHT("RIGHT", 3, EnumFacing.WEST, 1, 0);
        
        private final EnumFacing field_178376_e;
        private final int field_178373_f;
        private final int field_178374_g;
        private static final SpanFacing[] $VALUES;
        private static final String __OBFID = "CL_00002485";
        
        private SpanFacing(final String p_i46215_1_, final int p_i46215_2_, final EnumFacing p_i46215_3_, final int p_i46215_4_, final int p_i46215_5_) {
            this.field_178376_e = p_i46215_3_;
            this.field_178373_f = p_i46215_4_;
            this.field_178374_g = p_i46215_5_;
        }
        
        public EnumFacing func_178367_a() {
            return this.field_178376_e;
        }
        
        public int func_178372_b() {
            return this.field_178373_f;
        }
        
        public int func_178371_c() {
            return this.field_178374_g;
        }
        
        private boolean func_178369_d() {
            return this == SpanFacing.DOWN || this == SpanFacing.UP;
        }
        
        static {
            $VALUES = new SpanFacing[] { SpanFacing.UP, SpanFacing.DOWN, SpanFacing.LEFT, SpanFacing.RIGHT };
        }
    }
    
    static final class SwitchSpanFacing
    {
        static final int[] field_178390_a;
        private static final String __OBFID = "CL_00002487";
        
        static {
            field_178390_a = new int[SpanFacing.values().length];
            try {
                SwitchSpanFacing.field_178390_a[SpanFacing.UP.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchSpanFacing.field_178390_a[SpanFacing.DOWN.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchSpanFacing.field_178390_a[SpanFacing.LEFT.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchSpanFacing.field_178390_a[SpanFacing.RIGHT.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
