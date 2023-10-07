// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.util.IChatComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.FoodStats;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.scoreboard.Team;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import net.minecraft.scoreboard.Score;
import com.google.common.base.Predicate;
import net.minecraft.util.BlockPos;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.MovingObjectPosition;
import exhibition.Client;
import exhibition.module.impl.render.Crosshair;
import net.minecraft.util.StringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.MathHelper;
import java.awt.Color;
import exhibition.management.notifications.Notifications;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventRenderGui;
import net.minecraft.potion.Potion;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.util.ResourceLocation;

public class GuiIngame extends Gui
{
    private static final ResourceLocation vignetteTexPath;
    private static final ResourceLocation widgetsTexPath;
    private static final ResourceLocation pumpkinBlurTexPath;
    private final Random rand;
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    private final GuiNewChat persistantChatGUI;
    private final GuiStreamIndicator streamIndicator;
    private int updateCounter;
    private String recordPlaying;
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    public float prevVignetteBrightness;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSpectator field_175197_u;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private int field_175195_w;
    private String field_175201_x;
    private String field_175200_y;
    private int field_175199_z;
    private int field_175192_A;
    private int field_175193_B;
    private int field_175194_C;
    private int field_175189_D;
    private long field_175190_E;
    private long field_175191_F;
    private static final String __OBFID = "CL_00000661";
    
    public GuiIngame(final Minecraft mcIn) {
        this.rand = new Random();
        this.recordPlaying = "";
        this.prevVignetteBrightness = 1.0f;
        this.field_175201_x = "";
        this.field_175200_y = "";
        this.field_175194_C = 0;
        this.field_175189_D = 0;
        this.field_175190_E = 0L;
        this.field_175191_F = 0L;
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.field_175197_u = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.streamIndicator = new GuiStreamIndicator(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.func_175177_a();
    }
    
    public void func_175177_a() {
        this.field_175199_z = 10;
        this.field_175192_A = 70;
        this.field_175193_B = 20;
    }
    
    public void func_175180_a(final float p_175180_1_) {
        final ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int var3 = var2.getScaledWidth();
        final int var4 = var2.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        if (Minecraft.isFancyGraphicsEnabled()) {
            this.func_180480_a(this.mc.thePlayer.getBrightness(p_175180_1_), var2);
        }
        else {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        final ItemStack var5 = this.mc.thePlayer.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && var5 != null && var5.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.func_180476_e(var2);
        }
        if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
            final float var6 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * p_175180_1_;
            if (var6 > 0.0f) {
                this.func_180474_b(var6, var2);
            }
        }
        if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
            this.field_175197_u.func_175264_a(var2, p_175180_1_);
        }
        else {
            this.func_180479_a(var2, p_175180_1_);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiIngame.icons);
        GlStateManager.enableBlend();
        if (this.func_175183_b()) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(var3 / 2 - 7, var4 / 2 - 7, 0, 0, 16, 16);
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();
        if (this.mc.playerController.shouldDrawHUD()) {
            this.func_180477_d(var2);
        }
        GlStateManager.disableBlend();
        ((EventRenderGui)EventSystem.getInstance(EventRenderGui.class)).fire(var2);
        if (this.mc.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            final int var7 = this.mc.thePlayer.getSleepTimer();
            float var8 = var7 / 100.0f;
            if (var8 > 1.0f) {
                var8 = 1.0f - (var7 - 100) / 10.0f;
            }
            final int var9 = (int)(220.0f * var8) << 24 | 0x101020;
            Gui.drawRect(0, 0, var3, var4, var9);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int var7 = var3 / 2 - 91;
        if (this.mc.thePlayer.isRidingHorse()) {
            this.func_175186_a(var2, var7);
        }
        else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.func_175176_b(var2, var7);
        }
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.enableEverythingIsScrewedUpMode()) {
            this.func_175182_a(var2);
        }
        else if (this.mc.thePlayer.func_175149_v()) {
            this.field_175197_u.func_175263_a(var2);
        }
        if (this.mc.isDemo()) {
            this.func_175185_b(var2);
        }
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.func_175237_a(var2);
        }
        Notifications.getManager().updateAndRender();
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            final float var8 = this.recordPlayingUpFor - p_175180_1_;
            int var9 = (int)(var8 * 255.0f / 20.0f);
            if (var9 > 255) {
                var9 = 255;
            }
            if (var9 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(var3 / 2, var4 - 68, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int var10 = 16777215;
                if (this.recordIsPlaying) {
                    var10 = (Color.HSBtoRGB(var8 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF);
                }
                this.func_175179_f().drawString(this.recordPlaying, -this.func_175179_f().getStringWidth(this.recordPlaying) / 2, -4.0f, var10 + (var9 << 24 & 0xFF000000));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.field_175195_w > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            final float var8 = this.field_175195_w - p_175180_1_;
            int var9 = 255;
            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                final float var11 = this.field_175199_z + this.field_175192_A + this.field_175193_B - var8;
                var9 = (int)(var11 * 255.0f / this.field_175199_z);
            }
            if (this.field_175195_w <= this.field_175193_B) {
                var9 = (int)(var8 * 255.0f / this.field_175193_B);
            }
            var9 = MathHelper.clamp_int(var9, 0, 255);
            if (var9 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(var3 / 2, var4 / 2, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                final int var10 = var9 << 24 & 0xFF000000;
                this.func_175179_f().renderText(this.field_175201_x, -this.func_175179_f().getStringWidth(this.field_175201_x) / 2, -10.0f, 0xFFFFFF | var10, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                this.func_175179_f().renderText(this.field_175200_y, -this.func_175179_f().getStringWidth(this.field_175200_y) / 2, 5.0f, 0xFFFFFF | var10, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        final Scoreboard var12 = this.mc.theWorld.getScoreboard();
        ScoreObjective var13 = null;
        final ScorePlayerTeam var14 = var12.getPlayersTeam(this.mc.thePlayer.getName());
        if (var14 != null) {
            final int var15 = var14.func_178775_l().func_175746_b();
            if (var15 >= 0) {
                var13 = var12.getObjectiveInDisplaySlot(3 + var15);
            }
        }
        ScoreObjective var16 = (var13 != null) ? var13 : var12.getObjectiveInDisplaySlot(1);
        if (var16 != null) {
            this.func_180475_a(var16, var2);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, var4 - 48, 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        var16 = var12.getObjectiveInDisplaySlot(0);
        if (this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.func_175106_d().size() > 1 || var16 != null)) {
            this.overlayPlayerList.func_175246_a(true);
            this.overlayPlayerList.func_175249_a(var3, var12, var16);
        }
        else {
            this.overlayPlayerList.func_175246_a(false);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }
    
    protected void func_180479_a(final ScaledResolution p_180479_1_, final float p_180479_2_) {
        if (this.mc.func_175606_aa() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngame.widgetsTexPath);
            final EntityPlayer var3 = (EntityPlayer)this.mc.func_175606_aa();
            final int var4 = p_180479_1_.getScaledWidth() / 2;
            final float var5 = this.zLevel;
            this.zLevel = -90.0f;
            this.drawTexturedModalRect(var4 - 91, p_180479_1_.getScaledHeight() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(var4 - 91 - 1 + var3.inventory.currentItem * 20, p_180479_1_.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            this.zLevel = var5;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            for (int var6 = 0; var6 < 9; ++var6) {
                final int var7 = p_180479_1_.getScaledWidth() / 2 - 90 + var6 * 20 + 2;
                final int var8 = p_180479_1_.getScaledHeight() - 16 - 3;
                this.func_175184_a(var6, var7, var8, p_180479_2_, var3);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }
    
    public void func_175186_a(final ScaledResolution p_175186_1_, final int p_175186_2_) {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        final float var3 = this.mc.thePlayer.getHorseJumpPower();
        final short var4 = 182;
        final int var5 = (int)(var3 * (var4 + 1));
        final int var6 = p_175186_1_.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(p_175186_2_, var6, 0, 84, var4, 5);
        if (var5 > 0) {
            this.drawTexturedModalRect(p_175186_2_, var6, 0, 89, var5, 5);
        }
        this.mc.mcProfiler.endSection();
    }
    
    public void func_175176_b(final ScaledResolution p_175176_1_, final int p_175176_2_) {
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        final int var3 = this.mc.thePlayer.xpBarCap();
        if (var3 > 0) {
            final short var4 = 182;
            final int var5 = (int)(this.mc.thePlayer.experience * (var4 + 1));
            final int var6 = p_175176_1_.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(p_175176_2_, var6, 0, 64, var4, 5);
            if (var5 > 0) {
                this.drawTexturedModalRect(p_175176_2_, var6, 0, 69, var5, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (this.mc.thePlayer.experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            final int var7 = 8453920;
            final String var8 = "" + this.mc.thePlayer.experienceLevel;
            final int var6 = (p_175176_1_.getScaledWidth() - this.func_175179_f().getStringWidth(var8)) / 2;
            final int var9 = p_175176_1_.getScaledHeight() - 31 - 4;
            final boolean var10 = false;
            this.func_175179_f().drawString(var8, var6 + 1, var9, 0);
            this.func_175179_f().drawString(var8, var6 - 1, var9, 0);
            this.func_175179_f().drawString(var8, var6, var9 + 1, 0);
            this.func_175179_f().drawString(var8, var6, var9 - 1, 0);
            this.func_175179_f().drawString(var8, var6, var9, var7);
            this.mc.mcProfiler.endSection();
        }
    }
    
    public void func_175182_a(final ScaledResolution p_175182_1_) {
        this.mc.mcProfiler.startSection("toolHighlight");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            String var2 = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                var2 = EnumChatFormatting.ITALIC + var2;
            }
            final int var3 = (p_175182_1_.getScaledWidth() - this.func_175179_f().getStringWidth(var2)) / 2;
            int var4 = p_175182_1_.getScaledHeight() - 59;
            if (!this.mc.playerController.shouldDrawHUD()) {
                var4 += 14;
            }
            int var5 = (int)(this.remainingHighlightTicks * 256.0f / 10.0f);
            if (var5 > 255) {
                var5 = 255;
            }
            if (var5 > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.func_175179_f().drawStringWithShadow(var2, var3, var4, 16777215 + (var5 << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }
    
    public void func_175185_b(final ScaledResolution p_175185_1_) {
        this.mc.mcProfiler.startSection("demo");
        String var2 = "";
        if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
            var2 = I18n.format("demo.demoExpired", new Object[0]);
        }
        else {
            var2 = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime())));
        }
        final int var3 = this.func_175179_f().getStringWidth(var2);
        this.func_175179_f().drawStringWithShadow(var2, p_175185_1_.getScaledWidth() - var3 - 10, 5.0f, 16777215);
        this.mc.mcProfiler.endSection();
    }
    
    protected boolean func_175183_b() {
        final Crosshair c = Client.getModuleManager().get(Crosshair.class);
        if ((this.mc.gameSettings.showDebugInfo && !this.mc.thePlayer.func_175140_cp() && !this.mc.gameSettings.field_178879_v) || c.isEnabled()) {
            return false;
        }
        if (!this.mc.playerController.enableEverythingIsScrewedUpMode()) {
            return true;
        }
        if (this.mc.pointedEntity != null) {
            return true;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final BlockPos var1 = this.mc.objectMouseOver.getBlockPos();
            if (this.mc.theWorld.getTileEntity(var1) instanceof IInventory) {
                return true;
            }
        }
        return false;
    }
    
    public void func_180478_c(final ScaledResolution p_180478_1_) {
        this.streamIndicator.render(p_180478_1_.getScaledWidth() - 10, 10);
    }
    
    private void func_180475_a(final ScoreObjective p_180475_1_, final ScaledResolution p_180475_2_) {
        final Scoreboard var3 = p_180475_1_.getScoreboard();
        final Collection var4 = var3.getSortedScores(p_180475_1_);
        final ArrayList var5 = Lists.newArrayList(Iterables.filter((Iterable)var4, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00001958";
            
            public boolean func_178903_a(final Score p_178903_1_) {
                return p_178903_1_.getPlayerName() != null && !p_178903_1_.getPlayerName().startsWith("#");
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_178903_a((Score)p_apply_1_);
            }
        }));
        ArrayList var6;
        if (var5.size() > 15) {
            var6 = Lists.newArrayList(Iterables.skip((Iterable)var5, var4.size() - 15));
        }
        else {
            var6 = var5;
        }
        int var7 = this.func_175179_f().getStringWidth(p_180475_1_.getDisplayName());
        for (final Score var9 : var6) {
            final ScorePlayerTeam var10 = var3.getPlayersTeam(var9.getPlayerName());
            final String var11 = ScorePlayerTeam.formatPlayerName(var10, var9.getPlayerName()) + ": " + EnumChatFormatting.RED + var9.getScorePoints();
            var7 = Math.max(var7, this.func_175179_f().getStringWidth(var11));
        }
        final int var12 = var6.size() * this.func_175179_f().FONT_HEIGHT;
        final int var13 = p_180475_2_.getScaledHeight() / 2 + var12 / 3;
        final byte var14 = 3;
        final int var15 = p_180475_2_.getScaledWidth() - var7 - var14;
        int var16 = 0;
        for (final Score var18 : var6) {
            ++var16;
            final ScorePlayerTeam var19 = var3.getPlayersTeam(var18.getPlayerName());
            final String var20 = ScorePlayerTeam.formatPlayerName(var19, var18.getPlayerName());
            final String var21 = EnumChatFormatting.RED + "" + var18.getScorePoints();
            final int var22 = var13 - var16 * this.func_175179_f().FONT_HEIGHT;
            final int var23 = p_180475_2_.getScaledWidth() - var14 + 2;
            Gui.drawRect(var15 - 2, var22, var23, var22 + this.func_175179_f().FONT_HEIGHT, 1342177280);
            this.func_175179_f().drawString(var20, var15, var22, 553648127);
            this.func_175179_f().drawString(var21, var23 - this.func_175179_f().getStringWidth(var21), var22, 553648127);
            if (var16 == var6.size()) {
                final String var24 = p_180475_1_.getDisplayName();
                Gui.drawRect(var15 - 2, var22 - this.func_175179_f().FONT_HEIGHT - 1, var23, var22 - 1, 1610612736);
                Gui.drawRect(var15 - 2, var22 - 1, var23, var22, 1342177280);
                this.func_175179_f().drawString(var24, var15 + var7 / 2 - this.func_175179_f().getStringWidth(var24) / 2, var22 - this.func_175179_f().FONT_HEIGHT, 553648127);
            }
        }
    }
    
    private void func_180477_d(final ScaledResolution p_180477_1_) {
        if (this.mc.func_175606_aa() instanceof EntityPlayer) {
            final EntityPlayer var2 = (EntityPlayer)this.mc.func_175606_aa();
            final int var3 = MathHelper.ceiling_float_int(var2.getHealth());
            final boolean var4 = this.field_175191_F > this.updateCounter && (this.field_175191_F - this.updateCounter) / 3L % 2L == 1L;
            if (var3 < this.field_175194_C && var2.hurtResistantTime > 0) {
                this.field_175190_E = Minecraft.getSystemTime();
                this.field_175191_F = this.updateCounter + 20;
            }
            else if (var3 > this.field_175194_C && var2.hurtResistantTime > 0) {
                this.field_175190_E = Minecraft.getSystemTime();
                this.field_175191_F = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.field_175190_E > 1000L) {
                this.field_175194_C = var3;
                this.field_175189_D = var3;
                this.field_175190_E = Minecraft.getSystemTime();
            }
            this.field_175194_C = var3;
            final int var5 = this.field_175189_D;
            this.rand.setSeed(this.updateCounter * 312871);
            final boolean var6 = false;
            final FoodStats var7 = var2.getFoodStats();
            final int var8 = var7.getFoodLevel();
            final int var9 = var7.getPrevFoodLevel();
            final IAttributeInstance var10 = var2.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            final int var11 = p_180477_1_.getScaledWidth() / 2 - 91;
            final int var12 = p_180477_1_.getScaledWidth() / 2 + 91;
            final int var13 = p_180477_1_.getScaledHeight() - 39;
            final float var14 = (float)var10.getAttributeValue();
            final float var15 = var2.getAbsorptionAmount();
            final int var16 = MathHelper.ceiling_float_int((var14 + var15) / 2.0f / 10.0f);
            final int var17 = Math.max(10 - (var16 - 2), 3);
            final int var18 = var13 - (var16 - 1) * var17 - 10;
            float var19 = var15;
            final int var20 = var2.getTotalArmorValue();
            int var21 = -1;
            if (var2.isPotionActive(Potion.regeneration)) {
                var21 = this.updateCounter % MathHelper.ceiling_float_int(var14 + 5.0f);
            }
            this.mc.mcProfiler.startSection("armor");
            for (int var22 = 0; var22 < 10; ++var22) {
                if (var20 > 0) {
                    final int var23 = var11 + var22 * 8;
                    if (var22 * 2 + 1 < var20) {
                        this.drawTexturedModalRect(var23, var18, 34, 9, 9, 9);
                    }
                    if (var22 * 2 + 1 == var20) {
                        this.drawTexturedModalRect(var23, var18, 25, 9, 9, 9);
                    }
                    if (var22 * 2 + 1 > var20) {
                        this.drawTexturedModalRect(var23, var18, 16, 9, 9, 9);
                    }
                }
            }
            this.mc.mcProfiler.endStartSection("health");
            for (int var22 = MathHelper.ceiling_float_int((var14 + var15) / 2.0f) - 1; var22 >= 0; --var22) {
                int var23 = 16;
                if (var2.isPotionActive(Potion.poison)) {
                    var23 += 36;
                }
                else if (var2.isPotionActive(Potion.wither)) {
                    var23 += 72;
                }
                byte var24 = 0;
                if (var4) {
                    var24 = 1;
                }
                final int var25 = MathHelper.ceiling_float_int((var22 + 1) / 10.0f) - 1;
                final int var26 = var11 + var22 % 10 * 8;
                int var27 = var13 - var25 * var17;
                if (var3 <= 4) {
                    var27 += this.rand.nextInt(2);
                }
                if (var22 == var21) {
                    var27 -= 2;
                }
                byte var28 = 0;
                if (var2.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    var28 = 5;
                }
                this.drawTexturedModalRect(var26, var27, 16 + var24 * 9, 9 * var28, 9, 9);
                if (var4) {
                    if (var22 * 2 + 1 < var5) {
                        this.drawTexturedModalRect(var26, var27, var23 + 54, 9 * var28, 9, 9);
                    }
                    if (var22 * 2 + 1 == var5) {
                        this.drawTexturedModalRect(var26, var27, var23 + 63, 9 * var28, 9, 9);
                    }
                }
                if (var19 > 0.0f) {
                    if (var19 == var15 && var15 % 2.0f == 1.0f) {
                        this.drawTexturedModalRect(var26, var27, var23 + 153, 9 * var28, 9, 9);
                    }
                    else {
                        this.drawTexturedModalRect(var26, var27, var23 + 144, 9 * var28, 9, 9);
                    }
                    var19 -= 2.0f;
                }
                else {
                    if (var22 * 2 + 1 < var3) {
                        this.drawTexturedModalRect(var26, var27, var23 + 36, 9 * var28, 9, 9);
                    }
                    if (var22 * 2 + 1 == var3) {
                        this.drawTexturedModalRect(var26, var27, var23 + 45, 9 * var28, 9, 9);
                    }
                }
            }
            final Entity var29 = var2.ridingEntity;
            if (var29 == null) {
                this.mc.mcProfiler.endStartSection("food");
                for (int var23 = 0; var23 < 10; ++var23) {
                    int var30 = var13;
                    int var25 = 16;
                    byte var31 = 0;
                    if (var2.isPotionActive(Potion.hunger)) {
                        var25 += 36;
                        var31 = 13;
                    }
                    if (var2.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (var8 * 3 + 1) == 0) {
                        var30 = var13 + (this.rand.nextInt(3) - 1);
                    }
                    if (var6) {
                        var31 = 1;
                    }
                    final int var27 = var12 - var23 * 8 - 9;
                    this.drawTexturedModalRect(var27, var30, 16 + var31 * 9, 27, 9, 9);
                    if (var6) {
                        if (var23 * 2 + 1 < var9) {
                            this.drawTexturedModalRect(var27, var30, var25 + 54, 27, 9, 9);
                        }
                        if (var23 * 2 + 1 == var9) {
                            this.drawTexturedModalRect(var27, var30, var25 + 63, 27, 9, 9);
                        }
                    }
                    if (var23 * 2 + 1 < var8) {
                        this.drawTexturedModalRect(var27, var30, var25 + 36, 27, 9, 9);
                    }
                    if (var23 * 2 + 1 == var8) {
                        this.drawTexturedModalRect(var27, var30, var25 + 45, 27, 9, 9);
                    }
                }
            }
            else if (var29 instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                final EntityLivingBase var32 = (EntityLivingBase)var29;
                final int var30 = (int)Math.ceil(var32.getHealth());
                final float var33 = var32.getMaxHealth();
                int var26 = (int)(var33 + 0.5f) / 2;
                if (var26 > 30) {
                    var26 = 30;
                }
                int var27 = var13;
                int var34 = 0;
                while (var26 > 0) {
                    final int var35 = Math.min(var26, 10);
                    var26 -= var35;
                    for (int var36 = 0; var36 < var35; ++var36) {
                        final byte var37 = 52;
                        byte var38 = 0;
                        if (var6) {
                            var38 = 1;
                        }
                        final int var39 = var12 - var36 * 8 - 9;
                        this.drawTexturedModalRect(var39, var27, var37 + var38 * 9, 9, 9, 9);
                        if (var36 * 2 + 1 + var34 < var30) {
                            this.drawTexturedModalRect(var39, var27, var37 + 36, 9, 9, 9);
                        }
                        if (var36 * 2 + 1 + var34 == var30) {
                            this.drawTexturedModalRect(var39, var27, var37 + 45, 9, 9, 9);
                        }
                    }
                    var27 -= 10;
                    var34 += 20;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (var2.isInsideOfMaterial(Material.water)) {
                final int var23 = this.mc.thePlayer.getAir();
                for (int var30 = MathHelper.ceiling_double_int((var23 - 2) * 10.0 / 300.0), var25 = MathHelper.ceiling_double_int(var23 * 10.0 / 300.0) - var30, var26 = 0; var26 < var30 + var25; ++var26) {
                    if (var26 < var30) {
                        this.drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 16, 18, 9, 9);
                    }
                    else {
                        this.drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 25, 18, 9, 9);
                    }
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }
    
    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            final FontRenderer var1 = this.mc.fontRendererObj;
            final ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            final int var3 = var2.getScaledWidth();
            final short var4 = 182;
            final int var5 = var3 / 2 - var4 / 2;
            final int var6 = (int)(BossStatus.healthScale * (var4 + 1));
            final byte var7 = 12;
            this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
            this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
            if (var6 > 0) {
                this.drawTexturedModalRect(var5, var7, 0, 79, var6, 5);
            }
            final String var8 = BossStatus.bossName;
            this.func_175179_f().drawStringWithShadow(var8, var3 / 2 - this.func_175179_f().getStringWidth(var8) / 2, var7 - 10, 16777215);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngame.icons);
        }
    }
    
    private void func_180476_e(final ScaledResolution p_180476_1_) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(GuiIngame.pumpkinBlurTexPath);
        final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        var3.addVertexWithUV(0.0, p_180476_1_.getScaledHeight(), -90.0, 0.0, 1.0);
        var3.addVertexWithUV(p_180476_1_.getScaledWidth(), p_180476_1_.getScaledHeight(), -90.0, 1.0, 1.0);
        var3.addVertexWithUV(p_180476_1_.getScaledWidth(), 0.0, -90.0, 1.0, 0.0);
        var3.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
        var2.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void func_180480_a(float p_180480_1_, final ScaledResolution p_180480_2_) {
        p_180480_1_ = 1.0f - p_180480_1_;
        p_180480_1_ = MathHelper.clamp_float(p_180480_1_, 0.0f, 1.0f);
        final WorldBorder var3 = this.mc.theWorld.getWorldBorder();
        float var4 = (float)var3.getClosestDistance(this.mc.thePlayer);
        final double var5 = Math.min(var3.func_177749_o() * var3.getWarningTime() * 1000.0, Math.abs(var3.getTargetSize() - var3.getDiameter()));
        final double var6 = Math.max(var3.getWarningDistance(), var5);
        if (var4 < var6) {
            var4 = 1.0f - (float)(var4 / var6);
        }
        else {
            var4 = 0.0f;
        }
        this.prevVignetteBrightness += (float)((p_180480_1_ - this.prevVignetteBrightness) * 0.01);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
        if (var4 > 0.0f) {
            GlStateManager.color(0.0f, var4, var4, 1.0f);
        }
        else {
            GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
        }
        this.mc.getTextureManager().bindTexture(GuiIngame.vignetteTexPath);
        final Tessellator var7 = Tessellator.getInstance();
        final WorldRenderer var8 = var7.getWorldRenderer();
        var8.startDrawingQuads();
        var8.addVertexWithUV(0.0, p_180480_2_.getScaledHeight(), -90.0, 0.0, 1.0);
        var8.addVertexWithUV(p_180480_2_.getScaledWidth(), p_180480_2_.getScaledHeight(), -90.0, 1.0, 1.0);
        var8.addVertexWithUV(p_180480_2_.getScaledWidth(), 0.0, -90.0, 1.0, 0.0);
        var8.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
        var7.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }
    
    private void func_180474_b(float p_180474_1_, final ScaledResolution p_180474_2_) {
        if (p_180474_1_ < 1.0f) {
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ = p_180474_1_ * 0.8f + 0.2f;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, p_180474_1_);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        final TextureAtlasSprite var3 = this.mc.getBlockRendererDispatcher().func_175023_a().func_178122_a(Blocks.portal.getDefaultState());
        final float var4 = var3.getMinU();
        final float var5 = var3.getMinV();
        final float var6 = var3.getMaxU();
        final float var7 = var3.getMaxV();
        final Tessellator var8 = Tessellator.getInstance();
        final WorldRenderer var9 = var8.getWorldRenderer();
        var9.startDrawingQuads();
        var9.addVertexWithUV(0.0, p_180474_2_.getScaledHeight(), -90.0, var4, var7);
        var9.addVertexWithUV(p_180474_2_.getScaledWidth(), p_180474_2_.getScaledHeight(), -90.0, var6, var7);
        var9.addVertexWithUV(p_180474_2_.getScaledWidth(), 0.0, -90.0, var6, var5);
        var9.addVertexWithUV(0.0, 0.0, -90.0, var4, var5);
        var8.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void func_175184_a(final int p_175184_1_, final int p_175184_2_, final int p_175184_3_, final float p_175184_4_, final EntityPlayer p_175184_5_) {
        final ItemStack var6 = p_175184_5_.inventory.mainInventory[p_175184_1_];
        if (var6 != null) {
            final float var7 = var6.animationsToGo - p_175184_4_;
            if (var7 > 0.0f) {
                GlStateManager.pushMatrix();
                final float var8 = 1.0f + var7 / 5.0f;
                GlStateManager.translate(p_175184_2_ + 8, p_175184_3_ + 12, 0.0f);
                GlStateManager.scale(1.0f / var8, (var8 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate(-(p_175184_2_ + 8), -(p_175184_3_ + 12), 0.0f);
            }
            this.itemRenderer.func_180450_b(var6, p_175184_2_, p_175184_3_);
            if (var7 > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, var6, p_175184_2_, p_175184_3_);
        }
    }
    
    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }
        if (this.field_175195_w > 0) {
            --this.field_175195_w;
            if (this.field_175195_w <= 0) {
                this.field_175201_x = "";
                this.field_175200_y = "";
            }
        }
        ++this.updateCounter;
        this.streamIndicator.func_152439_a();
        if (this.mc.thePlayer != null) {
            final ItemStack var1 = this.mc.thePlayer.inventory.getCurrentItem();
            if (var1 == null) {
                this.remainingHighlightTicks = 0;
            }
            else if (this.highlightingItemStack != null && var1.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(var1, this.highlightingItemStack) && (var1.isItemStackDamageable() || var1.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            }
            else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = var1;
        }
    }
    
    public void setRecordPlayingMessage(final String p_73833_1_) {
        this.setRecordPlaying(I18n.format("record.nowPlaying", p_73833_1_), true);
    }
    
    public void setRecordPlaying(final String p_110326_1_, final boolean p_110326_2_) {
        this.recordPlaying = p_110326_1_;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = p_110326_2_;
    }
    
    public void func_175178_a(final String p_175178_1_, final String p_175178_2_, final int p_175178_3_, final int p_175178_4_, final int p_175178_5_) {
        if (p_175178_1_ == null && p_175178_2_ == null && p_175178_3_ < 0 && p_175178_4_ < 0 && p_175178_5_ < 0) {
            this.field_175201_x = "";
            this.field_175200_y = "";
            this.field_175195_w = 0;
        }
        else if (p_175178_1_ != null) {
            this.field_175201_x = p_175178_1_;
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
        }
        else if (p_175178_2_ != null) {
            this.field_175200_y = p_175178_2_;
        }
        else {
            if (p_175178_3_ >= 0) {
                this.field_175199_z = p_175178_3_;
            }
            if (p_175178_4_ >= 0) {
                this.field_175192_A = p_175178_4_;
            }
            if (p_175178_5_ >= 0) {
                this.field_175193_B = p_175178_5_;
            }
            if (this.field_175195_w > 0) {
                this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
            }
        }
    }
    
    public void func_175188_a(final IChatComponent p_175188_1_, final boolean p_175188_2_) {
        this.setRecordPlaying(p_175188_1_.getUnformattedText(), p_175188_2_);
    }
    
    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }
    
    public int getUpdateCounter() {
        return this.updateCounter;
    }
    
    public FontRenderer func_175179_f() {
        return this.mc.fontRendererObj;
    }
    
    public GuiSpectator func_175187_g() {
        return this.field_175197_u;
    }
    
    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }
    
    static {
        vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
        widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
        pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    }
}
