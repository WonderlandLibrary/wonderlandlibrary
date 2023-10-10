/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.command;

import org.newdawn.slick.command.Control;

public class KeyControl
implements Control {
    private int keycode;

    public KeyControl(int keycode) {
        this.keycode = keycode;
    }

    public boolean equals(Object o2) {
        if (o2 instanceof KeyControl) {
            return ((KeyControl)o2).keycode == this.keycode;
        }
        return false;
    }

    public int hashCode() {
        return this.keycode;
    }
}

