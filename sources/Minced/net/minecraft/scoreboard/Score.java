// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import java.util.Comparator;

public class Score
{
    public static final Comparator<Score> SCORE_COMPARATOR;
    private final Scoreboard scoreboard;
    private final ScoreObjective objective;
    private final String scorePlayerName;
    private int scorePoints;
    private boolean locked;
    private boolean forceUpdate;
    
    public Score(final Scoreboard scoreboard, final ScoreObjective objective, final String playerName) {
        this.scoreboard = scoreboard;
        this.objective = objective;
        this.scorePlayerName = playerName;
        this.forceUpdate = true;
    }
    
    public void increaseScore(final int amount) {
        if (this.objective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.setScorePoints(this.getScorePoints() + amount);
    }
    
    public void decreaseScore(final int amount) {
        if (this.objective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.setScorePoints(this.getScorePoints() - amount);
    }
    
    public void incrementScore() {
        if (this.objective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.increaseScore(1);
    }
    
    public int getScorePoints() {
        return this.scorePoints;
    }
    
    public void setScorePoints(final int points) {
        final int i = this.scorePoints;
        this.scorePoints = points;
        if (i != points || this.forceUpdate) {
            this.forceUpdate = false;
            this.getScoreScoreboard().onScoreUpdated(this);
        }
    }
    
    public ScoreObjective getObjective() {
        return this.objective;
    }
    
    public String getPlayerName() {
        return this.scorePlayerName;
    }
    
    public Scoreboard getScoreScoreboard() {
        return this.scoreboard;
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public void setLocked(final boolean locked) {
        this.locked = locked;
    }
    
    static {
        SCORE_COMPARATOR = new Comparator<Score>() {
            @Override
            public int compare(final Score p_compare_1_, final Score p_compare_2_) {
                if (p_compare_1_.getScorePoints() > p_compare_2_.getScorePoints()) {
                    return 1;
                }
                return (p_compare_1_.getScorePoints() < p_compare_2_.getScorePoints()) ? -1 : p_compare_2_.getPlayerName().compareToIgnoreCase(p_compare_1_.getPlayerName());
            }
        };
    }
}
