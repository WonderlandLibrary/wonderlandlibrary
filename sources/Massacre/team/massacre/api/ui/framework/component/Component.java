package team.massacre.api.ui.framework.component;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import team.massacre.api.ui.framework.CustomFontRenderer;
import team.massacre.api.ui.main.MipMappedFontRenderer;

public class Component {
   protected static final CustomFontRenderer GROUP_BOX_FONT_RENDERER = new MipMappedFontRenderer(new Font("Tahoma", 1, 11), true);
   protected static final CustomFontRenderer BOLD_FONT_RENDERER = new MipMappedFontRenderer(new Font("Tahoma", 1, 11), true);
   protected static final CustomFontRenderer FONT_RENDERER = new MipMappedFontRenderer(new Font("Tahoma", 0, 11), true);
   protected final List<Component> children = new ArrayList();
   private final Component parent;
   private float x;
   private float y;
   private float width;
   private float height;

   public Component(Component parent, float x, float y, float width, float height) {
      this.parent = parent;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public Component getParent() {
      return this.parent;
   }

   public void addChild(Component child) {
      this.children.add(child);
   }

   public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
      Iterator var4 = this.children.iterator();

      while(var4.hasNext()) {
         Component child = (Component)var4.next();
         child.drawComponent(lockedResolution, mouseX, mouseY);
      }

   }

   public void onMouseClick(int mouseX, int mouseY, int button) {
      Iterator var4 = this.children.iterator();

      while(var4.hasNext()) {
         Component child = (Component)var4.next();
         child.onMouseClick(mouseX, mouseY, button);
      }

   }

   public void onMouseRelease(int button) {
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         Component child = (Component)var2.next();
         child.onMouseRelease(button);
      }

   }

   public void onKeyPress(int keyCode) {
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         Component child = (Component)var2.next();
         child.onKeyPress(keyCode);
      }

   }

   public float getX() {
      Component familyMember = this.parent;

      float familyTreeX;
      for(familyTreeX = this.x; familyMember != null; familyMember = familyMember.parent) {
         familyTreeX += familyMember.x;
      }

      return familyTreeX;
   }

   public void setX(float x) {
      this.x = x;
   }

   public boolean isHovered(int mouseX, int mouseY) {
      float x;
      float y;
      return (float)mouseX >= (x = this.getX()) && (float)mouseY >= (y = this.getY()) && (float)mouseX <= x + this.getWidth() && (float)mouseY <= y + this.getHeight();
   }

   public float getY() {
      Component familyMember = this.parent;

      float familyTreeY;
      for(familyTreeY = this.y; familyMember != null; familyMember = familyMember.parent) {
         familyTreeY += familyMember.y;
      }

      return familyTreeY;
   }

   public void setY(float y) {
      this.y = y;
   }

   public float getWidth() {
      return this.width;
   }

   public void setWidth(float width) {
      this.width = width;
   }

   public float getHeight() {
      return this.height;
   }

   public void setHeight(float height) {
      this.height = height;
   }

   public List<Component> getChildren() {
      return this.children;
   }
}
