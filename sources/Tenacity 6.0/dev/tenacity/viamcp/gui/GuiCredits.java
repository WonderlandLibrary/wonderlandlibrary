// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.gui;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiCredits extends GuiScreen
{
    private final GuiScreen parent;
    
    public GuiCredits(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 25, 200, 20, "Back"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton guiButton) {
        if (guiButton.id == 1) {
            GuiCredits.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0, 2.0, 2.0);
        final String title = new StringBuilder().append(EnumChatFormatting.LIGHT_PURPLE).append(EnumChatFormatting.BOLD).append("Credits").toString();
        this.drawString(this.fontRendererObj, title, (int)((this.width - this.fontRendererObj.getStringWidth(title) * 2.0f) / 4.0f), 5, -1);
        GlStateManager.popMatrix();
        final int fixedHeight = (5 + this.fontRendererObj.FONT_HEIGHT) * 2 + 2;
        final String viaVerTeam = new StringBuilder().append(EnumChatFormatting.GRAY).append(EnumChatFormatting.BOLD).append("ViaVersion Team").toString();
        final String florMich = new StringBuilder().append(EnumChatFormatting.GRAY).append(EnumChatFormatting.BOLD).append("FlorianMichael").toString();
        final String laVache = new StringBuilder().append(EnumChatFormatting.GRAY).append(EnumChatFormatting.BOLD).append("LaVache-FR").toString();
        final String hideri = new StringBuilder().append(EnumChatFormatting.GRAY).append(EnumChatFormatting.BOLD).append("Hiderichan / Foreheadchan").toString();
        this.drawString(this.fontRendererObj, viaVerTeam, (int)((this.width - this.fontRendererObj.getStringWidth(viaVerTeam)) / 2.0f), fixedHeight, -1);
        this.drawString(this.fontRendererObj, "ViaVersion", (int)((this.width - this.fontRendererObj.getStringWidth("ViaVersion")) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT, -1);
        this.drawString(this.fontRendererObj, "ViaBackwards", (int)((this.width - this.fontRendererObj.getStringWidth("ViaBackwards")) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 2, -1);
        this.drawString(this.fontRendererObj, "ViaRewind", (int)((this.width - this.fontRendererObj.getStringWidth("ViaRewind")) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 3, -1);
        this.drawString(this.fontRendererObj, florMich, (int)((this.width - this.fontRendererObj.getStringWidth(florMich)) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 5, -1);
        this.drawString(this.fontRendererObj, "ViaForge", (int)((this.width - this.fontRendererObj.getStringWidth("ViaForge")) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 6, -1);
        this.drawString(this.fontRendererObj, laVache, (int)((this.width - this.fontRendererObj.getStringWidth(laVache)) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 8, -1);
        this.drawString(this.fontRendererObj, "Original ViaMCP", (int)((this.width - this.fontRendererObj.getStringWidth("Original ViaMCP")) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 9, -1);
        this.drawString(this.fontRendererObj, hideri, (int)((this.width - this.fontRendererObj.getStringWidth(hideri)) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 11, -1);
        this.drawString(this.fontRendererObj, "ViaMCP Reborn", (int)((this.width - this.fontRendererObj.getStringWidth("ViaMCP Reborn")) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 12, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
