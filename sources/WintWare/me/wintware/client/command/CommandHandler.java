/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.command;

import me.wintware.client.command.CommandManager;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.SendMessageEvent;

public final class CommandHandler {
    private final CommandManager commandManager;

    public CommandHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @EventTarget
    public final void onMessage(SendMessageEvent event) {
        String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".")) {
            event.setCancelled(this.commandManager.execute(msg));
        }
    }
}

