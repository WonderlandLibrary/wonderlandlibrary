/*
 * Decompiled with CFR 0.150.
 */
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
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemAnvilBlock;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmorStand;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemCarrotOnAStick;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.item.ItemClock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemColored;
import net.minecraft.item.ItemCompass;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemEmptyMap;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemEnderEye;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemFireball;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemKnowledgeBook;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemLeaves;
import net.minecraft.item.ItemLilyPad;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPiston;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemSaddle;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemSign;
import net.minecraft.item.ItemSimpleFoiled;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemSnow;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
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

public class Item {
    public static final RegistryNamespaced<ResourceLocation, Item> REGISTRY = new RegistryNamespaced();
    private static final Map<Block, Item> BLOCK_TO_ITEM = Maps.newHashMap();
    private static final IItemPropertyGetter DAMAGED_GETTER = new IItemPropertyGetter(){

        @Override
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            return stack.isItemDamaged() ? 1.0f : 0.0f;
        }
    };
    private static final IItemPropertyGetter DAMAGE_GETTER = new IItemPropertyGetter(){

        @Override
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            return MathHelper.clamp((float)stack.getItemDamage() / (float)stack.getMaxDamage(), 0.0f, 1.0f);
        }
    };
    private static final IItemPropertyGetter LEFTHANDED_GETTER = new IItemPropertyGetter(){

        @Override
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            return entityIn != null && entityIn.getPrimaryHand() != EnumHandSide.RIGHT ? 1.0f : 0.0f;
        }
    };
    private static final IItemPropertyGetter COOLDOWN_GETTER = new IItemPropertyGetter(){

        @Override
        public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            return entityIn instanceof EntityPlayer ? ((EntityPlayer)entityIn).getCooldownTracker().getCooldown(stack.getItem(), 0.0f) : 0.0f;
        }
    };
    private final IRegistry<ResourceLocation, IItemPropertyGetter> properties = new RegistrySimple<ResourceLocation, IItemPropertyGetter>();
    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    private CreativeTabs tabToDisplayOn;
    protected static Random itemRand = new Random();
    protected int maxStackSize = 64;
    private int maxDamage;
    protected boolean bFull3D;
    protected boolean hasSubtypes;
    private Item containerItem;
    private String unlocalizedName;

    public static int getIdFromItem(Item itemIn) {
        return itemIn == null ? 0 : REGISTRY.getIDForObject(itemIn);
    }

    public static Item getItemById(int id) {
        return REGISTRY.getObjectById(id);
    }

    public static Item getItemFromBlock(Block blockIn) {
        Item item = BLOCK_TO_ITEM.get(blockIn);
        return item == null ? Items.field_190931_a : item;
    }

    @Nullable
    public static Item getByNameOrId(String id) {
        Item item = REGISTRY.getObject(new ResourceLocation(id));
        if (item == null) {
            try {
                return Item.getItemById(Integer.parseInt(id));
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return item;
    }

    public final void addPropertyOverride(ResourceLocation key, IItemPropertyGetter getter) {
        this.properties.putObject(key, getter);
    }

    @Nullable
    public IItemPropertyGetter getPropertyGetter(ResourceLocation key) {
        return this.properties.getObject(key);
    }

    public boolean hasCustomProperties() {
        return !this.properties.getKeys().isEmpty();
    }

    public boolean updateItemStackNBT(NBTTagCompound nbt) {
        return false;
    }

    public Item() {
        this.addPropertyOverride(new ResourceLocation("lefthanded"), LEFTHANDED_GETTER);
        this.addPropertyOverride(new ResourceLocation("cooldown"), COOLDOWN_GETTER);
    }

    public Item setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        return EnumActionResult.PASS;
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        return 1.0f;
    }

    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
        return new ActionResult<ItemStack>(EnumActionResult.PASS, worldIn.getHeldItem(playerIn));
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        return stack;
    }

    public int getItemStackLimit() {
        return this.maxStackSize;
    }

    public int getMetadata(int damage) {
        return 0;
    }

    public boolean getHasSubtypes() {
        return this.hasSubtypes;
    }

    protected Item setHasSubtypes(boolean hasSubtypes) {
        this.hasSubtypes = hasSubtypes;
        return this;
    }

    public int getMaxDamage() {
        return this.maxDamage;
    }

    protected Item setMaxDamage(int maxDamageIn) {
        this.maxDamage = maxDamageIn;
        if (maxDamageIn > 0) {
            this.addPropertyOverride(new ResourceLocation("damaged"), DAMAGED_GETTER);
            this.addPropertyOverride(new ResourceLocation("damage"), DAMAGE_GETTER);
        }
        return this;
    }

    public boolean isDamageable() {
        return this.maxDamage > 0 && (!this.hasSubtypes || this.maxStackSize == 1);
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return false;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return false;
    }

    public boolean canHarvestBlock(IBlockState blockIn) {
        return false;
    }

    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        return false;
    }

    public Item setFull3D() {
        this.bFull3D = true;
        return this;
    }

    public boolean isFull3D() {
        return this.bFull3D;
    }

    public boolean shouldRotateAroundWhenRendering() {
        return false;
    }

    public Item setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack));
    }

    public String getUnlocalizedName() {
        return "item." + this.unlocalizedName;
    }

    public String getUnlocalizedName(ItemStack stack) {
        return "item." + this.unlocalizedName;
    }

    public Item setContainerItem(Item containerItem) {
        this.containerItem = containerItem;
        return this;
    }

    public boolean getShareTag() {
        return true;
    }

    @Nullable
    public Item getContainerItem() {
        return this.containerItem;
    }

    public boolean hasContainerItem() {
        return this.containerItem != null;
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    }

    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    }

    public boolean isMap() {
        return false;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.NONE;
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 0;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    }

    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }

    public boolean hasEffect(ItemStack stack) {
        return stack.isItemEnchanted();
    }

    public EnumRarity getRarity(ItemStack stack) {
        return stack.isItemEnchanted() ? EnumRarity.RARE : EnumRarity.COMMON;
    }

    public boolean isItemTool(ItemStack stack) {
        return this.getItemStackLimit() == 1 && this.isDamageable();
    }

    protected RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0;
        Vec3d vec3d1 = vec3d.add((double)f6 * 5.0, (double)f5 * 5.0, (double)f7 * 5.0);
        return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
    }

    public int getItemEnchantability() {
        return 0;
    }

    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            tab.add(new ItemStack(this));
        }
    }

    protected boolean func_194125_a(CreativeTabs p_194125_1_) {
        CreativeTabs creativetabs = this.getCreativeTab();
        return creativetabs != null && (p_194125_1_ == CreativeTabs.SEARCH || p_194125_1_ == creativetabs);
    }

    @Nullable
    public CreativeTabs getCreativeTab() {
        return this.tabToDisplayOn;
    }

    public Item setCreativeTab(CreativeTabs tab) {
        this.tabToDisplayOn = tab;
        return this;
    }

    public boolean canItemEditBlocks() {
        return false;
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.create();
    }

    public static void registerItems() {
        Item.registerItemBlock(Blocks.AIR, new ItemAir(Blocks.AIR));
        Item.registerItemBlock(Blocks.STONE, new ItemMultiTexture(Blocks.STONE, Blocks.STONE, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("stone"));
        Item.registerItemBlock(Blocks.GRASS, new ItemColored(Blocks.GRASS, false));
        Item.registerItemBlock(Blocks.DIRT, new ItemMultiTexture(Blocks.DIRT, Blocks.DIRT, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockDirt.DirtType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("dirt"));
        Item.registerItemBlock(Blocks.COBBLESTONE);
        Item.registerItemBlock(Blocks.PLANKS, new ItemMultiTexture(Blocks.PLANKS, Blocks.PLANKS, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("wood"));
        Item.registerItemBlock(Blocks.SAPLING, new ItemMultiTexture(Blocks.SAPLING, Blocks.SAPLING, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("sapling"));
        Item.registerItemBlock(Blocks.BEDROCK);
        Item.registerItemBlock(Blocks.SAND, new ItemMultiTexture((Block)Blocks.SAND, (Block)Blocks.SAND, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockSand.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("sand"));
        Item.registerItemBlock(Blocks.GRAVEL);
        Item.registerItemBlock(Blocks.GOLD_ORE);
        Item.registerItemBlock(Blocks.IRON_ORE);
        Item.registerItemBlock(Blocks.COAL_ORE);
        Item.registerItemBlock(Blocks.LOG, new ItemMultiTexture(Blocks.LOG, Blocks.LOG, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("log"));
        Item.registerItemBlock(Blocks.LOG2, new ItemMultiTexture(Blocks.LOG2, Blocks.LOG2, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockPlanks.EnumType.byMetadata(p_apply_1_.getMetadata() + 4).getUnlocalizedName();
            }
        }).setUnlocalizedName("log"));
        Item.registerItemBlock(Blocks.LEAVES, new ItemLeaves(Blocks.LEAVES).setUnlocalizedName("leaves"));
        Item.registerItemBlock(Blocks.LEAVES2, new ItemLeaves(Blocks.LEAVES2).setUnlocalizedName("leaves"));
        Item.registerItemBlock(Blocks.SPONGE, new ItemMultiTexture(Blocks.SPONGE, Blocks.SPONGE, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return (p_apply_1_.getMetadata() & 1) == 1 ? "wet" : "dry";
            }
        }).setUnlocalizedName("sponge"));
        Item.registerItemBlock(Blocks.GLASS);
        Item.registerItemBlock(Blocks.LAPIS_ORE);
        Item.registerItemBlock(Blocks.LAPIS_BLOCK);
        Item.registerItemBlock(Blocks.DISPENSER);
        Item.registerItemBlock(Blocks.SANDSTONE, new ItemMultiTexture(Blocks.SANDSTONE, Blocks.SANDSTONE, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockSandStone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("sandStone"));
        Item.registerItemBlock(Blocks.NOTEBLOCK);
        Item.registerItemBlock(Blocks.GOLDEN_RAIL);
        Item.registerItemBlock(Blocks.DETECTOR_RAIL);
        Item.registerItemBlock(Blocks.STICKY_PISTON, new ItemPiston(Blocks.STICKY_PISTON));
        Item.registerItemBlock(Blocks.WEB);
        Item.registerItemBlock(Blocks.TALLGRASS, new ItemColored(Blocks.TALLGRASS, true).setSubtypeNames(new String[]{"shrub", "grass", "fern"}));
        Item.registerItemBlock(Blocks.DEADBUSH);
        Item.registerItemBlock(Blocks.PISTON, new ItemPiston(Blocks.PISTON));
        Item.registerItemBlock(Blocks.WOOL, new ItemCloth(Blocks.WOOL).setUnlocalizedName("cloth"));
        Item.registerItemBlock(Blocks.YELLOW_FLOWER, new ItemMultiTexture((Block)Blocks.YELLOW_FLOWER, (Block)Blocks.YELLOW_FLOWER, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.YELLOW, p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("flower"));
        Item.registerItemBlock(Blocks.RED_FLOWER, new ItemMultiTexture((Block)Blocks.RED_FLOWER, (Block)Blocks.RED_FLOWER, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("rose"));
        Item.registerItemBlock(Blocks.BROWN_MUSHROOM);
        Item.registerItemBlock(Blocks.RED_MUSHROOM);
        Item.registerItemBlock(Blocks.GOLD_BLOCK);
        Item.registerItemBlock(Blocks.IRON_BLOCK);
        Item.registerItemBlock(Blocks.STONE_SLAB, new ItemSlab(Blocks.STONE_SLAB, Blocks.STONE_SLAB, Blocks.DOUBLE_STONE_SLAB).setUnlocalizedName("stoneSlab"));
        Item.registerItemBlock(Blocks.BRICK_BLOCK);
        Item.registerItemBlock(Blocks.TNT);
        Item.registerItemBlock(Blocks.BOOKSHELF);
        Item.registerItemBlock(Blocks.MOSSY_COBBLESTONE);
        Item.registerItemBlock(Blocks.OBSIDIAN);
        Item.registerItemBlock(Blocks.TORCH);
        Item.registerItemBlock(Blocks.END_ROD);
        Item.registerItemBlock(Blocks.CHORUS_PLANT);
        Item.registerItemBlock(Blocks.CHORUS_FLOWER);
        Item.registerItemBlock(Blocks.PURPUR_BLOCK);
        Item.registerItemBlock(Blocks.PURPUR_PILLAR);
        Item.registerItemBlock(Blocks.PURPUR_STAIRS);
        Item.registerItemBlock(Blocks.PURPUR_SLAB, new ItemSlab(Blocks.PURPUR_SLAB, Blocks.PURPUR_SLAB, Blocks.PURPUR_DOUBLE_SLAB).setUnlocalizedName("purpurSlab"));
        Item.registerItemBlock(Blocks.MOB_SPAWNER);
        Item.registerItemBlock(Blocks.OAK_STAIRS);
        Item.registerItemBlock(Blocks.CHEST);
        Item.registerItemBlock(Blocks.DIAMOND_ORE);
        Item.registerItemBlock(Blocks.DIAMOND_BLOCK);
        Item.registerItemBlock(Blocks.CRAFTING_TABLE);
        Item.registerItemBlock(Blocks.FARMLAND);
        Item.registerItemBlock(Blocks.FURNACE);
        Item.registerItemBlock(Blocks.LADDER);
        Item.registerItemBlock(Blocks.RAIL);
        Item.registerItemBlock(Blocks.STONE_STAIRS);
        Item.registerItemBlock(Blocks.LEVER);
        Item.registerItemBlock(Blocks.STONE_PRESSURE_PLATE);
        Item.registerItemBlock(Blocks.WOODEN_PRESSURE_PLATE);
        Item.registerItemBlock(Blocks.REDSTONE_ORE);
        Item.registerItemBlock(Blocks.REDSTONE_TORCH);
        Item.registerItemBlock(Blocks.STONE_BUTTON);
        Item.registerItemBlock(Blocks.SNOW_LAYER, new ItemSnow(Blocks.SNOW_LAYER));
        Item.registerItemBlock(Blocks.ICE);
        Item.registerItemBlock(Blocks.SNOW);
        Item.registerItemBlock(Blocks.CACTUS);
        Item.registerItemBlock(Blocks.CLAY);
        Item.registerItemBlock(Blocks.JUKEBOX);
        Item.registerItemBlock(Blocks.OAK_FENCE);
        Item.registerItemBlock(Blocks.SPRUCE_FENCE);
        Item.registerItemBlock(Blocks.BIRCH_FENCE);
        Item.registerItemBlock(Blocks.JUNGLE_FENCE);
        Item.registerItemBlock(Blocks.DARK_OAK_FENCE);
        Item.registerItemBlock(Blocks.ACACIA_FENCE);
        Item.registerItemBlock(Blocks.PUMPKIN);
        Item.registerItemBlock(Blocks.NETHERRACK);
        Item.registerItemBlock(Blocks.SOUL_SAND);
        Item.registerItemBlock(Blocks.GLOWSTONE);
        Item.registerItemBlock(Blocks.LIT_PUMPKIN);
        Item.registerItemBlock(Blocks.TRAPDOOR);
        Item.registerItemBlock(Blocks.MONSTER_EGG, new ItemMultiTexture(Blocks.MONSTER_EGG, Blocks.MONSTER_EGG, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockSilverfish.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("monsterStoneEgg"));
        Item.registerItemBlock(Blocks.STONEBRICK, new ItemMultiTexture(Blocks.STONEBRICK, Blocks.STONEBRICK, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockStoneBrick.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("stonebricksmooth"));
        Item.registerItemBlock(Blocks.BROWN_MUSHROOM_BLOCK);
        Item.registerItemBlock(Blocks.RED_MUSHROOM_BLOCK);
        Item.registerItemBlock(Blocks.IRON_BARS);
        Item.registerItemBlock(Blocks.GLASS_PANE);
        Item.registerItemBlock(Blocks.MELON_BLOCK);
        Item.registerItemBlock(Blocks.VINE, new ItemColored(Blocks.VINE, false));
        Item.registerItemBlock(Blocks.OAK_FENCE_GATE);
        Item.registerItemBlock(Blocks.SPRUCE_FENCE_GATE);
        Item.registerItemBlock(Blocks.BIRCH_FENCE_GATE);
        Item.registerItemBlock(Blocks.JUNGLE_FENCE_GATE);
        Item.registerItemBlock(Blocks.DARK_OAK_FENCE_GATE);
        Item.registerItemBlock(Blocks.ACACIA_FENCE_GATE);
        Item.registerItemBlock(Blocks.BRICK_STAIRS);
        Item.registerItemBlock(Blocks.STONE_BRICK_STAIRS);
        Item.registerItemBlock(Blocks.MYCELIUM);
        Item.registerItemBlock(Blocks.WATERLILY, new ItemLilyPad(Blocks.WATERLILY));
        Item.registerItemBlock(Blocks.NETHER_BRICK);
        Item.registerItemBlock(Blocks.NETHER_BRICK_FENCE);
        Item.registerItemBlock(Blocks.NETHER_BRICK_STAIRS);
        Item.registerItemBlock(Blocks.ENCHANTING_TABLE);
        Item.registerItemBlock(Blocks.END_PORTAL_FRAME);
        Item.registerItemBlock(Blocks.END_STONE);
        Item.registerItemBlock(Blocks.END_BRICKS);
        Item.registerItemBlock(Blocks.DRAGON_EGG);
        Item.registerItemBlock(Blocks.REDSTONE_LAMP);
        Item.registerItemBlock(Blocks.WOODEN_SLAB, new ItemSlab(Blocks.WOODEN_SLAB, Blocks.WOODEN_SLAB, Blocks.DOUBLE_WOODEN_SLAB).setUnlocalizedName("woodSlab"));
        Item.registerItemBlock(Blocks.SANDSTONE_STAIRS);
        Item.registerItemBlock(Blocks.EMERALD_ORE);
        Item.registerItemBlock(Blocks.ENDER_CHEST);
        Item.registerItemBlock(Blocks.TRIPWIRE_HOOK);
        Item.registerItemBlock(Blocks.EMERALD_BLOCK);
        Item.registerItemBlock(Blocks.SPRUCE_STAIRS);
        Item.registerItemBlock(Blocks.BIRCH_STAIRS);
        Item.registerItemBlock(Blocks.JUNGLE_STAIRS);
        Item.registerItemBlock(Blocks.COMMAND_BLOCK);
        Item.registerItemBlock(Blocks.BEACON);
        Item.registerItemBlock(Blocks.COBBLESTONE_WALL, new ItemMultiTexture(Blocks.COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockWall.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("cobbleWall"));
        Item.registerItemBlock(Blocks.WOODEN_BUTTON);
        Item.registerItemBlock(Blocks.ANVIL, new ItemAnvilBlock(Blocks.ANVIL).setUnlocalizedName("anvil"));
        Item.registerItemBlock(Blocks.TRAPPED_CHEST);
        Item.registerItemBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
        Item.registerItemBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        Item.registerItemBlock(Blocks.DAYLIGHT_DETECTOR);
        Item.registerItemBlock(Blocks.REDSTONE_BLOCK);
        Item.registerItemBlock(Blocks.QUARTZ_ORE);
        Item.registerItemBlock(Blocks.HOPPER);
        Item.registerItemBlock(Blocks.QUARTZ_BLOCK, new ItemMultiTexture(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, new String[]{"default", "chiseled", "lines"}).setUnlocalizedName("quartzBlock"));
        Item.registerItemBlock(Blocks.QUARTZ_STAIRS);
        Item.registerItemBlock(Blocks.ACTIVATOR_RAIL);
        Item.registerItemBlock(Blocks.DROPPER);
        Item.registerItemBlock(Blocks.STAINED_HARDENED_CLAY, new ItemCloth(Blocks.STAINED_HARDENED_CLAY).setUnlocalizedName("clayHardenedStained"));
        Item.registerItemBlock(Blocks.BARRIER);
        Item.registerItemBlock(Blocks.IRON_TRAPDOOR);
        Item.registerItemBlock(Blocks.HAY_BLOCK);
        Item.registerItemBlock(Blocks.CARPET, new ItemCloth(Blocks.CARPET).setUnlocalizedName("woolCarpet"));
        Item.registerItemBlock(Blocks.HARDENED_CLAY);
        Item.registerItemBlock(Blocks.COAL_BLOCK);
        Item.registerItemBlock(Blocks.PACKED_ICE);
        Item.registerItemBlock(Blocks.ACACIA_STAIRS);
        Item.registerItemBlock(Blocks.DARK_OAK_STAIRS);
        Item.registerItemBlock(Blocks.SLIME_BLOCK);
        Item.registerItemBlock(Blocks.GRASS_PATH);
        Item.registerItemBlock(Blocks.DOUBLE_PLANT, new ItemMultiTexture((Block)Blocks.DOUBLE_PLANT, (Block)Blocks.DOUBLE_PLANT, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockDoublePlant.EnumPlantType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("doublePlant"));
        Item.registerItemBlock(Blocks.STAINED_GLASS, new ItemCloth(Blocks.STAINED_GLASS).setUnlocalizedName("stainedGlass"));
        Item.registerItemBlock(Blocks.STAINED_GLASS_PANE, new ItemCloth(Blocks.STAINED_GLASS_PANE).setUnlocalizedName("stainedGlassPane"));
        Item.registerItemBlock(Blocks.PRISMARINE, new ItemMultiTexture(Blocks.PRISMARINE, Blocks.PRISMARINE, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockPrismarine.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("prismarine"));
        Item.registerItemBlock(Blocks.SEA_LANTERN);
        Item.registerItemBlock(Blocks.RED_SANDSTONE, new ItemMultiTexture(Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE, new ItemMultiTexture.Mapper(){

            @Override
            public String apply(ItemStack p_apply_1_) {
                return BlockRedSandstone.EnumType.byMetadata(p_apply_1_.getMetadata()).getUnlocalizedName();
            }
        }).setUnlocalizedName("redSandStone"));
        Item.registerItemBlock(Blocks.RED_SANDSTONE_STAIRS);
        Item.registerItemBlock(Blocks.STONE_SLAB2, new ItemSlab(Blocks.STONE_SLAB2, Blocks.STONE_SLAB2, Blocks.DOUBLE_STONE_SLAB2).setUnlocalizedName("stoneSlab2"));
        Item.registerItemBlock(Blocks.REPEATING_COMMAND_BLOCK);
        Item.registerItemBlock(Blocks.CHAIN_COMMAND_BLOCK);
        Item.registerItemBlock(Blocks.MAGMA);
        Item.registerItemBlock(Blocks.NETHER_WART_BLOCK);
        Item.registerItemBlock(Blocks.RED_NETHER_BRICK);
        Item.registerItemBlock(Blocks.BONE_BLOCK);
        Item.registerItemBlock(Blocks.STRUCTURE_VOID);
        Item.registerItemBlock(Blocks.OBSERVER);
        Item.registerItemBlock(Blocks.WHITE_SHULKER_BOX, new ItemShulkerBox(Blocks.WHITE_SHULKER_BOX));
        Item.registerItemBlock(Blocks.ORANGE_SHULKER_BOX, new ItemShulkerBox(Blocks.ORANGE_SHULKER_BOX));
        Item.registerItemBlock(Blocks.MAGENTA_SHULKER_BOX, new ItemShulkerBox(Blocks.MAGENTA_SHULKER_BOX));
        Item.registerItemBlock(Blocks.LIGHT_BLUE_SHULKER_BOX, new ItemShulkerBox(Blocks.LIGHT_BLUE_SHULKER_BOX));
        Item.registerItemBlock(Blocks.YELLOW_SHULKER_BOX, new ItemShulkerBox(Blocks.YELLOW_SHULKER_BOX));
        Item.registerItemBlock(Blocks.LIME_SHULKER_BOX, new ItemShulkerBox(Blocks.LIME_SHULKER_BOX));
        Item.registerItemBlock(Blocks.PINK_SHULKER_BOX, new ItemShulkerBox(Blocks.PINK_SHULKER_BOX));
        Item.registerItemBlock(Blocks.GRAY_SHULKER_BOX, new ItemShulkerBox(Blocks.GRAY_SHULKER_BOX));
        Item.registerItemBlock(Blocks.SILVER_SHULKER_BOX, new ItemShulkerBox(Blocks.SILVER_SHULKER_BOX));
        Item.registerItemBlock(Blocks.CYAN_SHULKER_BOX, new ItemShulkerBox(Blocks.CYAN_SHULKER_BOX));
        Item.registerItemBlock(Blocks.PURPLE_SHULKER_BOX, new ItemShulkerBox(Blocks.PURPLE_SHULKER_BOX));
        Item.registerItemBlock(Blocks.BLUE_SHULKER_BOX, new ItemShulkerBox(Blocks.BLUE_SHULKER_BOX));
        Item.registerItemBlock(Blocks.BROWN_SHULKER_BOX, new ItemShulkerBox(Blocks.BROWN_SHULKER_BOX));
        Item.registerItemBlock(Blocks.GREEN_SHULKER_BOX, new ItemShulkerBox(Blocks.GREEN_SHULKER_BOX));
        Item.registerItemBlock(Blocks.RED_SHULKER_BOX, new ItemShulkerBox(Blocks.RED_SHULKER_BOX));
        Item.registerItemBlock(Blocks.BLACK_SHULKER_BOX, new ItemShulkerBox(Blocks.BLACK_SHULKER_BOX));
        Item.registerItemBlock(Blocks.field_192427_dB);
        Item.registerItemBlock(Blocks.field_192428_dC);
        Item.registerItemBlock(Blocks.field_192429_dD);
        Item.registerItemBlock(Blocks.field_192430_dE);
        Item.registerItemBlock(Blocks.field_192431_dF);
        Item.registerItemBlock(Blocks.field_192432_dG);
        Item.registerItemBlock(Blocks.field_192433_dH);
        Item.registerItemBlock(Blocks.field_192434_dI);
        Item.registerItemBlock(Blocks.field_192435_dJ);
        Item.registerItemBlock(Blocks.field_192436_dK);
        Item.registerItemBlock(Blocks.field_192437_dL);
        Item.registerItemBlock(Blocks.field_192438_dM);
        Item.registerItemBlock(Blocks.field_192439_dN);
        Item.registerItemBlock(Blocks.field_192440_dO);
        Item.registerItemBlock(Blocks.field_192441_dP);
        Item.registerItemBlock(Blocks.field_192442_dQ);
        Item.registerItemBlock(Blocks.field_192443_dR, new ItemCloth(Blocks.field_192443_dR).setUnlocalizedName("concrete"));
        Item.registerItemBlock(Blocks.field_192444_dS, new ItemCloth(Blocks.field_192444_dS).setUnlocalizedName("concrete_powder"));
        Item.registerItemBlock(Blocks.STRUCTURE_BLOCK);
        Item.registerItem(256, "iron_shovel", new ItemSpade(ToolMaterial.IRON).setUnlocalizedName("shovelIron"));
        Item.registerItem(257, "iron_pickaxe", new ItemPickaxe(ToolMaterial.IRON).setUnlocalizedName("pickaxeIron"));
        Item.registerItem(258, "iron_axe", new ItemAxe(ToolMaterial.IRON).setUnlocalizedName("hatchetIron"));
        Item.registerItem(259, "flint_and_steel", new ItemFlintAndSteel().setUnlocalizedName("flintAndSteel"));
        Item.registerItem(260, "apple", new ItemFood(4, 0.3f, false).setUnlocalizedName("apple"));
        Item.registerItem(261, "bow", new ItemBow().setUnlocalizedName("bow"));
        Item.registerItem(262, "arrow", new ItemArrow().setUnlocalizedName("arrow"));
        Item.registerItem(263, "coal", new ItemCoal().setUnlocalizedName("coal"));
        Item.registerItem(264, "diamond", new Item().setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(265, "iron_ingot", new Item().setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(266, "gold_ingot", new Item().setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(267, "iron_sword", new ItemSword(ToolMaterial.IRON).setUnlocalizedName("swordIron"));
        Item.registerItem(268, "wooden_sword", new ItemSword(ToolMaterial.WOOD).setUnlocalizedName("swordWood"));
        Item.registerItem(269, "wooden_shovel", new ItemSpade(ToolMaterial.WOOD).setUnlocalizedName("shovelWood"));
        Item.registerItem(270, "wooden_pickaxe", new ItemPickaxe(ToolMaterial.WOOD).setUnlocalizedName("pickaxeWood"));
        Item.registerItem(271, "wooden_axe", new ItemAxe(ToolMaterial.WOOD).setUnlocalizedName("hatchetWood"));
        Item.registerItem(272, "stone_sword", new ItemSword(ToolMaterial.STONE).setUnlocalizedName("swordStone"));
        Item.registerItem(273, "stone_shovel", new ItemSpade(ToolMaterial.STONE).setUnlocalizedName("shovelStone"));
        Item.registerItem(274, "stone_pickaxe", new ItemPickaxe(ToolMaterial.STONE).setUnlocalizedName("pickaxeStone"));
        Item.registerItem(275, "stone_axe", new ItemAxe(ToolMaterial.STONE).setUnlocalizedName("hatchetStone"));
        Item.registerItem(276, "diamond_sword", new ItemSword(ToolMaterial.DIAMOND).setUnlocalizedName("swordDiamond"));
        Item.registerItem(277, "diamond_shovel", new ItemSpade(ToolMaterial.DIAMOND).setUnlocalizedName("shovelDiamond"));
        Item.registerItem(278, "diamond_pickaxe", new ItemPickaxe(ToolMaterial.DIAMOND).setUnlocalizedName("pickaxeDiamond"));
        Item.registerItem(279, "diamond_axe", new ItemAxe(ToolMaterial.DIAMOND).setUnlocalizedName("hatchetDiamond"));
        Item.registerItem(280, "stick", new Item().setFull3D().setUnlocalizedName("stick").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(281, "bowl", new Item().setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(282, "mushroom_stew", new ItemSoup(6).setUnlocalizedName("mushroomStew"));
        Item.registerItem(283, "golden_sword", new ItemSword(ToolMaterial.GOLD).setUnlocalizedName("swordGold"));
        Item.registerItem(284, "golden_shovel", new ItemSpade(ToolMaterial.GOLD).setUnlocalizedName("shovelGold"));
        Item.registerItem(285, "golden_pickaxe", new ItemPickaxe(ToolMaterial.GOLD).setUnlocalizedName("pickaxeGold"));
        Item.registerItem(286, "golden_axe", new ItemAxe(ToolMaterial.GOLD).setUnlocalizedName("hatchetGold"));
        Item.registerItem(287, "string", new ItemBlockSpecial(Blocks.TRIPWIRE).setUnlocalizedName("string").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(288, "feather", new Item().setUnlocalizedName("feather").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(289, "gunpowder", new Item().setUnlocalizedName("sulphur").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(290, "wooden_hoe", new ItemHoe(ToolMaterial.WOOD).setUnlocalizedName("hoeWood"));
        Item.registerItem(291, "stone_hoe", new ItemHoe(ToolMaterial.STONE).setUnlocalizedName("hoeStone"));
        Item.registerItem(292, "iron_hoe", new ItemHoe(ToolMaterial.IRON).setUnlocalizedName("hoeIron"));
        Item.registerItem(293, "diamond_hoe", new ItemHoe(ToolMaterial.DIAMOND).setUnlocalizedName("hoeDiamond"));
        Item.registerItem(294, "golden_hoe", new ItemHoe(ToolMaterial.GOLD).setUnlocalizedName("hoeGold"));
        Item.registerItem(295, "wheat_seeds", new ItemSeeds(Blocks.WHEAT, Blocks.FARMLAND).setUnlocalizedName("seeds"));
        Item.registerItem(296, "wheat", new Item().setUnlocalizedName("wheat").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(297, "bread", new ItemFood(5, 0.6f, false).setUnlocalizedName("bread"));
        Item.registerItem(298, "leather_helmet", new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("helmetCloth"));
        Item.registerItem(299, "leather_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("chestplateCloth"));
        Item.registerItem(300, "leather_leggings", new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("leggingsCloth"));
        Item.registerItem(301, "leather_boots", new ItemArmor(ItemArmor.ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("bootsCloth"));
        Item.registerItem(302, "chainmail_helmet", new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.HEAD).setUnlocalizedName("helmetChain"));
        Item.registerItem(303, "chainmail_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.CHEST).setUnlocalizedName("chestplateChain"));
        Item.registerItem(304, "chainmail_leggings", new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.LEGS).setUnlocalizedName("leggingsChain"));
        Item.registerItem(305, "chainmail_boots", new ItemArmor(ItemArmor.ArmorMaterial.CHAIN, 1, EntityEquipmentSlot.FEET).setUnlocalizedName("bootsChain"));
        Item.registerItem(306, "iron_helmet", new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.HEAD).setUnlocalizedName("helmetIron"));
        Item.registerItem(307, "iron_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.CHEST).setUnlocalizedName("chestplateIron"));
        Item.registerItem(308, "iron_leggings", new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.LEGS).setUnlocalizedName("leggingsIron"));
        Item.registerItem(309, "iron_boots", new ItemArmor(ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.FEET).setUnlocalizedName("bootsIron"));
        Item.registerItem(310, "diamond_helmet", new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.HEAD).setUnlocalizedName("helmetDiamond"));
        Item.registerItem(311, "diamond_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.CHEST).setUnlocalizedName("chestplateDiamond"));
        Item.registerItem(312, "diamond_leggings", new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.LEGS).setUnlocalizedName("leggingsDiamond"));
        Item.registerItem(313, "diamond_boots", new ItemArmor(ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.FEET).setUnlocalizedName("bootsDiamond"));
        Item.registerItem(314, "golden_helmet", new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.HEAD).setUnlocalizedName("helmetGold"));
        Item.registerItem(315, "golden_chestplate", new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.CHEST).setUnlocalizedName("chestplateGold"));
        Item.registerItem(316, "golden_leggings", new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.LEGS).setUnlocalizedName("leggingsGold"));
        Item.registerItem(317, "golden_boots", new ItemArmor(ItemArmor.ArmorMaterial.GOLD, 4, EntityEquipmentSlot.FEET).setUnlocalizedName("bootsGold"));
        Item.registerItem(318, "flint", new Item().setUnlocalizedName("flint").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(319, "porkchop", new ItemFood(3, 0.3f, true).setUnlocalizedName("porkchopRaw"));
        Item.registerItem(320, "cooked_porkchop", new ItemFood(8, 0.8f, true).setUnlocalizedName("porkchopCooked"));
        Item.registerItem(321, "painting", new ItemHangingEntity(EntityPainting.class).setUnlocalizedName("painting"));
        Item.registerItem(322, "golden_apple", new ItemAppleGold(4, 1.2f, false).setAlwaysEdible().setUnlocalizedName("appleGold"));
        Item.registerItem(323, "sign", new ItemSign().setUnlocalizedName("sign"));
        Item.registerItem(324, "wooden_door", new ItemDoor(Blocks.OAK_DOOR).setUnlocalizedName("doorOak"));
        Item item = new ItemBucket(Blocks.AIR).setUnlocalizedName("bucket").setMaxStackSize(16);
        Item.registerItem(325, "bucket", item);
        Item.registerItem(326, "water_bucket", new ItemBucket(Blocks.FLOWING_WATER).setUnlocalizedName("bucketWater").setContainerItem(item));
        Item.registerItem(327, "lava_bucket", new ItemBucket(Blocks.FLOWING_LAVA).setUnlocalizedName("bucketLava").setContainerItem(item));
        Item.registerItem(328, "minecart", new ItemMinecart(EntityMinecart.Type.RIDEABLE).setUnlocalizedName("minecart"));
        Item.registerItem(329, "saddle", new ItemSaddle().setUnlocalizedName("saddle"));
        Item.registerItem(330, "iron_door", new ItemDoor(Blocks.IRON_DOOR).setUnlocalizedName("doorIron"));
        Item.registerItem(331, "redstone", new ItemRedstone().setUnlocalizedName("redstone"));
        Item.registerItem(332, "snowball", new ItemSnowball().setUnlocalizedName("snowball"));
        Item.registerItem(333, "boat", (Item)new ItemBoat(EntityBoat.Type.OAK));
        Item.registerItem(334, "leather", new Item().setUnlocalizedName("leather").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(335, "milk_bucket", new ItemBucketMilk().setUnlocalizedName("milk").setContainerItem(item));
        Item.registerItem(336, "brick", new Item().setUnlocalizedName("brick").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(337, "clay_ball", new Item().setUnlocalizedName("clay").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(338, "reeds", new ItemBlockSpecial(Blocks.REEDS).setUnlocalizedName("reeds").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(339, "paper", new Item().setUnlocalizedName("paper").setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(340, "book", new ItemBook().setUnlocalizedName("book").setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(341, "slime_ball", new Item().setUnlocalizedName("slimeball").setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(342, "chest_minecart", new ItemMinecart(EntityMinecart.Type.CHEST).setUnlocalizedName("minecartChest"));
        Item.registerItem(343, "furnace_minecart", new ItemMinecart(EntityMinecart.Type.FURNACE).setUnlocalizedName("minecartFurnace"));
        Item.registerItem(344, "egg", new ItemEgg().setUnlocalizedName("egg"));
        Item.registerItem(345, "compass", new ItemCompass().setUnlocalizedName("compass").setCreativeTab(CreativeTabs.TOOLS));
        Item.registerItem(346, "fishing_rod", new ItemFishingRod().setUnlocalizedName("fishingRod"));
        Item.registerItem(347, "clock", new ItemClock().setUnlocalizedName("clock").setCreativeTab(CreativeTabs.TOOLS));
        Item.registerItem(348, "glowstone_dust", new Item().setUnlocalizedName("yellowDust").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(349, "fish", new ItemFishFood(false).setUnlocalizedName("fish").setHasSubtypes(true));
        Item.registerItem(350, "cooked_fish", new ItemFishFood(true).setUnlocalizedName("fish").setHasSubtypes(true));
        Item.registerItem(351, "dye", new ItemDye().setUnlocalizedName("dyePowder"));
        Item.registerItem(352, "bone", new Item().setUnlocalizedName("bone").setFull3D().setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(353, "sugar", new Item().setUnlocalizedName("sugar").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(354, "cake", new ItemBlockSpecial(Blocks.CAKE).setMaxStackSize(1).setUnlocalizedName("cake").setCreativeTab(CreativeTabs.FOOD));
        Item.registerItem(355, "bed", new ItemBed().setMaxStackSize(1).setUnlocalizedName("bed"));
        Item.registerItem(356, "repeater", new ItemBlockSpecial(Blocks.UNPOWERED_REPEATER).setUnlocalizedName("diode").setCreativeTab(CreativeTabs.REDSTONE));
        Item.registerItem(357, "cookie", new ItemFood(2, 0.1f, false).setUnlocalizedName("cookie"));
        Item.registerItem(358, "filled_map", new ItemMap().setUnlocalizedName("map"));
        Item.registerItem(359, "shears", new ItemShears().setUnlocalizedName("shears"));
        Item.registerItem(360, "melon", new ItemFood(2, 0.3f, false).setUnlocalizedName("melon"));
        Item.registerItem(361, "pumpkin_seeds", new ItemSeeds(Blocks.PUMPKIN_STEM, Blocks.FARMLAND).setUnlocalizedName("seeds_pumpkin"));
        Item.registerItem(362, "melon_seeds", new ItemSeeds(Blocks.MELON_STEM, Blocks.FARMLAND).setUnlocalizedName("seeds_melon"));
        Item.registerItem(363, "beef", new ItemFood(3, 0.3f, true).setUnlocalizedName("beefRaw"));
        Item.registerItem(364, "cooked_beef", new ItemFood(8, 0.8f, true).setUnlocalizedName("beefCooked"));
        Item.registerItem(365, "chicken", new ItemFood(2, 0.3f, true).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3f).setUnlocalizedName("chickenRaw"));
        Item.registerItem(366, "cooked_chicken", new ItemFood(6, 0.6f, true).setUnlocalizedName("chickenCooked"));
        Item.registerItem(367, "rotten_flesh", new ItemFood(4, 0.1f, true).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.8f).setUnlocalizedName("rottenFlesh"));
        Item.registerItem(368, "ender_pearl", new ItemEnderPearl().setUnlocalizedName("enderPearl"));
        Item.registerItem(369, "blaze_rod", new Item().setUnlocalizedName("blazeRod").setCreativeTab(CreativeTabs.MATERIALS).setFull3D());
        Item.registerItem(370, "ghast_tear", new Item().setUnlocalizedName("ghastTear").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(371, "gold_nugget", new Item().setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(372, "nether_wart", new ItemSeeds(Blocks.NETHER_WART, Blocks.SOUL_SAND).setUnlocalizedName("netherStalkSeeds"));
        Item.registerItem(373, "potion", new ItemPotion().setUnlocalizedName("potion"));
        Item item1 = new ItemGlassBottle().setUnlocalizedName("glassBottle");
        Item.registerItem(374, "glass_bottle", item1);
        Item.registerItem(375, "spider_eye", new ItemFood(2, 0.8f, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0f).setUnlocalizedName("spiderEye"));
        Item.registerItem(376, "fermented_spider_eye", new Item().setUnlocalizedName("fermentedSpiderEye").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(377, "blaze_powder", new Item().setUnlocalizedName("blazePowder").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(378, "magma_cream", new Item().setUnlocalizedName("magmaCream").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(379, "brewing_stand", new ItemBlockSpecial(Blocks.BREWING_STAND).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(380, "cauldron", new ItemBlockSpecial(Blocks.CAULDRON).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(381, "ender_eye", new ItemEnderEye().setUnlocalizedName("eyeOfEnder"));
        Item.registerItem(382, "speckled_melon", new Item().setUnlocalizedName("speckledMelon").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(383, "spawn_egg", new ItemMonsterPlacer().setUnlocalizedName("monsterPlacer"));
        Item.registerItem(384, "experience_bottle", new ItemExpBottle().setUnlocalizedName("expBottle"));
        Item.registerItem(385, "fire_charge", new ItemFireball().setUnlocalizedName("fireball"));
        Item.registerItem(386, "writable_book", new ItemWritableBook().setUnlocalizedName("writingBook").setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(387, "written_book", new ItemWrittenBook().setUnlocalizedName("writtenBook").setMaxStackSize(16));
        Item.registerItem(388, "emerald", new Item().setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(389, "item_frame", new ItemHangingEntity(EntityItemFrame.class).setUnlocalizedName("frame"));
        Item.registerItem(390, "flower_pot", new ItemBlockSpecial(Blocks.FLOWER_POT).setUnlocalizedName("flowerPot").setCreativeTab(CreativeTabs.DECORATIONS));
        Item.registerItem(391, "carrot", new ItemSeedFood(3, 0.6f, Blocks.CARROTS, Blocks.FARMLAND).setUnlocalizedName("carrots"));
        Item.registerItem(392, "potato", new ItemSeedFood(1, 0.3f, Blocks.POTATOES, Blocks.FARMLAND).setUnlocalizedName("potato"));
        Item.registerItem(393, "baked_potato", new ItemFood(5, 0.6f, false).setUnlocalizedName("potatoBaked"));
        Item.registerItem(394, "poisonous_potato", new ItemFood(2, 0.3f, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 0.6f).setUnlocalizedName("potatoPoisonous"));
        Item.registerItem(395, "map", new ItemEmptyMap().setUnlocalizedName("emptyMap"));
        Item.registerItem(396, "golden_carrot", new ItemFood(6, 1.2f, false).setUnlocalizedName("carrotGolden").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(397, "skull", new ItemSkull().setUnlocalizedName("skull"));
        Item.registerItem(398, "carrot_on_a_stick", new ItemCarrotOnAStick().setUnlocalizedName("carrotOnAStick"));
        Item.registerItem(399, "nether_star", new ItemSimpleFoiled().setUnlocalizedName("netherStar").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(400, "pumpkin_pie", new ItemFood(8, 0.3f, false).setUnlocalizedName("pumpkinPie").setCreativeTab(CreativeTabs.FOOD));
        Item.registerItem(401, "fireworks", new ItemFirework().setUnlocalizedName("fireworks"));
        Item.registerItem(402, "firework_charge", new ItemFireworkCharge().setUnlocalizedName("fireworksCharge").setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(403, "enchanted_book", new ItemEnchantedBook().setMaxStackSize(1).setUnlocalizedName("enchantedBook"));
        Item.registerItem(404, "comparator", new ItemBlockSpecial(Blocks.UNPOWERED_COMPARATOR).setUnlocalizedName("comparator").setCreativeTab(CreativeTabs.REDSTONE));
        Item.registerItem(405, "netherbrick", new Item().setUnlocalizedName("netherbrick").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(406, "quartz", new Item().setUnlocalizedName("netherquartz").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(407, "tnt_minecart", new ItemMinecart(EntityMinecart.Type.TNT).setUnlocalizedName("minecartTnt"));
        Item.registerItem(408, "hopper_minecart", new ItemMinecart(EntityMinecart.Type.HOPPER).setUnlocalizedName("minecartHopper"));
        Item.registerItem(409, "prismarine_shard", new Item().setUnlocalizedName("prismarineShard").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(410, "prismarine_crystals", new Item().setUnlocalizedName("prismarineCrystals").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(411, "rabbit", new ItemFood(3, 0.3f, true).setUnlocalizedName("rabbitRaw"));
        Item.registerItem(412, "cooked_rabbit", new ItemFood(5, 0.6f, true).setUnlocalizedName("rabbitCooked"));
        Item.registerItem(413, "rabbit_stew", new ItemSoup(10).setUnlocalizedName("rabbitStew"));
        Item.registerItem(414, "rabbit_foot", new Item().setUnlocalizedName("rabbitFoot").setCreativeTab(CreativeTabs.BREWING));
        Item.registerItem(415, "rabbit_hide", new Item().setUnlocalizedName("rabbitHide").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(416, "armor_stand", new ItemArmorStand().setUnlocalizedName("armorStand").setMaxStackSize(16));
        Item.registerItem(417, "iron_horse_armor", new Item().setUnlocalizedName("horsearmormetal").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(418, "golden_horse_armor", new Item().setUnlocalizedName("horsearmorgold").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(419, "diamond_horse_armor", new Item().setUnlocalizedName("horsearmordiamond").setMaxStackSize(1).setCreativeTab(CreativeTabs.MISC));
        Item.registerItem(420, "lead", new ItemLead().setUnlocalizedName("leash"));
        Item.registerItem(421, "name_tag", new ItemNameTag().setUnlocalizedName("nameTag"));
        Item.registerItem(422, "command_block_minecart", new ItemMinecart(EntityMinecart.Type.COMMAND_BLOCK).setUnlocalizedName("minecartCommandBlock").setCreativeTab(null));
        Item.registerItem(423, "mutton", new ItemFood(2, 0.3f, true).setUnlocalizedName("muttonRaw"));
        Item.registerItem(424, "cooked_mutton", new ItemFood(6, 0.8f, true).setUnlocalizedName("muttonCooked"));
        Item.registerItem(425, "banner", new ItemBanner().setUnlocalizedName("banner"));
        Item.registerItem(426, "end_crystal", (Item)new ItemEndCrystal());
        Item.registerItem(427, "spruce_door", new ItemDoor(Blocks.SPRUCE_DOOR).setUnlocalizedName("doorSpruce"));
        Item.registerItem(428, "birch_door", new ItemDoor(Blocks.BIRCH_DOOR).setUnlocalizedName("doorBirch"));
        Item.registerItem(429, "jungle_door", new ItemDoor(Blocks.JUNGLE_DOOR).setUnlocalizedName("doorJungle"));
        Item.registerItem(430, "acacia_door", new ItemDoor(Blocks.ACACIA_DOOR).setUnlocalizedName("doorAcacia"));
        Item.registerItem(431, "dark_oak_door", new ItemDoor(Blocks.DARK_OAK_DOOR).setUnlocalizedName("doorDarkOak"));
        Item.registerItem(432, "chorus_fruit", new ItemChorusFruit(4, 0.3f).setAlwaysEdible().setUnlocalizedName("chorusFruit").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(433, "chorus_fruit_popped", new Item().setUnlocalizedName("chorusFruitPopped").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(434, "beetroot", new ItemFood(1, 0.6f, false).setUnlocalizedName("beetroot"));
        Item.registerItem(435, "beetroot_seeds", new ItemSeeds(Blocks.BEETROOTS, Blocks.FARMLAND).setUnlocalizedName("beetroot_seeds"));
        Item.registerItem(436, "beetroot_soup", new ItemSoup(6).setUnlocalizedName("beetroot_soup"));
        Item.registerItem(437, "dragon_breath", new Item().setCreativeTab(CreativeTabs.BREWING).setUnlocalizedName("dragon_breath").setContainerItem(item1));
        Item.registerItem(438, "splash_potion", new ItemSplashPotion().setUnlocalizedName("splash_potion"));
        Item.registerItem(439, "spectral_arrow", new ItemSpectralArrow().setUnlocalizedName("spectral_arrow"));
        Item.registerItem(440, "tipped_arrow", new ItemTippedArrow().setUnlocalizedName("tipped_arrow"));
        Item.registerItem(441, "lingering_potion", new ItemLingeringPotion().setUnlocalizedName("lingering_potion"));
        Item.registerItem(442, "shield", new ItemShield().setUnlocalizedName("shield"));
        Item.registerItem(443, "elytra", new ItemElytra().setUnlocalizedName("elytra"));
        Item.registerItem(444, "spruce_boat", (Item)new ItemBoat(EntityBoat.Type.SPRUCE));
        Item.registerItem(445, "birch_boat", (Item)new ItemBoat(EntityBoat.Type.BIRCH));
        Item.registerItem(446, "jungle_boat", (Item)new ItemBoat(EntityBoat.Type.JUNGLE));
        Item.registerItem(447, "acacia_boat", (Item)new ItemBoat(EntityBoat.Type.ACACIA));
        Item.registerItem(448, "dark_oak_boat", (Item)new ItemBoat(EntityBoat.Type.DARK_OAK));
        Item.registerItem(449, "totem_of_undying", new Item().setUnlocalizedName("totem").setMaxStackSize(1).setCreativeTab(CreativeTabs.COMBAT));
        Item.registerItem(450, "shulker_shell", new Item().setUnlocalizedName("shulkerShell").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(452, "iron_nugget", new Item().setUnlocalizedName("ironNugget").setCreativeTab(CreativeTabs.MATERIALS));
        Item.registerItem(453, "knowledge_book", new ItemKnowledgeBook().setUnlocalizedName("knowledgeBook"));
        Item.registerItem(2256, "record_13", new ItemRecord("13", SoundEvents.RECORD_13).setUnlocalizedName("record"));
        Item.registerItem(2257, "record_cat", new ItemRecord("cat", SoundEvents.RECORD_CAT).setUnlocalizedName("record"));
        Item.registerItem(2258, "record_blocks", new ItemRecord("blocks", SoundEvents.RECORD_BLOCKS).setUnlocalizedName("record"));
        Item.registerItem(2259, "record_chirp", new ItemRecord("chirp", SoundEvents.RECORD_CHIRP).setUnlocalizedName("record"));
        Item.registerItem(2260, "record_far", new ItemRecord("far", SoundEvents.RECORD_FAR).setUnlocalizedName("record"));
        Item.registerItem(2261, "record_mall", new ItemRecord("mall", SoundEvents.RECORD_MALL).setUnlocalizedName("record"));
        Item.registerItem(2262, "record_mellohi", new ItemRecord("mellohi", SoundEvents.RECORD_MELLOHI).setUnlocalizedName("record"));
        Item.registerItem(2263, "record_stal", new ItemRecord("stal", SoundEvents.RECORD_STAL).setUnlocalizedName("record"));
        Item.registerItem(2264, "record_strad", new ItemRecord("strad", SoundEvents.RECORD_STRAD).setUnlocalizedName("record"));
        Item.registerItem(2265, "record_ward", new ItemRecord("ward", SoundEvents.RECORD_WARD).setUnlocalizedName("record"));
        Item.registerItem(2266, "record_11", new ItemRecord("11", SoundEvents.RECORD_11).setUnlocalizedName("record"));
        Item.registerItem(2267, "record_wait", new ItemRecord("wait", SoundEvents.RECORD_WAIT).setUnlocalizedName("record"));
    }

    private static void registerItemBlock(Block blockIn) {
        Item.registerItemBlock(blockIn, new ItemBlock(blockIn));
    }

    protected static void registerItemBlock(Block blockIn, Item itemIn) {
        Item.registerItem(Block.getIdFromBlock(blockIn), Block.REGISTRY.getNameForObject(blockIn), itemIn);
        BLOCK_TO_ITEM.put(blockIn, itemIn);
    }

    private static void registerItem(int id, String textualID, Item itemIn) {
        Item.registerItem(id, new ResourceLocation(textualID), itemIn);
    }

    private static void registerItem(int id, ResourceLocation textualID, Item itemIn) {
        REGISTRY.register(id, textualID, itemIn);
    }

    public ItemStack func_190903_i() {
        return new ItemStack(this);
    }

    public static enum ToolMaterial {
        WOOD(0, 59, 2.0f, 0.0f, 15),
        STONE(1, 131, 4.0f, 1.0f, 5),
        IRON(2, 250, 6.0f, 2.0f, 14),
        DIAMOND(3, 1561, 8.0f, 3.0f, 10),
        GOLD(0, 32, 12.0f, 0.0f, 22);

        private final int harvestLevel;
        private final int maxUses;
        private final float efficiencyOnProperMaterial;
        private final float damageVsEntity;
        private final int enchantability;

        private ToolMaterial(int harvestLevel, int maxUses, float efficiency, float damageVsEntity, int enchantability) {
            this.harvestLevel = harvestLevel;
            this.maxUses = maxUses;
            this.efficiencyOnProperMaterial = efficiency;
            this.damageVsEntity = damageVsEntity;
            this.enchantability = enchantability;
        }

        public int getMaxUses() {
            return this.maxUses;
        }

        public float getEfficiencyOnProperMaterial() {
            return this.efficiencyOnProperMaterial;
        }

        public float getDamageVsEntity() {
            return this.damageVsEntity;
        }

        public int getHarvestLevel() {
            return this.harvestLevel;
        }

        public int getEnchantability() {
            return this.enchantability;
        }

        public Item getRepairItem() {
            if (this == WOOD) {
                return Item.getItemFromBlock(Blocks.PLANKS);
            }
            if (this == STONE) {
                return Item.getItemFromBlock(Blocks.COBBLESTONE);
            }
            if (this == GOLD) {
                return Items.GOLD_INGOT;
            }
            if (this == IRON) {
                return Items.IRON_INGOT;
            }
            return this == DIAMOND ? Items.DIAMOND : null;
        }
    }
}

