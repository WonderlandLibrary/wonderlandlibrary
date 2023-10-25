/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.text.TextFormatting;

public abstract class Team {
    public boolean isSameTeam(@Nullable Team other) {
        if (other == null) {
            return false;
        }
        return this == other;
    }

    public abstract String getRegisteredName();

    public abstract String formatString(String var1);

    public abstract boolean getSeeFriendlyInvisiblesEnabled();

    public abstract boolean getAllowFriendlyFire();

    public abstract EnumVisible getNameTagVisibility();

    public abstract TextFormatting getChatFormat();

    public abstract Collection<String> getMembershipCollection();

    public abstract EnumVisible getDeathMessageVisibility();

    public abstract CollisionRule getCollisionRule();

    public static enum EnumVisible {
        ALWAYS("always", 0),
        NEVER("never", 1),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

        private static final Map<String, EnumVisible> nameMap;
        public final String internalName;
        public final int id;

        public static String[] getNames() {
            return nameMap.keySet().toArray(new String[nameMap.size()]);
        }

        @Nullable
        public static EnumVisible getByName(String nameIn) {
            return nameMap.get(nameIn);
        }

        private EnumVisible(String nameIn, int idIn) {
            this.internalName = nameIn;
            this.id = idIn;
        }

        static {
            nameMap = Maps.newHashMap();
            for (EnumVisible team$enumvisible : EnumVisible.values()) {
                nameMap.put(team$enumvisible.internalName, team$enumvisible);
            }
        }
    }

    public static enum CollisionRule {
        ALWAYS("always", 0),
        NEVER("never", 1),
        HIDE_FOR_OTHER_TEAMS("pushOtherTeams", 2),
        HIDE_FOR_OWN_TEAM("pushOwnTeam", 3);

        private static final Map<String, CollisionRule> nameMap;
        public final String name;
        public final int id;

        public static String[] getNames() {
            return nameMap.keySet().toArray(new String[nameMap.size()]);
        }

        @Nullable
        public static CollisionRule getByName(String nameIn) {
            return nameMap.get(nameIn);
        }

        private CollisionRule(String nameIn, int idIn) {
            this.name = nameIn;
            this.id = idIn;
        }

        static {
            nameMap = Maps.newHashMap();
            for (CollisionRule team$collisionrule : CollisionRule.values()) {
                nameMap.put(team$collisionrule.name, team$collisionrule);
            }
        }
    }
}

