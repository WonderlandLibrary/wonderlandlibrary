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

    public boolean equals(@Nullable Object object) {
        return object instanceof ScoreboardImpl && ((ScoreboardImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public ITeam getPlayersTeam(@Nullable String string) {
        ITeam iTeam;
        ScorePlayerTeam scorePlayerTeam = this.wrapped.func_96509_i(string);
        if (scorePlayerTeam != null) {
            Team team = (Team)scorePlayerTeam;
            boolean bl = false;
            iTeam = new TeamImpl(team);
        } else {
            iTeam = null;
        }
        return iTeam;
    }

    public final Scoreboard getWrapped() {
        return this.wrapped;
    }

    @Override
    public IScoreObjective getObjectiveInDisplaySlot(int n) {
        IScoreObjective iScoreObjective;
        ScoreObjective scoreObjective = this.wrapped.func_96539_a(n);
        if (scoreObjective != null) {
            ScoreObjective scoreObjective2 = scoreObjective;
            boolean bl = false;
            iScoreObjective = new ScoreObjectiveImpl(scoreObjective2);
        } else {
            iScoreObjective = null;
        }
        return iScoreObjective;
    }

    @Override
    public Collection getSortedScores(IScoreObjective iScoreObjective) {
        IScoreObjective iScoreObjective2 = iScoreObjective;
        Scoreboard scoreboard = this.wrapped;
        boolean bl = false;
        ScoreObjective scoreObjective = ((ScoreObjectiveImpl)iScoreObjective2).getWrapped();
        Function1 function1 = getSortedScores.2.INSTANCE;
        Function1 function12 = getSortedScores.1.INSTANCE;
        Collection collection = scoreboard.func_96534_i(scoreObjective);
        return new WrappedCollection(collection, function12, function1);
    }

    public ScoreboardImpl(Scoreboard scoreboard) {
        this.wrapped = scoreboard;
    }
}

