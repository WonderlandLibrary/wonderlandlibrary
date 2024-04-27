/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.scoreboard.Score
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScore;
import net.minecraft.scoreboard.Score;
import org.jetbrains.annotations.Nullable;

public final class ScoreImpl
implements IScore {
    private final Score wrapped;

    @Override
    public int getScorePoints() {
        return this.wrapped.func_96652_c();
    }

    public final Score getWrapped() {
        return this.wrapped;
    }

    @Override
    public String getPlayerName() {
        return this.wrapped.func_96653_e();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ScoreImpl && ((ScoreImpl)object).wrapped.equals(this.wrapped);
    }

    public ScoreImpl(Score score) {
        this.wrapped = score;
    }
}

