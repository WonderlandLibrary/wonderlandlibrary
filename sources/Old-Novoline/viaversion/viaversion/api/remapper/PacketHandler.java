package viaversion.viaversion.api.remapper;

import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.exception.InformativeException;

@FunctionalInterface
public interface PacketHandler extends ValueWriter {
    /**
     * Handle a packet
     *
     * @param wrapper The associated wrapper
     * @throws Exception Throws exception if it failed to handle the packet
     */
    void handle(PacketWrapper wrapper) throws Exception;

    @Override
    default void write(PacketWrapper writer, Object inputValue) throws Exception {
        try {
            handle(writer);
        } catch (InformativeException e) {
            e.addSource(this.getClass());
            throw e;
        }
    }
}
