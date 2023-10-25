/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui.achievement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;

public class GuiAchievement
extends Gui {
    private int height;
    private long notificationTime;
    private String achievementTitle;
    private RenderItem renderItem;
    private String achievementDescription;
    private boolean permanentNotification;
    private Achievement theAchievement;
    private static final ResourceLocation achievementBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    private int width;
    private Minecraft mc;

    public GuiAchievement(Minecraft minecraft) {
        this.mc = minecraft;
        this.renderItem = minecraft.getRenderItem();
    }

    public void displayAchievement(Achievement achievement) {
        this.achievementTitle = I18n.format("achievement.get", new Object[0]);
        this.achievementDescription = achievement.getStatName().getUnformattedText();
        this.notificationTime = Minecraft.getSystemTime();
        this.theAchievement = achievement;
        this.permanentNotification = false;
    }

    private void updateAchievementWindowScale() {
        GlStateManager.viewport(0, 0, this.mc.displayWidth, Minecraft.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        this.width = this.mc.displayWidth;
        this.height = Minecraft.displayHeight;
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        this.width = scaledResolution.getScaledWidth();
        this.height = scaledResolution.getScaledHeight();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, this.width, this.height, 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }

    public void clearAchievements() {
        this.theAchievement = null;
        this.notificationTime = 0L;
    }

    public void displayUnformattedAchievement(Achievement achievement) {
        this.achievementTitle = achievement.getStatName().getUnformattedText();
        this.achievementDescription = achievement.getDescription();
        this.notificationTime = Minecraft.getSystemTime() + 2500L;
        this.theAchievement = achievement;
        this.permanentNotification = true;
    }

    public void updateAchievementWindow() {
        if (this.theAchievement != null && this.notificationTime != 0L) {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer != null) {
                double d = (double)(Minecraft.getSystemTime() - this.notificationTime) / 3000.0;
                if (!this.permanentNotification) {
                    if (d < 0.0 || d > 1.0) {
                        this.notificationTime = 0L;
                        return;
                    }
                } else if (d > 0.5) {
                    d = 0.5;
                }
                this.updateAchievementWindowScale();
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                double d2 = d * 2.0;
                if (d2 > 1.0) {
                    d2 = 2.0 - d2;
                }
                d2 *= 4.0;
                if ((d2 = 1.0 - d2) < 0.0) {
                    d2 = 0.0;
                }
                d2 *= d2;
                d2 *= d2;
                int n = this.width - 160;
                int n2 = 0 - (int)(d2 * 36.0);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableTexture2D();
                this.mc.getTextureManager().bindTexture(achievementBg);
                GlStateManager.disableLighting();
                this.drawTexturedModalRect(n, n2, 96, 202, 160, 32);
                if (this.permanentNotification) {
                    Minecraft.fontRendererObj.drawSplitString(this.achievementDescription, n + 30, n2 + 7, 120, -1);
                } else {
                    Minecraft.fontRendererObj.drawString(this.achievementTitle, n + 30, n2 + 7, -256);
                    Minecraft.fontRendererObj.drawString(this.achievementDescription, n + 30, n2 + 18, -1);
                }
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableColorMaterial();
                GlStateManager.enableLighting();
                this.renderItem.renderItemAndEffectIntoGUI(this.theAchievement.theItemStack, n + 8, n2 + 8);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
            }
        }
    }
}

