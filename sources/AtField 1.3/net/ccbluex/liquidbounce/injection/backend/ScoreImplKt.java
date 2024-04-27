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
    public static final IScore wrap(Score score) {
        boolean bl = false;
        return new ScoreImpl(score);
    }

    public static final Score unwrap(IScore iScore) {
        boolean bl = false;
        return ((ScoreImpl)iScore).getWrapped();
    }
}

