package xyz.cucumber.base.module.feat.movement;

import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventMoveButton;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventUpdate;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.player.DisablerModule;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.module.settings.StringSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.math.Vector3d;

@ModuleInfo(category = Category.MOVEMENT, description = "Allows you to fly", name = "Flight", key = 0)
public class FlightModule extends Mod {

	private ModeSettings mode = new ModeSettings("Mode",
			new String[] { "Vanilla", "Verus", "Vulcan Jump", "Block Drop", "Flag" });

	public NumberSettings speed = new NumberSettings("Speed", 0.5, 0.1, 10, 0.01);

	public Timer timer = new Timer();

	public int flyTicks;

	private Vector3d blockdropPos;
	private Vector2f blockdropRot;

	public boolean started, done;

	public FlightModule() {
		this.addSettings(mode, speed);
	}

	public void onEnable() {
		flyTicks = 0;
		started = false;
		done = false;
		timer.reset();

		switch (mode.getMode().toLowerCase()) {
		case "blockdrop":
			this.blockdropPos = new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
			this.blockdropRot = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
			break;
		case "vulcan jump":
			if (!mc.thePlayer.onGround) {
				this.toggle();
				return;
			}
			break;
		}

		mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
	}

	public void onDisable() {
		mc.thePlayer.isDead = false;
		mc.thePlayer.speedInAir = 0.02f;
		mc.timer.timerSpeed = 1f;

		switch (mode.getMode().toLowerCase()) {
		case "verus":
			MovementUtils.strafe(0f);
			break;
		case "blockdrop":
			mc.thePlayer.motionX = mc.thePlayer.motionY = mc.thePlayer.motionZ = 0;
			for (int i = 0; i < 1; ++i) {
				if (blockdropRot != null)
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(
							new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY,
									mc.thePlayer.posZ, blockdropRot.getX(), blockdropRot.getY(), false));
			}
			break;
		}

		mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
	}

	@EventListener
	public void onMotion(EventMotion e) {
		setInfo(mode.getMode());
		
		switch (mode.getMode().toLowerCase()) {
		case "vanilla":
			if (e.getType() == EventType.PRE) {
				mc.thePlayer.motionY = mc.gameSettings.keyBindJump.pressed ? 1
						: mc.gameSettings.keyBindSneak.pressed ? -1 : 0;
				mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
				if (MovementUtils.isMoving()) {
					MovementUtils.strafe((float) speed.getValue());
				}
			}
			break;
		case "flag":
			if (e.getType() == EventType.PRE) {
				mc.thePlayer.motionY = mc.gameSettings.keyBindJump.pressed ? 1
						: mc.gameSettings.keyBindSneak.pressed ? -1 : 0;
				mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
				if (MovementUtils.isMoving()) {
					MovementUtils.strafe((float) speed.getValue());
				}

				mc.thePlayer.sendQueue
						.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX * 100, mc.thePlayer.posY * 100,
								mc.thePlayer.posZ * 100, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
			}
			break;
		case "block drop":
			if (e.getType() == EventType.PRE) {
				if (MovementUtils.isMoving()) {
					MovementUtils.strafe((float) speed.getValue());
				} else {
					mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
				}

				mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? speed.getValue()
						: mc.gameSettings.keyBindSneak.isKeyDown() ? -speed.getValue() : 0;

				for (int i = 0; i < 3; ++i) {
					if (blockdropPos != null && blockdropRot != null)
						mc.getNetHandler().getNetworkManager()
								.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(blockdropPos.getX(),
										blockdropPos.getY(), blockdropPos.getZ(), blockdropRot.getX(),
										blockdropRot.getY(), false));
				}
			} else {
				for (int i = 0; i < 1; ++i) {
					if (blockdropRot != null)
						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(
								new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY,
										mc.thePlayer.posZ, blockdropRot.getX(), blockdropRot.getY(), false));
				}
			}
			break;
		case "verus":
			if (e.getType() == EventType.PRE) {
				if (mc.thePlayer.hurtTime > 0)
					done = true;
				if (done) {
					mc.timer.timerSpeed = 1f;

					mc.gameSettings.keyBindForward.pressed = true;

					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
							new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
							new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ), 1,
							new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(-1, -1, -1))), 0, 0.94f, 0));

					MovementUtils
							.strafe((float) (timer.getTime() <= (Client.INSTANCE.getModuleManager().getModule(DisablerModule.class).isEnabled() ? 10000 : 1000) ? speed.getValue() : MovementUtils.getBaseMoveSpeed() * 1.3f));
					if (timer.getTime() <= 250)
						MovementUtils.strafe((float) 0f);

					if (mc.thePlayer.fallDistance > 0)
						mc.thePlayer.motionY = 0;
					if (mc.gameSettings.keyBindJump.pressed)
						mc.thePlayer.motionY = 0.2;
					else if (mc.gameSettings.keyBindSneak.pressed)
						mc.thePlayer.motionY = -0.2;
				}
			}
			break;
		case "vulcan jump":
			if (e.getType() == EventType.PRE) {
				if (flyTicks <= 5) {
					mc.thePlayer.motionX = 0;
					mc.thePlayer.motionY = 0;
					mc.thePlayer.motionZ = 0;

					if (done) {
						if (flyTicks == 0) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4, mc.thePlayer.posZ);
						} else if (flyTicks == 1) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4, mc.thePlayer.posZ);
						} else if (flyTicks == 2) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4, mc.thePlayer.posZ);
						} else if (flyTicks == 3) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4, mc.thePlayer.posZ);
						} else if (flyTicks == 4) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ);
						} else if (flyTicks == 5) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ);
						}
					}

					flyTicks++;
				} else {
					if (mc.thePlayer.fallDistance > 2.5f) {
						e.setOnGround(true);
						mc.thePlayer.fallDistance = 0f;
					}
					if (mc.thePlayer.ticksExisted % 2 == 0) {
						mc.thePlayer.motionY = -0.1;
					}

					if (mc.thePlayer.onGround) {
						this.toggle();
					}
				}

				if (!done && mc.thePlayer.onGround) {
					mc.thePlayer.motionX = 0;
					mc.thePlayer.motionZ = 0;
					e.setY(e.getY() - 0.1);

					done = true;
					flyTicks = 0;
				}
				break;
			}
		}
	}

	@EventListener
	public void onMove(EventMove e) {
		switch (mode.getMode().toLowerCase()) {
		case "vanilla":
			break;
		case "flag":
			break;
		case "block drop":
			break;
		case "verus":
			if (flyTicks <= 3) {
				e.setCancelled(true);
			}
			break;
		case "vulcan jump":
			break;
		}
	}

	@EventListener
	public void onMoveButton(EventMoveButton e) {
		switch (mode.getMode().toLowerCase()) {
		case "vanilla":
			break;
		case "flag":
			break;
		case "block drop":
			break;
		case "verus":
			e.forward = true;
			break;
		case "vulcan jump":
			break;
		}
	}

	@EventListener
	public void onSendPacket(EventSendPacket e) {
		switch (mode.getMode().toLowerCase()) {
		case "vanilla":
			break;
		case "flag":
			break;
		case "block drop":
			if (e.getPacket() instanceof C03PacketPlayer) {
				e.setCancelled(true);
			} else if (e.getPacket() instanceof C02PacketUseEntity) {
				mc.getNetHandler().getNetworkManager()
						.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
								mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw,
								mc.thePlayer.rotationPitch, false));
			}
			break;
		case "verus":
			if (e.getPacket() instanceof C03PacketPlayer) {
				if (!started) {
					if (flyTicks <= 3) {
						flyTicks++;
						e.setCancelled(true);
					} else {
						timer.reset();
						started = true;
						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
								new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
						mc.getNetHandler().getNetworkManager()
								.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
										new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ), 1,
										new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(-1, -1, -1))), 0,
										0.94f, 0));
						mc.getNetHandler().getNetworkManager()
								.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
										mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
						mc.getNetHandler().getNetworkManager()
								.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
										mc.thePlayer.posY, mc.thePlayer.posZ, false));
						mc.getNetHandler().getNetworkManager()
								.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
										mc.thePlayer.posY, mc.thePlayer.posZ, true));
					}
				}
			}
			break;
		case "vulcan jump":
			break;
		}
	}

	@EventListener
	public void onReceivePacket(EventReceivePacket e) {
		switch (mode.getMode().toLowerCase()) {
		case "vanilla":
			break;
		case "flag":
			break;
		case "block drop":
			if (e.getPacket() instanceof S08PacketPlayerPosLook) {
				S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) ((EventReceivePacket) e).getPacket();
				e.setCancelled(true);
				blockdropPos = new Vector3d(packet.x, packet.y, packet.z);
				blockdropRot = new Vector2f(packet.yaw, packet.pitch);
			}
			break;
		case "verus":
			break;
		case "vulcan jump":
			break;
		}
	}
}
