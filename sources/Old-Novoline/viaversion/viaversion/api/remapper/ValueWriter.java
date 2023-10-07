package viaversion.viaversion.api.remapper;

import viaversion.viaversion.api.PacketWrapper;

@FunctionalInterface
public interface ValueWriter<T> {
    /**
     * Write a value to a packet
     *
     * @param writer     The packet wrapper to write to
     * @param inputValue The value to write
     * @throws Exception Throws exception if it fails to write
     */
    void write(PacketWrapper writer, T inputValue) throws Exception;
}
