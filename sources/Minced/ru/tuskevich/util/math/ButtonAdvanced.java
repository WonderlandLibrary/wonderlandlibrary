// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import ru.tuskevich.util.render.BlurUtility;
import ru.tuskevich.util.font.Fonts;
import java.awt.Color;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.modules.impl.HUD.Hud;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.GuiButton;

public class ButtonAdvanced extends GuiButton
{
    public float size;
    
    public ButtonAdvanced(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 35, buttonText);
    }
    
    public ButtonAdvanced(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.size = 0.0f;
    }
    
    public static int getMouseX() {
        return Mouse.getX() * ButtonAdvanced.sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }
    
    public static int getMouseY() {
        return ButtonAdvanced.sr.getScaledHeight() - Mouse.getY() * ButtonAdvanced.sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float mouseButton) {
        if (this.visible) {
            final FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(ButtonAdvanced.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
            final int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.size = MathUtility.lerp(this.size, this.hovered ? 1.0f : 0.0f, 0.2f);
            RenderUtility.drawGradientRound((float)(this.x - 1), (float)(this.y + 2), (float)(this.width + 2), 17.0f, 3.0f, ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
            RenderUtility.drawRound((float)this.x, (float)(this.y + 3), (float)this.width, 15.0f, 2.0f, new Color(25, 25, 25, 255));
            this.mouseDragged(mc, mouseX, mouseY);
            final int j = 14737632;
            BlurUtility.drawShadow(this.size * 7.0f, 2.0f, () -> Fonts.Nunito17.drawCenteredString(this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2 + 1.5, -1), Color.WHITE);
            Fonts.Nunito17.drawCenteredString(this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2 + 1.5, -1);
        }
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height + 10;
    }
    
    @Override
    public boolean isMouseOver() {
        return this.hovered;
    }
    
    @Override
    public void playPressSound(final SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }
    
    @Override
    public int getButtonWidth() {
        return this.width;
    }
    
    @Override
    public void setWidth(final int width) {
        this.width = width;
    }
}
