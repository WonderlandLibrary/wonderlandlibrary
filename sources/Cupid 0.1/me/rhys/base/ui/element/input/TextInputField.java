package me.rhys.base.ui.element.input;

import java.awt.Color;
import me.rhys.base.ui.element.Element;
import me.rhys.base.ui.element.mc.TextField;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class TextInputField extends Element {
  private static final int BACKGROUND_COLOR = ColorUtil.darken((new Color(27, 34, 44, 255)).getRGB(), 10).getRGB();
  
  private static final int SHADOW_COLOR = ColorUtil.lighten(BACKGROUND_COLOR, 25).getRGB();
  
  private final CustomTextField textField;
  
  public TextInputField(Vec2f offset, int width, int height) {
    super(offset, width, height);
    this.textField = new CustomTextField(0, 0, width, height);
  }
  
  public void clickMouse(Vec2f pos, int button) {
    this.textField.mouseClicked((int)pos.x, (int)pos.y, button);
  }
  
  public void typeKey(char keyChar, int keyCode) {
    this.textField.textboxKeyTyped(keyChar, keyCode);
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    this.textField.xPosition = (int)this.pos.x;
    this.textField.yPosition = (int)this.pos.y;
    this.textField.setWidth(this.width);
    this.textField.setHeight(this.height);
    RenderUtil.drawRect(this.pos.clone().sub(1.0F, 1.0F), this.width + 2, this.height + 2, SHADOW_COLOR);
    RenderUtil.drawRect(this.pos, this.width, this.height, BACKGROUND_COLOR);
    this.textField.drawTextBox();
  }
  
  public void setIsPassword(boolean isPassword) {
    this.textField.setPassword(isPassword);
  }
  
  public String getText() {
    return this.textField.getText();
  }
  
  private final class CustomTextField extends TextField {
    public CustomTextField(int x, int y, int width, int height) {
      super(0, (Minecraft.getMinecraft()).fontRendererObj, x, y, width, height);
    }
    
    public void drawTextBox() {
      if (getVisible()) {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.isPassword) {
          for (int m = 0; m < this.text.length(); m++)
            stringBuilder.append("*"); 
        } else {
          for (int m = 0; m < this.text.length(); m++)
            stringBuilder.append(this.text.charAt(m)); 
        } 
        int i = this.isEnabled ? this.enabledColor : this.disabledColor;
        int j = this.cursorPosition - this.lineScrollOffset;
        int k = this.selectionEnd - this.lineScrollOffset;
        String s = this.fontRendererInstance.trimStringToWidth(stringBuilder.substring(this.lineScrollOffset), getWidth());
        boolean flag = (j >= 0 && j <= s.length());
        boolean flag1 = (this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag);
        int l = this.enableBackgroundDrawing ? (this.xPosition + 4) : this.xPosition;
        int i1 = this.enableBackgroundDrawing ? (this.yPosition + (this.height - 8) / 2) : this.yPosition;
        int j1 = l;
        if (k > s.length())
          k = s.length(); 
        if (s.length() > 0) {
          String s1 = flag ? s.substring(0, j) : s;
          j1 = this.fontRendererInstance.drawStringWithShadow(s1, l, i1, i);
        } 
        boolean flag2 = (this.cursorPosition < this.text.length() || this.text.length() >= getMaxStringLength());
        int k1 = j1;
        if (!flag) {
          k1 = (j > 0) ? (l + this.width) : l;
        } else if (flag2) {
          k1 = j1 - 1;
          j1--;
        } 
        if (s.length() > 0 && flag && j < s.length())
          j1 = this.fontRendererInstance.drawStringWithShadow(s.substring(j), j1, i1, i); 
        if (flag1)
          if (flag2) {
            Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT, -3092272);
          } else {
            this.fontRendererInstance.drawStringWithShadow("_", k1, i1, i);
          }  
        if (k != j) {
          int l1 = l + this.fontRendererInstance.getStringWidth(s.substring(0, k));
          drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererInstance.FONT_HEIGHT);
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\input\TextInputField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */