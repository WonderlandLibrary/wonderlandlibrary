/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.module.Module;
import me.arithmo.util.misc.ChatUtil;

public class Toggle
extends Command {
    public Toggle(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        Module module = null;
        if (args.length > 0) {
            module = Client.getModuleManager().get(args[0]);
        }
        if (module == null) {
            this.printUsage();
            return;
        }
        if (args.length == 1) {
            module.toggle();
            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + module.getName() + " has been" + (module.isEnabled() ? "\u00a7a Enabled." : "\u00a7c Disabled."));
        }
    }

    @Override
    public String getUsage() {
        return "toggle <module name>";
    }

    public void onEvent(Event event) {
    }
}

