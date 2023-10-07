package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import cc.novoline.utils.Timer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.gui.screen.setting.Manager.put;
import static cc.novoline.gui.screen.setting.SettingType.CHECKBOX;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.booleanFalse;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.createInt;
import static net.minecraft.enchantment.Enchantment.*;
import static net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel;

public class AutoArmor extends AbstractModule {

    /* fields */
    private final Timer timer = new Timer();

    /* properties @off */
    @Property("equip-delay")
    private final IntProperty equipDelay = createInt(3).minimum(1).maximum(10);
    @Property("open-inventory")
    private final BooleanProperty open_inv = booleanFalse();

    /* constructors @on */
    public AutoArmor(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "AutoArmor", "Auto Armor", EnumModuleType.PLAYER, "Equips armor for you");
        put(new Setting("Equip_Delay", "Delay", SettingType.SLIDER, this, equipDelay, 1));
        put(new Setting("AA_OPEN_INV", "Open Inventory", CHECKBOX, this, this.open_inv));
    }

    /* methods */
    private float getProtection(@NonNull ItemStack stack) {
        float protection = 0;

        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();

            protection += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * getEnchantmentLevel(
                    Enchantment.protection.effectId, stack) * 0.0075D;
            protection += getEnchantmentLevel(blastProtection.effectId, stack) / 100d;
            protection += getEnchantmentLevel(fireProtection.effectId, stack) / 100d;
            protection += getEnchantmentLevel(thorns.effectId, stack) / 100d;
            protection += getEnchantmentLevel(unbreaking.effectId, stack) / 50d;
            protection += getEnchantmentLevel(projectileProtection.effectId, stack) / 100d;
        }

        return protection;
    }

    @EventTarget
    public void onEvent(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (!(mc.currentScreen instanceof GuiInventory) && open_inv.get() || ServerUtils.serverIs(Servers.PRE) || ServerUtils.serverIs(Servers.LOBBY) || getModule(ChestStealer.class).isStealing()) {
                return;
            }

            if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat) {
                for (int type = 1; type < 5; type++) {
                    if (mc.player.inventoryContainer.getSlot(4 + type).getHasStack()) {
                        ItemStack slotStack = mc.player.inventoryContainer.getSlot(4 + type).getStack();

                        if (isBestArmor(slotStack, type)) {
                            continue;
                        } else {
                            mc.player.drop(4 + type);
                        }
                    }

                    for (int i = 9; i < 45; i++) {
                        if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                            ItemStack slotStack = mc.player.inventoryContainer.getSlot(i).getStack();

                            if (isBestArmor(slotStack, type) && getProtection(slotStack) > 0) {
                                if (timer.check(equipDelay.get() * 50)) {
                                    mc.player.shiftClick(i);
                                    timer.reset();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isWorking() {
        return !timer.check(equipDelay.get() * 100);
    }

    boolean isBestArmor(@NonNull ItemStack stack, int type) {
        String strType = "";

        switch (type) {
            case 1:
                strType = "helmet";
                break;
            case 2:
                strType = "chestplate";
                break;
            case 3:
                strType = "leggings";
                break;
            case 4:
                strType = "boots";
                break;
        }

        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }

        float protection = getProtection(stack);

        for (int i = 5; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if (getProtection(is) > protection && is.getUnlocalizedName().contains(strType)) return false;
            }
        }

        return true;
    }

    //region Lombok
    public IntProperty getEquipDelay() {
        return equipDelay;
    }
    //endregion
}
