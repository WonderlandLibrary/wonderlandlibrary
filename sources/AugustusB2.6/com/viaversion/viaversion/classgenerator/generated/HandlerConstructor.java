// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.classgenerator.generated;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import com.viaversion.viaversion.api.connection.UserConnection;

public interface HandlerConstructor
{
    MessageToByteEncoder newEncodeHandler(final UserConnection p0, final MessageToByteEncoder p1);
    
    ByteToMessageDecoder newDecodeHandler(final UserConnection p0, final ByteToMessageDecoder p1);
}
