/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd;

import org.celestial.client.cmd.CommandManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventMessage;

public class CommandHandler {
    private final CommandManager commandManager;

    public CommandHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @EventTarget
    public void onMessage(EventMessage event) {
        String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".")) {
            event.setCancelled(this.commandManager.execute(msg));
        }
    }
}

