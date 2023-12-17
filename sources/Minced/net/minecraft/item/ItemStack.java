// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import com.google.common.collect.HashMultimap;
import java.util.Iterator;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.Map;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextFormatting;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.Entity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Random;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import ru.tuskevich.modules.impl.PLAYER.GoldenAppleTimer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ActionResult;
import net.minecraft.block.state.IBlockState;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.BlockEntityTag;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.nbt.NBTTagCompound;
import java.text.DecimalFormat;

public final class ItemStack
{
    public static final ItemStack EMPTY;
    public static final DecimalFormat DECIMALFORMAT;
    public int stackSize;
    private int animationsToGo;
    private final Item item;
    private NBTTagCompound stackTagCompound;
    private boolean isEmpty;
    private int itemDamage;
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock;
    private boolean canDestroyCacheResult;
    private Block canPlaceOnCacheBlock;
    private boolean canPlaceOnCacheResult;
    
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
        this.item = itemIn;
        this.itemDamage = meta;
        this.stackSize = amount;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
        this.updateEmptyState();
    }
    
    private void updateEmptyState() {
        this.isEmpty = this.isEmpty();
    }
    
    public ItemStack(final NBTTagCompound compound) {
        this.item = Item.getByNameOrId(compound.getString("id"));
        this.stackSize = compound.getByte("Count");
        this.itemDamage = Math.max(0, compound.getShort("Damage"));
        if (compound.hasKey("tag", 10)) {
            this.stackTagCompound = compound.getCompoundTag("tag");
            if (this.item != null) {
                this.item.updateItemStackNBT(compound);
            }
        }
        this.updateEmptyState();
    }
    
    public boolean isEmpty() {
        return this == ItemStack.EMPTY || this.item == null || this.item == Item.getItemFromBlock(Blocks.AIR) || this.stackSize <= 0 || this.itemDamage < -32768 || this.itemDamage > 65535;
    }
    
    public static void registerFixes(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.ITEM_INSTANCE, new BlockEntityTag());
        fixer.registerWalker(FixTypes.ITEM_INSTANCE, new EntityTag());
    }
    
    public ItemStack splitStack(final int amount) {
        final int i = Math.min(amount, this.stackSize);
        final ItemStack itemstack = this.copy();
        itemstack.setCount(i);
        this.shrink(i);
        return itemstack;
    }
    
    public Item getItem() {
        return this.isEmpty ? Item.getItemFromBlock(Blocks.AIR) : this.item;
    }
    
    public EnumActionResult onItemUse(final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final EnumActionResult enumactionresult = this.getItem().onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);
        if (enumactionresult == EnumActionResult.SUCCESS) {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }
        return enumactionresult;
    }
    
    public float getDestroySpeed(final IBlockState blockIn) {
        return this.getItem().getDestroySpeed(this, blockIn);
    }
    
    public ActionResult<ItemStack> useItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand) {
        return this.getItem().onItemRightClick(worldIn, playerIn, hand);
    }
    
    public ItemStack onItemUseFinish(final World worldIn, final EntityLivingBase entityLiving) {
        if (this.getItem() instanceof ItemAppleGold) {
            GoldenAppleTimer.lastConsumeTime = (this.getItem().hasEffect(this) ? (System.currentTimeMillis() + 5000L) : System.currentTimeMillis());
        }
        return this.getItem().onItemUseFinish(this, worldIn, entityLiving);
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        final ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.item);
        nbt.setString("id", (resourcelocation == null) ? "minecraft:air" : resourcelocation.toString());
        nbt.setByte("Count", (byte)this.stackSize);
        nbt.setShort("Damage", (short)this.itemDamage);
        if (this.stackTagCompound != null) {
            nbt.setTag("tag", this.stackTagCompound);
        }
        return nbt;
    }
    
    public int getMaxStackSize() {
        return this.getItem().getItemStackLimit();
    }
    
    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }
    
    public boolean isItemStackDamageable() {
        return !this.isEmpty && this.item.getMaxDamage() > 0 && (!this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable"));
    }
    
    public boolean getHasSubtypes() {
        return this.getItem().getHasSubtypes();
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
        return this.getItem().getMaxDamage();
    }
    
    public boolean attemptDamageItem(int amount, final Random rand, @Nullable final EntityPlayerMP damager) {
        if (!this.isItemStackDamageable()) {
            return false;
        }
        if (amount > 0) {
            final int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this);
            int j = 0;
            for (int k = 0; i > 0 && k < amount; ++k) {
                if (EnchantmentDurability.negateDamage(this, i, rand)) {
                    ++j;
                }
            }
            amount -= j;
            if (amount <= 0) {
                return false;
            }
        }
        if (damager != null && amount != 0) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(damager, this, this.itemDamage + amount);
        }
        this.itemDamage += amount;
        return this.itemDamage > this.getMaxDamage();
    }
    
    public void damageItem(final int amount, final EntityLivingBase entityIn) {
        if ((!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode) && this.isItemStackDamageable() && this.attemptDamageItem(amount, entityIn.getRNG(), (entityIn instanceof EntityPlayerMP) ? ((EntityPlayerMP)entityIn) : null)) {
            entityIn.renderBrokenItemStack(this);
            this.shrink(1);
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)entityIn;
                entityplayer.addStat(StatList.getObjectBreakStats(this.item));
            }
            this.itemDamage = 0;
        }
    }
    
    public void hitEntity(final EntityLivingBase entityIn, final EntityPlayer playerIn) {
        final boolean flag = this.item.hitEntity(this, entityIn, playerIn);
        if (flag) {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }
    }
    
    public void onBlockDestroyed(final World worldIn, final IBlockState blockIn, final BlockPos pos, final EntityPlayer playerIn) {
        final boolean flag = this.getItem().onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);
        if (flag) {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }
    }
    
    public boolean canHarvestBlock(final IBlockState blockIn) {
        return this.getItem().canHarvestBlock(blockIn);
    }
    
    public boolean interactWithEntity(final EntityPlayer playerIn, final EntityLivingBase entityIn, final EnumHand hand) {
        return this.getItem().itemInteractionForEntity(this, playerIn, entityIn, hand);
    }
    
    public ItemStack copy() {
        final ItemStack itemstack = new ItemStack(this.item, this.stackSize, this.itemDamage);
        itemstack.setAnimationsToGo(this.getAnimationsToGo());
        if (this.stackTagCompound != null) {
            itemstack.stackTagCompound = this.stackTagCompound.copy();
        }
        return itemstack;
    }
    
    public static boolean areItemStackTagsEqual(final ItemStack stackA, final ItemStack stackB) {
        return (stackA.isEmpty() && stackB.isEmpty()) || (!stackA.isEmpty() && !stackB.isEmpty() && (stackA.stackTagCompound != null || stackB.stackTagCompound == null) && (stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)));
    }
    
    public static boolean areItemStacksEqual(final ItemStack stackA, final ItemStack stackB) {
        return (stackA.isEmpty() && stackB.isEmpty()) || (!stackA.isEmpty() && !stackB.isEmpty() && stackA.isItemStackEqual(stackB));
    }
    
    private boolean isItemStackEqual(final ItemStack other) {
        return this.stackSize == other.stackSize && this.getItem() == other.getItem() && this.itemDamage == other.itemDamage && (this.stackTagCompound != null || other.stackTagCompound == null) && (this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound));
    }
    
    public static boolean areItemsEqual(final ItemStack stackA, final ItemStack stackB) {
        return stackA == stackB || (!stackA.isEmpty() && !stackB.isEmpty() && stackA.isItemEqual(stackB));
    }
    
    public static boolean areItemsEqualIgnoreDurability(final ItemStack stackA, final ItemStack stackB) {
        return stackA == stackB || (!stackA.isEmpty() && !stackB.isEmpty() && stackA.isItemEqualIgnoreDurability(stackB));
    }
    
    public boolean isItemEqual(final ItemStack other) {
        return !other.isEmpty() && this.item == other.item && this.itemDamage == other.itemDamage;
    }
    
    public boolean isItemEqualIgnoreDurability(final ItemStack stack) {
        if (!this.isItemStackDamageable()) {
            return this.isItemEqual(stack);
        }
        return !stack.isEmpty() && this.item == stack.item;
    }
    
    public String getTranslationKey() {
        return this.getItem().getTranslationKey(this);
    }
    
    @Override
    public String toString() {
        return this.stackSize + "x" + this.getItem().getTranslationKey() + "@" + this.itemDamage;
    }
    
    public void updateAnimation(final World worldIn, final Entity entityIn, final int inventorySlot, final boolean isCurrentItem) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }
        if (this.item != null) {
            this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
        }
    }
    
    public void onCrafting(final World worldIn, final EntityPlayer playerIn, final int amount) {
        playerIn.addStat(StatList.getCraftStats(this.item), amount);
        this.getItem().onCreated(this, worldIn, playerIn);
    }
    
    public int getMaxItemUseDuration() {
        return this.getItem().getMaxItemUseDuration(this);
    }
    
    public EnumAction getItemUseAction() {
        return this.getItem().getItemUseAction(this);
    }
    
    public void onPlayerStoppedUsing(final World worldIn, final EntityLivingBase entityLiving, final int timeLeft) {
        this.getItem().onPlayerStoppedUsing(this, worldIn, entityLiving, timeLeft);
    }
    
    public boolean hasTagCompound() {
        return !this.isEmpty && this.stackTagCompound != null;
    }
    
    @Nullable
    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }
    
    public NBTTagCompound getOrCreateSubCompound(final String key) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) {
            return this.stackTagCompound.getCompoundTag(key);
        }
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.setTagInfo(key, nbttagcompound);
        return nbttagcompound;
    }
    
    @Nullable
    public NBTTagCompound getSubCompound(final String key) {
        return (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) ? this.stackTagCompound.getCompoundTag(key) : null;
    }
    
    public void removeSubCompound(final String key) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) {
            this.stackTagCompound.removeTag(key);
        }
    }
    
    public NBTTagList getEnchantmentTagList() {
        return (this.stackTagCompound != null) ? this.stackTagCompound.getTagList("ench", 10) : new NBTTagList();
    }
    
    public void setTagCompound(@Nullable final NBTTagCompound nbt) {
        this.stackTagCompound = nbt;
    }
    
    public String getDisplayName() {
        final NBTTagCompound nbttagcompound = this.getSubCompound("display");
        if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("Name", 8)) {
                return nbttagcompound.getString("Name");
            }
            if (nbttagcompound.hasKey("LocName", 8)) {
                return I18n.translateToLocal(nbttagcompound.getString("LocName"));
            }
        }
        return this.getItem().getItemStackDisplayName(this);
    }
    
    public ItemStack setTranslatableName(final String p_190924_1_) {
        this.getOrCreateSubCompound("display").setString("LocName", p_190924_1_);
        return this;
    }
    
    public ItemStack setStackDisplayName(final String displayName) {
        this.getOrCreateSubCompound("display").setString("Name", displayName);
        return this;
    }
    
    public void clearCustomName() {
        final NBTTagCompound nbttagcompound = this.getSubCompound("display");
        if (nbttagcompound != null) {
            nbttagcompound.removeTag("Name");
            if (nbttagcompound.isEmpty()) {
                this.removeSubCompound("display");
            }
        }
        if (this.stackTagCompound != null && this.stackTagCompound.isEmpty()) {
            this.stackTagCompound = null;
        }
    }
    
    public boolean hasDisplayName() {
        final NBTTagCompound nbttagcompound = this.getSubCompound("display");
        return nbttagcompound != null && nbttagcompound.hasKey("Name", 8);
    }
    
    public List<String> getTooltip(@Nullable final EntityPlayer playerIn, final ITooltipFlag advanced) {
        final List<String> list = (List<String>)Lists.newArrayList();
        String s = this.getDisplayName();
        if (this.hasDisplayName()) {
            s = TextFormatting.ITALIC + s;
        }
        s += TextFormatting.RESET;
        if (advanced.isAdvanced()) {
            String s2 = "";
            if (!s.isEmpty()) {
                s += " (";
                s2 = ")";
            }
            final int i = Item.getIdFromItem(this.item);
            if (this.getHasSubtypes()) {
                s += String.format("#%04d/%d%s", i, this.itemDamage, s2);
            }
            else {
                s += String.format("#%04d%s", i, s2);
            }
        }
        else if (!this.hasDisplayName() && this.item == Items.FILLED_MAP) {
            s = s + " #" + this.itemDamage;
        }
        list.add(s);
        int i2 = 0;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99)) {
            i2 = this.stackTagCompound.getInteger("HideFlags");
        }
        if ((i2 & 0x20) == 0x0) {
            this.getItem().addInformation(this, (playerIn == null) ? null : playerIn.world, list, advanced);
        }
        if (this.hasTagCompound()) {
            if ((i2 & 0x1) == 0x0) {
                final NBTTagList nbttaglist = this.getEnchantmentTagList();
                for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                    final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
                    final int k = nbttagcompound.getShort("id");
                    final int l = nbttagcompound.getShort("lvl");
                    final Enchantment enchantment = Enchantment.getEnchantmentByID(k);
                    if (enchantment != null) {
                        list.add(enchantment.getTranslatedName(l));
                    }
                }
            }
            if (this.stackTagCompound.hasKey("display", 10)) {
                final NBTTagCompound nbttagcompound2 = this.stackTagCompound.getCompoundTag("display");
                if (nbttagcompound2.hasKey("color", 3)) {
                    if (advanced.isAdvanced()) {
                        list.add(I18n.translateToLocalFormatted("item.color", String.format("#%06X", nbttagcompound2.getInteger("color"))));
                    }
                    else {
                        list.add(TextFormatting.ITALIC + I18n.translateToLocal("item.dyed"));
                    }
                }
                if (nbttagcompound2.getTagId("Lore") == 9) {
                    final NBTTagList nbttaglist2 = nbttagcompound2.getTagList("Lore", 8);
                    if (!nbttaglist2.isEmpty()) {
                        for (int l2 = 0; l2 < nbttaglist2.tagCount(); ++l2) {
                            list.add(TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + nbttaglist2.getStringTagAt(l2));
                        }
                    }
                }
            }
        }
        for (final EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            final Multimap<String, AttributeModifier> multimap = this.getAttributeModifiers(entityequipmentslot);
            if (!multimap.isEmpty() && (i2 & 0x2) == 0x0) {
                list.add("");
                list.add(I18n.translateToLocal("item.modifiers." + entityequipmentslot.getName()));
                for (final Map.Entry<String, AttributeModifier> entry : multimap.entries()) {
                    final AttributeModifier attributemodifier = entry.getValue();
                    double d0 = attributemodifier.getAmount();
                    boolean flag = false;
                    if (playerIn != null) {
                        if (attributemodifier.getID() == Item.ATTACK_DAMAGE_MODIFIER) {
                            d0 += playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                            d0 += EnchantmentHelper.getModifierForCreature(this, EnumCreatureAttribute.UNDEFINED);
                            flag = true;
                        }
                        else if (attributemodifier.getID() == Item.ATTACK_SPEED_MODIFIER) {
                            d0 += playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
                            flag = true;
                        }
                    }
                    double d2;
                    if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2) {
                        d2 = d0;
                    }
                    else {
                        d2 = d0 * 100.0;
                    }
                    if (flag) {
                        list.add(" " + I18n.translateToLocalFormatted("attribute.modifier.equals." + attributemodifier.getOperation(), ItemStack.DECIMALFORMAT.format(d2), I18n.translateToLocal("attribute.name." + entry.getKey())));
                    }
                    else if (d0 > 0.0) {
                        list.add(TextFormatting.BLUE + " " + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier.getOperation(), ItemStack.DECIMALFORMAT.format(d2), I18n.translateToLocal("attribute.name." + entry.getKey())));
                    }
                    else {
                        if (d0 >= 0.0) {
                            continue;
                        }
                        d2 *= -1.0;
                        list.add(TextFormatting.RED + " " + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier.getOperation(), ItemStack.DECIMALFORMAT.format(d2), I18n.translateToLocal("attribute.name." + entry.getKey())));
                    }
                }
            }
        }
        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (i2 & 0x4) == 0x0) {
            list.add(TextFormatting.BLUE + I18n.translateToLocal("item.unbreakable"));
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (i2 & 0x8) == 0x0) {
            final NBTTagList nbttaglist3 = this.stackTagCompound.getTagList("CanDestroy", 8);
            if (!nbttaglist3.isEmpty()) {
                list.add("");
                list.add(TextFormatting.GRAY + I18n.translateToLocal("item.canBreak"));
                for (int j2 = 0; j2 < nbttaglist3.tagCount(); ++j2) {
                    final Block block = Block.getBlockFromName(nbttaglist3.getStringTagAt(j2));
                    if (block != null) {
                        list.add(TextFormatting.DARK_GRAY + block.getLocalizedName());
                    }
                    else {
                        list.add(TextFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (i2 & 0x10) == 0x0) {
            final NBTTagList nbttaglist4 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            if (!nbttaglist4.isEmpty()) {
                list.add("");
                list.add(TextFormatting.GRAY + I18n.translateToLocal("item.canPlace"));
                for (int k2 = 0; k2 < nbttaglist4.tagCount(); ++k2) {
                    final Block block2 = Block.getBlockFromName(nbttaglist4.getStringTagAt(k2));
                    if (block2 != null) {
                        list.add(TextFormatting.DARK_GRAY + block2.getLocalizedName());
                    }
                    else {
                        list.add(TextFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }
        if (advanced.isAdvanced()) {
            if (this.isItemDamaged()) {
                list.add(I18n.translateToLocalFormatted("item.durability", this.getMaxDamage() - this.getItemDamage(), this.getMaxDamage()));
            }
            list.add(TextFormatting.DARK_GRAY + Item.REGISTRY.getNameForObject(this.item).toString());
            if (this.hasTagCompound()) {
                list.add(TextFormatting.DARK_GRAY + I18n.translateToLocalFormatted("item.nbt_tags", this.getTagCompound().getKeySet().size()));
            }
        }
        return list;
    }
    
    public boolean hasEffect() {
        return this.getItem().hasEffect(this);
    }
    
    public EnumRarity getRarity() {
        return this.getItem().getRarity(this);
    }
    
    public boolean isItemEnchantable() {
        return this.getItem().isEnchantable(this) && !this.isItemEnchanted();
    }
    
    public void addEnchantment(final Enchantment ench, final int level) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }
        final NBTTagList nbttaglist = this.stackTagCompound.getTagList("ench", 10);
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setShort("id", (short)Enchantment.getEnchantmentID(ench));
        nbttagcompound.setShort("lvl", (byte)level);
        nbttaglist.appendTag(nbttagcompound);
    }
    
    public boolean isItemEnchanted() {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9) && !this.stackTagCompound.getTagList("ench", 10).isEmpty();
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
    
    @Nullable
    public EntityItemFrame getItemFrame() {
        return this.isEmpty ? null : this.itemFrame;
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
    
    public Multimap<String, AttributeModifier> getAttributeModifiers(final EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            multimap = (Multimap<String, AttributeModifier>)HashMultimap.create();
            final NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);
                if (attributemodifier != null && (!nbttagcompound.hasKey("Slot", 8) || nbttagcompound.getString("Slot").equals(equipmentSlot.getName())) && attributemodifier.getID().getLeastSignificantBits() != 0L && attributemodifier.getID().getMostSignificantBits() != 0L) {
                    multimap.put((Object)nbttagcompound.getString("AttributeName"), (Object)attributemodifier);
                }
            }
        }
        else {
            multimap = this.getItem().getItemAttributeModifiers(equipmentSlot);
        }
        return multimap;
    }
    
    public void addAttributeModifier(final String attributeName, final AttributeModifier modifier, @Nullable final EntityEquipmentSlot equipmentSlot) {
        if (this.stackTagCompound == null) {
            this.stackTagCompound = new NBTTagCompound();
        }
        if (!this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            this.stackTagCompound.setTag("AttributeModifiers", new NBTTagList());
        }
        final NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
        final NBTTagCompound nbttagcompound = SharedMonsterAttributes.writeAttributeModifierToNBT(modifier);
        nbttagcompound.setString("AttributeName", attributeName);
        if (equipmentSlot != null) {
            nbttagcompound.setString("Slot", equipmentSlot.getName());
        }
        nbttaglist.appendTag(nbttagcompound);
    }
    
    public ITextComponent getTextComponent() {
        final TextComponentString textcomponentstring = new TextComponentString(this.getDisplayName());
        if (this.hasDisplayName()) {
            textcomponentstring.getStyle().setItalic(true);
        }
        final ITextComponent itextcomponent = new TextComponentString("[").appendSibling(textcomponentstring).appendText("]");
        if (!this.isEmpty) {
            final NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
            itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(nbttagcompound.toString())));
            itextcomponent.getStyle().setColor(this.getRarity().color);
        }
        return itextcomponent;
    }
    
    public boolean canDestroy(final Block blockIn) {
        if (blockIn == this.canDestroyCacheBlock) {
            return this.canDestroyCacheResult;
        }
        this.canDestroyCacheBlock = blockIn;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            final NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanDestroy", 8);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
                if (block == blockIn) {
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
            final NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
                if (block == blockIn) {
                    return this.canPlaceOnCacheResult = true;
                }
            }
        }
        return this.canPlaceOnCacheResult = false;
    }
    
    public int getAnimationsToGo() {
        return this.animationsToGo;
    }
    
    public void setAnimationsToGo(final int animations) {
        this.animationsToGo = animations;
    }
    
    public int getCount() {
        return this.isEmpty ? 0 : this.stackSize;
    }
    
    public void setCount(final int size) {
        this.stackSize = size;
        this.updateEmptyState();
    }
    
    public void grow(final int quantity) {
        this.setCount(this.stackSize + quantity);
    }
    
    public void shrink(final int quantity) {
        this.grow(-quantity);
    }
    
    static {
        EMPTY = new ItemStack((Item)null);
        DECIMALFORMAT = new DecimalFormat("#.##");
    }
}
