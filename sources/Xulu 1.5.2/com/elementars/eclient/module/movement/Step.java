// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import java.util.Iterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import com.elementars.eclient.event.events.MotionEvent;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Step extends Module
{
    private final Value<String> mode;
    private final double[] oneblockPositions;
    private final double[] onehalfblockPositions;
    private final double[] twoblockPositions;
    private int packets;
    
    public Step() {
        super("Step", "Step up blocks", 0, Category.MOVEMENT, true);
        this.mode = this.register(new Value<String>("Mode", this, "Normal", new String[] { "Normal" }));
        this.oneblockPositions = new double[] { 0.41999998688698, 0.7531999805212 };
        this.onehalfblockPositions = new double[] { 0.41999998688698, 0.7531999805212, 1.00133597911214, 1.16610926093821, 1.24918707874468, 1.1707870772188 };
        this.twoblockPositions = new double[] { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
    }
    
    @EventTarget
    public void onWalkingUpdate(final MotionEvent event) {
        if (!Step.mc.player.collidedHorizontally && this.mode.getValue().equalsIgnoreCase("Normal")) {
            return;
        }
        if (!Step.mc.player.onGround || Step.mc.player.isOnLadder() || Step.mc.player.isInWater() || Step.mc.player.isInLava() || Step.mc.player.movementInput.jump || Step.mc.player.noClip) {
            return;
        }
        if (Step.mc.player.moveForward == 0.0f && Step.mc.player.moveStrafing == 0.0f) {
            return;
        }
        if (Step.mc.player.collidedHorizontally && Step.mc.player.onGround) {
            ++this.packets;
        }
        final double n = this.get_n_normal();
        if (this.mode.getValue().equalsIgnoreCase("Normal")) {
            if (n < 0.0 || n > 2.0) {
                return;
            }
            if (n == 2.0 && this.packets > this.twoblockPositions.length - 2) {
                for (final double pos : this.twoblockPositions) {
                    Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + pos, Step.mc.player.posZ, true));
                }
                Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 2.0, Step.mc.player.posZ);
                this.packets = 0;
            }
            if (n == 1.5 && this.packets > this.onehalfblockPositions.length - 2) {
                for (final double pos : this.onehalfblockPositions) {
                    Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + pos, Step.mc.player.posZ, true));
                }
                Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + this.onehalfblockPositions[this.onehalfblockPositions.length - 1], Step.mc.player.posZ);
                this.packets = 0;
            }
            if (n == 1.0 && this.packets > this.oneblockPositions.length - 2) {
                for (final double pos : this.oneblockPositions) {
                    Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + pos, Step.mc.player.posZ, true));
                }
                Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + this.oneblockPositions[this.oneblockPositions.length - 1], Step.mc.player.posZ);
                this.packets = 0;
            }
        }
    }
    
    public double get_n_normal() {
        Step.mc.player.stepHeight = 0.5f;
        double max_y = -1.0;
        final AxisAlignedBB grow = Step.mc.player.getEntityBoundingBox().offset(0.0, 0.05, 0.0).grow(0.05);
        if (!Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, grow.offset(0.0, 2.0, 0.0)).isEmpty()) {
            return 100.0;
        }
        for (final AxisAlignedBB aabb : Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, grow)) {
            if (aabb.maxY > max_y) {
                max_y = aabb.maxY;
            }
        }
        return max_y - Step.mc.player.posY;
    }
}
