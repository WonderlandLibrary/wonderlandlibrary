package team.massacre.api.ui.main;

import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import team.massacre.api.ui.main.components.UIComponent;

public final class GuiMainUIScreen extends GuiScreen {
   private final UIComponent uiComponent = new UIComponent(300.0F, 270.0F);

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.uiComponent.drawComponent(new ScaledResolution(this.mc), mouseX, mouseY);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.uiComponent.onMouseClick(mouseX, mouseY, mouseButton);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      this.uiComponent.onKeyPress(keyCode);
   }

   protected void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
      this.uiComponent.onMouseRelease(state);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
