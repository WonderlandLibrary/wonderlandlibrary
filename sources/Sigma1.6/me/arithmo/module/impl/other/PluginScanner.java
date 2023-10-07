/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.other;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.Timer;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3APacketTabComplete;

public class PluginScanner
extends Module {
    boolean isListening;
    Timer timer = new Timer();

    public PluginScanner(ModuleData data) {
        super(data);
    }

    @Override
    public void onEnable() {
        this.isListening = true;
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        this.isListening = false;
        this.timer.reset();
    }

    @RegisterEvent(events={EventPacket.class})
    public void onEvent(Event event) {
        EventPacket ep = (EventPacket)event;
        if (ep.isIncoming() && ep.getPacket() instanceof S3APacketTabComplete && this.isListening && this.timer.delay(20000.0f)) {
            S3APacketTabComplete packet = (S3APacketTabComplete)ep.getPacket();
            String[] pluginsFound = packet.func_149630_c();
            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a73Found \u00a77[\u00a73" + pluginsFound.length + "\u00a77] \u00a73plugin(s): \u00a78" + pluginsFound + "\u00a73.");
            this.toggle();
        }
    }
}

