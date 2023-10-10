package ravenweave.client.module.modules.config;

import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.TickSetting;

public class ConfigSettings extends Module {

    public TickSetting updateConfig;

    public ConfigSettings() {
        super("Config", ModuleCategory.config);
        this.registerSetting(updateConfig = new TickSetting("Update ConfigList", false));
    }

    @Override
    public boolean canBeEnabled() {
        return false;
    }

    public void guiButtonToggled(TickSetting b) {
        b.setEnabled(false);
        Raven.configManager.discoverConfigs();
    }
}
