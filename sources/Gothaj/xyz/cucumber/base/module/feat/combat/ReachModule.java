package xyz.cucumber.base.module.feat.combat;

import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.Packet;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventReach;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.NumberSettings;


@ModuleInfo(category = Category.COMBAT, description = "Allows you hit entity from longer distance", name = "Reach", key = Keyboard.KEY_NONE, priority = ArrayPriority.HIGH)
public class ReachModule extends Mod{
	
    public NumberSettings expand = new NumberSettings("Expand", 0.5, 0.01, 3, 0.01);
    
    private ConcurrentHashMap<Packet<?>, Long> packets = new ConcurrentHashMap<>();

    public ReachModule() {
        this.addSettings(
                expand
           );    	
}
    @EventListener
    public void onReach (EventReach e) {
    	EventReach event = (EventReach)e;
    	event.setRange(event.getRange()+expand.getValue());
    }
    
    private boolean blockPacket(Packet packet) {
        if (packet instanceof net.minecraft.network.play.server.S12PacketEntityVelocity)
        	return true;
        if (packet instanceof net.minecraft.network.play.server.S27PacketExplosion)
        	return true;
        if (packet instanceof net.minecraft.network.play.server.S32PacketConfirmTransaction)
            return true;
        if (packet instanceof net.minecraft.network.play.server.S08PacketPlayerPosLook)
        	return true;
        return false;
    }
}