// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.clickgui.screens;

import java.io.IOException;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import net.augustus.Augustus;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;
import net.augustus.utils.interfaces.SM;
import net.augustus.utils.interfaces.MM;
import net.minecraft.client.gui.GuiScreen;

public class KeySettingGui extends GuiScreen implements MM, SM
{
    private final Module module;
    private final TimeHelper timeHelper;
    private boolean inNewKey;
    
    public KeySettingGui(final Module module) {
        this.module = module;
        this.timeHelper = new TimeHelper();
        this.inNewKey = false;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final FontRenderer fr = this.fontRendererObj;
        int width = this.mc.currentScreen.width / 2;
        int height = this.mc.currentScreen.height / 2;
        final int left = width - 100;
        final int right = width + 100;
        final int top = height - 60;
        final int bottom = height + 60;
        Gui.drawRect(left, top, right, bottom, new Color(0, 0, 0, 190).getRGB());
        this.drawHorizontalLine(left, right, top, Augustus.getInstance().getClientColor().getRGB());
        this.drawHorizontalLine(left, right, bottom, Augustus.getInstance().getClientColor().getRGB());
        this.drawVerticalLine(left, top, bottom, Augustus.getInstance().getClientColor().getRGB());
        this.drawVerticalLine(right, top, bottom, Augustus.getInstance().getClientColor().getRGB());
        final int thisWidth = (int)(fr.getStringWidth("Module to bind: ") + fr.getStringWidth(this.module.getName()) * 1.5);
        final int firstWidth = fr.getStringWidth("Module to bind: ");
        fr.drawStringWithShadow("Module to bind: ", (float)(width - thisWidth / 2), (float)(top + 10), new Color(139, 141, 145).getRGB());
        GL11.glPushMatrix();
        GL11.glScaled(1.5, 1.5, 1.0);
        width = (int)(this.mc.currentScreen.width / 1.5 / 2.0);
        height = (int)(this.mc.currentScreen.height / 1.5 / 2.0);
        fr.drawStringWithShadow(this.module.getName(), (float)(int)((int)(width - (firstWidth / 1.5 + fr.getStringWidth(this.module.getName())) / 2.0) + firstWidth / 1.5 + 2.0), (float)(height - 35), new Color(67, 122, 163, 255).getRGB());
        GL11.glScaled(1.0, 1.0, 1.0);
        GL11.glPopMatrix();
        width = this.mc.currentScreen.width / 2;
        height = this.mc.currentScreen.height / 2;
        if (this.inNewKey) {
            this.drawCenteredString(fr, "PRESSED!", width, top + 30, new Color(139, 141, 145).getRGB());
            this.drawCenteredString(fr, "The NEW key is:", width, top + 45, new Color(139, 141, 145).getRGB());
        }
        else {
            this.drawCenteredString(fr, "Wating for KEY to PRESS ...", width, top + 30, new Color(139, 141, 145).getRGB());
            this.drawCenteredString(fr, "The CURRENT key is:", width, top + 45, new Color(139, 141, 145).getRGB());
        }
        GL11.glPushMatrix();
        GL11.glScaled(3.0, 3.0, 1.0);
        width = (int)(this.mc.currentScreen.width / 3.0 / 2.0);
        height = (int)(this.mc.currentScreen.height / 3.0 / 2.0);
        final String name = Keyboard.getKeyName(this.module.getKey());
        int color = new Color(139, 141, 145).getRGB();
        if (this.inNewKey) {
            color = new Color(67, 122, 163, 255).getRGB();
        }
        this.drawCenteredString(fr, name, width, height + 3, color);
        GL11.glScaled(1.0, 1.0, 1.0);
        GL11.glPopMatrix();
        width = this.mc.currentScreen.width / 2;
        height = this.mc.currentScreen.height / 2;
        if (this.inNewKey) {
            this.drawCenteredString(fr, "Press ESCAPE to exit", width, bottom - 15, new Color(139, 141, 145).getRGB());
        }
        else {
            this.drawCenteredString(fr, "Press ESCAPE to unbind key", width, bottom - 15, new Color(139, 141, 145).getRGB());
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.inNewKey) {
            if (keyCode == 1) {
                this.module.setKey(0);
                this.mc.displayGuiScreen(Augustus.getInstance().getClickGui());
            }
            else {
                this.module.setKey(keyCode);
            }
            this.inNewKey = true;
        }
        else if (keyCode == 1) {
            this.mc.displayGuiScreen(Augustus.getInstance().getClickGui());
        }
    }
    
    @Override
    public void onGuiClosed() {
    }
}
