package viaversion.viaversion.api.type.types.minecraft;

import io.netty.buffer.ByteBuf;
import viaversion.viaversion.api.minecraft.Vector;
import viaversion.viaversion.api.type.Type;

public class VectorType extends Type<Vector> {
    public VectorType() {
        super("Vector", Vector.class);
    }

    @Override
    public Vector read(ByteBuf buffer) throws Exception {
        int x = Type.INT.read(buffer);
        int y = Type.INT.read(buffer);
        int z = Type.INT.read(buffer);

        return new Vector(x, y, z);
    }

    @Override
    public void write(ByteBuf buffer, Vector object) throws Exception {
        Type.INT.write(buffer, object.getBlockX());
        Type.INT.write(buffer, object.getBlockY());
        Type.INT.write(buffer, object.getBlockZ());
    }
}
