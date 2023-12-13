package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.multiplayer.GuiConnecting.1;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ChatComponentText;
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
      this.connect(serveraddress.getIP(), serveraddress.getPort());
   }

   public GuiConnecting(GuiScreen p_i1182_1_, Minecraft mcIn, String hostName, int port) {
      this.mc = mcIn;
      this.previousGuiScreen = p_i1182_1_;
      mcIn.loadWorld((WorldClient)null);
      this.connect(hostName, port);
   }

   // $FF: synthetic method
   static GuiScreen access$100(GuiConnecting x0) {
      return x0.previousGuiScreen;
   }

   // $FF: synthetic method
   static Minecraft access$200(GuiConnecting x0) {
      return x0.mc;
   }

   // $FF: synthetic method
   static NetworkManager access$300(GuiConnecting x0) {
      return x0.networkManager;
   }

   // $FF: synthetic method
   static boolean access$000(GuiConnecting x0) {
      return x0.cancel;
   }

   private void connect(String ip, int port) {
      logger.info("Connecting to " + ip + ", " + port);
      (new 1(this, "Server Connector #" + CONNECTION_ID.incrementAndGet(), ip, port)).start();
   }

   // $FF: synthetic method
   static NetworkManager access$302(GuiConnecting x0, NetworkManager x1) {
      return x0.networkManager = x1;
   }

   // $FF: synthetic method
   static Minecraft access$400(GuiConnecting x0) {
      return x0.mc;
   }

   // $FF: synthetic method
   static Minecraft access$500(GuiConnecting x0) {
      return x0.mc;
   }

   // $FF: synthetic method
   static Minecraft access$600(GuiConnecting x0) {
      return x0.mc;
   }

   // $FF: synthetic method
   static Logger access$700() {
      return logger;
   }

   // $FF: synthetic method
   static Minecraft access$800(GuiConnecting x0) {
      return x0.mc;
   }

   // $FF: synthetic method
   static Minecraft access$900(GuiConnecting x0) {
      return x0.mc;
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         this.cancel = true;
         if(this.networkManager != null) {
            this.networkManager.closeChannel(new ChatComponentText("Aborted"));
         }

         this.mc.displayGuiScreen(this.previousGuiScreen);
      }

   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      if(this.networkManager == null) {
         this.drawCenteredString(this.fontRendererObj, I18n.format("connect.connecting", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
      } else {
         this.drawCenteredString(this.fontRendererObj, I18n.format("connect.authorizing", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void updateScreen() {
      if(this.networkManager != null) {
         if(this.networkManager.isChannelOpen()) {
            this.networkManager.processReceivedPackets();
         } else {
            this.networkManager.checkDisconnected();
         }
      }

   }

   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
   }
}
