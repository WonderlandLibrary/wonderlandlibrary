/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects$ToStringHelper
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 */
package net.minecraft.network.play.server;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.Iterator;
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
        EntityPlayerMP[] entityPlayerMPArray = players;
        int n = entityPlayerMPArray.length;
        int n2 = 0;
        while (n2 < n) {
            EntityPlayerMP entityplayermp = entityPlayerMPArray[n2];
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
            ++n2;
        }
    }

    public S38PacketPlayerListItem(Action actionIn, Iterable<EntityPlayerMP> players) {
        this.action = actionIn;
        Iterator<EntityPlayerMP> iterator = players.iterator();
        while (iterator.hasNext()) {
            EntityPlayerMP entityplayermp = iterator.next();
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
        int i = buf.readVarIntFromBuffer();
        int j = 0;
        while (j < i) {
            GameProfile gameprofile = null;
            int k = 0;
            WorldSettings.GameType worldsettings$gametype = null;
            IChatComponent ichatcomponent = null;
            switch (1.$SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action[this.action.ordinal()]) {
                case 1: {
                    gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
                    int l = buf.readVarIntFromBuffer();
                    for (int i1 = 0; i1 < l; ++i1) {
                        String s = buf.readStringFromBuffer(Short.MAX_VALUE);
                        String s1 = buf.readStringFromBuffer(Short.MAX_VALUE);
                        if (buf.readBoolean()) {
                            gameprofile.getProperties().put((Object)s, (Object)new Property(s, s1, buf.readStringFromBuffer(Short.MAX_VALUE)));
                            continue;
                        }
                        gameprofile.getProperties().put((Object)s, (Object)new Property(s, s1));
                    }
                    worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
                    k = buf.readVarIntFromBuffer();
                    if (!buf.readBoolean()) break;
                    ichatcomponent = buf.readChatComponent();
                    break;
                }
                case 2: {
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);
                    worldsettings$gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
                    break;
                }
                case 3: {
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);
                    k = buf.readVarIntFromBuffer();
                    break;
                }
                case 4: {
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);
                    if (!buf.readBoolean()) break;
                    ichatcomponent = buf.readChatComponent();
                    break;
                }
                case 5: {
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);
                    break;
                }
            }
            this.players.add(new AddPlayerData(gameprofile, k, worldsettings$gametype, ichatcomponent));
            ++j;
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
        buf.writeVarIntToBuffer(this.players.size());
        Iterator<AddPlayerData> iterator = this.players.iterator();
        block7: while (iterator.hasNext()) {
            AddPlayerData s38packetplayerlistitem$addplayerdata = iterator.next();
            switch (1.$SwitchMap$net$minecraft$network$play$server$S38PacketPlayerListItem$Action[this.action.ordinal()]) {
                case 1: {
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
                case 2: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getGameMode().getID());
                    break;
                }
                case 3: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(s38packetplayerlistitem$addplayerdata.getPing());
                    break;
                }
                case 4: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    if (s38packetplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        break;
                    }
                    buf.writeBoolean(true);
                    buf.writeChatComponent(s38packetplayerlistitem$addplayerdata.getDisplayName());
                    break;
                }
                case 5: {
                    buf.writeUuid(s38packetplayerlistitem$addplayerdata.getProfile().getId());
                    continue block7;
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
        return Objects.toStringHelper((Object)this).add("action", (Object)this.action).add("entries", this.players).toString();
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
            String string;
            Objects.ToStringHelper toStringHelper = Objects.toStringHelper((Object)this).add("latency", this.ping).add("gameMode", (Object)this.gamemode).add("profile", (Object)this.profile);
            if (this.displayName == null) {
                string = null;
                return toStringHelper.add("displayName", string).toString();
            }
            string = IChatComponent.Serializer.componentToJson(this.displayName);
            return toStringHelper.add("displayName", string).toString();
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

