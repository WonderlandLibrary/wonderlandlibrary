package my.NewSnake.Tank.gui.click.component.slot.option.types;

import my.NewSnake.Tank.gui.click.ClickGui;
import my.NewSnake.Tank.gui.click.component.slot.option.OptionSlotComponent;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.Renders;
import my.NewSnake.Tank.module.modules.RENDER.Gui;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.utils.ClientUtils;
import org.lwjgl.input.Keyboard;

public class KeybindOptionSlot extends OptionSlotComponent {
   private Module module;
   private static final double PADDING = 4.0D;
   private static final double VALUE_WINDOW_PADDING = 6.0D;

   public void release(int var1, int var2, int var3) {
   }

   public void drag(int var1, int var2, int var3) {
   }

   public void keyPress(int var1, char var2) {
      if (ClickGui.getInstance().isBinding() && var1 == 1) {
         this.module.setKeybind(-1);
         ClickGui.getInstance().setBinding(false);
      } else if (ClickGui.getInstance().isBinding()) {
         this.module.setKeybind(var1);
         ClickGui.getInstance().setBinding(false);
      }

   }

   public void click(int var1, int var2, int var3) {
      if (this.hovering(var1, var2) && var3 == 0) {
         ClickGui.getInstance().setBinding(!ClickGui.getInstance().isBinding());
      }

   }

   public KeybindOptionSlot(Module var1, double var2, double var4, double var6, double var8) {
      super((Option)null, var2, var4, var6, var8);
      this.module = var1;
   }

   public void draw(int var1, int var2) {
      boolean var3 = ((Gui)(new Gui()).getInstance()).isDarkTheme();
      Renders.rectangleBordered(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0.5D, Renders.blend(var3 ? -14540254 : -13421773, -16777216, ClickGui.getInstance().isBinding() ? (this.hovering(var1, var2) ? 0.6F : 0.7F) : (this.hovering(var1, var2) ? 0.8F : 1.0F)), -15658735);
      Renders.rectangle(this.getX() + 1.0D, this.getY() + 0.5D, this.getX() + this.getWidth() - 1.0D, this.getY() + 1.0D, ClickGui.getInstance().isBinding() ? 536870912 : 553648127);
      ClientUtils.clientFont().drawCenteredString("Bind", this.getX() + this.getWidth() / 2.0D, this.getY() + 0.5D + this.getHeight() / 2.0D - (double)(ClientUtils.clientFont().FONT_HEIGHT / 2), -1);
      double var4 = (double)ClientUtils.clientFont().getStringWidth(this.module.getKeybind() < 0 ? "None" : Keyboard.getKeyName(this.module.getKeybind())) + 16.0D;
      double var6 = this.getX() + this.getWidth() + 6.0D;
      double var8 = this.getX() + this.getWidth() + 6.0D + var4;
      int[] var10 = new int[]{-14540254, -14540254, Renders.blend(-14540254, -16777216, 0.95F), Renders.blend(-14540254, -16777216, 0.95F)};
      int[] var11 = new int[]{Renders.blend(-15658735, -16777216, 0.95F), Renders.blend(-15658735, -16777216, 0.95F), -15658735, -15658735};
      Renders.rectangleBorderedGradient(var6, this.getY(), var8, this.getY() + this.getHeight(), var10, var11, 0.5D);
      Renders.rectangle(var6 + 1.0D, this.getY() + 0.5D, var8 - 1.0D, this.getY() + 1.0D, 553648127);
      ClientUtils.clientFont().drawScaledString(this.module.getKeybind() < 0 ? "None" : Keyboard.getKeyName(this.module.getKeybind()), var6 + var4 / 2.0D, this.getY() + this.getHeight() / 2.0D + 1.5D, -1, 1.1D);
   }
}
