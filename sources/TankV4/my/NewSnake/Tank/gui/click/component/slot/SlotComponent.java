package my.NewSnake.Tank.gui.click.component.slot;

import my.NewSnake.Tank.gui.click.component.Component;
import my.NewSnake.Tank.gui.click.component.window.OptionWindow;

public abstract class SlotComponent extends Component {
   protected static final int OUTLINE_COLOR = -15658735;
   private OptionWindow optionWindow;
   protected static final int FILL_COLOR_DEFAULT = -13421773;
   protected static final int FILL_COLOR_DARK = -14540254;

   public SlotComponent(Object var1, double var2, double var4, double var6, double var8) {
      super(var1, var2, var4, var6, var8);
   }

   public void setOptionWindow(OptionWindow var1) {
      this.optionWindow = var1;
   }

   public OptionWindow getOptionWindow() {
      return this.optionWindow;
   }
}
