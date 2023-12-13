package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class GuiControls extends GuiScreen {
   private GuiButton buttonReset;
   protected String screenTitle = "Controls";
   private static final GameSettings.Options[] optionsArr;
   public long time;
   private GameSettings options;
   private GuiScreen parentScreen;
   public KeyBinding buttonId = null;
   private GuiKeyBindingList keyBindingList;

   public void initGui() {
      this.keyBindingList = new GuiKeyBindingList(this, this.mc);
      this.buttonList.add(new GuiButton(200, width / 2 - 155, height - 29, 150, 20, I18n.format("gui.done")));
      this.buttonList.add(this.buttonReset = new GuiButton(201, width / 2 - 155 + 160, height - 29, 150, 20, I18n.format("controls.resetAll")));
      this.screenTitle = I18n.format("controls.title");
      int var1 = 0;
      GameSettings.Options[] var5;
      int var4 = (var5 = optionsArr).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         GameSettings.Options var2 = var5[var3];
         if (var2.getEnumFloat()) {
            this.buttonList.add(new GuiOptionSlider(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, 18 + 24 * (var1 >> 1), var2));
         } else {
            this.buttonList.add(new GuiOptionButton(var2.returnEnumOrdinal(), width / 2 - 155 + var1 % 2 * 160, 18 + 24 * (var1 >> 1), var2, this.options.getKeyBinding(var2)));
         }

         ++var1;
      }

   }

   static {
      optionsArr = new GameSettings.Options[]{GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN};
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.id == 200) {
         this.mc.displayGuiScreen(this.parentScreen);
      } else if (var1.id == 201) {
         KeyBinding[] var5;
         int var4 = (var5 = this.mc.gameSettings.keyBindings).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            KeyBinding var2 = var5[var3];
            var2.setKeyCode(var2.getKeyCodeDefault());
         }

         KeyBinding.resetKeyBindingArrayAndHash();
      } else if (var1.id < 100 && var1 instanceof GuiOptionButton) {
         this.options.setOptionValue(((GuiOptionButton)var1).returnEnumOptions(), 1);
         var1.displayString = this.options.getKeyBinding(GameSettings.Options.getEnumOptions(var1.id));
      }

   }

   protected void mouseReleased(int var1, int var2, int var3) {
      if (var3 != 0 || !this.keyBindingList.mouseReleased(var1, var2, var3)) {
         super.mouseReleased(var1, var2, var3);
      }

   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.keyBindingList.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, this.screenTitle, width / 2, 8, 16777215);
      boolean var4 = true;
      KeyBinding[] var8;
      int var7 = (var8 = this.options.keyBindings).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         KeyBinding var5 = var8[var6];
         if (var5.getKeyCode() != var5.getKeyCodeDefault()) {
            var4 = false;
            break;
         }
      }

      this.buttonReset.enabled = !var4;
      super.drawScreen(var1, var2, var3);
   }

   public GuiControls(GuiScreen var1, GameSettings var2) {
      this.parentScreen = var1;
      this.options = var2;
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.keyBindingList.handleMouseInput();
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (this.buttonId != null) {
         if (var2 == 1) {
            this.options.setOptionKeyBinding(this.buttonId, 0);
         } else if (var2 != 0) {
            this.options.setOptionKeyBinding(this.buttonId, var2);
         } else if (var1 > 0) {
            this.options.setOptionKeyBinding(this.buttonId, var1 + 256);
         }

         this.buttonId = null;
         this.time = Minecraft.getSystemTime();
         KeyBinding.resetKeyBindingArrayAndHash();
      } else {
         super.keyTyped(var1, var2);
      }

   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      if (this.buttonId != null) {
         this.options.setOptionKeyBinding(this.buttonId, -100 + var3);
         this.buttonId = null;
         KeyBinding.resetKeyBindingArrayAndHash();
      } else if (var3 != 0 || !this.keyBindingList.mouseClicked(var1, var2, var3)) {
         super.mouseClicked(var1, var2, var3);
      }

   }
}
