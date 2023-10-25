/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.events.Event2D;
import me.Tengoku.Terror.ui.Notification.NotificationManager;
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

public class GuiIngame
extends Gui {
    private int lastPlayerHealth = 0;
    private final GuiNewChat persistantChatGUI;
    private final GuiSpectator spectatorGui;
    private final GuiStreamIndicator streamIndicator;
    private static final ResourceLocation widgetsTexPath;
    private final RenderItem itemRenderer;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private final GuiOverlayDebug overlayDebug;
    private int field_175193_B;
    private long healthUpdateCounter = 0L;
    private int remainingHighlightTicks;
    private static final ResourceLocation pumpkinBlurTexPath;
    private String recordPlaying = "";
    private ItemStack highlightingItemStack;
    private static final ResourceLocation vignetteTexPath;
    private int playerHealth = 0;
    private String field_175201_x = "";
    private int recordPlayingUpFor;
    private long lastSystemTime = 0L;
    private final Random rand = new Random();
    private int field_175192_A;
    private boolean recordIsPlaying;
    private int updateCounter;
    private String field_175200_y = "";
    private int field_175199_z;
    public float prevVignetteBrightness = 1.0f;
    private final Minecraft mc;
    private int field_175195_w;

    public void setRecordPlaying(String string, boolean bl) {
        this.recordPlaying = string;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = bl;
    }

    public void setRecordPlaying(IChatComponent iChatComponent, boolean bl) {
        this.setRecordPlaying(iChatComponent.getUnformattedText(), bl);
    }

    public void displayTitle(String string, String string2, int n, int n2, int n3) {
        if (string == null && string2 == null && n < 0 && n2 < 0 && n3 < 0) {
            this.field_175201_x = "";
            this.field_175200_y = "";
            this.field_175195_w = 0;
        } else if (string != null) {
            this.field_175201_x = string;
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
        } else if (string2 != null) {
            this.field_175200_y = string2;
        } else {
            if (n >= 0) {
                this.field_175199_z = n;
            }
            if (n2 >= 0) {
                this.field_175192_A = n2;
            }
            if (n3 >= 0) {
                this.field_175193_B = n3;
            }
            if (this.field_175195_w > 0) {
                this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
            }
        }
    }

    public void setRecordPlayingMessage(String string) {
        this.setRecordPlaying(I18n.format("record.nowPlaying", string), true);
    }

    private void renderScoreboard(ScoreObjective scoreObjective, ScaledResolution scaledResolution) {
        Scoreboard scoreboard = scoreObjective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(scoreObjective);
        ArrayList arrayList = Lists.newArrayList((Iterable)Iterables.filter(collection, (Predicate)new Predicate(){
            private static final String __OBFID = "CL_00001958";

            public boolean apply(Score score) {
                return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
            }

            public boolean apply(Object object) {
                return this.apply((Score)object);
            }
        }));
        ArrayList arrayList2 = arrayList.size() > 15 ? Lists.newArrayList((Iterable)Iterables.skip((Iterable)arrayList, (int)(collection.size() - 15))) : arrayList;
        int n = this.getFontRenderer().getStringWidth(scoreObjective.getDisplayName());
        for (Object e : arrayList2) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(((Score)e).getPlayerName());
            String string = String.valueOf(ScorePlayerTeam.formatPlayerName(scorePlayerTeam, ((Score)e).getPlayerName())) + ": " + (Object)((Object)EnumChatFormatting.RED) + ((Score)e).getScorePoints();
            n = Math.max(n, this.getFontRenderer().getStringWidth(string));
        }
        int n2 = (int)Exodus.INSTANCE.settingsManager.getSettingByName("Height").getValDouble();
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByName("Hide").getValBoolean();
        int n3 = arrayList2.size() * this.getFontRenderer().FONT_HEIGHT;
        int n4 = scaledResolution.getScaledHeight() / 2 + n3 / 1 + n2;
        if (bl) {
            n4 = scaledResolution.getScaledHeight() / 2 + n3 / 1 + 300;
        }
        int n5 = 3;
        int n6 = scaledResolution.getScaledWidth() - n - n5;
        int n7 = 0;
        for (Object e : arrayList2) {
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(((Score)e).getPlayerName());
            String string = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, ((Score)e).getPlayerName());
            String string2 = "" + (Object)((Object)EnumChatFormatting.RED) + ((Score)e).getScorePoints();
            int n8 = n4 - ++n7 * this.getFontRenderer().FONT_HEIGHT;
            int n9 = scaledResolution.getScaledWidth() - n5 + 2;
            if (Exodus.INSTANCE.settingsManager.getSettingByName("Draw Shadow").getValBoolean()) {
                if (Exodus.INSTANCE.settingsManager.getSettingByName("Transparent").getValBoolean()) {
                    if (Exodus.INSTANCE.settingsManager.getSettingByName("Hide Numbers").getValBoolean()) {
                        this.getFontRenderer().drawStringWithShadow(string, n6 + 8, n8, 0x20FFFFFF);
                    } else {
                        this.getFontRenderer().drawStringWithShadow(string, n6, n8, 0x20FFFFFF);
                        this.getFontRenderer().drawStringWithShadow(string2, n9 - this.getFontRenderer().getStringWidth(string2), n8, 0x20FFFFFF);
                    }
                } else if (Exodus.INSTANCE.settingsManager.getSettingByName("Hide Numbers").getValBoolean()) {
                    GuiIngame.drawRect(n6 + 5, n8, n9, n8 + this.getFontRenderer().FONT_HEIGHT, 0x50000000);
                    this.getFontRenderer().drawStringWithShadow(string, n6 + 8, n8, 0x20FFFFFF);
                } else {
                    GuiIngame.drawRect(n6 - 2, n8, n9, n8 + this.getFontRenderer().FONT_HEIGHT, 0x50000000);
                    this.getFontRenderer().drawStringWithShadow(string, n6, n8, 0x20FFFFFF);
                    this.getFontRenderer().drawStringWithShadow(string2, n9 - this.getFontRenderer().getStringWidth(string2), n8, 0x20FFFFFF);
                }
            } else if (Exodus.INSTANCE.settingsManager.getSettingByName("Transparent").getValBoolean()) {
                if (Exodus.INSTANCE.settingsManager.getSettingByName("Hide Numbers").getValBoolean()) {
                    this.getFontRenderer().drawString(string, n6 + 8, n8, 0x20FFFFFF);
                } else {
                    this.getFontRenderer().drawString(string, n6, n8, 0x20FFFFFF);
                    this.getFontRenderer().drawString(string2, n9 - this.getFontRenderer().getStringWidth(string2), n8, 0x20FFFFFF);
                }
            } else if (Exodus.INSTANCE.settingsManager.getSettingByName("Hide Numbers").getValBoolean()) {
                GuiIngame.drawRect(n6 + 5, n8, n9, n8 + this.getFontRenderer().FONT_HEIGHT, 0x50000000);
                this.getFontRenderer().drawString(string, n6 + 8, n8, 0x20FFFFFF);
            } else {
                GuiIngame.drawRect(n6 - 2, n8, n9, n8 + this.getFontRenderer().FONT_HEIGHT, 0x50000000);
                this.getFontRenderer().drawString(string, n6, n8, 0x20FFFFFF);
                this.getFontRenderer().drawString(string2, n9 - this.getFontRenderer().getStringWidth(string2), n8, 0x20FFFFFF);
            }
            if (n7 != arrayList2.size()) continue;
            String string3 = scoreObjective.getDisplayName();
            if (Exodus.INSTANCE.settingsManager.getSettingByName("Draw Shadow").getValBoolean()) {
                if (Exodus.INSTANCE.settingsManager.getSettingByName("Transparent").getValBoolean()) {
                    if (Exodus.INSTANCE.settingsManager.getSettingByName("Hide Numbers").getValBoolean()) {
                        this.getFontRenderer().drawStringWithShadow(string3, n6 + 52 - this.getFontRenderer().getStringWidth(string3) / 2, n8 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
                        continue;
                    }
                    this.getFontRenderer().drawStringWithShadow(string3, n6 + n / 2 - this.getFontRenderer().getStringWidth(string3) / 2, n8 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
                    continue;
                }
                if (Exodus.INSTANCE.settingsManager.getSettingByName("Hide Numbers").getValBoolean()) {
                    GuiIngame.drawRect(n6 + 5, n8 - this.getFontRenderer().FONT_HEIGHT - 1, n9, n8 - 1, 0x50000000);
                    GuiIngame.drawRect(n6 + 5, n8 - 1, n9, n8, 0x50000000);
                    this.getFontRenderer().drawStringWithShadow(string3, n6 + 52 - this.getFontRenderer().getStringWidth(string3) / 2, n8 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
                    continue;
                }
                GuiIngame.drawRect(n6 - 2, n8 - this.getFontRenderer().FONT_HEIGHT - 1, n9, n8 - 1, 0x50000000);
                GuiIngame.drawRect(n6 - 2, n8 - 1, n9, n8, 0x50000000);
                this.getFontRenderer().drawStringWithShadow(string3, n6 + n / 2 - this.getFontRenderer().getStringWidth(string3) / 2, n8 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
                continue;
            }
            if (Exodus.INSTANCE.settingsManager.getSettingByName("Transparent").getValBoolean()) {
                if (Exodus.INSTANCE.settingsManager.getSettingByName("Hide Numbers").getValBoolean()) {
                    this.getFontRenderer().drawString(string3, n6 + 52 - this.getFontRenderer().getStringWidth(string3) / 2, n8 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
                    continue;
                }
                this.getFontRenderer().drawString(string3, n6 + n / 2 - this.getFontRenderer().getStringWidth(string3) / 2, n8 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
                continue;
            }
            if (Exodus.INSTANCE.settingsManager.getSettingByName("Hide Numbers").getValBoolean()) {
                GuiIngame.drawRect(n6 + 5, n8 - this.getFontRenderer().FONT_HEIGHT - 1, n9, n8 - 1, 0x50000000);
                GuiIngame.drawRect(n6 + 5, n8 - 1, n9, n8, 0x50000000);
                this.getFontRenderer().drawString(string3, n6 + 52 - this.getFontRenderer().getStringWidth(string3) / 2, n8 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
                continue;
            }
            GuiIngame.drawRect(n6 - 2, n8 - this.getFontRenderer().FONT_HEIGHT - 1, n9, n8 - 1, 0x50000000);
            GuiIngame.drawRect(n6 - 2, n8 - 1, n9, n8, 0x50000000);
            this.getFontRenderer().drawString(string3, n6 + n / 2 - this.getFontRenderer().getStringWidth(string3) / 2, n8 - this.getFontRenderer().FONT_HEIGHT, 0x20FFFFFF);
        }
    }

    private void func_180474_b(float f, ScaledResolution scaledResolution) {
        if (f < 1.0f) {
            f *= f;
            f *= f;
            f = f * 0.8f + 0.2f;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, f);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite textureAtlasSprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        float f2 = textureAtlasSprite.getMinU();
        float f3 = textureAtlasSprite.getMinV();
        float f4 = textureAtlasSprite.getMaxU();
        float f5 = textureAtlasSprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(0.0, scaledResolution.getScaledHeight(), -90.0).tex(f2, f5).endVertex();
        worldRenderer.pos(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), -90.0).tex(f4, f5).endVertex();
        worldRenderer.pos(scaledResolution.getScaledWidth(), 0.0, -90.0).tex(f4, f3).endVertex();
        worldRenderer.pos(0.0, 0.0, -90.0).tex(f2, f3).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public GuiIngame(Minecraft minecraft) {
        this.mc = minecraft;
        this.itemRenderer = minecraft.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(minecraft);
        this.spectatorGui = new GuiSpectator(minecraft);
        this.persistantChatGUI = new GuiNewChat(minecraft);
        this.streamIndicator = new GuiStreamIndicator(minecraft);
        this.overlayPlayerList = new GuiPlayerTabOverlay(minecraft, this);
        this.func_175177_a();
    }

    protected boolean showCrosshair() {
        if (Minecraft.gameSettings.showDebugInfo && !Minecraft.thePlayer.hasReducedDebug() && !Minecraft.gameSettings.reducedDebugInfo) {
            return false;
        }
        if (Minecraft.playerController.isSpectator()) {
            BlockPos blockPos;
            if (this.mc.pointedEntity != null) {
                return true;
            }
            return this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && Minecraft.theWorld.getTileEntity(blockPos = this.mc.objectMouseOver.getBlockPos()) instanceof IInventory;
        }
        return true;
    }

    public void renderStreamIndicator(ScaledResolution scaledResolution) {
        this.streamIndicator.render(scaledResolution.getScaledWidth() - 10, 10);
    }

    public void func_181551_a(ScaledResolution scaledResolution) {
        this.mc.mcProfiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            int n;
            String string = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                string = (Object)((Object)EnumChatFormatting.ITALIC) + string;
            }
            int n2 = (scaledResolution.getScaledWidth() - this.getFontRenderer().getStringWidth(string)) / 2;
            int n3 = scaledResolution.getScaledHeight() - 59;
            if (!Minecraft.playerController.shouldDrawHUD()) {
                n3 += 14;
            }
            if ((n = (int)((float)this.remainingHighlightTicks * 256.0f / 10.0f)) > 255) {
                n = 255;
            }
            if (n > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.getFontRenderer().drawStringWithShadow(string, n2, n3, 0xFFFFFF + (n << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }

    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }

    public void func_181029_i() {
        this.overlayPlayerList.func_181030_a();
    }

    protected void renderTooltip(ScaledResolution scaledResolution, float f) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityPlayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int n = scaledResolution.getScaledWidth() / 2;
            float f2 = zLevel;
            zLevel = -90.0f;
            this.drawTexturedModalRect(n - 91, scaledResolution.getScaledHeight() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(n - 91 - 1 + entityPlayer.inventory.currentItem * 20, scaledResolution.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            zLevel = f2;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            int n2 = 0;
            while (n2 < 9) {
                int n3 = scaledResolution.getScaledWidth() / 2 - 90 + n2 * 20 + 2;
                int n4 = scaledResolution.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(n2, n3, n4, f, entityPlayer);
                ++n2;
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }

    public void renderHorseJumpBar(ScaledResolution scaledResolution, int n) {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        float f = Minecraft.thePlayer.getHorseJumpPower();
        int n2 = 182;
        int n3 = (int)(f * (float)(n2 + 1));
        int n4 = scaledResolution.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(n, n4, 0, 84, n2, 5);
        if (n3 > 0) {
            this.drawTexturedModalRect(n, n4, 0, 89, n3, 5);
        }
        this.mc.mcProfiler.endSection();
    }

    public void func_175177_a() {
        this.field_175199_z = 10;
        this.field_175192_A = 70;
        this.field_175193_B = 20;
    }

    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }

    static {
        vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
        widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
        pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    }

    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            FontRenderer fontRenderer = Minecraft.fontRendererObj;
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            int n = scaledResolution.getScaledWidth();
            int n2 = 182;
            int n3 = n / 2 - n2 / 2;
            int n4 = (int)(BossStatus.healthScale * (float)(n2 + 1));
            int n5 = 12;
            this.drawTexturedModalRect(n3, n5, 0, 74, n2, 5);
            this.drawTexturedModalRect(n3, n5, 0, 74, n2, 5);
            if (n4 > 0) {
                this.drawTexturedModalRect(n3, n5, 0, 79, n4, 5);
            }
            String string = BossStatus.bossName;
            this.getFontRenderer().drawStringWithShadow(string, n / 2 - this.getFontRenderer().getStringWidth(string) / 2, n5 - 10, 0xFFFFFF);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }

    private void renderPumpkinOverlay(ScaledResolution scaledResolution) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(0.0, scaledResolution.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldRenderer.pos(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldRenderer.pos(scaledResolution.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldRenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderDemo(ScaledResolution scaledResolution) {
        this.mc.mcProfiler.startSection("demo");
        String string = "";
        string = Minecraft.theWorld.getTotalWorldTime() >= 120500L ? I18n.format("demo.demoExpired", new Object[0]) : I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - Minecraft.theWorld.getTotalWorldTime())));
        int n = this.getFontRenderer().getStringWidth(string);
        this.getFontRenderer().drawStringWithShadow(string, scaledResolution.getScaledWidth() - n - 10, 5.0f, 0xFFFFFF);
        this.mc.mcProfiler.endSection();
    }

    public GuiSpectator getSpectatorGui() {
        return this.spectatorGui;
    }

    private void renderPlayerStats(ScaledResolution scaledResolution) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            int n;
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            boolean bl;
            EntityPlayer entityPlayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int n7 = MathHelper.ceiling_float_int(entityPlayer.getHealth());
            boolean bl2 = bl = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;
            if (n7 < this.playerHealth && entityPlayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            } else if (n7 > this.playerHealth && entityPlayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = n7;
                this.lastPlayerHealth = n7;
                this.lastSystemTime = Minecraft.getSystemTime();
            }
            this.playerHealth = n7;
            int n8 = this.lastPlayerHealth;
            this.rand.setSeed(this.updateCounter * 312871);
            boolean bl3 = false;
            FoodStats foodStats = entityPlayer.getFoodStats();
            int n9 = foodStats.getFoodLevel();
            int n10 = foodStats.getPrevFoodLevel();
            IAttributeInstance iAttributeInstance = entityPlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int n11 = scaledResolution.getScaledWidth() / 2 - 91;
            int n12 = scaledResolution.getScaledWidth() / 2 + 91;
            int n13 = scaledResolution.getScaledHeight() - 39;
            float f = (float)iAttributeInstance.getAttributeValue();
            float f2 = entityPlayer.getAbsorptionAmount();
            int n14 = MathHelper.ceiling_float_int((f + f2) / 2.0f / 10.0f);
            int n15 = Math.max(10 - (n14 - 2), 3);
            int n16 = n13 - (n14 - 1) * n15 - 10;
            float f3 = f2;
            int n17 = entityPlayer.getTotalArmorValue();
            int n18 = -1;
            if (entityPlayer.isPotionActive(Potion.regeneration)) {
                n18 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0f);
            }
            this.mc.mcProfiler.startSection("armor");
            int n19 = 0;
            while (n19 < 10) {
                if (n17 > 0) {
                    n6 = n11 + n19 * 8;
                    if (n19 * 2 + 1 < n17) {
                        this.drawTexturedModalRect(n6, n16, 34, 9, 9, 9);
                    }
                    if (n19 * 2 + 1 == n17) {
                        this.drawTexturedModalRect(n6, n16, 25, 9, 9, 9);
                    }
                    if (n19 * 2 + 1 > n17) {
                        this.drawTexturedModalRect(n6, n16, 16, 9, 9, 9);
                    }
                }
                ++n19;
            }
            this.mc.mcProfiler.endStartSection("health");
            n19 = MathHelper.ceiling_float_int((f + f2) / 2.0f) - 1;
            while (n19 >= 0) {
                n6 = 16;
                if (entityPlayer.isPotionActive(Potion.poison)) {
                    n6 += 36;
                } else if (entityPlayer.isPotionActive(Potion.wither)) {
                    n6 += 72;
                }
                n5 = 0;
                if (bl) {
                    n5 = 1;
                }
                n4 = MathHelper.ceiling_float_int((float)(n19 + 1) / 10.0f) - 1;
                n3 = n11 + n19 % 10 * 8;
                n2 = n13 - n4 * n15;
                if (n7 <= 4) {
                    n2 += this.rand.nextInt(2);
                }
                if (n19 == n18) {
                    n2 -= 2;
                }
                n = 0;
                if (entityPlayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    n = 5;
                }
                this.drawTexturedModalRect(n3, n2, 16 + n5 * 9, 9 * n, 9, 9);
                if (bl) {
                    if (n19 * 2 + 1 < n8) {
                        this.drawTexturedModalRect(n3, n2, n6 + 54, 9 * n, 9, 9);
                    }
                    if (n19 * 2 + 1 == n8) {
                        this.drawTexturedModalRect(n3, n2, n6 + 63, 9 * n, 9, 9);
                    }
                }
                if (f3 > 0.0f) {
                    if (f3 == f2 && f2 % 2.0f == 1.0f) {
                        this.drawTexturedModalRect(n3, n2, n6 + 153, 9 * n, 9, 9);
                    } else {
                        this.drawTexturedModalRect(n3, n2, n6 + 144, 9 * n, 9, 9);
                    }
                    f3 -= 2.0f;
                } else {
                    if (n19 * 2 + 1 < n7) {
                        this.drawTexturedModalRect(n3, n2, n6 + 36, 9 * n, 9, 9);
                    }
                    if (n19 * 2 + 1 == n7) {
                        this.drawTexturedModalRect(n3, n2, n6 + 45, 9 * n, 9, 9);
                    }
                }
                --n19;
            }
            Entity entity = entityPlayer.ridingEntity;
            if (entity == null) {
                this.mc.mcProfiler.endStartSection("food");
                n6 = 0;
                while (n6 < 10) {
                    n5 = n13;
                    n4 = 16;
                    n3 = 0;
                    if (entityPlayer.isPotionActive(Potion.hunger)) {
                        n4 += 36;
                        n3 = 13;
                    }
                    if (entityPlayer.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (n9 * 3 + 1) == 0) {
                        n5 = n13 + (this.rand.nextInt(3) - 1);
                    }
                    if (bl3) {
                        n3 = 1;
                    }
                    n2 = n12 - n6 * 8 - 9;
                    this.drawTexturedModalRect(n2, n5, 16 + n3 * 9, 27, 9, 9);
                    if (bl3) {
                        if (n6 * 2 + 1 < n10) {
                            this.drawTexturedModalRect(n2, n5, n4 + 54, 27, 9, 9);
                        }
                        if (n6 * 2 + 1 == n10) {
                            this.drawTexturedModalRect(n2, n5, n4 + 63, 27, 9, 9);
                        }
                    }
                    if (n6 * 2 + 1 < n9) {
                        this.drawTexturedModalRect(n2, n5, n4 + 36, 27, 9, 9);
                    }
                    if (n6 * 2 + 1 == n9) {
                        this.drawTexturedModalRect(n2, n5, n4 + 45, 27, 9, 9);
                    }
                    ++n6;
                }
            } else if (entity instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                n5 = (int)Math.ceil(entityLivingBase.getHealth());
                float f4 = entityLivingBase.getMaxHealth();
                n3 = (int)(f4 + 0.5f) / 2;
                if (n3 > 30) {
                    n3 = 30;
                }
                n2 = n13;
                n = 0;
                while (n3 > 0) {
                    int n20 = Math.min(n3, 10);
                    n3 -= n20;
                    int n21 = 0;
                    while (n21 < n20) {
                        int n22 = 52;
                        int n23 = 0;
                        if (bl3) {
                            n23 = 1;
                        }
                        int n24 = n12 - n21 * 8 - 9;
                        this.drawTexturedModalRect(n24, n2, n22 + n23 * 9, 9, 9, 9);
                        if (n21 * 2 + 1 + n < n5) {
                            this.drawTexturedModalRect(n24, n2, n22 + 36, 9, 9, 9);
                        }
                        if (n21 * 2 + 1 + n == n5) {
                            this.drawTexturedModalRect(n24, n2, n22 + 45, 9, 9, 9);
                        }
                        ++n21;
                    }
                    n2 -= 10;
                    n += 20;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (entityPlayer.isInsideOfMaterial(Material.water)) {
                int n25 = Minecraft.thePlayer.getAir();
                n5 = MathHelper.ceiling_double_int((double)(n25 - 2) * 10.0 / 300.0);
                int n26 = MathHelper.ceiling_double_int((double)n25 * 10.0 / 300.0) - n5;
                n3 = 0;
                while (n3 < n5 + n26) {
                    if (n3 < n5) {
                        this.drawTexturedModalRect(n12 - n3 * 8 - 9, n16, 16, 18, 9, 9);
                    } else {
                        this.drawTexturedModalRect(n12 - n3 * 8 - 9, n16, 25, 18, 9, 9);
                    }
                    ++n3;
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }

    public void renderExpBar(ScaledResolution scaledResolution, int n) {
        int n2;
        int n3;
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int n4 = Minecraft.thePlayer.xpBarCap();
        if (n4 > 0) {
            n3 = 182;
            int n5 = (int)(Minecraft.thePlayer.experience * (float)(n3 + 1));
            n2 = scaledResolution.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(n, n2, 0, 64, n3, 5);
            if (n5 > 0) {
                this.drawTexturedModalRect(n, n2, 0, 69, n5, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (Minecraft.thePlayer.experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            n3 = 8453920;
            String string = "" + Minecraft.thePlayer.experienceLevel;
            n2 = (scaledResolution.getScaledWidth() - this.getFontRenderer().getStringWidth(string)) / 2;
            int n6 = scaledResolution.getScaledHeight() - 31 - 4;
            boolean bl = false;
            this.getFontRenderer().drawString(string, n2 + 1, n6, 0);
            this.getFontRenderer().drawString(string, n2 - 1, n6, 0);
            this.getFontRenderer().drawString(string, n2, n6 + 1, 0);
            this.getFontRenderer().drawString(string, n2, n6 - 1, 0);
            this.getFontRenderer().drawString(string, n2, n6, n3);
            this.mc.mcProfiler.endSection();
        }
    }

    public void renderGameOverlay(float f) throws IOException {
        ScoreObjective scoreObjective;
        int n;
        int n2;
        float f2;
        int n3;
        float f3;
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int n4 = scaledResolution.getScaledWidth();
        int n5 = scaledResolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        if (Minecraft.isFancyGraphicsEnabled()) {
            this.renderVignette(Minecraft.thePlayer.getBrightness(f), scaledResolution);
        } else {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        ItemStack itemStack = Minecraft.thePlayer.inventory.armorItemInSlot(3);
        if (Minecraft.gameSettings.thirdPersonView == 0 && itemStack != null && itemStack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.renderPumpkinOverlay(scaledResolution);
        }
        if (!Minecraft.thePlayer.isPotionActive(Potion.confusion) && (f3 = Minecraft.thePlayer.prevTimeInPortal + (Minecraft.thePlayer.timeInPortal - Minecraft.thePlayer.prevTimeInPortal) * f) > 0.0f) {
            this.func_180474_b(f3, scaledResolution);
        }
        if (Minecraft.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledResolution, f);
        } else {
            this.renderTooltip(scaledResolution, f);
        }
        Event2D event2D = new Event2D(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        event2D.call();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(icons);
        GlStateManager.enableBlend();
        if (this.showCrosshair()) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(n4 / 2 - 7, n5 / 2 - 7, 0, 0, 16, 16);
        }
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();
        if (Minecraft.playerController.shouldDrawHUD()) {
            this.renderPlayerStats(scaledResolution);
        }
        GlStateManager.disableBlend();
        if (Minecraft.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            n3 = Minecraft.thePlayer.getSleepTimer();
            f2 = (float)n3 / 100.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f - (float)(n3 - 100) / 10.0f;
            }
            n2 = (int)(220.0f * f2) << 24 | 0x101020;
            GuiIngame.drawRect(0.0, 0.0, n4, n5, n2);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        n3 = n4 / 2 - 91;
        if (Minecraft.thePlayer.isRidingHorse()) {
            this.renderHorseJumpBar(scaledResolution, n3);
        } else if (Minecraft.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledResolution, n3);
        }
        if (Minecraft.gameSettings.heldItemTooltips && !Minecraft.playerController.isSpectator()) {
            this.func_181551_a(scaledResolution);
        } else if (Minecraft.thePlayer.isSpectator()) {
            this.spectatorGui.func_175263_a(scaledResolution);
        }
        if (this.mc.isDemo()) {
            this.renderDemo(scaledResolution);
        }
        if (Minecraft.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledResolution);
        }
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            f2 = (float)this.recordPlayingUpFor - f;
            n2 = (int)(f2 * 255.0f / 20.0f);
            if (n2 > 255) {
                n2 = 255;
            }
            if (n2 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(n4 / 2, n5 - 68, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int n6 = 0xFFFFFF;
                if (this.recordIsPlaying) {
                    n6 = MathHelper.func_181758_c(f2 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF;
                }
                this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4.0, n6 + (n2 << 24 & 0xFF000000));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.field_175195_w > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            f2 = (float)this.field_175195_w - f;
            n2 = 255;
            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                float f4 = (float)(this.field_175199_z + this.field_175192_A + this.field_175193_B) - f2;
                n2 = (int)(f4 * 255.0f / (float)this.field_175199_z);
            }
            if (this.field_175195_w <= this.field_175193_B) {
                n2 = (int)(f2 * 255.0f / (float)this.field_175193_B);
            }
            if ((n2 = MathHelper.clamp_int(n2, 0, 255)) > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(n4 / 2, n5 / 2, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                int n7 = n2 << 24 & 0xFF000000;
                this.getFontRenderer().drawString(this.field_175201_x, -this.getFontRenderer().getStringWidth(this.field_175201_x) / 2, -10.0f, 0xFFFFFF | n7, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                this.getFontRenderer().drawString(this.field_175200_y, -this.getFontRenderer().getStringWidth(this.field_175200_y) / 2, 5.0f, 0xFFFFFF | n7, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        Scoreboard scoreboard = Minecraft.theWorld.getScoreboard();
        ScoreObjective scoreObjective2 = null;
        ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(Minecraft.thePlayer.getName());
        if (scorePlayerTeam != null && (n = scorePlayerTeam.getChatFormat().getColorIndex()) >= 0) {
            scoreObjective2 = scoreboard.getObjectiveInDisplaySlot(3 + n);
        }
        ScoreObjective scoreObjective3 = scoreObjective = scoreObjective2 != null ? scoreObjective2 : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreObjective != null) {
            this.renderScoreboard(scoreObjective, scaledResolution);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, n5 - 48, 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreObjective = scoreboard.getObjectiveInDisplaySlot(0);
        if (!Minecraft.gameSettings.keyBindPlayerList.isKeyDown() || this.mc.isIntegratedServerRunning() && Minecraft.thePlayer.sendQueue.getPlayerInfoMap().size() <= 1 && scoreObjective == null) {
            this.overlayPlayerList.updatePlayerList(false);
        } else {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(n4, scoreboard, scoreObjective);
        }
        Exodus.INSTANCE.HUD.draw();
        NotificationManager.render();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
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
        if (Minecraft.thePlayer != null) {
            ItemStack itemStack = Minecraft.thePlayer.inventory.getCurrentItem();
            if (itemStack == null) {
                this.remainingHighlightTicks = 0;
            } else if (this.highlightingItemStack != null && itemStack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemStack, this.highlightingItemStack) && (itemStack.isItemStackDamageable() || itemStack.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            } else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = itemStack;
        }
    }

    private void renderHotbarItem(int n, int n2, int n3, float f, EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.mainInventory[n];
        if (itemStack != null) {
            float f2 = (float)itemStack.animationsToGo - f;
            if (f2 > 0.0f) {
                GlStateManager.pushMatrix();
                float f3 = 1.0f + f2 / 5.0f;
                GlStateManager.translate(n2 + 8, n3 + 12, 0.0f);
                GlStateManager.scale(1.0f / f3, (f3 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate(-(n2 + 8), -(n3 + 12), 0.0f);
            }
            this.itemRenderer.renderItemAndEffectIntoGUI(itemStack, n2, n3);
            if (f2 > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(Minecraft.fontRendererObj, itemStack, n2, n3);
        }
    }

    private void renderVignette(float f, ScaledResolution scaledResolution) {
        f = 1.0f - f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        WorldBorder worldBorder = Minecraft.theWorld.getWorldBorder();
        float f2 = (float)worldBorder.getClosestDistance(Minecraft.thePlayer);
        double d = Math.min(worldBorder.getResizeSpeed() * (double)worldBorder.getWarningTime() * 1000.0, Math.abs(worldBorder.getTargetSize() - worldBorder.getDiameter()));
        double d2 = Math.max((double)worldBorder.getWarningDistance(), d);
        f2 = (double)f2 < d2 ? 1.0f - (float)((double)f2 / d2) : 0.0f;
        this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(f - this.prevVignetteBrightness) * 0.01);
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
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(0.0, scaledResolution.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldRenderer.pos(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldRenderer.pos(scaledResolution.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldRenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    public FontRenderer getFontRenderer() {
        return Minecraft.fontRendererObj;
    }
}

