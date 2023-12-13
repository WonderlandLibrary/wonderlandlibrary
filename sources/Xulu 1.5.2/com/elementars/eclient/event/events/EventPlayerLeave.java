// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import com.mojang.authlib.GameProfile;
import com.elementars.eclient.event.Event;

public class EventPlayerLeave extends Event
{
    GameProfile gameProfile;
    
    public EventPlayerLeave(final GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
}
