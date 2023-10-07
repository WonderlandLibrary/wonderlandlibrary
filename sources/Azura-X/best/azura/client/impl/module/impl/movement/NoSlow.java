package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventSlowDown;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.module.impl.combat.KillAura;
import best.azura.client.util.math.MathUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.dependency.ModeDependency;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

@SuppressWarnings("unused")
@ModuleInfo(name = "No Slow", description = "Doesn't slow you when right clicking", category = Category.MOVEMENT)
public class NoSlow extends Module {

	private final ModeValue mode = new ModeValue("Mode", "Bypass mode", "Vanilla", "Vanilla", "Watchdog", "AAC4", "IntaveB13", "Intave B14", "NCP");
	private final BooleanValue newMode = new BooleanValue("New", "Use the newer mode of the No Slow", new ModeDependency(mode, "Watchdog"), true);

	@EventHandler
    public final Listener<EventMotion> eventMotionListener = eventMotion -> {
		setSuffix(mode.getObject());
		KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule(KillAura.class);
		if (mc.thePlayer.isUsingItem() && !(killAura.isEnabled() && !killAura.targets.isEmpty())) {
			switch (mode.getObject()) {
				case "Vanilla":
					break;
				case "Watchdog":
					if (!newMode.getObject()) {
						if (eventMotion.isPre()) {
							if (mc.thePlayer.getCurrentEquippedItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
									|| mc.currentScreen instanceof GuiChat) break;
							mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
									BlockPos.ORIGIN, EnumFacing.DOWN));
						}
						if (eventMotion.isPost()) {
							if (mc.thePlayer.getCurrentEquippedItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
									|| mc.currentScreen instanceof GuiChat) break;
							mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
						}
					} else {
						/*if (eventMotion.isPre() && !(killAura.isEnabled() && killAura.block && !killAura.disableNoSlow)) {
							double motionY = MathUtil.getDifference(mc.thePlayer.posY, mc.thePlayer.lastTickPosY);
							if (mc.thePlayer.ticksExisted % 2 == 0 && motionY <= 0 && mc.thePlayer.motionY <= 0 && mc.thePlayer.onGround && mc.thePlayer.isMoving()
									&& mc.thePlayer.getItemInUseCount() > 2) {
								eventMotion.y += MathUtil.getRandom_double(0.08, 0.13);
								eventMotion.onGround = false;
							}
						}*/
						final boolean sword = mc.thePlayer.getHeldItem().getItem() instanceof ItemSword,
							bow = mc.thePlayer.getHeldItem().getItem() instanceof ItemBow;
						if (bow) {
							if (eventMotion.isPre()) {
								double motionY = MathUtil.getDifference(mc.thePlayer.posY, mc.thePlayer.lastTickPosY);
								if (mc.thePlayer.ticksExisted % 2 == 0 && motionY <= 0 && mc.thePlayer.motionY <= 0 && mc.thePlayer.onGround && mc.thePlayer.isMoving()
										&& mc.thePlayer.getItemInUseCount() > 2) {
									eventMotion.y += MathUtil.getRandom_double(0.08, 0.13);
									eventMotion.onGround = false;
								}
							}
						} else {
							if (eventMotion.isUpdate() && (mc.thePlayer.ticksExisted % 3 == 0 || mc.thePlayer.getItemInUseCount() < 3)) {
								if (mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion && ItemPotion.isSplash(mc.thePlayer.getHeldItem().getMetadata())) break;
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem - 1 < 0 ? 8 : mc.thePlayer.inventory.currentItem - 1));
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
							}
						}
					}
					break;
				case "AAC4":
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					break;
				case "IntaveB13":
					if (eventMotion.isUpdate()) {
						if (mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion && ItemPotion.isSplash(mc.thePlayer.getHeldItem().getMetadata())) break;
						mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem - 1 < 0 ? 8 : mc.thePlayer.inventory.currentItem - 1));
						mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					}
					break;
				case "Intave B14":
					if (eventMotion.isUpdate() && (mc.thePlayer.ticksExisted % 3 == 0 || mc.thePlayer.getItemInUseCount() < 3)) {
						if (mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion && ItemPotion.isSplash(mc.thePlayer.getHeldItem().getMetadata())) break;
						mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem - 1 < 0 ? 8 : mc.thePlayer.inventory.currentItem - 1));
						mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					}
					break;
				case "NCP":
					if (eventMotion.isUpdate()) mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
					break;
			}
		}
	};

	@EventHandler
    public final Listener<EventSlowDown> eventSlowDownListener = eventSlowDown -> {
		eventSlowDown.setForward(1.0f);
		eventSlowDown.setStrafe(1.0f);
		eventSlowDown.setStopSprint(false);
	};

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
