package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventAirCollide;
import xyz.cucumber.base.events.ext.EventLook;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.InventoryUtils;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to not take fall damage", name = "No Fall", key = Keyboard.KEY_NONE)
public class NoFallModule extends Mod {
	
public ModeSettings mode = new ModeSettings("Mode", new String[] {"Clutch", "Verus", "Vulcan", "Spoof", "Grim"});
	
	public Timer timer = new Timer();
	
	public boolean canWork, pickup;
	
	public NoFallModule() {
		this.addSettings(mode);
	}
	
	@EventListener
	public void onLook(EventLook e) {
		switch(mode.getMode().toLowerCase()) {
		case "clutch":
			if(mc.thePlayer.fallDistance > 3) {
				if(MovementUtils.isOnGround(1)) {
					
					int item = InventoryUtils.getBucketSlot();
					if(item == -1) {
						item = InventoryUtils.getCobwebSlot();
					}
					if(item == -1)return;
					
					if(timer.hasTimeElapsed(500, true)) {
						mc.thePlayer.inventory.currentItem = item;
					}
					e.setPitch(90);
					mc.rightClickMouse();
				}
			}
			break;
		}
	}
	
	@EventListener
	public void onMotion(EventMotion e) {
		setInfo(mode.getMode());
		
		switch(mode.getMode().toLowerCase()) {
		case "clutch":
			if(mc.thePlayer.fallDistance > 3) {
				if(MovementUtils.isOnGround(1)) {
					e.setPitch(90);
				}
			}
			break;
		case "verus":
			if(mc.thePlayer.fallDistance > 2.9) {
				mc.thePlayer.motionX *= 0.6;
				mc.thePlayer.motionZ *= 0.6;
				mc.thePlayer.motionY = 0;
				e.setOnGround(true);
				mc.thePlayer.fallDistance = 0;
			}
			break;
		case "vulcan":
			if(mc.thePlayer.fallDistance > 2.9) {
				mc.thePlayer.motionY = -0.11;
				e.setOnGround(true);
				mc.thePlayer.fallDistance = 0;
			}
			break;
		case "spoof":
			if(mc.thePlayer.fallDistance > 2.9) {
				e.setOnGround(true);
				mc.thePlayer.fallDistance = 0;
			}
			break;
		case "grim":
			if(mc.thePlayer.fallDistance >= 3) {
				mc.thePlayer.motionX *= 0.2D;
		        mc.thePlayer.motionZ *= 0.2D;
	
		        float distance = mc.thePlayer.fallDistance;
	
		        if (MovementUtils.isOnGround(2)) {
		            if (distance > 2) {
		            	MovementUtils.strafe(0.19f);
		            }
	
		            if (distance > 3 && MovementUtils.getSpeed() < 0.2) {
		                e.setOnGround(true);
		                distance = 0;
		            }
		        }
	
		        mc.thePlayer.fallDistance = distance;
			}
			break;
		}
	}
	
	@EventListener
	public void onReceivePacket(EventReceivePacket e) {
		switch(mode.getMode().toLowerCase()) {
		case "grim":
			if(e.getPacket() instanceof S08PacketPlayerPosLook) {
				canWork = true;
			}
		}
	}
	
	@EventListener
	public void onAirCollide(EventAirCollide e) {
		switch(mode.getMode().toLowerCase()) {
		case "grim":
			if (mc.thePlayer.fallDistance >= 3.0f && !canWork) {
				if (mc.theWorld.getBlockState(e.getPos()).getBlock() instanceof BlockAir && !mc.thePlayer.isSneaking()) {
		            final double x = e.getPos().getX(), y = e.getPos().getY(), z = e.getPos().getZ();
	
		            if (y < mc.thePlayer.posY) {
		                e.setReturnValue(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(x, y, z));
		            }
		        }
			}
		}
	}
}
