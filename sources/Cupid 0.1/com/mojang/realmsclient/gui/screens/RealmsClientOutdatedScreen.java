package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsConstants;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class RealmsClientOutdatedScreen extends RealmsScreen {
  private static final int BUTTON_BACK_ID = 0;
  
  private final RealmsScreen lastScreen;
  
  private final boolean outdated;
  
  public RealmsClientOutdatedScreen(RealmsScreen lastScreen, boolean outdated) {
    this.lastScreen = lastScreen;
    this.outdated = outdated;
  }
  
  public void init() {
    buttonsClear();
    buttonsAdd(newButton(0, width() / 2 - 100, RealmsConstants.row(12), "Back"));
  }
  
  public void render(int xm, int ym, float a) {
    renderBackground();
    String title = this.outdated ? getLocalizedString("mco.client.outdated.title") : getLocalizedString("mco.client.incompatible.title");
    String msg = this.outdated ? getLocalizedString("mco.client.outdated.msg") : getLocalizedString("mco.client.incompatible.msg");
    drawCenteredString(title, width() / 2, RealmsConstants.row(3), 16711680);
    drawCenteredString(msg, width() / 2, RealmsConstants.row(5), 16777215);
    super.render(xm, ym, a);
  }
  
  public void buttonClicked(RealmsButton button) {
    if (button.id() == 0)
      Realms.setScreen(this.lastScreen); 
  }
  
  public void keyPressed(char eventCharacter, int eventKey) {
    if (eventKey == 28 || eventKey == 156 || eventKey == 1)
      Realms.setScreen(this.lastScreen); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\gui\screens\RealmsClientOutdatedScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */