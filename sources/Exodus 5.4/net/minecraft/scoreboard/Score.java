/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.scoreboard;

import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class Score {
    private boolean locked;
    private boolean field_178818_g;
    public static final Comparator<Score> scoreComparator = new Comparator<Score>(){

        @Override
        public int compare(Score score, Score score2) {
            return score.getScorePoints() > score2.getScorePoints() ? 1 : (score.getScorePoints() < score2.getScorePoints() ? -1 : score2.getPlayerName().compareToIgnoreCase(score.getPlayerName()));
        }
    };
    private final String scorePlayerName;
    private final Scoreboard theScoreboard;
    private int scorePoints;
    private final ScoreObjective theScoreObjective;

    public void decreaseScore(int n) {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.setScorePoints(this.getScorePoints() - n);
    }

    public void setScorePoints(int n) {
        int n2 = this.scorePoints;
        this.scorePoints = n;
        if (n2 != n || this.field_178818_g) {
            this.field_178818_g = false;
            this.getScoreScoreboard().func_96536_a(this);
        }
    }

    public void func_96651_a(List<EntityPlayer> list) {
        this.setScorePoints(this.theScoreObjective.getCriteria().func_96635_a(list));
    }

    public int getScorePoints() {
        return this.scorePoints;
    }

    public Scoreboard getScoreScoreboard() {
        return this.theScoreboard;
    }

    public Score(Scoreboard scoreboard, ScoreObjective scoreObjective, String string) {
        this.theScoreboard = scoreboard;
        this.theScoreObjective = scoreObjective;
        this.scorePlayerName = string;
        this.field_178818_g = true;
    }

    public String getPlayerName() {
        return this.scorePlayerName;
    }

    public void func_96648_a() {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.increseScore(1);
    }

    public ScoreObjective getObjective() {
        return this.theScoreObjective;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean bl) {
        this.locked = bl;
    }

    public void increseScore(int n) {
        if (this.theScoreObjective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        this.setScorePoints(this.getScorePoints() + n);
    }
}

