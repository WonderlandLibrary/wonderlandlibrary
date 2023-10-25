package wtf.evolution.command.impl;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@CommandInfo(name = "kick")
public class KickCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        new Thread(() -> {
            String ip = ((InetSocketAddress) mc.player.connection.getNetworkManager().getRemoteAddress()).getAddress().getHostAddress();
            String ip2 = ((InetSocketAddress) mc.player.connection.getNetworkManager().getRemoteAddress()).getAddress().getHostName();
            int port = ((InetSocketAddress) mc.player.connection.getNetworkManager().getRemoteAddress()).getPort();
            InetAddress inetaddress = null;
            try {
                inetaddress = InetAddress.getByName(ip);
            } catch (UnknownHostException e) {
            }
            GuiConnecting.networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, (Minecraft.getMinecraft()).gameSettings.isUsingNativeTransport());
            GuiConnecting.networkManager.setNetHandler(new NetHandlerHandshakeTCP(mc.player.getServer(), GuiConnecting.networkManager));
            GuiConnecting.networkManager.sendPacket(new C00Handshake(ip2, port, EnumConnectionState.LOGIN));
            GuiConnecting.networkManager.sendPacket(new CPacketLoginStart(new GameProfile(null, args[1])));
            ChatUtil.print("Kicked " + args[1]);
        }).start();

    }
}
