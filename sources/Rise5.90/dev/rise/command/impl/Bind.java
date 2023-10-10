package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.module.Module;
import org.lwjgl.input.Keyboard;

@CommandInfo(name = "Bind", description = "Binds the given module to the given key", syntax = ".bind <module> <key>", aliases = "bind")
public final class Bind extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        final Module[] modules = Rise.INSTANCE.getModuleManager().getModuleList();

        for (final Module m : modules) {
            if (args[0].equalsIgnoreCase(m.getModuleInfo().name())) {
                args[1] = args[1].toUpperCase();
                final int key = Keyboard.getKeyIndex(args[1]);

                m.setKeyBind(key);

                Rise.INSTANCE.getNotificationManager().registerNotification("Set " + m.getModuleInfo().name() + "'s bind to " + Keyboard.getKeyName(key) + ".");
                return;
            }
        }

//        for (final Script script : Rise.INSTANCE.getScriptManager().getScripts()) {
//            if (args[0].equalsIgnoreCase(script.getName())) {
//                args[1] = args[1].toUpperCase();
//                final int key = Keyboard.getKeyIndex(args[1]);
//
//                script.setKey(key);
//
//                Rise.INSTANCE.getNotificationManager().registerNotification("Set " + script.getName() + "'s bind to " + Keyboard.getKeyName(key) + ".");
//                return;
//            }
//        }

        Rise.INSTANCE.getNotificationManager().registerNotification("Couldn't find module.");
    }
}
