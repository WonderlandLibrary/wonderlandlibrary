/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.scoreboard.Team
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.scoreboard.ITeam;
import net.ccbluex.liquidbounce.injection.backend.TeamImpl;
import net.minecraft.scoreboard.Team;

public final class TeamImplKt {
    public static final Team unwrap(ITeam $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((TeamImpl)$this$unwrap).getWrapped();
    }

    public static final ITeam wrap(Team $this$wrap) {
        int $i$f$wrap = 0;
        return new TeamImpl($this$wrap);
    }
}

