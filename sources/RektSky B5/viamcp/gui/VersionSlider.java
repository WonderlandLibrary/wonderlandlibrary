/*
 * Decompiled with CFR 0.152.
 */
package viamcp.gui;

import java.util.Arrays;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import viamcp.ViaMCP;
import viamcp.gui.GuiProtocolSelector;
import viamcp.protocols.ProtocolCollection;

public class VersionSlider
extends GuiButton {
    private final ProtocolCollection[] values = ProtocolCollection.values();
    private float sliderValue;
    public boolean dragging;

    public VersionSlider(int buttonId, int x2, int y2, int widthIn, int heightIn) {
        super(buttonId, x2, y2, MathHelper.clamp_int(widthIn, 110, Integer.MAX_VALUE), heightIn, "");
        Collections.reverse(Arrays.asList(this.values));
        this.sliderValue = GuiProtocolSelector.sliderDragValue != -1.0f ? GuiProtocolSelector.sliderDragValue : 0.0f;
        this.displayString = GuiProtocolSelector.sliderDragValue != -1.0f ? "Version: " + this.values[(int)(this.sliderValue * (float)(this.values.length - 1))].getVersion().getName() : "Drag for Version";
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                GuiProtocolSelector.sliderDragValue = this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0f, 1.0f);
                this.displayString = "Version: " + this.values[(int)(this.sliderValue * (float)(this.values.length - 1))].getVersion().getName();
                ViaMCP.getInstance().setVersion(this.values[(int)(this.sliderValue * (float)(this.values.length - 1))].getVersion().getVersion());
            }
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            GuiProtocolSelector.sliderDragValue = this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0f, 1.0f);
            this.displayString = "Version: " + this.values[(int)(this.sliderValue * (float)(this.values.length - 1))].getVersion().getName();
            ViaMCP.getInstance().setVersion(this.values[(int)(this.sliderValue * (float)(this.values.length - 1))].getVersion().getVersion());
            this.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }
}

