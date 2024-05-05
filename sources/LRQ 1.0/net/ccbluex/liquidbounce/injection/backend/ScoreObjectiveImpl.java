/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.Scoreboard
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreObjective;
import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import net.ccbluex.liquidbounce.injection.backend.ScoreboardImpl;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;

public final class ScoreObjectiveImpl
implements IScoreObjective {
    private final ScoreObjective wrapped;

    @Override
    public String getDisplayName() {
        return this.wrapped.func_96678_d();
    }

    @Override
    public IScoreboard getScoreboard() {
        Scoreboard $this$wrap$iv = this.wrapped.func_96682_a();
        boolean $i$f$wrap = false;
        return new ScoreboardImpl($this$wrap$iv);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ScoreObjectiveImpl && ((ScoreObjectiveImpl)other).wrapped.equals(this.wrapped);
    }

    public final ScoreObjective getWrapped() {
        return this.wrapped;
    }

    public ScoreObjectiveImpl(ScoreObjective wrapped) {
        this.wrapped = wrapped;
    }
}

