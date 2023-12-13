// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.tileentity.TileEntityRendererChestHelper;
import net.minecraft.block.Block;

public class ChestRenderer
{
    private static final String __OBFID = "CL_00002530";
    
    public void func_178175_a(final Block p_178175_1_, final float p_178175_2_) {
        GlStateManager.color(p_178175_2_, p_178175_2_, p_178175_2_, 1.0f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        TileEntityRendererChestHelper.instance.renderByItem(new ItemStack(p_178175_1_));
    }
}
