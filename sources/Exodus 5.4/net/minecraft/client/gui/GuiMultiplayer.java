/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer
extends GuiScreen
implements GuiYesNoCallback {
    private LanServerDetector.LanServerList lanServerList;
    private GuiScreen parentScreen;
    private boolean deletingServer;
    private GuiButton btnDeleteServer;
    private boolean directConnect;
    private boolean editingServer;
    private ServerSelectionList serverListSelector;
    private ServerData selectedServer;
    private GuiButton btnEditServer;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private String hoveringText;
    private boolean addingServer;
    private boolean initialized;
    private final OldServerPinger oldServerPinger = new OldServerPinger();
    private ServerList savedServerList;
    private GuiButton btnSelectServer;
    private static final Logger logger = LogManager.getLogger();

    public void selectServer(int n) {
        this.serverListSelector.setSelectedSlotIndex(n);
        GuiListExtended.IGuiListEntry iGuiListEntry = n < 0 ? null : this.serverListSelector.getListEntry(n);
        this.btnSelectServer.enabled = false;
        this.btnEditServer.enabled = false;
        this.btnDeleteServer.enabled = false;
        if (iGuiListEntry != null && !(iGuiListEntry instanceof ServerListEntryLanScan)) {
            this.btnSelectServer.enabled = true;
            if (iGuiListEntry instanceof ServerListEntryNormal) {
                this.btnEditServer.enabled = true;
                this.btnDeleteServer.enabled = true;
            }
        }
    }

    public void createButtons() {
        this.btnEditServer = new GuiButton(7, width / 2 - 154, height - 28, 70, 20, I18n.format("selectServer.edit", new Object[0]));
        this.buttonList.add(this.btnEditServer);
        this.btnDeleteServer = new GuiButton(2, width / 2 - 74, height - 28, 70, 20, I18n.format("selectServer.delete", new Object[0]));
        this.buttonList.add(this.btnDeleteServer);
        this.btnSelectServer = new GuiButton(1, width / 2 - 154, height - 52, 100, 20, I18n.format("selectServer.select", new Object[0]));
        this.buttonList.add(this.btnSelectServer);
        this.buttonList.add(new GuiButton(4, width / 2 - 50, height - 52, 100, 20, I18n.format("selectServer.direct", new Object[0])));
        this.buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 52, 100, 20, I18n.format("selectServer.add", new Object[0])));
        this.buttonList.add(new GuiButton(8, width / 2 + 4, height - 28, 70, 20, I18n.format("selectServer.refresh", new Object[0])));
        this.buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 28, 75, 20, I18n.format("gui.cancel", new Object[0])));
        this.selectServer(this.serverListSelector.func_148193_k());
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        if (this.lanServerDetector != null) {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }
        this.oldServerPinger.clearPendingNetworks();
    }

    public void func_175393_b(ServerListEntryNormal serverListEntryNormal, int n, boolean bl) {
        int n2 = bl ? this.savedServerList.countServers() - 1 : n + 1;
        this.savedServerList.swapServers(n, n2);
        if (this.serverListSelector.func_148193_k() == n) {
            this.selectServer(n2);
        }
        this.serverListSelector.func_148195_a(this.savedServerList);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            GuiListExtended.IGuiListEntry iGuiListEntry;
            GuiListExtended.IGuiListEntry iGuiListEntry2 = iGuiListEntry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
            if (guiButton.id == 2 && iGuiListEntry instanceof ServerListEntryNormal) {
                String string = ((ServerListEntryNormal)iGuiListEntry).getServerData().serverName;
                if (string != null) {
                    this.deletingServer = true;
                    String string2 = I18n.format("selectServer.deleteQuestion", new Object[0]);
                    String string3 = "'" + string + "' " + I18n.format("selectServer.deleteWarning", new Object[0]);
                    String string4 = I18n.format("selectServer.deleteButton", new Object[0]);
                    String string5 = I18n.format("gui.cancel", new Object[0]);
                    GuiYesNo guiYesNo = new GuiYesNo(this, string2, string3, string4, string5, this.serverListSelector.func_148193_k());
                    this.mc.displayGuiScreen(guiYesNo);
                }
            } else if (guiButton.id == 1) {
                this.connectToSelected();
            } else if (guiButton.id == 4) {
                this.directConnect = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
                this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer));
            } else if (guiButton.id == 3) {
                this.addingServer = true;
                this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            } else if (guiButton.id == 7 && iGuiListEntry instanceof ServerListEntryNormal) {
                this.editingServer = true;
                ServerData serverData = ((ServerListEntryNormal)iGuiListEntry).getServerData();
                this.selectedServer = new ServerData(serverData.serverName, serverData.serverIP, false);
                this.selectedServer.copyFrom(serverData);
                this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            } else if (guiButton.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (guiButton.id == 8) {
                this.refreshServerList();
            }
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.hoveringText = null;
        this.drawDefaultBackground();
        this.serverListSelector.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.title", new Object[0]), width / 2, 20, 0xFFFFFF);
        super.drawScreen(n, n2, f);
        if (this.hoveringText != null) {
            this.drawHoveringText(Lists.newArrayList((Iterable)Splitter.on((String)"\n").split((CharSequence)this.hoveringText)), n, n2);
        }
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        this.serverListSelector.mouseClicked(n, n2, n3);
    }

    public OldServerPinger getOldServerPinger() {
        return this.oldServerPinger;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.lanServerList.getWasUpdated()) {
            List<LanServerDetector.LanServer> list = this.lanServerList.getLanServers();
            this.lanServerList.setWasNotUpdated();
            this.serverListSelector.func_148194_a(list);
        }
        this.oldServerPinger.pingPendingNetworks();
    }

    public ServerList getServerList() {
        return this.savedServerList;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        if (!this.initialized) {
            this.initialized = true;
            this.savedServerList = new ServerList(this.mc);
            this.savedServerList.loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();
            try {
                this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
                this.lanServerDetector.start();
            }
            catch (Exception exception) {
                logger.warn("Unable to start LAN server detection: " + exception.getMessage());
            }
            this.serverListSelector = new ServerSelectionList(this, this.mc, width, height, 32, height - 64, 36);
            this.serverListSelector.func_148195_a(this.savedServerList);
        } else {
            this.serverListSelector.setDimensions(width, height, 32, height - 64);
        }
        this.createButtons();
    }

    public void connectToSelected() {
        GuiListExtended.IGuiListEntry iGuiListEntry;
        GuiListExtended.IGuiListEntry iGuiListEntry2 = iGuiListEntry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
        if (iGuiListEntry instanceof ServerListEntryNormal) {
            this.connectToServer(((ServerListEntryNormal)iGuiListEntry).getServerData());
        } else if (iGuiListEntry instanceof ServerListEntryLanDetected) {
            LanServerDetector.LanServer lanServer = ((ServerListEntryLanDetected)iGuiListEntry).getLanServer();
            this.connectToServer(new ServerData(lanServer.getServerMotd(), lanServer.getServerIpPort(), true));
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.serverListSelector.handleMouseInput();
    }

    public boolean func_175392_a(ServerListEntryNormal serverListEntryNormal, int n) {
        return n > 0;
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        GuiListExtended.IGuiListEntry iGuiListEntry;
        int n2 = this.serverListSelector.func_148193_k();
        GuiListExtended.IGuiListEntry iGuiListEntry2 = iGuiListEntry = n2 < 0 ? null : this.serverListSelector.getListEntry(n2);
        if (n == 63) {
            this.refreshServerList();
        } else if (n2 >= 0) {
            if (n == 200) {
                if (GuiMultiplayer.isShiftKeyDown()) {
                    if (n2 > 0 && iGuiListEntry instanceof ServerListEntryNormal) {
                        this.savedServerList.swapServers(n2, n2 - 1);
                        this.selectServer(this.serverListSelector.func_148193_k() - 1);
                        this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                        this.serverListSelector.func_148195_a(this.savedServerList);
                    }
                } else if (n2 > 0) {
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
            } else if (n == 208) {
                if (GuiMultiplayer.isShiftKeyDown()) {
                    if (n2 < this.savedServerList.countServers() - 1) {
                        this.savedServerList.swapServers(n2, n2 + 1);
                        this.selectServer(n2 + 1);
                        this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                        this.serverListSelector.func_148195_a(this.savedServerList);
                    }
                } else if (n2 < this.serverListSelector.getSize()) {
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
            } else if (n != 28 && n != 156) {
                super.keyTyped(c, n);
            } else {
                this.actionPerformed((GuiButton)this.buttonList.get(2));
            }
        } else {
            super.keyTyped(c, n);
        }
    }

    public void setHoveringText(String string) {
        this.hoveringText = string;
    }

    @Override
    public void confirmClicked(boolean bl, int n) {
        GuiListExtended.IGuiListEntry iGuiListEntry;
        GuiListExtended.IGuiListEntry iGuiListEntry2 = iGuiListEntry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
        if (this.deletingServer) {
            this.deletingServer = false;
            if (bl && iGuiListEntry instanceof ServerListEntryNormal) {
                this.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.func_148195_a(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        } else if (this.directConnect) {
            this.directConnect = false;
            if (bl) {
                this.connectToServer(this.selectedServer);
            } else {
                this.mc.displayGuiScreen(this);
            }
        } else if (this.addingServer) {
            this.addingServer = false;
            if (bl) {
                this.savedServerList.addServerData(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.func_148195_a(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        } else if (this.editingServer) {
            this.editingServer = false;
            if (bl && iGuiListEntry instanceof ServerListEntryNormal) {
                ServerData serverData = ((ServerListEntryNormal)iGuiListEntry).getServerData();
                serverData.serverName = this.selectedServer.serverName;
                serverData.serverIP = this.selectedServer.serverIP;
                serverData.copyFrom(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.func_148195_a(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
        }
    }

    public GuiMultiplayer(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
    }

    public void func_175391_a(ServerListEntryNormal serverListEntryNormal, int n, boolean bl) {
        int n2 = bl ? 0 : n - 1;
        this.savedServerList.swapServers(n, n2);
        if (this.serverListSelector.func_148193_k() == n) {
            this.selectServer(n2);
        }
        this.serverListSelector.func_148195_a(this.savedServerList);
    }

    public boolean func_175394_b(ServerListEntryNormal serverListEntryNormal, int n) {
        return n < this.savedServerList.countServers() - 1;
    }

    private void refreshServerList() {
        this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
    }

    private void connectToServer(ServerData serverData) {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, serverData));
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        super.mouseReleased(n, n2, n3);
        this.serverListSelector.mouseReleased(n, n2, n3);
    }
}

