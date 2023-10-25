package nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets;

import nl.matsv.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.viaversion.libs.bungeecordchat.api.chat.BaseComponent;
import us.myles.viaversion.libs.bungeecordchat.api.chat.TextComponent;
import us.myles.viaversion.libs.bungeecordchat.chat.ComponentSerializer;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;

public class PlayerPackets1_11 {
  private static final ValueTransformer<Short, Float> TO_NEW_FLOAT = new ValueTransformer<Short, Float>((Type)Type.FLOAT) {
      public Float transform(PacketWrapper wrapper, Short inputValue) throws Exception {
        return Float.valueOf(inputValue.shortValue() / 15.0F);
      }
    };
  
  public void register(Protocol1_10To1_11 protocol) {
    protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.TITLE, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(wrapper -> {
                  int action = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                  if (action == 2) {
                    JsonElement message = (JsonElement)wrapper.read(Type.COMPONENT);
                    wrapper.clearPacket();
                    wrapper.setId(ClientboundPackets1_9_3.CHAT_MESSAGE.ordinal());
                    BaseComponent[] parsed = ComponentSerializer.parse(message.toString());
                    String legacy = TextComponent.toLegacyText(parsed);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.getAsJsonObject().addProperty("text", legacy);
                    wrapper.write(Type.COMPONENT, jsonObject);
                    wrapper.write(Type.BYTE, Byte.valueOf((byte)2));
                  } else if (action > 2) {
                    wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(action - 1));
                  } 
                });
          }
        });
    protocol.registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.COLLECT_ITEM, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            handler(wrapper -> (Integer)wrapper.read((Type)Type.VAR_INT));
          }
        });
    protocol.registerIncoming((ServerboundPacketType)ServerboundPackets1_9_3.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
          public void registerMap() {
            map(Type.POSITION);
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            map(Type.UNSIGNED_BYTE, PlayerPackets1_11.TO_NEW_FLOAT);
            map(Type.UNSIGNED_BYTE, PlayerPackets1_11.TO_NEW_FLOAT);
            map(Type.UNSIGNED_BYTE, PlayerPackets1_11.TO_NEW_FLOAT);
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_10to1_11\packets\PlayerPackets1_11.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */