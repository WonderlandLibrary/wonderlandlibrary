// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.BufferBuilder;

public class ModelBox
{
    private final PositionTextureVertex[] vertexPositions;
    private final TexturedQuad[] quadList;
    public final float posX1;
    public final float posY1;
    public final float posZ1;
    public final float posX2;
    public final float posY2;
    public final float posZ2;
    public String boxName;
    
    public ModelBox(final ModelRenderer renderer, final int texU, final int texV, final float x, final float y, final float z, final int dx, final int dy, final int dz, final float delta) {
        this(renderer, texU, texV, x, y, z, dx, dy, dz, delta, renderer.mirror);
    }
    
    public ModelBox(final ModelRenderer p_i3_1_, final int[][] p_i3_2_, float p_i3_3_, float p_i3_4_, float p_i3_5_, final float p_i3_6_, final float p_i3_7_, final float p_i3_8_, final float p_i3_9_, final boolean p_i3_10_) {
        this.posX1 = p_i3_3_;
        this.posY1 = p_i3_4_;
        this.posZ1 = p_i3_5_;
        this.posX2 = p_i3_3_ + p_i3_6_;
        this.posY2 = p_i3_4_ + p_i3_7_;
        this.posZ2 = p_i3_5_ + p_i3_8_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = p_i3_3_ + p_i3_6_;
        float f2 = p_i3_4_ + p_i3_7_;
        float f3 = p_i3_5_ + p_i3_8_;
        p_i3_3_ -= p_i3_9_;
        p_i3_4_ -= p_i3_9_;
        p_i3_5_ -= p_i3_9_;
        f += p_i3_9_;
        f2 += p_i3_9_;
        f3 += p_i3_9_;
        if (p_i3_10_) {
            final float f4 = f;
            f = p_i3_3_;
            p_i3_3_ = f4;
        }
        final PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i3_3_, p_i3_4_, p_i3_5_, 0.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex8 = new PositionTextureVertex(f, p_i3_4_, p_i3_5_, 0.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex9 = new PositionTextureVertex(f, f2, p_i3_5_, 8.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex10 = new PositionTextureVertex(p_i3_3_, f2, p_i3_5_, 8.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex11 = new PositionTextureVertex(p_i3_3_, p_i3_4_, f3, 0.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex12 = new PositionTextureVertex(f, p_i3_4_, f3, 0.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex13 = new PositionTextureVertex(f, f2, f3, 8.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex14 = new PositionTextureVertex(p_i3_3_, f2, f3, 8.0f, 0.0f);
        this.vertexPositions[0] = positiontexturevertex7;
        this.vertexPositions[1] = positiontexturevertex8;
        this.vertexPositions[2] = positiontexturevertex9;
        this.vertexPositions[3] = positiontexturevertex10;
        this.vertexPositions[4] = positiontexturevertex11;
        this.vertexPositions[5] = positiontexturevertex12;
        this.vertexPositions[6] = positiontexturevertex13;
        this.vertexPositions[7] = positiontexturevertex14;
        this.quadList[0] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex12, positiontexturevertex8, positiontexturevertex9, positiontexturevertex13 }, p_i3_2_[4], false, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[1] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex11, positiontexturevertex14, positiontexturevertex10 }, p_i3_2_[5], false, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[2] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex12, positiontexturevertex11, positiontexturevertex7, positiontexturevertex8 }, p_i3_2_[1], true, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[3] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex9, positiontexturevertex10, positiontexturevertex14, positiontexturevertex13 }, p_i3_2_[0], true, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[4] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex8, positiontexturevertex7, positiontexturevertex10, positiontexturevertex9 }, p_i3_2_[2], false, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[5] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex11, positiontexturevertex12, positiontexturevertex13, positiontexturevertex14 }, p_i3_2_[3], false, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        if (p_i3_10_) {
            for (final TexturedQuad texturedquad : this.quadList) {
                texturedquad.flipFace();
            }
        }
    }
    
    private TexturedQuad makeTexturedQuad(final PositionTextureVertex[] p_makeTexturedQuad_1_, final int[] p_makeTexturedQuad_2_, final boolean p_makeTexturedQuad_3_, final float p_makeTexturedQuad_4_, final float p_makeTexturedQuad_5_) {
        if (p_makeTexturedQuad_2_ == null) {
            return null;
        }
        return p_makeTexturedQuad_3_ ? new TexturedQuad(p_makeTexturedQuad_1_, p_makeTexturedQuad_2_[2], p_makeTexturedQuad_2_[3], p_makeTexturedQuad_2_[0], p_makeTexturedQuad_2_[1], p_makeTexturedQuad_4_, p_makeTexturedQuad_5_) : new TexturedQuad(p_makeTexturedQuad_1_, p_makeTexturedQuad_2_[0], p_makeTexturedQuad_2_[1], p_makeTexturedQuad_2_[2], p_makeTexturedQuad_2_[3], p_makeTexturedQuad_4_, p_makeTexturedQuad_5_);
    }
    
    public ModelBox(final ModelRenderer renderer, final int texU, final int texV, float x, float y, float z, final int dx, final int dy, final int dz, final float delta, final boolean mirror) {
        this.posX1 = x;
        this.posY1 = y;
        this.posZ1 = z;
        this.posX2 = x + dx;
        this.posY2 = y + dy;
        this.posZ2 = z + dz;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = x + dx;
        float f2 = y + dy;
        float f3 = z + dz;
        x -= delta;
        y -= delta;
        z -= delta;
        f += delta;
        f2 += delta;
        f3 += delta;
        if (mirror) {
            final float f4 = f;
            f = x;
            x = f4;
        }
        final PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(x, y, z, 0.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex8 = new PositionTextureVertex(f, y, z, 0.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex9 = new PositionTextureVertex(f, f2, z, 8.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex10 = new PositionTextureVertex(x, f2, z, 8.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex11 = new PositionTextureVertex(x, y, f3, 0.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex12 = new PositionTextureVertex(f, y, f3, 0.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex13 = new PositionTextureVertex(f, f2, f3, 8.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex14 = new PositionTextureVertex(x, f2, f3, 8.0f, 0.0f);
        this.vertexPositions[0] = positiontexturevertex7;
        this.vertexPositions[1] = positiontexturevertex8;
        this.vertexPositions[2] = positiontexturevertex9;
        this.vertexPositions[3] = positiontexturevertex10;
        this.vertexPositions[4] = positiontexturevertex11;
        this.vertexPositions[5] = positiontexturevertex12;
        this.vertexPositions[6] = positiontexturevertex13;
        this.vertexPositions[7] = positiontexturevertex14;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex12, positiontexturevertex8, positiontexturevertex9, positiontexturevertex13 }, texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex11, positiontexturevertex14, positiontexturevertex10 }, texU, texV + dz, texU + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex12, positiontexturevertex11, positiontexturevertex7, positiontexturevertex8 }, texU + dz, texV, texU + dz + dx, texV + dz, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex9, positiontexturevertex10, positiontexturevertex14, positiontexturevertex13 }, texU + dz + dx, texV + dz, texU + dz + dx + dx, texV, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex8, positiontexturevertex7, positiontexturevertex10, positiontexturevertex9 }, texU + dz, texV + dz, texU + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex11, positiontexturevertex12, positiontexturevertex13, positiontexturevertex14 }, texU + dz + dx + dz, texV + dz, texU + dz + dx + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        if (mirror) {
            for (final TexturedQuad texturedquad : this.quadList) {
                texturedquad.flipFace();
            }
        }
    }
    
    public void render(final BufferBuilder renderer, final float scale) {
        for (final TexturedQuad texturedquad : this.quadList) {
            if (texturedquad != null) {
                texturedquad.draw(renderer, scale);
            }
        }
    }
    
    public ModelBox setBoxName(final String name) {
        this.boxName = name;
        return this;
    }
}
