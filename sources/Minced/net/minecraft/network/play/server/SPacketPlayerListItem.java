// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import javax.annotation.Nullable;
import net.minecraft.network.INetHandler;
import com.google.common.base.MoreObjects;
import java.io.IOException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.PacketBuffer;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketPlayerListItem implements Packet<INetHandlerPlayClient>
{
    private Action action;
    private final List<AddPlayerData> players;
    
    public SPacketPlayerListItem() {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
    }
    
    public SPacketPlayerListItem(final Action actionIn, final EntityPlayerMP... playersIn) {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
        this.action = actionIn;
        for (final EntityPlayerMP entityplayermp : playersIn) {
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.interactionManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }
    
    public SPacketPlayerListItem(final Action actionIn, final Iterable<EntityPlayerMP> playersIn) {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
        this.action = actionIn;
        for (final EntityPlayerMP entityplayermp : playersIn) {
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.interactionManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
        for (int i = buf.readVarInt(), j = 0; j < i; ++j) {
            GameProfile gameprofile = null;
            int k = 0;
            GameType gametype = null;
            ITextComponent itextcomponent = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    gameprofile = new GameProfile(buf.readUniqueId(), buf.readString(16));
                    for (int l = buf.readVarInt(), i2 = 0; i2 < l; ++i2) {
                        final String s = buf.readString(32767);
                        final String s2 = buf.readString(32767);
                        if (buf.readBoolean()) {
                            gameprofile.getProperties().put((Object)s, (Object)new Property(s, s2, buf.readString(32767)));
                        }
                        else {
                            gameprofile.getProperties().put((Object)s, (Object)new Property(s, s2));
                        }
                    }
                    gametype = GameType.getByID(buf.readVarInt());
                    k = buf.readVarInt();
                    if (buf.readBoolean()) {
                        itextcomponent = buf.readTextComponent();
                        break;
                    }
                    break;
                }
                case UPDATE_GAME_MODE: {
                    gameprofile = new GameProfile(buf.readUniqueId(), (String)null);
                    gametype = GameType.getByID(buf.readVarInt());
                    break;
                }
                case UPDATE_LATENCY: {
                    gameprofile = new GameProfile(buf.readUniqueId(), (String)null);
                    k = buf.readVarInt();
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    gameprofile = new GameProfile(buf.readUniqueId(), (String)null);
                    if (buf.readBoolean()) {
                        itextcomponent = buf.readTextComponent();
                        break;
                    }
                    break;
                }
                case REMOVE_PLAYER: {
                    gameprofile = new GameProfile(buf.readUniqueId(), (String)null);
                    break;
                }
            }
            this.players.add(new AddPlayerData(gameprofile, k, gametype, itextcomponent));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
        buf.writeVarInt(this.players.size());
        for (final AddPlayerData spacketplayerlistitem$addplayerdata : this.players) {
            switch (this.action) {
                case ADD_PLAYER: {
                    buf.writeUniqueId(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeString(spacketplayerlistitem$addplayerdata.getProfile().getName());
                    buf.writeVarInt(spacketplayerlistitem$addplayerdata.getProfile().getProperties().size());
                    for (final Property property : spacketplayerlistitem$addplayerdata.getProfile().getProperties().values()) {
                        buf.writeString(property.getName());
                        buf.writeString(property.getValue());
                        if (property.hasSignature()) {
                            buf.writeBoolean(true);
                            buf.writeString(property.getSignature());
                        }
                        else {
                            buf.writeBoolean(false);
                        }
                    }
                    buf.writeVarInt(spacketplayerlistitem$addplayerdata.getGameMode().getID());
                    buf.writeVarInt(spacketplayerlistitem$addplayerdata.getPing());
                    if (spacketplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        continue;
                    }
                    buf.writeBoolean(true);
                    buf.writeTextComponent(spacketplayerlistitem$addplayerdata.getDisplayName());
                    continue;
                }
                case UPDATE_GAME_MODE: {
                    buf.writeUniqueId(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarInt(spacketplayerlistitem$addplayerdata.getGameMode().getID());
                    continue;
                }
                case UPDATE_LATENCY: {
                    buf.writeUniqueId(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarInt(spacketplayerlistitem$addplayerdata.getPing());
                    continue;
                }
                case UPDATE_DISPLAY_NAME: {
                    buf.writeUniqueId(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    if (spacketplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        continue;
                    }
                    buf.writeBoolean(true);
                    buf.writeTextComponent(spacketplayerlistitem$addplayerdata.getDisplayName());
                    continue;
                }
                case REMOVE_PLAYER: {
                    buf.writeUniqueId(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    continue;
                }
            }
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handlePlayerListItem(this);
    }
    
    public List<AddPlayerData> getEntries() {
        return this.players;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper((Object)this).add("action", (Object)this.action).add("entries", (Object)this.players).toString();
    }
    
    public enum Action
    {
        ADD_PLAYER, 
        UPDATE_GAME_MODE, 
        UPDATE_LATENCY, 
        UPDATE_DISPLAY_NAME, 
        REMOVE_PLAYER;
    }
    
    public class AddPlayerData
    {
        private final int ping;
        private final GameType gamemode;
        private final GameProfile profile;
        private final ITextComponent displayName;
        
        public AddPlayerData(final GameProfile profileIn, final int latencyIn, @Nullable final GameType gameModeIn, final ITextComponent displayNameIn) {
            this.profile = profileIn;
            this.ping = latencyIn;
            this.gamemode = gameModeIn;
            this.displayName = displayNameIn;
        }
        
        public GameProfile getProfile() {
            return this.profile;
        }
        
        public int getPing() {
            return this.ping;
        }
        
        public GameType getGameMode() {
            return this.gamemode;
        }
        
        @Nullable
        public ITextComponent getDisplayName() {
            return this.displayName;
        }
        
        @Override
        public String toString() {
            return MoreObjects.toStringHelper((Object)this).add("latency", this.ping).add("gameMode", (Object)this.gamemode).add("profile", (Object)this.profile).add("displayName", (Object)((this.displayName == null) ? null : ITextComponent.Serializer.componentToJson(this.displayName))).toString();
        }
    }
}
