/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreCriteriaColored;
import net.minecraft.scoreboard.ScoreCriteriaHealth;
import net.minecraft.scoreboard.ScoreCriteriaReadOnly;
import net.minecraft.util.text.TextFormatting;

public interface IScoreCriteria {
    public static final Map<String, IScoreCriteria> INSTANCES = Maps.newHashMap();
    public static final IScoreCriteria DUMMY = new ScoreCriteria("dummy");
    public static final IScoreCriteria TRIGGER = new ScoreCriteria("trigger");
    public static final IScoreCriteria DEATH_COUNT = new ScoreCriteria("deathCount");
    public static final IScoreCriteria PLAYER_KILL_COUNT = new ScoreCriteria("playerKillCount");
    public static final IScoreCriteria TOTAL_KILL_COUNT = new ScoreCriteria("totalKillCount");
    public static final IScoreCriteria HEALTH = new ScoreCriteriaHealth("health");
    public static final IScoreCriteria FOOD = new ScoreCriteriaReadOnly("food");
    public static final IScoreCriteria AIR = new ScoreCriteriaReadOnly("air");
    public static final IScoreCriteria ARMOR = new ScoreCriteriaReadOnly("armor");
    public static final IScoreCriteria XP = new ScoreCriteriaReadOnly("xp");
    public static final IScoreCriteria LEVEL = new ScoreCriteriaReadOnly("level");
    public static final IScoreCriteria[] TEAM_KILL = new IScoreCriteria[]{new ScoreCriteriaColored("teamkill.", TextFormatting.BLACK), new ScoreCriteriaColored("teamkill.", TextFormatting.DARK_BLUE), new ScoreCriteriaColored("teamkill.", TextFormatting.DARK_GREEN), new ScoreCriteriaColored("teamkill.", TextFormatting.DARK_AQUA), new ScoreCriteriaColored("teamkill.", TextFormatting.DARK_RED), new ScoreCriteriaColored("teamkill.", TextFormatting.DARK_PURPLE), new ScoreCriteriaColored("teamkill.", TextFormatting.GOLD), new ScoreCriteriaColored("teamkill.", TextFormatting.GRAY), new ScoreCriteriaColored("teamkill.", TextFormatting.DARK_GRAY), new ScoreCriteriaColored("teamkill.", TextFormatting.BLUE), new ScoreCriteriaColored("teamkill.", TextFormatting.GREEN), new ScoreCriteriaColored("teamkill.", TextFormatting.AQUA), new ScoreCriteriaColored("teamkill.", TextFormatting.RED), new ScoreCriteriaColored("teamkill.", TextFormatting.LIGHT_PURPLE), new ScoreCriteriaColored("teamkill.", TextFormatting.YELLOW), new ScoreCriteriaColored("teamkill.", TextFormatting.WHITE)};
    public static final IScoreCriteria[] KILLED_BY_TEAM = new IScoreCriteria[]{new ScoreCriteriaColored("killedByTeam.", TextFormatting.BLACK), new ScoreCriteriaColored("killedByTeam.", TextFormatting.DARK_BLUE), new ScoreCriteriaColored("killedByTeam.", TextFormatting.DARK_GREEN), new ScoreCriteriaColored("killedByTeam.", TextFormatting.DARK_AQUA), new ScoreCriteriaColored("killedByTeam.", TextFormatting.DARK_RED), new ScoreCriteriaColored("killedByTeam.", TextFormatting.DARK_PURPLE), new ScoreCriteriaColored("killedByTeam.", TextFormatting.GOLD), new ScoreCriteriaColored("killedByTeam.", TextFormatting.GRAY), new ScoreCriteriaColored("killedByTeam.", TextFormatting.DARK_GRAY), new ScoreCriteriaColored("killedByTeam.", TextFormatting.BLUE), new ScoreCriteriaColored("killedByTeam.", TextFormatting.GREEN), new ScoreCriteriaColored("killedByTeam.", TextFormatting.AQUA), new ScoreCriteriaColored("killedByTeam.", TextFormatting.RED), new ScoreCriteriaColored("killedByTeam.", TextFormatting.LIGHT_PURPLE), new ScoreCriteriaColored("killedByTeam.", TextFormatting.YELLOW), new ScoreCriteriaColored("killedByTeam.", TextFormatting.WHITE)};

    public String getName();

    public boolean isReadOnly();

    public EnumRenderType getRenderType();

    public static enum EnumRenderType {
        INTEGER("integer"),
        HEARTS("hearts");

        private static final Map<String, EnumRenderType> BY_NAME;
        private final String renderType;

        private EnumRenderType(String renderTypeIn) {
            this.renderType = renderTypeIn;
        }

        public String getRenderType() {
            return this.renderType;
        }

        public static EnumRenderType getByName(String name) {
            EnumRenderType iscorecriteria$enumrendertype = BY_NAME.get(name);
            return iscorecriteria$enumrendertype == null ? INTEGER : iscorecriteria$enumrendertype;
        }

        static {
            BY_NAME = Maps.newHashMap();
            for (EnumRenderType iscorecriteria$enumrendertype : EnumRenderType.values()) {
                BY_NAME.put(iscorecriteria$enumrendertype.getRenderType(), iscorecriteria$enumrendertype);
            }
        }
    }
}

