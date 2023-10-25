/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 */
package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.server.MinecraftServer;

public class ServerScoreboard
extends Scoreboard {
    private final MinecraftServer scoreboardMCServer;
    private final Set<ScoreObjective> field_96553_b = Sets.newHashSet();
    private ScoreboardSaveData scoreboardSaveData;

    public List<Packet> func_96548_f(ScoreObjective scoreObjective) {
        ArrayList arrayList = Lists.newArrayList();
        arrayList.add(new S3BPacketScoreboardObjective(scoreObjective, 1));
        int n = 0;
        while (n < 19) {
            if (this.getObjectiveInDisplaySlot(n) == scoreObjective) {
                arrayList.add(new S3DPacketDisplayScoreboard(n, scoreObjective));
            }
            ++n;
        }
        return arrayList;
    }

    @Override
    public void broadcastTeamCreated(ScorePlayerTeam scorePlayerTeam) {
        super.broadcastTeamCreated(scorePlayerTeam);
        this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(scorePlayerTeam, 0));
        this.func_96551_b();
    }

    @Override
    public void removePlayerFromTeam(String string, ScorePlayerTeam scorePlayerTeam) {
        super.removePlayerFromTeam(string, scorePlayerTeam);
        this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(scorePlayerTeam, Arrays.asList(string), 4));
        this.func_96551_b();
    }

    protected void func_96551_b() {
        if (this.scoreboardSaveData != null) {
            this.scoreboardSaveData.markDirty();
        }
    }

    public void func_96547_a(ScoreboardSaveData scoreboardSaveData) {
        this.scoreboardSaveData = scoreboardSaveData;
    }

    @Override
    public void func_96536_a(Score score) {
        super.func_96536_a(score);
        if (this.field_96553_b.contains(score.getObjective())) {
            this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(score));
        }
        this.func_96551_b();
    }

    @Override
    public boolean addPlayerToTeam(String string, String string2) {
        if (super.addPlayerToTeam(string, string2)) {
            ScorePlayerTeam scorePlayerTeam = this.getTeam(string2);
            this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(scorePlayerTeam, Arrays.asList(string), 3));
            this.func_96551_b();
            return true;
        }
        return false;
    }

    @Override
    public void onScoreObjectiveAdded(ScoreObjective scoreObjective) {
        super.onScoreObjectiveAdded(scoreObjective);
        this.func_96551_b();
    }

    @Override
    public void sendTeamUpdate(ScorePlayerTeam scorePlayerTeam) {
        super.sendTeamUpdate(scorePlayerTeam);
        this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(scorePlayerTeam, 2));
        this.func_96551_b();
    }

    @Override
    public void func_96516_a(String string) {
        super.func_96516_a(string);
        this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(string));
        this.func_96551_b();
    }

    @Override
    public void func_178820_a(String string, ScoreObjective scoreObjective) {
        super.func_178820_a(string, scoreObjective);
        this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(string, scoreObjective));
        this.func_96551_b();
    }

    @Override
    public void func_96533_c(ScoreObjective scoreObjective) {
        super.func_96533_c(scoreObjective);
        if (this.field_96553_b.contains(scoreObjective)) {
            this.getPlayerIterator(scoreObjective);
        }
        this.func_96551_b();
    }

    public ServerScoreboard(MinecraftServer minecraftServer) {
        this.scoreboardMCServer = minecraftServer;
    }

    @Override
    public void func_96513_c(ScorePlayerTeam scorePlayerTeam) {
        super.func_96513_c(scorePlayerTeam);
        this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(scorePlayerTeam, 1));
        this.func_96551_b();
    }

    public void getPlayerIterator(ScoreObjective scoreObjective) {
        List<Packet> list = this.func_96548_f(scoreObjective);
        for (EntityPlayerMP entityPlayerMP : this.scoreboardMCServer.getConfigurationManager().func_181057_v()) {
            for (Packet packet : list) {
                entityPlayerMP.playerNetServerHandler.sendPacket(packet);
            }
        }
        this.field_96553_b.remove(scoreObjective);
    }

    @Override
    public void func_96532_b(ScoreObjective scoreObjective) {
        super.func_96532_b(scoreObjective);
        if (this.field_96553_b.contains(scoreObjective)) {
            this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3BPacketScoreboardObjective(scoreObjective, 2));
        }
        this.func_96551_b();
    }

    @Override
    public void setObjectiveInDisplaySlot(int n, ScoreObjective scoreObjective) {
        ScoreObjective scoreObjective2 = this.getObjectiveInDisplaySlot(n);
        super.setObjectiveInDisplaySlot(n, scoreObjective);
        if (scoreObjective2 != scoreObjective && scoreObjective2 != null) {
            if (this.func_96552_h(scoreObjective2) > 0) {
                this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3DPacketDisplayScoreboard(n, scoreObjective));
            } else {
                this.getPlayerIterator(scoreObjective2);
            }
        }
        if (scoreObjective != null) {
            if (this.field_96553_b.contains(scoreObjective)) {
                this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3DPacketDisplayScoreboard(n, scoreObjective));
            } else {
                this.func_96549_e(scoreObjective);
            }
        }
        this.func_96551_b();
    }

    public List<Packet> func_96550_d(ScoreObjective scoreObjective) {
        ArrayList arrayList = Lists.newArrayList();
        arrayList.add(new S3BPacketScoreboardObjective(scoreObjective, 0));
        int n = 0;
        while (n < 19) {
            if (this.getObjectiveInDisplaySlot(n) == scoreObjective) {
                arrayList.add(new S3DPacketDisplayScoreboard(n, scoreObjective));
            }
            ++n;
        }
        for (Score score : this.getSortedScores(scoreObjective)) {
            arrayList.add(new S3CPacketUpdateScore(score));
        }
        return arrayList;
    }

    public void func_96549_e(ScoreObjective scoreObjective) {
        List<Packet> list = this.func_96550_d(scoreObjective);
        for (EntityPlayerMP entityPlayerMP : this.scoreboardMCServer.getConfigurationManager().func_181057_v()) {
            for (Packet packet : list) {
                entityPlayerMP.playerNetServerHandler.sendPacket(packet);
            }
        }
        this.field_96553_b.add(scoreObjective);
    }

    public int func_96552_h(ScoreObjective scoreObjective) {
        int n = 0;
        int n2 = 0;
        while (n2 < 19) {
            if (this.getObjectiveInDisplaySlot(n2) == scoreObjective) {
                ++n;
            }
            ++n2;
        }
        return n;
    }
}

