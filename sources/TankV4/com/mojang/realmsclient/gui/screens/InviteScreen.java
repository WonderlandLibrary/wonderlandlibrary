package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class InviteScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private RealmsEditBox profileName;
   private RealmsServer serverData;
   private final RealmsScreen onlineScreen;
   private final ConfigureWorldScreen configureScreen;
   private final int BUTTON_INVITE = 0;
   private final int BUTTON_CANCEL = 1;
   private final int PROFILENAME_EDIT_BOX = 2;
   private String defaultErrorMsg = "Could not invite the provided name";
   private String errorMsg;
   private boolean showError;
   private RealmsButton inviteButton;

   public InviteScreen(RealmsScreen var1, ConfigureWorldScreen var2, RealmsServer var3) {
      this.onlineScreen = var1;
      this.configureScreen = var2;
      this.serverData = var3;
   }

   public void tick() {
      this.profileName.tick();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.buttonsAdd(this.inviteButton = newButton(0, this.width() / 2 - 100, this.height() / 4 + 96 + 12, getLocalizedString("mco.configure.world.buttons.invite")));
      this.buttonsAdd(newButton(1, this.width() / 2 - 100, this.height() / 4 + 120 + 12, getLocalizedString("gui.cancel")));
      this.profileName = this.newEditBox(2, this.width() / 2 - 100, 66, 200, 20);
      this.profileName.setFocus(true);
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 1) {
            Realms.setScreen(this.configureScreen);
         } else if (var1.id() == 0) {
            RealmsClient var2 = RealmsClient.createRealmsClient();
            if (this.profileName.getValue() == null || this.profileName.getValue().isEmpty()) {
               return;
            }

            try {
               RealmsServer var3 = var2.invite(this.serverData.id, this.profileName.getValue());
               if (var3 != null) {
                  this.serverData.players = var3.players;
                  Realms.setScreen(new ConfigureWorldScreen(this.onlineScreen, this.serverData.id));
               } else {
                  this.showError(this.defaultErrorMsg);
               }
            } catch (Exception var4) {
               LOGGER.error("Couldn't invite user");
               this.showError(this.defaultErrorMsg);
            }
         }

      }
   }

   private void showError(String var1) {
      this.showError = true;
      this.errorMsg = var1;
   }

   public void keyPressed(char var1, int var2) {
      this.profileName.keyPressed(var1, var2);
      if (var2 == 15) {
         if (this.profileName.isFocused()) {
            this.profileName.setFocus(false);
         } else {
            this.profileName.setFocus(true);
         }
      }

      if (var2 == 28 || var2 == 156) {
         this.buttonClicked(this.inviteButton);
      }

      if (var2 == 1) {
         Realms.setScreen(this.configureScreen);
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.profileName.mouseClicked(var1, var2, var3);
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawString(getLocalizedString("mco.configure.world.invite.profile.name"), this.width() / 2 - 100, 53, 10526880);
      if (this.showError) {
         this.drawCenteredString(this.errorMsg, this.width() / 2, 100, 16711680);
      }

      this.profileName.render();
      super.render(var1, var2, var3);
   }
}
