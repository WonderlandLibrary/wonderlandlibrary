// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import net.minecraft.util.IChatComponent;
import dev.tenacity.event.Event;

public class ChatReceivedEvent extends Event
{
    public final byte type;
    public IChatComponent message;
    private final String rawMessage;
    
    public ChatReceivedEvent(final byte type, final IChatComponent message) {
        this.type = type;
        this.message = message;
        this.rawMessage = message.getUnformattedText();
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public String getRawMessage() {
        return this.rawMessage;
    }
}
