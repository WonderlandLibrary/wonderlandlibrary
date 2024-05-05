package team.massacre.utils;

import java.awt.Color;

public class ColorUtil {
   public double rainbowState;

   public Color rainbow(int delay) {
      this.rainbowState = Math.ceil((double)((System.currentTimeMillis() + (long)delay) / 75L));
      this.rainbowState %= 90.0D;
      return Color.getHSBColor((float)(this.rainbowState / 45.0D), 0.5F, 1.0F);
   }

   public Color getGradientOffset(Color color1, Color color2, double offset) {
      double inverse_percent;
      int redPart;
      if (offset > 1.0D) {
         inverse_percent = offset % 1.0D;
         redPart = (int)offset;
         offset = redPart % 2 == 0 ? inverse_percent : 1.0D - inverse_percent;
      }

      inverse_percent = 1.0D - offset;
      redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
      int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
      int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
      return new Color(redPart, greenPart, bluePart);
   }
}
