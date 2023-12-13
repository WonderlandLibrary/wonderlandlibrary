package summer.cheat.cheats.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.cheat.cheats.combat.KillAura;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.base.utilities.TimerUtils;

public class InvManager extends Cheats {
    public Minecraft mc = Minecraft.getMinecraft();
    boolean invcleaner;
    boolean hasSet = false;
    private double delay;
    private TimerUtils timer;
    private TimerUtils updateTimer;
    boolean dropping = false;
    public int weaponSlot = 1;

    private Setting minDelay;
    private Setting maxDelay;
    private Setting blockCap;
    private Setting archeryItems;
    private Setting food;
    private Setting invCleaner;
    private Setting openInv;
    private Setting swordSlot;
    private Setting keepSword;
    private Setting keepPickaxe;
    private Setting keepAxe;
    private Setting keepShovel;

    public InvManager() {
        super("InvManager", "Manages your inventory automatically", Selection.PLAYER, false);
        this.timer = new TimerUtils();
        updateTimer = new TimerUtils();
    }

    @Override
    public void onSetup() {
        Summer.INSTANCE.settingsManager.Property(minDelay = new Setting("MinDelay", this, 50, 0, 250, true));
        Summer.INSTANCE.settingsManager.Property(maxDelay = new Setting("MaxDelay", this, 100, 0, 250, true));
        Summer.INSTANCE.settingsManager.Property(blockCap = new Setting("BlockCap", this, 128, 0, 256, true));
        Summer.INSTANCE.settingsManager.Property(archeryItems = new Setting("Archery", this, true));
        Summer.INSTANCE.settingsManager.Property(food = new Setting("Food", this, true));
        Summer.INSTANCE.settingsManager.Property(invCleaner = new Setting("InvCleaner", this, true));
        Summer.INSTANCE.settingsManager.Property(openInv = new Setting("OpenInv", this, false));
        Summer.INSTANCE.settingsManager.Property(swordSlot = new Setting("SwordSlot", this, 1, 0, 9, true));
        Summer.INSTANCE.settingsManager.Property(keepSword = new Setting("KeepSword", this, true));
        Summer.INSTANCE.settingsManager.Property(keepPickaxe = new Setting("KeepPickaxe", this, true));
        Summer.INSTANCE.settingsManager.Property(keepAxe = new Setting("KeepAxe", this, true));
        Summer.INSTANCE.settingsManager.Property(keepShovel = new Setting("KeepShovel", this, true));
    }

    @EventTarget
    public void onPre(final EventUpdate event) {
        double min = minDelay.getValDouble();
        double max = maxDelay.getValDouble();
        if (updateTimer.hasReached(Math.min(min, max))) {
            if (min > max || min == max) {
                max = min * 1.1;
            }
            delay = ThreadLocalRandom.current().nextDouble(min, max);
            hasSet = true;
        }
        if (!hasSet) {
            return;
        }
        int cap = blockCap.getValInt();
        boolean openinv = openInv.getValBoolean();
        boolean foodOption = food.getValBoolean();
        invcleaner = invCleaner.getValBoolean();
        boolean archery = archeryItems.getValBoolean();
        if (openinv && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        weaponSlot = swordSlot.getValInt();
        if (weaponSlot == 0 || weaponSlot > 9) {
            weaponSlot = 69;
        }
        weaponSlot--;
        if (event.isPre()) {
            KillAura ka = CheatManager.getInstance(KillAura.class);
            if (KillAura.target != null && ka.isToggled())
                return;
            if (Minecraft.thePlayer != null && !dropping
                    && (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory)
                    && this.timer.hasReached(delay)) {
                for (int i = 9; i < 45; ++i) {
                    if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        final ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (this.isBad(is, i) && !(is.getItem() instanceof ItemArmor)
                                && is != Minecraft.thePlayer.getCurrentEquippedItem()) {
                            if (invcleaner) {
                                this.mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, i,
                                        0, 0, Minecraft.thePlayer);
                                this.dropping = true;
                                this.timer.reset();
                            }
                            break;
                        } else {
                            if (weaponSlot < 10 && is.getItem() instanceof ItemSword && isBestWeapon(is)
                                    && 45 - i - 9 != weaponSlot && !dropping) {
                                mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, i, weaponSlot,
                                        2, Minecraft.thePlayer);
                                timer.reset();
                            }
                        }
                    }
                }
            } else if (dropping && this.timer.hasReached(delay / 2)) {
                this.mc.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, -999, 0, 0,
                        Minecraft.thePlayer);
                timer.reset();
                dropping = false;
            }
        }
        if (!invcleaner) {
            dropping = false;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.dropping = false;
    }

    private ItemStack bestSword() {
        ItemStack best = null;
        float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    final float swordD = this.getItemDamage(is);
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = is;
                    }
                }
            }
        }
        return best;
    }

    private boolean isBad(final ItemStack stack, int slot) {
        boolean swords = keepSword.getValBoolean();
        boolean axe = keepAxe.getValBoolean();
        boolean pickaxe = keepPickaxe.getValBoolean();
        boolean shovel = keepShovel.getValBoolean();
        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("\u00A7k||")) {
            return false;
        }
        if (stack.getItem() instanceof ItemBlock
                && (getBlockCount() > blockCap.getValInt()/*
         * || Scaffold.getBlacklistedBlocks().contains(((ItemBlock)stack.getItem()).
         * getBlock())
         */)) {
            return true;
        }
        if ((slot == weaponSlot && isBestWeapon(Minecraft.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack()))) {
            return false;
        }
        if (stack.getItem() instanceof ItemSword && !swords) {
            return true;
        } else if (swords && stack.getItem() instanceof ItemSword && !isBestWeapon(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemPickaxe && !pickaxe) {
            return true;
        } else if (pickaxe && stack.getItem() instanceof ItemPickaxe && !isBestPickaxe(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemAxe && !axe) {
            return true;
        } else if (axe && stack.getItem() instanceof ItemAxe && !isBestAxe(stack)) {
            return true;
        }
        if (stack.getItem().getUnlocalizedName().contains("shovel") && !shovel) {
            return true;
        } else if (shovel && stack.getItem().getUnlocalizedName().contains("shovel") && !isBestShovel(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemSword && weaponSlot > 10) {
            return true;
        }
        if (stack.getItem() instanceof ItemPotion) {
            if (isBadPotion(stack)) {
                return true;
            }
        }
        if (stack.getItem() instanceof ItemFood && food.getValBoolean()
                && !(stack.getItem() instanceof ItemAppleGold)) {
            return true;
        }
        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemArmor) {
            return true;
        }
        if ((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow"))
                && archeryItems.getValBoolean()) {
            return true;
        }
        if (((stack.getItem().getUnlocalizedName().contains("tnt"))
                || (stack.getItem().getUnlocalizedName().contains("stick"))
                || (stack.getItem().getUnlocalizedName().contains("egg"))
                || (stack.getItem().getUnlocalizedName().contains("string"))
                || (stack.getItem().getUnlocalizedName().contains("cake"))
                || (stack.getItem().getUnlocalizedName().contains("mushroom"))
                || (stack.getItem().getUnlocalizedName().contains("flint"))
                || (stack.getItem().getUnlocalizedName().contains("compass"))
                || (stack.getItem().getUnlocalizedName().contains("dyePowder"))
                || (stack.getItem().getUnlocalizedName().contains("feather"))
                || (stack.getItem().getUnlocalizedName().contains("bucket"))
                || (stack.getItem().getUnlocalizedName().contains("chest")
                && !stack.getDisplayName().toLowerCase().contains("collect"))
                || (stack.getItem().getUnlocalizedName().contains("snow"))
                || (stack.getItem().getUnlocalizedName().contains("fish"))
                || (stack.getItem().getUnlocalizedName().contains("enchant"))
                || (stack.getItem().getUnlocalizedName().contains("exp"))
                || (stack.getItem().getUnlocalizedName().contains("shears"))
                || (stack.getItem().getUnlocalizedName().contains("anvil"))
                || (stack.getItem().getUnlocalizedName().contains("torch"))
                || (stack.getItem().getUnlocalizedName().contains("seeds"))
                || (stack.getItem().getUnlocalizedName().contains("leather"))
                || (stack.getItem().getUnlocalizedName().contains("reeds"))
                || (stack.getItem().getUnlocalizedName().contains("skull"))
                || (stack.getItem().getUnlocalizedName().contains("record"))
                || (stack.getItem().getUnlocalizedName().contains("snowball"))
                || (stack.getItem() instanceof ItemGlassBottle)
                || (stack.getItem().getUnlocalizedName().contains("piston")))) {
            return true;
        }
        if (isDuplicate(stack, slot)) {
            return true;
        }
        return false;
    }

    private List<ItemStack> getBest() {
        final List<ItemStack> best = new ArrayList<ItemStack>();
        for (int i = 0; i < 4; ++i) {
            ItemStack armorStack = null;
            for (final ItemStack itemStack : Minecraft.thePlayer.inventory.armorInventory) {
                if (itemStack != null) {
                    if (itemStack.getItem() instanceof ItemArmor) {
                        final ItemArmor stackArmor = (ItemArmor) itemStack.getItem();
                        if (stackArmor.armorType == i) {
                            armorStack = itemStack;
                        }
                    }
                }
            }
            final double reduction = (armorStack == null) ? -1.0 : this.getArmorStrength(armorStack);
            ItemStack slotStack = this.findBestArmor(i);
            if (slotStack != null && this.getArmorStrength(slotStack) <= reduction) {
                slotStack = armorStack;
            }
            if (slotStack != null) {
                best.add(slotStack);
            }
        }
        return best;
    }

    public boolean isDuplicate(ItemStack stack, int slot) {
        for (int i = 9; i < 45; ++i) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is != stack && slot != i && is.getUnlocalizedName().equalsIgnoreCase(stack.getUnlocalizedName())
                        && !(is.getItem() instanceof ItemPotion) && !(is.getItem() instanceof ItemBlock)) {
                    if (is.getItem() instanceof ItemSword) {
                        if (this.getDamage(is) != this.getDamage(stack)) {
                        } else {
                            return true;
                        }
                    } else if (is.getItem() instanceof ItemTool) {
                        if (this.getToolEffect(is) != this.getToolEffect(stack)) {
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private ItemStack findBestArmor(final int itemSlot) {
        ItemStack i = null;
        double maxReduction = 0.0;
        for (int slot = 0; slot < 36; ++slot) {
            final ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[slot];
            if (itemStack != null) {
                final double reduction = this.getArmorStrength(itemStack);
                if (reduction != -1.0) {
                    final ItemArmor itemArmor = (ItemArmor) itemStack.getItem();
                    if (itemArmor.armorType == itemSlot) {
                        if (reduction >= maxReduction) {
                            maxReduction = reduction;
                            i = itemStack;
                        }
                    }
                }
            }
        }
        return i;
    }

    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return -1.0;
        }
        float damageReduction = (float) ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        final Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            final int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId()
                            || effect.getPotionID() == Potion.moveSlowdown.getId()
                            || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe))
            return false;
        float value = getToolEffect(stack);
        for (int i = 9; i < 45; i++) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = getDamage(stack);
        for (int i = 0; i < 36; i++) {
            if (Minecraft.thePlayer.inventory.mainInventory[i] != null) {
                ItemStack is = Minecraft.thePlayer.inventory.mainInventory[i];
                if (getDamage(is) > damage && (is.getItem() instanceof ItemSword))
                    return false;
            }
        }
        return true;
    }

    private float getDamage(ItemStack stack) {
        float damage = 0;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;
            damage += tool.getDamage();
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword) item;
            damage += sword.getAttackDamage();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f
                + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }

    private float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool))
            return 0;
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool) item;
        float value = 1;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5;
            }
        } else
            return 1f;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
        value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100d;
        return value;
    }
}
