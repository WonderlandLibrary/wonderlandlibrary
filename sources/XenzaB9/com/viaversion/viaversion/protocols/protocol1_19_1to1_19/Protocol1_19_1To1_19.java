// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_19_1to1_19;

import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import com.viaversion.viaversion.api.protocol.Protocol;
import java.io.IOException;
import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import java.util.List;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.NamedTextColor;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.util.CipherUtil;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.storage.NonceStorage;
import com.viaversion.viaversion.api.minecraft.ProfileKey;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.api.protocol.packet.State;
import java.util.Iterator;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.storage.ChatTypeStorage;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ServerboundPackets1_19;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public final class Protocol1_19_1To1_19 extends AbstractProtocol<ClientboundPackets1_19, ClientboundPackets1_19_1, ServerboundPackets1_19, ServerboundPackets1_19_1>
{
    private static final String CHAT_REGISTRY_SNBT = "{\n  \"minecraft:chat_type\": {\n    \"type\": \"minecraft:chat_type\",\n    \"value\": [\n         {\n            \"name\":\"minecraft:chat\",\n            \"id\":1,\n            \"element\":{\n               \"chat\":{\n                  \"translation_key\":\"chat.type.text\",\n                  \"parameters\":[\n                     \"sender\",\n                     \"content\"\n                  ]\n               },\n               \"narration\":{\n                  \"translation_key\":\"chat.type.text.narrate\",\n                  \"parameters\":[\n                     \"sender\",\n                     \"content\"\n                  ]\n               }\n            }\n         }    ]\n  }\n}";
    private static final CompoundTag CHAT_REGISTRY;
    
    public Protocol1_19_1To1_19() {
        super(ClientboundPackets1_19.class, ClientboundPackets1_19_1.class, ServerboundPackets1_19.class, ServerboundPackets1_19_1.class);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.SYSTEM_CHAT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.COMPONENT);
                this.handler(wrapper -> {
                    final int type = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final boolean overlay = type == 2;
                    wrapper.write(Type.BOOLEAN, overlay);
                });
            }
        });
        ((Protocol<ClientboundPackets1_19, ClientboundPackets1_19_1, S1, S2>)this).registerClientbound(ClientboundPackets1_19.PLAYER_CHAT, ClientboundPackets1_19_1.SYSTEM_CHAT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final JsonElement signedContent = wrapper.read(Type.COMPONENT);
                    final JsonElement unsignedContent = wrapper.read(Type.OPTIONAL_COMPONENT);
                    final int chatTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    wrapper.read(Type.UUID);
                    final JsonElement senderName = wrapper.read(Type.COMPONENT);
                    final JsonElement teamName = wrapper.read(Type.OPTIONAL_COMPONENT);
                    final CompoundTag chatType = wrapper.user().get(ChatTypeStorage.class).chatType(chatTypeId);
                    final ChatDecorationResult decorationResult = Protocol1_19_1To1_19.decorateChatMessage(chatType, chatTypeId, senderName, teamName, (unsignedContent != null) ? unsignedContent : signedContent);
                    if (decorationResult == null) {
                        wrapper.cancel();
                        return;
                    }
                    else {
                        wrapper.write(Type.COMPONENT, decorationResult.content());
                        wrapper.write(Type.BOOLEAN, decorationResult.overlay());
                        return;
                    }
                });
                this.read(Type.LONG);
                this.read(Type.LONG);
                this.read(Type.BYTE_ARRAY_PRIMITIVE);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_19_1>)this).registerServerbound(ServerboundPackets1_19_1.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.LONG);
                this.map(Type.BYTE_ARRAY_PRIMITIVE);
                this.map(Type.BOOLEAN);
                this.read(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY);
                this.read(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_19_1>)this).registerServerbound(ServerboundPackets1_19_1.CHAT_COMMAND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.LONG);
                this.handler(wrapper -> {
                    for (int signatures = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < signatures; ++i) {
                        wrapper.passthrough(Type.STRING);
                        wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                    }
                    return;
                });
                this.map(Type.BOOLEAN);
                this.read(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY);
                this.read(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_19_1>)this).cancelServerbound(ServerboundPackets1_19_1.CHAT_ACK);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.handler(wrapper -> {
                    final ChatTypeStorage chatTypeStorage = wrapper.user().get(ChatTypeStorage.class);
                    chatTypeStorage.clear();
                    final CompoundTag registry = wrapper.passthrough(Type.NBT);
                    final ListTag chatTypes = registry.get("minecraft:chat_type").get("value");
                    chatTypes.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag chatType = iterator.next();
                        final CompoundTag chatTypeCompound = (CompoundTag)chatType;
                        final NumberTag idTag = chatTypeCompound.get("id");
                        chatTypeStorage.addChatType(idTag.asInt(), chatTypeCompound);
                    }
                    registry.put("minecraft:chat_type", Protocol1_19_1To1_19.CHAT_REGISTRY.clone());
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.SERVER_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.OPTIONAL_COMPONENT);
                this.map(Type.OPTIONAL_STRING);
                this.map(Type.BOOLEAN);
                this.create(Type.BOOLEAN, false);
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final ProfileKey profileKey = wrapper.read(Type.OPTIONAL_PROFILE_KEY);
                    wrapper.write(Type.OPTIONAL_PROFILE_KEY, null);
                    if (profileKey == null) {
                        wrapper.user().put(new NonceStorage(null));
                    }
                    return;
                });
                this.read(Type.OPTIONAL_UUID);
            }
        });
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.HELLO.getId(), ClientboundLoginPackets.HELLO.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    if (!wrapper.user().has(NonceStorage.class)) {
                        final byte[] publicKey = wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                        final byte[] nonce = wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                        wrapper.user().put(new NonceStorage(CipherUtil.encryptNonce(publicKey, nonce)));
                    }
                });
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.ENCRYPTION_KEY.getId(), ServerboundLoginPackets.ENCRYPTION_KEY.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE_ARRAY_PRIMITIVE);
                this.handler(wrapper -> {
                    final NonceStorage nonceStorage = wrapper.user().remove(NonceStorage.class);
                    if (nonceStorage.nonce() != null) {
                        final boolean isNonce = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                        wrapper.write(Type.BOOLEAN, true);
                        if (!isNonce) {
                            wrapper.read((Type<Object>)Type.LONG);
                            wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                            wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, nonceStorage.nonce());
                        }
                    }
                });
            }
        });
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.CUSTOM_QUERY.getId(), ClientboundLoginPackets.CUSTOM_QUERY.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final String identifier = wrapper.get(Type.STRING, 0);
                    if (identifier.equals("velocity:player_info")) {
                        final byte[] data = wrapper.passthrough(Type.REMAINING_BYTES);
                        if (data.length == 1 && data[0] > 1) {
                            data[0] = 1;
                        }
                        else if (data.length == 0) {
                            final byte[] data2 = { 1 };
                            wrapper.set(Type.REMAINING_BYTES, 0, data2);
                        }
                        else {
                            Via.getPlatform().getLogger().warning("Received unexpected data in velocity:player_info (length=" + data.length + ")");
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public void init(final UserConnection connection) {
        connection.put(new ChatTypeStorage());
    }
    
    public static ChatDecorationResult decorateChatMessage(final CompoundTag chatType, final int chatTypeId, final JsonElement senderName, final JsonElement teamName, final JsonElement message) {
        if (chatType == null) {
            Via.getPlatform().getLogger().warning("Chat message has unknown chat type id " + chatTypeId + ". Message: " + message);
            return null;
        }
        CompoundTag chatData = chatType.get("element").get("chat");
        boolean overlay = false;
        if (chatData == null) {
            chatData = chatType.get("element").get("overlay");
            if (chatData == null) {
                return null;
            }
            overlay = true;
        }
        final CompoundTag decoaration = chatData.get("decoration");
        if (decoaration == null) {
            return new ChatDecorationResult(message, overlay);
        }
        final String translationKey = (String)decoaration.get("translation_key").getValue();
        final TranslatableComponent.Builder componentBuilder = Component.translatable().key(translationKey);
        final CompoundTag style = decoaration.get("style");
        if (style != null) {
            final Style.Builder styleBuilder = Style.style();
            final StringTag color = style.get("color");
            if (color != null) {
                final NamedTextColor textColor = NamedTextColor.NAMES.value(color.getValue());
                if (textColor != null) {
                    styleBuilder.color(NamedTextColor.NAMES.value(color.getValue()));
                }
            }
            for (final String key : TextDecoration.NAMES.keys()) {
                if (style.contains(key)) {
                    styleBuilder.decoration(TextDecoration.NAMES.value(key), style.get(key).asByte() == 1);
                }
            }
            componentBuilder.style(styleBuilder.build());
        }
        final ListTag parameters = decoaration.get("parameters");
        if (parameters != null) {
            final List<Component> arguments = new ArrayList<Component>();
            for (final Tag element : parameters) {
                JsonElement argument = null;
                final String s = (String)element.getValue();
                switch (s) {
                    case "sender": {
                        argument = senderName;
                        break;
                    }
                    case "content": {
                        argument = message;
                        break;
                    }
                    case "team_name": {
                        Preconditions.checkNotNull(teamName, (Object)"Team name is null");
                        argument = teamName;
                        break;
                    }
                    default: {
                        Via.getPlatform().getLogger().warning("Unknown parameter for chat decoration: " + element.getValue());
                        break;
                    }
                }
                if (argument != null) {
                    arguments.add(GsonComponentSerializer.gson().deserializeFromTree(argument));
                }
            }
            componentBuilder.args(arguments);
        }
        return new ChatDecorationResult(GsonComponentSerializer.gson().serializeToTree(((ComponentBuilder<Component, B>)componentBuilder).build()), overlay);
    }
    
    static {
        try {
            CHAT_REGISTRY = BinaryTagIO.readString("{\n  \"minecraft:chat_type\": {\n    \"type\": \"minecraft:chat_type\",\n    \"value\": [\n         {\n            \"name\":\"minecraft:chat\",\n            \"id\":1,\n            \"element\":{\n               \"chat\":{\n                  \"translation_key\":\"chat.type.text\",\n                  \"parameters\":[\n                     \"sender\",\n                     \"content\"\n                  ]\n               },\n               \"narration\":{\n                  \"translation_key\":\"chat.type.text.narrate\",\n                  \"parameters\":[\n                     \"sender\",\n                     \"content\"\n                  ]\n               }\n            }\n         }    ]\n  }\n}").get("minecraft:chat_type");
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
