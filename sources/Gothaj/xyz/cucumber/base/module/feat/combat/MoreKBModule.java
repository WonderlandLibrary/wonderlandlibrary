package xyz.cucumber.base.module.feat.combat;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;

@ModuleInfo(category = Category.COMBAT, description = "Allows you to deal more knockback", name = "More KB", key = Keyboard.KEY_NONE, priority = ArrayPriority.HIGH)

public class MoreKBModule extends Mod {
	
	public int ticks;
	
	public BooleanSettings combo = new BooleanSettings("Combo", true);
	public BooleanSettings fast = new BooleanSettings("Fast", false);
    public BooleanSettings onlyOnGround = new BooleanSettings("Only On Ground", false);

    public MoreKBModule()
    {
        this.addSettings(combo, fast, onlyOnGround);
    }
	
    public void onEnable() {
    	
    }
    
	@EventListener
	public void onTick(EventTick e) {
		if(fast.isEnabled()) {
        	setInfo("Fast");      	
        } else {
        	setInfo("Legit");  
        }
		
		KillAuraModule ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
		BackTrackModule backtrack = (BackTrackModule) Client.INSTANCE.getModuleManager().getModule(BackTrackModule.class);
		
		if(backtrack.isEnabled() && backtrack.packets.size() > 0 && combo.isEnabled()) {
			return;
		}
		
		if(ka.target != null && ka.target.hurtTime == 10) {
			if(fast.isEnabled()) {
				if(onlyOnGround.isEnabled()) {
					if(mc.thePlayer.onGround) {
						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
					}
				}else {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, Action.START_SPRINTING));
				}
			}else {
				ticks++;
				if(onlyOnGround.isEnabled()) {
					if(mc.thePlayer.onGround) {
						mc.thePlayer.reSprint = 2;
					}
				}else {
					mc.thePlayer.reSprint = 2;
				}
			}
		}
	}
}
