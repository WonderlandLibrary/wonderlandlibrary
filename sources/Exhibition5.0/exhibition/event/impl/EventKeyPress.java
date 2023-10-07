// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import exhibition.event.Event;

public class EventKeyPress extends Event
{
    private int key;
    
    public void fire(final int key) {
        this.key = key;
        super.fire();
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
}
