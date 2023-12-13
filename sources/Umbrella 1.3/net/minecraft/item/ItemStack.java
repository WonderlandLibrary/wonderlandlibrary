/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Multimap
 */
package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public final class ItemStack {
    public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.###");
    public int stackSize;
    public int animationsToGo;
    private Item item;
    private NBTTagCompound stackTagCompound;
    private int itemDamage;
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock = null;
    private boolean canDestroyCacheResult = false;
    private Block canPlaceOnCacheBlock = null;
    private boolean canPlaceOnCacheResult = false;
    private static final String __OBFID = "CL_00000043";

    public ItemStack(Block blockIn) {
        this(blockIn, 1);
    }

    public ItemStack(Block blockIn, int amount) {
        this(blockIn, amount, 0);
    }

    public ItemStack(Block blockIn, int amount, int meta) {
        this(Item.getItemFromBlock(blockIn), amount, meta);
    }

    public ItemStack(Item itemIn) {
        this(itemIn, 1);
    }

    public ItemStack(Item itemIn, int amount) {
        this(itemIn, amount, 0);
    }

    public ItemStack(Item itemIn, int amount, int meta) {
        this.item = itemIn;
        this.stackSize = amount;
        this.itemDamage = meta;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }

    public static ItemStack loadItemStackFromNBT(NBTTagCompound nbt) {
        ItemStack var1 = new ItemStack();
        var1.readFromNBT(nbt);
        return var1.getItem() != null ? var1 : null;
    }

    private ItemStack() {
    }

    public ItemStack splitStack(int amount) {
        ItemStack var2 = new ItemStack(this.item, amount, this.itemDamage);
        if (this.stackTagCompound != null) {
            var2.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }
        this.stackSize -= amount;
        return var2;
    }

    public Item getItem() {
        return this.item;
    }

    public boolean onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean var8 = this.getItem().onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
        if (var8) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
        return var8;
    }

    public float getStrVsBlock(Block p_150997_1_) {
        return this.getItem().getStrVsBlock(this, p_150997_1_);
    }

    public ItemStack useItemRightClick(World worldIn, EntityPlayer playerIn) {
        return this.getItem().onItemRightClick(this, worldIn, playerIn);
    }

    public ItemStack onItemUseFinish(World worldIn, EntityPlayer playerIn) {
        return this.getItem().onItemUseFinish(this, worldIn, playerIn);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        ResourceLocation var2 = (ResourceLocation)Item.itemRegistry.getNameForObject(this.item);
        nbt.setString("id", var2 == null ? "minecraft:air" : var2.toString());
        nbt.setByte("Count", (byte)this.stackSize);
        nbt.setShort("Damage", (short)this.itemDamage);
        if (this.stackTagCompound != null) {
            nbt.setTag("tag", this.stackTagCompound);
        }
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.item = nbt.hasKey("id", 8) ? Item.getByNameOrId(nbt.getString("id")) : Item.getItemById(nbt.getShort("id"));
        this.stackSize = nbt.getByte("Count");
        this.itemDamage = nbt.getShort("Damage");
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
        if (nbt.hasKey("tag", 10)) {
            this.stackTagCompound = nbt.getCompoundTag("tag");
            if (this.item != null) {
                this.item.updateItemStackNBT(this.stackTagCompound);
            }
        }
    }

    public int getMaxStackSize() {
        return this.getItem().getItemStackLimit();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }

    public boolean isItemStackDamageable() {
        return this.item == null ? false : (this.item.getMaxDamage() <= 0 ? false : !this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable"));
    }

    public boolean getHasSubtypes() {
        return this.item.getHasSubtypes();
    }

    public boolean isItemDamaged() {
        return this.isItemStackDamageable() && this.itemDamage > 0;
    }

    public int getItemDamage() {
        return this.itemDamage;
    }

    public int getMetadata() {
        return this.itemDamage;
    }

    public void setItemDamage(int meta) {
        this.itemDamage = meta;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }

    public int getMaxDamage() {
        return this.item.getMaxDamage();
    }

    public boolean attemptDamageItem(int amount, Random rand) {
        if (!this.isItemStackDamageable()) {
            return false;
        }
        if (amount > 0) {
            int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
            int var4 = 0;
            for (int var5 = 0; var3 > 0 && var5 < amount; ++var5) {
                if (!EnchantmentDurability.negateDamage(this, var3, rand)) continue;
                ++var4;
            }
            if ((amount -= var4) <= 0) {
                return false;
            }
        }
        this.itemDamage += amount;
        return this.itemDamage > this.getMaxDamage();
    }

    public void damageItem(int amount, EntityLivingBase entityIn) {
        if ((!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode) && this.isItemStackDamageable() && this.attemptDamageItem(amount, entityIn.getRNG())) {
            entityIn.renderBrokenItemStack(this);
            --this.stackSize;
            if (entityIn instanceof EntityPlayer) {
                EntityPlayer var3 = (EntityPlayer)entityIn;
                var3.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(this.item)]);
                if (this.stackSize == 0 && this.getItem() instanceof ItemBow) {
                    var3.destroyCurrentEquippedItem();
                }
            }
            if (this.stackSize < 0) {
                this.stackSize = 0;
            }
            this.itemDamage = 0;
        }
    }

    public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn) {
        boolean var3 = this.item.hitEntity(this, entityIn, playerIn);
        if (var3) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }

    public void onBlockDestroyed(World worldIn, Block blockIn, BlockPos pos, EntityPlayer playerIn) {
        boolean var5 = this.item.onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);
        if (var5) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }

    public boolean canHarvestBlock(Block p_150998_1_) {
        return this.item.canHarvestBlock(p_150998_1_);
    }

    public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn) {
        return this.item.itemInteractionForEntity(this, playerIn, entityIn);
    }

    public ItemStack copy() {
        ItemStack var1 = new ItemStack(this.item, this.stackSize, this.itemDamage);
        if (this.stackTagCompound != null) {
            var1.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }
        return var1;
    }

    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? (stackA.stackTagCompound == null && stackB.stackTagCompound != null ? false : stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)) : false);
    }

    public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? stackA.isItemStackEqual(stackB) : false);
    }

    private boolean isItemStackEqual(ItemStack other) {
        return this.stackSize != other.stackSize ? false : (this.item != other.item ? false : (this.itemDamage != other.itemDamage ? false : (this.stackTagCompound == null && other.stackTagCompound != null ? false : this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound))));
    }

    public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? stackA.isItemEqual(stackB) : false);
    }

    public boolean isItemEqual(ItemStack other) {
        return other != null && this.item == other.item && this.itemDamage == other.itemDamage;
    }

    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName(this);
    }

    public static ItemStack copyItemStack(ItemStack stack) {
        return stack == null ? null : stack.copy();
    }

    public String toString() {
        return String.valueOf(this.stackSize) + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
    }

    public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }
        this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
    }

    public void onCrafting(World worldIn, EntityPlayer playerIn, int amount) {
        playerIn.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.item)], amount);
        this.item.onCreated(this, worldIn, playerIn);
    }

    public boolean getIsItemStackEqual(ItemStack p_179549_1_) {
        return this.isItemStackEqual(p_179549_1_);
    }

    public int getMaxItemUseDuration() {
        return this.getItem().getMaxItemUseDuration(this);
    }

    public EnumAction getItemUseAction() {
        return this.getItem().getItemUseAction(this);
    }

    public void onPlayerStoppedUsing(World worldIn, EntityPlayer playerIn, int timeLeft) {
        this.getItem().onPlayerStoppedUsing(this, worldIn, playerIn, timeLeft);
    }

    public boolean hasTagCompound() {
        return this.stackTagCompound != null;
    }

    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }

    public NBTTagCompound getSubCompound(String key, boolean create) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) {
            return this.stackTagCompound.getCompoundTag(key);
        }
        if (create) {
            NBTTagCompound var3 = new NBTTagCompound();
            this.setTagInfo(key, var3);
            return var3;
        }
        return null;
    }

    public NBTTagList getEnchantmentTagList() {
        return this.stackTagCompound == null ? null : this.stackTagCompound.getTagList("ench", 10);
    }

    public void setTagCompound(NBTTagCompound nbt) {
        this.stackTagCompound = nbt;
    }

    public String getDisplayName() {
        NBTTagCompound var2;
        String var1 = this.getItem().getItemStackDisplayName(this);
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10) && (var2 = this.stackTagCompound.getCompoundTag("display")).hasKey("Name", 8)) {
            var1 = var2.getString("Name");
        }
        return var1;
    }

    public ItemStack setStackDisplayName(String p_151001_1_) {
        if (this.stackTagCompound == null) {
            this.stackTagCompound = new NBTTagCompound();
        }
        if (!this.stackTagCompound.hasKey("display", 10)) {
            this.stackTagCompound.setTag("display", new NBTTagCompound());
        }
        this.stackTagCompound.getCompoundTag("display").setString("Name", p_151001_1_);
        return this;
    }

    public void clearCustomName() {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
            NBTTagCompound var1 = this.stackTagCompound.getCompoundTag("display");
            var1.removeTag("Name");
            if (var1.hasNoTags()) {
                this.stackTagCompound.removeTag("display");
                if (this.stackTagCompound.hasNoTags()) {
                    this.setTagCompound(null);
                }
            }
        }
    }

    public boolean hasDisplayName() {
        return this.stackTagCompound == null ? false : (!this.stackTagCompound.hasKey("display", 10) ? false : this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8));
    }

    public List getTooltip(EntityPlayer playerIn, boolean advanced) {
        NBTTagList var18;
        Multimap var17;
        int var20;
        ArrayList var3 = Lists.newArrayList();
        String var4 = this.getDisplayName();
        if (this.hasDisplayName()) {
            var4 = (Object)((Object)EnumChatFormatting.ITALIC) + var4;
        }
        var4 = String.valueOf(var4) + (Object)((Object)EnumChatFormatting.RESET);
        if (advanced) {
            String var5 = "";
            if (var4.length() > 0) {
                var4 = String.valueOf(var4) + " (";
                var5 = ")";
            }
            int var6 = Item.getIdFromItem(this.item);
            var4 = this.getHasSubtypes() ? String.valueOf(var4) + String.format("#%04d/%d%s", var6, this.itemDamage, var5) : String.valueOf(var4) + String.format("#%04d%s", var6, var5);
        } else if (!this.hasDisplayName() && this.item == Items.filled_map) {
            var4 = String.valueOf(var4) + " #" + this.itemDamage;
        }
        var3.add(var4);
        int var14 = 0;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99)) {
            var14 = this.stackTagCompound.getInteger("HideFlags");
        }
        if ((var14 & 0x20) == 0) {
            this.item.addInformation(this, playerIn, var3, advanced);
        }
        if (this.hasTagCompound()) {
            NBTTagList var15;
            if ((var14 & 1) == 0 && (var15 = this.getEnchantmentTagList()) != null) {
                for (int var7 = 0; var7 < var15.tagCount(); ++var7) {
                    short var8 = var15.getCompoundTagAt(var7).getShort("id");
                    short var9 = var15.getCompoundTagAt(var7).getShort("lvl");
                    if (Enchantment.func_180306_c(var8) == null) continue;
                    var3.add(Enchantment.func_180306_c(var8).getTranslatedName(var9));
                }
            }
            if (this.stackTagCompound.hasKey("display", 10)) {
                NBTTagList var182;
                NBTTagCompound var16 = this.stackTagCompound.getCompoundTag("display");
                if (var16.hasKey("color", 3)) {
                    if (advanced) {
                        var3.add("Color: #" + Integer.toHexString(var16.getInteger("color")).toUpperCase());
                    } else {
                        var3.add((Object)((Object)EnumChatFormatting.ITALIC) + StatCollector.translateToLocal("item.dyed"));
                    }
                }
                if (var16.getTagType("Lore") == 9 && (var182 = var16.getTagList("Lore", 8)).tagCount() > 0) {
                    for (var20 = 0; var20 < var182.tagCount(); ++var20) {
                        var3.add((Object)((Object)EnumChatFormatting.DARK_PURPLE) + (Object)((Object)EnumChatFormatting.ITALIC) + var182.getStringTagAt(var20));
                    }
                }
            }
        }
        if (!(var17 = this.getAttributeModifiers()).isEmpty() && (var14 & 2) == 0) {
            var3.add("");
            for (Map.Entry var21 : var17.entries()) {
                AttributeModifier var22 = (AttributeModifier)var21.getValue();
                double var10 = var22.getAmount();
                if (var22.getID() == Item.itemModifierUUID) {
                    var10 += (double)EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
                }
                double var12 = var22.getOperation() != 1 && var22.getOperation() != 2 ? var10 : var10 * 100.0;
                if (var10 > 0.0) {
                    var3.add((Object)((Object)EnumChatFormatting.BLUE) + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var22.getOperation(), DECIMALFORMAT.format(var12), StatCollector.translateToLocal("attribute.name." + (String)var21.getKey())));
                    continue;
                }
                if (!(var10 < 0.0)) continue;
                var3.add((Object)((Object)EnumChatFormatting.RED) + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var22.getOperation(), DECIMALFORMAT.format(var12 *= -1.0), StatCollector.translateToLocal("attribute.name." + (String)var21.getKey())));
            }
        }
        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (var14 & 4) == 0) {
            var3.add((Object)((Object)EnumChatFormatting.BLUE) + StatCollector.translateToLocal("item.unbreakable"));
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (var14 & 8) == 0 && (var18 = this.stackTagCompound.getTagList("CanDestroy", 8)).tagCount() > 0) {
            var3.add("");
            var3.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocal("item.canBreak"));
            for (var20 = 0; var20 < var18.tagCount(); ++var20) {
                Block var23 = Block.getBlockFromName(var18.getStringTagAt(var20));
                if (var23 != null) {
                    var3.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + var23.getLocalizedName());
                    continue;
                }
                var3.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + "missingno");
            }
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (var14 & 0x10) == 0 && (var18 = this.stackTagCompound.getTagList("CanPlaceOn", 8)).tagCount() > 0) {
            var3.add("");
            var3.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocal("item.canPlace"));
            for (var20 = 0; var20 < var18.tagCount(); ++var20) {
                Block var23 = Block.getBlockFromName(var18.getStringTagAt(var20));
                if (var23 != null) {
                    var3.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + var23.getLocalizedName());
                    continue;
                }
                var3.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + "missingno");
            }
        }
        if (advanced) {
            if (this.isItemDamaged()) {
                var3.add("Durability: " + (this.getMaxDamage() - this.getItemDamage()) + " / " + this.getMaxDamage());
            }
            var3.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + ((ResourceLocation)Item.itemRegistry.getNameForObject(this.item)).toString());
            if (this.hasTagCompound()) {
                var3.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + "NBT: " + this.getTagCompound().getKeySet().size() + " tag(s)");
            }
        }
        return var3;
    }

    public boolean hasEffect() {
        return this.getItem().hasEffect(this);
    }

    public EnumRarity getRarity() {
        return this.getItem().getRarity(this);
    }

    public boolean isItemEnchantable() {
        return !this.getItem().isItemTool(this) ? false : !this.isItemEnchanted();
    }

    public void addEnchantment(Enchantment ench, int level) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }
        NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", (short)ench.effectId);
        var4.setShort("lvl", (byte)level);
        var3.appendTag(var4);
    }

    public boolean isItemEnchanted() {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9);
    }

    public void setTagInfo(String key, NBTBase value) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        this.stackTagCompound.setTag(key, value);
    }

    public boolean canEditBlocks() {
        return this.getItem().canItemEditBlocks();
    }

    public boolean isOnItemFrame() {
        return this.itemFrame != null;
    }

    public void setItemFrame(EntityItemFrame frame) {
        this.itemFrame = frame;
    }

    public EntityItemFrame getItemFrame() {
        return this.itemFrame;
    }

    public int getRepairCost() {
        return this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3) ? this.stackTagCompound.getInteger("RepairCost") : 0;
    }

    public void setRepairCost(int cost) {
        if (!this.hasTagCompound()) {
            this.stackTagCompound = new NBTTagCompound();
        }
        this.stackTagCompound.setInteger("RepairCost", cost);
    }

    public Multimap getAttributeModifiers() {
        Multimap var1;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            var1 = HashMultimap.create();
            NBTTagList var2 = this.stackTagCompound.getTagList("AttributeModifiers", 10);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                AttributeModifier var5 = SharedMonsterAttributes.readAttributeModifierFromNBT(var4);
                if (var5 == null || var5.getID().getLeastSignificantBits() == 0L || var5.getID().getMostSignificantBits() == 0L) continue;
                var1.put((Object)var4.getString("AttributeName"), (Object)var5);
            }
        } else {
            var1 = this.getItem().getItemAttributeModifiers();
        }
        return var1;
    }

    public void setItem(Item p_150996_1_) {
        this.item = p_150996_1_;
    }

    public IChatComponent getChatComponent() {
        ChatComponentText var1 = new ChatComponentText(this.getDisplayName());
        if (this.hasDisplayName()) {
            var1.getChatStyle().setItalic(true);
        }
        IChatComponent var2 = new ChatComponentText("[").appendSibling(var1).appendText("]");
        if (this.item != null) {
            NBTTagCompound var3 = new NBTTagCompound();
            this.writeToNBT(var3);
            var2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(var3.toString())));
            var2.getChatStyle().setColor(this.getRarity().rarityColor);
        }
        return var2;
    }

    public boolean canDestroy(Block blockIn) {
        if (blockIn == this.canDestroyCacheBlock) {
            return this.canDestroyCacheResult;
        }
        this.canDestroyCacheBlock = blockIn;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            NBTTagList var2 = this.stackTagCompound.getTagList("CanDestroy", 8);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));
                if (var4 != blockIn) continue;
                this.canDestroyCacheResult = true;
                return true;
            }
        }
        this.canDestroyCacheResult = false;
        return false;
    }

    public boolean canPlaceOn(Block blockIn) {
        if (blockIn == this.canPlaceOnCacheBlock) {
            return this.canPlaceOnCacheResult;
        }
        this.canPlaceOnCacheBlock = blockIn;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
            NBTTagList var2 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));
                if (var4 != blockIn) continue;
                this.canPlaceOnCacheResult = true;
                return true;
            }
        }
        this.canPlaceOnCacheResult = false;
        return false;
    }
}

