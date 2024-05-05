package sudo.module.render;

import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.particle.WaterSplashParticle;
import net.minecraft.sound.SoundCategory;
import sudo.core.event.EventTarget;
import sudo.events.EventParticle;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;

public class NoRender extends Mod  {

	public BooleanSetting explosion = new BooleanSetting("Explosion", true);
	public BooleanSetting nausea = new BooleanSetting("Nausea", true);
	public BooleanSetting blindness = new BooleanSetting("Blindness", true);
	public BooleanSetting weather = new BooleanSetting("Weather", true);
	public BooleanSetting blocks = new BooleanSetting("Weather", true);
	
	public NoRender() {
		super("NoRender", "Removes specified particles", Category.RENDER, 0);
		addSettings(explosion, nausea, blindness, weather);
	}

    @Override
    public void onTick() {
        mc.options.setSoundVolume(SoundCategory.WEATHER, 0);
    }

    @EventTarget 
	public void particle(EventParticle.Normal event) {
		if (event.getParticle() instanceof BlockDustParticle && blocks.isEnabled()) event.setCancelled(true);
		if (event.getParticle() instanceof RainSplashParticle && weather.isEnabled()) event.setCancelled(true);
		if (event.getParticle() instanceof WaterSplashParticle && weather.isEnabled()) event.setCancelled(true);
	}
    
    public static NoRender get;
}
