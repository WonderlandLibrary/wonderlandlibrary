package team.massacre.api.ui.main.components.groupBox.sub;

import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import team.massacre.api.ui.framework.CustomFontRenderer;
import team.massacre.api.ui.framework.component.ButtonComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.ExpandableComponent;
import team.massacre.api.ui.framework.component.PredicateComponent;
import team.massacre.api.ui.main.components.UIComponent;
import team.massacre.utils.OGLUtils;
import team.massacre.utils.RenderingUtils;

public abstract class ComboBoxComponent extends ButtonComponent implements PredicateComponent, ExpandableComponent {
   private final String name;
   private boolean expanded;
   private static final float COMBO_BOX_HEIGHT = 10.0F;
   private static final float MARGIN = 1.0F;

   public ComboBoxComponent(Component parent, String name) {
      super(parent, 20.0F, 0.0F, 0.0F, FONT_RENDERER.getHeight(name) + 1.0F + 10.0F);
      this.name = name;
   }

   private String getDisplayString() {
      if (this.isMultiSelectable()) {
         List<Enum<?>> values = this.getMultiSelectValues();
         int len = values.size();
         if (len == 0) {
            return "-";
         } else if (len == 1) {
            return StringUtils.upperSnakeCaseToPascal(((Enum)values.get(0)).name());
         } else {
            StringBuilder sb = (new StringBuilder(StringUtils.upperSnakeCaseToPascal(((Enum)values.get(0)).name()))).append(", ");

            for(int i = 1; i < len; ++i) {
               sb.append(StringUtils.upperSnakeCaseToPascal(((Enum)values.get(i)).name()));
               if (i != len - 1) {
                  sb.append(", ");
               }
            }

            return sb.toString();
         }
      } else {
         return StringUtils.upperSnakeCaseToPascal(this.getValue().name());
      }
   }

   public void drawComponent(ScaledResolution lockedResolution, int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      float comboBoxStart = FONT_RENDERER.getHeight(this.name) + 1.0F;
      Gui.drawRect((double)(x - 0.5F), (double)(y + comboBoxStart), (double)(x + width), (double)(y + comboBoxStart + 10.0F), -15921907);
      boolean hovered = this.isHovered(mouseX, mouseY);
      RenderingUtils.drawGradientRect((double)x, (double)(y + comboBoxStart + 0.5F), (double)(x + width - 0.5F), (double)(y + comboBoxStart + 10.0F - 0.5F), false, hovered ? RenderingUtils.darker(-14803426, 1.3F) : -14803426, hovered ? RenderingUtils.darker(-14474461, 1.3F) : -14474461);
      float scissorWidth = width - 6.0F;
      String displayString = this.getDisplayString();
      float stringWidth = FONT_RENDERER.getWidth(displayString);
      boolean needScissor = stringWidth >= scissorWidth;
      boolean isScissoring = false;
      if (needScissor) {
         isScissoring = GL11.glIsEnabled(3089);
         if (!isScissoring) {
            GL11.glEnable(3089);
         }

         OGLUtils.startScissorBox(lockedResolution, (int)x, (int)(y + comboBoxStart), (int)scissorWidth, 10);
      }

      FONT_RENDERER.drawString(this.getDisplayString(), x + 2.0F, y + comboBoxStart + 1.0F, -6908266);
      if (needScissor && !isScissoring) {
         GL11.glDisable(3089);
      }

      RenderingUtils.drawAndRotateArrow(x + width - 5.0F, y + comboBoxStart + 5.0F - 0.5F, 3.0F, this.isExpanded());
      FONT_RENDERER.drawString(this.name, x, y, -1);
      if (this.isExpanded()) {
         GL11.glTranslatef(0.0F, 0.0F, 2.0F);
         Enum<?>[] values = this.getValues();
         float dropDownHeight = (float)values.length * 10.0F;
         Gui.drawRect((double)(x - 0.5F), (double)(y + comboBoxStart + 10.0F), (double)(x + width), (double)(y + comboBoxStart + 10.0F + dropDownHeight + 0.5F), -15921907);
         float valueBoxHeight = 10.0F;
         Enum<?>[] enums = this.getValues();
         Enum[] var18 = enums;
         int var19 = enums.length;

         for(int var20 = 0; var20 < var19; ++var20) {
            Enum<?> value = var18[var20];
            boolean valueBoxHovered = (float)mouseX >= x && (float)mouseY >= y + comboBoxStart + valueBoxHeight && (float)mouseX <= x + width && (float)mouseY < y + comboBoxStart + valueBoxHeight + 10.0F;
            Gui.drawRect((double)x, (double)(y + comboBoxStart + valueBoxHeight), (double)(x + width - 0.5F), (double)(y + comboBoxStart + valueBoxHeight + 10.0F), valueBoxHovered ? RenderingUtils.darker(-14474461, 0.7F) : -14474461);
            boolean selected;
            if (this.isMultiSelectable()) {
               selected = this.getMultiSelectValues().contains(value);
            } else {
               selected = value == this.getValue();
            }

            int color = selected ? UIComponent.uiColor : -2302756;
            CustomFontRenderer fr;
            if (!selected && !valueBoxHovered) {
               fr = FONT_RENDERER;
            } else {
               fr = BOLD_FONT_RENDERER;
            }

            fr.drawString(StringUtils.upperSnakeCaseToPascal(value.name()), x + 2.0F, y + comboBoxStart + valueBoxHeight + 2.0F, color);
            valueBoxHeight += 10.0F;
         }

         GL11.glTranslatef(0.0F, 0.0F, -2.0F);
      }

   }

   public void onMouseClick(int mouseX, int mouseY, int button) {
      if (this.isHovered(mouseX, mouseY)) {
         this.onPress(button);
      }

      if (this.isExpanded() && button == 0) {
         float x = this.getX();
         float y = this.getY();
         float height = this.getHeight();
         float width = this.getWidth();
         float valueBoxHeight = height;

         for(int i = 0; i < this.getValues().length; ++i) {
            if ((float)mouseX >= x && (float)mouseY >= y + valueBoxHeight && (float)mouseX <= x + width && (float)mouseY <= y + valueBoxHeight + 10.0F) {
               this.setValue(i);
               if (!this.isMultiSelectable()) {
                  this.setExpanded(!this.isExpanded());
               }

               return;
            }

            valueBoxHeight += 10.0F;
         }
      }

   }

   public boolean isHovered(int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      float comboBoxStart = FONT_RENDERER.getHeight(this.name) + 1.0F;
      return (float)mouseX > x && (float)mouseX < x + width && (float)mouseY > y + comboBoxStart && (float)mouseY < y + comboBoxStart + 10.0F;
   }

   public void onPress(int mouseButton) {
      if (mouseButton == 1) {
         this.setExpanded(!this.isExpanded());
      }

   }

   public float getWidth() {
      Component parent = this.getParent();
      return parent.getWidth() - 40.0F;
   }

   public float getExpandedX() {
      return this.getX();
   }

   public float getExpandedY() {
      return this.getY();
   }

   public float getExpandedWidth() {
      return this.getWidth();
   }

   public float getExpandedHeight() {
      return this.getHeight() + (float)this.getValues().length * 10.0F;
   }

   public abstract Enum<?> getValue();

   public abstract void setValue(int var1);

   public abstract List<Enum<?>> getMultiSelectValues();

   public abstract boolean isMultiSelectable();

   public abstract Enum<?>[] getValues();

   public void setExpanded(boolean expanded) {
      this.expanded = expanded;
   }

   public boolean isExpanded() {
      return this.expanded;
   }
}
