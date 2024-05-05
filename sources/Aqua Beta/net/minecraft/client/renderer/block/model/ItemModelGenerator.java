package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

/*
 * Exception performing whole class analysis ignored.
 */
public class ItemModelGenerator {
    public static final List<String> LAYERS = Lists.newArrayList((Object[])new String[]{"layer0", "layer1", "layer2", "layer3", "layer4"});

    public ModelBlock makeItemModel(TextureMap textureMapIn, ModelBlock blockModel) {
        String s;
        HashMap map = Maps.newHashMap();
        ArrayList list = Lists.newArrayList();
        for (int i = 0; i < LAYERS.size() && blockModel.isTexturePresent(s = (String)LAYERS.get(i)); ++i) {
            String s1 = blockModel.resolveTextureName(s);
            map.put((Object)s, (Object)s1);
            TextureAtlasSprite textureatlassprite = textureMapIn.getAtlasSprite(new ResourceLocation(s1).toString());
            list.addAll(this.func_178394_a(i, s, textureatlassprite));
        }
        if (list.isEmpty()) {
            return null;
        }
        map.put((Object)"particle", (Object)(blockModel.isTexturePresent("particle") ? blockModel.resolveTextureName("particle") : (String)map.get((Object)"layer0")));
        return new ModelBlock((List)list, (Map)map, false, false, blockModel.getAllTransforms());
    }

    private List<BlockPart> func_178394_a(int p_178394_1_, String p_178394_2_, TextureAtlasSprite p_178394_3_) {
        HashMap map = Maps.newHashMap();
        map.put((Object)EnumFacing.SOUTH, (Object)new BlockPartFace((EnumFacing)null, p_178394_1_, p_178394_2_, new BlockFaceUV(new float[]{0.0f, 0.0f, 16.0f, 16.0f}, 0)));
        map.put((Object)EnumFacing.NORTH, (Object)new BlockPartFace((EnumFacing)null, p_178394_1_, p_178394_2_, new BlockFaceUV(new float[]{16.0f, 0.0f, 0.0f, 16.0f}, 0)));
        ArrayList list = Lists.newArrayList();
        list.add((Object)new BlockPart(new Vector3f(0.0f, 0.0f, 7.5f), new Vector3f(16.0f, 16.0f, 8.5f), (Map)map, (BlockPartRotation)null, true));
        list.addAll(this.func_178397_a(p_178394_3_, p_178394_2_, p_178394_1_));
        return list;
    }

    private List<BlockPart> func_178397_a(TextureAtlasSprite p_178397_1_, String p_178397_2_, int p_178397_3_) {
        float f = p_178397_1_.getIconWidth();
        float f1 = p_178397_1_.getIconHeight();
        ArrayList list = Lists.newArrayList();
        for (Span itemmodelgenerator$span : this.func_178393_a(p_178397_1_)) {
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
            float f12 = itemmodelgenerator$span.func_178385_b();
            float f13 = itemmodelgenerator$span.func_178384_c();
            float f14 = itemmodelgenerator$span.func_178381_d();
            SpanFacing itemmodelgenerator$spanfacing = itemmodelgenerator$span.func_178383_a();
            switch (1.$SwitchMap$net$minecraft$client$renderer$block$model$ItemModelGenerator$SpanFacing[itemmodelgenerator$spanfacing.ordinal()]) {
                case 1: {
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
                case 2: {
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
                case 3: {
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
                case 4: {
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
            HashMap map = Maps.newHashMap();
            map.put((Object)itemmodelgenerator$spanfacing.getFacing(), (Object)new BlockPartFace((EnumFacing)null, p_178397_3_, p_178397_2_, new BlockFaceUV(new float[]{f6 *= f10, f8 *= f11, f7 *= f10, f9 *= f11}, 0)));
            switch (1.$SwitchMap$net$minecraft$client$renderer$block$model$ItemModelGenerator$SpanFacing[itemmodelgenerator$spanfacing.ordinal()]) {
                case 1: {
                    list.add((Object)new BlockPart(new Vector3f(f2, f3, 7.5f), new Vector3f(f4, f3, 8.5f), (Map)map, (BlockPartRotation)null, true));
                    break;
                }
                case 2: {
                    list.add((Object)new BlockPart(new Vector3f(f2, f5, 7.5f), new Vector3f(f4, f5, 8.5f), (Map)map, (BlockPartRotation)null, true));
                    break;
                }
                case 3: {
                    list.add((Object)new BlockPart(new Vector3f(f2, f3, 7.5f), new Vector3f(f2, f5, 8.5f), (Map)map, (BlockPartRotation)null, true));
                    break;
                }
                case 4: {
                    list.add((Object)new BlockPart(new Vector3f(f4, f3, 7.5f), new Vector3f(f4, f5, 8.5f), (Map)map, (BlockPartRotation)null, true));
                }
            }
        }
        return list;
    }

    private List<Span> func_178393_a(TextureAtlasSprite p_178393_1_) {
        int i = p_178393_1_.getIconWidth();
        int j = p_178393_1_.getIconHeight();
        ArrayList list = Lists.newArrayList();
        for (int k = 0; k < p_178393_1_.getFrameCount(); ++k) {
            int[] aint = p_178393_1_.getFrameTextureData(k)[0];
            for (int l = 0; l < j; ++l) {
                for (int i1 = 0; i1 < i; ++i1) {
                    boolean flag = !this.func_178391_a(aint, i1, l, i, j);
                    this.func_178396_a(SpanFacing.UP, (List<Span>)list, aint, i1, l, i, j, flag);
                    this.func_178396_a(SpanFacing.DOWN, (List<Span>)list, aint, i1, l, i, j, flag);
                    this.func_178396_a(SpanFacing.LEFT, (List<Span>)list, aint, i1, l, i, j, flag);
                    this.func_178396_a(SpanFacing.RIGHT, (List<Span>)list, aint, i1, l, i, j, flag);
                }
            }
        }
        return list;
    }

    private void func_178396_a(SpanFacing p_178396_1_, List<Span> p_178396_2_, int[] p_178396_3_, int p_178396_4_, int p_178396_5_, int p_178396_6_, int p_178396_7_, boolean p_178396_8_) {
        boolean flag;
        boolean bl = flag = this.func_178391_a(p_178396_3_, p_178396_4_ + p_178396_1_.func_178372_b(), p_178396_5_ + p_178396_1_.func_178371_c(), p_178396_6_, p_178396_7_) && p_178396_8_;
        if (flag) {
            this.func_178395_a(p_178396_2_, p_178396_1_, p_178396_4_, p_178396_5_);
        }
    }

    private void func_178395_a(List<Span> p_178395_1_, SpanFacing p_178395_2_, int p_178395_3_, int p_178395_4_) {
        int k;
        Span itemmodelgenerator$span = null;
        for (Span itemmodelgenerator$span1 : p_178395_1_) {
            int i;
            if (itemmodelgenerator$span1.func_178383_a() != p_178395_2_) continue;
            int n = i = SpanFacing.access$000((SpanFacing)p_178395_2_) ? p_178395_4_ : p_178395_3_;
            if (itemmodelgenerator$span1.func_178381_d() != i) continue;
            itemmodelgenerator$span = itemmodelgenerator$span1;
            break;
        }
        int j = SpanFacing.access$000((SpanFacing)p_178395_2_) ? p_178395_4_ : p_178395_3_;
        int n = k = SpanFacing.access$000((SpanFacing)p_178395_2_) ? p_178395_3_ : p_178395_4_;
        if (itemmodelgenerator$span == null) {
            p_178395_1_.add((Object)new Span(p_178395_2_, k, j));
        } else {
            itemmodelgenerator$span.func_178382_a(k);
        }
    }

    private boolean func_178391_a(int[] p_178391_1_, int p_178391_2_, int p_178391_3_, int p_178391_4_, int p_178391_5_) {
        return p_178391_2_ >= 0 && p_178391_3_ >= 0 && p_178391_2_ < p_178391_4_ && p_178391_3_ < p_178391_5_ ? (p_178391_1_[p_178391_3_ * p_178391_4_ + p_178391_2_] >> 24 & 0xFF) == 0 : true;
    }
}
