package net.minecraft.client.gui;

import intent.AquaDev.aqua.Aqua;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class IMGButton
extends GuiButton {
    private ResourceLocation loc;
    private int notHoveredWidth;
    private int notHoveredHeight;
    private int scale = 0;
    public String buttonText;

    public IMGButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation loc) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.loc = loc;
        this.notHoveredWidth = widthIn;
        this.notHoveredHeight = heightIn;
        this.buttonText = buttonText;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(this.loc);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            GlStateManager.blendFunc((int)770, (int)771);
            this.mouseDragged(mc, mouseX, mouseY);
            if (this.hovered) {
                Color color = Color.white;
                Aqua.INSTANCE.comfortaa4.drawString(this.buttonText, (float)(this.xPosition + 1), (float)(this.yPosition + 55), color.getRGB());
                if (this.scale < 10 && this.scale + 1 < 10) {
                    this.scale += 2;
                }
                this.notHoveredWidth = this.width + this.scale;
                this.notHoveredHeight = this.height + this.scale;
            } else {
                if ((float)(this.scale + 20) > 9.5f) {
                    this.scale -= 2;
                }
                this.notHoveredWidth = this.width + this.scale;
                this.notHoveredHeight = this.height + this.scale;
            }
            if (this.hovered) {
                GL11.glColor4f((float)0.78431374f, (float)0.78431374f, (float)0.78431374f, (float)1.0f);
            } else {
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            }
            IMGButton.drawImage(this.xPosition - this.scale / 2, this.yPosition - this.scale / 2, this.notHoveredWidth, this.notHoveredHeight, this.loc);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    public static void drawImage(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
    }
}
