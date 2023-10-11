package io.github.raze.registry.collection;

import io.github.raze.events.system.manager.EventManager;
import io.github.raze.registry.system.commands.CommandRegistry;
import io.github.raze.registry.system.settings.SettingRegistry;
import io.github.raze.registry.system.modules.ModuleRegistry;
import io.github.raze.registry.system.themes.ThemeRegistry;

public class ManagerRegistry {

    public final EventManager eventManager = new EventManager();
    public final SettingRegistry settingRegistry = new SettingRegistry();
    public final ModuleRegistry moduleRegistry = new ModuleRegistry();
    public final CommandRegistry commandRegistry = new CommandRegistry();
    public final ThemeRegistry themeRegistry = new ThemeRegistry();

}
