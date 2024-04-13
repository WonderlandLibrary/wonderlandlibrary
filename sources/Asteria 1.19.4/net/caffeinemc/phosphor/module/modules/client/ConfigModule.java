package net.caffeinemc.phosphor.module.modules.client;

import imgui.ImGui;
import imgui.type.ImInt;
import net.caffeinemc.phosphor.api.config.ConfigManager;
import net.caffeinemc.phosphor.module.Module;

import java.util.ArrayList;
import java.util.List;

import static net.caffeinemc.phosphor.common.Phosphor.configManager;

public class ConfigModule extends Module {
    public ConfigModule() {
        super("Config", "Asteria's configuration", Category.CLIENT);
    }

    @Override
    public void enable() {}

    private final List<String> names = new ArrayList<>();

    @Override
    public void renderSettings() {
        configManager().renderGui();
    }
}
