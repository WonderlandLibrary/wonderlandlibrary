package net.minecraft.client.gui;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList extends GuiListExtended {
   private final GuiListExtended.IGuiListEntry[] listEntries;
   private final Minecraft mc;
   private final GuiControls field_148191_k;
   private int maxListLabelWidth = 0;

   public GuiListExtended.IGuiListEntry getListEntry(int var1) {
      return this.listEntries[var1];
   }

   static Minecraft access$0(GuiKeyBindingList var0) {
      return var0.mc;
   }

   static GuiControls access$1(GuiKeyBindingList var0) {
      return var0.field_148191_k;
   }

   static int access$2(GuiKeyBindingList var0) {
      return var0.maxListLabelWidth;
   }

   public int getListWidth() {
      return super.getListWidth() + 32;
   }

   protected int getScrollBarX() {
      return super.getScrollBarX() + 15;
   }

   protected int getSize() {
      return this.listEntries.length;
   }

   public GuiKeyBindingList(GuiControls var1, Minecraft var2) {
      super(var2, GuiControls.width, GuiControls.height, 63, GuiControls.height - 32, 20);
      this.field_148191_k = var1;
      this.mc = var2;
      KeyBinding[] var3 = (KeyBinding[])ArrayUtils.clone((Object[])var2.gameSettings.keyBindings);
      this.listEntries = new GuiListExtended.IGuiListEntry[var3.length + KeyBinding.getKeybinds().size()];
      Arrays.sort(var3);
      int var4 = 0;
      String var5 = null;
      KeyBinding[] var9 = var3;
      int var8 = var3.length;

      for(int var7 = 0; var7 < var8; ++var7) {
         KeyBinding var6 = var9[var7];
         String var10 = var6.getKeyCategory();
         if (!var10.equals(var5)) {
            var5 = var10;
            this.listEntries[var4++] = new GuiKeyBindingList.CategoryEntry(this, var10);
         }

         int var11 = Minecraft.fontRendererObj.getStringWidth(I18n.format(var6.getKeyDescription()));
         if (var11 > this.maxListLabelWidth) {
            this.maxListLabelWidth = var11;
         }

         this.listEntries[var4++] = new GuiKeyBindingList.KeyEntry(this, var6, (GuiKeyBindingList.KeyEntry)null);
      }

   }

   public class CategoryEntry implements GuiListExtended.IGuiListEntry {
      private final int labelWidth;
      final GuiKeyBindingList this$0;
      private final String labelText;

      public CategoryEntry(GuiKeyBindingList var1, String var2) {
         this.this$0 = var1;
         this.labelText = I18n.format(var2);
         this.labelWidth = Minecraft.fontRendererObj.getStringWidth(this.labelText);
      }

      public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
         return false;
      }

      public void setSelected(int var1, int var2, int var3) {
      }

      public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
         FontRenderer var10000 = Minecraft.fontRendererObj;
         String var10001 = this.labelText;
         GuiScreen var10002 = GuiKeyBindingList.access$0(this.this$0).currentScreen;
         var10000.drawString(var10001, (double)(GuiScreen.width / 2 - this.labelWidth / 2), (double)(var3 + var5 - Minecraft.fontRendererObj.FONT_HEIGHT - 1), 16777215);
      }

      public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
      }
   }

   public class KeyEntry implements GuiListExtended.IGuiListEntry {
      private final GuiButton btnChangeKeyBinding;
      private final KeyBinding keybinding;
      private final GuiButton btnReset;
      final GuiKeyBindingList this$0;
      private final String keyDesc;

      private KeyEntry(GuiKeyBindingList var1, KeyBinding var2) {
         this.this$0 = var1;
         this.keybinding = var2;
         this.keyDesc = I18n.format(var2.getKeyDescription());
         this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format(var2.getKeyDescription()));
         this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset"));
      }

      public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
         boolean var9 = GuiKeyBindingList.access$1(this.this$0).buttonId == this.keybinding;
         Minecraft.fontRendererObj.drawString(this.keyDesc, (double)(var2 + 90 - GuiKeyBindingList.access$2(this.this$0)), (double)(var3 + var5 / 2 - Minecraft.fontRendererObj.FONT_HEIGHT / 2), 16777215);
         this.btnReset.xPosition = var2 + 190;
         this.btnReset.yPosition = var3;
         this.btnReset.enabled = this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault();
         this.btnReset.drawButton(GuiKeyBindingList.access$0(this.this$0), var6, var7);
         this.btnChangeKeyBinding.xPosition = var2 + 105;
         this.btnChangeKeyBinding.yPosition = var3;
         this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());
         boolean var10 = false;
         if (this.keybinding.getKeyCode() != 0) {
            KeyBinding[] var14;
            int var13 = (var14 = GuiKeyBindingList.access$0(this.this$0).gameSettings.keyBindings).length;

            for(int var12 = 0; var12 < var13; ++var12) {
               KeyBinding var11 = var14[var12];
               if (var11 != this.keybinding && var11.getKeyCode() == this.keybinding.getKeyCode()) {
                  var10 = true;
                  break;
               }
            }
         }

         if (var9) {
            this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
         } else if (var10) {
            this.btnChangeKeyBinding.displayString = EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString;
         }

         this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.access$0(this.this$0), var6, var7);
      }

      KeyEntry(GuiKeyBindingList var1, KeyBinding var2, GuiKeyBindingList.KeyEntry var3) {
         this(var1, var2);
      }

      public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
         this.btnChangeKeyBinding.mouseReleased(var2, var3);
         this.btnReset.mouseReleased(var2, var3);
      }

      public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
         if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.access$0(this.this$0), var2, var3)) {
            GuiKeyBindingList.access$1(this.this$0).buttonId = this.keybinding;
            return true;
         } else if (this.btnReset.mousePressed(GuiKeyBindingList.access$0(this.this$0), var2, var3)) {
            GuiKeyBindingList.access$0(this.this$0).gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
            KeyBinding.resetKeyBindingArrayAndHash();
            return true;
         } else {
            return false;
         }
      }

      public void setSelected(int var1, int var2, int var3) {
      }
   }
}
