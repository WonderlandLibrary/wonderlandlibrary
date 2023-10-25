/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public abstract class Team {
    public boolean isSameTeam(Team team) {
        return team == null ? false : this == team;
    }

    public abstract boolean getAllowFriendlyFire();

    public abstract String formatString(String var1);

    public abstract boolean getSeeFriendlyInvisiblesEnabled();

    public abstract EnumVisible getNameTagVisibility();

    public abstract String getRegisteredName();

    public abstract Collection<String> getMembershipCollection();

    public abstract EnumVisible getDeathMessageVisibility();

    public static enum EnumVisible {
        ALWAYS("always", 0),
        NEVER("never", 1),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

        public final int field_178827_f;
        private static Map<String, EnumVisible> field_178828_g = Maps.newHashMap();
        public final String field_178830_e;

        private EnumVisible(String string2, int n2) {
            this.field_178830_e = string2;
            this.field_178827_f = n2;
        }

        static {
            EnumVisible[] enumVisibleArray = EnumVisible.values();
            int n = enumVisibleArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumVisible enumVisible = enumVisibleArray[n2];
                field_178828_g.put(enumVisible.field_178830_e, enumVisible);
                ++n2;
            }
        }

        public static EnumVisible func_178824_a(String string) {
            return field_178828_g.get(string);
        }

        public static String[] func_178825_a() {
            return field_178828_g.keySet().toArray(new String[field_178828_g.size()]);
        }
    }
}

