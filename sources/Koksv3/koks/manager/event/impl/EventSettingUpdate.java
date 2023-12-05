package koks.manager.event.impl;

import koks.api.settings.Setting;
import koks.manager.event.Event;

public class EventSettingUpdate extends Event {

    private final Setting setting;

    public EventSettingUpdate(Setting setting) {
        this.setting = setting;
    }

    public Setting getSetting() {
        return setting;
    }
}
