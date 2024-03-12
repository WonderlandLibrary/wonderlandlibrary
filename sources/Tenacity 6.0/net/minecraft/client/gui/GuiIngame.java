// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import dev.tenacity.utils.font.AbstractFontRenderer;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.block.material.Material;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.Score;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import dev.tenacity.module.impl.render.ScoreboardMod;
import net.minecraft.util.MathHelper;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.module.impl.render.NotificationsMod;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.impl.render.PostProcessing;
import dev.tenacity.event.Event;
import dev.tenacity.event.impl.render.PreRenderEvent;
import dev.tenacity.Tenacity;
import net.minecraft.potion.Potion;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.src.Config;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.animations.ContinualAnimation;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import dev.tenacity.utils.Utils;

public class GuiIngame extends Gui implements Utils
{
    private static final ResourceLocation vignetteTexPath;
    private static final ResourceLocation widgetsTexPath;
    private static final ResourceLocation pumpkinBlurTexPath;
    private final Random rand;
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    private final GuiNewChat persistantChatGUI;
    private int updateCounter;
    private String recordPlaying;
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    public float prevVignetteBrightness;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private int titlesTimer;
    private String displayedTitle;
    private String displayedSubTitle;
    private int titleFadeIn;
    private int titleDisplayTime;
    private int titleFadeOut;
    private int playerHealth;
    private int lastPlayerHealth;
    private long lastSystemTime;
    private long healthUpdateCounter;
    private final ContinualAnimation expAnim;
    private final ContinualAnimation jumpAnim;
    private final ContinualAnimation healthAnim;
    private final ContinualAnimation foodAnim;
    private final ContinualAnimation saturationAnim;
    private final ContinualAnimation armorAnim;
    private final ContinualAnimation airAnim;
    
    public GuiIngame(final Minecraft mcIn) {
        this.rand = new Random();
        this.recordPlaying = "";
        this.prevVignetteBrightness = 1.0f;
        this.displayedTitle = "";
        this.displayedSubTitle = "";
        this.playerHealth = 0;
        this.lastPlayerHealth = 0;
        this.lastSystemTime = 0L;
        this.healthUpdateCounter = 0L;
        this.expAnim = new ContinualAnimation();
        this.jumpAnim = new ContinualAnimation();
        this.healthAnim = new ContinualAnimation();
        this.foodAnim = new ContinualAnimation();
        this.saturationAnim = new ContinualAnimation();
        this.armorAnim = new ContinualAnimation();
        this.airAnim = new ContinualAnimation();
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.setDefaultTitlesTimes();
    }
    
    public void setDefaultTitlesTimes() {
        this.titleFadeIn = 10;
        this.titleDisplayTime = 70;
        this.titleFadeOut = 20;
    }
    
    public void renderGameOverlay(final float partialTicks) {
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        final int i = scaledresolution.getScaledWidth();
        final int j = scaledresolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GLUtil.startBlend();
        if (Config.isVignetteEnabled()) {
            this.renderVignette(this.mc.thePlayer.getBrightness(partialTicks), scaledresolution);
        }
        else {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        final ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.renderPumpkinOverlay(scaledresolution);
        }
        if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
            final float f = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
            if (f > 0.0f) {
                this.renderPortal(f, scaledresolution);
            }
        }
        Tenacity.INSTANCE.getEventProtocol().handleEvent(new PreRenderEvent());
        final PostProcessing postProcessing = (PostProcessing)Tenacity.INSTANCE.getModuleCollection().get(PostProcessing.class);
        postProcessing.blurScreen();
        Tenacity.INSTANCE.getEventProtocol().handleEvent(new Render2DEvent((float)scaledresolution.getScaledWidth(), (float)scaledresolution.getScaledHeight()));
        final NotificationsMod notif = Tenacity.INSTANCE.getModuleCollection().getModule(NotificationsMod.class);
        if (notif.isEnabled()) {
            notif.render();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiIngame.icons);
        if (this.showCrosshair()) {
            GLUtil.startBlend();
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            RenderUtil.setAlphaLimit(0.0f);
            this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }
        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        }
        else {
            this.renderTooltip(scaledresolution, partialTicks);
        }
        this.mc.getTextureManager().bindTexture(GuiIngame.icons);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, -15.0f * GuiChat.openingAnimation.getOutput().floatValue(), 0.0f);
        if (this.mc.playerController.shouldDrawHUD()) {
            RenderUtil.resetColor();
            GlStateManager.enableAlpha();
            this.renderPlayerStats(scaledresolution);
        }
        GlStateManager.popMatrix();
        if (this.mc.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            final int j2 = this.mc.thePlayer.getSleepTimer();
            float f2 = j2 / 100.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f - (j2 - 100) / 10.0f;
            }
            final int k = (int)(220.0f * f2) << 24 | 0x101020;
            Gui.drawRect(0.0, 0.0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int k2 = i / 2 - 91;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, -15.0f * GuiChat.openingAnimation.getOutput().floatValue(), 0.0f);
        if (this.mc.thePlayer.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, k2);
        }
        else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, k2);
        }
        GlStateManager.popMatrix();
        RenderUtil.resetColor();
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.renderSelectedItem(scaledresolution);
        }
        else if (this.mc.thePlayer.isSpectator()) {
            this.spectatorGui.renderSelectedItem(scaledresolution);
        }
        if (this.mc.isDemo()) {
            this.renderDemo(scaledresolution);
        }
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            final float f3 = this.recordPlayingUpFor - partialTicks;
            int l1 = (int)(f3 * 255.0f / 20.0f);
            if (l1 > 255) {
                l1 = 255;
            }
            if (this.recordPlayingUpFor >= 52) {
                l1 = (int)(255.0f * ((60 - this.recordPlayingUpFor) / 8.0f));
            }
            if (l1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0f);
                GLUtil.startBlend();
                int m = 16777215;
                if (this.recordIsPlaying) {
                    m = (MathHelper.hsvToRGB(f3 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF);
                }
                this.getFontRenderer().drawStringWithShadow(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2.0f, -4.0f - 15.0f * GuiChat.openingAnimation.getOutput().floatValue(), m + (l1 << 24 & 0xFF000000));
                GLUtil.endBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.titlesTimer > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            final float f4 = this.titlesTimer - partialTicks;
            int i2 = 255;
            if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
                final float f5 = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut - f4;
                i2 = (int)(f5 * 255.0f / this.titleFadeIn);
            }
            if (this.titlesTimer <= this.titleFadeOut) {
                i2 = (int)(f4 * 255.0f / this.titleFadeOut);
            }
            i2 = MathHelper.clamp_int(i2, 0, 255);
            if (i2 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j / 2), 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                final int j3 = i2 << 24 & 0xFF000000;
                this.getFontRenderer().drawString(this.displayedTitle, -this.getFontRenderer().getStringWidth(this.displayedTitle) / 2.0f, -10.0f, 0xFFFFFF | j3, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                this.getFontRenderer().drawString(this.displayedSubTitle, -this.getFontRenderer().getStringWidth(this.displayedSubTitle) / 2.0f, 5.0f, 0xFFFFFF | j3, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        final Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
        if (scoreplayerteam != null) {
            final int i3 = scoreplayerteam.getChatFormat().getColorIndex();
            if (i3 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i3);
            }
        }
        ScoreObjective scoreobjective2 = (scoreobjective != null) ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective2 != null && Tenacity.INSTANCE.isEnabled(ScoreboardMod.class)) {
            this.renderScoreboard(scoreobjective2, scaledresolution);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, (float)(j - 48), 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective2 = scoreboard.getObjectiveInDisplaySlot(0);
        if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.getPlayerInfoMap().size() > 1 || scoreobjective2 != null)) {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective2);
        }
        else {
            this.overlayPlayerList.updatePlayerList(false);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }
    
    protected void renderTooltip(final ScaledResolution sr, final float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngame.widgetsTexPath);
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final int i = sr.getScaledWidth() / 2;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Gui.drawTexturedModalRect((float)(i - 91), sr.getScaledHeight() - (22.0f + 15.0f * GuiChat.openingAnimation.getOutput().floatValue()), 0, 0, 182, 22);
            Gui.drawTexturedModalRect((float)(i - 91 - 1 + entityplayer.inventory.currentItem * 20), sr.getScaledHeight() - (22.0f + 15.0f * GuiChat.openingAnimation.getOutput().floatValue()), 0, 22, 24, 22);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            for (int j = 0; j < 9; ++j) {
                final int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                final int l = (int)(sr.getScaledHeight() - (19.0f + 15.0f * GuiChat.openingAnimation.getOutput().floatValue()));
                this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }
    
    public void renderHorseJumpBar(final ScaledResolution scaledRes, final int x) {
        this.mc.mcProfiler.startSection("jumpBar");
        final float f = this.mc.thePlayer.getHorseJumpPower();
        final int k = scaledRes.getScaledHeight() - 32 + 3;
        this.jumpAnim.animate(f, 18);
        final Color color = ColorUtil.blendColors(new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { new Color(5, 134, 105), new Color(236, 129, 44), new Color(250, 50, 56) }, this.jumpAnim.getOutput());
        RoundedUtil.drawRound((float)x, (float)k, 182.0f, 5.0f, 2.0f, new Color(43, 42, 43));
        RoundedUtil.drawRound((float)x, (float)k, this.jumpAnim.getOutput() * 182.0f, 5.0f, 2.0f, color);
        this.mc.mcProfiler.endSection();
    }
    
    public void renderExpBar(final ScaledResolution scaledRes, final int x) {
        this.mc.mcProfiler.startSection("expBar");
        if (this.mc.thePlayer.xpBarCap() > 0) {
            final int l = scaledRes.getScaledHeight() - 32 + 3;
            if (this.mc.thePlayer.experienceLevel > 0) {
                final String str = "EXP " + this.mc.thePlayer.experienceLevel;
                final float length = GuiIngame.tenacityBoldFont14.getStringWidth(str);
                this.expAnim.animate(this.mc.thePlayer.experience, 18);
                RoundedUtil.drawRound(x + length + 2.0f, (float)l, 182.0f - length - 2.0f, 5.0f, 2.0f, new Color(43, 42, 43));
                RoundedUtil.drawRound(x + length + 2.0f, (float)l, this.expAnim.getOutput() * (182.0f - length - 2.0f), 5.0f, 2.0f, new Color(0, 168, 107));
                GuiIngame.tenacityBoldFont14.drawString(str, (float)x, l - GuiIngame.tenacityBoldFont14.getHeight() / 2.0f + 2.5f, -1);
            }
            else {
                RoundedUtil.drawRound((float)x, (float)l, 182.0f, 5.0f, 2.0f, new Color(43, 42, 43));
                RoundedUtil.drawRound((float)x, (float)l, this.mc.thePlayer.experience * 182.0f, 5.0f, 2.0f, new Color(0, 168, 107));
            }
        }
        this.mc.mcProfiler.endSection();
    }
    
    public void renderSelectedItem(final ScaledResolution scaledRes) {
        this.mc.mcProfiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            String s = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                s = EnumChatFormatting.ITALIC + s;
            }
            final float i = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2.0f;
            int j = scaledRes.getScaledHeight() - 59;
            if (!this.mc.playerController.shouldDrawHUD()) {
                j += 14;
            }
            final int k = (int)(this.remainingHighlightTicks * 255.0f / 10.0f);
            if (k > 0) {
                this.getFontRenderer().drawStringWithShadow(s, i, j - 15.0f * GuiChat.openingAnimation.getOutput().floatValue(), ColorUtil.applyOpacity(Color.WHITE, k / 255.0f));
            }
        }
        this.mc.mcProfiler.endSection();
    }
    
    public void renderDemo(final ScaledResolution scaledRes) {
        this.mc.mcProfiler.startSection("demo");
        String s = "";
        if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
            s = I18n.format("demo.demoExpired", new Object[0]);
        }
        else {
            s = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime())));
        }
        final float i = this.getFontRenderer().getStringWidth(s);
        this.getFontRenderer().drawStringWithShadow(s, scaledRes.getScaledWidth() - i - 10.0f, 5.0f, 16777215);
        this.mc.mcProfiler.endSection();
    }
    
    protected boolean showCrosshair() {
        return (!this.mc.gameSettings.showDebugInfo || this.mc.thePlayer.hasReducedDebug() || this.mc.gameSettings.reducedDebugInfo) && (!this.mc.playerController.isSpectator() || this.mc.pointedEntity != null || (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.theWorld.getTileEntity(this.mc.objectMouseOver.getBlockPos()) instanceof IInventory));
    }
    
    public void renderScoreboardBlur(final ScaledResolution scaledRes) {
        final Scoreboard scoreboardOBJ = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        final ScorePlayerTeam scoreplayerteamObj = scoreboardOBJ.getPlayersTeam(this.mc.thePlayer.getName());
        if (scoreplayerteamObj != null) {
            final int i1 = scoreplayerteamObj.getChatFormat().getColorIndex();
            if (i1 >= 0) {
                scoreobjective = scoreboardOBJ.getObjectiveInDisplaySlot(3 + i1);
            }
        }
        final ScoreObjective objective = (scoreobjective != null) ? scoreobjective : scoreboardOBJ.getObjectiveInDisplaySlot(1);
        if (objective != null && Tenacity.INSTANCE.isEnabled(ScoreboardMod.class)) {
            final Scoreboard scoreboard = objective.getScoreboard();
            Collection<Score> collection = scoreboard.getSortedScores(objective);
            final List<Score> list = (List<Score>)Lists.newArrayList(Iterables.filter((Iterable)collection, p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")));
            if (list.size() > 15) {
                collection = (Collection<Score>)Lists.newArrayList(Iterables.skip((Iterable)list, collection.size() - 15));
            }
            else {
                collection = list;
            }
            float j = this.getScoreboardFontRenderer().getStringWidth(objective.getDisplayName());
            for (final Score score : collection) {
                final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                final String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                j = Math.max(j, this.getScoreboardFontRenderer().getStringWidth(s));
            }
            final int i2 = collection.size() * this.getScoreboardFontRenderer().getHeight();
            final int j2 = scaledRes.getScaledHeight() / 2 + i2 / 3 + ScoreboardMod.yOffset.getValue().intValue();
            final int k1 = 3;
            final float l1 = scaledRes.getScaledWidth() - j - k1;
            int m = 0;
            for (final Score score2 : collection) {
                ++m;
                final ScorePlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(score2.getPlayerName());
                final String s2 = ScorePlayerTeam.formatPlayerName(scoreplayerteam2, score2.getPlayerName());
                final int k2 = j2 - m * this.getScoreboardFontRenderer().getHeight();
                final int l2 = scaledRes.getScaledWidth() - k1 + 2;
                Gui.drawRect(l1 - 2.0f, k2, l2, k2 + this.getScoreboardFontRenderer().getHeight(), Color.BLACK.getRGB());
                if (m == collection.size()) {
                    final String s3 = objective.getDisplayName();
                    Gui.drawRect(l1 - 2.0f, k2 - this.getScoreboardFontRenderer().getHeight() - 1, l2, k2 - 1, Color.BLACK.getRGB());
                    Gui.drawRect(l1 - 2.0f, k2 - 1, l2, k2, Color.BLACK.getRGB());
                }
            }
        }
    }
    
    public void renderScoreboard(final ScoreObjective objective, final ScaledResolution scaledRes) {
        final Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        final List<Score> list = (List<Score>)Lists.newArrayList(Iterables.filter((Iterable)collection, p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")));
        if (list.size() > 15) {
            collection = (Collection<Score>)Lists.newArrayList(Iterables.skip((Iterable)list, collection.size() - 15));
        }
        else {
            collection = list;
        }
        float i = this.getScoreboardFontRenderer().getStringWidth(objective.getDisplayName());
        for (final Score score : collection) {
            final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            final String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max(i, this.getScoreboardFontRenderer().getStringWidth(s));
        }
        final int i2 = collection.size() * this.getScoreboardFontRenderer().getHeight();
        final int j1 = scaledRes.getScaledHeight() / 2 + i2 / 3 + ScoreboardMod.yOffset.getValue().intValue();
        final float l1 = scaledRes.getScaledWidth() - i - 3.0f;
        int k = 0;
        final Color color = ColorUtil.applyOpacity(Color.BLACK, 0.29411766f);
        for (final Score score2 : collection) {
            ++k;
            final ScorePlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(score2.getPlayerName());
            final String s2 = ScorePlayerTeam.formatPlayerName(scoreplayerteam2, score2.getPlayerName());
            final int m = j1 - k * this.getScoreboardFontRenderer().getHeight();
            final int l2 = scaledRes.getScaledWidth() - 3 + 2;
            GLUtil.startBlend();
            Gui.drawRect(l1 - 2.0f, m, l2, m + this.getScoreboardFontRenderer().getHeight(), color.getRGB());
            this.getScoreboardFontRenderer().drawString(s2, l1, (float)m, -1, ScoreboardMod.textShadow.isEnabled());
            if (ScoreboardMod.redNumbers.isEnabled()) {
                final String s3 = EnumChatFormatting.RED + "" + score2.getScorePoints();
                this.getScoreboardFontRenderer().drawString(s3, l2 - this.getScoreboardFontRenderer().getStringWidth(s3), (float)m, -1, ScoreboardMod.textShadow.isEnabled());
            }
            if (k == collection.size()) {
                final String s4 = objective.getDisplayName();
                Gui.drawRect(l1 - 2.0f, m - this.getScoreboardFontRenderer().getHeight() - 1, l2, m - 1, color.getRGB());
                GLUtil.startBlend();
                Gui.drawRect(l1 - 2.0f, m - 1, l2, m, color.getRGB());
                this.getScoreboardFontRenderer().drawString(s4, l1 + i / 2.0f - this.getScoreboardFontRenderer().getStringWidth(s4) / 2.0f, (float)(m - this.getScoreboardFontRenderer().getHeight()), -1, ScoreboardMod.textShadow.isEnabled());
            }
        }
    }
    
    private void renderPlayerStats(final ScaledResolution scaledRes) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            }
            else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = i;
                this.lastPlayerHealth = i;
                this.lastSystemTime = Minecraft.getSystemTime();
            }
            this.playerHealth = i;
            this.rand.setSeed(this.updateCounter * 312871L);
            final int halfWidth = scaledRes.getScaledWidth() / 2;
            final int k1 = scaledRes.getScaledHeight() - 40;
            final int j2 = k1 - 12;
            this.mc.mcProfiler.startSection("armor");
            if (entityplayer.getTotalArmorValue() > 0) {
                this.armorAnim.animate(entityplayer.getTotalArmorValue() / 20.0f, 18);
                RoundedUtil.drawRound((float)(halfWidth - 91), (float)j2, 85.0f, 8.0f, 2.0f, new Color(43, 42, 43));
                RoundedUtil.drawRound((float)(halfWidth - 91), (float)j2, 85.0f * this.armorAnim.getOutput(), 8.0f, 2.0f, new Color(0, 168, 107));
                GuiIngame.tenacityBoldFont14.drawString("Armor " + entityplayer.getTotalArmorValue() + " | 20", (float)(halfWidth - 91 + 2), j2 + 4 - GuiIngame.tenacityBoldFont14.getHeight() / 2.0f, -1);
            }
            this.mc.mcProfiler.endStartSection("health");
            this.healthAnim.animate(Math.min((entityplayer.getHealth() + entityplayer.getAbsorptionAmount()) / entityplayer.getMaxHealth(), 1.0f), 18);
            RoundedUtil.drawRound((float)(halfWidth - 91), (float)k1, 85.0f, 8.0f, 2.0f, new Color(43, 42, 43));
            RoundedUtil.drawRound((float)(halfWidth - 91), (float)k1, 85.0f * this.healthAnim.getOutput(), 8.0f, 2.0f, new Color(250, 50, 56));
            GuiIngame.tenacityBoldFont14.drawString("HP " + Math.ceil(entityplayer.getHealth() + entityplayer.getAbsorptionAmount()) + " | " + entityplayer.getMaxHealth(), (float)(halfWidth - 91 + 2), k1 + 4 - GuiIngame.tenacityBoldFont14.getHeight() / 2.0f, -1);
            this.mc.mcProfiler.endStartSection("food");
            this.foodAnim.animate(entityplayer.getFoodStats().getFoodLevel() / 20.0f, 18);
            this.saturationAnim.animate(entityplayer.getFoodStats().getSaturationLevel() / 20.0f, 18);
            RoundedUtil.drawRound((float)(halfWidth + 6), (float)k1, 85.0f, 8.0f, 2.0f, new Color(43, 42, 43));
            RoundedUtil.drawRound((float)(halfWidth + 6), (float)k1, 85.0f * this.foodAnim.getOutput(), 8.0f, 2.0f, new Color(16755200));
            RoundedUtil.drawRound((float)(halfWidth + 6), (float)k1, 85.0f * this.saturationAnim.getOutput(), 8.0f, 2.0f, new Color(255, 113, 0));
            GuiIngame.tenacityBoldFont14.drawString("Food " + entityplayer.getFoodStats().getFoodLevel() + " | 20", (float)(halfWidth + 6 + 2), k1 + 4 - GuiIngame.tenacityBoldFont14.getHeight() / 2.0f, -1);
            this.mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.water)) {
                final int l6 = this.mc.thePlayer.getAir();
                final int k2 = MathHelper.ceiling_double_int((l6 - 2) * 10.0 / 300.0);
                this.airAnim.animate(k2 / 10.0f, 18);
                RoundedUtil.drawRound((float)(halfWidth + 6), (float)j2, 85.0f, 8.0f, 2.0f, new Color(43, 42, 43));
                RoundedUtil.drawRound((float)(halfWidth + 6), (float)j2, 85.0f * this.airAnim.getOutput(), 8.0f, 2.0f, new Color(28, 167, 222));
                GuiIngame.tenacityBoldFont14.drawString("Drown in " + k2 + "s", (float)(halfWidth + 6 + 2), j2 + 4 - GuiIngame.tenacityBoldFont14.getHeight() / 2.0f, -1);
            }
            this.mc.mcProfiler.endSection();
        }
    }
    
    public void renderEffects() {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, -15.0f * GuiChat.openingAnimation.getOutput().floatValue(), 0.0f);
        final ScaledResolution scaledRes = new ScaledResolution(this.mc);
        final float halfWidth = scaledRes.getScaledWidth() / 2.0f;
        final int k1 = scaledRes.getScaledHeight() - 40;
        final int j2 = k1 - 12;
        final float x = halfWidth - 91.0f;
        final float i = (float)(scaledRes.getScaledHeight() - 32 + 3);
        if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            if (this.mc.thePlayer.getTotalArmorValue() > 0) {
                RoundedUtil.drawRound(halfWidth - 91.0f, (float)j2, 85.0f, 8.0f, 2.0f, Color.BLACK);
            }
            RoundedUtil.drawRound(halfWidth - 91.0f, (float)k1, 85.0f, 8.0f, 2.0f, Color.BLACK);
            RoundedUtil.drawRound(halfWidth + 6.0f, (float)k1, 85.0f, 8.0f, 2.0f, Color.BLACK);
            if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
                RoundedUtil.drawRound(halfWidth + 6.0f, (float)j2, 85.0f, 8.0f, 2.0f, Color.BLACK);
            }
            if (this.mc.thePlayer.xpBarCap() > 0) {
                if (this.mc.thePlayer.experienceLevel > 0) {
                    final String str = "EXP " + this.mc.thePlayer.experienceLevel;
                    final float length = GuiIngame.tenacityBoldFont14.getStringWidth(str);
                    RoundedUtil.drawRound(x + length + 2.0f, i, 182.0f - length - 2.0f, 5.0f, 2.0f, Color.BLACK);
                }
                else {
                    RoundedUtil.drawRound(x, i, 182.0f, 5.0f, 2.0f, Color.BLACK);
                }
            }
        }
        if (this.mc.thePlayer.isRidingHorse()) {
            RoundedUtil.drawRound(x, i, 182.0f, 5.0f, 2.0f, Color.BLACK);
        }
        GlStateManager.popMatrix();
    }
    
    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            final IFontRenderer fontrenderer = this.mc.fontRendererObj;
            final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            final int scaledWidth = scaledresolution.getScaledWidth();
            final int width = 182;
            final int k = scaledresolution.getScaledWidth() / 2 - 91;
            final int l = (int)(BossStatus.healthScale * (width + 1));
            final int i1 = 12;
            this.drawTexturedModalRect(k, i1, 0, 74, width, 5);
            this.drawTexturedModalRect(k, i1, 0, 74, width, 5);
            if (l > 0) {
                this.drawTexturedModalRect(k, i1, 0, 79, l, 5);
            }
            final String s = BossStatus.bossName;
            this.getFontRenderer().drawStringWithShadow(s, scaledWidth / 2.0f - this.getFontRenderer().getStringWidth(s) / 2.0f, 2.0f, 16777215);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngame.icons);
        }
    }
    
    private void renderPumpkinOverlay(final ScaledResolution scaledRes) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(GuiIngame.pumpkinBlurTexPath);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderVignette(float lightLevel, final ScaledResolution scaledRes) {
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        else {
            lightLevel = 1.0f - lightLevel;
            lightLevel = MathHelper.clamp_float(lightLevel, 0.0f, 1.0f);
            final WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
            float f = (float)worldborder.getClosestDistance(this.mc.thePlayer);
            final double d0 = Math.min(worldborder.getResizeSpeed() * worldborder.getWarningTime() * 1000.0, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
            final double d2 = Math.max(worldborder.getWarningDistance(), d0);
            if (f < d2) {
                f = 1.0f - (float)(f / d2);
            }
            else {
                f = 0.0f;
            }
            this.prevVignetteBrightness += (float)((lightLevel - this.prevVignetteBrightness) * 0.01);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
            if (f > 0.0f) {
                GlStateManager.color(0.0f, f, f, 1.0f);
            }
            else {
                GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
            }
            this.mc.getTextureManager().bindTexture(GuiIngame.vignetteTexPath);
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
            worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
            worldrenderer.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
            worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
    }
    
    private void renderPortal(float timeInPortal, final ScaledResolution scaledRes) {
        if (timeInPortal < 1.0f) {
            timeInPortal *= timeInPortal;
            timeInPortal *= timeInPortal;
            timeInPortal = timeInPortal * 0.8f + 0.2f;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, timeInPortal);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        final TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        final float f = textureatlassprite.getMinU();
        final float f2 = textureatlassprite.getMinV();
        final float f3 = textureatlassprite.getMaxU();
        final float f4 = textureatlassprite.getMaxV();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(f, f4).endVertex();
        worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(f3, f4).endVertex();
        worldrenderer.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(f3, f2).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(f, f2).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderHotbarItem(final int index, final int xPos, final int yPos, final float partialTicks, final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.mainInventory[index];
        if (itemstack != null) {
            final float f = itemstack.animationsToGo - partialTicks;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                final float f2 = 1.0f + f / 5.0f;
                GlStateManager.translate((float)(xPos + 8), (float)(yPos + 12), 0.0f);
                GlStateManager.scale(1.0f / f2, (f2 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate((float)(-(xPos + 8)), (float)(-(yPos + 12)), 0.0f);
            }
            this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
            if (f > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, itemstack, xPos, yPos);
        }
    }
    
    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }
        if (this.titlesTimer > 0) {
            --this.titlesTimer;
            if (this.titlesTimer <= 0) {
                this.displayedTitle = "";
                this.displayedSubTitle = "";
            }
        }
        ++this.updateCounter;
        if (this.mc.thePlayer != null) {
            final ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();
            if (itemstack == null) {
                this.remainingHighlightTicks = 0;
            }
            else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            }
            else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = itemstack;
        }
    }
    
    public void setRecordPlayingMessage(final String recordName) {
        this.setRecordPlaying(I18n.format("record.nowPlaying", recordName), true);
    }
    
    public void setRecordPlaying(final String message, final boolean isPlaying) {
        this.recordPlaying = message;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = isPlaying;
    }
    
    public void displayTitle(final String title, final String subTitle, final int timeFadeIn, final int displayTime, final int timeFadeOut) {
        if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0) {
            this.displayedTitle = "";
            this.displayedSubTitle = "";
            this.titlesTimer = 0;
        }
        else if (title != null) {
            this.displayedTitle = title;
            this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
        }
        else if (subTitle != null) {
            this.displayedSubTitle = subTitle;
        }
        else {
            if (timeFadeIn >= 0) {
                this.titleFadeIn = timeFadeIn;
            }
            if (displayTime >= 0) {
                this.titleDisplayTime = displayTime;
            }
            if (timeFadeOut >= 0) {
                this.titleFadeOut = timeFadeOut;
            }
            if (this.titlesTimer > 0) {
                this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
            }
        }
    }
    
    public void setRecordPlaying(final IChatComponent component, final boolean isPlaying) {
        this.setRecordPlaying(component.getUnformattedText(), isPlaying);
    }
    
    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }
    
    public int getUpdateCounter() {
        return this.updateCounter;
    }
    
    public AbstractFontRenderer getFontRenderer() {
        return this.mc.fontRendererObj;
    }
    
    public AbstractFontRenderer getScoreboardFontRenderer() {
        return ScoreboardMod.customFont.isEnabled() ? GuiIngame.tenacityFont20 : this.mc.fontRendererObj;
    }
    
    public GuiSpectator getSpectatorGui() {
        return this.spectatorGui;
    }
    
    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }
    
    public void resetPlayersOverlayFooterHeader() {
        this.overlayPlayerList.resetFooterHeader();
    }
    
    static {
        vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
        widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
        pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    }
}
