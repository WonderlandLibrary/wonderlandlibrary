// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import exhibition.event.Event;

public class EventChat extends Event
{
    private String text;
    
    public void fire(final String text) {
        this.text = text;
        super.fire();
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
}
