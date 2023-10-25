package me.rhys.client.ui.login;

import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UILoginMenu extends UIScreen {
  private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation("Lite/menu/background.jpg");
  
  protected void preDraw(Vec2f mouse, float partialTicks) {
    ScaledResolution resolution = getResolution();
    RenderUtil.drawImage(BACKGROUND_LOCATION, new Vec2f(0.0F, 0.0F), resolution.getScaledWidth(), resolution.getScaledHeight());
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\login\UILoginMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */