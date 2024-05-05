package ru.smertnix.celestial.feature.impl.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.movement.MovementUtils;

import java.util.ArrayList;
import java.util.List;

public class WaterSpeed extends Feature {

	public static ListSetting wsModeSetting = new ListSetting("WaterSpeed Mode", "Matrix", () -> true, "Matrix", "ReallyWorld");

	private NumberSetting waterSpeed = new NumberSetting("Speed", 0.4f, 0.1f, 2.f, 0.01f, () -> wsModeSetting.currentMode.equalsIgnoreCase("Matrix"));
	private BooleanSetting speedCheck = new BooleanSetting("Speed Potion Check", false, () -> wsModeSetting.currentMode.equalsIgnoreCase("Matrix"));

	private BooleanSetting miniJump = new BooleanSetting("Mini Jump", true, () -> wsModeSetting.currentMode.equalsIgnoreCase("ReallyWorld"));
	private BooleanSetting boost = new BooleanSetting("Boost", true, () -> wsModeSetting.currentMode.equalsIgnoreCase("ReallyWorld"));

	private float waterTicks = 0;

	    public WaterSpeed() {
	        super("WaterSpeed", "Позволяет прыгать на водичке, хи хи" , FeatureCategory.Movement);
			addSettings(wsModeSetting, waterSpeed, speedCheck, miniJump, boost);
	    }

	@EventTarget
	public void onUpdate(EventUpdate eventUpdate) {
		if (wsModeSetting.currentMode.equalsIgnoreCase("Matrix")) {
			if (speedCheck.getBoolValue() && !mc.player.isPotionActive(MobEffects.SPEED) || !mc.player.isInWater()) return;
			MovementUtils.setMotion(waterSpeed.getNumberValue());
		}
		if (wsModeSetting.currentMode.equalsIgnoreCase("ReallyWorld")) {
			if (MovementUtils.isInLiquid()) {
				waterTicks = 10;
			} else {
				waterTicks--;
			}
			if (mc.player.isCollidedHorizontally || waterTicks < 0 || !mc.player.isInWater()
					|| !mc.player.isPotionActive(MobEffects.SPEED)) return;

			List<ItemStack> stacks = new ArrayList<>();
			mc.player.getArmorInventoryList().forEach(stacks::add);
			stacks.removeIf(w -> w.getItem() instanceof ItemAir);
			for (ItemStack stack : stacks) {
				if (stack != null) {
					if (buildEnchantments(stack, 1)) {
						MovementUtils.setMotion(0.445f);
						if (mc.gameSettings.keyBindJump.isKeyDown()) {
							mc.player.motionY = 0.12f;
						} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
							mc.player.motionY = -0.35f;
						}
					} else if (buildEnchantments(stack, 2)) {
						MovementUtils.setMotion(0.4f);
						if (mc.gameSettings.keyBindJump.isKeyDown()) {
							mc.player.motionY = 0.12f;
						} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
							mc.player.motionY = -0.35f;
						}
					} else if (buildEnchantments(stack, 3)) {
						MovementUtils.setMotion(boost.getBoolValue() ? 0.45f : 0.34);
						if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isSneaking() && !(!miniJump.getBoolValue() && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock()
								instanceof BlockLiquid && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1,
								mc.player.posZ)).getBlock() instanceof BlockAir)) {
							mc.player.motionY = 0.12f;
						} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
							mc.player.motionY = -0.35f;
						}
					}
				}
			}
		}
	}

	@Override
	public void onDisable() {
		waterTicks = 0;
		super.onDisable();
	}

	public boolean buildEnchantments(ItemStack stack, float strenght) {

		if (stack.getItem() instanceof ItemArmor) {
			return EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack) == strenght;
		}

		return false;
	}
}
