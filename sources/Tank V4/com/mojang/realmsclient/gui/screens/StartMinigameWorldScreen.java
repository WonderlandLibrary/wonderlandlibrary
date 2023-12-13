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

public class StartMinigameWorldScreen extends ScreenWithCallback {
   private static final Logger LOGGER = LogManager.getLogger();
   private RealmsScreen lastScreen;
   private RealmsServer serverData;
   private final int START_BUTTON_ID = 1;
   private final int CANCEL_BUTTON = 2;
   private static int WORLD_TEMPLATE_BUTTON = 3;
   private WorldTemplate selectedWorldTemplate;
   private RealmsButton startButton;

   public StartMinigameWorldScreen(RealmsScreen var1, RealmsServer var2) {
      this.lastScreen = var1;
      this.serverData = var2;
   }

   public void tick() {
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.buttonsAdd(this.startButton = newButton(1, this.width() / 2 - 100, this.height() / 4 + 120 + 12, 97, 20, getLocalizedString("mco.minigame.world.startButton")));
      this.buttonsAdd(newButton(2, this.width() / 2 + 5, this.height() / 4 + 120 + 12, 97, 20, getLocalizedString("gui.cancel")));
      this.startButton.active(this.selectedWorldTemplate != null);
      if (this.selectedWorldTemplate == null) {
         this.buttonsAdd(newButton(WORLD_TEMPLATE_BUTTON, this.width() / 2 - 100, 102, 200, 20, getLocalizedString("mco.minigame.world.noSelection")));
      } else {
         this.buttonsAdd(newButton(WORLD_TEMPLATE_BUTTON, this.width() / 2 - 100, 102, 200, 20, this.selectedWorldTemplate.name));
      }

   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 28 || var2 == 156) {
         this.buttonClicked(this.startButton);
      }

      if (var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 2) {
            Realms.setScreen(this.lastScreen);
         } else if (var1.id() == 1) {
            this.startMinigame();
         } else if (var1.id() == WORLD_TEMPLATE_BUTTON) {
            Realms.setScreen(new RealmsWorldTemplateScreen(this, this.selectedWorldTemplate, true));
         }

      }
   }

   private void startMinigame() {
      StartMinigameWorldScreen.StartMinigameTask var1 = new StartMinigameWorldScreen.StartMinigameTask(this, this.serverData.id, this.selectedWorldTemplate);
      LongRunningMcoTaskScreen var2 = new LongRunningMcoTaskScreen(this.lastScreen, var1);
      var2.start();
      Realms.setScreen(var2);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(getLocalizedString("mco.minigame.world.title"), this.width() / 2, 17, 16777215);
      this.drawCenteredString(getLocalizedString("mco.minigame.world.info1"), this.width() / 2, 56, 16777215);
      this.drawCenteredString(getLocalizedString("mco.minigame.world.info2"), this.width() / 2, 68, 16777215);
      this.drawString(getLocalizedString("mco.minigame.world.selected"), this.width() / 2 - 100, 90, 10526880);
      super.render(var1, var2, var3);
   }

   void callback(WorldTemplate var1) {
      this.selectedWorldTemplate = var1;
   }

   void callback(Object var1) {
      this.callback((WorldTemplate)var1);
   }

   static RealmsServer access$000(StartMinigameWorldScreen var0) {
      return var0.serverData;
   }

   static RealmsScreen access$100(StartMinigameWorldScreen var0) {
      return var0.lastScreen;
   }

   static Logger access$200() {
      return LOGGER;
   }

   private class StartMinigameTask extends LongRunningTask {
      private final long worldId;
      private final WorldTemplate worldTemplate;
      final StartMinigameWorldScreen this$0;

      public StartMinigameTask(StartMinigameWorldScreen var1, long var2, WorldTemplate var4) {
         this.this$0 = var1;
         this.worldId = var2;
         this.worldTemplate = var4;
      }

      public void run() {
         RealmsClient var1 = RealmsClient.createRealmsClient();
         String var2 = RealmsScreen.getLocalizedString("mco.minigame.world.starting.screen.title");
         this.setTitle(var2);

         try {
            if (this.aborted()) {
               return;
            }

            Boolean var3 = var1.putIntoMinigameMode(this.worldId, this.worldTemplate.id);
            Thread.sleep(2000L);
            if (var3) {
               RealmsServer var4 = StartMinigameWorldScreen.access$000(this.this$0);
               var4.worldType = RealmsServer.WorldType.MINIGAME;
               var4.motd = this.worldTemplate.id;
            }

            if (this.aborted()) {
               return;
            }

            Realms.setScreen(StartMinigameWorldScreen.access$100(this.this$0));
         } catch (RealmsServiceException var5) {
            if (this.aborted()) {
               return;
            }

            StartMinigameWorldScreen.access$200().error("Couldn't start mini game!");
            this.error(var5.toString());
         } catch (Exception var6) {
            if (this.aborted()) {
               return;
            }

            StartMinigameWorldScreen.access$200().error("Couldn't start mini game!");
            this.error(var6.toString());
         }

      }
   }
}
