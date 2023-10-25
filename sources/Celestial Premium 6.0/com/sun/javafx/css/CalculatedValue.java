/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.StyleOrigin
 */
package com.sun.javafx.css;

import javafx.css.StyleOrigin;

public final class CalculatedValue {
    public static final CalculatedValue SKIP = new CalculatedValue(new int[0], null, false);
    private final Object value;
    private final StyleOrigin origin;
    private final boolean relative;

    public CalculatedValue(Object object, StyleOrigin styleOrigin, boolean bl) {
        this.value = object;
        this.origin = styleOrigin;
        this.relative = bl;
    }

    public Object getValue() {
        return this.value;
    }

    public StyleOrigin getOrigin() {
        return this.origin;
    }

    public boolean isRelative() {
        return this.relative;
    }

    public String toString() {
        return '{' + String.valueOf(this.value) + ", " + (Object)this.origin + ", " + this.relative + '}';
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        CalculatedValue calculatedValue = (CalculatedValue)object;
        if (this.relative != calculatedValue.relative) {
            return false;
        }
        if (this.origin != calculatedValue.origin) {
            return false;
        }
        return !(this.value == null ? calculatedValue.value != null : !this.value.equals(calculatedValue.value));
    }
}

