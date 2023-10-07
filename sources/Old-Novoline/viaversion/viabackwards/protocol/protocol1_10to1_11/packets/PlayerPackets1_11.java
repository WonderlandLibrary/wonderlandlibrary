package viaversion.viabackwards.protocol.protocol1_10to1_11.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import viaversion.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.remapper.ValueTransformer;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;

public class PlayerPackets1_11 {
    private static final ValueTransformer<Short, Float> TO_NEW_FLOAT = new ValueTransformer<Short, Float>(Type.FLOAT) {
        @Override
        public Float transform(PacketWrapper wrapper, Short inputValue) throws Exception {
            return inputValue / 15f;
        }
    };

    public void register(Protocol1_10To1_11 protocol) {
        protocol.registerOutgoing(ClientboundPackets1_9_3.TITLE, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.VAR_INT); // 0 - Action

                handler(wrapper -> {
                    int action = wrapper.get(Type.VAR_INT, 0);

                    if (action == 2) { // Handle the new ActionBar
                        JsonElement message = wrapper.read(Type.COMPONENT);

                        wrapper.clearPacket();
                        wrapper.setId(ClientboundPackets1_9_3.CHAT_MESSAGE.ordinal());

                        // https://bugs.mojang.com/browse/MC-119145to
                        BaseComponent[] parsed = ComponentSerializer.parse(message.toString());
                        String legacy = TextComponent.toLegacyText(parsed);
                        message = new JsonObject();
                        message.getAsJsonObject().addProperty("text", legacy);

                        wrapper.write(Type.COMPONENT, message);
                        wrapper.write(Type.BYTE, (byte) 2);
                    } else if (action > 2) {
                        wrapper.set(Type.VAR_INT, 0, action - 1); // Move everything one position down
                    }
                });
            }
        });

        protocol.registerOutgoing(ClientboundPackets1_9_3.COLLECT_ITEM, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.VAR_INT); // 0 - Collected entity id
                map(Type.VAR_INT); // 1 - Collector entity id

                handler(wrapper -> wrapper.read(Type.VAR_INT)); // Ignore item pickup count
            }
        });


        protocol.registerIncoming(ServerboundPackets1_9_3.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                map(Type.POSITION); // 0 - Location
                map(Type.VAR_INT); // 1 - Face
                map(Type.VAR_INT); // 2 - Hand

                map(Type.UNSIGNED_BYTE, TO_NEW_FLOAT);
                map(Type.UNSIGNED_BYTE, TO_NEW_FLOAT);
                map(Type.UNSIGNED_BYTE, TO_NEW_FLOAT);
            }
        });
    }
}
