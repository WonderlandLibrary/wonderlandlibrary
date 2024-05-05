package ru.smertnix.celestial.ui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import ru.smertnix.celestial.ui.mainmenu.MainMenu;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;

import org.lwjgl.input.Mouse;

import java.awt.*;

public class GuiMainMenuButton extends GuiButton {
	boolean a;
    private int fade = 0;

    public GuiMainMenuButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 35, buttonText);
    }

    public GuiMainMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }
    public GuiMainMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean a) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.a = true;
    }

    public static int getMouseX() {
        return Mouse.getX() * sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }

    public static int getMouseY() {
        return sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= (this.yPosition) && mouseX < this.xPosition + this.width && mouseY < (this.yPosition) + this.height + 10);
            Color text = new Color(255, 255, 255, 255);
            Color color = new Color(0, 128, 255, (int) (fade * 1.5f));
            
            if (this.hovered) {
                if (this.fade < 100) {
                    this.fade += 10;
                } else {
                	this.fade = 100;
                }
            } else {
                if (this.fade > 0)
                    this.fade -= 10;
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            int height = this.height + 11;
            if (MainMenu.s) {
            	   RenderUtils.drawShadow(10,1, () -> {
                       RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, height,3, new Color(-1));
                   });
                   RenderUtils.drawBlur(10, () -> {
                       RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, height,3, new Color(-1));
                   });
                   if (this.enabled)
                   RoundedUtil.drawGradientVertical(this.xPosition - fade + 100, this.yPosition, this.width + fade * 2 - 200, height,3, color.darker(), color);
            } else {
            	   RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, height,0, new Color(0,0,0,100));
            }
            if (a) {
                mc.mntsb_16.drawCenteredStringWithShadow(this.displayString.equals("1") ? (MainMenu.s ? "Shaders: true" : "Shaders: false") : this.displayString, this.xPosition + this.width / 2F, (this.yPosition) + (this.height / 1.2f + 1.5f), !this.enabled ? Color.GRAY.getRGB() : (hovered ? new Color(255,255,150).getRGB() : text.getRGB()));             
            } else {
                mc.mntsb_16.drawCenteredStringWithShadow(this.displayString.equals("1") ? (MainMenu.s ? "Shaders: true" : "Shaders: false") : this.displayString, this.xPosition + this.width / 2F, (this.yPosition) + (this.height / 1.2f), !this.enabled ? Color.GRAY.getRGB() : (hovered ? new Color(255,255,150).getRGB() : text.getRGB()));             
            }
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {

    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + 10);
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {

    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}