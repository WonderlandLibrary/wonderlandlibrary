package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class GuiScreenRealmsProxy extends GuiScreen {
   private RealmsScreen field_154330_a;

   public void drawCreativeTabHoveringText(String var1, int var2, int var3) {
      super.drawCreativeTabHoveringText(var1, var2, var3);
   }

   public int func_154329_h() {
      return this.fontRendererObj.FONT_HEIGHT;
   }

   public void drawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      GuiScreen.drawGradientRect((double)var1, (double)var2, (float)var3, (float)var4, var5, var6);
   }

   public void func_154322_b(String var1, int var2, int var3, int var4) {
      super.drawString(this.fontRendererObj, var1, var2, var3, var4);
   }

   public void updateScreen() {
      this.field_154330_a.tick();
      super.updateScreen();
   }

   public void handleMouseInput() throws IOException {
      this.field_154330_a.mouseEvent();
      super.handleMouseInput();
   }

   public void func_154325_a(String var1, int var2, int var3, int var4) {
      super.drawCenteredString(this.fontRendererObj, var1, var2, var3, var4);
   }

   public List func_154320_j() {
      ArrayList var1 = Lists.newArrayListWithExpectedSize(super.buttonList.size());
      Iterator var3 = super.buttonList.iterator();

      while(var3.hasNext()) {
         GuiButton var2 = (GuiButton)var3.next();
         var1.add(((GuiButtonRealmsProxy)var2).getRealmsButton());
      }

      return var1;
   }

   public void func_154327_a(RealmsButton var1) {
      super.buttonList.add(var1.getProxy());
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.field_154330_a.render(var1, var2, var3);
   }

   public void drawWorldBackground(int var1) {
      super.drawWorldBackground(var1);
   }

   public void func_154324_i() {
      super.buttonList.clear();
   }

   public void keyTyped(char var1, int var2) throws IOException {
      this.field_154330_a.keyPressed(var1, var2);
   }

   public void func_154328_b(RealmsButton var1) {
      super.buttonList.remove(var1);
   }

   public void mouseReleased(int var1, int var2, int var3) {
      this.field_154330_a.mouseReleased(var1, var2, var3);
   }

   public List func_154323_a(String var1, int var2) {
      return this.fontRendererObj.listFormattedStringToWidth(var1, var2);
   }

   public GuiScreenRealmsProxy(RealmsScreen var1) {
      this.field_154330_a = var1;
      super.buttonList = Collections.synchronizedList(Lists.newArrayList());
   }

   public int func_154326_c(String var1) {
      return this.fontRendererObj.getStringWidth(var1);
   }

   public void onGuiClosed() {
      this.field_154330_a.removed();
      super.onGuiClosed();
   }

   public void mouseClicked(int var1, int var2, int var3) throws IOException {
      this.field_154330_a.mouseClicked(var1, var2, var3);
      super.mouseClicked(var1, var2, var3);
   }

   public RealmsScreen func_154321_a() {
      return this.field_154330_a;
   }

   public void renderToolTip(ItemStack var1, int var2, int var3) {
      super.renderToolTip(var1, var2, var3);
   }

   public final void actionPerformed(GuiButton var1) throws IOException {
      this.field_154330_a.buttonClicked(((GuiButtonRealmsProxy)var1).getRealmsButton());
   }

   public void drawHoveringText(List var1, int var2, int var3) {
      super.drawHoveringText(var1, var2, var3);
   }

   public void initGui() {
      this.field_154330_a.init();
      super.initGui();
   }

   public void confirmClicked(boolean var1, int var2) {
      this.field_154330_a.confirmResult(var1, var2);
   }

   public void handleKeyboardInput() throws IOException {
      this.field_154330_a.keyboardEvent();
      super.handleKeyboardInput();
   }

   public void mouseClickMove(int var1, int var2, int var3, long var4) {
      this.field_154330_a.mouseDragged(var1, var2, var3, var4);
   }

   public void func_154319_c(String var1, int var2, int var3, int var4) {
      this.fontRendererObj.drawStringWithShadow(var1, (float)var2, (float)var3, var4);
   }

   public void drawDefaultBackground() {
      super.drawDefaultBackground();
   }

   public boolean doesGuiPauseGame() {
      return super.doesGuiPauseGame();
   }
}
