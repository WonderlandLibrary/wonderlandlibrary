// 
// Decompiled by Procyon v0.5.36
// 

package viamcp.gui;

import net.minecraft.client.renderer.GlStateManager;
import viamcp.ViaMCP;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import net.minecraft.util.MathHelper;
import viamcp.protocols.ProtocolCollection;
import net.minecraft.client.gui.GuiButton;

public class VersionSlider extends GuiButton
{
    private final ProtocolCollection[] values;
    private float sliderValue;
    public boolean dragging;
    
    public VersionSlider(final int buttonId, final int x, final int y, final int widthIn, final int heightIn) {
        super(buttonId, x, y, MathHelper.clamp_int(widthIn, 110, Integer.MAX_VALUE), heightIn, "");
        this.values = ProtocolCollection.values();
        Collections.reverse(Arrays.asList(this.values));
        this.sliderValue = ((GuiProtocolSelector.sliderDragValue != -1.0f) ? GuiProtocolSelector.sliderDragValue : 0.0f);
        this.displayString = ((GuiProtocolSelector.sliderDragValue != -1.0f) ? ("Version: " + this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getName()) : "Drag for Version");
    }
    
    @Override
    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }
    
    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0f, 1.0f);
                GuiProtocolSelector.sliderDragValue = this.sliderValue;
                this.displayString = "Version: " + this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getName();
                ViaMCP.getInstance().setVersion(this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getVersion());
            }
            mc.getTextureManager().bindTexture(VersionSlider.buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0f, 1.0f);
            GuiProtocolSelector.sliderDragValue = this.sliderValue;
            this.displayString = "Version: " + this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getName();
            ViaMCP.getInstance().setVersion(this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getVersion());
            return this.dragging = true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.dragging = false;
    }
}
