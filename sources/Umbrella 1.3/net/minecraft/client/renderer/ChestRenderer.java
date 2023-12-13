/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.item.ItemStack;

public class ChestRenderer {
    private static final String __OBFID = "CL_00002530";

    public void func_178175_a(Block p_178175_1_, float p_178175_2_) {
        GlStateManager.color(p_178175_2_, p_178175_2_, p_178175_2_, 1.0f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        TileEntityRendererChestHelper.instance.renderByItem(new ItemStack(p_178175_1_));
    }
}

