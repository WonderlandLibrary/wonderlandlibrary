package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.IntProperty;
import cc.novoline.modules.configurations.property.object.ListProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.move.Scaffold;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import cc.novoline.utils.Timer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.checkerframework.checker.nullness.qual.NonNull;

import static cc.novoline.gui.screen.setting.SettingType.*;
import static cc.novoline.modules.configurations.property.object.PropertyFactory.*;
import static net.minecraft.util.EnumChatFormatting.*;

public final class InvManager extends AbstractModule {

    /* fields */
    private final Timer timer = new Timer();

    /* properties @off */
    @Property("inventory-cleaner")
    private final BooleanProperty inventoryCleaner = booleanTrue();
    @Property("open-inventory")
    private final BooleanProperty open_inv = booleanFalse();
    @Property("delay")
    private final IntProperty delay = createInt(1).minimum(1).maximum(10);
    @Property("arrows")
    private final IntProperty arrows = createInt(128).minimum(64).maximum(512);
    @Property("blocks")
    private final IntProperty blocks = createInt(128).minimum(0).maximum(512);
    @Property("pickaxe-slot")
    private final IntProperty pickAxeSlot = createInt(7).minimum(1).maximum(9);
    @Property("axe-slot")
    private final IntProperty axeSlot = createInt(8).minimum(1).maximum(9);
    @Property("shovel-slot")
    private final IntProperty shovelSlot = createInt(9).minimum(1).maximum(9);
    @Property("weapon-slot")
    private final IntProperty swordSlot = createInt(1).minimum(1).maximum(9);
    @Property("bow-slot")
    private final IntProperty bowSlot = createInt(2).minimum(1).maximum(9);
    @Property("head-slot")
    private final IntProperty headSlot = createInt(3).minimum(1).maximum(9);
    @Property("gapple-slot")
    private final IntProperty gappleSlot = createInt(3).minimum(1).maximum(9);
    @Property("autodisable")
    private final ListProperty<String> autoDisable = PropertyFactory.createList("Death").acceptableValues("World Change", "Game End", "Death");
    @Property("keep-items")
    private final ListProperty<String> items = createList("Sword", "Bow").acceptableValues("Sword", "Bow", "PickAxe", "Axe", "Shovel", "Golden Apple", "Head", "Buckets");
    @Property("filters")
    private final ListProperty<String> materials = createList("Sticks").acceptableValues("Ores", "Sticks");

    /* constructors @on */
    public InvManager(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "InventoryManager", "Inventory Manager", EnumModuleType.PLAYER, "Manages inventory");
        Manager.put(new Setting("IM_CLEANER", "Cleaner", CHECKBOX, this, inventoryCleaner));
        Manager.put(new Setting("IM_OPEN_INV", "Open Inventory", CHECKBOX, this, open_inv));
        Manager.put(new Setting("IM_DELAY", "Delay", SLIDER, this, delay, 1, inventoryCleaner::get));
        Manager.put(new Setting("IM_BLOCKS", "Blocks", SLIDER, this, blocks, 64, inventoryCleaner::get));
        Manager.put(new Setting("IM_ARROWS", "Arrows", SLIDER, this, arrows, 64, () -> items.contains("Bow")));
        Manager.put(new Setting("IM_KEEP_ITEMS", "Keep Items", SELECTBOX, this, items));
        Manager.put(new Setting("IM_WEAPON_SLOT", "Sword Slot", SLIDER, this, swordSlot, 1, () -> items.contains("Sword")));
        Manager.put(new Setting("IM_BOW_SLOT", "Bow Slot", SLIDER, this, bowSlot, 1, () -> items.contains("Bow")));
        Manager.put(new Setting("IM_PICKAXE_SLOT", "Pickaxe Slot", SLIDER, this, pickAxeSlot, 1, () -> items.contains("PickAxe")));
        Manager.put(new Setting("IM_AXE_SLOT", "Axe Slot", SLIDER, this, axeSlot, 1, () -> items.contains("Axe")));
        Manager.put(new Setting("IM_SHOVEL_SLOT", "Shovel Slot", SLIDER, this, shovelSlot, 1, () -> items.contains("Shovel")));
        Manager.put(new Setting("IM_GAPPLE_SLOT", "Golden Apple Slot", SLIDER, this, gappleSlot, 1, () -> items.contains("Golden Apple")));
        Manager.put(new Setting("IM_HEAD_SLOT", "Head Slot", SLIDER, this, headSlot, 1, () -> items.contains("Head")));
        Manager.put(new Setting("IM_MATERIALS", "Keep Materials", SELECTBOX, this, materials));
        Manager.put(new Setting("IM_AUTO_DISABLE", "Disable On", SELECTBOX, this, autoDisable));
    }

    /* methods */
    private boolean isBestSword(ItemStack stack) {
        final float damage = getDamage(stack);

        for (int i = 9; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack is = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (getDamage(is) > damage && is.getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }

        return stack.getItem() instanceof ItemSword;
    }

    private boolean isHead(@NonNull ItemStack stack) {
        return stack.getItem() instanceof ItemSkull && stack.getDisplayName().contains("Head") && !stack
                .getDisplayName().equalsIgnoreCase("Wither Skeleton Skull") && !stack.getDisplayName()
                .equalsIgnoreCase("Zombie Head") && !stack.getDisplayName().equalsIgnoreCase("Creeper Head") && !stack
                .getDisplayName().equalsIgnoreCase("Skeleton Skull");
    }

    private boolean isGoldenApple(@NonNull ItemStack stack) {
        return stack.getItem() instanceof ItemAppleGold;
    }

    @EventTarget
    public void onPreUpdate(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (!(mc.currentScreen instanceof GuiInventory) && open_inv.get() || ServerUtils.serverIs(Servers.PRE) || ServerUtils.serverIs(Servers.LOBBY)
                    || isEnabled(AutoArmor.class) && getModule(AutoArmor.class).isWorking() || getModule(ChestStealer.class).isStealing()) {
                return;
            }

            if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat) {
                final int swordSlot = this.swordSlot.get() - 1, pickAxeSlot = this.pickAxeSlot.get() - 1,
                        bowSlot = this.bowSlot.get() - 1, shovelSlot = this.shovelSlot.get() - 1,
                        axeSlot = this.axeSlot.get() - 1, headSlot = this.headSlot.get() - 1, gappleSlot = this.gappleSlot.get() - 1;

                boolean pickAxe = items.contains("PickAxe"), shovel = items.contains("Shovel"),
                        axe = items.contains("Axe"), sword = items.contains("Sword"),
                        bow = items.contains("Bow"), head = items.contains("Head"), gapple = items.contains("Golden Apple");

                int tickDelay = delay.get() * 50;

                for (int slotIndex = 9; slotIndex < 45; slotIndex++) {
                    ItemStack stack = mc.player.getSlotFromPlayerContainer(slotIndex).getStack();
                    if (stack != null) {
                        if (timer.check(tickDelay)) {
                            if (isBestSword(stack) && sword && shouldSwap(swordSlot)[0]) {
                                mc.player.swap(slotIndex, swordSlot);
                                timer.reset();
                            } else if (isBestPickaxe(stack) && pickAxe && shouldSwap(pickAxeSlot)[2]) {
                                mc.player.swap(slotIndex, pickAxeSlot);
                                timer.reset();
                            } else if (isBestAxe(stack) && axe && shouldSwap(axeSlot)[1]) {
                                mc.player.swap(slotIndex, axeSlot);
                                timer.reset();
                            } else if (isBestBow(stack) && bow && shouldSwap(bowSlot)[5] && !stack.getDisplayName().toLowerCase().contains("kit selector")) {
                                mc.player.swap(slotIndex, bowSlot);
                                timer.reset();
                            } else if (isHead(stack) && head && shouldSwap(headSlot)[4]) {
                                mc.player.swap(slotIndex, headSlot);
                                timer.reset();
                            } else if (isBestShovel(stack) && shovel && shouldSwap(shovelSlot)[3]) {
                                mc.player.swap(slotIndex, shovelSlot);
                                timer.reset();
                            } else if (isGoldenApple(stack) && gapple && shouldSwap(gappleSlot)[6]) {
                                mc.player.swap(slotIndex, gappleSlot);
                                timer.reset();
                            }
                        }
                    }
                }

                for (int slotIndex = 9; slotIndex < 45; slotIndex++) {
                    if (!mc.player.getSlotFromPlayerContainer(slotIndex).getHasStack()) {
                        continue;
                    }

                    ItemStack stack = mc.player.getSlotFromPlayerContainer(slotIndex).getStack();

                    if (stack != null) {
                        if (shouldDrop(stack) && inventoryCleaner.get()) {
                            if (timer.delay(tickDelay)) {
                                mc.player.drop(slotIndex);
                                timer.reset();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean[] shouldSwap(int slot) {
        return new boolean[]{
                !mc.player.getSlotFromPlayerContainer(slot + 36).getHasStack() || !isBestSword(mc.player.getSlotFromPlayerContainer(slot + 36).getStack()),
                !mc.player.getSlotFromPlayerContainer(slot + 36).getHasStack() || !isBestAxe(mc.player.getSlotFromPlayerContainer(slot + 36).getStack()),
                !mc.player.getSlotFromPlayerContainer(slot + 36).getHasStack() || !isBestPickaxe(mc.player.getSlotFromPlayerContainer(slot + 36).getStack()),
                !mc.player.getSlotFromPlayerContainer(slot + 36).getHasStack() || !isBestShovel(mc.player.getSlotFromPlayerContainer(slot + 36).getStack()),
                !mc.player.getSlotFromPlayerContainer(slot + 36).getHasStack() || !isHead(mc.player.getSlotFromPlayerContainer(slot + 36).getStack()),
                !mc.player.getSlotFromPlayerContainer(slot + 36).getHasStack() || !isBestBow(mc.player.getSlotFromPlayerContainer(slot + 36).getStack()),
                !mc.player.getSlotFromPlayerContainer(slot + 36).getHasStack() || !isGoldenApple(mc.player.getSlotFromPlayerContainer(slot + 36).getStack())};
    }

    public boolean isWorking() {
        return !timer.check(delay.get() * 150);
    }

    private float getDamage(@NonNull ItemStack stack) {
        float damage = 0;
        final Item item = stack.getItem();

        if (item instanceof ItemTool) {
            damage += ((ItemTool) item).getDamage();
        } else if (item instanceof ItemSword) {
            damage += ((ItemSword) item).getAttackDamage();
        }

        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F + EnchantmentHelper.getEnchantmentLevel(
                Enchantment.fireAspect.effectId, stack) * 0.01F;
        return damage;
    }

    private int getBlocksCounter() {
        int blockCount = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                final Item item = stack.getItem();

                if (stack.getItem() instanceof ItemBlock && !getModule(Scaffold.class).getBlacklistedBlocks().contains(((ItemBlock) item).getBlock())) {
                    blockCount += stack.stackSize;
                }
            }
        }

        return blockCount;
    }

    private int getArrowsCounter() {
        int arrowCount = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack is = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (is.getItem() == Items.arrow) arrowCount += is.stackSize;
            }
        }

        return arrowCount;
    }

    private int getIronIngotsCounter() {
        int count = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (stack.getItem() == Items.iron_ingot) count += stack.stackSize;
            }
        }

        return count;
    }

    private int getCoalCounter() {
        int count = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (stack.getItem() == Items.coal) count += stack.stackSize;
            }
        }

        return count;
    }

    private int getSwordsCounter() {
        int count = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (stack.getItem() instanceof ItemSword && isBestSword(stack)) count += stack.stackSize;
            }
        }

        return count;
    }

    private int getBowsCounter() {
        int count = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (stack.getItem() instanceof ItemBow && isBestBow(stack)) count += stack.stackSize;
            }
        }

        return count;
    }

    private int getPickaxexCounter() {
        int count = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (stack.getItem() instanceof ItemPickaxe && isBestPickaxe(stack)) count += stack.stackSize;
            }
        }

        return count;
    }

    private int getAxesCounter() {
        int count = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (stack.getItem() instanceof ItemAxe && isBestAxe(stack)) count += stack.stackSize;
            }
        }

        return count;
    }

    private int getHeadsCounter() {
        int count = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (stack.getItem() instanceof ItemSkull && isBestShovel(stack)) count += stack.stackSize;
            }
        }

        return count;
    }

    private int getShovelsCounter() {
        int count = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack stack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (stack.getItem() instanceof ItemSpade && isBestShovel(stack)) count += stack.stackSize;
            }
        }

        return count;
    }

    private boolean isBestPickaxe(ItemStack stack) {
        final Item item = stack.getItem();

        if (!(item instanceof ItemPickaxe)) {
            return false;
        }

        final float value = getToolEffect(stack);

        for (int i = 9; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack slotStack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (getToolEffect(slotStack) > value && slotStack.getItem() instanceof ItemPickaxe) return false;
            }
        }

        return true;
    }

    private boolean shouldDrop(ItemStack stack) {
        final Item item = stack.getItem();
        final String displayName = stack.getDisplayName();
        final int idFromItem = Item.getIdFromItem(item);

        if (idFromItem == 58 || displayName.toLowerCase().contains(OBFUSCATED + "||") // @off
                || displayName.contains(GREEN + "Game Menu " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(AQUA + "" + BOLD + "Spectator Settings " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(AQUA + "" + BOLD + "Play Again " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(GREEN + "" + BOLD + "Teleporter " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(GREEN + "SkyWars Challenges " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(GREEN + "Collectibles " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(GREEN + "Kit Selector " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(GREEN + "Kill Effect Selector " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(WHITE + "Players: " + RED + "Hidden " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(GREEN + "Shop " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(WHITE + "Players: " + RED + "Visible " + GRAY + "(Right Click)")
                || displayName.equalsIgnoreCase(GOLD + "Excalibur") || displayName.equalsIgnoreCase("aDragon Sword")
                || displayName.equalsIgnoreCase(GREEN + "Cornucopia")
                || displayName.equalsIgnoreCase(RED + "Bloodlust") || displayName.equalsIgnoreCase(RED + "Artemis' Bow")
                || displayName.equalsIgnoreCase(GREEN + "Miner's Blessing") || displayName.equalsIgnoreCase(GOLD + "Axe of Perun")
                || displayName.equalsIgnoreCase(GOLD + "Cornucopia") || idFromItem == 116 || idFromItem == 145
                || (idFromItem == 15 || idFromItem == 14) && materials.contains("Ores") || displayName.equalsIgnoreCase("\u00a7aAnd\u00faril")
                || idFromItem == 259 || idFromItem == 46) { // @on
            return false;
        }

        boolean pickAxe = items.contains("PickAxe"), shovel = items.contains("Shovel"),
                axe = items.contains("Axe"), sword = items.contains("Sword"),
                bow = items.contains("Bow"), head = items.contains("Head");

        final int swordSlot = this.swordSlot.get() - 1, pickAxeSlot = this.pickAxeSlot.get() - 1,
                bowSlot = this.bowSlot.get() - 1, shovelSlot = this.shovelSlot.get() - 1,
                axeSlot = this.axeSlot.get() - 1, headSlot = this.headSlot.get() - 1;

        if ((isBestShovel(stack) && getShovelsCounter() < 2 || stack.getItem() instanceof ItemSpade && stack == mc.player.inventory.getStackInSlot(shovelSlot)) && shovel ||
                (isBestBow(stack) && getBowsCounter() < 2 || stack.getItem() instanceof ItemBow && stack == mc.player.inventory.getStackInSlot(bowSlot)) && bow ||
                (isHead(stack) && getHeadsCounter() < 2 || stack.getItem() instanceof ItemSkull && stack == mc.player.inventory.getStackInSlot(headSlot)) && head ||
                (isBestAxe(stack) && getAxesCounter() < 2 || stack.getItem() instanceof ItemAxe && stack == mc.player.inventory.getStackInSlot(axeSlot)) && axe ||
                (isBestPickaxe(stack) && getPickaxexCounter() < 2 || stack.getItem() instanceof ItemPickaxe && stack == mc.player.inventory.getStackInSlot(pickAxeSlot)) && pickAxe ||
                (isBestSword(stack) && getSwordsCounter() < 2 || stack.getItem() instanceof ItemSword && stack == mc.player.inventory.getStackInSlot(swordSlot)) && sword) {
            return false;
        }

        if (item instanceof ItemArmor) {
            final AutoArmor armorModule = getModule(AutoArmor.class);

            for (int type = 1; type < 5; type++) {
                if (mc.player.getSlotFromPlayerContainer(4 + type).getHasStack()) {
                    final ItemStack slotStack = mc.player.getSlotFromPlayerContainer(4 + type).getStack();
                    if (armorModule.isBestArmor(slotStack, type)) continue;
                }

                if (armorModule.isBestArmor(stack, type)) return false;
            }
        }

        if (item instanceof ItemBlock && (getBlocksCounter() > blocks.get() // @off
                || getModule(Scaffold.class).getBlacklistedBlocks().contains(((ItemBlock) item).getBlock()))
                || item instanceof ItemPotion && isBadPotion(stack) || item instanceof ItemFood
                && !(item instanceof ItemAppleGold) && item != Items.bread && item
                != Items.pumpkin_pie && item != Items.baked_potato && item != Items.cooked_chicken
                && item != Items.carrot && item != Items.apple && item != Items.beef
                && item != Items.cooked_beef && item != Items.porkchop && item != Items.cooked_porkchop
                && item != Items.mushroom_stew && item != Items.cooked_fish && item != Items.melon
                || item instanceof ItemHoe || item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemArmor) { // @on
            return true;
        }

        final String unlocalizedName = item.getUnlocalizedName();

        return !materials.contains("Sticks") && unlocalizedName.contains("stick") || unlocalizedName.contains("egg") // @off
                || getIronIngotsCounter() > 64 && item == Items.iron_ingot || getCoalCounter() > 64 && item == Items.coal
                || unlocalizedName.contains("string") || unlocalizedName.contains("flint")
                || unlocalizedName.contains("compass") || unlocalizedName.contains("dyePowder")
                || unlocalizedName.contains("feather")
                || unlocalizedName.contains("chest") && !displayName.toLowerCase().contains("collect")
                || unlocalizedName.contains("snow") || unlocalizedName.contains("torch")
                || unlocalizedName.contains("seeds") || unlocalizedName.contains("leather")
                || unlocalizedName.contains("reeds") || unlocalizedName.contains("record")
                || unlocalizedName.contains("snowball") || item instanceof ItemGlassBottle
                || item instanceof ItemSlab || idFromItem == 113 || idFromItem == 106
                || idFromItem == 325 || idFromItem == 326 && !items.contains("Buckets") || idFromItem == 327
                || idFromItem == 111 || idFromItem == 85 || idFromItem == 188
                || idFromItem == 189 || idFromItem == 190 || idFromItem == 191
                || idFromItem == 401 || idFromItem == 192 || idFromItem == 81
                || idFromItem == 32 || unlocalizedName.contains("gravel")
                || unlocalizedName.contains("flower") || unlocalizedName.contains("tallgrass")
                || item instanceof ItemBow || item == Items.arrow && getArrowsCounter() > (items.contains("Bow") ? arrows.get() : 0) || idFromItem == 175
                || idFromItem == 340 || idFromItem == 339 || idFromItem == 160
                || idFromItem == 101 || idFromItem == 102 || idFromItem == 321
                || idFromItem == 323 || idFromItem == 389 || idFromItem == 416
                || idFromItem == 171 || idFromItem == 139 || idFromItem == 23
                || idFromItem == 25 || idFromItem == 69 || idFromItem == 70
                || idFromItem == 72 || idFromItem == 77
                || idFromItem == 96 || idFromItem == 107 || idFromItem == 123
                || idFromItem == 131 || idFromItem == 143 || idFromItem == 147
                || idFromItem == 148 || idFromItem == 151 || idFromItem == 152
                || idFromItem == 154 || idFromItem == 158 || idFromItem == 167
                || idFromItem == 403 || idFromItem == 183 || idFromItem == 184
                || idFromItem == 185 || idFromItem == 186 || idFromItem == 187
                || idFromItem == 331 || idFromItem == 356 || idFromItem == 404
                || idFromItem == 27 || idFromItem == 28 || idFromItem == 66
                || idFromItem == 76 || idFromItem == 157
                || idFromItem == 328
                || idFromItem == 342 || idFromItem == 343 || idFromItem == 398
                || idFromItem == 407 || idFromItem == 408 || idFromItem == 138
                || idFromItem == 352 || idFromItem == 385
                || idFromItem == 386 || idFromItem == 395 || idFromItem == 402
                || idFromItem == 418 || idFromItem == 419
                || idFromItem == 281 || idFromItem == 289 || idFromItem == 337
                || idFromItem == 336 || idFromItem == 348 || idFromItem == 353
                || idFromItem == 369 || idFromItem == 372 || idFromItem == 405
                || idFromItem == 406 || idFromItem == 409 || idFromItem == 410
                || idFromItem == 415 || idFromItem == 370 || idFromItem == 376
                || idFromItem == 377 || idFromItem == 378 || idFromItem == 379
                || idFromItem == 380 || idFromItem == 382 || idFromItem == 414
                || idFromItem == 346 || idFromItem == 347 || idFromItem == 420
                || idFromItem == 397 || idFromItem == 421 || idFromItem == 341
                || unlocalizedName.contains("sapling") || unlocalizedName.contains("stairs")
                || unlocalizedName.contains("door") || unlocalizedName.contains("monster_egg")
                || unlocalizedName.contains("sand") || unlocalizedName.contains("piston"); // @on
    }

    private boolean isBestShovel(@NonNull ItemStack stack) {
        final Item item = stack.getItem();

        if (!(item instanceof ItemSpade)) {
            return false;
        }

        final float value = getToolEffect(stack);

        for (int i = 9; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack is = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) return false;
            }
        }

        return true;
    }

    private boolean isBestAxe(@NonNull ItemStack stack) {
        final Item item = stack.getItem();

        if (!(item instanceof ItemAxe)) {
            return false;
        }

        final float value = getToolEffect(stack);

        for (int i = 9; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack is = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestSword(stack)) return false;
            }
        }

        return true;
    }

    private float getToolEffect(@NonNull ItemStack stack) {
        final Item item = stack.getItem();

        if (!(item instanceof ItemTool)) {
            return 0;
        }

        final String name = item.getUnlocalizedName();
        final ItemTool tool = (ItemTool) item;
        float value;

        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) value -= 5;
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) value -= 5;
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) value -= 5;
        } else return 1f;

        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0D;

        return value;
    }

    private float getBowEffect(ItemStack stack) {
        return (float) (1 + EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack) + EnchantmentHelper.getEnchantmentLevel(
                Enchantment.flame.effectId, stack) + EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack));
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            return potion.getEffects(stack) == null || isBadPotionEffect(stack, potion);
        }

        return false;
    }

    public boolean isBadPotionEffect(ItemStack stack, ItemPotion pot) {
        for (final PotionEffect effect : pot.getEffects(stack)) {
            final int potionID = effect.getPotionID();
            final Potion potion = Potion.potionTypes[effect.getPotionID()];

            if (potion.isBadEffect()) {
                return true;
            }
        }

        return false;
    }

    private boolean isBestBow(@NonNull ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemBow)) return false;
        final float value = getBowEffect(stack);

        for (int i = 9; i < 45; i++) {
            if (mc.player.getSlotFromPlayerContainer(i).getHasStack()) {
                final ItemStack slotStack = mc.player.getSlotFromPlayerContainer(i).getStack();
                if (getBowEffect(slotStack) > value && slotStack.getItem() instanceof ItemBow) return false;
            }
        }

        return true;
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    public BooleanProperty getInventoryCleaner() {
        return inventoryCleaner;
    }

    public BooleanProperty getOpen_inv() {
        return open_inv;
    }

    public IntProperty getDelay() {
        return delay;
    }

    public IntProperty getBlocks() {
        return blocks;
    }

    public ListProperty<String> getItems() {
        return items;
    }

    public IntProperty getPickAxeSlot() {
        return pickAxeSlot;
    }

    public IntProperty getAxeSlot() {
        return axeSlot;
    }

    public IntProperty getShovelSlot() {
        return shovelSlot;
    }

    public IntProperty getSwordSlot() {
        return swordSlot;
    }

    public IntProperty getBowSlot() {
        return bowSlot;
    }

    public IntProperty getHeadSlot() {
        return headSlot;
    }

    public ListProperty<String> getAutoDisable() {
        return autoDisable;
    }

}
