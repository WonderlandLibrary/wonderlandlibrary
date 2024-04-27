package me.utils;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lme/utils/PacketUtils$PacketType;", "", "(Ljava/lang/String;I)V", "SERVERSIDE", "CLIENTSIDE", "UNKNOWN", "Pride"})
public static final class PacketUtils$PacketType
extends Enum<PacketUtils$PacketType> {
    public static final PacketUtils$PacketType SERVERSIDE;
    public static final PacketUtils$PacketType CLIENTSIDE;
    public static final PacketUtils$PacketType UNKNOWN;
    private static final PacketUtils$PacketType[] $VALUES;

    static {
        PacketUtils$PacketType[] packetTypeArray = new PacketUtils$PacketType[3];
        PacketUtils$PacketType[] packetTypeArray2 = packetTypeArray;
        packetTypeArray[0] = SERVERSIDE = new PacketUtils$PacketType();
        packetTypeArray[1] = CLIENTSIDE = new PacketUtils$PacketType();
        packetTypeArray[2] = UNKNOWN = new PacketUtils$PacketType();
        $VALUES = packetTypeArray;
    }

    public static PacketUtils$PacketType[] values() {
        return (PacketUtils$PacketType[])$VALUES.clone();
    }

    public static PacketUtils$PacketType valueOf(String string) {
        return Enum.valueOf(PacketUtils$PacketType.class, string);
    }
}
