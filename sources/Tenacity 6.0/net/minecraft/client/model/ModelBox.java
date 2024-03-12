// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.model;

import net.minecraft.client.renderer.WorldRenderer;

public class ModelBox
{
    private PositionTextureVertex[] vertexPositions;
    private TexturedQuad[] quadList;
    public final float posX1;
    public final float posY1;
    public final float posZ1;
    public final float posX2;
    public final float posY2;
    public final float posZ2;
    public String boxName;
    
    public ModelBox(final ModelRenderer renderer, final int p_i46359_2_, final int p_i46359_3_, final float p_i46359_4_, final float p_i46359_5_, final float p_i46359_6_, final int p_i46359_7_, final int p_i46359_8_, final int p_i46359_9_, final float p_i46359_10_) {
        this(renderer, p_i46359_2_, p_i46359_3_, p_i46359_4_, p_i46359_5_, p_i46359_6_, p_i46359_7_, p_i46359_8_, p_i46359_9_, p_i46359_10_, renderer.mirror);
    }
    
    public ModelBox(final ModelRenderer p_i0_1_, final int[][] p_i0_2_, float p_i0_3_, float p_i0_4_, float p_i0_5_, final float p_i0_6_, final float p_i0_7_, final float p_i0_8_, final float p_i0_9_, final boolean p_i0_10_) {
        this.posX1 = p_i0_3_;
        this.posY1 = p_i0_4_;
        this.posZ1 = p_i0_5_;
        this.posX2 = p_i0_3_ + p_i0_6_;
        this.posY2 = p_i0_4_ + p_i0_7_;
        this.posZ2 = p_i0_5_ + p_i0_8_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = p_i0_3_ + p_i0_6_;
        float f2 = p_i0_4_ + p_i0_7_;
        float f3 = p_i0_5_ + p_i0_8_;
        p_i0_3_ -= p_i0_9_;
        p_i0_4_ -= p_i0_9_;
        p_i0_5_ -= p_i0_9_;
        f += p_i0_9_;
        f2 += p_i0_9_;
        f3 += p_i0_9_;
        if (p_i0_10_) {
            final float f4 = f;
            f = p_i0_3_;
            p_i0_3_ = f4;
        }
        final PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i0_3_, p_i0_4_, p_i0_5_, 0.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex8 = new PositionTextureVertex(f, p_i0_4_, p_i0_5_, 0.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex9 = new PositionTextureVertex(f, f2, p_i0_5_, 8.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex10 = new PositionTextureVertex(p_i0_3_, f2, p_i0_5_, 8.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex11 = new PositionTextureVertex(p_i0_3_, p_i0_4_, f3, 0.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex12 = new PositionTextureVertex(f, p_i0_4_, f3, 0.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex13 = new PositionTextureVertex(f, f2, f3, 8.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex14 = new PositionTextureVertex(p_i0_3_, f2, f3, 8.0f, 0.0f);
        this.vertexPositions[0] = positiontexturevertex7;
        this.vertexPositions[1] = positiontexturevertex8;
        this.vertexPositions[2] = positiontexturevertex9;
        this.vertexPositions[3] = positiontexturevertex10;
        this.vertexPositions[4] = positiontexturevertex11;
        this.vertexPositions[5] = positiontexturevertex12;
        this.vertexPositions[6] = positiontexturevertex13;
        this.vertexPositions[7] = positiontexturevertex14;
        this.quadList[0] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex12, positiontexturevertex8, positiontexturevertex9, positiontexturevertex13 }, p_i0_2_[4], false, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
        this.quadList[1] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex11, positiontexturevertex14, positiontexturevertex10 }, p_i0_2_[5], false, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
        this.quadList[2] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex12, positiontexturevertex11, positiontexturevertex7, positiontexturevertex8 }, p_i0_2_[1], true, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
        this.quadList[3] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex9, positiontexturevertex10, positiontexturevertex14, positiontexturevertex13 }, p_i0_2_[0], true, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
        this.quadList[4] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex8, positiontexturevertex7, positiontexturevertex10, positiontexturevertex9 }, p_i0_2_[2], false, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
        this.quadList[5] = this.makeTexturedQuad(new PositionTextureVertex[] { positiontexturevertex11, positiontexturevertex12, positiontexturevertex13, positiontexturevertex14 }, p_i0_2_[3], false, p_i0_1_.textureWidth, p_i0_1_.textureHeight);
        if (p_i0_10_) {
            for (final TexturedQuad texturedquad : this.quadList) {
                texturedquad.flipFace();
            }
        }
    }
    
    private TexturedQuad makeTexturedQuad(final PositionTextureVertex[] p_makeTexturedQuad_1_, final int[] p_makeTexturedQuad_2_, final boolean p_makeTexturedQuad_3_, final float p_makeTexturedQuad_4_, final float p_makeTexturedQuad_5_) {
        return (p_makeTexturedQuad_2_ == null) ? null : (p_makeTexturedQuad_3_ ? new TexturedQuad(p_makeTexturedQuad_1_, p_makeTexturedQuad_2_[2], p_makeTexturedQuad_2_[3], p_makeTexturedQuad_2_[0], p_makeTexturedQuad_2_[1], p_makeTexturedQuad_4_, p_makeTexturedQuad_5_) : new TexturedQuad(p_makeTexturedQuad_1_, p_makeTexturedQuad_2_[0], p_makeTexturedQuad_2_[1], p_makeTexturedQuad_2_[2], p_makeTexturedQuad_2_[3], p_makeTexturedQuad_4_, p_makeTexturedQuad_5_));
    }
    
    public ModelBox(final ModelRenderer renderer, final int textureX, final int textureY, float p_i46301_4_, float p_i46301_5_, float p_i46301_6_, final int p_i46301_7_, final int p_i46301_8_, final int p_i46301_9_, final float p_i46301_10_, final boolean p_i46301_11_) {
        this.posX1 = p_i46301_4_;
        this.posY1 = p_i46301_5_;
        this.posZ1 = p_i46301_6_;
        this.posX2 = p_i46301_4_ + p_i46301_7_;
        this.posY2 = p_i46301_5_ + p_i46301_8_;
        this.posZ2 = p_i46301_6_ + p_i46301_9_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = p_i46301_4_ + p_i46301_7_;
        float f2 = p_i46301_5_ + p_i46301_8_;
        float f3 = p_i46301_6_ + p_i46301_9_;
        p_i46301_4_ -= p_i46301_10_;
        p_i46301_5_ -= p_i46301_10_;
        p_i46301_6_ -= p_i46301_10_;
        f += p_i46301_10_;
        f2 += p_i46301_10_;
        f3 += p_i46301_10_;
        if (p_i46301_11_) {
            final float f4 = f;
            f = p_i46301_4_;
            p_i46301_4_ = f4;
        }
        final PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, p_i46301_6_, 0.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex8 = new PositionTextureVertex(f, p_i46301_5_, p_i46301_6_, 0.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex9 = new PositionTextureVertex(f, f2, p_i46301_6_, 8.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex10 = new PositionTextureVertex(p_i46301_4_, f2, p_i46301_6_, 8.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex11 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, f3, 0.0f, 0.0f);
        final PositionTextureVertex positiontexturevertex12 = new PositionTextureVertex(f, p_i46301_5_, f3, 0.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex13 = new PositionTextureVertex(f, f2, f3, 8.0f, 8.0f);
        final PositionTextureVertex positiontexturevertex14 = new PositionTextureVertex(p_i46301_4_, f2, f3, 8.0f, 0.0f);
        this.vertexPositions[0] = positiontexturevertex7;
        this.vertexPositions[1] = positiontexturevertex8;
        this.vertexPositions[2] = positiontexturevertex9;
        this.vertexPositions[3] = positiontexturevertex10;
        this.vertexPositions[4] = positiontexturevertex11;
        this.vertexPositions[5] = positiontexturevertex12;
        this.vertexPositions[6] = positiontexturevertex13;
        this.vertexPositions[7] = positiontexturevertex14;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex12, positiontexturevertex8, positiontexturevertex9, positiontexturevertex13 }, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex11, positiontexturevertex14, positiontexturevertex10 }, textureX, textureY + p_i46301_9_, textureX + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex12, positiontexturevertex11, positiontexturevertex7, positiontexturevertex8 }, textureX + p_i46301_9_, textureY, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex9, positiontexturevertex10, positiontexturevertex14, positiontexturevertex13 }, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_7_, textureY, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex8, positiontexturevertex7, positiontexturevertex10, positiontexturevertex9 }, textureX + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex11, positiontexturevertex12, positiontexturevertex13, positiontexturevertex14 }, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        if (p_i46301_11_) {
            for (int i = 0; i < this.quadList.length; ++i) {
                this.quadList[i].flipFace();
            }
        }
    }
    
    public void render(final WorldRenderer renderer, final float scale) {
        for (int i = 0; i < this.quadList.length; ++i) {
            final TexturedQuad texturedquad = this.quadList[i];
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
