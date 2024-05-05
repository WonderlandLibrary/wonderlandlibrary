package team.massacre.api.ui.csgo.component;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import team.massacre.api.ui.csgo.SkeetUI;
import team.massacre.api.ui.csgo.component.impl.GroupBoxComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.ExpandableComponent;

public abstract class TabComponent extends Component {
   private final String name;

   public TabComponent(Component parent, String name, float x, float y, float width, float height) {
      super(parent, x, y, width, height);
      this.setupChildren();
      this.name = name;
   }

   public abstract void setupChildren();

   public void drawComponent(ScaledResolution resolution, int mouseX, int mouseY) {
      SkeetUI.FONT_RENDERER.drawStringWithShadow(this.name, this.getX() + 8.0F, this.getY() + 8.0F - 3.0F, SkeetUI.getColor(16777215));
      float x = 8.0F;
      List<Component> children = this.getChildren();

      for(int i = 0; i < children.size(); ++i) {
         Component child = (Component)children.get(i);
         child.setX(x);
         if (i < 3) {
            child.setY(14.0F);
         }

         child.drawComponent(resolution, mouseX, mouseY);
         x += 102.333336F;
         if (x + 8.0F + 94.333336F > 315.0F) {
            x = 8.0F;
         }

         if (i > 2) {
            int above = i - 3;
            int totalY = 14;

            do {
               Component componentAbove = (Component)this.getChildren().get(above);
               totalY = (int)((float)totalY + componentAbove.getHeight() + 8.0F);
               above -= 3;
            } while(above >= 0);

            child.setY((float)totalY);
         }
      }

   }

   public void onMouseClick(int mouseX, int mouseY, int button) {
      Iterator var4 = this.getChildren().iterator();

      Component child;
      while(var4.hasNext()) {
         child = (Component)var4.next();
         Iterator var6 = child.getChildren().iterator();

         while(var6.hasNext()) {
            Component child = (Component)var6.next();
            if (child instanceof ExpandableComponent) {
               ExpandableComponent expandable = (ExpandableComponent)child;
               if (expandable.isExpanded()) {
                  float x = expandable.getExpandedX();
                  float y = expandable.getExpandedY();
                  if ((float)mouseX >= x && (float)mouseY > y && (float)mouseX <= x + expandable.getExpandedWidth() && (float)mouseY < y + expandable.getExpandedHeight()) {
                     child.onMouseClick(mouseX, mouseY, button);
                     return;
                  }
               }
            }
         }
      }

      var4 = this.getChildren().iterator();

      do {
         if (!var4.hasNext()) {
            super.onMouseClick(mouseX, mouseY, button);
            return;
         }

         child = (Component)var4.next();
      } while(!child.isHovered(mouseX, mouseY));

      child.onMouseClick(mouseX, mouseY, button);
   }

   public boolean isHovered(int mouseX, int mouseY) {
      Iterator var3 = this.getChildren().iterator();

      while(var3.hasNext()) {
         Component child = (Component)var3.next();
         if (child instanceof GroupBoxComponent) {
            GroupBoxComponent groupBox = (GroupBoxComponent)child;
            if (groupBox.isHoveredEntire(mouseX, mouseY)) {
               return true;
            }
         }
      }

      return super.isHovered(mouseX, mouseY);
   }
}
