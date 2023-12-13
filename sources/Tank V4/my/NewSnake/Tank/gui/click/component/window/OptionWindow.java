package my.NewSnake.Tank.gui.click.component.window;

import java.util.Iterator;
import my.NewSnake.Tank.gui.click.component.slot.SlotComponent;
import my.NewSnake.Tank.gui.click.component.slot.option.types.BooleanOptionSlot;
import my.NewSnake.Tank.gui.click.component.slot.option.types.KeybindOptionSlot;
import my.NewSnake.Tank.gui.click.component.slot.option.types.NumberOptionSlot;
import my.NewSnake.Tank.gui.click.component.slot.option.types.VisibilityOptionSlot;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.Renders;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.Tank.option.types.BooleanOption;
import my.NewSnake.Tank.option.types.NumberOption;
import my.NewSnake.utils.ClientUtils;

public class OptionWindow extends Window {
   private static final double SLOT_COMPONENT_HEIGHT = 16.0D;
   private static final float BORDER_WIDTH = 1.5F;
   private static final double PADDING = 4.0D;
   private ModuleWindow parentWindow;

   public void drag(double var1, double var3, double[] var5) {
      Iterator var7 = this.getSlotList().iterator();

      while(var7.hasNext()) {
         SlotComponent var6 = (SlotComponent)var7.next();
         var6.setX(var6.getX() - var1);
         var6.setY(var6.getY() - var3);
         if (var6.getOptionWindow() != null) {
            var6.getOptionWindow().setX(var6.getOptionWindow().getX() - var1);
            var6.getOptionWindow().setY(var6.getOptionWindow().getY() - var3);
         }
      }

      this.setX(this.getX() - var1);
      this.setY(this.getY() - var3);
      this.getHandle().setX(this.getHandle().getX() - var1);
      this.getHandle().setY(this.getHandle().getY() - var3);
   }

   public void keyPress(int var1, char var2) {
      Iterator var4 = this.getSlotList().iterator();

      while(var4.hasNext()) {
         SlotComponent var3 = (SlotComponent)var4.next();
         var3.keyPress(var1, var2);
      }

   }

   public OptionWindow(Module var1, double var2, double var4, double var6, ModuleWindow var8) {
      super(var1, var2, var4, 0.0D, 0.0D, new Handle(var1.getDisplayName(), var2 + 1.5D - 1.5D, var4 - 18.0D + 1.5D + 0.5D, var6 + 16.0D - 3.0D + 3.0D, 18.0D));
      this.parentWindow = var8;
      double var9 = 1.5D;

      Option var11;
      Iterator var12;
      for(var12 = var1.getOptions().iterator(); var12.hasNext(); var6 = Math.max(var6, (double)ClientUtils.clientFont().getStringWidth(var11.getDisplayName()))) {
         var11 = (Option)var12.next();
      }

      var12 = var1.getOptions().iterator();

      while(var12.hasNext()) {
         var11 = (Option)var12.next();
         Object var13 = null;
         if (var11 instanceof BooleanOption) {
            var13 = new BooleanOptionSlot((BooleanOption)var11, var2 + 1.5D + 1.5D, var4 + var9 - 0.5D, var6 + 16.0D - 3.0D - 3.0D, 17.0D);
         } else if (var11 instanceof NumberOption) {
            var13 = new NumberOptionSlot((NumberOption)var11, var2 + 1.5D + 1.5D, var4 + var9 - 0.5D, var6 + 16.0D - 3.0D - 3.0D, 17.0D);
         }

         if (var13 != null) {
            this.getSlotList().add(var13);
            var9 += 18.0D;
         }
      }

      this.getSlotList().add(new KeybindOptionSlot(var1, var2 + 1.5D + 1.5D, var4 + var9 - 0.5D, var6 + 16.0D - 3.0D - 3.0D, 17.0D));
      var9 += 18.0D;
      this.getSlotList().add(new VisibilityOptionSlot(var1, var2 + 1.5D + 1.5D, var4 + var9 - 0.5D, var6 + 16.0D - 3.0D - 3.0D, 17.0D));
      var9 += 18.0D;
      var6 += 16.0D;
      ++var9;
      this.setWidth(var6);
      this.setHeight(var9);
      this.getHandle().setWidth(var6);
      this.getHandle().setParent(this);
   }

   public void release(int var1, int var2, int var3) {
   }

   public void draw(int var1, int var2) {
      this.getHandle().draw(var1, var2, this.isExtended());
      int[] var3 = new int[]{-14540254, -14540254, Renders.blend(-14540254, -16777216, 0.95F), Renders.blend(-14540254, -16777216, 0.95F)};
      int[] var4 = new int[]{Renders.blend(-15658735, -16777216, 0.95F), Renders.blend(-15658735, -16777216, 0.95F), -15658735, -15658735};
      Renders.rectangleBorderedGradient(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), var3, var4, 0.5D);
      Iterator var6 = this.getSlotList().iterator();

      while(var6.hasNext()) {
         SlotComponent var5 = (SlotComponent)var6.next();
         var5.draw(var1, var2);
      }

   }

   public void drag(int var1, int var2, int var3) {
      Iterator var5 = this.getSlotList().iterator();

      while(var5.hasNext()) {
         SlotComponent var4 = (SlotComponent)var5.next();
         var4.drag(var1, var2, var3);
      }

   }

   public void click(int var1, int var2, int var3) {
      Iterator var5 = this.getSlotList().iterator();

      while(var5.hasNext()) {
         SlotComponent var4 = (SlotComponent)var5.next();
         if (var4.hovering(var1, var2)) {
            var4.click(var1, var2, var3);
         }
      }

   }
}
