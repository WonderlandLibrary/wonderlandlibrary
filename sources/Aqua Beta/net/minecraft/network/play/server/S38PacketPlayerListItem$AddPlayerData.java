package net.minecraft.network.play.server;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;

public class S38PacketPlayerListItem.AddPlayerData {
    private final int ping;
    private final WorldSettings.GameType gamemode;
    private final GameProfile profile;
    private final IChatComponent displayName;

    public S38PacketPlayerListItem.AddPlayerData(GameProfile profile, int pingIn, WorldSettings.GameType gamemodeIn, IChatComponent displayNameIn) {
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
        return Objects.toStringHelper((Object)this).add("latency", this.ping).add("gameMode", (Object)this.gamemode).add("profile", (Object)this.profile).add("displayName", this.displayName == null ? null : IChatComponent.Serializer.componentToJson((IChatComponent)this.displayName)).toString();
    }
}
