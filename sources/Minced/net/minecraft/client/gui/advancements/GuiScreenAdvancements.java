// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.advancements;

import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenAdvancements extends GuiScreen implements ClientAdvancementManager.IListener
{
    private static final ResourceLocation WINDOW;
    private static final ResourceLocation TABS;
    private final ClientAdvancementManager clientAdvancementManager;
    private final Map<Advancement, GuiAdvancementTab> tabs;
    private GuiAdvancementTab selectedTab;
    private int scrollMouseX;
    private int scrollMouseY;
    private boolean isScrolling;
    
    public GuiScreenAdvancements(final ClientAdvancementManager p_i47383_1_) {
        this.tabs = (Map<Advancement, GuiAdvancementTab>)Maps.newLinkedHashMap();
        this.clientAdvancementManager = p_i47383_1_;
    }
    
    @Override
    public void initGui() {
        this.tabs.clear();
        this.selectedTab = null;
        this.clientAdvancementManager.setListener(this);
        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            this.clientAdvancementManager.setSelectedTab(this.tabs.values().iterator().next().getAdvancement(), true);
        }
        else {
            this.clientAdvancementManager.setSelectedTab((this.selectedTab == null) ? null : this.selectedTab.getAdvancement(), true);
        }
    }
    
    @Override
    public void onGuiClosed() {
        this.clientAdvancementManager.setListener(null);
        final NetHandlerPlayClient nethandlerplayclient = GuiScreenAdvancements.mc.getConnection();
        if (nethandlerplayclient != null) {
            nethandlerplayclient.sendPacket(CPacketSeenAdvancements.closedScreen());
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final int i = (this.width - 252) / 2;
            final int j = (this.height - 140) / 2;
            for (final GuiAdvancementTab guiadvancementtab : this.tabs.values()) {
                if (guiadvancementtab.isMouseOver(i, j, mouseX, mouseY)) {
                    this.clientAdvancementManager.setSelectedTab(guiadvancementtab.getAdvancement(), true);
                    break;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == GuiScreenAdvancements.mc.gameSettings.keyBindAdvancements.getKeyCode()) {
            GuiScreenAdvancements.mc.displayGuiScreen(null);
            GuiScreenAdvancements.mc.setIngameFocus();
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int i = (this.width - 252) / 2;
        final int j = (this.height - 140) / 2;
        if (Mouse.isButtonDown(0)) {
            if (!this.isScrolling) {
                this.isScrolling = true;
            }
            else if (this.selectedTab != null) {
                this.selectedTab.scroll(mouseX - this.scrollMouseX, mouseY - this.scrollMouseY);
            }
            this.scrollMouseX = mouseX;
            this.scrollMouseY = mouseY;
        }
        else {
            this.isScrolling = false;
        }
        this.drawDefaultBackground();
        this.renderInside(mouseX, mouseY, i, j);
        this.renderWindow(i, j);
        this.renderToolTips(mouseX, mouseY, i, j);
    }
    
    private void renderInside(final int p_191936_1_, final int p_191936_2_, final int p_191936_3_, final int p_191936_4_) {
        final GuiAdvancementTab guiadvancementtab = this.selectedTab;
        if (guiadvancementtab == null) {
            Gui.drawRect((float)(p_191936_3_ + 9), (float)(p_191936_4_ + 18), (float)(p_191936_3_ + 9 + 234), (float)(p_191936_4_ + 18 + 113), -16777216);
            final String s = I18n.format("advancements.empty", new Object[0]);
            final int i = this.fontRenderer.getStringWidth(s);
            this.fontRenderer.drawString(s, p_191936_3_ + 9 + 117 - i / 2, p_191936_4_ + 18 + 56 - this.fontRenderer.FONT_HEIGHT / 2, -1);
            this.fontRenderer.drawString(":(", p_191936_3_ + 9 + 117 - this.fontRenderer.getStringWidth(":(") / 2, p_191936_4_ + 18 + 113 - this.fontRenderer.FONT_HEIGHT, -1);
        }
        else {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(p_191936_3_ + 9), (float)(p_191936_4_ + 18), -400.0f);
            GlStateManager.enableDepth();
            guiadvancementtab.drawContents();
            GlStateManager.popMatrix();
            GlStateManager.depthFunc(515);
            GlStateManager.disableDepth();
        }
    }
    
    public void renderWindow(final int p_191934_1_, final int p_191934_2_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();
        GuiScreenAdvancements.mc.getTextureManager().bindTexture(GuiScreenAdvancements.WINDOW);
        this.drawTexturedModalRect(p_191934_1_, p_191934_2_, 0, 0, 252, 140);
        if (this.tabs.size() > 1) {
            GuiScreenAdvancements.mc.getTextureManager().bindTexture(GuiScreenAdvancements.TABS);
            for (final GuiAdvancementTab guiadvancementtab : this.tabs.values()) {
                guiadvancementtab.drawTab(p_191934_1_, p_191934_2_, guiadvancementtab == this.selectedTab);
            }
            GlStateManager.enableRescaleNormal();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();
            for (final GuiAdvancementTab guiadvancementtab2 : this.tabs.values()) {
                guiadvancementtab2.drawIcon(p_191934_1_, p_191934_2_, this.itemRender);
            }
            GlStateManager.disableBlend();
        }
        this.fontRenderer.drawString(I18n.format("gui.advancements", new Object[0]), p_191934_1_ + 8, p_191934_2_ + 6, 4210752);
    }
    
    private void renderToolTips(final int p_191937_1_, final int p_191937_2_, final int p_191937_3_, final int p_191937_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.selectedTab != null) {
            GlStateManager.pushMatrix();
            GlStateManager.enableDepth();
            GlStateManager.translate((float)(p_191937_3_ + 9), (float)(p_191937_4_ + 18), 400.0f);
            this.selectedTab.drawToolTips(p_191937_1_ - p_191937_3_ - 9, p_191937_2_ - p_191937_4_ - 18, p_191937_3_, p_191937_4_);
            GlStateManager.disableDepth();
            GlStateManager.popMatrix();
        }
        if (this.tabs.size() > 1) {
            for (final GuiAdvancementTab guiadvancementtab : this.tabs.values()) {
                if (guiadvancementtab.isMouseOver(p_191937_3_, p_191937_4_, p_191937_1_, p_191937_2_)) {
                    this.drawHoveringText(guiadvancementtab.getTitle(), p_191937_1_, p_191937_2_);
                }
            }
        }
    }
    
    @Override
    public void rootAdvancementAdded(final Advancement advancementIn) {
        final GuiAdvancementTab guiadvancementtab = GuiAdvancementTab.create(GuiScreenAdvancements.mc, this, this.tabs.size(), advancementIn);
        if (guiadvancementtab != null) {
            this.tabs.put(advancementIn, guiadvancementtab);
        }
    }
    
    @Override
    public void rootAdvancementRemoved(final Advancement advancementIn) {
    }
    
    @Override
    public void nonRootAdvancementAdded(final Advancement advancementIn) {
        final GuiAdvancementTab guiadvancementtab = this.getTab(advancementIn);
        if (guiadvancementtab != null) {
            guiadvancementtab.addAdvancement(advancementIn);
        }
    }
    
    @Override
    public void nonRootAdvancementRemoved(final Advancement advancementIn) {
    }
    
    @Override
    public void onUpdateAdvancementProgress(final Advancement advancementIn, final AdvancementProgress progress) {
        final GuiAdvancement guiadvancement = this.getAdvancementGui(advancementIn);
        if (guiadvancement != null) {
            guiadvancement.setAdvancementProgress(progress);
        }
    }
    
    @Override
    public void setSelectedTab(@Nullable final Advancement advancementIn) {
        this.selectedTab = this.tabs.get(advancementIn);
    }
    
    @Override
    public void advancementsCleared() {
        this.tabs.clear();
        this.selectedTab = null;
    }
    
    @Nullable
    public GuiAdvancement getAdvancementGui(final Advancement p_191938_1_) {
        final GuiAdvancementTab guiadvancementtab = this.getTab(p_191938_1_);
        return (guiadvancementtab == null) ? null : guiadvancementtab.getAdvancementGui(p_191938_1_);
    }
    
    @Nullable
    private GuiAdvancementTab getTab(Advancement p_191935_1_) {
        while (p_191935_1_.getParent() != null) {
            p_191935_1_ = p_191935_1_.getParent();
        }
        return this.tabs.get(p_191935_1_);
    }
    
    static {
        WINDOW = new ResourceLocation("textures/gui/advancements/window.png");
        TABS = new ResourceLocation("textures/gui/advancements/tabs.png");
    }
}
