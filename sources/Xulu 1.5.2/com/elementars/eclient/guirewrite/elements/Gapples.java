// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.elementars.eclient.guirewrite.Element;

public class Gapples extends Element
{
    public Gapples() {
        super("Gapples");
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
        int gapple = Gapples.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() instanceof ItemAppleGold).mapToInt(ItemStack::getCount).sum();
        if (Gapples.mc.player.getHeldItemOffhand().getItem() instanceof ItemAppleGold) {
            gapple += Gapples.mc.player.getHeldItemOffhand().stackSize;
        }
        final ItemStack items = new ItemStack(Items.GOLDEN_APPLE, gapple);
        items.addEnchantment(Enchantments.SHARPNESS, 32767);
        this.itemrender(items);
    }
    
    private void itemrender(final ItemStack itemStack) {
        preitemrender();
        Gapples.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)this.x, (int)this.y);
        Gapples.mc.getRenderItem().renderItemOverlays(Gapples.mc.fontRenderer, itemStack, (int)this.x, (int)this.y);
        postitemrender();
    }
}
