// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.events;

public class EventChat extends Event
{
    private String message;
    
    public EventChat(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
