package com.initial.commands.impl;

import com.initial.commands.*;
import com.initial.*;
import com.initial.modules.*;
import java.util.*;

public class Hide extends Command
{
    public Hide() {
        super("Hide", "Hides a module by name", "hide <module name>", new String[] { "h" });
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("clear")) {
                for (final Module m : Astomero.instance.moduleManager.getModules()) {
                    m.setVisible(true);
                }
                return;
            }
            for (final Module m : Astomero.instance.moduleManager.getModules()) {
                if (m.getName().equalsIgnoreCase(args[0])) {
                    Astomero.addChatMessage((m.isVisible() ? "Hidden " : "Now showing ") + m.getName());
                    m.setVisible(!m.isVisible());
                }
            }
        }
    }
}
