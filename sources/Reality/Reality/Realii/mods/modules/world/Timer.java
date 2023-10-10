package Reality.Realii.mods.modules.world;

import java.awt.Color;

import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.world.EventPreUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;

public class Timer extends Module {
    public Numbers<Number> timer = new Numbers<>("Timer", 1.2, 0, 20, 0.1);
    float set;

    public Timer() {
        super("Timer", ModuleType.World);
        addValues(timer);
    }

    @Override
    public void onEnable() {
        super.onEnable();
       // this.setSuffix(timer.getModeAsString());
        this.setSuffix("Balance");
     //   mc.timer.timerSpeed = timer.getValue().floatValue();
        set = mc.timer.timerSpeed;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.timer.timerSpeed == set) {
            mc.timer.timerSpeed = 1;
        }
        mc.timer.timerSpeed = 1f;
        
    }
    
    @EventHandler
    public void ONpre(EventPreUpdate event) {
    	 if (mc.thePlayer.ticksExisted % 1 == 0) {
    		 mc.timer.timerSpeed = 1.1f;
    	 }
    	 if (mc.thePlayer.ticksExisted % 2 == 0) {
    		 mc.timer.timerSpeed = 1.5f;
             //1.5
    	 }
    	 
    	 if (mc.thePlayer.ticksExisted % 3 == 0) {
    		 mc.timer.timerSpeed = 0.9f;
    	 }
    	 
    	 if (mc.thePlayer.ticksExisted % 4 == 0) {
    		 mc.timer.timerSpeed = 0.9f;
    	 }
    	 if (mc.thePlayer.ticksExisted % 5 == 0) {
    		 mc.timer.timerSpeed = 1.3f;
    	 }
    	 if (mc.thePlayer.ticksExisted % 6 == 0) {
    		 mc.timer.timerSpeed = 1.1f;
    	 }
    	 if (mc.thePlayer.ticksExisted % 7 == 0) {
    		 mc.timer.timerSpeed = 0.95f;
    	 }
    	 
    	 
    	 
    	
    }
    @EventHandler
    public void renderHud(EventRender2D event) {
    	float timerSpeed = Minecraft.getMinecraft().timer.timerSpeed;
    	
    	float roundedTimerSpeed = (float) (Math.round(timerSpeed * 10.0) / 10.0);
    	//  FontLoaders.arial18.drawStringWithShadow("Timer " + roundedTimerSpeed, 435, 261, new Color (255,255,255).getRGB());
    }
  
}
