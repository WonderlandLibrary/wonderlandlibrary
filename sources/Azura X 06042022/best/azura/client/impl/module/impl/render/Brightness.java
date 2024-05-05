package best.azura.client.impl.module.impl.render;

import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.other.DelayUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(name = "Full Bright", description = "No more darkness", category = Category.RENDER)
public class Brightness extends Module {
	DelayUtil delayUtil = new DelayUtil();
	
	@EventHandler
    public Listener<Event> eventListener = this::onEvent;
	
	@Override
	public void onEnable() {
		super.onEnable();
		mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5210, 1));
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		mc.thePlayer.removePotionEffect(Potion.nightVision.id);
	}
	
	public void onEvent(Event event) {
		if (event instanceof EventWorldChange && mc.thePlayer != null) {
			if (delayUtil.hasReached(1000))
				mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5210, 1));
		}
		if (event instanceof EventMotion && ((EventMotion) event).isPre()) {
			if (delayUtil.hasReached(1000) && mc.thePlayer.isPotionActive(Potion.nightVision))
				mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5210, 1));
		}
	}
}
