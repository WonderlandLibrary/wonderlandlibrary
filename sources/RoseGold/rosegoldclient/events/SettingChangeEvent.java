package rosegoldclient.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class SettingChangeEvent extends Event {
    public String setting;

    public SettingChangeEvent(String setting) {
        this.setting = setting;
    }
}
