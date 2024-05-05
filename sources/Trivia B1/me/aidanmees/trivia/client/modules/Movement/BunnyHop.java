package me.aidanmees.trivia.client.modules.Movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.PreMotionEvent;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class BunnyHop extends Module {

	
	private int stage;
	private double moveSpeed;
	private double lastDist;
	
	private int jumps = 0;

	public BunnyHop() {
		super("BunnyHop", Keyboard.KEY_B, Category.MOVEMENT, "Makes you go faster and it bypasses nocheat.");
	}

	@Override
	public void onToggle() {
		
		this.moveSpeed = getBaseMoveSpeed();
		
		this.stage = 1;
		

		jumps = 0;
		super.onToggle();
	}
	
	@Override
	public void onUpdate() {
		
		
              
       }
	

	@Override
	public void onPreMotion(PreMotionEvent event) {
		// trivia.chatMessage(round(mc.thePlayer.posY - (int) mc.thePlayer.posY,
		// 3));
		// if(currentMode.equals("FastHop")
		// || currentMode.equals("LowHop")) {
		// if(mc.thePlayer.onGround) {
		// timer.time = 10000000;
		// }
		// if(!timer.hasTimeElapsed(100000, false)) {
		// return;
		// }
		// }
		if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isInWeb
				|| trivia.getModuleByName("Flight").isToggled()) {
			jumps = -1;
			this.stage = 2;
			return;
		}
		MovementInput movementInput = mc.thePlayer.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		// trivia.chatMessage(round(mc.thePlayer.posY - (int) mc.thePlayer.posY,
		// 3) + " speed:" + (double)jumps / 1000);
		Minecraft.getMinecraft().thePlayer.aps = 0;
		double round = round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3);
	
		if(this.currentMode.equals("SlowHop") && forward != 0 || strafe != 0) {
			if (round == 0.869 || round == 0.15) {
				Entity thePlayer = mc.thePlayer;
				thePlayer.motionY -= 0.1D;
			}
		}
		
		// trivia.chatMessage(round);
		
		if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
			this.stage = 2;

			this.moveSpeed = (2.012D * getBaseMoveSpeed());
			if (this.currentMode.equals("NormalHop")) {
				this.moveSpeed = (1.38D * getBaseMoveSpeed() - 0.01D);
			}
			if(this.currentMode.equals("SlowHop")) {
				this.moveSpeed = getBaseMoveSpeed();
			}
		} else if (this.stage == 2) {
			this.stage = 3;
		
				mc.thePlayer.motionY = 0.399399995803833D;
			
			event.y = 0.399399995803833D;
			this.moveSpeed *= 2.149D;
			if(this.currentMode.equals("SlowHop")) {
				this.moveSpeed = getBaseMoveSpeed() * 1.88;
			}
			jumps++;

		} else if (this.stage == 3) {
			this.stage = 4;

			double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference);
		} else {
			boolean ground = false;
			
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D), true).size() > 0)) {
					ground = true;
				} else {
					ground = false;
				
			}
			// trivia.chatMessage(ground);
			if (ground || (mc.thePlayer.isCollidedVertically) || mc.thePlayer.onGround) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
		}
		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
			jumps = 0;
		} else if (forward != 0.0F) {
			if (strafe >= 1.0F) {
				yaw += (forward > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				yaw += (forward > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}
			if (forward > 0.0F) {
				forward = 1.0F;
			} else if (forward < 0.0F) {
				forward = -1.0F;
			}
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
		event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
		super.onPreMotion(event);
	}

	@Override
	public String[] getModes() {
		return new String[] { "NormalHop", "SlowHop" };
	}

	public void onLateUpdate() {
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		super.onLateUpdate();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	   
}
