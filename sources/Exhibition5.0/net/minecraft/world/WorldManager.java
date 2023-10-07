// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import java.util.Iterator;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess
{
    private MinecraftServer mcServer;
    private WorldServer theWorldServer;
    private static final String __OBFID = "CL_00001433";
    
    public WorldManager(final MinecraftServer p_i1517_1_, final WorldServer p_i1517_2_) {
        this.mcServer = p_i1517_1_;
        this.theWorldServer = p_i1517_2_;
    }
    
    @Override
    public void func_180442_a(final int p_180442_1_, final boolean p_180442_2_, final double p_180442_3_, final double p_180442_5_, final double p_180442_7_, final double p_180442_9_, final double p_180442_11_, final double p_180442_13_, final int... p_180442_15_) {
    }
    
    @Override
    public void onEntityAdded(final Entity entityIn) {
        this.theWorldServer.getEntityTracker().trackEntity(entityIn);
    }
    
    @Override
    public void onEntityRemoved(final Entity entityIn) {
        this.theWorldServer.getEntityTracker().untrackEntity(entityIn);
    }
    
    @Override
    public void playSound(final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
        this.mcServer.getConfigurationManager().sendToAllNear(x, y, z, (volume > 1.0f) ? ((double)(16.0f * volume)) : 16.0, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }
    
    @Override
    public void playSoundToNearExcept(final EntityPlayer except, final String soundName, final double x, final double y, final double z, final float volume, final float pitch) {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(except, x, y, z, (volume > 1.0f) ? ((double)(16.0f * volume)) : 16.0, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }
    
    @Override
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    }
    
    @Override
    public void markBlockForUpdate(final BlockPos pos) {
        this.theWorldServer.getPlayerManager().func_180244_a(pos);
    }
    
    @Override
    public void notifyLightSet(final BlockPos pos) {
    }
    
    @Override
    public void func_174961_a(final String p_174961_1_, final BlockPos p_174961_2_) {
    }
    
    @Override
    public void func_180439_a(final EntityPlayer p_180439_1_, final int p_180439_2_, final BlockPos p_180439_3_, final int p_180439_4_) {
        this.mcServer.getConfigurationManager().sendToAllNearExcept(p_180439_1_, p_180439_3_.getX(), p_180439_3_.getY(), p_180439_3_.getZ(), 64.0, this.theWorldServer.provider.getDimensionId(), new S28PacketEffect(p_180439_2_, p_180439_3_, p_180439_4_, false));
    }
    
    @Override
    public void func_180440_a(final int p_180440_1_, final BlockPos p_180440_2_, final int p_180440_3_) {
        this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(p_180440_1_, p_180440_2_, p_180440_3_, true));
    }
    
    @Override
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        for (final EntityPlayerMP var5 : this.mcServer.getConfigurationManager().playerEntityList) {
            if (var5 != null && var5.worldObj == this.theWorldServer && var5.getEntityId() != breakerId) {
                final double var6 = pos.getX() - var5.posX;
                final double var7 = pos.getY() - var5.posY;
                final double var8 = pos.getZ() - var5.posZ;
                if (var6 * var6 + var7 * var7 + var8 * var8 >= 1024.0) {
                    continue;
                }
                var5.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(breakerId, pos, progress));
            }
        }
    }
}
