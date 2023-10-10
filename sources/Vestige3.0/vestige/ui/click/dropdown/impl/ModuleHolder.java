package vestige.ui.click.dropdown.impl;

import lombok.Getter;
import vestige.module.Module;
import vestige.setting.AbstractSetting;
import vestige.util.misc.TimerUtil;

import java.util.ArrayList;

@Getter
public class ModuleHolder {

    private Module module;

    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil settingsShownTimer = new TimerUtil();

    private boolean lastEnabled;

    private final ArrayList<SettingHolder> settings = new ArrayList<>();

    private boolean settingsShown;

    public ModuleHolder(Module m) {
        this.module = m;

        for(AbstractSetting s : m.getSettings()) {
            settings.add(new SettingHolder(s));
        }
    }

    public void updateState() {
        boolean enabled = module.isEnabled();

        if(enabled != lastEnabled) {
            timer.reset();
        }

        lastEnabled = module.isEnabled();
    }

    public void setSettingsShown(boolean shown) {
        if(settingsShown != shown) {
            this.settingsShown = shown;

            settingsShownTimer.reset();
        }
    }

}
