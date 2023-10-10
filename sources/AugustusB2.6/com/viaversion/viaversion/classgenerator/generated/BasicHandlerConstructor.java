// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.classgenerator.generated;

import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import com.viaversion.viaversion.api.connection.UserConnection;

public class BasicHandlerConstructor implements HandlerConstructor
{
    @Override
    public BukkitEncodeHandler newEncodeHandler(final UserConnection info, final MessageToByteEncoder minecraftEncoder) {
        return new BukkitEncodeHandler(info, minecraftEncoder);
    }
    
    @Override
    public BukkitDecodeHandler newDecodeHandler(final UserConnection info, final ByteToMessageDecoder minecraftDecoder) {
        return new BukkitDecodeHandler(info, minecraftDecoder);
    }
}
