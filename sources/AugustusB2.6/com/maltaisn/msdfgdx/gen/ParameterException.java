// 
// Decompiled by Procyon v0.5.36
// 

package com.maltaisn.msdfgdx.gen;

import kotlin.jvm.internal.Intrinsics;

public final class ParameterException extends IllegalArgumentException
{
    public ParameterException(final String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        super(message);
    }
}
