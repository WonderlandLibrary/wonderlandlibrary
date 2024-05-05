package net.minecraft.network.play.server;

import java.util.EnumSet;
import java.util.Set;

public static enum S08PacketPlayerPosLook.EnumFlags {
    X(0),
    Y(1),
    Z(2),
    Y_ROT(3),
    X_ROT(4);

    private int field_180058_f;

    private S08PacketPlayerPosLook.EnumFlags(int p_i45992_3_) {
        this.field_180058_f = p_i45992_3_;
    }

    private int func_180055_a() {
        return 1 << this.field_180058_f;
    }

    private boolean func_180054_b(int p_180054_1_) {
        return (p_180054_1_ & this.func_180055_a()) == this.func_180055_a();
    }

    public static Set<S08PacketPlayerPosLook.EnumFlags> func_180053_a(int p_180053_0_) {
        EnumSet set = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
        for (S08PacketPlayerPosLook.EnumFlags s08packetplayerposlook$enumflags : S08PacketPlayerPosLook.EnumFlags.values()) {
            if (!s08packetplayerposlook$enumflags.func_180054_b(p_180053_0_)) continue;
            set.add((Object)s08packetplayerposlook$enumflags);
        }
        return set;
    }

    public static int func_180056_a(Set<S08PacketPlayerPosLook.EnumFlags> p_180056_0_) {
        int i = 0;
        for (S08PacketPlayerPosLook.EnumFlags s08packetplayerposlook$enumflags : p_180056_0_) {
            i |= s08packetplayerposlook$enumflags.func_180055_a();
        }
        return i;
    }
}
