/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.celestial.client.feature.impl.hud.HUD;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class ArmorComponent
extends DraggableModule {
    public ArmorComponent() {
        super("ArmorComponent", 380, 357);
    }

    @Override
    public int getWidth() {
        return 105;
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        int i = this.getX();
        int y = this.getY();
        int count = 0;
        for (ItemStack is : this.mc.player.inventory.armorInventory) {
            ++count;
            if (is.isEmpty()) {
                this.drag.setCanRender(false);
                continue;
            }
            this.drag.setCanRender(true);
            int x = i - 90 + (9 - count) * 20 + 2;
            GlStateManager.enableDepth();
            this.mc.getRenderItem().zLevel = 200.0f;
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
            this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRendererObj, is, x, y, "");
            this.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (HUD.armor.getCurrentValue()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            int i = this.getX();
            int y = this.getY();
            int count = 0;
            for (ItemStack is : this.mc.player.inventory.armorInventory) {
                ++count;
                if (is.isEmpty()) {
                    this.drag.setCanRender(false);
                    continue;
                }
                this.drag.setCanRender(true);
                int x = i - 90 + (9 - count) * 20 + 2;
                GlStateManager.enableDepth();
                this.mc.getRenderItem().zLevel = 200.0f;
                this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
                this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRendererObj, is, x, y, "");
                this.mc.getRenderItem().zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }
        super.draw();
    }
}

