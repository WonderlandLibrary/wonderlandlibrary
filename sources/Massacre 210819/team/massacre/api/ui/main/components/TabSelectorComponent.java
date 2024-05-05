package team.massacre.api.ui.main.components;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import team.massacre.api.ui.framework.component.ButtonComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.main.components.tab.Tab;
import team.massacre.utils.OGLUtils;
import team.massacre.utils.RenderingUtils;

public final class TabSelectorComponent extends Component {
   private final float buttonSize;
   private final float margin = 10.0F;
   private Tab selected;
   private double selectorPos;

   public TabSelectorComponent(Component parent, float width, float height) {
      super(parent, 0.0F, 0.0F, width, height);
      Tab[] tabs = Tab.values();
      this.selected = tabs[0];
      this.getClass();
      float pos = 10.0F;
      this.getClass();
      this.buttonSize = (height - 10.0F * 2.0F) / (float)tabs.length;
      Tab[] var6 = tabs;
      int var7 = tabs.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         final Tab tab = var6[var8];
         this.addChild(new ButtonComponent(this, 0.0F, pos, width, this.buttonSize) {
            public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
               float imageSize = tab.getRenderSize();
               OGLUtils.color(TabSelectorComponent.this.selected == tab ? -2960686 : -10855846);
               tab.getIcon().draw(this.getX() + (this.getWidth() - imageSize) / 2.0F, this.getY() + (this.getHeight() - imageSize) / 2.0F, imageSize, imageSize);
            }

            public void onPress(int mouseButton) {
               if (mouseButton == 0) {
                  TabSelectorComponent.this.selected = tab;
               }

            }
         });
         pos += this.buttonSize;
      }

   }

   public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
      List<Component> children = this.getChildren();
      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      float height = this.getHeight();
      this.getClass();
      float buttonSize = (height - 10.0F * 2.0F) / (float)children.size() - this.buttonSize;
      int innerColor = -16382458;
      int outerColor = -14671840;
      int backgroundColor = -15987700;
      int selectedIndex = this.selected.ordinal();
      float totalSize = this.buttonSize + buttonSize;
      double var10001 = this.selectorPos;
      this.getClass();
      this.selectorPos = RenderingUtils.progressiveAnimation(var10001, (double)(10.0F + (float)selectedIndex * totalSize), 1.0D);
      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)y + this.selectorPos, -15987700);
      Gui.drawRect((double)(x + width - 1.0F), (double)y, (double)(x + width), (double)y + this.selectorPos, -16382458);
      Gui.drawRect((double)(x + width - 0.5F), (double)y, (double)(x + width), (double)y + this.selectorPos, -14671840);
      Gui.drawRect((double)x, (double)y + this.selectorPos - 1.0D, (double)(x + width - 0.5F), (double)y + this.selectorPos, -16382458);
      Gui.drawRect((double)x, (double)y + this.selectorPos - 0.5D, (double)(x + width), (double)y + this.selectorPos, -14671840);
      Gui.drawRect((double)x, (double)y + this.selectorPos + (double)this.buttonSize, (double)(x + width), (double)(y + height), -15987700);
      Gui.drawRect((double)(x + width - 1.0F), (double)y + this.selectorPos + (double)this.buttonSize, (double)(x + width), (double)(y + height), -16382458);
      Gui.drawRect((double)(x + width - 0.5F), (double)y + this.selectorPos + (double)this.buttonSize, (double)(x + width), (double)(y + height), -14671840);
      Gui.drawRect((double)x, (double)y + this.selectorPos + (double)this.buttonSize, (double)(x + width - 0.5F), (double)y + this.selectorPos + (double)this.buttonSize + 1.0D, -16382458);
      Gui.drawRect((double)x, (double)y + this.selectorPos + (double)this.buttonSize, (double)(x + width), (double)y + this.selectorPos + (double)this.buttonSize + 0.5D, -14671840);
      this.getClass();
      float pos = 10.0F;

      for(Iterator var16 = children.iterator(); var16.hasNext(); pos += totalSize) {
         Component child = (Component)var16.next();
         child.setY(pos);
         child.drawComponent(lockedResolution, mouseX, mouseY);
      }

   }

   public Tab getSelected() {
      return this.selected;
   }
}
