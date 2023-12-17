// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import java.util.Iterator;
import org.lwjgl.util.vector.Vector3f;
import net.minecraft.util.EnumFacing;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.Map;
import java.util.Collection;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.List;

public class ItemModelGenerator
{
    public static final List<String> LAYERS;
    
    @Nullable
    public ModelBlock makeItemModel(final TextureMap textureMapIn, final ModelBlock blockModel) {
        final Map<String, String> map = (Map<String, String>)Maps.newHashMap();
        final List<BlockPart> list = (List<BlockPart>)Lists.newArrayList();
        for (int i = 0; i < ItemModelGenerator.LAYERS.size(); ++i) {
            final String s = ItemModelGenerator.LAYERS.get(i);
            if (!blockModel.isTexturePresent(s)) {
                break;
            }
            final String s2 = blockModel.resolveTextureName(s);
            map.put(s, s2);
            final TextureAtlasSprite textureatlassprite = textureMapIn.getAtlasSprite(new ResourceLocation(s2).toString());
            list.addAll(this.getBlockParts(i, s, textureatlassprite));
        }
        if (list.isEmpty()) {
            return null;
        }
        map.put("particle", blockModel.isTexturePresent("particle") ? blockModel.resolveTextureName("particle") : map.get("layer0"));
        return new ModelBlock(null, list, map, false, false, blockModel.getAllTransforms(), blockModel.getOverrides());
    }
    
    private List<BlockPart> getBlockParts(final int tintIndex, final String p_178394_2_, final TextureAtlasSprite p_178394_3_) {
        final Map<EnumFacing, BlockPartFace> map = (Map<EnumFacing, BlockPartFace>)Maps.newHashMap();
        map.put(EnumFacing.SOUTH, new BlockPartFace(null, tintIndex, p_178394_2_, new BlockFaceUV(new float[] { 0.0f, 0.0f, 16.0f, 16.0f }, 0)));
        map.put(EnumFacing.NORTH, new BlockPartFace(null, tintIndex, p_178394_2_, new BlockFaceUV(new float[] { 16.0f, 0.0f, 0.0f, 16.0f }, 0)));
        final List<BlockPart> list = (List<BlockPart>)Lists.newArrayList();
        list.add(new BlockPart(new Vector3f(0.0f, 0.0f, 7.5f), new Vector3f(16.0f, 16.0f, 8.5f), map, null, true));
        list.addAll(this.getBlockParts(p_178394_3_, p_178394_2_, tintIndex));
        return list;
    }
    
    private List<BlockPart> getBlockParts(final TextureAtlasSprite p_178397_1_, final String p_178397_2_, final int p_178397_3_) {
        final float f = (float)p_178397_1_.getIconWidth();
        final float f2 = (float)p_178397_1_.getIconHeight();
        final List<BlockPart> list = (List<BlockPart>)Lists.newArrayList();
        for (final Span itemmodelgenerator$span : this.getSpans(p_178397_1_)) {
            float f3 = 0.0f;
            float f4 = 0.0f;
            float f5 = 0.0f;
            float f6 = 0.0f;
            float f7 = 0.0f;
            float f8 = 0.0f;
            float f9 = 0.0f;
            float f10 = 0.0f;
            float f11 = 0.0f;
            float f12 = 0.0f;
            final float f13 = (float)itemmodelgenerator$span.getMin();
            final float f14 = (float)itemmodelgenerator$span.getMax();
            final float f15 = (float)itemmodelgenerator$span.getAnchor();
            final SpanFacing itemmodelgenerator$spanfacing = itemmodelgenerator$span.getFacing();
            switch (itemmodelgenerator$spanfacing) {
                case UP: {
                    f7 = f13;
                    f3 = f13;
                    f8 = (f5 = f14 + 1.0f);
                    f9 = f15;
                    f4 = f15;
                    f10 = f15;
                    f6 = f15;
                    f11 = 16.0f / f;
                    f12 = 16.0f / (f2 - 1.0f);
                    break;
                }
                case DOWN: {
                    f10 = f15;
                    f9 = f15;
                    f7 = f13;
                    f3 = f13;
                    f8 = (f5 = f14 + 1.0f);
                    f4 = f15 + 1.0f;
                    f6 = f15 + 1.0f;
                    f11 = 16.0f / f;
                    f12 = 16.0f / (f2 - 1.0f);
                    break;
                }
                case LEFT: {
                    f7 = f15;
                    f3 = f15;
                    f8 = f15;
                    f5 = f15;
                    f10 = f13;
                    f4 = f13;
                    f9 = (f6 = f14 + 1.0f);
                    f11 = 16.0f / (f - 1.0f);
                    f12 = 16.0f / f2;
                    break;
                }
                case RIGHT: {
                    f8 = f15;
                    f7 = f15;
                    f3 = f15 + 1.0f;
                    f5 = f15 + 1.0f;
                    f10 = f13;
                    f4 = f13;
                    f9 = (f6 = f14 + 1.0f);
                    f11 = 16.0f / (f - 1.0f);
                    f12 = 16.0f / f2;
                    break;
                }
            }
            final float f16 = 16.0f / f;
            final float f17 = 16.0f / f2;
            f3 *= f16;
            f5 *= f16;
            f4 *= f17;
            f6 *= f17;
            f4 = 16.0f - f4;
            f6 = 16.0f - f6;
            f7 *= f11;
            f8 *= f11;
            f9 *= f12;
            f10 *= f12;
            final Map<EnumFacing, BlockPartFace> map = (Map<EnumFacing, BlockPartFace>)Maps.newHashMap();
            map.put(itemmodelgenerator$spanfacing.getFacing(), new BlockPartFace(null, p_178397_3_, p_178397_2_, new BlockFaceUV(new float[] { f7, f9, f8, f10 }, 0)));
            switch (itemmodelgenerator$spanfacing) {
                case UP: {
                    list.add(new BlockPart(new Vector3f(f3, f4, 7.5f), new Vector3f(f5, f4, 8.5f), map, null, true));
                    continue;
                }
                case DOWN: {
                    list.add(new BlockPart(new Vector3f(f3, f6, 7.5f), new Vector3f(f5, f6, 8.5f), map, null, true));
                    continue;
                }
                case LEFT: {
                    list.add(new BlockPart(new Vector3f(f3, f4, 7.5f), new Vector3f(f3, f6, 8.5f), map, null, true));
                    continue;
                }
                case RIGHT: {
                    list.add(new BlockPart(new Vector3f(f5, f4, 7.5f), new Vector3f(f5, f6, 8.5f), map, null, true));
                    continue;
                }
            }
        }
        return list;
    }
    
    private List<Span> getSpans(final TextureAtlasSprite p_178393_1_) {
        final int i = p_178393_1_.getIconWidth();
        final int j = p_178393_1_.getIconHeight();
        final List<Span> list = (List<Span>)Lists.newArrayList();
        for (int k = 0; k < p_178393_1_.getFrameCount(); ++k) {
            final int[] aint = p_178393_1_.getFrameTextureData(k)[0];
            for (int l = 0; l < j; ++l) {
                for (int i2 = 0; i2 < i; ++i2) {
                    final boolean flag = !this.isTransparent(aint, i2, l, i, j);
                    this.checkTransition(SpanFacing.UP, list, aint, i2, l, i, j, flag);
                    this.checkTransition(SpanFacing.DOWN, list, aint, i2, l, i, j, flag);
                    this.checkTransition(SpanFacing.LEFT, list, aint, i2, l, i, j, flag);
                    this.checkTransition(SpanFacing.RIGHT, list, aint, i2, l, i, j, flag);
                }
            }
        }
        return list;
    }
    
    private void checkTransition(final SpanFacing p_178396_1_, final List<Span> p_178396_2_, final int[] p_178396_3_, final int p_178396_4_, final int p_178396_5_, final int p_178396_6_, final int p_178396_7_, final boolean p_178396_8_) {
        final boolean flag = this.isTransparent(p_178396_3_, p_178396_4_ + p_178396_1_.getXOffset(), p_178396_5_ + p_178396_1_.getYOffset(), p_178396_6_, p_178396_7_) && p_178396_8_;
        if (flag) {
            this.createOrExpandSpan(p_178396_2_, p_178396_1_, p_178396_4_, p_178396_5_);
        }
    }
    
    private void createOrExpandSpan(final List<Span> p_178395_1_, final SpanFacing p_178395_2_, final int p_178395_3_, final int p_178395_4_) {
        Span itemmodelgenerator$span = null;
        for (final Span itemmodelgenerator$span2 : p_178395_1_) {
            if (itemmodelgenerator$span2.getFacing() == p_178395_2_) {
                final int i = p_178395_2_.isHorizontal() ? p_178395_4_ : p_178395_3_;
                if (itemmodelgenerator$span2.getAnchor() == i) {
                    itemmodelgenerator$span = itemmodelgenerator$span2;
                    break;
                }
                continue;
            }
        }
        final int j = p_178395_2_.isHorizontal() ? p_178395_4_ : p_178395_3_;
        final int k = p_178395_2_.isHorizontal() ? p_178395_3_ : p_178395_4_;
        if (itemmodelgenerator$span == null) {
            p_178395_1_.add(new Span(p_178395_2_, k, j));
        }
        else {
            itemmodelgenerator$span.expand(k);
        }
    }
    
    private boolean isTransparent(final int[] p_178391_1_, final int p_178391_2_, final int p_178391_3_, final int p_178391_4_, final int p_178391_5_) {
        return p_178391_2_ < 0 || p_178391_3_ < 0 || p_178391_2_ >= p_178391_4_ || p_178391_3_ >= p_178391_5_ || (p_178391_1_[p_178391_3_ * p_178391_4_ + p_178391_2_] >> 24 & 0xFF) == 0x0;
    }
    
    static {
        LAYERS = Lists.newArrayList((Object[])new String[] { "layer0", "layer1", "layer2", "layer3", "layer4" });
    }
    
    static class Span
    {
        private final SpanFacing spanFacing;
        private int min;
        private int max;
        private final int anchor;
        
        public Span(final SpanFacing spanFacingIn, final int p_i46216_2_, final int p_i46216_3_) {
            this.spanFacing = spanFacingIn;
            this.min = p_i46216_2_;
            this.max = p_i46216_2_;
            this.anchor = p_i46216_3_;
        }
        
        public void expand(final int p_178382_1_) {
            if (p_178382_1_ < this.min) {
                this.min = p_178382_1_;
            }
            else if (p_178382_1_ > this.max) {
                this.max = p_178382_1_;
            }
        }
        
        public SpanFacing getFacing() {
            return this.spanFacing;
        }
        
        public int getMin() {
            return this.min;
        }
        
        public int getMax() {
            return this.max;
        }
        
        public int getAnchor() {
            return this.anchor;
        }
    }
    
    enum SpanFacing
    {
        UP(EnumFacing.UP, 0, -1), 
        DOWN(EnumFacing.DOWN, 0, 1), 
        LEFT(EnumFacing.EAST, -1, 0), 
        RIGHT(EnumFacing.WEST, 1, 0);
        
        private final EnumFacing facing;
        private final int xOffset;
        private final int yOffset;
        
        private SpanFacing(final EnumFacing facing, final int p_i46215_4_, final int p_i46215_5_) {
            this.facing = facing;
            this.xOffset = p_i46215_4_;
            this.yOffset = p_i46215_5_;
        }
        
        public EnumFacing getFacing() {
            return this.facing;
        }
        
        public int getXOffset() {
            return this.xOffset;
        }
        
        public int getYOffset() {
            return this.yOffset;
        }
        
        private boolean isHorizontal() {
            return this == SpanFacing.DOWN || this == SpanFacing.UP;
        }
    }
}
