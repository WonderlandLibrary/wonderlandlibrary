/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick;

import me.kiras.aimwhere.libraries.slick.Input;

public interface ControlledInputReciever {
    public void setInput(Input var1);

    public boolean isAcceptingInput();

    public void inputEnded();

    public void inputStarted();
}

