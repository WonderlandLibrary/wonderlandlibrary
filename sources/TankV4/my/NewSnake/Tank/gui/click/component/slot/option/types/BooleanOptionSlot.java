package my.NewSnake.Tank.gui.click.component.slot.option.types;

import my.NewSnake.Tank.gui.click.component.slot.option.OptionSlotComponent;
import my.NewSnake.Tank.module.Renders;
import my.NewSnake.Tank.module.modules.RENDER.Gui;
import my.NewSnake.Tank.option.types.BooleanOption;
import my.NewSnake.utils.ClientUtils;

public class BooleanOptionSlot extends OptionSlotComponent {
   public void draw(int var1, int var2) {
      boolean var3 = ((Gui)(new Gui()).getInstance()).isDarkTheme();
      Renders.rectangleBordered(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0.5D, Renders.blend(var3 ? -14540254 : -13421773, -16777216, (Boolean)((BooleanOption)this.getParent()).getValue() ? (this.hovering(var1, var2) ? 0.6F : 0.7F) : (this.hovering(var1, var2) ? 0.8F : 1.0F)), -15658735);
      Renders.rectangle(this.getX() + 1.0D, this.getY() + 0.5D, this.getX() + this.getWidth() - 1.0D, this.getY() + 1.0D, (Boolean)((BooleanOption)this.getParent()).getValue() ? 536870912 : 553648127);
      ClientUtils.clientFont().drawCenteredString(((BooleanOption)this.getParent()).getDisplayName(), this.getX() + this.getWidth() / 2.0D, this.getY() + 0.5D + this.getHeight() / 2.0D - (double)(ClientUtils.clientFont().FONT_HEIGHT / 2), -1);
   }

   public void keyPress(int var1, char var2) {
   }

   public BooleanOptionSlot(BooleanOption var1, double var2, double var4, double var6, double var8) {
      super(var1, var2, var4, var6, var8);
   }

   public void release(int var1, int var2, int var3) {
   }

   public void drag(int var1, int var2, int var3) {
   }

   public void click(int var1, int var2, int var3) {
      if (var3 == 0) {
         ((BooleanOption)this.getParent()).setValue(!(Boolean)((BooleanOption)this.getParent()).getValue());
      }

   }
}
