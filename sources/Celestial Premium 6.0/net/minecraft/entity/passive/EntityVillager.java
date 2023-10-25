/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.passive;

import java.util.Locale;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityVillager
extends EntityAgeable
implements INpc,
IMerchant {
    private static final Logger field_190674_bx = LogManager.getLogger();
    private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityVillager.class, DataSerializers.VARINT);
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village villageObj;
    @Nullable
    private EntityPlayer buyingPlayer;
    @Nullable
    private MerchantRecipeList buyingList;
    private int timeUntilReset;
    private boolean needsInitilization;
    private boolean isWillingToMate;
    private int wealth;
    private String lastBuyingPlayer;
    private int careerId;
    private int careerLevel;
    private boolean isLookingForHome;
    private boolean areAdditionalTasksSet;
    private final InventoryBasic villagerInventory = new InventoryBasic("Items", false, 8);
    private static final ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new ITradeList[][][][]{{{{new EmeraldForItems(Items.WHEAT, new PriceInfo(18, 22)), new EmeraldForItems(Items.POTATO, new PriceInfo(15, 19)), new EmeraldForItems(Items.CARROT, new PriceInfo(15, 19)), new ListItemForEmeralds(Items.BREAD, new PriceInfo(-4, -2))}, {new EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new PriceInfo(8, 13)), new ListItemForEmeralds(Items.PUMPKIN_PIE, new PriceInfo(-3, -2))}, {new EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new PriceInfo(7, 12)), new ListItemForEmeralds(Items.APPLE, new PriceInfo(-7, -5))}, {new ListItemForEmeralds(Items.COOKIE, new PriceInfo(-10, -6)), new ListItemForEmeralds(Items.CAKE, new PriceInfo(1, 1))}}, {{new EmeraldForItems(Items.STRING, new PriceInfo(15, 20)), new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ItemAndEmeraldToItem(Items.FISH, new PriceInfo(6, 6), Items.COOKED_FISH, new PriceInfo(6, 6))}, {new ListEnchantedItemForEmeralds(Items.FISHING_ROD, new PriceInfo(7, 8))}}, {{new EmeraldForItems(Item.getItemFromBlock(Blocks.WOOL), new PriceInfo(16, 22)), new ListItemForEmeralds(Items.SHEARS, new PriceInfo(3, 4))}, {new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL)), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 1), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 2), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 3), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 4), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 5), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 6), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 7), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 8), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 9), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 10), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 11), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 12), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 13), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 14), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 15), new PriceInfo(1, 2))}}, {{new EmeraldForItems(Items.STRING, new PriceInfo(15, 20)), new ListItemForEmeralds(Items.ARROW, new PriceInfo(-12, -8))}, {new ListItemForEmeralds(Items.BOW, new PriceInfo(2, 3)), new ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.GRAVEL), new PriceInfo(10, 10), Items.FLINT, new PriceInfo(6, 10))}}}, {{{new EmeraldForItems(Items.PAPER, new PriceInfo(24, 36)), new ListEnchantedBookForEmeralds()}, {new EmeraldForItems(Items.BOOK, new PriceInfo(8, 10)), new ListItemForEmeralds(Items.COMPASS, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new PriceInfo(3, 4))}, {new EmeraldForItems(Items.WRITTEN_BOOK, new PriceInfo(2, 2)), new ListItemForEmeralds(Items.CLOCK, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new PriceInfo(-5, -3))}, {new ListEnchantedBookForEmeralds()}, {new ListEnchantedBookForEmeralds()}, {new ListItemForEmeralds(Items.NAME_TAG, new PriceInfo(20, 22))}}, {{new EmeraldForItems(Items.PAPER, new PriceInfo(24, 36))}, {new EmeraldForItems(Items.COMPASS, new PriceInfo(1, 1))}, {new ListItemForEmeralds(Items.MAP, new PriceInfo(7, 11))}, {new TreasureMapForEmeralds(new PriceInfo(12, 20), "Monument", MapDecoration.Type.MONUMENT), new TreasureMapForEmeralds(new PriceInfo(16, 28), "Mansion", MapDecoration.Type.MANSION)}}}, {{{new EmeraldForItems(Items.ROTTEN_FLESH, new PriceInfo(36, 40)), new EmeraldForItems(Items.GOLD_INGOT, new PriceInfo(8, 10))}, {new ListItemForEmeralds(Items.REDSTONE, new PriceInfo(-4, -1)), new ListItemForEmeralds(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(-2, -1))}, {new ListItemForEmeralds(Items.ENDER_PEARL, new PriceInfo(4, 7)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new PriceInfo(-3, -1))}, {new ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new PriceInfo(3, 11))}}}, {{{new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.IRON_HELMET, new PriceInfo(4, 6))}, {new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListItemForEmeralds(Items.IRON_CHESTPLATE, new PriceInfo(10, 14))}, {new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, new PriceInfo(16, 19))}, {new ListItemForEmeralds(Items.CHAINMAIL_BOOTS, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.CHAINMAIL_LEGGINGS, new PriceInfo(9, 11)), new ListItemForEmeralds(Items.CHAINMAIL_HELMET, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.CHAINMAIL_CHESTPLATE, new PriceInfo(11, 15))}}, {{new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.IRON_AXE, new PriceInfo(6, 8))}, {new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.IRON_SWORD, new PriceInfo(9, 10))}, {new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new PriceInfo(12, 15)), new ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new PriceInfo(9, 12))}}, {{new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new PriceInfo(5, 7))}, {new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new PriceInfo(9, 11))}, {new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new PriceInfo(12, 15))}}}, {{{new EmeraldForItems(Items.PORKCHOP, new PriceInfo(14, 18)), new EmeraldForItems(Items.CHICKEN, new PriceInfo(14, 18))}, {new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.COOKED_PORKCHOP, new PriceInfo(-7, -5)), new ListItemForEmeralds(Items.COOKED_CHICKEN, new PriceInfo(-8, -6))}}, {{new EmeraldForItems(Items.LEATHER, new PriceInfo(9, 12)), new ListItemForEmeralds(Items.LEATHER_LEGGINGS, new PriceInfo(2, 4))}, {new ListEnchantedItemForEmeralds(Items.LEATHER_CHESTPLATE, new PriceInfo(7, 12))}, {new ListItemForEmeralds(Items.SADDLE, new PriceInfo(8, 10))}}}, {new ITradeList[0][]}};

    public EntityVillager(World worldIn) {
        this(worldIn, 0);
    }

    public EntityVillager(World worldIn, int professionId) {
        super(worldIn);
        this.setProfession(professionId);
        this.setSize(0.6f, 1.95f);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<EntityZombie>(this, EntityZombie.class, 8.0f, 0.6, 0.6));
        this.tasks.addTask(1, new EntityAIAvoidEntity<EntityEvoker>(this, EntityEvoker.class, 12.0f, 0.8, 0.8));
        this.tasks.addTask(1, new EntityAIAvoidEntity<EntityVindicator>(this, EntityVindicator.class, 8.0f, 0.8, 0.8));
        this.tasks.addTask(1, new EntityAIAvoidEntity<EntityVex>(this, EntityVex.class, 8.0f, 0.6, 0.6));
        this.tasks.addTask(1, new EntityAITradePlayer(this));
        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6));
        this.tasks.addTask(6, new EntityAIVillagerMate(this));
        this.tasks.addTask(7, new EntityAIFollowGolem(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(9, new EntityAIVillagerInteract(this));
        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
    }

    private void setAdditionalAItasks() {
        if (!this.areAdditionalTasksSet) {
            this.areAdditionalTasksSet = true;
            if (this.isChild()) {
                this.tasks.addTask(8, new EntityAIPlay(this, 0.32));
            } else if (this.getProfession() == 0) {
                this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6));
            }
        }
    }

    @Override
    protected void onGrowingAdult() {
        if (this.getProfession() == 0) {
            this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6));
        }
        super.onGrowingAdult();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
    }

    @Override
    protected void updateAITasks() {
        if (--this.randomTickDivider <= 0) {
            BlockPos blockpos = new BlockPos(this);
            this.world.getVillageCollection().addToVillagerPositionList(blockpos);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.villageObj = this.world.getVillageCollection().getNearestVillage(blockpos, 32);
            if (this.villageObj == null) {
                this.detachHome();
            } else {
                BlockPos blockpos1 = this.villageObj.getCenter();
                this.setHomePosAndDistance(blockpos1, this.villageObj.getVillageRadius());
                if (this.isLookingForHome) {
                    this.isLookingForHome = false;
                    this.villageObj.setDefaultPlayerReputation(5);
                }
            }
        }
        if (!this.isTrading() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                if (this.needsInitilization) {
                    for (MerchantRecipe merchantrecipe : this.buyingList) {
                        if (!merchantrecipe.isRecipeDisabled()) continue;
                        merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                    }
                    this.populateBuyingList();
                    this.needsInitilization = false;
                    if (this.villageObj != null && this.lastBuyingPlayer != null) {
                        this.world.setEntityState(this, (byte)14);
                        this.villageObj.modifyPlayerReputation(this.lastBuyingPlayer, 1);
                    }
                }
                this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
            }
        }
        super.updateAITasks();
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        boolean flag;
        ItemStack itemstack = player.getHeldItem(hand);
        boolean bl = flag = itemstack.getItem() == Items.NAME_TAG;
        if (flag) {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        if (!this.func_190669_a(itemstack, this.getClass()) && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
            if (this.buyingList == null) {
                this.populateBuyingList();
            }
            if (hand == EnumHand.MAIN_HAND) {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }
            if (!this.world.isRemote && !this.buyingList.isEmpty()) {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            } else if (this.buyingList.isEmpty()) {
                return super.processInteract(player, hand);
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(PROFESSION, 0);
    }

    public static void registerFixesVillager(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityVillager.class);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityVillager.class, "Inventory"));
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker(){

            @Override
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
                NBTTagCompound nbttagcompound;
                if (EntityList.getKey(EntityVillager.class).equals(new ResourceLocation(compound.getString("id"))) && compound.hasKey("Offers", 10) && (nbttagcompound = compound.getCompoundTag("Offers")).hasKey("Recipes", 9)) {
                    NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 10);
                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buy");
                        DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buyB");
                        DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "sell");
                        nbttaglist.set(i, nbttagcompound1);
                    }
                }
                return compound;
            }
        });
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Profession", this.getProfession());
        compound.setInteger("Riches", this.wealth);
        compound.setInteger("Career", this.careerId);
        compound.setInteger("CareerLevel", this.careerLevel);
        compound.setBoolean("Willing", this.isWillingToMate);
        if (this.buyingList != null) {
            compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (itemstack.isEmpty()) continue;
            nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
        }
        compound.setTag("Inventory", nbttaglist);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setProfession(compound.getInteger("Profession"));
        this.wealth = compound.getInteger("Riches");
        this.careerId = compound.getInteger("Career");
        this.careerLevel = compound.getInteger("CareerLevel");
        this.isWillingToMate = compound.getBoolean("Willing");
        if (compound.hasKey("Offers", 10)) {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }
        NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));
            if (itemstack.isEmpty()) continue;
            this.villagerInventory.addItem(itemstack);
        }
        this.setCanPickUpLoot(true);
        this.setAdditionalAItasks();
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.field_191184_at;
    }

    public void setProfession(int professionId) {
        this.dataManager.set(PROFESSION, professionId);
    }

    public int getProfession() {
        return Math.max(this.dataManager.get(PROFESSION) % 6, 0);
    }

    public boolean isMating() {
        return this.isMating;
    }

    public void setMating(boolean mating) {
        this.isMating = mating;
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    @Override
    public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
        super.setRevengeTarget(livingBase);
        if (this.villageObj != null && livingBase != null) {
            this.villageObj.addOrRenewAgressor(livingBase);
            if (livingBase instanceof EntityPlayer) {
                int i = -1;
                if (this.isChild()) {
                    i = -3;
                }
                this.villageObj.modifyPlayerReputation(livingBase.getName(), i);
                if (this.isEntityAlive()) {
                    this.world.setEntityState(this, (byte)13);
                }
            }
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (this.villageObj != null) {
            Entity entity = cause.getEntity();
            if (entity != null) {
                if (entity instanceof EntityPlayer) {
                    this.villageObj.modifyPlayerReputation(entity.getName(), -2);
                } else if (entity instanceof IMob) {
                    this.villageObj.endMatingSeason();
                }
            } else {
                EntityPlayer entityplayer = this.world.getClosestPlayerToEntity(this, 16.0);
                if (entityplayer != null) {
                    this.villageObj.endMatingSeason();
                }
            }
        }
        super.onDeath(cause);
    }

    @Override
    public void setCustomer(@Nullable EntityPlayer player) {
        this.buyingPlayer = player;
    }

    @Override
    @Nullable
    public EntityPlayer getCustomer() {
        return this.buyingPlayer;
    }

    public boolean isTrading() {
        return this.buyingPlayer != null;
    }

    public boolean getIsWillingToMate(boolean updateFirst) {
        if (!this.isWillingToMate && updateFirst && this.hasEnoughFoodToBreed()) {
            boolean flag = false;
            for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
                ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3) {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 3);
                    } else if ((itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT) && itemstack.getCount() >= 12) {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 12);
                    }
                }
                if (!flag) continue;
                this.world.setEntityState(this, (byte)18);
                this.isWillingToMate = true;
                break;
            }
        }
        return this.isWillingToMate;
    }

    public void setIsWillingToMate(boolean isWillingToMate) {
        this.isWillingToMate = isWillingToMate;
    }

    @Override
    public void useRecipe(MerchantRecipe recipe) {
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);
        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToMate = true;
            this.lastBuyingPlayer = this.buyingPlayer != null ? this.buyingPlayer.getName() : null;
            i += 5;
        }
        if (recipe.getItemToBuy().getItem() == Items.EMERALD) {
            this.wealth += recipe.getItemToBuy().getCount();
        }
        if (recipe.getRewardsExp()) {
            this.world.spawnEntityInWorld(new EntityXPOrb(this.world, this.posX, this.posY + 0.5, this.posZ, i));
        }
        if (this.buyingPlayer instanceof EntityPlayerMP) {
            CriteriaTriggers.field_192138_r.func_192234_a((EntityPlayerMP)this.buyingPlayer, this, recipe.getItemToSell());
        }
    }

    @Override
    public void verifySellingItem(ItemStack stack) {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    @Nullable
    public MerchantRecipeList getRecipes(EntityPlayer player) {
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        return this.buyingList;
    }

    private void populateBuyingList() {
        ITradeList[][][] aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP[this.getProfession()];
        if (this.careerId != 0 && this.careerLevel != 0) {
            ++this.careerLevel;
        } else {
            this.careerId = this.rand.nextInt(aentityvillager$itradelist.length) + 1;
            this.careerLevel = 1;
        }
        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }
        int i = this.careerId - 1;
        int j = this.careerLevel - 1;
        if (i >= 0 && i < aentityvillager$itradelist.length) {
            ITradeList[][] aentityvillager$itradelist1 = aentityvillager$itradelist[i];
            if (j >= 0 && j < aentityvillager$itradelist1.length) {
                ITradeList[] aentityvillager$itradelist2;
                for (ITradeList entityvillager$itradelist : aentityvillager$itradelist2 = aentityvillager$itradelist1[j]) {
                    entityvillager$itradelist.func_190888_a(this, this.buyingList, this.rand);
                }
            }
        }
    }

    @Override
    public void setRecipes(@Nullable MerchantRecipeList recipeList) {
    }

    @Override
    public World func_190670_t_() {
        return this.world;
    }

    @Override
    public BlockPos func_190671_u_() {
        return new BlockPos(this);
    }

    @Override
    public ITextComponent getDisplayName() {
        Team team = this.getTeam();
        String s = this.getCustomNameTag();
        if (s != null && !s.isEmpty()) {
            TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, s));
            textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
            textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
            return textcomponentstring;
        }
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        String s1 = null;
        switch (this.getProfession()) {
            case 0: {
                if (this.careerId == 1) {
                    s1 = "farmer";
                    break;
                }
                if (this.careerId == 2) {
                    s1 = "fisherman";
                    break;
                }
                if (this.careerId == 3) {
                    s1 = "shepherd";
                    break;
                }
                if (this.careerId != 4) break;
                s1 = "fletcher";
                break;
            }
            case 1: {
                if (this.careerId == 1) {
                    s1 = "librarian";
                    break;
                }
                if (this.careerId != 2) break;
                s1 = "cartographer";
                break;
            }
            case 2: {
                s1 = "cleric";
                break;
            }
            case 3: {
                if (this.careerId == 1) {
                    s1 = "armor";
                    break;
                }
                if (this.careerId == 2) {
                    s1 = "weapon";
                    break;
                }
                if (this.careerId != 3) break;
                s1 = "tool";
                break;
            }
            case 4: {
                if (this.careerId == 1) {
                    s1 = "butcher";
                    break;
                }
                if (this.careerId != 2) break;
                s1 = "leather";
                break;
            }
            case 5: {
                s1 = "nitwit";
            }
        }
        if (s1 != null) {
            TextComponentTranslation itextcomponent = new TextComponentTranslation("entity.Villager." + s1, new Object[0]);
            itextcomponent.getStyle().setHoverEvent(this.getHoverEvent());
            itextcomponent.getStyle().setInsertion(this.getCachedUniqueIdString());
            if (team != null) {
                itextcomponent.getStyle().setColor(team.getChatFormat());
            }
            return itextcomponent;
        }
        return super.getDisplayName();
    }

    @Override
    public float getEyeHeight() {
        return this.isChild() ? 0.81f : 1.62f;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 12) {
            this.spawnParticles(EnumParticleTypes.HEART);
        } else if (id == 13) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        } else if (id == 14) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    private void spawnParticles(EnumParticleTypes particleType) {
        for (int i = 0; i < 5; ++i) {
            double d0 = this.rand.nextGaussian() * 0.02;
            double d1 = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            this.world.spawnParticle(particleType, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 1.0 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d0, d1, d2, new int[0]);
        }
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        return this.func_190672_a(difficulty, livingdata, true);
    }

    public IEntityLivingData func_190672_a(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_) {
        p_190672_2_ = super.onInitialSpawn(p_190672_1_, p_190672_2_);
        if (p_190672_3_) {
            this.setProfession(this.world.rand.nextInt(6));
        }
        this.setAdditionalAItasks();
        this.populateBuyingList();
        return p_190672_2_;
    }

    public void setLookingForHome() {
        this.isLookingForHome = true;
    }

    @Override
    public EntityVillager createChild(EntityAgeable ageable) {
        EntityVillager entityvillager = new EntityVillager(this.world);
        entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), null);
        return entityvillager;
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        if (!this.world.isRemote && !this.isDead) {
            EntityWitch entitywitch = new EntityWitch(this.world);
            entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitywitch.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitywitch)), null);
            entitywitch.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitywitch.setCustomNameTag(this.getCustomNameTag());
                entitywitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.world.spawnEntityInWorld(entitywitch);
            this.setDead();
        }
    }

    public InventoryBasic getVillagerInventory() {
        return this.villagerInventory;
    }

    @Override
    protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        Item item = itemstack.getItem();
        if (this.canVillagerPickupItem(item)) {
            ItemStack itemstack1 = this.villagerInventory.addItem(itemstack);
            if (itemstack1.isEmpty()) {
                itemEntity.setDead();
            } else {
                itemstack.func_190920_e(itemstack1.getCount());
            }
        }
    }

    private boolean canVillagerPickupItem(Item itemIn) {
        return itemIn == Items.BREAD || itemIn == Items.POTATO || itemIn == Items.CARROT || itemIn == Items.WHEAT || itemIn == Items.WHEAT_SEEDS || itemIn == Items.BEETROOT || itemIn == Items.BEETROOT_SEEDS;
    }

    public boolean hasEnoughFoodToBreed() {
        return this.hasEnoughItems(1);
    }

    public boolean canAbondonItems() {
        return this.hasEnoughItems(2);
    }

    public boolean wantsMoreFood() {
        boolean flag;
        boolean bl = flag = this.getProfession() == 0;
        if (flag) {
            return !this.hasEnoughItems(5);
        }
        return !this.hasEnoughItems(1);
    }

    private boolean hasEnoughItems(int multiplier) {
        boolean flag = this.getProfession() == 0;
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (itemstack.isEmpty()) continue;
            if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3 * multiplier || itemstack.getItem() == Items.POTATO && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.CARROT && itemstack.getCount() >= 12 * multiplier || itemstack.getItem() == Items.BEETROOT && itemstack.getCount() >= 12 * multiplier) {
                return true;
            }
            if (!flag || itemstack.getItem() != Items.WHEAT || itemstack.getCount() < 9 * multiplier) continue;
            return true;
        }
        return false;
    }

    public boolean isFarmItemInInventory() {
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (itemstack.isEmpty() || itemstack.getItem() != Items.WHEAT_SEEDS && itemstack.getItem() != Items.POTATO && itemstack.getItem() != Items.CARROT && itemstack.getItem() != Items.BEETROOT_SEEDS) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn)) {
            return true;
        }
        int i = inventorySlot - 300;
        if (i >= 0 && i < this.villagerInventory.getSizeInventory()) {
            this.villagerInventory.setInventorySlotContents(i, itemStackIn);
            return true;
        }
        return false;
    }

    static class TreasureMapForEmeralds
    implements ITradeList {
        public PriceInfo field_190889_a;
        public String field_190890_b;
        public MapDecoration.Type field_190891_c;

        public TreasureMapForEmeralds(PriceInfo p_i47340_1_, String p_i47340_2_, MapDecoration.Type p_i47340_3_) {
            this.field_190889_a = p_i47340_1_;
            this.field_190890_b = p_i47340_2_;
            this.field_190891_c = p_i47340_3_;
        }

        @Override
        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
            int i = this.field_190889_a.getPrice(p_190888_3_);
            World world = p_190888_1_.func_190670_t_();
            BlockPos blockpos = world.func_190528_a(this.field_190890_b, p_190888_1_.func_190671_u_(), true);
            if (blockpos != null) {
                ItemStack itemstack = ItemMap.func_190906_a(world, blockpos.getX(), blockpos.getZ(), (byte)2, true, true);
                ItemMap.func_190905_a(world, itemstack);
                MapData.func_191094_a(itemstack, blockpos, "+", this.field_190891_c);
                itemstack.func_190924_f("filled_map." + this.field_190890_b.toLowerCase(Locale.ROOT));
                p_190888_2_.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack));
            }
        }
    }

    static class PriceInfo
    extends Tuple<Integer, Integer> {
        public PriceInfo(int p_i45810_1_, int p_i45810_2_) {
            super(p_i45810_1_, p_i45810_2_);
            if (p_i45810_2_ < p_i45810_1_) {
                field_190674_bx.warn("PriceRange({}, {}) invalid, {} smaller than {}", p_i45810_1_, p_i45810_2_, p_i45810_2_, p_i45810_1_);
            }
        }

        public int getPrice(Random rand) {
            return (Integer)this.getFirst() >= (Integer)this.getSecond() ? (Integer)this.getFirst() : (Integer)this.getFirst() + rand.nextInt((Integer)this.getSecond() - (Integer)this.getFirst() + 1);
        }
    }

    static class ListItemForEmeralds
    implements ITradeList {
        public ItemStack itemToBuy;
        public PriceInfo priceInfo;

        public ListItemForEmeralds(Item par1Item, PriceInfo priceInfo) {
            this.itemToBuy = new ItemStack(par1Item);
            this.priceInfo = priceInfo;
        }

        public ListItemForEmeralds(ItemStack stack, PriceInfo priceInfo) {
            this.itemToBuy = stack;
            this.priceInfo = priceInfo;
        }

        @Override
        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
            ItemStack itemstack1;
            ItemStack itemstack;
            int i = 1;
            if (this.priceInfo != null) {
                i = this.priceInfo.getPrice(p_190888_3_);
            }
            if (i < 0) {
                itemstack = new ItemStack(Items.EMERALD);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
            } else {
                itemstack = new ItemStack(Items.EMERALD, i, 0);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
            }
            p_190888_2_.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    static class ListEnchantedItemForEmeralds
    implements ITradeList {
        public ItemStack enchantedItemStack;
        public PriceInfo priceInfo;

        public ListEnchantedItemForEmeralds(Item p_i45814_1_, PriceInfo p_i45814_2_) {
            this.enchantedItemStack = new ItemStack(p_i45814_1_);
            this.priceInfo = p_i45814_2_;
        }

        @Override
        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
            int i = 1;
            if (this.priceInfo != null) {
                i = this.priceInfo.getPrice(p_190888_3_);
            }
            ItemStack itemstack = new ItemStack(Items.EMERALD, i, 0);
            ItemStack itemstack1 = EnchantmentHelper.addRandomEnchantment(p_190888_3_, new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata()), 5 + p_190888_3_.nextInt(15), false);
            p_190888_2_.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }

    static class ListEnchantedBookForEmeralds
    implements ITradeList {
        ListEnchantedBookForEmeralds() {
        }

        @Override
        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
            Enchantment enchantment = (Enchantment)Enchantment.REGISTRY.getRandomObject(p_190888_3_);
            int i = MathHelper.getInt(p_190888_3_, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + p_190888_3_.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasureEnchantment()) {
                j *= 2;
            }
            if (j > 64) {
                j = 64;
            }
            p_190888_2_.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, j), itemstack));
        }
    }

    static class ItemAndEmeraldToItem
    implements ITradeList {
        public ItemStack buyingItemStack;
        public PriceInfo buyingPriceInfo;
        public ItemStack sellingItemstack;
        public PriceInfo sellingPriceInfo;

        public ItemAndEmeraldToItem(Item p_i45813_1_, PriceInfo p_i45813_2_, Item p_i45813_3_, PriceInfo p_i45813_4_) {
            this.buyingItemStack = new ItemStack(p_i45813_1_);
            this.buyingPriceInfo = p_i45813_2_;
            this.sellingItemstack = new ItemStack(p_i45813_3_);
            this.sellingPriceInfo = p_i45813_4_;
        }

        @Override
        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
            int i = this.buyingPriceInfo.getPrice(p_190888_3_);
            int j = this.sellingPriceInfo.getPrice(p_190888_3_);
            p_190888_2_.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.EMERALD), new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
        }
    }

    static interface ITradeList {
        public void func_190888_a(IMerchant var1, MerchantRecipeList var2, Random var3);
    }

    static class EmeraldForItems
    implements ITradeList {
        public Item buyingItem;
        public PriceInfo price;

        public EmeraldForItems(Item itemIn, PriceInfo priceIn) {
            this.buyingItem = itemIn;
            this.price = priceIn;
        }

        @Override
        public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
            int i = 1;
            if (this.price != null) {
                i = this.price.getPrice(p_190888_3_);
            }
            p_190888_2_.add(new MerchantRecipe(new ItemStack(this.buyingItem, i, 0), Items.EMERALD));
        }
    }
}

