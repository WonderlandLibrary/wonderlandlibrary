/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import java.util.Arrays;

@Deprecated
public class ResourceLeakException
extends RuntimeException {
    private final StackTraceElement[] cachedStackTrace = this.getStackTrace();
    private static final long serialVersionUID = 7186453858343358280L;

    public boolean equals(Object object) {
        if (!(object instanceof ResourceLeakException)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        return Arrays.equals(this.cachedStackTrace, ((ResourceLeakException)object).cachedStackTrace);
    }

    public ResourceLeakException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public int hashCode() {
        StackTraceElement[] stackTraceElementArray = this.cachedStackTrace;
        int n = 0;
        for (StackTraceElement stackTraceElement : stackTraceElementArray) {
            n = n * 31 + stackTraceElement.hashCode();
        }
        return n;
    }

    public ResourceLeakException(String string) {
        super(string);
    }

    public ResourceLeakException(Throwable throwable) {
        super(throwable);
    }

    public ResourceLeakException() {
    }
}

