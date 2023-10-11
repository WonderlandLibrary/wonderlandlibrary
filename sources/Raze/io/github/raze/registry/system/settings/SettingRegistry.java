package io.github.raze.registry.system.settings;

import de.florianmichael.rclasses.storage.Storage;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.settings.system.BaseSetting;

import java.util.List;
import java.util.stream.Collectors;

public class SettingRegistry extends Storage<BaseSetting> {

    public BaseSetting getSetting(String input, AbstractModule parent) {
        return getSettingsByModule(parent).stream().filter(setting -> setting.getName().equalsIgnoreCase(input)).findFirst().get();
    }

    public List<BaseSetting> getSettingsByModule(AbstractModule parent) {
        return this.getList().stream().filter(setting -> setting.getParent() == parent).collect(Collectors.toList());
    }

    @Override
    public void init() {

    }
}
