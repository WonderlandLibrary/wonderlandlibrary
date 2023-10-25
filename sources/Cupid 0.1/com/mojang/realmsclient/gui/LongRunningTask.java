package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import net.minecraft.realms.RealmsButton;

public abstract class LongRunningTask implements Runnable, ErrorCallback, GuiCallback {
  protected RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen;
  
  public void setScreen(RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen) {
    this.longRunningMcoTaskScreen = longRunningMcoTaskScreen;
  }
  
  public void error(String errorMessage) {
    this.longRunningMcoTaskScreen.error(errorMessage);
  }
  
  public void setTitle(String title) {
    this.longRunningMcoTaskScreen.setTitle(title);
  }
  
  public boolean aborted() {
    return this.longRunningMcoTaskScreen.aborted();
  }
  
  public void tick() {}
  
  public void buttonClicked(RealmsButton button) {}
  
  public void init() {}
  
  public void abortTask() {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\gui\LongRunningTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */