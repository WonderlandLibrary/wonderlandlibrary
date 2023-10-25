package net.minecraft.client.multiplayer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiConnecting.1
extends Thread {
    final /* synthetic */ String val$ip;
    final /* synthetic */ int val$port;

    GuiConnecting.1(String x0, String string, int n) {
        this.val$ip = string;
        this.val$port = n;
        super(x0);
    }

    public void run() {
        InetAddress inetaddress = null;
        try {
            if (GuiConnecting.access$000((GuiConnecting)GuiConnecting.this)) {
                return;
            }
            inetaddress = InetAddress.getByName((String)this.val$ip);
            GuiConnecting.access$102((GuiConnecting)GuiConnecting.this, (NetworkManager)NetworkManager.createNetworkManagerAndConnect((InetAddress)inetaddress, (int)this.val$port, (boolean)GuiConnecting.access$200((GuiConnecting)GuiConnecting.this).gameSettings.isUsingNativeTransport()));
            GuiConnecting.access$100((GuiConnecting)GuiConnecting.this).setNetHandler((INetHandler)new NetHandlerLoginClient(GuiConnecting.access$100((GuiConnecting)GuiConnecting.this), GuiConnecting.access$300((GuiConnecting)GuiConnecting.this), GuiConnecting.access$400((GuiConnecting)GuiConnecting.this)));
            GuiConnecting.access$100((GuiConnecting)GuiConnecting.this).sendPacket((Packet)new C00Handshake(47, this.val$ip, this.val$port, EnumConnectionState.LOGIN));
            GuiConnecting.access$100((GuiConnecting)GuiConnecting.this).sendPacket((Packet)new C00PacketLoginStart(GuiConnecting.access$500((GuiConnecting)GuiConnecting.this).getSession().getProfile()));
        }
        catch (UnknownHostException unknownhostexception) {
            if (GuiConnecting.access$000((GuiConnecting)GuiConnecting.this)) {
                return;
            }
            GuiConnecting.access$600().error("Couldn't connect to server", (Throwable)unknownhostexception);
            GuiConnecting.access$700((GuiConnecting)GuiConnecting.this).displayGuiScreen((GuiScreen)new GuiDisconnected(GuiConnecting.access$400((GuiConnecting)GuiConnecting.this), "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host"})));
        }
        catch (Exception exception) {
            if (GuiConnecting.access$000((GuiConnecting)GuiConnecting.this)) {
                return;
            }
            GuiConnecting.access$600().error("Couldn't connect to server", (Throwable)exception);
            String s = exception.toString();
            if (inetaddress != null) {
                String s1 = inetaddress.toString() + ":" + this.val$port;
                s = s.replaceAll(s1, "");
            }
            GuiConnecting.access$800((GuiConnecting)GuiConnecting.this).displayGuiScreen((GuiScreen)new GuiDisconnected(GuiConnecting.access$400((GuiConnecting)GuiConnecting.this), "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[]{s})));
        }
    }
}
