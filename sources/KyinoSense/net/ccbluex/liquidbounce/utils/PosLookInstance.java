/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 */
package net.ccbluex.liquidbounce.utils;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class PosLookInstance {
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private float yaw = 0.0f;
    private float pitch = 0.0f;

    public PosLookInstance() {
    }

    public PosLookInstance(double a, double b, double c, float d, float e) {
        this.x = a;
        this.y = b;
        this.z = c;
        this.yaw = d;
        this.pitch = e;
    }

    public void reset() {
        this.set(0.0, 0.0, 0.0, 0.0f, 0.0f);
    }

    public void set(S08PacketPlayerPosLook packet) {
        this.set(packet.func_148932_c(), packet.func_148928_d(), packet.func_148933_e(), packet.field_148936_d, packet.field_148937_e);
    }

    public void set(double a, double b, double c, float d, float e) {
        this.x = a;
        this.y = b;
        this.z = c;
        this.yaw = d;
        this.pitch = e;
    }

    public boolean equalFlag(C03PacketPlayer.C06PacketPlayerPosLook packet) {
        return packet != null && !packet.field_149474_g && packet.field_149479_a == this.x && packet.field_149477_b == this.y && packet.field_149478_c == this.z && packet.field_149476_e == this.yaw && packet.field_149473_f == this.pitch;
    }
}

