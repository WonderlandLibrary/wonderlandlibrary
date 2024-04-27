/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.scoreboard.Score
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.scoreboard.IScore;
import net.ccbluex.liquidbounce.injection.backend.ScoreImpl;
import net.minecraft.scoreboard.Score;

public final class ScoreImplKt {
    public static final Score unwrap(IScore $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ScoreImpl)$this$unwrap).getWrapped();
    }

    public static final IScore wrap(Score $this$wrap) {
        int $i$f$wrap = 0;
        return new ScoreImpl($this$wrap);
    }
}

