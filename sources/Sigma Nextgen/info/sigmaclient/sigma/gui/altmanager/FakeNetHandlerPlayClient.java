package info.sigmaclient.sigma.gui.altmanager;

import com.google.common.base.MoreObjects;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;

import javax.annotation.Nullable;

public class FakeNetHandlerPlayClient extends ClientPlayNetHandler
{
    public class AddPlayerData
    {
        private final int ping;
        private final GameType gamemode;
        private final GameProfile profile;
        private final ITextComponent displayName;

        public AddPlayerData(GameProfile profileIn, int latencyIn, @Nullable GameType gameModeIn, @Nullable ITextComponent displayNameIn)
        {
            this.profile = profileIn;
            this.ping = latencyIn;
            this.gamemode = gameModeIn;
            this.displayName = displayNameIn;
        }

        public GameProfile getProfile()
        {
            return this.profile;
        }

        public int getPing()
        {
            return this.ping;
        }

        public GameType getGameMode()
        {
            return this.gamemode;
        }

        @Nullable
        public ITextComponent getDisplayName()
        {
            return this.displayName;
        }

        public String toString()
        {
            return MoreObjects.toStringHelper(this).add("latency", this.ping).add("gameMode", this.gamemode).add("profile", this.profile).add("displayName", this.displayName == null ? null : ITextComponent.Serializer.toJson(this.displayName)).toString();
        }
    }
    private NetworkPlayerInfo playerInfo;
    
    public FakeNetHandlerPlayClient(final Minecraft mcIn) {
        super(mcIn, mcIn.currentScreen, (NetworkManager) new FakeNetworkManager(PacketDirection.CLIENTBOUND), mcIn.getSession().getProfile());
        this.playerInfo = new NetworkPlayerInfo(new AddPlayerData(mcIn.session.getProfile(), 0, GameType.SURVIVAL, new StringTextComponent("")));
    }
    
    public NetworkPlayerInfo getPlayerInfo(final String name) {
        return this.playerInfo;
    }
}