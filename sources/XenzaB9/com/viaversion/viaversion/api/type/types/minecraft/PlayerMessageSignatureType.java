// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.type.OptionalType;
import java.util.UUID;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.PlayerMessageSignature;
import com.viaversion.viaversion.api.type.Type;

public class PlayerMessageSignatureType extends Type<PlayerMessageSignature>
{
    public PlayerMessageSignatureType() {
        super(PlayerMessageSignature.class);
    }
    
    @Override
    public PlayerMessageSignature read(final ByteBuf buffer) throws Exception {
        return new PlayerMessageSignature(Type.UUID.read(buffer), Type.BYTE_ARRAY_PRIMITIVE.read(buffer));
    }
    
    @Override
    public void write(final ByteBuf buffer, final PlayerMessageSignature value) throws Exception {
        Type.UUID.write(buffer, value.uuid());
        Type.BYTE_ARRAY_PRIMITIVE.write(buffer, value.signatureBytes());
    }
    
    public static final class OptionalPlayerMessageSignatureType extends OptionalType<PlayerMessageSignature>
    {
        public OptionalPlayerMessageSignatureType() {
            super(Type.PLAYER_MESSAGE_SIGNATURE);
        }
    }
}
