/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import net.minecraft.client.Minecraft;

public class FastPlace
extends Module {
    private static final String KEY_TIMES = "CLICKSPEED";

    public FastPlace(ModuleData data) {
        super(data);
        this.settings.put("CLICKSPEED", new Setting<Integer>("CLICKSPEED", 4, "Tick delay between clicks.", 1.0, 0.0, 20.0));
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        FastPlace.mc.rightClickDelayTimer = Math.min(FastPlace.mc.rightClickDelayTimer, 1);
    }
}

