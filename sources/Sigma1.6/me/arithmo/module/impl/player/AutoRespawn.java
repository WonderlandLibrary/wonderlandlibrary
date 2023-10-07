/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class AutoRespawn
extends Module {
    public AutoRespawn(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        if (AutoRespawn.mc.thePlayer.isDead) {
            AutoRespawn.mc.thePlayer.respawnPlayer();
        }
    }
}

