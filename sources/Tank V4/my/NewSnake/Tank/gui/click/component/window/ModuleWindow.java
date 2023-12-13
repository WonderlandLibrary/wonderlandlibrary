package my.NewSnake.Tank.gui.click.component.window;

import java.util.Iterator;
import my.NewSnake.Tank.gui.click.ClickGui;
import my.NewSnake.Tank.gui.click.component.slot.ModuleSlotComponent;
import my.NewSnake.Tank.gui.click.component.slot.SlotComponent;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.module.Renders;

public class ModuleWindow extends Window {
   private static final double PADDING = 4.0D;
   private static final double SLOT_COMPONENT_HEIGHT = 16.0D;
   private static final float BORDER_WIDTH = 1.5F;

   public ModuleWindow(Module.Category var1, double var2, double var4, double var6) {
      super(var1, var2, var4, 0.0D, 0.0D, new Handle(var1.name(), var2 + 1.5D - 1.5D, var4 - 18.0D + 1.5D + 0.5D, var6 + 16.0D - 3.0D + 3.0D, 18.0D));
      double var8 = 1.5D;
      Iterator var11 = ModuleManager.getModules().iterator();

      while(var11.hasNext()) {
         Module var10 = (Module)var11.next();
         if (var10.getCategory().equals(var1)) {
            ModuleSlotComponent var12 = new ModuleSlotComponent(var10, var2 + 1.5D + 1.5D, var4 + var8 - 0.5D, var6 + 16.0D - 3.0D - 3.0D, 17.0D, this);
            this.getSlotList().add(var12);
            var8 += 18.0D;
         }
      }

      var6 += 16.0D;
      ++var8;
      this.setWidth(var6);
      this.setHeight(var8);
      this.getHandle().setParent(this);
   }

   public void draw(int var1, int var2) {
      this.getHandle().draw(var1, var2, this.isExtended());
      if (this.isExtended()) {
         int[] var3 = new int[]{-14540254, -14540254, Renders.blend(-14540254, -16777216, 0.95F), Renders.blend(-14540254, -16777216, 0.95F)};
         int[] var4 = new int[]{Renders.blend(-15658735, -16777216, 0.95F), Renders.blend(-15658735, -16777216, 0.95F), -15658735, -15658735};
         Renders.rectangleBorderedGradient(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), var3, var4, 0.5D);
         Iterator var6 = this.getSlotList().iterator();

         while(var6.hasNext()) {
            SlotComponent var5 = (SlotComponent)var6.next();
            var5.draw(var1, var2);
         }
      }

   }

   public void click(int var1, int var2, int var3) {
      Window var4 = ClickGui.getInstance().getTopWindow(var1, var2);
      if (var4 != null && var4.equals(this) && this.getHandle().hovering(var1, var2)) {
         this.getHandle().click(var1, var2, var3);
      }

      Iterator var6 = this.getSlotList().iterator();

      while(var6.hasNext()) {
         SlotComponent var5 = (SlotComponent)var6.next();
         var5.click(var1, var2, var3);
      }

   }

   public void release(int var1, int var2, int var3) {
      this.getHandle().release(var1, var2, var3);
   }

   public void drag(int var1, int var2, int var3) {
      if (this.isDragging()) {
         double var4 = this.getX() - ((double)var1 - this.getStartOffset()[0]);
         double var6 = this.getY() - ((double)var2 - this.getStartOffset()[1]);
         var4 = (double)(Math.round(var4 / 10.0D) * 10L);
         var6 = (double)(Math.round(var6 / 10.0D) * 10L);
         Iterator var9 = this.getSlotList().iterator();

         while(var9.hasNext()) {
            SlotComponent var8 = (SlotComponent)var9.next();
            ((ModuleSlotComponent)var8).drag(var4, var6, this.getStartOffset());
         }

         this.setX(this.getX() - var4);
         this.setY(this.getY() - var6);
         this.getHandle().setX(this.getHandle().getX() - var4);
         this.getHandle().setY(this.getHandle().getY() - var6);
      }

      Iterator var5 = this.getSlotList().iterator();

      while(var5.hasNext()) {
         SlotComponent var10 = (SlotComponent)var5.next();
         ((ModuleSlotComponent)var10).drag(var1, var2, var3);
      }

   }

   public void keyPress(int var1, char var2) {
      Iterator var4 = this.getSlotList().iterator();

      while(var4.hasNext()) {
         SlotComponent var3 = (SlotComponent)var4.next();
         var3.keyPress(var1, var2);
      }

   }
}
