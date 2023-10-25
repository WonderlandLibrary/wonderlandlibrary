/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import java.awt.Color;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventRenderGUI;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.combat.KillAura;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.ui.Draw;
import me.Tengoku.Terror.util.AnimationUtil;
import me.Tengoku.Terror.util.ColorUtils;
import me.Tengoku.Terror.util.RenderUtils;
import me.Tengoku.Terror.util.RoundedRect;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TargetHUD
extends Module {
    private double easingHealth = 0.0;
    private double hudHeight;
    private double healthBarWidth;
    int fadeTimer;
    private double mcHeight;
    private Packet action;
    ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    private double mcWidth;
    int healthColor = 0;
    private FontRenderer font = Minecraft.fontRendererObj;
    private double width;

    public int getScaledHeight() {
        int n = new ScaledResolution(mc).getScaledHeight();
        return n;
    }

    public int getScaledWidth() {
        int n = new ScaledResolution(mc).getScaledWidth();
        return n;
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("TargetHUD Mode").getValString();
        this.setDisplayName("Target HUD \ufffdf" + string);
    }

    public void drawRect(int n, int n2, double d, int n3, int n4) {
        int n5 = (n4 >> 24 & 0xFF) / 255;
        int n6 = (n4 >> 16 & 0xFF) / 255;
        int n7 = (n4 >> 8 & 0xFF) / 255;
        int n8 = (n4 & 0xFF) / 255;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)n6, (float)n7, (float)n8, (float)n5);
        GL11.glBegin((int)6);
        GL11.glVertex2d((double)d, (double)n2);
        GL11.glVertex2d((double)n, (double)n2);
        GL11.glVertex2d((double)n, (double)n3);
        GL11.glVertex2d((double)d, (double)n3);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Exhibition");
        arrayList.add("Novoline");
        arrayList.add("Old_Astolfo");
        arrayList.add("Astolfo");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("TargetHUD Mode", (Module)this, "Astolfo", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Astolfo RGB", this, true));
    }

    public TargetHUD() {
        super("Target HUD", 0, Category.RENDER, "");
    }

    private void drawHead(ResourceLocation resourceLocation, int n, int n2) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        mc.getTextureManager().bindTexture(resourceLocation);
        Gui.drawScaledCustomSizeModalRect(n, n2, 8.0f, 8.0f, 8, 8, 16, 16, 64.0f, 64.0f);
    }

    @EventTarget
    public void onRender(EventRenderGUI eventRenderGUI) {
        int n;
        float f;
        float f2;
        float f3;
        double d;
        double d2;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("TargetHUD Mode").getValString();
        if (string.equalsIgnoreCase("Flux") && KillAura.target != null && Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).isToggled() && KillAura.target instanceof EntityOtherPlayerMP) {
            GL11.glPushMatrix();
            GL11.glTranslated((double)(this.mcWidth / 2.0 + 10.0), (double)(this.mcHeight / 2.0), (double)(this.mcWidth / 2.0 + 10.0));
            RoundedRect.drawRoundedRect(-2.0, 34.0, 2.0 + this.width, KillAura.target.getTotalArmorValue() != 0 ? 66 : 60, 5.0, new Color(48, 48, 48).darker().getRGB());
            this.drawHead(mc.getNetHandler().getPlayerInfo(KillAura.target.getUniqueID()).getLocationSkin(), 0, 36);
            RoundedRect.drawRoundedRect(0.0, 56.0, KillAura.target.getMaxHealth(), 58.0, 5.0, new Color(25, 25, 35, 255).getRGB());
            RoundedRect.drawRoundedRect(0.0, 56.0, (double)(KillAura.target.getHealth() / KillAura.target.getMaxHealth()) * this.width, 58.0, 0.0, ColorUtils.getHealthColor(KillAura.target.getHealth(), KillAura.target.getMaxHealth()).getRGB());
            if (KillAura.target.getTotalArmorValue() != 0) {
                this.drawRect(0, 62, this.width, 64, new Color(25, 25, 35, 255).getRGB());
                this.drawRect(0, 62, (double)(KillAura.target.getTotalArmorValue() / 20) * this.width, 64, new Color(77, 128, 255, 255).getRGB());
            }
            this.font.drawString(KillAura.target.getName(), 20.0, 40.0, ColorUtils.getHealthColor(KillAura.target.getHealth(), KillAura.target.getMaxHealth()).getRGB());
            GL11.glPopMatrix();
        }
        if (string.equalsIgnoreCase("Astolfo") && KillAura.target != null && Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).isToggled()) {
            if (KillAura.target instanceof EntityOtherPlayerMP) {
                boolean bl = Exodus.INSTANCE.settingsManager.getSettingByClass("Astolfo RGB", TargetHUD.class).getValBoolean();
                f8 = this.sr.getScaledWidth();
                f7 = this.sr.getScaledHeight();
                f6 = f7 / 2.0f + 20.0f;
                f5 = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
                f4 = -200.0f;
                d2 = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
                d2 = MathHelper.clamp_double(d2, 0.0, 1.0);
                d = 60.0 * d2;
                f3 = f8 / 90.0f;
                f2 = (float)(this.sr.getScaledWidth() / 2) + f4 + 100.0f;
                f = this.sr.getScaledHeight() / 2 + 10 + 20;
                n = 30;
                if (KillAura.target.getCurrentArmor(3) != null) {
                    n += 15;
                }
                if (KillAura.target.getCurrentArmor(2) != null) {
                    n += 15;
                }
                if (KillAura.target.getCurrentArmor(1) != null) {
                    n += 15;
                }
                if (KillAura.target.getCurrentArmor(0) != null) {
                    n += 15;
                }
                if (KillAura.target.getHeldItem() != null) {
                    n += 15;
                }
                this.healthColor = !bl ? ColorUtils.getHealthColor(KillAura.target.getHealth(), KillAura.target.getMaxHealth()).getRGB() : ColorUtils.getRainbow(1.25f, 1.0f, 1.0f);
                GlStateManager.pushMatrix();
                double d3 = 1.2f;
                GlStateManager.scale(d3, d3, d3);
                float f9 = Math.max(n, Minecraft.fontRendererObj.getStringWidth(KillAura.target.getName()) + 30);
                this.healthBarWidth = AnimationUtil.INSTANCE.animate(f2 + f9 * f5 - 200.0f, this.healthBarWidth, 0.1);
                Gui.drawRect(f2, f, f2 + 150.0f, f, new Color(0.0f, 0.0f, 0.0f, 0.3f).getRGB());
                Gui.drawRect(f2, f, f2 + 150.0f, f + 40.0f, new Color(0.0f, 0.0f, 0.0f, 0.3f).getRGB());
                Gui.drawRect(f2, f, f2 + 150.0f, f + 40.0f, new Color(0.0f, 0.0f, 0.0f, 0.3f).getRGB());
                Gui.drawRect(f2 + 25.0f, f + 30.0f, f2 + 135.0f, f + 35.0f, new Color(48, 48, 48).darker().getRGB());
                Gui.drawRect(f2 + 25.0f, f + 30.0f, (double)f2 + this.healthBarWidth - 150.0, f + 35.0f, this.healthColor);
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.5f, 1.5f, 1.5f);
                if (KillAura.target.getHealth() >= 10.0f) {
                    Draw.drawImg(new ResourceLocation("Terror/heart.png"), 285.0, 210.0, 9.0, 8.0);
                } else {
                    Draw.drawImg(new ResourceLocation("Terror/heart.png"), 278.0, 210.0, 9.0, 8.0);
                }
                Minecraft.fontRendererObj.drawStringWithShadow(String.valueOf((int)KillAura.target.getHealth()), f2 - 110.0f, f - 90.0f, this.healthColor);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.1f, 1.1f, 1.1f);
                Minecraft.fontRendererObj.drawStringWithShadow(KillAura.target.getName(), f2 - 12.5f, f - 25.0f, -1);
                GlStateManager.popMatrix();
                boolean bl2 = false;
                double d4 = 0.85;
                GlStateManager.pushMatrix();
                GlStateManager.scale(d4, d4, d4);
                GlStateManager.popMatrix();
                GuiInventory.drawEntityOnScreen((int)f2 + 12, (int)f + 35, 15, KillAura.target.rotationYaw, KillAura.target.rotationPitch, KillAura.target);
                GlStateManager.popMatrix();
            } else {
                this.healthBarWidth = 92.0;
                this.hudHeight = 0.0;
                KillAura.target = null;
            }
        }
        if (string.equalsIgnoreCase("Pandaware") && KillAura.target != null && Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).isToggled()) {
            if (KillAura.target instanceof EntityOtherPlayerMP) {
                float f10 = this.sr.getScaledWidth();
                f8 = this.sr.getScaledHeight();
                f7 = f10 / 2.0f + 10.0f;
                f6 = f8 / 2.0f + 10.0f;
                f5 = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
                f4 = -200.0f;
                d2 = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
                d2 = MathHelper.clamp_double(d2, 0.0, 1.0);
                d = 60.0 * d2;
                this.healthBarWidth = AnimationUtil.INSTANCE.animate(d, this.healthBarWidth, 0.1);
                f3 = (float)(this.sr.getScaledWidth() / 2) + f4 + 200.0f;
                f2 = this.sr.getScaledHeight() / 2 + 10 + 80;
                int n2 = 80;
                if (KillAura.target.getCurrentArmor(3) != null) {
                    n2 += 15;
                }
                if (KillAura.target.getCurrentArmor(2) != null) {
                    n2 += 15;
                }
                if (KillAura.target.getCurrentArmor(1) != null) {
                    n2 += 15;
                }
                if (KillAura.target.getCurrentArmor(0) != null) {
                    n2 += 15;
                }
                if (KillAura.target.getHeldItem() != null) {
                    n2 += 15;
                }
                n = CustomIngameGui.getColorInt((int)f3);
                float f11 = Math.max(n2, Minecraft.fontRendererObj.getStringWidth(KillAura.target.getName()) + 30);
                RoundedRect.drawRoundedRect(f3, f2, f3 + f11 + 20.0f, f2 + 40.0f, 5.0, new Color(0.0f, 0.0f, 0.0f, 0.3f).getRGB());
                RoundedRect.drawRoundedRect(f3, f2, f3 + f11 + 20.0f, f2 + 40.0f, 5.0, new Color(0.0f, 0.0f, 0.0f, 0.3f).getRGB());
                RoundedRect.drawRoundedRect(f3 + 2.0f, f2 + 30.0f, (double)(f3 + 25.0f) + this.healthBarWidth, (double)f2 + 34.29, 4.0, n);
                Draw.fixTextFlickering();
                this.drawHead(mc.getNetHandler().getPlayerInfo(KillAura.target.getUniqueID()).getLocationSkin(), (int)f3, (int)f2);
                FontUtil.normal.drawStringWithShadow(String.valueOf((int)KillAura.target.getHealth()), f3 + 20.0f, f2 + 10.0f, -1);
                FontUtil.normal.drawStringWithShadow(KillAura.target.getName(), f3 + 20.0f, f2 + 2.0f, -1);
                boolean bl = false;
                double d5 = 0.85;
                GlStateManager.pushMatrix();
                GlStateManager.scale(d5, d5, d5);
                GlStateManager.popMatrix();
            } else {
                this.healthBarWidth = 92.0;
                this.hudHeight = 0.0;
                KillAura.target = null;
            }
        }
        if (string.equalsIgnoreCase("Old_Astolfo") && KillAura.target != null && Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).isToggled()) {
            if (KillAura.target instanceof EntityOtherPlayerMP) {
                float f12 = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
                f8 = 10.0f;
                f7 = (float)(this.sr.getScaledWidth() / 2) + f8;
                f6 = this.sr.getScaledHeight() / 2 + 5;
                int n3 = 90;
                int n4 = ColorUtils.getHealthColor(KillAura.target.getHealth(), KillAura.target.getMaxHealth()).getRGB();
                d2 = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
                d2 = MathHelper.clamp_double(d2, 0.0, 1.0);
                d = 60.0 * d2;
                this.healthBarWidth = AnimationUtil.INSTANCE.animate(d, this.healthBarWidth, 0.1);
                f3 = Math.max(n3, Minecraft.fontRendererObj.getStringWidth(KillAura.target.getName()) + 30);
                Gui.drawRect(f7 - 10.0f, f6, f7 + f3 + 10.0f, f6 + 40.0f, -1879048192);
                Gui.drawRect(f7, f6 + 24.0f, (double)f7 + this.healthBarWidth, f6 + 15.0f, n4);
                Minecraft.fontRendererObj.drawStringWithShadow(String.valueOf((int)KillAura.target.getHealth()), (float)((double)f7 + this.healthBarWidth) - 10.0f, f6 + 16.0f, n4);
                Minecraft.fontRendererObj.drawStringWithShadow(KillAura.target.getName(), f7 + 12.0f, f6 + 4.0f, -1);
                boolean bl = false;
                double d6 = 0.85;
                GlStateManager.pushMatrix();
                GlStateManager.scale(d6, d6, d6);
                GlStateManager.popMatrix();
            } else {
                this.healthBarWidth = 92.0;
                this.hudHeight = 0.0;
                KillAura.target = null;
            }
        }
        if (string.equalsIgnoreCase("Novoline") && KillAura.target != null && Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).isToggled()) {
            if (KillAura.target instanceof EntityOtherPlayerMP) {
                float f13 = KillAura.target.getHealth() / KillAura.target.getMaxHealth();
                f8 = 10.0f;
                f7 = (float)(this.sr.getScaledWidth() / 2) + f8;
                f6 = this.sr.getScaledHeight() / 2 + 5;
                int n5 = 90;
                if (KillAura.target.getCurrentArmor(3) != null) {
                    n5 += 15;
                }
                if (KillAura.target.getCurrentArmor(2) != null) {
                    n5 += 15;
                }
                if (KillAura.target.getCurrentArmor(1) != null) {
                    n5 += 15;
                }
                if (KillAura.target.getCurrentArmor(0) != null) {
                    n5 += 15;
                }
                if (KillAura.target.getHeldItem() != null) {
                    n5 += 15;
                }
                int n6 = ColorUtils.getHealthColor(KillAura.target.getHealth(), KillAura.target.getMaxHealth()).getRGB();
                float f14 = Math.max(n5, Minecraft.fontRendererObj.getStringWidth(KillAura.target.getName()) + 30);
                Gui.drawRect(f7, f6, f7 + f14, f6 + 40.0f, new Color(0.0f, 0.0f, 0.0f, 0.3f).getRGB());
                Gui.drawRect(f7, f6 + 38.0f, f7 + f14 * f13, f6 + 40.0f, n6);
                Minecraft.fontRendererObj.drawStringWithShadow(KillAura.target.getName(), f7 + 25.0f, f6 + 7.0f, -1);
                int n7 = 0;
                d = 0.85;
                GlStateManager.pushMatrix();
                GlStateManager.scale(d, d, d);
                if (KillAura.target.getCurrentArmor(3) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(3), (int)((double)((float)(this.sr.getScaledWidth() / 2) + f8 + 23.0f + (float)n7) / d), (int)((double)(this.sr.getScaledHeight() / 2 + 28) / d));
                    n7 += 15;
                }
                if (KillAura.target.getCurrentArmor(2) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(2), (int)((double)((float)(this.sr.getScaledWidth() / 2) + f8 + 23.0f + (float)n7) / d), (int)((double)(this.sr.getScaledHeight() / 2 + 28) / d));
                    n7 += 15;
                }
                if (KillAura.target.getCurrentArmor(1) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(1), (int)((double)((float)(this.sr.getScaledWidth() / 2) + f8 + 23.0f + (float)n7) / d), (int)((double)(this.sr.getScaledHeight() / 2 + 28) / d));
                    n7 += 15;
                }
                if (KillAura.target.getCurrentArmor(0) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getCurrentArmor(0), (int)((double)((float)(this.sr.getScaledWidth() / 2) + f8 + 23.0f + (float)n7) / d), (int)((double)(this.sr.getScaledHeight() / 2 + 28) / d));
                    n7 += 15;
                }
                if (KillAura.target.getHeldItem() != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(KillAura.target.getHeldItem(), (int)((double)((float)(this.sr.getScaledWidth() / 2) + f8 + 23.0f + (float)n7) / d), (int)((double)(this.sr.getScaledHeight() / 2 + 28) / d));
                }
                GlStateManager.popMatrix();
                GuiInventory.drawEntityOnScreen((int)f7 + 12, (int)f6 + 33, 15, KillAura.target.rotationYaw, KillAura.target.rotationPitch, KillAura.target);
            } else {
                this.healthBarWidth = 92.0;
                this.hudHeight = 0.0;
                KillAura.target = null;
            }
        }
        if (string.equalsIgnoreCase("Exhibition")) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            f8 = scaledResolution.getScaledWidth();
            f7 = scaledResolution.getScaledHeight();
            EntityLivingBase entityLivingBase = KillAura.target;
            boolean bl = Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).isToggled();
            if (bl && entityLivingBase != null && entityLivingBase instanceof EntityOtherPlayerMP) {
                float f15 = 20.0f;
                float f16 = (float)(scaledResolution.getScaledWidth() / 2) + f15;
                float f17 = scaledResolution.getScaledHeight() / 2 + 10;
                int n8 = 30;
                if (entityLivingBase.getCurrentArmor(3) != null) {
                    n8 += 15;
                }
                if (entityLivingBase.getCurrentArmor(2) != null) {
                    n8 += 15;
                }
                if (entityLivingBase.getCurrentArmor(1) != null) {
                    n8 += 15;
                }
                if (entityLivingBase.getCurrentArmor(0) != null) {
                    n8 += 15;
                }
                if (entityLivingBase.getHeldItem() != null) {
                    n8 += 15;
                }
                float f18 = 140.0f;
                f3 = 40.0f;
                float f19 = 40.0f;
                f = f8 / 2.0f + 30.0f;
                float f20 = f7 / 2.0f + 30.0f;
                float f21 = entityLivingBase.getHealth();
                double d7 = f21 / entityLivingBase.getMaxHealth();
                d7 = MathHelper.clamp_double(d7, 0.0, 1.0);
                double d8 = 60.0 * d7;
                int n9 = ColorUtils.getHealthColor(entityLivingBase.getHealth(), entityLivingBase.getMaxHealth()).getRGB();
                String string2 = String.valueOf((float)((int)entityLivingBase.getHealth()) / 1.0f);
                int n10 = 0;
                double d9 = 0.85;
                GlStateManager.pushMatrix();
                GlStateManager.scale(d9, d9, d9);
                if (entityLivingBase.getCurrentArmor(3) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(entityLivingBase.getCurrentArmor(3), (int)((double)((float)(scaledResolution.getScaledWidth() / 2) + f15 + 33.0f + (float)n10) / d9), (int)((double)(scaledResolution.getScaledHeight() / 2 + 56) / d9));
                    n10 += 15;
                }
                if (entityLivingBase.getCurrentArmor(2) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(entityLivingBase.getCurrentArmor(2), (int)((double)((float)(scaledResolution.getScaledWidth() / 2) + f15 + 33.0f + (float)n10) / d9), (int)((double)(scaledResolution.getScaledHeight() / 2 + 56) / d9));
                    n10 += 15;
                }
                if (entityLivingBase.getCurrentArmor(1) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(entityLivingBase.getCurrentArmor(1), (int)((double)((float)(scaledResolution.getScaledWidth() / 2) + f15 + 33.0f + (float)n10) / d9), (int)((double)(scaledResolution.getScaledHeight() / 2 + 56) / d9));
                    n10 += 15;
                }
                if (entityLivingBase.getCurrentArmor(0) != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(entityLivingBase.getCurrentArmor(0), (int)((double)((float)(scaledResolution.getScaledWidth() / 2) + f15 + 33.0f + (float)n10) / d9), (int)((double)(scaledResolution.getScaledHeight() / 2 + 56) / d9));
                    n10 += 15;
                }
                if (entityLivingBase.getHeldItem() != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(entityLivingBase.getHeldItem(), (int)((double)((float)(scaledResolution.getScaledWidth() / 2) + f15 + 33.0f + (float)n10) / d9), (int)((double)(scaledResolution.getScaledHeight() / 2 + 56) / d9));
                }
                GlStateManager.popMatrix();
                this.healthBarWidth = AnimationUtil.INSTANCE.animate(d8, this.healthBarWidth, 0.1);
                Gui.drawGradientRect((double)f - 3.5, (double)f20 - 3.5, f + 105.5f, f20 + 42.4f, new Color(10, 10, 10, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
                Gui.drawGradientRect(f - 3.0f, (double)f20 - 3.2, f + 104.8f, f20 + 41.8f, new Color(40, 40, 40, 255).getRGB(), new Color(40, 40, 40, 255).getRGB());
                Gui.drawGradientRect((double)f - 1.4, (double)f20 - 1.5, f + 103.5f, f20 + 40.5f, new Color(74, 74, 74, 255).getRGB(), new Color(74, 74, 74, 255).getRGB());
                Gui.drawGradientRect(f - 1.0f, f20 - 1.0f, f + 103.0f, f20 + 40.0f, new Color(32, 32, 32, 255).getRGB(), new Color(10, 10, 10, 255).getRGB());
                Gui.drawRect(f + 25.0f, f20 + 11.0f, f + 87.0f, f20 + 14.29f, new Color(105, 105, 105, 40).getRGB());
                Gui.drawRect(f + 25.0f, f20 + 11.0f, (double)(f + 27.0f) + this.healthBarWidth, f20 + 14.29f, RenderUtils.getColorFromPercentage(entityLivingBase.getHealth(), entityLivingBase.getMaxHealth()));
                GuiInventory.drawEntityOnScreen((int)(f + 12.0f), (int)(f20 + 34.0f), 15, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch, entityLivingBase);
            }
        }
    }
}

