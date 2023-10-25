/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Interpolator
 */
package org.celestial.client.ui.components.draggable.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import javafx.animation.Interpolator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import optifine.CustomColors;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.render.AnimationHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;
import org.lwjgl.opengl.GL11;

public class TargetHUDComponent
extends DraggableModule {
    private double healthBarWidth;
    private float hurtAnim = 0.0f;

    public TargetHUDComponent() {
        super("TargetHUDComponent", 500, 275);
    }

    @Override
    public int getWidth() {
        return 155;
    }

    @Override
    public int getHeight() {
        return 87;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void render(int mouseX, int mouseY) {
        block71: {
            block80: {
                block75: {
                    block73: {
                        block79: {
                            block78: {
                                block77: {
                                    block76: {
                                        block74: {
                                            block72: {
                                                block70: {
                                                    mode = KillAura.targetHudMode.getOptions();
                                                    target = this.mc.player;
                                                    color = KillAura.targetHudColor.getColor();
                                                    if (!mode.equalsIgnoreCase("Astolfo")) break block70;
                                                    x = this.getX();
                                                    y = this.getY();
                                                    healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                                                    healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                                                    check = target.getHealth() < 18.0f && target.getHealth() > 1.0f ? 8.0 : 0.0;
                                                    this.healthBarWidth = healthWid;
                                                    top = (int)y;
                                                    left = (int)x;
                                                    RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)left, (double)top, 155.0, 60.0, 20);
                                                    RectHelper.drawRectBetter(x, y, 155.0, 60.0, new Color(20, 20, 20, 255).getRGB());
                                                    if (!target.getName().isEmpty()) {
                                                        this.mc.fontRendererObj.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 31.0f, y + 5.0f, -1);
                                                    }
                                                    GlStateManager.pushMatrix();
                                                    GlStateManager.translate(x, y, 1.0f);
                                                    GlStateManager.scale(2.5f, 2.5f, 2.5f);
                                                    GlStateManager.translate(-x - 3.0f, -y - 2.0f, 1.0f);
                                                    this.mc.fontRendererObj.drawStringWithShadow(MathematicHelper.round(target.getHealth() / 2.0f, 1) + " \u2764", x + 16.0f, y + 10.0f, new Color(color).getRGB());
                                                    GlStateManager.popMatrix();
                                                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                                    this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 7);
                                                    this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 1);
                                                    GuiInventory.drawEntityOnScreen(x + 16.0f, y + 55.0f, 25, target.rotationYaw, -target.rotationPitch, target);
                                                    RectHelper.drawRectBetter(x + 30.0f, y + 48.0f, 120.0, 8.0, new Color(color).darker().darker().darker().getRGB());
                                                    RectHelper.drawRectBetter(x + 30.0f, y + 48.0f, this.healthBarWidth + check, 8.0, new Color(color).darker().getRGB());
                                                    RectHelper.drawRectBetter(x + 30.0f, y + 48.0f, healthWid, 8.0, new Color(color).getRGB());
                                                    break block71;
                                                }
                                                if (!mode.equalsIgnoreCase("Moon Dev")) break block72;
                                                if (this.mc.gameSettings.ofFastRender) {
                                                    this.mc.gameSettings.ofFastRender = false;
                                                }
                                                x = this.getX();
                                                y = this.getY();
                                                right = 115 + this.mc.latoBig.getStringWidth(target.getName()) - 50;
                                                healthWid = target.getHealth() / target.getMaxHealth() * (float)(69 + this.mc.latoBig.getStringWidth(target.getName()) - 9);
                                                this.healthBarWidth = Interpolator.LINEAR.interpolate(this.healthBarWidth, (double)healthWid, (double)(10.0f / (float)Minecraft.getDebugFPS()));
                                                this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, (double)(69 + this.mc.latoBig.getStringWidth(target.getName()) - 9));
                                                GlStateManager.pushMatrix();
                                                GlStateManager.disableBlend();
                                                RenderHelper.blurAreaBoarder((int)x, y - 0.5f, right, 40.0f, 45.0f, 0.0f, 1.0f);
                                                if (target instanceof EntityPlayer) {
                                                    this.mc.latoBig.drawStringWithShadow((Object)ChatFormatting.BOLD + target.getName(), x + 37.0f, y + 2.0f, -1);
                                                    this.mc.fontRenderer.drawStringWithShadow("Health: " + MathematicHelper.round(target.getHealth(), 1), x + 37.0f, y + 15.0f, -1);
                                                    this.mc.fontRenderer.drawStringWithShadow("Distance: " + MathematicHelper.round(this.mc.player.getDistanceToEntity(target), 1) + "m", x + 37.0f, y + 25.0f, -1);
                                                }
                                                RenderHelper.renderItem(target.getHeldItem(EnumHand.OFF_HAND), (int)x + (int)right - 15, (int)y - 20);
                                                RectHelper.drawRectBetter(x + 2.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 26.0f, right - 5.0f, 2.0, new Color(35, 35, 35, 255).getRGB());
                                                RectHelper.drawRect(x + 2.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 26.0f, (double)(x + 2.0f) + this.healthBarWidth, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 26.0f + 2.0f, PaletteHelper.getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB());
                                                GlStateManager.pushMatrix();
                                                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                                check = this.mc.player.connection.getPlayerInfoMap().iterator();
                                                break block73;
                                            }
                                            if (!mode.equalsIgnoreCase("Red-Blue")) break block74;
                                            if (this.mc.gameSettings.ofFastRender) {
                                                this.mc.gameSettings.ofFastRender = false;
                                            }
                                            x = this.getX();
                                            y = this.getY();
                                            armorWid = (float)target.getTotalArmorValue() * 4.8f;
                                            healthWid = target.getHealth() / target.getMaxHealth() * 96.0f;
                                            healthWid = MathHelper.clamp(healthWid, 0.0, 96.0);
                                            this.healthBarWidth = Interpolator.LINEAR.interpolate(this.healthBarWidth, (double)((float)healthWid), (double)(10.0f / (float)Minecraft.getDebugFPS()));
                                            this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, 104.0);
                                            left = x;
                                            top = y;
                                            right = left + 48.0f + 120.0f;
                                            GlStateManager.pushMatrix();
                                            GlStateManager.disableBlend();
                                            RenderHelper.blurAreaBoarder((int)x, (int)y, 148.0f, 45.0f, 120.0f, 0.0f, 1.0f);
                                            RectHelper.drawRect(x - 1.0f, y - 0.5f, right - 168.0f, y + 45.0f, new Color(205, 205, 205).getRGB());
                                            RenderHelper.renderBlurredShadow(Color.WHITE, (double)(x - 1.0f), (double)y, 2.0, 45.0, 6);
                                            if (target instanceof EntityPlayer) {
                                                this.mc.robotoRegularFontRender.drawStringWithShadow(target.getName(), x + 8.0f, y + 5.0f, -1);
                                                RenderHelper.renderBlurredShadow(Color.WHITE, (double)(x + 9.0f), (double)(y + 5.0f), (double)(this.mc.robotoRegularFontRender.getStringWidth(target.getName()) + 2), 4.0, 10);
                                            }
                                            RenderHelper.renderItem(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 133, (int)y - 20);
                                            if (Minecraft.getDebugFPS() > 5) {
                                                RectHelper.drawSmoothRectBetter(x + 4.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 19.0f, 96.0, 5.0, new Color(35, 35, 35, 255).getRGB());
                                                RectHelper.drawSmoothRectBetter(x + 4.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 19.0f, this.healthBarWidth, 5.0, Color.RED.getRGB());
                                                RectHelper.drawSmoothRectBetter(x + 4.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 28.0f, 96.0, 5.0, new Color(35, 35, 35, 255).getRGB());
                                                if (target.getTotalArmorValue() > 0) {
                                                    RectHelper.drawSmoothRectBetter(x + 4.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 28.0f, armorWid, 5.0, new Color(55, 175, 255).getRGB());
                                                }
                                                if (target.getTotalArmorValue() > 0) {
                                                    RenderHelper.renderBlurredShadow(new Color(55, 175, 255).brighter(), (double)(x + 3.0f), (double)(y + 37.0f), armorWid, 5.0, 9);
                                                }
                                                RenderHelper.renderBlurredShadow(Color.RED.brighter(), (double)((int)x + 3), (double)((int)y + 28), (double)((int)this.healthBarWidth + 4), 5.0, 9);
                                            }
                                            GlStateManager.pushMatrix();
                                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                            var15_85 = this.mc.player.connection.getPlayerInfoMap().iterator();
                                            break block75;
                                        }
                                        if (!mode.equalsIgnoreCase("Glow")) break block76;
                                        x = this.getX();
                                        y = this.getY();
                                        healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                                        healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                                        this.healthBarWidth = Interpolator.LINEAR.interpolate(this.healthBarWidth, (double)((float)healthWid), (double)(10.0f / (float)Minecraft.getDebugFPS()));
                                        this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, 124.0);
                                        left = x;
                                        top = y;
                                        right = left + 48.0f + 120.0f;
                                        GlStateManager.pushMatrix();
                                        GlStateManager.disableBlend();
                                        RenderHelper.renderBlurredShadow(new Color(20, 20, 20, 200), (double)((int)x), (double)((int)y), 168.0, 56.0, 6);
                                        RenderHelper.renderBlurredShadow(new Color(KillAura.targetHudColor.getColor()).brighter(), (double)((int)x), (double)((int)y), 168.0, 3.0, 15);
                                        RectHelper.drawSmoothRect(x, y + 1.0f, right, y, new Color(KillAura.targetHudColor.getColor()).getRGB());
                                        RectHelper.drawRect(x + 3.0f, y + 14.0f, right - 3.0f, (double)y + 14.5, new Color(205, 205, 205).getRGB());
                                        this.mc.comfortaa.drawCenteredStringWithShadow(target.getName(), x + 85.0f, y + 5.0f, -1);
                                        RenderHelper.renderItem(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 148, (int)y + 15);
                                        endValue = MathHelper.clamp((float)target.hurtTime / 6.5f, 0.1f, this.hurtAnim * 60.0f);
                                        this.hurtAnim = (float)Interpolator.LINEAR.interpolate((double)this.hurtAnim, (double)endValue, (double)(5.0f / (float)Minecraft.getDebugFPS()));
                                        if (String.valueOf(this.hurtAnim).equals("NaN")) {
                                            this.hurtAnim = target.hurtTime;
                                            this.healthBarWidth = target.getHealth();
                                        }
                                        if (this.hurtAnim < 0.0f) {
                                            this.hurtAnim = 0.0f;
                                        }
                                        if (Minecraft.getDebugFPS() > 5) {
                                            RectHelper.drawRectBetter(left + 87.0f, top + 34.0f, 60.0, 2.0, new Color(35, 35, 35, 255).getRGB());
                                            RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)((int)x + 87), (double)((int)y + 34), (double)(this.hurtAnim * 60.0f), 2.0, 7);
                                            this.mc.fontRenderer.drawStringWithShadow("Ground: " + target.onGround, left + 41.0f, top + 20.0f, -1);
                                            RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)((int)x + 76), (double)((int)y + 22), target.onGround != false ? 16.0 : 19.0, 2.0, 10);
                                            this.mc.fontRenderer.drawStringWithShadow("HurtTime", left + 41.0f, top + 31.0f, -1);
                                            RectHelper.drawSmoothRectBetter(left + 87.0f, top + 34.0f, this.hurtAnim * 60.0f, 2.0, new Color(KillAura.targetHudColor.getColor()).getRGB());
                                            RectHelper.drawSmoothRectBetter(x + 41.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 35.0f, 120.0, 5.0, new Color(35, 35, 35, 255).getRGB());
                                            RectHelper.drawSmoothRectBetter(x + 41.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 35.0f, this.healthBarWidth, 5.0, new Color(color).getRGB());
                                            RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)((int)x + 40), (double)((int)y + 44), (double)((int)this.healthBarWidth + 4), 5.0, 10);
                                        }
                                        GlStateManager.pushMatrix();
                                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                        for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                            if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                                hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                                GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                                this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                                Gui.drawScaledCustomSizeModalRect((int)x + 3, (int)y + 17, 8.0f, 8.0f, 8.0f, 8.0f, 34.0f, 34.0f, 64.0f, 64.0f);
                                                GlStateManager.bindTexture(0);
                                            }
                                            GL11.glDisable(3089);
                                        }
                                        GlStateManager.popMatrix();
                                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                        GlStateManager.popMatrix();
                                        break block71;
                                    }
                                    if (!mode.equalsIgnoreCase("Novoline Old")) break block77;
                                    if (target == null) {
                                        return;
                                    }
                                    if (target.getHealth() < 0.0f) {
                                        return;
                                    }
                                    x = this.getX();
                                    y = this.getY();
                                    healthWid = target.getHealth() / target.getMaxHealth() * (float)(40 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()));
                                    healthWid = (float)MathHelper.clamp((double)healthWid, 0.0, 124.0);
                                    this.healthBarWidth = AnimationHelper.calculateCompensation(healthWid, (float)this.healthBarWidth, 5.0f, 5.0);
                                    this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, 124.0);
                                    RectHelper.drawRectBetter(x, y, 65 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 25, 40.0, new Color(19, 19, 19, 255).getRGB());
                                    RectHelper.drawRectBetter(x + 1.0f, y + 1.0f, 65 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 23, 38.0, new Color(41, 41, 41, 255).getRGB());
                                    if (!target.getName().isEmpty()) {
                                        this.mc.fontRendererObj.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 42.0f, y + 5.0f, -1);
                                    }
                                    GlStateManager.pushMatrix();
                                    GlStateManager.translate(x, y, 1.0f);
                                    GlStateManager.scale(1.05f, 1.05f, 1.05f);
                                    GlStateManager.translate(-x + 24.0f, -y + 18.0f, 1.0f);
                                    hp = MathematicHelper.round(target.getHealth() / 2.0f, 1) + "";
                                    this.mc.fontRendererObj.drawStringWithShadow(hp, x + 17.0f, y + 10.0f, -1);
                                    this.mc.fontRendererObj.drawStringWithShadow(" \u2764", x + (float)this.mc.fontRendererObj.getStringWidth(hp) + 16.0f, y + 10.0f, new Color(color).getRGB());
                                    GlStateManager.popMatrix();
                                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                    GlStateManager.pushMatrix();
                                    GlStateManager.translate(x, y, 1.0f);
                                    GlStateManager.scale(0.8f, 0.8f, 0.8f);
                                    GlStateManager.translate(-x + 148.0f, -y + 38.0f, 1.0f);
                                    v0 = stack = target.getHeldItemOffhand().isStackable() != false && target.getHeldItemOffhand().isEmpty() == false;
                                    if (stack) {
                                        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 39.0f + (float)this.mc.fontRenderer.getStringWidth(hp) - 21.0f), (int)(y - 8.0f));
                                        this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 49.0f + (float)this.mc.fontRenderer.getStringWidth(hp) - 21.0f), (int)y - 5);
                                    }
                                    GlStateManager.popMatrix();
                                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                    for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                        if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                            this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                            Gui.drawScaledCustomSizeModalRect((int)x + 1, (int)y + 1, 8.0f, 8.0f, 8.0f, 8.0f, 38.0f, 38.0f, 64.0f, 64.0f);
                                            GlStateManager.bindTexture(0);
                                        }
                                        GL11.glDisable(3089);
                                    }
                                    RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, 40 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()), 8.0, new Color(35, 35, 35, 255).getRGB());
                                    RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, target.getHealth() > 18.0f ? (double)healthWid : this.healthBarWidth + 4.0, 8.0, new Color(color).darker().getRGB());
                                    RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, healthWid, 8.0, new Color(color).getRGB());
                                    startAlpha = 0.25f;
                                    size = 8;
                                    top = (int)y;
                                    left = (int)x;
                                    right = (int)(x + 66.0f + (float)this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 23.0f);
                                    bottom = (int)y + 39;
                                    RectHelper.renderShadowHorizontal(Color.BLACK, 2.0f, startAlpha, size, top, left, right, true, true);
                                    RectHelper.renderShadowHorizontal(Color.BLACK, 2.0f, startAlpha, size, bottom, left, right, false, true);
                                    RectHelper.renderShadowVertical(Color.BLACK, 2.0f, startAlpha, size, right, top, bottom, true, true);
                                    RectHelper.renderShadowVertical(Color.BLACK, 2.0f, startAlpha, size, left, (double)top + 0.5, (double)bottom + 0.5, false, true);
                                    break block71;
                                }
                                if (!mode.equalsIgnoreCase("Novoline New")) break block78;
                                if (target == null) {
                                    return;
                                }
                                if (target.getHealth() < 0.0f) {
                                    return;
                                }
                                x = this.getX();
                                y = this.getY();
                                healthWid = target.getHealth() / target.getMaxHealth() * (float)(40 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()));
                                healthWid = (float)MathHelper.clamp((double)healthWid, 0.0, 124.0);
                                this.healthBarWidth = AnimationHelper.calculateCompensation(healthWid, (float)this.healthBarWidth, 5.0f, 5.0);
                                this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, 124.0);
                                RectHelper.drawRectBetter(x, y, 65 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 25, 40.0, new Color(19, 19, 19, 255).getRGB());
                                RectHelper.drawRectBetter(x + 1.0f, y + 1.0f, 65 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 23, 38.0, new Color(41, 41, 41, 255).getRGB());
                                if (!target.getName().isEmpty()) {
                                    this.mc.fontRendererObj.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 42.0f, y + 5.0f, -1);
                                }
                                GlStateManager.pushMatrix();
                                GlStateManager.translate(x, y, 1.0f);
                                GlStateManager.scale(1.05f, 1.05f, 1.05f);
                                GlStateManager.translate(-x + 24.0f, -y + 18.0f, 1.0f);
                                GlStateManager.popMatrix();
                                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                GlStateManager.pushMatrix();
                                GlStateManager.translate(x, y, 1.0f);
                                GlStateManager.scale(0.8f, 0.8f, 0.8f);
                                GlStateManager.translate(-x + 148.0f, -y + 38.0f, 1.0f);
                                v1 = stack = target.getHeldItemOffhand().isStackable() != false && target.getHeldItemOffhand().isEmpty() == false;
                                if (stack) {
                                    this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 88.0f), (int)(y - 8.0f));
                                    this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 98.0f), (int)y - 5);
                                }
                                GlStateManager.popMatrix();
                                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                    GlStateManager.pushMatrix();
                                    if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                        hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                        GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                        this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                        Gui.drawScaledCustomSizeModalRect((int)x + 1, (int)y + 1, 8.0f, 8.0f, 8.0f, 8.0f, 38.0f, 38.0f, 64.0f, 64.0f);
                                        GlStateManager.bindTexture(0);
                                    }
                                    GL11.glDisable(3089);
                                    GlStateManager.popMatrix();
                                }
                                hp = MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100.0f, 1) + "%";
                                RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, 40 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()), 10.0, new Color(35, 35, 35, 255).getRGB());
                                RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, target.getHealth() > 18.0f ? (double)healthWid : this.healthBarWidth + 4.0, 10.0, new Color(color).darker().getRGB());
                                RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, healthWid, 10.0, new Color(color).getRGB());
                                startAlpha = 0.25f;
                                size = 8;
                                top = (int)y;
                                left = (int)x;
                                right = (int)(x + 66.0f + (float)this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 23.0f);
                                bottom = (int)y + 39;
                                RectHelper.renderShadowHorizontal(Color.BLACK, 2.0f, startAlpha, size, top, left, right, true, true);
                                RectHelper.renderShadowHorizontal(Color.BLACK, 2.0f, startAlpha, size, bottom, left, right, false, true);
                                RectHelper.renderShadowVertical(Color.BLACK, 2.0f, startAlpha, size, right, top, bottom, true, true);
                                RectHelper.renderShadowVertical(Color.BLACK, 2.0f, startAlpha, size, left, (double)top + 0.5, (double)bottom + 0.5, false, true);
                                this.mc.fontRendererObj.drawStringWithShadow(hp, x + (float)this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) / 2.0f + 50.0f, y + 19.0f, -1);
                                break block71;
                            }
                            if (!mode.equalsIgnoreCase("Dev")) break block79;
                            x = this.getX();
                            y = this.getY();
                            x2 = this.getX();
                            y2 = this.getY();
                            healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                            healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                            if (target == null) ** GOTO lbl-1000
                            v2 = target.getHealth();
                            v3 = target instanceof EntityPlayer != false ? 18 : 10;
                            if (v2 < (float)v3 && target.getHealth() > 1.0f) {
                                v4 = 8.0;
                            } else lbl-1000:
                            // 2 sources

                            {
                                v4 = 0.0;
                            }
                            check = v4;
                            this.healthBarWidth = AnimationHelper.calculateCompensation((float)healthWid, (float)this.healthBarWidth, 0.0f, 0.005);
                            RectHelper.drawRectBetter(x, y, 145.0, 50.0, new Color(23, 23, 25, 203).getRGB());
                            if (!target.getName().isEmpty()) {
                                this.mc.robotoRegularFontRender.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 37.0f, y + 5.0f, -1);
                            }
                            GlStateManager.pushMatrix();
                            GlStateManager.translate(x, y, 1.0f);
                            GlStateManager.scale(1.5f, 1.5f, 1.5f);
                            GlStateManager.translate(-x - 14.0f, -y + 14.0f, 1.0f);
                            this.mc.fontRendererObj.drawStringWithShadow("\u00a7c\u2764", x + 16.0f, y + 10.0f, -1);
                            GlStateManager.popMatrix();
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 125, (int)y + 7);
                            this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 125, (int)y + 1);
                            list = new ArrayList<ItemStack>();
                            for (i = 0; i < 5; ++i) {
                                armorSlot = target.getEquipmentInSlot(i);
                                if (armorSlot == null) continue;
                                list.add(armorSlot);
                            }
                            for (ItemStack itemStack : list) {
                                net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
                                RenderHelper.renderItem(itemStack, (int)x2 + 36, (int)(y + 16.0f));
                                x2 += 16.0f;
                            }
                            i = target.getActivePotionEffects().iterator();
                            break block80;
                        }
                        if (mode.equalsIgnoreCase("Minecraft")) {
                            if (target == null) {
                                return;
                            }
                            x = this.getX();
                            y = this.getY();
                            GlStateManager.pushMatrix();
                            RectHelper.drawRectWithEdge(x - 2.0f, y - 7.0f, 155.0, 38.0, new Color(20, 20, 20, 255), new Color(255, 255, 255, 255));
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            this.mc.fontRendererObj.drawStringWithShadow(target.getName(), this.getX() + 37, this.getY() - 2, -1);
                            for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                GlStateManager.pushMatrix();
                                if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                    hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                    GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                    this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                    Gui.drawScaledCustomSizeModalRect((int)x, (int)y - 5, 8.0f, 8.0f, 8.0f, 8.0f, 34.0f, 34.0f, 64.0f, 64.0f);
                                    GlStateManager.bindTexture(0);
                                }
                                GL11.glDisable(3089);
                                GlStateManager.popMatrix();
                            }
                            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 135, (int)y + 2);
                            this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 136, (int)y - 6);
                            this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
                            i = 0;
                            while ((float)i < target.getMaxHealth() / 2.0f) {
                                this.mc.ingameGUI.drawTexturedModalRect((float)(this.getX() + 86) - target.getMaxHealth() / 2.0f * 10.0f / 2.0f + (float)(i * 8), (float)(this.getY() + 9), 16, 0, 9, 9);
                                ++i;
                            }
                            i = 0;
                            while ((float)i < target.getHealth() / 2.0f) {
                                this.mc.ingameGUI.drawTexturedModalRect((float)(this.getX() + 86) - target.getMaxHealth() / 2.0f * 10.0f / 2.0f + (float)(i * 8), (float)(this.getY() + 9), 52, 0, 9, 9);
                                ++i;
                            }
                            i3 = target.getTotalArmorValue();
                            for (k3 = 0; k3 < 10; ++k3) {
                                if (i3 <= 0) continue;
                                l3 = this.getX() + 36 + k3 * 8;
                                if (k3 * 2 + 1 < i3) {
                                    this.mc.ingameGUI.drawTexturedModalRect(l3, this.getY() + 20, 34, 9, 9, 9);
                                }
                                if (k3 * 2 + 1 == i3) {
                                    this.mc.ingameGUI.drawTexturedModalRect(l3, this.getY() + 20, 25, 9, 9, 9);
                                }
                                if (k3 * 2 + 1 <= i3) continue;
                                this.mc.ingameGUI.drawTexturedModalRect(l3, this.getY() + 20, 16, 9, 9, 9);
                            }
                            GlStateManager.popMatrix();
                        } else if (mode.equalsIgnoreCase("Skeet")) {
                            x = this.getX();
                            y = this.getY();
                            healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                            healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                            armorWid = target.getTotalArmorValue() * 6;
                            check = target.getHealth() < 18.0f && target.getHealth() > 1.0f ? 8.0 : 0.0;
                            this.healthBarWidth = healthWid;
                            RectHelper.drawSkeetRect(x + 10.0f, y + 60.0f, x + 115.0f, y);
                            if (!target.getName().isEmpty()) {
                                this.mc.verdanaFontRender.drawStringWithOutline(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 33.0f, y + 5.0f, -1);
                            }
                            healthPer = "" + MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100.0f, 1) + "%";
                            this.mc.verdanaFontRender.drawStringWithOutline("Health: " + healthPer, x + 33.0f, y + 15.0f, -1);
                            this.mc.verdanaFontRender.drawStringWithOutline("Ping: " + (this.mc.isSingleplayer() != false ? 0 : EntityHelper.getPing(target)), x + 33.0f, y + 25.0f, -1);
                            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 7);
                            this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 1);
                            GuiInventory.drawEntityOnScreen(x - 3.0f, y + 55.0f, 25, target.rotationYaw, -target.rotationPitch, target);
                            RectHelper.drawSkeetRectWithoutBorder(x + 12.0f, y + 62.0f, x - 15.0f, y - 2.0f);
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 42.0f, 120.0, 5.0, new Color(5, 5, 5).getRGB());
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 42.0f, this.healthBarWidth + check, 5.0, PaletteHelper.getHealthColor(target.getHealth(), target.getMaxHealth()).darker().darker().getRGB());
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 42.0f, healthWid, 5.0, PaletteHelper.getHealthColor(target.getHealth(), target.getMaxHealth()).darker().getRGB());
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 50.0f, 120.0, 5.0, new Color(5, 5, 5).getRGB());
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 50.0f, armorWid, 5.0, new Color(55, 175, 255).darker().getRGB());
                        } else if (mode.equalsIgnoreCase("Flux")) {
                            x = this.getX();
                            y = this.getY();
                            armorWid = target.getTotalArmorValue() * 6;
                            healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                            healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                            RectHelper.drawRectBetter(x, y, 125.0, 55.0, new Color(39, 39, 37, 235).getRGB());
                            pvpState = "";
                            if (this.mc.player.getHealth() == target.getHealth()) {
                                pvpState = "Finish Him!";
                            } else if (this.mc.player.getHealth() < target.getHealth()) {
                                pvpState = "Losing Fight";
                            } else if (this.mc.player.getHealth() > target.getHealth()) {
                                pvpState = "Winning Fight";
                            }
                            if (!target.getName().isEmpty()) {
                                this.mc.robotoRegularFontRender.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 38.0f, y + 6.0f, -1);
                                this.mc.clickguismall.drawStringWithShadow(pvpState, x + 38.0f, y + 17.0f, -1);
                            }
                            for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                GlStateManager.pushMatrix();
                                if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                    hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                    GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                    this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                    Gui.drawScaledCustomSizeModalRect(x + 1.5f, y + 1.5f, 8.0f, 8.0f, 8.0f, 8.0f, 34.0f, 34.0f, 64.0f, 64.0f);
                                    GlStateManager.bindTexture(0);
                                }
                                GL11.glDisable(3089);
                                GlStateManager.popMatrix();
                            }
                            RectHelper.drawRectBetter(x + 1.5f, y + 39.0f, 120.0, 4.0, new Color(26, 28, 25, 255).getRGB());
                            RectHelper.drawRectBetter(x + 1.5f, y + 39.0f, healthWid, 4.0, new Color(2, 145, 98, 255).getRGB());
                            RectHelper.drawRectBetter(x + 1.5f, y + 47.0f, 120.0, 4.0, new Color(26, 28, 25, 255).getRGB());
                            RectHelper.drawRectBetter(x + 1.5f, y + 47.0f, armorWid, 4.0, new Color(65, 138, 195, 255).getRGB());
                        }
                        break block71;
                    }
                    while (check.hasNext()) {
                        targetHead = check.next();
                        if (targetHead == null || !(target instanceof EntityPlayer)) continue;
                        if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                            hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                            GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                            this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            Gui.drawScaledCustomSizeModalRect((int)x + 2, (int)y + 1, 8.0f, 8.0f, 8.0f, 8.0f, 33.0f, 33.0f, 64.0f, 64.0f);
                            GlStateManager.bindTexture(0);
                        }
                        GL11.glDisable(3089);
                    }
                    GlStateManager.popMatrix();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                    break block71;
                }
                while (var15_85.hasNext()) {
                    targetHead = var15_85.next();
                    if (targetHead == null || !(target instanceof EntityPlayer)) continue;
                    if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                        hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                        RenderHelper.renderBlurredShadow(new Color(55, 175, 255).brighter(), (double)((int)x + 104), (double)((int)y), 44.0, 44.0, 8);
                        RenderHelper.renderBlurredShadow(Color.RED.brighter(), (double)((int)x + 104), (double)((int)y), 22.0, 44.0, 8);
                        GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                        this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                        Gui.drawScaledCustomSizeModalRect((int)x + 104, (int)y, 8.0f, 8.0f, 8.0f, 8.0f, 44.0f, 44.0f, 64.0f, 64.0f);
                        GlStateManager.bindTexture(0);
                    }
                    GL11.glDisable(3089);
                }
                GlStateManager.popMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
                break block71;
            }
            while (i.hasNext()) {
                effect = (PotionEffect)i.next();
                potion = Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()));
                if (!TargetHUDComponent.$assertionsDisabled && potion == null) {
                    throw new AssertionError();
                }
                name = I18n.format(potion.getName(), new Object[0]);
                PType = "";
                if (effect.getAmplifier() == 1) {
                    name = name + " 2";
                } else if (effect.getAmplifier() == 2) {
                    name = name + " 3";
                } else if (effect.getAmplifier() == 3) {
                    name = name + " 4";
                }
                if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                    PType = PType + " " + Potion.getDurationString(effect);
                } else if (effect.getDuration() < 300) {
                    PType = PType + " " + Potion.getDurationString(effect);
                } else if (effect.getDuration() > 600) {
                    PType = PType + " " + Potion.getDurationString(effect);
                }
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                this.mc.fontRendererObj.drawStringWithShadow(name + ":" + (Object)ChatFormatting.GRAY + PType, x + 1.0f, y2 - 9.0f, potion.getLiquidColor());
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
                y2 -= 10.0f;
            }
            for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                GlStateManager.pushMatrix();
                if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                    GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                    this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect((int)x + 1, (int)y + 1, 8.0f, 8.0f, 8.0f, 8.0f, 34.0f, 34.0f, 64.0f, 64.0f);
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
                GlStateManager.popMatrix();
            }
            RectHelper.drawRectBetter(x + 18.0f, y + 41.0f, 120.0, 3.0, new Color(20, 221, 32).darker().darker().darker().getRGB());
            RectHelper.drawRectBetter(x + 18.0f, y + 41.0f, this.healthBarWidth + check, 3.0, new Color(new Color(255, 55, 55).darker().getRGB()).getRGB());
            RectHelper.drawRectBetter(x + 18.0f, y + 41.0f, healthWid, 3.0, new Color(new Color(20, 221, 32).getRGB()).getRGB());
        }
        super.render(mouseX, mouseY);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void draw() {
        block72: {
            block81: {
                block76: {
                    block74: {
                        block80: {
                            block79: {
                                block78: {
                                    block77: {
                                        block75: {
                                            block73: {
                                                block71: {
                                                    if (this.mc.player == null) return;
                                                    if (this.mc.world == null) return;
                                                    if (!Celestial.instance.featureManager.getFeatureByClass(KillAura.class).getState()) {
                                                        return;
                                                    }
                                                    mode = KillAura.targetHudMode.getOptions();
                                                    target = KillAura.target;
                                                    color = KillAura.targetHudColor.getColor();
                                                    if (target == null) {
                                                        return;
                                                    }
                                                    if (!mode.equalsIgnoreCase("Astolfo")) break block71;
                                                    x = this.getX();
                                                    y = this.getY();
                                                    healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                                                    healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                                                    check = target.getHealth() < 18.0f && target.getHealth() > 1.0f ? 8.0 : 0.0;
                                                    this.healthBarWidth = healthWid;
                                                    top = (int)y;
                                                    left = (int)x;
                                                    RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)left, (double)top, 155.0, 60.0, 20);
                                                    RectHelper.drawRectBetter(x, y, 155.0, 60.0, new Color(20, 20, 20, 255).getRGB());
                                                    if (!target.getName().isEmpty()) {
                                                        this.mc.fontRendererObj.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 31.0f, y + 5.0f, -1);
                                                    }
                                                    GlStateManager.pushMatrix();
                                                    GlStateManager.translate(x, y, 1.0f);
                                                    GlStateManager.scale(2.5f, 2.5f, 2.5f);
                                                    GlStateManager.translate(-x - 3.0f, -y - 2.0f, 1.0f);
                                                    this.mc.fontRendererObj.drawStringWithShadow(MathematicHelper.round(target.getHealth() / 2.0f, 1) + " \u2764", x + 16.0f, y + 10.0f, new Color(color).getRGB());
                                                    GlStateManager.popMatrix();
                                                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                                    this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 7);
                                                    this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 1);
                                                    GuiInventory.drawEntityOnScreen(x + 16.0f, y + 55.0f, 25, target.rotationYaw, -target.rotationPitch, target);
                                                    RectHelper.drawRectBetter(x + 30.0f, y + 48.0f, 120.0, 8.0, new Color(color).darker().darker().darker().getRGB());
                                                    RectHelper.drawRectBetter(x + 30.0f, y + 48.0f, this.healthBarWidth + check, 8.0, new Color(color).darker().getRGB());
                                                    RectHelper.drawRectBetter(x + 30.0f, y + 48.0f, healthWid, 8.0, new Color(color).getRGB());
                                                    break block72;
                                                }
                                                if (!mode.equalsIgnoreCase("Red-Blue")) break block73;
                                                if (this.mc.gameSettings.ofFastRender) {
                                                    this.mc.gameSettings.ofFastRender = false;
                                                }
                                                x = this.getX();
                                                y = this.getY();
                                                armorWid = (float)target.getTotalArmorValue() * 4.8f;
                                                healthWid = target.getHealth() / target.getMaxHealth() * 96.0f;
                                                healthWid = MathHelper.clamp(healthWid, 0.0, 96.0);
                                                this.healthBarWidth = Interpolator.LINEAR.interpolate(this.healthBarWidth, (double)((float)healthWid), (double)(10.0f / (float)Minecraft.getDebugFPS()));
                                                this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, 104.0);
                                                left = x;
                                                top = y;
                                                right = left + 48.0f + 120.0f;
                                                GlStateManager.pushMatrix();
                                                GlStateManager.disableBlend();
                                                RenderHelper.blurAreaBoarder((int)x, (int)y, 148.0f, 45.0f, 120.0f, 0.0f, 1.0f);
                                                RectHelper.drawRect(x - 1.0f, y - 0.5f, right - 168.0f, y + 45.0f, new Color(205, 205, 205).getRGB());
                                                RenderHelper.renderBlurredShadow(Color.WHITE, (double)(x - 1.0f), (double)y, 2.0, 45.0, 6);
                                                if (target instanceof EntityPlayer) {
                                                    this.mc.robotoRegularFontRender.drawStringWithShadow(target.getName(), x + 8.0f, y + 5.0f, -1);
                                                    RenderHelper.renderBlurredShadow(Color.WHITE, (double)(x + 9.0f), (double)(y + 5.0f), (double)(this.mc.robotoRegularFontRender.getStringWidth(target.getName()) + 2), 4.0, 10);
                                                }
                                                RenderHelper.renderItem(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 133, (int)y - 20);
                                                if (Minecraft.getDebugFPS() > 5) {
                                                    RectHelper.drawSmoothRectBetter(x + 4.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 19.0f, 96.0, 5.0, new Color(35, 35, 35, 255).getRGB());
                                                    RectHelper.drawSmoothRectBetter(x + 4.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 19.0f, this.healthBarWidth, 5.0, Color.RED.getRGB());
                                                    RectHelper.drawSmoothRectBetter(x + 4.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 28.0f, 96.0, 5.0, new Color(35, 35, 35, 255).getRGB());
                                                    if (target.getTotalArmorValue() > 0) {
                                                        RectHelper.drawSmoothRectBetter(x + 4.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 28.0f, armorWid, 5.0, new Color(55, 175, 255).getRGB());
                                                    }
                                                    if (target.getTotalArmorValue() > 0) {
                                                        RenderHelper.renderBlurredShadow(new Color(55, 175, 255).brighter(), (double)(x + 3.0f), (double)(y + 37.0f), armorWid, 5.0, 9);
                                                    }
                                                    RenderHelper.renderBlurredShadow(Color.RED.brighter(), (double)((int)x + 3), (double)((int)y + 28), (double)((int)this.healthBarWidth + 4), 5.0, 9);
                                                }
                                                GlStateManager.pushMatrix();
                                                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                                var13_71 = this.mc.player.connection.getPlayerInfoMap().iterator();
                                                break block74;
                                            }
                                            if (!mode.equalsIgnoreCase("Moon Dev")) break block75;
                                            if (this.mc.gameSettings.ofFastRender) {
                                                this.mc.gameSettings.ofFastRender = false;
                                            }
                                            x = this.getX();
                                            y = this.getY();
                                            right = 115 + this.mc.latoBig.getStringWidth(target.getName()) - 50;
                                            healthWid = target.getHealth() / target.getMaxHealth() * (float)(69 + this.mc.latoBig.getStringWidth(target.getName()) - 9);
                                            this.healthBarWidth = Interpolator.LINEAR.interpolate(this.healthBarWidth, (double)healthWid, (double)(10.0f / (float)Minecraft.getDebugFPS()));
                                            this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, (double)(69 + this.mc.latoBig.getStringWidth(target.getName()) - 9));
                                            GlStateManager.pushMatrix();
                                            GlStateManager.disableBlend();
                                            RenderHelper.blurAreaBoarder((int)x, y - 0.5f, right, 40.0f, 45.0f, 0.0f, 1.0f);
                                            if (target instanceof EntityPlayer) {
                                                this.mc.latoBig.drawStringWithShadow((Object)ChatFormatting.BOLD + target.getName(), x + 37.0f, y + 2.0f, -1);
                                                this.mc.fontRenderer.drawStringWithShadow("Health: " + MathematicHelper.round(target.getHealth(), 1), x + 37.0f, y + 15.0f, -1);
                                                this.mc.fontRenderer.drawStringWithShadow("Distance: " + MathematicHelper.round(this.mc.player.getDistanceToEntity(target), 1) + "m", x + 37.0f, y + 25.0f, -1);
                                            }
                                            RenderHelper.renderItem(target.getHeldItem(EnumHand.OFF_HAND), (int)x + (int)right - 15, (int)y - 20);
                                            RectHelper.drawRectBetter(x + 2.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 26.0f, right - 5.0f, 2.0, new Color(35, 35, 35, 255).getRGB());
                                            RectHelper.drawRect(x + 2.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 26.0f, (double)(x + 2.0f) + this.healthBarWidth, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 26.0f + 2.0f, PaletteHelper.getHealthColor(target.getHealth(), target.getMaxHealth()).getRGB());
                                            GlStateManager.pushMatrix();
                                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                            healthWid = this.mc.player.connection.getPlayerInfoMap().iterator();
                                            break block76;
                                        }
                                        if (!mode.equalsIgnoreCase("Glow")) break block77;
                                        GlStateManager.pushMatrix();
                                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                        GlStateManager.enableAlpha();
                                        GlStateManager.disableBlend();
                                        x = this.getX();
                                        y = this.getY();
                                        healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                                        healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                                        this.healthBarWidth = Interpolator.LINEAR.interpolate(this.healthBarWidth, (double)((float)healthWid), (double)(10.0f / (float)Minecraft.getDebugFPS()));
                                        this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, 124.0);
                                        left = x;
                                        top = y;
                                        right = left + 48.0f + 120.0f;
                                        RenderHelper.renderBlurredShadow(new Color(20, 20, 20, 200), (double)((int)x), (double)((int)y), 168.0, 56.0, 6);
                                        RenderHelper.renderBlurredShadow(new Color(KillAura.targetHudColor.getColor()).brighter(), (double)((int)x), (double)((int)y), 168.0, 3.0, 15);
                                        RectHelper.drawSmoothRect(x, y + 1.0f, right, y, new Color(KillAura.targetHudColor.getColor()).getRGB());
                                        RectHelper.drawRect(x + 3.0f, y + 14.0f, right - 3.0f, (double)y + 14.5, new Color(205, 205, 205).getRGB());
                                        this.mc.comfortaa.drawCenteredStringWithShadow(target.getName(), x + 85.0f, y + 5.0f, -1);
                                        RenderHelper.renderItem(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 148, (int)y + 15);
                                        endValue = MathHelper.clamp((float)target.hurtTime / 6.5f, 0.1f, this.hurtAnim * 60.0f);
                                        this.hurtAnim = (float)Interpolator.LINEAR.interpolate((double)this.hurtAnim, (double)endValue, (double)(5.0f / (float)Minecraft.getDebugFPS()));
                                        if (String.valueOf(this.hurtAnim).equals("NaN")) {
                                            this.hurtAnim = target.hurtTime;
                                            this.healthBarWidth = target.getHealth();
                                        }
                                        if (this.hurtAnim < 0.0f) {
                                            this.hurtAnim = 0.0f;
                                        }
                                        if (Minecraft.getDebugFPS() > 5) {
                                            RectHelper.drawRectBetter(left + 87.0f, top + 34.0f, 60.0, 2.0, new Color(35, 35, 35, 255).getRGB());
                                            RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)((int)x + 87), (double)((int)y + 34), (double)(this.hurtAnim * 60.0f), 2.0, 7);
                                            this.mc.fontRenderer.drawStringWithShadow("Ground: " + target.onGround, left + 41.0f, top + 20.0f, -1);
                                            RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)((int)x + 76), (double)((int)y + 22), target.onGround != false ? 16.0 : 19.0, 2.0, 10);
                                            this.mc.fontRenderer.drawStringWithShadow("HurtTime", left + 41.0f, top + 31.0f, -1);
                                            RectHelper.drawSmoothRectBetter(left + 87.0f, top + 34.0f, this.hurtAnim * 60.0f, 2.0, new Color(KillAura.targetHudColor.getColor()).getRGB());
                                            RectHelper.drawSmoothRectBetter(x + 41.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 35.0f, 120.0, 5.0, new Color(35, 35, 35, 255).getRGB());
                                            RectHelper.drawSmoothRectBetter(x + 41.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 35.0f, this.healthBarWidth, 5.0, new Color(color).getRGB());
                                            RenderHelper.renderBlurredShadow(new Color(color).brighter(), (double)((int)x + 40), (double)((int)y + 44), (double)((int)this.healthBarWidth + 4), 5.0, 10);
                                        }
                                        GlStateManager.pushMatrix();
                                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                        for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                            if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                                hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                                GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                                this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                                Gui.drawScaledCustomSizeModalRect((int)x + 3, (int)y + 17, 8.0f, 8.0f, 8.0f, 8.0f, 34.0f, 34.0f, 64.0f, 64.0f);
                                                GlStateManager.bindTexture(0);
                                            }
                                            GL11.glDisable(3089);
                                        }
                                        GlStateManager.popMatrix();
                                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                        GlStateManager.popMatrix();
                                        break block72;
                                    }
                                    if (!mode.equalsIgnoreCase("Novoline Old")) break block78;
                                    if (target.getHealth() < 0.0f) {
                                        return;
                                    }
                                    x = this.getX();
                                    y = this.getY();
                                    healthWid = target.getHealth() / target.getMaxHealth() * (float)(40 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()));
                                    healthWid = (float)MathHelper.clamp((double)healthWid, 0.0, 124.0);
                                    this.healthBarWidth = AnimationHelper.calculateCompensation(healthWid, (float)this.healthBarWidth, 5.0f, 5.0);
                                    this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, 124.0);
                                    RectHelper.drawRectBetter(x, y, 65 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 25, 40.0, new Color(19, 19, 19, 255).getRGB());
                                    RectHelper.drawRectBetter(x + 1.0f, y + 1.0f, 65 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 23, 38.0, new Color(41, 41, 41, 255).getRGB());
                                    if (!target.getName().isEmpty()) {
                                        this.mc.fontRendererObj.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 42.0f, y + 5.0f, -1);
                                    }
                                    GlStateManager.pushMatrix();
                                    GlStateManager.translate(x, y, 1.0f);
                                    GlStateManager.scale(1.05f, 1.05f, 1.05f);
                                    GlStateManager.translate(-x + 24.0f, -y + 18.0f, 1.0f);
                                    hp = MathematicHelper.round(target.getHealth() / 2.0f, 1) + "";
                                    this.mc.fontRendererObj.drawStringWithShadow(hp, x + 17.0f, y + 10.0f, -1);
                                    this.mc.fontRendererObj.drawStringWithShadow(" \u2764", x + (float)this.mc.fontRendererObj.getStringWidth(hp) + 16.0f, y + 10.0f, new Color(color).getRGB());
                                    GlStateManager.popMatrix();
                                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                    GlStateManager.pushMatrix();
                                    GlStateManager.translate(x, y, 1.0f);
                                    GlStateManager.scale(0.8f, 0.8f, 0.8f);
                                    GlStateManager.translate(-x + 148.0f, -y + 38.0f, 1.0f);
                                    v0 = stack = target.getHeldItemOffhand().isStackable() != false && target.getHeldItemOffhand().isEmpty() == false;
                                    if (stack) {
                                        this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 39.0f + (float)this.mc.fontRenderer.getStringWidth(hp) - 21.0f), (int)(y - 8.0f));
                                        this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 49.0f + (float)this.mc.fontRenderer.getStringWidth(hp) - 21.0f), (int)y - 5);
                                    }
                                    GlStateManager.popMatrix();
                                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                    for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                        GlStateManager.pushMatrix();
                                        if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                            hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                            GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                            this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                            Gui.drawScaledCustomSizeModalRect((int)x + 1, (int)y + 1, 8.0f, 8.0f, 8.0f, 8.0f, 38.0f, 38.0f, 64.0f, 64.0f);
                                            GlStateManager.bindTexture(0);
                                        }
                                        GL11.glDisable(3089);
                                        GlStateManager.popMatrix();
                                    }
                                    RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, 40 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()), 8.0, new Color(35, 35, 35, 255).getRGB());
                                    RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, target.getHealth() > 18.0f ? (double)healthWid : this.healthBarWidth + 4.0, 8.0, new Color(color).darker().getRGB());
                                    RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, healthWid, 8.0, new Color(color).getRGB());
                                    startAlpha = 0.25f;
                                    size = 10;
                                    top = (int)y + 1;
                                    left = (int)x + 1;
                                    right = (int)(x + 66.0f + (float)this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 23.0f);
                                    bottom = (int)y + 39;
                                    RectHelper.renderShadowHorizontal(Color.BLACK, 2.0f, startAlpha, size, top, left, right, true, true);
                                    RectHelper.renderShadowHorizontal(Color.BLACK, 2.0f, startAlpha, size, bottom, left, right, false, true);
                                    RectHelper.renderShadowVertical(Color.BLACK, 2.0f, startAlpha, size, right, top, bottom, true, true);
                                    RectHelper.renderShadowVertical(Color.BLACK, 2.0f, startAlpha, size, left, (double)top + 0.5, (double)bottom + 0.5, false, true);
                                    break block72;
                                }
                                if (!mode.equalsIgnoreCase("Novoline New")) break block79;
                                if (target == null) {
                                    return;
                                }
                                if (target.getHealth() < 0.0f) {
                                    return;
                                }
                                x = this.getX();
                                y = this.getY();
                                healthWid = target.getHealth() / target.getMaxHealth() * (float)(40 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()));
                                healthWid = (float)MathHelper.clamp((double)healthWid, 0.0, 124.0);
                                this.healthBarWidth = AnimationHelper.calculateCompensation(healthWid, (float)this.healthBarWidth, 5.0f, 5.0);
                                this.healthBarWidth = MathHelper.clamp(this.healthBarWidth, 0.0, 124.0);
                                RectHelper.drawRectBetter(x, y, 65 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 25, 40.0, new Color(19, 19, 19, 255).getRGB());
                                RectHelper.drawRectBetter(x + 1.0f, y + 1.0f, 65 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 23, 38.0, new Color(41, 41, 41, 255).getRGB());
                                if (!target.getName().isEmpty()) {
                                    this.mc.fontRendererObj.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 42.0f, y + 5.0f, -1);
                                }
                                GlStateManager.pushMatrix();
                                GlStateManager.translate(x, y, 1.0f);
                                GlStateManager.scale(1.05f, 1.05f, 1.05f);
                                GlStateManager.translate(-x + 24.0f, -y + 18.0f, 1.0f);
                                GlStateManager.popMatrix();
                                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                GlStateManager.pushMatrix();
                                GlStateManager.translate(x, y, 1.0f);
                                GlStateManager.scale(0.8f, 0.8f, 0.8f);
                                GlStateManager.translate(-x + 148.0f, -y + 38.0f, 1.0f);
                                v1 = stack = target.getHeldItemOffhand().isStackable() != false && target.getHeldItemOffhand().isEmpty() == false;
                                if (stack) {
                                    this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 88.0f), (int)(y - 8.0f));
                                    this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)(x - 98.0f), (int)y - 5);
                                }
                                GlStateManager.popMatrix();
                                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                                for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                    GlStateManager.pushMatrix();
                                    if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                        hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                        GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                        this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                        Gui.drawScaledCustomSizeModalRect((int)x + 1, (int)y + 1, 8.0f, 8.0f, 8.0f, 8.0f, 38.0f, 38.0f, 64.0f, 64.0f);
                                        GlStateManager.bindTexture(0);
                                    }
                                    GL11.glDisable(3089);
                                    GlStateManager.popMatrix();
                                }
                                hp = MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100.0f, 1) + "%";
                                RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, 40 + this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()), 10.0, new Color(35, 35, 35, 255).getRGB());
                                RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, target.getHealth() > 18.0f ? (double)healthWid : this.healthBarWidth + 4.0, 10.0, new Color(color).darker().getRGB());
                                RectHelper.drawRectBetter(x + 42.0f, y + (float)this.mc.fontRendererObj.getStringHeight(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 9.0f, healthWid, 10.0, new Color(color).getRGB());
                                startAlpha = 0.25f;
                                size = 10;
                                top = (int)y + 1;
                                left = (int)x + 1;
                                right = (int)(x + 66.0f + (float)this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) + 23.0f);
                                bottom = (int)y + 39;
                                RectHelper.renderShadowHorizontal(Color.BLACK, 2.0f, startAlpha, size, top, left, right, true, true);
                                RectHelper.renderShadowHorizontal(Color.BLACK, 2.0f, startAlpha, size, bottom, left, right, false, true);
                                RectHelper.renderShadowVertical(Color.BLACK, 2.0f, startAlpha, size, right, top, bottom, true, true);
                                RectHelper.renderShadowVertical(Color.BLACK, 2.0f, startAlpha, size, left, (double)top + 0.5, (double)bottom + 0.5, false, true);
                                this.mc.fontRendererObj.drawStringWithShadow(hp, x + (float)this.mc.fontRendererObj.getStringWidth(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName()) / 2.0f + 50.0f, y + 19.0f, -1);
                                break block72;
                            }
                            if (!mode.equalsIgnoreCase("Dev")) break block80;
                            x = this.getX();
                            y = this.getY();
                            x2 = this.getX();
                            y2 = this.getY();
                            healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                            healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                            if (target == null) ** GOTO lbl-1000
                            v2 = target.getHealth();
                            v3 = target instanceof EntityPlayer != false ? 18 : 10;
                            if (v2 < (float)v3 && target.getHealth() > 1.0f) {
                                v4 = 8.0;
                            } else lbl-1000:
                            // 2 sources

                            {
                                v4 = 0.0;
                            }
                            check = v4;
                            this.healthBarWidth = AnimationHelper.calculateCompensation((float)healthWid, (float)this.healthBarWidth, 0.0f, 0.005);
                            RectHelper.drawRectBetter(x, y, 145.0, 50.0, new Color(23, 23, 25, 203).getRGB());
                            if (!target.getName().isEmpty()) {
                                this.mc.robotoRegularFontRender.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 37.0f, y + 5.0f, -1);
                            }
                            GlStateManager.pushMatrix();
                            GlStateManager.translate(x, y, 1.0f);
                            GlStateManager.scale(1.5f, 1.5f, 1.5f);
                            GlStateManager.translate(-x - 14.0f, -y + 14.0f, 1.0f);
                            this.mc.fontRendererObj.drawStringWithShadow("\u00a7c\u2764", x + 16.0f, y + 10.0f, -1);
                            GlStateManager.popMatrix();
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 125, (int)y + 7);
                            this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 125, (int)y + 1);
                            list = new ArrayList<ItemStack>();
                            for (i = 0; i < 5; ++i) {
                                armorSlot = ((EntityPlayer)target).getEquipmentInSlot(i);
                                if (armorSlot == null) continue;
                                list.add(armorSlot);
                            }
                            for (ItemStack itemStack : list) {
                                net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
                                RenderHelper.renderItem(itemStack, (int)x2 + 36, (int)(y + 16.0f));
                                x2 += 16.0f;
                            }
                            i = target.getActivePotionEffects().iterator();
                            break block81;
                        }
                        if (mode.equalsIgnoreCase("Minecraft")) {
                            if (target == null) {
                                return;
                            }
                            x = this.getX();
                            y = this.getY();
                            GlStateManager.pushMatrix();
                            RectHelper.drawRectWithEdge(x - 2.0f, y - 7.0f, 155.0, 38.0, new Color(20, 20, 20, 255), new Color(255, 255, 255, 255));
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            this.mc.fontRendererObj.drawStringWithShadow(target.getName(), this.getX() + 37, this.getY() - 2, -1);
                            for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                GlStateManager.pushMatrix();
                                if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                    hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                    GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                    this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                    Gui.drawScaledCustomSizeModalRect((int)x, (int)y - 5, 8.0f, 8.0f, 8.0f, 8.0f, 34.0f, 34.0f, 64.0f, 64.0f);
                                    GlStateManager.bindTexture(0);
                                }
                                GL11.glDisable(3089);
                                GlStateManager.popMatrix();
                            }
                            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 135, (int)y + 2);
                            this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 136, (int)y - 6);
                            this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
                            i = 0;
                            while ((float)i < target.getMaxHealth() / 2.0f) {
                                this.mc.ingameGUI.drawTexturedModalRect((float)(this.getX() + 86) - target.getMaxHealth() / 2.0f * 10.0f / 2.0f + (float)(i * 8), (float)(this.getY() + 9), 16, 0, 9, 9);
                                ++i;
                            }
                            i = 0;
                            while ((float)i < target.getHealth() / 2.0f) {
                                this.mc.ingameGUI.drawTexturedModalRect((float)(this.getX() + 86) - target.getMaxHealth() / 2.0f * 10.0f / 2.0f + (float)(i * 8), (float)(this.getY() + 9), 52, 0, 9, 9);
                                ++i;
                            }
                            i3 = target.getTotalArmorValue();
                            for (k3 = 0; k3 < 10; ++k3) {
                                if (i3 <= 0) continue;
                                l3 = this.getX() + 36 + k3 * 8;
                                if (k3 * 2 + 1 < i3) {
                                    this.mc.ingameGUI.drawTexturedModalRect(l3, this.getY() + 20, 34, 9, 9, 9);
                                }
                                if (k3 * 2 + 1 == i3) {
                                    this.mc.ingameGUI.drawTexturedModalRect(l3, this.getY() + 20, 25, 9, 9, 9);
                                }
                                if (k3 * 2 + 1 <= i3) continue;
                                this.mc.ingameGUI.drawTexturedModalRect(l3, this.getY() + 20, 16, 9, 9, 9);
                            }
                            GlStateManager.popMatrix();
                        } else if (mode.equalsIgnoreCase("Skeet")) {
                            x = this.getX();
                            y = this.getY();
                            healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                            healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                            armorWid = target.getTotalArmorValue() * 6;
                            check = target.getHealth() < 18.0f && target.getHealth() > 1.0f ? 8.0 : 0.0;
                            this.healthBarWidth = healthWid;
                            RectHelper.drawSkeetRect(x + 10.0f, y + 60.0f, x + 115.0f, y);
                            if (!target.getName().isEmpty()) {
                                this.mc.verdanaFontRender.drawStringWithOutline(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 33.0f, y + 5.0f, -1);
                            }
                            healthPer = "" + MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100.0f, 1) + "%";
                            this.mc.verdanaFontRender.drawStringWithOutline("Health: " + healthPer, x + 33.0f, y + 15.0f, -1);
                            this.mc.verdanaFontRender.drawStringWithOutline("Ping: " + (this.mc.isSingleplayer() != false ? 0 : EntityHelper.getPing((EntityPlayer)target)), x + 33.0f, y + 25.0f, -1);
                            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 7);
                            this.mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)x + 137, (int)y + 1);
                            GuiInventory.drawEntityOnScreen(x - 3.0f, y + 55.0f, 25, target.rotationYaw, -target.rotationPitch, target);
                            RectHelper.drawSkeetRectWithoutBorder(x + 12.0f, y + 62.0f, x - 15.0f, y - 2.0f);
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 42.0f, 120.0, 5.0, new Color(5, 5, 5).getRGB());
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 42.0f, this.healthBarWidth + check, 5.0, PaletteHelper.getHealthColor(target.getHealth(), target.getMaxHealth()).darker().darker().getRGB());
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 42.0f, healthWid, 5.0, PaletteHelper.getHealthColor(target.getHealth(), target.getMaxHealth()).darker().getRGB());
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 50.0f, 120.0, 5.0, new Color(5, 5, 5).getRGB());
                            RectHelper.drawSmoothRectBetter(x + 30.0f, y + 50.0f, armorWid, 5.0, new Color(55, 175, 255).darker().getRGB());
                        } else if (mode.equalsIgnoreCase("Flux")) {
                            x = this.getX();
                            y = this.getY();
                            armorWid = target.getTotalArmorValue() * 6;
                            healthWid = target.getHealth() / target.getMaxHealth() * 120.0f;
                            healthWid = MathHelper.clamp(healthWid, 0.0, 120.0);
                            RectHelper.drawRectBetter(x, y, 125.0, 55.0, new Color(39, 39, 37, 235).getRGB());
                            RectHelper.drawRectBetter(x, y, 125.0, 55.0, new Color(39, 39, 37, 235).getRGB());
                            pvpState = "";
                            if (this.mc.player.getHealth() == target.getHealth()) {
                                pvpState = "Finish Him!";
                            } else if (this.mc.player.getHealth() < target.getHealth()) {
                                pvpState = "Losing Fight";
                            } else if (this.mc.player.getHealth() > target.getHealth()) {
                                pvpState = "Winning Fight";
                            }
                            if (!target.getName().isEmpty()) {
                                this.mc.robotoRegularFontRender.drawStringWithShadow(Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() != false && StreamerMode.otherNames.getCurrentValue() != false ? "Protected" : target.getName(), x + 38.0f, y + 6.0f, -1);
                                this.mc.clickguismall.drawStringWithShadow(pvpState, x + 38.0f, y + 17.0f, -1);
                            }
                            for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                                GlStateManager.pushMatrix();
                                if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                                    hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                                    GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                                    this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                                    Gui.drawScaledCustomSizeModalRect(x + 1.5f, y + 1.5f, 8.0f, 8.0f, 8.0f, 8.0f, 34.0f, 34.0f, 64.0f, 64.0f);
                                    GlStateManager.bindTexture(0);
                                }
                                GL11.glDisable(3089);
                                GlStateManager.popMatrix();
                            }
                            RectHelper.drawRectBetter(x + 1.5f, y + 39.0f, 120.0, 4.0, new Color(26, 28, 25, 255).getRGB());
                            RectHelper.drawRectBetter(x + 1.5f, y + 39.0f, healthWid, 4.0, new Color(2, 145, 98, 255).getRGB());
                            RectHelper.drawRectBetter(x + 1.5f, y + 47.0f, 120.0, 4.0, new Color(26, 28, 25, 255).getRGB());
                            RectHelper.drawRectBetter(x + 1.5f, y + 47.0f, armorWid, 4.0, new Color(65, 138, 195, 255).getRGB());
                        }
                        break block72;
                    }
                    while (var13_71.hasNext()) {
                        targetHead = var13_71.next();
                        if (targetHead == null || !(target instanceof EntityPlayer)) continue;
                        if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                            hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                            RenderHelper.renderBlurredShadow(new Color(55, 175, 255).brighter(), (double)((int)x + 104), (double)((int)y), 44.0, 44.0, 8);
                            RenderHelper.renderBlurredShadow(Color.RED.brighter(), (double)((int)x + 104), (double)((int)y), 22.0, 44.0, 8);
                            GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                            this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            Gui.drawScaledCustomSizeModalRect((int)x + 104, (int)y, 8.0f, 8.0f, 8.0f, 8.0f, 44.0f, 44.0f, 64.0f, 64.0f);
                            GlStateManager.bindTexture(0);
                        }
                        GL11.glDisable(3089);
                    }
                    GlStateManager.popMatrix();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                    break block72;
                }
                while (healthWid.hasNext()) {
                    targetHead = healthWid.next();
                    if (targetHead == null || !(target instanceof EntityPlayer)) continue;
                    if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                        hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                        GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                        this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                        Gui.drawScaledCustomSizeModalRect((int)x + 2, (int)y + 1, 8.0f, 8.0f, 8.0f, 8.0f, 33.0f, 33.0f, 64.0f, 64.0f);
                        GlStateManager.bindTexture(0);
                    }
                    GL11.glDisable(3089);
                }
                GlStateManager.popMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
                break block72;
            }
            while (i.hasNext()) {
                effect = (PotionEffect)i.next();
                potion = Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()));
                if (!TargetHUDComponent.$assertionsDisabled && potion == null) {
                    throw new AssertionError();
                }
                name = I18n.format(potion.getName(), new Object[0]);
                PType = "";
                if (effect.getAmplifier() == 1) {
                    name = name + " 2";
                } else if (effect.getAmplifier() == 2) {
                    name = name + " 3";
                } else if (effect.getAmplifier() == 3) {
                    name = name + " 4";
                }
                if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                    PType = PType + " " + Potion.getDurationString(effect);
                } else if (effect.getDuration() < 300) {
                    PType = PType + " " + Potion.getDurationString(effect);
                } else if (effect.getDuration() > 600) {
                    PType = PType + " " + Potion.getDurationString(effect);
                }
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                this.mc.fontRendererObj.drawStringWithShadow(name + ":" + (Object)ChatFormatting.GRAY + PType, x + 1.0f, y2 - 9.0f, potion.getLiquidColor());
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
                y2 -= 10.0f;
            }
            for (NetworkPlayerInfo targetHead : this.mc.player.connection.getPlayerInfoMap()) {
                GlStateManager.pushMatrix();
                if (this.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    hurtPercent = ((float)target.hurtTime - this.mc.timer.renderPartialTicks) / 8.0f;
                    GlStateManager.color(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                    this.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect((int)x + 1, (int)y + 1, 8.0f, 8.0f, 8.0f, 8.0f, 34.0f, 34.0f, 64.0f, 64.0f);
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
                GlStateManager.popMatrix();
            }
            RectHelper.drawRectBetter(x + 18.0f, y + 41.0f, 120.0, 3.0, new Color(20, 221, 32).darker().darker().darker().getRGB());
            RectHelper.drawRectBetter(x + 18.0f, y + 41.0f, this.healthBarWidth + check, 3.0, new Color(new Color(255, 55, 55).darker().getRGB()).getRGB());
            RectHelper.drawRectBetter(x + 18.0f, y + 41.0f, healthWid, 3.0, new Color(new Color(20, 221, 32).getRGB()).getRGB());
        }
        super.draw();
    }
}

