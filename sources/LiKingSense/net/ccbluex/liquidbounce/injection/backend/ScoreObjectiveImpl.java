/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.Scoreboard
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreObjective;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import net.ccbluex.liquidbounce.injection.backend.ScoreboardImpl;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ScoreObjectiveImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreObjective;", "wrapped", "Lnet/minecraft/scoreboard/ScoreObjective;", "(Lnet/minecraft/scoreboard/ScoreObjective;)V", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "scoreboard", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "getScoreboard", "()Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScoreboard;", "getWrapped", "()Lnet/minecraft/scoreboard/ScoreObjective;", "equals", "", "other", "", "LiKingSense"})
public final class ScoreObjectiveImpl
implements IScoreObjective {
    @NotNull
    private final ScoreObjective wrapped;

    @Override
    @NotNull
    public String getDisplayName() {
        String string = this.wrapped.func_96678_d();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.displayName");
        return string;
    }

    @Override
    @NotNull
    public IScoreboard getScoreboard() {
        Scoreboard scoreboard = this.wrapped.func_96682_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)scoreboard, (String)"wrapped.scoreboard");
        Scoreboard $this$wrap$iv = scoreboard;
        boolean $i$f$wrap = false;
        return new ScoreboardImpl($this$wrap$iv);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ScoreObjectiveImpl && Intrinsics.areEqual((Object)((ScoreObjectiveImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final ScoreObjective getWrapped() {
        return this.wrapped;
    }

    public ScoreObjectiveImpl(@NotNull ScoreObjective wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

