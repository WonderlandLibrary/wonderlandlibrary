package epsilon.modules.combat;

import org.lwjgl.input.Keyboard;

import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.Timer;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
public class FastBow extends Module {

	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Matrix", "Vulcan", "Hawk");
	public ModeSetting pp = new ModeSetting("PreserveMode", "FullCharge", "FullCharge", "MinCharge", "None");
	public NumberSetting shootd = new NumberSetting("ShootDelay", 25,0,500,25);
	public BooleanSetting gun = new BooleanSetting ("NoDraw", false);
	public Timer timer = new Timer();
	public FastBow(){
		super("FastBow", Keyboard.KEY_NONE, Category.COMBAT, "Highschool simulator");
		this.addSettings(mode, pp, shootd, gun);
	}
	
	
	public void onEnable(){
	}
	
	
	
	
	public void onEvent(Event e){
		if(e instanceof EventMotion){
			if(!(mc.thePlayer.getHeldItem().getItem() instanceof ItemBow)) return;
			if(e.isPre()) {
				if(mode.getMode() == "Vanilla") {
					if(mc.thePlayer.isUsingItem()) {
						mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));

						for(int i = 0; i<21; i++) {
							mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ, mc.thePlayer.onGround));
						}
		                    
						
						
					
					}
					if(mc.thePlayer.isUsingItem()&& timer.hasTimeElapsed((long) shootd.getValue(), true)) {
						mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
						if(gun.isEnabled())
							mc.thePlayer.stopUsingItem();
	
					} 
				}	
			}
		}
	}	
	
	
}
