/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package net.minecraft.util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class MouseHelper {
    public int deltaX;
    public int deltaY;
    private static final String __OBFID = "CL_00000648";

    public void grabMouseCursor() {
        Mouse.setGrabbed((boolean)true);
        this.deltaX = 0;
        this.deltaY = 0;
    }

    public void ungrabMouseCursor() {
        Mouse.setCursorPosition((int)(Display.getWidth() / 2), (int)(Display.getHeight() / 2));
        Mouse.setGrabbed((boolean)false);
    }

    public void mouseGrab(boolean t) {
        Mouse.setGrabbed((boolean)t);
    }

    public void centerMouse() {
        Mouse.setCursorPosition((int)(Display.getWidth() / 2), (int)(Display.getHeight() / 2));
    }

    public void mouseXYChange() {
        this.deltaX = Mouse.getDX();
        this.deltaY = Mouse.getDY();
    }
}

