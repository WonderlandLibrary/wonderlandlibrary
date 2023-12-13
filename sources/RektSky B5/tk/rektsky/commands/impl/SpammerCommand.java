/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import tk.rektsky.commands.Command;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.player.Spammer;

public class SpammerCommand
extends Command {
    public SpammerCommand() {
        super("spammer", "[Message]", "Set the spamming message");
    }

    @Override
    public void onCommand(String label, String[] args) {
        StringBuilder messageBuilder = new StringBuilder();
        for (String arg : args) {
            messageBuilder.append(arg + " ");
        }
        String message = messageBuilder.toString();
        message = message.substring(0, message.length() - 1);
        ModulesManager.getModuleByClass(Spammer.class).message.setValue(message);
    }
}

