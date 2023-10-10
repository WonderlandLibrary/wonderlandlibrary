// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.clickgui.screens;

import net.augustus.Augustus;
import java.io.IOException;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.ColorPicker;
import net.augustus.settings.Setting;
import net.augustus.utils.interfaces.SM;
import net.augustus.utils.interfaces.MM;
import net.minecraft.client.gui.GuiScreen;

public class ColorPickerGui extends GuiScreen implements MM, SM
{
    private final Setting setting;
    private final ColorPicker colorPicker;
    
    public ColorPickerGui(final ColorSetting setting) {
        this.setting = setting;
        this.colorPicker = new ColorPicker(70.0, setting.getColor(), setting::setColor);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        GL11.glPushMatrix();
        GL11.glScaled(1.5, 1.5, 1.0);
        this.drawCenteredString(this.fontRendererObj, "Select color:", (int)(sr.getScaledWidth() / 1.5 / 2.0), (int)(sr.getScaledHeight() / 1.5 / 2.0 - 58.666666666666664), new Color(229, 231, 235).getRGB());
        GL11.glScaled(1.0, 1.0, 1.0);
        GL11.glPopMatrix();
        this.colorPicker.draw(mouseX, mouseY);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.colorPicker.click(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.colorPicker.click(mouseX, mouseY, clickedMouseButton);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(Augustus.getInstance().getClickGui());
        }
    }
    
    @Override
    public void onGuiClosed() {
    }
}
