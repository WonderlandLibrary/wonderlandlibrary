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
    public static final Scoreboard unwrap(IScoreboard $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ScoreboardImpl)$this$unwrap).getWrapped();
    }

    public static final IScoreboard wrap(Scoreboard $this$wrap) {
        int $i$f$wrap = 0;
        return new ScoreboardImpl($this$wrap);
    }
}

