// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui.augustusmanager;

import java.awt.Color;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import viamcp.gui.GuiProtocolSelector;
import net.minecraft.client.gui.GuiButton;
import net.augustus.ui.widgets.CustomButton;
import net.augustus.Augustus;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;

public class AugustusOptions extends GuiScreen
{
    private GuiScreen parent;
    private final AugustusBackgrounds augustusBackgrounds;
    private final AugustusSounds augustusSounds;
    private final AugustusProxy augustusProxy;
    
    public AugustusOptions(final GuiScreen parent) {
        this.parent = parent;
        this.augustusBackgrounds = new AugustusBackgrounds(this);
        this.augustusSounds = new AugustusSounds(this);
        this.augustusProxy = new AugustusProxy(this);
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
        final int startHeight = Math.min(40 + scaledHeight / 7, 135);
        this.buttonList.add(new CustomButton(1, scaledWidth / 2 - 100, startHeight, 200, 20, "Backgrounds", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(2, scaledWidth / 2 - 100, scaledHeight - scaledHeight / 10, 200, 20, "Back", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(3, scaledWidth / 2 - 100, startHeight + 30, 200, 20, "SoundOptions", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(4, scaledWidth / 2 - 100, startHeight + 60, 200, 20, "ProxyManager", Augustus.getInstance().getClientColor()));
        this.buttonList.add(new CustomButton(5, scaledWidth / 2 - 100, startHeight + 90, 200, 20, "ViaMCP", Augustus.getInstance().getClientColor()));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiProtocolSelector(this));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.augustusBackgrounds.start(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(this.parent);
        }
        if (button.id == 3) {
            this.mc.displayGuiScreen(this.augustusSounds.start(this));
        }
        if (button.id == 4) {
            this.mc.displayGuiScreen(this.augustusProxy.start(this));
        }
        super.actionPerformed(button);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawBackground(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        GL11.glScaled(2.0, 2.0, 1.0);
        this.fontRendererObj.drawStringWithShadow("Augustus Manager", sr.getScaledWidth() / 4.0f - this.fontRendererObj.getStringWidth("Augustus Manager") / 2.0f, 10.0f, Color.lightGray.getRGB());
        GL11.glScaled(1.0, 1.0, 1.0);
    }
}
