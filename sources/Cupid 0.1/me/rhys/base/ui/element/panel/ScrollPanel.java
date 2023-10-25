package me.rhys.base.ui.element.panel;

import com.google.common.util.concurrent.AtomicDouble;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ScrollPanel extends Panel {
  private float scrollAmount;
  
  private float itemMargin;
  
  public void setScrollAmount(float scrollAmount) {
    this.scrollAmount = scrollAmount;
  }
  
  public void setItemMargin(float itemMargin) {
    this.itemMargin = itemMargin;
  }
  
  public float getScrollAmount() {
    return this.scrollAmount;
  }
  
  public float getItemMargin() {
    return this.itemMargin;
  }
  
  public ScrollPanel(Vec2f offset, int width, int height) {
    super(offset, width, height);
    this.scrollAmount = 0.0F;
    this.itemMargin = 0.0F;
  }
  
  public void clickMouse(Vec2f pos, int button) {
    this.container.filter(element -> element.isHovered(pos.clone().add(0.0F, this.scrollAmount))).forEach(element -> element.clickMouse(pos.clone().add(0.0F, this.scrollAmount), button));
  }
  
  public void dragMouse(Vec2f pos, int button, long lastClickTime) {
    this.container.filter(element -> element.isHovered(pos.clone().add(0.0F, this.scrollAmount))).forEach(element -> element.dragMouse(pos.clone().add(0.0F, this.scrollAmount), button, lastClickTime));
  }
  
  public void releaseMouse(Vec2f pos, int button) {
    this.container.filter(element -> element.isHovered(pos.clone().add(0.0F, this.scrollAmount))).forEach(element -> element.releaseMouse(pos.clone().add(0.0F, this.scrollAmount), button));
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    float maxItemHeight = alignItems();
    boolean canScroll = (maxItemHeight > this.height);
    float maxScroll = maxItemHeight - this.height;
    if (canScroll) {
      if (Mouse.hasWheel() && isHovered(mouse)) {
        int scrollAmount = Mouse.getDWheel();
        if (scrollAmount != 0) {
          if (scrollAmount > 0) {
            scrollAmount = -1;
          } else {
            scrollAmount = 1;
          } 
          this.scrollAmount += (scrollAmount * 15);
          this.scrollAmount = Math.max(0.0F, Math.min(maxScroll, this.scrollAmount));
        } 
      } 
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.clipRect(this.pos, this.pos.clone().add(this.width, this.height), getScreen().getScale());
      GlStateManager.translate(0.0F, -this.scrollAmount, 0.0F);
    } 
    this.container.forEach(element -> element._draw(mouse.clone().add(0.0F, this.scrollAmount), partialTicks));
    if (canScroll) {
      GL11.glDisable(3089);
      GL11.glPopMatrix();
    } 
  }
  
  private float alignItems() {
    AtomicDouble yOffset = new AtomicDouble(0.0D);
    this.container.forEach(element -> element.offset = new Vec2f(this.itemMargin / 2.0F, this.itemMargin / 2.0F + (float)yOffset.getAndAdd((element.getHeight() + this.itemMargin))));
    return (float)yOffset.get();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\panel\ScrollPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */