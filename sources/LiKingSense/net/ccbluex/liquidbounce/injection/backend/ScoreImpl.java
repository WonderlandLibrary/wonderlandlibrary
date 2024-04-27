/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.scoreboard.Score
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScore;
import net.minecraft.scoreboard.Score;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ScoreImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/scoreboard/IScore;", "wrapped", "Lnet/minecraft/scoreboard/Score;", "(Lnet/minecraft/scoreboard/Score;)V", "playerName", "", "getPlayerName", "()Ljava/lang/String;", "scorePoints", "", "getScorePoints", "()I", "getWrapped", "()Lnet/minecraft/scoreboard/Score;", "equals", "", "other", "", "LiKingSense"})
public final class ScoreImpl
implements IScore {
    @NotNull
    private final Score wrapped;

    @Override
    public int getScorePoints() {
        return this.wrapped.func_96652_c();
    }

    @Override
    @NotNull
    public String getPlayerName() {
        String string = this.wrapped.func_96653_e();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.playerName");
        return string;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ScoreImpl && Intrinsics.areEqual((Object)((ScoreImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Score getWrapped() {
        return this.wrapped;
    }

    public ScoreImpl(@NotNull Score wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

