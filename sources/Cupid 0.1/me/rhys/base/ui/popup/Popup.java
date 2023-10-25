package me.rhys.base.ui.popup;

import java.awt.Color;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Popup extends Panel {
  protected static final int BACKGROUND_COLOR = (new Color(27, 34, 44, 255)).getRGB();
  
  protected static final int SHADOW_COLOR = ColorUtil.lighten(BACKGROUND_COLOR, 15).getRGB();
  
  private final String title;
  
  private Panel panel;
  
  public Popup(String title, int width, int height) {
    super(new Vec2f(), width, height);
    this.title = title;
    this.background = BACKGROUND_COLOR;
  }
  
  public void addToBody(Element element) {
    this.panel.add(element);
  }
  
  public void onShow() {
    getContainer().getItems().clear();
    this.panel = new Panel(new Vec2f(1.0F, FontUtil.getFontHeight() + 11.0F), this.width - 2, (int)(this.height - FontUtil.getFontHeight() + 12.0F));
    add((Element)this.panel);
  }
  
  public void onHide() {}
  
  public void onDraw() {}
  
  public void draw(Vec2f mouse, float partialTicks) {
    GlStateManager.pushMatrix();
    GL11.glLineWidth(4.0F);
    RenderUtil.drawOutlineRect(this.pos, this.pos.clone().add(this.width, this.height), SHADOW_COLOR);
    GL11.glLineWidth(1.0F);
    GlStateManager.popMatrix();
    int headerHeight = (int)(FontUtil.getFontHeight() + 10.0F);
    RenderUtil.drawRect(this.pos.clone().add(1, 1), this.width - 2, headerHeight, ColorUtil.darken(BACKGROUND_COLOR, 5).getRGB());
    RenderUtil.drawRect(this.pos.clone().add(0, headerHeight), this.width, 1, SHADOW_COLOR);
    GlStateManager.pushMatrix();
    float scale = 0.8F;
    GlStateManager.scale(scale, scale, scale);
    FontUtil.drawCenteredString(this.title, this.pos.clone().add(this.width / 2.0F, headerHeight / 2.0F).div(scale, scale), -1);
    GlStateManager.scale(1.0F / scale, 1.0F / scale, 1.0F / scale);
    GlStateManager.popMatrix();
    super.draw(mouse, partialTicks);
    onDraw();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\popup\Popup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */