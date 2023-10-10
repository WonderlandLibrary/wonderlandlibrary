package net.minecraft.realms;

import net.minecraft.client.gui.GuiSlotRealmsProxy;

public class RealmsScrolledSelectionList {
   private final GuiSlotRealmsProxy proxy;

   public boolean isSelectedItem(int var1) {
      return false;
   }

   public void scroll(int var1) {
      this.proxy.scrollBy(var1);
   }

   public int getMaxPosition() {
      return 0;
   }

   public void renderItem(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.renderItem(var1, var2, var3, var4, Tezzelator.instance, var5, var6);
   }

   public int getScrollbarPosition() {
      return this.proxy.func_154338_k() / 2 + 124;
   }

   public int ym() {
      return this.proxy.func_154339_l();
   }

   public void selectItem(int var1, boolean var2, int var3, int var4) {
   }

   public int width() {
      return this.proxy.func_154338_k();
   }

   public void render(int var1, int var2, float var3) {
      this.proxy.drawScreen(var1, var2, var3);
   }

   protected void renderItem(int var1, int var2, int var3, int var4, Tezzelator var5, int var6, int var7) {
   }

   public int xm() {
      return this.proxy.func_154337_m();
   }

   public void renderBackground() {
   }

   public RealmsScrolledSelectionList(int var1, int var2, int var3, int var4, int var5) {
      this.proxy = new GuiSlotRealmsProxy(this, var1, var2, var3, var4, var5);
   }

   public int getItemCount() {
      return 0;
   }

   public int getScroll() {
      return this.proxy.getAmountScrolled();
   }

   protected void renderList(int var1, int var2, int var3, int var4) {
   }

   public void mouseEvent() {
      this.proxy.handleMouseInput();
   }
}
