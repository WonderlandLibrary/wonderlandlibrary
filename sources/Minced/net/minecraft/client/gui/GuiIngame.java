// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.text.ITextComponent;
import net.optifine.TextureAnimations;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.FoodStats;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.modules.impl.PLAYER.StreamerMode;
import net.minecraft.scoreboard.Team;
import com.google.common.collect.Iterables;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.scoreboard.Score;
import ru.tuskevich.modules.impl.HUD.NoOverlay;
import net.minecraft.util.StringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.optifine.CustomColors;
import net.optifine.CustomItems;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import java.util.Iterator;
import java.util.Collection;
import net.optifine.reflect.Reflector;
import net.minecraft.potion.PotionEffect;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.inventory.IInventory;
import net.optifine.reflect.ReflectorForge;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import java.util.concurrent.TimeUnit;
import org.lwjgl.opengl.GL11;
import ru.tuskevich.commands.impl.GPSCommand;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.util.font.Fonts;
import java.awt.Color;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.util.render.GlowUtility;
import ru.tuskevich.util.color.ColorUtility;
import net.minecraft.util.math.MathHelper;
import ru.tuskevich.util.animations.AnimationMath;
import ru.tuskevich.modules.impl.PLAYER.Timer;
import ru.tuskevich.modules.impl.HUD.Hud;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.GlStateManager;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.HUD.Notifications;
import ru.tuskevich.Minced;
import net.minecraft.client.gui.chat.OverlayChatListener;
import net.minecraft.client.gui.chat.NormalChatListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.chat.IChatListener;
import java.util.List;
import net.minecraft.util.text.ChatType;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.util.ResourceLocation;

public class GuiIngame extends Gui
{
    private static final ResourceLocation VIGNETTE_TEX_PATH;
    private static final ResourceLocation WIDGETS_TEX_PATH;
    private static final ResourceLocation PUMPKIN_BLUR_TEX_PATH;
    private final Random rand;
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    private final GuiNewChat persistantChatGUI;
    private int updateCounter;
    private String overlayMessage;
    private int overlayMessageTime;
    private boolean animateOverlayMessageColor;
    public float prevVignetteBrightness;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSubtitleOverlay overlaySubtitle;
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private final GuiBossOverlay overlayBoss;
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
    private final Map<ChatType, List<IChatListener>> chatListeners;
    public static boolean joinedFirst;
    public float a;
    
    public GuiIngame(final Minecraft mcIn) {
        this.rand = new Random();
        this.overlayMessage = "";
        this.prevVignetteBrightness = 1.0f;
        this.highlightingItemStack = ItemStack.EMPTY;
        this.displayedTitle = "";
        this.displayedSubTitle = "";
        this.chatListeners = (Map<ChatType, List<IChatListener>>)Maps.newHashMap();
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.overlayBoss = new GuiBossOverlay(mcIn);
        this.overlaySubtitle = new GuiSubtitleOverlay(mcIn);
        for (final ChatType chattype : ChatType.values()) {
            this.chatListeners.put(chattype, Lists.newArrayList());
        }
        this.chatListeners.get(ChatType.CHAT).add(new NormalChatListener(mcIn));
        this.chatListeners.get(ChatType.SYSTEM).add(new NormalChatListener(mcIn));
        this.chatListeners.get(ChatType.GAME_INFO).add(new OverlayChatListener(mcIn));
        this.setDefaultTitlesTimes();
    }
    
    public void setDefaultTitlesTimes() {
        this.titleFadeIn = 10;
        this.titleDisplayTime = 70;
        this.titleFadeOut = 20;
    }
    
    public void renderGameOverlay(final float partialTicks) {
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        Minced.getInstance();
        if (Minced.getInstance().manager.getModule(Notifications.class).state) {
            Notifications.render(scaledresolution);
        }
        final int i = scaledresolution.getScaledWidth();
        final int j = scaledresolution.getScaledHeight();
        final FontRenderer fontrenderer = this.getFontRenderer();
        GlStateManager.enableBlend();
        if (Config.isVignetteEnabled()) {
            final Minecraft mc = this.mc;
            this.renderVignette(Minecraft.player.getBrightness(), scaledresolution);
        }
        else {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        final Minecraft mc2 = this.mc;
        final ItemStack itemstack = Minecraft.player.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
            this.renderPumpkinOverlay(scaledresolution);
        }
        final Minecraft mc3 = this.mc;
        if (!Minecraft.player.isPotionActive(MobEffects.NAUSEA)) {
            final Minecraft mc4 = this.mc;
            final float prevTimeInPortal = Minecraft.player.prevTimeInPortal;
            final Minecraft mc5 = this.mc;
            final float timeInPortal = Minecraft.player.timeInPortal;
            final Minecraft mc6 = this.mc;
            final float f = prevTimeInPortal + (timeInPortal - Minecraft.player.prevTimeInPortal) * partialTicks;
            if (f > 0.0f) {
                this.renderPortal(f, scaledresolution);
            }
        }
        if (this.mc.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        }
        else {
            this.renderHotbar(scaledresolution, partialTicks);
        }
        final Hud timer = (Hud)Minced.getInstance().manager.getModule(Hud.class);
        final float x = timer.timerDrag.getX();
        final float y = timer.timerDrag.getY();
        timer.timerDrag.setWidth(65.0f);
        timer.timerDrag.setHeight(8.0f);
        if (Timer.smart.get() && Hud.hudElements.get(4) && timer.state && !this.mc.gameSettings.showDebugInfo) {
            Minced.getInstance().scaleMath.pushScale();
            final boolean glow = !Timer.canEnableTimer(Timer.timerAmount.getFloatValue());
            Timer.animWidth = (float)MathHelper.clampedLerp(Timer.animWidth, (double)Math.round(Timer.getProgress() * 65.0), 40.0 * AnimationMath.deltaTime());
            if (Hud.arrayListElements.get(0)) {
                GlowUtility.drawGlow(x + 7.0f, y - 1.0f, 33.5f, 10.0f, 15, ColorUtility.applyOpacity(this.getColor(280), 0.85f));
                GlowUtility.drawGlow(x + 40.5f, y - 1.0f, 33.5f, 10.0f, 15, ColorUtility.applyOpacity(this.getColor(0), 0.85f));
            }
            RenderUtility.drawGradientRound(x + 7.0f, y - 1.0f, 67.0f, 10.0f, 3.0f, ColorUtility.applyOpacity(Hud.getColor(270), 0.85f), Hud.getColor(0), Hud.getColor(180), Hud.getColor(90));
            RenderUtility.drawRound(x + 8.0f, y, 65.0f, 8.0f, 2.0f, new Color(55, 55, 55));
            RenderUtility.drawRound(x + 8.0f, y, Timer.animWidth, 8.0f, 2.0f, new Color(25, 25, 25, 255));
            Fonts.Nunito14.drawCenteredStringWithShadow(Math.round(Timer.getProgress() * 100.0) + "%", x + 43.0f, y + 2.0f, new Color(255, 255, 255, 255).getRGB());
            Minced.getInstance().scaleMath.popScale();
        }
        EventManager.call(new EventDisplay(partialTicks, scaledresolution));
        if (GPSCommand.gps) {
            final double n = GPSCommand.z;
            final Minecraft mc7 = this.mc;
            final double y2 = n - Minecraft.player.posZ;
            final double n2 = GPSCommand.x;
            final Minecraft mc8 = this.mc;
            final double degrees = Math.toDegrees(Math.atan2(y2, n2 - Minecraft.player.posX));
            final Minecraft mc9 = this.mc;
            final double yaw = degrees - Minecraft.player.rotationYaw - 90.0;
            final double n3 = GPSCommand.x;
            final Minecraft mc10 = this.mc;
            final double pow = Math.pow(n3 - Minecraft.player.posX, 2.0);
            final double n4 = GPSCommand.z;
            final Minecraft mc11 = this.mc;
            final double dst = Math.sqrt(pow + Math.pow(n4 - Minecraft.player.posZ, 2.0));
            GL11.glPushMatrix();
            GL11.glTranslated(scaledresolution.getScaledWidth_double() / 2.0 + 0.5, scaledresolution.getScaledHeight_double() / 2.0 - 55.0, 0.0);
            GL11.glTranslated(Math.cos(Math.toRadians(yaw - 90.0)) * 1.3 * (Fonts.tenacity16.getStringWidth((int)dst + "m") / 2), Math.sin(Math.toRadians(yaw - 90.0)) * 10.0, 0.0);
            GL11.glRotated(yaw, 0.0, 0.0, 1.0);
            RenderUtility.drawTriangle();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(scaledresolution.getScaledWidth_double() / 2.0, scaledresolution.getScaledHeight_double() / 2.0 - 55.0, 0.0);
            Fonts.Nunito16.drawCenteredString((int)dst + "m", 0.0f, 0.0f, -1);
            GL11.glPopMatrix();
        }
        if (!GuiIngame.joinedFirst) {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5L);
                }
                catch (InterruptedException var3) {
                    throw new RuntimeException(var3);
                }
                return;
            }).start();
            GuiIngame.joinedFirst = true;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiIngame.ICONS);
        GlStateManager.enableBlend();
        this.renderAttackIndicator(partialTicks, scaledresolution);
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        this.mc.profiler.startSection("bossHealth");
        this.overlayBoss.renderBossHealth();
        this.mc.profiler.endSection();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiIngame.ICONS);
        if (this.mc.playerController.shouldDrawHUD()) {
            this.renderPlayerStats(scaledresolution);
        }
        this.renderMountHealth(scaledresolution);
        GlStateManager.disableBlend();
        final Minecraft mc12 = this.mc;
        if (Minecraft.player.getSleepTimer() > 0) {
            this.mc.profiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            final Minecraft mc13 = this.mc;
            final int j2 = Minecraft.player.getSleepTimer();
            float f2 = j2 / 100.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f - (j2 - 100) / 10.0f;
            }
            final int k = (int)(220.0f * f2) << 24 | 0x101020;
            Gui.drawRect(0.0f, 0.0f, (float)i, (float)j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.profiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int k2 = i / 2 - 91;
        final Minecraft mc14 = this.mc;
        if (Minecraft.player.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, k2);
        }
        else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, k2);
        }
        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
            this.renderSelectedItem(scaledresolution);
        }
        else {
            final Minecraft mc15 = this.mc;
            if (Minecraft.player.isSpectator()) {
                this.spectatorGui.renderSelectedItem(scaledresolution);
            }
        }
        if (this.mc.isDemo()) {
            this.renderDemo(scaledresolution);
        }
        if (!Minced.getInstance().manager.getModule(Hud.class).state || !Hud.hudElements.get(4)) {
            this.renderPotionEffects(scaledresolution);
        }
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }
        if (this.overlayMessageTime > 0) {
            this.mc.profiler.startSection("overlayMessage");
            final float f3 = this.overlayMessageTime - partialTicks;
            int l1 = (int)(f3 * 255.0f / 20.0f);
            if (l1 > 255) {
                l1 = 255;
            }
            if (l1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                int m = 16777215;
                if (this.animateOverlayMessageColor) {
                    m = (MathHelper.hsvToRGB(f3 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF);
                }
                fontrenderer.drawString(this.overlayMessage, -fontrenderer.getStringWidth(this.overlayMessage) / 2, -4, m + (l1 << 24 & 0xFF000000));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.profiler.endSection();
        }
        this.overlaySubtitle.renderSubtitles(scaledresolution);
        if (this.titlesTimer > 0) {
            this.mc.profiler.startSection("titleAndSubtitle");
            final float f4 = this.titlesTimer - partialTicks;
            int i2 = 255;
            if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
                final float f5 = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut - f4;
                i2 = (int)(f5 * 255.0f / this.titleFadeIn);
            }
            if (this.titlesTimer <= this.titleFadeOut) {
                i2 = (int)(f4 * 255.0f / this.titleFadeOut);
            }
            i2 = MathHelper.clamp(i2, 0, 255);
            if (i2 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j / 2), 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                final int j3 = i2 << 24 & 0xFF000000;
                fontrenderer.drawString(this.displayedTitle, (float)(-fontrenderer.getStringWidth(this.displayedTitle) / 2), -10.0f, 0xFFFFFF | j3, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                fontrenderer.drawString(this.displayedSubTitle, (float)(-fontrenderer.getStringWidth(this.displayedSubTitle) / 2), 5.0f, 0xFFFFFF | j3, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.profiler.endSection();
        }
        final Scoreboard scoreboard = this.mc.world.getScoreboard();
        ScoreObjective scoreobjective = null;
        final Scoreboard scoreboard2 = scoreboard;
        final Minecraft mc16 = this.mc;
        final ScorePlayerTeam scoreplayerteam = scoreboard2.getPlayersTeam(Minecraft.player.getName());
        if (scoreplayerteam != null) {
            final int i3 = scoreplayerteam.getColor().getColorIndex();
            if (i3 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i3);
            }
        }
        ScoreObjective scoreobjective2 = (scoreobjective != null) ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective2 != null) {
            this.renderScoreboard(scoreobjective2, scaledresolution);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, (float)(j - 48), 0.0f);
        this.mc.profiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.profiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective2 = scoreboard.getObjectiveInDisplaySlot(0);
        this.a = AnimationMath.fast(this.a, (float)(this.mc.gameSettings.keyBindPlayerList.isKeyDown() ? 1 : 0), 15.0f);
        this.a = MathHelper.clamp(this.a, 0.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glTranslated((double)(scaledresolution.getScaledWidth() / 2.0f), 10.0, 0.0);
        GL11.glScaled((double)this.a, (double)this.a, (double)this.a);
        GL11.glTranslated((double)(-scaledresolution.getScaledWidth() / 2.0f), -10.0, 0.0);
        this.overlayPlayerList.updatePlayerList(true);
        this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective2);
        GL11.glPopMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }
    
    private void renderAttackIndicator(final float partialTicks, final ScaledResolution p_184045_2_) {
        final GameSettings gamesettings = this.mc.gameSettings;
        if (gamesettings.thirdPersonView == 0) {
            if (this.mc.playerController.isSpectator() && this.mc.pointedEntity == null) {
                final RayTraceResult raytraceresult = this.mc.objectMouseOver;
                if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
                    return;
                }
                final BlockPos blockpos = raytraceresult.getBlockPos();
                final IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
                if (!ReflectorForge.blockHasTileEntity(iblockstate) || !(this.mc.world.getTileEntity(blockpos) instanceof IInventory)) {
                    return;
                }
            }
            final int l = p_184045_2_.getScaledWidth();
            final int i1 = p_184045_2_.getScaledHeight();
            if (gamesettings.showDebugInfo && !gamesettings.hideGUI) {
                final Minecraft mc = this.mc;
                if (!Minecraft.player.hasReducedDebug() && !gamesettings.reducedDebugInfo) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)(l / 2), (float)(i1 / 2), this.zLevel);
                    final Entity entity = this.mc.getRenderViewEntity();
                    GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0f, 1.0f, 0.0f);
                    GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                    OpenGlHelper.renderDirections(10);
                    GlStateManager.popMatrix();
                    return;
                }
            }
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(l / 2 - 7, i1 / 2 - 7, 0, 0, 16, 16);
            if (this.mc.gameSettings.attackIndicator == 1) {
                final Minecraft mc2 = this.mc;
                final float f = Minecraft.player.getCooledAttackStrength(0.0f);
                boolean flag = false;
                if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityLivingBase && f >= 1.0f) {
                    final Minecraft mc3 = this.mc;
                    flag = (Minecraft.player.getCooldownPeriod() > 5.0f);
                    flag &= ((EntityLivingBase)this.mc.pointedEntity).isEntityAlive();
                }
                final int j = i1 / 2 - 7 + 16;
                final int k = l / 2 - 8;
                if (flag) {
                    this.drawTexturedModalRect(k, j, 68, 94, 16, 16);
                }
                else if (f < 1.0f) {
                    final int m = (int)(f * 17.0f);
                    this.drawTexturedModalRect(k, j, 36, 94, 16, 4);
                    this.drawTexturedModalRect(k, j, 52, 94, m, 4);
                }
            }
        }
    }
    
    protected void renderPotionEffects(final ScaledResolution resolution) {
        final Minecraft mc = this.mc;
        final Collection<PotionEffect> collection = Minecraft.player.getActivePotionEffects();
        if (!collection.isEmpty()) {
            this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
            GlStateManager.enableBlend();
            int i = 0;
            int j = 0;
            for (final PotionEffect potioneffect : Ordering.natural().reverse().sortedCopy((Iterable)collection)) {
                final Potion potion = potioneffect.getPotion();
                boolean flag = potion.hasStatusIcon();
                if (Reflector.ForgePotion_shouldRenderHUD.exists()) {
                    if (!Reflector.callBoolean(potion, Reflector.ForgePotion_shouldRenderHUD, potioneffect)) {
                        continue;
                    }
                    this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                    flag = true;
                }
                if (flag && potioneffect.doesShowParticles()) {
                    int k = resolution.getScaledWidth();
                    int l = 1;
                    if (this.mc.isDemo()) {
                        l += 15;
                    }
                    final int i2 = potion.getStatusIconIndex();
                    if (potion.isBeneficial()) {
                        ++i;
                        k -= 25 * i;
                    }
                    else {
                        ++j;
                        k -= 25 * j;
                        l += 26;
                    }
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    float f = 1.0f;
                    if (potioneffect.getIsAmbient()) {
                        this.drawTexturedModalRect(k, l, 165, 166, 24, 24);
                    }
                    else {
                        this.drawTexturedModalRect(k, l, 141, 166, 24, 24);
                        if (potioneffect.getDuration() <= 200) {
                            final int j2 = 10 - potioneffect.getDuration() / 20;
                            f = MathHelper.clamp(potioneffect.getDuration() / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + MathHelper.cos(potioneffect.getDuration() * 3.1415927f / 5.0f) * MathHelper.clamp(j2 / 10.0f * 0.25f, 0.0f, 0.25f);
                        }
                    }
                    GlStateManager.color(1.0f, 1.0f, 1.0f, f);
                    if (Reflector.ForgePotion_renderHUDEffect.exists()) {
                        if (potion.hasStatusIcon()) {
                            this.drawTexturedModalRect(k + 3, l + 3, i2 % 8 * 18, 198 + i2 / 8 * 18, 18, 18);
                        }
                        Reflector.call(potion, Reflector.ForgePotion_renderHUDEffect, potioneffect, this, k, l, this.zLevel, f);
                    }
                    else {
                        this.drawTexturedModalRect(k + 3, l + 3, i2 % 8 * 18, 198 + i2 / 8 * 18, 18, 18);
                    }
                }
            }
        }
    }
    
    protected void renderHotbar(final ScaledResolution sr, final float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(GuiIngame.WIDGETS_TEX_PATH);
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final ItemStack itemstack = entityplayer.getHeldItemOffhand();
            final EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
            final int i = sr.getScaledWidth() / 2;
            final float f = this.zLevel;
            final int j = 182;
            final int k = 91;
            this.zLevel = -90.0f;
            this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            if (!itemstack.isEmpty()) {
                if (enumhandside == EnumHandSide.LEFT) {
                    this.drawTexturedModalRect(i - 91 - 29, sr.getScaledHeight() - 23, 24, 22, 29, 24);
                }
                else {
                    this.drawTexturedModalRect(i + 91, sr.getScaledHeight() - 23, 53, 22, 29, 24);
                }
            }
            this.zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();
            CustomItems.setRenderOffHand(false);
            for (int l = 0; l < 9; ++l) {
                final int i2 = i - 90 + l * 20 + 2;
                final int j2 = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(i2, j2, partialTicks, entityplayer, entityplayer.inventory.mainInventory.get(l));
            }
            if (!itemstack.isEmpty()) {
                CustomItems.setRenderOffHand(true);
                final int l2 = sr.getScaledHeight() - 16 - 3;
                if (enumhandside == EnumHandSide.LEFT) {
                    this.renderHotbarItem(i - 91 - 26, l2, partialTicks, entityplayer, itemstack);
                }
                else {
                    this.renderHotbarItem(i + 91 + 10, l2, partialTicks, entityplayer, itemstack);
                }
                CustomItems.setRenderOffHand(false);
            }
            if (this.mc.gameSettings.attackIndicator == 2) {
                final Minecraft mc = this.mc;
                final float f2 = Minecraft.player.getCooledAttackStrength(0.0f);
                if (f2 < 1.0f) {
                    final int i3 = sr.getScaledHeight() - 20;
                    int j3 = i + 91 + 6;
                    if (enumhandside == EnumHandSide.RIGHT) {
                        j3 = i - 91 - 22;
                    }
                    this.mc.getTextureManager().bindTexture(Gui.ICONS);
                    final int k2 = (int)(f2 * 19.0f);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    this.drawTexturedModalRect(j3, i3, 0, 94, 18, 18);
                    this.drawTexturedModalRect(j3, i3 + 18 - k2, 18, 112 - k2, 18, k2);
                }
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }
    
    public void renderHorseJumpBar(final ScaledResolution scaledRes, final int x) {
        this.mc.profiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.ICONS);
        final Minecraft mc = this.mc;
        final float f = Minecraft.player.getHorseJumpPower();
        final int i = 182;
        final int j = (int)(f * 183.0f);
        final int k = scaledRes.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(x, k, 0, 84, 182, 5);
        if (j > 0) {
            this.drawTexturedModalRect(x, k, 0, 89, j, 5);
        }
        this.mc.profiler.endSection();
    }
    
    public void renderExpBar(final ScaledResolution scaledRes, final int x) {
        this.mc.profiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.ICONS);
        final Minecraft mc = this.mc;
        final int i = Minecraft.player.xpBarCap();
        if (i > 0) {
            final int j = 182;
            final Minecraft mc2 = this.mc;
            final int k = (int)(Minecraft.player.experience * 183.0f);
            final int l = scaledRes.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(x, l, 0, 64, 182, 5);
            if (k > 0) {
                this.drawTexturedModalRect(x, l, 0, 69, k, 5);
            }
        }
        this.mc.profiler.endSection();
        final Minecraft mc3 = this.mc;
        if (Minecraft.player.experienceLevel > 0) {
            this.mc.profiler.startSection("expLevel");
            int j2 = 8453920;
            if (Config.isCustomColors()) {
                j2 = CustomColors.getExpBarTextColor(j2);
            }
            final StringBuilder append = new StringBuilder().append("");
            final Minecraft mc4 = this.mc;
            final String s = append.append(Minecraft.player.experienceLevel).toString();
            final int k2 = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            final int i2 = scaledRes.getScaledHeight() - 31 - 4;
            this.getFontRenderer().drawString(s, k2 + 1, i2, 0);
            this.getFontRenderer().drawString(s, k2 - 1, i2, 0);
            this.getFontRenderer().drawString(s, k2, i2 + 1, 0);
            this.getFontRenderer().drawString(s, k2, i2 - 1, 0);
            this.getFontRenderer().drawString(s, k2, i2, j2);
            this.mc.profiler.endSection();
        }
    }
    
    public void renderSelectedItem(final ScaledResolution scaledRes) {
        this.mc.profiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && !this.highlightingItemStack.isEmpty()) {
            String s = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                s = TextFormatting.ITALIC + s;
            }
            final int i = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int j = scaledRes.getScaledHeight() - 59;
            if (!this.mc.playerController.shouldDrawHUD()) {
                j += 14;
            }
            int k = (int)(this.remainingHighlightTicks * 256.0f / 10.0f);
            if (k > 255) {
                k = 255;
            }
            if (k > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                this.getFontRenderer().drawStringWithShadow(s, (float)i, (float)j, 16777215 + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.profiler.endSection();
    }
    
    public void renderDemo(final ScaledResolution scaledRes) {
        this.mc.profiler.startSection("demo");
        String s;
        if (this.mc.world.getTotalWorldTime() >= 120500L) {
            s = I18n.format("demo.demoExpired", new Object[0]);
        }
        else {
            s = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.world.getTotalWorldTime())));
        }
        final int i = this.getFontRenderer().getStringWidth(s);
        this.getFontRenderer().drawStringWithShadow(s, (float)(scaledRes.getScaledWidth() - i - 10), 5.0f, 16777215);
        this.mc.profiler.endSection();
    }
    
    private void renderScoreboard(final ScoreObjective objective, ScaledResolution scaledRes) {
        if (NoOverlay.element.get(0) && Minced.getInstance().manager.getModule(NoOverlay.class).state) {
            return;
        }
        scaledRes = new ScaledResolution(this.mc);
        this.mc.entityRenderer.setupOverlayRendering();
        final Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        final List<Score> list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")).collect((Collector<? super Score, ?, List<Score>>)Collectors.toList());
        if (list.size() > 15) {
            collection = (Collection<Score>)Lists.newArrayList(Iterables.skip((Iterable)list, collection.size() - 15));
        }
        else {
            collection = list;
        }
        int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());
        for (final Score score : collection) {
            final ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            final String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + TextFormatting.RED + score.getScorePoints();
            i = Math.max(i, this.getFontRenderer().getStringWidth(s));
        }
        final int i2 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
        final int j1 = scaledRes.getScaledHeight() / 2 + i2 / 3;
        final int k1 = 3;
        final int l1 = scaledRes.getScaledWidth() - i - 3;
        int m = 0;
        for (final Score score2 : collection) {
            ++m;
            final ScorePlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(score2.getPlayerName());
            String s2 = ScorePlayerTeam.formatPlayerName(scoreplayerteam2, score2.getPlayerName());
            final String s3 = TextFormatting.RED + "" + score2.getScorePoints();
            final int k2 = j1 - m * this.getFontRenderer().FONT_HEIGHT;
            final int l2 = scaledRes.getScaledWidth() - 3 + 2;
            Gui.drawRect((float)(l1 - 2), (float)k2, (float)l2, (float)(k2 + this.getFontRenderer().FONT_HEIGHT), 1342177280);
            final boolean check = Minced.getInstance().manager.getModule(StreamerMode.class).state;
            if (this.mc.getCurrentServerData() != null && check) {
                final StringBuilder builder = new StringBuilder();
                final char[] buffer = s2.toCharArray();
                for (int w = 0; w < buffer.length; ++w) {
                    final char c = buffer[w];
                    if (c == '\ufffd') {
                        ++w;
                    }
                    else {
                        builder.append(c);
                    }
                }
                final String string;
                final String s4 = string = builder.toString();
                final Minecraft mc = this.mc;
                if (string.contains(Minecraft.player.getName())) {
                    final String s6 = s4;
                    final Minecraft mc2 = this.mc;
                    s2 = s6.replace(Minecraft.player.getName(), ChatFormatting.DARK_PURPLE + "NameProtect");
                }
            }
            this.getFontRenderer().drawString(s2, l1, k2, 553648127);
            this.getFontRenderer().drawString(s3, l2 - this.getFontRenderer().getStringWidth(s3), k2, 553648127);
            if (m == collection.size()) {
                final String s5 = objective.getDisplayName();
                Gui.drawRect((float)(l1 - 2), (float)(k2 - this.getFontRenderer().FONT_HEIGHT - 1), (float)l2, (float)(k2 - 1), 1610612736);
                Gui.drawRect((float)(l1 - 2), (float)(k2 - 1), (float)l2, (float)k2, 1342177280);
                this.getFontRenderer().drawString(s5, l1 + i / 2 - this.getFontRenderer().getStringWidth(s5) / 2, k2 - this.getFontRenderer().FONT_HEIGHT, 553648127);
            }
        }
        this.mc.entityRenderer.setupOverlayRendering();
    }
    
    private void renderPlayerStats(final ScaledResolution scaledRes) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final int i = MathHelper.ceil(entityplayer.getHealth());
            final boolean flag = this.healthUpdateCounter > this.updateCounter && (this.healthUpdateCounter - this.updateCounter) / 3L % 2L == 1L;
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
            final int j = this.lastPlayerHealth;
            this.rand.setSeed(this.updateCounter * 312871);
            final FoodStats foodstats = entityplayer.getFoodStats();
            final int k = foodstats.getFoodLevel();
            final IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            final int l = scaledRes.getScaledWidth() / 2 - 91;
            final int i2 = scaledRes.getScaledWidth() / 2 + 91;
            final int j2 = scaledRes.getScaledHeight() - 39;
            final float f = (float)iattributeinstance.getAttributeValue();
            final int k2 = MathHelper.ceil(entityplayer.getAbsorptionAmount());
            final int l2 = MathHelper.ceil((f + k2) / 2.0f / 10.0f);
            final int i3 = Math.max(10 - (l2 - 2), 3);
            final int j3 = j2 - (l2 - 1) * i3 - 10;
            final int k3 = j2 - 10;
            int l3 = k2;
            final int i4 = entityplayer.getTotalArmorValue();
            int j4 = -1;
            if (entityplayer.isPotionActive(MobEffects.REGENERATION)) {
                j4 = this.updateCounter % MathHelper.ceil(f + 5.0f);
            }
            this.mc.profiler.startSection("armor");
            for (int k4 = 0; k4 < 10; ++k4) {
                if (i4 > 0) {
                    final int l4 = l + k4 * 8;
                    if (k4 * 2 + 1 < i4) {
                        this.drawTexturedModalRect(l4, j3, 34, 9, 9, 9);
                    }
                    if (k4 * 2 + 1 == i4) {
                        this.drawTexturedModalRect(l4, j3, 25, 9, 9, 9);
                    }
                    if (k4 * 2 + 1 > i4) {
                        this.drawTexturedModalRect(l4, j3, 16, 9, 9, 9);
                    }
                }
            }
            this.mc.profiler.endStartSection("health");
            for (int j5 = MathHelper.ceil((f + k2) / 2.0f) - 1; j5 >= 0; --j5) {
                int k5 = 16;
                if (entityplayer.isPotionActive(MobEffects.POISON)) {
                    k5 += 36;
                }
                else if (entityplayer.isPotionActive(MobEffects.WITHER)) {
                    k5 += 72;
                }
                int i5 = 0;
                if (flag) {
                    i5 = 1;
                }
                final int j6 = MathHelper.ceil((j5 + 1) / 10.0f) - 1;
                final int k6 = l + j5 % 10 * 8;
                int l5 = j2 - j6 * i3;
                if (i <= 4) {
                    l5 += this.rand.nextInt(2);
                }
                if (l3 <= 0 && j5 == j4) {
                    l5 -= 2;
                }
                int i6 = 0;
                if (entityplayer.world.getWorldInfo().isHardcoreModeEnabled()) {
                    i6 = 5;
                }
                this.drawTexturedModalRect(k6, l5, 16 + i5 * 9, 9 * i6, 9, 9);
                if (flag) {
                    if (j5 * 2 + 1 < j) {
                        this.drawTexturedModalRect(k6, l5, k5 + 54, 9 * i6, 9, 9);
                    }
                    if (j5 * 2 + 1 == j) {
                        this.drawTexturedModalRect(k6, l5, k5 + 63, 9 * i6, 9, 9);
                    }
                }
                if (l3 > 0) {
                    if (l3 == k2 && k2 % 2 == 1) {
                        this.drawTexturedModalRect(k6, l5, k5 + 153, 9 * i6, 9, 9);
                        --l3;
                    }
                    else {
                        this.drawTexturedModalRect(k6, l5, k5 + 144, 9 * i6, 9, 9);
                        l3 -= 2;
                    }
                }
                else {
                    if (j5 * 2 + 1 < i) {
                        this.drawTexturedModalRect(k6, l5, k5 + 36, 9 * i6, 9, 9);
                    }
                    if (j5 * 2 + 1 == i) {
                        this.drawTexturedModalRect(k6, l5, k5 + 45, 9 * i6, 9, 9);
                    }
                }
            }
            final Entity entity = entityplayer.getRidingEntity();
            if (entity == null || !(entity instanceof EntityLivingBase)) {
                this.mc.profiler.endStartSection("food");
                for (int l6 = 0; l6 < 10; ++l6) {
                    int j7 = j2;
                    int l7 = 16;
                    int j8 = 0;
                    if (entityplayer.isPotionActive(MobEffects.HUNGER)) {
                        l7 += 36;
                        j8 = 13;
                    }
                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (k * 3 + 1) == 0) {
                        j7 = j2 + (this.rand.nextInt(3) - 1);
                    }
                    final int l8 = i2 - l6 * 8 - 9;
                    this.drawTexturedModalRect(l8, j7, 16 + j8 * 9, 27, 9, 9);
                    if (l6 * 2 + 1 < k) {
                        this.drawTexturedModalRect(l8, j7, l7 + 36, 27, 9, 9);
                    }
                    if (l6 * 2 + 1 == k) {
                        this.drawTexturedModalRect(l8, j7, l7 + 45, 27, 9, 9);
                    }
                }
            }
            this.mc.profiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.WATER)) {
                final Minecraft mc = this.mc;
                final int i7 = Minecraft.player.getAir();
                for (int k7 = MathHelper.ceil((i7 - 2) * 10.0 / 300.0), i8 = MathHelper.ceil(i7 * 10.0 / 300.0) - k7, k8 = 0; k8 < k7 + i8; ++k8) {
                    if (k8 < k7) {
                        this.drawTexturedModalRect(i2 - k8 * 8 - 9, k3, 16, 18, 9, 9);
                    }
                    else {
                        this.drawTexturedModalRect(i2 - k8 * 8 - 9, k3, 25, 18, 9, 9);
                    }
                }
            }
            this.mc.profiler.endSection();
        }
    }
    
    private void renderMountHealth(final ScaledResolution p_184047_1_) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            final Entity entity = entityplayer.getRidingEntity();
            if (entity instanceof EntityLivingBase) {
                this.mc.profiler.endStartSection("mountHealth");
                final EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                final int i = (int)Math.ceil(entitylivingbase.getHealth());
                final float f = entitylivingbase.getMaxHealth();
                int j = (int)(f + 0.5f) / 2;
                if (j > 30) {
                    j = 30;
                }
                final int k = p_184047_1_.getScaledHeight() - 39;
                final int l = p_184047_1_.getScaledWidth() / 2 + 91;
                int i2 = k;
                int j2 = 0;
                final boolean flag = false;
                while (j > 0) {
                    final int k2 = Math.min(j, 10);
                    j -= k2;
                    for (int l2 = 0; l2 < k2; ++l2) {
                        final int i3 = 52;
                        final int j3 = 0;
                        final int k3 = l - l2 * 8 - 9;
                        this.drawTexturedModalRect(k3, i2, 52 + j3 * 9, 9, 9, 9);
                        if (l2 * 2 + 1 + j2 < i) {
                            this.drawTexturedModalRect(k3, i2, 88, 9, 9, 9);
                        }
                        if (l2 * 2 + 1 + j2 == i) {
                            this.drawTexturedModalRect(k3, i2, 97, 9, 9, 9);
                        }
                    }
                    i2 -= 10;
                    j2 += 20;
                }
            }
        }
    }
    
    private void renderPumpkinOverlay(final ScaledResolution scaledRes) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(GuiIngame.PUMPKIN_BLUR_TEX_PATH);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
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
    
    private void renderVignette(float lightLevel, final ScaledResolution scaledRes) {
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        else {
            lightLevel = 1.0f - lightLevel;
            lightLevel = MathHelper.clamp(lightLevel, 0.0f, 1.0f);
            final WorldBorder worldBorder;
            final WorldBorder worldborder = worldBorder = this.mc.world.getWorldBorder();
            final Minecraft mc = this.mc;
            float f = (float)worldBorder.getClosestDistance(Minecraft.player);
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
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (f > 0.0f) {
                GlStateManager.color(0.0f, f, f, 1.0f);
            }
            else {
                GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
            }
            this.mc.getTextureManager().bindTexture(GuiIngame.VIGNETTE_TEX_PATH);
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
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
    
    private void renderPortal(float timeInPortal, final ScaledResolution scaledRes) {
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
        final TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.getDefaultState());
        final float f = textureatlassprite.getMinU();
        final float f2 = textureatlassprite.getMinV();
        final float f3 = textureatlassprite.getMaxU();
        final float f4 = textureatlassprite.getMaxV();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(f, f4).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(f3, f4).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(f3, f2).endVertex();
        bufferbuilder.pos(0.0, 0.0, -90.0).tex(f, f2).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderHotbarItem(final int x, final int y, final float partialTicks, final EntityPlayer player, final ItemStack stack) {
        if (!stack.isEmpty()) {
            final float f = stack.getAnimationsToGo() - partialTicks;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                final float f2 = 1.0f + f / 5.0f;
                GlStateManager.translate((float)(x + 8), (float)(y + 12), 0.0f);
                GlStateManager.scale(1.0f / f2, (f2 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate((float)(-(x + 8)), (float)(-(y + 12)), 0.0f);
            }
            this.itemRenderer.renderItemAndEffectIntoGUI(player, stack, x, y);
            if (f > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(this.mc.fontRenderer, stack, x, y);
        }
    }
    
    public void updateTick() {
        if (this.mc.world == null) {
            TextureAnimations.updateAnimations();
        }
        if (this.overlayMessageTime > 0) {
            --this.overlayMessageTime;
        }
        if (this.titlesTimer > 0) {
            --this.titlesTimer;
            if (this.titlesTimer <= 0) {
                this.displayedTitle = "";
                this.displayedSubTitle = "";
            }
        }
        ++this.updateCounter;
        final Minecraft mc = this.mc;
        if (Minecraft.player != null) {
            final Minecraft mc2 = this.mc;
            final ItemStack itemstack = Minecraft.player.inventory.getCurrentItem();
            if (itemstack.isEmpty()) {
                this.remainingHighlightTicks = 0;
            }
            else if (!this.highlightingItemStack.isEmpty() && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
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
        this.setOverlayMessage(I18n.format("record.nowPlaying", recordName), true);
    }
    
    public void setOverlayMessage(final String message, final boolean animateColor) {
        this.overlayMessage = message;
        this.overlayMessageTime = 60;
        this.animateOverlayMessageColor = animateColor;
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
    
    public void setOverlayMessage(final ITextComponent component, final boolean animateColor) {
        this.setOverlayMessage(component.getUnformattedText(), animateColor);
    }
    
    public void addChatMessage(final ChatType chatTypeIn, final ITextComponent message) {
        for (final IChatListener ichatlistener : this.chatListeners.get(chatTypeIn)) {
            ichatlistener.say(chatTypeIn, message);
        }
    }
    
    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }
    
    public int getUpdateCounter() {
        return this.updateCounter;
    }
    
    public FontRenderer getFontRenderer() {
        return this.mc.fontRenderer;
    }
    
    public GuiSpectator getSpectatorGui() {
        return this.spectatorGui;
    }
    
    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }
    
    private Color getColor(final int count) {
        final Color colorOne = Hud.onecolor.getColorValueColor();
        final Color colorTwo = Hud.twocolor.getColorValueColor();
        return ColorUtility.interpolateColorsBackAndForth(5, count, colorOne, colorTwo, false);
    }
    
    public void resetPlayersOverlayFooterHeader() {
        this.overlayPlayerList.resetFooterHeader();
        this.overlayBoss.clearBossInfos();
        this.mc.getToastGui().clear();
    }
    
    public GuiBossOverlay getBossOverlay() {
        return this.overlayBoss;
    }
    
    static {
        VIGNETTE_TEX_PATH = new ResourceLocation("textures/misc/vignette.png");
        WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
        PUMPKIN_BLUR_TEX_PATH = new ResourceLocation("textures/misc/pumpkinblur.png");
        GuiIngame.joinedFirst = false;
    }
}
