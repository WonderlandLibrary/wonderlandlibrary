package ravenweave.client.module.modules.config;

import org.lwjgl.input.Keyboard;
import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;

public class ConfigModule extends Module {

    public static ConfigModule currentConfig;
    public boolean checked;

    public ConfigModule(String cfgName) {
        super(cfgName, Module.ModuleCategory.config);
        clientConfig = true;
        showInHud = false;
    }

    @Override
    public void toggle() {
        Raven.configManager.save();
        Raven.configManager.loadConfigByName(getName());
        currentConfig = this;
    }

    @Override
    public boolean isEnabled() {
        return currentConfig == this;
    }

    @Override
    public void keybind() {
        if (!this.isEnabled() && this.keycode != 0 && Keyboard.isKeyDown(this.keycode)) {
            this.toggle();
        }
    }
}
