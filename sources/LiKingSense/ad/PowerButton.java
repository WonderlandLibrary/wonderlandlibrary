/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package ad;

import org.lwjgl.input.Mouse;

public class PowerButton {
    private final int button;
    public boolean clicked;

    public PowerButton(int n) {
        this.button = n;
    }

    public boolean canExcecute() {
        if (Mouse.isButtonDown((int)this.button)) {
            if (!this.clicked) {
                this.clicked = true;
                return true;
            }
        } else {
            this.clicked = false;
        }
        return false;
    }
}

