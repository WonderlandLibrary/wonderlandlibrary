// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import dev.tenacity.Tenacity;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class UserBinding
{
    public String uid() {
        return String.valueOf(Tenacity.INSTANCE.getIntentAccount().client_uid);
    }
    
    public String username() {
        return String.valueOf(Tenacity.INSTANCE.getIntentAccount().username);
    }
    
    public String discordTag() {
        return String.valueOf(Tenacity.INSTANCE.getIntentAccount().discord_tag);
    }
}
