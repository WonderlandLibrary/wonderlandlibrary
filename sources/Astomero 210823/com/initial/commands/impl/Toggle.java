package com.initial.commands.impl;

import com.initial.commands.*;
import com.initial.*;
import com.initial.modules.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import java.util.*;

public class Toggle extends Command
{
    public Toggle() {
        super("Toggle", "Toggles a module by name.", "toggle <module name>", new String[] { "t" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            final String moduleName = args[0];
            boolean foundModule = false;
            for (final Module module : Astomero.instance.moduleManager.getModules()) {
                if (module.name.equalsIgnoreCase(moduleName)) {
                    module.toggle();
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(module.isEnabled() ? (module.name + " was enabled") : (module.name + " was disabled")));
                    foundModule = true;
                    break;
                }
            }
            if (!foundModule) {
                Astomero.addChatMessage("§8> §aCould not find the module.");
            }
        }
    }
}
