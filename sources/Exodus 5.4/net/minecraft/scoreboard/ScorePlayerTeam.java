/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;

public class ScorePlayerTeam
extends Team {
    private Team.EnumVisible nameTagVisibility;
    private final String registeredName;
    private boolean allowFriendlyFire = true;
    private final Set<String> membershipSet = Sets.newHashSet();
    private final Scoreboard theScoreboard;
    private EnumChatFormatting chatFormat;
    private boolean canSeeFriendlyInvisibles = true;
    private String namePrefixSPT = "";
    private String teamNameSPT;
    private Team.EnumVisible deathMessageVisibility;
    private String colorSuffix = "";

    public void setDeathMessageVisibility(Team.EnumVisible enumVisible) {
        this.deathMessageVisibility = enumVisible;
        this.theScoreboard.sendTeamUpdate(this);
    }

    @Override
    public Team.EnumVisible getDeathMessageVisibility() {
        return this.deathMessageVisibility;
    }

    public void setNameTagVisibility(Team.EnumVisible enumVisible) {
        this.nameTagVisibility = enumVisible;
        this.theScoreboard.sendTeamUpdate(this);
    }

    public void setNamePrefix(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        this.namePrefixSPT = string;
        this.theScoreboard.sendTeamUpdate(this);
    }

    public void setSeeFriendlyInvisiblesEnabled(boolean bl) {
        this.canSeeFriendlyInvisibles = bl;
        this.theScoreboard.sendTeamUpdate(this);
    }

    @Override
    public String formatString(String string) {
        return String.valueOf(this.getColorPrefix()) + string + this.getColorSuffix();
    }

    public static String formatPlayerName(Team team, String string) {
        return team == null ? string : team.formatString(string);
    }

    public void setAllowFriendlyFire(boolean bl) {
        this.allowFriendlyFire = bl;
        this.theScoreboard.sendTeamUpdate(this);
    }

    public ScorePlayerTeam(Scoreboard scoreboard, String string) {
        this.nameTagVisibility = Team.EnumVisible.ALWAYS;
        this.deathMessageVisibility = Team.EnumVisible.ALWAYS;
        this.chatFormat = EnumChatFormatting.RESET;
        this.theScoreboard = scoreboard;
        this.registeredName = string;
        this.teamNameSPT = string;
    }

    @Override
    public Team.EnumVisible getNameTagVisibility() {
        return this.nameTagVisibility;
    }

    @Override
    public Collection<String> getMembershipCollection() {
        return this.membershipSet;
    }

    @Override
    public boolean getSeeFriendlyInvisiblesEnabled() {
        return this.canSeeFriendlyInvisibles;
    }

    public int func_98299_i() {
        int n = 0;
        if (this.getAllowFriendlyFire()) {
            n |= 1;
        }
        if (this.getSeeFriendlyInvisiblesEnabled()) {
            n |= 2;
        }
        return n;
    }

    public EnumChatFormatting getChatFormat() {
        return this.chatFormat;
    }

    @Override
    public String getRegisteredName() {
        return this.registeredName;
    }

    public String getTeamName() {
        return this.teamNameSPT;
    }

    public void func_98298_a(int n) {
        this.setAllowFriendlyFire((n & 1) > 0);
        this.setSeeFriendlyInvisiblesEnabled((n & 2) > 0);
    }

    public void setNameSuffix(String string) {
        this.colorSuffix = string;
        this.theScoreboard.sendTeamUpdate(this);
    }

    @Override
    public boolean getAllowFriendlyFire() {
        return this.allowFriendlyFire;
    }

    public String getColorPrefix() {
        return this.namePrefixSPT;
    }

    public void setChatFormat(EnumChatFormatting enumChatFormatting) {
        this.chatFormat = enumChatFormatting;
    }

    public String getColorSuffix() {
        return this.colorSuffix;
    }

    public void setTeamName(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.teamNameSPT = string;
        this.theScoreboard.sendTeamUpdate(this);
    }
}

