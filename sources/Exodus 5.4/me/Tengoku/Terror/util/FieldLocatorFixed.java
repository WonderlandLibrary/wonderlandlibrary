/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.lang.reflect.Field;
import me.Tengoku.Terror.util.IFieldLocator;

public class FieldLocatorFixed
implements IFieldLocator {
    private Field field;

    public FieldLocatorFixed(Field field) {
        this.field = field;
    }

    @Override
    public Field getField() {
        return this.field;
    }
}

