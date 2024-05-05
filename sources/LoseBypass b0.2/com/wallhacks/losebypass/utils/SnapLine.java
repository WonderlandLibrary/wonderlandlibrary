/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import com.wallhacks.losebypass.systems.hud.HudComponent;

public class SnapLine {
    public int offset;
    public HudComponent parent;
    public Type type;

    public SnapLine(Type type, int offset) {
        this.type = type;
        this.offset = offset;
    }

    public SnapLine(Type type, int offset, HudComponent parent) {
        this.type = type;
        this.offset = offset;
        this.parent = parent;
    }

    public static enum Type {
        X,
        Y;

    }
}

