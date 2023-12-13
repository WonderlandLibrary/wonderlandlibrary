package optifine;

import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionSliderOF extends GuiOptionSlider implements IOptionControl {
   private GameSettings.Options option = null;

   public GuiOptionSliderOF(int var1, int var2, int var3, GameSettings.Options var4) {
      super(var1, var2, var3, var4);
      this.option = var4;
   }

   public GameSettings.Options getOption() {
      return this.option;
   }
}
