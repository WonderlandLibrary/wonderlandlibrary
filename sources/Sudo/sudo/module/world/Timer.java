package sudo.module.world;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import sudo.module.Mod;
import sudo.module.settings.NumberSetting;
import sudo.utils.ReflectionHelper;


public class Timer extends Mod {

	public NumberSetting speed = new NumberSetting("Speed",  0.2, 10, 1, 0.1);
	
    public Timer() {
        super("Timer", "Modifies the game speed (client side)", Category.WORLD, 0);
        addSetting(speed);
    }

    
    @Override
    public void onTick() {
        ReflectionHelper.setPrivateValue(RenderTickCounter.class, ReflectionHelper.getPrivateValue(MinecraftClient.class, mc, "renderTickCounter", "field_1728"), 1000.0F / (float) speed.getValue() / 20, "tickTime", "field_1968");
        super.onTick();
    }

    @Override
    public void onDisable() {
        ReflectionHelper.setPrivateValue(RenderTickCounter.class, ReflectionHelper.getPrivateValue(MinecraftClient.class, mc, "renderTickCounter", "field_1728"), 1000.0F / 20.0F, "tickTime", "field_1968");
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
    	// TODO Auto-generated method stub
    	super.onEnable();
    }
}