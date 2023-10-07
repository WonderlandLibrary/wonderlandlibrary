// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.type.OptionalType;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.ProfileKey;
import com.viaversion.viaversion.api.type.Type;

public class ProfileKeyType extends Type<ProfileKey>
{
    public ProfileKeyType() {
        super(ProfileKey.class);
    }
    
    @Override
    public ProfileKey read(final ByteBuf buffer) throws Exception {
        return new ProfileKey(buffer.readLong(), Type.BYTE_ARRAY_PRIMITIVE.read(buffer), Type.BYTE_ARRAY_PRIMITIVE.read(buffer));
    }
    
    @Override
    public void write(final ByteBuf buffer, final ProfileKey object) throws Exception {
        buffer.writeLong(object.expiresAt());
        Type.BYTE_ARRAY_PRIMITIVE.write(buffer, object.publicKey());
        Type.BYTE_ARRAY_PRIMITIVE.write(buffer, object.keySignature());
    }
    
    public static final class OptionalProfileKeyType extends OptionalType<ProfileKey>
    {
        public OptionalProfileKeyType() {
            super(Type.PROFILE_KEY);
        }
    }
}
