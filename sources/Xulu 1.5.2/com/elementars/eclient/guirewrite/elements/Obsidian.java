// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.elementars.eclient.guirewrite.Element;

public class Obsidian extends Element
{
    public Obsidian() {
        super("Obsidian");
    }
    
    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }
    
    private static void postitemrender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    @Override
    public void onEnable() {
        this.width = 16.0;
        this.height = 16.0;
        super.onEnable();
    }
    
    @Override
    public void onRender() {
        int obsidian = Obsidian.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)).mapToInt(ItemStack::getCount).sum();
        if (Obsidian.mc.player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
            obsidian += Obsidian.mc.player.getHeldItemOffhand().stackSize;
        }
        final ItemStack items = new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN), obsidian);
        this.itemrender(items);
    }
    
    private void itemrender(final ItemStack itemStack) {
        preitemrender();
        Obsidian.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)this.x, (int)this.y);
        Obsidian.mc.getRenderItem().renderItemOverlays(Obsidian.mc.fontRenderer, itemStack, (int)this.x, (int)this.y);
        postitemrender();
    }
}
