// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Ordering;

public class GuiPlayerTabOverlay extends Gui
{
    public static final Ordering field_175252_a;
    private final Minecraft field_175250_f;
    private final GuiIngame field_175251_g;
    private IChatComponent footer;
    private IChatComponent header;
    private long field_175253_j;
    private boolean field_175254_k;
    private static final String __OBFID = "CL_00001943";
    
    public GuiPlayerTabOverlay(final Minecraft mcIn, final GuiIngame p_i45529_2_) {
        this.field_175250_f = mcIn;
        this.field_175251_g = p_i45529_2_;
    }
    
    public String func_175243_a(final NetworkPlayerInfo p_175243_1_) {
        return (p_175243_1_.getDisplayName() != null) ? p_175243_1_.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(p_175243_1_.getPlayerTeam(), p_175243_1_.getGameProfile().getName());
    }
    
    public void func_175246_a(final boolean p_175246_1_) {
        if (p_175246_1_ && !this.field_175254_k) {
            this.field_175253_j = Minecraft.getSystemTime();
        }
        this.field_175254_k = p_175246_1_;
    }
    
    public void func_175249_a(final int p_175249_1_, final Scoreboard p_175249_2_, final ScoreObjective p_175249_3_) {
        final NetHandlerPlayClient var4 = this.field_175250_f.thePlayer.sendQueue;
        List var5 = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)var4.func_175106_d());
        int var6 = 0;
        int var7 = 0;
        for (final NetworkPlayerInfo var9 : var5) {
            int var10 = this.field_175250_f.fontRendererObj.getStringWidth(this.func_175243_a(var9));
            var6 = Math.max(var6, var10);
            if (p_175249_3_ != null && p_175249_3_.func_178766_e() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                var10 = this.field_175250_f.fontRendererObj.getStringWidth(" " + p_175249_2_.getValueFromObjective(var9.getGameProfile().getName(), p_175249_3_).getScorePoints());
                var7 = Math.max(var7, var10);
            }
        }
        var5 = var5.subList(0, Math.min(var5.size(), 80));
        int var10;
        int var12;
        int var11;
        for (var11 = (var12 = var5.size()), var10 = 1; var12 > 20; var12 = (var11 + var10 - 1) / var10) {
            ++var10;
        }
        final boolean var13 = this.field_175250_f.isIntegratedServerRunning() || this.field_175250_f.getNetHandler().getNetworkManager().func_179292_f();
        int var14;
        if (p_175249_3_ != null) {
            if (p_175249_3_.func_178766_e() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                var14 = 90;
            }
            else {
                var14 = var7;
            }
        }
        else {
            var14 = 0;
        }
        final int var15 = Math.min(var10 * ((var13 ? 9 : 0) + var6 + var14 + 13), p_175249_1_ - 50) / var10;
        final int var16 = p_175249_1_ / 2 - (var15 * var10 + (var10 - 1) * 5) / 2;
        int var17 = 10;
        int var18 = var15 * var10 + (var10 - 1) * 5;
        List var19 = null;
        List var20 = null;
        if (this.header != null) {
            var19 = this.field_175250_f.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), p_175249_1_ - 50);
            for (final String var22 : var19) {
                var18 = Math.max(var18, this.field_175250_f.fontRendererObj.getStringWidth(var22));
            }
        }
        if (this.footer != null) {
            var20 = this.field_175250_f.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), p_175249_1_ - 50);
            for (final String var22 : var20) {
                var18 = Math.max(var18, this.field_175250_f.fontRendererObj.getStringWidth(var22));
            }
        }
        if (var19 != null) {
            Gui.drawRect(p_175249_1_ / 2 - var18 / 2 - 1, var17 - 1, p_175249_1_ / 2 + var18 / 2 + 1, var17 + var19.size() * this.field_175250_f.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            for (final String var22 : var19) {
                final int var23 = this.field_175250_f.fontRendererObj.getStringWidth(var22);
                this.field_175250_f.fontRendererObj.drawStringWithShadow(var22, p_175249_1_ / 2 - var23 / 2, var17, -1);
                var17 += this.field_175250_f.fontRendererObj.FONT_HEIGHT;
            }
            ++var17;
        }
        Gui.drawRect(p_175249_1_ / 2 - var18 / 2 - 1, var17 - 1, p_175249_1_ / 2 + var18 / 2 + 1, var17 + var12 * 9, Integer.MIN_VALUE);
        for (int var24 = 0; var24 < var11; ++var24) {
            final int var25 = var24 / var12;
            final int var23 = var24 % var12;
            int var26 = var16 + var25 * var15 + var25 * 5;
            final int var27 = var17 + var23 * 9;
            Gui.drawRect(var26, var27, var26 + var15, var27 + 8, 553648127);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            if (var24 < var5.size()) {
                final NetworkPlayerInfo var28 = var5.get(var24);
                String var29 = this.func_175243_a(var28);
                if (var13) {
                    this.field_175250_f.getTextureManager().bindTexture(var28.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect(var26, var27, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                    final EntityPlayer var30 = this.field_175250_f.theWorld.getPlayerEntityByUUID(var28.getGameProfile().getId());
                    if (var30 != null && var30.func_175148_a(EnumPlayerModelParts.HAT)) {
                        Gui.drawScaledCustomSizeModalRect(var26, var27, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                    }
                    var26 += 9;
                }
                if (var28.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    var29 = EnumChatFormatting.ITALIC + var29;
                    this.field_175250_f.fontRendererObj.drawStringWithShadow(var29, var26, var27, -1862270977);
                }
                else {
                    this.field_175250_f.fontRendererObj.drawStringWithShadow(var29, var26, var27, -1);
                }
                if (p_175249_3_ != null && var28.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    final int var31 = var26 + var6 + 1;
                    final int var32 = var31 + var14;
                    if (var32 - var31 > 5) {
                        this.func_175247_a(p_175249_3_, var27, var28.getGameProfile().getName(), var31, var32, var28);
                    }
                }
                this.func_175245_a(var15, var26 - (var13 ? 9 : 0), var27, var28);
            }
        }
        if (var20 != null) {
            var17 += var12 * 9 + 1;
            Gui.drawRect(p_175249_1_ / 2 - var18 / 2 - 1, var17 - 1, p_175249_1_ / 2 + var18 / 2 + 1, var17 + var20.size() * this.field_175250_f.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            for (final String var22 : var20) {
                final int var23 = this.field_175250_f.fontRendererObj.getStringWidth(var22);
                this.field_175250_f.fontRendererObj.drawStringWithShadow(var22, p_175249_1_ / 2 - var23 / 2, var17, -1);
                var17 += this.field_175250_f.fontRendererObj.FONT_HEIGHT;
            }
        }
    }
    
    protected void func_175245_a(final int p_175245_1_, final int p_175245_2_, final int p_175245_3_, final NetworkPlayerInfo p_175245_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.field_175250_f.getTextureManager().bindTexture(GuiPlayerTabOverlay.icons);
        final byte var5 = 0;
        final boolean var6 = false;
        byte var7;
        if (p_175245_4_.getResponseTime() < 0) {
            var7 = 5;
        }
        else if (p_175245_4_.getResponseTime() < 150) {
            var7 = 0;
        }
        else if (p_175245_4_.getResponseTime() < 300) {
            var7 = 1;
        }
        else if (p_175245_4_.getResponseTime() < 600) {
            var7 = 2;
        }
        else if (p_175245_4_.getResponseTime() < 1000) {
            var7 = 3;
        }
        else {
            var7 = 4;
        }
        this.zLevel += 100.0f;
        this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + var5 * 10, 176 + var7 * 8, 10, 8);
        this.zLevel -= 100.0f;
    }
    
    private void func_175247_a(final ScoreObjective p_175247_1_, final int p_175247_2_, final String p_175247_3_, final int p_175247_4_, final int p_175247_5_, final NetworkPlayerInfo p_175247_6_) {
        final int var7 = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();
        if (p_175247_1_.func_178766_e() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
            this.field_175250_f.getTextureManager().bindTexture(GuiPlayerTabOverlay.icons);
            if (this.field_175253_j == p_175247_6_.getRenderVisibilityId()) {
                if (var7 < p_175247_6_.getLastHealth()) {
                    p_175247_6_.getLastHealthTime(Minecraft.getSystemTime());
                    p_175247_6_.setHealthBlinkTime(this.field_175251_g.getUpdateCounter() + 20);
                }
                else if (var7 > p_175247_6_.getLastHealth()) {
                    p_175247_6_.getLastHealthTime(Minecraft.getSystemTime());
                    p_175247_6_.setHealthBlinkTime(this.field_175251_g.getUpdateCounter() + 10);
                }
            }
            if (Minecraft.getSystemTime() - p_175247_6_.getLastHealthTime() > 1000L || this.field_175253_j != p_175247_6_.getRenderVisibilityId()) {
                p_175247_6_.setLastHealth(var7);
                p_175247_6_.setDisplayHealth(var7);
                p_175247_6_.getLastHealthTime(Minecraft.getSystemTime());
            }
            p_175247_6_.setRenderVisibilityId(this.field_175253_j);
            p_175247_6_.setLastHealth(var7);
            final int var8 = MathHelper.ceiling_float_int(Math.max(var7, p_175247_6_.getDisplayHealth()) / 2.0f);
            final int var9 = Math.max(MathHelper.ceiling_float_int(var7 / 2), Math.max(MathHelper.ceiling_float_int(p_175247_6_.getDisplayHealth() / 2), 10));
            final boolean var10 = p_175247_6_.getHealthBlinkTime() > this.field_175251_g.getUpdateCounter() && (p_175247_6_.getHealthBlinkTime() - this.field_175251_g.getUpdateCounter()) / 3L % 2L == 1L;
            if (var8 > 0) {
                final float var11 = Math.min((p_175247_5_ - p_175247_4_ - 4) / var9, 9.0f);
                if (var11 > 3.0f) {
                    for (int var12 = var8; var12 < var9; ++var12) {
                        this.func_175174_a(p_175247_4_ + var12 * var11, p_175247_2_, var10 ? 25 : 16, 0, 9, 9);
                    }
                    for (int var12 = 0; var12 < var8; ++var12) {
                        this.func_175174_a(p_175247_4_ + var12 * var11, p_175247_2_, var10 ? 25 : 16, 0, 9, 9);
                        if (var10) {
                            if (var12 * 2 + 1 < p_175247_6_.getDisplayHealth()) {
                                this.func_175174_a(p_175247_4_ + var12 * var11, p_175247_2_, 70, 0, 9, 9);
                            }
                            if (var12 * 2 + 1 == p_175247_6_.getDisplayHealth()) {
                                this.func_175174_a(p_175247_4_ + var12 * var11, p_175247_2_, 79, 0, 9, 9);
                            }
                        }
                        if (var12 * 2 + 1 < var7) {
                            this.func_175174_a(p_175247_4_ + var12 * var11, p_175247_2_, (var12 >= 10) ? 160 : 52, 0, 9, 9);
                        }
                        if (var12 * 2 + 1 == var7) {
                            this.func_175174_a(p_175247_4_ + var12 * var11, p_175247_2_, (var12 >= 10) ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else {
                    final float var13 = MathHelper.clamp_float(var7 / 20.0f, 0.0f, 1.0f);
                    final int var14 = (int)((1.0f - var13) * 255.0f) << 16 | (int)(var13 * 255.0f) << 8;
                    String var15 = "" + var7 / 2.0f;
                    if (p_175247_5_ - this.field_175250_f.fontRendererObj.getStringWidth(var15 + "hp") >= p_175247_4_) {
                        var15 += "hp";
                    }
                    this.field_175250_f.fontRendererObj.drawStringWithShadow(var15, (p_175247_5_ + p_175247_4_) / 2 - this.field_175250_f.fontRendererObj.getStringWidth(var15) / 2, p_175247_2_, var14);
                }
            }
        }
        else {
            final String var16 = EnumChatFormatting.YELLOW + "" + var7;
            this.field_175250_f.fontRendererObj.drawStringWithShadow(var16, p_175247_5_ - this.field_175250_f.fontRendererObj.getStringWidth(var16), p_175247_2_, 16777215);
        }
    }
    
    public void setFooter(final IChatComponent p_175248_1_) {
        this.footer = p_175248_1_;
    }
    
    public void setHeader(final IChatComponent p_175244_1_) {
        this.header = p_175244_1_;
    }
    
    static {
        field_175252_a = Ordering.from((Comparator)new PlayerComparator(null));
    }
    
    static class PlayerComparator implements Comparator
    {
        private static final String __OBFID = "CL_00001941";
        
        private PlayerComparator() {
        }
        
        public int func_178952_a(final NetworkPlayerInfo p_178952_1_, final NetworkPlayerInfo p_178952_2_) {
            final ScorePlayerTeam var3 = p_178952_1_.getPlayerTeam();
            final ScorePlayerTeam var4 = p_178952_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_178952_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_178952_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare((Comparable)((var3 != null) ? var3.getRegisteredName() : ""), (Comparable)((var4 != null) ? var4.getRegisteredName() : "")).compare((Comparable)p_178952_1_.getGameProfile().getName(), (Comparable)p_178952_2_.getGameProfile().getName()).result();
        }
        
        @Override
        public int compare(final Object p_compare_1_, final Object p_compare_2_) {
            return this.func_178952_a((NetworkPlayerInfo)p_compare_1_, (NetworkPlayerInfo)p_compare_2_);
        }
        
        PlayerComparator(final Object p_i45528_1_) {
            this();
        }
    }
}
