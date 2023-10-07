package vestige.util.player;

import vestige.util.base.IMinecraft;

public class HotbarUtil implements IMinecraft {
	
	public static int getAirSlot() {
		for(int i = 0; i < 9; i++) {
			if(mc.thePlayer.inventory.mainInventory[i] == null) {
				return i;
			}
		}
		return -1;
	}
}
