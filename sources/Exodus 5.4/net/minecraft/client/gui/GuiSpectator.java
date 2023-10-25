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
    private final Minecraft field_175268_g;
    private static final ResourceLocation field_175267_f = new ResourceLocation("textures/gui/widgets.png");
    private long field_175270_h;
    public static final ResourceLocation field_175269_a = new ResourceLocation("textures/gui/spectator_widgets.png");
    private SpectatorMenu field_175271_i;

    public void func_175263_a(ScaledResolution scaledResolution) {
        int n = (int)(this.func_175265_c() * 255.0f);
        if (n > 3 && this.field_175271_i != null) {
            String string;
            ISpectatorMenuObject iSpectatorMenuObject = this.field_175271_i.func_178645_b();
            String string2 = string = iSpectatorMenuObject != SpectatorMenu.field_178657_a ? iSpectatorMenuObject.getSpectatorName().getFormattedText() : this.field_175271_i.func_178650_c().func_178670_b().getFormattedText();
            if (string != null) {
                int n2 = (scaledResolution.getScaledWidth() - Minecraft.fontRendererObj.getStringWidth(string)) / 2;
                int n3 = scaledResolution.getScaledHeight() - 35;
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Minecraft.fontRendererObj.drawStringWithShadow(string, n2, n3, 0xFFFFFF + (n << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    public GuiSpectator(Minecraft minecraft) {
        this.field_175268_g = minecraft;
    }

    public void renderTooltip(ScaledResolution scaledResolution, float f) {
        if (this.field_175271_i != null) {
            float f2 = this.func_175265_c();
            if (f2 <= 0.0f) {
                this.field_175271_i.func_178641_d();
            } else {
                int n = scaledResolution.getScaledWidth() / 2;
                float f3 = zLevel;
                zLevel = -90.0f;
                float f4 = (float)scaledResolution.getScaledHeight() - 22.0f * f2;
                SpectatorDetails spectatorDetails = this.field_175271_i.func_178646_f();
                this.func_175258_a(scaledResolution, f2, n, f4, spectatorDetails);
                zLevel = f3;
            }
        }
    }

    public void func_175261_b() {
        this.field_175270_h = Minecraft.getSystemTime();
        if (this.func_175262_a()) {
            int n = this.field_175271_i.func_178648_e();
            if (n != -1) {
                this.field_175271_i.func_178644_b(n);
            }
        } else {
            this.field_175271_i = new SpectatorMenu(this);
        }
    }

    protected void func_175258_a(ScaledResolution scaledResolution, float f, int n, float f2, SpectatorDetails spectatorDetails) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, f);
        this.field_175268_g.getTextureManager().bindTexture(field_175267_f);
        this.drawTexturedModalRect((float)(n - 91), f2, 0, 0, 182, 22);
        if (spectatorDetails.func_178681_b() >= 0) {
            this.drawTexturedModalRect((float)(n - 91 - 1 + spectatorDetails.func_178681_b() * 20), f2 - 1.0f, 0, 22, 24, 22);
        }
        RenderHelper.enableGUIStandardItemLighting();
        int n2 = 0;
        while (n2 < 9) {
            this.func_175266_a(n2, scaledResolution.getScaledWidth() / 2 - 90 + n2 * 20 + 2, f2 + 3.0f, f, spectatorDetails.func_178680_a(n2));
            ++n2;
        }
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }

    private void func_175266_a(int n, int n2, float f, float f2, ISpectatorMenuObject iSpectatorMenuObject) {
        this.field_175268_g.getTextureManager().bindTexture(field_175269_a);
        if (iSpectatorMenuObject != SpectatorMenu.field_178657_a) {
            int n3 = (int)(f2 * 255.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(n2, f, 0.0f);
            float f3 = iSpectatorMenuObject.func_178662_A_() ? 1.0f : 0.25f;
            GlStateManager.color(f3, f3, f3, f2);
            iSpectatorMenuObject.func_178663_a(f3, n3);
            GlStateManager.popMatrix();
            String string = String.valueOf(GameSettings.getKeyDisplayString(Minecraft.gameSettings.keyBindsHotbar[n].getKeyCode()));
            if (n3 > 3 && iSpectatorMenuObject.func_178662_A_()) {
                Minecraft.fontRendererObj.drawStringWithShadow(string, n2 + 19 - 2 - Minecraft.fontRendererObj.getStringWidth(string), f + 6.0f + 3.0f, 0xFFFFFF + (n3 << 24));
            }
        }
    }

    public void func_175260_a(int n) {
        this.field_175270_h = Minecraft.getSystemTime();
        if (this.field_175271_i != null) {
            this.field_175271_i.func_178644_b(n);
        } else {
            this.field_175271_i = new SpectatorMenu(this);
        }
    }

    public boolean func_175262_a() {
        return this.field_175271_i != null;
    }

    public void func_175259_b(int n) {
        int n2 = this.field_175271_i.func_178648_e() + n;
        while (!(n2 < 0 || n2 > 8 || this.field_175271_i.func_178643_a(n2) != SpectatorMenu.field_178657_a && this.field_175271_i.func_178643_a(n2).func_178662_A_())) {
            n2 += n;
        }
        if (n2 >= 0 && n2 <= 8) {
            this.field_175271_i.func_178644_b(n2);
            this.field_175270_h = Minecraft.getSystemTime();
        }
    }

    private float func_175265_c() {
        long l = this.field_175270_h - Minecraft.getSystemTime() + 5000L;
        return MathHelper.clamp_float((float)l / 2000.0f, 0.0f, 1.0f);
    }

    @Override
    public void func_175257_a(SpectatorMenu spectatorMenu) {
        this.field_175271_i = null;
        this.field_175270_h = 0L;
    }
}

