package vestige.ui.click.dropdown.impl;

import lombok.Getter;
import lombok.Setter;
import vestige.setting.AbstractSetting;
import vestige.util.misc.TimerUtil;

public class SettingHolder {

    private AbstractSetting setting;

    @Getter
    @Setter
    private boolean holdingMouse;

    @Getter
    private final TimerUtil timer = new TimerUtil();

    public SettingHolder(AbstractSetting s) {
        this.setting = s;
    }

    public void click() {
        timer.reset();
    }

    public <T extends AbstractSetting> T getSetting() {
        return (T) setting;
    }

}
