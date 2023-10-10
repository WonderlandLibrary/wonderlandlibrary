package vestige.module.impl.player;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import vestige.event.Listener;
import vestige.event.impl.UpdateEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.util.player.ArmorType;
import vestige.setting.impl.IntegerSetting;

public class InventoryManager extends Module {

    private final IntegerSetting delaySetting = new IntegerSetting("Delay", 1, 0, 10, 1);

    private ItemStack helmet, chestplate, leggings, boots;
    private ItemStack weapon, pickaxe, axe, shovel;
    private ItemStack block_stack, golden_apples;

    private int delay;

    private final int helmet_slot = 5;
    private final int chestplate_slot = 6;
    private final int leggings_slot = 7;
    private final int boots_slot = 8;

    private final IntegerSetting weapon_slot = new IntegerSetting("Sword slot", 0, 0, 8, 1);
    private final IntegerSetting block_stack_slot = new IntegerSetting("Block stack slot", 1, 0, 8, 1);
    private final IntegerSetting axe_slot = new IntegerSetting("Axe slot", 2, 0, 8, 1);
    private final IntegerSetting pickaxe_slot = new IntegerSetting("Pickaxe slot", 3, 0, 8, 1);
    private final IntegerSetting shovel_slot = new IntegerSetting("Shovel slot", 4, 0, 8, 1);
    private final IntegerSetting golden_apples_slot = new IntegerSetting("Golden apples slot", 5, 0, 8, 1);

    private final boolean consider_silk_touch = false;
    private final boolean priorise_golden_tool = false;

    public InventoryManager() {
        super("Inventory Manager", Category.PLAYER);
        this.addSettings(delaySetting, weapon_slot, axe_slot, pickaxe_slot, shovel_slot, block_stack_slot, golden_apples_slot);
    }

    @Override
    public void onEnable() {
        delay = 0;
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        Container container = mc.thePlayer.inventoryContainer;

        helmet = container.getSlot(helmet_slot).getStack();
        chestplate = container.getSlot(chestplate_slot).getStack();
        leggings = container.getSlot(leggings_slot).getStack();
        boots = container.getSlot(boots_slot).getStack();

        weapon = container.getSlot(weapon_slot.getValue() + 36).getStack();
        axe = container.getSlot(axe_slot.getValue() + 36).getStack();
        pickaxe = container.getSlot(pickaxe_slot.getValue() + 36).getStack();
        shovel = container.getSlot(shovel_slot.getValue() + 36).getStack();

        block_stack = container.getSlot(block_stack_slot.getValue() + 36).getStack();
        golden_apples = container.getSlot(golden_apples_slot.getValue() + 36).getStack();

        if(mc.currentScreen instanceof GuiInventory) {
            if(++delay > delaySetting.getValue()) {
                for(ArmorType type : ArmorType.values()) {
                    getBestArmor(type);
                }

                getBestWeapon();
                getBestAxe();
                getBestPickaxe();
                getBestShovel();

                getBlockStack();
                getGoldenApples();
                dropUselessItems();
            }
        } else {
            delay = 0;
        }
    }

    private void hotbarExchange(int hotbarNumber, int slotId) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotId, hotbarNumber, 2, mc.thePlayer);
        delay = 0;
    }

    private void shiftClick(int slotId) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotId, 1, 1, mc.thePlayer);
        delay = 0;
    }

    private void drop(int slotId) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotId, 1, 4, mc.thePlayer);
        delay = 0;
    }

    private void dropUselessItems() {
        if (delay <= delaySetting.getValue()) return;

        Container container = mc.thePlayer.inventoryContainer;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = container.getSlot(i).getStack();

            if (stack != null) {
                if (isGarbage(stack)) {
                    drop(i);
                    break;
                }
            }
        }
    }

    public boolean isGarbage(ItemStack stack) {
        Item item = stack.getItem();

        if (item == Items.snowball || item == Items.egg || item == Items.fishing_rod || item == Items.experience_bottle || item == Items.skull || item == Items.flint || item == Items.lava_bucket || item == Items.flint_and_steel || item == Items.string) {
            return true;
        } else if (item instanceof ItemHoe) {
            return true;
        } else if (item instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion) item;

            for(PotionEffect effect : potion.getEffects(stack)) {
                int id = effect.getPotionID();
                if(id == Potion.moveSlowdown.getId() || id == Potion.blindness.getId() || id == Potion.poison.getId() || id == Potion.digSlowdown.getId() || id == Potion.weakness.getId() || id == Potion.harm.getId()) {
                    return true;
                }
            }
        } else {
            String itemName = stack.getItem().getUnlocalizedName().toLowerCase();

            if(itemName.contains("anvil") || itemName.contains("tnt") || itemName.contains("seed") || itemName.contains("table") || itemName.contains("string")
                    || itemName.contains("eye") || itemName.contains("mushroom") || (itemName.contains("chest") && !itemName.contains("plate")) || itemName.contains("pressure_plate")) {
                return true;
            }
        }

        return false;
    }

    public boolean isUseless(ItemStack stack) {
        if(!this.isEnabled()) {
            return isGarbage(stack);
        }

        if(isGarbage(stack)) {
            return true;
        } else if(helmet != null && stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == 0 && !isBetterArmor(stack, helmet, ArmorType.HELMET)) {
            return true;
        } else if(chestplate != null && stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == 1 && !isBetterArmor(stack, chestplate, ArmorType.CHESTPLATE)) {
            return true;
        } else if(leggings != null && stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == 2 && !isBetterArmor(stack, leggings, ArmorType.LEGGINGS)) {
            return true;
        } else if(boots != null && stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).armorType == 3 && !isBetterArmor(stack, boots, ArmorType.BOOTS)) {
            return true;
        } else if(stack.getItem() instanceof ItemSword && weapon != null && !isBetterWeapon(stack, weapon)) {
            return true;
        } else if(stack.getItem() instanceof ItemAxe && axe != null && !isBetterTool(stack, axe)) {
            return true;
        } else if(stack.getItem() instanceof ItemPickaxe && pickaxe != null && !isBetterTool(stack, pickaxe)) {
            return true;
        } else if(stack.getItem().getUnlocalizedName().toLowerCase().contains("shovel") && shovel != null && !isBetterTool(stack, shovel)) {
            return true;
        }

        return false;
    }

    private void getBlockStack() {
        if (delay <= delaySetting.getValue()) return;

        Container container = mc.thePlayer.inventoryContainer;

        ItemStack blockStack = null;

        int slot = -1;

        if(block_stack == null || !shouldChooseBlock(block_stack)) {
            for (int i = 9; i < 45; i++) {
                ItemStack stack = container.getSlot(i).getStack();

                if(stack != null && shouldChooseBlock(stack)) {
                    if(!(blockStack != null && stack.stackSize < blockStack.stackSize)) {
                        blockStack = stack;
                        slot = i;
                    }
                }
            }
        }

        if(blockStack != null) {
            hotbarExchange(block_stack_slot.getValue(), slot);
        }
    }

    private void getGoldenApples() {
        if (delay <= delaySetting.getValue()) return;

        Container container = mc.thePlayer.inventoryContainer;

        if(golden_apples == null || !(golden_apples.getItem() instanceof ItemAppleGold)) {
            for (int i = 9; i < 45; i++) {
                ItemStack stack = container.getSlot(i).getStack();

                if(stack != null && stack.getItem() instanceof ItemAppleGold) {
                    hotbarExchange(golden_apples_slot.getValue(), i);
                    return;
                }
            }
        }
    }

    private boolean shouldChooseBlock(ItemStack stack) {
        return stack.getItem() instanceof ItemBlock;
    }

    private void getBestWeapon() {
        if (delay <= delaySetting.getValue()) return;

        Container container = mc.thePlayer.inventoryContainer;

        ItemStack oldWeapon = this.weapon;
        int newSwordSlot = -1;

        int dropSlot = -1;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = container.getSlot(i).getStack();

            if(stack != null && (stack.getItem() instanceof ItemSword)) {
                if(i != weapon_slot.getValue() + 36) {
                    boolean better = isBetterWeapon(stack, oldWeapon);
                    boolean worse = isWorseWeapon(stack, oldWeapon);

                    if(better) {
                        newSwordSlot = i;
                        oldWeapon = stack;
                    } else if(stack.getItem() instanceof ItemSword /* && worse */) {
                        dropSlot = i;
                    }
                }
            }
        }

        if(newSwordSlot != -1) {
            hotbarExchange(weapon_slot.getValue(), newSwordSlot);
        } else if(dropSlot != -1) {
            drop(dropSlot);
        }
    }

    private void getBestAxe() {
        if (delay <= delaySetting.getValue()) return;

        Container container = mc.thePlayer.inventoryContainer;

        ItemStack oldAxe = this.axe;
        int newAxeSlot = -1;

        int dropSlot = -1;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = container.getSlot(i).getStack();

            if(stack != null && stack.getItem() instanceof ItemAxe) {
                if(i != axe_slot.getValue() + 36) {
                    if(isBetterTool(stack, oldAxe)) {
                        newAxeSlot = i;
                        oldAxe = stack;
                    } else {
                        dropSlot = i;
                    }
                }
            }
        }

        if(newAxeSlot != -1) {
            hotbarExchange(axe_slot.getValue(), newAxeSlot);
        } else if(dropSlot != -1) {
            drop(dropSlot);
        }
    }

    private void getBestPickaxe() {
        if (delay <= delaySetting.getValue()) return;

        Container container = mc.thePlayer.inventoryContainer;

        ItemStack oldPickaxe = this.pickaxe;
        int newPickaxeSlot = -1;

        int dropSlot = -1;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = container.getSlot(i).getStack();

            if(stack != null && stack.getItem() instanceof ItemPickaxe) {
                if(i != pickaxe_slot.getValue() + 36) {
                    if(isBetterTool(stack, oldPickaxe)) {
                        newPickaxeSlot = i;
                        oldPickaxe = stack;
                    } else {
                        dropSlot = i;
                    }
                }
            }
        }

        if(newPickaxeSlot != -1) {
            hotbarExchange(pickaxe_slot.getValue(), newPickaxeSlot);
        } else if(dropSlot != -1) {
            drop(dropSlot);
        }
    }

    private void getBestShovel() {
        if (delay <= delaySetting.getValue()) return;

        Container container = mc.thePlayer.inventoryContainer;

        ItemStack oldShovel = this.shovel;
        int newShovelSlot = -1;

        int dropSlot = -1;

        for (int i = 9; i < 45; i++) {
            ItemStack stack = container.getSlot(i).getStack();

            if(stack != null && stack.getItem() instanceof ItemTool && stack.getItem().getUnlocalizedName().toLowerCase().contains("shovel")) {
                if(i != shovel_slot.getValue() + 36) {
                    if(isBetterTool(stack, oldShovel)) {
                        newShovelSlot = i;
                        oldShovel = stack;
                    } else {
                        dropSlot = i;
                    }
                }
            }
        }

        if(newShovelSlot != -1) {
            hotbarExchange(shovel_slot.getValue(), newShovelSlot);
        } else if(dropSlot != -1) {
            drop(dropSlot);
        }
    }

    private void getBestArmor(ArmorType type) {
        if (delay <= delaySetting.getValue()) return;

        Container container = mc.thePlayer.inventoryContainer;

        ItemStack oldArmor = type == ArmorType.HELMET ? this.helmet : type == ArmorType.CHESTPLATE ? this.chestplate : type == ArmorType.LEGGINGS ? this.leggings : this.boots;
        int newArmorSlot = -1;

        int dropSlot = -1;

        int armorSlot = type == ArmorType.HELMET ? helmet_slot : type == ArmorType.CHESTPLATE ? chestplate_slot : type == ArmorType.LEGGINGS ? leggings_slot : boots_slot;

        for (int i = 5; i < 45; i++) {
            ItemStack stack = container.getSlot(i).getStack();

            if(stack != null && stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();

                boolean better = isBetterArmor(stack, oldArmor, type);
                boolean worse = isWorseArmor(stack, oldArmor, type);

                if(armor.armorType == type.ordinal()) {
                    if(better) {
                        if(i != armorSlot) {
                            if(oldArmor != null) {
                                dropSlot = armorSlot;
                            } else {
                                newArmorSlot = i;
                                oldArmor = stack;
                                armorSlot = i;
                            }
                        }
                    } else if(worse || i != armorSlot) {
                        dropSlot = i;
                    } else {
                        if(i != armorSlot) {
                            newArmorSlot = i;
                            oldArmor = stack;
                            armorSlot = i;
                        }
                    }
                }
            }
        }

        if(dropSlot != -1) {
            drop(dropSlot);
        } else if(newArmorSlot != -1) {
            shiftClick(newArmorSlot);
        }
    }

    private boolean isBetterWeapon(ItemStack newWeapon, ItemStack oldWeapon) {
        Item item = newWeapon.getItem();

        if(item instanceof ItemSword || item instanceof ItemTool) {
            if(oldWeapon != null) {
                return getAttackDamage(newWeapon) > getAttackDamage(oldWeapon);
            } else {
                return true;
            }
        }

        return false;
    }

    private boolean isWorseWeapon(ItemStack newWeapon, ItemStack oldWeapon) {
        Item item = newWeapon.getItem();

        if(item instanceof ItemSword || item instanceof ItemTool) {
            if(oldWeapon != null) {
                return getAttackDamage(newWeapon) < getAttackDamage(oldWeapon);
            } else {
                return false;
            }
        }

        return true;
    }

    private boolean isBetterTool(ItemStack newTool, ItemStack oldTool) {
        Item item = newTool.getItem();

        if(item instanceof ItemTool) {
            if(oldTool != null) {
                return getToolUsefulness(newTool) > getToolUsefulness(oldTool);
            } else {
                return true;
            }
        }

        return false;
    }

    private boolean isBetterArmor(ItemStack newArmor, ItemStack oldArmor, ArmorType type) {
        if(oldArmor == null) return true;

        Item oldItem = oldArmor.getItem();

        if(oldItem instanceof ItemArmor) {
            ItemArmor oldItemArmor = (ItemArmor) oldItem;

            if(oldArmor != null && oldItemArmor.armorType == type.ordinal()) {
                return getArmorProtection(newArmor) > getArmorProtection(oldArmor);
            } else {
                return true;
            }
        }

        return false;
    }

    private boolean isWorseArmor(ItemStack newArmor, ItemStack oldArmor, ArmorType type) {
        if(oldArmor == null) return false;

        Item oldItem = oldArmor.getItem();

        if(oldItem instanceof ItemArmor) {
            ItemArmor oldItemArmor = (ItemArmor) oldItem;

            if(oldArmor != null && oldItemArmor.armorType == type.ordinal()) {
                return getArmorProtection(newArmor) < getArmorProtection(oldArmor);
            } else {
                return false;
            }
        }

        return true;
    }

    private float getAttackDamage(ItemStack stack) {
        if(stack == null) return 0F;

        Item item = stack.getItem();

        float baseDamage = 0F;

        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword) item;
            baseDamage += sword.getAttackDamage();
        } else if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;
            baseDamage += tool.getAttackDamage();
        }

        float enchantsDamage = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.3F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack) * 0.15F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) * 0.1F;

        //LogUtil.addChatMessage("Damage : " + baseDamage + " Enchants damage : " + enchantsDamage);

        return baseDamage + enchantsDamage;
    }

    private float getToolUsefulness(ItemStack stack) {
        if(stack == null) return 0F;

        Item item = stack.getItem();

        float baseUsefulness = 0F;

        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;

            switch (tool.getToolMaterial()) {
                case WOOD:
                    baseUsefulness = 1F;
                    break;
                case GOLD:
                    baseUsefulness = priorise_golden_tool ? 1.9F : 1F;
                    break;
                case STONE:
                    baseUsefulness = 2F;
                    break;
                case IRON:
                    baseUsefulness = 3F;
                    break;
                case EMERALD:
                    baseUsefulness = 4F;
                    break;
            }
        }

        float enchantsUsefulness = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack) * 1.25F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) * 0.3F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) * 0.5F
                + (consider_silk_touch ? EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack) * 0.5F : 0F);

        return baseUsefulness + enchantsUsefulness;
    }

    private float getArmorProtection(ItemStack stack) {
        if(stack == null) return 0F;

        Item item = stack.getItem();

        float baseProtection = 0F;

        if (item instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) item;
            baseProtection += armor.damageReduceAmount;
        }

        float enchantsProtection = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 1.25F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) * 0.15F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) * 0.15F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) * 0.15F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) * 0.1F
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) * 0.1F;

        return baseProtection + enchantsProtection;
    }

}
