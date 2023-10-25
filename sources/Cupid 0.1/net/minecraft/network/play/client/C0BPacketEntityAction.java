package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0BPacketEntityAction implements Packet<INetHandlerPlayServer> {
  private int entityID;
  
  private Action action;
  
  private int auxData;
  
  public C0BPacketEntityAction() {}
  
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
    buf.writeEnumValue(this.action);
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
  
  public enum Action {
    START_SNEAKING, STOP_SNEAKING, STOP_SLEEPING, START_SPRINTING, STOP_SPRINTING, RIDING_JUMP, OPEN_INVENTORY;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\network\play\client\C0BPacketEntityAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */