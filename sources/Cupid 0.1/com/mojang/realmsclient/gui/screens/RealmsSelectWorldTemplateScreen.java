package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.util.RealmsTextureManager;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.Collections;
import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSharedConstants;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class RealmsSelectWorldTemplateScreen extends RealmsScreen {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final String LINK_ICON = "realms:textures/gui/realms/link_icons.png";
  
  private static final String TRAILER_ICON = "realms:textures/gui/realms/trailer_icons.png";
  
  private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
  
  private final RealmsScreenWithCallback<WorldTemplate> lastScreen;
  
  private WorldTemplate selectedWorldTemplate;
  
  private List<WorldTemplate> templates = Collections.emptyList();
  
  private WorldTemplateSelectionList worldTemplateSelectionList;
  
  private int selectedTemplate = -1;
  
  private String title;
  
  private static final int BUTTON_BACK_ID = 0;
  
  private static final int BUTTON_SELECT_ID = 1;
  
  private RealmsButton selectButton;
  
  private String toolTip = null;
  
  private String currentLink = null;
  
  private boolean isMiniGame;
  
  private boolean displayWarning = false;
  
  private int clicks = 0;
  
  public RealmsSelectWorldTemplateScreen(RealmsScreenWithCallback<WorldTemplate> lastScreen, WorldTemplate selectedWorldTemplate, boolean isMiniGame) {
    this.lastScreen = lastScreen;
    this.selectedWorldTemplate = selectedWorldTemplate;
    this.isMiniGame = isMiniGame;
    this.title = isMiniGame ? getLocalizedString("mco.template.title.minigame") : getLocalizedString("mco.template.title");
  }
  
  public RealmsSelectWorldTemplateScreen(RealmsScreenWithCallback<WorldTemplate> lastScreen, WorldTemplate selectedWorldTemplate, boolean isMiniGame, boolean displayWarning) {
    this(lastScreen, selectedWorldTemplate, isMiniGame);
    this.displayWarning = displayWarning;
  }
  
  public RealmsSelectWorldTemplateScreen(RealmsScreenWithCallback<WorldTemplate> lastScreen, WorldTemplate selectedWorldTemplate, boolean isMiniGame, boolean displayWarning, List<WorldTemplate> templates) {
    this(lastScreen, selectedWorldTemplate, isMiniGame, displayWarning);
    this.templates = templates;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public void mouseEvent() {
    super.mouseEvent();
    this.worldTemplateSelectionList.mouseEvent();
  }
  
  public void init() {
    Keyboard.enableRepeatEvents(true);
    buttonsClear();
    this.worldTemplateSelectionList = new WorldTemplateSelectionList();
    if (this.templates.size() == 0) {
      final boolean isMiniGame = this.isMiniGame;
      (new Thread("Realms-minigame-fetcher") {
          public void run() {
            RealmsClient client = RealmsClient.createRealmsClient();
            try {
              if (isMiniGame) {
                RealmsSelectWorldTemplateScreen.this.templates = (client.fetchMinigames()).templates;
              } else {
                RealmsSelectWorldTemplateScreen.this.templates = (client.fetchWorldTemplates()).templates;
              } 
            } catch (RealmsServiceException e) {
              RealmsSelectWorldTemplateScreen.LOGGER.error("Couldn't fetch templates");
            } 
          }
        }).start();
    } 
    buttonsAdd(newButton(0, width() / 2 + 6, height() - 32, 153, 20, this.isMiniGame ? getLocalizedString("gui.cancel") : getLocalizedString("gui.back")));
    buttonsAdd(this.selectButton = newButton(1, width() / 2 - 154, height() - 32, 153, 20, getLocalizedString("mco.template.button.select")));
    this.selectButton.active(false);
  }
  
  public void tick() {
    super.tick();
    this.clicks--;
    if (this.clicks < 0)
      this.clicks = 0; 
  }
  
  public void buttonClicked(RealmsButton button) {
    if (!button.active())
      return; 
    switch (button.id()) {
      case 0:
        backButtonClicked();
        return;
      case 1:
        selectTemplate();
        return;
    } 
  }
  
  public void keyPressed(char eventCharacter, int eventKey) {
    if (eventKey == 1)
      backButtonClicked(); 
  }
  
  private void backButtonClicked() {
    this.lastScreen.callback(null);
    Realms.setScreen(this.lastScreen);
  }
  
  private void selectTemplate() {
    if (this.selectedTemplate >= 0 && this.selectedTemplate < this.templates.size()) {
      WorldTemplate template = this.templates.get(this.selectedTemplate);
      template.setMinigame(this.isMiniGame);
      this.lastScreen.callback(template);
    } 
  }
  
  public void render(int xm, int ym, float a) {
    this.toolTip = null;
    this.currentLink = null;
    renderBackground();
    this.worldTemplateSelectionList.render(xm, ym, a);
    drawCenteredString(this.title, width() / 2, 13, 16777215);
    if (this.displayWarning) {
      drawCenteredString(getLocalizedString("mco.minigame.world.info1"), width() / 2, RealmsConstants.row(-1), 10526880);
      drawCenteredString(getLocalizedString("mco.minigame.world.info2"), width() / 2, RealmsConstants.row(0), 10526880);
    } 
    super.render(xm, ym, a);
    if (this.toolTip != null)
      renderMousehoverTooltip(this.toolTip, xm, ym); 
  }
  
  private class WorldTemplateSelectionList extends RealmsClickableScrolledSelectionList {
    public WorldTemplateSelectionList() {
      super(RealmsSelectWorldTemplateScreen.this.width(), RealmsSelectWorldTemplateScreen.this.height(), RealmsSelectWorldTemplateScreen.this.displayWarning ? RealmsConstants.row(1) : 32, RealmsSelectWorldTemplateScreen.this.height() - 40, 46);
    }
    
    public int getItemCount() {
      return RealmsSelectWorldTemplateScreen.this.templates.size();
    }
    
    public void customMouseEvent(int y0, int y1, int headerHeight, float yo, int itemHeight) {
      if (Mouse.isButtonDown(0) && 
        ym() >= y0 && ym() <= y1) {
        int x0 = width() / 2 - 150;
        int x1 = width();
        int clickSlotPos = ym() - y0 - headerHeight + (int)yo - 4;
        int slot = clickSlotPos / itemHeight;
        if (xm() >= x0 && xm() <= x1 && slot >= 0 && clickSlotPos >= 0 && slot < getItemCount()) {
          itemClicked(clickSlotPos, slot, xm(), ym(), width());
          if (slot >= RealmsSelectWorldTemplateScreen.this.templates.size())
            return; 
          RealmsSelectWorldTemplateScreen.this.selectButton.active(true);
          RealmsSelectWorldTemplateScreen.this.selectedTemplate = slot;
          RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate = null;
          RealmsSelectWorldTemplateScreen.this.clicks += RealmsSharedConstants.TICKS_PER_SECOND / 3 + 1;
          if (RealmsSelectWorldTemplateScreen.this.clicks >= RealmsSharedConstants.TICKS_PER_SECOND / 2)
            RealmsSelectWorldTemplateScreen.this.selectTemplate(); 
        } 
      } 
    }
    
    public boolean isSelectedItem(int item) {
      if (RealmsSelectWorldTemplateScreen.this.templates.size() == 0)
        return false; 
      if (item >= RealmsSelectWorldTemplateScreen.this.templates.size())
        return false; 
      if (RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate != null) {
        boolean same = RealmsSelectWorldTemplateScreen.this.selectedWorldTemplate.name.equals((RealmsSelectWorldTemplateScreen.this.templates.get(item)).name);
        if (same)
          RealmsSelectWorldTemplateScreen.this.selectedTemplate = item; 
        return same;
      } 
      return (item == RealmsSelectWorldTemplateScreen.this.selectedTemplate);
    }
    
    public void itemClicked(int clickSlotPos, int slot, int xm, int ym, int width) {
      if (slot >= RealmsSelectWorldTemplateScreen.this.templates.size())
        return; 
      if (RealmsSelectWorldTemplateScreen.this.currentLink != null)
        RealmsUtil.browseTo(RealmsSelectWorldTemplateScreen.this.currentLink); 
    }
    
    public int getMaxPosition() {
      return getItemCount() * 46;
    }
    
    public void renderBackground() {
      RealmsSelectWorldTemplateScreen.this.renderBackground();
    }
    
    public void renderItem(int i, int x, int y, int h, int mouseX, int mouseY) {
      if (i < RealmsSelectWorldTemplateScreen.this.templates.size())
        renderWorldTemplateItem(i, x, y, h); 
    }
    
    public int getScrollbarPosition() {
      return super.getScrollbarPosition() + 30;
    }
    
    public void renderSelected(int width, int y, int h, Tezzelator t) {
      int x0 = getScrollbarPosition() - 290;
      int x1 = getScrollbarPosition() - 10;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3553);
      t.begin(7, RealmsDefaultVertexFormat.POSITION_TEX_COLOR);
      t.vertex(x0, (y + h + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
      t.vertex(x1, (y + h + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
      t.vertex(x1, (y - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
      t.vertex(x0, (y - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
      t.vertex((x0 + 1), (y + h + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
      t.vertex((x1 - 1), (y + h + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
      t.vertex((x1 - 1), (y - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
      t.vertex((x0 + 1), (y - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
      t.end();
      GL11.glEnable(3553);
    }
    
    private void renderWorldTemplateItem(int i, int x, int y, int h) {
      WorldTemplate worldTemplate = RealmsSelectWorldTemplateScreen.this.templates.get(i);
      int textStart = x + 20;
      RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.name, textStart, y + 2, 16777215);
      RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.author, textStart, y + 15, 7105644);
      RealmsSelectWorldTemplateScreen.this.drawString(worldTemplate.version, textStart + 227 - RealmsSelectWorldTemplateScreen.this.fontWidth(worldTemplate.version), y + 1, 7105644);
      if (!worldTemplate.link.equals("") || !worldTemplate.trailer.equals("") || !worldTemplate.recommendedPlayers.equals(""))
        drawIcons(textStart - 1, y + 25, xm(), ym(), worldTemplate.link, worldTemplate.trailer, worldTemplate.recommendedPlayers); 
      drawImage(x - 25, y + 1, xm(), ym(), worldTemplate);
    }
    
    private void drawImage(int x, int y, int xm, int ym, WorldTemplate worldTemplate) {
      RealmsTextureManager.bindWorldTemplate(worldTemplate.id, worldTemplate.image);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RealmsScreen.blit(x + 1, y + 1, 0.0F, 0.0F, 38, 38, 38.0F, 38.0F);
      RealmsScreen.bind("realms:textures/gui/realms/slot_frame.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RealmsScreen.blit(x, y, 0.0F, 0.0F, 40, 40, 40.0F, 40.0F);
    }
    
    private void drawIcons(int x, int y, int xm, int ym, String link, String trailerLink, String recommendedPlayers) {
      boolean linkHovered = false;
      boolean trailerHovered = false;
      if (!recommendedPlayers.equals(""))
        RealmsSelectWorldTemplateScreen.this.drawString(recommendedPlayers, x, y + 4, 5000268); 
      int offset = recommendedPlayers.equals("") ? 0 : (RealmsSelectWorldTemplateScreen.this.fontWidth(recommendedPlayers) + 2);
      if (xm >= x + offset && xm <= x + offset + 32 && ym >= y && ym <= y + 15 && ym < RealmsSelectWorldTemplateScreen.this.height() - 15 && ym > 32)
        if (xm <= x + 15 + offset && xm > offset) {
          if (!link.equals("")) {
            linkHovered = true;
          } else {
            trailerHovered = true;
          } 
        } else if (!link.equals("")) {
          trailerHovered = true;
        }  
      if (!link.equals("")) {
        RealmsScreen.bind("realms:textures/gui/realms/link_icons.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        RealmsScreen.blit(x + offset, y, linkHovered ? 15.0F : 0.0F, 0.0F, 15, 15, 30.0F, 15.0F);
        GL11.glPopMatrix();
      } 
      if (!trailerLink.equals("")) {
        RealmsScreen.bind("realms:textures/gui/realms/trailer_icons.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        RealmsScreen.blit(x + offset + (link.equals("") ? 0 : 17), y, trailerHovered ? 15.0F : 0.0F, 0.0F, 15, 15, 30.0F, 15.0F);
        GL11.glPopMatrix();
      } 
      if (linkHovered && !link.equals("")) {
        RealmsSelectWorldTemplateScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.template.info.tooltip");
        RealmsSelectWorldTemplateScreen.this.currentLink = link;
      } else if (trailerHovered && !trailerLink.equals("")) {
        RealmsSelectWorldTemplateScreen.this.toolTip = RealmsScreen.getLocalizedString("mco.template.trailer.tooltip");
        RealmsSelectWorldTemplateScreen.this.currentLink = trailerLink;
      } 
    }
  }
  
  protected void renderMousehoverTooltip(String msg, int x, int y) {
    if (msg == null)
      return; 
    int rx = x + 12;
    int ry = y - 12;
    int width = fontWidth(msg);
    fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
    fontDrawShadow(msg, rx, ry, 16777215);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\gui\screens\RealmsSelectWorldTemplateScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */