package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.ArrayUtils;
import today.flux.event.PacketSendEvent;
import today.flux.event.PostUpdateEvent;
import today.flux.event.RespawnEvent;
import today.flux.gui.hud.notification.Notification;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Combat.AutoPot;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.ChatUtils;
import today.flux.utility.DelayTimer;
import today.flux.utility.TimeHelper;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChestStealer extends Module {
	public static boolean isChest;
	private FloatValue firstItemDelay = new FloatValue("ChestStealer", "First Item Delay", 30.0f, 0f, 1000f, 10.0f);
	private FloatValue delay = new FloatValue("ChestStealer", "Delay", 30.0f, 0f, 1000f, 10.0f);
	private BooleanValue onlychest = new BooleanValue("ChestStealer", "No Compass", true);
	private BooleanValue tools = new BooleanValue("ChestStealer", "Tools", true);
	private BooleanValue bow = new BooleanValue("ChestStealer", "Bow", true);
	private BooleanValue trash = new BooleanValue("ChestStealer", "Trash", false);
	public static BooleanValue silent = new BooleanValue("ChestStealer", "Silent Steal", false);
	public static BooleanValue respawnDisable = new BooleanValue("ChestStealer", "Auto Disable", true);

	public static BooleanValue blink = new BooleanValue("ChestStealer", "Blink", false);

	public static DelayTimer time = new DelayTimer();
	public static TimeHelper openGuiHelper = new TimeHelper();
	private final int[] itemHelmet;
	private final int[] itemChestplate;
	private final int[] itemLeggings;
	private final int[] itemBoots;

	int nextDelay = 0;

	public ChestStealer() {
		super("ChestStealer", Category.World, false);
		this.itemHelmet = new int[] { 298, 302, 306, 310, 314 };
		this.itemChestplate = new int[] { 299, 303, 307, 311, 315 };
		this.itemLeggings = new int[] { 300, 304, 308, 312, 316 };
		this.itemBoots = new int[] { 301, 305, 309, 313, 317 };
	}

	public boolean isStealing() {
		return !time.isDelayComplete(200);
	}

	@EventTarget
	public void onRespawn(RespawnEvent e) {
		isReleasing = false;
		if(respawnDisable.getValueState()) {
			this.disable();
			NotificationManager.show("Module", this.getName() + " Disabled (Auto)", Notification.Type.INFO);
		}
	}

	CopyOnWriteArrayList<Packet<?>> packets = new CopyOnWriteArrayList<>();
	boolean isReleasing = false;

	@EventTarget
	public void onPacketSend(PacketSendEvent e) {
		if (blink.getValueState() && e.getPacket() instanceof C03PacketPlayer) {
			if (mc.thePlayer.openContainer instanceof ContainerChest && isChest) {
				packets.add(e.getPacket());
				e.setCancelled(true);
				AutoPot.timer.reset();
			} else if (!packets.isEmpty() && !isReleasing) {
				isReleasing = true;
				ChatUtils.debug("Released " + packets.size() + " packets");
				for (Packet<?> packet : packets) {
					mc.getNetHandler().getNetworkManager().sendPacket(packet);
				}
				packets.clear();
				isReleasing = false;
			}
		}
	}
	
	@EventTarget
	public void onUpdate(PostUpdateEvent event) {
		if (!GuiChest.firstItem.isDelayComplete(firstItemDelay.getValueState()))
			return;

		if (!isChest && onlychest.getValueState())
			return;

		if (mc.thePlayer.openContainer != null) {
			if (mc.thePlayer.openContainer instanceof ContainerChest) {
				ContainerChest c = (ContainerChest) mc.thePlayer.openContainer;

				if (isChestEmpty(c) && openGuiHelper.isDelayComplete(800) && time.isDelayComplete(400)) {
					mc.thePlayer.closeScreen();
				}

				for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
					if (c.getLowerChestInventory().getStackInSlot(i) != null) {
						if (time.isDelayComplete(nextDelay) && (itemIsUseful(c, i) || trash.getValueState())) {
							nextDelay = (int) (delay.getValueState() * KillAura.getRandomDoubleInRange(0.75, 1.25));
							if (new Random().nextInt(100) > 80) continue; // Random
							mc.playerController.windowClick(c.windowId, i, 0, 1, mc.thePlayer);
							this.time.reset();
						}
					}
				}
			}
		}
	}

	@SoterObfuscator.Obfuscation(flags = "+native")
	private boolean isChestEmpty(ContainerChest c) {
		for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
			if (c.getLowerChestInventory().getStackInSlot(i) != null) {
				if (itemIsUseful(c, i) || trash.getValueState()) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean isPotionNegative(ItemStack itemStack) {
		ItemPotion potion = (ItemPotion) itemStack.getItem();

		List<PotionEffect> potionEffectList = potion.getEffects(itemStack);

		return potionEffectList.stream().map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
				.anyMatch(Potion::isBadEffect);
	}

	@SoterObfuscator.Obfuscation(flags = "+native")
	private boolean itemIsUseful(ContainerChest c, int i) {
		ItemStack itemStack = c.getLowerChestInventory().getStackInSlot(i);
		Item item = itemStack.getItem();

		if ((item instanceof ItemAxe || item instanceof ItemPickaxe) && tools.getValueState()) {
			return true;
		}

		if (item instanceof ItemFood)
			return true;
		if ((item instanceof ItemBow || item == Items.arrow) && bow.getValue())
			return true;
		if (item instanceof ItemPotion && !isPotionNegative(itemStack))
			return true;
		if (item instanceof ItemSword && isBestSword(c, itemStack))
			return true;
		if (item instanceof ItemArmor && isBestArmor(c, itemStack))
			return true;
		if (item instanceof ItemBlock)
			return true;

		return item instanceof ItemEnderPearl;
	}

	@SoterObfuscator.Obfuscation(flags = "+native")
	private float getSwordDamage(ItemStack itemStack) {
		float damage = 0f;
		Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
		if (attributeModifier.isPresent()) {
			damage = (float) ((AttributeModifier) attributeModifier.get()).getAmount();
		}
		return damage + EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
	}

	@SoterObfuscator.Obfuscation(flags = "+native")
	private boolean isBestSword(ContainerChest c, ItemStack item) {
		float itemdamage1 = getSwordDamage(item);
		float itemdamage2 = 0f;
		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				float tempdamage = getSwordDamage(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
				if (tempdamage >= itemdamage2)
					itemdamage2 = tempdamage;
			}
		}
		for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
			if (c.getLowerChestInventory().getStackInSlot(i) != null) {
				float tempdamage = getSwordDamage(c.getLowerChestInventory().getStackInSlot(i));
				if (tempdamage >= itemdamage2)
					itemdamage2 = tempdamage;
			}
		}
		return itemdamage1 == itemdamage2;
	}

	@SoterObfuscator.Obfuscation(flags = "+native")
	private boolean isBestArmor(ContainerChest c, ItemStack item) {
		float itempro1 = ((ItemArmor) item.getItem()).damageReduceAmount;
		float itempro2 = 0f;
		if (isContain(itemHelmet, Item.getIdFromItem(item.getItem()))) { // 头盔
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(itemHelmet,
						Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
					float temppro = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(itemHelmet,
						Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
					float temppro = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
		}

		if (isContain(itemChestplate, Item.getIdFromItem(item.getItem()))) { // 胸甲
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(itemChestplate,
						Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
					float temppro = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(itemChestplate,
						Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
					float temppro = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
		}

		if (isContain(itemLeggings, Item.getIdFromItem(item.getItem()))) { // 腿子
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(itemLeggings,
						Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
					float temppro = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(itemLeggings,
						Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
					float temppro = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
		}

		if (isContain(itemBoots, Item.getIdFromItem(item.getItem()))) { // 鞋子
			for (int i = 0; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() && isContain(itemBoots,
						Item.getIdFromItem(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()))) {
					float temppro = ((ItemArmor) mc.thePlayer.inventoryContainer.getSlot(i).getStack()
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
			for (int i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
				if (c.getLowerChestInventory().getStackInSlot(i) != null && isContain(itemBoots,
						Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem()))) {
					float temppro = ((ItemArmor) c.getLowerChestInventory().getStackInSlot(i)
							.getItem()).damageReduceAmount;
					if (temppro > itempro2)
						itempro2 = temppro;
				}
			}
		}

		return itempro1 == itempro2;
	}

	public static boolean isContain(int[] arr, int targetValue) {
		return ArrayUtils.contains(arr, targetValue);
	}
}
