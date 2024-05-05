package net.minecraft.client.gui.CustomButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.Font;

import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.tools.FontUtils;

public class SimpleResourceButton
extends GuiButton {
    private ResourceLocation resourceLoc;
    private int textColour;
    FontUtils fu_default = new FontUtils("Audiowide", Font.PLAIN, 20);

    public SimpleResourceButton(int buttonID, int x, int y, int widthIn, int heightIn, String text, ResourceLocation resourceLoc) {
        super(buttonID, x, y, widthIn, heightIn, text);
        this.resourceLoc = resourceLoc;
        this.textColour = -1;
    }

    public SimpleResourceButton(int buttonID, int x, int y, String text, ResourceLocation resourceLoc) {
        super(buttonID, x, y, text);
        this.resourceLoc = resourceLoc;
        this.textColour = -1;
    }

    public SimpleResourceButton(int buttonID, int x, int y, int widthIn, int heightIn, String text, ResourceLocation resourceLoc, int color) {
        super(buttonID, x, y, widthIn, heightIn, text);
        this.resourceLoc = resourceLoc;
        this.textColour = color;
    }

    public SimpleResourceButton(int buttonID, int x, int y, String text, ResourceLocation resourceLoc, int color) {
        super(buttonID, x, y, text);
        this.resourceLoc = resourceLoc;
        this.textColour = color;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(this.resourceLoc);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Gui.drawScaledCustomSizeModalRect(this.xPosition, this.yPosition, 0.0f, 0.0f, this.width, this.height, this.width, this.height, this.width, this.height);
            fu_default.drawString(this.displayString, (float)(this.xPosition + this.width / 2) - fu_default.getStringWidth(this.displayString) / 2.0f, this.yPosition + this.height, 0x0ffffff);
            Gui.drawRect(0.0, 0.0, 0.0, 0.0, -1);
            if (!this.enabled) {
                Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 1610612736);
            }
        }
    }
}

