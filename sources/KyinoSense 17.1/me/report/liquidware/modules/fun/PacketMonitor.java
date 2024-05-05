/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  org.jetbrains.annotations.Nullable
 */
package me.report.liquidware.modules.fun;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.Logger;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.network.play.client.C03PacketPlayer;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="PacketMonitor", description="qwq", category=ModuleCategory.RENDER)
public class PacketMonitor
extends Module {
    public static int packetcounter;
    public static MSTimer timer;

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            ++packetcounter;
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onUpdate(UpdateEvent e) {
        if (!LiquidBounce.moduleManager.getModule(Fly.class).getState()) {
            if (packetcounter > 22) {
                Logger.printinfo("\u00a7c\u8b66\u544a\uff01Packet\u53d1\u9001\u6570\u91cf\u4e0d\u6b63\u5e38! Packets:" + packetcounter);
                ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lKyinoClient\u00a78] \u00a73Now Packets!" + packetcounter);
            }
            packetcounter = 0;
            timer.reset();
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    @Nullable
    public String getTag() {
        return packetcounter + "";
    }

    static {
        timer = new MSTimer();
    }
}

