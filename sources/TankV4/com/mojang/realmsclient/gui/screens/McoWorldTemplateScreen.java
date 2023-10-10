package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.Collections;
import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class McoWorldTemplateScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private final ScreenWithCallback lastScreen;
   private WorldTemplate selectedWorldTemplate;
   private List templates = Collections.emptyList();
   private McoWorldTemplateScreen.WorldTemplateSelectionList worldTemplateSelectionList;
   private int selectedTemplate = -1;
   private static final int BACK_BUTTON_ID = 0;
   private static final int SELECT_BUTTON_ID = 1;
   private RealmsButton selectButton;
   private boolean isMiniGame;

   public McoWorldTemplateScreen(ScreenWithCallback var1, WorldTemplate var2, boolean var3) {
      this.lastScreen = var1;
      this.selectedWorldTemplate = var2;
      this.isMiniGame = var3;
   }

   public void mouseEvent() {
      super.mouseEvent();
      this.worldTemplateSelectionList.mouseEvent();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.worldTemplateSelectionList = new McoWorldTemplateScreen.WorldTemplateSelectionList(this);
      boolean var1 = this.isMiniGame;
      (new Thread(this, "Realms-minigame-fetcher", var1) {
         final boolean val$isMiniGame;
         final McoWorldTemplateScreen this$0;

         {
            this.this$0 = var1;
            this.val$isMiniGame = var3;
         }

         public void run() {
            RealmsClient var1 = RealmsClient.createRealmsClient();

            try {
               if (this.val$isMiniGame) {
                  McoWorldTemplateScreen.access$002(this.this$0, var1.fetchMinigames().templates);
               } else {
                  McoWorldTemplateScreen.access$002(this.this$0, var1.fetchWorldTemplates().templates);
               }
            } catch (RealmsServiceException var3) {
               McoWorldTemplateScreen.access$100().error("Couldn't fetch templates");
            }

         }
      }).start();
      this.postInit();
   }

   private void postInit() {
      this.buttonsAdd(newButton(0, this.width() / 2 + 6, this.height() - 52, 153, 20, getLocalizedString("gui.cancel")));
      this.buttonsAdd(this.selectButton = newButton(1, this.width() / 2 - 154, this.height() - 52, 153, 20, getLocalizedString("mco.template.button.select")));
   }

   public void tick() {
      super.tick();
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 1) {
            this.selectTemplate();
         } else if (var1.id() == 0) {
            this.backButtonClicked();
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         this.backButtonClicked();
      }

   }

   private void backButtonClicked() {
      this.lastScreen.callback((Object)null);
      Realms.setScreen(this.lastScreen);
   }

   private void selectTemplate() {
      if (this.selectedTemplate >= 0 && this.selectedTemplate < this.templates.size()) {
         this.lastScreen.callback(this.templates.get(this.selectedTemplate));
         Realms.setScreen(this.lastScreen);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.worldTemplateSelectionList.render(var1, var2, var3);
      String var4 = "";
      if (this.isMiniGame) {
         var4 = getLocalizedString("mco.template.title.minigame");
      } else {
         var4 = getLocalizedString("mco.template.title");
      }

      this.drawCenteredString(var4, this.width() / 2, 20, 16777215);
      super.render(var1, var2, var3);
   }

   static List access$002(McoWorldTemplateScreen var0, List var1) {
      return var0.templates = var1;
   }

   static Logger access$100() {
      return LOGGER;
   }

   static List access$000(McoWorldTemplateScreen var0) {
      return var0.templates;
   }

   static int access$202(McoWorldTemplateScreen var0, int var1) {
      return var0.selectedTemplate = var1;
   }

   static WorldTemplate access$302(McoWorldTemplateScreen var0, WorldTemplate var1) {
      return var0.selectedWorldTemplate = var1;
   }

   static WorldTemplate access$300(McoWorldTemplateScreen var0) {
      return var0.selectedWorldTemplate;
   }

   static int access$200(McoWorldTemplateScreen var0) {
      return var0.selectedTemplate;
   }

   private class WorldTemplateSelectionList extends RealmsScrolledSelectionList {
      final McoWorldTemplateScreen this$0;

      public WorldTemplateSelectionList(McoWorldTemplateScreen var1) {
         super(var1.width(), var1.height(), 32, var1.height() - 64, 36);
         this.this$0 = var1;
      }

      public int getItemCount() {
         return McoWorldTemplateScreen.access$000(this.this$0).size() + 1;
      }

      public void selectItem(int var1, boolean var2, int var3, int var4) {
         if (var1 < McoWorldTemplateScreen.access$000(this.this$0).size()) {
            McoWorldTemplateScreen.access$202(this.this$0, var1);
            McoWorldTemplateScreen.access$302(this.this$0, (WorldTemplate)null);
         }
      }

      public boolean isSelectedItem(int var1) {
         if (McoWorldTemplateScreen.access$000(this.this$0).size() == 0) {
            return false;
         } else if (var1 >= McoWorldTemplateScreen.access$000(this.this$0).size()) {
            return false;
         } else if (McoWorldTemplateScreen.access$300(this.this$0) != null) {
            boolean var2 = McoWorldTemplateScreen.access$300(this.this$0).name.equals(((WorldTemplate)McoWorldTemplateScreen.access$000(this.this$0).get(var1)).name);
            if (var2) {
               McoWorldTemplateScreen.access$202(this.this$0, var1);
            }

            return var2;
         } else {
            return var1 == McoWorldTemplateScreen.access$200(this.this$0);
         }
      }

      public int getMaxPosition() {
         return this.getItemCount() * 36;
      }

      public void renderBackground() {
         this.this$0.renderBackground();
      }

      public void renderItem(int var1, int var2, int var3, int var4, int var5, int var6) {
         if (var1 < McoWorldTemplateScreen.access$000(this.this$0).size()) {
            this.renderWorldTemplateItem(var1, var2, var3, var4);
         }

      }

      private void renderWorldTemplateItem(int var1, int var2, int var3, int var4) {
         WorldTemplate var5 = (WorldTemplate)McoWorldTemplateScreen.access$000(this.this$0).get(var1);
         this.this$0.drawString(var5.name, var2 + 2, var3 + 1, 16777215);
         this.this$0.drawString(var5.author, var2 + 2, var3 + 12, 7105644);
         this.this$0.drawString(var5.version, var2 + 2 + 207 - this.this$0.fontWidth(var5.version), var3 + 1, 5000268);
      }
   }
}
