package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.LongRunningTask;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class ResetWorldScreen extends ScreenWithCallback {
   private static final Logger LOGGER = LogManager.getLogger();
   private RealmsScreen lastScreen;
   private RealmsScreen onlineScreen;
   private RealmsScreen worldManagementScreen;
   private RealmsServer serverData;
   private RealmsEditBox seedEdit;
   private Boolean generateStructures = true;
   private Integer levelTypeIndex = 0;
   String[] levelTypes;
   private final int RESET_BUTTON_ID = 1;
   private final int CANCEL_BUTTON = 2;
   private static final int WORLD_TEMPLATE_BUTTON = 3;
   private static final int LEVEL_TYPE_BUTTON_ID = 4;
   private static final int GENERATE_STRUCTURES_BUTTON_ID = 5;
   private final int SEED_EDIT_BOX = 6;
   private WorldTemplate selectedWorldTemplate;
   private RealmsButton resetButton;
   private RealmsButton levelTypeButton;
   private RealmsButton generateStructuresButton;

   public ResetWorldScreen(RealmsScreen var1, RealmsScreen var2, RealmsScreen var3, RealmsServer var4) {
      this.lastScreen = var1;
      this.onlineScreen = var2;
      this.worldManagementScreen = var3;
      this.serverData = var4;
   }

   public void tick() {
      this.seedEdit.tick();
   }

   public void init() {
      this.levelTypes = new String[]{getLocalizedString("generator.default"), getLocalizedString("generator.flat"), getLocalizedString("generator.largeBiomes"), getLocalizedString("generator.amplified")};
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.buttonsAdd(this.resetButton = newButton(1, this.width() / 2 - 100, this.height() / 4 + 120 + 12, 97, 20, getLocalizedString("mco.configure.world.buttons.reset")));
      this.buttonsAdd(newButton(2, this.width() / 2 + 5, this.height() / 4 + 120 + 12, 97, 20, getLocalizedString("gui.cancel")));
      this.seedEdit = this.newEditBox(6, this.width() / 2 - 100, 99, 200, 20);
      this.seedEdit.setFocus(true);
      this.seedEdit.setMaxLength(32);
      this.seedEdit.setValue("");
      this.buttonsAdd(this.levelTypeButton = newButton(4, this.width() / 2 - 102, 145, 205, 20, this.levelTypeTitle()));
      this.buttonsAdd(this.generateStructuresButton = newButton(5, this.width() / 2 - 102, 165, 205, 20, this.generateStructuresTitle()));
      if (this.selectedWorldTemplate == null) {
         this.buttonsAdd(newButton(3, this.width() / 2 - 102, 125, 205, 20, getLocalizedString("mco.template.default.name")));
      } else {
         this.seedEdit.setValue("");
         this.seedEdit.setIsEditable(false);
         this.seedEdit.setFocus(false);
         this.buttonsAdd(newButton(3, this.width() / 2 - 102, 125, 205, 20, getLocalizedString("mco.template.name") + ": " + this.selectedWorldTemplate.name));
      }

   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void keyPressed(char var1, int var2) {
      this.seedEdit.keyPressed(var1, var2);
      if (var2 == 28 || var2 == 156) {
         this.buttonClicked(this.resetButton);
      }

      if (var2 == 1) {
         Realms.setScreen(this.worldManagementScreen);
      }

   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 2) {
            Realms.setScreen(this.worldManagementScreen);
         } else if (var1.id() == 1) {
            String var2 = getLocalizedString("mco.configure.world.reset.question.line1");
            String var3 = getLocalizedString("mco.configure.world.reset.question.line2");
            Realms.setScreen(new LongConfirmationScreen(this, LongConfirmationScreen.Type.Warning, var2, var3, 1));
         } else if (var1.id() == 3) {
            Realms.setScreen(new RealmsWorldTemplateScreen(this, this.selectedWorldTemplate, false));
         } else if (var1.id() == 4) {
            this.levelTypeIndex = (this.levelTypeIndex + 1) % this.levelTypes.length;
            var1.msg(this.levelTypeTitle());
         } else if (var1.id() == 5) {
            this.generateStructures = !this.generateStructures;
            var1.msg(this.generateStructuresTitle());
         }

      }
   }

   public void confirmResult(boolean var1, int var2) {
      if (var1 && var2 == 1) {
         this.resetWorld();
      } else {
         Realms.setScreen(this);
      }

   }

   private void resetWorld() {
      ResetWorldScreen.ResettingWorldTask var1 = new ResetWorldScreen.ResettingWorldTask(this, this.serverData.id, this.seedEdit.getValue(), this.selectedWorldTemplate, this.levelTypeIndex, this.generateStructures);
      LongRunningMcoTaskScreen var2 = new LongRunningMcoTaskScreen(this.lastScreen, var1);
      var2.start();
      Realms.setScreen(var2);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.seedEdit.mouseClicked(var1, var2, var3);
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(getLocalizedString("mco.reset.world.title"), this.width() / 2, 17, 16777215);
      this.drawCenteredString(getLocalizedString("mco.reset.world.warning"), this.width() / 2, 56, 16711680);
      this.drawString(getLocalizedString("mco.reset.world.seed"), this.width() / 2 - 100, 86, 10526880);
      this.seedEdit.render();
      super.render(var1, var2, var3);
   }

   void callback(WorldTemplate var1) {
      this.selectedWorldTemplate = var1;
   }

   private String levelTypeTitle() {
      String var1 = getLocalizedString("selectWorld.mapType");
      return var1 + " " + this.levelTypes[this.levelTypeIndex];
   }

   private String generateStructuresTitle() {
      return getLocalizedString("selectWorld.mapFeatures") + " " + (this.generateStructures ? getLocalizedString("mco.configure.world.on") : getLocalizedString("mco.configure.world.off"));
   }

   void callback(Object var1) {
      this.callback((WorldTemplate)var1);
   }

   static RealmsScreen access$000(ResetWorldScreen var0) {
      return var0.onlineScreen;
   }

   static Logger access$100() {
      return LOGGER;
   }

   private class ResettingWorldTask extends LongRunningTask {
      private final long worldId;
      private final String seed;
      private final WorldTemplate worldTemplate;
      private final int levelType;
      private final boolean generateStructures;
      final ResetWorldScreen this$0;

      public ResettingWorldTask(ResetWorldScreen var1, long var2, String var4, WorldTemplate var5, int var6, boolean var7) {
         this.this$0 = var1;
         this.worldId = var2;
         this.seed = var4;
         this.worldTemplate = var5;
         this.levelType = var6;
         this.generateStructures = var7;
      }

      public void run() {
         RealmsClient var1 = RealmsClient.createRealmsClient();
         String var2 = RealmsScreen.getLocalizedString("mco.reset.world.resetting.screen.title");
         this.setTitle(var2);
         int var3 = 0;

         while(var3 < 6) {
            try {
               if (this.aborted()) {
                  return;
               }

               if (this.worldTemplate != null) {
                  var1.resetWorldWithTemplate(this.worldId, this.worldTemplate.id);
                  Realms.setScreen(new RealmsConfigureWorldScreen(ResetWorldScreen.access$000(this.this$0), this.worldId));
                  return;
               }

               var1.resetWorldWithSeed(this.worldId, this.seed, this.levelType, this.generateStructures);
               if (this.aborted()) {
                  return;
               }

               Realms.setScreen(new RealmsConfigureWorldScreen(ResetWorldScreen.access$000(this.this$0), this.worldId));
               return;
            } catch (RetryCallException var5) {
               if (this.aborted()) {
                  return;
               }

               this.pause(var5.delaySeconds);
               ++var3;
            } catch (RealmsServiceException var6) {
               if (this.aborted()) {
                  return;
               }

               ResetWorldScreen.access$100().error("Couldn't reset world");
               this.error(var6.toString());
               return;
            } catch (Exception var7) {
               if (this.aborted()) {
                  return;
               }

               ResetWorldScreen.access$100().error("Couldn't reset world");
               this.error(var7.toString());
               return;
            }
         }

      }

      private void pause(int var1) {
         try {
            Thread.sleep((long)(var1 * 1000));
         } catch (InterruptedException var3) {
            ResetWorldScreen.access$100().error((Object)var3);
         }

      }
   }
}
