/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.scoreboard.Team
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0096\u0002J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\u0014\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\u0016\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u00142\u0006\u0010\u0016\u001a\u00020\fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0017"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ScoreboardImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "wrapped", "Lnet/minecraft/scoreboard/Scoreboard;", "(Lnet/minecraft/scoreboard/Scoreboard;)V", "getWrapped", "()Lnet/minecraft/scoreboard/Scoreboard;", "equals", "", "other", "", "getObjectiveInDisplaySlot", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreObjective;", "index", "", "getPlayersTeam", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/ITeam;", "name", "", "getSortedScores", "", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScore;", "objective", "LiKingSense"})
public final class ScoreboardImpl
implements IScoreboard {
    @NotNull
    private final Scoreboard wrapped;

    @Override
    @Nullable
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
    @Nullable
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
    @NotNull
    public Collection<IScore> getSortedScores(@NotNull IScoreObjective objective) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)objective, (String)"objective");
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
        return other instanceof ScoreboardImpl && Intrinsics.areEqual((Object)((ScoreboardImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Scoreboard getWrapped() {
        return this.wrapped;
    }

    public ScoreboardImpl(@NotNull Scoreboard wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

