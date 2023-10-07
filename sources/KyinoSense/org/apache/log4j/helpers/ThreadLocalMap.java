/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.helpers;

import java.util.Hashtable;

public final class ThreadLocalMap
extends InheritableThreadLocal {
    public final Object childValue(Object parentValue) {
        Hashtable ht = (Hashtable)parentValue;
        if (ht != null) {
            return ht.clone();
        }
        return null;
    }
}

