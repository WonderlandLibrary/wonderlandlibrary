// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

public class ScoreObjective
{
    private final Scoreboard scoreboard;
    private final String name;
    private final IScoreCriteria objectiveCriteria;
    private IScoreCriteria.EnumRenderType renderType;
    private String displayName;
    
    public ScoreObjective(final Scoreboard scoreboard, final String nameIn, final IScoreCriteria objectiveCriteriaIn) {
        this.scoreboard = scoreboard;
        this.name = nameIn;
        this.objectiveCriteria = objectiveCriteriaIn;
        this.displayName = nameIn;
        this.renderType = objectiveCriteriaIn.getRenderType();
    }
    
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }
    
    public String getName() {
        return this.name;
    }
    
    public IScoreCriteria getCriteria() {
        return this.objectiveCriteria;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String nameIn) {
        this.displayName = nameIn;
        this.scoreboard.onObjectiveDisplayNameChanged(this);
    }
    
    public IScoreCriteria.EnumRenderType getRenderType() {
        return this.renderType;
    }
    
    public void setRenderType(final IScoreCriteria.EnumRenderType type) {
        this.renderType = type;
        this.scoreboard.onObjectiveDisplayNameChanged(this);
    }
}
