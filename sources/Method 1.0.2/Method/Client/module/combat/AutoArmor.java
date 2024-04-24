package Method.Client.module.combat;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.system.Connection.Side;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;

public class AutoArmor extends Module {

    private int timer;
    Setting useEnchantments = setmgr.add(new Setting("Enchantments", this, true));
    Setting swapWhileMoving = setmgr.add(new Setting("SwapWhileMoving", this, true));
    Setting delay = setmgr.add(new Setting("Delay", this, 1, 0, 5, true));
    Setting nocurse = setmgr.add(new Setting("No Binding", this, true));
    Setting Elytra = setmgr.add(new Setting("Elytra Over Chest", this, true));


    public AutoArmor() {
        super("Auto Armor", Keyboard.KEY_NONE, Category.COMBAT, "Puts on Armor");
    }

    @Subscribe
    public void onClientTick(ClientTickEvent event) {
        if (timer > 0) {
            timer--;
            return;
        }

        if (mc.currentScreen instanceof GuiContainer
                && !(mc.currentScreen instanceof InventoryEffectRenderer))
            return;

        if (!swapWhileMoving.getValBoolean()
                && (mc.player.movementInput.moveForward != 0
                || mc.player.movementInput.moveStrafe != 0))
            return;

        for (EntityEquipmentSlot entityEquipmentSlot : EntityEquipmentSlot.values()) {
            if (entityEquipmentSlot.getSlotType() == EntityEquipmentSlot.Type.HAND) {
                continue;
            }
            ItemStack best = getBestArmor(entityEquipmentSlot);
            if (best != ItemStack.EMPTY) {
                int inventoryIndex = mc.player.inventory.getSlotFor(best);
                if (inventoryIndex != -1) {
                    swapArmor(entityEquipmentSlot, inventoryIndex);
                    break;
                }
            }
        }
    }

    private void swapArmor(EntityEquipmentSlot slot, int inventoryIndex) {
        int slotId = 8 - slot.getIndex();
        Container container = mc.player.inventoryContainer;
        mc.playerController.windowClick(container.windowId, inventoryIndex, slotId, ClickType.SWAP, mc.player);
    }

    private ItemStack getBestArmor(EntityEquipmentSlot slot) {
        NonNullList<ItemStack> inventory = mc.player.inventory.mainInventory;
        ItemStack best = ItemStack.EMPTY;

        for (ItemStack stack : inventory) {
            if (!isNullOrEmpty(stack)) {
                if (shouldEquip(mc.player.getItemStackFromSlot(slot), stack, best, slot)) {
                    best = stack;
                }
            }
        }

        return best;
    }

    public static boolean isNullOrEmpty(ItemStack stack) {
        return stack == null || stack.isEmpty();
    }

    private boolean shouldEquip(ItemStack equiped, ItemStack current, ItemStack best, EntityEquipmentSlot slot) {
        if (!((current.getItem() instanceof ItemArmor && ((ItemArmor) current.getItem()).armorType == slot) ||
                current.getItem() instanceof ItemElytra)
        )
            return false;

        if (nocurse.getValBoolean())
            if (EnchantmentHelper.hasBindingCurse(current))
                return false;
        int currentval = 0, equipedVal = 0, bestVal = 0;

        if (current.getItem() instanceof ItemArmor)
            currentval = getArmorValue((ItemArmor) current.getItem(), current, slot);
        if (!isNullOrEmpty(equiped) && equiped.getItem() instanceof ItemArmor)
            equipedVal = getArmorValue((ItemArmor) equiped.getItem(), equiped, slot);
        if (!isNullOrEmpty(best) && best.getItem() instanceof ItemArmor)
            bestVal = getArmorValue((ItemArmor) best.getItem(), best, slot);

        if (Elytra.getValBoolean() && slot == EntityEquipmentSlot.CHEST && current.getItem() instanceof ItemElytra) {
            if (!isNullOrEmpty(best))
                if (best.getItem() instanceof ItemElytra) {
                    if (current.getItemDamage() < best.getItemDamage())
                        return true;
                } else {
                    return true;
                }
            return true;
        }

        if (currentval < bestVal)
            return false;
        if (currentval > bestVal && currentval > equipedVal && best.getItem() instanceof ItemArmor)
            return true;

        if (isNullOrEmpty(equiped) && isNullOrEmpty(best)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onPacket(Object packet, Side side) {
        if (side == Side.OUT && packet instanceof CPacketClickWindow) {
            this.timer = (int) delay.getValDouble();
        }
        return true;
    }


    int getArmorValue(ItemArmor item, ItemStack stack, EntityEquipmentSlot slot) {
        int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        int armorToughness = (int) item.toughness;
        int armorType = item.getArmorMaterial().getDamageReductionAmount(slot);

        if (useEnchantments.getValBoolean()) {
            Enchantment protection = Enchantments.PROTECTION;
            int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);

            DamageSource dmgSource = DamageSource.causePlayerDamage(mc.player);
            prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        }

        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }

}
