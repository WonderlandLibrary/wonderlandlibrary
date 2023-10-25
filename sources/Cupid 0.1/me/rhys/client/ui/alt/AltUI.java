package me.rhys.client.ui.alt;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.rhys.base.Lite;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.button.Button;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.ui.element.panel.ScrollPanel;
import me.rhys.base.ui.popup.Popup;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.files.AccountsFile;
import me.rhys.client.ui.alt.element.AccountItem;
import me.rhys.client.ui.alt.element.AltServerButton;
import me.rhys.client.ui.alt.popup.AccountManagementPopup;
import me.rhys.client.ui.alt.popup.DirectLoginPopup;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;

public class AltUI extends UIScreen {
  public static OldServerPinger getServerPinger() {
    return serverPinger;
  }
  
  private static final OldServerPinger serverPinger = new OldServerPinger();
  
  public final Map<String, String> queue = new HashMap<>();
  
  private static final int BACKGROUND_COLOR = (new Color(27, 34, 44, 255)).getRGB();
  
  private static final int SHADOW_COLOR = ColorUtil.lighten(BACKGROUND_COLOR, 15).getRGB();
  
  private static final int TEXT_SHADOW_COLOR = ColorUtil.rgba(190, 190, 190, 1.0F);
  
  private final int ALT_PANEL_WIDTH = 135;
  
  private final int SCREEN_MARGIN = 5;
  
  private final int PANEL_BORDER_SIZE = 1;
  
  private int currentServer = -1;
  
  private Panel displayPanel;
  
  private Button actionButton;
  
  private Button backBtn;
  
  private Button importBtn;
  
  private Button directLoginBtn;
  
  private Button refreshBtn;
  
  public ScrollPanel altPanel;
  
  private ScrollPanel serversPanel;
  
  private ServerList savedServerList;
  
  private String hoveringText;
  
  public void setHoveringText(String hoveringText) {
    this.hoveringText = hoveringText;
  }
  
  public void initGui() {
    super.initGui();
    this.currentServer = -1;
    reloadServers();
    if (!this.queue.isEmpty()) {
      this.queue.forEach(this::addAccount);
      this.queue.clear();
    } 
  }
  
  public void typeKey(char keyChar, int keyCode) {
    if (keyCode == 63)
      reloadServers(); 
  }
  
  public void updateScreen() {
    serverPinger.pingPendingNetworks();
  }
  
  protected void init() {
    ScaledResolution resolution = getResolution();
    this.displayPanel = (Panel)new ScrollPanel(new Vec2f(5.0F, 5.0F), 135, (int)(FontUtil.getFontHeight() * 3.0F + 5.0F));
    this.displayPanel.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
    this.altPanel = new ScrollPanel(this.displayPanel.offset.clone().add(0, this.displayPanel.getHeight() - 5), this.displayPanel.getWidth(), resolution.getScaledHeight() - this.displayPanel.getHeight() + 6);
    this.altPanel.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
    this.serversPanel = new ScrollPanel(this.displayPanel.offset.clone().add((this.displayPanel.getWidth() + 5 + 1), FontUtil.getFontHeight() + 5.0F + 13.0F), resolution.getScaledWidth() - this.displayPanel.getWidth() + 10 + 1 - 6, this.altPanel.getHeight() - 25);
    this.serversPanel.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
    add((Element)this.displayPanel);
    add((Element)this.altPanel);
    add((Element)this.serversPanel);
    this.actionButton = new Button("☰", this.displayPanel.offset.clone().add((this.displayPanel.getWidth() - (int)(FontUtil.getFontHeight() * 2.0F + 5.0F)), (this.displayPanel.getHeight() - (int)(FontUtil.getFontHeight() * 3.0F + 5.0F)) / 2.0F), (int)(FontUtil.getFontHeight() * 2.0F + 5.0F), (int)(FontUtil.getFontHeight() * 2.0F + 5.0F));
    this.actionButton.background = ColorUtil.darken(BACKGROUND_COLOR, 20).getRGB();
    add((Element)this.actionButton);
    this.backBtn = new Button("Back", new Vec2f((resolution.getScaledWidth() - 106), (this.displayPanel.getHeight() - 20) / 2.0F), 100, 20);
    this.backBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
    add((Element)this.backBtn);
    this.refreshBtn = new Button("Refresh", this.backBtn.offset.clone().sub((5 + this.backBtn.getWidth()), 0.0F), 100, 20);
    this.refreshBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
    add((Element)this.refreshBtn);
    this.importBtn = new Button("Import", this.serversPanel.offset.clone().add(0, this.serversPanel.getHeight() + 5), (this.serversPanel.getWidth() - 5) / 2, 20);
    this.importBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
    add((Element)this.importBtn);
    this.directLoginBtn = new Button("Direct Login", this.importBtn.offset.clone().add(this.importBtn.getWidth() + 5, 0), this.importBtn.getWidth(), 20);
    this.directLoginBtn.background = ColorUtil.darken(BACKGROUND_COLOR, 10).getRGB();
    add((Element)this.directLoginBtn);
  }
  
  public void clickMouse(Vec2f pos, int button) {
    if (button == 0) {
      if (this.actionButton.isHovered(pos)) {
        displayPopup((Popup)new AccountManagementPopup(100, 100));
        this.actionButton.playSound();
      } else if (this.importBtn.isHovered(pos)) {
        this.importBtn.playSound();
      } else if (this.directLoginBtn.isHovered(pos)) {
        displayPopup((Popup)new DirectLoginPopup(200, 115));
        this.directLoginBtn.playSound();
      } else if (this.refreshBtn.isHovered(pos)) {
        reloadServers();
        this.directLoginBtn.playSound();
      } else if (this.backBtn.isHovered(pos)) {
        this.mc.displayGuiScreen(null);
        if (this.mc.currentScreen == null)
          this.mc.setIngameFocus(); 
        this.backBtn.playSound();
      } 
      if (pos.x >= this.serversPanel.pos.x && pos.y >= this.serversPanel.pos.y && pos.y <= this.serversPanel.pos.y + this.serversPanel.getHeight() && pos.x <= this.serversPanel.pos.x + this.serversPanel.getWidth()) {
        AltServerButton altServerButton = (AltServerButton)this.serversPanel.getContainer().find(element -> element.isHovered(pos.clone().add(0.0F, this.serversPanel.getScrollAmount())));
        if (altServerButton != null) {
          int index = this.serversPanel.getContainer().indexOf(altServerButton);
          if (this.currentServer != -1 && index == this.currentServer)
            this.mc.displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)this, this.mc, altServerButton.getServerData())); 
          this.currentServer = index;
        } 
      } 
    } 
  }
  
  protected void preDraw(Vec2f mouse, float partialTicks) {
    this.hoveringText = null;
    List<Element> items = this.altPanel.getContainer().getItems();
    for (int i = 0; i < items.size(); i++) {
      Element item = items.get(i);
      item.background = (i % 2 == 0) ? ColorUtil.darken(this.altPanel.background, 15).getRGB() : this.altPanel.background;
    } 
    ScaledResolution resolution = getResolution();
    this.displayPanel.setHeight((int)(FontUtil.getFontHeight() * 2.0F + 5.0F));
    this.altPanel.setHeight(resolution.getScaledHeight() - this.displayPanel.getHeight() + 10 + 2 + 5 - 1);
    this.serversPanel.setWidth(resolution.getScaledWidth() - this.displayPanel.getWidth() + 10 + 1 - 6);
    this.serversPanel.setHeight(this.altPanel.getHeight() - 25);
    this.importBtn.setWidth((this.serversPanel.getWidth() - 5) / 2);
    this.directLoginBtn.setWidth(this.importBtn.getWidth());
    this.serversPanel.getContainer().forEach(element -> element.setWidth(this.serversPanel.getWidth()));
    this.backBtn.offset = new Vec2f((resolution.getScaledWidth() - 106), (this.displayPanel.getHeight() - 10) / 2.0F);
    this.refreshBtn.offset = this.backBtn.offset.clone().sub((5 + this.backBtn.getWidth()), 0.0F);
    this.importBtn.offset = this.serversPanel.offset.clone().add(0, this.serversPanel.getHeight() + 5);
    this.directLoginBtn.offset = this.importBtn.offset.clone().add(this.importBtn.getWidth() + 5, 0);
    RenderUtil.drawRect(new Vec2f(), this.width, this.height, BACKGROUND_COLOR);
    drawBorder((Element)this.displayPanel);
    drawBorder((Element)this.altPanel);
    drawBorder((Element)this.serversPanel);
    drawBorder((Element)this.backBtn);
    drawBorder((Element)this.refreshBtn);
    drawBorder((Element)this.importBtn);
    drawBorder((Element)this.directLoginBtn);
    FontUtil.drawString("Servers", this.serversPanel.pos.clone().sub(0.0F, FontUtil.getFontHeight() * 2.2F), -1);
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    GlStateManager.pushMatrix();
    float scale = 0.6F;
    GlStateManager.scale(scale, scale, scale);
    FontUtil.drawString("Account", this.displayPanel.pos.clone().add(5, 3).div(scale, scale), TEXT_SHADOW_COLOR);
    GlStateManager.scale(1.0F / scale, 1.0F / scale, 1.0F / scale);
    GlStateManager.popMatrix();
    Session session = this.mc.session;
    if (session != null && session
      .getUsername() != null && 
      !session.getUsername().isEmpty()) {
      FontUtil.drawString(session.getUsername(), this.displayPanel.pos.clone().add(5.0F, (this.displayPanel.getHeight() - FontUtil.getFontHeight()) / 2.0F + 3.5F), -1);
    } else {
      FontUtil.drawString("UNKNOWN_USER", this.displayPanel.pos.clone().add(5.0F, (this.displayPanel.getHeight() - FontUtil.getFontHeight()) / 2.0F + 3.5F), -1);
    } 
    RenderUtil.drawRect(this.actionButton.pos.clone().sub(1.0F, 0.0F), 1, this.actionButton.getHeight(), SHADOW_COLOR);
    if (this.serversPanel.getContainer().isEmpty())
      FontUtil.drawCenteredString("No Servers found...", this.serversPanel.pos.clone().add(this.serversPanel.getWidth() / 2.0F, this.serversPanel.getHeight() / 2.0F), -1); 
    FontUtil.drawCenteredString("No Alts found...", this.altPanel.pos.clone().add(this.altPanel.getWidth() / 2.0F, this.altPanel.getHeight() / 2.0F), -1);
    if (this.hoveringText != null)
      drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), (int)mouse.x + 5, (int)mouse.y); 
    if (this.currentServer != -1) {
      GlStateManager.pushMatrix();
      GL11.glEnable(3089);
      GL11.glLineWidth(2.0F);
      RenderUtil.clipRect(this.serversPanel.pos, this.serversPanel.pos.clone().add(this.serversPanel.getWidth(), this.serversPanel.getHeight()), getScale());
      AltServerButton altServerButton = (AltServerButton)this.serversPanel.getContainer().get(this.currentServer);
      Vec2f pos = altServerButton.pos.clone().sub(0.0F, this.serversPanel.getScrollAmount());
      RenderUtil.drawOutlineRect(pos, pos.clone().add(altServerButton.getWidth(), altServerButton.getHeight()), -1);
      GL11.glLineWidth(1.0F);
      GL11.glDisable(3089);
      GlStateManager.popMatrix();
    } 
  }
  
  public void addAccount(String email, String password) {
    this.altPanel.add((Element)new AccountItem(email, password, this.altPanel.getWidth(), 20));
  }
  
  private void drawBorder(Element element) {
    RenderUtil.drawRect(element.pos.clone().sub(1.0F, 1.0F), element.getWidth() + 2, element.getHeight() + 2, SHADOW_COLOR);
  }
  
  private void reloadServers() {
    this.serversPanel.getContainer().getItems().clear();
    this.savedServerList = new ServerList(this.mc);
    this.savedServerList.loadServerList();
    int length = this.savedServerList.countServers();
    for (int i = 0; i < length; i++) {
      ServerData serverData = this.savedServerList.getServerData(i);
      if (serverData != null) {
        AltServerButton altServerButton = new AltServerButton(serverData, new Vec2f(), this.serversPanel.getWidth(), 32);
        altServerButton.background = (i % 2 == 0) ? ColorUtil.darken(this.serversPanel.background, 15).getRGB() : this.serversPanel.background;
        this.serversPanel.add((Element)altServerButton);
      } 
    } 
  }
  
  public void onGuiClosed() {
    serverPinger.clearPendingNetworks();
    Lite.FILE_FACTORY.saveFile(AccountsFile.class);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\alt\AltUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */