package me.earth.earthhack.impl.modules.client.pingbypass.guis;

import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import me.earth.earthhack.impl.util.text.IdleUtil;
import me.earth.earthhack.impl.util.thread.ThreadUtil;
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
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GuiConnecting for pingbypass.
 */
public class GuiConnectingPingBypass extends GuiScreen
{
    private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
    private static final Logger LOGGER = LogManager.getLogger();

    private NetworkManager networkManager;
    private boolean cancel;
    private final GuiScreen previousGuiScreen;

    public GuiConnectingPingBypass(GuiScreen parent, Minecraft mcIn, ServerData serverDataIn)
    {
        this.mc = mcIn;
        this.previousGuiScreen = parent;
        mcIn.loadWorld(null);
        mcIn.setServerData(serverDataIn);
        ServerAddress serveraddress = ServerAddress.fromString(serverDataIn.serverIP);
        final PingBypass module = PingBypass.getInstance();
        this.connect(module.getIp(), module.getPort(), serveraddress.getIP(), serveraddress.getPort());
    }

    /**
     * Connects to the given server via PingBypass.
     *
     * @param proxyIP the pingbypass ip.
     * @param proxyPort the pingbypass port.
     * @param actualIP ip of the server we want to connect to.
     * @param actualPort port of the server we want to connect to.
     */
    private void connect(final String proxyIP, final int proxyPort, final String actualIP, final int actualPort)
    {
        LOGGER.info("Connecting to PingBypass: {}, {}", proxyIP, proxyPort);
        (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet())
        {
            public void run()
            {
                InetAddress inetaddress = null;

                try
                {
                    if (cancel)
                    {
                        return;
                    }

                    inetaddress = InetAddress.getByName(proxyIP);
                    networkManager = NetworkManager
                            .createNetworkManagerAndConnect(
                                    inetaddress,
                                    proxyPort,
                                    mc.gameSettings.isUsingNativeTransport());

                    networkManager.setNetHandler(
                            new NetHandlerLoginClient(networkManager, mc, previousGuiScreen));
                    networkManager.sendPacket(
                            new C00Handshake(actualIP, actualPort, EnumConnectionState.LOGIN, true));
                    networkManager.sendPacket(
                            new CPacketLoginStart(mc.getSession().getProfile()));
                }
                catch (UnknownHostException e)
                {
                    if (cancel)
                    {
                        return;
                    }

                    LOGGER.error("Couldn't connect to PingBypass", e);
                    ThreadUtil.schedule(() ->
                            mc.displayGuiScreen(
                                    new GuiDisconnected(
                                            previousGuiScreen,
                                            "connect.failed",
                                            new TextComponentTranslation("disconnect.genericReason", "Unknown host"))));
                }
                catch (Exception exception)
                {
                    if (cancel)
                    {
                        return;
                    }

                    LOGGER.error("Couldn't connect to PingBypass", exception);
                    String s = exception.toString();

                    if (inetaddress != null)
                    {
                        String s1 = inetaddress + ":" + proxyPort;
                        s = s.replaceAll(s1, "");
                    }

                    String finalS = s;
                    ThreadUtil.schedule(() ->
                        mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed", new TextComponentTranslation("disconnect.genericReason", finalS))));
                }
            }
        }).start();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (this.networkManager != null)
        {
            if (this.networkManager.isChannelOpen())
            {
                this.networkManager.processReceivedPackets();
            } 
            else
            {
                this.networkManager.handleDisconnection();
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.cancel = true;

            if (this.networkManager != null)
            {
                this.networkManager.closeChannel(new TextComponentString("Aborted"));
            }

            this.mc.displayGuiScreen(this.previousGuiScreen);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        if (this.networkManager == null)
        {
            this.drawCenteredString(this.fontRenderer, "Authentication" + IdleUtil.getDots(), this.width / 2 + IdleUtil.getDots().length(), this.height / 2 - 50, 16777215);
        } 
        else
        {
            this.drawCenteredString(this.fontRenderer, "Loading PingBypass" + IdleUtil.getDots(), this.width / 2 + IdleUtil.getDots().length(), this.height / 2 - 50, 16777215);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
}
