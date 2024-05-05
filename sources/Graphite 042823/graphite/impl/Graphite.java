package graphite.impl;

import graphite.api.management.config.ConfigManager;
import graphite.api.management.config.ConfigManagerImpl;
import graphite.api.event.PubSub;
import graphite.api.management.file.FileManager;
import graphite.api.management.file.FileManagerImpl;
import graphite.api.management.font.FontManager;
import graphite.api.management.font.FontManagerImpl;
import graphite.api.management.module.ModuleManager;
import graphite.api.management.module.ModuleManagerImpl;
import graphite.impl.event.Event;
import graphite.impl.gui.click.ClickGUI;

public enum Graphite {
    INSTANCE;

    public static final String NAME = "Graphite";

    public static final String VERSION = "042823";
    
    public static final boolean DEVELOPER_MODE = true;

    private static final PubSub<Event> eventBus = PubSub.newInstance();

    private static final FontManager fontManager = new FontManagerImpl();

    private static final ModuleManager moduleManager = new ModuleManagerImpl();

    private static final FileManager fileManager = new FileManagerImpl();

    private static final ConfigManager configManager = new ConfigManagerImpl();

    private static final ClickGUI ui = new ClickGUI();

    public void initiate() {
        ui.init();

        this.getConfigManager().load("default");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.getConfigManager().save("default");
        }));
    }

    public PubSub<Event> getEventBus() {
        return eventBus;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
