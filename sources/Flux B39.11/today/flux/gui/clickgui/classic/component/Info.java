package today.flux.gui.clickgui.classic.component;

import net.minecraft.client.Minecraft;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.window.Window;
import today.flux.gui.fontRenderer.FontManager;

public class Info extends Component {
   boolean heightInitialized = false;

   public Info(Window window, int offX, int offY, String title) {
      super(window, offX, offY, title);
      this.width = Math.max(ClickGUI.defaultWidth, window.width);
      this.height = ClickGUI.defaultWidth;
      this.type = "Info";
      this.editable = false;
   }

   public void render(int mouseX, int mouseY) {
      float fontHeight = FontManager.normal.getHeight(this.title);
      int y = this.y;
      String coords = "X:" + (int) Minecraft.getMinecraft().thePlayer.posX + " " + "Y:" + (int) Minecraft.getMinecraft().thePlayer.posY + " " + "Z:" + (int) Minecraft.getMinecraft().thePlayer.posZ;
      GuiRenderUtils.drawRect((float) this.x, (float) y, (float) this.width, 14.0F, ClickGUI.backgroundColor);
      FontManager.normal.drawString(coords, (float) (this.x + 2), (float) (y + 7) - fontHeight / 2.0F, 16777215);
      y += 14;

      y += 14;
      this.height = y - this.y;
      if (!this.heightInitialized) {
         this.heightInitialized = true;
         this.parent.repositionComponents();
      }

   }

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
   }
}
