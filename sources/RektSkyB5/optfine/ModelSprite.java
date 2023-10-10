/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelSprite {
    private ModelRenderer modelRenderer = null;
    private int textureOffsetX = 0;
    private int textureOffsetY = 0;
    private float posX = 0.0f;
    private float posY = 0.0f;
    private float posZ = 0.0f;
    private int sizeX = 0;
    private int sizeY = 0;
    private int sizeZ = 0;
    private float sizeAdd = 0.0f;
    private float minU = 0.0f;
    private float minV = 0.0f;
    private float maxU = 0.0f;
    private float maxV = 0.0f;

    public ModelSprite(ModelRenderer p_i43_1_, int p_i43_2_, int p_i43_3_, float p_i43_4_, float p_i43_5_, float p_i43_6_, int p_i43_7_, int p_i43_8_, int p_i43_9_, float p_i43_10_) {
        this.modelRenderer = p_i43_1_;
        this.textureOffsetX = p_i43_2_;
        this.textureOffsetY = p_i43_3_;
        this.posX = p_i43_4_;
        this.posY = p_i43_5_;
        this.posZ = p_i43_6_;
        this.sizeX = p_i43_7_;
        this.sizeY = p_i43_8_;
        this.sizeZ = p_i43_9_;
        this.sizeAdd = p_i43_10_;
        this.minU = (float)p_i43_2_ / p_i43_1_.textureWidth;
        this.minV = (float)p_i43_3_ / p_i43_1_.textureHeight;
        this.maxU = (float)(p_i43_2_ + p_i43_7_) / p_i43_1_.textureWidth;
        this.maxV = (float)(p_i43_3_ + p_i43_8_) / p_i43_1_.textureHeight;
    }

    public void render(Tessellator p_render_1_, float p_render_2_) {
        GlStateManager.translate(this.posX * p_render_2_, this.posY * p_render_2_, this.posZ * p_render_2_);
        float f2 = this.minU;
        float f1 = this.maxU;
        float f22 = this.minV;
        float f3 = this.maxV;
        if (this.modelRenderer.mirror) {
            f2 = this.maxU;
            f1 = this.minU;
        }
        if (this.modelRenderer.mirrorV) {
            f22 = this.maxV;
            f3 = this.minV;
        }
        ModelSprite.renderItemIn2D(p_render_1_, f2, f22, f1, f3, this.sizeX, this.sizeY, p_render_2_ * (float)this.sizeZ, this.modelRenderer.textureWidth, this.modelRenderer.textureHeight);
        GlStateManager.translate(-this.posX * p_render_2_, -this.posY * p_render_2_, -this.posZ * p_render_2_);
    }

    public static void renderItemIn2D(Tessellator p_renderItemIn2D_0_, float p_renderItemIn2D_1_, float p_renderItemIn2D_2_, float p_renderItemIn2D_3_, float p_renderItemIn2D_4_, int p_renderItemIn2D_5_, int p_renderItemIn2D_6_, float p_renderItemIn2D_7_, float p_renderItemIn2D_8_, float p_renderItemIn2D_9_) {
        if (p_renderItemIn2D_7_ < 6.25E-4f) {
            p_renderItemIn2D_7_ = 6.25E-4f;
        }
        float f2 = p_renderItemIn2D_3_ - p_renderItemIn2D_1_;
        float f1 = p_renderItemIn2D_4_ - p_renderItemIn2D_2_;
        double d0 = MathHelper.abs(f2) * (p_renderItemIn2D_8_ / 16.0f);
        double d1 = MathHelper.abs(f1) * (p_renderItemIn2D_9_ / 16.0f);
        WorldRenderer worldrenderer = p_renderItemIn2D_0_.getWorldRenderer();
        GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, 0.0, 0.0).tex(p_renderItemIn2D_1_, p_renderItemIn2D_2_).endVertex();
        worldrenderer.pos(d0, 0.0, 0.0).tex(p_renderItemIn2D_3_, p_renderItemIn2D_2_).endVertex();
        worldrenderer.pos(d0, d1, 0.0).tex(p_renderItemIn2D_3_, p_renderItemIn2D_4_).endVertex();
        worldrenderer.pos(0.0, d1, 0.0).tex(p_renderItemIn2D_1_, p_renderItemIn2D_4_).endVertex();
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, p_renderItemIn2D_4_).endVertex();
        worldrenderer.pos(d0, d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, p_renderItemIn2D_4_).endVertex();
        worldrenderer.pos(d0, 0.0, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, p_renderItemIn2D_2_).endVertex();
        worldrenderer.pos(0.0, 0.0, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, p_renderItemIn2D_2_).endVertex();
        p_renderItemIn2D_0_.draw();
        float f22 = 0.5f * f2 / (float)p_renderItemIn2D_5_;
        float f3 = 0.5f * f1 / (float)p_renderItemIn2D_6_;
        GL11.glNormal3f(-1.0f, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int i2 = 0; i2 < p_renderItemIn2D_5_; ++i2) {
            float f4 = (float)i2 / (float)p_renderItemIn2D_5_;
            float f5 = p_renderItemIn2D_1_ + f2 * f4 + f22;
            worldrenderer.pos((double)f4 * d0, 0.0, p_renderItemIn2D_7_).tex(f5, p_renderItemIn2D_2_).endVertex();
            worldrenderer.pos((double)f4 * d0, 0.0, 0.0).tex(f5, p_renderItemIn2D_2_).endVertex();
            worldrenderer.pos((double)f4 * d0, d1, 0.0).tex(f5, p_renderItemIn2D_4_).endVertex();
            worldrenderer.pos((double)f4 * d0, d1, p_renderItemIn2D_7_).tex(f5, p_renderItemIn2D_4_).endVertex();
        }
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(1.0f, 0.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int j2 = 0; j2 < p_renderItemIn2D_5_; ++j2) {
            float f7 = (float)j2 / (float)p_renderItemIn2D_5_;
            float f10 = p_renderItemIn2D_1_ + f2 * f7 + f22;
            float f6 = f7 + 1.0f / (float)p_renderItemIn2D_5_;
            worldrenderer.pos((double)f6 * d0, d1, p_renderItemIn2D_7_).tex(f10, p_renderItemIn2D_4_).endVertex();
            worldrenderer.pos((double)f6 * d0, d1, 0.0).tex(f10, p_renderItemIn2D_4_).endVertex();
            worldrenderer.pos((double)f6 * d0, 0.0, 0.0).tex(f10, p_renderItemIn2D_2_).endVertex();
            worldrenderer.pos((double)f6 * d0, 0.0, p_renderItemIn2D_7_).tex(f10, p_renderItemIn2D_2_).endVertex();
        }
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int k2 = 0; k2 < p_renderItemIn2D_6_; ++k2) {
            float f8 = (float)k2 / (float)p_renderItemIn2D_6_;
            float f11 = p_renderItemIn2D_2_ + f1 * f8 + f3;
            float f13 = f8 + 1.0f / (float)p_renderItemIn2D_6_;
            worldrenderer.pos(0.0, (double)f13 * d1, 0.0).tex(p_renderItemIn2D_1_, f11).endVertex();
            worldrenderer.pos(d0, (double)f13 * d1, 0.0).tex(p_renderItemIn2D_3_, f11).endVertex();
            worldrenderer.pos(d0, (double)f13 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, f11).endVertex();
            worldrenderer.pos(0.0, (double)f13 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, f11).endVertex();
        }
        p_renderItemIn2D_0_.draw();
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        for (int l2 = 0; l2 < p_renderItemIn2D_6_; ++l2) {
            float f9 = (float)l2 / (float)p_renderItemIn2D_6_;
            float f12 = p_renderItemIn2D_2_ + f1 * f9 + f3;
            worldrenderer.pos(d0, (double)f9 * d1, 0.0).tex(p_renderItemIn2D_3_, f12).endVertex();
            worldrenderer.pos(0.0, (double)f9 * d1, 0.0).tex(p_renderItemIn2D_1_, f12).endVertex();
            worldrenderer.pos(0.0, (double)f9 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_1_, f12).endVertex();
            worldrenderer.pos(d0, (double)f9 * d1, p_renderItemIn2D_7_).tex(p_renderItemIn2D_3_, f12).endVertex();
        }
        p_renderItemIn2D_0_.draw();
    }
}

