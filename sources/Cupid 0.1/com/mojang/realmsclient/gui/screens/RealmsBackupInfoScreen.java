package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.Backup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsSimpleScrolledSelectionList;
import net.minecraft.realms.Tezzelator;
import org.lwjgl.input.Keyboard;

public class RealmsBackupInfoScreen extends RealmsScreen {
  private final RealmsScreen lastScreen;
  
  private final int BUTTON_BACK_ID = 0;
  
  private final Backup backup;
  
  private List<String> keys = new ArrayList<String>();
  
  private BackupInfoList backupInfoList;
  
  String[] difficulties = new String[] { getLocalizedString("options.difficulty.peaceful"), getLocalizedString("options.difficulty.easy"), getLocalizedString("options.difficulty.normal"), getLocalizedString("options.difficulty.hard") };
  
  String[] gameModes = new String[] { getLocalizedString("selectWorld.gameMode.survival"), getLocalizedString("selectWorld.gameMode.creative"), getLocalizedString("selectWorld.gameMode.adventure") };
  
  public RealmsBackupInfoScreen(RealmsScreen lastScreen, Backup backup) {
    this.lastScreen = lastScreen;
    this.backup = backup;
    if (backup.changeList != null)
      for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)backup.changeList.entrySet())
        this.keys.add(entry.getKey());  
  }
  
  public void mouseEvent() {
    super.mouseEvent();
    this.backupInfoList.mouseEvent();
  }
  
  public void tick() {}
  
  public void init() {
    Keyboard.enableRepeatEvents(true);
    buttonsAdd(newButton(0, width() / 2 - 100, height() / 4 + 120 + 24, getLocalizedString("gui.back")));
    this.backupInfoList = new BackupInfoList();
  }
  
  public void removed() {
    Keyboard.enableRepeatEvents(false);
  }
  
  public void buttonClicked(RealmsButton button) {
    if (!button.active())
      return; 
    if (button.id() == 0)
      Realms.setScreen(this.lastScreen); 
  }
  
  public void keyPressed(char ch, int eventKey) {
    if (eventKey == 1)
      Realms.setScreen(this.lastScreen); 
  }
  
  public void render(int xm, int ym, float a) {
    renderBackground();
    drawCenteredString("Changes from last backup", width() / 2, 10, 16777215);
    this.backupInfoList.render(xm, ym, a);
    super.render(xm, ym, a);
  }
  
  private String checkForSpecificMetadata(String key, String value) {
    String k = key.toLowerCase();
    if (k.contains("game") && k.contains("mode"))
      return gameModeMetadata(value); 
    if (k.contains("game") && k.contains("difficulty"))
      return gameDifficultyMetadata(value); 
    return value;
  }
  
  private String gameDifficultyMetadata(String value) {
    try {
      return this.difficulties[Integer.parseInt(value)];
    } catch (Exception e) {
      return "UNKNOWN";
    } 
  }
  
  private String gameModeMetadata(String value) {
    try {
      return this.gameModes[Integer.parseInt(value)];
    } catch (Exception e) {
      return "UNKNOWN";
    } 
  }
  
  private class BackupInfoList extends RealmsSimpleScrolledSelectionList {
    public BackupInfoList() {
      super(RealmsBackupInfoScreen.this.width(), RealmsBackupInfoScreen.this.height(), 32, RealmsBackupInfoScreen.this.height() - 64, 36);
    }
    
    public int getItemCount() {
      return RealmsBackupInfoScreen.this.backup.changeList.size();
    }
    
    public void selectItem(int item, boolean doubleClick, int xMouse, int yMouse) {}
    
    public boolean isSelectedItem(int item) {
      return false;
    }
    
    public int getMaxPosition() {
      return getItemCount() * 36;
    }
    
    public void renderBackground() {}
    
    protected void renderItem(int i, int x, int y, int h, Tezzelator t, int mouseX, int mouseY) {
      String key = RealmsBackupInfoScreen.this.keys.get(i);
      RealmsBackupInfoScreen.this.drawString(key, width() / 2 - 40, y, 10526880);
      String metadataValue = (String)RealmsBackupInfoScreen.this.backup.changeList.get(key);
      RealmsBackupInfoScreen.this.drawString(RealmsBackupInfoScreen.this.checkForSpecificMetadata(key, metadataValue), width() / 2 - 40, y + 12, 16777215);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\gui\screens\RealmsBackupInfoScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */