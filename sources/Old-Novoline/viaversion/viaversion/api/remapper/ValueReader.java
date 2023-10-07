package viaversion.viaversion.api.remapper;

import viaversion.viaversion.api.PacketWrapper;

@FunctionalInterface
public interface ValueReader<T> {
    /**
     * Reads value from a PacketWrapper
     *
     * @param wrapper The wrapper to read from
     * @return Returns the desired type
     * @throws Exception Throws exception if it fails to read
     */
    T read(PacketWrapper wrapper) throws Exception;
}
