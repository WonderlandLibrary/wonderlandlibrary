/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonMatcher {
    CommonMatcher() {
    }

    abstract boolean matches();

    abstract boolean find();

    abstract boolean find(int var1);

    abstract String replaceAll(String var1);

    abstract int end();

    abstract int start();
}

