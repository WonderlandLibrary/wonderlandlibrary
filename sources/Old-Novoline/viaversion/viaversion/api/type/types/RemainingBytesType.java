package viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import viaversion.viaversion.api.type.Type;

public class RemainingBytesType extends Type<byte[]> {
    public RemainingBytesType() {
        super("byte[]", byte[].class);
    }

    @Override
    public byte[] read(ByteBuf buffer) {
        byte[] array = new byte[buffer.readableBytes()];
        buffer.readBytes(array);
        return array;
    }

    @Override
    public void write(ByteBuf buffer, byte[] object) {
        buffer.writeBytes(object);
    }
}
