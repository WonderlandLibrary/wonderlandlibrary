package today.flux.module.implement.Player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.DelayTimer;
import today.flux.utility.InvUtils;

/**
 * Created by John on 2016/10/19.
 */
public class InvCleaner extends Module {
	public static DelayTimer delayTimer = new DelayTimer();

	private double handitemAttackValue;
	private int currentSlot = 9;
	public static BooleanValue keepTools = new BooleanValue("InvCleaner", "Best Tools", false);
	public static BooleanValue keepArmor = new BooleanValue("InvCleaner", "Best Armor", false);
	public static BooleanValue keepBow = new BooleanValue("InvCleaner", "Best Bow", true);
	public static BooleanValue keepBucket = new BooleanValue("InvCleaner", "Keep Buckets", false);
	public static BooleanValue keepArrow = new BooleanValue("InvCleaner", "Keep Arrows", true);
	public static FloatValue delay = new FloatValue("InvCleaner", "Delay", 80.0f, 10.0f, 500f, 10.0f, "ms");

	public static BooleanValue toggle = new BooleanValue("InvCleaner", "Auto Toggle", true);
	public static BooleanValue inInv = new BooleanValue("InvCleaner", "Inv Only", true);
	public static BooleanValue toggleTools = new BooleanValue("InvCleaner", "Manage Tools", true);

	public InvCleaner() {
		super("InvCleaner", Category.Player, false);
	}

	public void onEnable() {
		super.onEnable();
		currentSlot = 9; // EXCEPT ARMOR SLOT
		handitemAttackValue = getSwordAttackDamage(mc.thePlayer.getHeldItem());
	}

	@EventTarget
	public void onUpdate(WorldRenderEvent event) {
		if (ModuleManager.autoArmorMod.isWearing() || ModuleManager.chestStealerMod.isStealing()) {
			return;
		}

		if (!isEnabled() || mc.currentScreen instanceof GuiChest || KillAura.target != null) {
			return;
		}

		if (mc.thePlayer.openContainer != null) {
			if (mc.thePlayer.openContainer instanceof ContainerChest) {
				return;
			}
		}

		if (currentSlot >= 45) {
			currentSlot = 9;
			if (toggleTools.getValueState() && (mc.thePlayer.ticksExisted % 40 == 0 || toggle.getValueState())) {
				InvUtils.getBestAxe();
				InvUtils.getBestPickaxe();
				InvUtils.getBestShovel();
			}
			if (toggle.getValueState()) {
				this.toggle();
				return;
			}
		}


		if (mc.currentScreen instanceof GuiInventory || !inInv.getValueState()) {
			handitemAttackValue = getSwordAttackDamage(mc.thePlayer.getHeldItem());
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(currentSlot).getStack();
			if(delayTimer.hasPassed(delay.getValue())) {
				if (isShit(currentSlot) && getSwordAttackDamage(itemStack) < handitemAttackValue && itemStack != mc.thePlayer.getHeldItem()) {
					mc.playerController.windowClick(0, currentSlot, 1, 4, mc.thePlayer);
					delayTimer.reset();
				}
				currentSlot++;
			}
		}
	}

	private static int getItemAmount(Item shit) {
		int result = 0;

		for (Object item : mc.thePlayer.inventoryContainer.inventorySlots) {
			Slot slot = (Slot) item;

			if (slot.getHasStack() && slot.getStack().getItem() == shit)
				result++;
		}
		return result;
	}

	public static boolean isShit(int slot) {
		ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();

		if (itemStack == null)
			return false;

		if (itemStack.getItem() == Items.stick)
			return true;

		if (itemStack.getItem() == Items.egg)
			return true;

		if (itemStack.getItem() == Items.bone)
			return true;

		if (itemStack.getItem() == Items.bowl)
			return true;

		if (itemStack.getItem() == Items.glass_bottle)
			return true;

		if (itemStack.getItem() == Items.string)
			return true;

		if (itemStack.getItem() == Items.flint && getItemAmount(Items.flint) > 1)
			return true;

		if (itemStack.getItem() == Items.compass && getItemAmount(Items.compass) > 1)
			return true;

		if (itemStack.getItem() == Items.feather)
			return true;

		if (itemStack.getItem() == Items.fishing_rod)
			return true;

		// buckets
		if (itemStack.getItem() == Items.bucket && !keepBucket.getValue())
			return true;

		if (itemStack.getItem() == Items.lava_bucket && !keepBucket.getValue())
			return true;

		if (itemStack.getItem() == Items.water_bucket && !keepBucket.getValue())
			return true;

		if (itemStack.getItem() == Items.milk_bucket && !keepBucket.getValue())
			return true;

		// arrow
		if (itemStack.getItem() == Items.arrow && !keepArrow.getValue())
			return true;

		if (itemStack.getItem() == Items.snowball)
			return true;

		if (itemStack.getItem() == Items.fish)
			return true;

		if (itemStack.getItem() == Items.experience_bottle)
			return true;

		// tools
		if (itemStack.getItem() instanceof ItemTool && (!keepTools.getValue() || !isBestTool(itemStack)))
			return true;

		// sword
		if (itemStack.getItem() instanceof ItemSword && (!keepTools.getValue() || !isBestSword(itemStack)))
			return true;

		// armour
		if (itemStack.getItem() instanceof ItemArmor && (!keepArmor.getValue() || !isBestArmor(itemStack)))
			return true;

		// bow
		if (itemStack.getItem() instanceof ItemBow && (!keepBow.getValue() || !isBestBow(itemStack)))
			return true;

		if (itemStack.getItem().getUnlocalizedName().contains("potion")) {
			if (isBadPotion(itemStack))
				return true;
		}

		return false;
	}

	private static boolean isBestTool(ItemStack input) {
		for (ItemStack itemStack : InvUtils.getAllInventoryContent()) {
			if (itemStack == null)
				continue;

			if (!(itemStack.getItem() instanceof ItemTool))
				continue;

			if (itemStack == input)
				continue;

			if (itemStack.getItem() instanceof ItemPickaxe && !(input.getItem() instanceof ItemPickaxe))
				continue;

			if (itemStack.getItem() instanceof ItemAxe && !(input.getItem() instanceof ItemAxe))
				continue;

			if (itemStack.getItem() instanceof ItemSpade && !(input.getItem() instanceof ItemSpade))
				continue;

			if (getToolEffencly(itemStack) >= getToolEffencly(input))
				return false;
		}
		return true;
	}

	private static boolean isBestSword(ItemStack input) {
		for (ItemStack itemStack : InvUtils.getAllInventoryContent()) {
			if (itemStack == null)
				continue;

			if (!(itemStack.getItem() instanceof ItemSword))
				continue;

			if (itemStack == input)
				continue;

			if (getSwordAttackDamage(itemStack) >= getSwordAttackDamage(input))
				return false;
		}
		return true;
	}

	private static boolean isBestBow(ItemStack input) {
		for (ItemStack itemStack : InvUtils.getAllInventoryContent()) {
			if (itemStack == null)
				continue;

			if (!(itemStack.getItem() instanceof ItemBow))
				continue;

			if (itemStack == input)
				continue;

			if (getBowAttackDamage(itemStack) >= getBowAttackDamage(input))
				return false;
		}
		return true;
	}

	public static boolean isBestArmor(ItemStack input) {
		for (ItemStack itemStack : InvUtils.getAllInventoryContent()) {
			if (itemStack == null)
				continue;

			if (!(itemStack.getItem() instanceof ItemArmor))
				continue;

			if (itemStack == input)
				continue;

			if (((ItemArmor) itemStack.getItem()).armorType != ((ItemArmor) input.getItem()).armorType)
				continue;

			if (InvUtils.getArmorScore(itemStack) >= InvUtils.getArmorScore(input))
				return false;
		}
		return true;
	}

	private static boolean isBadPotion(final ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion) {
			final ItemPotion potion = (ItemPotion) stack.getItem();
			for (final Object o : potion.getEffects(stack)) {
				final PotionEffect effect = (PotionEffect) o;
				if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.moveSlowdown.getId()
						|| effect.getPotionID() == Potion.harm.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	private static double getSwordAttackDamage(ItemStack itemStack) {
		if (itemStack == null || !(itemStack.getItem() instanceof ItemSword))
			return 0;

		ItemSword itemSword = (ItemSword) itemStack.getItem();

		double result = 1.0;

		result += itemSword.getDamageVsEntity();

		result += 1.25 * EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
		result += 0.5 * EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack);

		return result;
	}

	private static double getBowAttackDamage(ItemStack itemStack) {
		if (itemStack == null || !(itemStack.getItem() instanceof ItemBow))
			return 0;

		return EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack)
				+ (EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack) * 0.1)
				+ (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) * 0.1);
	}

	private static double getToolEffencly(ItemStack itemStack) {
		if (itemStack == null || !(itemStack.getItem() instanceof ItemTool))
			return 0;

		ItemTool sword = (ItemTool) itemStack.getItem();

		return EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack)
				+ sword.getEfficiencyOnProperMaterial();
	}

}
