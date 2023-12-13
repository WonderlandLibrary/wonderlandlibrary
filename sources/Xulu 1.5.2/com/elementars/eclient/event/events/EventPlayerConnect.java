// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import java.util.UUID;
import com.elementars.eclient.event.Event;

public class EventPlayerConnect extends Event
{
    UUID uuid;
    String name;
    
    public EventPlayerConnect(final UUID uuid, final String name) {
        this.uuid = uuid;
        this.name = name;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static class Join extends EventPlayerConnect
    {
        public Join(final UUID uuid, final String name) {
            super(uuid, name);
        }
    }
    
    public static class Leave extends EventPlayerConnect
    {
        public Leave(final UUID uuid, final String name) {
            super(uuid, name);
        }
    }
}
