package viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import viaversion.viaversion.api.type.Type;

import java.util.UUID;

public class UUIDType extends Type<UUID> {
    public UUIDType() {
        super("UUID", UUID.class);
    }

    @Override
    public UUID read(ByteBuf buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }

    @Override
    public void write(ByteBuf buffer, UUID object) {
        buffer.writeLong(object.getMostSignificantBits());
        buffer.writeLong(object.getLeastSignificantBits());
    }
}
