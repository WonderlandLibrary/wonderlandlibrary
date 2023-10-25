/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.cursor;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.CursorType;

public final class StandardCursorFrame
extends CursorFrame {
    private CursorType cursorType;

    public StandardCursorFrame(CursorType cursorType) {
        this.cursorType = cursorType;
    }

    @Override
    public CursorType getCursorType() {
        return this.cursorType;
    }
}

