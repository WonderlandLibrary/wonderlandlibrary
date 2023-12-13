/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.screen.component;

import me.arithmo.Client;
import me.arithmo.management.FontManager;
import me.arithmo.util.render.Colors;
import me.arithmo.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class GuiMenuButton
extends GuiButton {
    private ResourceLocation icon;
    float targetX;
    float currentX;

    public GuiMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.icon = new ResourceLocation("textures/menu/" + this.displayString.toLowerCase() + ".png");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition + this.width / 2 && mouseY >= this.yPosition && mouseX < this.xPosition + this.width / 2 + 75 && mouseY < this.yPosition + 90;
            this.mouseDragged(mc, mouseX, mouseY);
            int text = this.hovered ? Colors.getColor(255) : Colors.getColor(232);
            this.targetX = this.hovered ? 10.0f : 0.0f;
            float diff = (this.targetX - this.currentX) * 0.6f;
            this.currentX += diff;
            GlStateManager.pushMatrix();
            float offset = this.xPosition + this.width / 2;
            GlStateManager.translate(offset, (float)this.yPosition - this.currentX, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            mc.getTextureManager().bindTexture(this.icon);
            GuiMenuButton.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, 75, 75, 75.0f, 75.0f);
            GlStateManager.bindTexture(0);
            TTFFontRenderer font = Client.fm.getFont("SFR 12");
            font.drawStringWithShadow(this.displayString, 37.0f - font.getWidth(this.displayString) / 2.0f, 80.0f, text);
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.visible && mouseX >= this.xPosition + this.width / 2 && mouseY >= this.yPosition && mouseX < this.xPosition + this.width / 2 + 75 && mouseY < this.yPosition + 90;
    }
}

