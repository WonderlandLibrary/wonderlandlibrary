/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable.impl;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.hud.InventoryPreview;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;

public class InvPreviewComponent
extends DraggableModule {
    public InvPreviewComponent() {
        super("InvPreviewComponent", 240, 220);
    }

    @Override
    public int getWidth() {
        return 145;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public void draw() {
        if (this.mc.player == null || this.mc.world == null || !Celestial.instance.featureManager.getFeatureByClass(InventoryPreview.class).getState()) {
            return;
        }
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        float x = this.getX();
        float y = this.getY();
        float left = x;
        float top = y;
        float right = left + 145.0f;
        float bottom = top + 45.0f;
        GlStateManager.pushMatrix();
        if (InventoryPreview.shadow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)(left - 10.0f), (double)(top - 8.0f), 162.0, 65.0, (int)InventoryPreview.shadowRadius.getCurrentValue());
        }
        RectHelper.drawRect(x, y - 16.0f, x + 145.0f, y + 50.0f, new Color(5, 5, 5, 175).getRGB());
        this.mc.comfortaa.drawStringWithShadow("Inventory Preview", x + 30.0f, y - 11.0f, -1);
        RectHelper.drawRect(x + 3.0f, y - 1.0f, right - 3.0f, (double)y - 1.5, new Color(205, 205, 205).getRGB());
        RectHelper.drawSmoothRect(x, y - 16.0f, right, y - 15.0f, ClientHelper.getClientColor().getRGB());
        if (InventoryPreview.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(ClientHelper.getClientColor().brighter().brighter(), (double)x, (double)(y - 16.0f), 145.0, 1.5, 10);
        }
        GlStateManager.pushMatrix();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        for (int i = 0; i < 27; ++i) {
            ItemStack itemStack = this.mc.player.inventory.mainInventory.get(i + 9);
            int offsetX = (int)(x + (float)(i % 9 * 16));
            int offsetY = (int)(y + (float)(i / 9 * 16));
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
            this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRendererObj, itemStack, offsetX, offsetY, null);
        }
        GlStateManager.popMatrix();
        this.mc.getRenderItem().zLevel = 0.0f;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
        super.draw();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (this.mc.player == null || this.mc.world == null || !Celestial.instance.featureManager.getFeatureByClass(InventoryPreview.class).getState()) {
            return;
        }
        if (this.mc.gameSettings.showDebugInfo) {
            return;
        }
        float x = this.getX();
        float y = this.getY();
        float left = x;
        float top = y;
        float right = left + 145.0f;
        float bottom = top + 45.0f;
        GlStateManager.pushMatrix();
        if (InventoryPreview.shadow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)(left - 10.0f), (double)(top - 8.0f), 162.0, 65.0, (int)InventoryPreview.shadowRadius.getCurrentValue());
        }
        RectHelper.drawRect(x, y - 16.0f, x + 145.0f, y + 50.0f, new Color(5, 5, 5, 175).getRGB());
        this.mc.comfortaa.drawStringWithShadow("Inventory Preview", x + 30.0f, y - 11.0f, -1);
        RectHelper.drawRect(x + 3.0f, y - 1.0f, right - 3.0f, (double)y - 1.5, new Color(205, 205, 205).getRGB());
        RectHelper.drawSmoothRect(x, y - 16.0f, right, y - 15.0f, ClientHelper.getClientColor().getRGB());
        if (InventoryPreview.glow.getCurrentValue()) {
            RenderHelper.renderBlurredShadow(ClientHelper.getClientColor().brighter().brighter(), (double)x, (double)(y - 16.0f), 145.0, 1.5, 10);
        }
        GlStateManager.pushMatrix();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        for (int i = 0; i < 27; ++i) {
            ItemStack itemStack = this.mc.player.inventory.mainInventory.get(i + 9);
            int offsetX = (int)(x + (float)(i % 9 * 16));
            int offsetY = (int)(y + (float)(i / 9 * 16));
            this.mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
            this.mc.getRenderItem().renderItemOverlayIntoGUI(this.mc.fontRendererObj, itemStack, offsetX, offsetY, null);
        }
        GlStateManager.popMatrix();
        this.mc.getRenderItem().zLevel = 0.0f;
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
        super.render(mouseX, mouseY);
    }
}

