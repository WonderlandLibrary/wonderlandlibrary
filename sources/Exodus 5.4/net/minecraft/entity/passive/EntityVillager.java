/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
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
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityVillager
extends EntityAgeable
implements IMerchant,
INpc {
    private EntityPlayer buyingPlayer;
    private int wealth;
    private int randomTickDivider;
    private static final ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new ITradeList[][][][]{{{{new EmeraldForItems(Items.wheat, new PriceInfo(18, 22)), new EmeraldForItems(Items.potato, new PriceInfo(15, 19)), new EmeraldForItems(Items.carrot, new PriceInfo(15, 19)), new ListItemForEmeralds(Items.bread, new PriceInfo(-4, -2))}, {new EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin), new PriceInfo(8, 13)), new ListItemForEmeralds(Items.pumpkin_pie, new PriceInfo(-3, -2))}, {new EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block), new PriceInfo(7, 12)), new ListItemForEmeralds(Items.apple, new PriceInfo(-5, -7))}, {new ListItemForEmeralds(Items.cookie, new PriceInfo(-6, -10)), new ListItemForEmeralds(Items.cake, new PriceInfo(1, 1))}}, {{new EmeraldForItems(Items.string, new PriceInfo(15, 20)), new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ItemAndEmeraldToItem(Items.fish, new PriceInfo(6, 6), Items.cooked_fish, new PriceInfo(6, 6))}, {new ListEnchantedItemForEmeralds(Items.fishing_rod, new PriceInfo(7, 8))}}, {{new EmeraldForItems(Item.getItemFromBlock(Blocks.wool), new PriceInfo(16, 22)), new ListItemForEmeralds(Items.shears, new PriceInfo(3, 4))}, {new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15), new PriceInfo(1, 2))}}, {{new EmeraldForItems(Items.string, new PriceInfo(15, 20)), new ListItemForEmeralds(Items.arrow, new PriceInfo(-12, -8))}, {new ListItemForEmeralds(Items.bow, new PriceInfo(2, 3)), new ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel), new PriceInfo(10, 10), Items.flint, new PriceInfo(6, 10))}}}, {{{new EmeraldForItems(Items.paper, new PriceInfo(24, 36)), new ListEnchantedBookForEmeralds()}, {new EmeraldForItems(Items.book, new PriceInfo(8, 10)), new ListItemForEmeralds(Items.compass, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf), new PriceInfo(3, 4))}, {new EmeraldForItems(Items.written_book, new PriceInfo(2, 2)), new ListItemForEmeralds(Items.clock, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass), new PriceInfo(-5, -3))}, {new ListEnchantedBookForEmeralds()}, {new ListEnchantedBookForEmeralds()}, {new ListItemForEmeralds(Items.name_tag, new PriceInfo(20, 22))}}}, {{{new EmeraldForItems(Items.rotten_flesh, new PriceInfo(36, 40)), new EmeraldForItems(Items.gold_ingot, new PriceInfo(8, 10))}, {new ListItemForEmeralds(Items.redstone, new PriceInfo(-4, -1)), new ListItemForEmeralds(new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(-2, -1))}, {new ListItemForEmeralds(Items.ender_eye, new PriceInfo(7, 11)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone), new PriceInfo(-3, -1))}, {new ListItemForEmeralds(Items.experience_bottle, new PriceInfo(3, 11))}}}, {{{new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.iron_helmet, new PriceInfo(4, 6))}, {new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListItemForEmeralds(Items.iron_chestplate, new PriceInfo(10, 14))}, {new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_chestplate, new PriceInfo(16, 19))}, {new ListItemForEmeralds(Items.chainmail_boots, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.chainmail_leggings, new PriceInfo(9, 11)), new ListItemForEmeralds(Items.chainmail_helmet, new PriceInfo(5, 7)), new ListItemForEmeralds(Items.chainmail_chestplate, new PriceInfo(11, 15))}}, {{new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.iron_axe, new PriceInfo(6, 8))}, {new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.iron_sword, new PriceInfo(9, 10))}, {new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_sword, new PriceInfo(12, 15)), new ListEnchantedItemForEmeralds(Items.diamond_axe, new PriceInfo(9, 12))}}, {{new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListEnchantedItemForEmeralds(Items.iron_shovel, new PriceInfo(5, 7))}, {new EmeraldForItems(Items.iron_ingot, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.iron_pickaxe, new PriceInfo(9, 11))}, {new EmeraldForItems(Items.diamond, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.diamond_pickaxe, new PriceInfo(12, 15))}}}, {{{new EmeraldForItems(Items.porkchop, new PriceInfo(14, 18)), new EmeraldForItems(Items.chicken, new PriceInfo(14, 18))}, {new EmeraldForItems(Items.coal, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.cooked_porkchop, new PriceInfo(-7, -5)), new ListItemForEmeralds(Items.cooked_chicken, new PriceInfo(-8, -6))}}, {{new EmeraldForItems(Items.leather, new PriceInfo(9, 12)), new ListItemForEmeralds(Items.leather_leggings, new PriceInfo(2, 4))}, {new ListEnchantedItemForEmeralds(Items.leather_chestplate, new PriceInfo(7, 12))}, {new ListItemForEmeralds(Items.saddle, new PriceInfo(8, 10))}}}};
    private boolean needsInitilization;
    private MerchantRecipeList buyingList;
    private String lastBuyingPlayer;
    private int timeUntilReset;
    private boolean isMating;
    private boolean isWillingToMate;
    private boolean areAdditionalTasksSet;
    private int careerLevel;
    private InventoryBasic villagerInventory = new InventoryBasic("Items", false, 8);
    Village villageObj;
    private boolean isPlaying;
    private int careerId;
    private boolean isLookingForHome;

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5);
    }

    public boolean isMating() {
        return this.isMating;
    }

    @Override
    public MerchantRecipeList getRecipes(EntityPlayer entityPlayer) {
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        return this.buyingList;
    }

    public void setPlaying(boolean bl) {
        this.isPlaying = bl;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, 0);
    }

    @Override
    public boolean allowLeashing() {
        return false;
    }

    @Override
    public void verifySellingItem(ItemStack itemStack) {
        if (!this.worldObj.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
            if (itemStack != null) {
                this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
            } else {
                this.playSound("mob.villager.no", this.getSoundVolume(), this.getSoundPitch());
            }
        }
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public boolean getIsWillingToMate(boolean bl) {
        if (!this.isWillingToMate && bl && this.func_175553_cp()) {
            boolean bl2 = false;
            int n = 0;
            while (n < this.villagerInventory.getSizeInventory()) {
                ItemStack itemStack = this.villagerInventory.getStackInSlot(n);
                if (itemStack != null) {
                    if (itemStack.getItem() == Items.bread && itemStack.stackSize >= 3) {
                        bl2 = true;
                        this.villagerInventory.decrStackSize(n, 3);
                    } else if ((itemStack.getItem() == Items.potato || itemStack.getItem() == Items.carrot) && itemStack.stackSize >= 12) {
                        bl2 = true;
                        this.villagerInventory.decrStackSize(n, 12);
                    }
                }
                if (bl2) {
                    this.worldObj.setEntityState(this, (byte)18);
                    this.isWillingToMate = true;
                    break;
                }
                ++n;
            }
        }
        return this.isWillingToMate;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        NBTBase nBTBase;
        super.readEntityFromNBT(nBTTagCompound);
        this.setProfession(nBTTagCompound.getInteger("Profession"));
        this.wealth = nBTTagCompound.getInteger("Riches");
        this.careerId = nBTTagCompound.getInteger("Career");
        this.careerLevel = nBTTagCompound.getInteger("CareerLevel");
        this.isWillingToMate = nBTTagCompound.getBoolean("Willing");
        if (nBTTagCompound.hasKey("Offers", 10)) {
            nBTBase = nBTTagCompound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList((NBTTagCompound)nBTBase);
        }
        nBTBase = nBTTagCompound.getTagList("Inventory", 10);
        int n = 0;
        while (n < ((NBTTagList)nBTBase).tagCount()) {
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(((NBTTagList)nBTBase).getCompoundTagAt(n));
            if (itemStack != null) {
                this.villagerInventory.func_174894_a(itemStack);
            }
            ++n;
        }
        this.setCanPickUpLoot(true);
        this.setAdditionalAItasks();
    }

    public void setLookingForHome() {
        this.isLookingForHome = true;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        iEntityLivingData = super.onInitialSpawn(difficultyInstance, iEntityLivingData);
        this.setProfession(this.worldObj.rand.nextInt(5));
        this.setAdditionalAItasks();
        return iEntityLivingData;
    }

    public void setIsWillingToMate(boolean bl) {
        this.isWillingToMate = bl;
    }

    public void setProfession(int n) {
        this.dataWatcher.updateObject(16, n);
    }

    @Override
    public EntityVillager createChild(EntityAgeable entityAgeable) {
        EntityVillager entityVillager = new EntityVillager(this.worldObj);
        entityVillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityVillager)), null);
        return entityVillager;
    }

    @Override
    public boolean replaceItemInInventory(int n, ItemStack itemStack) {
        if (super.replaceItemInInventory(n, itemStack)) {
            return true;
        }
        int n2 = n - 300;
        if (n2 >= 0 && n2 < this.villagerInventory.getSizeInventory()) {
            this.villagerInventory.setInventorySlotContents(n2, itemStack);
            return true;
        }
        return false;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (this.villageObj != null) {
            Entity entity = damageSource.getEntity();
            if (entity != null) {
                if (entity instanceof EntityPlayer) {
                    this.villageObj.setReputationForPlayer(entity.getName(), -2);
                } else if (entity instanceof IMob) {
                    this.villageObj.endMatingSeason();
                }
            } else {
                EntityPlayer entityPlayer = this.worldObj.getClosestPlayerToEntity(this, 16.0);
                if (entityPlayer != null) {
                    this.villageObj.endMatingSeason();
                }
            }
        }
        super.onDeath(damageSource);
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        boolean bl;
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        boolean bl2 = bl = itemStack != null && itemStack.getItem() == Items.spawn_egg;
        if (!bl && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
            if (!(this.worldObj.isRemote || this.buyingList != null && this.buyingList.size() <= 0)) {
                this.setCustomer(entityPlayer);
                entityPlayer.displayVillagerTradeGui(this);
            }
            entityPlayer.triggerAchievement(StatList.timesTalkedToVillagerStat);
            return true;
        }
        return super.interact(entityPlayer);
    }

    public boolean func_175557_cr() {
        boolean bl;
        boolean bl2 = bl = this.getProfession() == 0;
        return bl ? !this.hasEnoughItems(5) : !this.hasEnoughItems(1);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected String getDeathSound() {
        return "mob.villager.death";
    }

    public EntityVillager(World world, int n) {
        super(world);
        this.setProfession(n);
        this.setSize(0.6f, 1.8f);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<EntityZombie>(this, EntityZombie.class, 8.0f, 0.6, 0.6));
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
        this.tasks.addTask(9, new EntityAIWander(this, 0.6));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
        this.setCanPickUpLoot(true);
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 12) {
            this.spawnParticles(EnumParticleTypes.HEART);
        } else if (by == 13) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        } else if (by == 14) {
            this.spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    protected void updateAITasks() {
        if (--this.randomTickDivider <= 0) {
            Object object = new BlockPos(this);
            this.worldObj.getVillageCollection().addToVillagerPositionList((BlockPos)object);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.villageObj = this.worldObj.getVillageCollection().getNearestVillage((BlockPos)object, 32);
            if (this.villageObj == null) {
                this.detachHome();
            } else {
                BlockPos blockPos = this.villageObj.getCenter();
                this.setHomePosAndDistance(blockPos, (int)((float)this.villageObj.getVillageRadius() * 1.0f));
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
                    for (Object object : this.buyingList) {
                        if (!((MerchantRecipe)object).isRecipeDisabled()) continue;
                        ((MerchantRecipe)object).increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                    }
                    this.populateBuyingList();
                    this.needsInitilization = false;
                    if (this.villageObj != null && this.lastBuyingPlayer != null) {
                        this.worldObj.setEntityState(this, (byte)14);
                        this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
                    }
                }
                this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
            }
        }
        super.updateAITasks();
    }

    @Override
    public void useRecipe(MerchantRecipe merchantRecipe) {
        merchantRecipe.incrementToolUses();
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound("mob.villager.yes", this.getSoundVolume(), this.getSoundPitch());
        int n = 3 + this.rand.nextInt(4);
        if (merchantRecipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
            this.timeUntilReset = 40;
            this.needsInitilization = true;
            this.isWillingToMate = true;
            this.lastBuyingPlayer = this.buyingPlayer != null ? this.buyingPlayer.getName() : null;
            n += 5;
        }
        if (merchantRecipe.getItemToBuy().getItem() == Items.emerald) {
            this.wealth += merchantRecipe.getItemToBuy().stackSize;
        }
        if (merchantRecipe.getRewardsExp()) {
            this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + 0.5, this.posZ, n));
        }
    }

    public EntityVillager(World world) {
        this(world, 0);
    }

    private boolean hasEnoughItems(int n) {
        boolean bl = this.getProfession() == 0;
        int n2 = 0;
        while (n2 < this.villagerInventory.getSizeInventory()) {
            ItemStack itemStack = this.villagerInventory.getStackInSlot(n2);
            if (itemStack != null) {
                if (itemStack.getItem() == Items.bread && itemStack.stackSize >= 3 * n || itemStack.getItem() == Items.potato && itemStack.stackSize >= 12 * n || itemStack.getItem() == Items.carrot && itemStack.stackSize >= 12 * n) {
                    return true;
                }
                if (bl && itemStack.getItem() == Items.wheat && itemStack.stackSize >= 9 * n) {
                    return true;
                }
            }
            ++n2;
        }
        return false;
    }

    @Override
    public void setRecipes(MerchantRecipeList merchantRecipeList) {
    }

    @Override
    protected void updateEquipmentIfNeeded(EntityItem entityItem) {
        ItemStack itemStack = entityItem.getEntityItem();
        Item item = itemStack.getItem();
        if (this.canVillagerPickupItem(item)) {
            ItemStack itemStack2 = this.villagerInventory.func_174894_a(itemStack);
            if (itemStack2 == null) {
                entityItem.setDead();
            } else {
                itemStack.stackSize = itemStack2.stackSize;
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("Profession", this.getProfession());
        nBTTagCompound.setInteger("Riches", this.wealth);
        nBTTagCompound.setInteger("Career", this.careerId);
        nBTTagCompound.setInteger("CareerLevel", this.careerLevel);
        nBTTagCompound.setBoolean("Willing", this.isWillingToMate);
        if (this.buyingList != null) {
            nBTTagCompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.villagerInventory.getSizeInventory()) {
            ItemStack itemStack = this.villagerInventory.getStackInSlot(n);
            if (itemStack != null) {
                nBTTagList.appendTag(itemStack.writeToNBT(new NBTTagCompound()));
            }
            ++n;
        }
        nBTTagCompound.setTag("Inventory", nBTTagList);
    }

    @Override
    protected String getHurtSound() {
        return "mob.villager.hit";
    }

    public int getProfession() {
        return Math.max(this.dataWatcher.getWatchableObjectInt(16) % 5, 0);
    }

    public boolean isFarmItemInInventory() {
        int n = 0;
        while (n < this.villagerInventory.getSizeInventory()) {
            ItemStack itemStack = this.villagerInventory.getStackInSlot(n);
            if (itemStack != null && (itemStack.getItem() == Items.wheat_seeds || itemStack.getItem() == Items.potato || itemStack.getItem() == Items.carrot)) {
                return true;
            }
            ++n;
        }
        return false;
    }

    private void populateBuyingList() {
        ITradeList[][][] iTradeListArray = DEFAULT_TRADE_LIST_MAP[this.getProfession()];
        if (this.careerId != 0 && this.careerLevel != 0) {
            ++this.careerLevel;
        } else {
            this.careerId = this.rand.nextInt(iTradeListArray.length) + 1;
            this.careerLevel = 1;
        }
        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }
        int n = this.careerId - 1;
        int n2 = this.careerLevel - 1;
        ITradeList[][] iTradeListArray2 = iTradeListArray[n];
        if (n2 >= 0 && n2 < iTradeListArray2.length) {
            ITradeList[] iTradeListArray3;
            ITradeList[] iTradeListArray4 = iTradeListArray3 = iTradeListArray2[n2];
            int n3 = iTradeListArray3.length;
            int n4 = 0;
            while (n4 < n3) {
                ITradeList iTradeList = iTradeListArray4[n4];
                iTradeList.modifyMerchantRecipeList(this.buyingList, this.rand);
                ++n4;
            }
        }
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt entityLightningBolt) {
        if (!this.worldObj.isRemote && !this.isDead) {
            EntityWitch entityWitch = new EntityWitch(this.worldObj);
            entityWitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entityWitch.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(entityWitch)), null);
            entityWitch.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entityWitch.setCustomNameTag(this.getCustomNameTag());
                entityWitch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.worldObj.spawnEntityInWorld(entityWitch);
            this.setDead();
        }
    }

    @Override
    public void setCustomer(EntityPlayer entityPlayer) {
        this.buyingPlayer = entityPlayer;
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
    public float getEyeHeight() {
        float f = 1.62f;
        if (this.isChild()) {
            f = (float)((double)f - 0.81);
        }
        return f;
    }

    public boolean func_175553_cp() {
        return this.hasEnoughItems(1);
    }

    private void spawnParticles(EnumParticleTypes enumParticleTypes) {
        int n = 0;
        while (n < 5) {
            double d = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            double d3 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle(enumParticleTypes, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 1.0 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d, d2, d3, new int[0]);
            ++n;
        }
    }

    public void setMating(boolean bl) {
        this.isMating = bl;
    }

    @Override
    protected String getLivingSound() {
        return this.isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
    }

    private boolean canVillagerPickupItem(Item item) {
        return item == Items.bread || item == Items.potato || item == Items.carrot || item == Items.wheat || item == Items.wheat_seeds;
    }

    @Override
    public EntityPlayer getCustomer() {
        return this.buyingPlayer;
    }

    public boolean canAbondonItems() {
        return this.hasEnoughItems(2);
    }

    @Override
    protected void onGrowingAdult() {
        if (this.getProfession() == 0) {
            this.tasks.addTask(8, new EntityAIHarvestFarmland(this, 0.6));
        }
        super.onGrowingAdult();
    }

    public boolean isTrading() {
        return this.buyingPlayer != null;
    }

    @Override
    public IChatComponent getDisplayName() {
        String string = this.getCustomNameTag();
        if (string != null && string.length() > 0) {
            ChatComponentText chatComponentText = new ChatComponentText(string);
            chatComponentText.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatComponentText.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatComponentText;
        }
        if (this.buyingList == null) {
            this.populateBuyingList();
        }
        String string2 = null;
        switch (this.getProfession()) {
            case 0: {
                if (this.careerId == 1) {
                    string2 = "farmer";
                    break;
                }
                if (this.careerId == 2) {
                    string2 = "fisherman";
                    break;
                }
                if (this.careerId == 3) {
                    string2 = "shepherd";
                    break;
                }
                if (this.careerId != 4) break;
                string2 = "fletcher";
                break;
            }
            case 1: {
                string2 = "librarian";
                break;
            }
            case 2: {
                string2 = "cleric";
                break;
            }
            case 3: {
                if (this.careerId == 1) {
                    string2 = "armor";
                    break;
                }
                if (this.careerId == 2) {
                    string2 = "weapon";
                    break;
                }
                if (this.careerId != 3) break;
                string2 = "tool";
                break;
            }
            case 4: {
                if (this.careerId == 1) {
                    string2 = "butcher";
                    break;
                }
                if (this.careerId != 2) break;
                string2 = "leather";
            }
        }
        if (string2 != null) {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("entity.Villager." + string2, new Object[0]);
            chatComponentTranslation.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatComponentTranslation.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatComponentTranslation;
        }
        return super.getDisplayName();
    }

    public InventoryBasic getVillagerInventory() {
        return this.villagerInventory;
    }

    @Override
    public void setRevengeTarget(EntityLivingBase entityLivingBase) {
        super.setRevengeTarget(entityLivingBase);
        if (this.villageObj != null && entityLivingBase != null) {
            this.villageObj.addOrRenewAgressor(entityLivingBase);
            if (entityLivingBase instanceof EntityPlayer) {
                int n = -1;
                if (this.isChild()) {
                    n = -3;
                }
                this.villageObj.setReputationForPlayer(entityLivingBase.getName(), n);
                if (this.isEntityAlive()) {
                    this.worldObj.setEntityState(this, (byte)13);
                }
            }
        }
    }

    static interface ITradeList {
        public void modifyMerchantRecipeList(MerchantRecipeList var1, Random var2);
    }

    static class ListEnchantedItemForEmeralds
    implements ITradeList {
        public ItemStack field_179407_a;
        public PriceInfo field_179406_b;

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) {
            int n = 1;
            if (this.field_179406_b != null) {
                n = this.field_179406_b.getPrice(random);
            }
            ItemStack itemStack = new ItemStack(Items.emerald, n, 0);
            ItemStack itemStack2 = new ItemStack(this.field_179407_a.getItem(), 1, this.field_179407_a.getMetadata());
            itemStack2 = EnchantmentHelper.addRandomEnchantment(random, itemStack2, 5 + random.nextInt(15));
            merchantRecipeList.add(new MerchantRecipe(itemStack, itemStack2));
        }

        public ListEnchantedItemForEmeralds(Item item, PriceInfo priceInfo) {
            this.field_179407_a = new ItemStack(item);
            this.field_179406_b = priceInfo;
        }
    }

    static class ListItemForEmeralds
    implements ITradeList {
        public PriceInfo field_179402_b;
        public ItemStack field_179403_a;

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) {
            ItemStack itemStack;
            ItemStack itemStack2;
            int n = 1;
            if (this.field_179402_b != null) {
                n = this.field_179402_b.getPrice(random);
            }
            if (n < 0) {
                itemStack2 = new ItemStack(Items.emerald, 1, 0);
                itemStack = new ItemStack(this.field_179403_a.getItem(), -n, this.field_179403_a.getMetadata());
            } else {
                itemStack2 = new ItemStack(Items.emerald, n, 0);
                itemStack = new ItemStack(this.field_179403_a.getItem(), 1, this.field_179403_a.getMetadata());
            }
            merchantRecipeList.add(new MerchantRecipe(itemStack2, itemStack));
        }

        public ListItemForEmeralds(Item item, PriceInfo priceInfo) {
            this.field_179403_a = new ItemStack(item);
            this.field_179402_b = priceInfo;
        }

        public ListItemForEmeralds(ItemStack itemStack, PriceInfo priceInfo) {
            this.field_179403_a = itemStack;
            this.field_179402_b = priceInfo;
        }
    }

    static class ItemAndEmeraldToItem
    implements ITradeList {
        public PriceInfo field_179408_d;
        public PriceInfo field_179409_b;
        public ItemStack field_179410_c;
        public ItemStack field_179411_a;

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) {
            int n = 1;
            if (this.field_179409_b != null) {
                n = this.field_179409_b.getPrice(random);
            }
            int n2 = 1;
            if (this.field_179408_d != null) {
                n2 = this.field_179408_d.getPrice(random);
            }
            merchantRecipeList.add(new MerchantRecipe(new ItemStack(this.field_179411_a.getItem(), n, this.field_179411_a.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.field_179410_c.getItem(), n2, this.field_179410_c.getMetadata())));
        }

        public ItemAndEmeraldToItem(Item item, PriceInfo priceInfo, Item item2, PriceInfo priceInfo2) {
            this.field_179411_a = new ItemStack(item);
            this.field_179409_b = priceInfo;
            this.field_179410_c = new ItemStack(item2);
            this.field_179408_d = priceInfo2;
        }
    }

    static class ListEnchantedBookForEmeralds
    implements ITradeList {
        ListEnchantedBookForEmeralds() {
        }

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) {
            Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
            int n = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemStack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, n));
            int n2 = 2 + random.nextInt(5 + n * 10) + 3 * n;
            if (n2 > 64) {
                n2 = 64;
            }
            merchantRecipeList.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, n2), itemStack));
        }
    }

    static class PriceInfo
    extends Tuple<Integer, Integer> {
        public int getPrice(Random random) {
            return (Integer)this.getFirst() >= (Integer)this.getSecond() ? (Integer)this.getFirst() : (Integer)this.getFirst() + random.nextInt((Integer)this.getSecond() - (Integer)this.getFirst() + 1);
        }

        public PriceInfo(int n, int n2) {
            super(n, n2);
        }
    }

    static class EmeraldForItems
    implements ITradeList {
        public PriceInfo price;
        public Item sellItem;

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList merchantRecipeList, Random random) {
            int n = 1;
            if (this.price != null) {
                n = this.price.getPrice(random);
            }
            merchantRecipeList.add(new MerchantRecipe(new ItemStack(this.sellItem, n, 0), Items.emerald));
        }

        public EmeraldForItems(Item item, PriceInfo priceInfo) {
            this.sellItem = item;
            this.price = priceInfo;
        }
    }
}

