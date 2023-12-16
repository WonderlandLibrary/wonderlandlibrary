// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import org.lwjgl.input.Mouse;
import moonsense.event.SCEventCancellable;

public class SCMouseEvent extends SCEventCancellable
{
    public final int x;
    public final int y;
    public final int dx;
    public final int dy;
    public final int dwheel;
    public final int button;
    public final boolean buttonState;
    public final long nanoseconds;
    
    public SCMouseEvent() {
        this.x = Mouse.getEventX();
        this.y = Mouse.getEventY();
        this.dx = Mouse.getEventDX();
        this.dy = Mouse.getEventDY();
        this.dwheel = Mouse.getEventDWheel();
        this.button = Mouse.getEventButton();
        this.buttonState = Mouse.getEventButtonState();
        this.nanoseconds = Mouse.getEventNanoseconds();
    }
}
