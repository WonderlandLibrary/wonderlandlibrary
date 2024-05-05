package xyz.cucumber.base.events.ext;

import net.minecraft.util.BlockPos;
import xyz.cucumber.base.events.Event;

public class EventBlockDamage extends Event {

	float playerRelativeBlockHardness;
	BlockPos pos;
	
	public EventBlockDamage(float playerRelativeBlockHardness, BlockPos pos) {
		this.playerRelativeBlockHardness = playerRelativeBlockHardness;
		this.pos = pos;
	}

	public float getPlayerRelativeBlockHardness() {
		return playerRelativeBlockHardness;
	}

	public void setPlayerRelativeBlockHardness(float playerRelativeBlockHardness) {
		this.playerRelativeBlockHardness = playerRelativeBlockHardness;
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
	
	
}
