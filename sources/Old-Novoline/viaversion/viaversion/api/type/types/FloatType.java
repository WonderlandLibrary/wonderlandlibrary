package viaversion.viaversion.api.type.types;

import io.netty.buffer.ByteBuf;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.api.type.TypeConverter;

public class FloatType extends Type<Float> implements TypeConverter<Float> {

    public FloatType() {
        super("Float", Float.class);
    }

    public float readPrimitive(ByteBuf buffer) {
        return buffer.readFloat();
    }

    public void writePrimitive(ByteBuf buffer, float object) {
        buffer.writeFloat(object);
    }

    /**
     * @deprecated use {@link #readPrimitive(ByteBuf)} for manual reading to avoid wrapping
     */
    @Override
    @Deprecated
    public Float read(ByteBuf buffer) {
        return buffer.readFloat();
    }

    /**
     * @deprecated use {@link #writePrimitive(ByteBuf, float)} for manual reading to avoid wrapping
     */
    @Override
    @Deprecated
    public void write(ByteBuf buffer, Float object) {
        buffer.writeFloat(object);
    }

    @Override
    public Float from(Object o) {
        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? 1F : 0;
        }
        return (Float) o;
    }
}
