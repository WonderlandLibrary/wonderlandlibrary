package viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import java.util.HashMap;
import java.util.Objects;
import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;

public class Windows extends StoredObject {

	public HashMap<Short, Short> types = new HashMap<>();
	public HashMap<Short, Furnace> furnace = new HashMap<>();
	public short levelCost = 0;
	public short anvilId = -1;

	public Windows(UserConnection user) {
		super(user);
	}

	public short get(short windowId) {
		return types.getOrDefault(windowId, (short) -1);
	}

	public void remove(short windowId) {
		types.remove(windowId);
		furnace.remove(windowId);
	}

	public static int getInventoryType(String name) {
		switch(name) {
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
			default:
				throw new IllegalArgumentException("Unknown type " + name);
		}
	}

	public static class Furnace {

		private short fuelLeft = 0;
		private short maxFuel = 0;
		private short progress = 0;
		private short maxProgress = 200;

		public Furnace() {}

		public Furnace(short fuelLeft, short maxFuel, short progress, short maxProgress) {
			this.fuelLeft = fuelLeft;
			this.maxFuel = maxFuel;
			this.progress = progress;
			this.maxProgress = maxProgress;
		}

		public short getFuelLeft() { return fuelLeft; }
		public void setFuelLeft(short fuelLeft) {
			this.fuelLeft = fuelLeft;
		}
		public short getMaxFuel() { return maxFuel; }
		public void setMaxFuel(short maxFuel) {
			this.maxFuel = maxFuel;
		}
		public short getProgress() { return progress; }
		public void setProgress(short progress) {
			this.progress = progress;
		}
		public short getMaxProgress() { return maxProgress; }
		public void setMaxProgress(short maxProgress) {
			this.maxProgress = maxProgress;
		}
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(!(o instanceof Furnace)) return false;

			Furnace other = (Furnace) o;
			return fuelLeft == other.fuelLeft && maxFuel == other.maxFuel && progress == other.progress && maxProgress == other.maxProgress;
		}
		@Override
		public int hashCode() {
			return Objects.hash(fuelLeft, maxFuel, progress, maxProgress);
		}
		@Override
		public String toString() {
			return "Furnace{" + "fuelLeft=" + fuelLeft + ", maxFuel=" + maxFuel + ", progress=" + progress + ", maxProgress=" + maxProgress + '}';
		}
	}
}
