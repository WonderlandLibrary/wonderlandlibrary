// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import java.util.Locale;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemMap;
import net.minecraft.util.Tuple;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.Enchantment;
import java.util.Random;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import org.apache.logging.log4j.LogManager;
import net.minecraft.item.Item;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.village.MerchantRecipeList;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.Village;
import net.minecraft.network.datasync.DataParameter;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.EntityAgeable;

public class EntityVillager extends EntityAgeable implements INpc, IMerchant
{
    private static final Logger LOGGER;
    private static final DataParameter<Integer> PROFESSION;
    private int randomTickDivider;
    private boolean isMating;
    private boolean isPlaying;
    Village village;
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
    private final InventoryBasic villagerInventory;
    private static final ITradeList[][][][] DEFAULT_TRADE_LIST_MAP;
    
    public EntityVillager(final World worldIn) {
        this(worldIn, 0);
    }
    
    public EntityVillager(final World worldIn, final int professionId) {
        super(worldIn);
        this.villagerInventory = new InventoryBasic("Items", false, 8);
        this.setProfession(professionId);
        this.setSize(0.6f, 1.95f);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<Object>(this, EntityZombie.class, 8.0f, 0.6, 0.6));
        this.tasks.addTask(1, new EntityAIAvoidEntity<Object>(this, EntityEvoker.class, 12.0f, 0.8, 0.8));
        this.tasks.addTask(1, new EntityAIAvoidEntity<Object>(this, EntityVindicator.class, 8.0f, 0.8, 0.8));
        this.tasks.addTask(1, new EntityAIAvoidEntity<Object>(this, EntityVex.class, 8.0f, 0.6, 0.6));
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
            }
            else if (this.getProfession() == 0) {
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
        final int randomTickDivider = this.randomTickDivider - 1;
        this.randomTickDivider = randomTickDivider;
        if (randomTickDivider <= 0) {
            final BlockPos blockpos = new BlockPos(this);
            this.world.getVillageCollection().addToVillagerPositionList(blockpos);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.village = this.world.getVillageCollection().getNearestVillage(blockpos, 32);
            if (this.village == null) {
                this.detachHome();
            }
            else {
                final BlockPos blockpos2 = this.village.getCenter();
                this.setHomePosAndDistance(blockpos2, this.village.getVillageRadius());
                if (this.isLookingForHome) {
                    this.isLookingForHome = false;
                    this.village.setDefaultPlayerReputation(5);
                }
            }
        }
        if (!this.isTrading() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                if (this.needsInitilization) {
                    for (final MerchantRecipe merchantrecipe : this.buyingList) {
                        if (merchantrecipe.isRecipeDisabled()) {
                            merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                        }
                    }
                    this.populateBuyingList();
                    this.needsInitilization = false;
                    if (this.village != null && this.lastBuyingPlayer != null) {
                        this.world.setEntityState(this, (byte)14);
                        this.village.modifyPlayerReputation(this.lastBuyingPlayer, 1);
                    }
                }
                this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
            }
        }
        super.updateAITasks();
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        final boolean flag = itemstack.getItem() == Items.NAME_TAG;
        if (flag) {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        if (!this.holdingSpawnEggOfClass(itemstack, this.getClass()) && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
            if (this.buyingList == null) {
                this.populateBuyingList();
            }
            if (hand == EnumHand.MAIN_HAND) {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }
            if (!this.world.isRemote && !this.buyingList.isEmpty()) {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            }
            else if (this.buyingList.isEmpty()) {
                return super.processInteract(player, hand);
            }
            return true;
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityVillager.PROFESSION, 0);
    }
    
    public static void registerFixesVillager(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityVillager.class);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackDataLists(EntityVillager.class, new String[] { "Inventory" }));
        fixer.registerWalker(FixTypes.ENTITY, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                if (EntityList.getKey(EntityVillager.class).equals(new ResourceLocation(compound.getString("id"))) && compound.hasKey("Offers", 10)) {
                    final NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
                    if (nbttagcompound.hasKey("Recipes", 9)) {
                        final NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 10);
                        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                            final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                            DataFixesManager.processItemStack(fixer, nbttagcompound2, versionIn, "buy");
                            DataFixesManager.processItemStack(fixer, nbttagcompound2, versionIn, "buyB");
                            DataFixesManager.processItemStack(fixer, nbttagcompound2, versionIn, "sell");
                            nbttaglist.set(i, nbttagcompound2);
                        }
                    }
                }
                return compound;
            }
        });
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Profession", this.getProfession());
        compound.setInteger("Riches", this.wealth);
        compound.setInteger("Career", this.careerId);
        compound.setInteger("CareerLevel", this.careerLevel);
        compound.setBoolean("Willing", this.isWillingToMate);
        if (this.buyingList != null) {
            compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            }
        }
        compound.setTag("Inventory", nbttaglist);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setProfession(compound.getInteger("Profession"));
        this.wealth = compound.getInteger("Riches");
        this.careerId = compound.getInteger("Career");
        this.careerLevel = compound.getInteger("CareerLevel");
        this.isWillingToMate = compound.getBoolean("Willing");
        if (compound.hasKey("Offers", 10)) {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }
        final NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));
            if (!itemstack.isEmpty()) {
                this.villagerInventory.addItem(itemstack);
            }
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
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VILLAGER;
    }
    
    public void setProfession(final int professionId) {
        this.dataManager.set(EntityVillager.PROFESSION, professionId);
    }
    
    public int getProfession() {
        return Math.max(this.dataManager.get(EntityVillager.PROFESSION) % 6, 0);
    }
    
    public boolean isMating() {
        return this.isMating;
    }
    
    public void setMating(final boolean mating) {
        this.isMating = mating;
    }
    
    public void setPlaying(final boolean playing) {
        this.isPlaying = playing;
    }
    
    public boolean isPlaying() {
        return this.isPlaying;
    }
    
    @Override
    public void setRevengeTarget(@Nullable final EntityLivingBase livingBase) {
        super.setRevengeTarget(livingBase);
        if (this.village != null && livingBase != null) {
            this.village.addOrRenewAgressor(livingBase);
            if (livingBase instanceof EntityPlayer) {
                int i = -1;
                if (this.isChild()) {
                    i = -3;
                }
                this.village.modifyPlayerReputation(livingBase.getName(), i);
                if (this.isEntityAlive()) {
                    this.world.setEntityState(this, (byte)13);
                }
            }
        }
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        if (this.village != null) {
            final Entity entity = cause.getTrueSource();
            if (entity != null) {
                if (entity instanceof EntityPlayer) {
                    this.village.modifyPlayerReputation(entity.getName(), -2);
                }
                else if (entity instanceof IMob) {
                    this.village.endMatingSeason();
                }
            }
            else {
                final EntityPlayer entityplayer = this.world.getClosestPlayerToEntity(this, 16.0);
                if (entityplayer != null) {
                    this.village.endMatingSeason();
                }
            }
        }
        super.onDeath(cause);
    }
    
    @Override
    public void setCustomer(@Nullable final EntityPlayer player) {
        this.buyingPlayer = player;
    }
    
    @Nullable
    @Override
    public EntityPlayer getCustomer() {
        return this.buyingPlayer;
    }
    
    public boolean isTrading() {
        return this.buyingPlayer != null;
    }
    
    public boolean getIsWillingToMate(final boolean updateFirst) {
        if (!this.isWillingToMate && updateFirst && this.hasEnoughFoodToBreed()) {
            boolean flag = false;
            for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    if (itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3) {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 3);
                    }
                    else if ((itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT) && itemstack.getCount() >= 12) {
                        flag = true;
                        this.villagerInventory.decrStackSize(i, 12);
                    }
                }
                if (flag) {
                    this.world.setEntityState(this, (byte)18);
                    this.isWillingToMate = true;
                    break;
                }
            }
        }
        return this.isWillingToMate;
    }
    
    public void setIsWillingToMate(final boolean isWillingToMate) {
        this.isWillingToMate = isWillingToMate;
    }
    
    @Override
    public void useRecipe(final MerchantRecipe recipe) {
        recipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        int i = 3 + this.rand.nextInt(4);
        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToMate = true;
            if (this.buyingPlayer != null) {
                this.lastBuyingPlayer = this.buyingPlayer.getName();
            }
            else {
                this.lastBuyingPlayer = null;
            }
            i += 5;
        }
        if (recipe.getItemToBuy().getItem() == Items.EMERALD) {
            this.wealth += recipe.getItemToBuy().getCount();
        }
        if (recipe.getRewardsExp()) {
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5, this.posZ, i));
        }
        if (this.buyingPlayer instanceof EntityPlayerMP) {
            CriteriaTriggers.VILLAGER_TRADE.trigger((EntityPlayerMP)this.buyingPlayer, this, recipe.getItemToSell());
        }
    }
    
    @Override
    public void verifySellingItem(final ItemStack stack) {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        }
    }
    
    @Nullable
    @Override
    public MerchantRecipeList getRecipes(final EntityPlayer player) {
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        return this.buyingList;
    }
    
    private void populateBuyingList() {
        final ITradeList[][][] aentityvillager$itradelist = EntityVillager.DEFAULT_TRADE_LIST_MAP[this.getProfession()];
        if (this.careerId != 0 && this.careerLevel != 0) {
            ++this.careerLevel;
        }
        else {
            this.careerId = this.rand.nextInt(aentityvillager$itradelist.length) + 1;
            this.careerLevel = 1;
        }
        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }
        final int i = this.careerId - 1;
        final int j = this.careerLevel - 1;
        if (i >= 0 && i < aentityvillager$itradelist.length) {
            final ITradeList[][] aentityvillager$itradelist2 = aentityvillager$itradelist[i];
            if (j >= 0 && j < aentityvillager$itradelist2.length) {
                final ITradeList[] array;
                final ITradeList[] aentityvillager$itradelist3 = array = aentityvillager$itradelist2[j];
                for (final ITradeList entityvillager$itradelist : array) {
                    entityvillager$itradelist.addMerchantRecipe(this, this.buyingList, this.rand);
                }
            }
        }
    }
    
    @Override
    public void setRecipes(@Nullable final MerchantRecipeList recipeList) {
    }
    
    @Override
    public World getWorld() {
        return this.world;
    }
    
    @Override
    public BlockPos getPos() {
        return new BlockPos(this);
    }
    
    @Override
    public ITextComponent getDisplayName() {
        final Team team = this.getTeam();
        final String s = this.getCustomNameTag();
        if (s != null && !s.isEmpty()) {
            final TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, s));
            textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
            textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
            return textcomponentstring;
        }
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        String s2 = null;
        switch (this.getProfession()) {
            case 0: {
                if (this.careerId == 1) {
                    s2 = "farmer";
                    break;
                }
                if (this.careerId == 2) {
                    s2 = "fisherman";
                    break;
                }
                if (this.careerId == 3) {
                    s2 = "shepherd";
                    break;
                }
                if (this.careerId == 4) {
                    s2 = "fletcher";
                    break;
                }
                break;
            }
            case 1: {
                if (this.careerId == 1) {
                    s2 = "librarian";
                    break;
                }
                if (this.careerId == 2) {
                    s2 = "cartographer";
                    break;
                }
                break;
            }
            case 2: {
                s2 = "cleric";
                break;
            }
            case 3: {
                if (this.careerId == 1) {
                    s2 = "armor";
                    break;
                }
                if (this.careerId == 2) {
                    s2 = "weapon";
                    break;
                }
                if (this.careerId == 3) {
                    s2 = "tool";
                    break;
                }
                break;
            }
            case 4: {
                if (this.careerId == 1) {
                    s2 = "butcher";
                    break;
                }
                if (this.careerId == 2) {
                    s2 = "leather";
                    break;
                }
                break;
            }
            case 5: {
                s2 = "nitwit";
                break;
            }
        }
        if (s2 != null) {
            final ITextComponent itextcomponent = new TextComponentTranslation("entity.Villager." + s2, new Object[0]);
            itextcomponent.getStyle().setHoverEvent(this.getHoverEvent());
            itextcomponent.getStyle().setInsertion(this.getCachedUniqueIdString());
            if (team != null) {
                itextcomponent.getStyle().setColor(team.getColor());
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
    public void handleStatusUpdate(final byte id) {
        if (id == 12) {
            this.spawnParticles(EnumParticleTypes.HEART);
        }
        else if (id == 13) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else if (id == 14) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    private void spawnParticles(final EnumParticleTypes particleType) {
        for (int i = 0; i < 5; ++i) {
            final double d0 = this.rand.nextGaussian() * 0.02;
            final double d2 = this.rand.nextGaussian() * 0.02;
            final double d3 = this.rand.nextGaussian() * 0.02;
            this.world.spawnParticle(particleType, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 1.0 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
        }
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        return this.finalizeMobSpawn(difficulty, livingdata, true);
    }
    
    public IEntityLivingData finalizeMobSpawn(final DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, final boolean p_190672_3_) {
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
    public EntityVillager createChild(final EntityAgeable ageable) {
        final EntityVillager entityvillager = new EntityVillager(this.world);
        entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), null);
        return entityvillager;
    }
    
    @Override
    public boolean canBeLeashedTo(final EntityPlayer player) {
        return false;
    }
    
    @Override
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        if (!this.world.isRemote && !this.isDead) {
            final EntityWitch entitywitch = new EntityWitch(this.world);
            entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitywitch.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitywitch)), null);
            entitywitch.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitywitch.setCustomNameTag(this.getCustomNameTag());
                entitywitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.world.spawnEntity(entitywitch);
            this.setDead();
        }
    }
    
    public InventoryBasic getVillagerInventory() {
        return this.villagerInventory;
    }
    
    @Override
    protected void updateEquipmentIfNeeded(final EntityItem itemEntity) {
        final ItemStack itemstack = itemEntity.getItem();
        final Item item = itemstack.getItem();
        if (this.canVillagerPickupItem(item)) {
            final ItemStack itemstack2 = this.villagerInventory.addItem(itemstack);
            if (itemstack2.isEmpty()) {
                itemEntity.setDead();
            }
            else {
                itemstack.setCount(itemstack2.getCount());
            }
        }
    }
    
    private boolean canVillagerPickupItem(final Item itemIn) {
        return itemIn == Items.BREAD || itemIn == Items.POTATO || itemIn == Items.CARROT || itemIn == Items.WHEAT || itemIn == Items.WHEAT_SEEDS || itemIn == Items.BEETROOT || itemIn == Items.BEETROOT_SEEDS;
    }
    
    public boolean hasEnoughFoodToBreed() {
        return this.hasEnoughItems(1);
    }
    
    public boolean canAbondonItems() {
        return this.hasEnoughItems(2);
    }
    
    public boolean wantsMoreFood() {
        final boolean flag = this.getProfession() == 0;
        if (flag) {
            return !this.hasEnoughItems(5);
        }
        return !this.hasEnoughItems(1);
    }
    
    private boolean hasEnoughItems(final int multiplier) {
        final boolean flag = this.getProfession() == 0;
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                if ((itemstack.getItem() == Items.BREAD && itemstack.getCount() >= 3 * multiplier) || (itemstack.getItem() == Items.POTATO && itemstack.getCount() >= 12 * multiplier) || (itemstack.getItem() == Items.CARROT && itemstack.getCount() >= 12 * multiplier) || (itemstack.getItem() == Items.BEETROOT && itemstack.getCount() >= 12 * multiplier)) {
                    return true;
                }
                if (flag && itemstack.getItem() == Items.WHEAT && itemstack.getCount() >= 9 * multiplier) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isFarmItemInInventory() {
        for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
            if (!itemstack.isEmpty() && (itemstack.getItem() == Items.WHEAT_SEEDS || itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT || itemstack.getItem() == Items.BEETROOT_SEEDS)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        if (super.replaceItemInInventory(inventorySlot, itemStackIn)) {
            return true;
        }
        final int i = inventorySlot - 300;
        if (i >= 0 && i < this.villagerInventory.getSizeInventory()) {
            this.villagerInventory.setInventorySlotContents(i, itemStackIn);
            return true;
        }
        return false;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PROFESSION = EntityDataManager.createKey(EntityVillager.class, DataSerializers.VARINT);
        DEFAULT_TRADE_LIST_MAP = new ITradeList[][][][] { { { { new EmeraldForItems(Items.WHEAT, new PriceInfo(18, 22)), new EmeraldForItems(Items.POTATO, new PriceInfo(15, 19)), new EmeraldForItems(Items.CARROT, new PriceInfo(15, 19)), new ListItemForEmeralds(Items.BREAD, new PriceInfo(-4, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new PriceInfo(8, 13)), new ListItemForEmeralds(Items.PUMPKIN_PIE, new PriceInfo(-3, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new PriceInfo(7, 12)), new ListItemForEmeralds(Items.APPLE, new PriceInfo(-7, -5)) }, { new ListItemForEmeralds(Items.COOKIE, new PriceInfo(-10, -6)), new ListItemForEmeralds(Items.CAKE, new PriceInfo(1, 1)) } }, { { new EmeraldForItems(Items.STRING, new PriceInfo(15, 20)), new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ItemAndEmeraldToItem(Items.FISH, new PriceInfo(6, 6), Items.COOKED_FISH, new PriceInfo(6, 6)) }, { new ListEnchantedItemForEmeralds(Items.FISHING_ROD, new PriceInfo(7, 8)) } }, { { new EmeraldForItems(Item.getItemFromBlock(Blocks.WOOL), new PriceInfo(16, 22)), new ListItemForEmeralds(Items.SHEARS, new PriceInfo(3, 4)) }, { new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL)), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 1), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 2), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 3), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 4), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 5), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 6), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 7), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 8), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 9), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 10), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 11), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 12), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 13), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 14), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 15), new PriceInfo(1, 2)) } }, { { new EmeraldForItems(Items.STRING, new PriceInfo(15, 20)), new ListItemForEmeralds(Items.ARROW, new PriceInfo(-12, -8)) }, { new ListItemForEmeralds(Items.BOW, new PriceInfo(2, 3)), new ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.GRAVEL), new PriceInfo(10, 10), Items.FLINT, new PriceInfo(6, 10)) } } }, { { { new EmeraldForItems(Items.PAPER, new PriceInfo(24, 36)), new ListEnchantedBookForEmeralds() }, { new EmeraldForItems(Items.BOOK, new PriceInfo(8, 10)), new ListItemForEmeralds(Items.COMPASS, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new PriceInfo(3, 4)) }, { new EmeraldForItems(Items.WRITTEN_BOOK, new PriceInfo(2, 2)), new ListItemForEmeralds(Items.CLOCK, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new PriceInfo(-5, -3)) }, { new ListEnchantedBookForEmeralds() }, { new ListEnchantedBookForEmeralds() }, { new ListItemForEmeralds(Items.NAME_TAG, new PriceInfo(20, 22)) } }, { { new EmeraldForItems(Items.PAPER, new PriceInfo(24, 36)) }, { new EmeraldForItems(Items.COMPASS, new PriceInfo(1, 1)) }, { new ListItemForEmeralds(Items.MAP, new PriceInfo(7, 11)) }, { new TreasureMapForEmeralds(new PriceInfo(12, 20), "Monument", MapDecoration.Type.MONUMENT), new TreasureMapForEmeralds(new PriceInfo(16, 28), "Mansion", MapDecoration.Type.MANSION) } } }, { { { new EmeraldForItems(Items.ROTTEN_FLESH, new PriceInfo(36, 40)), new EmeraldForItems(Items.GOLD_INGOT, new PriceInfo(8, 10)) }, { new ListItemForEmeralds(Items.REDSTONE, new PriceInfo(-4, -1)), new ListItemForEmeralds(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(-2, -1)) }, { new ListItemForEmeralds(Items.ENDER_PEARL, new PriceInfo(4, 7)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new PriceInfo(-3, -1)) }, { new ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new PriceInfo(3, 11)) } } }, { { { new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.IRON_HELMET, new PriceInfo(4, 6)) }, { new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListItemForEmeralds(Items.IRON_CHESTPLATE, new PriceInfo(10, 14)) }, { new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, new PriceInfo(16, 19)) }, { new ListItemForEmeralds(Items.CHAINMAIL_BOOTS, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.CHAINMAIL_LEGGINGS, new PriceInfo(9, 11)), new ListItemForEmeralds(Items.CHAINMAIL_HELMET, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.CHAINMAIL_CHESTPLATE, new PriceInfo(11, 15)) } }, { { new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.IRON_AXE, new PriceInfo(6, 8)) }, { new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.IRON_SWORD, new PriceInfo(9, 10)) }, { new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new PriceInfo(12, 15)), new ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new PriceInfo(9, 12)) } }, { { new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new PriceInfo(5, 7)) }, { new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new PriceInfo(9, 11)) }, { new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new PriceInfo(12, 15)) } } }, { { { new EmeraldForItems(Items.PORKCHOP, new PriceInfo(14, 18)), new EmeraldForItems(Items.CHICKEN, new PriceInfo(14, 18)) }, { new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.COOKED_PORKCHOP, new PriceInfo(-7, -5)), new ListItemForEmeralds(Items.COOKED_CHICKEN, new PriceInfo(-8, -6)) } }, { { new EmeraldForItems(Items.LEATHER, new PriceInfo(9, 12)), new ListItemForEmeralds(Items.LEATHER_LEGGINGS, new PriceInfo(2, 4)) }, { new ListEnchantedItemForEmeralds(Items.LEATHER_CHESTPLATE, new PriceInfo(7, 12)) }, { new ListItemForEmeralds(Items.SADDLE, new PriceInfo(8, 10)) } } }, { new ITradeList[0][] } };
    }
    
    static class EmeraldForItems implements ITradeList
    {
        public Item buyingItem;
        public PriceInfo price;
        
        public EmeraldForItems(final Item itemIn, final PriceInfo priceIn) {
            this.buyingItem = itemIn;
            this.price = priceIn;
        }
        
        @Override
        public void addMerchantRecipe(final IMerchant merchant, final MerchantRecipeList recipeList, final Random random) {
            int i = 1;
            if (this.price != null) {
                i = this.price.getPrice(random);
            }
            recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItem, i, 0), Items.EMERALD));
        }
    }
    
    static class ItemAndEmeraldToItem implements ITradeList
    {
        public ItemStack buyingItemStack;
        public PriceInfo buyingPriceInfo;
        public ItemStack sellingItemstack;
        public PriceInfo sellingPriceInfo;
        
        public ItemAndEmeraldToItem(final Item p_i45813_1_, final PriceInfo p_i45813_2_, final Item p_i45813_3_, final PriceInfo p_i45813_4_) {
            this.buyingItemStack = new ItemStack(p_i45813_1_);
            this.buyingPriceInfo = p_i45813_2_;
            this.sellingItemstack = new ItemStack(p_i45813_3_);
            this.sellingPriceInfo = p_i45813_4_;
        }
        
        @Override
        public void addMerchantRecipe(final IMerchant merchant, final MerchantRecipeList recipeList, final Random random) {
            final int i = this.buyingPriceInfo.getPrice(random);
            final int j = this.sellingPriceInfo.getPrice(random);
            recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.EMERALD), new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
        }
    }
    
    static class ListEnchantedBookForEmeralds implements ITradeList
    {
        @Override
        public void addMerchantRecipe(final IMerchant merchant, final MerchantRecipeList recipeList, final Random random) {
            final Enchantment enchantment = Enchantment.REGISTRY.getRandomObject(random);
            final int i = MathHelper.getInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            final ItemStack itemstack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasureEnchantment()) {
                j *= 2;
            }
            if (j > 64) {
                j = 64;
            }
            recipeList.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, j), itemstack));
        }
    }
    
    static class ListEnchantedItemForEmeralds implements ITradeList
    {
        public ItemStack enchantedItemStack;
        public PriceInfo priceInfo;
        
        public ListEnchantedItemForEmeralds(final Item p_i45814_1_, final PriceInfo p_i45814_2_) {
            this.enchantedItemStack = new ItemStack(p_i45814_1_);
            this.priceInfo = p_i45814_2_;
        }
        
        @Override
        public void addMerchantRecipe(final IMerchant merchant, final MerchantRecipeList recipeList, final Random random) {
            int i = 1;
            if (this.priceInfo != null) {
                i = this.priceInfo.getPrice(random);
            }
            final ItemStack itemstack = new ItemStack(Items.EMERALD, i, 0);
            final ItemStack itemstack2 = EnchantmentHelper.addRandomEnchantment(random, new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata()), 5 + random.nextInt(15), false);
            recipeList.add(new MerchantRecipe(itemstack, itemstack2));
        }
    }
    
    static class ListItemForEmeralds implements ITradeList
    {
        public ItemStack itemToBuy;
        public PriceInfo priceInfo;
        
        public ListItemForEmeralds(final Item par1Item, final PriceInfo priceInfo) {
            this.itemToBuy = new ItemStack(par1Item);
            this.priceInfo = priceInfo;
        }
        
        public ListItemForEmeralds(final ItemStack stack, final PriceInfo priceInfo) {
            this.itemToBuy = stack;
            this.priceInfo = priceInfo;
        }
        
        @Override
        public void addMerchantRecipe(final IMerchant merchant, final MerchantRecipeList recipeList, final Random random) {
            int i = 1;
            if (this.priceInfo != null) {
                i = this.priceInfo.getPrice(random);
            }
            ItemStack itemstack;
            ItemStack itemstack2;
            if (i < 0) {
                itemstack = new ItemStack(Items.EMERALD);
                itemstack2 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
            }
            else {
                itemstack = new ItemStack(Items.EMERALD, i, 0);
                itemstack2 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
            }
            recipeList.add(new MerchantRecipe(itemstack, itemstack2));
        }
    }
    
    static class PriceInfo extends Tuple<Integer, Integer>
    {
        public PriceInfo(final int p_i45810_1_, final int p_i45810_2_) {
            super(p_i45810_1_, p_i45810_2_);
            if (p_i45810_2_ < p_i45810_1_) {
                EntityVillager.LOGGER.warn("PriceRange({}, {}) invalid, {} smaller than {}", (Object)p_i45810_1_, (Object)p_i45810_2_, (Object)p_i45810_2_, (Object)p_i45810_1_);
            }
        }
        
        public int getPrice(final Random rand) {
            return (((Tuple<Integer, B>)this).getFirst() >= ((Tuple<A, Integer>)this).getSecond()) ? ((Tuple<Integer, B>)this).getFirst() : (((Tuple<Integer, B>)this).getFirst() + rand.nextInt(((Tuple<A, Integer>)this).getSecond() - ((Tuple<Integer, B>)this).getFirst() + 1));
        }
    }
    
    static class TreasureMapForEmeralds implements ITradeList
    {
        public PriceInfo value;
        public String destination;
        public MapDecoration.Type destinationType;
        
        public TreasureMapForEmeralds(final PriceInfo p_i47340_1_, final String p_i47340_2_, final MapDecoration.Type p_i47340_3_) {
            this.value = p_i47340_1_;
            this.destination = p_i47340_2_;
            this.destinationType = p_i47340_3_;
        }
        
        @Override
        public void addMerchantRecipe(final IMerchant merchant, final MerchantRecipeList recipeList, final Random random) {
            final int i = this.value.getPrice(random);
            final World world = merchant.getWorld();
            final BlockPos blockpos = world.findNearestStructure(this.destination, merchant.getPos(), true);
            if (blockpos != null) {
                final ItemStack itemstack = ItemMap.setupNewMap(world, blockpos.getX(), blockpos.getZ(), (byte)2, true, true);
                ItemMap.renderBiomePreviewMap(world, itemstack);
                MapData.addTargetDecoration(itemstack, blockpos, "+", this.destinationType);
                itemstack.setTranslatableName("filled_map." + this.destination.toLowerCase(Locale.ROOT));
                recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack));
            }
        }
    }
    
    interface ITradeList
    {
        void addMerchantRecipe(final IMerchant p0, final MerchantRecipeList p1, final Random p2);
    }
}
