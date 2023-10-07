package net.minecraft.client.multiplayer;

import cc.novoline.utils.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
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
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiConnecting extends GuiScreen {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
    private final GuiScreen previousGuiScreen;
    private NetworkManager networkManager;
    private boolean cancel;

    private static final HashMap<String, Integer> HYPIXEL_IP_RANGES = new HashMap<>();

    static {
        // TODO: Поддержка IPv6
        HYPIXEL_IP_RANGES.put("209.222.114.0", 23);
        HYPIXEL_IP_RANGES.put("173.245.48.0", 20);
        HYPIXEL_IP_RANGES.put("103.21.244.0", 22);
        HYPIXEL_IP_RANGES.put("103.22.200.0", 22);
        HYPIXEL_IP_RANGES.put("103.31.4.0", 22);
        HYPIXEL_IP_RANGES.put("141.101.64.0", 18);
        HYPIXEL_IP_RANGES.put("108.162.192.0", 20);
        HYPIXEL_IP_RANGES.put("190.93.240.0", 20);
        HYPIXEL_IP_RANGES.put("188.114.96.0", 20);
        HYPIXEL_IP_RANGES.put("197.234.240.0", 22);
        HYPIXEL_IP_RANGES.put("198.41.128.0", 17);
        HYPIXEL_IP_RANGES.put("162.158.0.0", 15);
        HYPIXEL_IP_RANGES.put("104.16.0.0", 12);
        HYPIXEL_IP_RANGES.put("172.64.0.0", 13);
        HYPIXEL_IP_RANGES.put("131.0.72.0", 22);
    }

    public GuiConnecting(GuiScreen previousScreen, @NonNull Minecraft mc, @NonNull ServerData serverData) {
        this.mc = mc;
        previousGuiScreen = previousScreen;
        final ServerAddress serverAddress = ServerAddress.func_78860_a(serverData.serverIP);

        mc.loadWorld(null);
        mc.setServerData(serverData);

        connect(serverAddress.getIP(), serverAddress.getPort());
    }

    public GuiConnecting(GuiScreen previousScreen, @NonNull Minecraft mc, String hostName, int port) {
        this.mc = mc;
        previousGuiScreen = previousScreen;

        mc.loadWorld(null);
        connect(hostName, port);
    }

    /**
     * Checks if the given IP is within a subnet
     *
     * @param ip     IP address
     * @param net    Subnet range
     * @param prefix Subnet prefix
     */
    private boolean isIpInSubnet(final String ip, final String net, final int prefix) {
        try {
            byte[] ipBin = InetAddress.getByName(ip).getAddress();
            byte[] netBin = InetAddress.getByName(net).getAddress();

            if (ipBin.length != netBin.length) {
                return false;
            }

            int p = prefix;
            int i = 0;

            while (p >= 8) {
                if (ipBin[i] != netBin[i]) {
                    return false;
                }

                ++i;
                p -= 8;
            }

            int m = 65280 >> p & 255;
            return (ipBin[i] & m) == (netBin[i] & m);
        } catch (final Throwable t) {
            return false;
        }
    }

    public void connect(final String ip, final int port) {
        LOGGER.info("Connecting to {}, {}", ip, port);

        new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()) {

            @Override
            public void run() {
                InetAddress address = null;

                try {
                    if (cancel) {
                        return;
                    }

                    address = InetAddress.getByName(ip);

                    if (ip.endsWith("hypixel.net") || ip.endsWith("hypixel.net.")) {
                        boolean inHypixelSubnet = false;

                        for (Map.Entry<String, Integer> entry : HYPIXEL_IP_RANGES.entrySet()) {
                            String subnet = entry.getKey();
                            int prefix = entry.getValue();

                            if (isIpInSubnet(ip, subnet, prefix)) {
                                inHypixelSubnet = true;
                                break;
                            }
                        }

                        if (!inHypixelSubnet) {
                            LOGGER.warn("Connecting to *.hypixel.net, but the IP ({}) is not within any of the hypixel ranges", address);
                            ServerUtils.setFakeHypixel(true);
                        } else {
                            ServerUtils.setFakeHypixel(false);
                        }
                    }

                    ServerUtils.checkHypixel(mc.getCurrentServerData());

                    networkManager = NetworkManager.createNetworkManagerAndConnect(address, port, mc.gameSettings.func_181148_f());
                    networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, mc, previousGuiScreen));
                    networkManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
                    networkManager.sendPacket(new C00PacketLoginStart(mc.getSession().getProfile()));

                } catch (UnknownHostException e) {
                    if (cancel) {
                        return;
                    }

                    GuiConnecting.LOGGER.error("Couldn't connect to server", e);
                    mc.displayGuiScreen(
                            new GuiDisconnected(previousGuiScreen, "connect.failed",
                                    new ChatComponentTranslation("disconnect.genericReason", "Unknown host")));
                } catch (Exception e) {
                    if (cancel) {
                        return;
                    }

                    GuiConnecting.LOGGER.error("Couldn't connect to server", e);
                    String s = e.toString();

                    if (address != null) {
                        final String s1 = address.toString() + ":" + port;
                        s = s.replaceAll(s1, "");
                    }

                    mc.displayGuiScreen(
                            new GuiDisconnected(previousGuiScreen, "connect.failed",
                                    new ChatComponentTranslation("disconnect.genericReason", s)));
                }
            }
        }.start();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        if (networkManager != null) {
            if (networkManager.isChannelOpen()) {
                networkManager.processReceivedPackets();
            } else {
                networkManager.checkDisconnected();
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel")));

        super.initGui();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            cancel = true;

            if (networkManager != null) {
                networkManager.closeChannel(new ChatComponentText("Aborted"));
            }

            mc.displayGuiScreen(previousGuiScreen);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        if (networkManager == null) {
            drawCenteredString(fontRendererObj, I18n.format("connect.connecting"), width / 2,
                    height / 2 - 50, 16777215);
        } else {
            drawCenteredString(fontRendererObj, I18n.format("connect.authorizing"), width / 2,
                    height / 2 - 50, 16777215);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
