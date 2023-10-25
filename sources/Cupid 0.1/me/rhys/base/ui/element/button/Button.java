package me.rhys.base.ui.element.button;

import me.rhys.base.ui.UIScreen;
import me.rhys.base.ui.element.Element;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class Button extends Element {
  protected String label;
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public Button(String label, Vec2f offset, int width, int height) {
    super(offset, width, height);
    this.label = label;
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    UIScreen screen = getScreen();
    if (this.background == ColorUtil.Colors.TRANSPARENT.getColor())
      RenderUtil.drawRect(this.pos, this.width, this.height, screen.theme.buttonColors.background); 
    FontUtil.drawCenteredStringWithShadow(this.label, this.pos.clone().add(this.width / 2.0F, this.height / 2.0F), screen.theme.buttonColors.text);
  }
  
  public void playSound() {
    Minecraft.getMinecraft().getSoundHandler().playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\button\Button.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */