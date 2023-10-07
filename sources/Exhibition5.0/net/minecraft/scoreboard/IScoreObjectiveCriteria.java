// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.scoreboard;

import net.minecraft.util.EnumChatFormatting;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public interface IScoreObjectiveCriteria
{
    public static final Map INSTANCES = Maps.newHashMap();
    public static final IScoreObjectiveCriteria DUMMY = new ScoreDummyCriteria("dummy");
    public static final IScoreObjectiveCriteria field_178791_c = new ScoreDummyCriteria("trigger");
    public static final IScoreObjectiveCriteria deathCount = new ScoreDummyCriteria("deathCount");
    public static final IScoreObjectiveCriteria playerKillCount = new ScoreDummyCriteria("playerKillCount");
    public static final IScoreObjectiveCriteria totalKillCount = new ScoreDummyCriteria("totalKillCount");
    public static final IScoreObjectiveCriteria health = new ScoreHealthCriteria("health");
    public static final IScoreObjectiveCriteria[] field_178792_h = { new GoalColor("teamkill.", EnumChatFormatting.BLACK), new GoalColor("teamkill.", EnumChatFormatting.DARK_BLUE), new GoalColor("teamkill.", EnumChatFormatting.DARK_GREEN), new GoalColor("teamkill.", EnumChatFormatting.DARK_AQUA), new GoalColor("teamkill.", EnumChatFormatting.DARK_RED), new GoalColor("teamkill.", EnumChatFormatting.DARK_PURPLE), new GoalColor("teamkill.", EnumChatFormatting.GOLD), new GoalColor("teamkill.", EnumChatFormatting.GRAY), new GoalColor("teamkill.", EnumChatFormatting.DARK_GRAY), new GoalColor("teamkill.", EnumChatFormatting.BLUE), new GoalColor("teamkill.", EnumChatFormatting.GREEN), new GoalColor("teamkill.", EnumChatFormatting.AQUA), new GoalColor("teamkill.", EnumChatFormatting.RED), new GoalColor("teamkill.", EnumChatFormatting.LIGHT_PURPLE), new GoalColor("teamkill.", EnumChatFormatting.YELLOW), new GoalColor("teamkill.", EnumChatFormatting.WHITE) };
    public static final IScoreObjectiveCriteria[] field_178793_i = { new GoalColor("killedByTeam.", EnumChatFormatting.BLACK), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_BLUE), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GREEN), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_AQUA), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_RED), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_PURPLE), new GoalColor("killedByTeam.", EnumChatFormatting.GOLD), new GoalColor("killedByTeam.", EnumChatFormatting.GRAY), new GoalColor("killedByTeam.", EnumChatFormatting.DARK_GRAY), new GoalColor("killedByTeam.", EnumChatFormatting.BLUE), new GoalColor("killedByTeam.", EnumChatFormatting.GREEN), new GoalColor("killedByTeam.", EnumChatFormatting.AQUA), new GoalColor("killedByTeam.", EnumChatFormatting.RED), new GoalColor("killedByTeam.", EnumChatFormatting.LIGHT_PURPLE), new GoalColor("killedByTeam.", EnumChatFormatting.YELLOW), new GoalColor("killedByTeam.", EnumChatFormatting.WHITE) };
    
    String getName();
    
    int func_96635_a(final List p0);
    
    boolean isReadOnly();
    
    EnumRenderType func_178790_c();
    
    public enum EnumRenderType
    {
        INTEGER("INTEGER", 0, "integer"), 
        HEARTS("HEARTS", 1, "hearts");
        
        private static final Map field_178801_c;
        private final String field_178798_d;
        private static final EnumRenderType[] $VALUES;
        private static final String __OBFID = "CL_00001960";
        
        private EnumRenderType(final String p_i45548_1_, final int p_i45548_2_, final String p_i45548_3_) {
            this.field_178798_d = p_i45548_3_;
        }
        
        public String func_178796_a() {
            return this.field_178798_d;
        }
        
        public static EnumRenderType func_178795_a(final String p_178795_0_) {
            final EnumRenderType var1 = EnumRenderType.field_178801_c.get(p_178795_0_);
            return (var1 == null) ? EnumRenderType.INTEGER : var1;
        }
        
        static {
            field_178801_c = Maps.newHashMap();
            $VALUES = new EnumRenderType[] { EnumRenderType.INTEGER, EnumRenderType.HEARTS };
            for (final EnumRenderType var4 : values()) {
                EnumRenderType.field_178801_c.put(var4.func_178796_a(), var4);
            }
        }
    }
}
