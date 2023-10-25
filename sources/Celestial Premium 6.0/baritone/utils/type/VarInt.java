/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.bytes.ByteArrayList
 */
package baritone.utils.type;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;

public final class VarInt {
    private final int value;
    private final byte[] serialized;
    private final int size;

    public VarInt(int value) {
        this.value = value;
        this.serialized = VarInt.serialize0(this.value);
        this.size = this.serialized.length;
    }

    public final int getValue() {
        return this.value;
    }

    public final int getSize() {
        return this.size;
    }

    public final byte[] serialize() {
        return this.serialized;
    }

    private static byte[] serialize0(int valueIn) {
        ByteArrayList bytes = new ByteArrayList();
        int value = valueIn;
        while ((value & 0x80) != 0) {
            bytes.add((byte)(value & 0x7F | 0x80));
            value >>>= 7;
        }
        bytes.add((byte)(value & 0xFF));
        return bytes.toByteArray();
    }

    public static VarInt read(byte[] bytes) {
        return VarInt.read(bytes, 0);
    }

    public static VarInt read(byte[] bytes, int start) {
        byte b;
        int value = 0;
        int size = 0;
        int index = start;
        do {
            b = bytes[index++];
            value |= (b & 0x7F) << size++ * 7;
            if (size <= 5) continue;
            throw new IllegalArgumentException("VarInt size cannot exceed 5 bytes");
        } while ((b & 0x80) != 0);
        return new VarInt(value);
    }
}

