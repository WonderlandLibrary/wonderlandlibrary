/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.InputMethodRequests
 */
package com.sun.javafx.scene.input;

import javafx.scene.input.InputMethodRequests;

public interface ExtendedInputMethodRequests
extends InputMethodRequests {
    public int getInsertPositionOffset();

    public String getCommittedText(int var1, int var2);

    public int getCommittedTextLength();
}

