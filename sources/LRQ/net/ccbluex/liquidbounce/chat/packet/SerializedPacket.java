/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.chat.packet;

import com.google.gson.annotations.SerializedName;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.Nullable;

public final class SerializedPacket {
    @SerializedName(value="m")
    private final String packetName;
    @SerializedName(value="c")
    private final Packet packetContent;

    public final String getPacketName() {
        return this.packetName;
    }

    public final Packet getPacketContent() {
        return this.packetContent;
    }

    public SerializedPacket(String packetName, @Nullable Packet packetContent) {
        this.packetName = packetName;
        this.packetContent = packetContent;
    }

    public final String component1() {
        return this.packetName;
    }

    public final Packet component2() {
        return this.packetContent;
    }

    public final SerializedPacket copy(String packetName, @Nullable Packet packetContent) {
        return new SerializedPacket(packetName, packetContent);
    }

    public static /* synthetic */ SerializedPacket copy$default(SerializedPacket serializedPacket, String string, Packet packet, int n, Object object) {
        if ((n & 1) != 0) {
            string = serializedPacket.packetName;
        }
        if ((n & 2) != 0) {
            packet = serializedPacket.packetContent;
        }
        return serializedPacket.copy(string, packet);
    }

    public String toString() {
        return "SerializedPacket(packetName=" + this.packetName + ", packetContent=" + this.packetContent + ")";
    }

    public int hashCode() {
        String string = this.packetName;
        Packet packet = this.packetContent;
        return (string != null ? string.hashCode() : 0) * 31 + (packet != null ? packet.hashCode() : 0);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof SerializedPacket)) break block3;
                SerializedPacket serializedPacket = (SerializedPacket)object;
                if (!this.packetName.equals(serializedPacket.packetName) || !this.packetContent.equals(serializedPacket.packetContent)) break block3;
            }
            return true;
        }
        return false;
    }
}

