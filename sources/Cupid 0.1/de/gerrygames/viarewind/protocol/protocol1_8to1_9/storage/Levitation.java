package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.utils.Tickable;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.type.Type;

public class Levitation extends StoredObject implements Tickable {
  private int amplifier;
  
  private volatile boolean active = false;
  
  public Levitation(UserConnection user) {
    super(user);
  }
  
  public void tick() {
    if (!this.active)
      return; 
    int vY = (this.amplifier + 1) * 360;
    PacketWrapper packet = new PacketWrapper(18, null, getUser());
    packet.write((Type)Type.VAR_INT, Integer.valueOf(((EntityTracker)getUser().get(EntityTracker.class)).getPlayerId()));
    packet.write((Type)Type.SHORT, Short.valueOf((short)0));
    packet.write((Type)Type.SHORT, Short.valueOf((short)vY));
    packet.write((Type)Type.SHORT, Short.valueOf((short)0));
    PacketUtil.sendPacket(packet, Protocol1_8TO1_9.class);
  }
  
  public void setActive(boolean active) {
    this.active = active;
  }
  
  public void setAmplifier(int amplifier) {
    this.amplifier = amplifier;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\storage\Levitation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */