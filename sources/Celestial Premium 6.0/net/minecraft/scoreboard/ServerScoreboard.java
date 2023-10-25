/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;

public class ServerScoreboard
extends Scoreboard {
    private final MinecraftServer scoreboardMCServer;
    private final Set<ScoreObjective> addedObjectives = Sets.newHashSet();
    private Runnable[] dirtyRunnables = new Runnable[0];

    public ServerScoreboard(MinecraftServer mcServer) {
        this.scoreboardMCServer = mcServer;
    }

    @Override
    public void onScoreUpdated(Score scoreIn) {
        super.onScoreUpdated(scoreIn);
        if (this.addedObjectives.contains(scoreIn.getObjective())) {
            this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreIn));
        }
        this.markSaveDataDirty();
    }

    @Override
    public void broadcastScoreUpdate(String scoreName) {
        super.broadcastScoreUpdate(scoreName);
        this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreName));
        this.markSaveDataDirty();
    }

    @Override
    public void broadcastScoreUpdate(String scoreName, ScoreObjective objective) {
        super.broadcastScoreUpdate(scoreName, objective);
        this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreName, objective));
        this.markSaveDataDirty();
    }

    @Override
    public void setObjectiveInDisplaySlot(int objectiveSlot, ScoreObjective objective) {
        ScoreObjective scoreobjective = this.getObjectiveInDisplaySlot(objectiveSlot);
        super.setObjectiveInDisplaySlot(objectiveSlot, objective);
        if (scoreobjective != objective && scoreobjective != null) {
            if (this.getObjectiveDisplaySlotCount(scoreobjective) > 0) {
                this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(objectiveSlot, objective));
            } else {
                this.sendDisplaySlotRemovalPackets(scoreobjective);
            }
        }
        if (objective != null) {
            if (this.addedObjectives.contains(objective)) {
                this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(objectiveSlot, objective));
            } else {
                this.addObjective(objective);
            }
        }
        this.markSaveDataDirty();
    }

    @Override
    public boolean addPlayerToTeam(String player, String newTeam) {
        if (super.addPlayerToTeam(player, newTeam)) {
            ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);
            this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(scoreplayerteam, Arrays.asList(player), 3));
            this.markSaveDataDirty();
            return true;
        }
        return false;
    }

    @Override
    public void removePlayerFromTeam(String username, ScorePlayerTeam playerTeam) {
        super.removePlayerFromTeam(username, playerTeam);
        this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, Arrays.asList(username), 4));
        this.markSaveDataDirty();
    }

    @Override
    public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {
        super.onScoreObjectiveAdded(scoreObjectiveIn);
        this.markSaveDataDirty();
    }

    @Override
    public void onObjectiveDisplayNameChanged(ScoreObjective objective) {
        super.onObjectiveDisplayNameChanged(objective);
        if (this.addedObjectives.contains(objective)) {
            this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketScoreboardObjective(objective, 2));
        }
        this.markSaveDataDirty();
    }

    @Override
    public void onScoreObjectiveRemoved(ScoreObjective objective) {
        super.onScoreObjectiveRemoved(objective);
        if (this.addedObjectives.contains(objective)) {
            this.sendDisplaySlotRemovalPackets(objective);
        }
        this.markSaveDataDirty();
    }

    @Override
    public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {
        super.broadcastTeamCreated(playerTeam);
        this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 0));
        this.markSaveDataDirty();
    }

    @Override
    public void broadcastTeamInfoUpdate(ScorePlayerTeam playerTeam) {
        super.broadcastTeamInfoUpdate(playerTeam);
        this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 2));
        this.markSaveDataDirty();
    }

    @Override
    public void broadcastTeamRemove(ScorePlayerTeam playerTeam) {
        super.broadcastTeamRemove(playerTeam);
        this.scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 1));
        this.markSaveDataDirty();
    }

    public void addDirtyRunnable(Runnable runnable) {
        this.dirtyRunnables = Arrays.copyOf(this.dirtyRunnables, this.dirtyRunnables.length + 1);
        this.dirtyRunnables[this.dirtyRunnables.length - 1] = runnable;
    }

    protected void markSaveDataDirty() {
        for (Runnable runnable : this.dirtyRunnables) {
            runnable.run();
        }
    }

    public List<Packet<?>> getCreatePackets(ScoreObjective objective) {
        ArrayList<Packet<?>> list = Lists.newArrayList();
        list.add(new SPacketScoreboardObjective(objective, 0));
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) != objective) continue;
            list.add(new SPacketDisplayObjective(i, objective));
        }
        for (Score score : this.getSortedScores(objective)) {
            list.add(new SPacketUpdateScore(score));
        }
        return list;
    }

    public void addObjective(ScoreObjective objective) {
        List<Packet<?>> list = this.getCreatePackets(objective);
        for (EntityPlayerMP entityplayermp : this.scoreboardMCServer.getPlayerList().getPlayerList()) {
            for (Packet<?> packet : list) {
                entityplayermp.connection.sendPacket(packet);
            }
        }
        this.addedObjectives.add(objective);
    }

    public List<Packet<?>> getDestroyPackets(ScoreObjective p_96548_1_) {
        ArrayList<Packet<?>> list = Lists.newArrayList();
        list.add(new SPacketScoreboardObjective(p_96548_1_, 1));
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) != p_96548_1_) continue;
            list.add(new SPacketDisplayObjective(i, p_96548_1_));
        }
        return list;
    }

    public void sendDisplaySlotRemovalPackets(ScoreObjective p_96546_1_) {
        List<Packet<?>> list = this.getDestroyPackets(p_96546_1_);
        for (EntityPlayerMP entityplayermp : this.scoreboardMCServer.getPlayerList().getPlayerList()) {
            for (Packet<?> packet : list) {
                entityplayermp.connection.sendPacket(packet);
            }
        }
        this.addedObjectives.remove(p_96546_1_);
    }

    public int getObjectiveDisplaySlotCount(ScoreObjective p_96552_1_) {
        int i = 0;
        for (int j = 0; j < 19; ++j) {
            if (this.getObjectiveInDisplaySlot(j) != p_96552_1_) continue;
            ++i;
        }
        return i;
    }
}

