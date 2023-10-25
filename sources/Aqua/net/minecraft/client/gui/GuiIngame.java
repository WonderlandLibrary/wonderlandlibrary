package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import events.Event;
import events.listeners.EventPostRender2D;
import events.listeners.EventRender2D;
import events.listeners.EventRenderShaderESP;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.fr.lavache.anime.Easing;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSpectator;
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
import net.minecraft.scoreboard.Team;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.border.WorldBorder;
import net.optifine.CustomColors;
import windows.GamsterUI;

public class GuiIngame
extends Gui {
    private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    private final Random rand = new Random();
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    Animate anim = new Animate();
    private final GuiNewChat persistantChatGUI;
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
    private int titlesTimer;
    private String displayedTitle = "";
    private String displayedSubTitle = "";
    private int titleFadeIn;
    private int titleDisplayTime;
    private int titleFadeOut;
    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    private long lastSystemTime = 0L;
    private long healthUpdateCounter = 0L;

    public GuiIngame(Minecraft mcIn) {
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

    public void renderGameOverlay(float partialTicks) {
        ScoreObjective finalScoreobjective;
        ScoreObjective scoreobjective1;
        int i1;
        float f;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        EventRender2D event = new EventRender2D();
        if (!this.mc.isSingleplayer() && this.mc.getCurrentServerData().serverIP.equalsIgnoreCase("mc.gamster.org") && !Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Gamster")) {
            this.mc.displayGuiScreen((GuiScreen)new GamsterUI());
        }
        Aqua.INSTANCE.onEvent((Event)event);
        GlStateManager.enableBlend();
        if (Config.isVignetteEnabled()) {
            this.renderVignette(this.mc.thePlayer.getBrightness(partialTicks), scaledresolution);
        } else {
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        }
        ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock((Block)Blocks.pumpkin)) {
            this.renderPumpkinOverlay(scaledresolution);
        }
        if (!this.mc.thePlayer.isPotionActive(Potion.confusion) && (f = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks) > 0.0f) {
            this.renderPortal(f, scaledresolution);
        }
        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        } else {
            this.renderTooltip(scaledresolution, partialTicks);
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(icons);
        GlStateManager.enableBlend();
        if (this.showCrosshair()) {
            GlStateManager.tryBlendFuncSeparate((int)775, (int)769, (int)1, (int)0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
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
            int j1 = this.mc.thePlayer.getSleepTimer();
            float f1 = (float)j1 / 100.0f;
            if (f1 > 1.0f) {
                f1 = 1.0f - (float)(j1 - 100) / 10.0f;
            }
            int k = (int)(220.0f * f1) << 24 | 0x101020;
            GuiIngame.drawRect((int)0, (int)0, (int)i, (int)j, (int)k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int k1 = i / 2 - 91;
        if (this.mc.thePlayer.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, k1);
        } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, k1);
        }
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.renderSelectedItem(scaledresolution);
        } else if (this.mc.thePlayer.isSpectator()) {
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
            float f2 = (float)this.recordPlayingUpFor - partialTicks;
            int l1 = (int)(f2 * 255.0f / 20.0f);
            if (l1 > 255) {
                l1 = 255;
            }
            if (l1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j - 68), (float)0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
                int l = 0xFFFFFF;
                if (this.recordIsPlaying) {
                    l = MathHelper.hsvToRGB((float)(f2 / 50.0f), (float)0.7f, (float)0.6f) & 0xFFFFFF;
                }
                this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4, l + (l1 << 24 & 0xFF000000));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.titlesTimer > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            float f3 = (float)this.titlesTimer - partialTicks;
            int i2 = 255;
            if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
                float f4 = (float)(this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - f3;
                i2 = (int)(f4 * 255.0f / (float)this.titleFadeIn);
            }
            if (this.titlesTimer <= this.titleFadeOut) {
                i2 = (int)(f3 * 255.0f / (float)this.titleFadeOut);
            }
            if ((i2 = MathHelper.clamp_int((int)i2, (int)0, (int)255)) > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j / 2), (float)0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)4.0f, (float)4.0f, (float)4.0f);
                int j2 = i2 << 24 & 0xFF000000;
                this.getFontRenderer().drawString(this.displayedTitle, (float)(-this.getFontRenderer().getStringWidth(this.displayedTitle) / 2), -10.0f, 0xFFFFFF | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
                this.getFontRenderer().drawString(this.displayedSubTitle, (float)(-this.getFontRenderer().getStringWidth(this.displayedSubTitle) / 2), 5.0f, 0xFFFFFF | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());
        if (scoreplayerteam != null && (i1 = scoreplayerteam.getChatFormat().getColorIndex()) >= 0) {
            scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
        }
        ScoreObjective scoreObjective = scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective1 != null) {
            finalScoreobjective = scoreobjective1;
            this.renderScoreboardBlur(finalScoreobjective, scaledresolution);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)0.0f, (float)(j - 48), (float)0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat2(this.updateCounter);
        if (scoreobjective1 != null) {
            finalScoreobjective = scoreobjective1;
            this.renderScoreboard(finalScoreobjective, scaledresolution);
        }
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
        if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.getPlayerInfoMap().size() > 1 || scoreobjective1 != null)) {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
        } else {
            this.overlayPlayerList.updatePlayerList(false);
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableLighting();
        EventPostRender2D even1t = new EventPostRender2D();
        Aqua.INSTANCE.onEvent((Event)even1t);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)0.0f, (float)(j - 48), (float)0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        EventRenderShaderESP eventt = new EventRenderShaderESP();
        Aqua.INSTANCE.onEvent((Event)even1t);
        GlStateManager.enableAlpha();
    }

    protected void renderTooltip(ScaledResolution sr, float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            float f = zLevel;
            zLevel = -90.0f;
            this.anim.setEase(Easing.LINEAR).setMin(11.0f).setMax(40.0f).setSpeed(GuiNewChat.animatedChatOpen ? 45.0f : 100.0f).setReversed(!GuiNewChat.animatedChatOpen).update();
            if (!Aqua.moduleManager.getModuleByName("CustomHotbar").isToggled()) {
                this.drawTexturedModalRect(i - 91, (float)sr.getScaledHeight() - this.anim.getValue(), 0, 0, 182, 22);
                this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, (float)sr.getScaledHeight() - this.anim.getValue() - 1.0f, 0, 22, 24, 22);
            }
            zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            RenderHelper.enableGUIStandardItemLighting();
            if (!Aqua.moduleManager.getModuleByName("CustomHotbar").isToggled()) {
                for (int j = 0; j < 9; ++j) {
                    int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                    int l = (int)((float)sr.getScaledHeight() - this.anim.getValue() + 2.0f);
                    this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
                }
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        float f = this.mc.thePlayer.getHorseJumpPower();
        int i = 182;
        int j = (int)(f * 183.0f);
        int k = scaledRes.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(x, k, 0, 84, 182, 5);
        if (j > 0) {
            this.drawTexturedModalRect(x, k, 0, 89, j, 5);
        }
        this.mc.mcProfiler.endSection();
    }

    public void renderExpBar(ScaledResolution scaledRes, int x) {
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int i = this.mc.thePlayer.xpBarCap();
        if (i > 0) {
            int j = 182;
            int k = (int)(this.mc.thePlayer.experience * 183.0f);
            int l = scaledRes.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(x, (float)l - this.anim.getValue() + 22.0f, 0, 64, 182, 5);
            if (k > 0) {
                this.drawTexturedModalRect(x, (float)l - this.anim.getValue() + 22.0f, 0, 69, k, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (this.mc.thePlayer.experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            int k1 = 8453920;
            if (Config.isCustomColors()) {
                k1 = CustomColors.getExpBarTextColor((int)k1);
            }
            String s = "" + this.mc.thePlayer.experienceLevel;
            int l1 = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int i1 = scaledRes.getScaledHeight() - 31 - 4;
            boolean j1 = false;
            this.getFontRenderer().drawString(s, l1 + 1, (int)((float)i1 - this.anim.getValue() + 22.0f), 0);
            this.getFontRenderer().drawString(s, l1 - 1, (int)((float)i1 - this.anim.getValue() + 22.0f), 0);
            this.getFontRenderer().drawString(s, l1, (int)((float)(i1 + 1) - this.anim.getValue() + 22.0f), 0);
            this.getFontRenderer().drawString(s, l1, (int)((float)(i1 - 1) - this.anim.getValue() + 22.0f), 0);
            this.getFontRenderer().drawString(s, l1, (int)((float)i1 - this.anim.getValue() + 22.0f), k1);
            this.mc.mcProfiler.endSection();
        }
    }

    public void renderSelectedItem(ScaledResolution scaledRes) {
        this.mc.mcProfiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            int k;
            String s = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                s = EnumChatFormatting.ITALIC + s;
            }
            int i = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int j = scaledRes.getScaledHeight() - 59;
            if (!this.mc.playerController.shouldDrawHUD()) {
                j += 14;
            }
            if ((k = (int)((float)this.remainingHighlightTicks * 256.0f / 10.0f)) > 255) {
                k = 255;
            }
            if (k > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
                this.getFontRenderer().drawStringWithShadow(s, (float)i, (float)j, 0xFFFFFF + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }

    public void renderDemo(ScaledResolution scaledRes) {
        this.mc.mcProfiler.startSection("demo");
        String s = "";
        s = this.mc.theWorld.getTotalWorldTime() >= 120500L ? I18n.format((String)"demo.demoExpired", (Object[])new Object[0]) : I18n.format((String)"demo.remainingTime", (Object[])new Object[]{StringUtils.ticksToElapsedTime((int)((int)(120500L - this.mc.theWorld.getTotalWorldTime())))});
        int i = this.getFontRenderer().getStringWidth(s);
        this.getFontRenderer().drawStringWithShadow(s, (float)(scaledRes.getScaledWidth() - i - 10), 5.0f, 0xFFFFFF);
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

    public void renderStreamIndicator(ScaledResolution scaledRes) {
    }

    private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
        int color = Aqua.setmgr.getSetting("HUDColor").getColor();
        int secondColor = Aqua.setmgr.getSetting("ArraylistColor").getColor();
        int finalColor = Aqua.setmgr.getSetting("CustomScoreboardFade").isState() ? Arraylist.getGradientOffset((Color)new Color(color), (Color)new Color(secondColor), (double)15.0).getRGB() : color;
        Scoreboard scoreboard = objective.getScoreboard();
        Collection collection = scoreboard.getSortedScores(objective);
        ArrayList list = Lists.newArrayList((Iterable)Iterables.filter((Iterable)collection, (Predicate)new /* Unavailable Anonymous Inner Class!! */));
        collection = list.size() > 15 ? Lists.newArrayList((Iterable)Iterables.skip((Iterable)list, (int)(collection.size() - 15))) : list;
        int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());
        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName((Team)scoreplayerteam, (String)score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max((int)i, (int)this.getFontRenderer().getStringWidth(s));
        }
        int n = collection.size();
        this.getFontRenderer();
        int i1 = n * FontRenderer.FONT_HEIGHT;
        float posY = (float)Aqua.setmgr.getSetting("CustomScoreboardScorePosY").getCurrentNumber();
        int j1 = Aqua.moduleManager.getModuleByName("CustomScoreboard").isToggled() ? (int)((float)(scaledRes.getScaledHeight() / 2 + i1 / 3) + posY) : scaledRes.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = scaledRes.getScaledWidth() - i - k1;
        int j = 0;
        for (Score score1 : collection) {
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName((Team)scoreplayerteam1, (String)score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
            this.getFontRenderer();
            int k = j1 - j * FontRenderer.FONT_HEIGHT;
            int l = scaledRes.getScaledWidth() - k1 + 2;
            if (Aqua.setmgr.getSetting("CustomScoreboardShaders").isState()) {
                if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Glow")) {
                    Arraylist.drawGlowArray(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)k, (int)l, (int)(k + FontRenderer.FONT_HEIGHT), (int)finalColor);
                    }, (boolean)false);
                    this.getFontRenderer();
                    GuiIngame.drawRect((int)(l1 - 2), (int)k, (int)l, (int)(k + FontRenderer.FONT_HEIGHT), (int)0x50000000);
                } else if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
                    Shadow.drawGlow(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)k, (int)l, (int)(k + FontRenderer.FONT_HEIGHT), (int)Color.black.getRGB());
                    }, (boolean)false);
                    this.getFontRenderer();
                    GuiIngame.drawRect((int)(l1 - 2), (int)k, (int)l, (int)(k + FontRenderer.FONT_HEIGHT), (int)0x50000000);
                }
            }
            this.getFontRenderer().drawString(s1, l1, k, 0x20FFFFFF);
            this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 0x20FFFFFF);
            if (j != collection.size()) continue;
            String s3 = objective.getDisplayName();
            if (Aqua.setmgr.getSetting("CustomScoreboardShaders").isState()) {
                if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Glow")) {
                    Arraylist.drawGlowArray(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)(k - FontRenderer.FONT_HEIGHT - 1), (int)l, (int)(k - 1), (int)finalColor);
                    }, (boolean)false);
                    this.getFontRenderer();
                    GuiIngame.drawRect((int)(l1 - 2), (int)(k - FontRenderer.FONT_HEIGHT - 1), (int)l, (int)(k - 1), (int)0x50000000);
                } else if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
                    Shadow.drawGlow(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)(k - FontRenderer.FONT_HEIGHT - 1), (int)l, (int)(k - 1), (int)Color.black.getRGB());
                    }, (boolean)false);
                    this.getFontRenderer();
                    GuiIngame.drawRect((int)(l1 - 2), (int)(k - FontRenderer.FONT_HEIGHT - 1), (int)l, (int)(k - 1), (int)0x50000000);
                }
            }
            if (Aqua.setmgr.getSetting("CustomScoreboardShaders").isState()) {
                if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Glow")) {
                    Arraylist.drawGlowArray(() -> GuiIngame.drawRect((int)(l1 - 2), (int)(k - 1), (int)l, (int)k, (int)finalColor), (boolean)false);
                    GuiIngame.drawRect((int)(l1 - 2), (int)(k - 1), (int)l, (int)k, (int)0x50000000);
                } else if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
                    Shadow.drawGlow(() -> GuiIngame.drawRect((int)(l1 - 2), (int)(k - 1), (int)l, (int)k, (int)Color.black.getRGB()), (boolean)false);
                    GuiIngame.drawRect((int)(l1 - 2), (int)(k - 1), (int)l, (int)k, (int)0x50000000);
                }
            }
            FontRenderer fontRenderer = this.getFontRenderer();
            int n2 = l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2;
            this.getFontRenderer();
            fontRenderer.drawString(s3, n2, k - FontRenderer.FONT_HEIGHT, 0x20FFFFFF);
        }
    }

    private void renderScoreboardBlur(ScoreObjective objective, ScaledResolution scaledRes) {
        int color = Aqua.setmgr.getSetting("HUDColor").getColor();
        int secondColor = Aqua.setmgr.getSetting("ArraylistColor").getColor();
        int finalColor = Aqua.setmgr.getSetting("CustomScoreboardFade").isState() ? Arraylist.getGradientOffset((Color)new Color(color), (Color)new Color(secondColor), (double)15.0).getRGB() : color;
        Scoreboard scoreboard = objective.getScoreboard();
        Collection collection = scoreboard.getSortedScores(objective);
        ArrayList list = Lists.newArrayList((Iterable)Iterables.filter((Iterable)collection, (Predicate)new /* Unavailable Anonymous Inner Class!! */));
        collection = list.size() > 15 ? Lists.newArrayList((Iterable)Iterables.skip((Iterable)list, (int)(collection.size() - 15))) : list;
        int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());
        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName((Team)scoreplayerteam, (String)score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max((int)i, (int)this.getFontRenderer().getStringWidth(s));
        }
        int n = collection.size();
        this.getFontRenderer();
        int i1 = n * FontRenderer.FONT_HEIGHT;
        float posY = (float)Aqua.setmgr.getSetting("CustomScoreboardScorePosY").getCurrentNumber();
        int j1 = Aqua.moduleManager.getModuleByName("CustomScoreboard").isToggled() ? (int)((float)(scaledRes.getScaledHeight() / 2 + i1 / 3) + posY) : scaledRes.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = scaledRes.getScaledWidth() - i - k1;
        int j = 0;
        for (Score score1 : collection) {
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName((Team)scoreplayerteam1, (String)score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
            this.getFontRenderer();
            int k = j1 - j * FontRenderer.FONT_HEIGHT;
            int l = scaledRes.getScaledWidth() - k1 + 2;
            if (Aqua.setmgr.getSetting("CustomScoreboardShaders").isState()) {
                if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Glow")) {
                    Arraylist.drawGlowArray(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)k, (int)l, (int)(k + FontRenderer.FONT_HEIGHT), (int)finalColor);
                    }, (boolean)false);
                    Blur.drawBlurred(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)k, (int)l, (int)(k + FontRenderer.FONT_HEIGHT), (int)finalColor);
                    }, (boolean)false);
                } else if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
                    Shadow.drawGlow(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)k, (int)l, (int)(k + FontRenderer.FONT_HEIGHT), (int)Color.black.getRGB());
                    }, (boolean)false);
                    Blur.drawBlurred(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)k, (int)l, (int)(k + FontRenderer.FONT_HEIGHT), (int)Color.black.getRGB());
                    }, (boolean)false);
                }
            }
            this.getFontRenderer().drawString(s1, l1, k, 0x20FFFFFF);
            this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 0x20FFFFFF);
            if (j != collection.size()) continue;
            String s3 = objective.getDisplayName();
            if (Aqua.setmgr.getSetting("CustomScoreboardShaders").isState()) {
                if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Glow")) {
                    Arraylist.drawGlowArray(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)(k - FontRenderer.FONT_HEIGHT - 1), (int)l, (int)(k - 1), (int)finalColor);
                    }, (boolean)false);
                    Blur.drawBlurred(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)(k - FontRenderer.FONT_HEIGHT - 1), (int)l, (int)(k - 1), (int)finalColor);
                    }, (boolean)false);
                } else if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
                    Shadow.drawGlow(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)(k - FontRenderer.FONT_HEIGHT - 1), (int)l, (int)(k - 1), (int)Color.black.getRGB());
                    }, (boolean)false);
                    Blur.drawBlurred(() -> {
                        this.getFontRenderer();
                        GuiIngame.drawRect((int)(l1 - 2), (int)(k - FontRenderer.FONT_HEIGHT - 1), (int)l, (int)(k - 1), (int)Color.black.getRGB());
                    }, (boolean)false);
                }
            }
            if (Aqua.setmgr.getSetting("CustomScoreboardShaders").isState()) {
                if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Glow")) {
                    Arraylist.drawGlowArray(() -> GuiIngame.drawRect((int)(l1 - 2), (int)(k - 1), (int)l, (int)k, (int)finalColor), (boolean)false);
                    Blur.drawBlurred(() -> GuiIngame.drawRect((int)(l1 - 2), (int)(k - 1), (int)l, (int)k, (int)finalColor), (boolean)false);
                } else if (Aqua.setmgr.getSetting("CustomScoreboardMode").getCurrentMode().equalsIgnoreCase("Shadow")) {
                    Shadow.drawGlow(() -> GuiIngame.drawRect((int)(l1 - 2), (int)(k - 1), (int)l, (int)k, (int)Color.black.getRGB()), (boolean)false);
                    Blur.drawBlurred(() -> GuiIngame.drawRect((int)(l1 - 2), (int)(k - 1), (int)l, (int)k, (int)Color.black.getRGB()), (boolean)false);
                }
            }
            FontRenderer fontRenderer = this.getFontRenderer();
            int n2 = l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2;
            this.getFontRenderer();
            fontRenderer.drawString(s3, n2, k - FontRenderer.FONT_HEIGHT, 0x20FFFFFF);
        }
    }

    private void renderPlayerStats(ScaledResolution scaledRes) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            boolean flag;
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = MathHelper.ceiling_float_int((float)entityplayer.getHealth());
            boolean bl = flag = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;
            if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            } else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = i;
                this.lastPlayerHealth = i;
                this.lastSystemTime = Minecraft.getSystemTime();
            }
            this.playerHealth = i;
            int j = this.lastPlayerHealth;
            this.rand.setSeed((long)(this.updateCounter * 312871));
            boolean flag1 = false;
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            int l = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = scaledRes.getScaledWidth() / 2 - 91;
            int j1 = scaledRes.getScaledWidth() / 2 + 91;
            int k1 = scaledRes.getScaledHeight() - 39;
            float f = (float)iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((float)((f + f1) / 2.0f / 10.0f));
            int i2 = Math.max((int)(10 - (l1 - 2)), (int)3);
            int j2 = k1 - (l1 - 1) * i2 - 10;
            float f2 = f1;
            int k2 = entityplayer.getTotalArmorValue();
            int l2 = -1;
            if (entityplayer.isPotionActive(Potion.regeneration)) {
                l2 = this.updateCounter % MathHelper.ceiling_float_int((float)(f + 5.0f));
            }
            this.mc.mcProfiler.startSection("armor");
            for (int i3 = 0; i3 < 10; ++i3) {
                if (k2 <= 0) continue;
                int j3 = i1 + i3 * 8;
                if (i3 * 2 + 1 < k2) {
                    this.drawTexturedModalRect(j3, (float)j2 - this.anim.getValue() + 22.0f, 34, 9, 9, 9);
                }
                if (i3 * 2 + 1 == k2) {
                    this.drawTexturedModalRect(j3, (float)j2 - this.anim.getValue() + 22.0f, 25, 9, 9, 9);
                }
                if (i3 * 2 + 1 <= k2) continue;
                this.drawTexturedModalRect(j3, (float)j2 - this.anim.getValue() + 22.0f, 16, 9, 9, 9);
            }
            this.mc.mcProfiler.endStartSection("health");
            for (int i6 = MathHelper.ceiling_float_int((float)((f + f1) / 2.0f)) - 1; i6 >= 0; --i6) {
                int j6 = 16;
                if (entityplayer.isPotionActive(Potion.poison)) {
                    j6 += 36;
                } else if (entityplayer.isPotionActive(Potion.wither)) {
                    j6 += 72;
                }
                int k3 = 0;
                if (flag) {
                    k3 = 1;
                }
                int l3 = MathHelper.ceiling_float_int((float)((float)(i6 + 1) / 10.0f)) - 1;
                int i4 = i1 + i6 % 10 * 8;
                int j4 = k1 - l3 * i2;
                if (i <= 4) {
                    j4 += this.rand.nextInt(2);
                }
                if (i6 == l2) {
                    j4 -= 2;
                }
                int k4 = 0;
                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    k4 = 5;
                }
                this.drawTexturedModalRect(i4, (float)j4 - this.anim.getValue() + 22.0f, 16 + k3 * 9, 9 * k4, 9, 9);
                if (flag) {
                    if (i6 * 2 + 1 < j) {
                        this.drawTexturedModalRect(i4, (float)j4 - this.anim.getValue() + 22.0f, j6 + 54, 9 * k4, 9, 9);
                    }
                    if (i6 * 2 + 1 == j) {
                        this.drawTexturedModalRect(i4, (float)j4 - this.anim.getValue() + 22.0f, j6 + 63, 9 * k4, 9, 9);
                    }
                }
                if (f2 <= 0.0f) {
                    if (i6 * 2 + 1 < i) {
                        this.drawTexturedModalRect(i4, (float)j4 - this.anim.getValue() + 22.0f, j6 + 36, 9 * k4, 9, 9);
                    }
                    if (i6 * 2 + 1 != i) continue;
                    this.drawTexturedModalRect(i4, (float)j4 - this.anim.getValue() + 22.0f, j6 + 45, 9 * k4, 9, 9);
                    continue;
                }
                if (f2 == f1 && f1 % 2.0f == 1.0f) {
                    this.drawTexturedModalRect(i4, (float)j4 - this.anim.getValue() + 22.0f, j6 + 153, 9 * k4, 9, 9);
                } else {
                    this.drawTexturedModalRect(i4, (float)j4 - this.anim.getValue() + 22.0f, j6 + 144, 9 * k4, 9, 9);
                }
                f2 -= 2.0f;
            }
            Entity entity = entityplayer.ridingEntity;
            if (entity == null) {
                this.mc.mcProfiler.endStartSection("food");
                for (int k6 = 0; k6 < 10; ++k6) {
                    int j7 = k1;
                    int l7 = 16;
                    int k8 = 0;
                    if (entityplayer.isPotionActive(Potion.hunger)) {
                        l7 += 36;
                        k8 = 13;
                    }
                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (k * 3 + 1) == 0) {
                        j7 = k1 + (this.rand.nextInt(3) - 1);
                    }
                    int j9 = j1 - k6 * 8 - 9;
                    this.drawTexturedModalRect(j9, (float)j7 - this.anim.getValue() + 22.0f, 16 + k8 * 9, 27, 9, 9);
                    if (k6 * 2 + 1 < k) {
                        this.drawTexturedModalRect(j9, (float)j7 - this.anim.getValue() + 22.0f, l7 + 36, 27, 9, 9);
                    }
                    if (k6 * 2 + 1 != k) continue;
                    this.drawTexturedModalRect(j9, (float)j7 - this.anim.getValue() + 22.0f, l7 + 45, 27, 9, 9);
                }
            } else if (entity instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                int i7 = (int)Math.ceil((double)entitylivingbase.getHealth());
                float f3 = entitylivingbase.getMaxHealth();
                int j8 = (int)(f3 + 0.5f) / 2;
                if (j8 > 30) {
                    j8 = 30;
                }
                int i9 = k1;
                int k9 = 0;
                while (j8 > 0) {
                    int l4 = Math.min((int)j8, (int)10);
                    j8 -= l4;
                    for (int i5 = 0; i5 < l4; ++i5) {
                        int j5 = 52;
                        int k5 = 0;
                        int l5 = j1 - i5 * 8 - 9;
                        this.drawTexturedModalRect(l5, i9, 52 + k5 * 9, 9, 9, 9);
                        if (i5 * 2 + 1 + k9 < i7) {
                            this.drawTexturedModalRect(l5, (float)i9 - this.anim.getValue() + 22.0f, 88, 9, 9, 9);
                        }
                        if (i5 * 2 + 1 + k9 != i7) continue;
                        this.drawTexturedModalRect(l5, (float)i9 - this.anim.getValue() + 22.0f, 97, 9, 9, 9);
                    }
                    i9 -= 10;
                    k9 += 20;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.water)) {
                int l6 = this.mc.thePlayer.getAir();
                int k7 = MathHelper.ceiling_double_int((double)((double)(l6 - 2) * 10.0 / 300.0));
                int i8 = MathHelper.ceiling_double_int((double)((double)l6 * 10.0 / 300.0)) - k7;
                for (int l8 = 0; l8 < k7 + i8; ++l8) {
                    if (l8 < k7) {
                        this.drawTexturedModalRect(j1 - l8 * 8 - 9, (float)j2 - this.anim.getValue() + 22.0f, 16, 18, 9, 9);
                        continue;
                    }
                    this.drawTexturedModalRect(j1 - l8 * 8 - 9, (float)j2 - this.anim.getValue() + 22.0f, 25, 18, 9, 9);
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
            int i = scaledresolution.getScaledWidth();
            int j = 182;
            int k = i / 2 - 91;
            int l = (int)(BossStatus.healthScale * 183.0f);
            int i1 = 12;
            this.drawTexturedModalRect(k, 12, 0, 74, 182, 5);
            this.drawTexturedModalRect(k, 12, 0, 74, 182, 5);
            if (l > 0) {
                this.drawTexturedModalRect(k, 12, 0, 79, l, 5);
            }
            String s = BossStatus.bossName;
            this.getFontRenderer().drawStringWithShadow(s, (float)(i / 2 - this.getFontRenderer().getStringWidth(s) / 2), 2.0f, 0xFFFFFF);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }

    private void renderPumpkinOverlay(ScaledResolution scaledRes) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, (double)scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private void renderVignette(float lightLevel, ScaledResolution scaledRes) {
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        } else {
            lightLevel = 1.0f - lightLevel;
            lightLevel = MathHelper.clamp_float((float)lightLevel, (float)0.0f, (float)1.0f);
            WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
            float f = (float)worldborder.getClosestDistance((Entity)this.mc.thePlayer);
            double d0 = Math.min((double)(worldborder.getResizeSpeed() * (double)worldborder.getWarningTime() * 1000.0), (double)Math.abs((double)(worldborder.getTargetSize() - worldborder.getDiameter())));
            double d1 = Math.max((double)worldborder.getWarningDistance(), (double)d0);
            f = (double)f < d1 ? 1.0f - (float)((double)f / d1) : 0.0f;
            this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(lightLevel - this.prevVignetteBrightness) * 0.01);
            GlStateManager.disableDepth();
            GlStateManager.depthMask((boolean)false);
            GlStateManager.tryBlendFuncSeparate((int)0, (int)769, (int)1, (int)0);
            if (f > 0.0f) {
                GlStateManager.color((float)0.0f, (float)f, (float)f, (float)1.0f);
            } else {
                GlStateManager.color((float)this.prevVignetteBrightness, (float)this.prevVignetteBrightness, (float)this.prevVignetteBrightness, (float)1.0f);
            }
            this.mc.getTextureManager().bindTexture(vignetteTexPath);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0, (double)scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
            worldrenderer.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
            worldrenderer.pos((double)scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
            worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        }
    }

    private void renderPortal(float timeInPortal, ScaledResolution scaledRes) {
        if (timeInPortal < 1.0f) {
            timeInPortal *= timeInPortal;
            timeInPortal *= timeInPortal;
            timeInPortal = timeInPortal * 0.8f + 0.2f;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)timeInPortal);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, (double)scaledRes.getScaledHeight(), -90.0).tex((double)f, (double)f3).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0).tex((double)f2, (double)f3).endVertex();
        worldrenderer.pos((double)scaledRes.getScaledWidth(), 0.0, -90.0).tex((double)f2, (double)f1).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex((double)f, (double)f1).endVertex();
        tessellator.draw();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player) {
        ItemStack itemstack = player.inventory.mainInventory[index];
        if (itemstack != null) {
            float f = (float)itemstack.animationsToGo - partialTicks;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                float f1 = 1.0f + f / 5.0f;
                GlStateManager.translate((float)(xPos + 8), (float)(yPos + 12), (float)0.0f);
                GlStateManager.scale((float)(1.0f / f1), (float)((f1 + 1.0f) / 2.0f), (float)1.0f);
                GlStateManager.translate((float)(-(xPos + 8)), (float)(-(yPos + 12)), (float)0.0f);
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
            ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();
            if (itemstack == null) {
                this.remainingHighlightTicks = 0;
            } else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual((ItemStack)itemstack, (ItemStack)this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            } else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = itemstack;
        }
    }

    public void setRecordPlayingMessage(String recordName) {
        this.setRecordPlaying(I18n.format((String)"record.nowPlaying", (Object[])new Object[]{recordName}), true);
    }

    public void setRecordPlaying(String message, boolean isPlaying) {
        this.recordPlaying = message;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = isPlaying;
    }

    public void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut) {
        if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0) {
            this.displayedTitle = "";
            this.displayedSubTitle = "";
            this.titlesTimer = 0;
        } else if (title != null) {
            this.displayedTitle = title;
            this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
        } else if (subTitle != null) {
            this.displayedSubTitle = subTitle;
        } else {
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

    public void setRecordPlaying(IChatComponent component, boolean isPlaying) {
        this.setRecordPlaying(component.getUnformattedText(), isPlaying);
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

    public void resetPlayersOverlayFooterHeader() {
        this.overlayPlayerList.resetFooterHeader();
    }
}
