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
import net.minecraft.network.play.server.S38PacketPlayerListItem;
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
            this.players.add((Object)new AddPlayerData(this, entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }

    public S38PacketPlayerListItem(Action actionIn, Iterable<EntityPlayerMP> players) {
        this.action = actionIn;
        for (EntityPlayerMP entityplayermp : players) {
            this.players.add((Object)new AddPlayerData(this, entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.action = (Action)buf.readEnumValue(Action.class);
        int i = buf.readVarIntFromBuffer();
        for (int j = 0; j < i; ++j) {
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
                    worldsettings$gametype = WorldSettings.GameType.getByID((int)buf.readVarIntFromBuffer());
                    k = buf.readVarIntFromBuffer();
                    if (!buf.readBoolean()) break;
                    ichatcomponent = buf.readChatComponent();
                    break;
                }
                case 2: {
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);
                    worldsettings$gametype = WorldSettings.GameType.getByID((int)buf.readVarIntFromBuffer());
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
                }
            }
            this.players.add((Object)new AddPlayerData(this, gameprofile, k, worldsettings$gametype, ichatcomponent));
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue((Enum)this.action);
        buf.writeVarIntToBuffer(this.players.size());
        for (AddPlayerData s38packetplayerlistitem$addplayerdata : this.players) {
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
                }
            }
        }
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handlePlayerListItem(this);
    }

    public List<AddPlayerData> getEntries() {
        return this.players;
    }

    public Action getAction() {
        return this.action;
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("action", (Object)this.action).add("entries", this.players).toString();
    }
}
