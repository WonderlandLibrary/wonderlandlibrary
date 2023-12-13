// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.Gui;
import com.elementars.eclient.util.ColorUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import net.minecraft.util.ResourceLocation;
import com.elementars.eclient.guirewrite.Element;

public class CraftingPreview extends Element
{
    private static final ResourceLocation box;
    private Value<String> background;
    
    public CraftingPreview() {
        super("CraftingPreview");
        this.background = this.register(new Value<String>("Background", this, "Texture", new String[] { "Texture", "Transparent", "None" }));
    }
    
    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
    }
    
    private static void postboxrender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
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
        this.width = 36.0;
        this.height = 36.0;
        super.onEnable();
    }
    
    @Override
    public void onRender() {
        if (this.background.getValue().equalsIgnoreCase("Texture")) {
            this.boxrender((int)this.x, (int)this.y);
        }
        else if (this.background.getValue().equalsIgnoreCase("Transparent")) {
            Gui.drawRect((int)this.x, (int)this.y, (int)this.x + (int)this.width, (int)this.y + (int)this.height, ColorUtils.changeAlpha(-15592942, 100));
        }
        for (int i = 1; i < 5; ++i) {
            final ItemStack itemStack = (ItemStack)CraftingPreview.mc.player.inventoryContainer.getInventory().get(i);
            final int slotx = (int)this.x + 1 + (i - 1) * 18 - ((i > 2) ? 36 : 0);
            final int sloty = (int)this.y + 1 + ((i > 2) ? 18 : 0);
            this.itemrender(itemStack, slotx, sloty);
        }
    }
    
    private void boxrender(final int x, final int y) {
        preboxrender();
        InvPreview.mc.renderEngine.bindTexture(CraftingPreview.box);
        InvPreview.mc.ingameGUI.drawTexturedModalRect(x, y, 7, 17, 36, 36);
        postboxrender();
    }
    
    private void itemrender(final ItemStack itemStack, final int x, final int y) {
        preitemrender();
        InvPreview.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        InvPreview.mc.getRenderItem().renderItemOverlays(InvPreview.mc.fontRenderer, itemStack, x, y);
        postitemrender();
    }
    
    static {
        box = new ResourceLocation("textures/gui/container/generic_54.png");
    }
}
