// 
// Decompiled by Procyon v0.5.36
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
        catch (Exception e) {
            System.err.println(e + " ErrorEvent");
        }
    }
}
