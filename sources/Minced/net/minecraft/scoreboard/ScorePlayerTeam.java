// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import javax.annotation.Nullable;
import java.util.Collection;
import com.google.common.collect.Sets;
import net.minecraft.util.text.TextFormatting;
import java.util.Set;

public class ScorePlayerTeam extends Team
{
    private final Scoreboard scoreboard;
    private final String name;
    private final Set<String> membershipSet;
    private String displayName;
    private String prefix;
    private String suffix;
    private boolean allowFriendlyFire;
    private boolean canSeeFriendlyInvisibles;
    private EnumVisible nameTagVisibility;
    private EnumVisible deathMessageVisibility;
    private TextFormatting color;
    private CollisionRule collisionRule;
    
    public ScorePlayerTeam(final Scoreboard scoreboardIn, final String name) {
        this.membershipSet = (Set<String>)Sets.newHashSet();
        this.prefix = "";
        this.suffix = "";
        this.allowFriendlyFire = true;
        this.canSeeFriendlyInvisibles = true;
        this.nameTagVisibility = EnumVisible.ALWAYS;
        this.deathMessageVisibility = EnumVisible.ALWAYS;
        this.color = TextFormatting.RESET;
        this.collisionRule = CollisionRule.ALWAYS;
        this.scoreboard = scoreboardIn;
        this.name = name;
        this.displayName = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.displayName = name;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public Collection<String> getMembershipCollection() {
        return this.membershipSet;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void setPrefix(final String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        this.prefix = prefix;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }
    
    public String getSuffix() {
        return this.suffix;
    }
    
    public void setSuffix(final String suffix) {
        this.suffix = suffix;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public String formatString(final String input) {
        return this.getPrefix() + input + this.getSuffix();
    }
    
    public static String formatPlayerName(@Nullable final Team teamIn, final String string) {
        return (teamIn == null) ? string : teamIn.formatString(string);
    }
    
    @Override
    public boolean getAllowFriendlyFire() {
        return this.allowFriendlyFire;
    }
    
    public void setAllowFriendlyFire(final boolean friendlyFire) {
        this.allowFriendlyFire = friendlyFire;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public boolean getSeeFriendlyInvisiblesEnabled() {
        return this.canSeeFriendlyInvisibles;
    }
    
    public void setSeeFriendlyInvisiblesEnabled(final boolean friendlyInvisibles) {
        this.canSeeFriendlyInvisibles = friendlyInvisibles;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public EnumVisible getNameTagVisibility() {
        return this.nameTagVisibility;
    }
    
    @Override
    public EnumVisible getDeathMessageVisibility() {
        return this.deathMessageVisibility;
    }
    
    public void setNameTagVisibility(final EnumVisible visibility) {
        this.nameTagVisibility = visibility;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }
    
    public void setDeathMessageVisibility(final EnumVisible visibility) {
        this.deathMessageVisibility = visibility;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public CollisionRule getCollisionRule() {
        return this.collisionRule;
    }
    
    public void setCollisionRule(final CollisionRule rule) {
        this.collisionRule = rule;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }
    
    public int getFriendlyFlags() {
        int i = 0;
        if (this.getAllowFriendlyFire()) {
            i |= 0x1;
        }
        if (this.getSeeFriendlyInvisiblesEnabled()) {
            i |= 0x2;
        }
        return i;
    }
    
    public void setFriendlyFlags(final int flags) {
        this.setAllowFriendlyFire((flags & 0x1) > 0);
        this.setSeeFriendlyInvisiblesEnabled((flags & 0x2) > 0);
    }
    
    public void setColor(final TextFormatting color) {
        this.color = color;
    }
    
    @Override
    public TextFormatting getColor() {
        return this.color;
    }
    
    public String getColorPrefix() {
        return this.prefix;
    }
}
