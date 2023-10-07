package viaversion.viaversion.api.remapper;

import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.type.Type;

public class TypeRemapper<T> implements ValueReader<T>, ValueWriter<T> {
    private final Type<T> type;

    public TypeRemapper(Type<T> type) {
        this.type = type;
    }

    @Override
    public T read(PacketWrapper wrapper) throws Exception {
        return wrapper.read(type);
    }

    @Override
    public void write(PacketWrapper output, T inputValue) {
        output.write(type, inputValue);
    }
}
