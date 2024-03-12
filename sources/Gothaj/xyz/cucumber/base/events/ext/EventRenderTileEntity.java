package xyz.cucumber.base.events.ext;

import net.minecraft.tileentity.TileEntity;
import xyz.cucumber.base.events.Event;

public class EventRenderTileEntity extends Event {
	TileEntity entity;

	public TileEntity getEntity() {
		return entity;
	}

	public void setEntity(TileEntity entity) {
		this.entity = entity;
	}

	public EventRenderTileEntity(TileEntity entity) {
		this.entity = entity;
	}
}
