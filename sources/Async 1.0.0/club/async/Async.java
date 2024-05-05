package club.async;

import club.async.clickgui.dropdown.ClickGUI;
import club.async.command.CommandRegistry;
import club.async.event.impl.EventChat;
import club.async.event.impl.EventKey;
import club.async.fontrenderer.FontManager;
import club.async.module.Module;
import club.async.module.ModuleManager;
import club.async.util.ModuleSaver;
import rip.hippo.lwjeb.annotation.Handler;
import rip.hippo.lwjeb.bus.PubSub;
import rip.hippo.lwjeb.configuration.BusConfigurations;
import rip.hippo.lwjeb.configuration.config.impl.BusPubSubConfiguration;
import rip.hippo.lwjeb.message.scan.impl.MethodAndFieldBasedMessageScanner;

public enum Async {

    INSTANCE;

    private final String name = "Async", version = "1.0.0", dev = "Exeos, CodeMan, ClientSiders";

    private final PubSub<String> pubSub = new PubSub<>(new BusConfigurations.Builder().setConfiguration(BusPubSubConfiguration.class, () -> {
                BusPubSubConfiguration busPubSubConfiguration = BusPubSubConfiguration.getDefault();
                busPubSubConfiguration.setScanner(new MethodAndFieldBasedMessageScanner<>());
                return busPubSubConfiguration;
            }).build());

    private FontManager fontManager;
    private ModuleManager moduleManager;
    private CommandRegistry commandRegistry;
    private ClickGUI dropDown;
    private club.async.clickgui.flat.ClickGUI flat;

    /*
    Initializing the Client
    */

    public final void startUp() {
        commandRegistry = new CommandRegistry();
        moduleManager = new ModuleManager();
        dropDown = new ClickGUI();
        flat = new club.async.clickgui.flat.ClickGUI();
        fontManager = new FontManager();

        ModuleSaver.load();

        pubSub.subscribe(this);
    }

    /*
    Saving keybindings, enabled Modules and settings
    */

    public final void shutDown() {
        ModuleSaver.save();
    }

    @Handler
    public final void onKey(EventKey eventKey) {
        for (Module m : moduleManager.getModules())
        {
            if (eventKey.getKey() == m.getKey())
                m.toggle();
        }
    }

    @Handler
    public final void onChat(EventChat event) {
        if(event.getMessage().startsWith(".")) {
            event.setCancelled(true);
            commandRegistry.execute(event.getMessage().substring(1));
        }
    }

    /*
    Some getter and setters
    */

    public final PubSub getPubSub() {
        return pubSub;
    }
    public final String getName() {
        return name;
    }
    public final String getVersion() {
        return version;
    }
    public final String getDev() {
        return dev;
    }
    public final ClickGUI getDropDown() {
        return dropDown;
    }
    public final club.async.clickgui.flat.ClickGUI getFlat() {
        return flat;
    }
    public final ModuleManager getModuleManager() {
        return moduleManager;
    }
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
    public final FontManager getFontManager() {
        return fontManager;
    }

}
