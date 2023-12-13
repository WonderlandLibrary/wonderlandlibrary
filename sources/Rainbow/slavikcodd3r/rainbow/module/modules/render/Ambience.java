package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.AmbienceMode;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Ambience")
public class Ambience extends Module
{
	private AmbienceMode day;
	private AmbienceMode night;
	private AmbienceMode morning;
	private AmbienceMode twilight;
	private AmbienceMode sunrise;
	private AmbienceMode sunset;
	private AmbienceMode custom;
	public long time;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public Ambience() {
        this.day = new AmbienceMode("Day", true, this);
        this.night = new AmbienceMode("Night", false, this);
        this.morning = new AmbienceMode("Morning", false, this);
        this.twilight = new AmbienceMode("Twilight", false, this);
        this.sunrise = new AmbienceMode("Sunrise", false, this);
        this.sunset = new AmbienceMode("Sunset", false, this);
        this.custom = new AmbienceMode("Custom", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.day);
        OptionManager.getOptionList().add(this.night);
        OptionManager.getOptionList().add(this.morning);
        OptionManager.getOptionList().add(this.twilight);
        OptionManager.getOptionList().add(this.sunrise);
        OptionManager.getOptionList().add(this.sunset);
        OptionManager.getOptionList().add(this.custom);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.day.getValue()) {
            this.setSuffix("Day");
        }
        else if (this.night.getValue()) {
        	this.setSuffix("Night");
        }
        else if (this.morning.getValue()) {
        	this.setSuffix("Morning");
        }
        else if (this.twilight.getValue()) {
        	this.setSuffix("Twilight");
        }
        else if (this.sunrise.getValue()) {
        	this.setSuffix("Sunrise");
        }
        else if (this.sunset.getValue()) {
        	this.setSuffix("Sunset");
        }
        else if (this.custom.getValue()) {
        	this.setSuffix("Custom");
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    private void onPacket(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    	if (this.day.getValue()) {
    		mc.theWorld.setWorldTime(6000L);
    	}
    	else if (this.night.getValue()) {
    		mc.theWorld.setWorldTime(18000L);
    	}
    	else if (this.morning.getValue()) {
    		mc.theWorld.setWorldTime(0L);
    	}
    	else if (this.twilight.getValue()) {
    		mc.theWorld.setWorldTime(13000L);
    	}
    	else if (this.sunrise.getValue()) {
    		mc.theWorld.setWorldTime(1151250L);
    	}
    	else if (this.sunset.getValue()) {
    		mc.theWorld.setWorldTime(12750L);
    	}
    	else if (this.custom.getValue()) {
            for (int i = 0; i < 150; ++i) {
        		time++;
            }
            mc.theWorld.setWorldTime(time);
    	}
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
