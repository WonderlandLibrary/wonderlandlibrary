// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.event.impl.network.PacketSendEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class SpeedMine extends Module
{
    private final NumberSetting speed;
    private EnumFacing facing;
    private BlockPos pos;
    private boolean boost;
    private float damage;
    
    public SpeedMine() {
        super("SpeedMine", "Speed Mine", Category.PLAYER, "mines blocks faster");
        this.speed = new NumberSetting("Speed", 1.4, 3.0, 1.0, 0.1);
        this.addSettings(this.speed);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (e.isPre()) {
            SpeedMine.mc.playerController.blockHitDelay = 0;
            if (this.pos != null && this.boost) {
                final IBlockState blockState = SpeedMine.mc.theWorld.getBlockState(this.pos);
                if (blockState == null) {
                    return;
                }
                try {
                    this.damage += (float)(blockState.getBlock().getPlayerRelativeBlockHardness(SpeedMine.mc.thePlayer) * this.speed.getValue());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                if (this.damage >= 1.0f) {
                    try {
                        SpeedMine.mc.theWorld.setBlockState(this.pos, Blocks.air.getDefaultState(), 11);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.pos, this.facing));
                    this.damage = 0.0f;
                    this.boost = false;
                }
            }
        }
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent e) {
        if (e.getPacket() instanceof C07PacketPlayerDigging) {
            final C07PacketPlayerDigging packet = (C07PacketPlayerDigging)e.getPacket();
            if (packet.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.boost = true;
                this.pos = packet.getPosition();
                this.facing = packet.getFacing();
                this.damage = 0.0f;
            }
            else if (packet.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK | packet.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.boost = false;
                this.pos = null;
                this.facing = null;
            }
        }
    }
}
