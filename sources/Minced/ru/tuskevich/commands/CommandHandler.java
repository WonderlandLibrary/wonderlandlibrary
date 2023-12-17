// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands;

import ru.tuskevich.event.EventTarget;
import ru.tuskevich.event.events.impl.EventMessage;

public class CommandHandler
{
    public CommandManager commandManager;
    
    public CommandHandler(final CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    
    @EventTarget
    public void onMessage(final EventMessage event) {
        final String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".") && this.commandManager.execute(msg)) {
            event.cancel();
        }
    }
}
