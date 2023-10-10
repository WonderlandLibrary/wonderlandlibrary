package my.NewSnake.Tank.gui.click.component.slot;

import java.util.Iterator;
import my.NewSnake.Tank.gui.click.ClickGui;
import my.NewSnake.Tank.gui.click.component.window.ModuleWindow;
import my.NewSnake.Tank.gui.click.component.window.OptionWindow;
import my.NewSnake.Tank.gui.click.component.window.Window;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.Renders;
import my.NewSnake.Tank.module.modules.RENDER.Gui;
import my.NewSnake.utils.ClientUtils;

public class ModuleSlotComponent extends SlotComponent {
   private ModuleWindow parentWindow;

   public void keyPress(int var1, char var2) {
      if (this.getOptionWindow() != null) {
         this.getOptionWindow().keyPress(var1, var2);
      }

   }

   public void drag(int var1, int var2, int var3) {
      if (this.getOptionWindow() != null) {
         this.getOptionWindow().drag(var1, var2, var3);
      }

   }

   public void draw(int var1, int var2) {
      boolean var3 = ((Gui)(new Gui()).getInstance()).isDarkTheme();
      Renders.rectangleBordered(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0.5D, Renders.blend(var3 ? -14540254 : -13421773, -16777216, ((Module)this.getParent()).isEnabled() ? (this.hovering(var1, var2) ? 0.6F : 0.7F) : (this.hovering(var1, var2) ? 0.8F : 1.0F)), -15658735);
      Renders.rectangle(this.getX() + 1.0D, this.getY() + 0.5D, this.getX() + this.getWidth() - 1.0D, this.getY() + 1.0D, ((Module)this.getParent()).isEnabled() ? 536870912 : 553648127);
      ClientUtils.clientFont().drawCenteredString(((Module)this.getParent()).getDisplayName(), this.getX() + this.getWidth() / 2.0D, this.getY() + 0.5D + this.getHeight() / 2.0D - (double)(ClientUtils.clientFont().FONT_HEIGHT / 2), -1);
      if (this.getOptionWindow() != null) {
         this.getOptionWindow().draw(var1, var2);
      }

   }

   public ModuleSlotComponent(Module var1, double var2, double var4, double var6, double var8, ModuleWindow var10) {
      super(var1, var2, var4, var6, var8);
      this.parentWindow = var10;
   }

   public void release(int var1, int var2, int var3) {
   }

   public void drag(double var1, double var3, double[] var5) {
      this.setX(this.getX() - var1);
      this.setY(this.getY() - var3);
      if (this.getOptionWindow() != null) {
         this.getOptionWindow().drag(var1, var3, var5);
      }

   }

   public void click(int var1, int var2, int var3) {
      if (this.getOptionWindow() != null) {
         this.getOptionWindow().click(var1, var2, var3);
      }

      if (this.parentWindow.isExtended() && this.hovering(var1, var2)) {
         if (var3 == 0) {
            ((Module)this.getParent()).toggle();
         } else if (var3 == 1) {
            if (this.getOptionWindow() == null) {
               Iterator var5 = ClickGui.getInstance().getWindows().iterator();

               while(var5.hasNext()) {
                  Window var4 = (Window)var5.next();
                  Iterator var7 = var4.getSlotList().iterator();

                  while(var7.hasNext()) {
                     Object var6 = var7.next();
                     SlotComponent var8 = (SlotComponent)var6;
                     var8.setOptionWindow((OptionWindow)null);
                  }
               }

               this.setOptionWindow(new OptionWindow((Module)this.getParent(), this.getX() + this.getWidth() + 4.0D, this.parentWindow.getY(), this.getWidth(), this.parentWindow));
            } else {
               this.setOptionWindow((OptionWindow)null);
            }
         }
      }

   }
}
