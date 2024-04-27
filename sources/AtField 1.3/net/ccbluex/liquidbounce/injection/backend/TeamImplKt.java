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
    public static final Team unwrap(ITeam iTeam) {
        boolean bl = false;
        return ((TeamImpl)iTeam).getWrapped();
    }

    public static final ITeam wrap(Team team) {
        boolean bl = false;
        return new TeamImpl(team);
    }
}

