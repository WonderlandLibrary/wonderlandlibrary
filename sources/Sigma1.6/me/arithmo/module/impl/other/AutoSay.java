/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.other;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.Timer;
import me.arithmo.util.misc.ChatUtil;

public class AutoSay
extends Module {
    Timer timer = new Timer();
    public static final String WORDS = "SAY";
    public final String DELAY = "DELAY";

    public AutoSay(ModuleData data) {
        super(data);
        this.settings.put("SAY", new Setting<String>("SAY", "/warp", "Message to send."));
        this.settings.put("DELAY", new Setting<Integer>("DELAY", 500, "MS delay between messages.", 100.0, 100.0, 10000.0));
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        String message = ((String)((Setting)this.settings.get("SAY")).getValue()).toString();
        if (this.timer.delay(((Number)((Setting)this.settings.get("DELAY")).getValue()).longValue())) {
            ChatUtil.sendChat(message + " " + (int)(Math.random() * 100000.0));
            this.timer.reset();
        }
    }
}

