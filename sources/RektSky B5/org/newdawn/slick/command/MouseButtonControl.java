/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.command;

import org.newdawn.slick.command.Control;

public class MouseButtonControl
implements Control {
    private int button;

    public MouseButtonControl(int button) {
        this.button = button;
    }

    public boolean equals(Object o2) {
        if (o2 instanceof MouseButtonControl) {
            return ((MouseButtonControl)o2).button == this.button;
        }
        return false;
    }

    public int hashCode() {
        return this.button;
    }
}

