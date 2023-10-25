/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldServer;

public class WorldManager
implements IWorldAccess {
    private MinecraftServer mcServer;
    private WorldServer theWorldServer;

    @Override
    public void onEntityRemoved(Entity entity) {
        this.theWorldServer.getEntityTracker().untrackEntity(entity);
        this.theWorldServer.getScoreboard().func_181140_a(entity);
    }

    @Override
    public void sendBlockBreakProgress(int n, BlockPos blockPos, int n2) {
        for (EntityPlayerMP entityPlayerMP : this.mcServer.getConfigurationManager().func_181057_v()) {
            double d;
            double d2;
            double d3;
            if (entityPlayerMP == null || entityPlayerMP.worldObj != this.theWorldServer || entityPlayerMP.getEntityId() == n || !((d3 = (double)blockPos.getX() - entityPlayerMP.posX) * d3 + (d2 = (double)blockPos.getY() - entityPlayerMP.posY) * d2 + (d = (double)blockPos.getZ() - entityPlayerMP.posZ) * d < 1024.0)) continue;
            entityPlayerMP.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(n, blockPos, n2));
        }
    }

    @Override
    public void markBlockRangeForRenderUpdate(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    @Override
    public void broadcastSound(int n, BlockPos blockPos, int n2) {
        this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(n, blockPos, n2, true));
    }

    @Override
    public void notifyLightSet(BlockPos blockPos) {
    }

    @Override
    public void spawnParticle(int n, boolean bl, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
    }

    @Override
    public void playAuxSFX(EntityPlayer entityPlayer, int n, BlockPos blockPos, int n2) {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(entityPlayer, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 64.0, this.theWorldServer.provider.getDimensionId(), new S28PacketEffect(n, blockPos, n2, false));
    }

    @Override
    public void playSound(String string, double d, double d2, double d3, float f, float f2) {
        this.mcServer.getConfigurationManager().sendToAllNear(d, d2, d3, f > 1.0f ? (double)(16.0f * f) : 16.0, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(string, d, d2, d3, f, f2));
    }

    @Override
    public void onEntityAdded(Entity entity) {
        this.theWorldServer.getEntityTracker().trackEntity(entity);
    }

    @Override
    public void playSoundToNearExcept(EntityPlayer entityPlayer, String string, double d, double d2, double d3, float f, float f2) {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(entityPlayer, d, d2, d3, f > 1.0f ? (double)(16.0f * f) : 16.0, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(string, d, d2, d3, f, f2));
    }

    @Override
    public void markBlockForUpdate(BlockPos blockPos) {
        this.theWorldServer.getPlayerManager().markBlockForUpdate(blockPos);
    }

    public WorldManager(MinecraftServer minecraftServer, WorldServer worldServer) {
        this.mcServer = minecraftServer;
        this.theWorldServer = worldServer;
    }

    @Override
    public void playRecord(String string, BlockPos blockPos) {
    }
}

