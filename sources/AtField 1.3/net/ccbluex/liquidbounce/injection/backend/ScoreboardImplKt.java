/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.scoreboard.Scoreboard
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScoreboard;
import net.ccbluex.liquidbounce.injection.backend.ScoreboardImpl;
import net.minecraft.scoreboard.Scoreboard;

public final class ScoreboardImplKt {
    public static final Scoreboard unwrap(IScoreboard iScoreboard) {
        boolean bl = false;
        return ((ScoreboardImpl)iScoreboard).getWrapped();
    }

    public static final IScoreboard wrap(Scoreboard scoreboard) {
        boolean bl = false;
        return new ScoreboardImpl(scoreboard);
    }
}

