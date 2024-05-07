package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.impl.gui.HUD;
import com.masterof13fps.manager.eventmanager.impl.EventRender;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.manager.notificationmanager.NotificationManager;
import com.masterof13fps.manager.settingsmanager.Setting;
import com.masterof13fps.utils.render.Rainbow;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.*;
import net.minecraft.world.border.WorldBorder;
import net.optifine.Config;
import net.optifine.CustomColors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class GuiIngame extends Gui implements Wrapper {
    private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    private static final String __OBFID = "CL_00000661";
    private final Random rand = new Random();
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    /**
     * ChatGUI instance that retains all previous chat data
     */
    private final GuiNewChat persistantChatGUI;
    private final GuiStreamIndicator streamIndicator;
    private final GuiOverlayDebug overlayDebug;
    /**
     * The spectator GUI for this in-game GUI instance
     */
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    /**
     * Previous frame vignette brightness (slowly changes by 1% each frame)
     */
    public float prevVignetteBrightness = 1.0F;
    private int updateCounter;
    /**
     * The string specifying which record music is playing
     */
    private String recordPlaying = "";
    /**
     * How many ticks the record playing message will be displayed
     */
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    /**
     * Remaining ticks the item highlight should be visible
     */
    private int remainingHighlightTicks;
    /**
     * The ItemStack that is currently being highlighted
     */
    private ItemStack highlightingItemStack;
    private int field_175195_w;
    private String field_175201_x = "";
    private String field_175200_y = "";
    private int field_175199_z;
    private int field_175192_A;
    private int field_175193_B;
    private int playerHealth = 0;
    private int lastPlayerHealth = 0;
    /**
     * The last recorded system time
     */
    private long lastSystemTime = 0L;
    /**
     * Used with updateCounter to make the heart bar flash
     */
    private long healthUpdateCounter = 0L;

    public GuiIngame(Minecraft mcIn) {
        mc = mcIn;
        itemRenderer = mcIn.getRenderItem();
        overlayDebug = new GuiOverlayDebug(mcIn);
        spectatorGui = new GuiSpectator(mcIn);
        persistantChatGUI = new GuiNewChat(mcIn);
        streamIndicator = new GuiStreamIndicator(mcIn);
        overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        func_175177_a();
    }

    public void func_175177_a() {
        field_175199_z = 10;
        field_175192_A = 70;
        field_175193_B = 20;
    }

    public void renderGameOverlay(float partialTicks) {
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        int width = scaledresolution.width();
        int height = scaledresolution.height();
        mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();

        if (Config.isVignetteEnabled()) {
            renderVignette(mc.thePlayer.getBrightness(partialTicks), scaledresolution);
        } else {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }

        ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(3);

        if (mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            renderPumpkinOverlay(scaledresolution);
        }

        if (!mc.thePlayer.isPotionActive(Potion.confusion)) {
            float f = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * partialTicks;

            if (f > 0.0F) {
                func_180474_b(f, scaledresolution);
            }
        }

        if (mc.playerController.isSpectator()) {
            spectatorGui.renderTooltip(scaledresolution, partialTicks);
        } else {
            renderTooltip(scaledresolution, partialTicks);
        }


        EventRender eventRender = new EventRender(EventRender.Type.twoD);
        eventManager.onEvent(eventRender);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(icons);
        GlStateManager.enableBlend();

        if (showCrosshair() && mc.gameSettings.thirdPersonView < 1) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            drawTexturedModalRect(width / 2 - 7, height / 2 - 7, 0, 0, 16, 16);
        }

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        mc.mcProfiler.startSection("bossHealth");
        renderBossHealth();
        mc.mcProfiler.endSection();

        if (mc.playerController.shouldDrawHUD()) {
            renderPlayerStats(scaledresolution);
        }

        GlStateManager.disableBlend();

        if (mc.thePlayer.getSleepTimer() > 0) {
            mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int l = mc.thePlayer.getSleepTimer();
            float f2 = (float) l / 100.0F;

            if (f2 > 1.0F) {
                f2 = 1.0F - (float) (l - 100) / 10.0F;
            }

            int k = (int) (220.0F * f2) << 24 | 1052704;
            drawRect(0, 0, width, height, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            mc.mcProfiler.endSection();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        int i2 = width / 2 - 91;

        if (mc.thePlayer.isRidingHorse()) {
            renderHorseJumpBar(scaledresolution, i2);
        } else if (mc.playerController.gameIsSurvivalOrAdventure()) {
            renderExpBar(scaledresolution, i2);
        }

        if (mc.gameSettings.heldItemTooltips && !mc.playerController.isSpectator()) {
            func_181551_a(scaledresolution);
        } else if (mc.thePlayer.isSpectator()) {
            spectatorGui.func_175263_a(scaledresolution);
        }

        if (mc.isDemo()) {
            renderDemo(scaledresolution);
        }

        if (mc.gameSettings.showDebugInfo) {
            overlayDebug.renderDebugInfo(scaledresolution);
        }

        if (recordPlayingUpFor > 0) {
            mc.mcProfiler.startSection("overlayMessage");
            float f3 = (float) recordPlayingUpFor - partialTicks;
            int k1 = (int) (f3 * 255.0F / 20.0F);

            if (k1 > 255) {
                k1 = 255;
            }

            if (k1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (width / 2), (float) (height - 68), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int i1 = 16777215;

                if (recordIsPlaying) {
                    i1 = MathHelper.func_181758_c(f3 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                getFontRenderer().drawString(recordPlaying, -getFontRenderer().getStringWidth(recordPlaying) / 2, -4, i1 + (k1 << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            mc.mcProfiler.endSection();
        }

        if (field_175195_w > 0) {
            mc.mcProfiler.startSection("titleAndSubtitle");
            float f4 = (float) field_175195_w - partialTicks;
            int l1 = 255;

            if (field_175195_w > field_175193_B + field_175192_A) {
                float f1 = (float) (field_175199_z + field_175192_A + field_175193_B) - f4;
                l1 = (int) (f1 * 255.0F / (float) field_175199_z);
            }

            if (field_175195_w <= field_175193_B) {
                l1 = (int) (f4 * 255.0F / (float) field_175193_B);
            }

            l1 = MathHelper.clamp_int(l1, 0, 255);

            if (l1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (width / 2), (float) (height / 2), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                int j2 = l1 << 24 & -16777216;
                getFontRenderer().drawString(field_175201_x, (float) (-getFontRenderer().getStringWidth(field_175201_x) / 2), -10.0F, 16777215 | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                getFontRenderer().drawString(field_175200_y, (float) (-getFontRenderer().getStringWidth(field_175200_y) / 2), 5.0F, 16777215 | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            mc.mcProfiler.endSection();
        }

        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.thePlayer.getName());

        if (scoreplayerteam != null) {
            int j1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (j1 >= 0) {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + j1);
            }
        }

        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

        if (scoreobjective1 != null) {
            renderScoreboard(scoreobjective1, scaledresolution);
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, (float) (height - 48), 0.0F);
        mc.mcProfiler.startSection("chat");
        persistantChatGUI.drawChat(updateCounter);
        mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);

        if (!mc.gameSettings.keyBindPlayerList.isKeyDown() || mc.isIntegratedServerRunning() && mc.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1 && scoreobjective1 == null) {
            overlayPlayerList.updatePlayerList(false);
        } else {
            overlayPlayerList.updatePlayerList(true);
            overlayPlayerList.renderPlayerlist(width, scoreboard, scoreobjective1);
        }

        NotificationManager.render();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }

    protected void renderTooltip(ScaledResolution sr, float partialTicks) {
        renderHotbar(partialTicks);
    }

    private void renderHotbar(float partialTicks) {
        Setting hotbar = Client.main().setMgr().settingByName("Hotbar", Client.main().modMgr().getModule(HUD.class));
        ScaledResolution s = new ScaledResolution(mc);

        if (Client.main().modMgr().getByName("HUD").state() && hotbar.isToggled()) {
            RenderUtils.drawRect((s.width() / 2) - 91, s.height() - 23, (s.width() / 2) + 91, s.height(), new Color(14, 14, 14).getRGB());

            Color selectedColor = new Color(32, 32, 32);
            Module hudModule = getModuleManager().getModule(HUD.class);
            int rainbowOffset = (int) getSettingByName("Rainbow Offset", hudModule).getCurrentValue();
            int rainbowSpeed = (int) getSettingByName("Rainbow Speed", hudModule).getCurrentValue();
            float rainbowSaturation = (float) getSettingByName("Rainbow Saturation", hudModule).getCurrentValue();
            float rainbowBrightness = (float) getSettingByName("Rainbow Brightness", hudModule).getCurrentValue();
            String hotbarMode = getSettingByName("Hotbar Mode", hudModule).getCurrentMode();

            Color hotbarTopColor = new Color(0xbd4b56);

            if (mc.thePlayer.inventory.currentItem == 0) {
                RenderUtils.drawRect((s.width() / 2) - 91 + mc.thePlayer.inventory.currentItem * 20,
                        s.height() - 23, (s.width() / 2) + 91 - 20 * 8, s.height(), selectedColor.getRGB());
            } else {
                RenderUtils.drawRect((s.width() / 2) - 91 + mc.thePlayer.inventory.currentItem * 20,
                        s.height() - 23, (s.width() / 2) + 91 - 20 * (8 - mc.thePlayer.inventory.currentItem), s.height(), selectedColor.getRGB());
            }

            switch (hotbarMode) {
                case "Rainbow":
                    RenderUtils.drawRect(s.width() / 2 - 91, s.height() - 24, s.width() / 2 + 91, s.height() - 23,
                            Rainbow.getRainbow(rainbowOffset, rainbowSpeed, rainbowSaturation, rainbowBrightness));
                    break;
                case "Static Color":
                    RenderUtils.drawRect(s.width() / 2 - 91, s.height() - 24, s.width() / 2 + 91, s.height() - 23,
                            hotbarTopColor.getRGB());
                    break;
            }

            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
            RenderHelper.enableGUIStandardItemLighting();

            for (int j = 0; j < 9; ++j) {
                int k = s.width() / 2 - 90 + j * 20 + 2;
                int l = s.height() - 16 - 3;
                renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        } else {
            if (mc.getRenderViewEntity() instanceof EntityPlayer) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(widgetsTexPath);
                EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
                int i = s.width() / 2;
                float f = zLevel;
                zLevel = -90.0F;
                drawTexturedModalRect(i - 91, s.height() - 22, 0, 0, 182, 22);
                drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, s.height() - 22 - 1, 0, 22, 24, 22);
                zLevel = f;
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();

                for (int j = 0; j < 9; ++j) {
                    int k = s.width() / 2 - 90 + j * 20 + 2;
                    int l = s.height() - 16 - 3;
                    renderHotbarItem(j, k, l, partialTicks, entityplayer);
                }

                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
    }

    public void renderHorseJumpBar(ScaledResolution p_175186_1_, int p_175186_2_) {
        mc.mcProfiler.startSection("jumpBar");
        mc.getTextureManager().bindTexture(Gui.icons);
        float f = mc.thePlayer.getHorseJumpPower();
        short short1 = 182;
        int i = (int) (f * (float) (short1 + 1));
        int j = p_175186_1_.height() - 32 + 3;
        drawTexturedModalRect(p_175186_2_, j, 0, 84, short1, 5);

        if (i > 0) {
            drawTexturedModalRect(p_175186_2_, j, 0, 89, i, 5);
        }

        mc.mcProfiler.endSection();
    }

    public void renderExpBar(ScaledResolution scaledResolution, int p_175176_2_) {
        if (!Client.main().modMgr().getByName("NoEXP").state()) {
            mc.mcProfiler.startSection("expBar");
            mc.getTextureManager().bindTexture(Gui.icons);
            int i = mc.thePlayer.xpBarCap();

            if (i > 0) {
                short short1 = 182;
                int k = (int) (mc.thePlayer.experience * (float) (short1 + 1));
                int j = scaledResolution.height() - 32 + 3;
                drawTexturedModalRect(p_175176_2_, j, 0, 64, short1, 5);

                if (k > 0) {
                    drawTexturedModalRect(p_175176_2_, j, 0, 69, k, 5);
                }
            }

            mc.mcProfiler.endSection();

            if (mc.thePlayer.experienceLevel > 0) {
                mc.mcProfiler.startSection("expLevel");
                int j1 = 8453920;

                if (Config.isCustomColors()) {
                    j1 = CustomColors.getExpBarTextColor(j1);
                }

                String s = "" + mc.thePlayer.experienceLevel;
                int i1 = (scaledResolution.width() - getFontRenderer().getStringWidth(s)) / 2;
                int l = scaledResolution.height() - 31 - 4;
                boolean flag = false;
                getFontRenderer().drawString(s, i1 + 1, l, 0);
                getFontRenderer().drawString(s, i1 - 1, l, 0);
                getFontRenderer().drawString(s, i1, l + 1, 0);
                getFontRenderer().drawString(s, i1, l - 1, 0);
                getFontRenderer().drawString(s, i1, l, j1);
                mc.mcProfiler.endSection();
            }
        }
    }

    public void func_181551_a(ScaledResolution p_181551_1_) {
        mc.mcProfiler.startSection("selectedItemName");

        if (remainingHighlightTicks > 0 && highlightingItemStack != null) {
            String s = highlightingItemStack.getDisplayName();

            if (highlightingItemStack.hasDisplayName()) {
                s = EnumChatFormatting.ITALIC + s;
            }

            int i = (p_181551_1_.width() - getFontRenderer().getStringWidth(s)) / 2;
            int j = p_181551_1_.height() - 59;

            if (!mc.playerController.shouldDrawHUD()) {
                j += 14;
            }

            int k = (int) ((float) remainingHighlightTicks * 256.0F / 10.0F);

            if (k > 255) {
                k = 255;
            }

            if (k > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                getFontRenderer().drawStringWithShadow(s, (float) i, (float) j, 16777215 + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }

        mc.mcProfiler.endSection();
    }

    public void renderDemo(ScaledResolution p_175185_1_) {
        mc.mcProfiler.startSection("demo");
        String s = "";

        if (mc.theWorld.getTotalWorldTime() >= 120500L) {
            s = I18n.format("demo.demoExpired", new Object[0]);
        } else {
            s = I18n.format("demo.remainingTime", new Object[]{StringUtils.ticksToElapsedTime((int) (120500L - mc.theWorld.getTotalWorldTime()))});
        }

        int i = getFontRenderer().getStringWidth(s);
        getFontRenderer().drawStringWithShadow(s, (float) (p_175185_1_.width() - i - 10), 5.0F, 16777215);
        mc.mcProfiler.endSection();
    }

    protected boolean showCrosshair() {
        if (mc.gameSettings.showDebugInfo && !mc.thePlayer.hasReducedDebug() && !mc.gameSettings.reducedDebugInfo) {
            return false;
        } else if (mc.playerController.isSpectator()) {
            if (mc.pointedEntity != null) {
                return true;
            } else {
                if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    BlockPos blockpos = mc.objectMouseOver.getBlockPos();

                    if (mc.theWorld.getTileEntity(blockpos) instanceof IInventory) {
                        return true;
                    }
                }

                return false;
            }
        } else {
            return true;
        }
    }

    public void renderStreamIndicator(ScaledResolution p_180478_1_) {
        streamIndicator.render(p_180478_1_.width() - 10, 10);
    }

    // TODO: NoScoreboard / Scoreboard Rendering Method
    private void renderScoreboard(ScoreObjective scoreObjective, ScaledResolution scaledResolution) {

        boolean customScoreboard = getSettingByName("Custom Scoreboard", getModuleManager().getModule(HUD.class)).isToggled() && getModuleManager().getModule(HUD.class).state();
        UnicodeFontRenderer scoreboardFont = getFontManager().font("Exo", 19, Font.PLAIN);

        if (!Client.main().modMgr().getByName("NoScoreboard").state()) {
            Scoreboard scoreboard = scoreObjective.getScoreboard();
            Collection collection = scoreboard.getSortedScores(scoreObjective);
            ArrayList arraylist = Lists.newArrayList(Iterables.filter(collection, new Predicate() {
                private static final String __OBFID = "CL_00001958";

                public boolean apply(Score p_apply_1_) {
                    return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
                }

                public boolean apply(Object p_apply_1_) {
                    return apply((Score) p_apply_1_);
                }
            }));
            ArrayList arraylist1;

            if (arraylist.size() > 15) {
                arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
            } else {
                arraylist1 = arraylist;
            }

            int i = getFontRenderer().getStringWidth(scoreObjective.getDisplayName());

            for (Object score : arraylist1) {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(((Score) score).getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, ((Score) score).getPlayerName()) + ": " + EnumChatFormatting.RED + ((Score) score).getScorePoints();
                if (customScoreboard) {
                    i = Math.max(i, scoreboardFont.getStringWidth(s));
                } else {
                    i = Math.max(i, getFontRenderer().getStringWidth(s));
                }
            }

            int j1 = arraylist1.size() * getFontRenderer().FONT_HEIGHT;

            int k1 = scaledResolution.height() / 2 + j1 / 3;
            byte b0 = 3;
            int j = scaledResolution.width() - i - b0;
            int k = 0;

            for (Object score1 : arraylist1) {
                ++k;
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(((Score) score1).getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, ((Score) score1).getPlayerName());
                String s2 = EnumChatFormatting.RED + "" + ((Score) score1).getScorePoints();
                int l = k1 - k * getFontRenderer().FONT_HEIGHT;

                int i1 = scaledResolution.width() - b0 + 2;
                if (customScoreboard) {
                    RenderUtils.drawRect(j - 2, l, i1, l + getFontRenderer().FONT_HEIGHT, 1342177280);
                    scoreboardFont.drawStringWithShadow(s1, j, l, -1);
                    scoreboardFont.drawStringWithShadow(s2, i1 - scoreboardFont.getStringWidth(s2), l, -1);
                } else {
                    drawRect(j - 2, l, i1, l + getFontRenderer().FONT_HEIGHT, 1342177280);
                    getFontRenderer().drawString(s1, j, l, 553648127);
                    getFontRenderer().drawString(s2, i1 - getFontRenderer().getStringWidth(s2), l, 553648127);
                }

                if (k == arraylist1.size()) {
                    String s3 = scoreObjective.getDisplayName();

                    if (customScoreboard) {
                        RenderUtils.drawRect(j - 2, l - getFontRenderer().FONT_HEIGHT - 1, i1, l - 1, 1610612736);
                        RenderUtils.drawRect(j - 2, l - 1, i1, l, 1342177280);
                        scoreboardFont.drawStringWithShadow(s3, j + i / 2 - scoreboardFont.getStringWidth(s3) / 2, l - getFontRenderer().FONT_HEIGHT, -1);
                    } else {
                        drawRect(j - 2, l - getFontRenderer().FONT_HEIGHT - 1, i1, l - 1, 1610612736);
                        drawRect(j - 2, l - 1, i1, l, 1342177280);
                        getFontRenderer().drawString(s3, j + i / 2 - getFontRenderer().getStringWidth(s3) / 2, l - getFontRenderer().FONT_HEIGHT, 553648127);
                    }
                }
            }
        }
    }

    private void renderPlayerStats(ScaledResolution p_180477_1_) {
        if (mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
            int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean flag = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

            if (i < playerHealth && entityplayer.hurtResistantTime > 0) {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = (long) (updateCounter + 20);
            } else if (i > playerHealth && entityplayer.hurtResistantTime > 0) {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = (long) (updateCounter + 10);
            }

            if (Minecraft.getSystemTime() - lastSystemTime > 1000L) {
                playerHealth = i;
                lastPlayerHealth = i;
                lastSystemTime = Minecraft.getSystemTime();
            }

            playerHealth = i;
            int j = lastPlayerHealth;
            rand.setSeed((long) (updateCounter * 312871));
            boolean flag1 = false;
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            int l = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = p_180477_1_.width() / 2 - 91;
            int j1 = p_180477_1_.width() / 2 + 91;
            int k1 = p_180477_1_.height() - 39;
            float f = (float) iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = k1 - (l1 - 1) * i2 - 10;
            float f2 = f1;
            int k2 = entityplayer.getTotalArmorValue();
            int l2 = -1;

            if (entityplayer.isPotionActive(Potion.regeneration)) {
                l2 = updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
            }

            mc.mcProfiler.startSection("armor");

            for (int i3 = 0; i3 < 10; ++i3) {
                if (k2 > 0) {
                    int j3 = i1 + i3 * 8;

                    if (i3 * 2 + 1 < k2) {
                        drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 == k2) {
                        drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 > k2) {
                        drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
                    }
                }
            }

            mc.mcProfiler.endStartSection("health");

            for (int j5 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; j5 >= 0; --j5) {
                int k5 = 16;

                if (entityplayer.isPotionActive(Potion.poison)) {
                    k5 += 36;
                } else if (entityplayer.isPotionActive(Potion.wither)) {
                    k5 += 72;
                }

                byte b0 = 0;

                if (flag) {
                    b0 = 1;
                }

                int k3 = MathHelper.ceiling_float_int((float) (j5 + 1) / 10.0F) - 1;
                int l3 = i1 + j5 % 10 * 8;
                int i4 = k1 - k3 * i2;

                if (i <= 4) {
                    i4 += rand.nextInt(2);
                }

                if (j5 == l2) {
                    i4 -= 2;
                }

                byte b1 = 0;

                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    b1 = 5;
                }

                drawTexturedModalRect(l3, i4, 16 + b0 * 9, 9 * b1, 9, 9);

                if (flag) {
                    if (j5 * 2 + 1 < j) {
                        drawTexturedModalRect(l3, i4, k5 + 54, 9 * b1, 9, 9);
                    }

                    if (j5 * 2 + 1 == j) {
                        drawTexturedModalRect(l3, i4, k5 + 63, 9 * b1, 9, 9);
                    }
                }

                if (f2 <= 0.0F) {
                    if (j5 * 2 + 1 < i) {
                        drawTexturedModalRect(l3, i4, k5 + 36, 9 * b1, 9, 9);
                    }

                    if (j5 * 2 + 1 == i) {
                        drawTexturedModalRect(l3, i4, k5 + 45, 9 * b1, 9, 9);
                    }
                } else {
                    if (f2 == f1 && f1 % 2.0F == 1.0F) {
                        drawTexturedModalRect(l3, i4, k5 + 153, 9 * b1, 9, 9);
                    } else {
                        drawTexturedModalRect(l3, i4, k5 + 144, 9 * b1, 9, 9);
                    }

                    f2 -= 2.0F;
                }
            }

            Entity entity = entityplayer.ridingEntity;

            if (entity == null) {
                mc.mcProfiler.endStartSection("food");

                for (int l5 = 0; l5 < 10; ++l5) {
                    int i8 = k1;
                    int j6 = 16;
                    byte b4 = 0;

                    if (entityplayer.isPotionActive(Potion.hunger)) {
                        j6 += 36;
                        b4 = 13;
                    }

                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (k * 3 + 1) == 0) {
                        i8 = k1 + (rand.nextInt(3) - 1);
                    }

                    if (flag1) {
                        b4 = 1;
                    }

                    int k7 = j1 - l5 * 8 - 9;
                    drawTexturedModalRect(k7, i8, 16 + b4 * 9, 27, 9, 9);

                    if (flag1) {
                        if (l5 * 2 + 1 < l) {
                            drawTexturedModalRect(k7, i8, j6 + 54, 27, 9, 9);
                        }

                        if (l5 * 2 + 1 == l) {
                            drawTexturedModalRect(k7, i8, j6 + 63, 27, 9, 9);
                        }
                    }

                    if (l5 * 2 + 1 < k) {
                        drawTexturedModalRect(k7, i8, j6 + 36, 27, 9, 9);
                    }

                    if (l5 * 2 + 1 == k) {
                        drawTexturedModalRect(k7, i8, j6 + 45, 27, 9, 9);
                    }
                }
            } else if (entity instanceof EntityLivingBase) {
                mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
                int l7 = (int) Math.ceil((double) entitylivingbase.getHealth());
                float f3 = entitylivingbase.getMaxHealth();
                int l6 = (int) (f3 + 0.5F) / 2;

                if (l6 > 30) {
                    l6 = 30;
                }

                int j7 = k1;

                for (int j4 = 0; l6 > 0; j4 += 20) {
                    int k4 = Math.min(l6, 10);
                    l6 -= k4;

                    for (int l4 = 0; l4 < k4; ++l4) {
                        byte b2 = 52;
                        byte b3 = 0;

                        if (flag1) {
                            b3 = 1;
                        }

                        int i5 = j1 - l4 * 8 - 9;
                        drawTexturedModalRect(i5, j7, b2 + b3 * 9, 9, 9, 9);

                        if (l4 * 2 + 1 + j4 < l7) {
                            drawTexturedModalRect(i5, j7, b2 + 36, 9, 9, 9);
                        }

                        if (l4 * 2 + 1 + j4 == l7) {
                            drawTexturedModalRect(i5, j7, b2 + 45, 9, 9, 9);
                        }
                    }

                    j7 -= 10;
                }
            }

            mc.mcProfiler.endStartSection("air");

            if (entityplayer.isInsideOfMaterial(Material.water)) {
                int i6 = mc.thePlayer.getAir();
                int j8 = MathHelper.ceiling_double_int((double) (i6 - 2) * 10.0D / 300.0D);
                int k6 = MathHelper.ceiling_double_int((double) i6 * 10.0D / 300.0D) - j8;

                for (int i7 = 0; i7 < j8 + k6; ++i7) {
                    if (i7 < j8) {
                        drawTexturedModalRect(j1 - i7 * 8 - 9, j2, 16, 18, 9, 9);
                    } else {
                        drawTexturedModalRect(j1 - i7 * 8 - 9, j2, 25, 18, 9, 9);
                    }
                }
            }

            mc.mcProfiler.endSection();
        }
    }

    /**
     * Renders dragon's (boss) health on the HUD
     */
    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            FontRenderer fontrenderer = mc.fontRendererObj;
            ScaledResolution scaledresolution = new ScaledResolution(mc);
            int i = scaledresolution.width();
            short short1 = 182;
            int j = i / 2 - short1 / 2;
            int k = (int) (BossStatus.healthScale * (float) (short1 + 1));
            byte b0 = 12;
            drawTexturedModalRect(j, b0, 0, 74, short1, 5);
            drawTexturedModalRect(j, b0, 0, 74, short1, 5);

            if (k > 0) {
                drawTexturedModalRect(j, b0, 0, 79, k, 5);
            }

            String s = BossStatus.bossName;
            int l = 16777215;

            if (Config.isCustomColors()) {
                l = CustomColors.getBossTextColor(l);
            }

            getFontRenderer().drawStringWithShadow(s, (float) (i / 2 - getFontRenderer().getStringWidth(s) / 2), (float) (b0 - 10), l);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(icons);
        }
    }

    private void renderPumpkinOverlay(ScaledResolution p_180476_1_) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, (double) p_180476_1_.height(), -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((double) p_180476_1_.width(), (double) p_180476_1_.height(), -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((double) p_180476_1_.width(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a Vignette arount the entire screen that changes with light level.
     */
    private void renderVignette(float p_180480_1_, ScaledResolution p_180480_2_) {
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        } else {
            p_180480_1_ = 1.0F - p_180480_1_;
            p_180480_1_ = MathHelper.clamp_float(p_180480_1_, 0.0F, 1.0F);
            WorldBorder worldborder = mc.theWorld.getWorldBorder();
            float f = (float) worldborder.getClosestDistance(mc.thePlayer);
            double d0 = Math.min(worldborder.getResizeSpeed() * (double) worldborder.getWarningTime() * 1000.0D, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
            double d1 = Math.max((double) worldborder.getWarningDistance(), d0);

            if ((double) f < d1) {
                f = 1.0F - (float) ((double) f / d1);
            } else {
                f = 0.0F;
            }

            prevVignetteBrightness = (float) ((double) prevVignetteBrightness + (double) (p_180480_1_ - prevVignetteBrightness) * 0.01D);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);

            if (f > 0.0F) {
                GlStateManager.color(0.0F, f, f, 1.0F);
            } else {
                GlStateManager.color(prevVignetteBrightness, prevVignetteBrightness, prevVignetteBrightness, 1.0F);
            }

            mc.getTextureManager().bindTexture(vignetteTexPath);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0D, (double) p_180480_2_.height(), -90.0D).tex(0.0D, 1.0D).endVertex();
            worldrenderer.pos((double) p_180480_2_.width(), (double) p_180480_2_.height(), -90.0D).tex(1.0D, 1.0D).endVertex();
            worldrenderer.pos((double) p_180480_2_.width(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
    }

    private void func_180474_b(float p_180474_1_, ScaledResolution p_180474_2_) {
        if (p_180474_1_ < 1.0F) {
            p_180474_1_ = p_180474_1_ * p_180474_1_;
            p_180474_1_ = p_180474_1_ * p_180474_1_;
            p_180474_1_ = p_180474_1_ * 0.8F + 0.2F;
        }

        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, p_180474_1_);
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite textureatlassprite = mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, (double) p_180474_2_.height(), -90.0D).tex((double) f, (double) f3).endVertex();
        worldrenderer.pos((double) p_180474_2_.width(), (double) p_180474_2_.height(), -90.0D).tex((double) f2, (double) f3).endVertex();
        worldrenderer.pos((double) p_180474_2_.width(), 0.0D, -90.0D).tex((double) f2, (double) f1).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex((double) f, (double) f1).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_) {
        ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];

        if (itemstack != null) {
            float f = (float) itemstack.animationsToGo - partialTicks;

            if (f > 0.0F) {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GlStateManager.translate((float) (xPos + 8), (float) (yPos + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(xPos + 8)), (float) (-(yPos + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);

            if (f > 0.0F) {
                GlStateManager.popMatrix();
            }

            itemRenderer.renderItemOverlays(mc.fontRendererObj, itemstack, xPos, yPos);
        }
    }

    /**
     * The update tick for the ingame UI
     */
    public void updateTick() {
        if (recordPlayingUpFor > 0) {
            --recordPlayingUpFor;
        }

        if (field_175195_w > 0) {
            --field_175195_w;

            if (field_175195_w <= 0) {
                field_175201_x = "";
                field_175200_y = "";
            }
        }

        ++updateCounter;
        streamIndicator.func_152439_a();

        if (mc.thePlayer != null) {
            ItemStack itemstack = mc.thePlayer.inventory.getCurrentItem();

            if (itemstack == null) {
                remainingHighlightTicks = 0;
            } else if (highlightingItemStack != null && itemstack.getItem() == highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == highlightingItemStack.getMetadata())) {
                if (remainingHighlightTicks > 0) {
                    --remainingHighlightTicks;
                }
            } else {
                remainingHighlightTicks = 40;
            }

            highlightingItemStack = itemstack;
        }
    }

    public void setRecordPlayingMessage(String p_73833_1_) {
        setRecordPlaying(I18n.format("record.nowPlaying", new Object[]{p_73833_1_}), true);
    }

    public void setRecordPlaying(String p_110326_1_, boolean p_110326_2_) {
        recordPlaying = p_110326_1_;
        recordPlayingUpFor = 60;
        recordIsPlaying = p_110326_2_;
    }

    public void displayTitle(String p_175178_1_, String p_175178_2_, int p_175178_3_, int p_175178_4_, int p_175178_5_) {
        if (p_175178_1_ == null && p_175178_2_ == null && p_175178_3_ < 0 && p_175178_4_ < 0 && p_175178_5_ < 0) {
            field_175201_x = "";
            field_175200_y = "";
            field_175195_w = 0;
        } else if (p_175178_1_ != null) {
            field_175201_x = p_175178_1_;
            field_175195_w = field_175199_z + field_175192_A + field_175193_B;
        } else if (p_175178_2_ != null) {
            field_175200_y = p_175178_2_;
        } else {
            if (p_175178_3_ >= 0) {
                field_175199_z = p_175178_3_;
            }

            if (p_175178_4_ >= 0) {
                field_175192_A = p_175178_4_;
            }

            if (p_175178_5_ >= 0) {
                field_175193_B = p_175178_5_;
            }

            if (field_175195_w > 0) {
                field_175195_w = field_175199_z + field_175192_A + field_175193_B;
            }
        }
    }

    public void setRecordPlaying(IChatComponent p_175188_1_, boolean p_175188_2_) {
        setRecordPlaying(p_175188_1_.getUnformattedText(), p_175188_2_);
    }

    /**
     * returns a pointer to the persistant Chat GUI, containing all previous chat messages and such
     */
    public GuiNewChat getChatGUI() {
        return persistantChatGUI;
    }

    public int getUpdateCounter() {
        return updateCounter;
    }

    public FontRenderer getFontRenderer() {
        return mc.fontRendererObj;
    }

    public GuiSpectator getSpectatorGui() {
        return spectatorGui;
    }

    public GuiPlayerTabOverlay getTabList() {
        return overlayPlayerList;
    }

    public void func_181029_i() {
        overlayPlayerList.func_181030_a();
    }
}
