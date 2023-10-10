package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.LongRunningTask;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class ModifyMinigameWorldScreen extends ScreenWithCallback {
   private static final Logger LOGGER = LogManager.getLogger();
   private RealmsScreen lastScreen;
   private RealmsServer serverData;
   private final int SWITCH_BUTTON_ID = 1;
   private final int END_BUTTON_ID = 2;
   private final int CANCEL_BUTTON = 3;

   public ModifyMinigameWorldScreen(RealmsScreen var1, RealmsServer var2) {
      this.lastScreen = var1;
      this.serverData = var2;
   }

   public void tick() {
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.buttonsAdd(newButton(1, this.width() / 2 - 100, 116, 200, 20, getLocalizedString("mco.minigame.world.changeButton")));
      this.buttonsAdd(newButton(2, this.width() / 2 - 100, 70, 200, 20, getLocalizedString("mco.minigame.world.stopButton")));
      this.buttonsAdd(newButton(3, this.width() / 2 - 45, this.height() / 4 + 120 + 12, 97, 20, getLocalizedString("gui.cancel")));
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 3) {
            Realms.setScreen(this.lastScreen);
         } else if (var1.id() == 1) {
            Realms.setScreen(new StartMinigameWorldScreen(this.lastScreen, this.serverData));
         } else if (var1.id() == 2) {
            String var2 = getLocalizedString("mco.minigame.world.restore.question.line1");
            String var3 = getLocalizedString("mco.minigame.world.restore.question.line2");
            Realms.setScreen(new LongConfirmationScreen(this, LongConfirmationScreen.Type.Info, var2, var3, 2));
         }

      }
   }

   public void confirmResult(boolean var1, int var2) {
      if (var2 == 2) {
         if (var1) {
            this.stopMinigame();
         } else {
            Realms.setScreen(this);
         }
      }

   }

   private void stopMinigame() {
      ModifyMinigameWorldScreen.StopMinigameTask var1 = new ModifyMinigameWorldScreen.StopMinigameTask(this);
      LongRunningMcoTaskScreen var2 = new LongRunningMcoTaskScreen(this.lastScreen, var1);
      var2.start();
      Realms.setScreen(var2);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(getLocalizedString("mco.minigame.world.modify.title"), this.width() / 2, 17, 16777215);
      this.drawCenteredString(getLocalizedString("mco.minigame.world.modify.new"), this.width() / 2, 102, 16777215);
      this.drawCenteredString(getLocalizedString("mco.minigame.world.modify.end"), this.width() / 2, 56, 16777215);
      super.render(var1, var2, var3);
   }

   void callback(WorldTemplate var1) {
   }

   void callback(Object var1) {
      this.callback((WorldTemplate)var1);
   }

   static RealmsServer access$000(ModifyMinigameWorldScreen var0) {
      return var0.serverData;
   }

   static RealmsScreen access$100(ModifyMinigameWorldScreen var0) {
      return var0.lastScreen;
   }

   static Logger access$200() {
      return LOGGER;
   }

   private class StopMinigameTask extends LongRunningTask {
      final ModifyMinigameWorldScreen this$0;

      public StopMinigameTask(ModifyMinigameWorldScreen var1) {
         this.this$0 = var1;
      }

      public void run() {
         RealmsClient var1 = RealmsClient.createRealmsClient();
         String var2 = RealmsScreen.getLocalizedString("mco.minigame.world.restore");
         this.setTitle(var2);

         try {
            boolean var3 = var1.putIntoNormalMode(ModifyMinigameWorldScreen.access$000(this.this$0).id);
            Thread.sleep(2000L);
            if (var3) {
               ModifyMinigameWorldScreen.access$000(this.this$0).worldType = RealmsServer.WorldType.NORMAL;
               ModifyMinigameWorldScreen.access$000(this.this$0).motd = "";
               this.init();
            }

            Realms.setScreen(ModifyMinigameWorldScreen.access$100(this.this$0));
         } catch (RealmsServiceException var4) {
            if (this.aborted()) {
               return;
            }

            ModifyMinigameWorldScreen.access$200().error("Couldn't start mini game!");
            this.error(var4.toString());
         } catch (Exception var5) {
            if (this.aborted()) {
               return;
            }

            ModifyMinigameWorldScreen.access$200().error("Couldn't start mini game!");
            this.error(var5.toString());
         }

      }
   }
}
