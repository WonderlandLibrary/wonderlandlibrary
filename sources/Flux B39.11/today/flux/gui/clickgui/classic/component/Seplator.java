package today.flux.gui.clickgui.classic.component;

import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.window.Window;

public class Seplator extends Component {
   public Seplator(Window window, int offX, int offY) {
      super(window, offX, offY, "");
      this.width = ClickGUI.settingsWidth;
      this.height = 1;
      this.type = "Seplator";
   }

   public void render(int mouseX, int mouseY) {
      GuiRenderUtils.drawRect((float) (this.x), (float) (this.y), (float) (this.width - (this.parent.scrollbarEnabled ? ClickGUI.scrollbarWidth : 0)), 1.0F, ClickGUI.backgroundColor);
      GuiRenderUtils.drawRect((float) (this.x + 2), (float) (this.y), (float) (this.width - (this.parent.scrollbarEnabled ? ClickGUI.scrollbarWidth : 0) - 4), 1.0F, ClickGUI.mainColor);
   }

   @Override
   public void mouseUpdates(int var1, int var2, boolean var3) {

   }
}
