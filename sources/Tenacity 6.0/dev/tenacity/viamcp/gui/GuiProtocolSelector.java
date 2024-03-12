// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import dev.tenacity.viamcp.protocols.ProtocolCollection;
import dev.tenacity.viamcp.ViaMCP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.client.gui.GuiMultiplayer;
import dev.tenacity.ui.mainmenu.CustomMainMenu;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiProtocolSelector extends GuiScreen
{
    private GuiScreen parent;
    public SlotList list;
    
    public GuiProtocolSelector(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 25, 200, 20, "Back"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 180, this.height - 25, 75, 20, "Credits"));
        this.list = new SlotList(GuiProtocolSelector.mc, this.width, this.height, 32, this.height - 32, 10);
    }
    
    @Override
    protected void actionPerformed(final GuiButton guiButton) {
        this.list.actionPerformed(guiButton);
        if (guiButton.id == 1) {
            GuiProtocolSelector.mc.displayGuiScreen(new GuiMultiplayer(new CustomMainMenu()));
        }
        if (guiButton.id == 2) {
            GuiProtocolSelector.mc.displayGuiScreen(new GuiCredits(this));
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        if (keyCode == 1) {
            GuiProtocolSelector.mc.displayGuiScreen(new GuiMultiplayer(new CustomMainMenu()));
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.list.handleMouseInput();
        super.handleMouseInput();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0, 2.0, 2.0);
        final String title = new StringBuilder().append(EnumChatFormatting.LIGHT_PURPLE).append(EnumChatFormatting.BOLD).append("ViaMCP Reborn").toString();
        this.drawString(this.fontRendererObj, title, (int)((this.width - this.fontRendererObj.getStringWidth(title) * 2.0f) / 4.0f), 5, -1);
        GlStateManager.popMatrix();
        final String versionName = ProtocolCollection.getProtocolById(ViaMCP.getInstance().getVersion()).getName();
        final String versionCodeName = ProtocolCollection.getProtocolInfoById(ViaMCP.getInstance().getVersion()).getName();
        final String versionReleaseDate = ProtocolCollection.getProtocolInfoById(ViaMCP.getInstance().getVersion()).getReleaseDate();
        final String versionTitle = "Version: " + versionName + " - " + versionCodeName;
        final String versionReleased = "Released: " + versionReleaseDate;
        final int fixedHeight = (5 + this.fontRendererObj.FONT_HEIGHT) * 2 + 2;
        this.drawString(this.fontRendererObj, new StringBuilder().append(EnumChatFormatting.GRAY).append(EnumChatFormatting.BOLD).append("Version Information").toString(), (int)((this.width - this.fontRendererObj.getStringWidth("Version Information")) / 2.0f), fixedHeight, -1);
        this.drawString(this.fontRendererObj, versionTitle, (int)((this.width - this.fontRendererObj.getStringWidth(versionTitle)) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT, -1);
        this.drawString(this.fontRendererObj, versionReleased, (int)((this.width - this.fontRendererObj.getStringWidth(versionReleased)) / 2.0f), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 2, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    class SlotList extends GuiSlot
    {
        public SlotList(final Minecraft mc, final int width, final int height, final int top, final int bottom, final int slotHeight) {
            super(mc, width, height, top + 30, bottom, 18);
        }
        
        @Override
        protected int getSize() {
            return ProtocolCollection.values().length;
        }
        
        @Override
        protected void elementClicked(final int i, final boolean b, final int i1, final int i2) {
            final int protocolVersion = ProtocolCollection.values()[i].getVersion().getVersion();
            ViaMCP.getInstance().setVersion(protocolVersion);
            ViaMCP.getInstance().asyncSlider.setVersion(protocolVersion);
        }
        
        @Override
        protected boolean isSelected(final int i) {
            return false;
        }
        
        @Override
        protected void drawBackground() {
            GuiProtocolSelector.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawSlot(final int i, final int i1, final int i2, final int i3, final int i4, final int i5) {
            GuiProtocolSelector.this.drawCenteredString(this.mc.fontRendererObj, ((ViaMCP.getInstance().getVersion() == ProtocolCollection.values()[i].getVersion().getVersion()) ? (EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD) : EnumChatFormatting.GRAY.toString()) + ProtocolCollection.getProtocolById(ProtocolCollection.values()[i].getVersion().getVersion()).getName(), this.width / 2, i2 + 2, -1);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GuiProtocolSelector.this.drawCenteredString(this.mc.fontRendererObj, "PVN: " + ProtocolCollection.getProtocolById(ProtocolCollection.values()[i].getVersion().getVersion()).getVersion(), this.width, (i2 + 2) * 2 + 20, -1);
            GlStateManager.popMatrix();
        }
    }
}
