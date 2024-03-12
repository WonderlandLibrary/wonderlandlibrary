package xyz.cucumber.base.events.ext;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import xyz.cucumber.base.events.Event;

public class EventModelPlayer extends Event {
	AbstractClientPlayer player;
	ModelPlayer model;
	public AbstractClientPlayer getPlayer() {
		return player;
	}
	public void setPlayer(AbstractClientPlayer player) {
		this.player = player;
	}
	public ModelPlayer getModel() {
		return model;
	}
	public void setModel(ModelPlayer model) {
		this.model = model;
	}
	
	public EventModelPlayer(AbstractClientPlayer player, ModelPlayer model) {
		this.player = player;
		this.model = model;
	}
}
