/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class RenderTntMinecart
extends RenderMinecart<EntityMinecartTNT> {
    @Override
    protected void func_180560_a(EntityMinecartTNT entityMinecartTNT, float f, IBlockState iBlockState) {
        int n = entityMinecartTNT.getFuseTicks();
        if (n > -1 && (float)n - f + 1.0f < 10.0f) {
            float f2 = 1.0f - ((float)n - f + 1.0f) / 10.0f;
            f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
            f2 *= f2;
            f2 *= f2;
            float f3 = 1.0f + f2 * 0.3f;
            GlStateManager.scale(f3, f3, f3);
        }
        super.func_180560_a(entityMinecartTNT, f, iBlockState);
        if (n > -1 && n / 5 % 2 == 0) {
            BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0f, 1.0f, 1.0f, (1.0f - ((float)n - f + 1.0f) / 100.0f) * 0.8f);
            GlStateManager.pushMatrix();
            blockRendererDispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
    }

    public RenderTntMinecart(RenderManager renderManager) {
        super(renderManager);
    }
}

