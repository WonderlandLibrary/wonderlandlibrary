package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.LongRunningTask;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class CreateRealmsWorldScreen extends ScreenWithCallback {
   private static final Logger LOGGER = LogManager.getLogger();
   private final long worldId;
   private RealmsScreen lastScreen;
   private RealmsEditBox nameBox;
   private String name;
   private static int CREATE_BUTTON = 0;
   private static int CANCEL_BUTTON = 1;
   private static int WORLD_TEMPLATE_BUTTON = 2;
   private static int NAME_BOX_ID = 3;
   private boolean error;
   private String errorMessage = "You must enter a name!";
   private WorldTemplate selectedWorldTemplate;
   private RealmsButton templateButton;

   public CreateRealmsWorldScreen(long var1, RealmsScreen var3) {
      this.worldId = var1;
      this.lastScreen = var3;
   }

   public void tick() {
      this.nameBox.tick();
      this.name = this.nameBox.getValue();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.buttonsAdd(newButton(CREATE_BUTTON, this.width() / 2 - 100, this.height() / 4 + 120 + 17, 97, 20, getLocalizedString("mco.create.world")));
      this.buttonsAdd(newButton(CANCEL_BUTTON, this.width() / 2 + 5, this.height() / 4 + 120 + 17, 95, 20, getLocalizedString("gui.cancel")));
      this.nameBox = this.newEditBox(NAME_BOX_ID, this.width() / 2 - 100, 65, 200, 20);
      this.nameBox.setFocus(true);
      if (this.name != null) {
         this.nameBox.setValue(this.name);
      }

      if (this.selectedWorldTemplate == null) {
         this.buttonsAdd(this.templateButton = newButton(WORLD_TEMPLATE_BUTTON, this.width() / 2 - 100, 107, 200, 20, getLocalizedString("mco.template.default.name")));
      } else {
         this.buttonsAdd(this.templateButton = newButton(WORLD_TEMPLATE_BUTTON, this.width() / 2 - 100, 107, 200, 20, getLocalizedString("mco.template.name") + ": " + this.selectedWorldTemplate.name));
      }

   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == CANCEL_BUTTON) {
            Realms.setScreen(this.lastScreen);
         } else if (var1.id() == CREATE_BUTTON) {
            this.createWorld();
         } else if (var1.id() == WORLD_TEMPLATE_BUTTON) {
            Realms.setScreen(new RealmsWorldTemplateScreen(this, this.selectedWorldTemplate, false));
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      this.nameBox.keyPressed(var1, var2);
      if (var2 == 15) {
         this.nameBox.setFocus(!this.nameBox.isFocused());
      }

      if (var2 == 28 || var2 == 156) {
         this.buttonClicked(this.templateButton);
      }

      if (var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   private void createWorld() {
      if (this != null) {
         CreateRealmsWorldScreen.WorldCreationTask var1 = new CreateRealmsWorldScreen.WorldCreationTask(this, this.worldId, this.nameBox.getValue(), this.selectedWorldTemplate);
         LongRunningMcoTaskScreen var2 = new LongRunningMcoTaskScreen(this.lastScreen, var1);
         var2.start();
         Realms.setScreen(var2);
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.nameBox.mouseClicked(var1, var2, var3);
   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(getLocalizedString("mco.selectServer.create"), this.width() / 2, 11, 16777215);
      this.drawString(getLocalizedString("mco.configure.world.name"), this.width() / 2 - 100, 52, 10526880);
      if (this.error) {
         this.drawCenteredString(this.errorMessage, this.width() / 2, 167, 16711680);
      }

      this.nameBox.render();
      super.render(var1, var2, var3);
   }

   public void callback(WorldTemplate var1) {
      this.selectedWorldTemplate = var1;
   }

   public void callback(Object var1) {
      this.callback((WorldTemplate)var1);
   }

   static RealmsScreen access$000(CreateRealmsWorldScreen var0) {
      return var0.lastScreen;
   }

   static Logger access$100() {
      return LOGGER;
   }

   class WorldCreationTask extends LongRunningTask {
      private final String name;
      private final WorldTemplate selectedWorldTemplate;
      private final long worldId;
      final CreateRealmsWorldScreen this$0;

      public WorldCreationTask(CreateRealmsWorldScreen var1, long var2, String var4, WorldTemplate var5) {
         this.this$0 = var1;
         this.worldId = var2;
         this.name = var4;
         this.selectedWorldTemplate = var5;
      }

      public void run() {
         String var1 = RealmsScreen.getLocalizedString("mco.create.world.wait");
         this.setTitle(var1);
         RealmsClient var2 = RealmsClient.createRealmsClient();

         try {
            if (this.selectedWorldTemplate != null) {
               var2.initializeWorld(this.worldId, this.name, this.selectedWorldTemplate.id);
            } else {
               var2.initializeWorld(this.worldId, this.name, "-1");
            }

            Realms.setScreen(CreateRealmsWorldScreen.access$000(this.this$0));
         } catch (RealmsServiceException var4) {
            CreateRealmsWorldScreen.access$100().error("Couldn't create world");
            this.error(var4.toString());
         } catch (UnsupportedEncodingException var5) {
            CreateRealmsWorldScreen.access$100().error("Couldn't create world");
            this.error(var5.getLocalizedMessage());
         } catch (IOException var6) {
            CreateRealmsWorldScreen.access$100().error("Could not parse response creating world");
            this.error(var6.getLocalizedMessage());
         } catch (Exception var7) {
            CreateRealmsWorldScreen.access$100().error("Could not create world");
            this.error(var7.getLocalizedMessage());
         }

      }
   }
}
