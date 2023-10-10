package today.flux.gui.clickgui.classic.component;

import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.window.Window;

public class Placeholder extends Component {
   public Placeholder(Window window, int offX, int offY, Component target) {
      super(window, offX, offY, target.title);
      this.width = Math.max(ClickGUI.defaultWidth, window.width);
      this.height = 0;
      this.type = "Placeholder";
   }

   public void render(int mouseX, int mouseY) {
   }

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
   }
}
