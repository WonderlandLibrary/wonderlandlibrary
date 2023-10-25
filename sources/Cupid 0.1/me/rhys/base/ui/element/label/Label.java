package me.rhys.base.ui.element.label;

import me.rhys.base.ui.element.Element;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;

public class Label extends Element {
  private String label;
  
  private float scale;
  
  private boolean centered;
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public void setScale(float scale) {
    this.scale = scale;
  }
  
  public void setCentered(boolean centered) {
    this.centered = centered;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public float getScale() {
    return this.scale;
  }
  
  public boolean isCentered() {
    return this.centered;
  }
  
  public Label(String label, Vec2f offset) {
    super(offset, (int)FontUtil.getStringWidth(label), (int)FontUtil.getFontHeight());
    this.label = label;
    this.scale = 1.0F;
    this.centered = false;
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    if (this.scale != 1.0F) {
      GlStateManager.pushMatrix();
      GlStateManager.scale(this.scale, this.scale, this.scale);
    } 
    if (this.centered) {
      if (this.scale != 1.0F) {
        FontUtil.drawCenteredStringWithShadow(this.label, this.pos.div(this.scale, this.scale), (getScreen()).theme.labelColors.text);
      } else {
        FontUtil.drawCenteredStringWithShadow(this.label, this.pos, (getScreen()).theme.labelColors.text);
      } 
    } else if (this.scale != 1.0F) {
      FontUtil.drawStringWithShadow(this.label, this.pos.div(this.scale, this.scale), (getScreen()).theme.labelColors.text);
    } else {
      FontUtil.drawStringWithShadow(this.label, this.pos, (getScreen()).theme.labelColors.text);
    } 
    if (this.scale != 1.0F) {
      GlStateManager.scale(1.0F / this.scale, 1.0F / this.scale, 1.0F / this.scale);
      GlStateManager.popMatrix();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\label\Label.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */