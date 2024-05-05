package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Map;

public static enum Team.EnumVisible {
    ALWAYS("always", 0),
    NEVER("never", 1),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

    private static Map<String, Team.EnumVisible> field_178828_g;
    public final String internalName;
    public final int id;

    public static String[] func_178825_a() {
        return (String[])field_178828_g.keySet().toArray((Object[])new String[field_178828_g.size()]);
    }

    public static Team.EnumVisible func_178824_a(String p_178824_0_) {
        return (Team.EnumVisible)((Object)field_178828_g.get((Object)p_178824_0_));
    }

    private Team.EnumVisible(String p_i45550_3_, int p_i45550_4_) {
        this.internalName = p_i45550_3_;
        this.id = p_i45550_4_;
    }

    static {
        field_178828_g = Maps.newHashMap();
        for (Team.EnumVisible team$enumvisible : Team.EnumVisible.values()) {
            field_178828_g.put((Object)team$enumvisible.internalName, (Object)team$enumvisible);
        }
    }
}
