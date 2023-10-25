/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S08PacketPlayerPosLook
implements Packet<INetHandlerPlayClient> {
    public float pitch;
    public static double y;
    private Set<EnumFlags> field_179835_f;
    public static double z;
    public float yaw;
    public static double x;

    public Set<EnumFlags> func_179834_f() {
        return this.field_179835_f;
    }

    public double getZ() {
        return z;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handlePlayerPosLook(this);
    }

    public S08PacketPlayerPosLook() {
    }

    public float getYaw() {
        return this.yaw;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        x = packetBuffer.readDouble();
        y = packetBuffer.readDouble();
        z = packetBuffer.readDouble();
        this.yaw = packetBuffer.readFloat();
        this.pitch = packetBuffer.readFloat();
        this.field_179835_f = EnumFlags.func_180053_a(packetBuffer.readUnsignedByte());
    }

    public S08PacketPlayerPosLook(double d, double d2, double d3, float f, float f2, Set<EnumFlags> set) {
        x = d;
        y = d2;
        z = d3;
        this.yaw = f;
        this.pitch = f2;
        this.field_179835_f = set;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeDouble(x);
        packetBuffer.writeDouble(y);
        packetBuffer.writeDouble(z);
        packetBuffer.writeFloat(this.yaw);
        packetBuffer.writeFloat(this.pitch);
        packetBuffer.writeByte(EnumFlags.func_180056_a(this.field_179835_f));
    }

    public float getPitch() {
        return this.pitch;
    }

    public static double getY() {
        return y;
    }

    public static double getX() {
        return x;
    }

    public static enum EnumFlags {
        X(0),
        Y(1),
        Z(2),
        Y_ROT(3),
        X_ROT(4);

        private int field_180058_f;

        private int func_180055_a() {
            return 1 << this.field_180058_f;
        }

        public static Set<EnumFlags> func_180053_a(int n) {
            EnumSet<EnumFlags> enumSet = EnumSet.noneOf(EnumFlags.class);
            EnumFlags[] enumFlagsArray = EnumFlags.values();
            int n2 = enumFlagsArray.length;
            int n3 = 0;
            while (n3 < n2) {
                EnumFlags enumFlags = enumFlagsArray[n3];
                if (enumFlags.func_180054_b(n)) {
                    enumSet.add(enumFlags);
                }
                ++n3;
            }
            return enumSet;
        }

        private EnumFlags(int n2) {
            this.field_180058_f = n2;
        }

        public static int func_180056_a(Set<EnumFlags> set) {
            int n = 0;
            for (EnumFlags enumFlags : set) {
                n |= enumFlags.func_180055_a();
            }
            return n;
        }

        private boolean func_180054_b(int n) {
            return (n & this.func_180055_a()) == this.func_180055_a();
        }
    }
}

