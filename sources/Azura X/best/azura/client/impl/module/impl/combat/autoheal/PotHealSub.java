package best.azura.client.impl.module.impl.combat.autoheal;

import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.module.impl.combat.KillAura;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.player.MovementUtil;
import best.azura.client.util.player.RaytraceUtil;
import best.azura.eventbus.core.Event;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PotHealSub extends HealSub {

	public static final ModeValue throwModeValue = new ModeValue("Mode", "Change modes.", "Ground", "Ground", "Jump");
	private final BooleanValue hypixelInventoryFix = new BooleanValue("Hypixel Inv. Fix", "Fix hypixel bad client inventory move kick", false);
	private final BooleanValue noMove = new BooleanValue("No Move", "Stops you from moving when healing", false);
	private final BooleanValue disableAuraValue = new BooleanValue("Disable Aura", "Disable the Aura once you heal.", false);
	private final BooleanValue silentValue = new BooleanValue("Silent", "Make the AutoPot silent", true);
	private final BooleanValue legitValue = new BooleanValue("Legit", "Make it more legit", false);
	private final BooleanValue regenerationValue = new BooleanValue("Regeneration potions", "Use regeneration potions", true);
	private final BooleanValue speedValue = new BooleanValue("Speed potions", "Use speed potions.", true);
	private final BooleanValue healValue = new BooleanValue("Heal potions", "Use health potions", true);
	private final NumberValue<Double> healthValue = new NumberValue<>("Health", "Heal once the you are on the HP level",
			() -> regenerationValue.getObject() || healValue.getObject(), 10D, 1D, 5D, 20D);
	private final ArrayList<Packet<?>> additionalPackets = new ArrayList<>();
	private final DelayUtil potDelay = new DelayUtil();
	private boolean potting, doHealth;
	private int ticks, lastSlot, currentSlot, disableTicks;
	private float pitch;
	private KillAura killAura = null;

	@Override
	public String getName() {
		return "AutoPot";
	}

	@Override
	public List<Value<?>> getValues() {
		return Arrays.asList(throwModeValue, hypixelInventoryFix, noMove, disableAuraValue, silentValue,
				legitValue, regenerationValue, healthValue, speedValue, healValue);
	}

	@Override
	public void handle(Event event) {
		if (killAura == null) killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
		try {
			if (event instanceof EventMotion) {
				EventMotion e = (EventMotion) event;
				if (potting) {
					if (disableAuraValue.getObject()) {
						killAura.targets.clear();
						killAura.disable = true;
						disableTicks = 1;
					}
					if (!legitValue.getObject()) e.pitch = pitch;
				} else if (disableTicks != 0) {
					if (legitValue.getObject()) mc.thePlayer.rotationPitch = pitch;
					disableTicks = 0;
					killAura.disable = false;
				}
			}
			if (event instanceof EventMotion) {
				EventMotion e = (EventMotion) event;
				if (e.isUpdate()) {
					if (potting && MovementUtil.isOverVoid()) {
						reset();
						potting = false;
						return;
					}
					final MovingObjectPosition obj = RaytraceUtil.rayTrace(3, mc.thePlayer.rotationYaw, 90.0F);
					if (mc.thePlayer.getHealth() < healthValue.getObject()) doHealth = true;
					if (!potting) {
						currentSlot = -1;
						findSlot();
					}
					if (currentSlot == -1) {
						findSlotInv();
						findSlot();
						reset();
						return;
					}
					if (potting) {
						if (legitValue.getObject()) {
							noMove.setObject(false);
						}
						if (ticks == 0) {
							pitch = throwModeValue.getObject().equals("Jump") ? -90.0F : 90;
							if (legitValue.getObject()) {
								float oldPitch = mc.thePlayer.rotationPitch;
								mc.thePlayer.rotationPitch = throwModeValue.getObject().equals("Jump") ? -90.0F : 90;
								pitch = oldPitch;
							}
						} else {
							if (noMove.getObject()) {
								if (obj != null && obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
									double diffX = mc.thePlayer.posX - mc.thePlayer.prevPosX;
									double diffZ = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
									mc.thePlayer.setPosition(mc.thePlayer.posX - diffX, mc.thePlayer.posY, mc.thePlayer.posZ - diffZ);
								}
							}
						}
						if (ticks > 30) {
							if (silentValue.getObject() && potting)
								mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
							reset();
							return;
						}
						if (ticks > 5) {
							if (silentValue.getObject() && potting)
								mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
							potting = false;
							ticks++;
							return;
						}
						if (legitValue.getObject()) {
							if (mc.thePlayer.inventory.currentItem != currentSlot) {
								lastSlot = mc.thePlayer.inventory.currentItem;
								mc.thePlayer.inventory.currentItem = currentSlot;
							}
							if (ticks == 2) {
								if (obj == null || obj.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;
								mc.rightClickMouse();
							}
							if (ticks == 3) {
								if (!potDelay.hasReached(450)) return;
								reset();
								mc.thePlayer.inventory.currentItem = lastSlot;
							}
						} else {
							if (ticks == 0) {
								if (throwModeValue.getObject().equals("Jump") && (!mc.thePlayer.onGround || MovementUtil.isOverVoid()))
									return;
							}
							if (!silentValue.getObject() && mc.thePlayer.inventory.currentItem != currentSlot) {
								lastSlot = mc.thePlayer.inventory.currentItem;
								mc.thePlayer.inventory.currentItem = currentSlot;
							}
							if (ticks == 2) {
								if (obj == null || obj.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;
								if (silentValue.getObject())
									mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(currentSlot));
								mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(currentSlot));
								mc.playerController.onStoppedUsingItem(mc.thePlayer);
								if (throwModeValue.getObject().equals("Jump") && mc.thePlayer.onGround)
									mc.thePlayer.jump();
								if (silentValue.getObject())
									mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
								else mc.thePlayer.inventory.currentItem = lastSlot;

							}
							if (ticks == 3) {
								if (!potDelay.hasReached(350)) return;
								doHealth = false;
							}
							if (ticks >= 4) {
								boolean doReset = true;
								for (int i = 0; i <= 8; i++) {
									ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
									if (stack == null) continue;
									if ((doHealth && healValue.getObject() && isPotionValid(stack, Potion.heal))
											|| (speedValue.getObject() && isPotionValid(stack, Potion.moveSpeed))
											|| (doHealth && regenerationValue.getObject() && isPotionValid(stack, Potion.regeneration))) {
										doReset = false;
										break;
									}
								}
								if (doReset) reset();
							}
						}
						ticks++;
					} else {
						if (!additionalPackets.isEmpty()) {
							additionalPackets.forEach(mc.thePlayer.sendQueue::addToSendQueue);
							additionalPackets.clear();
						}
					}
				}
			}
			if (event instanceof EventSentPacket) {
				EventSentPacket e = (EventSentPacket) event;
				if (e.getPacket() instanceof C09PacketHeldItemChange && potting && silentValue.getObject() && !legitValue.getObject()) {
					e.setCancelled(true);
					additionalPackets.add(e.getPacket());
				}
			}
		} catch (Exception exception) {
			if (silentValue.getObject() && potting && ticks > 0)
				mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
			reset();
		}
	}

	private void findSlot() {
		for (int i = 0; i <= 8; i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			if (stack == null) continue;
			if ((doHealth && healValue.getObject() && isPotionValid(stack, Potion.heal))
					|| (speedValue.getObject() && isPotionValid(stack, Potion.moveSpeed))
					|| (doHealth && regenerationValue.getObject() && isPotionValid(stack, Potion.regeneration))) {
				currentSlot = i;
				potting = true;
				break;
			}
		}
	}

	private void findSlotInv() {
		final boolean fixInv = hypixelInventoryFix.getObject() && !(mc.currentScreen instanceof GuiContainer);
		for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
			ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
			if (stack == null) continue;
			if ((doHealth && healValue.getObject() && isPotionValid(stack, Potion.heal))
					|| (speedValue.getObject() && isPotionValid(stack, Potion.moveSpeed))
					|| (doHealth && regenerationValue.getObject() && isPotionValid(stack, Potion.regeneration))) {
				if (fixInv)
					mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
				mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, getFreeSlot(), 2, mc.thePlayer);
				if (fixInv)
					mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
				break;
			}
		}
	}

	private boolean isPotionValid(ItemStack check, Potion needed) {
		if (mc.thePlayer.getActivePotionEffects().stream().anyMatch(e -> e.getPotionID() == needed.getId()))
			return false;
		if (check.getItem() instanceof ItemPotion) {
			if (ItemPotion.isSplash(check.getMetadata())) {
				ItemPotion potion = (ItemPotion) check.getItem();
				return potion.getEffects(check.getMetadata()).stream().anyMatch(e -> e.getPotionID() == needed.getId());
			}
		}
		return false;
	}

	private void reset() {
		potting = false;
		potDelay.reset();
		ticks = 0;
		doHealth = false;
	}

	private int getFreeSlot() {
		if (mc.thePlayer.getHeldItem() == null) return mc.thePlayer.inventory.currentItem;
		else for (int i = 0; i < 9; i++) if (mc.thePlayer.inventory.getStackInSlot(i) == null) return i;
		return 5;
	}

}