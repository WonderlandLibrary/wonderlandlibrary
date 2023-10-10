package net.minecraft.realms;

import net.minecraft.client.gui.GuiSimpleScrolledSelectionListProxy;

public class RealmsSimpleScrolledSelectionList {
   private final GuiSimpleScrolledSelectionListProxy proxy;

   public void mouseEvent() {
      this.proxy.handleMouseInput();
   }

   public int ym() {
      return this.proxy.getMouseY();
   }

   protected void renderList(int var1, int var2, int var3, int var4) {
   }

   public int getScrollbarPosition() {
      return this.proxy.getWidth() / 2 + 124;
   }

   public int getMaxPosition() {
      return 0;
   }

   public void render(int var1, int var2, float var3) {
      this.proxy.drawScreen(var1, var2, var3);
   }

   protected void renderItem(int var1, int var2, int var3, int var4, Tezzelator var5, int var6, int var7) {
   }

   public int xm() {
      return this.proxy.getMouseX();
   }

   public boolean isSelectedItem(int var1) {
      return false;
   }

   public int getScroll() {
      return this.proxy.getAmountScrolled();
   }

   public int width() {
      return this.proxy.getWidth();
   }

   public void renderItem(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.renderItem(var1, var2, var3, var4, Tezzelator.instance, var5, var6);
   }

   public RealmsSimpleScrolledSelectionList(int var1, int var2, int var3, int var4, int var5) {
      this.proxy = new GuiSimpleScrolledSelectionListProxy(this, var1, var2, var3, var4, var5);
   }

   public int getItemCount() {
      return 0;
   }

   public void selectItem(int var1, boolean var2, int var3, int var4) {
   }

   public void scroll(int var1) {
      this.proxy.scrollBy(var1);
   }

   public void renderBackground() {
   }
}
