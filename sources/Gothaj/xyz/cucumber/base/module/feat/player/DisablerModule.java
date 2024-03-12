package xyz.cucumber.base.module.feat.player;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S14PacketEntity.S15PacketEntityRelMove;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventHit;
import xyz.cucumber.base.events.ext.EventMotion;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.events.ext.EventWorldChange;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.combat.KillAuraModule;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.game.InventoryUtils;
import xyz.cucumber.base.utils.math.RotationUtils;

@ModuleInfo(category = Category.PLAYER, description = "Allows you to disable anticheats", name = "Disabler", priority = ArrayPriority.HIGH)
public class DisablerModule extends Mod {

	public boolean disabled, move, attack;
	public int counter, balance, maxBalance;
	public double difference;
	public long lastSetBack;

	private int currentDelay = 5000;
	private int currentBuffer = 4;
	private int currentDec = -1;

	public Timer timer = new Timer();
	public Timer timer1 = new Timer();

	public ModeSettings mode = new ModeSettings("Mode", new String[] { "Intave", "Verus Experimental", "Vulcan",
			"Spectate", "Verus", "Verus new", "Minemen club", "Via Version", "BMC Combat" });
	public BooleanSettings expandScaffold = new BooleanSettings("Expand Scaffold", true);
	public BooleanSettings intaveReach = new BooleanSettings("Intave Reach", false);
	public BooleanSettings intaveReachStrong = new BooleanSettings("Intave Reach Strong", false);
	public BooleanSettings autoAura = new BooleanSettings("Auto Aura", false);

	public ArrayList<Packet> packets = new ArrayList<>();
	public ArrayList<Packet> packets1 = new ArrayList<>();

	private final ConcurrentHashMap<Packet<?>, Long> pingSpoofPackets = new ConcurrentHashMap<>();

	public DisablerModule() {
		this.addSettings(mode, expandScaffold, intaveReach, intaveReachStrong, autoAura);
	}

	public void onEnable() {
		timer.reset();
		disabled = false;
		packets.clear();
		packets1.clear();

		switch (mode.getMode().toLowerCase()) {
		case "verus experimental":
			Client.INSTANCE.getCommandManager().sendChatMessage("idk what to say");
			break;
		case "intave":
			if (autoAura.isEnabled()) {
				KillAuraModule ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
				ka.Range.setValue(6);
				ka.interactRange.setValue(6);
			}
			break;
		}
	}

	public void onDisable() {
		counter = 0;
		mc.timer.timerSpeed = 1f;
		balance = 0;

		try {
			for (Packet p : packets) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(p);
			}

			for (Packet p : packets1) {
				p.processPacket(mc.getNetHandler().getNetworkManager().getNetHandler());
			}
		} catch (Exception ex) {
		}
		switch (mode.getMode().toLowerCase()) {
		case "verus experimental":
			Client.INSTANCE.getCommandManager().sendChatMessage("idk what to say");
			break;
		case "intave":
			if (autoAura.isEnabled()) {
				KillAuraModule ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
				ka.Range.setValue(3);
				ka.interactRange.setValue(4);
			}
			break;
		}
	}

	@EventListener
	public void onMotion(EventMotion e) {
		switch (mode.getMode().toLowerCase()) {
		case "verus experimental":
			break;
		case "vulcan":
			if (mc.thePlayer.ticksExisted % 5 == 0) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));
			}

			if (mc.thePlayer.ticksExisted % 7 == 0) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, BlockPos.ORIGIN, EnumFacing.DOWN));
			}
			break;
		case "spectate":
			break;
		}
	}

	@EventListener
	public void onSendPacket(EventSendPacket e) {
		switch (mode.getMode().toLowerCase()) {
		case "bmc reach":
			e.setCancelled(true);
			packets.add(e.getPacket());
			break;

		case "minemen club":
			if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
				e.setCancelled(true);
				packets.add(e.getPacket());
			}

			try {
				while (packets.size() > 20) {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packets.get(0));
					packets.remove(0);
				}
			} catch (Exception ex) {
			}
			break;
		case "intave":

			if (intaveReach.isEnabled()) {
				if (e.getPacket() instanceof C02PacketUseEntity || e.getPacket() instanceof C0APacketAnimation) {
					KillAuraModule killAura = ((KillAuraModule) Client.INSTANCE.getModuleManager()
							.getModule(KillAuraModule.class));

					TeamsModule teams = (TeamsModule) Client.INSTANCE.getModuleManager().getModule(TeamsModule.class);

					EntityLivingBase entity = EntityUtils.getTargetBox(8, killAura.Targets.getMode(),
							killAura.Switch.isEnabled() ? "Switch" : "Single", 500, teams.isEnabled(),

							killAura.TroughWalls.isEnabled(), killAura.attackDead.isEnabled(),
							killAura.attackInvisible.isEnabled());

					if (entity == null) {
						e.setCancelled(true);
						return;
					}

					if (EntityUtils.getDistanceToEntityBox(entity) > 3 && killAura.isEnabled()) {
						e.setCancelled(true);
					}
				}

				if (e.getPacket() instanceof C03PacketPlayer && move) {
					move = false;
					attack = true;

					KillAuraModule killAura = ((KillAuraModule) Client.INSTANCE.getModuleManager()
							.getModule(KillAuraModule.class));

					TeamsModule teams = (TeamsModule) Client.INSTANCE.getModuleManager().getModule(TeamsModule.class);

					EntityLivingBase entity = EntityUtils.getTargetBox(8, killAura.Targets.getMode(),
							killAura.Switch.isEnabled() ? "Switch" : "Single", 500, teams.isEnabled(),

							killAura.TroughWalls.isEnabled(), killAura.attackDead.isEnabled(),
							killAura.attackInvisible.isEnabled());

					if (entity == null)
						return;

					e.setCancelled(true);
					float[] rots = RotationUtils.getRotationsFromPositionToPosition(entity.posX, entity.posY,
							entity.posZ, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
					float yaw = rots[0];
					float pitch = rots[1];

					for (int i = 0; i < (intaveReachStrong.isEnabled() ? 20 : 2); i++) {
						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(
								new C06PacketPlayerPosLook(entity.posX, entity.posY, entity.posZ, yaw, pitch, true));
					}
					attack = true;
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0APacketAnimation());
					mc.getNetHandler().getNetworkManager()
							.sendPacketNoEvent(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));

				}
			}
			break;
		case "verus experimental":
			if (e.getPacket() instanceof C03PacketPlayer) {
				if (this.mc.thePlayer.isRiding()) {
					((C03PacketPlayer) e.getPacket()).onGround = false;
				}
				if (disabled) {
					((C03PacketPlayer) e.getPacket()).onGround = false;
				}
			}
			break;
		case "verus new":
			mc.getNetHandler().getNetworkManager()
					.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
							new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 255,
							new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			mc.getNetHandler().getNetworkManager()
					.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
							new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - .5, mc.thePlayer.posZ), 255,
							new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			mc.getNetHandler().getNetworkManager()
					.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
							new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ), 1,
							new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
			if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
				e.setCancelled(true);
				packets.add(e.getPacket());
			}

			while (packets.size() > 200) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packets.get(0));
				packets.remove(0);
			}
			break;
		case "vulcan":
			if (mc.thePlayer.ticksExisted % 2 == 0) {
				if (e.getPacket() instanceof C08PacketPlayerBlockPlacement || e.getPacket() instanceof C03PacketPlayer
						|| e.getPacket() instanceof C0APacketAnimation || e.getPacket() instanceof C0BPacketEntityAction
						|| e.getPacket() instanceof C02PacketUseEntity) {
					e.setCancelled(true);
					packets.add(e.getPacket());
				}
			} else {
				for (Packet p : packets) {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(p);
				}
				packets.clear();
			}
			break;
		case "spectate":
			if (e.getPacket() instanceof C03PacketPlayer) {
				mc.getNetHandler().getNetworkManager().sendPacket(new C18PacketSpectate(mc.thePlayer.getUniqueID()));
			}
			break;

		case "verus":
			if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
				e.setCancelled(true);
				packets.add(e.getPacket());
			}

			while (packets.size() > 200) {
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packets.get(0));
				packets.remove(0);

				if (!disabled) {
					disabled = true;
					Client.INSTANCE.getCommandManager().sendChatMessage("Verus is now disabled");
				}
			}
			break;
		}
	}

	@EventListener
	public void onReceivePacket(EventReceivePacket e) {
		switch (mode.getMode().toLowerCase()) {
		case "verus experimental":
			if (e.getPacket() instanceof S07PacketRespawn || e.getPacket() instanceof S01PacketJoinGame) {
				packets.clear();
				counter = 0;
				disabled = false;
			}
			if (e.getPacket() instanceof S13PacketDestroyEntities) {
				if (((S13PacketDestroyEntities) e.getPacket()).getEntityIDs().length != 100) {
					for (final int entityID : ((S13PacketDestroyEntities) e.getPacket()).getEntityIDs()) {
						if (entityID == this.mc.thePlayer.ridingEntity.getEntityId()) {
							this.mc.timer.timerSpeed = 1.0f;
							Client.INSTANCE.getCommandManager().sendChatMessage("Verus has been disabled");
							disabled = true;
						}
					}
				} else {
					for (final int entityID : ((S13PacketDestroyEntities) e.getPacket()).getEntityIDs()) {
						if (entityID == this.mc.thePlayer.ridingEntity.getEntityId()) {

							Client.INSTANCE.getCommandManager().sendChatMessage("Verus has been disabled");
							for (int i = 0; i < 20; ++i) {
								Client.INSTANCE.getCommandManager()
										.sendChatMessage("Verus disabler have not enabled successfully");
							}
						}
					}
				}
			}
			if (e.getPacket() instanceof S1BPacketEntityAttach
					&& ((S1BPacketEntityAttach) e.getPacket()).getEntityId() == this.mc.thePlayer.getEntityId()
					&& ((S1BPacketEntityAttach) e.getPacket()).getVehicleEntityId() > 0) {
				counter = 0;
			}
			break;
		case "vulcan":
			break;
		case "spectate":
			break;
		case "intave":
			break;
		case "via version":
			if (e.getPacket() instanceof S08PacketPlayerPosLook) {
				S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();

				e.setCancelled(true);
				mc.thePlayer.setPositionAndRotation(packet.x, packet.y, packet.z, packet.yaw, packet.pitch);
				mc.getNetHandler().addToSendQueue(new C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw,
						packet.pitch, mc.thePlayer.onGround));
			}
			break;
		}
	}

	@EventListener
	public void onTick(EventTick e) {
		setInfo(mode.getMode());
		switch (mode.getMode().toLowerCase()) {
		case "bmc combat":
			mc.getNetHandler().getNetworkManager().sendPacketNoEvent(
					new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));

			boolean stop = false;
			for (int i = 9; i < 45 && !stop; i++) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
					if(is != null) {
						(Minecraft.getMinecraft()).playerController.windowClick((Minecraft.getMinecraft()).thePlayer.inventoryContainer.windowId, i, 0, 2, (EntityPlayer)(Minecraft.getMinecraft()).thePlayer);
						stop = true;
					}
				}
			}

			for (Packet p : packets) {
				if (p != null)
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(p);
			}
			packets.clear();

			mc.getNetHandler().getNetworkManager()
					.sendPacketNoEvent(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
			break;
			
		case "intave":
			break;
			
		case "verus experimental":
			counter++;
			if (this.mc.thePlayer.ridingEntity != null) {
				for (Entity entity : this.mc.theWorld.getLoadedEntityList()) {
					if (entity instanceof EntityBoat) {
						final double deltaX = entity.posX - this.mc.thePlayer.posX;
						final double deltaY = entity.posY - this.mc.thePlayer.posY;
						final double deltaZ = entity.posZ - this.mc.thePlayer.posZ;
						if (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) >= 5.0) {
							continue;
						}
						mc.timer.timerSpeed = 0.3f;

						if (entity == this.mc.thePlayer.ridingEntity) {
							mc.timer.timerSpeed = 0.3f;
							continue;
						}
						int item = -1;
						double highest = 0.0;
						for (int i = 36; i < 45; ++i) {
							if (this.mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null
									&& InventoryUtils.getItemDamage(
											this.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > highest) {
								highest = InventoryUtils
										.getItemDamage(this.mc.thePlayer.inventoryContainer.getSlot(i).getStack());
								item = i - 36;
							}
						}
						if (highest == 0.0) {
							item = -1;
						}
						if (item == -1) {
							Client.INSTANCE.getCommandManager().sendChatMessage("You need to hold better weapon");

							return;
						}
						if (item != this.mc.thePlayer.inventory.currentItem) {
							this.mc.thePlayer.inventory.currentItem = item;
							return;
						}
						if (this.mc.thePlayer.getCurrentEquippedItem() == null
								|| InventoryUtils.getItemDamage(this.mc.thePlayer.getCurrentEquippedItem()) < 4.0) {
							Client.INSTANCE.getCommandManager().sendChatMessage("You need to hold better weapon");
							return;
						}
						RotationUtils.customRots = true;
						RotationUtils.serverPitch = 90;

						this.mc.thePlayer.swingItem();
						this.mc.playerController.attackEntity((EntityPlayer) this.mc.thePlayer,
								this.mc.thePlayer.ridingEntity);
						this.mc.thePlayer.swingItem();
						this.mc.playerController.attackEntity((EntityPlayer) this.mc.thePlayer, entity);
					}
				}
			}
			break;
		case "vulcan":
			break;
		case "spectate":
			break;
		}
	}

	@EventListener
	public void onWorldChange(EventWorldChange e) {
		switch (mode.getMode().toLowerCase()) {
		case "intave":
			break;
		case "verus experimental":
			break;
		case "vulcan":
			packets.clear();
			break;
		case "spectate":
			break;
		case "verus":
			packets.clear();
			disabled = false;
			break;
		}
	}

	@EventListener
	public void onHit(EventHit e) {
		switch (mode.getMode().toLowerCase()) {
		case "intave":
			if (intaveReach.isEnabled()) {
				KillAuraModule killAura = ((KillAuraModule) Client.INSTANCE.getModuleManager()
						.getModule(KillAuraModule.class));
				TeamsModule teams = (TeamsModule) Client.INSTANCE.getModuleManager().getModule(TeamsModule.class);
				EntityLivingBase entity = EntityUtils.getTargetBox(8, killAura.Targets.getMode(),
						killAura.Switch.isEnabled() ? "Switch" : "Single", 500, teams.isEnabled(),

						killAura.TroughWalls.isEnabled(), killAura.attackDead.isEnabled(),
						killAura.attackInvisible.isEnabled());

				if (entity == null)
					return;

				if (EntityUtils.getDistanceToEntityBox(entity) > 3 && killAura.isEnabled() && entity.hurtTime <= 1
						&& mc.thePlayer.onGround) {
					move = true;
				}
			}
			break;
		case "verus experimental":
			break;
		case "vulcan":
			packets.clear();
			break;
		case "spectate":
			break;
		}
	}

	private boolean intaveIncoming(Packet packet) {
		if (packet instanceof S00PacketKeepAlive || packet instanceof S32PacketConfirmTransaction
				|| packet instanceof S08PacketPlayerPosLook || packet instanceof S12PacketEntityVelocity
				|| packet instanceof S27PacketExplosion) {
			return true;
		}

		return false;
	}

	private boolean intaveOutgoing(Packet packet) {
		if (packet instanceof C03PacketPlayer || packet instanceof C0FPacketConfirmTransaction
				|| packet instanceof C00PacketKeepAlive || packet instanceof C0BPacketEntityAction) {
			return true;
		}

		return false;
	}

	private int getNullSlot() {
		int item = -1;

		for (int i = 36; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null
					&& !(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock)) {
				item = i - 36;
			}
		}

		return item;
	}

	private boolean isInventory(C0FPacketConfirmTransaction packet) {
		return packet.uid > 0 && packet.uid < 100;
	}
}
