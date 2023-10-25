/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ServerWorldEventHandler
implements IWorldEventListener {
    private final MinecraftServer mcServer;
    private final WorldServer theWorldServer;

    public ServerWorldEventHandler(MinecraftServer mcServerIn, WorldServer worldServerIn) {
        this.mcServer = mcServerIn;
        this.theWorldServer = worldServerIn;
    }

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int ... parameters) {
    }

    @Override
    public void func_190570_a(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, double p_190570_4_, double p_190570_6_, double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_, int ... p_190570_16_) {
    }

    @Override
    public void onEntityAdded(Entity entityIn) {
        this.theWorldServer.getEntityTracker().trackEntity(entityIn);
        if (entityIn instanceof EntityPlayerMP) {
            this.theWorldServer.provider.onPlayerAdded((EntityPlayerMP)entityIn);
        }
    }

    @Override
    public void onEntityRemoved(Entity entityIn) {
        this.theWorldServer.getEntityTracker().untrackEntity(entityIn);
        this.theWorldServer.getScoreboard().removeEntity(entityIn);
        if (entityIn instanceof EntityPlayerMP) {
            this.theWorldServer.provider.onPlayerRemoved((EntityPlayerMP)entityIn);
        }
    }

    @Override
    public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {
        this.mcServer.getPlayerList().sendToAllNearExcept(player, x, y, z, volume > 1.0f ? (double)(16.0f * volume) : 16.0, this.theWorldServer.provider.getDimensionType().getId(), new SPacketSoundEffect(soundIn, category, x, y, z, volume, pitch));
    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    }

    @Override
    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
        this.theWorldServer.getPlayerChunkMap().markBlockForUpdate(pos);
    }

    @Override
    public void notifyLightSet(BlockPos pos) {
    }

    @Override
    public void playRecord(SoundEvent soundIn, BlockPos pos) {
    }

    @Override
    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
        this.mcServer.getPlayerList().sendToAllNearExcept(player, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ(), 64.0, this.theWorldServer.provider.getDimensionType().getId(), new SPacketEffect(type, blockPosIn, data, false));
    }

    @Override
    public void broadcastSound(int soundID, BlockPos pos, int data) {
        this.mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketEffect(soundID, pos, data, true));
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
        for (EntityPlayerMP entityplayermp : this.mcServer.getPlayerList().getPlayerList()) {
            double d2;
            double d1;
            double d0;
            if (entityplayermp == null || entityplayermp.world != this.theWorldServer || entityplayermp.getEntityId() == breakerId || !((d0 = (double)pos.getX() - entityplayermp.posX) * d0 + (d1 = (double)pos.getY() - entityplayermp.posY) * d1 + (d2 = (double)pos.getZ() - entityplayermp.posZ) * d2 < 1024.0)) continue;
            entityplayermp.connection.sendPacket(new SPacketBlockBreakAnim(breakerId, pos, progress));
        }
    }
}

