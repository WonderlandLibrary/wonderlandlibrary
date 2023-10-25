package net.minecraft.inventory;

import net.minecraft.util.IChatComponent;

public class AnimalChest extends InventoryBasic {
  public AnimalChest(String inventoryName, int slotCount) {
    super(inventoryName, false, slotCount);
  }
  
  public AnimalChest(IChatComponent invTitle, int slotCount) {
    super(invTitle, slotCount);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\inventory\AnimalChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */