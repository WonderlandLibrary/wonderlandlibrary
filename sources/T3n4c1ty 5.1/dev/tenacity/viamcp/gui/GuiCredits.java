package dev.tenacity.viamcp.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;

public class GuiCredits extends GuiScreen {
    private final GuiScreen parent;

    public GuiCredits(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 25, 200, 20, "Back"));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.id == 1) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0, 2.0, 2.0);
        String title = EnumChatFormatting.LIGHT_PURPLE + (EnumChatFormatting.BOLD + "Credits");
        drawString(this.fontRendererObj, title, (int) ((this.width - (this.fontRendererObj.getStringWidth(title) * 2)) / 4), 5, -1);
        GlStateManager.popMatrix();

        int fixedHeight = ((5 + this.fontRendererObj.FONT_HEIGHT) * 2) + 2;

        String viaVerTeam = EnumChatFormatting.GRAY + (EnumChatFormatting.BOLD + "ViaVersion Team");
        String florMich = EnumChatFormatting.GRAY + (EnumChatFormatting.BOLD + "FlorianMichael");
        String laVache = EnumChatFormatting.GRAY + (EnumChatFormatting.BOLD + "LaVache-FR");
        String hideri = EnumChatFormatting.GRAY + (EnumChatFormatting.BOLD + "Hiderichan / Foreheadchan");

        drawString(this.fontRendererObj, viaVerTeam, (int) ((this.width - this.fontRendererObj.getStringWidth(viaVerTeam)) / 2), fixedHeight, -1);
        drawString(this.fontRendererObj, "ViaVersion", (int) ((this.width - this.fontRendererObj.getStringWidth("ViaVersion")) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT, -1);
        drawString(this.fontRendererObj, "ViaBackwards", (int) ((this.width - this.fontRendererObj.getStringWidth("ViaBackwards")) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 2, -1);
        drawString(this.fontRendererObj, "ViaRewind", (int) ((this.width - this.fontRendererObj.getStringWidth("ViaRewind")) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 3, -1);

        drawString(this.fontRendererObj, florMich, (int) ((this.width - this.fontRendererObj.getStringWidth(florMich)) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 5, -1);
        drawString(this.fontRendererObj, "ViaForge", (int) ((this.width - this.fontRendererObj.getStringWidth("ViaForge")) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 6, -1);

        drawString(this.fontRendererObj, laVache, (int) ((this.width - this.fontRendererObj.getStringWidth(laVache)) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 8, -1);
        drawString(this.fontRendererObj, "Original ViaMCP", (int) ((this.width - this.fontRendererObj.getStringWidth("Original ViaMCP")) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 9, -1);

        drawString(this.fontRendererObj, hideri, (int) ((this.width - this.fontRendererObj.getStringWidth(hideri)) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 11, -1);
        drawString(this.fontRendererObj, "ViaMCP Reborn", (int) ((this.width - this.fontRendererObj.getStringWidth("ViaMCP Reborn")) / 2), fixedHeight + this.fontRendererObj.FONT_HEIGHT * 12, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
