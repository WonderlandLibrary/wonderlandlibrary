package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import java.util.HashMap;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class Windows extends StoredObject {
  public HashMap<Short, Short> types = new HashMap<>();
  
  public HashMap<Short, Furnace> furnace = new HashMap<>();
  
  public short levelCost = 0;
  
  public short anvilId = -1;
  
  public Windows(UserConnection user) {
    super(user);
  }
  
  public short get(short windowId) {
    return ((Short)this.types.getOrDefault(Short.valueOf(windowId), Short.valueOf((short)-1))).shortValue();
  }
  
  public void remove(short windowId) {
    this.types.remove(Short.valueOf(windowId));
    this.furnace.remove(Short.valueOf(windowId));
  }
  
  public static int getInventoryType(String name) {
    switch (name) {
      case "minecraft:container":
        return 0;
      case "minecraft:chest":
        return 0;
      case "minecraft:crafting_table":
        return 1;
      case "minecraft:furnace":
        return 2;
      case "minecraft:dispenser":
        return 3;
      case "minecraft:enchanting_table":
        return 4;
      case "minecraft:brewing_stand":
        return 5;
      case "minecraft:villager":
        return 6;
      case "minecraft:beacon":
        return 7;
      case "minecraft:anvil":
        return 8;
      case "minecraft:hopper":
        return 9;
      case "minecraft:dropper":
        return 10;
      case "EntityHorse":
        return 11;
    } 
    throw new IllegalArgumentException("Unknown type " + name);
  }
  
  public static class Furnace {
    public void setFuelLeft(short fuelLeft) {
      this.fuelLeft = fuelLeft;
    }
    
    public void setMaxFuel(short maxFuel) {
      this.maxFuel = maxFuel;
    }
    
    public void setProgress(short progress) {
      this.progress = progress;
    }
    
    public void setMaxProgress(short maxProgress) {
      this.maxProgress = maxProgress;
    }
    
    public boolean equals(Object o) {
      if (o == this)
        return true; 
      if (!(o instanceof Furnace))
        return false; 
      Furnace other = (Furnace)o;
      return !other.canEqual(this) ? false : ((getFuelLeft() != other.getFuelLeft()) ? false : ((getMaxFuel() != other.getMaxFuel()) ? false : ((getProgress() != other.getProgress()) ? false : (!(getMaxProgress() != other.getMaxProgress())))));
    }
    
    protected boolean canEqual(Object other) {
      return other instanceof Furnace;
    }
    
    public int hashCode() {
      int PRIME = 59;
      result = 1;
      result = result * 59 + getFuelLeft();
      result = result * 59 + getMaxFuel();
      result = result * 59 + getProgress();
      return result * 59 + getMaxProgress();
    }
    
    public String toString() {
      return "Windows.Furnace(fuelLeft=" + getFuelLeft() + ", maxFuel=" + getMaxFuel() + ", progress=" + getProgress() + ", maxProgress=" + getMaxProgress() + ")";
    }
    
    private short fuelLeft = 0;
    
    public short getFuelLeft() {
      return this.fuelLeft;
    }
    
    private short maxFuel = 0;
    
    public short getMaxFuel() {
      return this.maxFuel;
    }
    
    private short progress = 0;
    
    public short getProgress() {
      return this.progress;
    }
    
    private short maxProgress = 200;
    
    public short getMaxProgress() {
      return this.maxProgress;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\storage\Windows.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */