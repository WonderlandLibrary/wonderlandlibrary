/*
 * Decompiled with CFR 0.150.
 */
package markgg.command.impl;

import markgg.Client;
import markgg.command.Command;

public class Help
extends Command {
    public Help() {
        super("Help", "Shows client information", "help", "h");
    }

    @Override
    public void onCommand(String[] args, String command) {
        Client.addClearChatMessage();
        Client.addChatMessage(".bind <module> <key> - Binds a module to a key.");
        Client.addChatMessage(".toggle <module> - Toggles a module on or off.");
        Client.addChatMessage(".vclip <blocks> - Clip up or down.");
        Client.addChatMessage(".flip - Does a jonathan backflip.");
        Client.addChatMessage("To unbind in the clickgui press the spacebar!");
        Client.addClearChatMessage();
        Client.addChatMessage("Client is running version " + Client.version);
        Client.addClearChatMessage();
    }
}

