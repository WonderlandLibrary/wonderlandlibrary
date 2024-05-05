// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventMessage;

public class CommandHandler
{
    public CommandManager commandManager;
    
    public CommandHandler(final CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    
    @EventTarget
    public void onMessage(final EventMessage event) {
        final String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".")) {
            event.setCancelled(this.commandManager.execute(msg));
        }
    }
}
