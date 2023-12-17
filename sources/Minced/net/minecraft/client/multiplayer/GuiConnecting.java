// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import java.io.IOException;
import java.net.UnknownHostException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.network.NetHandlerLoginClient;
import java.net.InetAddress;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.gui.GuiScreen;

public class GuiConnecting extends GuiScreen
{
    private static final AtomicInteger CONNECTION_ID;
    private static final Logger LOGGER;
    private NetworkManager networkManager;
    private boolean cancel;
    private final GuiScreen previousGuiScreen;
    
    public GuiConnecting(final GuiScreen parent, final Minecraft mcIn, final ServerData serverDataIn) {
        GuiConnecting.mc = mcIn;
        this.previousGuiScreen = parent;
        final ServerAddress serveraddress = ServerAddress.fromString(serverDataIn.serverIP);
        mcIn.loadWorld(null);
        mcIn.setServerData(serverDataIn);
        this.connect(serveraddress.getIP(), serveraddress.getPort());
    }
    
    public GuiConnecting(final GuiScreen parent, final Minecraft mcIn, final String hostName, final int port) {
        GuiConnecting.mc = mcIn;
        this.previousGuiScreen = parent;
        mcIn.loadWorld(null);
        this.connect(hostName, port);
    }
    
    private void connect(final String ip, final int port) {
        GuiConnecting.LOGGER.info("Connecting to {}, {}", (Object)ip, (Object)port);
        new Thread("Server Connector #" + GuiConnecting.CONNECTION_ID.incrementAndGet()) {
            @Override
            public void run() {
                InetAddress inetaddress = null;
                try {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    inetaddress = InetAddress.getByName(ip);
                    final GuiConnecting this$0 = GuiConnecting.this;
                    final InetAddress address = inetaddress;
                    final int val$port = port;
                    final GuiConnecting this$2 = GuiConnecting.this;
                    this$0.networkManager = NetworkManager.createNetworkManagerAndConnect(address, val$port, GuiConnecting.mc.gameSettings.isUsingNativeTransport());
                    final NetworkManager access$100 = GuiConnecting.this.networkManager;
                    final NetworkManager access$101 = GuiConnecting.this.networkManager;
                    final GuiConnecting this$3 = GuiConnecting.this;
                    access$100.setNetHandler(new NetHandlerLoginClient(access$101, GuiConnecting.mc, GuiConnecting.this.previousGuiScreen));
                    GuiConnecting.this.networkManager.sendPacket(new C00Handshake(ip, port, EnumConnectionState.LOGIN));
                    final NetworkManager access$102 = GuiConnecting.this.networkManager;
                    final GuiConnecting this$4 = GuiConnecting.this;
                    access$102.sendPacket(new CPacketLoginStart(GuiConnecting.mc.getSession().getProfile()));
                }
                catch (UnknownHostException unknownhostexception) {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    GuiConnecting.LOGGER.error("Couldn't connect to server", (Throwable)unknownhostexception);
                    final GuiConnecting this$5 = GuiConnecting.this;
                    GuiConnecting.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host" })));
                }
                catch (Exception exception) {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    GuiConnecting.LOGGER.error("Couldn't connect to server", (Throwable)exception);
                    String s = exception.toString();
                    if (inetaddress != null) {
                        final String s2 = inetaddress + ":" + port;
                        s = s.replaceAll(s2, "");
                    }
                    final GuiConnecting this$6 = GuiConnecting.this;
                    GuiConnecting.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", new Object[] { s })));
                }
            }
        }.start();
    }
    
    @Override
    public void updateScreen() {
        if (this.networkManager != null) {
            if (this.networkManager.isChannelOpen()) {
                this.networkManager.processReceivedPackets();
            }
            else {
                this.networkManager.handleDisconnection();
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.cancel = true;
            if (this.networkManager != null) {
                this.networkManager.closeChannel(new TextComponentString("Aborted"));
            }
            GuiConnecting.mc.displayGuiScreen(this.previousGuiScreen);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        if (this.networkManager == null) {
            this.drawCenteredString(this.fontRenderer, I18n.format("connect.connecting", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        }
        else {
            this.drawCenteredString(this.fontRenderer, I18n.format("connect.authorizing", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    static {
        CONNECTION_ID = new AtomicInteger(0);
        LOGGER = LogManager.getLogger();
    }
}
