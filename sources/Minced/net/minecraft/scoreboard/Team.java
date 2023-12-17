// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Collection;
import net.minecraft.util.text.TextFormatting;
import javax.annotation.Nullable;

public abstract class Team
{
    public boolean isSameTeam(@Nullable final Team other) {
        return other != null && this == other;
    }
    
    public abstract String getName();
    
    public abstract String formatString(final String p0);
    
    public abstract boolean getSeeFriendlyInvisiblesEnabled();
    
    public abstract boolean getAllowFriendlyFire();
    
    public abstract EnumVisible getNameTagVisibility();
    
    public abstract TextFormatting getColor();
    
    public abstract Collection<String> getMembershipCollection();
    
    public abstract EnumVisible getDeathMessageVisibility();
    
    public abstract CollisionRule getCollisionRule();
    
    public enum CollisionRule
    {
        ALWAYS("always", 0), 
        NEVER("never", 1), 
        HIDE_FOR_OTHER_TEAMS("pushOtherTeams", 2), 
        HIDE_FOR_OWN_TEAM("pushOwnTeam", 3);
        
        private static final Map<String, CollisionRule> nameMap;
        public final String name;
        public final int id;
        
        public static String[] getNames() {
            return CollisionRule.nameMap.keySet().toArray(new String[CollisionRule.nameMap.size()]);
        }
        
        @Nullable
        public static CollisionRule getByName(final String nameIn) {
            return CollisionRule.nameMap.get(nameIn);
        }
        
        private CollisionRule(final String nameIn, final int idIn) {
            this.name = nameIn;
            this.id = idIn;
        }
        
        static {
            nameMap = Maps.newHashMap();
            for (final CollisionRule team$collisionrule : values()) {
                CollisionRule.nameMap.put(team$collisionrule.name, team$collisionrule);
            }
        }
    }
    
    public enum EnumVisible
    {
        ALWAYS("always", 0), 
        NEVER("never", 1), 
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2), 
        HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);
        
        private static final Map<String, EnumVisible> nameMap;
        public final String internalName;
        public final int id;
        
        public static String[] getNames() {
            return EnumVisible.nameMap.keySet().toArray(new String[EnumVisible.nameMap.size()]);
        }
        
        @Nullable
        public static EnumVisible getByName(final String nameIn) {
            return EnumVisible.nameMap.get(nameIn);
        }
        
        private EnumVisible(final String nameIn, final int idIn) {
            this.internalName = nameIn;
            this.id = idIn;
        }
        
        static {
            nameMap = Maps.newHashMap();
            for (final EnumVisible team$enumvisible : values()) {
                EnumVisible.nameMap.put(team$enumvisible.internalName, team$enumvisible);
            }
        }
    }
}
