/*
 * Decompiled with CFR 0.152.
 */
package javassist;

import javassist.CtField;

class FieldInitLink {
    FieldInitLink next = null;
    CtField field;
    CtField.Initializer init;

    FieldInitLink(CtField f2, CtField.Initializer i2) {
        this.field = f2;
        this.init = i2;
    }
}

