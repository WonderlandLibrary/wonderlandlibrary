/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public abstract class Team {
    private static final String __OBFID = "CL_00000621";

    public boolean isSameTeam(Team other) {
        return other == null ? false : this == other;
    }

    public abstract String getRegisteredName();

    public abstract String formatString(String var1);

    public abstract boolean func_98297_h();

    public abstract boolean getAllowFriendlyFire();

    public abstract EnumVisible func_178770_i();

    public abstract Collection getMembershipCollection();

    public abstract EnumVisible func_178771_j();

    public static enum EnumVisible {
        ALWAYS("ALWAYS", 0, "always", 0),
        NEVER("NEVER", 1, "never", 1),
        HIDE_FOR_OTHER_TEAMS("HIDE_FOR_OTHER_TEAMS", 2, "hideForOtherTeams", 2),
        HIDE_FOR_OWN_TEAM("HIDE_FOR_OWN_TEAM", 3, "hideForOwnTeam", 3);

        private static Map field_178828_g;
        public final String field_178830_e;
        public final int field_178827_f;
        private static final EnumVisible[] $VALUES;
        private static final String __OBFID = "CL_00001962";

        static {
            field_178828_g = Maps.newHashMap();
            $VALUES = new EnumVisible[]{ALWAYS, NEVER, HIDE_FOR_OTHER_TEAMS, HIDE_FOR_OWN_TEAM};
            for (EnumVisible var3 : EnumVisible.values()) {
                field_178828_g.put(var3.field_178830_e, var3);
            }
        }

        public static String[] func_178825_a() {
            return field_178828_g.keySet().toArray(new String[field_178828_g.size()]);
        }

        public static EnumVisible func_178824_a(String p_178824_0_) {
            return (EnumVisible)((Object)field_178828_g.get(p_178824_0_));
        }

        private EnumVisible(String p_i45550_1_, int p_i45550_2_, String p_i45550_3_, int p_i45550_4_) {
            this.field_178830_e = p_i45550_3_;
            this.field_178827_f = p_i45550_4_;
        }
    }
}

