package today.flux.gui.clickgui.classic.component;

import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;

public abstract class Button extends Component {
   public boolean isToggled;

   public Button(Window window, int offX, int offY, String title) {
      super(window, offX, offY, title);
      this.width = ClickGUI.defaultWidth;
      this.height = ClickGUI.buttonHeight;
      this.type = "Button";
   }

   public void render(int mouseX, int mouseY) {
      if (this.isHovered) {
         GuiRenderUtils.drawRect((float) this.x, (float) this.y, (float) (this.width - (this.parent.scrollbarEnabled ? ClickGUI.scrollbarWidth : 0)), (float) this.height, 0);
      } else {
         GuiRenderUtils.drawRect((float) this.x, (float) this.y, (float) (this.width - (this.parent.scrollbarEnabled ? ClickGUI.scrollbarWidth : 0)), (float) this.height, ClickGUI.backgroundColor);
      }

      FontManager.small.drawString(this.title, this.x + 2, this.y + this.height / 2 - FontManager.small.getHeight(this.title) / 2, this.isToggled ? ClickGUI.mainColor : 16777215);
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
   }

   protected abstract void pressed();

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
      this.isHovered = this.contains(mouseX, mouseY) && this.parent.mouseOver(mouseX, mouseY);
      if (isPressed && !this.wasMousePressed && this.isHovered) {
         this.pressed();
      }

      this.wasMousePressed = isPressed;
   }
}
