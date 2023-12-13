package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class ChestRenderer
{
    public void renderChestBrightness(Block p_178175_1_, float color)
    {
        GL11.glColor4f(color, color, color, 1.0F);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        TileEntityItemStackRenderer.instance.renderByItem(new ItemStack(p_178175_1_));
    }
}
