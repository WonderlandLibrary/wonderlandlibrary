package team.massacre.api.ui.main.components.groupBox.sub;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import team.massacre.api.ui.framework.component.ButtonComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.PredicateComponent;
import team.massacre.api.ui.main.components.UIComponent;
import team.massacre.utils.RenderingUtils;

public abstract class CheckBoxComponent extends ButtonComponent implements PredicateComponent {
   private static final float CHECK_BOX_SIZE = 5.0F;
   private static final float MARGIN = 6.0F;
   private static final float TEXT_OFFSET = 1.5F;
   private final String name;

   public CheckBoxComponent(Component parent, String name) {
      super(parent, 10.0F, 0.0F, 11.0F + FONT_RENDERER.getWidth(name), FONT_RENDERER.getHeight(name) - 1.5F);
      this.name = name;
   }

   public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      Gui.drawRect((double)x, (double)y, (double)(x + 5.0F), (double)(y + 5.0F), -15921907);
      boolean checked = this.isChecked();
      boolean hovered = this.isHovered(mouseX, mouseY);
      RenderingUtils.drawGradientRect((double)(x + 0.5F), (double)(y + 0.5F), (double)(x + 5.0F - 0.5F), (double)(y + 5.0F - 0.5F), false, checked ? UIComponent.uiColor : (hovered ? RenderingUtils.darker(-12566464, 1.3F) : -12566464), checked ? RenderingUtils.darker(UIComponent.uiColor) : (hovered ? RenderingUtils.darker(-13224394, 1.3F) : -13224394));
      FONT_RENDERER.drawString(this.name, x + 5.0F + 6.0F, y - 1.5F, -1);
   }

   public void onPress(int mouseButton) {
      if (mouseButton == 0) {
         this.onChecked();
      }

   }

   public abstract void onChecked();

   public abstract boolean isChecked();
}
