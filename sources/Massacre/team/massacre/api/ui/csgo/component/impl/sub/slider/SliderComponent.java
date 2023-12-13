package team.massacre.api.ui.csgo.component.impl.sub.slider;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import team.massacre.Massacre;
import team.massacre.api.property.Representation;
import team.massacre.api.ui.csgo.SkeetUI;
import team.massacre.api.ui.framework.component.ButtonComponent;
import team.massacre.api.ui.framework.component.Component;
import team.massacre.api.ui.framework.component.PredicateComponent;
import team.massacre.utils.RenderingUtils;

public abstract class SliderComponent extends ButtonComponent implements PredicateComponent {
   private boolean sliding;

   public SliderComponent(Component parent, float x, float y, float width, float height) {
      super(parent, x, y, width, height);
   }

   public void drawComponent(ScaledResolution resolution, int mouseX, int mouseY) {
      float x = this.getX();
      float y = this.getY();
      float width = this.getWidth();
      float height = this.getHeight();
      double min = this.getMin();
      double max = this.getMax();
      double dValue = this.getValue();
      Representation representation = this.getRepresentation();
      boolean isInt = representation == Representation.MILLISECONDS;
      double value;
      if (isInt) {
         value = (double)((int)dValue);
      } else {
         value = dValue;
      }

      boolean hovered = this.isHovered(mouseX, mouseY);
      if (this.sliding) {
         if ((float)mouseX >= x - 0.5F && (float)mouseY >= y - 0.5F && (float)mouseX <= x + width + 0.5F && (float)mouseY <= y + height + 0.5F) {
            this.setValue(MathHelper.clamp_double(this.roundToIncrement((double)((float)mouseX - x) * (max - min) / (double)(width - 1.0F) + min), min, max));
         } else {
            this.sliding = false;
         }
      }

      double sliderPercentage = (value - min) / (max - min);
      String valueString;
      if (isInt) {
         valueString = Integer.toString((int)value);
      } else {
         DecimalFormat format = new DecimalFormat("####.##");
         valueString = format.format(value);
      }

      switch(representation) {
      case PERCENTAGE:
         valueString = valueString + "%";
         break;
      case MILLISECONDS:
         valueString = valueString + "ms";
         break;
      case DISTANCE:
         valueString = valueString + "m";
         break;
      case DEGREES:
         valueString = valueString + "Â°";
         break;
      case PIXELS:
         valueString = valueString + "px";
      }

      Gui.drawRect((double)x, (double)y, (double)(x + width), (double)(y + height), SkeetUI.getColor(855309));
      RenderingUtils.drawGradientRect((double)(x + 0.5F), (double)(y + 0.5F), (double)(x + width - 0.5F), (double)(y + height - 0.5F), false, SkeetUI.getColor(hovered ? RenderingUtils.darker(4802889, 1.4F) : 4802889), SkeetUI.getColor(hovered ? RenderingUtils.darker(3158064, 1.4F) : 3158064));
      RenderingUtils.drawGradientRect((double)(x + 0.5F), (double)(y + 0.5F), (double)x + (double)width * sliderPercentage - 0.5D, (double)(y + height - 0.5F), false, SkeetUI.getColor(), RenderingUtils.darker(SkeetUI.getColor(), 0.8F));
      if (SkeetUI.shouldRenderText()) {
         float stringWidth = SkeetUI.GROUP_BOX_HEADER_RENDERER.getWidth(valueString);
         GL11.glTranslatef(0.0F, 0.0F, 1.0F);
         if (SkeetUI.getAlpha() > 120.0D) {
            Massacre.INSTANCE.getFontManager().getLatoRegularSmall().drawStringWithShadow(valueString, x + width * (float)sliderPercentage - stringWidth / 2.0F, y + height / 2.0F + 2.0F, SkeetUI.getColor(16777215));
         } else {
            SkeetUI.GROUP_BOX_HEADER_RENDERER.drawString(valueString, x + width * (float)sliderPercentage - stringWidth / 2.0F, y + height / 2.0F, SkeetUI.getColor(16777215));
         }

         GL11.glTranslatef(0.0F, 0.0F, -1.0F);
      }

   }

   public void onPress(int mouseButton) {
      if (!this.sliding && mouseButton == 0) {
         this.sliding = true;
      }

   }

   public void onMouseRelease(int button) {
      this.sliding = false;
   }

   private double roundToIncrement(double value) {
      double inc = this.getIncrement();
      double halfOfInc = inc / 2.0D;
      double floored = StrictMath.floor(value / inc) * inc;
      return value >= floored + halfOfInc ? (new BigDecimal(StrictMath.ceil(value / inc) * inc)).setScale(2, 4).doubleValue() : (new BigDecimal(floored)).setScale(2, 4).doubleValue();
   }

   public abstract double getValue();

   public abstract void setValue(double var1);

   public abstract Representation getRepresentation();

   public abstract double getMin();

   public abstract double getMax();

   public abstract double getIncrement();
}
