package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets;

import com.google.common.base.Joiner;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.Rewriter;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.ParticleMapping;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage.TabCompleteStorage;
import nl.matsv.viabackwards.utils.ChatUtil;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.Position;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueCreator;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.packets.InventoryPackets;
import us.myles.viaversion.libs.gson.JsonElement;

public class PlayerPacket1_13 extends Rewriter<Protocol1_12_2To1_13> {
  public PlayerPacket1_13(Protocol1_12_2To1_13 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing(State.LOGIN, 4, -1, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(final PacketWrapper packetWrapper) throws Exception {
                    packetWrapper.cancel();
                    packetWrapper.create(2, new ValueCreator() {
                          public void write(PacketWrapper newWrapper) throws Exception {
                            newWrapper.write((Type)Type.VAR_INT, packetWrapper.read((Type)Type.VAR_INT));
                            newWrapper.write(Type.BOOLEAN, Boolean.valueOf(false));
                          }
                        }).sendToServer(Protocol1_12_2To1_13.class);
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.PLUGIN_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    String channel = (String)wrapper.read(Type.STRING);
                    if (channel.equals("minecraft:trader_list")) {
                      wrapper.write(Type.STRING, "MC|TrList");
                      wrapper.passthrough(Type.INT);
                      int size = ((Short)wrapper.passthrough(Type.UNSIGNED_BYTE)).shortValue();
                      for (int i = 0; i < size; i++) {
                        Item input = (Item)wrapper.read(Type.FLAT_ITEM);
                        wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)PlayerPacket1_13.this.getProtocol()).getBlockItemPackets().handleItemToClient(input));
                        Item output = (Item)wrapper.read(Type.FLAT_ITEM);
                        wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)PlayerPacket1_13.this.getProtocol()).getBlockItemPackets().handleItemToClient(output));
                        boolean secondItem = ((Boolean)wrapper.passthrough(Type.BOOLEAN)).booleanValue();
                        if (secondItem) {
                          Item second = (Item)wrapper.read(Type.FLAT_ITEM);
                          wrapper.write(Type.ITEM, ((Protocol1_12_2To1_13)PlayerPacket1_13.this.getProtocol()).getBlockItemPackets().handleItemToClient(second));
                        } 
                        wrapper.passthrough(Type.BOOLEAN);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                      } 
                    } else {
                      String oldChannel = InventoryPackets.getOldPluginChannelId(channel);
                      if (oldChannel == null) {
                        if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug())
                          ViaBackwards.getPlatform().getLogger().warning("Ignoring outgoing plugin message with channel: " + channel); 
                        wrapper.cancel();
                        return;
                      } 
                      wrapper.write(Type.STRING, oldChannel);
                      if (oldChannel.equals("REGISTER") || oldChannel.equals("UNREGISTER")) {
                        String[] channels = (new String((byte[])wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8)).split("\000");
                        List<String> rewrittenChannels = new ArrayList<>();
                        for (String s : channels) {
                          String rewritten = InventoryPackets.getOldPluginChannelId(s);
                          if (rewritten != null) {
                            rewrittenChannels.add(rewritten);
                          } else if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                            ViaBackwards.getPlatform().getLogger().warning("Ignoring plugin channel in outgoing REGISTER: " + s);
                          } 
                        } 
                        wrapper.write(Type.REMAINING_BYTES, Joiner.on(false).join(rewrittenChannels).getBytes(StandardCharsets.UTF_8));
                      } 
                    } 
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SPAWN_PARTICLE, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.BOOLEAN);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    ParticleMapping.ParticleData old = ParticleMapping.getMapping(((Integer)wrapper.get(Type.INT, 0)).intValue());
                    wrapper.set(Type.INT, 0, Integer.valueOf(old.getHistoryId()));
                    int[] data = old.rewriteData((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol, wrapper);
                    if (data != null) {
                      if (old.getHandler().isBlockHandler() && data[0] == 0) {
                        wrapper.cancel();
                        return;
                      } 
                      for (int i : data)
                        wrapper.write((Type)Type.VAR_INT, Integer.valueOf(i)); 
                    } 
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.PLAYER_INFO, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper packetWrapper) throws Exception {
                    TabCompleteStorage storage = (TabCompleteStorage)packetWrapper.user().get(TabCompleteStorage.class);
                    int action = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    int nPlayers = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    for (int i = 0; i < nPlayers; i++) {
                      UUID uuid = (UUID)packetWrapper.passthrough(Type.UUID);
                      if (action == 0) {
                        String name = (String)packetWrapper.passthrough(Type.STRING);
                        storage.usernames.put(uuid, name);
                        int nProperties = ((Integer)packetWrapper.passthrough((Type)Type.VAR_INT)).intValue();
                        for (int j = 0; j < nProperties; j++) {
                          packetWrapper.passthrough(Type.STRING);
                          packetWrapper.passthrough(Type.STRING);
                          if (((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue())
                            packetWrapper.passthrough(Type.STRING); 
                        } 
                        packetWrapper.passthrough((Type)Type.VAR_INT);
                        packetWrapper.passthrough((Type)Type.VAR_INT);
                        if (((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue())
                          packetWrapper.passthrough(Type.COMPONENT); 
                      } else if (action == 1) {
                        packetWrapper.passthrough((Type)Type.VAR_INT);
                      } else if (action == 2) {
                        packetWrapper.passthrough((Type)Type.VAR_INT);
                      } else if (action == 3) {
                        if (((Boolean)packetWrapper.passthrough(Type.BOOLEAN)).booleanValue())
                          packetWrapper.passthrough(Type.COMPONENT); 
                      } else if (action == 4) {
                        storage.usernames.remove(uuid);
                      } 
                    } 
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SCOREBOARD_OBJECTIVE, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    byte mode = ((Byte)wrapper.get(Type.BYTE, 0)).byteValue();
                    if (mode == 0 || mode == 2) {
                      String value = ((JsonElement)wrapper.read(Type.COMPONENT)).toString();
                      value = ChatRewriter.jsonTextToLegacy(value);
                      if (value.length() > 32)
                        value = value.substring(0, 32); 
                      wrapper.write(Type.STRING, value);
                      int type = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                      wrapper.write(Type.STRING, (type == 1) ? "hearts" : "integer");
                    } 
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.TEAMS, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            map(Type.BYTE);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    byte action = ((Byte)wrapper.get(Type.BYTE, 0)).byteValue();
                    if (action == 0 || action == 2) {
                      String displayName = (String)wrapper.read(Type.STRING);
                      displayName = ChatRewriter.jsonTextToLegacy(displayName);
                      displayName = ChatUtil.removeUnusedColor(displayName, 'f');
                      if (displayName.length() > 32)
                        displayName = displayName.substring(0, 32); 
                      wrapper.write(Type.STRING, displayName);
                      byte flags = ((Byte)wrapper.read(Type.BYTE)).byteValue();
                      String nameTagVisibility = (String)wrapper.read(Type.STRING);
                      String collisionRule = (String)wrapper.read(Type.STRING);
                      int colour = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                      if (colour == 21)
                        colour = -1; 
                      JsonElement prefixComponent = (JsonElement)wrapper.read(Type.COMPONENT);
                      JsonElement suffixComponent = (JsonElement)wrapper.read(Type.COMPONENT);
                      String prefix = (prefixComponent == null || prefixComponent.isJsonNull()) ? "" : ChatRewriter.jsonTextToLegacy(prefixComponent.toString());
                      if (ViaBackwards.getConfig().addTeamColorTo1_13Prefix())
                        prefix = prefix + "§" + ((colour > -1 && colour <= 15) ? Integer.toHexString(colour) : "r"); 
                      prefix = ChatUtil.removeUnusedColor(prefix, 'f', true);
                      if (prefix.length() > 16)
                        prefix = prefix.substring(0, 16); 
                      if (prefix.endsWith("§"))
                        prefix = prefix.substring(0, prefix.length() - 1); 
                      String suffix = (suffixComponent == null || suffixComponent.isJsonNull()) ? "" : ChatRewriter.jsonTextToLegacy(suffixComponent.toString());
                      suffix = ChatUtil.removeUnusedColor(suffix, false);
                      if (suffix.length() > 16)
                        suffix = suffix.substring(0, 16); 
                      if (suffix.endsWith("§"))
                        suffix = suffix.substring(0, suffix.length() - 1); 
                      wrapper.write(Type.STRING, prefix);
                      wrapper.write(Type.STRING, suffix);
                      wrapper.write(Type.BYTE, Byte.valueOf(flags));
                      wrapper.write(Type.STRING, nameTagVisibility);
                      wrapper.write(Type.STRING, collisionRule);
                      wrapper.write(Type.BYTE, Byte.valueOf((byte)colour));
                    } 
                    if (action == 0 || action == 3 || action == 4)
                      wrapper.passthrough(Type.STRING_ARRAY); 
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.TAB_COMPLETE, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    TabCompleteStorage storage = (TabCompleteStorage)wrapper.user().get(TabCompleteStorage.class);
                    if (storage.lastRequest == null) {
                      wrapper.cancel();
                      return;
                    } 
                    if (storage.lastId != ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue())
                      wrapper.cancel(); 
                    int start = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int length = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int lastRequestPartIndex = storage.lastRequest.lastIndexOf(' ') + 1;
                    if (lastRequestPartIndex != start)
                      wrapper.cancel(); 
                    if (length != storage.lastRequest.length() - lastRequestPartIndex)
                      wrapper.cancel(); 
                    int count = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    for (int i = 0; i < count; i++) {
                      String match = (String)wrapper.read(Type.STRING);
                      wrapper.write(Type.STRING, ((start == 0 && !storage.lastAssumeCommand) ? "/" : "") + match);
                      if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                        wrapper.read(Type.STRING); 
                    } 
                  }
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.TAB_COMPLETE, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  TabCompleteStorage storage = (TabCompleteStorage)wrapper.user().get(TabCompleteStorage.class);
                  int id = ThreadLocalRandom.current().nextInt();
                  wrapper.write((Type)Type.VAR_INT, Integer.valueOf(id));
                  String command = (String)wrapper.read(Type.STRING);
                  boolean assumeCommand = ((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue();
                  wrapper.read(Type.OPTIONAL_POSITION);
                  if (!assumeCommand)
                    if (command.startsWith("/")) {
                      command = command.substring(1);
                    } else {
                      wrapper.cancel();
                      PacketWrapper response = wrapper.create(14);
                      List<String> usernames = new ArrayList<>();
                      for (String value : storage.usernames.values()) {
                        if (value.toLowerCase().startsWith(command.substring(command.lastIndexOf(' ') + 1).toLowerCase()))
                          usernames.add(value); 
                      } 
                      response.write((Type)Type.VAR_INT, Integer.valueOf(usernames.size()));
                      for (String value : usernames)
                        response.write(Type.STRING, value); 
                      response.send(((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getClass());
                    }  
                  wrapper.write(Type.STRING, command);
                  storage.lastId = id;
                  storage.lastAssumeCommand = assumeCommand;
                  storage.lastRequest = command;
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  Item book;
                  boolean signing;
                  byte type;
                  int x;
                  int y;
                  int z;
                  byte flags;
                  String mode;
                  String str1;
                  int modeId;
                  int i;
                  String mirror;
                  int mirrorId;
                  String rotation;
                  int rotationId;
                  byte b1;
                  String channel = (String)wrapper.read(Type.STRING);
                  switch (channel) {
                    case "MC|BSign":
                    case "MC|BEdit":
                      wrapper.setId(11);
                      book = (Item)wrapper.read(Type.ITEM);
                      wrapper.write(Type.FLAT_ITEM, ((Protocol1_12_2To1_13)PlayerPacket1_13.this.getProtocol()).getBlockItemPackets().handleItemToServer(book));
                      signing = channel.equals("MC|BSign");
                      wrapper.write(Type.BOOLEAN, Boolean.valueOf(signing));
                      return;
                    case "MC|ItemName":
                      wrapper.setId(28);
                      return;
                    case "MC|AdvCmd":
                      type = ((Byte)wrapper.read(Type.BYTE)).byteValue();
                      if (type == 0) {
                        wrapper.setId(34);
                        wrapper.cancel();
                        ViaBackwards.getPlatform().getLogger().warning("Client send MC|AdvCmd custom payload to update command block, weird!");
                      } else if (type == 1) {
                        wrapper.setId(35);
                        wrapper.write((Type)Type.VAR_INT, wrapper.read(Type.INT));
                        wrapper.passthrough(Type.STRING);
                        wrapper.passthrough(Type.BOOLEAN);
                      } else {
                        wrapper.cancel();
                      } 
                      return;
                    case "MC|AutoCmd":
                      wrapper.setId(34);
                      x = ((Integer)wrapper.read(Type.INT)).intValue();
                      y = ((Integer)wrapper.read(Type.INT)).intValue();
                      z = ((Integer)wrapper.read(Type.INT)).intValue();
                      wrapper.write(Type.POSITION, new Position(x, (short)y, z));
                      wrapper.passthrough(Type.STRING);
                      flags = 0;
                      if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                        flags = (byte)(flags | 0x1); 
                      str1 = (String)wrapper.read(Type.STRING);
                      i = str1.equals("SEQUENCE") ? 0 : (str1.equals("AUTO") ? 1 : 2);
                      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(i));
                      if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                        flags = (byte)(flags | 0x2); 
                      if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                        flags = (byte)(flags | 0x4); 
                      wrapper.write(Type.BYTE, Byte.valueOf(flags));
                      return;
                    case "MC|Struct":
                      wrapper.setId(37);
                      x = ((Integer)wrapper.read(Type.INT)).intValue();
                      y = ((Integer)wrapper.read(Type.INT)).intValue();
                      z = ((Integer)wrapper.read(Type.INT)).intValue();
                      wrapper.write(Type.POSITION, new Position(x, (short)y, z));
                      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Byte)wrapper.read(Type.BYTE)).byteValue() - 1));
                      mode = (String)wrapper.read(Type.STRING);
                      modeId = mode.equals("SAVE") ? 0 : (mode.equals("LOAD") ? 1 : (mode.equals("CORNER") ? 2 : 3));
                      wrapper.write((Type)Type.VAR_INT, Integer.valueOf(modeId));
                      wrapper.passthrough(Type.STRING);
                      wrapper.write(Type.BYTE, Byte.valueOf(((Integer)wrapper.read(Type.INT)).byteValue()));
                      wrapper.write(Type.BYTE, Byte.valueOf(((Integer)wrapper.read(Type.INT)).byteValue()));
                      wrapper.write(Type.BYTE, Byte.valueOf(((Integer)wrapper.read(Type.INT)).byteValue()));
                      wrapper.write(Type.BYTE, Byte.valueOf(((Integer)wrapper.read(Type.INT)).byteValue()));
                      wrapper.write(Type.BYTE, Byte.valueOf(((Integer)wrapper.read(Type.INT)).byteValue()));
                      wrapper.write(Type.BYTE, Byte.valueOf(((Integer)wrapper.read(Type.INT)).byteValue()));
                      mirror = (String)wrapper.read(Type.STRING);
                      mirrorId = mode.equals("NONE") ? 0 : (mode.equals("LEFT_RIGHT") ? 1 : 2);
                      rotation = (String)wrapper.read(Type.STRING);
                      rotationId = mode.equals("NONE") ? 0 : (mode.equals("CLOCKWISE_90") ? 1 : (mode.equals("CLOCKWISE_180") ? 2 : 3));
                      wrapper.passthrough(Type.STRING);
                      b1 = 0;
                      if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                        b1 = (byte)(b1 | 0x1); 
                      if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                        b1 = (byte)(b1 | 0x2); 
                      if (((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue())
                        b1 = (byte)(b1 | 0x4); 
                      wrapper.passthrough((Type)Type.FLOAT);
                      wrapper.passthrough((Type)Type.VAR_LONG);
                      wrapper.write(Type.BYTE, Byte.valueOf(b1));
                      return;
                    case "MC|Beacon":
                      wrapper.setId(32);
                      wrapper.write((Type)Type.VAR_INT, wrapper.read(Type.INT));
                      wrapper.write((Type)Type.VAR_INT, wrapper.read(Type.INT));
                      return;
                    case "MC|TrSel":
                      wrapper.setId(31);
                      wrapper.write((Type)Type.VAR_INT, wrapper.read(Type.INT));
                      return;
                    case "MC|PickItem":
                      wrapper.setId(21);
                      return;
                  } 
                  String newChannel = InventoryPackets.getNewPluginChannelId(channel);
                  if (newChannel == null) {
                    if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug())
                      ViaBackwards.getPlatform().getLogger().warning("Ignoring incoming plugin message with channel: " + channel); 
                    wrapper.cancel();
                    return;
                  } 
                  wrapper.write(Type.STRING, newChannel);
                  if (newChannel.equals("minecraft:register") || newChannel.equals("minecraft:unregister")) {
                    String[] channels = (new String((byte[])wrapper.read(Type.REMAINING_BYTES), StandardCharsets.UTF_8)).split("\000");
                    List<String> rewrittenChannels = new ArrayList<>();
                    for (String s : channels) {
                      String rewritten = InventoryPackets.getNewPluginChannelId(s);
                      if (rewritten != null) {
                        rewrittenChannels.add(rewritten);
                      } else if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                        ViaBackwards.getPlatform().getLogger().warning("Ignoring plugin channel in incoming REGISTER: " + s);
                      } 
                    } 
                    if (!rewrittenChannels.isEmpty()) {
                      wrapper.write(Type.REMAINING_BYTES, Joiner.on(false).join(rewrittenChannels).getBytes(StandardCharsets.UTF_8));
                    } else {
                      wrapper.cancel();
                      return;
                    } 
                  } 
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.STATISTICS, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int size = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    int newSize = size;
                    for (int i = 0; i < size; i++) {
                      int categoryId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                      int statisticId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                      String name = "";
                      switch (categoryId) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                          wrapper.read((Type)Type.VAR_INT);
                          newSize--;
                          break;
                        case 8:
                          name = (String)((Protocol1_12_2To1_13)PlayerPacket1_13.this.protocol).getMappingData().getStatisticMappings().get(statisticId);
                          if (name == null) {
                            wrapper.read((Type)Type.VAR_INT);
                            newSize--;
                            break;
                          } 
                        default:
                          wrapper.write(Type.STRING, name);
                          wrapper.passthrough((Type)Type.VAR_INT);
                          break;
                      } 
                    } 
                    if (newSize != size)
                      wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(newSize)); 
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\packets\PlayerPacket1_13.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */