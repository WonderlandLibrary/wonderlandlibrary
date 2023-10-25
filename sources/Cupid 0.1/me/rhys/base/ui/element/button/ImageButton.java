package me.rhys.base.ui.element.button;

import java.awt.Color;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImageButton extends Button {
  private final ResourceLocation image;
  
  private final int imgWidth;
  
  private final int imgHeight;
  
  private float scale;
  
  private Color hoverColor;
  
  public float getScale() {
    return this.scale;
  }
  
  public void setScale(float scale) {
    this.scale = scale;
  }
  
  public ImageButton(ResourceLocation image, Vec2f offset, int width, int height, int imgWidth, int imgHeight) {
    super("", offset, width, height);
    this.image = image;
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
    this.scale = 1.225F;
  }
  
  public ImageButton(ResourceLocation image, Vec2f offset, int width, int height, int imgWidth, int imgHeight, Color color) {
    super("", offset, width, height);
    this.image = image;
    this.imgWidth = imgWidth;
    this.imgHeight = imgHeight;
    this.scale = 1.225F;
    this.hoverColor = color;
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    UIScreen screen = getScreen();
    if (this.background == ColorUtil.Colors.TRANSPARENT.getColor())
      RenderUtil.drawRect(this.pos, this.width, this.height, screen.theme.buttonColors.background); 
    GlStateManager.pushMatrix();
    GlStateManager.scale(this.scale, this.scale, this.scale);
    RenderUtil.drawImage(this.image, this.pos.clone().add((this.width - this.imgWidth) / 2.0F, (this.height - this.imgHeight) / 2.0F).div(this.scale, this.scale), this.imgWidth, this.imgHeight);
    GlStateManager.popMatrix();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\button\ImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */