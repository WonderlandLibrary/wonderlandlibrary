/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S45PacketTitle
implements Packet<INetHandlerPlayClient> {
    private int displayTime;
    private int fadeOutTime;
    private int fadeInTime;
    private Type type;
    private IChatComponent message;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleTitle(this);
    }

    public int getDisplayTime() {
        return this.displayTime;
    }

    public int getFadeOutTime() {
        return this.fadeOutTime;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.type = packetBuffer.readEnumValue(Type.class);
        if (this.type == Type.TITLE || this.type == Type.SUBTITLE) {
            this.message = packetBuffer.readChatComponent();
        }
        if (this.type == Type.TIMES) {
            this.fadeInTime = packetBuffer.readInt();
            this.displayTime = packetBuffer.readInt();
            this.fadeOutTime = packetBuffer.readInt();
        }
    }

    public S45PacketTitle() {
    }

    public S45PacketTitle(Type type, IChatComponent iChatComponent, int n, int n2, int n3) {
        this.type = type;
        this.message = iChatComponent;
        this.fadeInTime = n;
        this.displayTime = n2;
        this.fadeOutTime = n3;
    }

    public IChatComponent getMessage() {
        return this.message;
    }

    public S45PacketTitle(Type type, IChatComponent iChatComponent) {
        this(type, iChatComponent, -1, -1, -1);
    }

    public S45PacketTitle(int n, int n2, int n3) {
        this(Type.TIMES, null, n, n2, n3);
    }

    public int getFadeInTime() {
        return this.fadeInTime;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeEnumValue(this.type);
        if (this.type == Type.TITLE || this.type == Type.SUBTITLE) {
            packetBuffer.writeChatComponent(this.message);
        }
        if (this.type == Type.TIMES) {
            packetBuffer.writeInt(this.fadeInTime);
            packetBuffer.writeInt(this.displayTime);
            packetBuffer.writeInt(this.fadeOutTime);
        }
    }

    public static enum Type {
        TITLE,
        SUBTITLE,
        TIMES,
        CLEAR,
        RESET;


        public static Type byName(String string) {
            Type[] typeArray = Type.values();
            int n = typeArray.length;
            int n2 = 0;
            while (n2 < n) {
                Type type = typeArray[n2];
                if (type.name().equalsIgnoreCase(string)) {
                    return type;
                }
                ++n2;
            }
            return TITLE;
        }

        public static String[] getNames() {
            String[] stringArray = new String[Type.values().length];
            int n = 0;
            Type[] typeArray = Type.values();
            int n2 = typeArray.length;
            int n3 = 0;
            while (n3 < n2) {
                Type type = typeArray[n3];
                stringArray[n++] = type.name().toLowerCase();
                ++n3;
            }
            return stringArray;
        }
    }
}

