/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.command;

import me.kiras.aimwhere.libraries.slick.command.Control;

public class MouseButtonControl
implements Control {
    private int button;

    public MouseButtonControl(int button) {
        this.button = button;
    }

    public boolean equals(Object o) {
        if (o instanceof MouseButtonControl) {
            return ((MouseButtonControl)o).button == this.button;
        }
        return false;
    }

    public int hashCode() {
        return this.button;
    }
}

