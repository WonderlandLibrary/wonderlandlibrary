package net.minecraft.init;

import com.mojang.authlib.GameProfile;
import java.io.PrintStream;
import java.util.Random;
import java.util.UUID;
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
   private static final Logger LOGGER;
   private static boolean alreadyRegistered;
   private static final PrintStream SYSOUT;

   public static void register() {
      if (!alreadyRegistered) {
         alreadyRegistered = true;
         if (LOGGER.isDebugEnabled()) {
            redirectOutputToLog();
         }

         Block.registerBlocks();
         BlockFire.init();
         Item.registerItems();
         StatList.init();
         registerDispenserBehaviors();
      }

   }

   static {
      SYSOUT = System.out;
      alreadyRegistered = false;
      LOGGER = LogManager.getLogger();
   }

   private static void redirectOutputToLog() {
      System.setErr(new LoggingPrintStream("STDERR", System.err));
      System.setOut(new LoggingPrintStream("STDOUT", SYSOUT));
   }

   static void registerDispenserBehaviors() {
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense() {
         protected IProjectile getProjectileEntity(World var1, IPosition var2) {
            EntityArrow var3 = new EntityArrow(var1, var2.getX(), var2.getY(), var2.getZ());
            var3.canBePickedUp = 1;
            return var3;
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense() {
         protected IProjectile getProjectileEntity(World var1, IPosition var2) {
            return new EntityEgg(var1, var2.getX(), var2.getY(), var2.getZ());
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense() {
         protected IProjectile getProjectileEntity(World var1, IPosition var2) {
            return new EntitySnowball(var1, var2.getX(), var2.getY(), var2.getZ());
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense() {
         protected IProjectile getProjectileEntity(World var1, IPosition var2) {
            return new EntityExpBottle(var1, var2.getX(), var2.getY(), var2.getZ());
         }

         protected float func_82498_a() {
            return super.func_82498_a() * 0.5F;
         }

         protected float func_82500_b() {
            return super.func_82500_b() * 1.25F;
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem() {
         private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();

         public ItemStack dispense(IBlockSource var1, ItemStack var2) {
            return ItemPotion.isSplash(var2.getMetadata()) ? (new BehaviorProjectileDispense(this, var2) {
               final <undefinedtype> this$1;
               private final ItemStack val$stack;

               {
                  this.this$1 = var1;
                  this.val$stack = var2;
               }

               protected float func_82500_b() {
                  return super.func_82500_b() * 1.25F;
               }

               protected float func_82498_a() {
                  return super.func_82498_a() * 0.5F;
               }

               protected IProjectile getProjectileEntity(World var1, IPosition var2) {
                  return new EntityPotion(var1, var2.getX(), var2.getY(), var2.getZ(), this.val$stack.copy());
               }
            }).dispense(var1, var2) : this.field_150843_b.dispense(var1, var2);
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem() {
         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            EnumFacing var3 = BlockDispenser.getFacing(var1.getBlockMetadata());
            double var4 = var1.getX() + (double)var3.getFrontOffsetX();
            double var6 = (double)((float)var1.getBlockPos().getY() + 0.2F);
            double var8 = var1.getZ() + (double)var3.getFrontOffsetZ();
            Entity var10 = ItemMonsterPlacer.spawnCreature(var1.getWorld(), var2.getMetadata(), var4, var6, var8);
            if (var10 instanceof EntityLivingBase && var2.hasDisplayName()) {
               ((EntityLiving)var10).setCustomNameTag(var2.getDisplayName());
            }

            var2.splitStack(1);
            return var2;
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem() {
         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            EnumFacing var3 = BlockDispenser.getFacing(var1.getBlockMetadata());
            double var4 = var1.getX() + (double)var3.getFrontOffsetX();
            double var6 = (double)((float)var1.getBlockPos().getY() + 0.2F);
            double var8 = var1.getZ() + (double)var3.getFrontOffsetZ();
            EntityFireworkRocket var10 = new EntityFireworkRocket(var1.getWorld(), var4, var6, var8, var2);
            var1.getWorld().spawnEntityInWorld(var10);
            var2.splitStack(1);
            return var2;
         }

         protected void playDispenseSound(IBlockSource var1) {
            var1.getWorld().playAuxSFX(1002, var1.getBlockPos(), 0);
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem() {
         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            EnumFacing var3 = BlockDispenser.getFacing(var1.getBlockMetadata());
            IPosition var4 = BlockDispenser.getDispensePosition(var1);
            double var5 = var4.getX() + (double)((float)var3.getFrontOffsetX() * 0.3F);
            double var7 = var4.getY() + (double)((float)var3.getFrontOffsetY() * 0.3F);
            double var9 = var4.getZ() + (double)((float)var3.getFrontOffsetZ() * 0.3F);
            World var11 = var1.getWorld();
            Random var12 = var11.rand;
            double var13 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetX();
            double var15 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetY();
            double var17 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetZ();
            var11.spawnEntityInWorld(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
            var2.splitStack(1);
            return var2;
         }

         protected void playDispenseSound(IBlockSource var1) {
            var1.getWorld().playAuxSFX(1009, var1.getBlockPos(), 0);
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem() {
         private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();

         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            EnumFacing var3 = BlockDispenser.getFacing(var1.getBlockMetadata());
            World var4 = var1.getWorld();
            double var5 = var1.getX() + (double)((float)var3.getFrontOffsetX() * 1.125F);
            double var7 = var1.getY() + (double)((float)var3.getFrontOffsetY() * 1.125F);
            double var9 = var1.getZ() + (double)((float)var3.getFrontOffsetZ() * 1.125F);
            BlockPos var11 = var1.getBlockPos().offset(var3);
            Material var12 = var4.getBlockState(var11).getBlock().getMaterial();
            double var13;
            if (Material.water.equals(var12)) {
               var13 = 1.0D;
            } else {
               if (!Material.air.equals(var12) || !Material.water.equals(var4.getBlockState(var11.down()).getBlock().getMaterial())) {
                  return this.field_150842_b.dispense(var1, var2);
               }

               var13 = 0.0D;
            }

            EntityBoat var15 = new EntityBoat(var4, var5, var7 + var13, var9);
            var4.spawnEntityInWorld(var15);
            var2.splitStack(1);
            return var2;
         }

         protected void playDispenseSound(IBlockSource var1) {
            var1.getWorld().playAuxSFX(1000, var1.getBlockPos(), 0);
         }
      });
      BehaviorDefaultDispenseItem var0 = new BehaviorDefaultDispenseItem() {
         private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();

         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            ItemBucket var3 = (ItemBucket)var2.getItem();
            BlockPos var4 = var1.getBlockPos().offset(BlockDispenser.getFacing(var1.getBlockMetadata()));
            if (var3.tryPlaceContainedLiquid(var1.getWorld(), var4)) {
               var2.setItem(Items.bucket);
               var2.stackSize = 1;
               return var2;
            } else {
               return this.field_150841_b.dispense(var1, var2);
            }
         }
      };
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, var0);
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, var0);
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem() {
         private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();

         public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World var3 = var1.getWorld();
            BlockPos var4 = var1.getBlockPos().offset(BlockDispenser.getFacing(var1.getBlockMetadata()));
            IBlockState var5 = var3.getBlockState(var4);
            Block var6 = var5.getBlock();
            Material var7 = var6.getMaterial();
            Item var8;
            if (Material.water.equals(var7) && var6 instanceof BlockLiquid && (Integer)var5.getValue(BlockLiquid.LEVEL) == 0) {
               var8 = Items.water_bucket;
            } else {
               if (!Material.lava.equals(var7) || !(var6 instanceof BlockLiquid) || (Integer)var5.getValue(BlockLiquid.LEVEL) != 0) {
                  return super.dispenseStack(var1, var2);
               }

               var8 = Items.lava_bucket;
            }

            var3.setBlockToAir(var4);
            if (--var2.stackSize == 0) {
               var2.setItem(var8);
               var2.stackSize = 1;
            } else if (((TileEntityDispenser)var1.getBlockTileEntity()).addItemStack(new ItemStack(var8)) < 0) {
               this.field_150840_b.dispense(var1, new ItemStack(var8));
            }

            return var2;
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem() {
         private boolean field_150839_b = true;

         protected void playDispenseSound(IBlockSource var1) {
            if (this.field_150839_b) {
               var1.getWorld().playAuxSFX(1000, var1.getBlockPos(), 0);
            } else {
               var1.getWorld().playAuxSFX(1001, var1.getBlockPos(), 0);
            }

         }

         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World var3 = var1.getWorld();
            BlockPos var4 = var1.getBlockPos().offset(BlockDispenser.getFacing(var1.getBlockMetadata()));
            if (var3.isAirBlock(var4)) {
               var3.setBlockState(var4, Blocks.fire.getDefaultState());
               if (var2.attemptDamageItem(1, var3.rand)) {
                  var2.stackSize = 0;
               }
            } else if (var3.getBlockState(var4).getBlock() == Blocks.tnt) {
               Blocks.tnt.onBlockDestroyedByPlayer(var3, var4, Blocks.tnt.getDefaultState().withProperty(BlockTNT.EXPLODE, true));
               var3.setBlockToAir(var4);
            } else {
               this.field_150839_b = false;
            }

            return var2;
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem() {
         private boolean field_150838_b = true;

         protected void playDispenseSound(IBlockSource var1) {
            if (this.field_150838_b) {
               var1.getWorld().playAuxSFX(1000, var1.getBlockPos(), 0);
            } else {
               var1.getWorld().playAuxSFX(1001, var1.getBlockPos(), 0);
            }

         }

         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(var2.getMetadata())) {
               World var3 = var1.getWorld();
               BlockPos var4 = var1.getBlockPos().offset(BlockDispenser.getFacing(var1.getBlockMetadata()));
               if (ItemDye.applyBonemeal(var2, var3, var4)) {
                  if (!var3.isRemote) {
                     var3.playAuxSFX(2005, var4, 0);
                  }
               } else {
                  this.field_150838_b = false;
               }

               return var2;
            } else {
               return super.dispenseStack(var1, var2);
            }
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem() {
         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World var3 = var1.getWorld();
            BlockPos var4 = var1.getBlockPos().offset(BlockDispenser.getFacing(var1.getBlockMetadata()));
            EntityTNTPrimed var5 = new EntityTNTPrimed(var3, (double)var4.getX() + 0.5D, (double)var4.getY(), (double)var4.getZ() + 0.5D, (EntityLivingBase)null);
            var3.spawnEntityInWorld(var5);
            var3.playSoundAtEntity(var5, "game.tnt.primed", 1.0F, 1.0F);
            --var2.stackSize;
            return var2;
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem() {
         private boolean field_179240_b = true;

         protected void playDispenseSound(IBlockSource var1) {
            if (this.field_179240_b) {
               var1.getWorld().playAuxSFX(1000, var1.getBlockPos(), 0);
            } else {
               var1.getWorld().playAuxSFX(1001, var1.getBlockPos(), 0);
            }

         }

         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World var3 = var1.getWorld();
            EnumFacing var4 = BlockDispenser.getFacing(var1.getBlockMetadata());
            BlockPos var5 = var1.getBlockPos().offset(var4);
            BlockSkull var6 = Blocks.skull;
            if (var3.isAirBlock(var5) && var6.canDispenserPlace(var3, var5, var2)) {
               if (!var3.isRemote) {
                  var3.setBlockState(var5, var6.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP), 3);
                  TileEntity var7 = var3.getTileEntity(var5);
                  if (var7 instanceof TileEntitySkull) {
                     if (var2.getMetadata() == 3) {
                        GameProfile var8 = null;
                        if (var2.hasTagCompound()) {
                           NBTTagCompound var9 = var2.getTagCompound();
                           if (var9.hasKey("SkullOwner", 10)) {
                              var8 = NBTUtil.readGameProfileFromNBT(var9.getCompoundTag("SkullOwner"));
                           } else if (var9.hasKey("SkullOwner", 8)) {
                              String var10 = var9.getString("SkullOwner");
                              if (!StringUtils.isNullOrEmpty(var10)) {
                                 var8 = new GameProfile((UUID)null, var10);
                              }
                           }
                        }

                        ((TileEntitySkull)var7).setPlayerProfile(var8);
                     } else {
                        ((TileEntitySkull)var7).setType(var2.getMetadata());
                     }

                     ((TileEntitySkull)var7).setSkullRotation(var4.getOpposite().getHorizontalIndex() * 4);
                     Blocks.skull.checkWitherSpawn(var3, var5, (TileEntitySkull)var7);
                  }

                  --var2.stackSize;
               }
            } else {
               this.field_179240_b = false;
            }

            return var2;
         }
      });
      BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin), new BehaviorDefaultDispenseItem() {
         private boolean field_179241_b = true;

         protected void playDispenseSound(IBlockSource var1) {
            if (this.field_179241_b) {
               var1.getWorld().playAuxSFX(1000, var1.getBlockPos(), 0);
            } else {
               var1.getWorld().playAuxSFX(1001, var1.getBlockPos(), 0);
            }

         }

         protected ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
            World var3 = var1.getWorld();
            BlockPos var4 = var1.getBlockPos().offset(BlockDispenser.getFacing(var1.getBlockMetadata()));
            BlockPumpkin var5 = (BlockPumpkin)Blocks.pumpkin;
            if (var3.isAirBlock(var4) && var5.canDispenserPlace(var3, var4)) {
               if (!var3.isRemote) {
                  var3.setBlockState(var4, var5.getDefaultState(), 3);
               }

               --var2.stackSize;
            } else {
               this.field_179241_b = false;
            }

            return var2;
         }
      });
   }

   public static void printToSYSOUT(String var0) {
      SYSOUT.println(var0);
   }

   public static boolean isRegistered() {
      return alreadyRegistered;
   }
}
