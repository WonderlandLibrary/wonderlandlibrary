package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.gui.screens.LongRunningMcoTaskScreen;
import net.minecraft.realms.RealmsButton;

public abstract class LongRunningTask implements Runnable, ErrorCallback, GuiCallback {
   protected LongRunningMcoTaskScreen longRunningMcoTaskScreen;

   public void setScreen(LongRunningMcoTaskScreen var1) {
      this.longRunningMcoTaskScreen = var1;
   }

   public void error(String var1) {
      this.longRunningMcoTaskScreen.error(var1);
   }

   public void setTitle(String var1) {
      this.longRunningMcoTaskScreen.setTitle(var1);
   }

   public boolean aborted() {
      return this.longRunningMcoTaskScreen.aborted();
   }

   public void tick() {
   }

   public void buttonClicked(RealmsButton var1) {
   }

   public void init() {
   }

   public void abortTask() {
   }
}
