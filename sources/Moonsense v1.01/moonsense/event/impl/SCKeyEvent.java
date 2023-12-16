// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event.impl;

import org.lwjgl.input.Keyboard;
import moonsense.event.SCEvent;
import moonsense.event.SCEventCancellable;

public class SCKeyEvent extends SCEventCancellable
{
    private final int key;
    
    public SCKeyEvent(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
    
    @Override
    public SCEvent call() {
        try {
            if (Keyboard.isKeyDown(this.key)) {
                return super.call();
            }
            return this;
        }
        catch (IndexOutOfBoundsException e) {
            return this;
        }
    }
}
