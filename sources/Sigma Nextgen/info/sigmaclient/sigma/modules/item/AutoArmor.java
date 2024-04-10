//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\1\Desktop\Minecraft-Deobfuscator3000-1.2.3\config"!

package info.sigmaclient.sigma.modules.item;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import top.fl0wowp4rty.phantomshield.annotations.Native;
import net.minecraft.item.*;


public class AutoArmor extends Module {

    private final NumberValue delay = new NumberValue("Delay", 0.1, 1, 0, NumberValue.NUMBER_TYPE.FLOAT);
    private final BooleanValue onlyWhileNotMoving = new BooleanValue("Stop when moving", false);
    private final BooleanValue invOnly = new BooleanValue("Inventory only", true);
    private final TimerUtil timer = new TimerUtil();
    public static boolean isWearing = false;
    ItemStack checkItem = null;
    int checkSlot = -1;
    public static void swap(int slot, int hSlot) {
        mc.playerController.windowClickFixed(mc.player.container.windowId, hSlot, slot, ClickType.SWAP, mc.player, false);
    }
    public AutoArmor() {
        super("AutoArmor", Category.Item, "Automatically equips armor");
     registerValue(delay);
     registerValue(onlyWhileNotMoving);
     registerValue(invOnly);
    }

    public void onUpdateEvent(UpdateEvent e) {
        if (e.isPost()) return;
        if ((invOnly.isEnable() && !(mc.currentScreen instanceof InventoryScreen)) || (onlyWhileNotMoving.isEnable() && MovementUtils.isMoving())) {
            return;
        }
        if (mc.player.openContainer instanceof ChestContainer) {
            // so it doesn't put on armor immediately after closing a chest
            timer.reset();
        }
        if (timer.hasTimeElapsed(delay.getValue().floatValue() * 10 * 50)) {
            isWearing = false;
            for (int armorSlot = 5; armorSlot < 9; armorSlot++) {
                if (equipBest(armorSlot)) {
                    timer.reset();
                    break;
                }
            }
        }
    }
    public static void drop(int slot) {
        mc.playerController.windowClickFixed(mc.player.container.windowId, slot, 0, ClickType.THROW, mc.player, false);
    }
    public static void click(int slot, int mouseButton, boolean shiftClick) {
        mc.playerController.windowClickFixed(mc.player.container.windowId, slot, mouseButton, shiftClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, mc.player, false);
    }

    private boolean equipBest(int armorSlot) {
        int equipSlot = -1, currProt = -1;
        ArmorItem currItem = null;
        ItemStack slotStack = mc.player.container.getSlot(armorSlot).getStack();
        if (slotStack != ItemStack.EMPTY && slotStack.getItem() instanceof ArmorItem) {
            currItem = (ArmorItem) slotStack.getItem();
            currProt = currItem.getDamageReduceAmount() + EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), mc.player.container.getSlot(armorSlot).getStack());
        }
        // find best piece
        for (int i = 9; i < 45; i++) {
            ItemStack is = mc.player.container.getSlot(i).getStack();
            if (is != ItemStack.EMPTY && is.getItem() instanceof ArmorItem) {
                int prot = ((ArmorItem) is.getItem()).getDamageReduceAmount() + EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), is);
                if ((currItem == null || currProt < prot)
                        && isValidPiece(armorSlot, (ArmorItem) is.getItem())
                ) {
                    currItem = (ArmorItem) is.getItem();
                    equipSlot = i;
                    currProt = prot;
                }
            }
        }
        // equip best piece (if there is a better one)
        if (equipSlot != -1) {
            isWearing = true;
            if (slotStack.getItem() instanceof ArmorItem) {
//                mc.player.inventory.armorInventory.set(armorSlot - 5, ItemStack.EMPTY);
                drop(armorSlot);
            } else {
//                mc.player.inventory.armorInventory.set(armorSlot - 5, mc.player.inventory.getStackInSlot(equipSlot));
//                drop(armorSlot);
                click(equipSlot, 0, true);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        isWearing = false;
        super.onDisable();
    }

    private boolean isValidPiece(int armorSlot, ArmorItem item) {
        String unlocalizedName = item.getTranslationKey();
        return armorSlot == 5 && unlocalizedName.contains("helmet")
                || armorSlot == 6 && unlocalizedName.contains("chestplate")
                || armorSlot == 7 && unlocalizedName.contains("leggings")
                || armorSlot == 8 && unlocalizedName.contains("boots");
    }
}
