package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class Item
{
    public static final RegistryNamespaced<ResourceLocation, Item> REGISTRY = new RegistryNamespaced<ResourceLocation, Item>();
    private static final Map<Block, Item> BLOCK_TO_ITEM = Maps.<Block, Item>newHashMap();
    private static final IItemPropertyGetter DAMAGED_GETTER = new IItemPropertyGetter()
    {
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
        {
            return stack.isItemDamaged() ? 1.0F : 0.0F;
        }
    };
    private static final IItemPropertyGetter DAMAGE_GETTER = new IItemPropertyGetter()
    {
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
        {
            return MathHelper.clamp((float)stack.getItemDamage() / (float)stack.getMaxDamage(), 0.0F, 1.0F);
        }
    };
    private static final IItemPropertyGetter LEFTHANDED_GETTER = new IItemPropertyGetter()
    {
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
        {
            return entityIn != null && entityIn.getPrimaryHand() != EnumHandSide.RIGHT ? 1.0F : 0.0F;
        }
    };
    private static final IItemPropertyGetter COOLDOWN_GETTER = new IItemPropertyGetter()
    {
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
        {
            return entityIn instanceof EntityPlayer ? ((EntityPlayer)entityIn).getCooldownTracker().getCooldown(stack.getItem(), 0.0F) : 0.0F;
        }
    };
    private final IRegistry<ResourceLocation, IItemPropertyGetter> properties = new RegistrySimple<ResourceLocation, IItemPropertyGetter>();
    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    private CreativeTabs tabToDisplayOn;

    /** The RNG used by the Item subclasses. */
    protected static Random itemRand = new Random();

    /** Maximum size of the stack. */
    protected int maxStackSize = 64;

    /** Maximum damage an item can handle. */
    private int maxDamage;

    /** If true, render the object in full 3D, like weapons and tools. */
    protected boolean bFull3D;

    /**
     * Some items (like dyes) have multiple subtypes on same item, this is field define this behavior
     */
    protected boolean hasSubtypes;
    private Item containerItem;

    /** The unlocalized name of this item. */
    private String translationKey;

    public static int getIdFromItem(Item itemIn)
    {
        return itemIn == null ? 0 : REGISTRY.getIDForObject(itemIn);
    }

    public static Item getItemById(int id)
    {
        return REGISTRY.getObjectById(id);
    }

    public static Item getItemFromBlock(Block blockIn)
    {
        Item item = BLOCK_TO_ITEM.get(blockIn);
        return item == null ? Items.AIR : item;
    }

    @Nullable

    /**
     * Tries to get an Item by it's name (e.g. minecraft:apple) or a String representation of a numerical ID. If both
     * fail, null is returned.
     */
    public static Item getByNameOrId(String id)
    {
        Item item = REGISTRY.getObject(new ResourceLocation(id));

        if (item == null)
        {
            try
            {
                return getItemById(Integer.parseInt(id));
            }
            catch (NumberFormatException var3)
            {
                ;
            }
        }

        return item;
    }

    /**
     * Creates a new override param for item models. See usage in clock, compass, elytra, etc.
     */
    public final void addPropertyOverride(ResourceLocation key, IItemPropertyGetter getter)
    {
        this.properties.putObject(key, getter);
    }

    @Nullable
    public IItemPropertyGetter getPropertyGetter(ResourceLocation key)
    {
        return this.properties.getObject(key);
    }

    public boolean hasCustomProperties()
    {
        return !this.properties.getKeys().isEmpty();
    }

    /**
     * Called when an ItemStack with NBT data is read to potentially that ItemStack's NBT data
     */
    public boolean updateItemStackNBT(NBTTagCompound nbt)
    {
        return false;
    }

    public Item()
    {
        this.addPropertyOverride(new ResourceLocation("lefthanded"), LEFTHANDED_GETTER);
        this.addPropertyOverride(new ResourceLocation("cooldown"), COOLDOWN_GETTER);
    }

    public Item setMaxStackSize(int maxStackSize)
    {
        this.maxStackSize = maxStackSize;
        return this;
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return EnumActionResult.PASS;
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        return 1.0F;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        return stack;
    }

    /**
     * Returns the maximum size of the stack for a specific item.
     */
    public int getItemStackLimit()
    {
        return this.maxStackSize;
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    {
        return 0;
    }

    public boolean getHasSubtypes()
    {
        return this.hasSubtypes;
    }

    protected Item setHasSubtypes(boolean hasSubtypes)
    {
        this.hasSubtypes = hasSubtypes;
        return this;
    }

    /**
     * Returns the maximum damage an item can take.
     */
    public int getMaxDamage()
    {
        return this.maxDamage;
    }

    /**
     * set max damage of an Item
     */
    protected Item setMaxDamage(int maxDamageIn)
    {
        this.maxDamage = maxDamageIn;

        if (maxDamageIn > 0)
        {
            this.addPropertyOverride(new ResourceLocation("damaged"), DAMAGED_GETTER);
            this.addPropertyOverride(new ResourceLocation("damage"), DAMAGE_GETTER);
        }

        return this;
    }

    public boolean isDamageable()
    {
        return this.maxDamage > 0 && (!this.hasSubtypes || this.maxStackSize == 1);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        return false;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        return false;
    }

    /**
     * Check whether this Item can harvest the given Block
     */
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        return false;
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        return false;
    }

    /**
     * Sets bFull3D to True and return the object.
     */
    public Item setFull3D()
    {
        this.bFull3D = true;
        return this;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return this.bFull3D;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    public boolean shouldRotateAroundWhenRendering()
    {
        return false;
    }

    /**
     * Sets the unlocalized name of this item to the string passed as the parameter, prefixed by "item."
     */
    public Item setTranslationKey(String key)
    {
        this.translationKey = key;
        return this;
    }

    /**
     * Translates the unlocalized name of this item, but without the .name suffix, so the translation fails and the
     * unlocalized name itself is returned.
     */
    public String getUnlocalizedNameInefficiently(ItemStack stack)
    {
        return I18n.translateToLocal(this.getTranslationKey(stack));
    }

    /**
     * Returns the unlocalized name of this item.
     */
    public String getTranslationKey()
    {
        return "item." + this.translationKey;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getTranslationKey(ItemStack stack)
    {
        return "item." + this.translationKey;
    }

    public Item setContainerItem(Item containerItem)
    {
        this.containerItem = containerItem;
        return this;
    }

    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    public boolean getShareTag()
    {
        return true;
    }

    @Nullable
    public Item getContainerItem()
    {
        return this.containerItem;
    }

    /**
     * True if this Item has a container item (a.k.a. crafting result)
     */
    public boolean hasContainerItem()
    {
        return this.containerItem != null;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
    }

    /**
     * false for all Items except sub-classes of ItemMapBase
     */
    public boolean isMap()
    {
        return false;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 0;
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }

    /**
     * Returns true if this item has an enchantment glint. By default, this returns
     * <code>stack.isItemEnchanted()</code>, but other items can override it (for instance, written books always return
     * true).
     *  
     * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
     * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
     */
    public boolean hasEffect(ItemStack stack)
    {
        return stack.isItemEnchanted();
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return stack.isItemEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean isEnchantable(ItemStack stack)
    {
        return this.getItemStackLimit() == 1 && this.isDamageable();
    }

    protected RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids)
    {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;
        Vec3d vec3d1 = vec3d.add((double)f6 * 5.0D, (double)f5 * 5.0D, (double)f7 * 5.0D);
        return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 0;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            items.add(new ItemStack(this));
        }
    }

    protected boolean isInCreativeTab(CreativeTabs targetTab)
    {
        CreativeTabs creativetabs = this.getCreativeTab();
        return creativetabs != null && (targetTab == CreativeTabs.SEARCH || targetTab == creativetabs);
    }

    @Nullable

    /**
     * gets the CreativeTab this item is displayed on
     */
    public CreativeTabs getCreativeTab()
    {
        return this.tabToDisplayOn;
    }

    /**
     * returns this;
     */
    public Item setCreativeTab(CreativeTabs tab)
    {
        this.tabToDisplayOn = tab;
        return this;
    }

    /**
     * Returns whether this item is always allowed to edit the world. Forces {@link
     * net.minecraft.entity.player.EntityPlayer#canPlayerEdit EntityPlayer#canPlayerEdit} to return {@code true}.

     * @return whether this item ignores other restrictions on how a player can modify the world.
     * @see ItemStack#canEditBlocks
     */
    public boolean canItemEditBlocks()
    {
        return false;
    }

    /**
     * Return whether this item is repairable in an anvil.
     *  
     * @param toRepair the {@code ItemStack} being repaired
     * @param repair the {@code ItemStack} being used to perform the repair
     */
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        return HashMultimap.<String, AttributeModifier>create();
    }

    public static void registerItems()
    {
        registerItemBlock(Blocks.AIR, new ItemAir(Blocks.AIR));
        registerItemBlock(Blocks.STONE, (new ItemMultiTexture(Blocks.STONE, Blocks.STONE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("stone"));
        registerItemBlock(Blocks.GRASS, new ItemColored(Blocks.GRASS, false));
        registerItemBlock(Blocks.DIRT, (new ItemMultiTexture(Blocks.DIRT, Blocks.DIRT, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockDirt.DirtType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("dirt"));
        registerItemBlock(Blocks.COBBLESTONE);
        registerItemBlock(Blocks.PLANKS, (new ItemMultiTexture(Blocks.PLANKS, Blocks.PLANKS, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("wood"));
        registerItemBlock(Blocks.SAPLING, (new ItemMultiTexture(Blocks.SAPLING, Blocks.SAPLING, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("sapling"));
        registerItemBlock(Blocks.BEDROCK);
        registerItemBlock(Blocks.SAND, (new ItemMultiTexture(Blocks.SAND, Blocks.SAND, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockSand.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("sand"));
        registerItemBlock(Blocks.GRAVEL);
        registerItemBlock(Blocks.GOLD_ORE);
        registerItemBlock(Blocks.IRON_ORE);
        registerItemBlock(Blocks.COAL_ORE);
        registerItemBlock(Blocks.LOG, (new ItemMultiTexture(Blocks.LOG, Blocks.LOG, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("log"));
        registerItemBlock(Blocks.LOG2, (new ItemMultiTexture(Blocks.LOG2, Blocks.LOG2, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata() + 4).getTranslationKey();
            }
        })).setTranslationKey("log"));
        registerItemBlock(Blocks.LEAVES, (new ItemLeaves(Blocks.LEAVES)).setTranslationKey("leaves"));
        registerItemBlock(Blocks.LEAVES2, (new ItemLeaves(Blocks.LEAVES2)).setTranslationKey("leaves"));
        registerItemBlock(Blocks.SPONGE, (new ItemMultiTexture(Blocks.SPONGE, Blocks.SPONGE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return (p_apply_1_.getMetadata() & 1) == 1 ? "wet" : "dry";
            }
        })).setTranslationKey("sponge"));
        registerItemBlock(Blocks.GLASS);
        registerItemBlock(Blocks.LAPIS_ORE);
        registerItemBlock(Blocks.LAPIS_BLOCK);
        registerItemBlock(Blocks.DISPENSER);
        registerItemBlock(Blocks.SANDSTONE, (new ItemMultiTexture(Blocks.SANDSTONE, Blocks.SANDSTONE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockSandStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("sandStone"));
        registerItemBlock(Blocks.NOTEBLOCK);
        registerItemBlock(Blocks.GOLDEN_RAIL);
        registerItemBlock(Blocks.DETECTOR_RAIL);
        registerItemBlock(Blocks.STICKY_PISTON, new ItemPiston(Blocks.STICKY_PISTON));
        registerItemBlock(Blocks.WEB);
        registerItemBlock(Blocks.TALLGRASS, (new ItemColored(Blocks.TALLGRASS, true)).setSubtypeNames(new String[] {"shrub", "grass", "fern"}));
        registerItemBlock(Blocks.DEADBUSH);
        registerItemBlock(Blocks.PISTON, new ItemPiston(Blocks.PISTON));
        registerItemBlock(Blocks.WOOL, (new ItemCloth(Blocks.WOOL)).setTranslationKey("cloth"));
        registerItemBlock(Blocks.YELLOW_FLOWER, (new ItemMultiTexture(Blocks.YELLOW_FLOWER, Blocks.YELLOW_FLOWER, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.YELLOW, p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("flower"));
        registerItemBlock(Blocks.RED_FLOWER, (new ItemMultiTexture(Blocks.RED_FLOWER, Blocks.RED_FLOWER, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("rose"));
        registerItemBlock(Blocks.BROWN_MUSHROOM);
        registerItemBlock(Blocks.RED_MUSHROOM);
        registerItemBlock(Blocks.GOLD_BLOCK);
        registerItemBlock(Blocks.IRON_BLOCK);
        registerItemBlock(Blocks.STONE_SLAB, (new ItemSlab(Blocks.STONE_SLAB, Blocks.STONE_SLAB, Blocks.DOUBLE_STONE_SLAB)).setTranslationKey("stoneSlab"));
        registerItemBlock(Blocks.BRICK_BLOCK);
        registerItemBlock(Blocks.TNT);
        registerItemBlock(Blocks.BOOKSHELF);
        registerItemBlock(Blocks.MOSSY_COBBLESTONE);
        registerItemBlock(Blocks.OBSIDIAN);
        registerItemBlock(Blocks.TORCH);
        registerItemBlock(Blocks.END_ROD);
        registerItemBlock(Blocks.CHORUS_PLANT);
        registerItemBlock(Blocks.CHORUS_FLOWER);
        registerItemBlock(Blocks.PURPUR_BLOCK);
        registerItemBlock(Blocks.PURPUR_PILLAR);
        registerItemBlock(Blocks.PURPUR_STAIRS);
        registerItemBlock(Blocks.PURPUR_SLAB, (new ItemSlab(Blocks.PURPUR_SLAB, Blocks.PURPUR_SLAB, Blocks.PURPUR_DOUBLE_SLAB)).setTranslationKey("purpurSlab"));
        registerItemBlock(Blocks.MOB_SPAWNER);
        registerItemBlock(Blocks.OAK_STAIRS);
        registerItemBlock(Blocks.CHEST);
        registerItemBlock(Blocks.DIAMOND_ORE);
        registerItemBlock(Blocks.DIAMOND_BLOCK);
        registerItemBlock(Blocks.CRAFTING_TABLE);
        registerItemBlock(Blocks.FARMLAND);
        registerItemBlock(Blocks.FURNACE);
        registerItemBlock(Blocks.LADDER);
        registerItemBlock(Blocks.RAIL);
        registerItemBlock(Blocks.STONE_STAIRS);
        registerItemBlock(Blocks.LEVER);
        registerItemBlock(Blocks.STONE_PRESSURE_PLATE);
        registerItemBlock(Blocks.WOODEN_PRESSURE_PLATE);
        registerItemBlock(Blocks.REDSTONE_ORE);
        registerItemBlock(Blocks.REDSTONE_TORCH);
        registerItemBlock(Blocks.STONE_BUTTON);
        registerItemBlock(Blocks.SNOW_LAYER, new ItemSnow(Blocks.SNOW_LAYER));
        registerItemBlock(Blocks.ICE);
        registerItemBlock(Blocks.SNOW);
        registerItemBlock(Blocks.CACTUS);
        registerItemBlock(Blocks.CLAY);
        registerItemBlock(Blocks.JUKEBOX);
        registerItemBlock(Blocks.OAK_FENCE);
        registerItemBlock(Blocks.SPRUCE_FENCE);
        registerItemBlock(Blocks.BIRCH_FENCE);
        registerItemBlock(Blocks.JUNGLE_FENCE);
        registerItemBlock(Blocks.DARK_OAK_FENCE);
        registerItemBlock(Blocks.ACACIA_FENCE);
        registerItemBlock(Blocks.PUMPKIN);
        registerItemBlock(Blocks.NETHERRACK);
        registerItemBlock(Blocks.SOUL_SAND);
        registerItemBlock(Blocks.GLOWSTONE);
        registerItemBlock(Blocks.LIT_PUMPKIN);
        registerItemBlock(Blocks.TRAPDOOR);
        registerItemBlock(Blocks.MONSTER_EGG, (new ItemMultiTexture(Blocks.MONSTER_EGG, Blocks.MONSTER_EGG, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockSilverfish.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("monsterStoneEgg"));
        registerItemBlock(Blocks.STONEBRICK, (new ItemMultiTexture(Blocks.STONEBRICK, Blocks.STONEBRICK, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockStoneBrick.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("stonebricksmooth"));
        registerItemBlock(Blocks.BROWN_MUSHROOM_BLOCK);
        registerItemBlock(Blocks.RED_MUSHROOM_BLOCK);
        registerItemBlock(Blocks.IRON_BARS);
        registerItemBlock(Blocks.GLASS_PANE);
        registerItemBlock(Blocks.MELON_BLOCK);
        registerItemBlock(Blocks.VINE, new ItemColored(Blocks.VINE, false));
        registerItemBlock(Blocks.OAK_FENCE_GATE);
        registerItemBlock(Blocks.SPRUCE_FENCE_GATE);
        registerItemBlock(Blocks.BIRCH_FENCE_GATE);
        registerItemBlock(Blocks.JUNGLE_FENCE_GATE);
        registerItemBlock(Blocks.DARK_OAK_FENCE_GATE);
        registerItemBlock(Blocks.ACACIA_FENCE_GATE);
        registerItemBlock(Blocks.BRICK_STAIRS);
        registerItemBlock(Blocks.STONE_BRICK_STAIRS);
        registerItemBlock(Blocks.MYCELIUM);
        registerItemBlock(Blocks.WATERLILY, new ItemLilyPad(Blocks.WATERLILY));
        registerItemBlock(Blocks.NETHER_BRICK);
        registerItemBlock(Blocks.NETHER_BRICK_FENCE);
        registerItemBlock(Blocks.NETHER_BRICK_STAIRS);
        registerItemBlock(Blocks.ENCHANTING_TABLE);
        registerItemBlock(Blocks.END_PORTAL_FRAME);
        registerItemBlock(Blocks.END_STONE);
        registerItemBlock(Blocks.END_BRICKS);
        registerItemBlock(Blocks.DRAGON_EGG);
        registerItemBlock(Blocks.REDSTONE_LAMP);
        registerItemBlock(Blocks.WOODEN_SLAB, (new ItemSlab(Blocks.WOODEN_SLAB, Blocks.WOODEN_SLAB, Blocks.DOUBLE_WOODEN_SLAB)).setTranslationKey("woodSlab"));
        registerItemBlock(Blocks.SANDSTONE_STAIRS);
        registerItemBlock(Blocks.EMERALD_ORE);
        registerItemBlock(Blocks.ENDER_CHEST);
        registerItemBlock(Blocks.TRIPWIRE_HOOK);
        registerItemBlock(Blocks.EMERALD_BLOCK);
        registerItemBlock(Blocks.SPRUCE_STAIRS);
        registerItemBlock(Blocks.BIRCH_STAIRS);
        registerItemBlock(Blocks.JUNGLE_STAIRS);
        registerItemBlock(Blocks.COMMAND_BLOCK);
        registerItemBlock(Blocks.BEACON);
        registerItemBlock(Blocks.COBBLESTONE_WALL, (new ItemMultiTexture(Blocks.COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockWall.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("cobbleWall"));
        registerItemBlock(Blocks.WOODEN_BUTTON);
        registerItemBlock(Blocks.ANVIL, (new ItemAnvilBlock(Blocks.ANVIL)).setTranslationKey("anvil"));
        registerItemBlock(Blocks.TRAPPED_CHEST);
        registerItemBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
        registerItemBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        registerItemBlock(Blocks.DAYLIGHT_DETECTOR);
        registerItemBlock(Blocks.REDSTONE_BLOCK);
        registerItemBlock(Blocks.QUARTZ_ORE);
        registerItemBlock(Blocks.HOPPER);
        registerItemBlock(Blocks.QUARTZ_BLOCK, (new ItemMultiTexture(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, new String[] {"default", "chiseled", "lines"})).setTranslationKey("quartzBlock"));
        registerItemBlock(Blocks.QUARTZ_STAIRS);
        registerItemBlock(Blocks.ACTIVATOR_RAIL);
        registerItemBlock(Blocks.DROPPER);
        registerItemBlock(Blocks.STAINED_HARDENED_CLAY, (new ItemCloth(Blocks.STAINED_HARDENED_CLAY)).setTranslationKey("clayHardenedStained"));
        registerItemBlock(Blocks.BARRIER);
        registerItemBlock(Blocks.IRON_TRAPDOOR);
        registerItemBlock(Blocks.HAY_BLOCK);
        registerItemBlock(Blocks.CARPET, (new ItemCloth(Blocks.CARPET)).setTranslationKey("woolCarpet"));
        registerItemBlock(Blocks.HARDENED_CLAY);
        registerItemBlock(Blocks.COAL_BLOCK);
        registerItemBlock(Blocks.PACKED_ICE);
        registerItemBlock(Blocks.ACACIA_STAIRS);
        registerItemBlock(Blocks.DARK_OAK_STAIRS);
        registerItemBlock(Blocks.SLIME_BLOCK);
        registerItemBlock(Blocks.GRASS_PATH);
        registerItemBlock(Blocks.DOUBLE_PLANT, (new ItemMultiTexture(Blocks.DOUBLE_PLANT, Blocks.DOUBLE_PLANT, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockDoublePlant.EnumPlantType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("doublePlant"));
        registerItemBlock(Blocks.STAINED_GLASS, (new ItemCloth(Blocks.STAINED_GLASS)).setTranslationKey("stainedGlass"));
        registerItemBlock(Blocks.STAINED_GLASS_PANE, (new ItemCloth(Blocks.STAINED_GLASS_PANE)).setTranslationKey("stainedGlassPane"));
        registerItemBlock(Blocks.PRISMARINE, (new ItemMultiTexture(Blocks.PRISMARINE, Blocks.PRISMARINE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockPrismarine.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("prismarine"));
        registerItemBlock(Blocks.SEA_LANTERN);
        registerItemBlock(Blocks.RED_SANDSTONE, (new ItemMultiTexture(Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE, new ItemMultiTexture.Mapper()
        {
            public String apply(ItemStack p_apply_1_)
            {
                return BlockRedSandstone.EnumType.byMetadata(p_apply_1_.getMetadata()).getTranslationKey();
            }
        })).setTranslationKey("redSandStone"));
        registerItemBlock(Blocks.RED_SANDSTONE_STAIRS);
        registerItemBlock(Blocks.STONE_SLAB2, (new ItemSlab(Blocks.STONE_SLAB2, Blocks.STONE_SLAB2, Blocks.DOUBLE_STONE_SLAB2)).setTranslationKey("stoneSlab2"));
        registerItemBlock(Blocks.REPEATING_COMMAND_BLOCK);
        registerItemBlock(Blocks.CHAIN_COMMAND_BLOCK);
        registerItemBlock(Blocks.MAGMA);
        registerItemBlock(Blocks.NETHER_WART_BLOCK);
        registerItemBlock(Blocks.RED_NETHER_BRICK);
        registerItemBlock(Blocks.BONE_BLOCK);
        registerItemBlock(Blocks.STRUCTURE_VOID);
        registerItemBlock(Blocks.OBSERVER);
        registerItemBlock(Blocks.WHITE_SHULKER_BOX, new ItemShulkerBox(Blocks.WHITE_SHULKER_BOX));
        registerItemBlock(Blocks.ORANGE_SHULKER_BOX, new ItemShulkerBox(Blocks.ORANGE_SHULKER_BOX));
        registerItemBlock(Blocks.MAGENTA_SHULKER_BOX, new ItemShulkerBox(Blocks.MAGENTA_SHULKER_BOX));
        registerItemBlock(Blocks.LIGHT_BLUE_SHULKER_BOX, new ItemShulkerBox(Blocks.LIGHT_BLUE_SHULKER_BOX));
        registerItemBlock(Blocks.YELLOW_SHULKER_BOX, new ItemShulkerBox(Blocks.YELLOW_SHULKER_BOX));
        registerItemBlock(Blocks.LIME_SHULKER_BOX, new ItemShulkerBox(Blocks.LIME_SHULKER_BOX));
        registerItemBlock(Blocks.PINK_SHULKER_BOX, new ItemShulkerBox(Blocks.PINK_SHULKER_BOX));
        registerItemBlock(Blocks.GRAY_SHULKER_BOX, new ItemShulkerBox(Blocks.GRAY_SHULKER_BOX));
        registerItemBlock(Blocks.SILVER_SHULKER_BOX, new ItemShulkerBox(Blocks.SILVER_SHULKER_BOX));
        registerItemBlock(Blocks.CYAN_SHULKER_BOX, new ItemShulkerBox(Blocks.CYAN_SHULKER_BOX));
        registerItemBlock(Blocks.PURPLE_SHULKER_BOX, new ItemShulkerBox(Blocks.PURPLE_SHULKER_BOX));
        registerItemBlock(Blocks.BLUE_SHULKER_BOX, new ItemShulkerBox(Blocks.BLUE_SHULKER_BOX));
        registerItemBlock(Blocks.BROWN_SHULKER_BOX, new ItemShulkerBox(Blocks.BROWN_SHULKER_BOX));
        registerItemBlock(Blocks.GREEN_SHULKER_BOX, new ItemShulkerBox(Blocks.GREEN_SHULKER_BOX));
        registerItemBlock(Blocks.RED_SHULKER_BOX, new ItemShulkerBox(Blocks.RED_SHULKER_BOX));
        registerItemBlock(Blocks.BLACK_SHULKER_BOX, new ItemShulkerBox(Blocks.BLACK_SHULKER_BOX));
        registerItemBlock(Blocks.WHITE_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.ORANGE_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.MAGENTA_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.YELLOW_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.LIME_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.PINK_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.GRAY_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.SILVER_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.CYAN_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.PURPLE_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.BLUE_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.BROWN_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.GREEN_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.RED_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.BLACK_GLAZED_TERRACOTTA);
        registerItemBlock(Blocks.CONCRETE, (new ItemCloth(Blocks.CONCRETE)).setTranslationKey("concrete"));
        registerItemBlock(Blocks.CONCRETE_POWDER, (new ItemCloth(Blocks.CONCRETE_POWDER)).setTranslationKey("concrete_powder"));
        registerItemBlock(Blocks.STRUCTURE_BLOCK);
        registerItem(256, "iron_shovel", (new ItemSpade(Item.ToolMaterial.IRON)).setTranslationKey("shovelIron"));
        registerItem(257, "iron_pickaxe", (new ItemPickaxe(Item.ToolMaterial.IRON)).setTranslationKey("pickaxeIron"));
        registerItem(258, "iron_axe", (new ItemAxe(Item.ToolMaterial.IRON)).setTranslationKey("hatchetIron"));
        registerItem(259, "flint_and_steel", (new ItemFlintAndSteel()).setTranslationKey("flintAndSteel"));
        registerItem(260, "apple", (new ItemFood(4, 0.3F, false)).setTranslationKey("apple"));
        registerItem(261, "bow", (new ItemBow()).setTranslationKey("bow"));
        registerItem(262, "arrow", (new ItemArrow()).setTranslationKey("arrow"));
        registerItem(263, "coal", (new ItemCoal()).setTranslationKey("coal"));
        registerItem(264, "diamond", (new Item()).setTranslationKey("diamond").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(265, "iron_ingot", (new Item()).setTranslationKey("ingotIron").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(266, "gold_ingot", (new Item()).setTranslationKey("ingotGold").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(267, "iron_sword", (new ItemSword(Item.ToolMaterial.IRON)).setTranslationKey("swordIron"));
        registerItem(268, "wooden_sword", (new ItemSword(Item.ToolMaterial.WOOD)).setTranslationKey("swordWood"));
        registerItem(269, "wooden_shovel", (new ItemSpade(Item.ToolMaterial.WOOD)).setTranslationKey("shovelWood"));
        registerItem(270, "wooden_pickaxe", (new ItemPickaxe(Item.ToolMaterial.WOOD)).setTranslationKey("pickaxeWood"));
        registerItem(271, "wooden_axe", (new ItemAxe(Item.ToolMaterial.WOOD)).setTranslationKey("hatchetWood"));
        registerItem(272, "stone_sword", (new ItemSword(Item.ToolMaterial.STONE)).setTranslationKey("swordStone"));
        registerItem(273, "stone_shovel", (new ItemSpade(Item.ToolMaterial.STONE)).setTranslationKey("shovelStone"));
        registerItem(274, "stone_pickaxe", (new ItemPickaxe(Item.ToolMaterial.STONE)).setTranslationKey("pickaxeStone"));
        registerItem(275, "stone_axe", (new ItemAxe(Item.ToolMaterial.STONE)).setTranslationKey("hatchetStone"));
        registerItem(276, "diamond_sword", (new ItemSword(Item.ToolMaterial.DIAMOND)).setTranslationKey("swordDiamond"));
        registerItem(277, "diamond_shovel", (new ItemSpade(Item.ToolMaterial.DIAMOND)).setTranslationKey("shovelDiamond"));
        registerItem(278, "diamond_pickaxe", (new ItemPickaxe(Item.ToolMaterial.DIAMOND)).setTranslationKey("pickaxeDiamond"));
        registerItem(279, "diamond_axe", (new ItemAxe(Item.ToolMaterial.DIAMOND)).setTranslationKey("hatchetDiamond"));
        registerItem(280, "stick", (new Item()).setFull3D().setTranslationKey("stick").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(281, "bowl", (new Item()).setTranslationKey("bowl").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(282, "mushroom_stew", (new ItemSoup(6)).setTranslationKey("mushroomStew"));
        registerItem(283, "golden_sword", (new ItemSword(Item.ToolMaterial.GOLD)).setTranslationKey("swordGold"));
        registerItem(284, "golden_shovel", (new ItemSpade(Item.ToolMaterial.GOLD)).setTranslationKey("shovelGold"));
        registerItem(285, "golden_pickaxe", (new ItemPickaxe(Item.ToolMaterial.GOLD)).setTranslationKey("pickaxeGold"));
        registerItem(286, "golden_axe", (new ItemAxe(Item.ToolMaterial.GOLD)).setTranslationKey("hatchetGold"));
        registerItem(287, "string", (new ItemBlockSpecial(Blocks.TRIPWIRE)).setTranslationKey("string").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(288, "feather", (new Item()).setTranslationKey("feather").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(289, "gunpowder", (new Item()).setTranslationKey("sulphur").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(290, "wooden_hoe", (new ItemHoe(Item.ToolMaterial.WOOD)).setTranslationKey("hoeWood"));
        registerItem(291, "stone_hoe", (new ItemHoe(Item.ToolMaterial.STONE)).setTranslationKey("hoeStone"));
        registerItem(292, "iron_hoe", (new ItemHoe(Item.ToolMaterial.IRON)).setTranslationKey("hoeIron"));
        registerItem(293, "diamond_hoe", (new ItemHoe(Item.ToolMaterial.DIAMOND)).setTranslationKey("hoeDiamond"));
        registerItem(294, "golden_hoe", (new ItemHoe(Item.ToolMaterial.GOLD)).setTranslationKey("hoeGold"));
        registerItem(295, "wheat_seeds", (new ItemSeeds(Blocks.WHEAT, Blocks.FARMLAND)).setTranslationKey("seeds"));
        registerItem(296, "wheat", (new Item()).setTranslationKey("wheat").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(297, "bread", (new ItemFood(5, 0.6F, false)).setTranslationKey("bread"));
        registerItem(298, "leather_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD)).setTranslationKey("helmetCloth"));
        registerItem(299, "leather_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST)).setTranslationKey("chestplateCloth"));
        registerItem(300, "leather_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.LEGS)).setTranslationKey("leggingsCloth"));
        registerItem(301, "leather_boots", (new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.FEET)).setTranslationKey("bootsCloth"));
        registerItem(302, "chainmail_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.HEAD)).setTranslationKey("helmetChain"));
        registerItem(303, "chainmail_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.CHEST)).setTranslationKey("chestplateChain"));
        registerItem(304, "chainmail_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.LEGS)).setTranslationKey("leggingsChain"));
        registerItem(305, "chainmail_boots", (new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.FEET)).setTranslationKey("bootsChain"));
        registerItem(306, "iron_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.HEAD)).setTranslationKey("helmetIron"));
        registerItem(307, "iron_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.CHEST)).setTranslationKey("chestplateIron"));
        registerItem(308, "iron_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.LEGS)).setTranslationKey("leggingsIron"));
        registerItem(309, "iron_boots", (new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.FEET)).setTranslationKey("bootsIron"));
        registerItem(310, "diamond_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.HEAD)).setTranslationKey("helmetDiamond"));
        registerItem(311, "diamond_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.CHEST)).setTranslationKey("chestplateDiamond"));
        registerItem(312, "diamond_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.LEGS)).setTranslationKey("leggingsDiamond"));
        registerItem(313, "diamond_boots", (new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.FEET)).setTranslationKey("bootsDiamond"));
        registerItem(314, "golden_helmet", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.HEAD)).setTranslationKey("helmetGold"));
        registerItem(315, "golden_chestplate", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.CHEST)).setTranslationKey("chestplateGold"));
        registerItem(316, "golden_leggings", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.LEGS)).setTranslationKey("leggingsGold"));
        registerItem(317, "golden_boots", (new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.FEET)).setTranslationKey("bootsGold"));
        registerItem(318, "flint", (new Item()).setTranslationKey("flint").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(319, "porkchop", (new ItemFood(3, 0.3F, true)).setTranslationKey("porkchopRaw"));
        registerItem(320, "cooked_porkchop", (new ItemFood(8, 0.8F, true)).setTranslationKey("porkchopCooked"));
        registerItem(321, "painting", (new ItemHangingEntity(EntityPainting.class)).setTranslationKey("painting"));
        registerItem(322, "golden_apple", (new ItemAppleGold(4, 1.2F, false)).setAlwaysEdible().setTranslationKey("appleGold"));
        registerItem(323, "sign", (new ItemSign()).setTranslationKey("sign"));
        registerItem(324, "wooden_door", (new ItemDoor(Blocks.OAK_DOOR)).setTranslationKey("doorOak"));
        Item item = (new ItemBucket(Blocks.AIR)).setTranslationKey("bucket").setMaxStackSize(16);
        registerItem(325, "bucket", item);
        registerItem(326, "water_bucket", (new ItemBucket(Blocks.FLOWING_WATER)).setTranslationKey("bucketWater").setContainerItem(item));
        registerItem(327, "lava_bucket", (new ItemBucket(Blocks.FLOWING_LAVA)).setTranslationKey("bucketLava").setContainerItem(item));
        registerItem(328, "minecart", (new ItemMinecart(EntityMinecart.Type.RIDEABLE)).setTranslationKey("minecart"));
        registerItem(329, "saddle", (new ItemSaddle()).setTranslationKey("saddle"));
        registerItem(330, "iron_door", (new ItemDoor(Blocks.IRON_DOOR)).setTranslationKey("doorIron"));
        registerItem(331, "redstone", (new ItemRedstone()).setTranslationKey("redstone"));
        registerItem(332, "snowball", (new ItemSnowball()).setTranslationKey("snowball"));
        registerItem(333, "boat", new ItemBoat(EntityBoat.Type.OAK));
        registerItem(334, "leather", (new Item()).setTranslationKey("leather").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(335, "milk_bucket", (new ItemBucketMilk()).setTranslationKey("milk").setContainerItem(item));
        registerItem(336, "brick", (new Item()).setTranslationKey("brick").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(337, "clay_ball", (new Item()).setTranslationKey("clay").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(338, "reeds", (new ItemBlockSpecial(Blocks.REEDS)).setTranslationKey("reeds").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(339, "paper", (new Item()).setTranslationKey("paper").setCreativeTab(CreativeTabs.MISC));
        registerItem(340, "book", (new ItemBook()).setTranslationKey("book").setCreativeTab(CreativeTabs.MISC));
        registerItem(341, "slime_ball", (new Item()).setTranslationKey("slimeball").setCreativeTab(CreativeTabs.MISC));
        registerItem(342, "chest_minecart", (new ItemMinecart(EntityMinecart.Type.CHEST)).setTranslationKey("minecartChest"));
        registerItem(343, "furnace_minecart", (new ItemMinecart(EntityMinecart.Type.FURNACE)).setTranslationKey("minecartFurnace"));
        registerItem(344, "egg", (new ItemEgg()).setTranslationKey("egg"));
        registerItem(345, "compass", (new ItemCompass()).setTranslationKey("compass").setCreativeTab(CreativeTabs.TOOLS));
        registerItem(346, "fishing_rod", (new ItemFishingRod()).setTranslationKey("fishingRod"));
        registerItem(347, "clock", (new ItemClock()).setTranslationKey("clock").setCreativeTab(CreativeTabs.TOOLS));
        registerItem(348, "glowstone_dust", (new Item()).setTranslationKey("yellowDust").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(349, "fish", (new ItemFishFood(false)).setTranslationKey("fish").setHasSubtypes(true));
        registerItem(350, "cooked_fish", (new ItemFishFood(true)).setTranslationKey("fish").setHasSubtypes(true));
        registerItem(351, "dye", (new ItemDye()).setTranslationKey("dyePowder"));
        registerItem(352, "bone", (new Item()).setTranslationKey("bone").setFull3D().setCreativeTab(CreativeTabs.MISC));
        registerItem(353, "sugar", (new Item()).setTranslationKey("sugar").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(354, "cake", (new ItemBlockSpecial(Blocks.CAKE)).setMaxStackSize(1).setTranslationKey("cake").setCreativeTab(CreativeTabs.FOOD));
        registerItem(355, "bed", (new ItemBed()).setMaxStackSize(1).setTranslationKey("bed"));
        registerItem(356, "repeater", (new ItemBlockSpecial(Blocks.UNPOWERED_REPEATER)).setTranslationKey("diode").setCreativeTab(CreativeTabs.REDSTONE));
        registerItem(357, "cookie", (new ItemFood(2, 0.1F, false)).setTranslationKey("cookie"));
        registerItem(358, "filled_map", (new ItemMap()).setTranslationKey("map"));
        registerItem(359, "shears", (new ItemShears()).setTranslationKey("shears"));
        registerItem(360, "melon", (new ItemFood(2, 0.3F, false)).setTranslationKey("melon"));
        registerItem(361, "pumpkin_seeds", (new ItemSeeds(Blocks.PUMPKIN_STEM, Blocks.FARMLAND)).setTranslationKey("seeds_pumpkin"));
        registerItem(362, "melon_seeds", (new ItemSeeds(Blocks.MELON_STEM, Blocks.FARMLAND)).setTranslationKey("seeds_melon"));
        registerItem(363, "beef", (new ItemFood(3, 0.3F, true)).setTranslationKey("beefRaw"));
        registerItem(364, "cooked_beef", (new ItemFood(8, 0.8F, true)).setTranslationKey("beefCooked"));
        registerItem(365, "chicken", (new ItemFood(2, 0.3F, true)).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3F).setTranslationKey("chickenRaw"));
        registerItem(366, "cooked_chicken", (new ItemFood(6, 0.6F, true)).setTranslationKey("chickenCooked"));
        registerItem(367, "rotten_flesh", (new ItemFood(4, 0.1F, true)).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.8F).setTranslationKey("rottenFlesh"));
        registerItem(368, "ender_pearl", (new ItemEnderPearl()).setTranslationKey("enderPearl"));
        registerItem(369, "blaze_rod", (new Item()).setTranslationKey("blazeRod").setCreativeTab(CreativeTabs.MATERIALS).setFull3D());
        registerItem(370, "ghast_tear", (new Item()).setTranslationKey("ghastTear").setCreativeTab(CreativeTabs.BREWING));
        registerItem(371, "gold_nugget", (new Item()).setTranslationKey("goldNugget").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(372, "nether_wart", (new ItemSeeds(Blocks.NETHER_WART, Blocks.SOUL_SAND)).setTranslationKey("netherStalkSeeds"));
        registerItem(373, "potion", (new ItemPotion()).setTranslationKey("potion"));
        Item item1 = (new ItemGlassBottle()).setTranslationKey("glassBottle");
        registerItem(374, "glass_bottle", item1);
        registerItem(375, "spider_eye", (new ItemFood(2, 0.8F, false)).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F).setTranslationKey("spiderEye"));
        registerItem(376, "fermented_spider_eye", (new Item()).setTranslationKey("fermentedSpiderEye").setCreativeTab(CreativeTabs.BREWING));
        registerItem(377, "blaze_powder", (new Item()).setTranslationKey("blazePowder").setCreativeTab(CreativeTabs.BREWING));
        registerItem(378, "magma_cream", (new Item()).setTranslationKey("magmaCream").setCreativeTab(CreativeTabs.BREWING));
        registerItem(379, "brewing_stand", (new ItemBlockSpecial(Blocks.BREWING_STAND)).setTranslationKey("brewingStand").setCreativeTab(CreativeTabs.BREWING));
        registerItem(380, "cauldron", (new ItemBlockSpecial(Blocks.CAULDRON)).setTranslationKey("cauldron").setCreativeTab(CreativeTabs.BREWING));
        registerItem(381, "ender_eye", (new ItemEnderEye()).setTranslationKey("eyeOfEnder"));
        registerItem(382, "speckled_melon", (new Item()).setTranslationKey("speckledMelon").setCreativeTab(CreativeTabs.BREWING));
        registerItem(383, "spawn_egg", (new ItemMonsterPlacer()).setTranslationKey("monsterPlacer"));
        registerItem(384, "experience_bottle", (new ItemExpBottle()).setTranslationKey("expBottle"));
        registerItem(385, "fire_charge", (new ItemFireball()).setTranslationKey("fireball"));
        registerItem(386, "writable_book", (new ItemWritableBook()).setTranslationKey("writingBook").setCreativeTab(CreativeTabs.MISC));
        registerItem(387, "written_book", (new ItemWrittenBook()).setTranslationKey("writtenBook").setMaxStackSize(16));
        registerItem(388, "emerald", (new Item()).setTranslationKey("emerald").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(389, "item_frame", (new ItemHangingEntity(EntityItemFrame.class)).setTranslationKey("frame"));
        registerItem(390, "flower_pot", (new ItemBlockSpecial(Blocks.FLOWER_POT)).setTranslationKey("flowerPot").setCreativeTab(CreativeTabs.DECORATIONS));
        registerItem(391, "carrot", (new ItemSeedFood(3, 0.6F, Blocks.CARROTS, Blocks.FARMLAND)).setTranslationKey("carrots"));
        registerItem(392, "potato", (new ItemSeedFood(1, 0.3F, Blocks.POTATOES, Blocks.FARMLAND)).setTranslationKey("potato"));
        registerItem(393, "baked_potato", (new ItemFood(5, 0.6F, false)).setTranslationKey("potatoBaked"));
        registerItem(394, "poisonous_potato", (new ItemFood(2, 0.3F, false)).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 0.6F).setTranslationKey("potatoPoisonous"));
        registerItem(395, "map", (new ItemEmptyMap()).setTranslationKey("emptyMap"));
        registerItem(396, "golden_carrot", (new ItemFood(6, 1.2F, false)).setTranslationKey("carrotGolden").setCreativeTab(CreativeTabs.BREWING));
        registerItem(397, "skull", (new ItemSkull()).setTranslationKey("skull"));
        registerItem(398, "carrot_on_a_stick", (new ItemCarrotOnAStick()).setTranslationKey("carrotOnAStick"));
        registerItem(399, "nether_star", (new ItemSimpleFoiled()).setTranslationKey("netherStar").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(400, "pumpkin_pie", (new ItemFood(8, 0.3F, false)).setTranslationKey("pumpkinPie").setCreativeTab(CreativeTabs.FOOD));
        registerItem(401, "fireworks", (new ItemFirework()).setTranslationKey("fireworks"));
        registerItem(402, "firework_charge", (new ItemFireworkCharge()).setTranslationKey("fireworksCharge").setCreativeTab(CreativeTabs.MISC));
        registerItem(403, "enchanted_book", (new ItemEnchantedBook()).setMaxStackSize(1).setTranslationKey("enchantedBook"));
        registerItem(404, "comparator", (new ItemBlockSpecial(Blocks.UNPOWERED_COMPARATOR)).setTranslationKey("comparator").setCreativeTab(CreativeTabs.REDSTONE));
        registerItem(405, "netherbrick", (new Item()).setTranslationKey("netherbrick").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(406, "quartz", (new Item()).setTranslationKey("netherquartz").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(407, "tnt_minecart", (new ItemMinecart(EntityMinecart.Type.TNT)).setTranslationKey("minecartTnt"));
        registerItem(408, "hopper_minecart", (new ItemMinecart(EntityMinecart.Type.HOPPER)).setTranslationKey("minecartHopper"));
        registerItem(409, "prismarine_shard", (new Item()).setTranslationKey("prismarineShard").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(410, "prismarine_crystals", (new Item()).setTranslationKey("prismarineCrystals").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(411, "rabbit", (new ItemFood(3, 0.3F, true)).setTranslationKey("rabbitRaw"));
        registerItem(412, "cooked_rabbit", (new ItemFood(5, 0.6F, true)).setTranslationKey("rabbitCooked"));
        registerItem(413, "rabbit_stew", (new ItemSoup(10)).setTranslationKey("rabbitStew"));
        registerItem(414, "rabbit_foot", (new Item()).setTranslationKey("rabbitFoot").setCreativeTab(CreativeTabs.BREWING));
        registerItem(415, "rabbit_hide", (new Item()).setTranslationKey("rabbitHide").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(416, "armor_stand", (new ItemArmorStand()).setTranslationKey("armorStand").setMaxStackSize(16));
        registerItem(417, "iron_horse_armor", (new Item()).setTranslationKey("horsearmormetal").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
        registerItem(418, "golden_horse_armor", (new Item()).setTranslationKey("horsearmorgold").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
        registerItem(419, "diamond_horse_armor", (new Item()).setTranslationKey("horsearmordiamond").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
        registerItem(420, "lead", (new ItemLead()).setTranslationKey("leash"));
        registerItem(421, "name_tag", (new ItemNameTag()).setTranslationKey("nameTag"));
        registerItem(422, "command_block_minecart", (new ItemMinecart(EntityMinecart.Type.COMMAND_BLOCK)).setTranslationKey("minecartCommandBlock").setCreativeTab((CreativeTabs)null));
        registerItem(423, "mutton", (new ItemFood(2, 0.3F, true)).setTranslationKey("muttonRaw"));
        registerItem(424, "cooked_mutton", (new ItemFood(6, 0.8F, true)).setTranslationKey("muttonCooked"));
        registerItem(425, "banner", (new ItemBanner()).setTranslationKey("banner"));
        registerItem(426, "end_crystal", new ItemEndCrystal());
        registerItem(427, "spruce_door", (new ItemDoor(Blocks.SPRUCE_DOOR)).setTranslationKey("doorSpruce"));
        registerItem(428, "birch_door", (new ItemDoor(Blocks.BIRCH_DOOR)).setTranslationKey("doorBirch"));
        registerItem(429, "jungle_door", (new ItemDoor(Blocks.JUNGLE_DOOR)).setTranslationKey("doorJungle"));
        registerItem(430, "acacia_door", (new ItemDoor(Blocks.ACACIA_DOOR)).setTranslationKey("doorAcacia"));
        registerItem(431, "dark_oak_door", (new ItemDoor(Blocks.DARK_OAK_DOOR)).setTranslationKey("doorDarkOak"));
        registerItem(432, "chorus_fruit", (new ItemChorusFruit(4, 0.3F)).setAlwaysEdible().setTranslationKey("chorusFruit").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(433, "chorus_fruit_popped", (new Item()).setTranslationKey("chorusFruitPopped").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(434, "beetroot", (new ItemFood(1, 0.6F, false)).setTranslationKey("beetroot"));
        registerItem(435, "beetroot_seeds", (new ItemSeeds(Blocks.BEETROOTS, Blocks.FARMLAND)).setTranslationKey("beetroot_seeds"));
        registerItem(436, "beetroot_soup", (new ItemSoup(6)).setTranslationKey("beetroot_soup"));
        registerItem(437, "dragon_breath", (new Item()).setCreativeTab(CreativeTabs.BREWING).setTranslationKey("dragon_breath").setContainerItem(item1));
        registerItem(438, "splash_potion", (new ItemSplashPotion()).setTranslationKey("splash_potion"));
        registerItem(439, "spectral_arrow", (new ItemSpectralArrow()).setTranslationKey("spectral_arrow"));
        registerItem(440, "tipped_arrow", (new ItemTippedArrow()).setTranslationKey("tipped_arrow"));
        registerItem(441, "lingering_potion", (new ItemLingeringPotion()).setTranslationKey("lingering_potion"));
        registerItem(442, "shield", (new ItemShield()).setTranslationKey("shield"));
        registerItem(443, "elytra", (new ItemElytra()).setTranslationKey("elytra"));
        registerItem(444, "spruce_boat", new ItemBoat(EntityBoat.Type.SPRUCE));
        registerItem(445, "birch_boat", new ItemBoat(EntityBoat.Type.BIRCH));
        registerItem(446, "jungle_boat", new ItemBoat(EntityBoat.Type.JUNGLE));
        registerItem(447, "acacia_boat", new ItemBoat(EntityBoat.Type.ACACIA));
        registerItem(448, "dark_oak_boat", new ItemBoat(EntityBoat.Type.DARK_OAK));
        registerItem(449, "totem_of_undying", (new Item()).setTranslationKey("totem").setMaxStackSize(1).setCreativeTab(CreativeTabs.COMBAT));
        registerItem(450, "shulker_shell", (new Item()).setTranslationKey("shulkerShell").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(452, "iron_nugget", (new Item()).setTranslationKey("ironNugget").setCreativeTab(CreativeTabs.MATERIALS));
        registerItem(453, "knowledge_book", (new ItemKnowledgeBook()).setTranslationKey("knowledgeBook"));
        registerItem(2256, "record_13", (new ItemRecord("13", SoundEvents.RECORD_13)).setTranslationKey("record"));
        registerItem(2257, "record_cat", (new ItemRecord("cat", SoundEvents.RECORD_CAT)).setTranslationKey("record"));
        registerItem(2258, "record_blocks", (new ItemRecord("blocks", SoundEvents.RECORD_BLOCKS)).setTranslationKey("record"));
        registerItem(2259, "record_chirp", (new ItemRecord("chirp", SoundEvents.RECORD_CHIRP)).setTranslationKey("record"));
        registerItem(2260, "record_far", (new ItemRecord("far", SoundEvents.RECORD_FAR)).setTranslationKey("record"));
        registerItem(2261, "record_mall", (new ItemRecord("mall", SoundEvents.RECORD_MALL)).setTranslationKey("record"));
        registerItem(2262, "record_mellohi", (new ItemRecord("mellohi", SoundEvents.RECORD_MELLOHI)).setTranslationKey("record"));
        registerItem(2263, "record_stal", (new ItemRecord("stal", SoundEvents.RECORD_STAL)).setTranslationKey("record"));
        registerItem(2264, "record_strad", (new ItemRecord("strad", SoundEvents.RECORD_STRAD)).setTranslationKey("record"));
        registerItem(2265, "record_ward", (new ItemRecord("ward", SoundEvents.RECORD_WARD)).setTranslationKey("record"));
        registerItem(2266, "record_11", (new ItemRecord("11", SoundEvents.RECORD_11)).setTranslationKey("record"));
        registerItem(2267, "record_wait", (new ItemRecord("wait", SoundEvents.RECORD_WAIT)).setTranslationKey("record"));
    }

    /**
     * Register a default ItemBlock for the given Block.
     */
    private static void registerItemBlock(Block blockIn)
    {
        registerItemBlock(blockIn, new ItemBlock(blockIn));
    }

    /**
     * Register the given Item as the ItemBlock for the given Block.
     */
    protected static void registerItemBlock(Block blockIn, Item itemIn)
    {
        registerItem(Block.getIdFromBlock(blockIn), Block.REGISTRY.getNameForObject(blockIn), itemIn);
        BLOCK_TO_ITEM.put(blockIn, itemIn);
    }

    private static void registerItem(int id, String textualID, Item itemIn)
    {
        registerItem(id, new ResourceLocation(textualID), itemIn);
    }

    private static void registerItem(int id, ResourceLocation textualID, Item itemIn)
    {
        REGISTRY.register(id, textualID, itemIn);
    }

    public ItemStack getDefaultInstance()
    {
        return new ItemStack(this);
    }

    public static enum ToolMaterial
    {
        WOOD(0, 59, 2.0F, 0.0F, 15),
        STONE(1, 131, 4.0F, 1.0F, 5),
        IRON(2, 250, 6.0F, 2.0F, 14),
        DIAMOND(3, 1561, 8.0F, 3.0F, 10),
        GOLD(0, 32, 12.0F, 0.0F, 22);

        private final int harvestLevel;
        private final int maxUses;
        private final float efficiency;
        private final float attackDamage;
        private final int enchantability;

        private ToolMaterial(int harvestLevel, int maxUses, float efficiency, float damageVsEntity, int enchantability)
        {
            this.harvestLevel = harvestLevel;
            this.maxUses = maxUses;
            this.efficiency = efficiency;
            this.attackDamage = damageVsEntity;
            this.enchantability = enchantability;
        }

        public int getMaxUses()
        {
            return this.maxUses;
        }

        public float getEfficiency()
        {
            return this.efficiency;
        }

        public float getAttackDamage()
        {
            return this.attackDamage;
        }

        public int getHarvestLevel()
        {
            return this.harvestLevel;
        }

        public int getEnchantability()
        {
            return this.enchantability;
        }

        public Item getRepairItem()
        {
            if (this == WOOD)
            {
                return Item.getItemFromBlock(Blocks.PLANKS);
            }
            else if (this == STONE)
            {
                return Item.getItemFromBlock(Blocks.COBBLESTONE);
            }
            else if (this == GOLD)
            {
                return Items.GOLD_INGOT;
            }
            else if (this == IRON)
            {
                return Items.IRON_INGOT;
            }
            else
            {
                return this == DIAMOND ? Items.DIAMOND : null;
            }
        }
    }
}
