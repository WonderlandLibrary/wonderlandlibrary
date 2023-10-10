package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsScrolledSelectionList;

public class GuiSlotRealmsProxy extends GuiSlot {
   private final RealmsScrolledSelectionList selectionList;

   protected int getScrollBarX() {
      return this.selectionList.getScrollbarPosition();
   }

   protected void drawBackground() {
      this.selectionList.renderBackground();
   }

   public int func_154338_k() {
      return super.width;
   }

   protected boolean isSelected(int var1) {
      return this.selectionList.isSelectedItem(var1);
   }

   public GuiSlotRealmsProxy(RealmsScrolledSelectionList var1, int var2, int var3, int var4, int var5, int var6) {
      super(Minecraft.getMinecraft(), var2, var3, var4, var5, var6);
      this.selectionList = var1;
   }

   protected int getContentHeight() {
      return this.selectionList.getMaxPosition();
   }

   protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      this.selectionList.selectItem(var1, var2, var3, var4);
   }

   public int func_154339_l() {
      return super.mouseY;
   }

   public int func_154337_m() {
      return super.mouseX;
   }

   protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.selectionList.renderItem(var1, var2, var3, var4, var5, var6);
   }

   public void handleMouseInput() {
      super.handleMouseInput();
   }

   protected int getSize() {
      return this.selectionList.getItemCount();
   }
}
