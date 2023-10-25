/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.cursor;

import com.sun.javafx.cursor.CursorType;
import java.util.HashMap;
import java.util.Map;

public abstract class CursorFrame {
    private Class<?> firstPlatformCursorClass;
    private Object firstPlatformCursor;
    private Map<Class<?>, Object> otherPlatformCursors;

    public abstract CursorType getCursorType();

    public <T> T getPlatformCursor(Class<T> class_) {
        if (this.firstPlatformCursorClass == class_) {
            return (T)this.firstPlatformCursor;
        }
        if (this.otherPlatformCursors != null) {
            return (T)this.otherPlatformCursors.get(class_);
        }
        return null;
    }

    public <T> void setPlatforCursor(Class<T> class_, T t) {
        if (this.firstPlatformCursorClass == null || this.firstPlatformCursorClass == class_) {
            this.firstPlatformCursorClass = class_;
            this.firstPlatformCursor = t;
            return;
        }
        if (this.otherPlatformCursors == null) {
            this.otherPlatformCursors = new HashMap();
        }
        this.otherPlatformCursors.put(class_, t);
    }
}

