// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;

public class GuiSpectator extends Gui implements ISpectatorMenuRecipient
{
    private static final ResourceLocation WIDGETS;
    public static final ResourceLocation SPECTATOR_WIDGETS;
    private final Minecraft mc;
    private long lastSelectionTime;
    private SpectatorMenu menu;
    
    public GuiSpectator(final Minecraft mcIn) {
        this.mc = mcIn;
    }
    
    public void onHotbarSelected(final int p_175260_1_) {
        this.lastSelectionTime = Minecraft.getSystemTime();
        if (this.menu != null) {
            this.menu.selectSlot(p_175260_1_);
        }
        else {
            this.menu = new SpectatorMenu(this);
        }
    }
    
    private float getHotbarAlpha() {
        final long i = this.lastSelectionTime - Minecraft.getSystemTime() + 5000L;
        return MathHelper.clamp(i / 2000.0f, 0.0f, 1.0f);
    }
    
    public void renderTooltip(final ScaledResolution p_175264_1_, final float p_175264_2_) {
        if (this.menu != null) {
            final float f = this.getHotbarAlpha();
            if (f <= 0.0f) {
                this.menu.exit();
            }
            else {
                final int i = p_175264_1_.getScaledWidth() / 2;
                final float f2 = this.zLevel;
                this.zLevel = -90.0f;
                final float f3 = p_175264_1_.getScaledHeight() - 22.0f * f;
                final SpectatorDetails spectatordetails = this.menu.getCurrentPage();
                this.renderPage(p_175264_1_, f, i, f3, spectatordetails);
                this.zLevel = f2;
            }
        }
    }
    
    protected void renderPage(final ScaledResolution p_175258_1_, final float p_175258_2_, final int p_175258_3_, final float p_175258_4_, final SpectatorDetails p_175258_5_) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_175258_2_);
        this.mc.getTextureManager().bindTexture(GuiSpectator.WIDGETS);
        this.drawTexturedModalRect((float)(p_175258_3_ - 91), p_175258_4_, 0, 0, 182, 22);
        if (p_175258_5_.getSelectedSlot() >= 0) {
            this.drawTexturedModalRect((float)(p_175258_3_ - 91 - 1 + p_175258_5_.getSelectedSlot() * 20), p_175258_4_ - 1.0f, 0, 22, 24, 22);
        }
        RenderHelper.enableGUIStandardItemLighting();
        for (int i = 0; i < 9; ++i) {
            this.renderSlot(i, p_175258_1_.getScaledWidth() / 2 - 90 + i * 20 + 2, p_175258_4_ + 3.0f, p_175258_2_, p_175258_5_.getObject(i));
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }
    
    private void renderSlot(final int p_175266_1_, final int p_175266_2_, final float p_175266_3_, final float p_175266_4_, final ISpectatorMenuObject p_175266_5_) {
        this.mc.getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
        if (p_175266_5_ != SpectatorMenu.EMPTY_SLOT) {
            final int i = (int)(p_175266_4_ * 255.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_175266_2_, p_175266_3_, 0.0f);
            final float f = p_175266_5_.isEnabled() ? 1.0f : 0.25f;
            GlStateManager.color(f, f, f, p_175266_4_);
            p_175266_5_.renderIcon(f, i);
            GlStateManager.popMatrix();
            final String s = String.valueOf(GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[p_175266_1_].getKeyCode()));
            if (i > 3 && p_175266_5_.isEnabled()) {
                this.mc.fontRenderer.drawStringWithShadow(s, (float)(p_175266_2_ + 19 - 2 - this.mc.fontRenderer.getStringWidth(s)), p_175266_3_ + 6.0f + 3.0f, 16777215 + (i << 24));
            }
        }
    }
    
    public void renderSelectedItem(final ScaledResolution p_175263_1_) {
        final int i = (int)(this.getHotbarAlpha() * 255.0f);
        if (i > 3 && this.menu != null) {
            final ISpectatorMenuObject ispectatormenuobject = this.menu.getSelectedItem();
            final String s = (ispectatormenuobject == SpectatorMenu.EMPTY_SLOT) ? this.menu.getSelectedCategory().getPrompt().getFormattedText() : ispectatormenuobject.getSpectatorName().getFormattedText();
            if (s != null) {
                final int j = (p_175263_1_.getScaledWidth() - this.mc.fontRenderer.getStringWidth(s)) / 2;
                final int k = p_175263_1_.getScaledHeight() - 35;
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                this.mc.fontRenderer.drawStringWithShadow(s, (float)j, (float)k, 16777215 + (i << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
    
    @Override
    public void onSpectatorMenuClosed(final SpectatorMenu menu) {
        this.menu = null;
        this.lastSelectionTime = 0L;
    }
    
    public boolean isMenuActive() {
        return this.menu != null;
    }
    
    public void onMouseScroll(final int p_175259_1_) {
        int i;
        for (i = this.menu.getSelectedSlot() + p_175259_1_; i >= 0 && i <= 8 && (this.menu.getItem(i) == SpectatorMenu.EMPTY_SLOT || !this.menu.getItem(i).isEnabled()); i += p_175259_1_) {}
        if (i >= 0 && i <= 8) {
            this.menu.selectSlot(i);
            this.lastSelectionTime = Minecraft.getSystemTime();
        }
    }
    
    public void onMiddleClick() {
        this.lastSelectionTime = Minecraft.getSystemTime();
        if (this.isMenuActive()) {
            final int i = this.menu.getSelectedSlot();
            if (i != -1) {
                this.menu.selectSlot(i);
            }
        }
        else {
            this.menu = new SpectatorMenu(this);
        }
    }
    
    static {
        WIDGETS = new ResourceLocation("textures/gui/widgets.png");
        SPECTATOR_WIDGETS = new ResourceLocation("textures/gui/spectator_widgets.png");
    }
}
