// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Collection;

public abstract class Team
{
    private static final String __OBFID = "CL_00000621";
    
    public boolean isSameTeam(final Team other) {
        return other != null && this == other;
    }
    
    public abstract String getRegisteredName();
    
    public abstract String formatString(final String p0);
    
    public abstract boolean func_98297_h();
    
    public abstract boolean getAllowFriendlyFire();
    
    public abstract EnumVisible func_178770_i();
    
    public abstract Collection getMembershipCollection();
    
    public abstract EnumVisible func_178771_j();
    
    public enum EnumVisible
    {
        ALWAYS("ALWAYS", 0, "always", 0), 
        NEVER("NEVER", 1, "never", 1), 
        HIDE_FOR_OTHER_TEAMS("HIDE_FOR_OTHER_TEAMS", 2, "hideForOtherTeams", 2), 
        HIDE_FOR_OWN_TEAM("HIDE_FOR_OWN_TEAM", 3, "hideForOwnTeam", 3);
        
        private static Map field_178828_g;
        public final String field_178830_e;
        public final int field_178827_f;
        private static final EnumVisible[] $VALUES;
        private static final String __OBFID = "CL_00001962";
        
        public static String[] func_178825_a() {
            return (String[])EnumVisible.field_178828_g.keySet().toArray(new String[EnumVisible.field_178828_g.size()]);
        }
        
        public static EnumVisible func_178824_a(final String p_178824_0_) {
            return EnumVisible.field_178828_g.get(p_178824_0_);
        }
        
        private EnumVisible(final String p_i45550_1_, final int p_i45550_2_, final String p_i45550_3_, final int p_i45550_4_) {
            this.field_178830_e = p_i45550_3_;
            this.field_178827_f = p_i45550_4_;
        }
        
        static {
            EnumVisible.field_178828_g = Maps.newHashMap();
            $VALUES = new EnumVisible[] { EnumVisible.ALWAYS, EnumVisible.NEVER, EnumVisible.HIDE_FOR_OTHER_TEAMS, EnumVisible.HIDE_FOR_OWN_TEAM };
            for (final EnumVisible var4 : values()) {
                EnumVisible.field_178828_g.put(var4.field_178830_e, var4);
            }
        }
    }
}
