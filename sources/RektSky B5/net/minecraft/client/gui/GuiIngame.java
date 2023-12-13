/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.GuiStreamIndicator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.border.WorldBorder;
import optfine.Config;
import tk.rektsky.Client;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.render.NoClientRender;

public class GuiIngame
extends Gui {
    private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    private static final HyperiumFontRenderer fr = Client.getFont();
    private static final HyperiumFontRenderer frbig = Client.getFontBig();
    private static final HyperiumFontRenderer frhd = Client.getHDFont();
    private final Random rand = new Random();
    private final Minecraft mc;
    public final RenderItem itemRenderer;
    private final GuiNewChat persistantChatGUI;
    private final GuiStreamIndicator streamIndicator;
    private int updateCounter;
    private String recordPlaying = "";
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    public float prevVignetteBrightness = 1.0f;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private int field_175195_w;
    public String title = "";
    public String subtitle = "";
    private int field_175199_z;
    private int field_175192_A;
    private int field_175193_B;
    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    private long lastSystemTime = 0L;
    private long healthUpdateCounter = 0L;
    private static final String __OBFID = "CL_00000661";

    public GuiIngame(Minecraft mcIn) {
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
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

    public void renderGameOverlay(float partialTicks) {
        ScoreObjective scoreobjective1;
        int j1;
        float f2;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int i2 = scaledresolution.getScaledWidth();
        int j2 = scaledresolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        if (Config.isVignetteEnabled()) {
            this.renderVignette(this.mc.thePlayer.getBrightness(partialTicks), scaledresolution);
        } else {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.renderPumpkinOverlay(scaledresolution);
        }
        if (!this.mc.thePlayer.isPotionActive(Potion.confusion) && (f2 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks) > 0.0f) {
            this.func_180474_b(f2, scaledresolution);
        }
        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        } else {
            this.renderTooltip(scaledresolution, partialTicks);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(icons);
        GlStateManager.enableBlend();
        if (this.showCrosshair()) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(i2 / 2 - 7, j2 / 2 - 7, 0, 0, 16, 16);
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();
        if (this.mc.playerController.shouldDrawHUD()) {
            this.renderPlayerStats(scaledresolution);
        }
        GlStateManager.disableBlend();
        if (this.mc.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int l2 = this.mc.thePlayer.getSleepTimer();
            float f22 = (float)l2 / 100.0f;
            if (f22 > 1.0f) {
                f22 = 1.0f - (float)(l2 - 100) / 10.0f;
            }
            int k2 = (int)(220.0f * f22) << 24 | 0x101020;
            GuiIngame.drawRect(0, 0, i2, j2, k2);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int i22 = i2 / 2 - 91;
        if (this.mc.thePlayer.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, i22);
        } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, i22);
        }
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.func_181551_a(scaledresolution);
        } else if (this.mc.thePlayer.isSpectator()) {
            this.spectatorGui.func_175263_a(scaledresolution);
        }
        if (this.mc.isDemo()) {
            this.renderDemo(scaledresolution);
        }
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            float f3 = (float)this.recordPlayingUpFor - partialTicks;
            int k1 = (int)(f3 * 255.0f / 20.0f);
            if (k1 > 255) {
                k1 = 255;
            }
            if (k1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(i2 / 2, j2 - 68, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int i1 = 0xFFFFFF;
                if (this.recordIsPlaying) {
                    i1 = MathHelper.func_181758_c(f3 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF;
                }
                this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4, i1 + (k1 << 24 & 0xFF000000));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.field_175195_w > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            float f4 = (float)this.field_175195_w - partialTicks;
            int l1 = 255;
            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                float f1 = (float)(this.field_175199_z + this.field_175192_A + this.field_175193_B) - f4;
                l1 = (int)(f1 * 255.0f / (float)this.field_175199_z);
            }
            if (this.field_175195_w <= this.field_175193_B) {
                l1 = (int)(f4 * 255.0f / (float)this.field_175193_B);
            }
            if ((l1 = MathHelper.clamp_int(l1, 0, 255)) > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(i2 / 2, j2 / 2, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                int j22 = l1 << 24 & 0xFF000000;
                this.getFontRenderer().drawString(this.title, -this.getFontRenderer().getStringWidth(this.title) / 2, -10.0f, 0xFFFFFF | j22, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                this.getFontRenderer().drawString(this.subtitle, -this.getFontRenderer().getStringWidth(this.subtitle) / 2, 5.0f, 0xFFFFFF | j22, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
        if (scoreplayerteam != null && (j1 = scoreplayerteam.getChatFormat().getColorIndex()) >= 0) {
            scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + j1);
        }
        ScoreObjective scoreObjective = scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective1 != null) {
            this.renderScoreboard(scoreobjective1, scaledresolution);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, j2 - 48, 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
        if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown() || this.mc.isIntegratedServerRunning() && this.mc.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1 && scoreobjective1 == null) {
            this.overlayPlayerList.updatePlayerList(false);
        } else {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i2, scoreboard, scoreobjective1);
        }
        if (!ModulesManager.getModuleByClass(NoClientRender.class).isToggled() && !Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            Client.mainhud.draw(scaledresolution, this);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }

    private int getScaledX() {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return newSR.getScaledWidth();
    }

    private int getScaledY() {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return newSR.getScaledHeight();
    }

    private float fromPercentToScaledX(float x2) {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return (float)newSR.getScaledWidth() / 100.0f * x2;
    }

    private float fromPercentToScaledY(float y2) {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return (float)newSR.getScaledHeight() / 100.0f * y2;
    }

    protected void renderTooltip(ScaledResolution sr, float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i2 = sr.getScaledWidth() / 2;
            float f2 = this.zLevel;
            this.zLevel = -90.0f;
            this.drawTexturedModalRect(i2 - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(i2 - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            this.zLevel = f2;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            for (int j2 = 0; j2 < 9; ++j2) {
                int k2 = sr.getScaledWidth() / 2 - 90 + j2 * 20 + 2;
                int l2 = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j2, k2, l2, partialTicks, entityplayer);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    public void renderHorseJumpBar(ScaledResolution p_175186_1_, int p_175186_2_) {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        float f2 = this.mc.thePlayer.getHorseJumpPower();
        int short1 = 182;
        int i2 = (int)(f2 * (float)(short1 + 1));
        int j2 = p_175186_1_.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(p_175186_2_, j2, 0, 84, short1, 5);
        if (i2 > 0) {
            this.drawTexturedModalRect(p_175186_2_, j2, 0, 89, i2, 5);
        }
        this.mc.mcProfiler.endSection();
    }

    public void renderExpBar(ScaledResolution p_175176_1_, int p_175176_2_) {
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int i2 = this.mc.thePlayer.xpBarCap();
        if (i2 > 0) {
            int short1 = 182;
            int k2 = (int)(this.mc.thePlayer.experience * (float)(short1 + 1));
            int j2 = p_175176_1_.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(p_175176_2_, j2, 0, 64, short1, 5);
            if (k2 > 0) {
                this.drawTexturedModalRect(p_175176_2_, j2, 0, 69, k2, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (this.mc.thePlayer.experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            int j1 = 8453920;
            String s2 = "" + this.mc.thePlayer.experienceLevel;
            int i1 = (p_175176_1_.getScaledWidth() - this.getFontRenderer().getStringWidth(s2)) / 2;
            int l2 = p_175176_1_.getScaledHeight() - 31 - 4;
            boolean flag = false;
            this.getFontRenderer().drawString(s2, i1 + 1, l2, 0);
            this.getFontRenderer().drawString(s2, i1 - 1, l2, 0);
            this.getFontRenderer().drawString(s2, i1, l2 + 1, 0);
            this.getFontRenderer().drawString(s2, i1, l2 - 1, 0);
            this.getFontRenderer().drawString(s2, i1, l2, j1);
            this.mc.mcProfiler.endSection();
        }
    }

    public void func_181551_a(ScaledResolution p_181551_1_) {
        this.mc.mcProfiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            int k2;
            String s2 = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                s2 = (Object)((Object)EnumChatFormatting.ITALIC) + s2;
            }
            int i2 = (p_181551_1_.getScaledWidth() - this.getFontRenderer().getStringWidth(s2)) / 2;
            int j2 = p_181551_1_.getScaledHeight() - 59;
            if (!this.mc.playerController.shouldDrawHUD()) {
                j2 += 14;
            }
            if ((k2 = (int)((float)this.remainingHighlightTicks * 256.0f / 10.0f)) > 255) {
                k2 = 255;
            }
            if (k2 > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.getFontRenderer().drawStringWithShadow(s2, i2, j2, 0xFFFFFF + (k2 << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }

    public void renderDemo(ScaledResolution p_175185_1_) {
        this.mc.mcProfiler.startSection("demo");
        String s2 = "";
        s2 = this.mc.theWorld.getTotalWorldTime() >= 120500L ? I18n.format("demo.demoExpired", new Object[0]) : I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime())));
        int i2 = this.getFontRenderer().getStringWidth(s2);
        this.getFontRenderer().drawStringWithShadow(s2, p_175185_1_.getScaledWidth() - i2 - 10, 5.0f, 0xFFFFFF);
        this.mc.mcProfiler.endSection();
    }

    protected boolean showCrosshair() {
        if (this.mc.gameSettings.showDebugInfo && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
            return false;
        }
        if (this.mc.playerController.isSpectator()) {
            BlockPos blockpos;
            if (this.mc.pointedEntity != null) {
                return true;
            }
            return this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.theWorld.getTileEntity(blockpos = this.mc.objectMouseOver.getBlockPos()) instanceof IInventory;
        }
        return true;
    }

    public void renderStreamIndicator(ScaledResolution p_180478_1_) {
        this.streamIndicator.render(p_180478_1_.getScaledWidth() - 10, 10);
    }

    private void renderScoreboard(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_) {
        Scoreboard scoreboard = p_180475_1_.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(p_180475_1_);
        ArrayList<Score> arraylist = Lists.newArrayList(Iterables.filter(collection, new Predicate(){
            private static final String __OBFID = "CL_00001958";

            public boolean apply(Score p_apply_1_) {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }

            public boolean apply(Object p_apply_1_) {
                return this.apply((Score)p_apply_1_);
            }
        }));
        ArrayList<Score> arraylist1 = arraylist.size() > 15 ? Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15)) : arraylist;
        int i2 = this.getFontRenderer().getStringWidth(p_180475_1_.getDisplayName());
        Iterator<Score> iterator = arraylist1.iterator();
        while (iterator.hasNext()) {
            Score score0;
            Score score = score0 = iterator.next();
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s2 = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + (Object)((Object)EnumChatFormatting.RED) + score.getScorePoints();
            i2 = Math.max(i2, this.getFontRenderer().getStringWidth(s2));
        }
        int j1 = arraylist1.size() * this.getFontRenderer().FONT_HEIGHT;
        int k1 = p_180475_2_.getScaledHeight() / 2 + j1 / 3;
        int b0 = 3;
        int j2 = p_180475_2_.getScaledWidth() - i2 - b0;
        int k2 = 0;
        Iterator<Score> iterator2 = arraylist1.iterator();
        while (iterator2.hasNext()) {
            Score score10;
            Score score1 = score10 = iterator2.next();
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = (Object)((Object)EnumChatFormatting.RED) + "" + score1.getScorePoints();
            int l2 = k1 - ++k2 * this.getFontRenderer().FONT_HEIGHT;
            int i1 = p_180475_2_.getScaledWidth() - b0 + 2;
            GuiIngame.drawRect(j2 - 2, l2, i1, l2 + this.getFontRenderer().FONT_HEIGHT, 0x50000000);
            this.getFontRenderer().drawString(s1, j2, l2, 0x20FFFFFF);
            this.getFontRenderer().drawString(s2, i1 - this.getFontRenderer().getStringWidth(s2), l2, 0x20FFFFFF);
            if (k2 != arraylist1.size()) continue;
            String s3 = p_180475_1_.getDisplayName();
            GuiIngame.drawRect(j2 - 2, l2 - this.getFontRenderer().FONT_HEIGHT - 1, i1, l2 - 1, 0x60000000);
            GuiIngame.drawRect(j2 - 2, l2 - 1, i1, l2, 0x50000000);
            this.getFontRenderer().drawString(s3, j2 + i2 / 2 - this.getFontRenderer().getStringWidth(s3) / 2, l2 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
        }
    }

    private void renderPlayerStats(ScaledResolution p_180477_1_) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            boolean flag;
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i2 = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean bl = flag = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;
            if (i2 < this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            } else if (i2 > this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = i2;
                this.lastPlayerHealth = i2;
                this.lastSystemTime = Minecraft.getSystemTime();
            }
            this.playerHealth = i2;
            int j2 = this.lastPlayerHealth;
            this.rand.setSeed(this.updateCounter * 312871);
            boolean flag1 = false;
            FoodStats foodstats = entityplayer.getFoodStats();
            int k2 = foodstats.getFoodLevel();
            int l2 = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = p_180477_1_.getScaledWidth() / 2 - 91;
            int j1 = p_180477_1_.getScaledWidth() / 2 + 91;
            int k1 = p_180477_1_.getScaledHeight() - 39;
            float f2 = (float)iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((f2 + f1) / 2.0f / 10.0f);
            int i22 = Math.max(10 - (l1 - 2), 3);
            int j22 = k1 - (l1 - 1) * i22 - 10;
            float f22 = f1;
            int k22 = entityplayer.getTotalArmorValue();
            int l22 = -1;
            if (entityplayer.isPotionActive(Potion.regeneration)) {
                l22 = this.updateCounter % MathHelper.ceiling_float_int(f2 + 5.0f);
            }
            this.mc.mcProfiler.startSection("armor");
            for (int i3 = 0; i3 < 10; ++i3) {
                if (k22 <= 0) continue;
                int j3 = i1 + i3 * 8;
                if (i3 * 2 + 1 < k22) {
                    this.drawTexturedModalRect(j3, j22, 34, 9, 9, 9);
                }
                if (i3 * 2 + 1 == k22) {
                    this.drawTexturedModalRect(j3, j22, 25, 9, 9, 9);
                }
                if (i3 * 2 + 1 <= k22) continue;
                this.drawTexturedModalRect(j3, j22, 16, 9, 9, 9);
            }
            this.mc.mcProfiler.endStartSection("health");
            for (int j5 = MathHelper.ceiling_float_int((f2 + f1) / 2.0f) - 1; j5 >= 0; --j5) {
                int k5 = 16;
                if (entityplayer.isPotionActive(Potion.poison)) {
                    k5 += 36;
                } else if (entityplayer.isPotionActive(Potion.wither)) {
                    k5 += 72;
                }
                int b0 = 0;
                if (flag) {
                    b0 = 1;
                }
                int k3 = MathHelper.ceiling_float_int((float)(j5 + 1) / 10.0f) - 1;
                int l3 = i1 + j5 % 10 * 8;
                int i4 = k1 - k3 * i22;
                if (i2 <= 4) {
                    i4 += this.rand.nextInt(2);
                }
                if (j5 == l22) {
                    i4 -= 2;
                }
                int b1 = 0;
                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    b1 = 5;
                }
                this.drawTexturedModalRect(l3, i4, 16 + b0 * 9, 9 * b1, 9, 9);
                if (flag) {
                    if (j5 * 2 + 1 < j2) {
                        this.drawTexturedModalRect(l3, i4, k5 + 54, 9 * b1, 9, 9);
                    }
                    if (j5 * 2 + 1 == j2) {
                        this.drawTexturedModalRect(l3, i4, k5 + 63, 9 * b1, 9, 9);
                    }
                }
                if (f22 <= 0.0f) {
                    if (j5 * 2 + 1 < i2) {
                        this.drawTexturedModalRect(l3, i4, k5 + 36, 9 * b1, 9, 9);
                    }
                    if (j5 * 2 + 1 != i2) continue;
                    this.drawTexturedModalRect(l3, i4, k5 + 45, 9 * b1, 9, 9);
                    continue;
                }
                if (f22 == f1 && f1 % 2.0f == 1.0f) {
                    this.drawTexturedModalRect(l3, i4, k5 + 153, 9 * b1, 9, 9);
                } else {
                    this.drawTexturedModalRect(l3, i4, k5 + 144, 9 * b1, 9, 9);
                }
                f22 -= 2.0f;
            }
            Entity entity = entityplayer.ridingEntity;
            if (entity == null) {
                this.mc.mcProfiler.endStartSection("food");
                for (int l5 = 0; l5 < 10; ++l5) {
                    int i8 = k1;
                    int j6 = 16;
                    int b4 = 0;
                    if (entityplayer.isPotionActive(Potion.hunger)) {
                        j6 += 36;
                        b4 = 13;
                    }
                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (k2 * 3 + 1) == 0) {
                        i8 = k1 + (this.rand.nextInt(3) - 1);
                    }
                    if (flag1) {
                        b4 = 1;
                    }
                    int k7 = j1 - l5 * 8 - 9;
                    this.drawTexturedModalRect(k7, i8, 16 + b4 * 9, 27, 9, 9);
                    if (flag1) {
                        if (l5 * 2 + 1 < l2) {
                            this.drawTexturedModalRect(k7, i8, j6 + 54, 27, 9, 9);
                        }
                        if (l5 * 2 + 1 == l2) {
                            this.drawTexturedModalRect(k7, i8, j6 + 63, 27, 9, 9);
                        }
                    }
                    if (l5 * 2 + 1 < k2) {
                        this.drawTexturedModalRect(k7, i8, j6 + 36, 27, 9, 9);
                    }
                    if (l5 * 2 + 1 != k2) continue;
                    this.drawTexturedModalRect(k7, i8, j6 + 45, 27, 9, 9);
                }
            } else if (entity instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                int l7 = (int)Math.ceil(entitylivingbase.getHealth());
                float f3 = entitylivingbase.getMaxHealth();
                int l6 = (int)(f3 + 0.5f) / 2;
                if (l6 > 30) {
                    l6 = 30;
                }
                int j7 = k1;
                int j4 = 0;
                while (l6 > 0) {
                    int k4 = Math.min(l6, 10);
                    l6 -= k4;
                    for (int l4 = 0; l4 < k4; ++l4) {
                        int b2 = 52;
                        int b3 = 0;
                        if (flag1) {
                            b3 = 1;
                        }
                        int i5 = j1 - l4 * 8 - 9;
                        this.drawTexturedModalRect(i5, j7, b2 + b3 * 9, 9, 9, 9);
                        if (l4 * 2 + 1 + j4 < l7) {
                            this.drawTexturedModalRect(i5, j7, b2 + 36, 9, 9, 9);
                        }
                        if (l4 * 2 + 1 + j4 != l7) continue;
                        this.drawTexturedModalRect(i5, j7, b2 + 45, 9, 9, 9);
                    }
                    j7 -= 10;
                    j4 += 20;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.water)) {
                int i6 = this.mc.thePlayer.getAir();
                int j8 = MathHelper.ceiling_double_int((double)(i6 - 2) * 10.0 / 300.0);
                int k6 = MathHelper.ceiling_double_int((double)i6 * 10.0 / 300.0) - j8;
                for (int i7 = 0; i7 < j8 + k6; ++i7) {
                    if (i7 < j8) {
                        this.drawTexturedModalRect(j1 - i7 * 8 - 9, j22, 16, 18, 9, 9);
                        continue;
                    }
                    this.drawTexturedModalRect(j1 - i7 * 8 - 9, j22, 25, 18, 9, 9);
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }

    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            FontRenderer fontrenderer = this.mc.fontRendererObj;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i2 = scaledresolution.getScaledWidth();
            int short1 = 182;
            int j2 = i2 / 2 - short1 / 2;
            int k2 = (int)(BossStatus.healthScale * (float)(short1 + 1));
            int b0 = 12;
            this.drawTexturedModalRect(j2, b0, 0, 74, short1, 5);
            this.drawTexturedModalRect(j2, b0, 0, 74, short1, 5);
            if (k2 > 0) {
                this.drawTexturedModalRect(j2, b0, 0, 79, k2, 5);
            }
            String s2 = BossStatus.bossName;
            this.getFontRenderer().drawStringWithShadow(s2, i2 / 2 - this.getFontRenderer().getStringWidth(s2) / 2, b0 - 10, 0xFFFFFF);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }

    private void renderPumpkinOverlay(ScaledResolution p_180476_1_) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, p_180476_1_.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(p_180476_1_.getScaledWidth(), p_180476_1_.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(p_180476_1_.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderVignette(float p_180480_1_, ScaledResolution p_180480_2_) {
        if (Config.isVignetteEnabled()) {
            p_180480_1_ = 1.0f - p_180480_1_;
            p_180480_1_ = MathHelper.clamp_float(p_180480_1_, 0.0f, 1.0f);
            WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
            float f2 = (float)worldborder.getClosestDistance(this.mc.thePlayer);
            double d0 = Math.min(worldborder.getResizeSpeed() * (double)worldborder.getWarningTime() * 1000.0, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
            double d1 = Math.max((double)worldborder.getWarningDistance(), d0);
            f2 = (double)f2 < d1 ? 1.0f - (float)((double)f2 / d1) : 0.0f;
            this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(p_180480_1_ - this.prevVignetteBrightness) * 0.01);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
            if (f2 > 0.0f) {
                GlStateManager.color(0.0f, f2, f2, 1.0f);
            } else {
                GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
            }
            this.mc.getTextureManager().bindTexture(vignetteTexPath);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0, p_180480_2_.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
            worldrenderer.pos(p_180480_2_.getScaledWidth(), p_180480_2_.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
            worldrenderer.pos(p_180480_2_.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
            worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
    }

    private void func_180474_b(float p_180474_1_, ScaledResolution p_180474_2_) {
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
        TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        float f2 = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f22 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, p_180474_2_.getScaledHeight(), -90.0).tex(f2, f3).endVertex();
        worldrenderer.pos(p_180474_2_.getScaledWidth(), p_180474_2_.getScaledHeight(), -90.0).tex(f22, f3).endVertex();
        worldrenderer.pos(p_180474_2_.getScaledWidth(), 0.0, -90.0).tex(f22, f1).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(f2, f1).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_) {
        ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];
        if (itemstack != null) {
            float f2 = (float)itemstack.animationsToGo - partialTicks;
            if (f2 > 0.0f) {
                GlStateManager.pushMatrix();
                float f1 = 1.0f + f2 / 5.0f;
                GlStateManager.translate(xPos + 8, yPos + 12, 0.0f);
                GlStateManager.scale(1.0f / f1, (f1 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate(-(xPos + 8), -(yPos + 12), 0.0f);
            }
            this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
            if (f2 > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, itemstack, xPos, yPos);
        }
    }

    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }
        if (this.field_175195_w > 0) {
            --this.field_175195_w;
            if (this.field_175195_w <= 0) {
                this.title = "";
                this.subtitle = "";
            }
        }
        ++this.updateCounter;
        this.streamIndicator.func_152439_a();
        if (this.mc.thePlayer != null) {
            ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();
            if (itemstack == null) {
                this.remainingHighlightTicks = 0;
            } else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            } else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = itemstack;
        }
    }

    public void setRecordPlayingMessage(String p_73833_1_) {
        this.setRecordPlaying(I18n.format("record.nowPlaying", p_73833_1_), true);
    }

    public void setRecordPlaying(String p_110326_1_, boolean p_110326_2_) {
        this.recordPlaying = p_110326_1_;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = p_110326_2_;
    }

    public void displayTitle(String title, String subTitle, int fadeIn, int keep, int fadeOut) {
        if (title == null && subTitle == null && fadeIn < 0 && keep < 0 && fadeOut < 0) {
            this.title = "";
            this.subtitle = "";
            this.field_175195_w = 0;
        } else if (title != null) {
            this.title = title;
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
        } else if (subTitle != null) {
            this.subtitle = subTitle;
        } else {
            if (fadeIn >= 0) {
                this.field_175199_z = fadeIn;
            }
            if (keep >= 0) {
                this.field_175192_A = keep;
            }
            if (fadeOut >= 0) {
                this.field_175193_B = fadeOut;
            }
            if (this.field_175195_w > 0) {
                this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
            }
        }
    }

    public void setRecordPlaying(IChatComponent p_175188_1_, boolean p_175188_2_) {
        this.setRecordPlaying(p_175188_1_.getUnformattedText(), p_175188_2_);
    }

    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }

    public FontRenderer getFontRenderer() {
        return this.mc.fontRendererObj;
    }

    public GuiSpectator getSpectatorGui() {
        return this.spectatorGui;
    }

    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }

    public void func_181029_i() {
        this.overlayPlayerList.func_181030_a();
    }
}

