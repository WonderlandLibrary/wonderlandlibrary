/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.PushOutEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

@ModuleInfo(name="Phase", category=ModuleCategory.MOVEMENT, description="Allows you to walk through blocks.")
public class Phase
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Skip", "Spartan", "Clip", "AAC3.5.0", "AAC4.4.0", "Matrix", "Mineplex", "Redesky", "FastFall"}, "Vanilla");
    private final TickTimer tickTimer = new TickTimer();
    private boolean mineplexClip;
    private final TickTimer mineplexTickTimer = new TickTimer();

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.modeValue.equals("AAC4.4.0")) {
            Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u + -1.0E-8, Phase.mc.field_71439_g.field_70161_v, Phase.mc.field_71439_g.field_70177_z, Phase.mc.field_71439_g.field_70125_A, false));
            Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u - 1.0, Phase.mc.field_71439_g.field_70161_v, Phase.mc.field_71439_g.field_70177_z, Phase.mc.field_71439_g.field_70125_A, false));
            this.setState(false);
            return;
        }
        if (this.modeValue.equals("fastfall")) {
            Phase.mc.field_71439_g.field_70145_X = true;
            Phase.mc.field_71439_g.field_70181_x -= 10.0;
            Phase.mc.field_71439_g.func_70634_a(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u - 0.5, Phase.mc.field_71439_g.field_70161_v);
            Phase.mc.field_71439_g.field_70122_E = BlockUtils.collideBlockIntersects(Phase.mc.field_71439_g.func_174813_aQ(), block -> !(block instanceof BlockAir));
            return;
        }
        if (this.modeValue.equals("Matrix")) {
            Phase.mc.field_71439_g.func_70107_b(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u - 3.0, Phase.mc.field_71439_g.field_70161_v);
            Phase.mc.field_71474_y.field_74351_w.field_74513_e = true;
            MovementUtils.strafe(0.1f);
            Phase.mc.field_71474_y.field_74351_w.field_74513_e = false;
            return;
        }
        boolean isInsideBlock = BlockUtils.collideBlockIntersects(Phase.mc.field_71439_g.func_174813_aQ(), block -> !(block instanceof BlockAir));
        if (isInsideBlock && !this.modeValue.equals("Mineplex")) {
            Phase.mc.field_71439_g.field_70145_X = true;
            Phase.mc.field_71439_g.field_70181_x = 0.0;
            Phase.mc.field_71439_g.field_70122_E = true;
        }
        NetHandlerPlayClient netHandlerPlayClient = mc.func_147114_u();
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "vanilla": {
                if (!Phase.mc.field_71439_g.field_70122_E || !this.tickTimer.hasTimePassed(2) || !Phase.mc.field_71439_g.field_70123_F || isInsideBlock && !Phase.mc.field_71439_g.func_70093_af()) break;
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u + 0.2, Phase.mc.field_71439_g.field_70161_v, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t + 0.5, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v + 0.5, true));
                double yaw = Math.toRadians(Phase.mc.field_71439_g.field_70177_z);
                double x = -Math.sin(yaw) * 0.04;
                double z = Math.cos(yaw) * 0.04;
                Phase.mc.field_71439_g.func_70107_b(Phase.mc.field_71439_g.field_70165_t + x, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v + z);
                this.tickTimer.reset();
                break;
            }
            case "skip": {
                if (!Phase.mc.field_71439_g.field_70122_E || !this.tickTimer.hasTimePassed(2) || !Phase.mc.field_71439_g.field_70123_F || isInsideBlock && !Phase.mc.field_71439_g.func_70093_af()) break;
                double direction = MovementUtils.getDirection();
                double posX = -Math.sin(direction) * 0.3;
                double posZ = Math.cos(direction) * 0.3;
                for (int i = 0; i < 3; ++i) {
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u + 0.06, Phase.mc.field_71439_g.field_70161_v, true));
                    mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t + posX * (double)i, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v + posZ * (double)i, true));
                }
                Phase.mc.field_71439_g.func_174826_a(Phase.mc.field_71439_g.func_174813_aQ().func_72317_d(posX, 0.0, posZ));
                Phase.mc.field_71439_g.func_70634_a(Phase.mc.field_71439_g.field_70165_t + posX, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v + posZ);
                this.tickTimer.reset();
                break;
            }
            case "spartan": {
                if (!Phase.mc.field_71439_g.field_70122_E || !this.tickTimer.hasTimePassed(2) || !Phase.mc.field_71439_g.field_70123_F || isInsideBlock && !Phase.mc.field_71439_g.func_70093_af()) break;
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u - 0.2, Phase.mc.field_71439_g.field_70161_v, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
                netHandlerPlayClient.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Phase.mc.field_71439_g.field_70165_t + 0.5, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v + 0.5, true));
                double yaw = Math.toRadians(Phase.mc.field_71439_g.field_70177_z);
                double x = -Math.sin(yaw) * 0.04;
                double z = Math.cos(yaw) * 0.04;
                Phase.mc.field_71439_g.func_70107_b(Phase.mc.field_71439_g.field_70165_t + x, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v + z);
                this.tickTimer.reset();
                break;
            }
            case "clip": {
                if (!this.tickTimer.hasTimePassed(2) || !Phase.mc.field_71439_g.field_70123_F || isInsideBlock && !Phase.mc.field_71439_g.func_70093_af()) break;
                double yaw = Math.toRadians(Phase.mc.field_71439_g.field_70177_z);
                double oldX = Phase.mc.field_71439_g.field_70165_t;
                double oldZ = Phase.mc.field_71439_g.field_70161_v;
                for (int i = 1; i <= 10; ++i) {
                    double z;
                    double x = -Math.sin(yaw) * (double)i;
                    if (!(BlockUtils.getBlock(new BlockPos(oldX + x, Phase.mc.field_71439_g.field_70163_u, oldZ + (z = Math.cos(yaw) * (double)i))) instanceof BlockAir) || !(BlockUtils.getBlock(new BlockPos(oldX + x, Phase.mc.field_71439_g.field_70163_u + 1.0, oldZ + z)) instanceof BlockAir)) continue;
                    Phase.mc.field_71439_g.func_70107_b(oldX + x, Phase.mc.field_71439_g.field_70163_u, oldZ + z);
                    break;
                }
                this.tickTimer.reset();
                break;
            }
            case "aac3.5.0": {
                if (!this.tickTimer.hasTimePassed(2) || !Phase.mc.field_71439_g.field_70123_F || isInsideBlock && !Phase.mc.field_71439_g.func_70093_af()) break;
                double yaw = Math.toRadians(Phase.mc.field_71439_g.field_70177_z);
                double oldX = Phase.mc.field_71439_g.field_70165_t;
                double oldZ = Phase.mc.field_71439_g.field_70161_v;
                double x = -Math.sin(yaw);
                double z = Math.cos(yaw);
                Phase.mc.field_71439_g.func_70107_b(oldX + x, Phase.mc.field_71439_g.field_70163_u, oldZ + z);
                this.tickTimer.reset();
                break;
            }
            case "redesky": 
            case "redesky2": {
                this.setState(false);
            }
        }
        this.tickTimer.update();
    }

    @EventTarget
    public void onBlockBB(BlockBBEvent event) {
        if (Phase.mc.field_71439_g != null && BlockUtils.collideBlockIntersects(Phase.mc.field_71439_g.func_174813_aQ(), block -> !(block instanceof BlockAir)) && event.getBoundingBox() != null && event.getBoundingBox().field_72337_e > Phase.mc.field_71439_g.func_174813_aQ().field_72338_b && !this.modeValue.equals("Mineplex")) {
            AxisAlignedBB axisAlignedBB = event.getBoundingBox();
            event.setBoundingBox(new AxisAlignedBB(axisAlignedBB.field_72336_d, Phase.mc.field_71439_g.func_174813_aQ().field_72338_b, axisAlignedBB.field_72334_f, axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c));
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            if (this.modeValue.equals("AAC3.5.0")) {
                float yaw = (float)MovementUtils.getDirection();
                packetPlayer.field_149479_a -= (double)MathHelper.func_76126_a((float)yaw) * 1.0E-8;
                packetPlayer.field_149478_c += (double)MathHelper.func_76134_b((float)yaw) * 1.0E-8;
            }
        }
    }

    @EventTarget
    private void onMove(MoveEvent event) {
        if (this.modeValue.equals("mineplex")) {
            if (Phase.mc.field_71439_g.field_70123_F) {
                this.mineplexClip = true;
            }
            if (!this.mineplexClip) {
                return;
            }
            this.mineplexTickTimer.update();
            event.setX(0.0);
            event.setZ(0.0);
            if (this.mineplexTickTimer.hasTimePassed(3)) {
                this.mineplexTickTimer.reset();
                this.mineplexClip = false;
            } else if (this.mineplexTickTimer.hasTimePassed(1)) {
                double offset = this.mineplexTickTimer.hasTimePassed(2) ? 1.6 : 0.06;
                double direction = MovementUtils.getDirection();
                Phase.mc.field_71439_g.func_70107_b(Phase.mc.field_71439_g.field_70165_t + -Math.sin(direction) * offset, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v + Math.cos(direction) * offset);
            }
        }
    }

    @EventTarget
    public void onPushOut(PushOutEvent event) {
        event.cancelEvent();
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

