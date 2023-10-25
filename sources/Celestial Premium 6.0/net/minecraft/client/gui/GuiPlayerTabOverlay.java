/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Interpolator
 */
package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.animation.Interpolator;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.misc.StreamerMode;

public class GuiPlayerTabOverlay
extends Gui {
    public static final Ordering<NetworkPlayerInfo> ENTRY_ORDERING = Ordering.from(new PlayerComparator());
    private final Minecraft mc;
    private final GuiIngame guiIngame;
    private ITextComponent footer;
    private ITextComponent header;
    private long lastTimeOpened;
    private boolean isBeingRendered;
    public static float tabAnim = -100.0f;

    public GuiPlayerTabOverlay(Minecraft mcIn, GuiIngame guiIngameIn) {
        this.mc = mcIn;
        this.guiIngame = guiIngameIn;
    }

    public static List<EntityPlayer> getPlayers() {
        List<NetworkPlayerInfo> list = ENTRY_ORDERING.sortedCopy(Minecraft.getMinecraft().player.connection.getPlayerInfoMap());
        ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
        for (NetworkPlayerInfo player : list) {
            if (player == null) continue;
            players.add(Minecraft.getMinecraft().world.getPlayerEntityByName(player.getGameProfile().getName()));
        }
        return players;
    }

    public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }

    public void updatePlayerList(boolean willBeRendered) {
        if (willBeRendered && !this.isBeingRendered) {
            this.lastTimeOpened = Minecraft.getSystemTime();
        }
        this.isBeingRendered = willBeRendered;
        tabAnim = willBeRendered ? (float)Interpolator.LINEAR.interpolate((double)tabAnim, 0.0, (double)(7.0f / (float)Minecraft.getDebugFPS())) : -200.0f;
    }

    /*
     * WARNING - void declaration
     */
    public void renderPlayerlist(int width, Scoreboard scoreboardIn, @Nullable ScoreObjective scoreObjectiveIn) {
        void var19_27;
        boolean flag;
        int l3;
        GlStateManager.pushMatrix();
        GlStateManager.translate(1.0f, tabAnim, 1.0f);
        NetHandlerPlayClient nethandlerplayclient = this.mc.player.connection;
        List<NetworkPlayerInfo> list = ENTRY_ORDERING.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        int i = 0;
        int j = 0;
        for (NetworkPlayerInfo networkplayerinfo : list) {
            int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo));
            i = Math.max(i, k);
            if (scoreObjectiveIn == null || scoreObjectiveIn.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) continue;
            k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getOrCreateScore(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
            j = Math.max(j, k);
        }
        list = list.subList(0, Math.min(list.size(), 80));
        int i4 = l3 = list.size();
        int j4 = 1;
        while (i4 > 20) {
            i4 = (l3 + ++j4 - 1) / j4;
        }
        boolean bl = flag = this.mc.isIntegratedServerRunning() || this.mc.getConnection().getNetworkManager().isEncrypted();
        int l = scoreObjectiveIn != null ? (scoreObjectiveIn.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS ? 90 : j) : 0;
        int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
        int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
        int k1 = 10;
        int l1 = i1 * j4 + (j4 - 1) * 5;
        List<String> list1 = null;
        if (this.header != null) {
            list1 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);
            for (String string : list1) {
                l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(string));
            }
        }
        List<String> list2 = null;
        if (this.footer != null) {
            list2 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);
            for (String s1 : list2) {
                l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s1));
            }
        }
        if (list1 != null) {
            GuiPlayerTabOverlay.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            for (String s2 : list1) {
                int i2 = this.mc.fontRendererObj.getStringWidth(s2);
                this.mc.fontRendererObj.drawStringWithShadow(s2, width / 2 - i2 / 2, k1, -1);
                k1 += this.mc.fontRendererObj.FONT_HEIGHT;
            }
            ++k1;
        }
        GuiPlayerTabOverlay.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);
        boolean bl2 = false;
        while (++var19_27 < l3) {
            int k5;
            int l5;
            void l4 = var19_27 / i4;
            void i5 = var19_27 % i4;
            int j2 = j1 + l4 * i1 + l4 * 5;
            int k2 = k1 + i5 * 9;
            GuiPlayerTabOverlay.drawRect(j2, k2, j2 + i1, k2 + 8, 0x20FFFFFF);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (var19_27 >= list.size()) continue;
            NetworkPlayerInfo networkplayerinfo1 = list.get((int)var19_27);
            GameProfile gameprofile = networkplayerinfo1.getGameProfile();
            if (flag) {
                EntityPlayer entityplayer = this.mc.world.getPlayerEntityByUUID(gameprofile.getId());
                boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && ("Dinnerbone".equals(gameprofile.getName()) || "Grumm".equals(gameprofile.getName()));
                this.mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
                int l2 = 8 + (flag1 ? 8 : 0);
                int i3 = 8 * (flag1 ? -1 : 1);
                Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0f, l2, 8.0f, i3, 8.0f, 8.0f, 64.0f, 64.0f);
                if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                    int j3 = 8 + (flag1 ? 8 : 0);
                    int k3 = 8 * (flag1 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0f, j3, 8.0f, k3, 8.0f, 8.0f, 64.0f, 64.0f);
                }
                j2 += 9;
            }
            String s4 = this.getPlayerName(networkplayerinfo1);
            if (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState()) {
                if (StreamerMode.tabSpoof.getCurrentValue() && !s4.contains(this.mc.player.getName())) {
                    s4 = "\u00a7aProtected";
                }
                if (StreamerMode.ownName.getCurrentValue()) {
                    s4 = s4.replace(this.mc.player.getName(), (Object)((Object)TextFormatting.GREEN) + "Protected");
                }
            }
            if (networkplayerinfo1.getGameType() == GameType.SPECTATOR) {
                this.mc.fontRendererObj.drawStringWithShadow((Object)((Object)TextFormatting.ITALIC) + s4, j2, k2, -1862270977);
            } else {
                this.mc.fontRendererObj.drawStringWithShadow(s4, j2, k2, -1);
            }
            if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != GameType.SPECTATOR && (l5 = (k5 = j2 + i + 1) + l) - k5 > 5) {
                this.drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
            }
            this.drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
        }
        if (list2 != null) {
            k1 = k1 + i4 * 9 + 1;
            GuiPlayerTabOverlay.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            for (String s3 : list2) {
                int j5 = this.mc.fontRendererObj.getStringWidth(s3);
                this.mc.fontRendererObj.drawStringWithShadow(s3, width / 2 - j5 / 2, k1, -1);
                k1 += this.mc.fontRendererObj.FONT_HEIGHT;
            }
        }
        GlStateManager.popMatrix();
    }

    protected void drawPing(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo networkPlayerInfoIn) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ICONS);
        int j = 0;
        j = networkPlayerInfoIn.getResponseTime() < 0 ? 5 : (networkPlayerInfoIn.getResponseTime() < 150 ? 0 : (networkPlayerInfoIn.getResponseTime() < 300 ? 1 : (networkPlayerInfoIn.getResponseTime() < 600 ? 2 : (networkPlayerInfoIn.getResponseTime() < 1000 ? 3 : 4))));
        this.zLevel += 100.0f;
        this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0, 176 + j * 8, 10, 8);
        this.zLevel -= 100.0f;
    }

    private void drawScoreboardValues(ScoreObjective objective, int p_175247_2_, String name, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo info) {
        int i = objective.getScoreboard().getOrCreateScore(name, objective).getScorePoints();
        if (objective.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
            boolean flag;
            this.mc.getTextureManager().bindTexture(ICONS);
            if (this.lastTimeOpened == info.getRenderVisibilityId()) {
                if (i < info.getLastHealth()) {
                    info.setLastHealthTime(Minecraft.getSystemTime());
                    info.setHealthBlinkTime(this.guiIngame.getUpdateCounter() + 20);
                } else if (i > info.getLastHealth()) {
                    info.setLastHealthTime(Minecraft.getSystemTime());
                    info.setHealthBlinkTime(this.guiIngame.getUpdateCounter() + 10);
                }
            }
            if (Minecraft.getSystemTime() - info.getLastHealthTime() > 1000L || this.lastTimeOpened != info.getRenderVisibilityId()) {
                info.setLastHealth(i);
                info.setDisplayHealth(i);
                info.setLastHealthTime(Minecraft.getSystemTime());
            }
            info.setRenderVisibilityId(this.lastTimeOpened);
            info.setLastHealth(i);
            int j = MathHelper.ceil((float)Math.max(i, info.getDisplayHealth()) / 2.0f);
            int k = Math.max(MathHelper.ceil(i / 2), Math.max(MathHelper.ceil(info.getDisplayHealth() / 2), 10));
            boolean bl = flag = info.getHealthBlinkTime() > (long)this.guiIngame.getUpdateCounter() && (info.getHealthBlinkTime() - (long)this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;
            if (j > 0) {
                float f = Math.min((float)(p_175247_5_ - p_175247_4_ - 4) / (float)k, 9.0f);
                if (f > 3.0f) {
                    for (int l = j; l < k; ++l) {
                        this.drawTexturedModalRect((float)p_175247_4_ + (float)l * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }
                    for (int j1 = 0; j1 < j; ++j1) {
                        this.drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                        if (flag) {
                            if (j1 * 2 + 1 < info.getDisplayHealth()) {
                                this.drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, 70, 0, 9, 9);
                            }
                            if (j1 * 2 + 1 == info.getDisplayHealth()) {
                                this.drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, 79, 0, 9, 9);
                            }
                        }
                        if (j1 * 2 + 1 < i) {
                            this.drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, j1 >= 10 ? 160 : 52, 0, 9, 9);
                        }
                        if (j1 * 2 + 1 != i) continue;
                        this.drawTexturedModalRect((float)p_175247_4_ + (float)j1 * f, (float)p_175247_2_, j1 >= 10 ? 169 : 61, 0, 9, 9);
                    }
                } else {
                    float f1 = MathHelper.clamp((float)i / 20.0f, 0.0f, 1.0f);
                    int i1 = (int)((1.0f - f1) * 255.0f) << 16 | (int)(f1 * 255.0f) << 8;
                    String s = "" + (float)i / 2.0f;
                    if (p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s + "hp") >= p_175247_4_) {
                        s = s + "hp";
                    }
                    this.mc.fontRendererObj.drawStringWithShadow(s, (p_175247_5_ + p_175247_4_) / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, p_175247_2_, i1);
                }
            }
        } else {
            String s1 = (Object)((Object)TextFormatting.YELLOW) + "" + i;
            this.mc.fontRendererObj.drawStringWithShadow(s1, p_175247_5_ - this.mc.fontRendererObj.getStringWidth(s1), p_175247_2_, 0xFFFFFF);
        }
    }

    public void setFooter(@Nullable ITextComponent footerIn) {
        this.footer = footerIn;
    }

    public void setHeader(@Nullable ITextComponent headerIn) {
        this.header = headerIn;
    }

    public void resetFooterHeader() {
        this.header = null;
        this.footer = null;
    }

    static class PlayerComparator
    implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        @Override
        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
            ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != GameType.SPECTATOR, p_compare_2_.getGameType() != GameType.SPECTATOR).compare((Comparable<?>)((Object)(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "")), (Comparable<?>)((Object)(scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : ""))).compare((Comparable<?>)((Object)p_compare_1_.getGameProfile().getName()), (Comparable<?>)((Object)p_compare_2_.getGameProfile().getName())).result();
        }
    }
}

