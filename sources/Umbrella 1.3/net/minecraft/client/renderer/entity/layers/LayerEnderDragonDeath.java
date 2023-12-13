/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;

public class LayerEnderDragonDeath
implements LayerRenderer {
    private static final String __OBFID = "CL_00002420";

    public void func_177213_a(EntityDragon p_177213_1_, float p_177213_2_, float p_177213_3_, float p_177213_4_, float p_177213_5_, float p_177213_6_, float p_177213_7_, float p_177213_8_) {
        if (p_177213_1_.deathTicks > 0) {
            Tessellator var9 = Tessellator.getInstance();
            WorldRenderer var10 = var9.getWorldRenderer();
            RenderHelper.disableStandardItemLighting();
            float var11 = ((float)p_177213_1_.deathTicks + p_177213_4_) / 200.0f;
            float var12 = 0.0f;
            if (var11 > 0.8f) {
                var12 = (var11 - 0.8f) / 0.2f;
            }
            Random var13 = new Random(432L);
            GlStateManager.func_179090_x();
            GlStateManager.shadeModel(7425);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 1);
            GlStateManager.disableAlpha();
            GlStateManager.enableCull();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, -1.0f, -2.0f);
            int var14 = 0;
            while ((float)var14 < (var11 + var11 * var11) / 2.0f * 60.0f) {
                GlStateManager.rotate(var13.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(var13.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(var13.nextFloat() * 360.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(var13.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(var13.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(var13.nextFloat() * 360.0f + var11 * 90.0f, 0.0f, 0.0f, 1.0f);
                var10.startDrawing(6);
                float var15 = var13.nextFloat() * 20.0f + 5.0f + var12 * 10.0f;
                float var16 = var13.nextFloat() * 2.0f + 1.0f + var12 * 2.0f;
                var10.func_178974_a(0xFFFFFF, (int)(255.0f * (1.0f - var12)));
                var10.addVertex(0.0, 0.0, 0.0);
                var10.func_178974_a(0xFF00FF, 0);
                var10.addVertex(-0.866 * (double)var16, var15, -0.5f * var16);
                var10.addVertex(0.866 * (double)var16, var15, -0.5f * var16);
                var10.addVertex(0.0, var15, 1.0f * var16);
                var10.addVertex(-0.866 * (double)var16, var15, -0.5f * var16);
                var9.draw();
                ++var14;
            }
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.shadeModel(7424);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.func_179098_w();
            GlStateManager.enableAlpha();
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        this.func_177213_a((EntityDragon)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}

