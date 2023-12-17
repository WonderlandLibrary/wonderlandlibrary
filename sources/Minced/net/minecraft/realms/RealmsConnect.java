// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import org.apache.logging.log4j.LogManager;
import java.net.UnknownHostException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.Minecraft;
import java.net.InetAddress;
import net.minecraft.network.NetworkManager;
import org.apache.logging.log4j.Logger;

public class RealmsConnect
{
    private static final Logger LOGGER;
    private final RealmsScreen onlineScreen;
    private volatile boolean aborted;
    private NetworkManager connection;
    
    public RealmsConnect(final RealmsScreen onlineScreenIn) {
        this.onlineScreen = onlineScreenIn;
    }
    
    public void connect(final String p_connect_1_, final int p_connect_2_) {
        Realms.setConnectedToRealms(true);
        new Thread("Realms-connect-task") {
            @Override
            public void run() {
                InetAddress inetaddress = null;
                try {
                    inetaddress = InetAddress.getByName(p_connect_1_);
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection = NetworkManager.createNetworkManagerAndConnect(inetaddress, p_connect_2_, Minecraft.getMinecraft().gameSettings.isUsingNativeTransport());
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection.setNetHandler(new NetHandlerLoginClient(RealmsConnect.this.connection, Minecraft.getMinecraft(), RealmsConnect.this.onlineScreen.getProxy()));
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection.sendPacket(new C00Handshake(p_connect_1_, p_connect_2_, EnumConnectionState.LOGIN));
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection.sendPacket(new CPacketLoginStart(Minecraft.getMinecraft().getSession().getProfile()));
                }
                catch (UnknownHostException unknownhostexception) {
                    Realms.clearResourcePack();
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.LOGGER.error("Couldn't connect to world", (Throwable)unknownhostexception);
                    Realms.setScreen(new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host '" + p_connect_1_ + "'" })));
                }
                catch (Exception exception) {
                    Realms.clearResourcePack();
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.LOGGER.error("Couldn't connect to world", (Throwable)exception);
                    String s = exception.toString();
                    if (inetaddress != null) {
                        final String s2 = inetaddress + ":" + p_connect_2_;
                        s = s.replaceAll(s2, "");
                    }
                    Realms.setScreen(new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] { s })));
                }
            }
        }.start();
    }
    
    public void abort() {
        this.aborted = true;
        if (this.connection != null && this.connection.isChannelOpen()) {
            this.connection.closeChannel(new TextComponentTranslation("disconnect.genericReason", new Object[0]));
            this.connection.handleDisconnection();
        }
    }
    
    public void tick() {
        if (this.connection != null) {
            if (this.connection.isChannelOpen()) {
                this.connection.processReceivedPackets();
            }
            else {
                this.connection.handleDisconnection();
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
