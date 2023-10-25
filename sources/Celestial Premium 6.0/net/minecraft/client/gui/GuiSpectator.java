/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class GuiSpectator
extends Gui
implements ISpectatorMenuRecipient {
    private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation SPECTATOR_WIDGETS = new ResourceLocation("textures/gui/spectator_widgets.png");
    private final Minecraft mc;
    private long lastSelectionTime;
    private SpectatorMenu menu;

    public GuiSpectator(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void onHotbarSelected(int p_175260_1_) {
        this.lastSelectionTime = Minecraft.getSystemTime();
        if (this.menu != null) {
            this.menu.selectSlot(p_175260_1_);
        } else {
            this.menu = new SpectatorMenu(this);
        }
    }

    private float getHotbarAlpha() {
        long i = this.lastSelectionTime - Minecraft.getSystemTime() + 5000L;
        return MathHelper.clamp((float)i / 2000.0f, 0.0f, 1.0f);
    }

    public void renderTooltip(ScaledResolution p_175264_1_, float p_175264_2_) {
        if (this.menu != null) {
            float f = this.getHotbarAlpha();
            if (f <= 0.0f) {
                this.menu.exit();
            } else {
                int i = p_175264_1_.getScaledWidth() / 2;
                float f1 = this.zLevel;
                this.zLevel = -90.0f;
                float f2 = (float)p_175264_1_.getScaledHeight() - 22.0f * f;
                SpectatorDetails spectatordetails = this.menu.getCurrentPage();
                this.renderPage(p_175264_1_, f, i, f2, spectatordetails);
                this.zLevel = f1;
            }
        }
    }

    protected void renderPage(ScaledResolution p_175258_1_, float p_175258_2_, int p_175258_3_, float p_175258_4_, SpectatorDetails p_175258_5_) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_175258_2_);
        this.mc.getTextureManager().bindTexture(WIDGETS);
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

    private void renderSlot(int p_175266_1_, int p_175266_2_, float p_175266_3_, float p_175266_4_, ISpectatorMenuObject p_175266_5_) {
        this.mc.getTextureManager().bindTexture(SPECTATOR_WIDGETS);
        if (p_175266_5_ != SpectatorMenu.EMPTY_SLOT) {
            int i = (int)(p_175266_4_ * 255.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(p_175266_2_, p_175266_3_, 0.0f);
            float f = p_175266_5_.isEnabled() ? 1.0f : 0.25f;
            GlStateManager.color(f, f, f, p_175266_4_);
            p_175266_5_.renderIcon(f, i);
            GlStateManager.popMatrix();
            String s = String.valueOf(GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[p_175266_1_].getKeyCode()));
            if (i > 3 && p_175266_5_.isEnabled()) {
                this.mc.fontRendererObj.drawStringWithShadow(s, p_175266_2_ + 19 - 2 - this.mc.fontRendererObj.getStringWidth(s), p_175266_3_ + 6.0f + 3.0f, 0xFFFFFF + (i << 24));
            }
        }
    }

    public void renderSelectedItem(ScaledResolution p_175263_1_) {
        int i = (int)(this.getHotbarAlpha() * 255.0f);
        if (i > 3 && this.menu != null) {
            String s;
            ISpectatorMenuObject ispectatormenuobject = this.menu.getSelectedItem();
            String string = s = ispectatormenuobject == SpectatorMenu.EMPTY_SLOT ? this.menu.getSelectedCategory().getPrompt().getFormattedText() : ispectatormenuobject.getSpectatorName().getFormattedText();
            if (s != null) {
                int j = (p_175263_1_.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(s)) / 2;
                int k = p_175263_1_.getScaledHeight() - 35;
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                this.mc.fontRendererObj.drawStringWithShadow(s, j, k, 0xFFFFFF + (i << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public void onSpectatorMenuClosed(SpectatorMenu p_175257_1_) {
        this.menu = null;
        this.lastSelectionTime = 0L;
    }

    public boolean isMenuActive() {
        return this.menu != null;
    }

    public void onMouseScroll(int p_175259_1_) {
        int i;
        for (i = this.menu.getSelectedSlot() + p_175259_1_; !(i < 0 || i > 8 || this.menu.getItem(i) != SpectatorMenu.EMPTY_SLOT && this.menu.getItem(i).isEnabled()); i += p_175259_1_) {
        }
        if (i >= 0 && i <= 8) {
            this.menu.selectSlot(i);
            this.lastSelectionTime = Minecraft.getSystemTime();
        }
    }

    public void onMiddleClick() {
        this.lastSelectionTime = Minecraft.getSystemTime();
        if (this.isMenuActive()) {
            int i = this.menu.getSelectedSlot();
            if (i != -1) {
                this.menu.selectSlot(i);
            }
        } else {
            this.menu = new SpectatorMenu(this);
        }
    }
}

