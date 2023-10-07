// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.ui.augustusmanager;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.augustus.Augustus;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.ui.widgets.CustomButton;
import net.minecraft.client.gui.GuiScreen;

public class CreditsScreen extends GuiScreen
{
    private GuiScreen parent;
    private CustomButton pikabutton;
    
    public CreditsScreen(final GuiScreen parent) {
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
        final int scaledWidth = sr.getScaledWidth();
        final int scaledHeight = sr.getScaledHeight();
        this.buttonList.add(new CustomButton(2, scaledWidth / 2 - 100, scaledHeight - scaledHeight / 10, 200, 20, "Back", Augustus.getInstance().getClientColor()));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        GL11.glScaled(2.0, 2.0, 1.0);
        this.fontRendererObj.drawStringWithShadow("Credits", sr.getScaledWidth() / 4.0f - this.fontRendererObj.getStringWidth("Credits") / 2.0f, 10.0f, Color.lightGray.getRGB());
        GL11.glScaled(1.0, 1.0, 1.0);
        this.fontRendererObj.drawString("This is a modified version of the free version on Augustus!", sr.getScaledWidth() / 4 - this.fontRendererObj.getStringWidth("This is a modified version of the free version on Augustus!") / 2, sr.getScaledHeight() / 4 - this.fontRendererObj.FONT_HEIGHT - 100, -1);
        this.fontRendererObj.drawString("So basically 80% of the code is made by E-Sound", sr.getScaledWidth() / 4 - this.fontRendererObj.getStringWidth("So basically 80% of the code is made by E-Sound") / 2, sr.getScaledHeight() / 4 - this.fontRendererObj.FONT_HEIGHT, -1);
        this.fontRendererObj.drawString("I hope i dont get in legal trouble because of this client", sr.getScaledWidth() / 4 - this.fontRendererObj.getStringWidth("I hope i dont get in legal trouble because of this client") / 2, sr.getScaledHeight() / 4 - this.fontRendererObj.FONT_HEIGHT + 100, -1);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 2) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1 && this.mc.theWorld == null) {
            this.mc.displayGuiScreen(this.parent);
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
}
