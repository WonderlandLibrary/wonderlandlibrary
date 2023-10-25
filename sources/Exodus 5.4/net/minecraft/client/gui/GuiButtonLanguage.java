/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonLanguage
extends GuiButton {
    @Override
    public void drawButton(Minecraft minecraft, int n, int n2) {
        if (this.visible) {
            minecraft.getTextureManager().bindTexture(GuiButton.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            boolean bl = n >= this.xPosition && n2 >= this.yPosition && n < this.xPosition + this.width && n2 < this.yPosition + this.height;
            int n3 = 106;
            if (bl) {
                n3 += this.height;
            }
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, n3, this.width, this.height);
        }
    }

    public GuiButtonLanguage(int n, int n2, int n3) {
        super(n, n2, n3, 20, 20, "");
    }
}

