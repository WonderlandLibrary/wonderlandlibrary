package com.canon.majik.api.utils.turok.hardware.mouse;

import org.lwjgl.input.Mouse;

/**
 * @author Rina.
 * @since 02/10/2020.
 */
public class TurokMouse {
    private int scroll;

    private int x;
    private int y;

    public TurokMouse(int mx, int my) {
        this.x = mx;
        this.y = my;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScroll() {
        return -(Mouse.getDWheel() / 10);
    }

    public boolean hasWheel() {
        return Mouse.hasWheel();
    }
}
