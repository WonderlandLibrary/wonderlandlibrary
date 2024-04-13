package com.canon.majik.impl.command.impl;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.client.ChatUtils;
import com.canon.majik.impl.command.api.Command;
import com.canon.majik.impl.modules.api.Module;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind", "Allows You To Bind Modules To A Key", "bind" + " " + "[module]" + " " + "[key]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 2) {
            for (Module module : Initializer.moduleManager.getModules()) {
                if (module.getName().equalsIgnoreCase(args.get(0))) {
                    if (args.get(0).isEmpty()) {
                        ChatUtils.tempMessage("Please Only Enter One Character", 1);
                        return;
                    }
                    String bind = args.get(1);
                    int key = Keyboard.getKeyIndex(bind.toUpperCase());
                    if (key == 0) {
                        ChatUtils.tempMessage("Unknown Keybind", 1);
                        return;
                    }
                    module.setKey(key);
                    ChatUtils.tempMessage(module.getName() + " Has Been Bound To " + Keyboard.getKeyName(module.getKey()), 1);
                }
            }
        } else {
            ChatUtils.tempMessage(getSyntax() + "", 1);
        }
    }
}
