package optifine;

import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionButtonOF extends GuiOptionButton implements IOptionControl {
   private GameSettings.Options option = null;

   public GuiOptionButtonOF(int var1, int var2, int var3, GameSettings.Options var4, String var5) {
      super(var1, var2, var3, var4, var5);
      this.option = var4;
   }

   public GameSettings.Options getOption() {
      return this.option;
   }
}
