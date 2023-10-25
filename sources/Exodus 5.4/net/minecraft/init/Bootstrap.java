/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.init;

import com.mojang.authlib.GameProfile;
import java.io.PrintStream;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bootstrap {
    private static final PrintStream SYSOUT = System.out;
    private static final Logger LOGGER;
    private static boolean alreadyRegistered;

    public static void register() {
        if (!alreadyRegistered) {
            alreadyRegistered = true;
            if (LOGGER.isDebugEnabled()) {
                Bootstrap.redirectOutputToLog();
            }
            Block.registerBlocks();
            BlockFire.init();
            Item.registerItems();
            StatList.init();
            Bootstrap.registerDispenserBehaviors();
        }
    }

    public static void printToSYSOUT(String string) {
        SYSOUT.println(string);
    }

    static {
        alreadyRegistered = false;
        LOGGER = LogManager.getLogger();
    }

    private static void redirectOutputToLog() {
        System.setErr(new LoggingPrintStream("STDERR", System.err));
        System.setOut(new LoggingPrintStream("STDOUT", SYSOUT));
    }

    static void registerDispenserBehaviors() {
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World world, IPosition iPosition) {
                EntityArrow entityArrow = new EntityArrow(world, iPosition.getX(), iPosition.getY(), iPosition.getZ());
                entityArrow.canBePickedUp = 1;
                return entityArrow;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World world, IPosition iPosition) {
                return new EntityEgg(world, iPosition.getX(), iPosition.getY(), iPosition.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense(){

            @Override
            protected IProjectile getProjectileEntity(World world, IPosition iPosition) {
                return new EntitySnowball(world, iPosition.getX(), iPosition.getY(), iPosition.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense(){

            @Override
            protected float func_82500_b() {
                return super.func_82500_b() * 1.25f;
            }

            @Override
            protected float func_82498_a() {
                return super.func_82498_a() * 0.5f;
            }

            @Override
            protected IProjectile getProjectileEntity(World world, IPosition iPosition) {
                return new EntityExpBottle(world, iPosition.getX(), iPosition.getY(), iPosition.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem(){
            private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();

            @Override
            public ItemStack dispense(IBlockSource iBlockSource, final ItemStack itemStack) {
                return ItemPotion.isSplash(itemStack.getMetadata()) ? new BehaviorProjectileDispense(){

                    @Override
                    protected float func_82498_a() {
                        return super.func_82498_a() * 0.5f;
                    }

                    @Override
                    protected float func_82500_b() {
                        return super.func_82500_b() * 1.25f;
                    }

                    @Override
                    protected IProjectile getProjectileEntity(World world, IPosition iPosition) {
                        return new EntityPotion(world, iPosition.getX(), iPosition.getY(), iPosition.getZ(), itemStack.copy());
                    }
                }.dispense(iBlockSource, itemStack) : this.field_150843_b.dispense(iBlockSource, itemStack);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem(){

            @Override
            public ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                EnumFacing enumFacing = BlockDispenser.getFacing(iBlockSource.getBlockMetadata());
                double d = iBlockSource.getX() + (double)enumFacing.getFrontOffsetX();
                double d2 = (float)iBlockSource.getBlockPos().getY() + 0.2f;
                double d3 = iBlockSource.getZ() + (double)enumFacing.getFrontOffsetZ();
                Entity entity = ItemMonsterPlacer.spawnCreature(iBlockSource.getWorld(), itemStack.getMetadata(), d, d2, d3);
                if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
                    ((EntityLiving)entity).setCustomNameTag(itemStack.getDisplayName());
                }
                itemStack.splitStack(1);
                return itemStack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem(){

            @Override
            public ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                EnumFacing enumFacing = BlockDispenser.getFacing(iBlockSource.getBlockMetadata());
                double d = iBlockSource.getX() + (double)enumFacing.getFrontOffsetX();
                double d2 = (float)iBlockSource.getBlockPos().getY() + 0.2f;
                double d3 = iBlockSource.getZ() + (double)enumFacing.getFrontOffsetZ();
                EntityFireworkRocket entityFireworkRocket = new EntityFireworkRocket(iBlockSource.getWorld(), d, d2, d3, itemStack);
                iBlockSource.getWorld().spawnEntityInWorld(entityFireworkRocket);
                itemStack.splitStack(1);
                return itemStack;
            }

            @Override
            protected void playDispenseSound(IBlockSource iBlockSource) {
                iBlockSource.getWorld().playAuxSFX(1002, iBlockSource.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem(){

            @Override
            protected void playDispenseSound(IBlockSource iBlockSource) {
                iBlockSource.getWorld().playAuxSFX(1009, iBlockSource.getBlockPos(), 0);
            }

            @Override
            public ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                EnumFacing enumFacing = BlockDispenser.getFacing(iBlockSource.getBlockMetadata());
                IPosition iPosition = BlockDispenser.getDispensePosition(iBlockSource);
                double d = iPosition.getX() + (double)((float)enumFacing.getFrontOffsetX() * 0.3f);
                double d2 = iPosition.getY() + (double)((float)enumFacing.getFrontOffsetY() * 0.3f);
                double d3 = iPosition.getZ() + (double)((float)enumFacing.getFrontOffsetZ() * 0.3f);
                World world = iBlockSource.getWorld();
                Random random = world.rand;
                double d4 = random.nextGaussian() * 0.05 + (double)enumFacing.getFrontOffsetX();
                double d5 = random.nextGaussian() * 0.05 + (double)enumFacing.getFrontOffsetY();
                double d6 = random.nextGaussian() * 0.05 + (double)enumFacing.getFrontOffsetZ();
                world.spawnEntityInWorld(new EntitySmallFireball(world, d, d2, d3, d4, d5, d6));
                itemStack.splitStack(1);
                return itemStack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem(){
            private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();

            @Override
            protected void playDispenseSound(IBlockSource iBlockSource) {
                iBlockSource.getWorld().playAuxSFX(1000, iBlockSource.getBlockPos(), 0);
            }

            @Override
            public ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                double d;
                EnumFacing enumFacing = BlockDispenser.getFacing(iBlockSource.getBlockMetadata());
                World world = iBlockSource.getWorld();
                double d2 = iBlockSource.getX() + (double)((float)enumFacing.getFrontOffsetX() * 1.125f);
                double d3 = iBlockSource.getY() + (double)((float)enumFacing.getFrontOffsetY() * 1.125f);
                double d4 = iBlockSource.getZ() + (double)((float)enumFacing.getFrontOffsetZ() * 1.125f);
                BlockPos blockPos = iBlockSource.getBlockPos().offset(enumFacing);
                Material material = world.getBlockState(blockPos).getBlock().getMaterial();
                if (Material.water.equals(material)) {
                    d = 1.0;
                } else {
                    if (!Material.air.equals(material) || !Material.water.equals(world.getBlockState(blockPos.down()).getBlock().getMaterial())) {
                        return this.field_150842_b.dispense(iBlockSource, itemStack);
                    }
                    d = 0.0;
                }
                EntityBoat entityBoat = new EntityBoat(world, d2, d3 + d, d4);
                world.spawnEntityInWorld(entityBoat);
                itemStack.splitStack(1);
                return itemStack;
            }
        });
        BehaviorDefaultDispenseItem behaviorDefaultDispenseItem = new BehaviorDefaultDispenseItem(){
            private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();

            @Override
            public ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                ItemBucket itemBucket = (ItemBucket)itemStack.getItem();
                BlockPos blockPos = iBlockSource.getBlockPos().offset(BlockDispenser.getFacing(iBlockSource.getBlockMetadata()));
                if (itemBucket.tryPlaceContainedLiquid(iBlockSource.getWorld(), blockPos)) {
                    itemStack.setItem(Items.bucket);
                    itemStack.stackSize = 1;
                    return itemStack;
                }
                return this.field_150841_b.dispense(iBlockSource, itemStack);
            }
        };
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, behaviorDefaultDispenseItem);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, behaviorDefaultDispenseItem);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem(){
            private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();

            @Override
            public ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                Item item;
                BlockPos blockPos;
                World world = iBlockSource.getWorld();
                IBlockState iBlockState = world.getBlockState(blockPos = iBlockSource.getBlockPos().offset(BlockDispenser.getFacing(iBlockSource.getBlockMetadata())));
                Block block = iBlockState.getBlock();
                Material material = block.getMaterial();
                if (Material.water.equals(material) && block instanceof BlockLiquid && iBlockState.getValue(BlockLiquid.LEVEL) == 0) {
                    item = Items.water_bucket;
                } else {
                    if (!Material.lava.equals(material) || !(block instanceof BlockLiquid) || iBlockState.getValue(BlockLiquid.LEVEL) != 0) {
                        return super.dispenseStack(iBlockSource, itemStack);
                    }
                    item = Items.lava_bucket;
                }
                world.setBlockToAir(blockPos);
                if (--itemStack.stackSize == 0) {
                    itemStack.setItem(item);
                    itemStack.stackSize = 1;
                } else if (((TileEntityDispenser)iBlockSource.getBlockTileEntity()).addItemStack(new ItemStack(item)) < 0) {
                    this.field_150840_b.dispense(iBlockSource, new ItemStack(item));
                }
                return itemStack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem(){
            private boolean field_150839_b = true;

            @Override
            protected void playDispenseSound(IBlockSource iBlockSource) {
                if (this.field_150839_b) {
                    iBlockSource.getWorld().playAuxSFX(1000, iBlockSource.getBlockPos(), 0);
                } else {
                    iBlockSource.getWorld().playAuxSFX(1001, iBlockSource.getBlockPos(), 0);
                }
            }

            @Override
            protected ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                BlockPos blockPos;
                World world = iBlockSource.getWorld();
                if (world.isAirBlock(blockPos = iBlockSource.getBlockPos().offset(BlockDispenser.getFacing(iBlockSource.getBlockMetadata())))) {
                    world.setBlockState(blockPos, Blocks.fire.getDefaultState());
                    if (itemStack.attemptDamageItem(1, world.rand)) {
                        itemStack.stackSize = 0;
                    }
                } else if (world.getBlockState(blockPos).getBlock() == Blocks.tnt) {
                    Blocks.tnt.onBlockDestroyedByPlayer(world, blockPos, Blocks.tnt.getDefaultState().withProperty(BlockTNT.EXPLODE, true));
                    world.setBlockToAir(blockPos);
                } else {
                    this.field_150839_b = false;
                }
                return itemStack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem(){
            private boolean field_150838_b = true;

            @Override
            protected void playDispenseSound(IBlockSource iBlockSource) {
                if (this.field_150838_b) {
                    iBlockSource.getWorld().playAuxSFX(1000, iBlockSource.getBlockPos(), 0);
                } else {
                    iBlockSource.getWorld().playAuxSFX(1001, iBlockSource.getBlockPos(), 0);
                }
            }

            @Override
            protected ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(itemStack.getMetadata())) {
                    BlockPos blockPos;
                    World world = iBlockSource.getWorld();
                    if (ItemDye.applyBonemeal(itemStack, world, blockPos = iBlockSource.getBlockPos().offset(BlockDispenser.getFacing(iBlockSource.getBlockMetadata())))) {
                        if (!world.isRemote) {
                            world.playAuxSFX(2005, blockPos, 0);
                        }
                    } else {
                        this.field_150838_b = false;
                    }
                    return itemStack;
                }
                return super.dispenseStack(iBlockSource, itemStack);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem(){

            @Override
            protected ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                World world = iBlockSource.getWorld();
                BlockPos blockPos = iBlockSource.getBlockPos().offset(BlockDispenser.getFacing(iBlockSource.getBlockMetadata()));
                EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed(world, (double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5, null);
                world.spawnEntityInWorld(entityTNTPrimed);
                world.playSoundAtEntity(entityTNTPrimed, "game.tnt.primed", 1.0f, 1.0f);
                --itemStack.stackSize;
                return itemStack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem(){
            private boolean field_179240_b = true;

            @Override
            protected ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                World world = iBlockSource.getWorld();
                EnumFacing enumFacing = BlockDispenser.getFacing(iBlockSource.getBlockMetadata());
                BlockPos blockPos = iBlockSource.getBlockPos().offset(enumFacing);
                BlockSkull blockSkull = Blocks.skull;
                if (world.isAirBlock(blockPos) && blockSkull.canDispenserPlace(world, blockPos, itemStack)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockPos, blockSkull.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP), 3);
                        TileEntity tileEntity = world.getTileEntity(blockPos);
                        if (tileEntity instanceof TileEntitySkull) {
                            if (itemStack.getMetadata() == 3) {
                                GameProfile gameProfile = null;
                                if (itemStack.hasTagCompound()) {
                                    String string;
                                    NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
                                    if (nBTTagCompound.hasKey("SkullOwner", 10)) {
                                        gameProfile = NBTUtil.readGameProfileFromNBT(nBTTagCompound.getCompoundTag("SkullOwner"));
                                    } else if (nBTTagCompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(string = nBTTagCompound.getString("SkullOwner"))) {
                                        gameProfile = new GameProfile(null, string);
                                    }
                                }
                                ((TileEntitySkull)tileEntity).setPlayerProfile(gameProfile);
                            } else {
                                ((TileEntitySkull)tileEntity).setType(itemStack.getMetadata());
                            }
                            ((TileEntitySkull)tileEntity).setSkullRotation(enumFacing.getOpposite().getHorizontalIndex() * 4);
                            Blocks.skull.checkWitherSpawn(world, blockPos, (TileEntitySkull)tileEntity);
                        }
                        --itemStack.stackSize;
                    }
                } else {
                    this.field_179240_b = false;
                }
                return itemStack;
            }

            @Override
            protected void playDispenseSound(IBlockSource iBlockSource) {
                if (this.field_179240_b) {
                    iBlockSource.getWorld().playAuxSFX(1000, iBlockSource.getBlockPos(), 0);
                } else {
                    iBlockSource.getWorld().playAuxSFX(1001, iBlockSource.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin), new BehaviorDefaultDispenseItem(){
            private boolean field_179241_b = true;

            @Override
            protected ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack itemStack) {
                World world = iBlockSource.getWorld();
                BlockPos blockPos = iBlockSource.getBlockPos().offset(BlockDispenser.getFacing(iBlockSource.getBlockMetadata()));
                BlockPumpkin blockPumpkin = (BlockPumpkin)Blocks.pumpkin;
                if (world.isAirBlock(blockPos) && blockPumpkin.canDispenserPlace(world, blockPos)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockPos, blockPumpkin.getDefaultState(), 3);
                    }
                    --itemStack.stackSize;
                } else {
                    this.field_179241_b = false;
                }
                return itemStack;
            }

            @Override
            protected void playDispenseSound(IBlockSource iBlockSource) {
                if (this.field_179241_b) {
                    iBlockSource.getWorld().playAuxSFX(1000, iBlockSource.getBlockPos(), 0);
                } else {
                    iBlockSource.getWorld().playAuxSFX(1001, iBlockSource.getBlockPos(), 0);
                }
            }
        });
    }

    public static boolean isRegistered() {
        return alreadyRegistered;
    }
}

