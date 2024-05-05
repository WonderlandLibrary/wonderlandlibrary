package xyz.cucumber.base.events.ext;

import net.minecraft.util.BlockPos;
import xyz.cucumber.base.events.Event;

public class EventBlockBreak extends Event {

	BlockPos location;
	
	public EventBlockBreak(BlockPos loc) {
		this.location = loc;
	}

	public BlockPos getLocation() {
		return location;
	}

	public void setLocation(BlockPos location) {
		this.location = location;
	}

	
}
