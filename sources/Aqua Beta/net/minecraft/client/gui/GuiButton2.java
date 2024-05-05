package net.minecraft.client.gui;

import intent.AquaDev.aqua.fontrenderer.ClientFont;
import intent.AquaDev.aqua.fontrenderer.GlyphPageFontRenderer;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButton2
extends GuiButton {
    GlyphPageFontRenderer fluxButton = ClientFont.font((int)15, (String)"Vision", (boolean)true);

    public GuiButton2(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public GuiButton2(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        int unhovered = Color.decode((String)"#0FA292").getRGB();
        int hovered = Color.decode((String)"#0C8275").getRGB();
        int blockedunhovered = Color.decode((String)"#7D7D7D").getRGB();
        int blockedhovered = Color.decode((String)"#646464").getRGB();
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        RenderUtil.glColor((int)Color.white.getRGB());
        mc.fontRendererObj.drawString(this.displayString, this.xPosition + this.width / 2, this.yPosition + this.height / 2 - this.fluxButton.getFontHeight() / 2, -1);
    }
}
