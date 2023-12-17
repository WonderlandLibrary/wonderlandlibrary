// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import ru.tuskevich.util.font.Fonts;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.minecraft.scoreboard.IScoreCriteria;
import javax.annotation.Nullable;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.google.common.collect.Ordering;

public class GuiPlayerTabOverlay extends Gui
{
    public static final Ordering<NetworkPlayerInfo> ENTRY_ORDERING;
    private final Minecraft mc;
    private final GuiIngame guiIngame;
    private ITextComponent footer;
    private ITextComponent header;
    public float height;
    private long lastTimeOpened;
    private boolean isBeingRendered;
    
    public GuiPlayerTabOverlay(final Minecraft mcIn, final GuiIngame guiIngameIn) {
        this.height = 0.0f;
        this.mc = mcIn;
        this.guiIngame = guiIngameIn;
    }
    
    public String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        return (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }
    
    public void updatePlayerList(final boolean willBeRendered) {
        if (willBeRendered && !this.isBeingRendered) {
            this.lastTimeOpened = Minecraft.getSystemTime();
        }
        this.isBeingRendered = willBeRendered;
    }
    
    public void renderPlayerlist(final int width, final Scoreboard scoreboardIn, @Nullable final ScoreObjective scoreObjectiveIn) {
        final Minecraft mc = this.mc;
        final NetHandlerPlayClient nethandlerplayclient = Minecraft.player.connection;
        List<NetworkPlayerInfo> list = (List<NetworkPlayerInfo>)GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy((Iterable)nethandlerplayclient.getPlayerInfoMap());
        int i = 0;
        int j = 0;
        for (final NetworkPlayerInfo networkplayerinfo : list) {
            int k = this.mc.fontRenderer.getStringWidth(this.getPlayerName(networkplayerinfo));
            i = Math.max(i, k);
            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreCriteria.EnumRenderType.HEARTS) {
                k = this.mc.fontRenderer.getStringWidth(" " + scoreboardIn.getOrCreateScore(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }
        list = list.subList(0, Math.min(list.size(), 80));
        int i2;
        int l3;
        int j2;
        for (l3 = (i2 = list.size()), j2 = 1; i2 > 20; i2 = (l3 + j2 - 1) / j2) {
            ++j2;
        }
        final boolean flag = this.mc.isIntegratedServerRunning() || this.mc.getConnection().getNetworkManager().isEncrypted();
        int m;
        if (scoreObjectiveIn != null) {
            if (scoreObjectiveIn.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
                m = 90;
            }
            else {
                m = j;
            }
        }
        else {
            m = 0;
        }
        final int i3 = Math.min(j2 * ((flag ? 9 : 0) + i + m + 13), width - 50) / j2;
        final int j3 = width / 2 - (i3 * j2 + (j2 - 1) * 5) / 2;
        int k2 = 10;
        int l4 = i3 * j2 + (j2 - 1) * 5;
        List<String> list2 = null;
        if (this.header != null) {
            list2 = this.mc.fontRenderer.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);
            for (final String s : list2) {
                l4 = Math.max(l4, this.mc.fontRenderer.getStringWidth(s));
            }
        }
        List<String> list3 = null;
        if (this.footer != null) {
            list3 = this.mc.fontRenderer.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);
            for (final String s2 : list3) {
                l4 = Math.max(l4, this.mc.fontRenderer.getStringWidth(s2));
            }
        }
        if (list2 != null) {
            Gui.drawRect((float)(width / 2 - l4 / 2 - 1), (float)(k2 - 1), (float)(width / 2 + l4 / 2 + 1), (float)(k2 + list2.size() * this.mc.fontRenderer.FONT_HEIGHT), Integer.MIN_VALUE);
            for (final String s3 : list2) {
                final int i4 = this.mc.fontRenderer.getStringWidth(s3);
                this.mc.fontRenderer.drawStringWithShadow(s3, (float)(width / 2 - i4 / 2), (float)k2, -1);
                k2 += this.mc.fontRenderer.FONT_HEIGHT;
            }
            ++k2;
        }
        Gui.drawRect((float)(width / 2 - l4 / 2 - 1), (float)(k2 - 1), (float)(width / 2 + l4 / 2 + 1), (float)(k2 + i2 * 9), Integer.MIN_VALUE);
        for (int k3 = 0; k3 < l3; ++k3) {
            final int l5 = k3 / i2;
            final int i5 = k3 % i2;
            int j4 = j3 + l5 * i3 + l5 * 5;
            final int k4 = k2 + i5 * 9;
            final float left = (float)j4;
            final float top = (float)k4;
            final float right = (float)(j4 + i3);
            final float bottom = (float)(k4 + 8);
            final Minecraft mc2 = this.mc;
            Gui.drawRect(left, top, right, bottom, (Minecraft.player == this.mc.world.getPlayerEntityByUUID(list.get(k3).getGameProfile().getId())) ? new Color(0, 157, 255, 128).getRGB() : 553648127);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (k3 < list.size()) {
                final NetworkPlayerInfo networkplayerinfo2 = list.get(k3);
                final GameProfile gameprofile = networkplayerinfo2.getGameProfile();
                if (flag) {
                    final EntityPlayer entityplayer = this.mc.world.getPlayerEntityByUUID(gameprofile.getId());
                    final boolean flag2 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && ("Dinnerbone".equals(gameprofile.getName()) || "Grumm".equals(gameprofile.getName()));
                    this.mc.getTextureManager().bindTexture(networkplayerinfo2.getLocationSkin());
                    final int l6 = 8 + (flag2 ? 8 : 0);
                    final int i6 = 8 * (flag2 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect((float)j4, (float)k4, 8.0f, (float)l6, 8, i6, 8, 8, 64.0f, 64.0f);
                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                        final int j5 = 8 + (flag2 ? 8 : 0);
                        final int k5 = 8 * (flag2 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect((float)j4, (float)k4, 40.0f, (float)j5, 8, k5, 8, 8, 64.0f, 64.0f);
                    }
                    j4 += 9;
                }
                final String s4 = this.getPlayerName(networkplayerinfo2);
                if (networkplayerinfo2.getGameType() == GameType.SPECTATOR) {
                    this.mc.fontRenderer.drawStringWithShadow(TextFormatting.ITALIC + s4, j4 + 1.0f, (float)k4, -1862270977);
                }
                else {
                    this.mc.fontRenderer.drawStringWithShadow(s4, j4 + 1.0f, (float)k4, -1);
                }
                if (scoreObjectiveIn != null && networkplayerinfo2.getGameType() != GameType.SPECTATOR) {
                    final int k6 = j4 + i + 1;
                    final int l7 = k6 + m;
                    if (l7 - k6 > 5) {
                        this.drawScoreboardValues(scoreObjectiveIn, k4, gameprofile.getName(), k6, l7, networkplayerinfo2);
                    }
                }
                this.drawPing(i3, j4 - (flag ? 9 : 0), k4, networkplayerinfo2);
            }
        }
        if (list3 != null) {
            k2 = k2 + i2 * 9 + 1;
            Gui.drawRect((float)(width / 2 - l4 / 2 - 1), (float)(k2 - 1), (float)(width / 2 + l4 / 2 + 1), (float)(k2 + list3.size() * this.mc.fontRenderer.FONT_HEIGHT), Integer.MIN_VALUE);
            for (final String s5 : list3) {
                final int j6 = this.mc.fontRenderer.getStringWidth(s5);
                this.mc.fontRenderer.drawStringWithShadow(s5, (float)(width / 2 - j6 / 2), (float)k2, -1);
                k2 += this.mc.fontRenderer.FONT_HEIGHT;
            }
        }
        this.height = (float)k2;
    }
    
    protected void drawPing(final int p_175245_1_, final int p_175245_2_, final int p_175245_3_, final NetworkPlayerInfo networkPlayerInfoIn) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int clr = -1;
        if (networkPlayerInfoIn.getResponseTime() >= 0) {
            clr = new Color(59, 234, 146).getRGB();
        }
        if (networkPlayerInfoIn.getResponseTime() > 200) {
            clr = new Color(248, 227, 76).getRGB();
        }
        if (networkPlayerInfoIn.getResponseTime() > 500) {
            clr = new Color(239, 158, 67).getRGB();
        }
        if (networkPlayerInfoIn.getResponseTime() > 800) {
            clr = new Color(255, 75, 75).getRGB();
        }
        this.zLevel += 100.0f;
        final String p = String.valueOf(MathHelper.clamp(networkPlayerInfoIn.getResponseTime(), 0, 999));
        Fonts.MCR8.drawStringWithShadow(p, (float)(p_175245_2_ + p_175245_1_ - 2 - Fonts.MCR8.getStringWidth(p)), p_175245_3_ + 5.5f, clr);
        this.zLevel -= 100.0f;
    }
    
    private void drawScoreboardValues(final ScoreObjective objective, final int p_175247_2_, final String name, final int p_175247_4_, final int p_175247_5_, final NetworkPlayerInfo info) {
        final int i = objective.getScoreboard().getOrCreateScore(name, objective).getScorePoints();
        if (objective.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
            this.mc.getTextureManager().bindTexture(GuiPlayerTabOverlay.ICONS);
            if (this.lastTimeOpened == info.getRenderVisibilityId()) {
                if (i < info.getLastHealth()) {
                    info.setLastHealthTime(Minecraft.getSystemTime());
                    info.setHealthBlinkTime(this.guiIngame.getUpdateCounter() + 20);
                }
                else if (i > info.getLastHealth()) {
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
            final int j = MathHelper.ceil(Math.max(i, info.getDisplayHealth()) / 2.0f);
            final int k = Math.max(MathHelper.ceil((float)(i / 2)), Math.max(MathHelper.ceil((float)(info.getDisplayHealth() / 2)), 10));
            final boolean flag = info.getHealthBlinkTime() > this.guiIngame.getUpdateCounter() && (info.getHealthBlinkTime() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;
            if (j > 0) {
                final float f = Math.min((p_175247_5_ - p_175247_4_ - 4) / (float)k, 9.0f);
                if (f > 3.0f) {
                    for (int l = j; l < k; ++l) {
                        this.drawTexturedModalRect(p_175247_4_ + l * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }
                    for (int j2 = 0; j2 < j; ++j2) {
                        this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                        if (flag) {
                            if (j2 * 2 + 1 < info.getDisplayHealth()) {
                                this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, 70, 0, 9, 9);
                            }
                            if (j2 * 2 + 1 == info.getDisplayHealth()) {
                                this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, 79, 0, 9, 9);
                            }
                        }
                        if (j2 * 2 + 1 < i) {
                            this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, (j2 >= 10) ? 160 : 52, 0, 9, 9);
                        }
                        if (j2 * 2 + 1 == i) {
                            this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, (j2 >= 10) ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else {
                    final float f2 = MathHelper.clamp(i / 20.0f, 0.0f, 1.0f);
                    final int i2 = (int)((1.0f - f2) * 255.0f) << 16 | (int)(f2 * 255.0f) << 8;
                    String s = "" + i / 2.0f;
                    if (p_175247_5_ - this.mc.fontRenderer.getStringWidth(s + "hp") >= p_175247_4_) {
                        s += "hp";
                    }
                    this.mc.fontRenderer.drawStringWithShadow(s, (float)((p_175247_5_ + p_175247_4_) / 2 - this.mc.fontRenderer.getStringWidth(s) / 2), (float)p_175247_2_, i2);
                }
            }
        }
        else {
            final String s2 = TextFormatting.YELLOW + "" + i;
            this.mc.fontRenderer.drawStringWithShadow(s2, (float)(p_175247_5_ - this.mc.fontRenderer.getStringWidth(s2)), (float)p_175247_2_, 16777215);
        }
    }
    
    public void setFooter(@Nullable final ITextComponent footerIn) {
        this.footer = footerIn;
    }
    
    public void setHeader(@Nullable final ITextComponent headerIn) {
        this.header = headerIn;
    }
    
    public void resetFooterHeader() {
        this.header = null;
        this.footer = null;
    }
    
    static {
        ENTRY_ORDERING = Ordering.from((Comparator)new PlayerComparator());
    }
    
    static class PlayerComparator implements Comparator<NetworkPlayerInfo>
    {
        private PlayerComparator() {
        }
        
        @Override
        public int compare(final NetworkPlayerInfo p_compare_1_, final NetworkPlayerInfo p_compare_2_) {
            final ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            final ScorePlayerTeam scoreplayerteam2 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != GameType.SPECTATOR, p_compare_2_.getGameType() != GameType.SPECTATOR).compare((Comparable)((scoreplayerteam != null) ? scoreplayerteam.getName() : ""), (Comparable)((scoreplayerteam2 != null) ? scoreplayerteam2.getName() : "")).compare((Comparable)p_compare_1_.getGameProfile().getName(), (Comparable)p_compare_2_.getGameProfile().getName()).result();
        }
    }
}
