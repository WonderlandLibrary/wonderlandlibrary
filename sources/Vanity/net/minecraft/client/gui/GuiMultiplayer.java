package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.masterof13fps.Client;
import com.masterof13fps.features.ui.guiscreens.altmanager.GuiAltManager;
import com.masterof13fps.features.ui.guiscreens.altmanager.GuiProxy;
import com.masterof13fps.utils.render.GLSLSandboxShader;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiMultiplayer extends GuiScreen implements GuiYesNoCallback {
    private static final Logger logger = LogManager.getLogger();
    private final OldServerPinger oldServerPinger = new OldServerPinger();
    private GLSLSandboxShader shader;
    private GuiScreen parentScreen;
    private ServerSelectionList serverListSelector;
    private ServerList savedServerList;
    private GuiButton btnEditServer;
    private GuiButton btnSelectServer;
    private GuiButton btnDeleteServer;
    private GuiButton btnProxy;
    private boolean deletingServer;
    private boolean addingServer;
    private boolean editingServer;
    private boolean directConnect;

    /**
     * The text to be displayed when the player's cursor hovers over a server listing.
     */
    private String hoveringText;
    private ServerData selectedServer;
    private LanServerDetector.LanServerList lanServerList;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private boolean initialized;

    public GuiMultiplayer(GuiScreen parentScreen) {
        try {
            shader = new GLSLSandboxShader("/assets/minecraft/client/shader/main.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load the Shader");
        }

        parentScreen = parentScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();

        if (!initialized) {
            initialized = true;
            savedServerList = new ServerList(mc);
            savedServerList.loadServerList();
            lanServerList = new LanServerDetector.LanServerList();

            try {
                lanServerDetector = new LanServerDetector.ThreadLanServerFind(lanServerList);
                lanServerDetector.start();
            } catch (Exception exception) {
                logger.warn("Unable to start LAN server detection: " + exception.getMessage());
            }

            serverListSelector = new ServerSelectionList(this, mc, width, height, 32, height - 64, 36);
            serverListSelector.func_148195_a(savedServerList);
        } else {
            serverListSelector.setDimensions(width, height, 32, height - 64);
        }

        createButtons();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        serverListSelector.handleMouseInput();
    }

    public void createButtons() {
        buttonList.add(btnEditServer = new GuiButton(7, width / 2 - 154, height - 28, 70, 20, I18n.format("selectServer.edit")));
        buttonList.add(btnDeleteServer = new GuiButton(2, width / 2 - 74, height - 28, 70, 20, I18n.format("selectServer.delete")));
        buttonList.add(btnSelectServer = new GuiButton(1, width / 2 - 154, height - 52, 100, 20, I18n.format("selectServer.select")));
        buttonList.add(new GuiButton(4, width / 2 - 50, height - 52, 100, 20, I18n.format("selectServer.direct")));
        buttonList.add(new GuiButton(3, width / 2 + 4 + 50, height - 52, 100, 20, I18n.format("selectServer.add")));
        buttonList.add(new GuiButton(8, width / 2 + 4, height - 28, 70, 20, I18n.format("selectServer.refresh")));
        buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 28, 75, 20, I18n.format("gui.cancel")));
        buttonList.add(new GuiButton(9, 5, height - 28, 120, 20, "Alt Manager"));
        buttonList.add(new GuiButton(10, 5, height - 52, 120, 20, "Proxy"));
        selectServer(serverListSelector.func_148193_k());
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();

        if (lanServerList.getWasUpdated()) {
            List<LanServerDetector.LanServer> list = lanServerList.getLanServers();
            lanServerList.setWasNotUpdated();
            serverListSelector.func_148194_a(list);
        }

        oldServerPinger.pingPendingNetworks();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);

        if (lanServerDetector != null) {
            lanServerDetector.interrupt();
            lanServerDetector = null;
        }

        oldServerPinger.clearPendingNetworks();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            GuiListExtended.IGuiListEntry guilistextended$iguilistentry = serverListSelector.func_148193_k() < 0 ? null : serverListSelector.getListEntry(serverListSelector.func_148193_k());

            if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                String s4 = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData().serverName;

                if (s4 != null) {
                    deletingServer = true;
                    String s = I18n.format("selectServer.deleteQuestion", new Object[0]);
                    String s1 = "\'" + s4 + "\' " + I18n.format("selectServer.deleteWarning", new Object[0]);
                    String s2 = I18n.format("selectServer.deleteButton", new Object[0]);
                    String s3 = I18n.format("gui.cancel", new Object[0]);
                    GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, serverListSelector.func_148193_k());
                    mc.displayGuiScreen(guiyesno);
                }
            } else if (button.id == 1) {
                connectToSelected();
            } else if (button.id == 4) {
                directConnect = true;
                mc.displayGuiScreen(new GuiScreenServerList(this, selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
            } else if (button.id == 3) {
                addingServer = true;
                mc.displayGuiScreen(new GuiScreenAddServer(this, selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
            } else if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                editingServer = true;
                ServerData serverdata = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData();
                selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
                selectedServer.copyFrom(serverdata);
                mc.displayGuiScreen(new GuiScreenAddServer(this, selectedServer));
            } else if (button.id == 0) {
                mc.displayGuiScreen(parentScreen);
            } else if (button.id == 8) {
                refreshServerList();
            } else if (button.id == 9) {
                mc.displayGuiScreen(new GuiAltManager(this));
            } else if (button.id == 10) {
                mc.displayGuiScreen(new GuiProxy(this));
            }
        }
    }

    private void refreshServerList() {
        mc.displayGuiScreen(new GuiMultiplayer(parentScreen));
    }

    public void confirmClicked(boolean result, int id) {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = serverListSelector.func_148193_k() < 0 ? null : serverListSelector.getListEntry(serverListSelector.func_148193_k());

        if (deletingServer) {
            deletingServer = false;

            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                savedServerList.removeServerData(serverListSelector.func_148193_k());
                savedServerList.saveServerList();
                serverListSelector.setSelectedSlotIndex(-1);
                serverListSelector.func_148195_a(savedServerList);
            }

            mc.displayGuiScreen(this);
        } else if (directConnect) {
            directConnect = false;

            if (result) {
                connectToServer(selectedServer);
            } else {
                mc.displayGuiScreen(this);
            }
        } else if (addingServer) {
            addingServer = false;

            if (result) {
                savedServerList.addServerData(selectedServer);
                savedServerList.saveServerList();
                serverListSelector.setSelectedSlotIndex(-1);
                serverListSelector.func_148195_a(savedServerList);
            }

            mc.displayGuiScreen(this);
        } else if (editingServer) {
            editingServer = false;

            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                ServerData serverdata = ((ServerListEntryNormal) guilistextended$iguilistentry).getServerData();
                serverdata.serverName = selectedServer.serverName;
                serverdata.serverIP = selectedServer.serverIP;
                serverdata.copyFrom(selectedServer);
                savedServerList.saveServerList();
                serverListSelector.func_148195_a(savedServerList);
            }

            mc.displayGuiScreen(this);
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        int i = serverListSelector.func_148193_k();
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = i < 0 ? null : serverListSelector.getListEntry(i);

        if (keyCode == 63) {
            refreshServerList();
        } else {
            if (i >= 0) {
                if (keyCode == 200) {
                    if (isShiftKeyDown()) {
                        if (i > 0 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                            savedServerList.swapServers(i, i - 1);
                            selectServer(serverListSelector.func_148193_k() - 1);
                            serverListSelector.scrollBy(-serverListSelector.getSlotHeight());
                            serverListSelector.func_148195_a(savedServerList);
                        }
                    } else if (i > 0) {
                        selectServer(serverListSelector.func_148193_k() - 1);
                        serverListSelector.scrollBy(-serverListSelector.getSlotHeight());

                        if (serverListSelector.getListEntry(serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
                            if (serverListSelector.func_148193_k() > 0) {
                                selectServer(serverListSelector.getSize() - 1);
                                serverListSelector.scrollBy(-serverListSelector.getSlotHeight());
                            } else {
                                selectServer(-1);
                            }
                        }
                    } else {
                        selectServer(-1);
                    }
                } else if (keyCode == 208) {
                    if (isShiftKeyDown()) {
                        if (i < savedServerList.countServers() - 1) {
                            savedServerList.swapServers(i, i + 1);
                            selectServer(i + 1);
                            serverListSelector.scrollBy(serverListSelector.getSlotHeight());
                            serverListSelector.func_148195_a(savedServerList);
                        }
                    } else if (i < serverListSelector.getSize()) {
                        selectServer(serverListSelector.func_148193_k() + 1);
                        serverListSelector.scrollBy(serverListSelector.getSlotHeight());

                        if (serverListSelector.getListEntry(serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan) {
                            if (serverListSelector.func_148193_k() < serverListSelector.getSize() - 1) {
                                selectServer(serverListSelector.getSize() + 1);
                                serverListSelector.scrollBy(serverListSelector.getSlotHeight());
                            } else {
                                selectServer(-1);
                            }
                        }
                    } else {
                        selectServer(-1);
                    }
                } else if (keyCode != 28 && keyCode != 156) {
                    super.keyTyped(typedChar, keyCode);
                } else {
                    actionPerformed((GuiButton) buttonList.get(2));
                }
            } else {
                super.keyTyped(typedChar, keyCode);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();

        shader.useShader(width, height, mouseX, mouseY, (System.currentTimeMillis() - Client.main().getInitTime()) / 1000F);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        GL20.glUseProgram(0);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        ScaledResolution sr = new ScaledResolution(mc);

        RenderUtils.drawRect(0, sr.height() - 60, sr.width(), sr.height(), new Color(0, 0, 0, 120).getRGB());

        hoveringText = null;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissor(0, 30, sr.width(), sr.height() - 60);
        serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        drawCenteredString(fontRendererObj, I18n.format("multiplayer.title", new Object[0]), width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (hoveringText != null) {
            drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(hoveringText)), mouseX, mouseY);
        }
    }

    public void connectToSelected() {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = serverListSelector.func_148193_k() < 0 ? null : serverListSelector.getListEntry(serverListSelector.func_148193_k());

        if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
            connectToServer(((ServerListEntryNormal) guilistextended$iguilistentry).getServerData());
        } else if (guilistextended$iguilistentry instanceof ServerListEntryLanDetected) {
            LanServerDetector.LanServer lanserverdetector$lanserver = ((ServerListEntryLanDetected) guilistextended$iguilistentry).getLanServer();
            connectToServer(new ServerData(lanserverdetector$lanserver.getServerMotd(), lanserverdetector$lanserver.getServerIpPort(), true));
        }
    }

    private void connectToServer(ServerData server) {
        mc.displayGuiScreen(new GuiConnecting(this, mc, server));
    }

    public void selectServer(int index) {
        serverListSelector.setSelectedSlotIndex(index);
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null : serverListSelector.getListEntry(index);
        btnSelectServer.enabled = false;
        btnEditServer.enabled = false;
        btnDeleteServer.enabled = false;

        if (guilistextended$iguilistentry != null && !(guilistextended$iguilistentry instanceof ServerListEntryLanScan)) {
            btnSelectServer.enabled = true;

            if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                btnEditServer.enabled = true;
                btnDeleteServer.enabled = true;
            }
        }
    }

    public OldServerPinger getOldServerPinger() {
        return oldServerPinger;
    }

    public void setHoveringText(String p_146793_1_) {
        hoveringText = p_146793_1_;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        serverListSelector.mouseReleased(mouseX, mouseY, state);
    }

    public ServerList getServerList() {
        return savedServerList;
    }

    public boolean func_175392_a(ServerListEntryNormal p_175392_1_, int p_175392_2_) {
        return p_175392_2_ > 0;
    }

    public boolean func_175394_b(ServerListEntryNormal p_175394_1_, int p_175394_2_) {
        return p_175394_2_ < savedServerList.countServers() - 1;
    }

    public void func_175391_a(ServerListEntryNormal p_175391_1_, int p_175391_2_, boolean p_175391_3_) {
        int i = p_175391_3_ ? 0 : p_175391_2_ - 1;
        savedServerList.swapServers(p_175391_2_, i);

        if (serverListSelector.func_148193_k() == p_175391_2_) {
            selectServer(i);
        }

        serverListSelector.func_148195_a(savedServerList);
    }

    public void func_175393_b(ServerListEntryNormal p_175393_1_, int p_175393_2_, boolean p_175393_3_) {
        int i = p_175393_3_ ? savedServerList.countServers() - 1 : p_175393_2_ + 1;
        savedServerList.swapServers(p_175393_2_, i);

        if (serverListSelector.func_148193_k() == p_175393_2_) {
            selectServer(i);
        }

        serverListSelector.func_148195_a(savedServerList);
    }
}
