/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.functions.Function1
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.scoreboard.Team
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScore;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreObjective;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.ScoreObjectiveImpl;
import net.ccbluex.liquidbounce.injection.backend.ScoreboardImpl;
import net.ccbluex.liquidbounce.injection.backend.TeamImpl;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

public final class ScoreboardImpl
implements IScoreboard {
    private final Scoreboard wrapped;

    @Override
    public ITeam getPlayersTeam(@Nullable String name) {
        ITeam iTeam;
        ScorePlayerTeam scorePlayerTeam = this.wrapped.func_96509_i(name);
        if (scorePlayerTeam != null) {
            Team $this$wrap$iv = (Team)scorePlayerTeam;
            boolean $i$f$wrap = false;
            iTeam = new TeamImpl($this$wrap$iv);
        } else {
            iTeam = null;
        }
        return iTeam;
    }

    @Override
    public IScoreObjective getObjectiveInDisplaySlot(int index) {
        IScoreObjective iScoreObjective;
        ScoreObjective scoreObjective = this.wrapped.func_96539_a(index);
        if (scoreObjective != null) {
            ScoreObjective $this$wrap$iv = scoreObjective;
            boolean $i$f$wrap = false;
            iScoreObjective = new ScoreObjectiveImpl($this$wrap$iv);
        } else {
            iScoreObjective = null;
        }
        return iScoreObjective;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public Collection<IScore> getSortedScores(IScoreObjective objective) {
        void $this$unwrap$iv;
        IScoreObjective iScoreObjective = objective;
        Scoreboard scoreboard = this.wrapped;
        boolean $i$f$unwrap = false;
        ScoreObjective scoreObjective = ((ScoreObjectiveImpl)$this$unwrap$iv).getWrapped();
        Function1 function1 = getSortedScores.2.INSTANCE;
        Function1 function12 = getSortedScores.1.INSTANCE;
        Collection collection = scoreboard.func_96534_i(scoreObjective);
        return new WrappedCollection(collection, function12, function1);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ScoreboardImpl && ((ScoreboardImpl)other).wrapped.equals(this.wrapped);
    }

    public final Scoreboard getWrapped() {
        return this.wrapped;
    }

    public ScoreboardImpl(Scoreboard wrapped) {
        this.wrapped = wrapped;
    }
}

