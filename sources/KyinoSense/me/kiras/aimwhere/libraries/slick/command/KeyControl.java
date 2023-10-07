/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.command;

import me.kiras.aimwhere.libraries.slick.command.Control;

public class KeyControl
implements Control {
    private int keycode;

    public KeyControl(int keycode) {
        this.keycode = keycode;
    }

    public boolean equals(Object o) {
        if (o instanceof KeyControl) {
            return ((KeyControl)o).keycode == this.keycode;
        }
        return false;
    }

    public int hashCode() {
        return this.keycode;
    }
}

