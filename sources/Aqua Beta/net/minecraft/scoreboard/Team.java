package net.minecraft.scoreboard;

import java.util.Collection;
import net.minecraft.scoreboard.Team;

public abstract class Team {
    public boolean isSameTeam(Team other) {
        return other == null ? false : this == other;
    }

    public abstract String getRegisteredName();

    public abstract String formatString(String var1);

    public abstract boolean getSeeFriendlyInvisiblesEnabled();

    public abstract boolean getAllowFriendlyFire();

    public abstract EnumVisible getNameTagVisibility();

    public abstract Collection<String> getMembershipCollection();

    public abstract EnumVisible getDeathMessageVisibility();
}
