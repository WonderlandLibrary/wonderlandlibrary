// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.nbt;

import java.io.IOException;

class StringTagParseException extends IOException
{
    private static final long serialVersionUID = -3001637554903912905L;
    private final CharSequence buffer;
    private final int position;
    
    StringTagParseException(final String message, final CharSequence buffer, final int position) {
        super(message);
        this.buffer = buffer;
        this.position = position;
    }
    
    @Override
    public String getMessage() {
        return super.getMessage() + "(at position " + this.position + ")";
    }
}
