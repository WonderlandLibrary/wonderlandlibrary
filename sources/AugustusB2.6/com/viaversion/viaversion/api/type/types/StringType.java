// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.type.types;

import java.nio.charset.StandardCharsets;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.Type;

public class StringType extends Type<String>
{
    private static final int maxJavaCharUtf8Length;
    private final int maxLength;
    
    public StringType() {
        this(32767);
    }
    
    public StringType(final int maxLength) {
        super(String.class);
        this.maxLength = maxLength;
    }
    
    @Override
    public String read(final ByteBuf buffer) throws Exception {
        final int len = Type.VAR_INT.readPrimitive(buffer);
        Preconditions.checkArgument(len <= this.maxLength * StringType.maxJavaCharUtf8Length, "Cannot receive string longer than Short.MAX_VALUE * " + StringType.maxJavaCharUtf8Length + " bytes (got %s bytes)", new Object[] { len });
        final String string = buffer.toString(buffer.readerIndex(), len, StandardCharsets.UTF_8);
        buffer.skipBytes(len);
        Preconditions.checkArgument(string.length() <= this.maxLength, "Cannot receive string longer than Short.MAX_VALUE characters (got %s bytes)", new Object[] { string.length() });
        return string;
    }
    
    @Override
    public void write(final ByteBuf buffer, final String object) throws Exception {
        Preconditions.checkArgument(object.length() <= this.maxLength, "Cannot send string longer than Short.MAX_VALUE (got %s characters)", new Object[] { object.length() });
        final byte[] b = object.getBytes(StandardCharsets.UTF_8);
        Type.VAR_INT.writePrimitive(buffer, b.length);
        buffer.writeBytes(b);
    }
    
    static {
        maxJavaCharUtf8Length = Character.toString('\uffff').getBytes(StandardCharsets.UTF_8).length;
    }
}
