package me.rhys.base.ui.element.button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import me.rhys.base.ui.UIScreen;
import me.rhys.base.util.render.ColorUtil;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import me.rhys.base.util.vec.Vec2f;

public class DropDownButton extends Button {
  protected final List<String> items;
  
  private boolean expanded;
  
  protected int current;
  
  private int expandHeight;
  
  public DropDownButton(Vec2f offset, int width, int height, String... itms) {
    super(itms[0], offset, width, height);
    this.items = new ArrayList<>();
    Arrays.<String>stream(itms).sorted(Comparator.comparingInt(value -> value.charAt(0))).forEach(this.items::add);
    this.expanded = false;
    this.current = 0;
    this.expandHeight = 105;
  }
  
  public DropDownButton(Vec2f offset, int width, int height, List<String> items) {
    super(items.get(0), offset, width, height);
    this.items = items;
    items.sort(Comparator.comparingInt(value -> value.charAt(0)));
    this.expanded = false;
    this.current = 0;
    this.expandHeight = 100;
  }
  
  public void setCurrent(String current) {
    if (this.items.contains(current)) {
      this.label = current;
      this.current = this.items.indexOf(current);
    } 
  }
  
  public void clickMouse(Vec2f pos, int button) {
    if (pos.x >= this.pos.x && pos.y >= this.pos.y && pos.x <= this.pos.x + this.width && pos.y <= this.pos.y + (this.height + (this.expanded ? (this.expandHeight + 1) : 0)) && pos.y >= this.pos.y + this.height && this.expanded) {
      Vec2f expandPos = this.pos.clone().add(0, this.height + 1);
      List<String> cleaned = new ArrayList<>();
      this.items.stream().filter(s -> !s.equalsIgnoreCase(this.label)).sorted(Comparator.comparingInt(value -> value.charAt(0))).forEachOrdered(cleaned::add);
      float yOffset = 5.0F;
      for (String item : cleaned) {
        Vec2f itemPos = expandPos.clone().add(10.0F, yOffset);
        if (pos.x >= itemPos.x && pos.y >= itemPos.y && pos.x <= itemPos.x + 
          
          FontUtil.getStringWidth(item) && pos.y <= itemPos.y + 
          FontUtil.getFontHeight()) {
          setCurrent(item);
          break;
        } 
        yOffset += FontUtil.getFontHeight() + 5.0F;
      } 
    } else if (button == 0 && this.items.size() > 1) {
      this.expanded = !this.expanded;
    } 
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    UIScreen screen = getScreen();
    if (this.background == ColorUtil.Colors.TRANSPARENT.getColor()) {
      RenderUtil.drawRect(this.pos.clone().sub(1.0F, 1.0F), this.width + 2, this.height + 2, ColorUtil.darken(screen.theme.buttonColors.background, 10).getRGB());
      RenderUtil.drawRect(this.pos, this.width, this.height, screen.theme.buttonColors.background);
      RenderUtil.drawRect(this.pos.clone().add(this.width - FontUtil.getStringWidth("☰") - 15.0F, 0.0F), (int)(FontUtil.getStringWidth("☰") + 15.0F), this.height, screen.theme.buttonColors.background);
    } 
    FontUtil.drawStringWithShadow(String.valueOf(this.label.charAt(0)).toUpperCase() + this.label.substring(1).toLowerCase(), this.pos.clone().add(10.0F, (this.height - FontUtil.getFontHeight()) / 2.0F), screen.theme.buttonColors.text);
    FontUtil.drawStringWithShadow("☰", this.pos.clone().add(this.width - FontUtil.getStringWidth("☰") - 10.0F, (this.height - FontUtil.getFontHeight()) / 2.0F - 1.0F), -1);
    this.expandHeight = (int)((this.items.size() - 1) * (FontUtil.getFontHeight() + 5.0F) + 5.0F);
    if (this.expanded) {
      Vec2f expandPos = this.pos.clone().add(0, this.height + 1);
      RenderUtil.drawRect(expandPos, this.width, this.expandHeight, screen.theme.buttonColors.background);
      List<String> cleaned = new ArrayList<>();
      this.items.stream().filter(s -> !s.equalsIgnoreCase(this.label)).sorted(Comparator.comparingInt(value -> value.charAt(0))).forEachOrdered(cleaned::add);
      float yOffset = 5.0F;
      for (String item : cleaned) {
        FontUtil.drawStringWithShadow(String.valueOf(item.charAt(0)).toUpperCase() + item.substring(1).toLowerCase(), expandPos.clone().add(10.0F, yOffset), -1);
        yOffset += FontUtil.getFontHeight() + 5.0F;
      } 
    } 
  }
  
  public boolean isHovered(Vec2f mouse) {
    return (mouse.x >= this.pos.x && mouse.y >= this.pos.y && mouse.x <= this.pos.x + this.width && mouse.y <= this.pos.y + (this.height + (this.expanded ? (this.expandHeight + 1) : 0)));
  }
  
  public int getHeight() {
    return this.height + (this.expanded ? (this.expandHeight + 1) : 0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\ui\element\button\DropDownButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */