/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

public class ItemModelGenerator {
    public static final List<String> LAYERS = Lists.newArrayList("layer0", "layer1", "layer2", "layer3", "layer4");

    @Nullable
    public ModelBlock makeItemModel(TextureMap textureMapIn, ModelBlock blockModel) {
        String s;
        HashMap<String, String> map = Maps.newHashMap();
        ArrayList<BlockPart> list = Lists.newArrayList();
        for (int i = 0; i < LAYERS.size() && blockModel.isTexturePresent(s = LAYERS.get(i)); ++i) {
            String s1 = blockModel.resolveTextureName(s);
            map.put(s, s1);
            TextureAtlasSprite textureatlassprite = textureMapIn.getAtlasSprite(new ResourceLocation(s1).toString());
            list.addAll(this.getBlockParts(i, s, textureatlassprite));
        }
        if (list.isEmpty()) {
            return null;
        }
        map.put("particle", blockModel.isTexturePresent("particle") ? blockModel.resolveTextureName("particle") : (String)map.get("layer0"));
        return new ModelBlock(null, list, map, false, false, blockModel.getAllTransforms(), blockModel.getOverrides());
    }

    private List<BlockPart> getBlockParts(int tintIndex, String p_178394_2_, TextureAtlasSprite p_178394_3_) {
        HashMap<EnumFacing, BlockPartFace> map = Maps.newHashMap();
        map.put(EnumFacing.SOUTH, new BlockPartFace(null, tintIndex, p_178394_2_, new BlockFaceUV(new float[]{0.0f, 0.0f, 16.0f, 16.0f}, 0)));
        map.put(EnumFacing.NORTH, new BlockPartFace(null, tintIndex, p_178394_2_, new BlockFaceUV(new float[]{16.0f, 0.0f, 0.0f, 16.0f}, 0)));
        ArrayList<BlockPart> list = Lists.newArrayList();
        list.add(new BlockPart(new Vector3f(0.0f, 0.0f, 7.5f), new Vector3f(16.0f, 16.0f, 8.5f), map, null, true));
        list.addAll(this.getBlockParts(p_178394_3_, p_178394_2_, tintIndex));
        return list;
    }

    private List<BlockPart> getBlockParts(TextureAtlasSprite p_178397_1_, String p_178397_2_, int p_178397_3_) {
        float f = p_178397_1_.getIconWidth();
        float f1 = p_178397_1_.getIconHeight();
        ArrayList<BlockPart> list = Lists.newArrayList();
        for (Span itemmodelgenerator$span : this.getSpans(p_178397_1_)) {
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            float f5 = 0.0f;
            float f6 = 0.0f;
            float f7 = 0.0f;
            float f8 = 0.0f;
            float f9 = 0.0f;
            float f10 = 0.0f;
            float f11 = 0.0f;
            float f12 = itemmodelgenerator$span.getMin();
            float f13 = itemmodelgenerator$span.getMax();
            float f14 = itemmodelgenerator$span.getAnchor();
            SpanFacing itemmodelgenerator$spanfacing = itemmodelgenerator$span.getFacing();
            switch (itemmodelgenerator$spanfacing) {
                case UP: {
                    f6 = f12;
                    f2 = f12;
                    f4 = f7 = f13 + 1.0f;
                    f8 = f14;
                    f3 = f14;
                    f9 = f14;
                    f5 = f14;
                    f10 = 16.0f / f;
                    f11 = 16.0f / (f1 - 1.0f);
                    break;
                }
                case DOWN: {
                    f9 = f14;
                    f8 = f14;
                    f6 = f12;
                    f2 = f12;
                    f4 = f7 = f13 + 1.0f;
                    f3 = f14 + 1.0f;
                    f5 = f14 + 1.0f;
                    f10 = 16.0f / f;
                    f11 = 16.0f / (f1 - 1.0f);
                    break;
                }
                case LEFT: {
                    f6 = f14;
                    f2 = f14;
                    f7 = f14;
                    f4 = f14;
                    f9 = f12;
                    f3 = f12;
                    f5 = f8 = f13 + 1.0f;
                    f10 = 16.0f / (f - 1.0f);
                    f11 = 16.0f / f1;
                    break;
                }
                case RIGHT: {
                    f7 = f14;
                    f6 = f14;
                    f2 = f14 + 1.0f;
                    f4 = f14 + 1.0f;
                    f9 = f12;
                    f3 = f12;
                    f5 = f8 = f13 + 1.0f;
                    f10 = 16.0f / (f - 1.0f);
                    f11 = 16.0f / f1;
                }
            }
            float f15 = 16.0f / f;
            float f16 = 16.0f / f1;
            f2 *= f15;
            f4 *= f15;
            f3 *= f16;
            f5 *= f16;
            f3 = 16.0f - f3;
            f5 = 16.0f - f5;
            HashMap<EnumFacing, BlockPartFace> map = Maps.newHashMap();
            map.put(itemmodelgenerator$spanfacing.getFacing(), new BlockPartFace(null, p_178397_3_, p_178397_2_, new BlockFaceUV(new float[]{f6 *= f10, f8 *= f11, f7 *= f10, f9 *= f11}, 0)));
            switch (itemmodelgenerator$spanfacing) {
                case UP: {
                    list.add(new BlockPart(new Vector3f(f2, f3, 7.5f), new Vector3f(f4, f3, 8.5f), map, null, true));
                    break;
                }
                case DOWN: {
                    list.add(new BlockPart(new Vector3f(f2, f5, 7.5f), new Vector3f(f4, f5, 8.5f), map, null, true));
                    break;
                }
                case LEFT: {
                    list.add(new BlockPart(new Vector3f(f2, f3, 7.5f), new Vector3f(f2, f5, 8.5f), map, null, true));
                    break;
                }
                case RIGHT: {
                    list.add(new BlockPart(new Vector3f(f4, f3, 7.5f), new Vector3f(f4, f5, 8.5f), map, null, true));
                }
            }
        }
        return list;
    }

    private List<Span> getSpans(TextureAtlasSprite p_178393_1_) {
        int i = p_178393_1_.getIconWidth();
        int j = p_178393_1_.getIconHeight();
        ArrayList<Span> list = Lists.newArrayList();
        for (int k = 0; k < p_178393_1_.getFrameCount(); ++k) {
            int[] aint = p_178393_1_.getFrameTextureData(k)[0];
            for (int l = 0; l < j; ++l) {
                for (int i1 = 0; i1 < i; ++i1) {
                    boolean flag = !this.isTransparent(aint, i1, l, i, j);
                    this.checkTransition(SpanFacing.UP, list, aint, i1, l, i, j, flag);
                    this.checkTransition(SpanFacing.DOWN, list, aint, i1, l, i, j, flag);
                    this.checkTransition(SpanFacing.LEFT, list, aint, i1, l, i, j, flag);
                    this.checkTransition(SpanFacing.RIGHT, list, aint, i1, l, i, j, flag);
                }
            }
        }
        return list;
    }

    private void checkTransition(SpanFacing p_178396_1_, List<Span> p_178396_2_, int[] p_178396_3_, int p_178396_4_, int p_178396_5_, int p_178396_6_, int p_178396_7_, boolean p_178396_8_) {
        boolean flag;
        boolean bl = flag = this.isTransparent(p_178396_3_, p_178396_4_ + p_178396_1_.getXOffset(), p_178396_5_ + p_178396_1_.getYOffset(), p_178396_6_, p_178396_7_) && p_178396_8_;
        if (flag) {
            this.createOrExpandSpan(p_178396_2_, p_178396_1_, p_178396_4_, p_178396_5_);
        }
    }

    private void createOrExpandSpan(List<Span> p_178395_1_, SpanFacing p_178395_2_, int p_178395_3_, int p_178395_4_) {
        int k;
        Span itemmodelgenerator$span = null;
        for (Span itemmodelgenerator$span1 : p_178395_1_) {
            int i;
            if (itemmodelgenerator$span1.getFacing() != p_178395_2_) continue;
            int n = i = p_178395_2_.isHorizontal() ? p_178395_4_ : p_178395_3_;
            if (itemmodelgenerator$span1.getAnchor() != i) continue;
            itemmodelgenerator$span = itemmodelgenerator$span1;
            break;
        }
        int j = p_178395_2_.isHorizontal() ? p_178395_4_ : p_178395_3_;
        int n = k = p_178395_2_.isHorizontal() ? p_178395_3_ : p_178395_4_;
        if (itemmodelgenerator$span == null) {
            p_178395_1_.add(new Span(p_178395_2_, k, j));
        } else {
            itemmodelgenerator$span.expand(k);
        }
    }

    private boolean isTransparent(int[] p_178391_1_, int p_178391_2_, int p_178391_3_, int p_178391_4_, int p_178391_5_) {
        if (p_178391_2_ >= 0 && p_178391_3_ >= 0 && p_178391_2_ < p_178391_4_ && p_178391_3_ < p_178391_5_) {
            return (p_178391_1_[p_178391_3_ * p_178391_4_ + p_178391_2_] >> 24 & 0xFF) == 0;
        }
        return true;
    }

    static enum SpanFacing {
        UP(EnumFacing.UP, 0, -1),
        DOWN(EnumFacing.DOWN, 0, 1),
        LEFT(EnumFacing.EAST, -1, 0),
        RIGHT(EnumFacing.WEST, 1, 0);

        private final EnumFacing facing;
        private final int xOffset;
        private final int yOffset;

        private SpanFacing(EnumFacing facing, int p_i46215_4_, int p_i46215_5_) {
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
            return this == DOWN || this == UP;
        }
    }

    static class Span {
        private final SpanFacing spanFacing;
        private int min;
        private int max;
        private final int anchor;

        public Span(SpanFacing spanFacingIn, int p_i46216_2_, int p_i46216_3_) {
            this.spanFacing = spanFacingIn;
            this.min = p_i46216_2_;
            this.max = p_i46216_2_;
            this.anchor = p_i46216_3_;
        }

        public void expand(int p_178382_1_) {
            if (p_178382_1_ < this.min) {
                this.min = p_178382_1_;
            } else if (p_178382_1_ > this.max) {
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
}

