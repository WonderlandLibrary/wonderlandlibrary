package org.dreamcore.client.ui.components.draggable.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import optifine.CustomColors;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.impl.combat.KillAura;
import org.dreamcore.client.feature.impl.misc.StreamerMode;
import org.dreamcore.client.helpers.math.MathematicHelper;
import org.dreamcore.client.helpers.render.AnimationHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.ui.components.draggable.DraggableModule;

import java.awt.*;
import java.util.ArrayList;

public class TargetHUDComponent extends DraggableModule {

    private float healthBarWidth;

    private long changeTime;
    private float displayPercent;
    private long lastUpdate;

    public TargetHUDComponent() {
        super("TargetHUDComponent", 200, 200);
    }

    @Override
    public int getWidth() {
        return 155;
    }

    @Override
    public int getHeight() {
        return 87;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        String mode = KillAura.targetHudMode.getOptions();
        EntityPlayer target = mc.player;
        int color = KillAura.targetHudColor.getColorValue();
        if (mode.equalsIgnoreCase("Astolfo")) {
            float x = getX(), y = getY();
            double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
            healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
            double check = target.getHealth() < 18 && target.getHealth() > 1 ? 8 : 0;
            healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, (long) 0.005, 0.005);
            RectHelper.drawRectBetter(x, y, 155, 60, new Color(20, 20, 20, 200).getRGB());
            if (!target.getName().isEmpty()) {
                mc.fontRendererObj.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 31, y + 5, -1);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(2.5f, 2.5f, 2.5f);
            GlStateManager.translate(-x - 3, -y - 2, 1);
            mc.fontRendererObj.drawStringWithShadow(MathematicHelper.round((target.getHealth() / 2.0f), 1) + " \u2764", x + 16, y + 10, new Color(color).getRGB());
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);

            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 7);
            mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 1);

            GuiInventory.drawEntityOnScreen(x + 16, y + 55, 25, target.rotationYaw, -target.rotationPitch, target);
            RectHelper.drawRectBetter(x + 30, y + 48, 120, 8, new Color(color).darker().darker().darker().getRGB());
            RectHelper.drawRectBetter(x + 30, y + 48, (float) (healthBarWidth + check), 8, new Color(color).darker().getRGB());
            RectHelper.drawRectBetter(x + 30, y + 48, (float) healthWid, 8, new Color(color).getRGB());
        } else if (mode.equalsIgnoreCase("Novoline Old")) {
            if (target == null)
                return;
            if (target.getHealth() < 0)
                return;
            float x = getX(), y = getY();
            float healthWid = (target.getHealth() / target.getMaxHealth() * (40 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName())));
            healthWid = MathHelper.clamp(healthWid, 0, 124);
            healthBarWidth = AnimationHelper.calculateCompensation(healthWid, healthBarWidth, 0, 0);
            healthBarWidth = MathHelper.clamp(healthBarWidth, 0, 124);
            RectHelper.drawRectBetter(x, y, 65 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 25, 40, new Color(19, 19, 19, 255).getRGB());
            RectHelper.drawRectBetter(x + 1, y + 1, 65 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 23, 38, new Color(41, 41, 41, 255).getRGB());
            if (!target.getName().isEmpty()) {
                mc.fontRendererObj.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 42, y + 5, -1);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(1.05f, 1.05f, 1.05f);
            GlStateManager.translate(-x + 24, -y + 18, 1);
            String hp = MathematicHelper.round((target.getHealth() / 2.0f), 1) + "";
            mc.fontRendererObj.drawStringWithShadow(hp, x + 17, y + 10, -1);
            mc.fontRendererObj.drawStringWithShadow(" \u2764", x + mc.fontRendererObj.getStringWidth(hp) + 16, y + 10, new Color(color).getRGB());
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(0.8f, 0.8f, 0.8f);
            GlStateManager.translate(-x + 148, -y + 38, 1);
            boolean stack = target.getHeldItemOffhand().isStackable() && !target.getHeldItemOffhand().isEmpty();
            if (stack) {
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) (x - 39 + mc.fontRenderer.getStringWidth(hp) - 21), (int) (y - 8));
                mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) (x - 49 + mc.fontRenderer.getStringWidth(hp) - 21), (int) y - 5);
            }
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);
            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect((int) x + 1, (int) y + 1, 8.0f, 8.0f, 8, 8, 38, 38, 64.0f, 64.0f);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, 40 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()), 8, new Color(35, 35, 35, 255).getRGB());
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, target.getHealth() > 18 ? healthWid : healthBarWidth + 4, 8, new Color(color).darker().getRGB());
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, healthWid, 8, new Color(color).getRGB());
        } else if (mode.equalsIgnoreCase("Novoline New")) {
            if (target == null)
                return;
            if (target.getHealth() < 0)
                return;
            float x = getX(), y = getY();
            float healthWid = (target.getHealth() / target.getMaxHealth() * (40 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName())));
            healthWid = (float) MathHelper.clamp(healthWid, 0.0D, 124.0D);
            healthBarWidth = AnimationHelper.calculateCompensation(healthWid, healthBarWidth, 0, 0);
            healthBarWidth = MathHelper.clamp(healthBarWidth, 0, 124);
            RectHelper.drawRectBetter(x, y, 65 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 25, 40, new Color(19, 19, 19, 255).getRGB());
            RectHelper.drawRectBetter(x + 1, y + 1, 65 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 23, 38, new Color(41, 41, 41, 255).getRGB());
            if (!target.getName().isEmpty()) {
                mc.fontRendererObj.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 42, y + 5, -1);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(1.05f, 1.05f, 1.05f);
            GlStateManager.translate(-x + 24, -y + 18, 1);
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(0.8f, 0.8f, 0.8f);
            GlStateManager.translate(-x + 148, -y + 38, 1);
            boolean stack = target.getHeldItemOffhand().isStackable() && !target.getHeldItemOffhand().isEmpty();
            if (stack) {
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) (x - 88), (int) (y - 8));
                mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) (x - 98), (int) y - 5);
            }
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);
            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect((int) x + 1, (int) y + 1, 8.0f, 8.0f, 8, 8, 38, 38, 64.0f, 64.0f);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }
            String hp = MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100F, 1) + "%";
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, 40 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()), 10, new Color(35, 35, 35, 255).getRGB());
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, target.getHealth() > 18 ? healthWid : healthBarWidth + 4, 10, new Color(color).darker().getRGB());
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, healthWid, 10, new Color(color).getRGB());
            mc.fontRendererObj.drawStringWithShadow(hp, x + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) / 2F + 50, y + 19, -1);
        } else if (mode.equalsIgnoreCase("Dev")) {
            float x = getX(), y = getY();
            float x2 = getX(), y2 = getY();
            double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
            healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
            double check = target != null && target.getHealth() < (target instanceof EntityPlayer ? 18 : 10) && target.getHealth() > 1 ? 8 : 0;
            healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, (long) 0.005, 0.005);
            RectHelper.drawRectBetter(x, y, 145, 50, new Color(23, 23, 25, 203).getRGB());
            if (!target.getName().isEmpty()) {
                mc.robotoRegularFontRender.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 37, y + 5, -1);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(1.5F, 1.5F, 1.5F);
            GlStateManager.translate(-x - 14, -y + 14, 1);
            mc.fontRendererObj.drawStringWithShadow("§c\u2764", x + 16, y + 10, -1);
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);

            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 125, (int) y + 7);
            mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 125, (int) y + 1);

            ArrayList<ItemStack> list = new ArrayList<>();
            for (int i = 0; i < 5; ++i) {
                ItemStack armorSlot = target.getEquipmentInSlot(i);
                if (armorSlot != null) {
                    list.add(armorSlot);
                }
            }
            for (ItemStack itemStack : list) {
                RenderHelper.enableGUIStandardItemLighting();
                org.dreamcore.client.helpers.render.RenderHelper.renderItem(itemStack, (int) x2 + 36, (int) (y + 16));
                x2 += 16;
            }

            for (PotionEffect effect : target.getActivePotionEffects()) {
                Potion potion = Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()));
                assert potion != null;
                String name = I18n.format(potion.getName());
                String PType = "";
                if (effect.getAmplifier() == 1) {
                    name = name + " 2";
                } else if (effect.getAmplifier() == 2) {
                    name = name + " 3";
                } else if (effect.getAmplifier() == 3) {
                    name = name + " 4";
                }
                if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
                    PType = PType + " " + Potion.getDurationString(effect);
                } else if (effect.getDuration() < 300) {
                    PType = PType + " " + Potion.getDurationString(effect);
                } else if (effect.getDuration() > 600) {
                    PType = PType + " " + Potion.getDurationString(effect);
                }
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                mc.fontRendererObj.drawStringWithShadow(name + ": " + ChatFormatting.GRAY + PType, x + 1, y2 - 9, potion.getLiquidColor());
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.popMatrix();
                y2 -= 10;
            }

            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect((int) x + 1, (int) y + 1, 8.0f, 8.0f, 8, 8, 34, 34, 64.0F, 64.0F);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }

            RectHelper.drawRectBetter(x + 18, y + 41, 120, 3, new Color(20, 221, 32).darker().darker().darker().getRGB());
            RectHelper.drawRectBetter(x + 18, y + 41, healthBarWidth + check, 3, new Color(new Color(255, 55, 55).darker().getRGB()).getRGB());
            RectHelper.drawRectBetter(x + 18, y + 41, healthWid, 3, new Color(new Color(20, 221, 32).getRGB()).getRGB());
        } else if (mode.equalsIgnoreCase("Minecraft")) {
            if (target == null)
                return;
            float x = getX(), y = getY();
            GlStateManager.pushMatrix();
            RectHelper.drawOutlineRect(x - 2, y - 7, 155, 38, new Color(20, 20, 20, 255), new Color(255, 255, 255, 255));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.fontRendererObj.drawStringWithShadow(target.getName(), (getX() + 37), getY() - 2, -1);
            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect((int) x, (int) y - 5, 8.0f, 8.0f, 8, 8, 34, 34, 64.0F, 64.0F);
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
            int i = 0;
            while ((float) i < target.getMaxHealth() / 2.0F) {
                mc.ingameGUI.drawTexturedModalRect((getX() + 86) - target.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), getY() + 9, 16, 0, 9, 9);
                ++i;
            }
            i = 0;
            while ((float) i < target.getHealth() / 2.0F) {
                mc.ingameGUI.drawTexturedModalRect((getX() + 86) - target.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), getY() + 9, 52, 0, 9, 9);
                ++i;
            }

            int i3 = target.getTotalArmorValue();
            for (int k3 = 0; k3 < 10; ++k3) {
                if (i3 > 0) {
                    int l3 = (getX() + 36) + k3 * 8;
                    if (k3 * 2 + 1 < i3) {
                        mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 34, 9, 9, 9);
                    }

                    if (k3 * 2 + 1 == i3) {
                        mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 25, 9, 9, 9);
                    }

                    if (k3 * 2 + 1 > i3) {
                        mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 16, 9, 9, 9);
                    }
                }
            }
            GlStateManager.popMatrix();
        } else if (mode.equalsIgnoreCase("Flux")) {
            float x = getX(), y = getY();

            double armorWid = (target.getTotalArmorValue() * 6);
            double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
            healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);

            RectHelper.drawRectBetter(x, y, 125, 55, new Color(39, 39, 37, 235).getRGB());

            String pvpState = "";
            if (mc.player.getHealth() == target.getHealth()) {
                pvpState = "Finish Him!";
            } else if (mc.player.getHealth() < target.getHealth()) {
                pvpState = "Losing Fight";
            } else if (mc.player.getHealth() > target.getHealth()) {
                pvpState = "Winning Fight";
            }
            if (!target.getName().isEmpty()) {
                mc.robotoRegularFontRender.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 38, y + 6, -1);
                mc.clickguismall.drawStringWithShadow(pvpState, x + 38, y + 17, -1);
            }

            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect(x + 1.5F, y + 1.5F, 8.0f, 8.0f, 8, 8, 34, 34, 64.0F, 64.0F);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }

            /* HEALTH BAR */
            RectHelper.drawRectBetter(x + 1.5F, y + 39, 120, 4, new Color(26, 28, 25, 255).getRGB());
            RectHelper.drawRectBetter(x + 1.5F, y + 39, healthWid, 4, new Color(2, 145, 98, 255).getRGB());

            /* ARMOR BAR */
            RectHelper.drawRectBetter(x + 1.5F, y + 47, 120, 4, new Color(26, 28, 25, 255).getRGB());
            RectHelper.drawRectBetter(x + 1.5F, y + 47, armorWid, 4, new Color(65, 138, 195, 255).getRGB());
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
        String mode = KillAura.targetHudMode.getOptions();
        EntityLivingBase target = KillAura.target;
        int color = KillAura.targetHudColor.getColorValue();

        long time = System.currentTimeMillis();
        float pct = (float)(time - this.lastUpdate) / (20 * 50.0f);
        this.lastUpdate = System.currentTimeMillis();
        if (target != null) {
            if (this.displayPercent < 1.0f) {
                displayPercent += pct;
            }
            if (this.displayPercent > 1.0f) {
                this.displayPercent = 1.0f;
            }
        } else {
            if (this.displayPercent > 0.0f) {
                displayPercent -= pct;
            }
            if (this.displayPercent < 0.0f) {
                this.displayPercent = 0.0f;
            }
        }

        if (mode.equalsIgnoreCase("Astolfo")) {
            float x = getX(), y = getY();
            double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
            healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);
            double check = target.getHealth() < 18 && target.getHealth() > 1 ? 8 : 0;
            healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, (long) 0.005, 0.005);
            RectHelper.drawRectBetter(x, y, 155, 60, new Color(20, 20, 20, 200).getRGB());
            if (!target.getName().isEmpty()) {
                mc.fontRendererObj.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 31, y + 5, -1);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(2.5f, 2.5f, 2.5f);
            GlStateManager.translate(-x - 3, -y - 2, 1);
            mc.fontRendererObj.drawStringWithShadow(MathematicHelper.round((target.getHealth() / 2.0f), 1) + " \u2764", x + 16, y + 10, new Color(color).getRGB());
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);

            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 7);
            mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 1);

            GuiInventory.drawEntityOnScreen(x + 16, y + 55, 25, target.rotationYaw, -target.rotationPitch, target);
            RectHelper.drawRectBetter(x + 30, y + 48, 120, 8, new Color(color).darker().darker().darker().getRGB());
            RectHelper.drawRectBetter(x + 30, y + 48, healthBarWidth + check, 8, new Color(color).darker().getRGB());
            RectHelper.drawRectBetter(x + 30, y + 48, healthWid, 8, new Color(color).getRGB());
        } else if (mode.equalsIgnoreCase("Small")) {
            float x = getX(), y = getY();

            double healthWid = (target.getHealth() / target.getMaxHealth() * 100);
            healthWid = MathHelper.clamp(healthWid, 0, 100);
            healthBarWidth = AnimationHelper.calculateCompensation((float) healthWid, healthBarWidth, (long) 0.005, 0.005);
            RectHelper.drawRectBetter(x, y, 140, 34, new Color(20, 20, 20).getRGB());

            //  org.dreamcore.client.helpers.render.RenderHelper.drawCircle(x + 120, y + 13, 0, mc.player.getHealth() / mc.player.getMaxHealth() * 360, 8, new Color(color));

            if (!target.getName().isEmpty()) {
                mc.fontRenderer.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 38, y + 5, -1);
            }
            mc.fontRenderer.drawStringWithShadow(mc.player.connection.getPlayerInfo(target.getName()).getResponseTime() + "ms", x + 38, y + 15, -1);

            RectHelper.drawRectBetter(x + 38, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 15, 10 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()), 8, new Color(35, 35, 35, 255).getRGB());
            RectHelper.drawRectBetter(x + 38, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 15, target.getHealth() > 18 ? healthWid : healthBarWidth + 4, 8, new Color(color).darker().getRGB());
            RectHelper.drawRectBetter(x + 38, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 15, healthWid, 8, new Color(color).getRGB());

            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 7);
            mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 137, (int) y + 1);
            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect((int) x + 1, (int) y + 1, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
                    GL11.glPushMatrix();
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }
        } else if (mode.equalsIgnoreCase("Novoline Old")) {
            if (target == null)
                return;
            if (target.getHealth() < 0)
                return;
            float x = getX(), y = getY();
            float healthWid = (target.getHealth() / target.getMaxHealth() * (40 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName())));
            healthWid = (float) MathHelper.clamp(healthWid, 0.0D, 124.0D);
            healthBarWidth = AnimationHelper.calculateCompensation(healthWid, healthBarWidth, 0, 0);
            healthBarWidth = MathHelper.clamp(healthBarWidth, 0, 124);
            RectHelper.drawRectBetter(x, y, 65 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 25, 40, new Color(19, 19, 19, 255).getRGB());
            RectHelper.drawRectBetter(x + 1, y + 1, 65 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 23, 38, new Color(41, 41, 41, 255).getRGB());
            if (!target.getName().isEmpty()) {
                mc.fontRendererObj.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 42, y + 5, -1);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(1.05f, 1.05f, 1.05f);
            GlStateManager.translate(-x + 24, -y + 18, 1);
            String hp = MathematicHelper.round((target.getHealth() / 2.0f), 1) + "";
            mc.fontRendererObj.drawStringWithShadow(hp, x + 17, y + 10, -1);
            mc.fontRendererObj.drawStringWithShadow(" \u2764", x + mc.fontRendererObj.getStringWidth(hp) + 16, y + 10, new Color(color).getRGB());
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(0.8f, 0.8f, 0.8f);
            GlStateManager.translate(-x + 148, -y + 38, 1);
            boolean stack = target.getHeldItemOffhand().isStackable() && !target.getHeldItemOffhand().isEmpty();
            if (stack) {
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) (x - 39 + mc.fontRenderer.getStringWidth(hp) - 21), (int) (y - 8));
                mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) (x - 49 + mc.fontRenderer.getStringWidth(hp) - 21), (int) y - 5);
            }
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);
            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect((int) x + 1, (int) y + 1, 8.0f, 8.0f, 8, 8, 38, 38, 64.0f, 64.0f);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, 40 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()), 8, new Color(35, 35, 35, 255).getRGB());
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, target.getHealth() > 18 ? healthWid : healthBarWidth + 4, 8, new Color(color).darker().getRGB());
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, healthWid, 8, new Color(color).getRGB());
        } else if (mode.equalsIgnoreCase("Novoline New")) {
            if (target == null)
                return;
            if (target.getHealth() < 0)
                return;
            float x = getX(), y = getY();
         //   GL11.glScalef(this.displayPercent, this.displayPercent, this.displayPercent);
          //  GL11.glTranslatef(getX() * 0.5f * (1.0f - this.displayPercent) / this.displayPercent, getY() * 0.5f * (1.0f - this.displayPercent) / this.displayPercent, 0.0f);
            float healthWid = (target.getHealth() / target.getMaxHealth() * (40 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName())));
            healthWid = (float) MathHelper.clamp(healthWid, 0.0D, 124.0D);
            healthBarWidth = AnimationHelper.calculateCompensation(healthWid, healthBarWidth, 0, 0);
            healthBarWidth = MathHelper.clamp(healthBarWidth, 0, 124);
            RectHelper.drawRectBetter(x, y, 65 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 25, 40, new Color(19, 19, 19, 255).getRGB());
            RectHelper.drawRectBetter(x + 1, y + 1, 65 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 23, 38, new Color(41, 41, 41, 255).getRGB());
            if (!target.getName().isEmpty()) {
                mc.fontRendererObj.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 42, y + 5, -1);
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(1.05f, 1.05f, 1.05f);
            GlStateManager.translate(-x + 24, -y + 18, 1);
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 1);
            GlStateManager.scale(0.8f, 0.8f, 0.8f);
            GlStateManager.translate(-x + 148, -y + 38, 1);
            boolean stack = target.getHeldItemOffhand().isStackable() && !target.getHeldItemOffhand().isEmpty();
            if (stack) {
                mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, target.getHeldItem(EnumHand.OFF_HAND), (int) (x - 88), (int) (y - 8));
                mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) (x - 98), (int) y - 5);
            }
            GlStateManager.popMatrix();
            GlStateManager.color(1, 1, 1, 1);
            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    float hurtPercent = getHurtPercent(target);
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                    Gui.drawScaledCustomSizeModalRect((int) x + 1, (int) y + 1, 8.0f, 8.0f, 8, 8, 38, 38, 64.0f, 64.0f);
                    GL11.glPopMatrix();
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }
            String hp = MathematicHelper.round(target.getHealth() / target.getMaxHealth() * 100F, 1) + "%";
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, 40 + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()), 10, new Color(35, 35, 35, 255).getRGB());
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, target.getHealth() > 18 ? healthWid : healthBarWidth + 4, 10, new Color(color).darker().getRGB());
            RectHelper.drawRectBetter(x + 42, y + mc.fontRendererObj.getStringHeight(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) + 9, healthWid, 10, new Color(color).getRGB());
            mc.fontRendererObj.drawStringWithShadow(hp, x + mc.fontRendererObj.getStringWidth(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName()) / 2F + 50, y + 19, -1);
        } else if (mode.equalsIgnoreCase("dfgopkododfg")) {
            if (target == null)
                return;
            float x = getX(), y = getY();
            GlStateManager.pushMatrix();
            RectHelper.drawOutlineRect(x - 2, y - 7, 155, 38, new Color(20, 20, 20, 255), new Color(255, 255, 255, 255));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.fontRendererObj.drawStringWithShadow(target.getName(), (getX() + 37), getY() - 2, -1);
            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                if (mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) == target) {
                    mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect((int) x, (int) y - 5, 8.0f, 8.0f, 8, 8, 34, 34, 64.0F, 64.0F);
                    GlStateManager.bindTexture(0);
                }
                GL11.glDisable(3089);
            }
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
            int i = 0;
            while ((float) i < target.getMaxHealth() / 2.0F) {
                mc.ingameGUI.drawTexturedModalRect((getX() + 86) - target.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), getY() + 9, 16, 0, 9, 9);
                ++i;
            }
            i = 0;
            while ((float) i < target.getHealth() / 2.0F) {
                mc.ingameGUI.drawTexturedModalRect((getX() + 86) - target.getMaxHealth() / 2.0F * 10.0F / 2.0F + (i * 8), getY() + 9, 52, 0, 9, 9);
                ++i;
            }

            int i3 = target.getTotalArmorValue();
            for (int k3 = 0; k3 < 10; ++k3) {
                if (i3 > 0) {
                    int l3 = (getX() + 36) + k3 * 8;
                    if (k3 * 2 + 1 < i3) {
                        mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 34, 9, 9, 9);
                    }

                    if (k3 * 2 + 1 == i3) {
                        mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 25, 9, 9, 9);
                    }

                    if (k3 * 2 + 1 > i3) {
                        mc.ingameGUI.drawTexturedModalRect(l3, getY() + 20, 16, 9, 9, 9);
                    }
                }
            }

            GlStateManager.popMatrix();
        } else if (mode.equalsIgnoreCase("Flux")) {
            float x = getX(), y = getY();

            double armorWid = (target.getTotalArmorValue() * 6);
            double healthWid = (target.getHealth() / target.getMaxHealth() * 120);
            healthWid = MathHelper.clamp(healthWid, 0.0D, 120.0D);

            RectHelper.drawRectBetter(x, y, 125, 55, new Color(39, 39, 37, 235).getRGB());

            String pvpState = "";
            if (mc.player.getHealth() == target.getHealth()) {
                pvpState = "Finish Him!";
            } else if (mc.player.getHealth() < target.getHealth()) {
                pvpState = "Losing Fight";
            } else if (mc.player.getHealth() > target.getHealth()) {
                pvpState = "Winning Fight";
            }
            if (!target.getName().isEmpty()) {
                mc.robotoRegularFontRender.drawStringWithShadow(dreamcore.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : target.getName(), x + 38, y + 6, -1);
                mc.clickguismall.drawStringWithShadow(pvpState, x + 38, y + 17, -1);
            }

            for (NetworkPlayerInfo targetHead : mc.player.connection.getPlayerInfoMap()) {
                mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                float hurtPercent = getHurtPercent(target);
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                Gui.drawScaledCustomSizeModalRect(x + 1.5F, y + 1.5F, 8, 8, 8, 8, 34, 34, 64, 64);
                GL11.glPopMatrix();
            }

            /* HEALTH BAR */
            RectHelper.drawRectBetter(x + 1.5F, y + 39, 120, 4, new Color(26, 28, 25, 255).getRGB());
            RectHelper.drawRectBetter(x + 1.5F, y + 39, healthWid, 4, new Color(2, 145, 98, 255).getRGB());

            /* ARMOR BAR */
            RectHelper.drawRectBetter(x + 1.5F, y + 47, 120, 4, new Color(26, 28, 25, 255).getRGB());
            RectHelper.drawRectBetter(x + 1.5F, y + 47, armorWid, 4, new Color(65, 138, 195, 255).getRGB());
        }
        super.draw();
    }

    public static float getRenderHurtTime(EntityLivingBase hurt) {
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? mc.timer.renderPartialTicks : 0.0f);
    }

    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / (float) 10;
    }

}
