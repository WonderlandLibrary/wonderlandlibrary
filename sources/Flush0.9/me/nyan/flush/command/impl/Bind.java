package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.module.Module;
import me.nyan.flush.utils.other.ChatUtils;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind() {
        super("Bind", "Binds a module by name.", "bind <module> <key> | <clear> | <list>", "b");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 2) {
            Module module = flush.getModuleManager().getModule(args[0]);
            String keyname = args[1].toUpperCase();

            if (module != null) {
                module.clearKeys();
                module.addKey(Keyboard.getKeyIndex(keyname));

                if (keyname.equals("NONE")) {
                    ChatUtils.println("Unbound " + module.getName() + ".");
                } else {
                    ChatUtils.println("Bound " + module.getName() + " to " + module.getKeysString() + ".");
                }
                return;
            }
            ChatUtils.println("Module \"" + args[0] + "\" was not found.");
            return;
        } else if (args[0].equalsIgnoreCase("clear")) {
            flush.getModuleManager().getModules().forEach(Module::clearKeys);
            ChatUtils.println("Cleared all binds.");
            return;
        } else if (args[0].equalsIgnoreCase("list")) {
            int bound = 0;
            for (Module module : flush.getModuleManager().getModules()) {
                if (!module.getKeys().isEmpty()) {
                    bound++;
                }
            }

            if (bound > 0) {
                ChatUtils.println("§9Current binds: ");
                for (Module module : flush.getModuleManager().getModules()) {
                    if (!module.getKeys().isEmpty()) {
                        ChatUtils.println(module.getName() + ": " + module.getKeysString());
                    }
                }
                return;
            }

            ChatUtils.println("§4No modules are currently bound.");
            return;
        }

        sendSyntaxHelpMessage();
    }
}
