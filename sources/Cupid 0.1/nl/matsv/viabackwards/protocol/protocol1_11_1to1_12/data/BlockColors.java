package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data;

public class BlockColors {
  private static final String[] COLORS = new String[16];
  
  static {
    COLORS[0] = "White";
    COLORS[1] = "Orange";
    COLORS[2] = "Magenta";
    COLORS[3] = "Light Blue";
    COLORS[4] = "Yellow";
    COLORS[5] = "Lime";
    COLORS[6] = "Pink";
    COLORS[7] = "Gray";
    COLORS[8] = "Light Gray";
    COLORS[9] = "Cyan";
    COLORS[10] = "Purple";
    COLORS[11] = "Blue";
    COLORS[12] = "Brown";
    COLORS[13] = "Green";
    COLORS[14] = "Red";
    COLORS[15] = "Black";
  }
  
  public static String get(int key) {
    return (key >= 0 && key < COLORS.length) ? COLORS[key] : "Unknown color";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_11_1to1_12\data\BlockColors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */