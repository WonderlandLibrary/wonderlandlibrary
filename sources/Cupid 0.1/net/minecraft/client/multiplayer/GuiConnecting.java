package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiConnecting extends GuiScreen {
  private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
  
  private static final Logger logger = LogManager.getLogger();
  
  private NetworkManager networkManager;
  
  private boolean cancel;
  
  private final GuiScreen previousGuiScreen;
  
  public GuiConnecting(GuiScreen p_i1181_1_, Minecraft mcIn, ServerData p_i1181_3_) {
    this.mc = mcIn;
    this.previousGuiScreen = p_i1181_1_;
    ServerAddress serveraddress = ServerAddress.func_78860_a(p_i1181_3_.serverIP);
    mcIn.loadWorld((WorldClient)null);
    mcIn.setServerData(p_i1181_3_);
    connect(serveraddress.getIP(), serveraddress.getPort());
  }
  
  public GuiConnecting(GuiScreen p_i1182_1_, Minecraft mcIn, String hostName, int port) {
    this.mc = mcIn;
    this.previousGuiScreen = p_i1182_1_;
    mcIn.loadWorld((WorldClient)null);
    connect(hostName, port);
  }
  
  private void connect(final String ip, final int port) {
    logger.info("Connecting to " + ip + ", " + port);
    (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet()) {
        public void run() {
          InetAddress inetaddress = null;
          try {
            if (GuiConnecting.this.cancel)
              return; 
            inetaddress = InetAddress.getByName(ip);
            GuiConnecting.this.networkManager = NetworkManager.func_181124_a(inetaddress, port, GuiConnecting.this.mc.gameSettings.func_181148_f());
            GuiConnecting.this.networkManager.setNetHandler((INetHandler)new NetHandlerLoginClient(GuiConnecting.this.networkManager, GuiConnecting.this.mc, GuiConnecting.this.previousGuiScreen));
            GuiConnecting.this.networkManager.sendPacket((Packet)new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
            GuiConnecting.this.networkManager.sendPacket((Packet)new C00PacketLoginStart(GuiConnecting.this.mc.getSession().getProfile()));
          } catch (UnknownHostException unknownhostexception) {
            if (GuiConnecting.this.cancel)
              return; 
            GuiConnecting.logger.error("Couldn't connect to server", unknownhostexception);
            GuiConnecting.this.mc.displayGuiScreen((GuiScreen)new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[] { "Unknown host" })));
          } catch (Exception exception) {
            if (GuiConnecting.this.cancel)
              return; 
            GuiConnecting.logger.error("Couldn't connect to server", exception);
            String s = exception.toString();
            if (inetaddress != null) {
              String s1 = inetaddress.toString() + ":" + port;
              s = s.replaceAll(s1, "");
            } 
            GuiConnecting.this.mc.displayGuiScreen((GuiScreen)new GuiDisconnected(GuiConnecting.this.previousGuiScreen, "connect.failed", (IChatComponent)new ChatComponentTranslation("disconnect.genericReason", new Object[] { s })));
          } 
        }
      }).start();
  }
  
  public void updateScreen() {
    if (this.networkManager != null)
      if (this.networkManager.isChannelOpen()) {
        this.networkManager.processReceivedPackets();
      } else {
        this.networkManager.checkDisconnected();
      }  
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {}
  
  public void initGui() {
    this.buttonList.clear();
    this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 0) {
      this.cancel = true;
      if (this.networkManager != null)
        this.networkManager.closeChannel((IChatComponent)new ChatComponentText("Aborted")); 
      this.mc.displayGuiScreen(this.previousGuiScreen);
    } 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    if (this.networkManager == null) {
      drawCenteredString(this.fontRendererObj, I18n.format("connect.connecting", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
    } else {
      drawCenteredString(this.fontRendererObj, I18n.format("connect.authorizing", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
    } 
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\multiplayer\GuiConnecting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */