package xyz.cucumber.base.module.feat.combat;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.Timer;

@ModuleInfo(category = Category.COMBAT, description = "Automatically goes backwards to dodge hits", name = "STap", key = Keyboard.KEY_NONE, priority = ArrayPriority.HIGH)

public class STapModule extends Mod {
	public Timer timer = new Timer();
	public Timer timer1 = new Timer();
	
	public NumberSettings holdTime = new NumberSettings("Hold Time", 100, 25, 350, 25);
	
    public STapModule()
    {
        this.addSettings(holdTime);
    }
    
    @EventListener
    public void onSendPacket(EventSendPacket e) {
    	if(e.getPacket() instanceof C02PacketUseEntity) {
    		C02PacketUseEntity packet = (C02PacketUseEntity)e.getPacket();
    		if(packet.getAction() == Action.ATTACK) {
    			Entity entity = packet.getEntityFromWorld(mc.theWorld);
    			
    			timer1.reset();
    		}
    	}
    }
    
    @EventListener
    public void onTick(EventTick e) {
    	if(mc.thePlayer.hurtTime == 1) {
    		timer.reset();
    	}
    }
    
    @EventListener
    public void onMoveButton(EventMoveButton e) {
    	if(mc.thePlayer.hurtTime == 0 && mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.pressed && !timer1.hasTimeElapsed(holdTime.getValue(), false) && timer.hasTimeElapsed(500, false) && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit.hurtResistantTime > 0) {
    		e.forward = false;
    		e.backward = true;
    	}
    }
}
