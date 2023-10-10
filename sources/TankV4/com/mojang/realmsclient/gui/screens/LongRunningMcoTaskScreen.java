package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.ErrorCallback;
import com.mojang.realmsclient.gui.LongRunningTask;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class LongRunningMcoTaskScreen extends RealmsScreen implements ErrorCallback {
   private final int BUTTON_CANCEL_ID = 666;
   private final int BUTTON_BACK_ID = 667;
   private final RealmsScreen lastScreen;
   private final LongRunningTask taskThread;
   private volatile String title = "";
   private volatile boolean error;
   private volatile String errorMessage;
   private volatile boolean aborted;
   private int animTicks;
   private LongRunningTask task;
   private int buttonLength = 212;
   public static final String[] symbols = new String[]{"▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄", "_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ _ _ ▃ ▄ ▅ ▆ ▇ █", "_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄", "▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _", "▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _", "▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "█ ▇ ▆ ▅ ▄ ▃ _ _ _ _ _", "▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _", "▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _"};

   public LongRunningMcoTaskScreen(RealmsScreen var1, LongRunningTask var2) {
      this.lastScreen = var1;
      this.task = var2;
      var2.setScreen(this);
      this.taskThread = var2;
   }

   public void start() {
      (new Thread(this.taskThread, "Realms-long-running-task")).start();
   }

   public void tick() {
      super.tick();
      ++this.animTicks;
      this.task.tick();
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         this.cancelOrBackButtonClicked();
      }

   }

   public void init() {
      this.task.init();
      this.buttonsAdd(newButton(666, this.width() / 2 - this.buttonLength / 2, this.height() / 2 + 50, this.buttonLength, 20, getLocalizedString("gui.cancel")));
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.id() == 666 || var1.id() == 667) {
         this.cancelOrBackButtonClicked();
      }

      this.task.buttonClicked(var1);
   }

   private void cancelOrBackButtonClicked() {
      this.aborted = true;
      this.task.abortTask();
      Realms.setScreen(this.lastScreen);
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(this.title, this.width() / 2, this.height() / 2 - 50, 16777215);
      this.drawCenteredString("", this.width() / 2, this.height() / 2 - 10, 16777215);
      if (!this.error) {
         this.drawCenteredString(symbols[this.animTicks % symbols.length], this.width() / 2, this.height() / 2 + 15, 8421504);
      }

      if (this.error) {
         this.drawCenteredString(this.errorMessage, this.width() / 2, this.height() / 2 + 15, 16711680);
      }

      super.render(var1, var2, var3);
   }

   public void error(String var1) {
      this.error = true;
      this.errorMessage = var1;
      this.buttonsClear();
      this.buttonsAdd(newButton(667, this.width() / 2 - this.buttonLength / 2, this.height() / 4 + 120 + 12, getLocalizedString("gui.back")));
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public boolean aborted() {
      return this.aborted;
   }
}
