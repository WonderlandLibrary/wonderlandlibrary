/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import baritone.api.utils.accessor.IItemStack;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.BlockEntityTag;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public final class ItemStack
implements IItemStack {
    public static final ItemStack field_190927_a = new ItemStack((Item)null);
    public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    public int stackSize;
    private int animationsToGo;
    private final Item item;
    private NBTTagCompound stackTagCompound;
    private boolean field_190928_g;
    private int itemDamage;
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock;
    private boolean canDestroyCacheResult;
    private Block canPlaceOnCacheBlock;
    private boolean canPlaceOnCacheResult;
    private int baritoneHash;

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
        this.itemDamage = meta;
        this.stackSize = amount;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
        this.func_190923_F();
    }

    private void func_190923_F() {
        this.field_190928_g = this.isEmpty();
    }

    public ItemStack(NBTTagCompound p_i47263_1_) {
        this.item = Item.getByNameOrId(p_i47263_1_.getString("id"));
        this.stackSize = p_i47263_1_.getByte("Count");
        this.itemDamage = Math.max(0, p_i47263_1_.getShort("Damage"));
        if (p_i47263_1_.hasKey("tag", 10)) {
            this.stackTagCompound = p_i47263_1_.getCompoundTag("tag");
            if (this.item != null) {
                this.item.updateItemStackNBT(p_i47263_1_);
            }
        }
        this.func_190923_F();
    }

    public boolean isEmpty() {
        if (this == field_190927_a) {
            return true;
        }
        if (this.item != null && this.item != Item.getItemFromBlock(Blocks.AIR)) {
            if (this.stackSize <= 0) {
                return true;
            }
            return this.itemDamage < -32768 || this.itemDamage > 65535;
        }
        return true;
    }

    public static void registerFixes(DataFixer fixer) {
        fixer.registerWalker(FixTypes.ITEM_INSTANCE, new BlockEntityTag());
        fixer.registerWalker(FixTypes.ITEM_INSTANCE, new EntityTag());
    }

    public ItemStack splitStack(int amount) {
        int i = Math.min(amount, this.stackSize);
        ItemStack itemstack = this.copy();
        itemstack.func_190920_e(i);
        this.func_190918_g(i);
        return itemstack;
    }

    public Item getItem() {
        return this.field_190928_g ? Item.getItemFromBlock(Blocks.AIR) : this.item;
    }

    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        EnumActionResult enumactionresult = this.getItem().onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);
        if (enumactionresult == EnumActionResult.SUCCESS) {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }
        return enumactionresult;
    }

    public float getDestroySpeed(IBlockState blockIn) {
        return this.getItem().getStrVsBlock(this, blockIn);
    }

    public ActionResult<ItemStack> useItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return this.getItem().onItemRightClick(worldIn, playerIn, hand);
    }

    public ItemStack onItemUseFinish(World worldIn, EntityLivingBase entityLiving) {
        return this.getItem().onItemUseFinish(this, worldIn, entityLiving);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.item);
        nbt.setString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
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
        if (this.field_190928_g) {
            return false;
        }
        if (this.item.getMaxDamage() <= 0) {
            return false;
        }
        return !this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable");
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

    public void setItemDamage(int meta) {
        this.itemDamage = meta;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }

    public int getMaxDamage() {
        return this.getItem().getMaxDamage();
    }

    public boolean attemptDamageItem(int amount, Random rand, @Nullable EntityPlayerMP p_96631_3_) {
        if (!this.isItemStackDamageable()) {
            return false;
        }
        if (amount > 0) {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this);
            int j = 0;
            for (int k = 0; i > 0 && k < amount; ++k) {
                if (!EnchantmentDurability.negateDamage(this, i, rand)) continue;
                ++j;
            }
            if ((amount -= j) <= 0) {
                return false;
            }
        }
        if (p_96631_3_ != null && amount != 0) {
            CriteriaTriggers.field_193132_s.func_193158_a(p_96631_3_, this, this.itemDamage + amount);
        }
        this.itemDamage += amount;
        return this.itemDamage > this.getMaxDamage();
    }

    public void damageItem(int amount, EntityLivingBase entityIn) {
        if ((!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode) && this.isItemStackDamageable() && this.attemptDamageItem(amount, entityIn.getRNG(), entityIn instanceof EntityPlayerMP ? (EntityPlayerMP)entityIn : null)) {
            entityIn.renderBrokenItemStack(this);
            this.func_190918_g(1);
            if (entityIn instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                entityplayer.addStat(StatList.getObjectBreakStats(this.item));
            }
            this.itemDamage = 0;
        }
    }

    public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn) {
        boolean flag = this.item.hitEntity(this, entityIn, playerIn);
        if (flag) {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }
    }

    public void onBlockDestroyed(World worldIn, IBlockState blockIn, BlockPos pos, EntityPlayer playerIn) {
        boolean flag = this.getItem().onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);
        if (flag) {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }
    }

    public boolean canHarvestBlock(IBlockState blockIn) {
        return this.getItem().canHarvestBlock(blockIn);
    }

    public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn, EnumHand hand) {
        return this.getItem().itemInteractionForEntity(this, playerIn, entityIn, hand);
    }

    public ItemStack copy() {
        ItemStack itemstack = new ItemStack(this.item, this.stackSize, this.itemDamage);
        itemstack.func_190915_d(this.func_190921_D());
        if (this.stackTagCompound != null) {
            itemstack.stackTagCompound = this.stackTagCompound.copy();
        }
        return itemstack;
    }

    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
        if (stackA.isEmpty() && stackB.isEmpty()) {
            return true;
        }
        if (!stackA.isEmpty() && !stackB.isEmpty()) {
            if (stackA.stackTagCompound == null && stackB.stackTagCompound != null) {
                return false;
            }
            return stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound);
        }
        return false;
    }

    public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        if (stackA.isEmpty() && stackB.isEmpty()) {
            return true;
        }
        return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemStackEqual(stackB) : false;
    }

    private boolean isItemStackEqual(ItemStack other) {
        if (this.stackSize != other.stackSize) {
            return false;
        }
        if (this.getItem() != other.getItem()) {
            return false;
        }
        if (this.itemDamage != other.itemDamage) {
            return false;
        }
        if (this.stackTagCompound == null && other.stackTagCompound != null) {
            return false;
        }
        return this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound);
    }

    public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB) {
        if (stackA == stackB) {
            return true;
        }
        return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemEqual(stackB) : false;
    }

    public static boolean areItemsEqualIgnoreDurability(ItemStack stackA, ItemStack stackB) {
        if (stackA == stackB) {
            return true;
        }
        return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemEqualIgnoreDurability(stackB) : false;
    }

    public boolean isItemEqual(ItemStack other) {
        return !other.isEmpty() && this.item == other.item && this.itemDamage == other.itemDamage;
    }

    public boolean isItemEqualIgnoreDurability(ItemStack stack) {
        if (!this.isItemStackDamageable()) {
            return this.isItemEqual(stack);
        }
        return !stack.isEmpty() && this.item == stack.item;
    }

    public String getUnlocalizedName() {
        return this.getItem().getUnlocalizedName(this);
    }

    public String toString() {
        return this.stackSize + "x" + this.getItem().getUnlocalizedName() + "@" + this.itemDamage;
    }

    public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }
        if (this.item != null) {
            this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
        }
    }

    public void onCrafting(World worldIn, EntityPlayer playerIn, int amount) {
        playerIn.addStat(StatList.getCraftStats(this.item), amount);
        this.getItem().onCreated(this, worldIn, playerIn);
    }

    public int getMaxItemUseDuration() {
        return this.getItem().getMaxItemUseDuration(this);
    }

    public EnumAction getItemUseAction() {
        return this.getItem().getItemUseAction(this);
    }

    public void onPlayerStoppedUsing(World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        this.getItem().onPlayerStoppedUsing(this, worldIn, entityLiving, timeLeft);
    }

    public boolean hasTagCompound() {
        return !this.field_190928_g && this.stackTagCompound != null;
    }

    @Nullable
    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }

    public NBTTagCompound func_190925_c(String p_190925_1_) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(p_190925_1_, 10)) {
            return this.stackTagCompound.getCompoundTag(p_190925_1_);
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.setTagInfo(p_190925_1_, nbttagcompound);
        return nbttagcompound;
    }

    @Nullable
    public NBTTagCompound getSubCompound(String key) {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10) ? this.stackTagCompound.getCompoundTag(key) : null;
    }

    public void func_190919_e(String p_190919_1_) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(p_190919_1_, 10)) {
            this.stackTagCompound.removeTag(p_190919_1_);
        }
    }

    public NBTTagList getEnchantmentTagList() {
        return this.stackTagCompound != null ? this.stackTagCompound.getTagList("ench", 10) : new NBTTagList();
    }

    public void setTagCompound(@Nullable NBTTagCompound nbt) {
        this.stackTagCompound = nbt;
    }

    public String getDisplayName() {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");
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

    public ItemStack func_190924_f(String p_190924_1_) {
        this.func_190925_c("display").setString("LocName", p_190924_1_);
        return this;
    }

    public ItemStack setStackDisplayName(String displayName) {
        this.func_190925_c("display").setString("Name", displayName);
        return this;
    }

    public void clearCustomName() {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");
        if (nbttagcompound != null) {
            nbttagcompound.removeTag("Name");
            if (nbttagcompound.hasNoTags()) {
                this.func_190919_e("display");
            }
        }
        if (this.stackTagCompound != null && this.stackTagCompound.hasNoTags()) {
            this.stackTagCompound = null;
        }
    }

    public boolean hasDisplayName() {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");
        return nbttagcompound != null && nbttagcompound.hasKey("Name", 8);
    }

    public List<String> getTooltip(@Nullable EntityPlayer playerIn, ITooltipFlag advanced) {
        NBTTagList nbttaglist2;
        NBTTagList nbttaglist1;
        ArrayList<String> list = Lists.newArrayList();
        String s = this.getDisplayName();
        if (this.hasDisplayName()) {
            s = (Object)((Object)TextFormatting.ITALIC) + s;
        }
        s = s + (Object)((Object)TextFormatting.RESET);
        if (advanced.func_194127_a()) {
            String s1 = "";
            if (!s.isEmpty()) {
                s = s + " (";
                s1 = ")";
            }
            int i = Item.getIdFromItem(this.item);
            s = this.getHasSubtypes() ? s + String.format("#%04d/%d%s", i, this.itemDamage, s1) : s + String.format("#%04d%s", i, s1);
        } else if (!this.hasDisplayName() && this.item == Items.FILLED_MAP) {
            s = s + " #" + this.itemDamage;
        }
        list.add(s);
        int i1 = 0;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99)) {
            i1 = this.stackTagCompound.getInteger("HideFlags");
        }
        if ((i1 & 0x20) == 0) {
            this.getItem().addInformation(this, playerIn == null ? null : playerIn.world, list, advanced);
        }
        if (this.hasTagCompound()) {
            if ((i1 & 1) == 0) {
                NBTTagList nbttaglist = this.getEnchantmentTagList();
                for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                    NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
                    short k = nbttagcompound.getShort("id");
                    short l = nbttagcompound.getShort("lvl");
                    Enchantment enchantment = Enchantment.getEnchantmentByID(k);
                    if (enchantment == null) continue;
                    list.add(enchantment.getTranslatedName(l));
                }
            }
            if (this.stackTagCompound.hasKey("display", 10)) {
                NBTTagList nbttaglist3;
                NBTTagCompound nbttagcompound1 = this.stackTagCompound.getCompoundTag("display");
                if (nbttagcompound1.hasKey("color", 3)) {
                    if (advanced.func_194127_a()) {
                        list.add(I18n.translateToLocalFormatted("item.color", String.format("#%06X", nbttagcompound1.getInteger("color"))));
                    } else {
                        list.add((Object)((Object)TextFormatting.ITALIC) + I18n.translateToLocal("item.dyed"));
                    }
                }
                if (nbttagcompound1.getTagId("Lore") == 9 && !(nbttaglist3 = nbttagcompound1.getTagList("Lore", 8)).hasNoTags()) {
                    for (int l1 = 0; l1 < nbttaglist3.tagCount(); ++l1) {
                        list.add((Object)((Object)TextFormatting.DARK_PURPLE) + "" + (Object)((Object)TextFormatting.ITALIC) + nbttaglist3.getStringTagAt(l1));
                    }
                }
            }
        }
        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
            Multimap<String, AttributeModifier> multimap = this.getAttributeModifiers(entityequipmentslot);
            if (multimap.isEmpty() || (i1 & 2) != 0) continue;
            list.add("");
            list.add(I18n.translateToLocal("item.modifiers." + entityequipmentslot.getName()));
            for (Map.Entry<String, AttributeModifier> entry : multimap.entries()) {
                AttributeModifier attributemodifier = entry.getValue();
                double d0 = attributemodifier.getAmount();
                boolean flag = false;
                if (playerIn != null) {
                    if (attributemodifier.getID() == Item.ATTACK_DAMAGE_MODIFIER) {
                        d0 += playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                        d0 += (double)EnchantmentHelper.getModifierForCreature(this, EnumCreatureAttribute.UNDEFINED);
                        flag = true;
                    } else if (attributemodifier.getID() == Item.ATTACK_SPEED_MODIFIER) {
                        d0 += playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
                        flag = true;
                    }
                }
                double d1 = attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2 ? d0 : d0 * 100.0;
                if (flag) {
                    list.add(" " + I18n.translateToLocalFormatted("attribute.modifier.equals." + attributemodifier.getOperation(), DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + entry.getKey())));
                    continue;
                }
                if (d0 > 0.0) {
                    list.add((Object)((Object)TextFormatting.BLUE) + " " + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier.getOperation(), DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + entry.getKey())));
                    continue;
                }
                if (!(d0 < 0.0)) continue;
                list.add((Object)((Object)TextFormatting.RED) + " " + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier.getOperation(), DECIMALFORMAT.format(d1 *= -1.0), I18n.translateToLocal("attribute.name." + entry.getKey())));
            }
        }
        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (i1 & 4) == 0) {
            list.add((Object)((Object)TextFormatting.BLUE) + I18n.translateToLocal("item.unbreakable"));
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (i1 & 8) == 0 && !(nbttaglist1 = this.stackTagCompound.getTagList("CanDestroy", 8)).hasNoTags()) {
            list.add("");
            list.add((Object)((Object)TextFormatting.GRAY) + I18n.translateToLocal("item.canBreak"));
            for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1) {
                Block block = Block.getBlockFromName(nbttaglist1.getStringTagAt(j1));
                if (block != null) {
                    list.add((Object)((Object)TextFormatting.DARK_GRAY) + block.getLocalizedName());
                    continue;
                }
                list.add((Object)((Object)TextFormatting.DARK_GRAY) + "missingno");
            }
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (i1 & 0x10) == 0 && !(nbttaglist2 = this.stackTagCompound.getTagList("CanPlaceOn", 8)).hasNoTags()) {
            list.add("");
            list.add((Object)((Object)TextFormatting.GRAY) + I18n.translateToLocal("item.canPlace"));
            for (int k1 = 0; k1 < nbttaglist2.tagCount(); ++k1) {
                Block block1 = Block.getBlockFromName(nbttaglist2.getStringTagAt(k1));
                if (block1 != null) {
                    list.add((Object)((Object)TextFormatting.DARK_GRAY) + block1.getLocalizedName());
                    continue;
                }
                list.add((Object)((Object)TextFormatting.DARK_GRAY) + "missingno");
            }
        }
        if (advanced.func_194127_a()) {
            if (this.isItemDamaged()) {
                list.add(I18n.translateToLocalFormatted("item.durability", this.getMaxDamage() - this.getItemDamage(), this.getMaxDamage()));
            }
            list.add((Object)((Object)TextFormatting.DARK_GRAY) + Item.REGISTRY.getNameForObject(this.item).toString());
            if (this.hasTagCompound()) {
                list.add((Object)((Object)TextFormatting.DARK_GRAY) + I18n.translateToLocalFormatted("item.nbt_tags", this.getTagCompound().getKeySet().size()));
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
        if (!this.getItem().isItemTool(this)) {
            return false;
        }
        return !this.isItemEnchanted();
    }

    public void addEnchantment(Enchantment ench, int level) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }
        NBTTagList nbttaglist = this.stackTagCompound.getTagList("ench", 10);
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setShort("id", (short)Enchantment.getEnchantmentID(ench));
        nbttagcompound.setShort("lvl", (byte)level);
        nbttaglist.appendTag(nbttagcompound);
    }

    public boolean isItemEnchanted() {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9)) {
            return !this.stackTagCompound.getTagList("ench", 10).hasNoTags();
        }
        return false;
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

    @Nullable
    public EntityItemFrame getItemFrame() {
        return this.field_190928_g ? null : this.itemFrame;
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

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            multimap = HashMultimap.create();
            NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);
                if (attributemodifier == null || nbttagcompound.hasKey("Slot", 8) && !nbttagcompound.getString("Slot").equals(equipmentSlot.getName()) || attributemodifier.getID().getLeastSignificantBits() == 0L || attributemodifier.getID().getMostSignificantBits() == 0L) continue;
                multimap.put(nbttagcompound.getString("AttributeName"), attributemodifier);
            }
        } else {
            multimap = this.getItem().getItemAttributeModifiers(equipmentSlot);
        }
        return multimap;
    }

    public void addAttributeModifier(String attributeName, AttributeModifier modifier, @Nullable EntityEquipmentSlot equipmentSlot) {
        if (this.stackTagCompound == null) {
            this.stackTagCompound = new NBTTagCompound();
        }
        if (!this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            this.stackTagCompound.setTag("AttributeModifiers", new NBTTagList());
        }
        NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
        NBTTagCompound nbttagcompound = SharedMonsterAttributes.writeAttributeModifierToNBT(modifier);
        nbttagcompound.setString("AttributeName", attributeName);
        if (equipmentSlot != null) {
            nbttagcompound.setString("Slot", equipmentSlot.getName());
        }
        nbttaglist.appendTag(nbttagcompound);
    }

    public ITextComponent getTextComponent() {
        TextComponentString textcomponentstring = new TextComponentString(this.getDisplayName());
        if (this.hasDisplayName()) {
            textcomponentstring.getStyle().setItalic(true);
        }
        ITextComponent itextcomponent = new TextComponentString("[").appendSibling(textcomponentstring).appendText("]");
        if (!this.field_190928_g) {
            NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
            itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(nbttagcompound.toString())));
            itextcomponent.getStyle().setColor(this.getRarity().rarityColor);
        }
        return itextcomponent;
    }

    public boolean canDestroy(Block blockIn) {
        if (blockIn == this.canDestroyCacheBlock) {
            return this.canDestroyCacheResult;
        }
        this.canDestroyCacheBlock = blockIn;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanDestroy", 8);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
                if (block != blockIn) continue;
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
            NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
                if (block != blockIn) continue;
                this.canPlaceOnCacheResult = true;
                return true;
            }
        }
        this.canPlaceOnCacheResult = false;
        return false;
    }

    public int func_190921_D() {
        return this.animationsToGo;
    }

    public void func_190915_d(int p_190915_1_) {
        this.animationsToGo = p_190915_1_;
    }

    public int getCount() {
        return this.field_190928_g ? 0 : this.stackSize;
    }

    public void func_190920_e(int p_190920_1_) {
        this.stackSize = p_190920_1_;
        this.func_190923_F();
    }

    public void func_190917_f(int p_190917_1_) {
        this.func_190920_e(this.stackSize + p_190917_1_);
    }

    public void func_190918_g(int p_190918_1_) {
        this.func_190917_f(-p_190918_1_);
    }

    private void recalculateHash() {
        this.baritoneHash = this.item == null ? -1 : this.item.hashCode() + this.itemDamage;
    }

    @Override
    public int getBaritoneHash() {
        return this.baritoneHash;
    }
}

