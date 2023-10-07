/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.SinceKotlin;
import kotlin.jvm.internal.PropertyReference;
import kotlin.reflect.KMutableProperty;

public abstract class MutablePropertyReference
extends PropertyReference
implements KMutableProperty {
    public MutablePropertyReference() {
    }

    @SinceKotlin(version="1.1")
    public MutablePropertyReference(Object receiver) {
        super(receiver);
    }
}

