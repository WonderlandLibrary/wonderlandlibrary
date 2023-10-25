/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiConnecting
extends GuiScreen {
    private boolean cancel;
    private static final Logger logger;
    private static final AtomicInteger CONNECTION_ID;
    private final GuiScreen previousGuiScreen;
    private NetworkManager networkManager;

    static {
        CONNECTION_ID = new AtomicInteger(0);
        logger = LogManager.getLogger();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        if (this.networkManager == null) {
            this.drawCenteredString(this.fontRendererObj, I18n.format("connect.connecting", new Object[0]), width / 2, height / 2 - 50, 0xFFFFFF);
        } else {
            this.drawCenteredString(this.fontRendererObj, I18n.format("connect.authorizing", new Object[0]), width / 2, height / 2 - 50, 0xFFFFFF);
        }
        super.drawScreen(n, n2, f);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.id == 0) {
            this.cancel = true;
            if (this.networkManager != null) {
                this.networkManager.closeChannel(new ChatComponentText("Aborted"));
            }
            this.mc.displayGuiScreen(this.previousGuiScreen);
        }
    }

    public GuiConnecting(GuiScreen guiScreen, Minecraft minecraft, ServerData serverData) {
        this.mc = minecraft;
        this.previousGuiScreen = guiScreen;
        ServerAddress serverAddress = ServerAddress.func_78860_a(serverData.serverIP);
        minecraft.loadWorld(null);
        minecraft.setServerData(serverData);
        this.connect(serverAddress.getIP(), serverAddress.getPort());
    }

    public GuiConnecting(GuiScreen guiScreen, Minecraft minecraft, String string, int n) {
        this.mc = minecraft;
        this.previousGuiScreen = guiScreen;
        minecraft.loadWorld(null);
        this.connect(string, n);
    }

    @Override
    public void updateScreen() {
        if (this.networkManager != null) {
            if (this.networkManager.isChannelOpen()) {
                this.networkManager.processReceivedPackets();
            } else {
                this.networkManager.checkDisconnected();
            }
        }
    }

    private void connect(final String string, final int n) {
        logger.info("Connecting to " + string + ", " + n);
        new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()){

            @Override
            public void run() {
                InetAddress inetAddress = null;
                try {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    inetAddress = InetAddress.getByName(string);
                    GuiConnecting.this.networkManager = NetworkManager.func_181124_a(inetAddress, n, Minecraft.gameSettings.func_181148_f());
                    GuiConnecting.this.networkManager.setNetHandler(new NetHandlerLoginClient(GuiConnecting.this.networkManager, GuiConnecting.this.mc, GuiConnecting.this.previousGuiScreen));
                    GuiConnecting.this.networkManager.sendPacket(new C00Handshake(47, string, n, EnumConnectionState.LOGIN));
                    GuiConnecting.this.networkManager.sendPacket(new C00PacketLoginStart(GuiConnecting.this.mc.getSession().getProfile()));
                }
                catch (UnknownHostException unknownHostException) {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    logger.error("Couldn't connect to server", (Throwable)unknownHostException);
                    GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", "Unknown host")));
                }
                catch (Exception exception) {
                    if (GuiConnecting.this.cancel) {
                        return;
                    }
                    logger.error("Couldn't connect to server", (Throwable)exception);
                    String string3 = exception.toString();
                    if (inetAddress != null) {
                        String string2 = String.valueOf(inetAddress.toString()) + ":" + n;
                        string3 = string3.replaceAll(string2, "");
                    }
                    GuiConnecting.this.mc.displayGuiScreen(new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", string3)));
                }
            }
        }.start();
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
    }
}

