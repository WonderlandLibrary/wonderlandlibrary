/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  joptsimple.internal.Strings
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C14PacketTabComplete
 *  net.minecraft.network.play.server.S3APacketTabComplete
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import java.util.Collections;
import joptsimple.internal.Strings;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

@ModuleInfo(name="Plugins", description="Allows you to see which plugins the server is using.", category=ModuleCategory.FUN)
public class Plugins
extends Module {
    private final TickTimer tickTimer = new TickTimer();

    @Override
    public void onEnable() {
        if (Plugins.mc.field_71439_g == null) {
            return;
        }
        mc.func_147114_u().func_147297_a((Packet)new C14PacketTabComplete("/"));
        this.tickTimer.reset();
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.tickTimer.update();
        if (this.tickTimer.hasTimePassed(20)) {
            ClientUtils.displayChatMessage("\u00a7cPlugins check timed out...");
            this.tickTimer.reset();
            this.setState(false);
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S3APacketTabComplete) {
            String[] commands;
            S3APacketTabComplete s3APacketTabComplete = (S3APacketTabComplete)event.getPacket();
            ArrayList<String> plugins = new ArrayList<String>();
            for (String command1 : commands = s3APacketTabComplete.func_149630_c()) {
                String pluginName;
                String[] command = command1.split(":");
                if (command.length <= 1 || plugins.contains(pluginName = command[0].replace("/", ""))) continue;
                plugins.add(pluginName);
            }
            Collections.sort(plugins);
            if (!plugins.isEmpty()) {
                ClientUtils.displayChatMessage("\u00a7aPlugins \u00a77(\u00a78" + plugins.size() + "\u00a77): \u00a7c" + Strings.join((String[])plugins.toArray(new String[0]), (String)"\u00a77, \u00a7c"));
            } else {
                ClientUtils.displayChatMessage("\u00a7cNo plugins found.");
            }
            this.setState(false);
            this.tickTimer.reset();
        }
    }
}

