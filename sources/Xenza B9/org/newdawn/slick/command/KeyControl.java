// 
// Decompiled by Procyon v0.6.0
// 

package org.newdawn.slick.command;

public class KeyControl implements Control
{
    private int keycode;
    
    public KeyControl(final int keycode) {
        this.keycode = keycode;
    }
    
    public boolean equals(final Object o) {
        return o instanceof KeyControl && ((KeyControl)o).keycode == this.keycode;
    }
    
    public int hashCode() {
        return this.keycode;
    }
}
