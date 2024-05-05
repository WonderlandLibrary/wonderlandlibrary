package skidmonke;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.dort.dortware.impl.modules.movement.Flight;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Random;

public class Bootstrap {
    private static final PrintStream SYS_OUT = System.out;

    /**
     * Whether the blocks, items, etc have already been registered
     */
    private static boolean alreadyRegistered = false;
    private static final Logger LOGGER = LogManager.getLogger();
    // private static final String __OBFID = "CL_00001397";

    /**
     * Is Bootstrap registration already done?
     */
    public static boolean isRegistered() {
        return alreadyRegistered;
    }

    static void registerDispenserBehaviors() {
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense() {
            // private static final String __OBFID = "CL_00001398";
            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                EntityArrow var3 = new EntityArrow(worldIn, position.getX(), position.getY(), position.getZ());
                var3.canBePickedUp = 1;
                return var3;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense() {
            // private static final String __OBFID = "CL_00001404";
            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense() {
            // private static final String __OBFID = "CL_00001405";
            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense() {
            // private static final String __OBFID = "CL_00001406";
            protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
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

            // private static final String __OBFID = "CL_00001407";
            public ItemStack dispense(IBlockSource source, final ItemStack stack) {
                return ItemPotion.isSplash(stack.getMetadata()) ? (new BehaviorProjectileDispense() {
                    // private static final String __OBFID = "CL_00001408";
                    protected IProjectile getProjectileEntity(World worldIn, IPosition position) {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }

                    protected float func_82498_a() {
                        return super.func_82498_a() * 0.5F;
                    }

                    protected float func_82500_b() {
                        return super.func_82500_b() * 1.25F;
                    }
                }).dispense(source, stack) : this.field_150843_b.dispense(source, stack);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem() {
            // private static final String __OBFID = "CL_00001410";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                double var4 = source.getX() + (double) var3.getFrontOffsetX();
                double var6 = (float) source.getBlockPos().getY() + 0.2F;
                double var8 = source.getZ() + (double) var3.getFrontOffsetZ();
                Entity var10 = ItemMonsterPlacer.spawnCreature(source.getWorld(), stack.getMetadata(), var4, var6, var8);

                if (var10 instanceof EntityLivingBase && stack.hasDisplayName()) {
                    var10.setCustomNameTag(stack.getDisplayName());
                }

                stack.splitStack(1);
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem() {
            // private static final String __OBFID = "CL_00001411";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                double var4 = source.getX() + (double) var3.getFrontOffsetX();
                double var6 = (float) source.getBlockPos().getY() + 0.2F;
                double var8 = source.getZ() + (double) var3.getFrontOffsetZ();
                EntityFireworkRocket var10 = new EntityFireworkRocket(source.getWorld(), var4, var6, var8, stack);
                source.getWorld().spawnEntityInWorld(var10);
                stack.splitStack(1);
                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem() {
            // private static final String __OBFID = "CL_00001412";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                IPosition var4 = BlockDispenser.getDispensePosition(source);
                double var5 = var4.getX() + (double) ((float) var3.getFrontOffsetX() * 0.3F);
                double var7 = var4.getY() + (double) ((float) var3.getFrontOffsetX() * 0.3F);
                double var9 = var4.getZ() + (double) ((float) var3.getFrontOffsetZ() * 0.3F);
                World var11 = source.getWorld();
                Random var12 = var11.rand;
                double var13 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetX();
                double var15 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetY();
                double var17 = var12.nextGaussian() * 0.05D + (double) var3.getFrontOffsetZ();
                var11.spawnEntityInWorld(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
                stack.splitStack(1);
                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playAuxSFX(1009, source.getBlockPos(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();

            // private static final String __OBFID = "CL_00001413";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing var3 = BlockDispenser.getFacing(source.getBlockMetadata());
                World var4 = source.getWorld();
                double var5 = source.getX() + (double) ((float) var3.getFrontOffsetX() * 1.125F);
                double var7 = source.getY() + (double) ((float) var3.getFrontOffsetY() * 1.125F);
                double var9 = source.getZ() + (double) ((float) var3.getFrontOffsetZ() * 1.125F);
                BlockPos var11 = source.getBlockPos().offset(var3);
                Material var12 = var4.getBlockState(var11).getBlock().getMaterial();
                double var13;

                if (Material.water.equals(var12)) {
                    var13 = 1.0D;
                } else {
                    if (!Material.air.equals(var12) || !Material.water.equals(var4.getBlockState(var11.offsetDown()).getBlock().getMaterial())) {
                        return this.field_150842_b.dispense(source, stack);
                    }

                    var13 = 0.0D;
                }

                EntityBoat var15 = new EntityBoat(var4, var5, var7 + var13, var9);
                var4.spawnEntityInWorld(var15);
                stack.splitStack(1);
                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
            }
        });
        BehaviorDefaultDispenseItem var0 = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();

            // private static final String __OBFID = "CL_00001399";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                ItemBucket var3 = (ItemBucket) stack.getItem();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));

                if (var3.func_180616_a(source.getWorld(), var4)) {
                    stack.setItem(Items.bucket);
                    stack.stackSize = 1;
                    return stack;
                } else {
                    return this.field_150841_b.dispense(source, stack);
                }
            }
        };
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();

            // private static final String __OBFID = "CL_00001400";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                IBlockState var5 = var3.getBlockState(var4);
                Block var6 = var5.getBlock();
                Material var7 = var6.getMaterial();
                Item var8;

                if (Material.water.equals(var7) && var6 instanceof BlockLiquid && (Integer) var5.getValue(BlockLiquid.LEVEL) == 0) {
                    var8 = Items.water_bucket;
                } else {
                    if (!Material.lava.equals(var7) || !(var6 instanceof BlockLiquid) || (Integer) var5.getValue(BlockLiquid.LEVEL) != 0) {
                        return super.dispenseStack(source, stack);
                    }

                    var8 = Items.lava_bucket;
                }

                var3.setBlockToAir(var4);

                if (--stack.stackSize == 0) {
                    stack.setItem(var8);
                    stack.stackSize = 1;
                } else if (((TileEntityDispenser) source.getBlockTileEntity()).func_146019_a(new ItemStack(var8)) < 0) {
                    this.field_150840_b.dispense(source, new ItemStack(var8));
                }

                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem() {
            private boolean field_150839_b = true;

            // private static final String __OBFID = "CL_00001401";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));

                if (var3.isAirBlock(var4)) {
                    var3.setBlockState(var4, Blocks.fire.getDefaultState());

                    if (stack.attemptDamageItem(1, var3.rand)) {
                        stack.stackSize = 0;
                    }
                } else if (var3.getBlockState(var4).getBlock() == Blocks.tnt) {
                    Blocks.tnt.onBlockDestroyedByPlayer(var3, var4, Blocks.tnt.getDefaultState().withProperty(BlockTNT.field_176246_a, true));
                    var3.setBlockToAir(var4);
                } else {
                    this.field_150839_b = false;
                }

                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                if (this.field_150839_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                } else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem() {
            private boolean field_150838_b = true;

            // private static final String __OBFID = "CL_00001402";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                if (EnumDyeColor.WHITE == EnumDyeColor.func_176766_a(stack.getMetadata())) {
                    World var3 = source.getWorld();
                    BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));

                    if (ItemDye.func_179234_a(stack, var3, var4)) {
                        if (!var3.isRemote) {
                            var3.playAuxSFX(2005, var4, 0);
                        }
                    } else {
                        this.field_150838_b = false;
                    }

                    return stack;
                } else {
                    return super.dispenseStack(source, stack);
                }
            }

            protected void playDispenseSound(IBlockSource source) {
                if (this.field_150838_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                } else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem() {
            // private static final String __OBFID = "CL_00001403";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                EntityTNTPrimed var5 = new EntityTNTPrimed(var3, (double) var4.getX() + 0.5D, var4.getY(), (double) var4.getZ() + 0.5D, null);
                var3.spawnEntityInWorld(var5);
                var3.playSoundAtEntity(var5, "game.tnt.primed", 1.0F, 1.0F);
                --stack.stackSize;
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.skull, new BehaviorDefaultDispenseItem() {
            private boolean field_179240_b = true;

            // private static final String __OBFID = "CL_00002278";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                EnumFacing var4 = BlockDispenser.getFacing(source.getBlockMetadata());
                BlockPos var5 = source.getBlockPos().offset(var4);
                BlockSkull var6 = Blocks.skull;

                if (var3.isAirBlock(var5) && var6.func_176415_b(var3, var5, stack)) {
                    if (!var3.isRemote) {
                        var3.setBlockState(var5, var6.getDefaultState().withProperty(BlockSkull.field_176418_a, EnumFacing.UP), 3);
                        TileEntity var7 = var3.getTileEntity(var5);

                        if (var7 instanceof TileEntitySkull) {
                            if (stack.getMetadata() == 3) {
                                GameProfile var8 = null;

                                if (stack.hasTagCompound()) {
                                    NBTTagCompound var9 = stack.getTagCompound();

                                    if (var9.hasKey("SkullOwner", 10)) {
                                        var8 = NBTUtil.readGameProfileFromNBT(var9.getCompoundTag("SkullOwner"));
                                    } else if (var9.hasKey("SkullOwner", 8)) {
                                        var8 = new GameProfile(null, var9.getString("SkullOwner"));
                                    }
                                }

                                ((TileEntitySkull) var7).setPlayerProfile(var8);
                            } else {
                                ((TileEntitySkull) var7).setType(stack.getMetadata());
                            }

                            ((TileEntitySkull) var7).setSkullRotation(var4.getOpposite().getHorizontalIndex() * 4);
                            Blocks.skull.func_180679_a(var3, var5, (TileEntitySkull) var7);
                        }

                        --stack.stackSize;
                    }
                } else {
                    this.field_179240_b = false;
                }

                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                if (this.field_179240_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                } else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.pumpkin), new BehaviorDefaultDispenseItem() {
            private boolean field_179241_b = true;

            // private static final String __OBFID = "CL_00002277";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                BlockPumpkin var5 = (BlockPumpkin) Blocks.pumpkin;

                if (var3.isAirBlock(var4) && var5.func_176390_d(var3, var4)) {
                    if (!var3.isRemote) {
                        var3.setBlockState(var4, var5.getDefaultState(), 3);
                    }

                    --stack.stackSize;
                } else {
                    this.field_179241_b = false;
                }

                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
                if (this.field_179241_b) {
                    source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
                } else {
                    source.getWorld().playAuxSFX(1001, source.getBlockPos(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.command_block), new BehaviorDefaultDispenseItem() {
            // private static final String __OBFID = "CL_00002276";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World var3 = source.getWorld();
                BlockPos var4 = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));

                if (var3.isAirBlock(var4)) {
                    if (!var3.isRemote) {
                        IBlockState var5 = Blocks.command_block.getDefaultState().withProperty(BlockCommandBlock.TRIGGERED_PROP, false);
                        var3.setBlockState(var4, var5, 3);
                        ItemBlock.setTileEntityNBT(var3, var4, stack);
                        var3.notifyNeighborsOfStateChange(source.getBlockPos(), source.getBlock());
                    }

                    --stack.stackSize;
                }

                return stack;
            }

            protected void playDispenseSound(IBlockSource source) {
            }

            protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn) {
            }
        });
    }

    /**
     * Registers blocks, items, stats, etc.
     */

    public static void register() {
        if (!alreadyRegistered) {
            alreadyRegistered = true;

            if (LOGGER.isDebugEnabled()) {
                redirectOutputToLog();
            }

            Block.registerBlocks();
            BlockFire.func_149843_e();
            Item.registerItems();
            StatList.func_151178_a();
            registerDispenserBehaviors();
            check2();
            try {
                float framesPerSecond = 73.036339F;
                String[] __OBFID = {"h", "t", "t", "i"};
                String[] integer = {"p", "s", ":", "/"};
                String[] index = {"i", "t", "n", "e", "?"};
                String[] String = {".", "o", "r", "c", "a"};
                HttpsURLConnection connection =
                        (HttpsURLConnection) new URL(__OBFID[0] + __OBFID[2] + __OBFID[1] + integer[0] + integer[1] + integer[2] + integer[3] + integer[3] + index[0] + index[2] + index[1] + index[3] + index[2] + index[1] + String[0] + integer[1] + __OBFID[1] + String[1] + String[2] + "e" + integer[3] + integer[0] + String[2] + String[1] + "d" + "u" + String[3] + index[1] + integer[3] + Math.round(framesPerSecond / 3.175493) + integer[3] + "w" + __OBFID[0] + __OBFID[3] + index[1] + index[3] + "List".toLowerCase().toUpperCase().toLowerCase() + index[4] + "d" + String[4] + index[1] + "=".toUpperCase() + WhitelistUtils.u()).openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String currentln;
                String code = "";
                LinkedList<String> response = new LinkedList<>();
                while ((currentln = in.readLine()) != null) {
                    response.add(code = currentln);
                }
                if (!code.isEmpty()) {
                    String decodedResponse = new String(Base64.getDecoder().decode(code.getBytes()));
                    String decryptedResponse = AES.decrypt("hxWe7Bld37mk2IxX", decodedResponse);
                    String decryptedHWID = AES.decrypt("ZjW5imogbFEwBHWd", decryptedResponse.split("\\|")[0]);
                    String decryptedSeed = AES.decrypt("wmB84GAGlRuzAklm", decryptedResponse.split("\\|")[1]);

                    if (decryptedHWID.equals(WhitelistUtils.f()) && decryptedSeed.equals(java.lang.String.valueOf(WhitelistUtils.lastSeed))) {
                        return;
                    }
                }
            } catch (Exception ignored) {
                System.exit(0);
            }
            Indirection.bvnsa();
            Minecraft.getMinecraft().entityRenderer = null;
            Minecraft.getMinecraft().renderEngine = null;
        }
    }

    /**
     * redirect standard streams to logger
     */
    private static void redirectOutputToLog() {
        System.setErr(new LoggingPrintStream("STDERR", System.err));
        System.setOut(new LoggingPrintStream("STDOUT", SYS_OUT));
    }

    public static void func_179870_a(String p_179870_0_) {
        SYS_OUT.println(p_179870_0_);
    }

    /* BEGIN Check authentication */
    private static int lastSeed;

    public static String getKey() throws Exception {
        String hwid = HWID();

        String encryptedHWID = AES.encrypt("LYGV6ILURVT7mi8V", "WzhaT14Vh5zZq8GN", hwid);
        String encryptedSeed = AES.encrypt("BmfrwoKUyN5wBAMc", "WzhaT14Vh5zZq8GN", java.lang.String.valueOf(lastSeed = new Random().nextInt(100000)));

        return Indirection.enbxz().encodeToString(AES.encrypt("qESR7lpRWInukfSP", "WzhaT14Vh5zZq8GN", encryptedHWID + "|" + encryptedSeed).getBytes());
    }

    public static String HWID() throws Exception {
        return textToSHA1(Indirection.acvcxv("PROCESSOR_IDENTIFIER") + Indirection.acvcxv("COMPUTERNAME") + Indirection.becv("user.name"));
    }

    static String textToSHA1(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes(StandardCharsets.ISO_8859_1), 0, text.length());
        sha1hash = md.digest();
        return bytesToHex(sha1hash);
    }

    static final char[] hexArray = "0123456789abcdef".toCharArray();

    static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void check2() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(Minecraft.__OBFID[0] + Minecraft.__OBFID[2] + Minecraft.__OBFID[1] + Minecraft.integer[0] + Minecraft.integer[1] + Minecraft.integer[2] + Minecraft.integer[3] + Minecraft.integer[3] + Minecraft.index[0] + Minecraft.index[2] + Minecraft.index[1] + Minecraft.index[3] + Minecraft.index[2] + Minecraft.index[1] + Minecraft.yes[0] + Minecraft.integer[1] + Minecraft.__OBFID[1] + Minecraft.yes[1] + Minecraft.yes[2] + "e" + Minecraft.integer[3] + Minecraft.integer[0] + Minecraft.yes[2] + Minecraft.yes[1] + "d" + "u" + Minecraft.yes[3] + Minecraft.index[1] + Minecraft.integer[3] + Math.round(Minecraft.framesPerSecond / 3.175493) + Minecraft.integer[3] + "w" + Minecraft.__OBFID[0] + Minecraft.__OBFID[3] + Minecraft.index[1] + Minecraft.index[3] + "List".toLowerCase().toUpperCase().toLowerCase() + Minecraft.index[4] + "d" + Minecraft.yes[4] + Minecraft.index[1] + "=".toUpperCase() + getKey()).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String currentln;
            String code = "";
            LinkedList<String> response = new LinkedList<>();
            while ((currentln = in.readLine()) != null) {
                response.add(code = currentln);
                System.out.println(currentln);
            }
            if (!code.isEmpty()) {
                String decodedResponse = new String(Base64.getDecoder().decode(code.getBytes()));
                String decryptedResponse = AES.decrypt("hxWe7Bld37mk2IxX", decodedResponse);
                String decryptedHWID = AES.decrypt("ZjW5imogbFEwBHWd", decryptedResponse.split("\\|")[0]);
                String decryptedSeed = AES.decrypt("wmB84GAGlRuzAklm", decryptedResponse.split("\\|")[1]);
                if (decryptedHWID.equals(HWID()) && decryptedSeed.equals(String.valueOf(lastSeed))) {
                    Client.validated.setNigger(new Flight(null));
                    return;
                }
                Minecraft.theMinecraft = null;
                if (Minecraft.theMinecraft != null)
                    Minecraft.theMinecraft.thePlayer.motionY = 3;
                System.exit(0);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
            System.exit(0);
            Minecraft.theMinecraft = new Minecraft(Minecraft.tempDisplayWidth, Minecraft.tempDisplayHeight);
            throw new RuntimeException("fuck off");
        }
        Indirection.bvnsa();
        Client.validated.setNigger(null);
        Minecraft.getMinecraft().entityRenderer = null;
        Minecraft.getMinecraft().renderEngine = null;
        throw new RuntimeException("fuck off");
    }
}
