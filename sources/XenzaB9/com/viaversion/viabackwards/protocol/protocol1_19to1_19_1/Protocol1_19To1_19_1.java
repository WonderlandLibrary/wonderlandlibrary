// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_19to1_19_1;

import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import java.util.List;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.NamedTextColor;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19Types;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.util.CipherUtil;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.storage.NonceStorage;
import com.viaversion.viaversion.api.minecraft.ProfileKey;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.storage.ReceivedMessagesStorage;
import com.viaversion.viaversion.api.minecraft.PlayerMessageSignature;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.packets.EntityPackets;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.storage.ChatRegistryStorage;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_19to1_19_1.packets.EntityPackets1_19_1;
import java.util.UUID;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ServerboundPackets1_19;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ServerboundPackets1_19_1;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
import com.viaversion.viaversion.protocols.protocol1_19_1to1_19.ClientboundPackets1_19_1;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_19To1_19_1 extends BackwardsProtocol<ClientboundPackets1_19_1, ClientboundPackets1_19, ServerboundPackets1_19_1, ServerboundPackets1_19>
{
    public static final int SYSTEM_CHAT_ID = 1;
    public static final int GAME_INFO_ID = 2;
    private static final UUID ZERO_UUID;
    private static final byte[] EMPTY_BYTES;
    private final EntityPackets1_19_1 entityRewriter;
    private final TranslatableRewriter translatableRewriter;
    
    public Protocol1_19To1_19_1() {
        super(ClientboundPackets1_19_1.class, ClientboundPackets1_19.class, ServerboundPackets1_19_1.class, ServerboundPackets1_19.class);
        this.entityRewriter = new EntityPackets1_19_1(this);
        this.translatableRewriter = new TranslatableRewriter(this);
    }
    
    @Override
    protected void registerPackets() {
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_1.ACTIONBAR);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_1.TITLE_TEXT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19_1.TITLE_SUBTITLE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_19_1.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_19_1.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_19_1.TAB_LIST);
        this.translatableRewriter.registerOpenWindow(ClientboundPackets1_19_1.OPEN_WINDOW);
        this.translatableRewriter.registerCombatKill(ClientboundPackets1_19_1.COMBAT_KILL);
        this.translatableRewriter.registerPing();
        this.entityRewriter.register();
        ((AbstractProtocol<ClientboundPackets1_19_1, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19_1.JOIN_GAME, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.INT);
                this.map(Type.BOOLEAN);
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BYTE);
                this.map(Type.STRING_ARRAY);
                this.map(Type.NBT);
                this.map(Type.STRING);
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final ChatRegistryStorage chatTypeStorage = wrapper.user().get(ChatRegistryStorage.class);
                    chatTypeStorage.clear();
                    final CompoundTag registry = wrapper.get(Type.NBT, 0);
                    final ListTag chatTypes = registry.get("minecraft:chat_type").get("value");
                    chatTypes.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final Tag chatType = iterator.next();
                        final CompoundTag chatTypeCompound = (CompoundTag)chatType;
                        final NumberTag idTag = chatTypeCompound.get("id");
                        chatTypeStorage.addChatType(idTag.asInt(), chatTypeCompound);
                    }
                    registry.put("minecraft:chat_type", EntityPackets.CHAT_REGISTRY.clone());
                    return;
                });
                this.handler(Protocol1_19To1_19_1.this.entityRewriter.worldTrackerHandlerByKey());
            }
        });
        ((Protocol<ClientboundPackets1_19_1, ClientboundPackets1_19, S1, S2>)this).registerClientbound(ClientboundPackets1_19_1.PLAYER_CHAT, ClientboundPackets1_19.SYSTEM_CHAT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                        wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                    }
                    final PlayerMessageSignature signature = wrapper.read(Type.PLAYER_MESSAGE_SIGNATURE);
                    if (!signature.uuid().equals(Protocol1_19To1_19_1.ZERO_UUID) && signature.signatureBytes().length != 0) {
                        final ReceivedMessagesStorage messagesStorage = wrapper.user().get(ReceivedMessagesStorage.class);
                        messagesStorage.add(signature);
                        if (messagesStorage.tickUnacknowledged() > 64) {
                            messagesStorage.resetUnacknowledgedCount();
                            final PacketWrapper chatAckPacket = wrapper.create(ServerboundPackets1_19_1.CHAT_ACK);
                            chatAckPacket.write(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY, messagesStorage.lastSignatures());
                            chatAckPacket.write(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE, null);
                            chatAckPacket.sendToServer(Protocol1_19To1_19_1.class);
                        }
                    }
                    final String plainMessage = wrapper.read(Type.STRING);
                    JsonElement message = null;
                    final JsonElement decoratedMessage = wrapper.read(Type.OPTIONAL_COMPONENT);
                    if (decoratedMessage != null) {
                        message = decoratedMessage;
                    }
                    wrapper.read((Type<Object>)Type.LONG);
                    wrapper.read((Type<Object>)Type.LONG);
                    wrapper.read(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY);
                    final JsonElement unsignedMessage = wrapper.read(Type.OPTIONAL_COMPONENT);
                    if (unsignedMessage != null) {
                        message = unsignedMessage;
                    }
                    if (message == null) {
                        message = GsonComponentSerializer.gson().serializeToTree(Component.text(plainMessage));
                    }
                    final int filterMaskType = wrapper.read((Type<Integer>)Type.VAR_INT);
                    if (filterMaskType == 2) {
                        wrapper.read(Type.LONG_ARRAY_PRIMITIVE);
                    }
                    final int chatTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                    final JsonElement senderName = wrapper.read(Type.COMPONENT);
                    final JsonElement targetName = wrapper.read(Type.OPTIONAL_COMPONENT);
                    final JsonElement decoratedMessage2 = Protocol1_19To1_19_1.this.decorateChatMessage(wrapper, chatTypeId, senderName, targetName, message);
                    if (decoratedMessage2 == null) {
                        wrapper.cancel();
                    }
                    else {
                        Protocol1_19To1_19_1.this.translatableRewriter.processText(decoratedMessage2);
                        wrapper.write(Type.COMPONENT, decoratedMessage2);
                        wrapper.write(Type.VAR_INT, 1);
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_1, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19_1.SYSTEM_CHAT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final JsonElement content = wrapper.passthrough(Type.COMPONENT);
                    Protocol1_19To1_19_1.this.translatableRewriter.processText(content);
                    final boolean overlay = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    wrapper.write(Type.VAR_INT, overlay ? 2 : 1);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_19>)this).registerServerbound(ServerboundPackets1_19.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.LONG);
                this.read(Type.BYTE_ARRAY_PRIMITIVE);
                this.create(Type.BYTE_ARRAY_PRIMITIVE, Protocol1_19To1_19_1.EMPTY_BYTES);
                this.map(Type.BOOLEAN);
                this.handler(wrapper -> {
                    final ReceivedMessagesStorage messagesStorage = wrapper.user().get(ReceivedMessagesStorage.class);
                    messagesStorage.resetUnacknowledgedCount();
                    wrapper.write(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY, messagesStorage.lastSignatures());
                    wrapper.write(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE, null);
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_19>)this).registerServerbound(ServerboundPackets1_19.CHAT_COMMAND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.LONG);
                this.map(Type.LONG);
                this.handler(wrapper -> {
                    for (int signatures = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < signatures; ++i) {
                        wrapper.passthrough(Type.STRING);
                        wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, Protocol1_19To1_19_1.EMPTY_BYTES);
                    }
                    wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                    final ReceivedMessagesStorage messagesStorage = wrapper.user().get(ReceivedMessagesStorage.class);
                    messagesStorage.resetUnacknowledgedCount();
                    wrapper.write(Type.PLAYER_MESSAGE_SIGNATURE_ARRAY, messagesStorage.lastSignatures());
                    wrapper.write(Type.OPTIONAL_PLAYER_MESSAGE_SIGNATURE, null);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_1, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19_1.SERVER_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.OPTIONAL_COMPONENT);
                this.map(Type.OPTIONAL_STRING);
                this.map(Type.BOOLEAN);
                this.read(Type.BOOLEAN);
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    final ProfileKey profileKey = wrapper.read(Type.OPTIONAL_PROFILE_KEY);
                    if (profileKey == null) {
                        wrapper.user().put(new NonceStorage(null));
                    }
                    return;
                });
                this.create(Type.OPTIONAL_PROFILE_KEY, null);
                this.create(Type.OPTIONAL_UUID, null);
            }
        });
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.HELLO.getId(), ClientboundLoginPackets.HELLO.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    if (wrapper.user().get(NonceStorage.class) == null) {
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
                    final boolean isNonce = wrapper.read((Type<Boolean>)Type.BOOLEAN);
                    wrapper.write(Type.BOOLEAN, true);
                    if (isNonce) {
                        wrapper.passthrough(Type.BYTE_ARRAY_PRIMITIVE);
                    }
                    else if (nonceStorage == null || nonceStorage.nonce() == null) {
                        throw new IllegalArgumentException("Server sent nonce is missing");
                    }
                    else {
                        wrapper.read((Type<Object>)Type.LONG);
                        wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                        wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, nonceStorage.nonce());
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
                            ViaBackwards.getPlatform().getLogger().warning("Received unexpected data in velocity:player_info (length=" + data.length + ")");
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19_1, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19_1.CUSTOM_CHAT_COMPLETIONS);
        ((AbstractProtocol<ClientboundPackets1_19_1, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19_1.DELETE_CHAT_MESSAGE);
        ((AbstractProtocol<ClientboundPackets1_19_1, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19_1.PLAYER_CHAT_HEADER);
    }
    
    @Override
    public void init(final UserConnection user) {
        user.put(new ChatRegistryStorage());
        user.put(new ReceivedMessagesStorage());
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_19Types.PLAYER, true));
    }
    
    @Override
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.entityRewriter;
    }
    
    private JsonElement decorateChatMessage(final PacketWrapper wrapper, final int chatTypeId, final JsonElement senderName, final JsonElement targetName, final JsonElement message) {
        CompoundTag chatType = wrapper.user().get(ChatRegistryStorage.class).chatType(chatTypeId);
        if (chatType == null) {
            ViaBackwards.getPlatform().getLogger().warning("Chat message has unknown chat type id " + chatTypeId + ". Message: " + message);
            return null;
        }
        chatType = chatType.get("element").get("chat");
        if (chatType == null) {
            return null;
        }
        final String translationKey = (String)chatType.get("translation_key").getValue();
        final TranslatableComponent.Builder componentBuilder = Component.translatable().key(translationKey);
        final CompoundTag style = chatType.get("style");
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
        final ListTag parameters = chatType.get("parameters");
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
                    case "target": {
                        Preconditions.checkNotNull(targetName, (Object)"Target name is null");
                        argument = targetName;
                        break;
                    }
                    default: {
                        ViaBackwards.getPlatform().getLogger().warning("Unknown parameter for chat decoration: " + element.getValue());
                        break;
                    }
                }
                if (argument != null) {
                    arguments.add(GsonComponentSerializer.gson().deserializeFromTree(argument));
                }
            }
            componentBuilder.args(arguments);
        }
        return GsonComponentSerializer.gson().serializeToTree(((ComponentBuilder<Component, B>)componentBuilder).build());
    }
    
    static {
        ZERO_UUID = new UUID(0L, 0L);
        EMPTY_BYTES = new byte[0];
    }
}
