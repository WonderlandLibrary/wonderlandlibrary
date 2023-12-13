package my.NewSnake.utils;

import java.awt.Color;
import my.NewSnake.Tank.Snake;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextBox extends GuiTextField {
   private final String title;
   private final boolean hide;

   public void drawTextBox(int var1, int var2) {
      Render2D var10000 = Snake.RENDER2D;
      double var10001 = (double)this.xPosition;
      double var10002 = (double)this.yPosition;
      double var10003 = (double)this.width;
      double var10004 = (double)this.height;
      GuiTextBox var10005;
      double var10006;
      Color var10007;
      if (this.isFocused()) {
         var10005 = this;
         var10006 = var1;
         var10007 = var2 != 0 ? new Color(255, 255, 255, 100) : new Color(255, 255, 255, 50);
      } else {
         var10005 = this;
         var10006 = var1;
         var10007 = var2 != 0 ? new Color(40, 40, 40) : new Color(30, 30, 30);
      }

      var10002.rect(var10003, var10004, (double)var10005, var10006, var10007);
      boolean var3 = Snake.fontManager.getFont("SFL 10").getWidth(this.hide ? this.getText().replaceAll(".", "*") : this.getText()) > (float)(this.width - 10);
      int var4 = (int)(var3 ? Snake.fontManager.getFont("SFL 10").getWidth(this.hide ? this.getText().replaceAll(".", "*") : this.getText()) - (float)this.width + 12.0F : 0.0F);
      Snake.RENDER2D.push();
      Snake.RENDER2D.enable(3089);
      Snake.RENDER2D.scissor((double)(this.xPosition + 5), (double)this.yPosition, (double)(this.width - 5), (double)this.height);
      if ((!this.isFocused() || this.isFocused()) && this.getTextUntrimmed().length() == 0) {
         Snake.fontManager.getFont("SFL 10").drawStringWithShadow(this.title, (float)(this.xPosition + 5), (float)(this.yPosition + this.height / 2) - Snake.fontManager.getFont("SFL 10").getHeight(this.title) / 2.0F, -2139062144);
      }

      if (this.getText().length() > 0) {
         String var5 = this.hide ? this.getTextUntrimmed().replaceAll(".", "*") : this.getTextUntrimmed();
         if (this.cursorPosition != this.getTextUntrimmed().length()) {
            var5 = var5.substring(0, this.cursorPosition).concat(this.cursorCounter / 6 % 2 == 0 ? "_" : "  ").concat(var5.substring(this.cursorPosition, var5.length()));
         }

         Snake.fontManager.getFont("SFL 10").drawStringWithShadow(var5, (float)(this.xPosition + 5 - var4), (float)(this.yPosition + this.height / 2) - Snake.fontManager.getFont("SFL 10").getHeight(var5) / 2.0F, -1);
      }

      if (this.cursorCounter / 6 % 2 == 0 && this.isFocused() && this.cursorPosition == this.getTextUntrimmed().length()) {
         Snake.fontManager.getFont("SFL 10").drawStringWithShadow("_", (float)(this.xPosition + 4 - var4) + Snake.fontManager.getFont("SFL 10").getWidth(this.hide ? this.getTextUntrimmed().replaceAll(".", "*") : this.getText()), (float)(this.yPosition + this.height / 2) - Snake.fontManager.getFont("SFL 10").getHeight("_") / 2.0F, -1);
      }

      Snake.RENDER2D.disable(3089);
      Snake.RENDER2D.pop();
   }

   public GuiTextBox(int var1, FontRenderer var2, String var3, int var4, int var5, int var6, int var7, boolean var8) {
      super(var1, var2, var4, var5, var6, var7);
      this.title = var3;
      this.hide = var8;
   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
   }

   public boolean textboxKeyTyped(char var1, int var2) {
      return super.textboxKeyTyped(var1, var2);
   }

   public String getText() {
      return super.getText().trim();
   }

   public String getTextUntrimmed() {
      return super.getText();
   }

   public GuiTextBox(int var1, FontRenderer var2, String var3, int var4, int var5, int var6, int var7) {
      super(var1, var2, var4, var5, var6, var7);
      this.title = var3;
      this.hide = false;
   }
}
