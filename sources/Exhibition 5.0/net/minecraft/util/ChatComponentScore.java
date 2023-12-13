// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;

public class ChatComponentScore extends ChatComponentStyle
{
    private final String field_179999_b;
    private final String field_180000_c;
    private String field_179998_d;
    private static final String __OBFID = "CL_00002309";
    
    public ChatComponentScore(final String p_i45997_1_, final String p_i45997_2_) {
        this.field_179998_d = "";
        this.field_179999_b = p_i45997_1_;
        this.field_180000_c = p_i45997_2_;
    }
    
    public String func_179995_g() {
        return this.field_179999_b;
    }
    
    public String func_179994_h() {
        return this.field_180000_c;
    }
    
    public void func_179997_b(final String p_179997_1_) {
        this.field_179998_d = p_179997_1_;
    }
    
    @Override
    public String getUnformattedTextForChat() {
        final MinecraftServer var1 = MinecraftServer.getServer();
        if (var1 != null && var1.func_175578_N() && StringUtils.isNullOrEmpty(this.field_179998_d)) {
            final Scoreboard var2 = var1.worldServerForDimension(0).getScoreboard();
            final ScoreObjective var3 = var2.getObjective(this.field_180000_c);
            if (var2.func_178819_b(this.field_179999_b, var3)) {
                final Score var4 = var2.getValueFromObjective(this.field_179999_b, var3);
                this.func_179997_b(String.format("%d", var4.getScorePoints()));
            }
            else {
                this.field_179998_d = "";
            }
        }
        return this.field_179998_d;
    }
    
    public ChatComponentScore func_179996_i() {
        final ChatComponentScore var1 = new ChatComponentScore(this.field_179999_b, this.field_180000_c);
        var1.func_179997_b(this.field_179998_d);
        var1.setChatStyle(this.getChatStyle().createShallowCopy());
        for (final IChatComponent var3 : this.getSiblings()) {
            var1.appendSibling(var3.createCopy());
        }
        return var1;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentScore)) {
            return false;
        }
        final ChatComponentScore var2 = (ChatComponentScore)p_equals_1_;
        return this.field_179999_b.equals(var2.field_179999_b) && this.field_180000_c.equals(var2.field_180000_c) && super.equals(p_equals_1_);
    }
    
    @Override
    public String toString() {
        return "ScoreComponent{name='" + this.field_179999_b + '\'' + "objective='" + this.field_180000_c + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
    
    @Override
    public IChatComponent createCopy() {
        return this.func_179996_i();
    }
}
