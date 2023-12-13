// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.event.events;

import net.minecraft.util.IChatComponent;
import Lavish.event.Event;

public class EventChat extends Event<EventChat>
{
    private IChatComponent chat;
    private boolean incoming;
    public String message;
    
    public EventChat(final IChatComponent chat) {
        this.chat = chat;
    }
    
    public IChatComponent getChatComponent() {
        return this.chat;
    }
    
    public void setChat(final IChatComponent chat) {
        this.chat = chat;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
