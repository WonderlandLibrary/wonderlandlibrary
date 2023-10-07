package viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.api.type.TypeConverter;

public class BooleanType extends Type<Boolean> implements TypeConverter<Boolean> {
    public BooleanType() {
        super("Boolean", Boolean.class);
    }

    @Override
    public Boolean read(ByteBuf buffer) {
        return buffer.readBoolean();
    }

    @Override
    public void write(ByteBuf buffer, Boolean object) {
        buffer.writeBoolean(object);
    }


    @Override
    public Boolean from(Object o) {
        if (o instanceof Number) {
            return ((Number) o).intValue() == 1;
        }
        return (Boolean) o;
    }
}
