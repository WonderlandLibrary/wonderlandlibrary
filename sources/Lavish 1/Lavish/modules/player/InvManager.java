// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSpade;
import java.util.Iterator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import java.util.Map;
import net.minecraft.util.DamageSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import Lavish.utils.misc.NetUtil;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.gui.inventory.GuiInventory;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.utils.misc.Timer;
import net.minecraft.client.Minecraft;
import Lavish.modules.Module;

public class InvManager extends Module
{
    public Minecraft mc;
    boolean invcleaner;
    boolean hasSet;
    private double delay;
    Timer timer;
    boolean dropping;
    public int weaponSlot;
    private boolean archeryItems;
    private int swordSlot;
    public static boolean isSorting;
    
    static {
        InvManager.isSorting = false;
    }
    
    public InvManager() {
        super("InvManager", 0, true, Category.Player, "Manages your inventory");
        this.mc = Minecraft.getMinecraft();
        this.hasSet = false;
        this.timer = new Timer();
        this.dropping = false;
        this.weaponSlot = 1;
        this.archeryItems = true;
        this.swordSlot = this.weaponSlot;
        Client.instance.setmgr.rSetting(new Setting("Manager Speed", this, 20.0, 1.0, 500.0, true));
        Client.instance.setmgr.rSetting(new Setting("BlockLimit", this, 128.0, 1.0, 640.0, true));
        Client.instance.setmgr.rSetting(new Setting("Food", this, true));
        Client.instance.setmgr.rSetting(new Setting("Cleaner", this, true));
        Client.instance.setmgr.rSetting(new Setting("OpenInv", this, false));
        Client.instance.setmgr.rSetting(new Setting("KeepSword", this, true));
        Client.instance.setmgr.rSetting(new Setting("KeepPickaxe", this, false));
        Client.instance.setmgr.rSetting(new Setting("KeepAxe", this, false));
        Client.instance.setmgr.rSetting(new Setting("KeepShovel", this, false));
    }
    
    @Override
    public void onEvent(final Event e) {
        if (this.timer.hasTimeElapsed(Client.instance.setmgr.getSettingByName("Manager Speed").getValDouble(), false)) {
            this.hasSet = true;
        }
        if (!this.hasSet) {
            return;
        }
        final int cap = (int)Client.instance.setmgr.getSettingByName("BlockLimit").getValDouble();
        final boolean openinv = Client.instance.setmgr.getSettingByName("OpenInv").getValBoolean();
        final boolean foodOption = Client.instance.setmgr.getSettingByName("Food").getValBoolean();
        this.invcleaner = Client.instance.setmgr.getSettingByName("Cleaner").getValBoolean();
        final boolean archery = this.archeryItems;
        if (openinv && !(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        this.weaponSlot = this.swordSlot;
        if (this.weaponSlot == 0 || this.weaponSlot > 9) {
            this.weaponSlot = 69;
        }
        --this.weaponSlot;
        if (e.isPre()) {
            if (this.mc.thePlayer != null && !this.dropping && (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory) && this.timer.hasTimeElapsed(Client.instance.setmgr.getSettingByName("Manager Speed").getValDouble(), false)) {
                for (int i = 9; i < 45; ++i) {
                    if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (this.isBad(is, i) && !(is.getItem() instanceof ItemArmor)) {
                            if (this.invcleaner) {
                                InvManager.isSorting = true;
                                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, i, 0, 0, this.mc.thePlayer);
                                this.dropping = true;
                                NetUtil.sendPacketNoEvents(new C0DPacketCloseWindow(0));
                                this.timer.reset();
                                break;
                            }
                            break;
                        }
                        else if (this.weaponSlot < 10 && is.getItem() instanceof ItemSword && this.isBestWeapon(is) && 45 - i - 9 != this.weaponSlot && !this.dropping) {
                            InvManager.isSorting = true;
                            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, i, this.weaponSlot, 2, this.mc.thePlayer);
                            NetUtil.sendPacketNoEvents(new C0DPacketCloseWindow(0));
                            this.timer.reset();
                        }
                    }
                }
            }
            else if (this.dropping && this.timer.hasTimeElapsed(Client.instance.setmgr.getSettingByName("Manager Speed").getValDouble(), false)) {
                this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, -999, 0, 0, this.mc.thePlayer);
                this.timer.reset();
                this.dropping = false;
                InvManager.isSorting = false;
            }
        }
        if (!this.invcleaner) {
            this.dropping = false;
        }
    }
    
    @Override
    public void onEnable() {
        this.dropping = false;
        InvManager.isSorting = false;
    }
    
    private ItemStack bestSword() {
        ItemStack best = null;
        float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
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
    
    private boolean isBad(final ItemStack stack, final int slot) {
        final boolean swords = Client.instance.setmgr.getSettingByName("KeepSword").getValBoolean();
        final boolean axe = Client.instance.setmgr.getSettingByName("KeepAxe").getValBoolean();
        final boolean pickaxe = Client.instance.setmgr.getSettingByName("KeepPickaxe").getValBoolean();
        final boolean shovel = Client.instance.setmgr.getSettingByName("KeepShovel").getValBoolean();
        return !stack.getDisplayName().toLowerCase().contains("(right click)") && !stack.getDisplayName().toLowerCase().contains("§k||") && ((stack.getItem() instanceof ItemBlock && this.getBlockCount() > Client.instance.setmgr.getSettingByName("BlockLimit").getValDouble()) || ((slot != this.weaponSlot || !this.isBestWeapon(this.mc.thePlayer.inventoryContainer.getSlot(this.weaponSlot).getStack())) && ((stack.getItem() instanceof ItemSword && !swords) || (swords && stack.getItem() instanceof ItemSword && !this.isBestWeapon(stack)) || (stack.getItem() instanceof ItemPickaxe && !pickaxe) || (pickaxe && stack.getItem() instanceof ItemPickaxe && !this.isBestPickaxe(stack)) || (stack.getItem() instanceof ItemAxe && !axe) || (axe && stack.getItem() instanceof ItemAxe && !this.isBestAxe(stack)) || (stack.getItem().getUnlocalizedName().contains("shovel") && !shovel) || (shovel && stack.getItem().getUnlocalizedName().contains("shovel") && !this.isBestShovel(stack)) || (stack.getItem() instanceof ItemSword && this.weaponSlot > 10) || (stack.getItem() instanceof ItemPotion && this.isBadPotion(stack)) || ((!(stack.getItem() instanceof ItemFood) || !Client.instance.setmgr.getSettingByName("Food").getValBoolean() || stack.getItem() instanceof ItemAppleGold) && (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemArmor || (((!(stack.getItem() instanceof ItemBow) && !stack.getItem().getUnlocalizedName().contains("arrow")) || !this.archeryItems) && (stack.getItem() instanceof ItemFishingRod || (stack.getItem().getUnlocalizedName().contains("tnt") || stack.getItem().getUnlocalizedName().contains("stick") || stack.getItem().getUnlocalizedName().contains("egg") || stack.getItem().getUnlocalizedName().contains("string") || stack.getItem().getUnlocalizedName().contains("cake") || stack.getItem().getUnlocalizedName().contains("mushroom") || stack.getItem().getUnlocalizedName().contains("flint") || stack.getItem().getUnlocalizedName().contains("dyePowder") || stack.getItem().getUnlocalizedName().contains("feather") || stack.getItem().getUnlocalizedName().contains("bucket") || (stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) || stack.getItem().getUnlocalizedName().contains("snow") || stack.getItem().getUnlocalizedName().contains("fish") || stack.getItem().getUnlocalizedName().contains("enchant") || stack.getItem().getUnlocalizedName().contains("exp") || stack.getItem().getUnlocalizedName().contains("shears") || stack.getItem().getUnlocalizedName().contains("anvil") || stack.getItem().getUnlocalizedName().contains("torch") || stack.getItem().getUnlocalizedName().contains("seeds") || stack.getItem().getUnlocalizedName().contains("leather") || stack.getItem().getUnlocalizedName().contains("reeds") || stack.getItem().getUnlocalizedName().contains("skull") || stack.getItem().getUnlocalizedName().contains("record") || stack.getItem().getUnlocalizedName().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getUnlocalizedName().contains("piston")) || this.isDuplicate(stack, slot))))))));
    }
    
    private List<ItemStack> getBest() {
        final List<ItemStack> best = new ArrayList<ItemStack>();
        for (int i = 0; i < 4; ++i) {
            ItemStack armorStack = null;
            ItemStack[] armorInventory;
            for (int length = (armorInventory = this.mc.thePlayer.inventory.armorInventory).length, j = 0; j < length; ++j) {
                final ItemStack itemStack = armorInventory[j];
                if (itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                    final ItemArmor stackArmor = (ItemArmor)itemStack.getItem();
                    if (stackArmor.armorType == i) {
                        armorStack = itemStack;
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
    
    public boolean isDuplicate(final ItemStack stack, final int slot) {
        for (int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is != stack && slot != i && is.getUnlocalizedName().equalsIgnoreCase(stack.getUnlocalizedName()) && !(is.getItem() instanceof ItemPotion) && !(is.getItem() instanceof ItemBlock)) {
                    if (is.getItem() instanceof ItemSword) {
                        if (this.getDamage(is) == this.getDamage(stack)) {
                            return true;
                        }
                    }
                    else if (is.getItem() instanceof ItemTool && this.getToolEffect(is) == this.getToolEffect(stack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
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
            final ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[slot];
            if (itemStack != null) {
                final double reduction = this.getArmorStrength(itemStack);
                if (reduction != -1.0) {
                    final ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
                    if (itemArmor.armorType == itemSlot && reduction >= maxReduction) {
                        maxReduction = reduction;
                        i = itemStack;
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
        float damageReduction = (float)((ItemArmor)itemStack.getItem()).damageReduceAmount;
        final Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            final int level = enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }
    
    private boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword)itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }
    
    private boolean isBestPickaxe(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestShovel(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBestAxe(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        final float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (this.getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !this.isBestWeapon(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isBestWeapon(final ItemStack stack) {
        final float damage = this.getDamage(stack);
        for (int i = 0; i < 36; ++i) {
            if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = this.mc.thePlayer.inventory.mainInventory[i];
                if (this.getDamage(is) > damage && is.getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private float getDamage(final ItemStack stack) {
        float damage = 0.0f;
        final Item item = stack.getItem();
        if (item instanceof ItemTool) {
            final ItemTool tool = (ItemTool)item;
            damage += tool.getMaxDamage();
        }
        if (item instanceof ItemSword) {
            final ItemSword sword = (ItemSword)item;
            damage += sword.getMaxDamage();
        }
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
        return damage;
    }
    
    private float getToolEffect(final ItemStack stack) {
        final Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        final String name = item.getUnlocalizedName();
        final ItemTool tool = (ItemTool)item;
        float value = 1.0f;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        else {
            if (!(item instanceof ItemAxe)) {
                return 1.0f;
            }
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        }
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }
}
