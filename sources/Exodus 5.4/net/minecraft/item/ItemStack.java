/*
 * Decompiled with CFR 0.152.
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
    private Item item;
    private EntityItemFrame itemFrame;
    public int stackSize;
    public int animationsToGo;
    private NBTTagCompound stackTagCompound;
    private boolean canDestroyCacheResult = false;
    private boolean canPlaceOnCacheResult = false;
    private Block canPlaceOnCacheBlock = null;
    private Block canDestroyCacheBlock = null;
    private int itemDamage;

    public void hitEntity(EntityLivingBase entityLivingBase, EntityPlayer entityPlayer) {
        boolean bl = this.item.hitEntity(this, entityLivingBase, entityPlayer);
        if (bl) {
            entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }

    public String getDisplayName() {
        NBTTagCompound nBTTagCompound;
        String string = this.getItem().getItemStackDisplayName(this);
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10) && (nBTTagCompound = this.stackTagCompound.getCompoundTag("display")).hasKey("Name", 8)) {
            string = nBTTagCompound.getString("Name");
        }
        return string;
    }

    public void onCrafting(World world, EntityPlayer entityPlayer, int n) {
        entityPlayer.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.item)], n);
        this.item.onCreated(this, world, entityPlayer);
    }

    public boolean hasEffect() {
        return this.getItem().hasEffect(this);
    }

    public boolean hasTagCompound() {
        return this.stackTagCompound != null;
    }

    public boolean isOnItemFrame() {
        return this.itemFrame != null;
    }

    public void setTagInfo(String string, NBTBase nBTBase) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        this.stackTagCompound.setTag(string, nBTBase);
    }

    public boolean isItemDamaged() {
        return this.isItemStackDamageable() && this.itemDamage > 0;
    }

    public NBTTagCompound getSubCompound(String string, boolean bl) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(string, 10)) {
            return this.stackTagCompound.getCompoundTag(string);
        }
        if (bl) {
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            this.setTagInfo(string, nBTTagCompound);
            return nBTTagCompound;
        }
        return null;
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }

    public void updateAnimation(World world, Entity entity, int n, boolean bl) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }
        this.item.onUpdate(this, world, entity, n, bl);
    }

    public void setTagCompound(NBTTagCompound nBTTagCompound) {
        this.stackTagCompound = nBTTagCompound;
    }

    public static boolean areItemStackTagsEqual(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack == null && itemStack2 == null ? true : (itemStack != null && itemStack2 != null ? (itemStack.stackTagCompound == null && itemStack2.stackTagCompound != null ? false : itemStack.stackTagCompound == null || itemStack.stackTagCompound.equals(itemStack2.stackTagCompound)) : false);
    }

    public EntityItemFrame getItemFrame() {
        return this.itemFrame;
    }

    public ItemStack splitStack(int n) {
        ItemStack itemStack = new ItemStack(this.item, n, this.itemDamage);
        if (this.stackTagCompound != null) {
            itemStack.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }
        this.stackSize -= n;
        return itemStack;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        HashMultimap hashMultimap;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            hashMultimap = HashMultimap.create();
            NBTTagList nBTTagList = this.stackTagCompound.getTagList("AttributeModifiers", 10);
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                NBTTagCompound nBTTagCompound = nBTTagList.getCompoundTagAt(n);
                AttributeModifier attributeModifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nBTTagCompound);
                if (attributeModifier != null && attributeModifier.getID().getLeastSignificantBits() != 0L && attributeModifier.getID().getMostSignificantBits() != 0L) {
                    hashMultimap.put((Object)nBTTagCompound.getString("AttributeName"), (Object)attributeModifier);
                }
                ++n;
            }
        } else {
            hashMultimap = this.getItem().getItemAttributeModifiers();
        }
        return hashMultimap;
    }

    public ItemStack(Block block, int n) {
        this(block, n, 0);
    }

    public boolean isItemStackDamageable() {
        return this.item == null ? false : (this.item.getMaxDamage() <= 0 ? false : !this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable"));
    }

    public static boolean areItemStacksEqual(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack == null && itemStack2 == null ? true : (itemStack != null && itemStack2 != null ? itemStack.isItemStackEqual(itemStack2) : false);
    }

    private ItemStack() {
    }

    public static ItemStack copyItemStack(ItemStack itemStack) {
        return itemStack == null ? null : itemStack.copy();
    }

    public ItemStack(Block block, int n, int n2) {
        this(Item.getItemFromBlock(block), n, n2);
    }

    public boolean getIsItemStackEqual(ItemStack itemStack) {
        return this.isItemStackEqual(itemStack);
    }

    public EnumAction getItemUseAction() {
        return this.getItem().getItemUseAction(this);
    }

    public ItemStack(Block block) {
        this(block, 1);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
        ResourceLocation resourceLocation = Item.itemRegistry.getNameForObject(this.item);
        nBTTagCompound.setString("id", resourceLocation == null ? "minecraft:air" : resourceLocation.toString());
        nBTTagCompound.setByte("Count", (byte)this.stackSize);
        nBTTagCompound.setShort("Damage", (short)this.itemDamage);
        if (this.stackTagCompound != null) {
            nBTTagCompound.setTag("tag", this.stackTagCompound);
        }
        return nBTTagCompound;
    }

    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName(this);
    }

    public void setItemFrame(EntityItemFrame entityItemFrame) {
        this.itemFrame = entityItemFrame;
    }

    public ItemStack onItemUseFinish(World world, EntityPlayer entityPlayer) {
        return this.getItem().onItemUseFinish(this, world, entityPlayer);
    }

    public boolean isItemEnchantable() {
        return !this.getItem().isItemTool(this) ? false : !this.isItemEnchanted();
    }

    public boolean canEditBlocks() {
        return this.getItem().canItemEditBlocks();
    }

    public ItemStack useItemRightClick(World world, EntityPlayer entityPlayer) {
        return this.getItem().onItemRightClick(this, world, entityPlayer);
    }

    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        this.item = nBTTagCompound.hasKey("id", 8) ? Item.getByNameOrId(nBTTagCompound.getString("id")) : Item.getItemById(nBTTagCompound.getShort("id"));
        this.stackSize = nBTTagCompound.getByte("Count");
        this.itemDamage = nBTTagCompound.getShort("Damage");
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
        if (nBTTagCompound.hasKey("tag", 10)) {
            this.stackTagCompound = nBTTagCompound.getCompoundTag("tag");
            if (this.item != null) {
                this.item.updateItemStackNBT(this.stackTagCompound);
            }
        }
    }

    public EnumRarity getRarity() {
        return this.getItem().getRarity(this);
    }

    public static ItemStack loadItemStackFromNBT(NBTTagCompound nBTTagCompound) {
        ItemStack itemStack = new ItemStack();
        itemStack.readFromNBT(nBTTagCompound);
        return itemStack.getItem() != null ? itemStack : null;
    }

    public void setRepairCost(int n) {
        if (!this.hasTagCompound()) {
            this.stackTagCompound = new NBTTagCompound();
        }
        this.stackTagCompound.setInteger("RepairCost", n);
    }

    public int getMetadata() {
        return this.itemDamage;
    }

    public int getItemDamage() {
        return this.itemDamage;
    }

    public void setItemDamage(int n) {
        this.itemDamage = n;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }

    public void onBlockDestroyed(World world, Block block, BlockPos blockPos, EntityPlayer entityPlayer) {
        boolean bl = this.item.onBlockDestroyed(this, world, block, blockPos, entityPlayer);
        if (bl) {
            entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }

    public boolean getHasSubtypes() {
        return this.item.getHasSubtypes();
    }

    public ItemStack setStackDisplayName(String string) {
        if (this.stackTagCompound == null) {
            this.stackTagCompound = new NBTTagCompound();
        }
        if (!this.stackTagCompound.hasKey("display", 10)) {
            this.stackTagCompound.setTag("display", new NBTTagCompound());
        }
        this.stackTagCompound.getCompoundTag("display").setString("Name", string);
        return this;
    }

    public boolean onItemUse(EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3) {
        boolean bl = this.getItem().onItemUse(this, entityPlayer, world, blockPos, enumFacing, f, f2, f3);
        if (bl) {
            entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
        return bl;
    }

    public boolean hasDisplayName() {
        return this.stackTagCompound == null ? false : (!this.stackTagCompound.hasKey("display", 10) ? false : this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8));
    }

    public int getMaxStackSize() {
        return this.getItem().getItemStackLimit();
    }

    public boolean canHarvestBlock(Block block) {
        return this.item.canHarvestBlock(block);
    }

    public int getRepairCost() {
        return this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3) ? this.stackTagCompound.getInteger("RepairCost") : 0;
    }

    public List<String> getTooltip(EntityPlayer entityPlayer, boolean bl) {
        NBTTagList nBTTagList;
        NBTTagList nBTTagList2;
        Multimap<String, AttributeModifier> multimap;
        ArrayList arrayList = Lists.newArrayList();
        String string = this.getDisplayName();
        if (this.hasDisplayName()) {
            string = (Object)((Object)EnumChatFormatting.ITALIC) + string;
        }
        string = String.valueOf(string) + (Object)((Object)EnumChatFormatting.RESET);
        if (bl) {
            String string2 = "";
            if (string.length() > 0) {
                string = String.valueOf(string) + " (";
                string2 = ")";
            }
            int n = Item.getIdFromItem(this.item);
            string = this.getHasSubtypes() ? String.valueOf(string) + String.format("#%04d/%d%s", n, this.itemDamage, string2) : String.valueOf(string) + String.format("#%04d%s", n, string2);
        } else if (!this.hasDisplayName() && this.item == Items.filled_map) {
            string = String.valueOf(string) + " #" + this.itemDamage;
        }
        arrayList.add(string);
        int n = 0;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99)) {
            n = this.stackTagCompound.getInteger("HideFlags");
        }
        if ((n & 0x20) == 0) {
            this.item.addInformation(this, entityPlayer, arrayList, bl);
        }
        if (this.hasTagCompound()) {
            int n2;
            NBTTagList nBTTagList3;
            if ((n & 1) == 0 && (nBTTagList3 = this.getEnchantmentTagList()) != null) {
                int n3 = 0;
                while (n3 < nBTTagList3.tagCount()) {
                    n2 = nBTTagList3.getCompoundTagAt(n3).getShort("id");
                    short s = nBTTagList3.getCompoundTagAt(n3).getShort("lvl");
                    if (Enchantment.getEnchantmentById(n2) != null) {
                        arrayList.add(Enchantment.getEnchantmentById(n2).getTranslatedName(s));
                    }
                    ++n3;
                }
            }
            if (this.stackTagCompound.hasKey("display", 10)) {
                NBTTagList nBTTagList4;
                NBTTagCompound nBTTagCompound = this.stackTagCompound.getCompoundTag("display");
                if (nBTTagCompound.hasKey("color", 3)) {
                    if (bl) {
                        arrayList.add("Color: #" + Integer.toHexString(nBTTagCompound.getInteger("color")).toUpperCase());
                    } else {
                        arrayList.add((Object)((Object)EnumChatFormatting.ITALIC) + StatCollector.translateToLocal("item.dyed"));
                    }
                }
                if (nBTTagCompound.getTagId("Lore") == 9 && (nBTTagList4 = nBTTagCompound.getTagList("Lore", 8)).tagCount() > 0) {
                    n2 = 0;
                    while (n2 < nBTTagList4.tagCount()) {
                        arrayList.add((Object)((Object)EnumChatFormatting.DARK_PURPLE) + (Object)((Object)EnumChatFormatting.ITALIC) + nBTTagList4.getStringTagAt(n2));
                        ++n2;
                    }
                }
            }
        }
        if (!(multimap = this.getAttributeModifiers()).isEmpty() && (n & 2) == 0) {
            arrayList.add("");
            for (Map.Entry entry : multimap.entries()) {
                AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
                double d = attributeModifier.getAmount();
                if (attributeModifier.getID() == Item.itemModifierUUID) {
                    d += (double)EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
                }
                double d2 = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? d : d * 100.0;
                if (d > 0.0) {
                    arrayList.add((Object)((Object)EnumChatFormatting.BLUE) + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributeModifier.getOperation(), DECIMALFORMAT.format(d2), StatCollector.translateToLocal("attribute.name." + (String)entry.getKey())));
                    continue;
                }
                if (!(d < 0.0)) continue;
                arrayList.add((Object)((Object)EnumChatFormatting.RED) + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributeModifier.getOperation(), DECIMALFORMAT.format(d2 *= -1.0), StatCollector.translateToLocal("attribute.name." + (String)entry.getKey())));
            }
        }
        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (n & 4) == 0) {
            arrayList.add((Object)((Object)EnumChatFormatting.BLUE) + StatCollector.translateToLocal("item.unbreakable"));
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (n & 8) == 0 && (nBTTagList2 = this.stackTagCompound.getTagList("CanDestroy", 8)).tagCount() > 0) {
            arrayList.add("");
            arrayList.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocal("item.canBreak"));
            int n4 = 0;
            while (n4 < nBTTagList2.tagCount()) {
                Block block = Block.getBlockFromName(nBTTagList2.getStringTagAt(n4));
                if (block != null) {
                    arrayList.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + block.getLocalizedName());
                } else {
                    arrayList.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + "missingno");
                }
                ++n4;
            }
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (n & 0x10) == 0 && (nBTTagList = this.stackTagCompound.getTagList("CanPlaceOn", 8)).tagCount() > 0) {
            arrayList.add("");
            arrayList.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocal("item.canPlace"));
            int n5 = 0;
            while (n5 < nBTTagList.tagCount()) {
                Block block = Block.getBlockFromName(nBTTagList.getStringTagAt(n5));
                if (block != null) {
                    arrayList.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + block.getLocalizedName());
                } else {
                    arrayList.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + "missingno");
                }
                ++n5;
            }
        }
        if (bl) {
            if (this.isItemDamaged()) {
                arrayList.add("Durability: " + (this.getMaxDamage() - this.getItemDamage()) + " / " + this.getMaxDamage());
            }
            arrayList.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + Item.itemRegistry.getNameForObject(this.item).toString());
            if (this.hasTagCompound()) {
                arrayList.add((Object)((Object)EnumChatFormatting.DARK_GRAY) + "NBT: " + this.getTagCompound().getKeySet().size() + " tag(s)");
            }
        }
        return arrayList;
    }

    public ItemStack(Item item, int n, int n2) {
        this.item = item;
        this.stackSize = n;
        this.itemDamage = n2;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }

    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }

    public ItemStack(Item item) {
        this(item, 1);
    }

    public void onPlayerStoppedUsing(World world, EntityPlayer entityPlayer, int n) {
        this.getItem().onPlayerStoppedUsing(this, world, entityPlayer, n);
    }

    public boolean attemptDamageItem(int n, Random random) {
        if (!this.isItemStackDamageable()) {
            return false;
        }
        if (n > 0) {
            int n2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
            int n3 = 0;
            int n4 = 0;
            while (n2 > 0 && n4 < n) {
                if (EnchantmentDurability.negateDamage(this, n2, random)) {
                    ++n3;
                }
                ++n4;
            }
            if ((n -= n3) <= 0) {
                return false;
            }
        }
        this.itemDamage += n;
        return this.itemDamage > this.getMaxDamage();
    }

    public int getMaxItemUseDuration() {
        return this.getItem().getMaxItemUseDuration(this);
    }

    public boolean canDestroy(Block block) {
        if (block == this.canDestroyCacheBlock) {
            return this.canDestroyCacheResult;
        }
        this.canDestroyCacheBlock = block;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            NBTTagList nBTTagList = this.stackTagCompound.getTagList("CanDestroy", 8);
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                Block block2 = Block.getBlockFromName(nBTTagList.getStringTagAt(n));
                if (block2 == block) {
                    this.canDestroyCacheResult = true;
                    return true;
                }
                ++n;
            }
        }
        this.canDestroyCacheResult = false;
        return false;
    }

    public void clearCustomName() {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
            NBTTagCompound nBTTagCompound = this.stackTagCompound.getCompoundTag("display");
            nBTTagCompound.removeTag("Name");
            if (nBTTagCompound.hasNoTags()) {
                this.stackTagCompound.removeTag("display");
                if (this.stackTagCompound.hasNoTags()) {
                    this.setTagCompound(null);
                }
            }
        }
    }

    public boolean isItemEnchanted() {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9);
    }

    public boolean interactWithEntity(EntityPlayer entityPlayer, EntityLivingBase entityLivingBase) {
        return this.item.itemInteractionForEntity(this, entityPlayer, entityLivingBase);
    }

    public IChatComponent getChatComponent() {
        ChatComponentText chatComponentText = new ChatComponentText(this.getDisplayName());
        if (this.hasDisplayName()) {
            chatComponentText.getChatStyle().setItalic(true);
        }
        IChatComponent iChatComponent = new ChatComponentText("[").appendSibling(chatComponentText).appendText("]");
        if (this.item != null) {
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            this.writeToNBT(nBTTagCompound);
            iChatComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(nBTTagCompound.toString())));
            iChatComponent.getChatStyle().setColor(this.getRarity().rarityColor);
        }
        return iChatComponent;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public static boolean areItemsEqual(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack == null && itemStack2 == null ? true : (itemStack != null && itemStack2 != null ? itemStack.isItemEqual(itemStack2) : false);
    }

    public void damageItem(int n, EntityLivingBase entityLivingBase) {
        if ((!(entityLivingBase instanceof EntityPlayer) || !((EntityPlayer)entityLivingBase).capabilities.isCreativeMode) && this.isItemStackDamageable() && this.attemptDamageItem(n, entityLivingBase.getRNG())) {
            entityLivingBase.renderBrokenItemStack(this);
            --this.stackSize;
            if (entityLivingBase instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer)entityLivingBase;
                entityPlayer.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(this.item)]);
                if (this.stackSize == 0 && this.getItem() instanceof ItemBow) {
                    entityPlayer.destroyCurrentEquippedItem();
                }
            }
            if (this.stackSize < 0) {
                this.stackSize = 0;
            }
            this.itemDamage = 0;
        }
    }

    public void addEnchantment(Enchantment enchantment, int n) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }
        NBTTagList nBTTagList = this.stackTagCompound.getTagList("ench", 10);
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        nBTTagCompound.setShort("id", (short)enchantment.effectId);
        nBTTagCompound.setShort("lvl", (byte)n);
        nBTTagList.appendTag(nBTTagCompound);
    }

    public boolean canPlaceOn(Block block) {
        if (block == this.canPlaceOnCacheBlock) {
            return this.canPlaceOnCacheResult;
        }
        this.canPlaceOnCacheBlock = block;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
            NBTTagList nBTTagList = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                Block block2 = Block.getBlockFromName(nBTTagList.getStringTagAt(n));
                if (block2 == block) {
                    this.canPlaceOnCacheResult = true;
                    return true;
                }
                ++n;
            }
        }
        this.canPlaceOnCacheResult = false;
        return false;
    }

    public int getMaxDamage() {
        return this.item.getMaxDamage();
    }

    private boolean isItemStackEqual(ItemStack itemStack) {
        return this.stackSize != itemStack.stackSize ? false : (this.item != itemStack.item ? false : (this.itemDamage != itemStack.itemDamage ? false : (this.stackTagCompound == null && itemStack.stackTagCompound != null ? false : this.stackTagCompound == null || this.stackTagCompound.equals(itemStack.stackTagCompound))));
    }

    public ItemStack(Item item, int n) {
        this(item, n, 0);
    }

    public NBTTagList getEnchantmentTagList() {
        return this.stackTagCompound == null ? null : this.stackTagCompound.getTagList("ench", 10);
    }

    public String toString() {
        return String.valueOf(this.stackSize) + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
    }

    public boolean isItemEqual(ItemStack itemStack) {
        return itemStack != null && this.item == itemStack.item && this.itemDamage == itemStack.itemDamage;
    }

    public Item getItem() {
        return this.item;
    }

    public float getStrVsBlock(Block block) {
        return this.getItem().getStrVsBlock(this, block);
    }

    public ItemStack copy() {
        ItemStack itemStack = new ItemStack(this.item, this.stackSize, this.itemDamage);
        if (this.stackTagCompound != null) {
            itemStack.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }
        return itemStack;
    }
}

