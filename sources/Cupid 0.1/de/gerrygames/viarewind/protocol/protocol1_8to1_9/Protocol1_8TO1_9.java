package de.gerrygames.viarewind.protocol.protocol1_8to1_9;

import com.google.common.collect.ImmutableSet;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.EntityPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.InventoryPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.PlayerPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.ScoreboardPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.SpawnPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets.WorldPackets;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BlockPlaceDestroyTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.BossBarStorage;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Cooldown;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Levitation;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.PlayerPosition;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage.Windows;
import de.gerrygames.viarewind.utils.Ticker;
import java.util.Timer;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;

public class Protocol1_8TO1_9 extends Protocol {
  public static final Timer TIMER = new Timer("ViaRewind-1_8TO1_9", true);
  
  public static final ImmutableSet<Object> VALID_ATTRIBUTES;
  
  static {
    ImmutableSet.Builder<Object> builder = ImmutableSet.builder();
    builder.add("generic.maxHealth");
    builder.add("generic.followRange");
    builder.add("generic.knockbackResistance");
    builder.add("generic.movementSpeed");
    builder.add("generic.attackDamage");
    builder.add("horse.jumpStrength");
    builder.add("zombie.spawnReinforcements");
    VALID_ATTRIBUTES = builder.build();
  }
  
  public static final ValueTransformer<Double, Integer> TO_OLD_INT = new ValueTransformer<Double, Integer>(Type.INT) {
      public Integer transform(PacketWrapper wrapper, Double inputValue) {
        return Integer.valueOf((int)(inputValue.doubleValue() * 32.0D));
      }
    };
  
  public static final ValueTransformer<Float, Byte> DEGREES_TO_ANGLE = new ValueTransformer<Float, Byte>(Type.BYTE) {
      public Byte transform(PacketWrapper packetWrapper, Float degrees) throws Exception {
        return Byte.valueOf((byte)(int)(degrees.floatValue() / 360.0F * 256.0F));
      }
    };
  
  protected void registerPackets() {
    EntityPackets.register(this);
    InventoryPackets.register(this);
    PlayerPackets.register(this);
    ScoreboardPackets.register(this);
    SpawnPackets.register(this);
    WorldPackets.register(this);
    registerOutgoing(State.PLAY, 31, 0);
    registerIncoming(State.PLAY, 11, 0);
  }
  
  public void init(UserConnection userConnection) {
    Ticker.init();
    userConnection.put((StoredObject)new Windows(userConnection));
    userConnection.put((StoredObject)new EntityTracker(userConnection));
    userConnection.put((StoredObject)new Levitation(userConnection));
    userConnection.put((StoredObject)new PlayerPosition(userConnection));
    userConnection.put((StoredObject)new Cooldown(userConnection));
    userConnection.put((StoredObject)new BlockPlaceDestroyTracker(userConnection));
    userConnection.put((StoredObject)new BossBarStorage(userConnection));
    userConnection.put((StoredObject)new ClientWorld(userConnection));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\Protocol1_8TO1_9.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */