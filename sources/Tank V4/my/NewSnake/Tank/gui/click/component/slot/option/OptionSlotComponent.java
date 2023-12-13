package my.NewSnake.Tank.gui.click.component.slot.option;

import java.util.ArrayList;
import java.util.List;
import my.NewSnake.Tank.gui.click.component.slot.SlotComponent;
import my.NewSnake.Tank.option.Option;

public abstract class OptionSlotComponent extends SlotComponent {
   private List optionList = new ArrayList();

   public Object getParent() {
      return this.getParent();
   }

   public OptionSlotComponent(Option var1, double var2, double var4, double var6, double var8) {
      super(var1, var2, var4, var6, var8);
   }

   public void drag(int var1, int var2, double[] var3) {
      double var4 = this.getX() - ((double)var1 - var3[0]);
      double var6 = this.getY() - ((double)var2 - var3[1]);
      var4 = (double)(Math.round(var4 / 10.0D) * 10L);
      var6 = (double)(Math.round(var6 / 10.0D) * 10L);
      this.setX(this.getX() - var4);
      this.setY(this.getY() - var6);
      if (this.getOptionWindow() != null) {
         this.getOptionWindow().drag((double)var1, (double)var2, var3);
      }

   }

   public Option getParent() {
      return (Option)super.getParent();
   }

   public List getOptionList() {
      return this.optionList;
   }
}
