package shadersmod.client;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiSlot;
import optifine.Lang;

class GuiSlotShaders extends GuiSlot {
   private ArrayList shaderslist;
   private int selectedIndex;
   private long lastClickedCached = 0L;
   final GuiShaders shadersGui;

   public GuiSlotShaders(GuiShaders var1, int var2, int var3, int var4, int var5, int var6) {
      super(var1.getMc(), var2, var3, var4, var5, var6);
      this.shadersGui = var1;
      this.updateList();
      this.amountScrolled = 0.0F;
      int var7 = this.selectedIndex * var6;
      int var8 = (var5 - var4) / 2;
      if (var7 > var8) {
         this.scrollBy(var7 - var8);
      }

   }

   public int getListWidth() {
      return this.width - 20;
   }

   public void updateList() {
      this.shaderslist = Shaders.listOfShaders();
      this.selectedIndex = 0;
      int var1 = 0;

      for(int var2 = this.shaderslist.size(); var1 < var2; ++var1) {
         if (((String)this.shaderslist.get(var1)).equals(Shaders.currentshadername)) {
            this.selectedIndex = var1;
            break;
         }
      }

   }

   protected int getSize() {
      return this.shaderslist.size();
   }

   protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      if (var1 != this.selectedIndex || this.lastClicked != this.lastClickedCached) {
         this.selectedIndex = var1;
         this.lastClickedCached = this.lastClicked;
         Shaders.setShaderPack((String)this.shaderslist.get(var1));
         Shaders.uninit();
         this.shadersGui.updateButtons();
      }

   }

   protected boolean isSelected(int var1) {
      return var1 == this.selectedIndex;
   }

   protected int getScrollBarX() {
      return this.width - 6;
   }

   protected int getContentHeight() {
      return this.getSize() * 18;
   }

   protected void drawBackground() {
   }

   protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = (String)this.shaderslist.get(var1);
      if (var7.equals(Shaders.packNameNone)) {
         var7 = Lang.get("of.options.shaders.packNone");
      } else if (var7.equals(Shaders.packNameDefault)) {
         var7 = Lang.get("of.options.shaders.packDefault");
      }

      this.shadersGui.drawCenteredString(var7, this.width / 2, var3 + 1, 16777215);
   }

   public int getSelectedIndex() {
      return this.selectedIndex;
   }
}
