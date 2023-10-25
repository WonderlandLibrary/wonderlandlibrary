/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.GuiSubtitleOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.chat.NormalChatListener;
import net.minecraft.client.gui.chat.OverlayChatListener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.border.WorldBorder;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomItems;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.TextureAnimations;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.feature.impl.hud.ArrayList;
import org.celestial.client.feature.impl.hud.HUD;
import org.celestial.client.feature.impl.visuals.Crosshair;
import org.celestial.client.feature.impl.visuals.NoRender;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.components.draggable.DraggableModule;
import org.celestial.client.ui.components.draggable.impl.ScoreboardComponent;
import org.celestial.client.ui.notification.NotificationManager;

public class GuiIngame
extends Gui {
    private static final ResourceLocation VIGNETTE_TEX_PATH = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation PUMPKIN_BLUR_TEX_PATH = new ResourceLocation("textures/misc/pumpkinblur.png");
    private final Random rand = new Random();
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    public static ScoreObjective objective1;
    public static ScaledResolution scaledRes1;
    private final GuiNewChat persistantChatGUI;
    private int updateCounter;
    private String recordPlaying = "";
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    public float prevVignetteBrightness = 1.0f;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack = ItemStack.field_190927_a;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSubtitleOverlay overlaySubtitle;
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private final GuiBossOverlay overlayBoss;
    private int titlesTimer;
    private String displayedTitle = "";
    private String displayedSubTitle = "";
    private int titleFadeIn;
    private int titleDisplayTime;
    private int titleFadeOut;
    private int playerHealth;
    private int lastPlayerHealth;
    private long lastSystemTime;
    private long healthUpdateCounter;
    private final Map<ChatType, List<IChatListener>> field_191743_I = Maps.newHashMap();

    public GuiIngame(Minecraft mcIn) {
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.overlayBoss = new GuiBossOverlay(mcIn);
        this.overlaySubtitle = new GuiSubtitleOverlay(mcIn);
        for (ChatType chattype : ChatType.values()) {
            this.field_191743_I.put(chattype, Lists.newArrayList());
        }
        NarratorChatListener ichatlistener = NarratorChatListener.field_193643_a;
        this.field_191743_I.get((Object)ChatType.CHAT).add(new NormalChatListener(mcIn));
        this.field_191743_I.get((Object)ChatType.CHAT).add(ichatlistener);
        this.field_191743_I.get((Object)ChatType.SYSTEM).add(new NormalChatListener(mcIn));
        this.field_191743_I.get((Object)ChatType.SYSTEM).add(ichatlistener);
        this.field_191743_I.get((Object)ChatType.GAME_INFO).add(new OverlayChatListener(mcIn));
        this.setDefaultTitlesTimes();
    }

    public void setDefaultTitlesTimes() {
        this.titleFadeIn = 10;
        this.titleDisplayTime = 70;
        this.titleFadeOut = 20;
    }

    public void renderGameOverlay(float partialTicks) {
        ScoreObjective scoreobjective1;
        int i1;
        float f;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        if (Celestial.instance.featureManager.getFeatureByClass(ArrayList.class).getState()) {
            ArrayList.onRenderModuleList(scaledresolution);
        }
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = this.getFontRenderer();
        GlStateManager.enableBlend();
        if (Config.isVignetteEnabled()) {
            this.renderVignette(this.mc.player.getBrightness(), scaledresolution);
        } else {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        ItemStack itemstack = this.mc.player.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            this.renderPumpkinOverlay(scaledresolution);
        }
        if (!this.mc.player.isPotionActive(MobEffects.NAUSEA) && (f = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * partialTicks) > 0.0f) {
            this.renderPortal(f, scaledresolution);
        }
        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        } else {
            this.renderHotbar(scaledresolution, partialTicks);
        }
        NotificationManager.renderNotification(scaledresolution);
        EventRender2D event = new EventRender2D(scaledresolution);
        EventManager.call(event);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ICONS);
        GlStateManager.enableBlend();
        this.renderAttackIndicator(partialTicks, scaledresolution);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        this.mc.mcProfiler.startSection("bossHealth");
        this.overlayBoss.renderBossHealth();
        this.mc.mcProfiler.endSection();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ICONS);
        if (this.mc.playerController.shouldDrawHUD()) {
            this.renderPlayerStats(scaledresolution);
        }
        this.renderMountHealth(scaledresolution);
        GlStateManager.disableBlend();
        if (this.mc.player.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int j1 = this.mc.player.getSleepTimer();
            float f1 = (float)j1 / 100.0f;
            if (f1 > 1.0f) {
                f1 = 1.0f - (float)(j1 - 100) / 10.0f;
            }
            int k = (int)(220.0f * f1) << 24 | 0x101020;
            GuiIngame.drawRect(0, 0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int k1 = i / 2 - 91;
        if (this.mc.player.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, k1);
        } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, k1);
        }
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.renderSelectedItem(scaledresolution);
        } else if (this.mc.player.isSpectator()) {
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
                GlStateManager.translate(i / 2, j - 68, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                int l = 0xFFFFFF;
                if (this.recordIsPlaying) {
                    l = MathHelper.hsvToRGB(f2 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF;
                }
                fontrenderer.drawString(this.recordPlaying, -fontrenderer.getStringWidth(this.recordPlaying) / 2, -4.0f, l + (l1 << 24 & 0xFF000000));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        this.overlaySubtitle.renderSubtitles(scaledresolution);
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
            if ((i2 = MathHelper.clamp(i2, 0, 255)) > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(i / 2, j / 2, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                int j2 = i2 << 24 & 0xFF000000;
                fontrenderer.drawString(this.displayedTitle, -fontrenderer.getStringWidth(this.displayedTitle) / 2, -10.0f, 0xFFFFFF | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                fontrenderer.drawString(this.displayedSubTitle, -fontrenderer.getStringWidth(this.displayedSubTitle) / 2, 5.0f, 0xFFFFFF | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        Scoreboard scoreboard = this.mc.world.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.player.getName());
        if (scoreplayerteam != null && (i1 = scoreplayerteam.getChatFormat().getColorIndex()) >= 0) {
            scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
        }
        objective1 = scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        scaledRes1 = scaledresolution;
        if (!(scoreobjective1 == null || Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.noScoreBoard.getCurrentValue())) {
            for (DraggableModule draggableModule : Celestial.instance.draggableManager.getMods()) {
                if (!(draggableModule instanceof ScoreboardComponent)) continue;
                draggableModule.draw();
            }
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, j - 48, 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
        if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || this.mc.player.connection.getPlayerInfoMap().size() > 1 || scoreobjective1 != null)) {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
        } else {
            this.overlayPlayerList.updatePlayerList(false);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }

    private void renderAttackIndicator(float p_184045_1_, ScaledResolution p_184045_2_) {
        GameSettings gamesettings = this.mc.gameSettings;
        if (gamesettings.thirdPersonView == 0) {
            if (this.mc.playerController.isSpectator() && this.mc.pointedEntity == null) {
                RayTraceResult raytraceresult = this.mc.objectMouseOver;
                if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
                    return;
                }
                BlockPos blockpos = raytraceresult.getBlockPos();
                IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
                if (!ReflectorForge.blockHasTileEntity(iblockstate) || !(this.mc.world.getTileEntity(blockpos) instanceof IInventory)) {
                    return;
                }
            }
            int l = p_184045_2_.getScaledWidth();
            int i1 = p_184045_2_.getScaledHeight();
            if (gamesettings.showDebugInfo && !gamesettings.hideGUI && !this.mc.player.hasReducedDebug() && !gamesettings.reducedDebugInfo) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(l / 2, i1 / 2, this.zLevel);
                Entity entity = this.mc.getRenderViewEntity();
                GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_184045_1_, -1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * p_184045_1_, 0.0f, 1.0f, 0.0f);
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                OpenGlHelper.renderDirections(10);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.enableAlpha();
                if (!Celestial.instance.featureManager.getFeatureByClass(Crosshair.class).getState()) {
                    this.drawTexturedModalRect(l / 2 - 7, i1 / 2 - 7, 0, 0, 16, 16);
                }
                if (this.mc.gameSettings.attackIndicator == 1) {
                    float f = this.mc.player.getCooledAttackStrength(0.0f);
                    boolean flag = false;
                    if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityLivingBase && f >= 1.0f) {
                        flag = this.mc.player.getCooldownPeriod() > 5.0f;
                        flag &= ((EntityLivingBase)this.mc.pointedEntity).isEntityAlive();
                    }
                    int i = i1 / 2 - 7 + 16;
                    int j = l / 2 - 8;
                    if (flag) {
                        this.drawTexturedModalRect(j, i, 68, 94, 16, 16);
                    } else if (f < 1.0f) {
                        int k = (int)(f * 17.0f);
                        this.drawTexturedModalRect(j, i, 36, 94, 16, 4);
                        this.drawTexturedModalRect(j, i, 52, 94, k, 4);
                    }
                }
            }
        }
    }

    protected void renderPotionEffects(ScaledResolution resolution) {
        Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();
        if (!collection.isEmpty()) {
            this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
            GlStateManager.enableBlend();
            int i = 0;
            int j = 0;
            Iterator<PotionEffect> iterator = Ordering.natural().reverse().sortedCopy(collection).iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    return;
                }
                PotionEffect potioneffect = iterator.next();
                Potion potion = potioneffect.getPotion();
                boolean flag = potion.hasStatusIcon();
                if (Reflector.ForgePotion_shouldRenderHUD.exists()) {
                    if (!Reflector.callBoolean(potion, Reflector.ForgePotion_shouldRenderHUD, potioneffect)) continue;
                    this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                    flag = true;
                }
                if (!flag || !potioneffect.doesShowParticles()) continue;
                int k = resolution.getScaledWidth();
                int l = 1;
                if (this.mc.isDemo()) {
                    l += 15;
                }
                int i1 = potion.getStatusIconIndex();
                if (potion.isBeneficial()) {
                    k -= 25 * ++i;
                } else {
                    k -= 25 * ++j;
                    l += 26;
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                float f = 1.0f;
                if (potioneffect.getIsAmbient()) {
                    this.drawTexturedModalRect(k, l, 165, 166, 24, 24);
                } else {
                    this.drawTexturedModalRect(k, l, 141, 166, 24, 24);
                    if (potioneffect.getDuration() <= 200) {
                        int j1 = 10 - potioneffect.getDuration() / 20;
                        f = MathHelper.clamp((float)potioneffect.getDuration() / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + MathHelper.cos((float)potioneffect.getDuration() * (float)Math.PI / 5.0f) * MathHelper.clamp((float)j1 / 10.0f * 0.25f, 0.0f, 0.25f);
                    }
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, f);
                if (Reflector.ForgePotion_renderHUDEffect.exists()) {
                    if (potion.hasStatusIcon()) {
                        this.drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                    }
                    Reflector.call(potion, Reflector.ForgePotion_renderHUDEffect, k, l, potioneffect, this.mc, Float.valueOf(f));
                    continue;
                }
                this.drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
            }
        }
    }

    protected void renderHotbar(ScaledResolution sr, float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            float f1;
            int l1;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            ItemStack itemstack = entityplayer.getHeldItemOffhand();
            EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
            int i = sr.getScaledWidth() / 2;
            float f = this.zLevel;
            int j = 182;
            int k = 91;
            this.zLevel = -90.0f;
            if (Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() && HUD.rustHUD.getCurrentValue()) {
                RectHelper.drawRectBetter(i - 91, sr.getScaledHeight() - 38, 182.0, 25.0, new Color(95, 95, 95, 100).getRGB());
                RectHelper.drawRectBetter(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 38, 21.0, 25.0, new Color(55, 155, 215, 175).getRGB());
            } else {
                this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
                this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            }
            if (!(itemstack.isEmpty() || HUD.rustHUD.getCurrentValue() && Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState())) {
                CustomItems.setRenderOffHand(true);
                l1 = sr.getScaledHeight() - 16 - 3;
                if (enumhandside == EnumHandSide.LEFT) {
                    this.renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
                } else {
                    this.renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
                }
                CustomItems.setRenderOffHand(false);
            }
            this.zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            CustomItems.setRenderOffHand(false);
            for (int l = 0; l < 9; ++l) {
                int i1 = i - (Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() && HUD.rustHUD.getCurrentValue() ? 92 : 90) + l * 20 + 2;
                int j1 = sr.getScaledHeight() - 16 - (Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() && HUD.rustHUD.getCurrentValue() ? 10 : 3);
                this.renderHotbarItem(i1, j1, partialTicks, entityplayer, entityplayer.inventory.mainInventory.get(l));
            }
            if (!(itemstack.isEmpty() || HUD.rustHUD.getCurrentValue() && Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState())) {
                CustomItems.setRenderOffHand(true);
                l1 = sr.getScaledHeight() - 16 - 3;
                if (enumhandside == EnumHandSide.LEFT) {
                    this.renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
                } else {
                    this.renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
                }
                CustomItems.setRenderOffHand(false);
            }
            if (this.mc.gameSettings.attackIndicator == 2 && (f1 = this.mc.player.getCooledAttackStrength(0.0f)) < 1.0f) {
                int i2 = sr.getScaledHeight() - 20;
                int j2 = i + 91 + 6;
                if (enumhandside == EnumHandSide.RIGHT) {
                    j2 = i - 91 - 22;
                }
                this.mc.getTextureManager().bindTexture(Gui.ICONS);
                int k1 = (int)(f1 * 19.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
                this.drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
            }
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.ICONS);
        float f = this.mc.player.getHorseJumpPower();
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
        if (Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() && HUD.rustHUD.getCurrentValue()) {
            return;
        }
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.ICONS);
        int i = this.mc.player.xpBarCap();
        if (i > 0) {
            int j = 182;
            int k = (int)(this.mc.player.experience * 183.0f);
            int l = scaledRes.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(x, l, 0, 64, 182, 5);
            if (k > 0) {
                this.drawTexturedModalRect(x, l, 0, 69, k, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (this.mc.player.experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            int j1 = 8453920;
            if (Config.isCustomColors()) {
                j1 = CustomColors.getExpBarTextColor(j1);
            }
            String s = "" + this.mc.player.experienceLevel;
            int k1 = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int i1 = scaledRes.getScaledHeight() - 31 - 4;
            this.getFontRenderer().drawString(s, k1 + 1, i1, 0);
            this.getFontRenderer().drawString(s, k1 - 1, i1, 0);
            this.getFontRenderer().drawString(s, k1, i1 + 1, 0);
            this.getFontRenderer().drawString(s, k1, i1 - 1, 0);
            this.getFontRenderer().drawString(s, k1, i1, j1);
            this.mc.mcProfiler.endSection();
        }
    }

    public void renderSelectedItem(ScaledResolution scaledRes) {
        this.mc.mcProfiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && !this.highlightingItemStack.isEmpty()) {
            int k;
            String s = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                s = (Object)((Object)TextFormatting.ITALIC) + s;
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
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                this.getFontRenderer().drawStringWithShadow(s, i, j, 0xFFFFFF + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }

    public void renderDemo(ScaledResolution scaledRes) {
        this.mc.mcProfiler.startSection("demo");
        String s = this.mc.world.getTotalWorldTime() >= 120500L ? I18n.format("demo.demoExpired", new Object[0]) : I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.world.getTotalWorldTime())));
        int i = this.getFontRenderer().getStringWidth(s);
        this.getFontRenderer().drawStringWithShadow(s, scaledRes.getScaledWidth() - i - 10, 5.0f, 0xFFFFFF);
        this.mc.mcProfiler.endSection();
    }

    private void renderPlayerStats(ScaledResolution scaledRes) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            boolean flag;
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = MathHelper.ceil(entityplayer.getHealth());
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
            this.rand.setSeed(this.updateCounter * 312871);
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            int l = scaledRes.getScaledWidth() / 2 - 91;
            int i1 = scaledRes.getScaledWidth() / 2 + 91;
            int j1 = scaledRes.getScaledHeight() - 39;
            float f = (float)iattributeinstance.getAttributeValue();
            int k1 = MathHelper.ceil(entityplayer.getAbsorptionAmount());
            int l1 = MathHelper.ceil((f + (float)k1) / 2.0f / 10.0f);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = j1 - (l1 - 1) * i2 - 10;
            int k2 = j1 - 10;
            int l2 = k1;
            int i3 = entityplayer.getTotalArmorValue();
            int j3 = -1;
            ScaledResolution sr = new ScaledResolution(this.mc);
            float width = sr.getScaledWidth() - 155 - this.mc.helvetica.getStringWidth("zalupa");
            float height = sr.getScaledHeight() - this.mc.helvetica.getFontHeight() - 110;
            if (entityplayer.isPotionActive(MobEffects.REGENERATION)) {
                j3 = this.updateCounter % MathHelper.ceil(f + 5.0f);
            }
            this.mc.mcProfiler.startSection("armor");
            if (!Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() || !HUD.rustHUD.getCurrentValue()) {
                for (int k3 = 0; k3 < 10; ++k3) {
                    if (i3 <= 0) continue;
                    int l3 = l + k3 * 8;
                    if (k3 * 2 + 1 < i3) {
                        this.drawTexturedModalRect(l3, j2, 34, 9, 9, 9);
                    }
                    if (k3 * 2 + 1 == i3) {
                        this.drawTexturedModalRect(l3, j2, 25, 9, 9, 9);
                    }
                    if (k3 * 2 + 1 <= i3) continue;
                    this.drawTexturedModalRect(l3, j2, 16, 9, 9, 9);
                }
            }
            this.mc.mcProfiler.endStartSection("health");
            if (Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() && HUD.rustHUD.getCurrentValue()) {
                float healthPercentage = this.mc.player.getHealth() / this.mc.player.getMaxHealth() * 100.0f;
                String healthPercentageStr = "" + (int)healthPercentage;
                RectHelper.drawRectBetter(width, height, 122.0, 20.0, new Color(75, 75, 75, 175).getRGB());
                RenderHelper.drawImage(new ResourceLocation("celestial/rusthud/health_tag_icon.png"), width + 3.5f, height + 3.5f, 13.0f, 13.0f, new Color(150, 150, 150, 255));
                RectHelper.drawRectBetter(width + 20.0f, height + 2.0f, healthPercentage, 16.0, new Color(135, 185, 75, 215).getRGB());
                this.mc.helvetica.drawString(healthPercentageStr, width + 24.0f, height + 9.0f, -1);
                float armorPercentage = this.mc.player.getTotalArmorValue() * 5;
                String armorPercentageStr = "" + (int)armorPercentage;
                RectHelper.drawRectBetter(width, height + 21.0f, 122.0, 20.0, new Color(75, 75, 75, 175).getRGB());
                RenderHelper.drawImage(new ResourceLocation("celestial/rusthud/armor_icon.png"), width + 3.5f, height + 24.5f, 13.0f, 13.0f, new Color(150, 150, 150, 255));
                for (ItemStack armorList : this.mc.player.getArmorInventoryList()) {
                    if (armorList.isEmpty()) continue;
                    RectHelper.drawRectBetter(width + 20.0f, height + 23.0f, armorPercentage, 16.0, new Color(55, 155, 215, 215).getRGB());
                }
                this.mc.helvetica.drawString(armorPercentageStr, width + 24.0f, height + 30.0f, -1);
                float foodPercentage = this.mc.player.getFoodStats().getFoodLevel() * 5;
                String foodPercentageStr = "" + (int)foodPercentage;
                RectHelper.drawRectBetter(width, height + 42.0f, 122.0, 20.0, new Color(75, 75, 75, 175).getRGB());
                RenderHelper.drawImage(new ResourceLocation("celestial/rusthud/food_icon.png"), width + 3.5f, height + 45.5f, 13.0f, 13.0f, new Color(150, 150, 150, 255));
                RectHelper.drawRectBetter(width + 20.0f, height + 44.0f, foodPercentage, 16.0, new Color(215, 118, 7, 215).getRGB());
                this.mc.helvetica.drawString(foodPercentageStr, width + 24.0f, height + 51.0f, -1);
            } else {
                for (int j5 = MathHelper.ceil((f + (float)k1) / 2.0f) - 1; j5 >= 0; --j5) {
                    int k5 = 16;
                    if (entityplayer.isPotionActive(MobEffects.POISON)) {
                        k5 += 36;
                    } else if (!(!entityplayer.isPotionActive(MobEffects.WITHER) || Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.noWitherHearts.getCurrentValue())) {
                        k5 += 72;
                    }
                    int i4 = 0;
                    if (flag) {
                        i4 = 1;
                    }
                    int j4 = MathHelper.ceil((float)(j5 + 1) / 10.0f) - 1;
                    int k4 = l + j5 % 10 * 8;
                    int l4 = j1 - j4 * i2;
                    if (i <= 4) {
                        l4 += this.rand.nextInt(2);
                    }
                    if (l2 <= 0 && j5 == j3) {
                        l4 -= 2;
                    }
                    int i5 = 0;
                    if (entityplayer.world.getWorldInfo().isHardcoreModeEnabled()) {
                        i5 = 5;
                    }
                    this.drawTexturedModalRect(k4, l4, 16 + i4 * 9, 9 * i5, 9, 9);
                    if (flag) {
                        if (j5 * 2 + 1 < j) {
                            this.drawTexturedModalRect(k4, l4, k5 + 54, 9 * i5, 9, 9);
                        }
                        if (j5 * 2 + 1 == j) {
                            this.drawTexturedModalRect(k4, l4, k5 + 63, 9 * i5, 9, 9);
                        }
                    }
                    if (l2 > 0) {
                        if (l2 == k1 && k1 % 2 == 1) {
                            this.drawTexturedModalRect(k4, l4, k5 + 153, 9 * i5, 9, 9);
                            --l2;
                            continue;
                        }
                        this.drawTexturedModalRect(k4, l4, k5 + 144, 9 * i5, 9, 9);
                        l2 -= 2;
                        continue;
                    }
                    if (j5 * 2 + 1 < i) {
                        this.drawTexturedModalRect(k4, l4, k5 + 36, 9 * i5, 9, 9);
                    }
                    if (j5 * 2 + 1 != i) continue;
                    this.drawTexturedModalRect(k4, l4, k5 + 45, 9 * i5, 9, 9);
                }
            }
            Entity entity = entityplayer.getRidingEntity();
            if (entity == null || !(entity instanceof EntityLivingBase)) {
                this.mc.mcProfiler.endStartSection("food");
                if (!Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() || !HUD.rustHUD.getCurrentValue()) {
                    for (int l5 = 0; l5 < 10; ++l5) {
                        int j6 = j1;
                        int l6 = 16;
                        int j7 = 0;
                        if (entityplayer.isPotionActive(MobEffects.HUNGER)) {
                            l6 += 36;
                            j7 = 13;
                        }
                        if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (k * 3 + 1) == 0) {
                            j6 = j1 + (this.rand.nextInt(3) - 1);
                        }
                        int l7 = i1 - l5 * 8 - 9;
                        this.drawTexturedModalRect(l7, j6, 16 + j7 * 9, 27, 9, 9);
                        if (l5 * 2 + 1 < k) {
                            this.drawTexturedModalRect(l7, j6, l6 + 36, 27, 9, 9);
                        }
                        if (l5 * 2 + 1 != k) continue;
                        this.drawTexturedModalRect(l7, j6, l6 + 45, 27, 9, 9);
                    }
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.WATER)) {
                int i6 = this.mc.player.getAir();
                int k6 = MathHelper.ceil((double)(i6 - 2) * 10.0 / 300.0);
                int i7 = MathHelper.ceil((double)i6 * 10.0 / 300.0) - k6;
                for (int k7 = 0; k7 < k6 + i7; ++k7) {
                    if (k7 < k6) {
                        this.drawTexturedModalRect(i1 - k7 * 8 - 9, k2, 16, 18, 9, 9);
                        continue;
                    }
                    this.drawTexturedModalRect(i1 - k7 * 8 - 9, k2, 25, 18, 9, 9);
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }

    private void renderMountHealth(ScaledResolution p_184047_1_) {
        EntityPlayer entityplayer;
        Entity entity;
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer && (entity = (entityplayer = (EntityPlayer)this.mc.getRenderViewEntity()).getRidingEntity()) instanceof EntityLivingBase) {
            this.mc.mcProfiler.endStartSection("mountHealth");
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            int i = (int)Math.ceil(entitylivingbase.getHealth());
            float f = entitylivingbase.getMaxHealth();
            int j = (int)(f + 0.5f) / 2;
            if (j > 30) {
                j = 30;
            }
            int k = p_184047_1_.getScaledHeight() - 39;
            int l = p_184047_1_.getScaledWidth() / 2 + 91;
            int i1 = k;
            int j1 = 0;
            boolean flag = false;
            while (j > 0) {
                int k1 = Math.min(j, 10);
                j -= k1;
                for (int l1 = 0; l1 < k1; ++l1) {
                    int i2 = 52;
                    int j2 = 0;
                    int k2 = l - l1 * 8 - 9;
                    this.drawTexturedModalRect(k2, i1, 52 + j2 * 9, 9, 9, 9);
                    if (l1 * 2 + 1 + j1 < i) {
                        this.drawTexturedModalRect(k2, i1, 88, 9, 9, 9);
                    }
                    if (l1 * 2 + 1 + j1 != i) continue;
                    this.drawTexturedModalRect(k2, i1, 97, 9, 9, 9);
                }
                i1 -= 10;
                j1 += 20;
            }
        }
    }

    private void renderPumpkinOverlay(ScaledResolution scaledRes) {
        if (Celestial.instance.featureManager.getFeatureByClass(NoRender.class).getState() && NoRender.pumpkin.getCurrentValue()) {
            return;
        }
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(PUMPKIN_BLUR_TEX_PATH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        bufferbuilder.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderVignette(float lightLevel, ScaledResolution scaledRes) {
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        } else {
            lightLevel = 1.0f - lightLevel;
            lightLevel = MathHelper.clamp(lightLevel, 0.0f, 1.0f);
            WorldBorder worldborder = this.mc.world.getWorldBorder();
            float f = (float)worldborder.getClosestDistance(this.mc.player);
            double d0 = Math.min(worldborder.getResizeSpeed() * (double)worldborder.getWarningTime() * 1000.0, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
            double d1 = Math.max((double)worldborder.getWarningDistance(), d0);
            f = (double)f < d1 ? 1.0f - (float)((double)f / d1) : 0.0f;
            this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(lightLevel - this.prevVignetteBrightness) * 0.01);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (f > 0.0f) {
                GlStateManager.color(0.0f, f, f, 1.0f);
            } else {
                GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
            }
            this.mc.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
            bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
            bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
            bufferbuilder.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
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
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, timeInPortal);
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(f, f3).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(f2, f3).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(f2, f1).endVertex();
        bufferbuilder.pos(0.0, 0.0, -90.0).tex(f, f1).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderHotbarItem(int p_184044_1_, int p_184044_2_, float p_184044_3_, EntityPlayer player, ItemStack stack) {
        if (!stack.isEmpty()) {
            float f = (float)stack.func_190921_D() - p_184044_3_;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                float f1 = 1.0f + f / 5.0f;
                GlStateManager.translate(p_184044_1_ + 8, p_184044_2_ + 12, 0.0f);
                GlStateManager.scale(1.0f / f1, (f1 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate(-(p_184044_1_ + 8), -(p_184044_2_ + 12), 0.0f);
            }
            if (Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() && HUD.rustHUD.getCurrentValue()) {
                this.itemRenderer.renderItemAndEffectIntoGUI(player, stack, p_184044_1_, p_184044_2_ - 7);
            } else {
                this.itemRenderer.renderItemAndEffectIntoGUI(player, stack, p_184044_1_, p_184044_2_);
            }
            if (f > 0.0f) {
                GlStateManager.popMatrix();
            }
            if (Celestial.instance.featureManager.getFeatureByClass(HUD.class).getState() && HUD.rustHUD.getCurrentValue()) {
                this.itemRenderer.renderItemOverlays(this.mc.helvetica, stack, p_184044_1_, p_184044_2_ - 4);
            } else {
                this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, stack, p_184044_1_, p_184044_2_);
            }
        }
    }

    public void updateTick() {
        if (this.mc.world == null) {
            TextureAnimations.updateAnimations();
        }
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
        if (this.mc.player != null) {
            ItemStack itemstack = this.mc.player.inventory.getCurrentItem();
            if (itemstack.isEmpty()) {
                this.remainingHighlightTicks = 0;
            } else if (!this.highlightingItemStack.isEmpty() && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
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
        this.setRecordPlaying(I18n.format("record.nowPlaying", recordName), true);
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

    public void setRecordPlaying(ITextComponent component, boolean isPlaying) {
        this.setRecordPlaying(component.getUnformattedText(), isPlaying);
    }

    public void func_191742_a(ChatType p_191742_1_, ITextComponent p_191742_2_) {
        for (IChatListener ichatlistener : this.field_191743_I.get((Object)p_191742_1_)) {
            ichatlistener.func_192576_a(p_191742_1_, p_191742_2_);
        }
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
        this.overlayBoss.clearBossInfos();
        this.mc.getToastGui().func_191788_b();
    }

    public GuiBossOverlay getBossOverlay() {
        return this.overlayBoss;
    }
}

