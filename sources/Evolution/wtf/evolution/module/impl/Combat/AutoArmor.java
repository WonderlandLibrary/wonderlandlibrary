package wtf.evolution.module.impl.Combat;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.helpers.animation.Counter;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.SliderSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@ModuleInfo(name = "AutoArmor", type = Category.Combat)
public class AutoArmor extends Module {
    public BooleanSetting openInventory = new BooleanSetting("openInventory", true).call(this);
    public SliderSetting delay = new SliderSetting("Delay", 1F, 0.1F, 10F, 0.1F).call(this);
    private final Counter timerUtils = new Counter();


    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (!(mc.currentScreen instanceof GuiInventory) && openInventory.get()) {
            return;
        }
        if (mc.currentScreen instanceof GuiContainer
                && !(mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        final InventoryPlayer inventory = mc.player.inventory;
        final int[] ArmorSlots = new int[4];
        final int[] ArmorValues = new int[4];
        for (int type = 0; type < 4; ++type) {
            ArmorSlots[type] = -1;
            final ItemStack stack = inventory.armorItemInSlot(type);
            if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                final ItemArmor item = (ItemArmor) stack.getItem();
                ArmorValues[type] = this.getArmorValue(item, stack);
            }
        }
        for (int slot = 0; slot < 36; ++slot) {
            final ItemStack stack = inventory.getStackInSlot(slot);
            if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                final ItemArmor item = (ItemArmor) stack.getItem();
                final int armorType = item.armorType.getIndex();
                final int armorValue = this.getArmorValue(item, stack);
                if (armorValue > ArmorValues[armorType]) {
                    ArmorSlots[armorType] = slot;
                    ArmorValues[armorType] = armorValue;
                }
            }
        }
        final ArrayList<Integer> types = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(types);
        for (final int i : types) {
            int j = ArmorSlots[i];
            if (j == -1) {
                continue;
            }
            final ItemStack oldArmor = inventory.armorItemInSlot(i);
            if (!isNullOrEmpty(oldArmor) && inventory.getFirstEmptyStack() == -1) {
                continue;
            }
            if (j < 9) {
                j += 36;
            }
            if (this.timerUtils.hasReached(this.delay.get() * 100.0f)) {
                if (!isNullOrEmpty(oldArmor)) {
                    mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, mc.player);
                }
                mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, mc.player);
                this.timerUtils.reset();
                break;
            }
            break;
        }
    }


    private int getArmorValue(final ItemArmor item, final ItemStack stack) {
        final int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        final int armorToughness = (int) item.toughness;
        final int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        final Enchantment protection = Enchantments.PROTECTION;
        final int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
        final DamageSource dmgSource = DamageSource.causePlayerDamage(mc.player);
        prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }

    public static boolean isNullOrEmpty(final ItemStack stack) {
        return stack == null || stack.isEmpty();
    }
}
