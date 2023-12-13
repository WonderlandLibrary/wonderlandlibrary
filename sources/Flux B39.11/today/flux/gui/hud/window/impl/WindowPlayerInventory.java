package today.flux.gui.hud.window.impl;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import today.flux.gui.hud.window.HudWindow;

public class WindowPlayerInventory extends HudWindow {
    public WindowPlayerInventory() {
        super("PlayerInventory", 5, 100, 180, 62, "Inventory", "", 12, 1, .5f);
    }

    public float animation = 0;

    @Override
    public void draw() {
        super.draw();

        float startX = x + 2;
        float startY = y + draggableHeight + 3;
        int curIndex = 0;

        //Inventory Item
        for(int i = 9; i < 36; ++i) {
            ItemStack slot = mc.thePlayer.inventory.mainInventory[i];
            if(slot == null) {
                startX += 20;
                curIndex += 1;

                if(curIndex > 8) {
                    curIndex = 0;
                    startY += 20;
                    startX = x + 2;
                }

                continue;
            }

            this.drawItemStack(slot, startX, startY);
            startX += 20;
            curIndex += 1;
            if(curIndex > 8) {
                curIndex = 0;
                startY += 20;
                startX = x + 2;
            }
        }
    }

    private void drawItemStack(ItemStack stack, float x, float y) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        mc.getRenderItem().zLevel = -150.0F;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        mc.getRenderItem().renderItemIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, null);
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.enableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
