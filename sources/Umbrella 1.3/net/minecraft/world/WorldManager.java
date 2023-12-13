/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00001433";

    public WorldManager(MinecraftServer p_i1517_1_, WorldServer p_i1517_2_) {
        this.mcServer = p_i1517_1_;
        this.theWorldServer = p_i1517_2_;
    }

    @Override
    public void func_180442_a(int p_180442_1_, boolean p_180442_2_, double p_180442_3_, double p_180442_5_, double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int ... p_180442_15_) {
    }

    @Override
    public void onEntityAdded(Entity entityIn) {
        this.theWorldServer.getEntityTracker().trackEntity(entityIn);
    }

    @Override
    public void onEntityRemoved(Entity entityIn) {
        this.theWorldServer.getEntityTracker().untrackEntity(entityIn);
    }

    @Override
    public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {
        this.mcServer.getConfigurationManager().sendToAllNear(x, y, z, volume > 1.0f ? (double)(16.0f * volume) : 16.0, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }

    @Override
    public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(except, x, y, z, volume > 1.0f ? (double)(16.0f * volume) : 16.0, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    }

    @Override
    public void markBlockForUpdate(BlockPos pos) {
        this.theWorldServer.getPlayerManager().func_180244_a(pos);
    }

    @Override
    public void notifyLightSet(BlockPos pos) {
    }

    @Override
    public void func_174961_a(String p_174961_1_, BlockPos p_174961_2_) {
    }

    @Override
    public void func_180439_a(EntityPlayer p_180439_1_, int p_180439_2_, BlockPos p_180439_3_, int p_180439_4_) {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(p_180439_1_, p_180439_3_.getX(), p_180439_3_.getY(), p_180439_3_.getZ(), 64.0, this.theWorldServer.provider.getDimensionId(), new S28PacketEffect(p_180439_2_, p_180439_3_, p_180439_4_, false));
    }

    @Override
    public void func_180440_a(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_) {
        this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(p_180440_1_, p_180440_2_, p_180440_3_, true));
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
        for (EntityPlayerMP var5 : this.mcServer.getConfigurationManager().playerEntityList) {
            double var10;
            double var8;
            double var6;
            if (var5 == null || var5.worldObj != this.theWorldServer || var5.getEntityId() == breakerId || !((var6 = (double)pos.getX() - var5.posX) * var6 + (var8 = (double)pos.getY() - var5.posY) * var8 + (var10 = (double)pos.getZ() - var5.posZ) * var10 < 1024.0)) continue;
            var5.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(breakerId, pos, progress));
        }
    }
}

