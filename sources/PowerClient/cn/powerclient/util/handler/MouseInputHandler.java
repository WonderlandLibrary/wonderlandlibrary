/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util.handler;

import org.lwjgl.input.Mouse;

public class MouseInputHandler {
    public boolean clicked;
    private int button;

    public MouseInputHandler(int key) {
        this.button = key;
    }

    public boolean canExcecute() {
        if (Mouse.isButtonDown(this.button)) {
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

