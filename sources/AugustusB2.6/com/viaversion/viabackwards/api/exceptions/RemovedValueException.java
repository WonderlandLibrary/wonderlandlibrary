// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.exceptions;

import java.io.IOException;

public class RemovedValueException extends IOException
{
    public static final RemovedValueException EX;
    
    static {
        EX = new RemovedValueException() {
            @Override
            public Throwable fillInStackTrace() {
                return this;
            }
        };
    }
}
