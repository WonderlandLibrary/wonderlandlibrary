package markgg.modules.impl.movement;

import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.Module.Category;
import markgg.modules.ModuleInfo;
import markgg.settings.ModeSetting;
import markgg.util.MoveUtil;
import markgg.util.timer.TickTimer;
import markgg.util.timer.Timer;
import net.minecraft.client.settings.GameSettings;

@ModuleInfo(name = "Speed", category = Module.Category.MOVEMENT)
public class Speed extends Module {

	public ModeSetting mode = new ModeSetting("Mode", this, "Blockdrop", "Blockdrop", "SkyCave", "NCP", "NCP Hop", "Vulcan", "HypixelDev", "Legit", "Lowhop", "YPort", "Verus");
	public Timer timer = new Timer();
	public TickTimer timer2 = new TickTimer();
	private int motionTicks = 0;
	private boolean move = false;

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		switch(mode.getMode()) {
		case "Blockdrop":
			if (timer.hasTimeElapsed(1000, false))
				mc.timer.timerSpeed = (float) 1.08;
			else if(timer.hasTimeElapsed(1200, true))
				mc.timer.timerSpeed = 1;

			if(MoveUtil.isMoving()){
				MoveUtil.strafe((float) 0.26);
				mc.gameSettings.keyBindSprint.pressed = true;
				mc.gameSettings.keyBindJump.pressed = true;
			}else {
				mc.gameSettings.keyBindJump.pressed = false;
				mc.thePlayer.motionX = 0.0;
				mc.thePlayer.motionZ = 0.0;
			}
			break;
		case "SkyCave":
			if(MoveUtil.isMoving()){
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					mc.thePlayer.speedInAir = 0.0223f;
				}
				MoveUtil.strafe((float) MoveUtil.getBaseMoveSpeed());
			} else {
				final double rotation = Math.toRadians(mc.thePlayer.rotationYaw), x = Math.sin(rotation), z = Math.cos(rotation);
				mc.thePlayer.setPosition(mc.thePlayer.posX - x * 0.005, mc.thePlayer.posY, mc.thePlayer.posZ + z * 0.005);	
			}
			break;
		case "NCP":
			if(MoveUtil.isMoving()){
				double speed = 4.25;
				if (mc.thePlayer.onGround) {
					mc.thePlayer.jump();
					if (motionTicks == 1) {
						timer2.reset();
						if (move) {
							mc.thePlayer.motionX = 0.0;
							mc.thePlayer.motionZ = 0.0;
							move = false;
						}
						motionTicks = 0;
					}else motionTicks = 1;
				}else if (!move && motionTicks == 1 && timer2.hasTimePassed(5)) {
					mc.thePlayer.motionX *= speed;
					mc.thePlayer.motionZ *= speed;
					move = true;
				}
				if(!mc.thePlayer.onGround) 
					MoveUtil.strafe((float) MoveUtil.getBaseMoveSpeed());
				timer2.update();
			}
			break;
		case "NCP Hop":
			if(mc.thePlayer.fallDistance > 0.9)
				mc.timer.timerSpeed = 1.13F;
			else if(!mc.thePlayer.onGround)
				mc.timer.timerSpeed = 1F;
			if(MoveUtil.isMoving()) {
				MoveUtil.strafe(MoveUtil.getSpeed() * 1);
				mc.gameSettings.keyBindJump.pressed = true;
			} else {
				mc.gameSettings.keyBindJump.pressed = false;
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
			break;
		case "Vulcan":
			if(MoveUtil.getSpeed() < 0.29) {
				mc.thePlayer.motionX *= 1.0005F;
				mc.thePlayer.motionZ *= 1.0005F;
			}
			
			if(timer.hasTimeElapsed(2000, true)) {
				mc.thePlayer.motionX *= 1.008F;
				mc.thePlayer.motionZ *= 1.008F;
			}
			
			mc.thePlayer.jumpMovementFactor = 0.0244F;
			mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
			
			if (MoveUtil.getSpeed() < 0.215f && !mc.thePlayer.onGround) {
				MoveUtil.strafe(0.215f);
			}
			if (mc.thePlayer.onGround && MoveUtil.isMoving()) {
				mc.gameSettings.keyBindJump.pressed = false;
				mc.thePlayer.jump();
			}
			
			if (!mc.thePlayer.isAirBorne) 
				return;
			break;
		case "HypixelDev":
			if(MoveUtil.isMoving())
				mc.gameSettings.keyBindJump.pressed = true;
			else
				mc.gameSettings.keyBindJump.pressed = false;
			if(mc.thePlayer.onGround)
				MoveUtil.setSpeed(.165F);
			else if(mc.thePlayer.fallDistance > 0.7)
				mc.timer.timerSpeed = 1.06F;
			break;
		case "Legit":
			if(MoveUtil.isMoving())
				mc.gameSettings.keyBindJump.pressed = true;
			else
				mc.gameSettings.keyBindJump.pressed = false;
			break;
		case "YPort":
			if(mc.thePlayer.onGround){
				mc.thePlayer.motionY = 0.01f;
				MoveUtil.setSpeed(.500F);
			} else
				mc.gameSettings.keyBindJump.pressed = false;
		case "Lowhop":
			if(mc.thePlayer.onGround){
				mc.thePlayer.motionY = 0.3f;
				MoveUtil.setSpeed(.160F);
			} else
				mc.gameSettings.keyBindJump.pressed = false;
			break;
		case "Verus":
			if(MoveUtil.isMoving() && mc.thePlayer.onGround)
				mc.thePlayer.jump();
			
			if(MoveUtil.isMoving())
				MoveUtil.strafe(.285F);
			break;
		}
	};

	public void onEnable() {
		switch(mode.getMode()) {
		case "SkyCave":
			mc.timer.timerSpeed = 1.0865f;
			break;
		case "Verus":
			mc.timer.timerSpeed = 1.005F;
			break;
		}
	}


	public void onDisable() {
		mc.timer.timerSpeed = 1;
		mc.gameSettings.keyBindJump.pressed = false;
		mc.thePlayer.speedInAir = 0.02f;
	}

}
