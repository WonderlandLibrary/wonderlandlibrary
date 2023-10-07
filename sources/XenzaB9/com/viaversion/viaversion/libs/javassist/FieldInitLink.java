// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.javassist;

class FieldInitLink
{
    FieldInitLink next;
    CtField field;
    CtField.Initializer init;
    
    FieldInitLink(final CtField f, final CtField.Initializer i) {
        this.next = null;
        this.field = f;
        this.init = i;
    }
}
