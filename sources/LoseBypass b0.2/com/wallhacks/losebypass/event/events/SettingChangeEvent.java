/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.events;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.eventbus.Event;
import com.wallhacks.losebypass.systems.setting.Setting;

public class SettingChangeEvent
extends Event {
    Setting<?> setting;

    public SettingChangeEvent(Setting<?> setting) {
        this.setting = setting;
        LoseBypass.eventBus.post(this);
    }

    public Setting<?> getSetting() {
        return this.setting;
    }
}

