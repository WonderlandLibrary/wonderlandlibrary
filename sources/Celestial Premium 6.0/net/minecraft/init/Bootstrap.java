/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.init;

import com.mojang.authlib.GameProfile;
import java.io.PrintStream;
import java.util.Random;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.server.DebugLoggingPrintStream;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bootstrap {
    public static final PrintStream SYSOUT = System.out;
    private static boolean alreadyRegistered;
    public static boolean field_194219_b;
    private static final Logger LOGGER;

    public static boolean isRegistered() {
        return alreadyRegistered;
    }

    static void registerDispenserBehaviors() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ARROW, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.TIPPED_ARROW, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entitytippedarrow.setPotionEffect(stackIn);
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPECTRAL_ARROW, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                EntitySpectralArrow entityarrow = new EntitySpectralArrow(worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.EGG, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SNOWBALL, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.EXPERIENCE_BOTTLE, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
            }

            @Override
            protected float getProjectileInaccuracy() {
                return super.getProjectileInaccuracy() * 0.5f;
            }

            @Override
            protected float getProjectileVelocity() {
                return super.getProjectileVelocity() * 1.25f;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPLASH_POTION, new IBehaviorDispenseItem(){

            @Override
            public ItemStack dispense(IBlockSource source, final ItemStack stack) {
                return new BehaviorProjectileDispense(){

                    @Override
                    protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }

                    @Override
                    protected float getProjectileInaccuracy() {
                        return super.getProjectileInaccuracy() * 0.5f;
                    }

                    @Override
                    protected float getProjectileVelocity() {
                        return super.getProjectileVelocity() * 1.25f;
                    }
                }.dispense(source, stack);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.LINGERING_POTION, new IBehaviorDispenseItem(){

            @Override
            public ItemStack dispense(IBlockSource source, final ItemStack stack) {
                return new BehaviorProjectileDispense(){

                    @Override
                    protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }

                    @Override
                    protected float getProjectileInaccuracy() {
                        return super.getProjectileInaccuracy() * 0.5f;
                    }

                    @Override
                    protected float getProjectileVelocity() {
                        return super.getProjectileVelocity() * 1.25f;
                    }
                }.dispense(source, stack);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPAWN_EGG, new BehaviorDefaultDispenseItem(){

            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                double d0 = source.getX() + (double)enumfacing.getXOffset();
                double d1 = (float)(source.getBlockPos().getY() + enumfacing.getYOffset()) + 0.2f;
                double d2 = source.getZ() + (double)enumfacing.getZOffset();
                Entity entity = ItemMonsterPlacer.spawnCreature(source.getWorld(), ItemMonsterPlacer.func_190908_h(stack), d0, d1, d2);
                if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                    entity.setCustomNameTag(stack.getDisplayName());
                }
                ItemMonsterPlacer.applyItemEntityDataToEntity(source.getWorld(), null, stack, entity);
                stack.func_190918_g(1);
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIREWORKS, new BehaviorDefaultDispenseItem(){

            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                double d0 = source.getX() + (double)enumfacing.getXOffset();
                double d1 = (float)source.getBlockPos().getY() + 0.2f;
                double d2 = source.getZ() + (double)enumfacing.getZOffset();
                EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(source.getWorld(), d0, d1, d2, stack);
                source.getWorld().spawnEntityInWorld(entityfireworkrocket);
                stack.func_190918_g(1);
                return stack;
            }

            @Override
            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playEvent(1004, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIRE_CHARGE, new BehaviorDefaultDispenseItem(){

            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(source);
                double d0 = iposition.getX() + (double)((float)enumfacing.getXOffset() * 0.3f);
                double d1 = iposition.getY() + (double)((float)enumfacing.getYOffset() * 0.3f);
                double d2 = iposition.getZ() + (double)((float)enumfacing.getZOffset() * 0.3f);
                World world = source.getWorld();
                Random random = world.rand;
                double d3 = random.nextGaussian() * 0.05 + (double)enumfacing.getXOffset();
                double d4 = random.nextGaussian() * 0.05 + (double)enumfacing.getYOffset();
                double d5 = random.nextGaussian() * 0.05 + (double)enumfacing.getZOffset();
                world.spawnEntityInWorld(new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5));
                stack.func_190918_g(1);
                return stack;
            }

            @Override
            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playEvent(1018, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BOAT, new BehaviorDispenseBoat(EntityBoat.Type.OAK));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPRUCE_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.SPRUCE));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BIRCH_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.BIRCH));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.JUNGLE_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.JUNGLE));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DARK_OAK_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.DARK_OAK));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ACACIA_BOAT, new BehaviorDispenseBoat(EntityBoat.Type.ACACIA));
        BehaviorDefaultDispenseItem ibehaviordispenseitem = new BehaviorDefaultDispenseItem(){
            private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();

            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                ItemBucket itembucket = (ItemBucket)stack.getItem();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                return itembucket.tryPlaceContainedLiquid(null, source.getWorld(), blockpos) ? new ItemStack(Items.BUCKET) : this.dispenseBehavior.dispense(source, stack);
            }
        };
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.LAVA_BUCKET, ibehaviordispenseitem);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WATER_BUCKET, ibehaviordispenseitem);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BUCKET, new BehaviorDefaultDispenseItem(){
            private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();

            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                Item item;
                World world = source.getWorld();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                Material material = iblockstate.getMaterial();
                if (Material.WATER.equals(material) && block instanceof BlockLiquid && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
                    item = Items.WATER_BUCKET;
                } else {
                    if (!Material.LAVA.equals(material) || !(block instanceof BlockLiquid) || iblockstate.getValue(BlockLiquid.LEVEL) != 0) {
                        return super.dispenseStack(source, stack);
                    }
                    item = Items.LAVA_BUCKET;
                }
                world.setBlockToAir(blockpos);
                stack.func_190918_g(1);
                if (stack.isEmpty()) {
                    return new ItemStack(item);
                }
                if (((TileEntityDispenser)source.getBlockTileEntity()).addItemStack(new ItemStack(item)) < 0) {
                    this.dispenseBehavior.dispense(source, new ItemStack(item));
                }
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FLINT_AND_STEEL, new BehaviorDispenseOptional(){

            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World world = source.getWorld();
                this.field_190911_b = true;
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                if (world.isAirBlock(blockpos)) {
                    world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    if (stack.attemptDamageItem(1, world.rand, null)) {
                        stack.func_190920_e(0);
                    }
                } else if (world.getBlockState(blockpos).getBlock() == Blocks.TNT) {
                    Blocks.TNT.onBlockDestroyedByPlayer(world, blockpos, Blocks.TNT.getDefaultState().withProperty(BlockTNT.EXPLODE, true));
                    world.setBlockToAir(blockpos);
                } else {
                    this.field_190911_b = false;
                }
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DYE, new BehaviorDispenseOptional(){

            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                this.field_190911_b = true;
                if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(stack.getMetadata())) {
                    BlockPos blockpos;
                    World world = source.getWorld();
                    if (ItemDye.applyBonemeal(stack, world, blockpos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING)))) {
                        if (!world.isRemote) {
                            world.playEvent(2005, blockpos, 0);
                        }
                    } else {
                        this.field_190911_b = false;
                    }
                    return stack;
                }
                return super.dispenseStack(source, stack);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.TNT), new BehaviorDefaultDispenseItem(){

            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World world = source.getWorld();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double)blockpos.getX() + 0.5, blockpos.getY(), (double)blockpos.getZ() + 0.5, null);
                world.spawnEntityInWorld(entitytntprimed);
                world.playSound(null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                stack.func_190918_g(1);
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SKULL, new BehaviorDispenseOptional(){

            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World world = source.getWorld();
                EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
                BlockPos blockpos = source.getBlockPos().offset(enumfacing);
                BlockSkull blockskull = Blocks.SKULL;
                this.field_190911_b = true;
                if (world.isAirBlock(blockpos) && blockskull.canDispenserPlace(world, blockpos, stack)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockpos, blockskull.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP), 3);
                        TileEntity tileentity = world.getTileEntity(blockpos);
                        if (tileentity instanceof TileEntitySkull) {
                            if (stack.getMetadata() == 3) {
                                GameProfile gameprofile = null;
                                if (stack.hasTagCompound()) {
                                    String s;
                                    NBTTagCompound nbttagcompound = stack.getTagCompound();
                                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                                    } else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(s = nbttagcompound.getString("SkullOwner"))) {
                                        gameprofile = new GameProfile(null, s);
                                    }
                                }
                                ((TileEntitySkull)tileentity).setPlayerProfile(gameprofile);
                            } else {
                                ((TileEntitySkull)tileentity).setType(stack.getMetadata());
                            }
                            ((TileEntitySkull)tileentity).setSkullRotation(enumfacing.getOpposite().getHorizontalIndex() * 4);
                            Blocks.SKULL.checkWitherSpawn(world, blockpos, (TileEntitySkull)tileentity);
                        }
                        stack.func_190918_g(1);
                    }
                } else if (ItemArmor.dispenseArmor(source, stack).isEmpty()) {
                    this.field_190911_b = false;
                }
                return stack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.PUMPKIN), new BehaviorDispenseOptional(){

            @Override
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World world = source.getWorld();
                BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                BlockPumpkin blockpumpkin = (BlockPumpkin)Blocks.PUMPKIN;
                this.field_190911_b = true;
                if (world.isAirBlock(blockpos) && blockpumpkin.canDispenserPlace(world, blockpos)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockpos, blockpumpkin.getDefaultState(), 3);
                    }
                    stack.func_190918_g(1);
                } else {
                    ItemStack itemstack = ItemArmor.dispenseArmor(source, stack);
                    if (itemstack.isEmpty()) {
                        this.field_190911_b = false;
                    }
                }
                return stack;
            }
        });
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(BlockShulkerBox.func_190952_a(enumdyecolor)), new BehaviorDispenseShulkerBox());
        }
    }

    public static void register() {
        if (!alreadyRegistered) {
            alreadyRegistered = true;
            Bootstrap.redirectOutputToLog();
            SoundEvent.registerSounds();
            Block.registerBlocks();
            BlockFire.init();
            Potion.registerPotions();
            Enchantment.registerEnchantments();
            Item.registerItems();
            PotionType.registerPotionTypes();
            PotionHelper.init();
            EntityList.init();
            Biome.registerBiomes();
            Bootstrap.registerDispenserBehaviors();
            if (!CraftingManager.func_193377_a()) {
                field_194219_b = true;
                LOGGER.error("Errors with built-in recipes!");
            }
            StatList.init();
            if (LOGGER.isDebugEnabled()) {
                if (new AdvancementManager(null).func_193767_b()) {
                    field_194219_b = true;
                    LOGGER.error("Errors with built-in advancements!");
                }
                if (!LootTableList.func_193579_b()) {
                    field_194219_b = true;
                    LOGGER.error("Errors with built-in loot tables");
                }
            }
        }
    }

    private static void redirectOutputToLog() {
        if (LOGGER.isDebugEnabled()) {
            System.setErr(new DebugLoggingPrintStream("STDERR", System.err));
            System.setOut(new DebugLoggingPrintStream("STDOUT", SYSOUT));
        } else {
            System.setErr(new LoggingPrintStream("STDERR", System.err));
            System.setOut(new LoggingPrintStream("STDOUT", SYSOUT));
        }
    }

    public static void printToSYSOUT(String message) {
        SYSOUT.println(message);
    }

    static {
        LOGGER = LogManager.getLogger();
    }

    static class BehaviorDispenseShulkerBox
    extends BehaviorDispenseOptional {
        private BehaviorDispenseShulkerBox() {
        }

        @Override
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            Block block = Block.getBlockFromItem(stack.getItem());
            World world = source.getWorld();
            EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
            BlockPos blockpos = source.getBlockPos().offset(enumfacing);
            this.field_190911_b = world.mayPlace(block, blockpos, false, EnumFacing.DOWN, null);
            if (this.field_190911_b) {
                EnumFacing enumfacing1 = world.isAirBlock(blockpos.down()) ? enumfacing : EnumFacing.UP;
                IBlockState iblockstate = block.getDefaultState().withProperty(BlockShulkerBox.field_190957_a, enumfacing1);
                world.setBlockState(blockpos, iblockstate);
                TileEntity tileentity = world.getTileEntity(blockpos);
                ItemStack itemstack = stack.splitStack(1);
                if (itemstack.hasTagCompound()) {
                    ((TileEntityShulkerBox)tileentity).func_190586_e(itemstack.getTagCompound().getCompoundTag("BlockEntityTag"));
                }
                if (itemstack.hasDisplayName()) {
                    ((TileEntityShulkerBox)tileentity).func_190575_a(itemstack.getDisplayName());
                }
                world.updateComparatorOutputLevel(blockpos, iblockstate.getBlock());
            }
            return stack;
        }
    }

    public static abstract class BehaviorDispenseOptional
    extends BehaviorDefaultDispenseItem {
        protected boolean field_190911_b = true;

        @Override
        protected void playDispenseSound(IBlockSource source) {
            source.getWorld().playEvent(this.field_190911_b ? 1000 : 1001, source.getBlockPos(), 0);
        }
    }

    public static class BehaviorDispenseBoat
    extends BehaviorDefaultDispenseItem {
        private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();
        private final EntityBoat.Type boatType;

        public BehaviorDispenseBoat(EntityBoat.Type boatTypeIn) {
            this.boatType = boatTypeIn;
        }

        @Override
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            double d3;
            EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
            World world = source.getWorld();
            double d0 = source.getX() + (double)((float)enumfacing.getXOffset() * 1.125f);
            double d1 = source.getY() + (double)((float)enumfacing.getYOffset() * 1.125f);
            double d2 = source.getZ() + (double)((float)enumfacing.getZOffset() * 1.125f);
            BlockPos blockpos = source.getBlockPos().offset(enumfacing);
            Material material = world.getBlockState(blockpos).getMaterial();
            if (Material.WATER.equals(material)) {
                d3 = 1.0;
            } else {
                if (!Material.AIR.equals(material) || !Material.WATER.equals(world.getBlockState(blockpos.down()).getMaterial())) {
                    return this.dispenseBehavior.dispense(source, stack);
                }
                d3 = 0.0;
            }
            EntityBoat entityboat = new EntityBoat(world, d0, d1 + d3, d2);
            entityboat.setBoatType(this.boatType);
            entityboat.rotationYaw = enumfacing.getHorizontalAngle();
            world.spawnEntityInWorld(entityboat);
            stack.func_190918_g(1);
            return stack;
        }

        @Override
        protected void playDispenseSound(IBlockSource source) {
            source.getWorld().playEvent(1000, source.getBlockPos(), 0);
        }
    }
}

