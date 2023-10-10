/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick;

import org.newdawn.slick.Input;

public interface ControlledInputReciever {
    public void setInput(Input var1);

    public boolean isAcceptingInput();

    public void inputEnded();

    public void inputStarted();
}

