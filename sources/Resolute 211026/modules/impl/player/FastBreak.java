// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.block.Block;
import vip.Resolute.events.impl.EventBlockDamaged;
import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.impl.EventPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class FastBreak extends Module
{
    public ModeSetting mode;
    private boolean bzs;
    private float bzx;
    public BlockPos blockPos;
    public EnumFacing facing;
    public static float speed;
    public static int delay;
    
    public FastBreak() {
        super("FastBreak", 0, "Allows you to break blocks faster", Category.PLAYER);
        this.mode = new ModeSetting("Mode", "Normal", new String[] { "Normal", "Watchdog" });
        this.bzs = false;
        this.bzx = 0.0f;
        this.addSettings(this.mode);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion && this.mode.is("Watchdog")) {
            if (FastBreak.mc.playerController.extendedReach()) {
                FastBreak.mc.playerController.blockHitDelay = 0;
            }
            else if (this.bzs) {
                final Block block = FastBreak.mc.theWorld.getBlockState(this.blockPos).getBlock();
                this.bzx += (float)(block.getPlayerRelativeBlockHardness(FastBreak.mc.thePlayer, FastBreak.mc.theWorld, this.blockPos) * 1.4);
                if (this.bzx >= 1.0f) {
                    FastBreak.mc.theWorld.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                    FastBreak.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                    this.bzx = 0.0f;
                    this.bzs = false;
                }
            }
        }
        if (e instanceof EventPacket && this.mode.is("Watchdog")) {
            try {
                if (((EventPacket)e).getPacket() instanceof C07PacketPlayerDigging && FastBreak.mc.playerController != null) {
                    final C07PacketPlayerDigging c07PacketPlayerDigging = ((EventPacket)e).getPacket();
                    if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                        this.bzs = true;
                        this.blockPos = c07PacketPlayerDigging.getPosition();
                        this.facing = c07PacketPlayerDigging.getFacing();
                        this.bzx = 0.0f;
                    }
                    else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                        this.bzs = false;
                        this.blockPos = null;
                        this.facing = null;
                    }
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (e instanceof EventUpdate && e.isPre() && this.mode.is("Normal")) {
            FastBreak.mc.playerController.blockHitDelay = 0;
        }
        if (e instanceof EventBlockDamaged) {
            final EventBlockDamaged event = (EventBlockDamaged)e;
            if (this.mode.is("Normal")) {
                final PlayerControllerMP playerController = FastBreak.mc.playerController;
                final BlockPos pos = event.getBlockPos();
                FastBreak.mc.thePlayer.swingItem();
                final PlayerControllerMP playerControllerMP = playerController;
                playerControllerMP.curBlockDamageMP += this.getBlock(pos.getX(), pos.getY(), pos.getZ()).getPlayerRelativeBlockHardness(FastBreak.mc.thePlayer, FastBreak.mc.theWorld, pos) * 0.186f;
            }
        }
    }
    
    public Block getBlock(final double posX, final double posY, final double posZ) {
        final BlockPos pos = new BlockPos((int)posX, (int)posY, (int)posZ);
        return FastBreak.mc.theWorld.getChunkFromBlockCoords(pos).getBlock(pos);
    }
}
