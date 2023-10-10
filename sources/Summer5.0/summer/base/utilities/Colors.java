package summer.base.utilities;

import java.awt.Color;

public enum Colors {
  BLACK(-16711423),
  BLUE(-12028161),
  DARKBLUE(-12621684),
  GREEN(-9830551),
  DARKGREEN(-9320847),
  WHITE(-65794),
  AQUA(-7820064),
  DARKAQUA(-12621684),
  GREY(-9868951),
  DARKGREY(-14342875),
  RED(-65536),
  DARKRED(-8388608),
  ORANGE(-29696),
  DARKORANGE(-2263808),
  YELLOW(-256),
  DARKYELLOW(-2702025),
  MAGENTA(-18751),
  DARKMAGENTA(-2252579);
  
  public int c;
  
  Colors(int co) {
    this.c = co;
  }
  
  public static int getColor(Color color) {
    return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }
  
  public static int getColor(int brightness) {
    return getColor(brightness, brightness, brightness, 255);
  }
  
  public static int getColor(int brightness, int alpha) {
    return getColor(brightness, brightness, brightness, alpha);
  }
  
  public static int getColor(int red, int green, int blue) {
    return getColor(red, green, blue, 255);
  }
  
  public static int getColor(int red, int green, int blue, int alpha) {
    int color = 0;
    color |= alpha << 24;
    color |= red << 16;
    color |= green << 8;
    color |= blue;
    return color;
  }
}

