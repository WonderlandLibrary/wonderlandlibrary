package my.NewSnake.Tank.gui.click.component.slot.option.types;

import my.NewSnake.Tank.gui.click.component.slot.option.OptionSlotComponent;
import my.NewSnake.Tank.module.Renders;
import my.NewSnake.Tank.module.modules.RENDER.Gui;
import my.NewSnake.Tank.option.types.NumberOption;
import my.NewSnake.utils.ClientUtils;

public class NumberOptionSlot extends OptionSlotComponent {
   private static final double PADDING = 4.0D;
   private boolean drag;
   private static final double VALUE_WINDOW_PADDING = 6.0D;

   public void draw(int var1, int var2) {
      double var3 = (this.getWidth() - 2.0D) * (((double)((Number)((NumberOption)this.getParent()).getValue()).floatValue() - ((NumberOption)this.getParent()).getMin()) / (((NumberOption)this.getParent()).getMax() - ((NumberOption)this.getParent()).getMin()));
      boolean var5 = ((Gui)(new Gui()).getInstance()).isDarkTheme();
      Renders.rectangleBordered(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0.5D, Renders.blend(var5 ? -14540254 : -13421773, -16777216, 0.6F), -15658735);
      Renders.rectangle(this.getX() + 1.0D, this.getY() + 0.5D, this.getX() + this.getWidth() - 1.0D, this.getY() + 1.0D, 536870912);
      Renders.rectangle(this.getX() + 0.5D, this.getY() + 0.5D, this.getX() + var3 + 1.5D, this.getY() + this.getHeight() - 0.5D, -1259146510);
      ClientUtils.clientFont().drawCenteredString(((NumberOption)this.getParent()).getDisplayName(), this.getX() + this.getWidth() / 2.0D, this.getY() + 0.5D + this.getHeight() / 2.0D - (double)(ClientUtils.clientFont().FONT_HEIGHT / 2), -1);
      double var6 = (double)ClientUtils.clientFont().getStringWidth("" + ((Number)((NumberOption)this.getParent()).getValue()).floatValue()) + 16.0D;
      double var8 = this.getX() + this.getWidth() + 6.0D;
      double var10 = this.getX() + this.getWidth() + 6.0D + var6;
      int[] var12 = new int[]{-14540254, -14540254, Renders.blend(-14540254, -16777216, 0.95F), Renders.blend(-14540254, -16777216, 0.95F)};
      int[] var13 = new int[]{Renders.blend(-15658735, -16777216, 0.95F), Renders.blend(-15658735, -16777216, 0.95F), -15658735, -15658735};
      Renders.rectangleBorderedGradient(var8, this.getY(), var10, this.getY() + this.getHeight(), var12, var13, 0.5D);
      Renders.rectangle(var8 + 1.0D, this.getY() + 0.5D, var10 - 1.0D, this.getY() + 1.0D, 553648127);
      ClientUtils.clientFont().drawScaledString("" + ((Number)((NumberOption)this.getParent()).getValue()).floatValue(), var8 + var6 / 2.0D, this.getY() + this.getHeight() / 2.0D + 1.5D, -1, 1.1D);
   }

   public void click(int var1, int var2, int var3) {
      if (this.hovering(var1, var2)) {
         double var4 = ((NumberOption)this.getParent()).getMin();
         double var6 = ((NumberOption)this.getParent()).getMax();
         double var8 = ((NumberOption)this.getParent()).getIncrement();
         double var10 = (double)var1 - (this.getX() + 1.0D);
         double var12 = var10 / (this.getWidth() - 2.0D);
         var12 = Math.min(Math.max(0.0D, var12), 1.0D);
         double var14 = (var6 - var4) * var12;
         double var16 = var4 + var14;
         var16 = (double)Math.round(var16 * (1.0D / var8)) / (1.0D / var8);
         ((NumberOption)this.getParent()).setValue((Number)var16);
         this.drag = true;
      }

   }

   public void drag(int var1, int var2, int var3) {
      if (this.drag && this.hovering(var1, var2)) {
         double var4 = ((NumberOption)this.getParent()).getMin();
         double var6 = ((NumberOption)this.getParent()).getMax();
         double var8 = ((NumberOption)this.getParent()).getIncrement();
         double var10 = (double)var1 - (this.getX() + 1.0D);
         double var12 = var10 / (this.getWidth() - 2.0D);
         var12 = Math.min(Math.max(0.0D, var12), 1.0D);
         double var14 = (var6 - var4) * var12;
         double var16 = var4 + var14;
         var16 = (double)Math.round(var16 * (1.0D / var8)) / (1.0D / var8);
         ((NumberOption)this.getParent()).setValue((Number)var16);
      }

   }

   public NumberOptionSlot(NumberOption var1, double var2, double var4, double var6, double var8) {
      super(var1, var2, var4, var6, var8);
   }

   public void keyPress(int var1, char var2) {
   }

   public void release(int var1, int var2, int var3) {
      this.drag = false;
   }
}
