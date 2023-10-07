package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.command.impl.Xray;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arithmo on 5/3/2017 aat 12:48 AM.
 */
public class InventoryCleaner extends Module {
    //TODO: ADD THE FUCKING NIGGER OPTIONS FOR DIFFERENT ITEMS
    public static List<Integer> blacklistedItems = new ArrayList<>();

    private final String TOGGLE = "TOGGLE";
    private final String BLOCKCAP = "BLOCKCAP";
    private final String ARCHERY = "ARCHERY";
    private final String FOOD = "FOOD";

    private Timer timer = new Timer();

    public InventoryCleaner(ModuleData data) {
        super(data);
        settings.put(TOGGLE, new Setting<>(TOGGLE, false, "Turn off when finished."));
        settings.put(ARCHERY, new Setting<>(ARCHERY, false, "Clean bows and arrows."));
        settings.put(FOOD, new Setting<>(FOOD, false, "Clean food. Keeps Golden Apples."));
        settings.put(BLOCKCAP, new Setting<>(BLOCKCAP, 128, "Max blocks allowed in your inventory.", 8, -1, 256));
        Xray.loadIDs();
    }

    private static boolean isCleaning;

    public static boolean isCleaning() {
        return isCleaning;
    }

    public void onEnable() {
        isCleaning = false;
    }

    public void onDisable() {
        isCleaning = false;
    }

    @RegisterEvent(events = EventUpdate.class)
    public void onEvent(Event event) {
        EventUpdate em = (EventUpdate) event;
        if (em.isPre() && mc.thePlayer != null && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) && timer.delay(100)) {
            for (int i = 9; i < 45; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (isBad(is) && (is != mc.thePlayer.getCurrentEquippedItem())) {
                        isCleaning = true;
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 0, mc.thePlayer);
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, mc.thePlayer);
                        timer.reset();
                        break;
                    }
                }
                if (i == 44 && (Boolean) settings.get(TOGGLE).getValue()) {
                    isCleaning = false;
                    toggle();
                } else if (i == 44 && isCleaning) {
                    isCleaning = false;
                }
            }
        }
    }

    private boolean isBad(ItemStack item) {
        if (item != null && item.getItem() instanceof ItemSword) {
            int swordCount = 0;
            for (int i = 9; i < 45; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (is.getItem() instanceof ItemSword) {
                        swordCount++;
                    }
                }
            }
            for (int i = 9; i < 45; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (is.getItem() instanceof ItemSword) {
                        float itemDamage = getDamage(is);
                        float currentDamage = getDamage(item);
                        if (itemDamage >= currentDamage && swordCount > 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return item != null &&
                ((item.getItem().getUnlocalizedName().contains("tnt")) ||
                        (item.getItem().getUnlocalizedName().contains("stick")) ||
                        (item.getItem().getUnlocalizedName().contains("egg")) ||
                        (item.getItem().getUnlocalizedName().contains("string")) ||
                        (item.getItem().getUnlocalizedName().contains("flint")) ||
                        (item.getItem().getUnlocalizedName().contains("compass")) ||
                        (item.getItem().getUnlocalizedName().contains("feather")) ||
                        (item.getItem().getUnlocalizedName().contains("bucket")) ||
                        (item.getItem().getUnlocalizedName().contains("chest") && !item.getDisplayName().toLowerCase().contains("collect")) ||
                        (item.getItem().getUnlocalizedName().contains("snow")) ||
                        (item.getItem().getUnlocalizedName().contains("fish")) ||
                        (item.getItem().getUnlocalizedName().contains("enchant")) ||
                        (item.getItem().getUnlocalizedName().contains("exp")) ||
                        (item.getItem().getUnlocalizedName().contains("shears")) ||
                        (item.getItem().getUnlocalizedName().contains("anvil")) ||
                        (item.getItem().getUnlocalizedName().contains("torch")) ||
                        (item.getItem().getUnlocalizedName().contains("seeds")) ||
                        (item.getItem().getUnlocalizedName().contains("leather")) ||
                        ((item.getItem() instanceof ItemPickaxe)) ||
                        ((item.getItem() instanceof ItemGlassBottle)) ||
                        ((item.getItem() instanceof ItemTool)) ||
                        ((item.getItem() instanceof ItemArmor)) ||
                        (item.getItem().getUnlocalizedName().contains("piston")) ||
                        ((item.getItem().getUnlocalizedName().contains("potion")) && (isBadPotion(item))) ||
                        (item.getItem() instanceof ItemBlock && getBlockCount() > ((Number) settings.get(BLOCKCAP).getValue()).intValue()) ||
                        (item.getItem() instanceof ItemFood && (Boolean) settings.get(FOOD).getValue() && !(item.getItem() instanceof ItemAppleGold)) ||
                        ((item.getItem() instanceof ItemBow || item.getItem().getUnlocalizedName().contains("arrow")) && (Boolean) settings.get(ARCHERY).getValue()));
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !Scaffold.getBlacklistedBlocks().contains(((ItemBlock) item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private float getDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword) stack.getItem()).getAttackDamage()
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }


}