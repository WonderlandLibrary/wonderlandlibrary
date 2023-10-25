package net.minecraft.util;

public enum EnumWorldBlockLayer {
  SOLID("Solid"),
  CUTOUT_MIPPED("Mipped Cutout"),
  CUTOUT("Cutout"),
  TRANSLUCENT("Translucent");
  
  private final String layerName;
  
  EnumWorldBlockLayer(String layerNameIn) {
    this.layerName = layerNameIn;
  }
  
  public String toString() {
    return this.layerName;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\EnumWorldBlockLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */