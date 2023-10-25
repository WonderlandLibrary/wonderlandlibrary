package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8;

import de.gerrygames.viarewind.netty.EmptyChannelHandler;
import de.gerrygames.viarewind.netty.ForwardMessageToByteEncoder;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.EntityPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.InventoryPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.PlayerPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.ScoreboardPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.SpawnPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.packets.WorldPackets;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.CompressionSendStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerAbilities;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerPosition;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Scoreboard;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage.WorldBorder;
import de.gerrygames.viarewind.utils.Ticker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.CustomByteType;
import us.myles.ViaVersion.packets.Direction;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.storage.ClientChunks;

public class Protocol1_7_6_10TO1_8 extends Protocol {
  protected void registerPackets() {
    EntityPackets.register(this);
    InventoryPackets.register(this);
    PlayerPackets.register(this);
    ScoreboardPackets.register(this);
    SpawnPackets.register(this);
    WorldPackets.register(this);
    registerOutgoing(State.PLAY, 0, 0, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT, Type.INT);
          }
        });
    registerOutgoing(State.PLAY, 70, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                  }
                });
          }
        });
    registerIncoming(State.PLAY, 0, 0, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT, (Type)Type.VAR_INT);
          }
        });
    registerOutgoing(State.LOGIN, 1, 1, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int publicKeyLength = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)publicKeyLength));
                    packetWrapper.passthrough((Type)new CustomByteType(Integer.valueOf(publicKeyLength)));
                    int verifyTokenLength = ((Integer)packetWrapper.read((Type)Type.VAR_INT)).intValue();
                    packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)verifyTokenLength));
                    packetWrapper.passthrough((Type)new CustomByteType(Integer.valueOf(verifyTokenLength)));
                  }
                });
          }
        });
    registerOutgoing(State.LOGIN, 3, 3, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    ((CompressionSendStorage)packetWrapper.user().get(CompressionSendStorage.class)).setCompressionSend(true);
                  }
                });
          }
        });
    registerIncoming(State.LOGIN, 1, 1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    int sharedSecretLength = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(sharedSecretLength));
                    packetWrapper.passthrough((Type)new CustomByteType(Integer.valueOf(sharedSecretLength)));
                    int verifyTokenLength = ((Short)packetWrapper.read((Type)Type.SHORT)).shortValue();
                    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(verifyTokenLength));
                    packetWrapper.passthrough((Type)new CustomByteType(Integer.valueOf(verifyTokenLength)));
                  }
                });
          }
        });
  }
  
  public void transform(Direction direction, State state, PacketWrapper packetWrapper) throws Exception {
    CompressionSendStorage compressionSendStorage = (CompressionSendStorage)packetWrapper.user().get(CompressionSendStorage.class);
    if (compressionSendStorage.isCompressionSend()) {
      Channel channel = packetWrapper.user().getChannel();
      if (channel.pipeline().get("compress") != null) {
        channel.pipeline().replace("decompress", "decompress", (ChannelHandler)new EmptyChannelHandler());
        channel.pipeline().replace("compress", "compress", (ChannelHandler)new ForwardMessageToByteEncoder());
      } else if (channel.pipeline().get("compression-encoder") != null) {
        channel.pipeline().replace("compression-decoder", "compression-decoder", (ChannelHandler)new EmptyChannelHandler());
        channel.pipeline().replace("compression-encoder", "compression-encoder", (ChannelHandler)new ForwardMessageToByteEncoder());
      } 
      compressionSendStorage.setCompressionSend(false);
    } 
    super.transform(direction, state, packetWrapper);
  }
  
  public void init(UserConnection userConnection) {
    Ticker.init();
    userConnection.put((StoredObject)new Windows(userConnection));
    userConnection.put((StoredObject)new EntityTracker(userConnection));
    userConnection.put((StoredObject)new PlayerPosition(userConnection));
    userConnection.put((StoredObject)new GameProfileStorage(userConnection));
    userConnection.put((StoredObject)new ClientChunks(userConnection));
    userConnection.put((StoredObject)new Scoreboard(userConnection));
    userConnection.put((StoredObject)new CompressionSendStorage(userConnection));
    userConnection.put((StoredObject)new WorldBorder(userConnection));
    userConnection.put((StoredObject)new PlayerAbilities(userConnection));
    userConnection.put((StoredObject)new ClientWorld(userConnection));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\Protocol1_7_6_10TO1_8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */