// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.exception;

import com.viaversion.viaversion.api.Via;
import io.netty.handler.codec.EncoderException;

public class CancelEncoderException extends EncoderException implements CancelCodecException
{
    public static final CancelEncoderException CACHED;
    
    public CancelEncoderException() {
    }
    
    public CancelEncoderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CancelEncoderException(final String message) {
        super(message);
    }
    
    public CancelEncoderException(final Throwable cause) {
        super(cause);
    }
    
    public static CancelEncoderException generate(final Throwable cause) {
        return Via.getManager().isDebug() ? new CancelEncoderException(cause) : CancelEncoderException.CACHED;
    }
    
    static {
        CACHED = new CancelEncoderException("This packet is supposed to be cancelled; If you have debug enabled, you can ignore these") {
            @Override
            public Throwable fillInStackTrace() {
                return this;
            }
        };
    }
}
