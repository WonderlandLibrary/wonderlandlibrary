package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class C0BPacketEntityAction
implements Packet<INetHandlerPlayServer> {
    private int entityID;
    private Action action;
    private int auxData;

    public C0BPacketEntityAction() {
    }

    public C0BPacketEntityAction(Entity entity, Action action) {
        this(entity, action, 0);
    }

    public C0BPacketEntityAction(Entity entity, Action action, int auxData) {
        this.entityID = entity.getEntityId();
        this.action = action;
        this.auxData = auxData;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarIntFromBuffer();
        this.action = (Action)buf.readEnumValue(Action.class);
        this.auxData = buf.readVarIntFromBuffer();
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeEnumValue((Enum)this.action);
        buf.writeVarIntToBuffer(this.auxData);
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processEntityAction(this);
    }

    public Action getAction() {
        return this.action;
    }

    public int getAuxData() {
        return this.auxData;
    }
}
