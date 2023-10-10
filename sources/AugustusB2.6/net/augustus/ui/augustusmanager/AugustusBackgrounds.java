// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.augustusmanager;

import org.lwjgl.opengl.GL11;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import java.util.Iterator;
import net.augustus.ui.widgets.BGShaderButton;
import java.awt.Color;
import net.augustus.ui.widgets.CustomButton;
import net.augustus.Augustus;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;

public class AugustusBackgrounds extends GuiScreen
{
    private GuiScreen parent;
    
    public AugustusBackgrounds(final GuiScreen parent) {
        this.parent = parent;
    }
    
    public GuiScreen start(final GuiScreen parent) {
        this.parent = parent;
        return this;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.buttonList.add(new CustomButton(1, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() - sr.getScaledHeight() / 10, 200, 20, "Back", Augustus.getInstance().getClientColor()));
        int i = 0;
        int j = 2;
        for (final String s : Augustus.getInstance().getBackgroundShaderUtil().getShaderNames()) {
            final float x = sr.getScaledWidth() / 2.0f - this.fontRendererObj.getStringWidth(s) / 2.0f;
            final float y = (float)(60 + i);
            final float widthh = (float)this.fontRendererObj.getStringWidth(s);
            final float heightt = 11.0f;
            this.buttonList.add(new BGShaderButton(j, (int)x, (int)y, (int)widthh, (int)heightt, s, new Color(139, 141, 145, 255), new Color(67, 122, 163, 255)));
            i += 13;
            ++j;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
        for (final String s : Augustus.getInstance().getBackgroundShaderUtil().getShaderNames()) {
            if (button.displayString.equals(s)) {
                Augustus.getInstance().getBackgroundShaderUtil().setCurrentShader(s);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        GL11.glPushMatrix();
        GL11.glScaled(2.0, 2.0, 1.0);
        this.fontRendererObj.drawStringWithShadow("Backgrounds", sr.getScaledWidth() / 4.0f - this.fontRendererObj.getStringWidth("Backgrounds") / 2.0f, 10.0f, Color.lightGray.getRGB());
        GL11.glScaled(1.0, 1.0, 1.0);
        GL11.glPopMatrix();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 && this.mc.theWorld == null) {
            this.mc.displayGuiScreen(this.parent);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
