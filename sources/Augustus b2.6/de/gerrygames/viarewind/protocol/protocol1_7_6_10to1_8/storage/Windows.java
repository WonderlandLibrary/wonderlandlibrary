// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.HashMap;
import com.viaversion.viaversion.api.connection.StoredObject;

public class Windows extends StoredObject
{
    public HashMap<Short, Short> types;
    public HashMap<Short, Furnace> furnace;
    public short levelCost;
    public short anvilId;
    
    public Windows(final UserConnection user) {
        super(user);
        this.types = new HashMap<Short, Short>();
        this.furnace = new HashMap<Short, Furnace>();
        this.levelCost = 0;
        this.anvilId = -1;
    }
    
    public short get(final short windowId) {
        return this.types.getOrDefault(windowId, (Short)(-1));
    }
    
    public void remove(final short windowId) {
        this.types.remove(windowId);
        this.furnace.remove(windowId);
    }
    
    public static int getInventoryType(final String name) {
        switch (name) {
            case "minecraft:container": {
                return 0;
            }
            case "minecraft:chest": {
                return 0;
            }
            case "minecraft:crafting_table": {
                return 1;
            }
            case "minecraft:furnace": {
                return 2;
            }
            case "minecraft:dispenser": {
                return 3;
            }
            case "minecraft:enchanting_table": {
                return 4;
            }
            case "minecraft:brewing_stand": {
                return 5;
            }
            case "minecraft:villager": {
                return 6;
            }
            case "minecraft:beacon": {
                return 7;
            }
            case "minecraft:anvil": {
                return 8;
            }
            case "minecraft:hopper": {
                return 9;
            }
            case "minecraft:dropper": {
                return 10;
            }
            case "EntityHorse": {
                return 11;
            }
            default: {
                throw new IllegalArgumentException("Unknown type " + name);
            }
        }
    }
    
    public static class Furnace
    {
        private short fuelLeft;
        private short maxFuel;
        private short progress;
        private short maxProgress;
        
        public Furnace() {
            this.fuelLeft = 0;
            this.maxFuel = 0;
            this.progress = 0;
            this.maxProgress = 200;
        }
        
        public short getFuelLeft() {
            return this.fuelLeft;
        }
        
        public short getMaxFuel() {
            return this.maxFuel;
        }
        
        public short getProgress() {
            return this.progress;
        }
        
        public short getMaxProgress() {
            return this.maxProgress;
        }
        
        public void setFuelLeft(final short fuelLeft) {
            this.fuelLeft = fuelLeft;
        }
        
        public void setMaxFuel(final short maxFuel) {
            this.maxFuel = maxFuel;
        }
        
        public void setProgress(final short progress) {
            this.progress = progress;
        }
        
        public void setMaxProgress(final short maxProgress) {
            this.maxProgress = maxProgress;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Furnace)) {
                return false;
            }
            final Furnace other = (Furnace)o;
            return other.canEqual(this) && this.getFuelLeft() == other.getFuelLeft() && this.getMaxFuel() == other.getMaxFuel() && this.getProgress() == other.getProgress() && this.getMaxProgress() == other.getMaxProgress();
        }
        
        protected boolean canEqual(final Object other) {
            return other instanceof Furnace;
        }
        
        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getFuelLeft();
            result = result * 59 + this.getMaxFuel();
            result = result * 59 + this.getProgress();
            result = result * 59 + this.getMaxProgress();
            return result;
        }
        
        @Override
        public String toString() {
            return "Windows.Furnace(fuelLeft=" + this.getFuelLeft() + ", maxFuel=" + this.getMaxFuel() + ", progress=" + this.getProgress() + ", maxProgress=" + this.getMaxProgress() + ")";
        }
    }
}
