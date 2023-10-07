// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class CommandResultStats
{
    private static final int field_179676_a;
    private static final String[] field_179674_b;
    private String[] field_179675_c;
    private String[] field_179673_d;
    private static final String __OBFID = "CL_00002364";
    
    public CommandResultStats() {
        this.field_179675_c = CommandResultStats.field_179674_b;
        this.field_179673_d = CommandResultStats.field_179674_b;
    }
    
    public void func_179672_a(final ICommandSender p_179672_1_, final Type p_179672_2_, final int p_179672_3_) {
        final String var4 = this.field_179675_c[p_179672_2_.func_179636_a()];
        if (var4 != null) {
            String var5;
            try {
                var5 = CommandBase.func_175758_e(p_179672_1_, var4);
            }
            catch (EntityNotFoundException var10) {
                return;
            }
            final String var6 = this.field_179673_d[p_179672_2_.func_179636_a()];
            if (var6 != null) {
                final Scoreboard var7 = p_179672_1_.getEntityWorld().getScoreboard();
                final ScoreObjective var8 = var7.getObjective(var6);
                if (var8 != null && var7.func_178819_b(var5, var8)) {
                    final Score var9 = var7.getValueFromObjective(var5, var8);
                    var9.setScorePoints(p_179672_3_);
                }
            }
        }
    }
    
    public void func_179668_a(final NBTTagCompound p_179668_1_) {
        if (p_179668_1_.hasKey("CommandStats", 10)) {
            final NBTTagCompound var2 = p_179668_1_.getCompoundTag("CommandStats");
            for (final Type var6 : Type.values()) {
                final String var7 = var6.func_179637_b() + "Name";
                final String var8 = var6.func_179637_b() + "Objective";
                if (var2.hasKey(var7, 8) && var2.hasKey(var8, 8)) {
                    final String var9 = var2.getString(var7);
                    final String var10 = var2.getString(var8);
                    func_179667_a(this, var6, var9, var10);
                }
            }
        }
    }
    
    public void func_179670_b(final NBTTagCompound p_179670_1_) {
        final NBTTagCompound var2 = new NBTTagCompound();
        for (final Type var6 : Type.values()) {
            final String var7 = this.field_179675_c[var6.func_179636_a()];
            final String var8 = this.field_179673_d[var6.func_179636_a()];
            if (var7 != null && var8 != null) {
                var2.setString(var6.func_179637_b() + "Name", var7);
                var2.setString(var6.func_179637_b() + "Objective", var8);
            }
        }
        if (!var2.hasNoTags()) {
            p_179670_1_.setTag("CommandStats", var2);
        }
    }
    
    public static void func_179667_a(final CommandResultStats p_179667_0_, final Type p_179667_1_, final String p_179667_2_, final String p_179667_3_) {
        if (p_179667_2_ != null && p_179667_2_.length() != 0 && p_179667_3_ != null && p_179667_3_.length() != 0) {
            if (p_179667_0_.field_179675_c == CommandResultStats.field_179674_b || p_179667_0_.field_179673_d == CommandResultStats.field_179674_b) {
                p_179667_0_.field_179675_c = new String[CommandResultStats.field_179676_a];
                p_179667_0_.field_179673_d = new String[CommandResultStats.field_179676_a];
            }
            p_179667_0_.field_179675_c[p_179667_1_.func_179636_a()] = p_179667_2_;
            p_179667_0_.field_179673_d[p_179667_1_.func_179636_a()] = p_179667_3_;
        }
        else {
            func_179669_a(p_179667_0_, p_179667_1_);
        }
    }
    
    private static void func_179669_a(final CommandResultStats p_179669_0_, final Type p_179669_1_) {
        if (p_179669_0_.field_179675_c != CommandResultStats.field_179674_b && p_179669_0_.field_179673_d != CommandResultStats.field_179674_b) {
            p_179669_0_.field_179675_c[p_179669_1_.func_179636_a()] = null;
            p_179669_0_.field_179673_d[p_179669_1_.func_179636_a()] = null;
            boolean var2 = true;
            for (final Type var6 : Type.values()) {
                if (p_179669_0_.field_179675_c[var6.func_179636_a()] != null && p_179669_0_.field_179673_d[var6.func_179636_a()] != null) {
                    var2 = false;
                    break;
                }
            }
            if (var2) {
                p_179669_0_.field_179675_c = CommandResultStats.field_179674_b;
                p_179669_0_.field_179673_d = CommandResultStats.field_179674_b;
            }
        }
    }
    
    public void func_179671_a(final CommandResultStats p_179671_1_) {
        for (final Type var5 : Type.values()) {
            func_179667_a(this, var5, p_179671_1_.field_179675_c[var5.func_179636_a()], p_179671_1_.field_179673_d[var5.func_179636_a()]);
        }
    }
    
    static {
        field_179676_a = Type.values().length;
        field_179674_b = new String[CommandResultStats.field_179676_a];
    }
    
    public enum Type
    {
        SUCCESS_COUNT("SUCCESS_COUNT", 0, 0, "SuccessCount"), 
        AFFECTED_BLOCKS("AFFECTED_BLOCKS", 1, 1, "AffectedBlocks"), 
        AFFECTED_ENTITIES("AFFECTED_ENTITIES", 2, 2, "AffectedEntities"), 
        AFFECTED_ITEMS("AFFECTED_ITEMS", 3, 3, "AffectedItems"), 
        QUERY_RESULT("QUERY_RESULT", 4, 4, "QueryResult");
        
        final int field_179639_f;
        final String field_179640_g;
        private static final Type[] $VALUES;
        private static final String __OBFID = "CL_00002363";
        
        private Type(final String p_i46050_1_, final int p_i46050_2_, final int p_i46050_3_, final String p_i46050_4_) {
            this.field_179639_f = p_i46050_3_;
            this.field_179640_g = p_i46050_4_;
        }
        
        public int func_179636_a() {
            return this.field_179639_f;
        }
        
        public String func_179637_b() {
            return this.field_179640_g;
        }
        
        public static String[] func_179634_c() {
            final String[] var0 = new String[values().length];
            int var2 = 0;
            for (final Type var6 : values()) {
                var0[var2++] = var6.func_179637_b();
            }
            return var0;
        }
        
        public static Type func_179635_a(final String p_179635_0_) {
            for (final Type var4 : values()) {
                if (var4.func_179637_b().equals(p_179635_0_)) {
                    return var4;
                }
            }
            return null;
        }
        
        static {
            $VALUES = new Type[] { Type.SUCCESS_COUNT, Type.AFFECTED_BLOCKS, Type.AFFECTED_ENTITIES, Type.AFFECTED_ITEMS, Type.QUERY_RESULT };
        }
    }
}
