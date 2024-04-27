/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 */
package net.dev.important.modules.module.modules.player;

import java.util.ArrayList;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.TimerUtil;
import net.dev.important.utils.timer.TimeHelper;
import net.dev.important.value.FloatValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import oh.yalan.NativeClass;

@NativeClass
@Info(name="AntiVoid", description="Anti fall.", category=Category.PLAYER, cnName="\u53cd\u865a\u7a7a\u6389\u843d")
public class AntiVoid
extends Module {
    public double[] lastGroundPos = new double[3];
    public static FloatValue pullbackTime = new FloatValue("VoidBack-Delay", 800.0f, 500.0f, 1500.0f);
    private final TimerUtil pullTimer = new TimerUtil();
    public static TimeHelper timer = new TimeHelper();
    public static ArrayList<C03PacketPlayer> packets = new ArrayList();
    private String mValue = "Hypixel";
    static final AntiVoid antifall = (AntiVoid)Client.moduleManager.getModule(AntiVoid.class);

    public static boolean isInVoid() {
        for (int i = 0; i <= 128; ++i) {
            if (!MovementUtils.isOnGround(i)) continue;
            return false;
        }
        return true;
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (!packets.isEmpty() && AntiVoid.mc.field_71439_g.field_70173_aa < 100) {
            packets.clear();
        }
        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
            if (AntiVoid.isInVoid()) {
                event.cancelEvent();
                packets.add(packet);
                if (timer.isDelayComplete(((Float)pullbackTime.get()).floatValue())) {
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)new C03PacketPlayer.C04PacketPlayerPosition(this.lastGroundPos[0], this.lastGroundPos[1] - 1.0, this.lastGroundPos[2], true));
                }
            } else {
                this.lastGroundPos[0] = AntiVoid.mc.field_71439_g.field_70165_t;
                this.lastGroundPos[1] = AntiVoid.mc.field_71439_g.field_70163_u;
                this.lastGroundPos[2] = AntiVoid.mc.field_71439_g.field_70161_v;
                if (!packets.isEmpty()) {
                    for (Packet packet2 : packets) {
                        PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)packet2);
                    }
                    packets.clear();
                }
                timer.reset();
            }
        }
    }

    @EventTarget
    public void onRevPacket(PacketEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && packets.size() > 1) {
            packets.clear();
        }
    }

    public static boolean isPullbacking() {
        return antifall.getState() && !packets.isEmpty();
    }

    @Override
    public String getTag() {
        return this.mValue;
    }
}

