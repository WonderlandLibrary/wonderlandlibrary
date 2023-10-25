/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.collect.Lists
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 */
package net.minecraft.network.play.server;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class S38PacketPlayerListItem
implements Packet<INetHandlerPlayClient> {
    private final List<AddPlayerData> players = Lists.newArrayList();
    private Action action;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handlePlayerListItem(this);
    }

    public S38PacketPlayerListItem(Action action, Iterable<EntityPlayerMP> iterable) {
        this.action = action;
        for (EntityPlayerMP entityPlayerMP : iterable) {
            this.players.add(new AddPlayerData(entityPlayerMP.getGameProfile(), entityPlayerMP.ping, entityPlayerMP.theItemInWorldManager.getGameType(), entityPlayerMP.getTabListDisplayName()));
        }
    }

    public S38PacketPlayerListItem(Action action, EntityPlayerMP ... entityPlayerMPArray) {
        this.action = action;
        EntityPlayerMP[] entityPlayerMPArray2 = entityPlayerMPArray;
        int n = entityPlayerMPArray.length;
        int n2 = 0;
        while (n2 < n) {
            EntityPlayerMP entityPlayerMP = entityPlayerMPArray2[n2];
            this.players.add(new AddPlayerData(entityPlayerMP.getGameProfile(), entityPlayerMP.ping, entityPlayerMP.theItemInWorldManager.getGameType(), entityPlayerMP.getTabListDisplayName()));
            ++n2;
        }
    }

    public S38PacketPlayerListItem() {
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("action", (Object)this.action).add("entries", this.players).toString();
    }

    public List<AddPlayerData> func_179767_a() {
        return this.players;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeEnumValue(this.action);
        packetBuffer.writeVarIntToBuffer(this.players.size());
        for (AddPlayerData addPlayerData : this.players) {
            switch (this.action) {
                case ADD_PLAYER: {
                    packetBuffer.writeUuid(addPlayerData.getProfile().getId());
                    packetBuffer.writeString(addPlayerData.getProfile().getName());
                    packetBuffer.writeVarIntToBuffer(addPlayerData.getProfile().getProperties().size());
                    for (Property property : addPlayerData.getProfile().getProperties().values()) {
                        packetBuffer.writeString(property.getName());
                        packetBuffer.writeString(property.getValue());
                        if (property.hasSignature()) {
                            packetBuffer.writeBoolean(true);
                            packetBuffer.writeString(property.getSignature());
                            continue;
                        }
                        packetBuffer.writeBoolean(false);
                    }
                    packetBuffer.writeVarIntToBuffer(addPlayerData.getGameMode().getID());
                    packetBuffer.writeVarIntToBuffer(addPlayerData.getPing());
                    if (addPlayerData.getDisplayName() == null) {
                        packetBuffer.writeBoolean(false);
                        break;
                    }
                    packetBuffer.writeBoolean(true);
                    packetBuffer.writeChatComponent(addPlayerData.getDisplayName());
                    break;
                }
                case UPDATE_GAME_MODE: {
                    packetBuffer.writeUuid(addPlayerData.getProfile().getId());
                    packetBuffer.writeVarIntToBuffer(addPlayerData.getGameMode().getID());
                    break;
                }
                case UPDATE_LATENCY: {
                    packetBuffer.writeUuid(addPlayerData.getProfile().getId());
                    packetBuffer.writeVarIntToBuffer(addPlayerData.getPing());
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    packetBuffer.writeUuid(addPlayerData.getProfile().getId());
                    if (addPlayerData.getDisplayName() == null) {
                        packetBuffer.writeBoolean(false);
                        break;
                    }
                    packetBuffer.writeBoolean(true);
                    packetBuffer.writeChatComponent(addPlayerData.getDisplayName());
                    break;
                }
                case REMOVE_PLAYER: {
                    packetBuffer.writeUuid(addPlayerData.getProfile().getId());
                }
            }
        }
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.action = packetBuffer.readEnumValue(Action.class);
        int n = packetBuffer.readVarIntFromBuffer();
        int n2 = 0;
        while (n2 < n) {
            GameProfile gameProfile = null;
            int n3 = 0;
            WorldSettings.GameType gameType = null;
            IChatComponent iChatComponent = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    gameProfile = new GameProfile(packetBuffer.readUuid(), packetBuffer.readStringFromBuffer(16));
                    int n4 = packetBuffer.readVarIntFromBuffer();
                    int n5 = 0;
                    while (n5 < n4) {
                        String string = packetBuffer.readStringFromBuffer(Short.MAX_VALUE);
                        String string2 = packetBuffer.readStringFromBuffer(Short.MAX_VALUE);
                        if (packetBuffer.readBoolean()) {
                            gameProfile.getProperties().put((Object)string, (Object)new Property(string, string2, packetBuffer.readStringFromBuffer(Short.MAX_VALUE)));
                        } else {
                            gameProfile.getProperties().put((Object)string, (Object)new Property(string, string2));
                        }
                        ++n5;
                    }
                    gameType = WorldSettings.GameType.getByID(packetBuffer.readVarIntFromBuffer());
                    n3 = packetBuffer.readVarIntFromBuffer();
                    if (!packetBuffer.readBoolean()) break;
                    iChatComponent = packetBuffer.readChatComponent();
                    break;
                }
                case UPDATE_GAME_MODE: {
                    gameProfile = new GameProfile(packetBuffer.readUuid(), null);
                    gameType = WorldSettings.GameType.getByID(packetBuffer.readVarIntFromBuffer());
                    break;
                }
                case UPDATE_LATENCY: {
                    gameProfile = new GameProfile(packetBuffer.readUuid(), null);
                    n3 = packetBuffer.readVarIntFromBuffer();
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    gameProfile = new GameProfile(packetBuffer.readUuid(), null);
                    if (!packetBuffer.readBoolean()) break;
                    iChatComponent = packetBuffer.readChatComponent();
                    break;
                }
                case REMOVE_PLAYER: {
                    gameProfile = new GameProfile(packetBuffer.readUuid(), null);
                }
            }
            this.players.add(new AddPlayerData(gameProfile, n3, gameType, iChatComponent));
            ++n2;
        }
    }

    public Action func_179768_b() {
        return this.action;
    }

    public class AddPlayerData {
        private final GameProfile profile;
        private final IChatComponent displayName;
        private final int ping;
        private final WorldSettings.GameType gamemode;

        public String toString() {
            return Objects.toStringHelper((Object)this).add("latency", this.ping).add("gameMode", (Object)this.gamemode).add("profile", (Object)this.profile).add("displayName", this.displayName == null ? null : IChatComponent.Serializer.componentToJson(this.displayName)).toString();
        }

        public int getPing() {
            return this.ping;
        }

        public AddPlayerData(GameProfile gameProfile, int n, WorldSettings.GameType gameType, IChatComponent iChatComponent) {
            this.profile = gameProfile;
            this.ping = n;
            this.gamemode = gameType;
            this.displayName = iChatComponent;
        }

        public GameProfile getProfile() {
            return this.profile;
        }

        public WorldSettings.GameType getGameMode() {
            return this.gamemode;
        }

        public IChatComponent getDisplayName() {
            return this.displayName;
        }
    }

    public static enum Action {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER;

    }
}

