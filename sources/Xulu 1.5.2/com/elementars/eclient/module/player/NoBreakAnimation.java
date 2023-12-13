// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import net.minecraft.network.Packet;
import com.elementars.eclient.event.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import com.elementars.eclient.event.events.EventSendPacket;
import com.elementars.eclient.module.Category;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import com.elementars.eclient.module.Module;

public class NoBreakAnimation extends Module
{
    private boolean isMining;
    private BlockPos lastPos;
    private EnumFacing lastFacing;
    
    public NoBreakAnimation() {
        super("NoBreakAnimation", "Prevents block break animation server side", 0, Category.PLAYER, true);
        this.isMining = false;
        this.lastPos = null;
        this.lastFacing = null;
    }
    
    @EventTarget
    public void onSend(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            final CPacketPlayerDigging cPacketPlayerDigging = (CPacketPlayerDigging)event.getPacket();
            for (final Entity entity : NoBreakAnimation.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(cPacketPlayerDigging.getPosition()))) {
                if (entity instanceof EntityEnderCrystal) {
                    this.resetMining();
                    return;
                }
                if (entity instanceof EntityLivingBase) {
                    this.resetMining();
                    return;
                }
            }
            if (cPacketPlayerDigging.getAction().equals((Object)CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                this.isMining = true;
                this.setMiningInfo(cPacketPlayerDigging.getPosition(), cPacketPlayerDigging.getFacing());
            }
            if (cPacketPlayerDigging.getAction().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                this.resetMining();
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (!NoBreakAnimation.mc.gameSettings.keyBindAttack.isKeyDown()) {
            this.resetMining();
            return;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null) {
            NoBreakAnimation.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
    }
    
    private void setMiningInfo(final BlockPos lastPos, final EnumFacing lastFacing) {
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }
    
    public void resetMining() {
        this.isMining = false;
        this.lastPos = null;
        this.lastFacing = null;
    }
}
