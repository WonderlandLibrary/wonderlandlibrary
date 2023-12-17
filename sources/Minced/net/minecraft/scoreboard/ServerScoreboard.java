// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import java.util.Collection;
import net.minecraft.network.play.server.SPacketTeams;
import java.util.Arrays;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateScore;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.server.MinecraftServer;

public class ServerScoreboard extends Scoreboard
{
    private final MinecraftServer server;
    private final Set<ScoreObjective> addedObjectives;
    private Runnable[] dirtyRunnables;
    
    public ServerScoreboard(final MinecraftServer mcServer) {
        this.addedObjectives = (Set<ScoreObjective>)Sets.newHashSet();
        this.dirtyRunnables = new Runnable[0];
        this.server = mcServer;
    }
    
    @Override
    public void onScoreUpdated(final Score scoreIn) {
        super.onScoreUpdated(scoreIn);
        if (this.addedObjectives.contains(scoreIn.getObjective())) {
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreIn));
        }
        this.markSaveDataDirty();
    }
    
    @Override
    public void broadcastScoreUpdate(final String scoreName) {
        super.broadcastScoreUpdate(scoreName);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreName));
        this.markSaveDataDirty();
    }
    
    @Override
    public void broadcastScoreUpdate(final String scoreName, final ScoreObjective objective) {
        super.broadcastScoreUpdate(scoreName, objective);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreName, objective));
        this.markSaveDataDirty();
    }
    
    @Override
    public void setObjectiveInDisplaySlot(final int objectiveSlot, final ScoreObjective objective) {
        final ScoreObjective scoreobjective = this.getObjectiveInDisplaySlot(objectiveSlot);
        super.setObjectiveInDisplaySlot(objectiveSlot, objective);
        if (scoreobjective != objective && scoreobjective != null) {
            if (this.getObjectiveDisplaySlotCount(scoreobjective) > 0) {
                this.server.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(objectiveSlot, objective));
            }
            else {
                this.sendDisplaySlotRemovalPackets(scoreobjective);
            }
        }
        if (objective != null) {
            if (this.addedObjectives.contains(objective)) {
                this.server.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(objectiveSlot, objective));
            }
            else {
                this.addObjective(objective);
            }
        }
        this.markSaveDataDirty();
    }
    
    @Override
    public boolean addPlayerToTeam(final String player, final String newTeam) {
        if (super.addPlayerToTeam(player, newTeam)) {
            final ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(scoreplayerteam, Arrays.asList(player), 3));
            this.markSaveDataDirty();
            return true;
        }
        return false;
    }
    
    @Override
    public void removePlayerFromTeam(final String username, final ScorePlayerTeam playerTeam) {
        super.removePlayerFromTeam(username, playerTeam);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, Arrays.asList(username), 4));
        this.markSaveDataDirty();
    }
    
    @Override
    public void onScoreObjectiveAdded(final ScoreObjective scoreObjectiveIn) {
        super.onScoreObjectiveAdded(scoreObjectiveIn);
        this.markSaveDataDirty();
    }
    
    @Override
    public void onObjectiveDisplayNameChanged(final ScoreObjective objective) {
        super.onObjectiveDisplayNameChanged(objective);
        if (this.addedObjectives.contains(objective)) {
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketScoreboardObjective(objective, 2));
        }
        this.markSaveDataDirty();
    }
    
    @Override
    public void onScoreObjectiveRemoved(final ScoreObjective objective) {
        super.onScoreObjectiveRemoved(objective);
        if (this.addedObjectives.contains(objective)) {
            this.sendDisplaySlotRemovalPackets(objective);
        }
        this.markSaveDataDirty();
    }
    
    @Override
    public void broadcastTeamCreated(final ScorePlayerTeam playerTeam) {
        super.broadcastTeamCreated(playerTeam);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 0));
        this.markSaveDataDirty();
    }
    
    @Override
    public void broadcastTeamInfoUpdate(final ScorePlayerTeam playerTeam) {
        super.broadcastTeamInfoUpdate(playerTeam);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 2));
        this.markSaveDataDirty();
    }
    
    @Override
    public void broadcastTeamRemove(final ScorePlayerTeam playerTeam) {
        super.broadcastTeamRemove(playerTeam);
        this.server.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 1));
        this.markSaveDataDirty();
    }
    
    public void addDirtyRunnable(final Runnable runnable) {
        (this.dirtyRunnables = Arrays.copyOf(this.dirtyRunnables, this.dirtyRunnables.length + 1))[this.dirtyRunnables.length - 1] = runnable;
    }
    
    protected void markSaveDataDirty() {
        for (final Runnable runnable : this.dirtyRunnables) {
            runnable.run();
        }
    }
    
    public List<Packet<?>> getCreatePackets(final ScoreObjective objective) {
        final List<Packet<?>> list = (List<Packet<?>>)Lists.newArrayList();
        list.add(new SPacketScoreboardObjective(objective, 0));
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) == objective) {
                list.add(new SPacketDisplayObjective(i, objective));
            }
        }
        for (final Score score : this.getSortedScores(objective)) {
            list.add(new SPacketUpdateScore(score));
        }
        return list;
    }
    
    public void addObjective(final ScoreObjective objective) {
        final List<Packet<?>> list = this.getCreatePackets(objective);
        for (final EntityPlayerMP entityplayermp : this.server.getPlayerList().getPlayers()) {
            for (final Packet<?> packet : list) {
                entityplayermp.connection.sendPacket(packet);
            }
        }
        this.addedObjectives.add(objective);
    }
    
    public List<Packet<?>> getDestroyPackets(final ScoreObjective p_96548_1_) {
        final List<Packet<?>> list = (List<Packet<?>>)Lists.newArrayList();
        list.add(new SPacketScoreboardObjective(p_96548_1_, 1));
        for (int i = 0; i < 19; ++i) {
            if (this.getObjectiveInDisplaySlot(i) == p_96548_1_) {
                list.add(new SPacketDisplayObjective(i, p_96548_1_));
            }
        }
        return list;
    }
    
    public void sendDisplaySlotRemovalPackets(final ScoreObjective p_96546_1_) {
        final List<Packet<?>> list = this.getDestroyPackets(p_96546_1_);
        for (final EntityPlayerMP entityplayermp : this.server.getPlayerList().getPlayers()) {
            for (final Packet<?> packet : list) {
                entityplayermp.connection.sendPacket(packet);
            }
        }
        this.addedObjectives.remove(p_96546_1_);
    }
    
    public int getObjectiveDisplaySlotCount(final ScoreObjective p_96552_1_) {
        int i = 0;
        for (int j = 0; j < 19; ++j) {
            if (this.getObjectiveInDisplaySlot(j) == p_96552_1_) {
                ++i;
            }
        }
        return i;
    }
}
