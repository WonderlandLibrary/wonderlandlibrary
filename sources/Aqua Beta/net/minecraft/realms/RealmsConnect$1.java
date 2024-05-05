package net.minecraft.realms;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsConnect;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

/*
 * Exception performing whole class analysis ignored.
 */
class RealmsConnect.1
extends Thread {
    final /* synthetic */ String val$p_connect_1_;
    final /* synthetic */ int val$p_connect_2_;

    RealmsConnect.1(String arg0, String string, int n) {
        this.val$p_connect_1_ = string;
        this.val$p_connect_2_ = n;
        super(arg0);
    }

    public void run() {
        InetAddress inetaddress = null;
        try {
            inetaddress = InetAddress.getByName((String)this.val$p_connect_1_);
            if (RealmsConnect.access$000((RealmsConnect)RealmsConnect.this)) {
                return;
            }
            if (RealmsConnect.access$000((RealmsConnect)RealmsConnect.this)) {
                return;
            }
            RealmsConnect.access$100((RealmsConnect)RealmsConnect.this).setNetHandler((INetHandler)new NetHandlerLoginClient(RealmsConnect.access$100((RealmsConnect)RealmsConnect.this), Minecraft.getMinecraft(), (GuiScreen)RealmsConnect.access$200((RealmsConnect)RealmsConnect.this).getProxy()));
            if (RealmsConnect.access$000((RealmsConnect)RealmsConnect.this)) {
                return;
            }
            RealmsConnect.access$100((RealmsConnect)RealmsConnect.this).sendPacket((Packet)new C00Handshake(47, this.val$p_connect_1_, this.val$p_connect_2_, EnumConnectionState.LOGIN));
            if (RealmsConnect.access$000((RealmsConnect)RealmsConnect.this)) {
                return;
            }
            RealmsConnect.access$100((RealmsConnect)RealmsConnect.this).sendPacket((Packet)new C00PacketLoginStart(Minecraft.getMinecraft().getSession().getProfile()));
        }
        catch (UnknownHostException unknownhostexception) {
            Realms.clearResourcePack();
            if (RealmsConnect.access$000((RealmsConnect)RealmsConnect.this)) {
                return;
            }
            RealmsConnect.access$300().error("Couldn't connect to world", (Throwable)unknownhostexception);
            Minecraft.getMinecraft().getResourcePackRepository().clearResourcePack();
            Realms.setScreen((RealmsScreen)new DisconnectedRealmsScreen(RealmsConnect.access$200((RealmsConnect)RealmsConnect.this), "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Unknown host '" + this.val$p_connect_1_ + "'"})));
        }
        catch (Exception exception) {
            Realms.clearResourcePack();
            if (RealmsConnect.access$000((RealmsConnect)RealmsConnect.this)) {
                return;
            }
            RealmsConnect.access$300().error("Couldn't connect to world", (Throwable)exception);
            String s = exception.toString();
            if (inetaddress != null) {
                String s1 = inetaddress.toString() + ":" + this.val$p_connect_2_;
                s = s.replaceAll(s1, "");
            }
            Realms.setScreen((RealmsScreen)new DisconnectedRealmsScreen(RealmsConnect.access$200((RealmsConnect)RealmsConnect.this), "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[]{s})));
        }
    }
}
