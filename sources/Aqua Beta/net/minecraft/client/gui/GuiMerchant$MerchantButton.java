package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.renderer.GlStateManager;

/*
 * Exception performing whole class analysis ignored.
 */
static class GuiMerchant.MerchantButton
extends GuiButton {
    private final boolean field_146157_o;

    public GuiMerchant.MerchantButton(int buttonID, int x, int y, boolean p_i1095_4_) {
        super(buttonID, x, y, 12, 19, "");
        this.field_146157_o = p_i1095_4_;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(GuiMerchant.access$000());
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = 0;
            int j = 176;
            if (!this.enabled) {
                j += this.width * 2;
            } else if (flag) {
                j += this.width;
            }
            if (!this.field_146157_o) {
                i += this.height;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
        }
    }
}
