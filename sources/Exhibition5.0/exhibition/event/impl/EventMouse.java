// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event.impl;

import exhibition.event.Event;

public class EventMouse extends Event
{
    private int buttonID;
    private boolean mouseDown;
    
    public void fire(final int buttonID, final boolean mouseDown) {
        this.buttonID = buttonID;
        super.fire();
    }
    
    public int getButtonID() {
        return this.buttonID;
    }
    
    public void setButtonID(final int buttonID) {
        this.buttonID = buttonID;
    }
    
    public boolean isMouseDown() {
        return this.mouseDown;
    }
    
    public boolean isMotionEvent() {
        return this.buttonID == -1;
    }
    
    public void setMouseDown(final boolean mouseDown) {
        this.mouseDown = mouseDown;
    }
}
