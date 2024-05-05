package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;

public class S45PacketTitle
implements Packet<INetHandlerPlayClient> {
    private Type type;
    private IChatComponent message;
    private int fadeInTime;
    private int displayTime;
    private int fadeOutTime;

    public S45PacketTitle() {
    }

    public S45PacketTitle(Type type, IChatComponent message) {
        this(type, message, -1, -1, -1);
    }

    public S45PacketTitle(int fadeInTime, int displayTime, int fadeOutTime) {
        this(Type.TIMES, null, fadeInTime, displayTime, fadeOutTime);
    }

    public S45PacketTitle(Type type, IChatComponent message, int fadeInTime, int displayTime, int fadeOutTime) {
        this.type = type;
        this.message = message;
        this.fadeInTime = fadeInTime;
        this.displayTime = displayTime;
        this.fadeOutTime = fadeOutTime;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.type = (Type)buf.readEnumValue(Type.class);
        if (this.type == Type.TITLE || this.type == Type.SUBTITLE) {
            this.message = buf.readChatComponent();
        }
        if (this.type == Type.TIMES) {
            this.fadeInTime = buf.readInt();
            this.displayTime = buf.readInt();
            this.fadeOutTime = buf.readInt();
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue((Enum)this.type);
        if (this.type == Type.TITLE || this.type == Type.SUBTITLE) {
            buf.writeChatComponent(this.message);
        }
        if (this.type == Type.TIMES) {
            buf.writeInt(this.fadeInTime);
            buf.writeInt(this.displayTime);
            buf.writeInt(this.fadeOutTime);
        }
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleTitle(this);
    }

    public Type getType() {
        return this.type;
    }

    public IChatComponent getMessage() {
        return this.message;
    }

    public int getFadeInTime() {
        return this.fadeInTime;
    }

    public int getDisplayTime() {
        return this.displayTime;
    }

    public int getFadeOutTime() {
        return this.fadeOutTime;
    }
}
