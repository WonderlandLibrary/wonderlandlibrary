// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.init;

import org.apache.logging.log4j.LogManager;
import java.io.OutputStream;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.stats.StatList;
import net.minecraft.block.BlockFire;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.block.BlockSkull;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemDye;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockTNT;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.IProjectile;
import net.minecraft.dispenser.IPosition;
import net.minecraft.world.World;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.block.BlockDispenser;
import org.apache.logging.log4j.Logger;
import java.io.PrintStream;

public class Bootstrap
{
    private static final PrintStream SYSOUT;
    private static boolean alreadyRegistered;
    private static final Logger LOGGER;
    private static final String __OBFID = "CL_00001397";
    
    public static boolean isRegistered() {
        return Bootstrap.alreadyRegistered;
    }
    
    static void registerDispenserBehaviors() {
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense() {
            private static final String __OBFID = "CL_00001398";
            
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                final EntityArrow var3 = new EntityArrow(worldIn, position.getX(), position.getY(), position.getZ());
                var3.canBePickedUp = 1;
                return var3;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense() {
            private static final String __OBFID = "CL_00001404";
            
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense() {
            private static final String __OBFID = "CL_00001405";
            
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense() {
            private static final String __OBFID = "CL_00001406";
            
            @Override
            protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
            }
            
            @Override
            protected float func_82498_a() {
                return super.func_82498_a() * 0.5f;
            }
            
            @Override
            protected float func_82500_b() {
                return super.func_82500_b() * 1.25f;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001407";
            
            @Override
            public ItemStack dispense(final IBlockSource source, final ItemStack stack) {
                return ItemPotion.isSplash(stack.getMetadata()) ? new BehaviorProjectileDispense() {
                    private static final String __OBFID = "CL_00001408";
                    
                    @Override
                    protected IProjectile getProjectileEntity(final World worldIn, final IPosition position) {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }
                    
                    @Override
                    protected float func_82498_a() {
                        return super.func_82498_a() * 0.5f;
                    }
                    
                    @Override
                    protected float func_82500_b() {
                        return super.func_82500_b() * 1.25f;
                    }
                }.dispense(source, stack) : this.field_150843_b.dispense(source, stack);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001410";
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                final double var4 = source.getX() + var3.getFrontOffsetX();
                final double var5 = source.getBlockPos().getY() + 0.2f;
                final double var6 = source.getZ() + var3.getFrontOffsetZ();
                final Entity var7 = ItemMonsterPlacer.spawnCreature(source.getWorld(), stack.getMetadata(), var4, var5, var6);
                if (var7 instanceof EntityLivingBase && stack.hasDisplayName()) {
                    ((EntityLiving)var7).setCustomNameTag(stack.getDisplayName());
                }
                stack.splitStack(1);
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001411";
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                final double var4 = source.getX() + var3.getFrontOffsetX();
                final double var5 = source.getBlockPos().getY() + 0.2f;
                final double var6 = source.getZ() + var3.getFrontOffsetZ();
                final EntityFireworkRocket var7 = new EntityFireworkRocket(source.getWorld(), var4, var5, var6, stack);
                source.getWorld().spawnEntityInWorld(var7);
                stack.splitStack(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001412";
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                final IPosition var4 = BlockDispenser.getDispensePosition(source);
                final double var5 = var4.getX() + var3.getFrontOffsetX() * 0.3f;
                final double var6 = var4.getY() + var3.getFrontOffsetX() * 0.3f;
                final double var7 = var4.getZ() + var3.getFrontOffsetZ() * 0.3f;
                final World var8 = source.getWorld();
                final Random var9 = var8.rand;
                final double var10 = var9.nextGaussian() * 0.05 + var3.getFrontOffsetX();
                final double var11 = var9.nextGaussian() * 0.05 + var3.getFrontOffsetY();
                final double var12 = var9.nextGaussian() * 0.05 + var3.getFrontOffsetZ();
                var8.spawnEntityInWorld(new EntitySmallFireball(var8, var5, var6, var7, var10, var11, var12));
                stack.splitStack(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playAuxSFX(1009, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001413";
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                final World var4 = source.getWorld();
                final double var5 = source.getX() + var3.getFrontOffsetX() * 1.125f;
                final double var6 = source.getY() + var3.getFrontOffsetY() * 1.125f;
                final double var7 = source.getZ() + var3.getFrontOffsetZ() * 1.125f;
                final BlockPos var8 = source.getBlockPos().offset(var3);
                final Material var9 = var4.getBlockState(var8).getBlock().getMaterial();
                double var10;
                if (Material.water.equals(var9)) {
                    var10 = 1.0;
                }
                else {
                    if (!Material.air.equals(var9) || !Material.water.equals(var4.getBlockState(var8.offsetDown()).getBlock().getMaterial())) {
                        return this.field_150842_b.dispense(source, stack);
                    }
                    var10 = 0.0;
                }
                final EntityBoat var11 = new EntityBoat(var4, var5, var6 + var10, var7);
                var4.spawnEntityInWorld(var11);
                stack.splitStack(1);
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
            }
        });
        final BehaviorDefaultDispenseItem var0 = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001399";
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final ItemBucket var3 = (ItemBucket)stack.getItem();
                final BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                if (var3.func_180616_a(source.getWorld(), var4)) {
                    stack.setItem(Items.bucket);
                    stack.stackSize = 1;
                    return stack;
                }
                return this.field_150841_b.dispense(source, stack);
            }
        };
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001400";
            
            public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World var3 = source.getWorld();
                final BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                final IBlockState var5 = var3.getBlockState(var4);
                final Block var6 = var5.getBlock();
                final Material var7 = var6.getMaterial();
                Item var8;
                if (Material.water.equals(var7) && var6 instanceof BlockLiquid && (int)var5.getValue(BlockLiquid.LEVEL) == 0) {
                    var8 = Items.water_bucket;
                }
                else {
                    if (!Material.lava.equals(var7) || !(var6 instanceof BlockLiquid) || (int)var5.getValue(BlockLiquid.LEVEL) != 0) {
                        return super.dispenseStack(source, stack);
                    }
                    var8 = Items.lava_bucket;
                }
                var3.setBlockToAir(var4);
                final int stackSize = stack.stackSize - 1;
                stack.stackSize = stackSize;
                if (stackSize == 0) {
                    stack.setItem(var8);
                    stack.stackSize = 1;
                }
                else if (((TileEntityDispenser)source.getBlockTileEntity()).func_146019_a(new ItemStack(var8)) < 0) {
                    this.field_150840_b.dispense(source, new ItemStack(var8));
                }
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem() {
            private boolean field_150839_b = true;
            private static final String __OBFID = "CL_00001401";
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World var3 = source.getWorld();
                final BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                if (var3.isAirBlock(var4)) {
                    var3.setBlockState(var4, Blocks.fire.getDefaultState());
                    if (stack.attemptDamageItem(1, var3.rand)) {
                        stack.stackSize = 0;
                    }
                }
                else if (var3.getBlockState(var4).getBlock() == Blocks.tnt) {
                    Blocks.tnt.onBlockDestroyedByPlayer(var3, var4, Blocks.tnt.getDefaultState().withProperty(BlockTNT.field_176246_a, true));
                    var3.setBlockToAir(var4);
                }
                else {
                    this.field_150839_b = false;
                }
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                if (this.field_150839_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                }
                else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem() {
            private boolean field_150838_b = true;
            private static final String __OBFID = "CL_00001402";
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                if (EnumDyeColor.WHITE == EnumDyeColor.func_176766_a(stack.getMetadata())) {
                    final World var3 = source.getWorld();
                    final BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                    if (ItemDye.func_179234_a(stack, var3, var4)) {
                        if (!var3.isRemote) {
                            var3.playAuxSFX(2005, var4, 0);
                        }
                    }
                    else {
                        this.field_150838_b = false;
                    }
                    return stack;
                }
                return super.dispenseStack(source, stack);
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                if (this.field_150838_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                }
                else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00001403";
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World var3 = source.getWorld();
                final BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                final EntityTNTPrimed var5 = new EntityTNTPrimed(var3, var4.getX() + 0.5, var4.getY(), var4.getZ() + 0.5, null);
                var3.spawnEntityInWorld(var5);
                var3.playSoundAtEntity(var5, "game.tnt.primed", 1.0f, 1.0f);
                --stack.stackSize;
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem() {
            private boolean field_179240_b = true;
            private static final String __OBFID = "CL_00002278";
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World var3 = source.getWorld();
                final EnumFacing var4 = BlockDispenser.getFacing(source.getBlockMetadata());
                final BlockPos var5 = source.getBlockPos().offset(var4);
                final BlockSkull var6 = Blocks.skull;
                if (var3.isAirBlock(var5) && var6.func_176415_b(var3, var5, stack)) {
                    if (!var3.isRemote) {
                        var3.setBlockState(var5, var6.getDefaultState().withProperty(BlockSkull.field_176418_a, EnumFacing.UP), 3);
                        final TileEntity var7 = var3.getTileEntity(var5);
                        if (var7 instanceof TileEntitySkull) {
                            if (stack.getMetadata() == 3) {
                                GameProfile var8 = null;
                                if (stack.hasTagCompound()) {
                                    final NBTTagCompound var9 = stack.getTagCompound();
                                    if (var9.hasKey("SkullOwner", 10)) {
                                        var8 = NBTUtil.readGameProfileFromNBT(var9.getCompoundTag("SkullOwner"));
                                    }
                                    else if (var9.hasKey("SkullOwner", 8)) {
                                        var8 = new GameProfile((UUID)null, var9.getString("SkullOwner"));
                                    }
                                }
                                ((TileEntitySkull)var7).setPlayerProfile(var8);
                            }
                            else {
                                ((TileEntitySkull)var7).setType(stack.getMetadata());
                            }
                            ((TileEntitySkull)var7).setSkullRotation(var4.getOpposite().getHorizontalIndex() * 4);
                            Blocks.skull.func_180679_a(var3, var5, (TileEntitySkull)var7);
                        }
                        --stack.stackSize;
                    }
                }
                else {
                    this.field_179240_b = false;
                }
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                if (this.field_179240_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                }
                else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin), new BehaviorDefaultDispenseItem() {
            private boolean field_179241_b = true;
            private static final String __OBFID = "CL_00002277";
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World var3 = source.getWorld();
                final BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                final BlockPumpkin var5 = (BlockPumpkin)Blocks.pumpkin;
                if (var3.isAirBlock(var4) && var5.func_176390_d(var3, var4)) {
                    if (!var3.isRemote) {
                        var3.setBlockState(var4, var5.getDefaultState(), 3);
                    }
                    --stack.stackSize;
                }
                else {
                    this.field_179241_b = false;
                }
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
                if (this.field_179241_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                }
                else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.command_block), new BehaviorDefaultDispenseItem() {
            private static final String __OBFID = "CL_00002276";
            
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final World var3 = source.getWorld();
                final BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                if (var3.isAirBlock(var4)) {
                    if (!var3.isRemote) {
                        final IBlockState var5 = Blocks.command_block.getDefaultState().withProperty(BlockCommandBlock.TRIGGERED_PROP, false);
                        var3.setBlockState(var4, var5, 3);
                        ItemBlock.setTileEntityNBT(var3, var4, stack);
                        var3.notifyNeighborsOfStateChange(source.getBlockPos(), source.getBlock());
                    }
                    --stack.stackSize;
                }
                return stack;
            }
            
            @Override
            protected void playDispenseSound(final IBlockSource source) {
            }
            
            @Override
            protected void spawnDispenseParticles(final IBlockSource source, final EnumFacing facingIn) {
            }
        });
    }
    
    public static void register() {
        if (!Bootstrap.alreadyRegistered) {
            Bootstrap.alreadyRegistered = true;
            if (Bootstrap.LOGGER.isDebugEnabled()) {
                redirectOutputToLog();
            }
            Block.registerBlocks();
            BlockFire.func_149843_e();
            Item.registerItems();
            StatList.func_151178_a();
            registerDispenserBehaviors();
        }
    }
    
    private static void redirectOutputToLog() {
        System.setErr(new LoggingPrintStream("STDERR", System.err));
        System.setOut(new LoggingPrintStream("STDOUT", Bootstrap.SYSOUT));
    }
    
    public static void func_179870_a(final String p_179870_0_) {
        Bootstrap.SYSOUT.println(p_179870_0_);
    }
    
    static {
        SYSOUT = System.out;
        Bootstrap.alreadyRegistered = false;
        LOGGER = LogManager.getLogger();
    }
}
