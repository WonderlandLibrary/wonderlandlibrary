package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.util.RealmsTasks;
import com.mojang.realmsclient.util.RealmsTextureManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsResetWorldScreen extends RealmsScreenWithCallback<WorldTemplate> {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private RealmsScreen lastScreen;
  
  private RealmsServer serverData;
  
  private RealmsScreen returnScreen;
  
  private String title = getLocalizedString("mco.reset.world.title");
  
  private String subtitle = getLocalizedString("mco.reset.world.warning");
  
  private String buttonTitle = getLocalizedString("gui.cancel");
  
  private int subtitleColor = 16711680;
  
  private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
  
  private static final String UPLOAD_LOCATION = "realms:textures/gui/realms/upload.png";
  
  private final int BUTTON_CANCEL_ID = 0;
  
  private boolean loaded = false;
  
  private List<WorldTemplate> templates = new ArrayList<WorldTemplate>();
  
  private List<WorldTemplate> adventuremaps = new ArrayList<WorldTemplate>();
  
  private final Random random = new Random();
  
  private ResetType selectedType = ResetType.NONE;
  
  private int templateId;
  
  private int adventureMapId;
  
  public int slot = -1;
  
  private ResetType typeToReset = ResetType.NONE;
  
  private ResetWorldInfo worldInfoToReset = null;
  
  private WorldTemplate worldTemplateToReset = null;
  
  private String resetTitle = null;
  
  public RealmsResetWorldScreen(RealmsScreen lastScreen, RealmsServer serverData, RealmsScreen returnScreen) {
    this.lastScreen = lastScreen;
    this.serverData = serverData;
    this.returnScreen = returnScreen;
  }
  
  public RealmsResetWorldScreen(RealmsScreen lastScreen, RealmsServer serverData, RealmsScreen returnScreen, String title, String subtitle, int subtitleColor, String buttonTitle) {
    this(lastScreen, serverData, returnScreen);
    this.title = title;
    this.subtitle = subtitle;
    this.subtitleColor = subtitleColor;
    this.buttonTitle = buttonTitle;
  }
  
  public void setSlot(int slot) {
    this.slot = slot;
  }
  
  public void setResetTitle(String title) {
    this.resetTitle = title;
  }
  
  public void init() {
    buttonsClear();
    buttonsAdd(newButton(0, width() / 2 - 40, RealmsConstants.row(14) - 10, 80, 20, this.buttonTitle));
    if (!this.loaded)
      (new Thread("Realms-reset-world-fetcher") {
          public void run() {
            RealmsClient client = RealmsClient.createRealmsClient();
            try {
              for (WorldTemplate wt : (client.fetchWorldTemplates()).templates) {
                if (!wt.recommendedPlayers.equals("")) {
                  RealmsResetWorldScreen.this.adventuremaps.add(wt);
                  continue;
                } 
                RealmsResetWorldScreen.this.templates.add(wt);
              } 
              RealmsResetWorldScreen.this.templateId = RealmsResetWorldScreen.this.random.nextInt(RealmsResetWorldScreen.this.templates.size());
              RealmsResetWorldScreen.this.adventureMapId = RealmsResetWorldScreen.this.random.nextInt(RealmsResetWorldScreen.this.adventuremaps.size());
              RealmsResetWorldScreen.this.loaded = true;
            } catch (RealmsServiceException e) {
              RealmsResetWorldScreen.LOGGER.error("Couldn't fetch templates in reset world");
            } 
          }
        }).start(); 
  }
  
  public void removed() {
    Keyboard.enableRepeatEvents(false);
  }
  
  public void keyPressed(char ch, int eventKey) {
    if (eventKey == 1)
      Realms.setScreen(this.lastScreen); 
  }
  
  public void buttonClicked(RealmsButton button) {
    if (!button.active())
      return; 
    if (button.id() == 0)
      Realms.setScreen(this.lastScreen); 
  }
  
  public void mouseClicked(int x, int y, int buttonNum) {
    RealmsSelectWorldTemplateScreen screen;
    RealmsSelectWorldTemplateScreen templateScreen;
    switch (this.selectedType) {
      case NONE:
        return;
      case GENERATE:
        Realms.setScreen(new RealmsResetNormalWorldScreen(this));
      case UPLOAD:
        Realms.setScreen(new RealmsSelectFileToUploadScreen(this.serverData.id, (this.slot != -1) ? this.slot : this.serverData.activeSlot, this));
      case ADVENTURE:
        screen = new RealmsSelectWorldTemplateScreen(this, null, false, false, this.adventuremaps);
        screen.setTitle(getLocalizedString("mco.reset.world.adventure"));
        Realms.setScreen(screen);
      case SURVIVAL_SPAWN:
        templateScreen = new RealmsSelectWorldTemplateScreen(this, null, false, false, this.templates);
        templateScreen.setTitle(getLocalizedString("mco.reset.world.template"));
        Realms.setScreen(templateScreen);
    } 
  }
  
  private int frame(int i) {
    return width() / 2 - 80 + (i - 1) * 100;
  }
  
  public void render(int xm, int ym, float a) {
    this.selectedType = ResetType.NONE;
    renderBackground();
    drawCenteredString(this.title, width() / 2, 7, 16777215);
    drawCenteredString(this.subtitle, width() / 2, 22, this.subtitleColor);
    if (this.loaded) {
      drawFrame(frame(1), RealmsConstants.row(0) + 10, xm, ym, getLocalizedString("mco.reset.world.generate"), -1L, "textures/gui/title/background/panorama_3.png", ResetType.GENERATE);
      drawFrame(frame(2), RealmsConstants.row(0) + 10, xm, ym, getLocalizedString("mco.reset.world.upload"), -1L, "realms:textures/gui/realms/upload.png", ResetType.UPLOAD);
      drawFrame(frame(1), RealmsConstants.row(6) + 20, xm, ym, getLocalizedString("mco.reset.world.adventure"), Long.valueOf(((WorldTemplate)this.adventuremaps.get(this.adventureMapId)).id).longValue(), ((WorldTemplate)this.adventuremaps.get(this.adventureMapId)).image, ResetType.ADVENTURE);
      drawFrame(frame(2), RealmsConstants.row(6) + 20, xm, ym, getLocalizedString("mco.reset.world.template"), Long.valueOf(((WorldTemplate)this.templates.get(this.templateId)).id).longValue(), ((WorldTemplate)this.templates.get(this.templateId)).image, ResetType.SURVIVAL_SPAWN);
    } 
    super.render(xm, ym, a);
  }
  
  private void drawFrame(int x, int y, int xm, int ym, String text, long imageId, String image, ResetType resetType) {
    boolean hovered = false;
    if (xm >= x && xm <= x + 60 && ym >= y - 12 && ym <= y + 60) {
      hovered = true;
      this.selectedType = resetType;
    } 
    if (imageId != -1L) {
      RealmsTextureManager.bindWorldTemplate(String.valueOf(imageId), image);
    } else {
      bind(image);
    } 
    if (hovered) {
      GL11.glColor4f(0.56F, 0.56F, 0.56F, 1.0F);
    } else {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    } 
    RealmsScreen.blit(x + 2, y + 2, 0.0F, 0.0F, 56, 56, 56.0F, 56.0F);
    bind("realms:textures/gui/realms/slot_frame.png");
    if (hovered) {
      GL11.glColor4f(0.56F, 0.56F, 0.56F, 1.0F);
    } else {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    } 
    RealmsScreen.blit(x, y, 0.0F, 0.0F, 60, 60, 60.0F, 60.0F);
    drawCenteredString(text, x + 30, y - 12, hovered ? 10526880 : 16777215);
  }
  
  void callback(WorldTemplate worldTemplate) {
    if (worldTemplate != null)
      if (this.slot != -1) {
        this.typeToReset = worldTemplate.recommendedPlayers.equals("") ? ResetType.SURVIVAL_SPAWN : ResetType.ADVENTURE;
        this.worldTemplateToReset = worldTemplate;
        switchSlot();
      } else {
        resetWorldWithTemplate(worldTemplate);
      }  
  }
  
  private void switchSlot() {
    switchSlot(this);
  }
  
  public void switchSlot(RealmsScreen screen) {
    RealmsTasks.SwitchSlotTask switchSlotTask = new RealmsTasks.SwitchSlotTask(this.serverData.id, this.slot, screen, 100);
    RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, (LongRunningTask)switchSlotTask);
    longRunningMcoTaskScreen.start();
    Realms.setScreen(longRunningMcoTaskScreen);
  }
  
  enum ResetType {
    NONE, GENERATE, UPLOAD, ADVENTURE, SURVIVAL_SPAWN;
  }
  
  public void confirmResult(boolean result, int id) {
    if (id == 100 && result) {
      switch (this.typeToReset) {
        case ADVENTURE:
        case SURVIVAL_SPAWN:
          if (this.worldTemplateToReset != null)
            resetWorldWithTemplate(this.worldTemplateToReset); 
          return;
        case GENERATE:
          if (this.worldInfoToReset != null)
            triggerResetWorld(this.worldInfoToReset); 
          return;
      } 
      return;
    } 
    if (result)
      Realms.setScreen(this.returnScreen); 
  }
  
  public void resetWorldWithTemplate(WorldTemplate template) {
    RealmsTasks.ResettingWorldTask resettingWorldTask = new RealmsTasks.ResettingWorldTask(this.serverData.id, this.returnScreen, template);
    if (this.resetTitle != null)
      resettingWorldTask.setResetTitle(this.resetTitle); 
    RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, (LongRunningTask)resettingWorldTask);
    longRunningMcoTaskScreen.start();
    Realms.setScreen(longRunningMcoTaskScreen);
  }
  
  public static class ResetWorldInfo {
    String seed;
    
    int levelType;
    
    boolean generateStructures;
    
    public ResetWorldInfo(String seed, int levelType, boolean generateStructures) {
      this.seed = seed;
      this.levelType = levelType;
      this.generateStructures = generateStructures;
    }
  }
  
  public void resetWorld(ResetWorldInfo resetWorldInfo) {
    if (this.slot != -1) {
      this.typeToReset = ResetType.GENERATE;
      this.worldInfoToReset = resetWorldInfo;
      switchSlot();
    } else {
      triggerResetWorld(resetWorldInfo);
    } 
  }
  
  private void triggerResetWorld(ResetWorldInfo resetWorldInfo) {
    RealmsTasks.ResettingWorldTask resettingWorldTask = new RealmsTasks.ResettingWorldTask(this.serverData.id, this.returnScreen, resetWorldInfo.seed, resetWorldInfo.levelType, resetWorldInfo.generateStructures);
    if (this.resetTitle != null)
      resettingWorldTask.setResetTitle(this.resetTitle); 
    RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, (LongRunningTask)resettingWorldTask);
    longRunningMcoTaskScreen.start();
    Realms.setScreen(longRunningMcoTaskScreen);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\gui\screens\RealmsResetWorldScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */