// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.achievement;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.stats.Achievement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiAchievement extends Gui
{
    private static final ResourceLocation achievementBg;
    private Minecraft mc;
    private int width;
    private int height;
    private String achievementTitle;
    private String achievementDescription;
    private Achievement theAchievement;
    private long notificationTime;
    private RenderItem renderItem;
    private boolean permanentNotification;
    private static final String __OBFID = "CL_00000721";
    
    public GuiAchievement(final Minecraft mc) {
        this.mc = mc;
        this.renderItem = mc.getRenderItem();
    }
    
    public void displayAchievement(final Achievement p_146256_1_) {
        this.achievementTitle = I18n.format("achievement.get", new Object[0]);
        this.achievementDescription = p_146256_1_.getStatName().getUnformattedText();
        this.notificationTime = Minecraft.getSystemTime();
        this.theAchievement = p_146256_1_;
        this.permanentNotification = false;
    }
    
    public void displayUnformattedAchievement(final Achievement p_146255_1_) {
        this.achievementTitle = p_146255_1_.getStatName().getUnformattedText();
        this.achievementDescription = p_146255_1_.getDescription();
        this.notificationTime = Minecraft.getSystemTime() + 2500L;
        this.theAchievement = p_146255_1_;
        this.permanentNotification = true;
    }
    
    private void updateAchievementWindowScale() {
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        this.width = this.mc.displayWidth;
        this.height = this.mc.displayHeight;
        final ScaledResolution var1 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.width = var1.getScaledWidth();
        this.height = var1.getScaledHeight();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, this.width, this.height, 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }
    
    public void updateAchievementWindow() {
        if (this.theAchievement != null && this.notificationTime != 0L && Minecraft.getMinecraft().thePlayer != null) {
            double var1 = (Minecraft.getSystemTime() - this.notificationTime) / 3000.0;
            if (!this.permanentNotification) {
                if (var1 < 0.0 || var1 > 1.0) {
                    this.notificationTime = 0L;
                    return;
                }
            }
            else if (var1 > 0.5) {
                var1 = 0.5;
            }
            this.updateAchievementWindowScale();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            double var2 = var1 * 2.0;
            if (var2 > 1.0) {
                var2 = 2.0 - var2;
            }
            var2 *= 4.0;
            var2 = 1.0 - var2;
            if (var2 < 0.0) {
                var2 = 0.0;
            }
            var2 *= var2;
            var2 *= var2;
            final int var3 = this.width - 160;
            final int var4 = 0 - (int)(var2 * 36.0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableTextures();
            this.mc.getTextureManager().bindTexture(GuiAchievement.achievementBg);
            GlStateManager.disableLighting();
            this.drawTexturedModalRect(var3, var4, 96, 202, 160, 32);
            if (this.permanentNotification) {
                this.mc.fontRendererObj.drawSplitString(this.achievementDescription, var3 + 30, var4 + 7, 120, -1);
            }
            else {
                this.mc.fontRendererObj.drawString(this.achievementTitle, var3 + 30, var4 + 7, -256);
                this.mc.fontRendererObj.drawString(this.achievementDescription, var3 + 30, var4 + 18, -1);
            }
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            this.renderItem.func_180450_b(this.theAchievement.theItemStack, var3 + 8, var4 + 8);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
        }
    }
    
    public void clearAchievements() {
        this.theAchievement = null;
        this.notificationTime = 0L;
    }
    
    static {
        achievementBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    }
}
