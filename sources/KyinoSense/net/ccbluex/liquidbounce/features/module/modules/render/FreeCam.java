/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.world.World
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.world.World;

@ModuleInfo(name="FreeCam", description="Allows you to move out of your body.", category=ModuleCategory.RENDER)
public class FreeCam
extends Module {
    private final FloatValue speedValue = new FloatValue("Speed", 0.8f, 0.1f, 2.0f);
    private final BoolValue flyValue = new BoolValue("Fly", true);
    private final BoolValue noClipValue = new BoolValue("NoClip", true);
    private EntityOtherPlayerMP fakePlayer = null;
    private double oldX;
    private double oldY;
    private double oldZ;

    @Override
    public void onEnable() {
        if (FreeCam.mc.field_71439_g == null) {
            return;
        }
        this.oldX = FreeCam.mc.field_71439_g.field_70165_t;
        this.oldY = FreeCam.mc.field_71439_g.field_70163_u;
        this.oldZ = FreeCam.mc.field_71439_g.field_70161_v;
        this.fakePlayer = new EntityOtherPlayerMP((World)FreeCam.mc.field_71441_e, FreeCam.mc.field_71439_g.func_146103_bH());
        this.fakePlayer.func_71049_a((EntityPlayer)FreeCam.mc.field_71439_g, true);
        this.fakePlayer.field_70759_as = FreeCam.mc.field_71439_g.field_70759_as;
        this.fakePlayer.func_82149_j((Entity)FreeCam.mc.field_71439_g);
        FreeCam.mc.field_71441_e.func_73027_a(-1000, (Entity)this.fakePlayer);
        if (((Boolean)this.noClipValue.get()).booleanValue()) {
            FreeCam.mc.field_71439_g.field_70145_X = true;
        }
    }

    @Override
    public void onDisable() {
        if (FreeCam.mc.field_71439_g == null || this.fakePlayer == null) {
            return;
        }
        FreeCam.mc.field_71439_g.func_70080_a(this.oldX, this.oldY, this.oldZ, FreeCam.mc.field_71439_g.field_70177_z, FreeCam.mc.field_71439_g.field_70125_A);
        FreeCam.mc.field_71441_e.func_73028_b(this.fakePlayer.func_145782_y());
        this.fakePlayer = null;
        FreeCam.mc.field_71439_g.field_70159_w = 0.0;
        FreeCam.mc.field_71439_g.field_70181_x = 0.0;
        FreeCam.mc.field_71439_g.field_70179_y = 0.0;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (((Boolean)this.noClipValue.get()).booleanValue()) {
            FreeCam.mc.field_71439_g.field_70145_X = true;
        }
        FreeCam.mc.field_71439_g.field_70143_R = 0.0f;
        if (((Boolean)this.flyValue.get()).booleanValue()) {
            float value = ((Float)this.speedValue.get()).floatValue();
            FreeCam.mc.field_71439_g.field_70181_x = 0.0;
            FreeCam.mc.field_71439_g.field_70159_w = 0.0;
            FreeCam.mc.field_71439_g.field_70179_y = 0.0;
            if (FreeCam.mc.field_71474_y.field_74314_A.func_151470_d()) {
                FreeCam.mc.field_71439_g.field_70181_x += (double)value;
            }
            if (FreeCam.mc.field_71474_y.field_74311_E.func_151470_d()) {
                FreeCam.mc.field_71439_g.field_70181_x -= (double)value;
            }
            MovementUtils.strafe(value);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer || packet instanceof C0BPacketEntityAction) {
            event.cancelEvent();
        }
    }
}

