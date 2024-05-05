/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.scoreboard.ScoreObjective
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreObjective;
import net.ccbluex.liquidbounce.injection.backend.ScoreObjectiveImpl;
import net.minecraft.scoreboard.ScoreObjective;

public final class ScoreObjectiveImplKt {
    public static final ScoreObjective unwrap(IScoreObjective $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ScoreObjectiveImpl)$this$unwrap).getWrapped();
    }

    public static final IScoreObjective wrap(ScoreObjective $this$wrap) {
        int $i$f$wrap = 0;
        return new ScoreObjectiveImpl($this$wrap);
    }
}

