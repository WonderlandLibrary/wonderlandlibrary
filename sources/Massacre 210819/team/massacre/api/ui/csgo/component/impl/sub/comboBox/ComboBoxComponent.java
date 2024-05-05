package team.massacre.api.ui.csgo.component.impl.sub.comboBox;

import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import team.massacre.api.ui.csgo.SkeetUI;
import team.massacre.api.ui.framework.component.ButtonComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.ExpandableComponent;
import team.massacre.api.ui.framework.component.PredicateComponent;
import team.massacre.utils.OGLUtils;
import team.massacre.utils.RenderingUtils;
import team.massacre.utils.TTFFontRenderer;

public abstract class ComboBoxComponent extends ButtonComponent implements PredicateComponent, ExpandableComponent {
   private boolean expanded;

   public ComboBoxComponent(Component parent, float x, float y, float width, float height) {
      super(parent, x, y, width, height);
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
      float height = this.getHeight();
      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height), SkeetUI.getColor(855309));
      boolean hovered = this.isHovered(mouseX, mouseY);
      RenderingUtils.drawGradientRect((double)(x + 0.5F), (double)(y + 0.5F), (double)(x + width - 0.5F), (double)(y + height - 0.5F), false, SkeetUI.getColor(hovered ? RenderingUtils.darker(1973790, 1.4F) : 1973790), SkeetUI.getColor(hovered ? RenderingUtils.darker(2302755, 1.4F) : 2302755));
      GL11.glColor4f(0.6F, 0.6F, 0.6F, (float)SkeetUI.getAlpha() / 255.0F);
      RenderingUtils.drawAndRotateArrow(x + width - 5.0F, y + height / 2.0F - 0.5F, 3.0F, this.isExpanded());
      if (SkeetUI.shouldRenderText()) {
         GL11.glEnable(3089);
         OGLUtils.startScissorBox(lockedResolution, (int)x + 2, (int)y + 1, (int)width - 8, (int)height - 1);
         SkeetUI.FONT_RENDERER.drawString(this.getDisplayString(), x + 2.0F, y + height / 3.0F, SkeetUI.getColor(9868950));
         GL11.glDisable(3089);
      }

      if (this.expanded) {
         GL11.glTranslatef(0.0F, 0.0F, 2.0F);
         Enum<?>[] values = this.getValues();
         float dropDownHeight = (float)values.length * height;
         Gui.drawRect((double)x, (double)(y + height), (double)(x + width), (double)(y + height + dropDownHeight + 0.5F), SkeetUI.getColor(855309));
         float valueBoxHeight = height;
         Enum<?>[] enums = this.getValues();
         Enum[] var13 = enums;
         int var14 = enums.length;

         for(int var15 = 0; var15 < var14; ++var15) {
            Enum<?> value = var13[var15];
            boolean valueBoxHovered = (float)mouseX >= x && (float)mouseY >= y + valueBoxHeight && (float)mouseX <= x + width && (float)mouseY < y + valueBoxHeight + height;
            Gui.drawRect((double)(x + 0.5F), (double)(y + valueBoxHeight), (double)(x + width - 0.5F), (double)(y + valueBoxHeight + height), SkeetUI.getColor(valueBoxHovered ? RenderingUtils.darker(2302755, 0.7F) : 2302755));
            boolean selected;
            if (this.isMultiSelectable()) {
               selected = this.getMultiSelectValues().contains(value);
            } else {
               selected = value == this.getValue();
            }

            int color = selected ? SkeetUI.getColor() : SkeetUI.getColor(14474460);
            TTFFontRenderer fr;
            if (!selected && !valueBoxHovered) {
               fr = SkeetUI.FONT_RENDERER;
            } else {
               fr = SkeetUI.GROUP_BOX_HEADER_RENDERER;
            }

            fr.drawString(StringUtils.upperSnakeCaseToPascal(value.name()), x + 2.0F, y + valueBoxHeight + 4.0F, color);
            valueBoxHeight += height;
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
            if ((float)mouseX >= x && (float)mouseY >= y + valueBoxHeight && (float)mouseX <= x + width && (float)mouseY <= y + valueBoxHeight + height) {
               this.setValue(i);
               if (!this.isMultiSelectable()) {
                  this.expandOrClose();
               }

               return;
            }

            valueBoxHeight += height;
         }
      }

   }

   private void expandOrClose() {
      this.setExpanded(!this.isExpanded());
   }

   public void onPress(int mouseButton) {
      if (mouseButton == 1) {
         this.expandOrClose();
      }

   }

   public float getExpandedX() {
      return this.getX();
   }

   public float getExpandedY() {
      return this.getY();
   }

   public abstract Enum<?> getValue();

   public abstract void setValue(int var1);

   public abstract List<Enum<?>> getMultiSelectValues();

   public abstract boolean isMultiSelectable();

   public abstract Enum<?>[] getValues();

   public boolean isExpanded() {
      return this.expanded;
   }

   public void setExpanded(boolean expanded) {
      this.expanded = expanded;
   }

   public float getExpandedWidth() {
      return this.getWidth();
   }

   public float getExpandedHeight() {
      float height = this.getHeight();
      return height + (float)this.getValues().length * height + height;
   }
}
