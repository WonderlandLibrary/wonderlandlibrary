package markgg.modules.impl.movement;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

import markgg.RazeClient;
import markgg.event.Event;
import markgg.event.handler.EventHandler;
import markgg.event.handler.Listener;
import markgg.event.impl.MotionEvent;
import markgg.event.impl.PacketEvent;
import markgg.modules.Module;
import markgg.modules.ModuleInfo;
import markgg.modules.impl.movement.OldScaffold.BlockData;
import markgg.settings.BooleanSetting;
import markgg.settings.ModeSetting;
import markgg.settings.NumberSetting;
import markgg.util.MoveUtil;
import markgg.util.timer.Timer;
import net.minecraft.block.Block;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Fly", category = Module.Category.MOVEMENT)
public class Fly extends Module{
	public ModeSetting flyMode = new ModeSetting("Mode", this, "Motion", "Motion", "SkyCave","BW Practice", "Packet", "NCP", "Verus", "SpoofGround");
	public NumberSetting flySpeed = new NumberSetting("Fly Speed", this, 0.5, 0.1, 9.5, 0.1);
	public BooleanSetting viewBob = new BooleanSetting("View Bob", this, false);
	public Timer timer = new Timer();

	double moveSpeed = 0.0, startY = 0.0;
	boolean wasHurt;

	@EventHandler
	private final Listener<MotionEvent> e = e -> {
		if (e.getType() == MotionEvent.Type.PRE) {
			if(viewBob.getValue()) {
				mc.thePlayer.cameraPitch = 0.1F;
				mc.thePlayer.cameraYaw = 0.1F;
			}
			switch(flyMode.getMode()) {
			case "Motion":
				e.setOnGround(true);

				MoveUtil.setSpeed(flySpeed.getValue());
				if (GameSettings.isKeyDown(mc.gameSettings.keyBindJump)) 
					mc.thePlayer.motionY = 0.5D;
				else if (GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) 
					mc.thePlayer.motionY = -0.5D;
				else 
					mc.thePlayer.motionY = 0.0D;
				break;
			case "SkyCave":
				mc.thePlayer.motionY = 0;
				mc.thePlayer.jumpMovementFactor = 0;
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.000008, mc.thePlayer.posZ);
				if (!MoveUtil.isMoving() || mc.thePlayer.isCollidedHorizontally)
					moveSpeed = 0.25;
				if (moveSpeed > 0.25)
					moveSpeed -= moveSpeed / 120;
				MoveUtil.setSpeed((float) moveSpeed);
				break;
			case "BW Practice":
				MoveUtil.strafe(0.29f);
				mc.thePlayer.motionX *= 1.07;
				mc.thePlayer.motionZ *= 1.07;
				mc.thePlayer.motionY = 0; 
				mc.thePlayer.cameraPitch = 0.1F;
				mc.thePlayer.cameraYaw = 0.1F;
				mc.thePlayer.onGround = true;
				e.setOnGround(true);
				mc.theWorld.setBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), null);
				if(PacketEvent.getPacket() instanceof C03PacketPlayer) {
					e.setCancelled(true);
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
				break;
			case "Packet":
				final double rotation = Math.toRadians(mc.thePlayer.rotationYaw), x2 = Math.sin(rotation), z2 = Math.cos(rotation);
				mc.thePlayer.motionY = 0;
				if(timer.hasTimeElapsed(200, true))
					mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - x2, mc.thePlayer.posY, mc.thePlayer.posZ + z2, false));
				break;
			case "NCP":
				if (startY > mc.thePlayer.posY) 
					mc.thePlayer.motionY = -0.000000000000000000000000000000001;

				if (mc.gameSettings.keyBindSneak.pressed) 
					mc.thePlayer.motionY = -0.2;

				if (mc.gameSettings.keyBindJump.pressed && mc.thePlayer.posY < startY - 0.1) 
					mc.thePlayer.motionY  = 0.2;

				MoveUtil.strafe(MoveUtil.getSpeed());
				break;
			case "Verus":
				mc.timer.timerSpeed = 1.005F;
				if(!mc.thePlayer.isBlocking() && !mc.thePlayer.isUsingItem() && !mc.gameSettings.keyBindSneak.pressed)
					MoveUtil.strafe(0.285F);

				if(!MoveUtil.isMoving())
					MoveUtil.stop();

				if(viewBob.getValue()) {
					mc.thePlayer.cameraPitch = 0.1F;
					mc.thePlayer.cameraYaw = 0.1F;
				}

				if(verusUp(true))
					startY = mc.thePlayer.posY;

				if(mc.gameSettings.keyBindSneak.pressed) {
					startY = mc.thePlayer.posY;
					return;
				}
				if(mc.thePlayer.posY <= startY || verusUp(true)) {
					mc.thePlayer.motionX *= 0.66F;
					mc.thePlayer.motionZ *= 0.66F;
					mc.thePlayer.jump();
					mc.thePlayer.onGround = true;
					e.setOnGround(true);
					mc.theWorld.setBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), null);
					if(PacketEvent.getPacket() instanceof C03PacketPlayer) {
						e.setCancelled(true);
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
					}
				}
				break;
			case "SpoofGround":
				mc.thePlayer.motionY = 0; 
				mc.thePlayer.cameraPitch = 0.1F;
				mc.thePlayer.cameraYaw = 0.1F;
				mc.thePlayer.onGround = true;
				e.setOnGround(true);
				mc.theWorld.setBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), null);
				if(PacketEvent.getPacket() instanceof C03PacketPlayer) {
					e.setCancelled(true);
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
				break;
			}
		}
	};

	public void onEnable() {
		wasHurt = false;
		startY = mc.thePlayer.posY;
		double x = mc.thePlayer.posX,y = mc.thePlayer.posY,z = mc.thePlayer.posZ;
		switch(flyMode.getMode()) {
		case "SkyCave":
			moveSpeed = 1.7;

			if (mc.thePlayer.onGround)
				mc.thePlayer.jump();
			break;
		case "NCP":
			if (!mc.thePlayer.onGround) 
				return;

			for (int i = 0; i <= 3; i++) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.01, z, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
			}

			mc.thePlayer.jump();
			mc.thePlayer.swingItem();
			break;
		}
	}

	public void onDisable() {
		wasHurt = false;
		mc.gameSettings.keyBindJump.pressed = false;
		switch(flyMode.getMode()) {
		case "SkyCave":
			if (moveSpeed > 0.25)
				MoveUtil.strafe((float) 0.0);
			break;
		}
	}

	private boolean verusUp(boolean verusUp) {
		if(mc.gameSettings.keyBindJump.pressed && timer.hasTimeElapsed(400, true))
			return true;
		else
			return false;
	}

}