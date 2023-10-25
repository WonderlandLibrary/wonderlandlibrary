/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.lwjgl.util.vector.Vector3f
 */
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
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector3f;

public class ItemModelGenerator {
    public static final List<String> LAYERS = Lists.newArrayList((Object[])new String[]{"layer0", "layer1", "layer2", "layer3", "layer4"});

    private boolean func_178391_a(int[] nArray, int n, int n2, int n3, int n4) {
        return n >= 0 && n2 >= 0 && n < n3 && n2 < n4 ? (nArray[n2 * n3 + n] >> 24 & 0xFF) == 0 : true;
    }

    public ModelBlock makeItemModel(TextureMap textureMap, ModelBlock modelBlock) {
        HashMap hashMap = Maps.newHashMap();
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < LAYERS.size()) {
            String string = LAYERS.get(n);
            if (!modelBlock.isTexturePresent(string)) break;
            String string2 = modelBlock.resolveTextureName(string);
            hashMap.put(string, string2);
            TextureAtlasSprite textureAtlasSprite = textureMap.getAtlasSprite(new ResourceLocation(string2).toString());
            arrayList.addAll(this.func_178394_a(n, string, textureAtlasSprite));
            ++n;
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        hashMap.put("particle", modelBlock.isTexturePresent("particle") ? modelBlock.resolveTextureName("particle") : (String)hashMap.get("layer0"));
        return new ModelBlock(arrayList, (Map<String, String>)hashMap, false, false, modelBlock.func_181682_g());
    }

    private void func_178395_a(List<Span> list, SpanFacing spanFacing, int n, int n2) {
        int n3;
        Span span = null;
        for (Span span2 : list) {
            int n4;
            if (span2.func_178383_a() != spanFacing) continue;
            int n5 = n4 = spanFacing.func_178369_d() ? n2 : n;
            if (span2.func_178381_d() != n4) continue;
            span = span2;
            break;
        }
        int n6 = spanFacing.func_178369_d() ? n2 : n;
        int n7 = n3 = spanFacing.func_178369_d() ? n : n2;
        if (span == null) {
            list.add(new Span(spanFacing, n3, n6));
        } else {
            span.func_178382_a(n3);
        }
    }

    private List<BlockPart> func_178397_a(TextureAtlasSprite textureAtlasSprite, String string, int n) {
        float f = textureAtlasSprite.getIconWidth();
        float f2 = textureAtlasSprite.getIconHeight();
        ArrayList arrayList = Lists.newArrayList();
        for (Span span : this.func_178393_a(textureAtlasSprite)) {
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
            float f13 = span.func_178385_b();
            float f14 = span.func_178384_c();
            float f15 = span.func_178381_d();
            SpanFacing spanFacing = span.func_178383_a();
            switch (spanFacing) {
                case UP: {
                    f7 = f13;
                    f3 = f13;
                    f5 = f8 = f14 + 1.0f;
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
                    f5 = f8 = f14 + 1.0f;
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
                    f6 = f9 = f14 + 1.0f;
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
                    f6 = f9 = f14 + 1.0f;
                    f11 = 16.0f / (f - 1.0f);
                    f12 = 16.0f / f2;
                }
            }
            float f16 = 16.0f / f;
            float f17 = 16.0f / f2;
            f3 *= f16;
            f5 *= f16;
            f4 *= f17;
            f6 *= f17;
            f4 = 16.0f - f4;
            f6 = 16.0f - f6;
            HashMap hashMap = Maps.newHashMap();
            hashMap.put(spanFacing.getFacing(), new BlockPartFace(null, n, string, new BlockFaceUV(new float[]{f7 *= f11, f9 *= f12, f8 *= f11, f10 *= f12}, 0)));
            switch (spanFacing) {
                case UP: {
                    arrayList.add(new BlockPart(new Vector3f(f3, f4, 7.5f), new Vector3f(f5, f4, 8.5f), hashMap, null, true));
                    break;
                }
                case DOWN: {
                    arrayList.add(new BlockPart(new Vector3f(f3, f6, 7.5f), new Vector3f(f5, f6, 8.5f), hashMap, null, true));
                    break;
                }
                case LEFT: {
                    arrayList.add(new BlockPart(new Vector3f(f3, f4, 7.5f), new Vector3f(f3, f6, 8.5f), hashMap, null, true));
                    break;
                }
                case RIGHT: {
                    arrayList.add(new BlockPart(new Vector3f(f5, f4, 7.5f), new Vector3f(f5, f6, 8.5f), hashMap, null, true));
                }
            }
        }
        return arrayList;
    }

    private List<BlockPart> func_178394_a(int n, String string, TextureAtlasSprite textureAtlasSprite) {
        HashMap hashMap = Maps.newHashMap();
        hashMap.put(EnumFacing.SOUTH, new BlockPartFace(null, n, string, new BlockFaceUV(new float[]{0.0f, 0.0f, 16.0f, 16.0f}, 0)));
        hashMap.put(EnumFacing.NORTH, new BlockPartFace(null, n, string, new BlockFaceUV(new float[]{16.0f, 0.0f, 0.0f, 16.0f}, 0)));
        ArrayList arrayList = Lists.newArrayList();
        arrayList.add(new BlockPart(new Vector3f(0.0f, 0.0f, 7.5f), new Vector3f(16.0f, 16.0f, 8.5f), hashMap, null, true));
        arrayList.addAll(this.func_178397_a(textureAtlasSprite, string, n));
        return arrayList;
    }

    private void func_178396_a(SpanFacing spanFacing, List<Span> list, int[] nArray, int n, int n2, int n3, int n4, boolean bl) {
        boolean bl2;
        boolean bl3 = bl2 = this.func_178391_a(nArray, n + spanFacing.func_178372_b(), n2 + spanFacing.func_178371_c(), n3, n4) && bl;
        if (bl2) {
            this.func_178395_a(list, spanFacing, n, n2);
        }
    }

    private List<Span> func_178393_a(TextureAtlasSprite textureAtlasSprite) {
        int n = textureAtlasSprite.getIconWidth();
        int n2 = textureAtlasSprite.getIconHeight();
        ArrayList arrayList = Lists.newArrayList();
        int n3 = 0;
        while (n3 < textureAtlasSprite.getFrameCount()) {
            int[] nArray = textureAtlasSprite.getFrameTextureData(n3)[0];
            int n4 = 0;
            while (n4 < n2) {
                int n5 = 0;
                while (n5 < n) {
                    boolean bl = !this.func_178391_a(nArray, n5, n4, n, n2);
                    this.func_178396_a(SpanFacing.UP, arrayList, nArray, n5, n4, n, n2, bl);
                    this.func_178396_a(SpanFacing.DOWN, arrayList, nArray, n5, n4, n, n2, bl);
                    this.func_178396_a(SpanFacing.LEFT, arrayList, nArray, n5, n4, n, n2, bl);
                    this.func_178396_a(SpanFacing.RIGHT, arrayList, nArray, n5, n4, n, n2, bl);
                    ++n5;
                }
                ++n4;
            }
            ++n3;
        }
        return arrayList;
    }

    static class Span {
        private int field_178388_c;
        private final int field_178386_d;
        private int field_178387_b;
        private final SpanFacing spanFacing;

        public SpanFacing func_178383_a() {
            return this.spanFacing;
        }

        public Span(SpanFacing spanFacing, int n, int n2) {
            this.spanFacing = spanFacing;
            this.field_178387_b = n;
            this.field_178388_c = n;
            this.field_178386_d = n2;
        }

        public int func_178385_b() {
            return this.field_178387_b;
        }

        public void func_178382_a(int n) {
            if (n < this.field_178387_b) {
                this.field_178387_b = n;
            } else if (n > this.field_178388_c) {
                this.field_178388_c = n;
            }
        }

        public int func_178384_c() {
            return this.field_178388_c;
        }

        public int func_178381_d() {
            return this.field_178386_d;
        }
    }

    static enum SpanFacing {
        UP(EnumFacing.UP, 0, -1),
        DOWN(EnumFacing.DOWN, 0, 1),
        LEFT(EnumFacing.EAST, -1, 0),
        RIGHT(EnumFacing.WEST, 1, 0);

        private final int field_178374_g;
        private final int field_178373_f;
        private final EnumFacing facing;

        public int func_178371_c() {
            return this.field_178374_g;
        }

        public int func_178372_b() {
            return this.field_178373_f;
        }

        private boolean func_178369_d() {
            return this == DOWN || this == UP;
        }

        private SpanFacing(EnumFacing enumFacing, int n2, int n3) {
            this.facing = enumFacing;
            this.field_178373_f = n2;
            this.field_178374_g = n3;
        }

        public EnumFacing getFacing() {
            return this.facing;
        }
    }
}

