// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.collect.HashMultimap;
import java.util.Iterator;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.Map;
import net.minecraft.util.StatCollector;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import java.util.Random;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.nbt.NBTTagCompound;
import java.text.DecimalFormat;

public final class ItemStack
{
    public static final DecimalFormat DECIMALFORMAT;
    public int stackSize;
    public int animationsToGo;
    private Item item;
    private NBTTagCompound stackTagCompound;
    private int itemDamage;
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock;
    private boolean canDestroyCacheResult;
    private Block canPlaceOnCacheBlock;
    private boolean canPlaceOnCacheResult;
    private static final String __OBFID = "CL_00000043";
    
    public ItemStack(final Block blockIn) {
        this(blockIn, 1);
    }
    
    public ItemStack(final Block blockIn, final int amount) {
        this(blockIn, amount, 0);
    }
    
    public ItemStack(final Block blockIn, final int amount, final int meta) {
        this(Item.getItemFromBlock(blockIn), amount, meta);
    }
    
    public ItemStack(final Item itemIn) {
        this(itemIn, 1);
    }
    
    public ItemStack(final Item itemIn, final int amount) {
        this(itemIn, amount, 0);
    }
    
    public ItemStack(final Item itemIn, final int amount, final int meta) {
        this.canDestroyCacheBlock = null;
        this.canDestroyCacheResult = false;
        this.canPlaceOnCacheBlock = null;
        this.canPlaceOnCacheResult = false;
        this.item = itemIn;
        this.stackSize = amount;
        this.itemDamage = meta;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }
    
    public static ItemStack loadItemStackFromNBT(final NBTTagCompound nbt) {
        final ItemStack var1 = new ItemStack();
        var1.readFromNBT(nbt);
        return (var1.getItem() != null) ? var1 : null;
    }
    
    private ItemStack() {
        this.canDestroyCacheBlock = null;
        this.canDestroyCacheResult = false;
        this.canPlaceOnCacheBlock = null;
        this.canPlaceOnCacheResult = false;
    }
    
    public ItemStack splitStack(final int amount) {
        final ItemStack var2 = new ItemStack(this.item, amount, this.itemDamage);
        if (this.stackTagCompound != null) {
            var2.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }
        this.stackSize -= amount;
        return var2;
    }
    
    public Item getItem() {
        return this.item;
    }
    
    public boolean onItemUse(final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final boolean var8 = this.getItem().onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
        if (var8) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
        return var8;
    }
    
    public float getStrVsBlock(final Block p_150997_1_) {
        return this.getItem().getStrVsBlock(this, p_150997_1_);
    }
    
    public ItemStack useItemRightClick(final World worldIn, final EntityPlayer playerIn) {
        return this.getItem().onItemRightClick(this, worldIn, playerIn);
    }
    
    public ItemStack onItemUseFinish(final World worldIn, final EntityPlayer playerIn) {
        return this.getItem().onItemUseFinish(this, worldIn, playerIn);
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        final ResourceLocation var2 = (ResourceLocation)Item.itemRegistry.getNameForObject(this.item);
        nbt.setString("id", (var2 == null) ? "minecraft:air" : var2.toString());
        nbt.setByte("Count", (byte)this.stackSize);
        nbt.setShort("Damage", (short)this.itemDamage);
        if (this.stackTagCompound != null) {
            nbt.setTag("tag", this.stackTagCompound);
        }
        return nbt;
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        if (nbt.hasKey("id", 8)) {
            this.item = Item.getByNameOrId(nbt.getString("id"));
        }
        else {
            this.item = Item.getItemById(nbt.getShort("id"));
        }
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
        return this.item != null && this.item.getMaxDamage() > 0 && (!this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable"));
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
    
    public void setItemDamage(final int meta) {
        this.itemDamage = meta;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }
    
    public int getMaxDamage() {
        return this.item.getMaxDamage();
    }
    
    public boolean attemptDamageItem(int amount, final Random rand) {
        if (!this.isItemStackDamageable()) {
            return false;
        }
        if (amount > 0) {
            final int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
            int var4 = 0;
            for (int var5 = 0; var3 > 0 && var5 < amount; ++var5) {
                if (EnchantmentDurability.negateDamage(this, var3, rand)) {
                    ++var4;
                }
            }
            amount -= var4;
            if (amount <= 0) {
                return false;
            }
        }
        this.itemDamage += amount;
        return this.itemDamage > this.getMaxDamage();
    }
    
    public void damageItem(final int amount, final EntityLivingBase entityIn) {
        if ((!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode) && this.isItemStackDamageable() && this.attemptDamageItem(amount, entityIn.getRNG())) {
            entityIn.renderBrokenItemStack(this);
            --this.stackSize;
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer var3 = (EntityPlayer)entityIn;
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
    
    public void hitEntity(final EntityLivingBase entityIn, final EntityPlayer playerIn) {
        final boolean var3 = this.item.hitEntity(this, entityIn, playerIn);
        if (var3) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }
    
    public void onBlockDestroyed(final World worldIn, final Block blockIn, final BlockPos pos, final EntityPlayer playerIn) {
        final boolean var5 = this.item.onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);
        if (var5) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }
    
    public boolean canHarvestBlock(final Block p_150998_1_) {
        return this.item.canHarvestBlock(p_150998_1_);
    }
    
    public boolean interactWithEntity(final EntityPlayer playerIn, final EntityLivingBase entityIn) {
        return this.item.itemInteractionForEntity(this, playerIn, entityIn);
    }
    
    public ItemStack copy() {
        final ItemStack var1 = new ItemStack(this.item, this.stackSize, this.itemDamage);
        if (this.stackTagCompound != null) {
            var1.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }
        return var1;
    }
    
    public static boolean areItemStackTagsEqual(final ItemStack stackA, final ItemStack stackB) {
        return (stackA == null && stackB == null) || (stackA != null && stackB != null && (stackA.stackTagCompound != null || stackB.stackTagCompound == null) && (stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)));
    }
    
    public static boolean areItemStacksEqual(final ItemStack stackA, final ItemStack stackB) {
        return (stackA == null && stackB == null) || (stackA != null && stackB != null && stackA.isItemStackEqual(stackB));
    }
    
    private boolean isItemStackEqual(final ItemStack other) {
        return this.stackSize == other.stackSize && this.item == other.item && this.itemDamage == other.itemDamage && (this.stackTagCompound != null || other.stackTagCompound == null) && (this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound));
    }
    
    public static boolean areItemsEqual(final ItemStack stackA, final ItemStack stackB) {
        return (stackA == null && stackB == null) || (stackA != null && stackB != null && stackA.isItemEqual(stackB));
    }
    
    public boolean isItemEqual(final ItemStack other) {
        return other != null && this.item == other.item && this.itemDamage == other.itemDamage;
    }
    
    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName(this);
    }
    
    public static ItemStack copyItemStack(final ItemStack stack) {
        return (stack == null) ? null : stack.copy();
    }
    
    @Override
    public String toString() {
        return this.stackSize + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
    }
    
    public void updateAnimation(final World worldIn, final Entity entityIn, final int inventorySlot, final boolean isCurrentItem) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }
        this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
    }
    
    public void onCrafting(final World worldIn, final EntityPlayer playerIn, final int amount) {
        playerIn.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.item)], amount);
        this.item.onCreated(this, worldIn, playerIn);
    }
    
    public boolean getIsItemStackEqual(final ItemStack p_179549_1_) {
        return this.isItemStackEqual(p_179549_1_);
    }
    
    public int getMaxItemUseDuration() {
        return this.getItem().getMaxItemUseDuration(this);
    }
    
    public EnumAction getItemUseAction() {
        return this.getItem().getItemUseAction(this);
    }
    
    public void onPlayerStoppedUsing(final World worldIn, final EntityPlayer playerIn, final int timeLeft) {
        this.getItem().onPlayerStoppedUsing(this, worldIn, playerIn, timeLeft);
    }
    
    public boolean hasTagCompound() {
        return this.stackTagCompound != null;
    }
    
    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }
    
    public NBTTagCompound getSubCompound(final String key, final boolean create) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) {
            return this.stackTagCompound.getCompoundTag(key);
        }
        if (create) {
            final NBTTagCompound var3 = new NBTTagCompound();
            this.setTagInfo(key, var3);
            return var3;
        }
        return null;
    }
    
    public NBTTagList getEnchantmentTagList() {
        return (this.stackTagCompound == null) ? null : this.stackTagCompound.getTagList("ench", 10);
    }
    
    public void setTagCompound(final NBTTagCompound nbt) {
        this.stackTagCompound = nbt;
    }
    
    public String getDisplayName() {
        String var1 = this.getItem().getItemStackDisplayName(this);
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
            final NBTTagCompound var2 = this.stackTagCompound.getCompoundTag("display");
            if (var2.hasKey("Name", 8)) {
                var1 = var2.getString("Name");
            }
        }
        return var1;
    }
    
    public ItemStack setStackDisplayName(final String p_151001_1_) {
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
            final NBTTagCompound var1 = this.stackTagCompound.getCompoundTag("display");
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
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10) && this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8);
    }
    
    public List getTooltip(final EntityPlayer playerIn, final boolean advanced) {
        final ArrayList var3 = Lists.newArrayList();
        String var4 = this.getDisplayName();
        if (this.hasDisplayName()) {
            var4 = EnumChatFormatting.ITALIC + var4;
        }
        var4 += EnumChatFormatting.RESET;
        if (advanced) {
            String var5 = "";
            if (var4.length() > 0) {
                var4 += " (";
                var5 = ")";
            }
            final int var6 = Item.getIdFromItem(this.item);
            if (this.getHasSubtypes()) {
                var4 += String.format("#%04d/%d%s", var6, this.itemDamage, var5);
            }
            else {
                var4 += String.format("#%04d%s", var6, var5);
            }
        }
        else if (!this.hasDisplayName() && this.item == Items.filled_map) {
            var4 = var4 + " #" + this.itemDamage;
        }
        var3.add(var4);
        int var7 = 0;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99)) {
            var7 = this.stackTagCompound.getInteger("HideFlags");
        }
        if ((var7 & 0x20) == 0x0) {
            this.item.addInformation(this, playerIn, var3, advanced);
        }
        if (this.hasTagCompound()) {
            if ((var7 & 0x1) == 0x0) {
                final NBTTagList var8 = this.getEnchantmentTagList();
                if (var8 != null) {
                    for (int var9 = 0; var9 < var8.tagCount(); ++var9) {
                        final short var10 = var8.getCompoundTagAt(var9).getShort("id");
                        final short var11 = var8.getCompoundTagAt(var9).getShort("lvl");
                        if (Enchantment.func_180306_c(var10) != null) {
                            var3.add(Enchantment.func_180306_c(var10).getTranslatedName(var11));
                        }
                    }
                }
            }
            if (this.stackTagCompound.hasKey("display", 10)) {
                final NBTTagCompound var12 = this.stackTagCompound.getCompoundTag("display");
                if (var12.hasKey("color", 3)) {
                    if (advanced) {
                        var3.add("Color: #" + Integer.toHexString(var12.getInteger("color")).toUpperCase());
                    }
                    else {
                        var3.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.dyed"));
                    }
                }
                if (var12.getTagType("Lore") == 9) {
                    final NBTTagList var13 = var12.getTagList("Lore", 8);
                    if (var13.tagCount() > 0) {
                        for (int var14 = 0; var14 < var13.tagCount(); ++var14) {
                            var3.add(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + var13.getStringTagAt(var14));
                        }
                    }
                }
            }
        }
        final Multimap var15 = this.getAttributeModifiers();
        if (!var15.isEmpty() && (var7 & 0x2) == 0x0) {
            var3.add("");
            for (final Map.Entry var17 : var15.entries()) {
                final AttributeModifier var18 = var17.getValue();
                double var19 = var18.getAmount();
                if (var18.getID() == Item.itemModifierUUID) {
                    var19 += EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
                }
                double var20;
                if (var18.getOperation() != 1 && var18.getOperation() != 2) {
                    var20 = var19;
                }
                else {
                    var20 = var19 * 100.0;
                }
                if (var19 > 0.0) {
                    var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var18.getOperation(), ItemStack.DECIMALFORMAT.format(var20), StatCollector.translateToLocal("attribute.name." + var17.getKey())));
                }
                else {
                    if (var19 >= 0.0) {
                        continue;
                    }
                    var20 *= -1.0;
                    var3.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var18.getOperation(), ItemStack.DECIMALFORMAT.format(var20), StatCollector.translateToLocal("attribute.name." + var17.getKey())));
                }
            }
        }
        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (var7 & 0x4) == 0x0) {
            var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (var7 & 0x8) == 0x0) {
            final NBTTagList var13 = this.stackTagCompound.getTagList("CanDestroy", 8);
            if (var13.tagCount() > 0) {
                var3.add("");
                var3.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canBreak"));
                for (int var14 = 0; var14 < var13.tagCount(); ++var14) {
                    final Block var21 = Block.getBlockFromName(var13.getStringTagAt(var14));
                    if (var21 != null) {
                        var3.add(EnumChatFormatting.DARK_GRAY + var21.getLocalizedName());
                    }
                    else {
                        var3.add(EnumChatFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (var7 & 0x10) == 0x0) {
            final NBTTagList var13 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            if (var13.tagCount() > 0) {
                var3.add("");
                var3.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canPlace"));
                for (int var14 = 0; var14 < var13.tagCount(); ++var14) {
                    final Block var21 = Block.getBlockFromName(var13.getStringTagAt(var14));
                    if (var21 != null) {
                        var3.add(EnumChatFormatting.DARK_GRAY + var21.getLocalizedName());
                    }
                    else {
                        var3.add(EnumChatFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }
        if (advanced) {
            if (this.isItemDamaged()) {
                var3.add("Durability: " + (this.getMaxDamage() - this.getItemDamage()) + " / " + this.getMaxDamage());
            }
            var3.add(EnumChatFormatting.DARK_GRAY + ((ResourceLocation)Item.itemRegistry.getNameForObject(this.item)).toString());
            if (this.hasTagCompound()) {
                var3.add(EnumChatFormatting.DARK_GRAY + "NBT: " + this.getTagCompound().getKeySet().size() + " tag(s)");
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
        return this.getItem().isItemTool(this) && !this.isItemEnchanted();
    }
    
    public void addEnchantment(final Enchantment ench, final int level) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }
        final NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
        final NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", (short)ench.effectId);
        var4.setShort("lvl", (byte)level);
        var3.appendTag(var4);
    }
    
    public boolean isItemEnchanted() {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9);
    }
    
    public void setTagInfo(final String key, final NBTBase value) {
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
    
    public void setItemFrame(final EntityItemFrame frame) {
        this.itemFrame = frame;
    }
    
    public EntityItemFrame getItemFrame() {
        return this.itemFrame;
    }
    
    public int getRepairCost() {
        return (this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3)) ? this.stackTagCompound.getInteger("RepairCost") : 0;
    }
    
    public void setRepairCost(final int cost) {
        if (!this.hasTagCompound()) {
            this.stackTagCompound = new NBTTagCompound();
        }
        this.stackTagCompound.setInteger("RepairCost", cost);
    }
    
    public Multimap getAttributeModifiers() {
        Object var1;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            var1 = HashMultimap.create();
            final NBTTagList var2 = this.stackTagCompound.getTagList("AttributeModifiers", 10);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                final AttributeModifier var5 = SharedMonsterAttributes.readAttributeModifierFromNBT(var4);
                if (var5 != null && var5.getID().getLeastSignificantBits() != 0L && var5.getID().getMostSignificantBits() != 0L) {
                    ((Multimap)var1).put((Object)var4.getString("AttributeName"), (Object)var5);
                }
            }
        }
        else {
            var1 = this.getItem().getItemAttributeModifiers();
        }
        return (Multimap)var1;
    }
    
    public void setItem(final Item p_150996_1_) {
        this.item = p_150996_1_;
    }
    
    public IChatComponent getChatComponent() {
        final ChatComponentText var1 = new ChatComponentText(this.getDisplayName());
        if (this.hasDisplayName()) {
            var1.getChatStyle().setItalic(true);
        }
        final IChatComponent var2 = new ChatComponentText("[").appendSibling(var1).appendText("]");
        if (this.item != null) {
            final NBTTagCompound var3 = new NBTTagCompound();
            this.writeToNBT(var3);
            var2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(var3.toString())));
            var2.getChatStyle().setColor(this.getRarity().rarityColor);
        }
        return var2;
    }
    
    public boolean canDestroy(final Block blockIn) {
        if (blockIn == this.canDestroyCacheBlock) {
            return this.canDestroyCacheResult;
        }
        this.canDestroyCacheBlock = blockIn;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            final NBTTagList var2 = this.stackTagCompound.getTagList("CanDestroy", 8);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                final Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));
                if (var4 == blockIn) {
                    return this.canDestroyCacheResult = true;
                }
            }
        }
        return this.canDestroyCacheResult = false;
    }
    
    public boolean canPlaceOn(final Block blockIn) {
        if (blockIn == this.canPlaceOnCacheBlock) {
            return this.canPlaceOnCacheResult;
        }
        this.canPlaceOnCacheBlock = blockIn;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
            final NBTTagList var2 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                final Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));
                if (var4 == blockIn) {
                    return this.canPlaceOnCacheResult = true;
                }
            }
        }
        return this.canPlaceOnCacheResult = false;
    }
    
    static {
        DECIMALFORMAT = new DecimalFormat("#.###");
    }
}
