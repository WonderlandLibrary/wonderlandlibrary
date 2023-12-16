// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import optifine.Config;
import net.minecraft.client.Minecraft;
import optifine.Reflector;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexProducer;

public class BakedQuad implements IVertexProducer
{
    protected int[] field_178215_a;
    protected final int field_178213_b;
    protected final EnumFacing face;
    private static final String __OBFID = "CL_00002512";
    private TextureAtlasSprite sprite;
    private int[] vertexDataSingle;
    
    public BakedQuad(final int[] p_i46232_1_, final int p_i46232_2_, final EnumFacing p_i46232_3_, final TextureAtlasSprite sprite) {
        this.sprite = null;
        this.vertexDataSingle = null;
        this.field_178215_a = p_i46232_1_;
        this.field_178213_b = p_i46232_2_;
        this.face = p_i46232_3_;
        this.sprite = sprite;
        this.fixVertexData();
    }
    
    public TextureAtlasSprite getSprite() {
        if (this.sprite == null) {
            this.sprite = getSpriteByUv(this.func_178209_a());
        }
        return this.sprite;
    }
    
    @Override
    public String toString() {
        return "vertex: " + this.field_178215_a.length / 7 + ", tint: " + this.field_178213_b + ", facing: " + this.face + ", sprite: " + this.sprite;
    }
    
    public BakedQuad(final int[] p_i46232_1_, final int p_i46232_2_, final EnumFacing p_i46232_3_) {
        this.sprite = null;
        this.vertexDataSingle = null;
        this.field_178215_a = p_i46232_1_;
        this.field_178213_b = p_i46232_2_;
        this.face = p_i46232_3_;
        this.fixVertexData();
    }
    
    public int[] func_178209_a() {
        this.fixVertexData();
        return this.field_178215_a;
    }
    
    public boolean func_178212_b() {
        return this.field_178213_b != -1;
    }
    
    public int func_178211_c() {
        return this.field_178213_b;
    }
    
    public EnumFacing getFace() {
        return this.face;
    }
    
    public int[] getVertexDataSingle() {
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = makeVertexDataSingle(this.func_178209_a(), this.getSprite());
        }
        return this.vertexDataSingle;
    }
    
    private static int[] makeVertexDataSingle(final int[] vd, final TextureAtlasSprite sprite) {
        final int[] vdSingle = vd.clone();
        final int ku = sprite.sheetWidth / sprite.getIconWidth();
        final int kv = sprite.sheetHeight / sprite.getIconHeight();
        final int step = vdSingle.length / 4;
        for (int i = 0; i < 4; ++i) {
            final int pos = i * step;
            final float tu = Float.intBitsToFloat(vdSingle[pos + 4]);
            final float tv = Float.intBitsToFloat(vdSingle[pos + 4 + 1]);
            final float u = sprite.toSingleU(tu);
            final float v = sprite.toSingleV(tv);
            vdSingle[pos + 4] = Float.floatToRawIntBits(u);
            vdSingle[pos + 4 + 1] = Float.floatToRawIntBits(v);
        }
        return vdSingle;
    }
    
    @Override
    public void pipe(final IVertexConsumer consumer) {
        Reflector.callVoid(Reflector.LightUtil_putBakedQuad, consumer, this);
    }
    
    private static TextureAtlasSprite getSpriteByUv(final int[] vertexData) {
        float uMin = 1.0f;
        float vMin = 1.0f;
        float uMax = 0.0f;
        float vMax = 0.0f;
        final int step = vertexData.length / 4;
        for (int uMid = 0; uMid < 4; ++uMid) {
            final int vMid = uMid * step;
            final float spriteUv = Float.intBitsToFloat(vertexData[vMid + 4]);
            final float tv = Float.intBitsToFloat(vertexData[vMid + 4 + 1]);
            uMin = Math.min(uMin, spriteUv);
            vMin = Math.min(vMin, tv);
            uMax = Math.max(uMax, spriteUv);
            vMax = Math.max(vMax, tv);
        }
        final float var10 = (uMin + uMax) / 2.0f;
        final float var11 = (vMin + vMax) / 2.0f;
        final TextureAtlasSprite var12 = Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV(var10, var11);
        return var12;
    }
    
    private void fixVertexData() {
        if (Config.isShaders()) {
            if (this.field_178215_a.length == 28) {
                this.field_178215_a = expandVertexData(this.field_178215_a);
            }
        }
        else if (this.field_178215_a.length == 56) {
            this.field_178215_a = compactVertexData(this.field_178215_a);
        }
    }
    
    private static int[] expandVertexData(final int[] vd) {
        final int step = vd.length / 4;
        final int stepNew = step * 2;
        final int[] vdNew = new int[stepNew * 4];
        for (int i = 0; i < 4; ++i) {
            System.arraycopy(vd, i * step, vdNew, i * stepNew, step);
        }
        return vdNew;
    }
    
    private static int[] compactVertexData(final int[] vd) {
        final int step = vd.length / 4;
        final int stepNew = step / 2;
        final int[] vdNew = new int[stepNew * 4];
        for (int i = 0; i < 4; ++i) {
            System.arraycopy(vd, i * step, vdNew, i * stepNew, stepNew);
        }
        return vdNew;
    }
}
