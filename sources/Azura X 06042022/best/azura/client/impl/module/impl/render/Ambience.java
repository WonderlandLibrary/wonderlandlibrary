package best.azura.client.impl.module.impl.render;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.NumberValue;

@ModuleInfo(name = "Ambience", category = Category.RENDER, description = "Change the day light cycle or the weather")
public class Ambience extends Module {

	public final NumberValue<Long> worldTime = new NumberValue<>("World Time",
			"Change the time", 12000L, 100L, 1000L, 24000L);
	/*public final ModeValue weather = new ModeValue("Weather", "Change the wheater",
			"Clear", "Clear", "Rain", "Snow");

	 */

	/*@EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
		if (e.isPre()) {
			switch (weather.getObject()) {
				case "Clear":
					mc.theWorld.getWorldInfo().setCleanWeatherTime((int) mc.theWorld.getWorldTime());
					break;
				case "Rain":
					//mc.theWorld.getWorldInfo().setRaining(true);
					break;
				case "Snow":
					mc.theWorld.getWorldInfo().setRaining(true);
					mc.theWorld.canSnowAt(BlockPos.ORIGIN, true);
					break;
			}
		}
	};

	 */

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
