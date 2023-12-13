package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.commands.option.SingleCustomOptionActivator;
import me.robbanrobbin.jigsaw.client.commands.option.SinglePredeterminedOption;
import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class FakeHackers extends Module {
	
	public static String fakehacker = "";
	
	private WaitTimer killAuraTimer = new WaitTimer();

	public FakeHackers() {
		super("FakeHackers", Keyboard.KEY_NONE, Category.WORLD, "Makes legit players look like as if they were cheating! Then record them and report them to the admins. Works really well!");
	}

	@Override
	public void onDisable() {
		if (this.currentMode.equals("Sneak")) {
			EntityPlayer player = mc.world.getPlayerEntityByName(fakehacker);
			player.setSneaking(false);
		}
		super.onDisable();
	}

	@Override
	public void onToggle() {
		super.onToggle();
	}

	@Override
	public void onLateUpdate() {
		final EntityPlayer player = mc.world.getPlayerEntityByName(fakehacker);
		if (player == null) {
			return;
		}
		if (ClientSettings.fakeHackersSneak) {
			player.setSneaking(true);
		}
		boolean didKillauraFindTarget = false;
		if (ClientSettings.fakeHackersKillaura) {
			EntityLivingBase toFace = Utils.getClosestEntityToEntity(4, player);
			if(toFace == null) {
				return;
			}
			didKillauraFindTarget = true;
			if(killAuraTimer.hasTimeElapsed(1000 / ClientSettings.fakeHackersKillauraSpeed, true)) {
				player.swingArm(EnumHand.MAIN_HAND);
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					final float[] rots = Utils.getFacePosRemote(
							new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ),
							new Vec3d(toFace.prevPosX, toFace.prevPosY + toFace.getEyeHeight(), toFace.prevPosZ));
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					player.prevRotationYawHead = rots[0];
					player.prevRotationYaw = rots[0];
					player.prevRotationPitch = rots[1];
					Minecraft.getMinecraft().addScheduledTask(new Runnable() {
						
						@Override
						public void run() {
							player.rotationYawHead = rots[0];
							player.rotationYaw = rots[0];
							player.rotationPitch = rots[1];
						}
					});
				}
			}).start();
		}
		if (ClientSettings.fakeHackersHeadless && !didKillauraFindTarget) {
			player.prevRotationPitch = -180;
			player.rotationPitch = -180;
		}
		if (ClientSettings.fakeHackersSpin) {
			int mod = mc.player.ticksExisted % 360 - 180;
			player.prevRotationYaw = mod * 10;
			if(!didKillauraFindTarget) {
				player.prevRotationYawHead = mod * 10;
				player.rotationYawHead = mod * 10;
			}
			player.rotationYaw = mod * 10;
		}
		super.onLateUpdate();
	}
	
	@Override
	public void onPacketRecieved(PacketEvent event) {
		super.onPacketRecieved(event);
		
		boolean interceptSPacketEntity = ClientSettings.fakeHackersKillaura || ClientSettings.fakeHackersHeadless || ClientSettings.fakeHackersSpin;
		
		Packet packet = event.getPacket();
		
		if(interceptSPacketEntity) {
			if(packet instanceof SPacketEntity) {
				Entity entity = ((SPacketEntity) packet).getEntity(mc.world);
				if(entity != null && fakehacker.equals(entity.getName())) {
					entity.prevRotationYaw = ((SPacketEntity) packet).yaw;
					entity.rotationYaw = ((SPacketEntity) packet).yaw;
					((SPacketEntity) packet).rotating = false;
				}
			}
			
		}
		if(interceptSPacketEntity) {
			if(packet instanceof SPacketEntityHeadLook) {
				Entity entity = ((SPacketEntityHeadLook) packet).getEntity(mc.world);
				if(entity != null && fakehacker.equals(entity.getName())) {
					event.cancel();
				}
			}
		}
		
	}
	
	@Override
	public ModSetting[] getModSettings() {
		return new ModSetting[] {
			new CheckBtnSetting("Sneak", "fakeHackersSneak") {
				@Override
				public void onValueChanged() {
					super.onValueChanged();
					if(mc.world != null && mc.world.getPlayerEntityByName(fakehacker) != null) {
						mc.world.getPlayerEntityByName(fakehacker).setSneaking(false);
					}
				}
			},
			new CheckBtnSetting("Kill Aura", "fakeHackersKillaura"),
			new CheckBtnSetting("Headless", "fakeHackersHeadless") {
				@Override
				public void onValueChanged() {
					super.onValueChanged();
					if(mc.world != null && mc.world.getPlayerEntityByName(fakehacker) != null) {
						mc.world.getPlayerEntityByName(fakehacker).rotationPitch = 0;
						mc.world.getPlayerEntityByName(fakehacker).prevRotationPitch = 0;
					}
				}
			},
			new CheckBtnSetting("Spin", "fakeHackersSpin"),
			new SliderSetting("Fake KillAura Speed", "fakeHackersKillauraSpeed", 1, 20, ValueFormat.DECIMAL),
		};
	}

	public static boolean isFakeHacker(EntityPlayer player) {
		EntityPlayer en = mc.world.getPlayerEntityByName(fakehacker);
		if (en == null) {
			return false;
		}
		if (player.isEntityEqual(en)) {
			return true;
		}
		return false;
	}

	public static void removeHacker() {
		fakehacker = "";
	}
	
	@Override
	public CommandOption[] getCommandOptions() {
		return new CommandOption[] {
				new SingleCustomOptionActivator("set", "player") {
					
					@Override
					public void run(String arg) {
						EntityPlayer player = mc.world.getPlayerEntityByName(arg);
						if (player == null) {
							addResult("That player could not be found!");
							return;
						}
						FakeHackers.this.setToggled(true, true);
						if (FakeHackers.isFakeHacker(player)) {
							addResult("\"" + arg + "§7 is already a fakehacker!");
						} else {
							removeHacker();
							fakehacker = player.getName();
							addResult("\"" + arg + "§7 is now the fakehacker!");
						}
					}
				},
				new SinglePredeterminedOption("clear") {
					@Override
					public void run() {
						removeHacker();
						addResult("FakeHacker list was cleared!");
					}
					
				}
		};
	}

}
