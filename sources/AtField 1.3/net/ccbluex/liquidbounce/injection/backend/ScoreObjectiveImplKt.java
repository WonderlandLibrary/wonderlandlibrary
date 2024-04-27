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
    public static final ScoreObjective unwrap(IScoreObjective iScoreObjective) {
        boolean bl = false;
        return ((ScoreObjectiveImpl)iScoreObjective).getWrapped();
    }

    public static final IScoreObjective wrap(ScoreObjective scoreObjective) {
        boolean bl = false;
        return new ScoreObjectiveImpl(scoreObjective);
    }
}

