package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class ChestRenderer {
    public void renderChestBrightness(Block p_178175_1_, float color) {
        GlStateManager.color((float)color, (float)color, (float)color, (float)1.0f);
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        TileEntityItemStackRenderer.instance.renderByItem(new ItemStack(p_178175_1_));
    }
}
