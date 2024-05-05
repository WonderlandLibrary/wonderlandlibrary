// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import vip.Resolute.util.player.RaytraceUtils;
import vip.Resolute.util.world.BlockUtils;
import net.minecraft.init.Blocks;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import vip.Resolute.util.player.RotationUtils;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.ModeSetting;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import vip.Resolute.util.misc.TimerUtils;
import vip.Resolute.modules.Module;

public class Breaker extends Module
{
    private int h;
    private int i;
    private int j;
    private static int k;
    protected TimerUtils time;
    protected long lastFail;
    protected boolean failed;
    protected BlockPos failedBlock;
    protected BlockPos lastArround;
    protected Block lastArroundBlock;
    protected int ftrys;
    protected int blockID;
    protected float yaw;
    protected float pitch;
    public ModeSetting mode;
    
    public Breaker() {
        super("Breaker", 0, "Breaks certain blocks", Category.PLAYER);
        this.time = new TimerUtils();
        this.blockID = 92;
        this.mode = new ModeSetting("Mode", "Bed", new String[] { "Bed", "Cake" });
        this.addSettings(this.mode);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.time.reset();
        this.lastFail = 0L;
        this.ftrys = 0;
        this.failed = false;
        this.lastArround = null;
        this.lastArroundBlock = null;
        this.failedBlock = null;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getMode());
        if (e instanceof EventMotion && e.isPre()) {
            if (this.mode.is("Cake")) {
                final EventMotion eventMotion = (EventMotion)e;
                if (Breaker.mc.theWorld != null && Breaker.mc.thePlayer != null) {
                    if (this.failedBlock != null && this.getBlocksArround(this.failedBlock).isEmpty()) {
                        this.lastArround = null;
                        this.lastArroundBlock = null;
                        this.failed = false;
                    }
                    try {
                        if (this.failed && !this.getBlocksArround(this.failedBlock).isEmpty()) {
                            this.checkFailed();
                        }
                        for (int y = -5; y < 5; ++y) {
                            for (int x = -5; x < 5; ++x) {
                                for (int z = -5; z < 5; ++z) {
                                    final BlockPos blockPos = Breaker.mc.thePlayer.getPosition().add(x, y, (double)z);
                                    final Block block = Breaker.mc.theWorld.getBlockState(blockPos).getBlock();
                                    if (block == Block.getBlockById(this.blockID)) {
                                        this.setRotation(blockPos);
                                        if (this.time.hasTimeElapsed(4L, true)) {
                                            this.destroyBlock(blockPos);
                                            ++this.ftrys;
                                            this.time.reset();
                                            if (this.failedDestroy(blockPos, block) && !this.getBlocksArround(blockPos).isEmpty() && this.ftrys > 5.0) {
                                                this.lastFail = System.currentTimeMillis();
                                                this.failed = true;
                                                this.failedBlock = blockPos;
                                                this.ftrys = 0;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
            if (this.mode.is("Bed")) {
                for (int x2 = -Breaker.k; x2 < Breaker.k; ++x2) {
                    for (int y = Breaker.k; y > -Breaker.k; --y) {
                        for (int z2 = -Breaker.k; z2 < Breaker.k; ++z2) {
                            this.h = (int)Breaker.mc.thePlayer.posX + x2;
                            this.i = (int)Breaker.mc.thePlayer.posY + y;
                            this.j = (int)Breaker.mc.thePlayer.posZ + z2;
                            final BlockPos blockPos2 = new BlockPos(this.h, this.i, this.j);
                            final Block block2 = Breaker.mc.theWorld.getBlockState(blockPos2).getBlock();
                            final float[] rotations = RotationUtils.getRotationFromPosition(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
                            if (block2.getBlockState().getBlock() == Block.getBlockById(26)) {
                                ((EventMotion)e).setYaw(rotations[0]);
                                ((EventMotion)e).setPitch(rotations[1]);
                                Breaker.mc.getNetHandler().sendPacketNoEvent(new C0APacketAnimation());
                                Breaker.mc.playerController.curBlockDamageMP = 1.0f;
                                Breaker.mc.playerController.onPlayerDamageBlock(blockPos2, EnumFacing.NORTH);
                                Breaker.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos2, EnumFacing.NORTH));
                                Breaker.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos2, EnumFacing.NORTH));
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected void checkFailed() {
        final long sinceLastFail = System.currentTimeMillis() - this.lastFail;
        if (!this.getBlocksArround(this.failedBlock).isEmpty()) {
            final BlockPos blockPos = this.getClosetBlock(this.getBlocksArround(this.failedBlock));
            this.setRotation(blockPos);
            if (sinceLastFail > 0L) {
                this.lastArround = blockPos;
                this.lastArroundBlock = Breaker.mc.theWorld.getBlockState(blockPos).getBlock();
                this.destroyBlock(blockPos);
            }
        }
        if (this.lastArround != null) {
            this.lastArround = null;
            this.lastArroundBlock = null;
            this.failed = false;
        }
    }
    
    protected BlockPos getClosetBlock(final List list) {
        BlockPos currentPos = null;
        double currentRange = Double.MAX_VALUE;
        for (final BlockPos blockPos : list) {
            if (Breaker.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) < currentRange) {
                currentRange = Breaker.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                currentPos = blockPos;
            }
        }
        return currentPos;
    }
    
    protected List getBlocksArround(final BlockPos mainPos) {
        final ArrayList arroundBlocks = new ArrayList();
        final Block mainBlock = Breaker.mc.theWorld.getBlockState(mainPos).getBlock();
        for (int y = 0; y < 2; ++y) {
            for (int x = -5; x < 5; ++x) {
                for (int z = -5; z < 5; ++z) {
                    final BlockPos blockPos = mainPos.add(x, y, z);
                    final Block block = Breaker.mc.theWorld.getBlockState(blockPos).getBlock();
                    if (block != Blocks.air) {
                        arroundBlocks.add(blockPos);
                    }
                }
            }
        }
        return arroundBlocks;
    }
    
    protected boolean failedDestroy(final BlockPos blockPos, final Block block) {
        return Breaker.mc.theWorld.getBlockState(blockPos).getBlock() == block;
    }
    
    protected void setRotation(final BlockPos blockPos) {
        final BlockUtils sLoc = new BlockUtils(Breaker.mc.thePlayer.posX, Breaker.mc.thePlayer.posY + 1.6, Breaker.mc.thePlayer.posZ);
        final BlockUtils eLoc = new BlockUtils(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        final RaytraceUtils rayTrace = new RaytraceUtils(sLoc, eLoc);
        this.pitch = (float)rayTrace.getPitch();
        this.yaw = (float)rayTrace.getYaw();
    }
    
    protected void destroyBlock(final BlockPos blockPos) {
        Breaker.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
        Breaker.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
        Breaker.mc.getNetHandler().addToSendQueueSilent(new C0APacketAnimation());
    }
    
    static {
        Breaker.k = 5;
    }
}
