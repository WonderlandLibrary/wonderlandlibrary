/*
 * Decompiled with CFR 0.152.
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
    private Action action;
    private final List<AddPlayerData> players = Lists.newArrayList();

    public S38PacketPlayerListItem() {
    }

    public S38PacketPlayerListItem(Action actionIn, EntityPlayerMP ... players) {
        this.action = actionIn;
        for (EntityPlayerMP entityplayermp : players) {
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }

    public S38PacketPlayerListItem(Action actionIn, Iterable<EntityPlayerMP> players) {
        this.action = actionIn;
        for (EntityPlayerMP entityplayermp : players) {
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
        int i2 = buf.readVarIntFromBuffer();
        for (int j2 = 0; j2 < i2; ++j2) {
            GameProfile gameprofile = null;
            int k2 = 0;
            WorldSettings.GameType worldsettings$gametype = null;
            IChatComponent ichatcomponent = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
                    int l2 = buf.readVarIntFromBuffer();
                    for (int i1 = 0; i1 < l2; ++i1) {
                        String s2 = buf.readStringFromBuffer(Short.MAX_VALUE);
                        String s1 = buf.readStringFromBuffer(Short.MAX_VALUE);
                        if (buf.readBoolean()) {
                            gameprofile.getProperties().put(s2, new Property(s2, s1, buf.readStringFromBuffer(Short.MAX_VALUE)));
                            continue;
                        }
                        gameprofile.getProperties().put(s2, new Property(s2, s1));
                    }
                    worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
                    k2 = buf.readVarIntFromBuffer();
                    if (!buf.readBoolean()) break;
                    ichatcomponent = buf.readChatComponent();
                    break;
                }
                case UPDATE_GAME_MODE: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
                    break;
                }
                case UPDATE_LATENCY: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    k2 = buf.readVarIntFromBuffer();
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    if (!buf.readBoolean()) break;
                    ichatcomponent = buf.readChatComponent();
                    break;
                }
                case REMOVE_PLAYER: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                }
            }
            this.players.add(new AddPlayerData(gameprofile, k2, worldsettings$gametype, ichatcomponent));
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
        buf.writeVarIntToBuffer(this.players.size());
        for (AddPlayerData s38packetplayerlistitem$addplayerdata : this.players) {
            switch (this.action) {
                case ADD_PLAYER: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeString(s38packetplayerlistitem$addplayerdata.getProfile().getName());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getProfile().getProperties().size());
                    for (Property property : s38packetplayerlistitem$addplayerdata.getProfile().getProperties().values()) {
                        buf.writeString(property.getName());
                        buf.writeString(property.getValue());
                        if (property.hasSignature()) {
                            buf.writeBoolean(true);
                            buf.writeString(property.getSignature());
                            continue;
                        }
                        buf.writeBoolean(false);
                    }
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
                    if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        break;
                    }
                    buf.writeBoolean(true);
                    buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
                    break;
                }
                case UPDATE_GAME_MODE: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
                    break;
                }
                case UPDATE_LATENCY: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        break;
                    }
                    buf.writeBoolean(true);
                    buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
                    break;
                }
                case REMOVE_PLAYER: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                }
            }
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handlePlayerListItem(this);
    }

    public List<AddPlayerData> func_179767_a() {
        return this.players;
    }

    public Action func_179768_b() {
        return this.action;
    }

    public String toString() {
        return Objects.toStringHelper(this).add("action", (Object)this.action).add("entries", this.players).toString();
    }

    public class AddPlayerData {
        private final int ping;
        private final WorldSettings.GameType gamemode;
        private final GameProfile profile;
        private final IChatComponent displayName;

        public AddPlayerData(GameProfile profile, int pingIn, WorldSettings.GameType gamemodeIn, IChatComponent displayNameIn) {
            this.profile = profile;
            this.ping = pingIn;
            this.gamemode = gamemodeIn;
            this.displayName = displayNameIn;
        }

        public GameProfile getProfile() {
            return this.profile;
        }

        public int getPing() {
            return this.ping;
        }

        public WorldSettings.GameType getGameMode() {
            return this.gamemode;
        }

        public IChatComponent getDisplayName() {
            return this.displayName;
        }

        public String toString() {
            return Objects.toStringHelper(this).add("latency", this.ping).add("gameMode", (Object)this.gamemode).add("profile", this.profile).add("displayName", this.displayName == null ? null : IChatComponent.Serializer.componentToJson(this.displayName)).toString();
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

