package org.dreamcore.client.ui.components.draggable.impl;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.dreamcore.client.feature.impl.hud.HUD;
import org.dreamcore.client.ui.components.draggable.DraggableModule;

public class ArmorComponent extends DraggableModule {

    public ArmorComponent() {
        super("ArmorComponent", 100, 350);
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
        int i = getX();
        int count = 0;
        int y = getY();
        for (ItemStack is : mc.player.inventory.armorInventory) {
            count++;
            if (is.isEmpty()) {
                drag.setCanRender(false);
            } else {
                drag.setCanRender(true);
                int x = i - 90 + (9 - count) * 20 + 2;
                GlStateManager.enableDepth();
                mc.getRenderItem().zLevel = 200F;
                mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, is, x, y, "");
                mc.getRenderItem().zLevel = 0F;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        if (HUD.armor.getBoolValue()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableTexture2D();
            int i = getX();
            int count = 0;
            int y = getY();
            for (ItemStack is : mc.player.inventory.armorInventory) {
                count++;
                if (is.isEmpty()) {
                    drag.setCanRender(false);
                } else {
                    drag.setCanRender(true);
                    int x = i - 90 + (9 - count) * 20 + 2;
                    GlStateManager.enableDepth();
                    mc.getRenderItem().zLevel = 200F;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, is, x, y, "");
                    mc.getRenderItem().renderItemAndEffectIntoGUI(is, x, y);
                    mc.getRenderItem().zLevel = 0F;
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                }
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }
        super.draw();
    }
}
