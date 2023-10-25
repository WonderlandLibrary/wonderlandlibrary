package net.minecraft.block.material;

public class MaterialPortal extends Material {
  public MaterialPortal(MapColor color) {
    super(color);
  }
  
  public boolean isSolid() {
    return false;
  }
  
  public boolean blocksLight() {
    return false;
  }
  
  public boolean blocksMovement() {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\material\MaterialPortal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */