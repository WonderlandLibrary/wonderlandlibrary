/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.GoalColor;
import net.minecraft.scoreboard.ScoreDummyCriteria;
import net.minecraft.scoreboard.ScoreHealthCriteria;
import net.minecraft.util.EnumChatFormatting;

public interface IScoreObjectiveCriteria {
    public static final IScoreObjectiveCriteria TRIGGER;
    public static final IScoreObjectiveCriteria deathCount;
    public static final IScoreObjectiveCriteria totalKillCount;
    public static final IScoreObjectiveCriteria DUMMY;
    public static final IScoreObjectiveCriteria playerKillCount;
    public static final IScoreObjectiveCriteria[] field_178792_h;
    public static final Map<String, IScoreObjectiveCriteria> INSTANCES;
    public static final IScoreObjectiveCriteria health;
    public static final IScoreObjectiveCriteria[] field_178793_i;

    public String getName();

    static {
        INSTANCES = Maps.newHashMap();
        DUMMY = new ScoreDummyCriteria("dummy");
        TRIGGER = new ScoreDummyCriteria("trigger");
        deathCount = new ScoreDummyCriteria("deathCount");
        playerKillCount = new ScoreDummyCriteria("playerKillCount");
        totalKillCount = new ScoreDummyCriteria("totalKillCount");
        health = new ScoreHealthCriteria("health");
        field_178792_h = new IScoreObjectiveCriteria[]{new GoalColor("teamkill.", EnumChatFormatting.BLACK), new GoalColor("teamkill.", EnumChatFormatting.DARK_BLUE), new GoalColor("teamkill.", EnumChatFormatting.DARK_GREEN), new GoalColor("teamkill.", EnumChatFormatting.DARK_AQUA), new GoalColor("teamkill.", EnumChatFormatting.DARK_RED), new GoalColor("teamkill.", EnumChatFormatting.DARK_PURPLE), new GoalColor("teamkill.", EnumChatFormatting.GOLD), new GoalColor("teamkill.", EnumChatFormatting.GRAY), new GoalColor("teamkill.", EnumChatFormatting.DARK_GRAY), new GoalColor("teamkill.", EnumChatFormatting.BLUE), new GoalColor("teamkill.", EnumChatFormatting.GREEN), new GoalColor("teamkill.", EnumChatFormatting.AQUA), new GoalColor("teamkill.", EnumChatFormatting.RED), new GoalColor("teamkill.", EnumChatFormatting.LIGHT_PURPLE), new GoalColor("teamkill.", EnumChatFormatting.YELLOW), new GoalColor("teamkill.", EnumChatFormatting.WHITE)};
        field_178793_i = new IScoreObjectiveCriteria[]{new GoalColor("killedByTeam.", EnumChatFormatting.BLACK), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_BLUE), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GREEN), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_AQUA), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_RED), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_PURPLE), new GoalColor("killedByTeam.", EnumChatFormatting.GOLD), new GoalColor("killedByTeam.", EnumChatFormatting.GRAY), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GRAY), new GoalColor("killedByTeam.", EnumChatFormatting.BLUE), new GoalColor("killedByTeam.", EnumChatFormatting.GREEN), new GoalColor("killedByTeam.", EnumChatFormatting.AQUA), new GoalColor("killedByTeam.", EnumChatFormatting.RED), new GoalColor("killedByTeam.", EnumChatFormatting.LIGHT_PURPLE), new GoalColor("killedByTeam.", EnumChatFormatting.YELLOW), new GoalColor("killedByTeam.", EnumChatFormatting.WHITE)};
    }

    public int func_96635_a(List<EntityPlayer> var1);

    public EnumRenderType getRenderType();

    public boolean isReadOnly();

    public static enum EnumRenderType {
        INTEGER("integer"),
        HEARTS("hearts");

        private final String field_178798_d;
        private static final Map<String, EnumRenderType> field_178801_c;

        public static EnumRenderType func_178795_a(String string) {
            EnumRenderType enumRenderType = field_178801_c.get(string);
            return enumRenderType == null ? INTEGER : enumRenderType;
        }

        static {
            field_178801_c = Maps.newHashMap();
            EnumRenderType[] enumRenderTypeArray = EnumRenderType.values();
            int n = enumRenderTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumRenderType enumRenderType = enumRenderTypeArray[n2];
                field_178801_c.put(enumRenderType.func_178796_a(), enumRenderType);
                ++n2;
            }
        }

        private EnumRenderType(String string2) {
            this.field_178798_d = string2;
        }

        public String func_178796_a() {
            return this.field_178798_d;
        }
    }
}

