// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import net.lenni0451.eventapi.manager.EventManager;
import net.lenni0451.eventapi.events.IEvent;

public class EventHandler
{
    public static void call(final IEvent event) {
        try {
            EventManager.call(event);
        }
        catch (final Exception var2) {
            System.err.println(var2 + " ErrorEvent");
        }
    }
}
