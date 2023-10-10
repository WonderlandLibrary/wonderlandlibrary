package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConfirmResultListener;
import java.io.UnsupportedEncodingException;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class EditRealmsWorldScreen extends RealmsScreen implements RealmsConfirmResultListener {
   private static final Logger LOGGER = LogManager.getLogger();
   private RealmsScreen configureWorldScreen;
   private RealmsScreen onlineScreen;
   private RealmsEditBox descEdit;
   private RealmsEditBox nameEdit;
   private RealmsServer serverData;
   private static final int DONE_BUTTON_ID = 0;
   private static final int CANCEL_BUTTON = 1;
   private static final int MORE_SETTINGS_BUTTON_ID = 3;
   private static final int NAME_EDIT_BOX = 4;
   private static final int DESC_EDIT_BOX = 5;
   private RealmsButton doneButton;
   private RealmsButton moreSettingsButton;

   public EditRealmsWorldScreen(RealmsScreen var1, RealmsScreen var2, RealmsServer var3) {
      this.configureWorldScreen = var1;
      this.onlineScreen = var2;
      this.serverData = var3;
   }

   public void tick() {
      this.nameEdit.tick();
      this.descEdit.tick();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.buttonsAdd(this.moreSettingsButton = newButton(3, this.width() / 2 - 106, this.height() / 4 + 120 - 3, 216, 20, getLocalizedString("mco.configure.world.buttons.moreoptions")));
      this.buttonsAdd(this.doneButton = newButton(0, this.width() / 2 - 106, this.height() / 4 + 120 + 22, 106, 20, getLocalizedString("mco.configure.world.buttons.done")));
      this.buttonsAdd(newButton(1, this.width() / 2 + 4, this.height() / 4 + 120 + 22, 106, 20, getLocalizedString("gui.cancel")));
      this.nameEdit = this.newEditBox(4, this.width() / 2 - 106, 56, 212, 20);
      this.nameEdit.setFocus(true);
      this.nameEdit.setMaxLength(32);
      this.nameEdit.setValue(this.serverData.getName());
      this.descEdit = this.newEditBox(5, this.width() / 2 - 106, 96, 212, 20);
      this.descEdit.setMaxLength(32);
      this.descEdit.setValue(this.serverData.getMotd());
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 1) {
            Realms.setScreen(this.configureWorldScreen);
         } else if (var1.id() == 0) {
            this.update();
         } else if (var1.id() == 3) {
            this.saveServerData();
            Realms.setScreen(new RealmsWorldSettingsSubScreen(this, this.serverData));
         }

      }
   }

   private void saveServerData() {
      this.serverData.setName(this.nameEdit.getValue());
      this.serverData.setMotd(this.descEdit.getValue());
   }

   public void keyPressed(char var1, int var2) {
      this.nameEdit.keyPressed(var1, var2);
      this.descEdit.keyPressed(var1, var2);
      if (var2 == 15) {
         this.nameEdit.setFocus(!this.nameEdit.isFocused());
         this.descEdit.setFocus(!this.descEdit.isFocused());
      }

      if (var2 == 28 || var2 == 156) {
         this.update();
      }

      if (var2 == 1) {
         Realms.setScreen(this.configureWorldScreen);
      }

      this.doneButton.active(this.nameEdit.getValue() != null && !this.nameEdit.getValue().trim().equals(""));
   }

   private void update() {
      RealmsClient var1 = RealmsClient.createRealmsClient();

      try {
         String var2 = this.descEdit.getValue() != null && !this.descEdit.getValue().trim().equals("") ? this.descEdit.getValue() : null;
         var1.update(this.serverData.id, this.nameEdit.getValue(), var2, this.serverData.options);
         this.saveServerData();
         Realms.setScreen(new RealmsConfigureWorldScreen(this.onlineScreen, this.serverData.id));
      } catch (RealmsServiceException var3) {
         LOGGER.error("Couldn't edit world");
         Realms.setScreen(new RealmsGenericErrorScreen(var3, this));
      } catch (UnsupportedEncodingException var4) {
         LOGGER.error("Couldn't edit world");
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.descEdit.mouseClicked(var1, var2, var3);
      this.nameEdit.mouseClicked(var1, var2, var3);
   }

   public void confirmResult(boolean var1, int var2) {
      Realms.setScreen(this);
   }

   public void saveServerData(int var1, int var2, boolean var3, boolean var4, boolean var5, boolean var6, int var7, boolean var8, boolean var9) {
      this.serverData.options.difficulty = var1;
      this.serverData.options.gameMode = var2;
      this.serverData.options.pvp = var3;
      this.serverData.options.spawnNPCs = var4;
      this.serverData.options.spawnAnimals = var5;
      this.serverData.options.spawnMonsters = var6;
      this.serverData.options.spawnProtection = var7;
      this.serverData.options.commandBlocks = var8;
      this.serverData.options.forceGameMode = var9;
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(getLocalizedString("mco.configure.world.edit.title"), this.width() / 2, 17, 16777215);
      String var4 = getLocalizedString("mco.configure.world.name");
      String var5 = getLocalizedString("mco.configure.world.description");
      this.drawString(var4, this.width() / 2 - 106, 43, 10526880);
      this.drawString(var5, this.width() / 2 - 106, 84, 10526880);
      this.nameEdit.render();
      this.descEdit.render();
      super.render(var1, var2, var3);
   }
}
