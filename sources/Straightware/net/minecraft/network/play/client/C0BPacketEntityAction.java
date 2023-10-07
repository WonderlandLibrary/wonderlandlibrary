package net.minecraft.network.play.client;

import java.io.IOException;


import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0BPacketEntityAction implements Packet<INetHandlerPlayServer>
{
    private int entityID;
    private C0BPacketEntityAction.Action action;
    private int auxData;

    public C0BPacketEntityAction()
    {
    }

    public int getEntityID() {
        return entityID;
    }

    public C0BPacketEntityAction(Entity entity, C0BPacketEntityAction.Action action)
    {
        this(entity, action, 0);
    }

    public C0BPacketEntityAction(Entity entity, C0BPacketEntityAction.Action action, int auxData)
    {
        this.entityID = entity.getEntityId();
        this.action = action;
        this.auxData = auxData;
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityID = buf.readVarIntFromBuffer();
        this.action = (C0BPacketEntityAction.Action)buf.readEnumValue(C0BPacketEntityAction.Action.class);
        this.auxData = buf.readVarIntFromBuffer();
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeEnumValue(this.action);
        buf.writeVarIntToBuffer(this.auxData);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processEntityAction(this);
    }

    public C0BPacketEntityAction.Action getAction()
    {
        return this.action;
    }

    public int getAuxData()
    {
        return this.auxData;
    }

    public enum Action {
        START_SNEAKING(0),
        STOP_SNEAKING(1),
        STOP_SLEEPING(2),
        START_SPRINTING(3),
        STOP_SPRINTING(4),
        RIDING_JUMP(5),
        OPEN_INVENTORY(6);
        private final int id;

        Action(int id){
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
