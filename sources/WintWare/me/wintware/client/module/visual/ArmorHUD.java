/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event2D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ArmorHUD
extends Module {
    private static RenderItem kappita;
    private static final RenderItem itemRender;

    public ArmorHUD() {
        super("ArmorHUD", Category.Visuals);
    }

    @EventTarget
    public void onRender2D(Event2D event) {
        GlStateManager.enableTexture2D();
        ScaledResolution resolution = new ScaledResolution(mc);
        int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        int y = resolution.getScaledHeight() - 65 - (Minecraft.player.isInWater() ? 10 : 0);
        for (ItemStack is : Minecraft.player.inventory.armorInventory) {
            ++iteration;
            if (is.func_190926_b()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            ArmorHUD.itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            itemRender.renderItemOverlayIntoGUI(ArmorHUD.mc.fontRendererObj, is, x, y, "");
            ArmorHUD.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            ArmorHUD.mc.clickguismall.drawStringWithShadow(s, x + 19 - 2 - ArmorHUD.mc.fontRenderer.getStringWidth(s), y + 20, 0xFFFFFF);
            int green = Math.abs(is.getMaxDamage() - is.getItemDamage());
            ArmorHUD.mc.clickguismall.drawStringWithShadow(green + "", x + 8 - ArmorHUD.mc.clickguismall.getStringWidth(green + "") / 2, y - -18, -1);
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    static {
        itemRender = Minecraft.getMinecraft().getRenderItem();
    }
}

