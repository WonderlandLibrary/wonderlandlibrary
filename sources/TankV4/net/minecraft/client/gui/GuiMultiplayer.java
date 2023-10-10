package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen implements GuiYesNoCallback {
   private ServerList savedServerList;
   private ServerSelectionList serverListSelector;
   private GuiButton btnSelectServer;
   private static final Logger logger = LogManager.getLogger();
   private ServerData selectedServer;
   private final OldServerPinger oldServerPinger = new OldServerPinger();
   private LanServerDetector.LanServerList lanServerList;
   private boolean deletingServer;
   private LanServerDetector.ThreadLanServerFind lanServerDetector;
   private GuiScreen parentScreen;
   private String hoveringText;
   private boolean addingServer;
   private GuiButton btnEditServer;
   private boolean editingServer;
   private GuiButton btnDeleteServer;
   private boolean initialized;
   private boolean directConnect;

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.serverListSelector.handleMouseInput();
   }

   public void createButtons() {
      this.buttonList.add(this.btnEditServer = new GuiButton(7, width / 2 - 154, height - 28, 70, 20, I18n.format("selectServer.edit")));
      this.buttonList.add(this.btnDeleteServer = new GuiButton(2, width / 2 - 74, height - 28, 70, 20, I18n.format("selectServer.delete")));
      this.buttonList.add(this.btnSelectServer = new GuiButton(1, width / 2 - 154, height - 52, 100, 20, I18n.format("selectServer.select")));
      this.buttonList.add(new GuiButton(4, width / 2 - 50, height - 52, 100, 20, I18n.format("selectServer.direct")));
      this.buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 52, 100, 20, I18n.format("selectServer.add")));
      this.buttonList.add(new GuiButton(8, width / 2 + 4, height - 28, 70, 20, I18n.format("selectServer.refresh")));
      this.buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 28, 75, 20, I18n.format("gui.cancel")));
      this.selectServer(this.serverListSelector.func_148193_k());
   }

   public void setHoveringText(String var1) {
      this.hoveringText = var1;
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      if (this.lanServerDetector != null) {
         this.lanServerDetector.interrupt();
         this.lanServerDetector = null;
      }

      this.oldServerPinger.clearPendingNetworks();
   }

   public void confirmClicked(boolean var1, int var2) {
      GuiListExtended.IGuiListEntry var3 = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
      if (this.deletingServer) {
         this.deletingServer = false;
         if (var1 && var3 instanceof ServerListEntryNormal) {
            this.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
            this.savedServerList.saveServerList();
            this.serverListSelector.setSelectedSlotIndex(-1);
            this.serverListSelector.func_148195_a(this.savedServerList);
         }

         this.mc.displayGuiScreen(this);
      } else if (this.directConnect) {
         this.directConnect = false;
         if (var1) {
            this.connectToServer(this.selectedServer);
         } else {
            this.mc.displayGuiScreen(this);
         }
      } else if (this.addingServer) {
         this.addingServer = false;
         if (var1) {
            this.savedServerList.addServerData(this.selectedServer);
            this.savedServerList.saveServerList();
            this.serverListSelector.setSelectedSlotIndex(-1);
            this.serverListSelector.func_148195_a(this.savedServerList);
         }

         this.mc.displayGuiScreen(this);
      } else if (this.editingServer) {
         this.editingServer = false;
         if (var1 && var3 instanceof ServerListEntryNormal) {
            ServerData var4 = ((ServerListEntryNormal)var3).getServerData();
            var4.serverName = this.selectedServer.serverName;
            var4.serverIP = this.selectedServer.serverIP;
            var4.copyFrom(this.selectedServer);
            this.savedServerList.saveServerList();
            this.serverListSelector.func_148195_a(this.savedServerList);
         }

         this.mc.displayGuiScreen(this);
      }

   }

   public void func_175391_a(ServerListEntryNormal var1, int var2, boolean var3) {
      int var4 = var3 ? 0 : var2 - 1;
      this.savedServerList.swapServers(var2, var4);
      if (this.serverListSelector.func_148193_k() == var2) {
         this.selectServer(var4);
      }

      this.serverListSelector.func_148195_a(this.savedServerList);
   }

   protected void mouseReleased(int var1, int var2, int var3) {
      super.mouseReleased(var1, var2, var3);
      this.serverListSelector.mouseReleased(var1, var2, var3);
   }

   public OldServerPinger getOldServerPinger() {
      return this.oldServerPinger;
   }

   private void refreshServerList() {
      this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      if (!this.initialized) {
         this.initialized = true;
         this.savedServerList = new ServerList(this.mc);
         this.savedServerList.loadServerList();
         this.lanServerList = new LanServerDetector.LanServerList();

         try {
            this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
            this.lanServerDetector.start();
         } catch (Exception var2) {
            logger.warn("Unable to start LAN server detection: " + var2.getMessage());
         }

         this.serverListSelector = new ServerSelectionList(this, this.mc, width, height, 32, height - 64, 36);
         this.serverListSelector.func_148195_a(this.savedServerList);
      } else {
         this.serverListSelector.setDimensions(width, height, 32, height - 64);
      }

      this.createButtons();
   }

   public void updateScreen() {
      super.updateScreen();
      if (this.lanServerList.getWasUpdated()) {
         List var1 = this.lanServerList.getLanServers();
         this.lanServerList.setWasNotUpdated();
         this.serverListSelector.func_148194_a(var1);
      }

      this.oldServerPinger.pingPendingNetworks();
   }

   public boolean func_175394_b(ServerListEntryNormal var1, int var2) {
      return var2 < this.savedServerList.countServers() - 1;
   }

   private void connectToServer(ServerData var1) {
      this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, var1));
   }

   public ServerList getServerList() {
      return this.savedServerList;
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         GuiListExtended.IGuiListEntry var2 = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
         if (var1.id == 2 && var2 instanceof ServerListEntryNormal) {
            String var9 = ((ServerListEntryNormal)var2).getServerData().serverName;
            if (var9 != null) {
               this.deletingServer = true;
               String var4 = I18n.format("selectServer.deleteQuestion");
               String var5 = "'" + var9 + "' " + I18n.format("selectServer.deleteWarning");
               String var6 = I18n.format("selectServer.deleteButton");
               String var7 = I18n.format("gui.cancel");
               GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.serverListSelector.func_148193_k());
               this.mc.displayGuiScreen(var8);
            }
         } else if (var1.id == 1) {
            this.connectToSelected();
         } else if (var1.id == 4) {
            this.directConnect = true;
            this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false)));
         } else if (var1.id == 3) {
            this.addingServer = true;
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName"), "", false)));
         } else if (var1.id == 7 && var2 instanceof ServerListEntryNormal) {
            this.editingServer = true;
            ServerData var3 = ((ServerListEntryNormal)var2).getServerData();
            this.selectedServer = new ServerData(var3.serverName, var3.serverIP, false);
            this.selectedServer.copyFrom(var3);
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
         } else if (var1.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (var1.id == 8) {
            this.refreshServerList();
         }
      }

   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      super.mouseClicked(var1, var2, var3);
      this.serverListSelector.mouseClicked(var1, var2, var3);
   }

   public GuiMultiplayer(GuiScreen var1) {
      this.parentScreen = var1;
   }

   public void func_175393_b(ServerListEntryNormal var1, int var2, boolean var3) {
      int var4 = var3 ? this.savedServerList.countServers() - 1 : var2 + 1;
      this.savedServerList.swapServers(var2, var4);
      if (this.serverListSelector.func_148193_k() == var2) {
         this.selectServer(var4);
      }

      this.serverListSelector.func_148195_a(this.savedServerList);
   }

   public void connectToSelected() {
      GuiListExtended.IGuiListEntry var1 = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
      if (var1 instanceof ServerListEntryNormal) {
         this.connectToServer(((ServerListEntryNormal)var1).getServerData());
      } else if (var1 instanceof ServerListEntryLanDetected) {
         LanServerDetector.LanServer var2 = ((ServerListEntryLanDetected)var1).getLanServer();
         this.connectToServer(new ServerData(var2.getServerMotd(), var2.getServerIpPort(), true));
      }

   }

   public boolean func_175392_a(ServerListEntryNormal var1, int var2) {
      return var2 > 0;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.hoveringText = null;
      this.drawDefaultBackground();
      this.serverListSelector.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.title"), width / 2, 20, 16777215);
      super.drawScreen(var1, var2, var3);
      if (this.hoveringText != null) {
         this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), var1, var2);
      }

   }

   public void selectServer(int var1) {
      this.serverListSelector.setSelectedSlotIndex(var1);
      GuiListExtended.IGuiListEntry var2 = var1 < 0 ? null : this.serverListSelector.getListEntry(var1);
      this.btnSelectServer.enabled = false;
      this.btnEditServer.enabled = false;
      this.btnDeleteServer.enabled = false;
      if (var2 != null && !(var2 instanceof ServerListEntryLanScan)) {
         this.btnSelectServer.enabled = true;
         if (var2 instanceof ServerListEntryNormal) {
            this.btnEditServer.enabled = true;
            this.btnDeleteServer.enabled = true;
         }
      }

   }

   protected void keyTyped(char var1, int var2) throws IOException {
      int var3 = this.serverListSelector.func_148193_k();
      GuiListExtended.IGuiListEntry var4 = var3 < 0 ? null : this.serverListSelector.getListEntry(var3);
      if (var2 == 63) {
         this.refreshServerList();
      } else if (var3 >= 0) {
         if (var2 == 200) {
            if (isShiftKeyDown()) {
               if (var3 > 0 && var4 instanceof ServerListEntryNormal) {
                  this.savedServerList.swapServers(var3, var3 - 1);
                  this.selectServer(this.serverListSelector.func_148193_k() - 1);
                  this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                  this.serverListSelector.func_148195_a(this.savedServerList);
               }
            } else if (var3 > 0) {
               this.selectServer(this.serverListSelector.func_148193_k() - 1);
               this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
               if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
                  if (this.serverListSelector.func_148193_k() > 0) {
                     this.selectServer(this.serverListSelector.getSize() - 1);
                     this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                  } else {
                     this.selectServer(-1);
                  }
               }
            } else {
               this.selectServer(-1);
            }
         } else if (var2 == 208) {
            if (isShiftKeyDown()) {
               if (var3 < this.savedServerList.countServers() - 1) {
                  this.savedServerList.swapServers(var3, var3 + 1);
                  this.selectServer(var3 + 1);
                  this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                  this.serverListSelector.func_148195_a(this.savedServerList);
               }
            } else if (var3 < this.serverListSelector.getSize()) {
               this.selectServer(this.serverListSelector.func_148193_k() + 1);
               this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
               if (this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
                  if (this.serverListSelector.func_148193_k() < this.serverListSelector.getSize() - 1) {
                     this.selectServer(this.serverListSelector.getSize() + 1);
                     this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                  } else {
                     this.selectServer(-1);
                  }
               }
            } else {
               this.selectServer(-1);
            }
         } else if (var2 != 28 && var2 != 156) {
            super.keyTyped(var1, var2);
         } else {
            this.actionPerformed((GuiButton)this.buttonList.get(2));
         }
      } else {
         super.keyTyped(var1, var2);
      }

   }
}
