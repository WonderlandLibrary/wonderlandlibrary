// 
// Decompiled by Procyon v0.5.30
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
    public String field_78247_g;
    private static final String __OBFID = "CL_00000872";
    
    public ModelBox(final ModelRenderer p_i46359_1_, final int p_i46359_2_, final int p_i46359_3_, final float p_i46359_4_, final float p_i46359_5_, final float p_i46359_6_, final int p_i46359_7_, final int p_i46359_8_, final int p_i46359_9_, final float p_i46359_10_) {
        this(p_i46359_1_, p_i46359_2_, p_i46359_3_, p_i46359_4_, p_i46359_5_, p_i46359_6_, p_i46359_7_, p_i46359_8_, p_i46359_9_, p_i46359_10_, p_i46359_1_.mirror);
    }
    
    public ModelBox(final ModelRenderer p_i46301_1_, final int p_i46301_2_, final int p_i46301_3_, float p_i46301_4_, float p_i46301_5_, float p_i46301_6_, final int p_i46301_7_, final int p_i46301_8_, final int p_i46301_9_, final float p_i46301_10_, final boolean p_i46301_11_) {
        this.posX1 = p_i46301_4_;
        this.posY1 = p_i46301_5_;
        this.posZ1 = p_i46301_6_;
        this.posX2 = p_i46301_4_ + p_i46301_7_;
        this.posY2 = p_i46301_5_ + p_i46301_8_;
        this.posZ2 = p_i46301_6_ + p_i46301_9_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float var12 = p_i46301_4_ + p_i46301_7_;
        float var13 = p_i46301_5_ + p_i46301_8_;
        float var14 = p_i46301_6_ + p_i46301_9_;
        p_i46301_4_ -= p_i46301_10_;
        p_i46301_5_ -= p_i46301_10_;
        p_i46301_6_ -= p_i46301_10_;
        var12 += p_i46301_10_;
        var13 += p_i46301_10_;
        var14 += p_i46301_10_;
        if (p_i46301_11_) {
            final float var15 = var12;
            var12 = p_i46301_4_;
            p_i46301_4_ = var15;
        }
        final PositionTextureVertex var16 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, p_i46301_6_, 0.0f, 0.0f);
        final PositionTextureVertex var17 = new PositionTextureVertex(var12, p_i46301_5_, p_i46301_6_, 0.0f, 8.0f);
        final PositionTextureVertex var18 = new PositionTextureVertex(var12, var13, p_i46301_6_, 8.0f, 8.0f);
        final PositionTextureVertex var19 = new PositionTextureVertex(p_i46301_4_, var13, p_i46301_6_, 8.0f, 0.0f);
        final PositionTextureVertex var20 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, var14, 0.0f, 0.0f);
        final PositionTextureVertex var21 = new PositionTextureVertex(var12, p_i46301_5_, var14, 0.0f, 8.0f);
        final PositionTextureVertex var22 = new PositionTextureVertex(var12, var13, var14, 8.0f, 8.0f);
        final PositionTextureVertex var23 = new PositionTextureVertex(p_i46301_4_, var13, var14, 8.0f, 0.0f);
        this.vertexPositions[0] = var16;
        this.vertexPositions[1] = var17;
        this.vertexPositions[2] = var18;
        this.vertexPositions[3] = var19;
        this.vertexPositions[4] = var20;
        this.vertexPositions[5] = var21;
        this.vertexPositions[6] = var22;
        this.vertexPositions[7] = var23;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] { var21, var17, var18, var22 }, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] { var16, var20, var23, var19 }, p_i46301_2_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] { var21, var20, var16, var17 }, p_i46301_2_ + p_i46301_9_, p_i46301_3_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] { var18, var19, var23, var22 }, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_7_, p_i46301_3_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] { var17, var16, var19, var18 }, p_i46301_2_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] { var20, var21, var22, var23 }, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        if (p_i46301_11_) {
            for (int var24 = 0; var24 < this.quadList.length; ++var24) {
                this.quadList[var24].flipFace();
            }
        }
    }
    
    public void render(final WorldRenderer p_178780_1_, final float p_178780_2_) {
        for (int var3 = 0; var3 < this.quadList.length; ++var3) {
            this.quadList[var3].func_178765_a(p_178780_1_, p_178780_2_);
        }
    }
    
    public ModelBox func_78244_a(final String p_78244_1_) {
        this.field_78247_g = p_78244_1_;
        return this;
    }
}
