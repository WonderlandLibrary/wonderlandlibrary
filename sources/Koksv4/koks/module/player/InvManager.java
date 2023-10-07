package koks.module.player;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.TimeHelper;
import koks.event.GuiHandleEvent;
import lombok.Getter;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;

import java.util.*;

/*
 * Created on 5/31/2021 by dirt.
 */
@Module.Info(name = "InvManager(WIP)", description = "PastebinWeilIchPasteBin.club", category = Module.Category.PLAYER)
public class InvManager extends Module {

    private final HashMap<String, ItemCategory> itemCategories = new HashMap<>();
    private final TimeHelper dropTimeHelper = new TimeHelper();
    private final TimeHelper startDelayTimeHelper = new TimeHelper();
    private final List<Integer> itemsToDrop = new ArrayList<>();

    @Value(name = "Drop Delay", maximum = 500, minimum = 0)
    int dropDelay = 100;

    @Value(name = "Start Delay", maximum = 1000, minimum = 0)
    int startDelay = 100;

    @Value(name = "Random")
    boolean random = false;

    @Value(name = "Ignore Inventory")
    boolean ignoreInventory = false;

    @Value(name = "Single Drop")
    boolean singleDrop = true;

    public InvManager() {
        // add the default item categories and the default items in the categories
        itemCategories.put("food", new ItemCategory("food", "item.rabbitRaw", "item.rabbitCooked", "item.rabbitStew", "item.muttonRaw", "item.muttonCooked", "item.fish.salmon.raw", "item.fish.cod.raw", "item.appleGold", "item.appleGold", "item.porkchopCooked", "item.porkchopRaw", "item.bread", "item.mushroomStew", "item.apple", "item.beefCooked", "item.beefRaw", "item.melon", "item.cookie", "item.cake", "item.fish.salmon.cooked", "item.fish.cod.cooked", "item.fish.pufferfish.raw", "item.fish.clownfish.raw", "item.potatoPoisonous", "item.potatoBaked", "item.pumpkinPie", "item.carrots", "item.spiderEye", "item.rottenFlesh", "item.chickenCooked", "item.potato", "item.chickenRaw"));
        itemCategories.put("armor", new ItemCategory("armor", "item.bootsDiamond", "item.helmetChain", "item.leggingsChain", "item.bootsChain", "item.chestplateGold", "item.leggingsGold", "item.helmetDiamond", "item.chestplateDiamond", "item.leggingsDiamond", "item.helmetCloth", "item.chestplateCloth", "item.bootsCloth", "item.leggingsCloth", "item.bootsGold", "item.helmetGold", "item.bootsIron", "item.leggingsIron", "item.helmetIron", "item.chestplateIron", "item.chestplateChain"));
        itemCategories.put("tools", new ItemCategory("tools", "item.pickaxeWood", "item.shovelStone", "item.pickaxeStone", "item.hatchetStone", "item.hatchetWood", "item.shovelWood", "item.shovelIron", "item.pickaxeIron", "item.hoeGold", "item.hatchetGold", "item.hatchetDiamond", "item.hoeWood", "item.hoeIron", "item.pickaxeDiamond", "item.hoeDiamond", "item.shovelDiamond", "item.shovelGold", "item.pickaxeGold", "item.hatchetIron", "item.hoeStone"));
        itemCategories.put("sword", new ItemCategory("sword", "item.swordDiamond", "item.swordGold", "item.swordIron", "item.swordWood", "item.swordStone"));
        itemCategories.put("utility", new ItemCategory("utility", "item.bucketWater", "item.arrow", "item.bow", "item.flintAndSteel", "item.compass", "item.shears", "item.fishingRod", "item.potion", "item.bucketLava", "item.fireball", "item.expBottle", "item.egg", "item.enderPearl", "item.snowball"));
        itemCategories.put("block", new ItemCategory("block", "tile.cloth.orange", "tile.cloth.magenta", "tile.cloth.lightBlue", "tile.cloth.yellow", "tile.cloth.lime", "tile.cloth.pink", "tile.cloth.gray", "tile.cloth.silver", "tile.cloth.cyan", "tile.stone.stone", "tile.stone.granite", "tile.stone.graniteSmooth", "tile.stone.diorite", "tile.stone.dioriteSmooth", "tile.stone.andesite", "tile.stone.andesiteSmooth", "tile.grass", "tile.dirt.default", "tile.dirt.coarse", "tile.dirt.podzol", "tile.stonebrick", "tile.wood.oak", "tile.wood.spruce", "tile.wood.birch", "tile.wood.jungle", "tile.wood.acacia", "tile.wood.big_oak", "tile.log.oak", "tile.log.spruce", "tile.log.birch", "tile.log.jungle", "tile.glass", "tile.sandStone.default", "tile.sandStone.chiseled", "tile.sandStone.smooth", "tile.cloth.white", "tile.stonebricksmooth.default", "tile.lightgem", "tile.snow", "tile.prismarine.bricks", "tile.prismarine.dark", "tile.seaLantern", "tile.hayBlock", "tile.clayHardened", "tile.blockCoal", "tile.cloth.purple", "tile.cloth.blue", "tile.cloth.brown", "tile.cloth.green", "tile.cloth.red", "tile.cloth.black", "tile.quartzBlock.default", "tile.quartzBlock.lines", "tile.quartzBlock.chiseled", "tile.clayHardenedStained.orange", "tile.clayHardenedStained.magenta", "tile.clayHardenedStained.lightBlue", "tile.clayHardenedStained.yellow", "tile.clayHardenedStained.lime", "tile.clayHardenedStained.pink", "tile.clayHardenedStained.gray", "tile.clayHardenedStained.silver", "tile.clayHardenedStained.cyan", "tile.clayHardenedStained.purple", "tile.clayHardenedStained.blue", "tile.clayHardenedStained.brown", "tile.clayHardenedStained.green", "tile.clayHardenedStained.red", "tile.clayHardenedStained.black", "tile.log.acacia", "tile.log.big_oak", "tile.prismarine.rough", "tile.blockEmerald", "tile.blockDiamond", "tile.blockIron", "tile.bookshelf", "tile.stoneMoss", "tile.obsidian", "tile.blockGold", "tile.blockCoal", "tile.icePacked", "tile.whiteStone"));
        itemCategories.put("material", new ItemCategory("material", "item.blazeRod", "item.sulphur", "item.feather", "item.stick", "item.netherStar", "item.emerald", "item.diamond", "item.brick", "item.ingotGold", "item.ingotIron"));
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof GuiHandleEvent) {
            if ((mc.currentScreen instanceof GuiInventory || ignoreInventory)) {
                if (startDelayTimeHelper.hasReached(startDelay)) {
                    itemsToDrop.clear();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < getPlayer().inventory.getSizeInventory(); i++) {
                        ItemStack itemStack = getPlayer().inventory.getStackInSlot(i);
                        if (itemStack != null) {
                            stringBuilder.append("\"").append(itemStack.getUnlocalizedName()).append("\", ");
                            // check if the item is in any itemgroup
                            boolean drop = true;
                            for (ItemCategory itemCategory : itemCategories.values()) {
                                if (itemCategory.getItems().contains(itemStack.getUnlocalizedName())) {
                                    drop = false;
                                }
                            }
                            // add the item to an list and
                            if (drop) {
                                itemsToDrop.add(i);
                            }
                        }
                    }
                    System.out.println(stringBuilder.toString());
                    if (random) {
                        Collections.shuffle(itemsToDrop);
                    }
                    for (Integer integer : itemsToDrop) {
                        if (dropTimeHelper.hasReached(dropDelay)) {
                            dropTimeHelper.reset();
                            System.out.println("drop " + integer);
                            int slot = integer;
                            slot = slot < 9 ? slot + 36 : slot;
                            getPlayerController().windowClick(getPlayer().inventoryContainer.windowId, slot, singleDrop ? 0 : 1, 4, getPlayer());
                        }
                    }
                }
            } else {
                startDelayTimeHelper.reset();
            }
        }
    }

    @Override
    public void onEnable() {
        startDelayTimeHelper.reset();
    }

    @Override
    public void onDisable() {

    }

    public double getItemStackRating(String group, ItemStack itemStack) {
        final ItemCategory itemCategory = itemCategories.get(group);
        if (itemCategory.getCustomItemRating().containsKey(itemStack.getUnlocalizedName())) {
            return itemCategory.getCustomItemRating().get(itemStack.getUnlocalizedName());
        }
        switch (group) {
            case "armor" -> {
                ItemArmor armor = (ItemArmor) itemStack.getItem();
                return armor.getArmorMaterial().getDamageReductionAmount(armor.armorType) + (EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack) * 1.25) + (EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack) * 0.5) + ((EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 0.2));
            }
            case "sword" -> {
                return ((ItemSword) itemStack.getItem()).getAttackDamage() + (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25) + (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.5) + ((EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, itemStack) * 0.2));
            }
            case "tool" -> {
                return ((ItemTool) itemStack.getItem()).getDamageVsEntity() + (EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack) * 1.25) + (EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 0.5) + ((EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, itemStack) * 0.2));
            }
        }
        return 0;
    }

    @Getter
    static class ItemCategory {
        private final String categoryName;
        private final List<String> items;
        private final HashMap<String, Integer> customItemRating;

        public ItemCategory(String categoryName, String... items) {
            this.categoryName = categoryName;
            this.items = Arrays.asList(items);
            customItemRating = new HashMap<>();
        }
    }

    //<editor-fold desc="todo: use this to add items">
    //                    String[] splitName = itemStack.getUnlocalizedName().split("\\.");
//                    String name = splitName[splitName.length - 1];
//                    // we have to manually remove
//                    if(name.equals("raw")) {
//                        name = splitName[splitName.length - 2];
//                    }
    //</editor-fold>
}
