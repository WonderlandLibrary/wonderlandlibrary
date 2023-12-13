/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.viamcp.viamcp.gui;

import java.awt.Color;
import java.io.IOException;
import me.wintware.client.utils.visual.RenderUtil;
import me.wintware.client.viamcp.viafabric.ViaFabric;
import me.wintware.client.viamcp.viafabric.util.ProtocolUtils;
import me.wintware.client.viamcp.viamcp.utils.ProtocolSorter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

public class GuiProtocolSelector
extends GuiScreen {
    public SlotList list;
    private final GuiScreen parent;

    public GuiProtocolSelector(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height - 27, 200, 20, "Back"));
        this.list = new SlotList(this.mc, width, height, 32, height - 32, 10);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.list.actionPerformed(button);
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        this.list.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int drawScreen, int mouseX, float mouseY) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtil.drawGradientSideways(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 226, 255).getRGB(), new Color(180, 0, 255).getRGB());
        this.list.drawScreend(drawScreen, mouseX, mouseY);
        this.mc.net.drawCenteredString(TextFormatting.BOLD.toString() + "WintMcp", width / 2, 6.0f, 0xFFFFFF);
        super.drawScreen(drawScreen, mouseX, mouseY);
    }

    class SlotList
    extends GuiSlot {
        public SlotList(Minecraft p_i1052_1_, int p_i1052_2_, int p_i1052_3_, int p_i1052_4_, int p_i1052_5_, int p_i1052_6_) {
            super(p_i1052_1_, p_i1052_2_, p_i1052_3_, p_i1052_4_, p_i1052_5_, p_i1052_6_);
        }

        @Override
        protected int getSize() {
            return ProtocolSorter.getProtocolVersions().size();
        }

        @Override
        protected void elementClicked(int i, boolean b, int i1, int i2) {
            ViaFabric.clientSideVersion = ProtocolSorter.getProtocolVersions().get(i).getVersion();
        }

        @Override
        protected boolean isSelected(int i) {
            return false;
        }

        @Override
        protected void drawBackground() {
            ScaledResolution sr = new ScaledResolution(this.mc);
            RenderUtil.drawGradientSideways(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 226, 255).getRGB(), new Color(180, 0, 255).getRGB());
        }

        @Override
        protected void drawSlot(int i, int i1, int i2, int i3, int i4, int p_192637_6_, float p_192637_7_) {
            this.mc.fontRenderer.drawCenteredString((ViaFabric.clientSideVersion == ProtocolSorter.getProtocolVersions().get(i).getVersion() ? TextFormatting.GREEN.toString() : TextFormatting.WHITE.toString()) + ProtocolUtils.getProtocolName(ProtocolSorter.getProtocolVersions().get(i).getVersion()), this.width / 2, i2 + 2, -1);
        }
    }
}

