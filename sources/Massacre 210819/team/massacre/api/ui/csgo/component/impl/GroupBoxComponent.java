package team.massacre.api.ui.csgo.component.impl;

import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import team.massacre.Massacre;
import team.massacre.api.ui.csgo.SkeetUI;
import team.massacre.api.ui.csgo.component.impl.sub.key.KeyBindComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.ExpandableComponent;
import team.massacre.api.ui.framework.component.PredicateComponent;

public final class GroupBoxComponent extends Component {
   private final String name;

   public GroupBoxComponent(Component parent, String name, float x, float y, float width, float height) {
      super(parent, x, y, width, height);
      this.name = name;
   }

   public void drawComponent(ScaledResolution resolution, int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      float height = this.getHeight();
      float length = SkeetUI.GROUP_BOX_HEADER_RENDERER.getWidth(this.name);
      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height), SkeetUI.getColor(789516));
      Gui.drawRect((double)(x + 0.5F), (double)(y + 0.5F), (double)(x + width - 0.5F), (double)(y + height - 0.5F), SkeetUI.getColor(2631720));
      Gui.drawRect((double)(x + 4.0F), (double)y, (double)(x + 4.0F + length + 2.0F), (double)(y + 1.0F), SkeetUI.getColor(1513239));
      Gui.drawRect((double)(x + 1.0F), (double)(y + 1.0F), (double)(x + width - 1.0F), (double)(y + height - 1.0F), SkeetUI.getColor(1513239));
      if (SkeetUI.shouldRenderText()) {
         Massacre.INSTANCE.getFontManager().getLatoRegularSmall().drawStringWithShadow(this.name, x + 5.0F, y - 0.5F, SkeetUI.getColor(14474460));
      }

      float childYLeft = 6.0F;
      float childYRight = 6.0F;
      boolean left = true;
      float right = 49.166668F;
      Iterator var13 = this.children.iterator();

      while(true) {
         Component component;
         while(true) {
            if (!var13.hasNext()) {
               return;
            }

            component = (Component)var13.next();
            if (component instanceof PredicateComponent) {
               PredicateComponent predicateComponent = (PredicateComponent)component;
               if (!predicateComponent.isVisible()) {
                  continue;
               }
            } else if (component instanceof KeyBindComponent) {
               continue;
            }
            break;
         }

         if (component.getWidth() >= 80.333336F) {
            component.setX(3.0F);
            component.setY(childYLeft);
            component.drawComponent(resolution, mouseX, mouseY);
            float yOffset = component.getHeight() + 4.0F;
            childYLeft += yOffset;
            childYRight += yOffset;
            left = true;
         } else {
            component.setX(left ? 3.0F : 49.166668F);
            component.setY(left ? childYLeft : childYRight);
            component.drawComponent(resolution, mouseX, mouseY);
            if (left) {
               childYLeft += component.getHeight() + 4.0F;
            } else {
               childYRight += component.getHeight() + 4.0F;
            }

            left = childYRight >= childYLeft;
         }
      }
   }

   public void onMouseClick(int mouseX, int mouseY, int button) {
      Component child;
      for(Iterator var4 = this.getChildren().iterator(); var4.hasNext(); child.onMouseClick(mouseX, mouseY, button)) {
         child = (Component)var4.next();
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

   public boolean isHoveredEntire(int mouseX, int mouseY) {
      Iterator var3 = this.getChildren().iterator();

      while(var3.hasNext()) {
         Component child = (Component)var3.next();
         if (child instanceof ExpandableComponent) {
            ExpandableComponent expandable = (ExpandableComponent)child;
            if (expandable.isExpanded()) {
               float x = expandable.getExpandedX();
               float y = expandable.getExpandedY();
               if ((float)mouseX >= x && (float)mouseY >= y && (float)mouseX <= x + expandable.getExpandedWidth() && (float)mouseY <= y + expandable.getExpandedHeight()) {
                  return true;
               }
            }
         }
      }

      return super.isHovered(mouseX, mouseY);
   }

   public float getHeight() {
      float initHeight = super.getHeight();
      float heightLeft = initHeight;
      float heightRight = initHeight;
      boolean left = true;
      Iterator var5 = this.getChildren().iterator();

      while(true) {
         Component component;
         PredicateComponent predicateComponent;
         do {
            if (!var5.hasNext()) {
               float heightWithComponents = Math.max(heightLeft, heightRight);
               return heightWithComponents - initHeight > initHeight ? heightWithComponents : initHeight;
            }

            component = (Component)var5.next();
            if (!(component instanceof PredicateComponent)) {
               break;
            }

            predicateComponent = (PredicateComponent)component;
         } while(!predicateComponent.isVisible());

         if (component.getWidth() >= 80.333336F) {
            float yOffset = component.getHeight() + 4.0F;
            heightLeft += yOffset;
            heightLeft += yOffset;
            left = true;
         } else {
            if (left) {
               heightLeft += component.getHeight() + 4.0F;
            } else {
               heightRight += component.getHeight() + 4.0F;
            }

            left = heightRight >= heightLeft;
         }
      }
   }
}
