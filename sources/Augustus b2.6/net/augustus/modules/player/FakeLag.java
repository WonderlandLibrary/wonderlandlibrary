// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.augustus.utils.RotationUtil;
import net.augustus.utils.RayTraceUtil;
import net.augustus.utils.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.augustus.events.EventSendPacket;
import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import net.augustus.utils.BlockUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.augustus.events.EventEarlyTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.network.Packet;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class FakeLag extends Module
{
    private final ArrayList<Packet> packets;
    private final TimeHelper timeHelper;
    public DoubleValue startDelay;
    public DoubleValue lagDuration;
    public BooleanValue onlyMove;
    public BooleanValue combat;
    public int sentC03Packets;
    private boolean shouldBlockPackets;
    
    public FakeLag() {
        super("FakeLaag", Color.red, Categorys.PLAYER);
        this.packets = new ArrayList<Packet>();
        this.timeHelper = new TimeHelper();
        this.startDelay = new DoubleValue(1, "StartDelay", this, 500.0, 0.0, 2000.0, 0);
        this.lagDuration = new DoubleValue(2, "LagPackets", this, 10.0, 1.0, 50.0, 0);
        this.onlyMove = new BooleanValue(3, "OnlyMove", this, false);
        this.combat = new BooleanValue(4, "Combat", this, false);
        this.sentC03Packets = 0;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.shouldBlockPackets = false;
    }
    
    @Override
    public void onPreDisable() {
        this.resetPackets();
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        int count = 0;
        for (final Packet p : this.packets) {
            if (p instanceof C03PacketPlayer) {
                ++count;
            }
        }
        this.sentC03Packets = count;
        if (this.combat.getBoolean()) {
            if (count > this.lagDuration.getValue() || BlockUtil.isScaffoldToggled()) {
                this.shouldBlockPackets = false;
            }
        }
        else if (count <= this.lagDuration.getValue() && !BlockUtil.isScaffoldToggled()) {
            this.shouldBlockPackets = true;
        }
        else {
            this.shouldBlockPackets = false;
            this.resetPackets();
        }
        if (count <= this.lagDuration.getValue() && !BlockUtil.isScaffoldToggled()) {
            if (!this.combat.getBoolean()) {
                this.shouldBlockPackets = true;
            }
        }
        else {
            this.shouldBlockPackets = false;
            this.resetPackets();
        }
    }
    
    @EventTarget
    public void onEventSendPacket(final EventSendPacket eventSendPacket) {
        final Packet packet = eventSendPacket.getPacket();
        if (this.combat.getBoolean()) {
            if (packet instanceof C02PacketUseEntity) {
                this.shouldBlockPackets = false;
                this.resetPackets();
            }
            else if (packet instanceof C03PacketPlayer && FakeLag.mm.killAura.isToggled() && FakeLag.mm.killAura.target != null) {
                final EntityLivingBase entityLivingBase = FakeLag.mm.killAura.target;
                if (entityLivingBase instanceof EntityPlayer) {
                    final EntityPlayer player = (EntityPlayer)entityLivingBase;
                    final double[] predictedTarget = PlayerUtil.predictPosition(player, (int)Math.max(this.lagDuration.getValue(), 0.0));
                    final double[] predictedMe = PlayerUtil.predictPosition(FakeLag.mc.thePlayer, (int)Math.max(this.lagDuration.getValue(), 0.0));
                    final MovingObjectPosition movingObjectPosition = RayTraceUtil.rayCast(4.0f);
                    final Vec3 positionEyes = FakeLag.mc.thePlayer.getPositionEyes(1.0f);
                    final Vec3 positionEyesServer = FakeLag.mc.thePlayer.getSeverPosition().addVector(0.0, FakeLag.mc.thePlayer.getEyeHeight(), 0.0);
                    final Vec3 bestHitVec = RotationUtil.getBestHitVec(player);
                    if (!this.shouldBlockPackets && player.hurtTime < 3 && positionEyes.distanceTo(bestHitVec) > 2.9 && positionEyes.distanceTo(bestHitVec) < 3.3 && positionEyes.distanceTo(bestHitVec) < positionEyesServer.distanceTo(bestHitVec)) {
                        this.shouldBlockPackets = true;
                    }
                }
            }
        }
        if (FakeLag.mc.theWorld != null && this.shouldBlockPackets && this.timeHelper.reached((long)this.startDelay.getValue())) {
            if (this.onlyMove.getBoolean()) {
                if (packet instanceof C03PacketPlayer && !this.packets.contains(packet)) {
                    this.packets.add(packet);
                    eventSendPacket.setCanceled(true);
                }
            }
            else if (!this.packets.contains(packet)) {
                this.packets.add(packet);
                eventSendPacket.setCanceled(true);
            }
        }
    }
    
    private void resetPackets() {
        if (FakeLag.mc.thePlayer != null) {
            if (!this.packets.isEmpty()) {
                this.packets.forEach(packet -> FakeLag.mc.thePlayer.sendQueue.addToSendQueueDirect(packet));
                this.packets.clear();
            }
        }
        else {
            this.packets.clear();
        }
    }
}
