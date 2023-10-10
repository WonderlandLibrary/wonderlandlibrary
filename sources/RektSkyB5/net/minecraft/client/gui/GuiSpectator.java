/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiSpectator
extends Gui
implements ISpectatorMenuRecipient {
    private static final ResourceLocation field_175267_f = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation field_175269_a = new ResourceLocation("textures/gui/spectator_widgets.png");
    private final Minecraft field_175268_g;
    private long field_175270_h;
    private SpectatorMenu field_175271_i;

    public GuiSpectator(Minecraft mcIn) {
        this.field_175268_g = mcIn;
    }

    public void func_175260_a(int p_175260_1_) {
        this.field_175270_h = Minecraft.getSystemTime();
        if (this.field_175271_i != null) {
            this.field_175271_i.func_178644_b(p_175260_1_);
        } else {
            this.field_175271_i = new SpectatorMenu(this);
        }
    }

    private float func_175265_c() {
        long i2 = this.field_175270_h - Minecraft.getSystemTime() + 5000L;
        return MathHelper.clamp_float((float)i2 / 2000.0f, 0.0f, 1.0f);
    }

    public void renderTooltip(ScaledResolution p_175264_1_, float p_175264_2_) {
        if (this.field_175271_i != null) {
            float f2 = this.func_175265_c();
            if (f2 <= 0.0f) {
                this.field_175271_i.func_178641_d();
            } else {
                int i2 = p_175264_1_.getScaledWidth() / 2;
                float f1 = this.zLevel;
                this.zLevel = -90.0f;
                float f22 = (float)p_175264_1_.getScaledHeight() - 22.0f * f2;
                SpectatorDetails spectatordetails = this.field_175271_i.func_178646_f();
                this.func_175258_a(p_175264_1_, f2, i2, f22, spectatordetails);
                this.zLevel = f1;
            }
        }
    }

    protected void func_175258_a(ScaledResolution p_175258_1_, float p_175258_2_, int p_175258_3_, float p_175258_4_, SpectatorDetails p_175258_5_) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_175258_2_);
        this.field_175268_g.getTextureManager().bindTexture(field_175267_f);
        this.drawTexturedModalRect((float)(p_175258_3_ - 91), p_175258_4_, 0, 0, 182, 22);
        if (p_175258_5_.func_178681_b() >= 0) {
            this.drawTexturedModalRect((float)(p_175258_3_ - 91 - 1 + p_175258_5_.func_178681_b() * 20), p_175258_4_ - 1.0f, 0, 22, 24, 22);
        }
        RenderHelper.enableGUIStandardItemLighting();
        for (int i2 = 0; i2 < 9; ++i2) {
            this.func_175266_a(i2, p_175258_1_.getScaledWidth() / 2 - 90 + i2 * 20 + 2, p_175258_4_ + 3.0f, p_175258_2_, p_175258_5_.func_178680_a(i2));
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }

    private void func_175266_a(int p_175266_1_, int p_175266_2_, float p_175266_3_, float p_175266_4_, ISpectatorMenuObject p_175266_5_) {
        this.field_175268_g.getTextureManager().bindTexture(field_175269_a);
        if (p_175266_5_ != SpectatorMenu.field_178657_a) {
            int i2 = (int)(p_175266_4_ * 255.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(p_175266_2_, p_175266_3_, 0.0f);
            float f2 = p_175266_5_.func_178662_A_() ? 1.0f : 0.25f;
            GlStateManager.color(f2, f2, f2, p_175266_4_);
            p_175266_5_.func_178663_a(f2, i2);
            GlStateManager.popMatrix();
            String s2 = String.valueOf(GameSettings.getKeyDisplayString(this.field_175268_g.gameSettings.keyBindsHotbar[p_175266_1_].getKeyCode()));
            if (i2 > 3 && p_175266_5_.func_178662_A_()) {
                this.field_175268_g.fontRendererObj.drawStringWithShadow(s2, p_175266_2_ + 19 - 2 - this.field_175268_g.fontRendererObj.getStringWidth(s2), p_175266_3_ + 6.0f + 3.0f, 0xFFFFFF + (i2 << 24));
            }
        }
    }

    public void func_175263_a(ScaledResolution p_175263_1_) {
        int i2 = (int)(this.func_175265_c() * 255.0f);
        if (i2 > 3 && this.field_175271_i != null) {
            String s2;
            ISpectatorMenuObject ispectatormenuobject = this.field_175271_i.func_178645_b();
            String string = s2 = ispectatormenuobject != SpectatorMenu.field_178657_a ? ispectatormenuobject.getSpectatorName().getFormattedText() : this.field_175271_i.func_178650_c().func_178670_b().getFormattedText();
            if (s2 != null) {
                int j2 = (p_175263_1_.getScaledWidth() - this.field_175268_g.fontRendererObj.getStringWidth(s2)) / 2;
                int k2 = p_175263_1_.getScaledHeight() - 35;
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.field_175268_g.fontRendererObj.drawStringWithShadow(s2, j2, k2, 0xFFFFFF + (i2 << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public void func_175257_a(SpectatorMenu p_175257_1_) {
        this.field_175271_i = null;
        this.field_175270_h = 0L;
    }

    public boolean func_175262_a() {
        return this.field_175271_i != null;
    }

    public void func_175259_b(int p_175259_1_) {
        int i2;
        for (i2 = this.field_175271_i.func_178648_e() + p_175259_1_; !(i2 < 0 || i2 > 8 || this.field_175271_i.func_178643_a(i2) != SpectatorMenu.field_178657_a && this.field_175271_i.func_178643_a(i2).func_178662_A_()); i2 += p_175259_1_) {
        }
        if (i2 >= 0 && i2 <= 8) {
            this.field_175271_i.func_178644_b(i2);
            this.field_175270_h = Minecraft.getSystemTime();
        }
    }

    public void func_175261_b() {
        this.field_175270_h = Minecraft.getSystemTime();
        if (this.func_175262_a()) {
            int i2 = this.field_175271_i.func_178648_e();
            if (i2 != -1) {
                this.field_175271_i.func_178644_b(i2);
            }
        } else {
            this.field_175271_i = new SpectatorMenu(this);
        }
    }
}

