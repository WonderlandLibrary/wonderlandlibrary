package markgg.modules.impl.movement;

import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.util.MoveUtil;
import markgg.util.timer.Timer;
import net.minecraft.potion.Potion;

@ModuleInfo(name = "LongJump", category = Module.Category.MOVEMENT)
public class LongJump extends Module{

	public ModeSetting jumpMode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Verus", "Vulcan", "VulcanTP", "SkyCave");
	public BooleanSetting landDisable = new BooleanSetting("Safe Mode", this, true);
	public BooleanSetting skyCaveExtra = new BooleanSetting("Extra Motion", this, true);

	public Timer timer = new Timer();

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			switch (jumpMode.getMode()) {
			case "Vanilla":
				if (mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed && mc.gameSettings.keyBindJump.pressed) {
					if (mc.thePlayer.isAirBorne){
						mc.thePlayer.motionX *= 1.11;
						mc.thePlayer.motionZ *= 1.11;
					}
				}else{
					mc.thePlayer.motionX *= 1;
					mc.thePlayer.motionZ *= 1;
				}
				if(landDisable.getValue()) {
					if(mc.thePlayer.onGround)
						toggle();
				}
				break;
			case "Verus":
				if(mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindLeft.pressed || mc.gameSettings.keyBindRight.pressed && mc.gameSettings.keyBindJump.pressed){
					float dir = mc.thePlayer.rotationYaw + ((mc.thePlayer.moveForward < 0) ? 180 : 0) + ((mc.thePlayer.moveStrafing > 0) ? (-90F * ((mc.thePlayer.moveForward < 0) ? -.5F : ((mc.thePlayer.moveForward > 0) ? .4F : 1F))) : 0);
					float xDir = (float)Math.cos((dir + 90F) * Math.PI / 180);
					float zDir = (float)Math.sin((dir + 90F) * Math.PI / 180);
					if(mc.thePlayer.isCollidedVertically && (mc.gameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.gameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.gameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.gameSettings.isKeyDown(mc.gameSettings.keyBindBack)) && mc.gameSettings.isKeyDown(mc.gameSettings.keyBindJump)) {
						mc.thePlayer.motionX = xDir * .29F;
						mc.thePlayer.motionZ = zDir * .29F;
					}
					if(mc.thePlayer.motionY == .33319999363422365 && (mc.gameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.gameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.gameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.gameSettings.isKeyDown(mc.gameSettings.keyBindBack))) {
						if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
							mc.thePlayer.motionX = xDir * 1.34;
							mc.thePlayer.motionZ = zDir * 1.34;
						}else {
							mc.thePlayer.motionX = xDir * 0.50;
							mc.thePlayer.motionZ = zDir * 0.50;
						}
					}
				}
				if(landDisable.getValue()) {
					if(mc.thePlayer.onGround)
						toggle();
				}
				break;
			case "Vulcan":
				mc.thePlayer.speedInAir = 0.079912F;

				if(mc.thePlayer.onGround)
					mc.thePlayer.speedInAir = 0.02F;

				if(landDisable.getValue())
					if(mc.thePlayer.onGround)
						toggle();
				break;
			case "VulcanTP":
				if(timer.hasTimeElapsed(100, true)) 
					mc.thePlayer.motionY = -0.155F;
				else 
					mc.thePlayer.motionY = -0.1F;

				if(landDisable.getValue()) {
					if(mc.thePlayer.onGround)
						toggle();
				}
				break;
			case "SkyCave":
				if(mc.thePlayer.onGround && MoveUtil.isMoving()) {
					if(skyCaveExtra.getValue())
						mc.thePlayer.motionY = 0.493F;
					else
						mc.thePlayer.motionY = 0.42F;
				}  else {
					final double rotation = Math.toRadians(mc.thePlayer.rotationYaw), x = Math.sin(rotation), z = Math.cos(rotation);
					if(MoveUtil.isMoving() && !mc.thePlayer.onGround) {
						mc.thePlayer.setPosition(mc.thePlayer.posX - x * 0.1673, mc.thePlayer.posY, mc.thePlayer.posZ + z * 0.1673);
						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01557F, mc.thePlayer.posZ);
						mc.thePlayer.motionY = mc.thePlayer.motionY + 0.01557F;
						mc.thePlayer.speedInAir = 0.027F;
						mc.thePlayer.jumpMovementFactor = mc.thePlayer.jumpMovementFactor + 0.00155F;
						mc.thePlayer.cameraPitch = 0.1F;
						mc.thePlayer.cameraYaw = 0.1F;
					}
				}
				if(landDisable.getValue()) {
					if(mc.thePlayer.onGround)
						toggle();
				}
				break;
			}
		}
	};

	public void onEnable() {
		switch(jumpMode.getMode()) {
		case "Vulcan":
			mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 3.7, mc.thePlayer.posZ);
			break;
		case "VulcanTP":
			mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 10, mc.thePlayer.posZ);
			break;
		default:
			mc.thePlayer.jump();
			break;
		}
	}

	public void onDisable() {
		mc.thePlayer.speedInAir = 0.02F;
	}

}
