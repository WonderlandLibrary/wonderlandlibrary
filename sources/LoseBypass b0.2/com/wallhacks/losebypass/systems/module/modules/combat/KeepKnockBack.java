/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.combat;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.PacketReceiveEvent;
import com.wallhacks.losebypass.systems.module.Module;
import java.util.Iterator;
import net.minecraft.entity.DataWatcher;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;

@Module.Registration(name="KeepKnockBack", description="Resets sprint serverside to always deal knockback like the first hit", category=Module.Category.COMBAT)
public class KeepKnockBack
extends Module {
    @SubscribeEvent
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!(event.getPacket() instanceof S1CPacketEntityMetadata)) return;
        S1CPacketEntityMetadata p = (S1CPacketEntityMetadata)event.getPacket();
        if (p.getEntityId() != KeepKnockBack.mc.thePlayer.getEntityId()) return;
        Iterator<DataWatcher.WatchableObject> iterator = p.func_149376_c().iterator();
        while (iterator.hasNext()) {
            DataWatcher.WatchableObject data = iterator.next();
            if (data.getDataValueId() != 0) continue;
            KeepKnockBack.mc.thePlayer.serverSprintState = ((Byte)data.getObject() & 8) != 0;
        }
    }
}

