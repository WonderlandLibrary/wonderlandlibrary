/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.ICUException;

public class ICUCloneNotSupportedException
extends ICUException {
    private static final long serialVersionUID = -4824446458488194964L;

    public ICUCloneNotSupportedException() {
    }

    public ICUCloneNotSupportedException(String message) {
        super(message);
    }

    public ICUCloneNotSupportedException(Throwable cause) {
        super(cause);
    }

    public ICUCloneNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}

