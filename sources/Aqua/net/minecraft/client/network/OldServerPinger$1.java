package net.minecraft.client.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.ArrayUtils;

/*
 * Exception performing whole class analysis ignored.
 */
class OldServerPinger.1
implements INetHandlerStatusClient {
    private boolean field_147403_d = false;
    private boolean field_183009_e = false;
    private long field_175092_e = 0L;
    final /* synthetic */ NetworkManager val$networkmanager;
    final /* synthetic */ ServerData val$server;

    OldServerPinger.1(NetworkManager networkManager, ServerData serverData) {
        this.val$networkmanager = networkManager;
        this.val$server = serverData;
    }

    public void handleServerInfo(S00PacketServerInfo packetIn) {
        if (this.field_183009_e) {
            this.val$networkmanager.closeChannel((IChatComponent)new ChatComponentText("Received unrequested status"));
        } else {
            this.field_183009_e = true;
            ServerStatusResponse serverstatusresponse = packetIn.getResponse();
            this.val$server.serverMOTD = serverstatusresponse.getServerDescription() != null ? serverstatusresponse.getServerDescription().getFormattedText() : "";
            if (serverstatusresponse.getProtocolVersionInfo() != null) {
                this.val$server.gameVersion = serverstatusresponse.getProtocolVersionInfo().getName();
                this.val$server.version = serverstatusresponse.getProtocolVersionInfo().getProtocol();
            } else {
                this.val$server.gameVersion = "Old";
                this.val$server.version = 0;
            }
            if (serverstatusresponse.getPlayerCountData() != null) {
                this.val$server.populationInfo = EnumChatFormatting.GRAY + "" + serverstatusresponse.getPlayerCountData().getOnlinePlayerCount() + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + serverstatusresponse.getPlayerCountData().getMaxPlayers();
                if (ArrayUtils.isNotEmpty((Object[])serverstatusresponse.getPlayerCountData().getPlayers())) {
                    StringBuilder stringbuilder = new StringBuilder();
                    for (GameProfile gameprofile : serverstatusresponse.getPlayerCountData().getPlayers()) {
                        if (stringbuilder.length() > 0) {
                            stringbuilder.append("\n");
                        }
                        stringbuilder.append(gameprofile.getName());
                    }
                    if (serverstatusresponse.getPlayerCountData().getPlayers().length < serverstatusresponse.getPlayerCountData().getOnlinePlayerCount()) {
                        if (stringbuilder.length() > 0) {
                            stringbuilder.append("\n");
                        }
                        stringbuilder.append("... and ").append(serverstatusresponse.getPlayerCountData().getOnlinePlayerCount() - serverstatusresponse.getPlayerCountData().getPlayers().length).append(" more ...");
                    }
                    this.val$server.playerList = stringbuilder.toString();
                }
            } else {
                this.val$server.populationInfo = EnumChatFormatting.DARK_GRAY + "???";
            }
            if (serverstatusresponse.getFavicon() != null) {
                String s = serverstatusresponse.getFavicon();
                if (s.startsWith("data:image/png;base64,")) {
                    this.val$server.setBase64EncodedIconData(s.substring("data:image/png;base64,".length()));
                } else {
                    OldServerPinger.access$000().error("Invalid server icon (unknown format)");
                }
            } else {
                this.val$server.setBase64EncodedIconData((String)null);
            }
            this.field_175092_e = Minecraft.getSystemTime();
            this.val$networkmanager.sendPacket((Packet)new C01PacketPing(this.field_175092_e));
            this.field_147403_d = true;
        }
    }

    public void handlePong(S01PacketPong packetIn) {
        long i = this.field_175092_e;
        long j = Minecraft.getSystemTime();
        this.val$server.pingToServer = j - i;
        this.val$networkmanager.closeChannel((IChatComponent)new ChatComponentText("Finished"));
    }

    public void onDisconnect(IChatComponent reason) {
        if (!this.field_147403_d) {
            OldServerPinger.access$000().error("Can't ping " + this.val$server.serverIP + ": " + reason.getUnformattedText());
            this.val$server.serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
            this.val$server.populationInfo = "";
            OldServerPinger.access$100((OldServerPinger)OldServerPinger.this, (ServerData)this.val$server);
        }
    }
}
