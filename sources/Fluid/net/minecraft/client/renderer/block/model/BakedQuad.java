// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.block.model;

import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import net.optifine.reflect.Reflector;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.optifine.model.QuadBounds;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexProducer;

public class BakedQuad implements IVertexProducer
{
    protected int[] vertexData;
    protected final int tintIndex;
    protected EnumFacing face;
    protected TextureAtlasSprite sprite;
    private int[] vertexDataSingle;
    private QuadBounds quadBounds;
    private boolean quadEmissiveChecked;
    private BakedQuad quadEmissive;
    
    public BakedQuad(final int[] p_i3_1_, final int p_i3_2_, final EnumFacing p_i3_3_, final TextureAtlasSprite p_i3_4_) {
        this.vertexDataSingle = null;
        this.vertexData = p_i3_1_;
        this.tintIndex = p_i3_2_;
        this.face = p_i3_3_;
        this.sprite = p_i3_4_;
        this.fixVertexData();
    }
    
    public BakedQuad(final int[] vertexDataIn, final int tintIndexIn, final EnumFacing faceIn) {
        this.vertexDataSingle = null;
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
        this.fixVertexData();
    }
    
    public TextureAtlasSprite getSprite() {
        if (this.sprite == null) {
            this.sprite = getSpriteByUv(this.getVertexData());
        }
        return this.sprite;
    }
    
    public int[] getVertexData() {
        this.fixVertexData();
        return this.vertexData;
    }
    
    public boolean hasTintIndex() {
        return this.tintIndex != -1;
    }
    
    public int getTintIndex() {
        return this.tintIndex;
    }
    
    public EnumFacing getFace() {
        if (this.face == null) {
            this.face = FaceBakery.getFacingFromVertexData(this.getVertexData());
        }
        return this.face;
    }
    
    public int[] getVertexDataSingle() {
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = makeVertexDataSingle(this.getVertexData(), this.getSprite());
        }
        return this.vertexDataSingle;
    }
    
    private static int[] makeVertexDataSingle(final int[] p_makeVertexDataSingle_0_, final TextureAtlasSprite p_makeVertexDataSingle_1_) {
        final int[] aint = p_makeVertexDataSingle_0_.clone();
        final int i = aint.length / 4;
        for (int j = 0; j < 4; ++j) {
            final int k = j * i;
            final float f = Float.intBitsToFloat(aint[k + 4]);
            final float f2 = Float.intBitsToFloat(aint[k + 4 + 1]);
            final float f3 = p_makeVertexDataSingle_1_.toSingleU(f);
            final float f4 = p_makeVertexDataSingle_1_.toSingleV(f2);
            aint[k + 4] = Float.floatToRawIntBits(f3);
            aint[k + 4 + 1] = Float.floatToRawIntBits(f4);
        }
        return aint;
    }
    
    @Override
    public void pipe(final IVertexConsumer p_pipe_1_) {
        Reflector.callVoid(Reflector.LightUtil_putBakedQuad, p_pipe_1_, this);
    }
    
    private static TextureAtlasSprite getSpriteByUv(final int[] p_getSpriteByUv_0_) {
        float f = 1.0f;
        float f2 = 1.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        final int i = p_getSpriteByUv_0_.length / 4;
        for (int j = 0; j < 4; ++j) {
            final int k = j * i;
            final float f5 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4]);
            final float f6 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4 + 1]);
            f = Math.min(f, f5);
            f2 = Math.min(f2, f6);
            f3 = Math.max(f3, f5);
            f4 = Math.max(f4, f6);
        }
        final float f7 = (f + f3) / 2.0f;
        final float f8 = (f2 + f4) / 2.0f;
        final TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV(f7, f8);
        return textureatlassprite;
    }
    
    protected void fixVertexData() {
        if (Config.isShaders()) {
            if (this.vertexData.length == 28) {
                this.vertexData = expandVertexData(this.vertexData);
            }
        }
        else if (this.vertexData.length == 56) {
            this.vertexData = compactVertexData(this.vertexData);
        }
    }
    
    private static int[] expandVertexData(final int[] p_expandVertexData_0_) {
        final int i = p_expandVertexData_0_.length / 4;
        final int j = i * 2;
        final int[] aint = new int[j * 4];
        for (int k = 0; k < 4; ++k) {
            System.arraycopy(p_expandVertexData_0_, k * i, aint, k * j, i);
        }
        return aint;
    }
    
    private static int[] compactVertexData(final int[] p_compactVertexData_0_) {
        final int i = p_compactVertexData_0_.length / 4;
        final int j = i / 2;
        final int[] aint = new int[j * 4];
        for (int k = 0; k < 4; ++k) {
            System.arraycopy(p_compactVertexData_0_, k * i, aint, k * j, j);
        }
        return aint;
    }
    
    public QuadBounds getQuadBounds() {
        if (this.quadBounds == null) {
            this.quadBounds = new QuadBounds(this.getVertexData());
        }
        return this.quadBounds;
    }
    
    public float getMidX() {
        final QuadBounds quadbounds = this.getQuadBounds();
        return (quadbounds.getMaxX() + quadbounds.getMinX()) / 2.0f;
    }
    
    public double getMidY() {
        final QuadBounds quadbounds = this.getQuadBounds();
        return (quadbounds.getMaxY() + quadbounds.getMinY()) / 2.0f;
    }
    
    public double getMidZ() {
        final QuadBounds quadbounds = this.getQuadBounds();
        return (quadbounds.getMaxZ() + quadbounds.getMinZ()) / 2.0f;
    }
    
    public boolean isFaceQuad() {
        final QuadBounds quadbounds = this.getQuadBounds();
        return quadbounds.isFaceQuad(this.face);
    }
    
    public boolean isFullQuad() {
        final QuadBounds quadbounds = this.getQuadBounds();
        return quadbounds.isFullQuad(this.face);
    }
    
    public boolean isFullFaceQuad() {
        return this.isFullQuad() && this.isFaceQuad();
    }
    
    public BakedQuad getQuadEmissive() {
        if (this.quadEmissiveChecked) {
            return this.quadEmissive;
        }
        if (this.quadEmissive == null && this.sprite != null && this.sprite.spriteEmissive != null) {
            this.quadEmissive = new BreakingFour(this, this.sprite.spriteEmissive);
        }
        this.quadEmissiveChecked = true;
        return this.quadEmissive;
    }
    
    @Override
    public String toString() {
        return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
    }
}
