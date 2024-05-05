package client.module.impl.other;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.MotionEvent;
import client.event.impl.other.Render2DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.*;
import java.util.List;
import java.util.*;

@ModuleInfo(name = "Scaffold", description = "Scaffold", category = Category.OTHER)
public class Scaffold extends Module {

    private final float[] rotations = new float[2];
    private static final Map<Integer, Boolean> glCapMap = new HashMap<>();
    private final List<Block> badBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);
    private BlockData blockData;
    private static BlockPos currentPos;
    private EnumFacing currentFacing;
    private boolean rotated = false;
    int stage = 0;
    float yaw = 0;
    float pitch = 0;
    public static boolean isPlaceTick = false;
    public static boolean stopWalk = false;
    private double startY;
    int slotIndex = 0;
    private final TimeUtil timer = new TimeUtil();

    @EventLink
    public final Listener<Render2DEvent> onRender2DEventListener = event -> {
        if (true) {
            ScaledResolution sr = new ScaledResolution(mc);
            int blockCount = 0;
            for (int i = 0; i < 45; ++i) {
                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                    continue;
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (!(is.getItem() instanceof ItemBlock) || !InventoryUtils.isValidBlock(((ItemBlock) item).getBlock(), false))
                    continue;
                blockCount += is.stackSize;
            }
            int color = new Color(255, 0, 0).getRGB();
            int bgcolor = new Color(1,1,1).getRGB();
            if (blockCount >= 64 && 128 > blockCount) {
                color = new Color(255, 255, 0).getRGB();
            } else if (blockCount >= 128) {
                color = new Color(0, 255, 0).getRGB();
            }

            GlStateManager.pushMatrix();
            mc.fontRendererObj.drawString(Integer.toString(blockCount), (sr.getScaledWidth() >> 1)  - mc.fontRendererObj.getStringWidth(Integer.toString(blockCount)) / 2 + 1, (sr.getScaledHeight() >> 1) - 15, bgcolor);
            mc.fontRendererObj.drawString(Integer.toString(blockCount), (sr.getScaledWidth() >> 1)  - mc.fontRendererObj.getStringWidth(Integer.toString(blockCount)) / 2 - 1, (sr.getScaledHeight() >> 1) - 15, bgcolor);
            mc.fontRendererObj.drawString(Integer.toString(blockCount), (sr.getScaledWidth() >> 1)  - mc.fontRendererObj.getStringWidth(Integer.toString(blockCount)) / 2, (sr.getScaledHeight() >> 1) - 15 + 1, bgcolor);
            mc.fontRendererObj.drawString(Integer.toString(blockCount), (sr.getScaledWidth() >> 1)  - mc.fontRendererObj.getStringWidth(Integer.toString(blockCount)) / 2, (sr.getScaledHeight() >> 1) - 15 - 1, bgcolor);
            mc.fontRendererObj.drawString(Integer.toString(blockCount), (sr.getScaledWidth() >> 1)  - mc.fontRendererObj.getStringWidth(Integer.toString(blockCount)) / 2, (sr.getScaledHeight() >> 1) - 15, color);
            GlStateManager.popMatrix();
        }
    };
    @EventLink
    public final Listener<MotionEvent> onMotion = event -> {

        int slot = this.getSlot ();

        isPlaceTick = blockData != null && slot != -1;
        this.blockData = getBlockData ();
        if (this.blockData == null) {
            return;
        }
        if (isPlaceTick) {
            Rotation targetRotation = new Rotation ( setBlockAndFacing.BlockUtil.getDirectionToBlock ( blockData.getPosition ().getX (), blockData.getPosition ().getY (), blockData.getPosition ().getZ (), blockData.getFacing () )[0], 79.44f );
            Rotation limitedRotation = setBlockAndFacing.BlockUtil.limitAngleChange ( new Rotation ( yaw, event.getPitch () ), targetRotation, (float) ThreadLocalRandom.current ().nextDouble ( 20, 30 ) );
            yaw = getRotations(blockData.getPosition(), blockData.getFacing())[0];
            pitch = limitedRotation.getPitch ();



                event.setYaw(yaw);
            if (MoveUtil.isMoving()) {
                mc.thePlayer.setSprinting(true);
            }

                event.setPitch((float) (84));
            event.setYaw((float) (45));






            rotated = false;
            currentPos = null;
            currentFacing = null;

            BlockPos pos = new BlockPos ( mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ );
            if (mc.theWorld.getBlockState ( pos ).getBlock () instanceof BlockAir) {
                setBlockAndFacing ( pos );

                if (currentPos != null) {
                    float[] facing = setBlockAndFacing.BlockUtil.getDirectionToBlock ( currentPos.getX (), currentPos.getY (), currentPos.getZ (), currentFacing );

                    float yaw = facing[0] + randomNumber ( 3, -3 );
                    float pitch = Math.min ( 90, facing[1] + 9 + randomNumber ( 3, -3 ) );

                    rotations[0] = yaw;
                    rotations[1] = pitch;

                    rotated = true;


                }
            } else {


                yaw = getRotations(blockData.getPosition(), blockData.getFacing())[0];
                pitch = limitedRotation.getPitch ();





                        event.setYaw(yaw);
                        if(mc.thePlayer.onGround) {
                            event.setPitch((float) (84));
                        }
            }

        }
        mc.thePlayer.rotationYawHead = event.getYaw ();
        mc.thePlayer.renderYawOffset = event.getYaw ();

        if (MoveUtil.isMoving()) {
                mc.thePlayer.setSprinting(true);

                    MoveUtil.setSpeed(0.25);


                    mc.gameSettings.keyBindSprint.setPressed(true);
                }



            int ass = this.getSlot ();
            double x = mc.thePlayer.posX;
            double z = mc.thePlayer.posZ;
            double forward = MovementInput.moveForward;
            double strafe = MovementInput.moveStrafe;
            float YAW = mc.thePlayer.rotationYaw;

            BlockPos pos = new BlockPos ( x, mc.thePlayer.posY - 1, z );
            if (ass != -1 && this.blockData != null) {
                final int currentSlot = mc.thePlayer.inventory.currentItem;

                mc.thePlayer.inventory.currentItem = ass;
                if (this.getPlaceBlock(this.blockData.getPosition(), this.blockData.getFacing())) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
                }


                    mc.thePlayer.inventory.currentItem = currentSlot;


            }
    };

    private boolean getPlaceBlock(final BlockPos pos, final EnumFacing facing) {
        final Vec3 eyesPos = new Vec3( mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        Vec3i data = this.blockData.getFacing().getDirectionVec();
        if (timer.hasTimePassed( 0)){
            if (mc.playerController.onPlayerRightClick( mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, facing, getVec3(new BlockData(pos, facing)))) {
                if (true) {
                    //mc.thePlayer.swingItem();

                } else {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }

                timer.reset();
                return true;
            }


        }
        return false;
    }

    private Vec3 getVec3(BlockData data) {
        BlockPos pos = data.getPosition();
        EnumFacing face = data.getFacing();
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + 0.5D;
        double z = (double) pos.getZ() + 0.5D;
        x += (double) face.getFrontOffsetX() / 2.0D;
        z += (double) face.getFrontOffsetZ() / 2.0D;
        y += (double) face.getFrontOffsetY() / 2.0D;
        if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            y += this.randomNumber(0.49D, 0.5D);
        } else {
            x += this.randomNumber(0.3D, -0.3D);
            z += this.randomNumber(0.3D, -0.3D);
        }

        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += this.randomNumber(0.3D, -0.3D);
        }

        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += this.randomNumber(0.3D, -0.3D);
        }

        return new Vec3(x, y, z);
    }

    private double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static boolean rayTrace(float yaw, float pitch) {
        Vec3 vec3 = Minecraft.getMinecraft().thePlayer.getPositionEyes(1.0f);
        Vec3 vec31 = RotationUtil.getVectorForRotation (new float[]{yaw, pitch});
        Vec3 vec32 = vec3.addVector(vec31.xCoord * 5, vec31.yCoord * 5, vec31.zCoord * 5);


        MovingObjectPosition result = Minecraft.getMinecraft().theWorld.rayTraceBlocks(vec3, vec32, false);


        return result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && currentPos.equals(result.getBlockPos());
    }

    static Random rng = new Random();

    public static int getRandom(final int floor, final int cap) {
        return floor + rng.nextInt(cap - floor + 1);
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f);
    }

    public static float[] getRotations(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5 -  Minecraft.getMinecraft().thePlayer.posX;
        double z = block.getZ() + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
        double y = (block.getY() + 0.2);
        double d1 = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360f;
        }

        return new float[]{yaw, pitch};
    }

    public void setBlockAndFacing(BlockPos var1) {


        if (mc.theWorld.getBlockState(var1.add(0, -1, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(0, -1, 0);
            currentFacing = EnumFacing.UP;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 0, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, 0, 0);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 0, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, 0, 0);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, -1)).getBlock() != Blocks.air) {

            currentPos = var1.add(0, 0, -1);
            currentFacing = EnumFacing.SOUTH;

        } else if (mc.theWorld.getBlockState(var1.add(0, 0, 1)).getBlock() != Blocks.air) {

            currentPos = var1.add(0, 0, 1);
            currentFacing = EnumFacing.NORTH;

        } else if (mc.theWorld.getBlockState(var1.add(-1, 0, -1)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, 0, -1);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 0, 1)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, 0, 1);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 0, -1)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, 0, -1);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 0, 1)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, 0, 1);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(-1, -1, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, -1, 0);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, -1, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, -1, 0);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, -1, -1)).getBlock() != Blocks.air) {
            currentPos = var1.add(0, -1, -1);
            currentFacing = EnumFacing.SOUTH;
        } else if (mc.theWorld.getBlockState(var1.add(0, -1, 1)).getBlock() != Blocks.air) {
            currentPos = var1.add(0, -1, 1);
            currentFacing = EnumFacing.NORTH;
        } else if (mc.theWorld.getBlockState(var1.add(-1, -1, -1)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, -1, -1);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(-1, -1, 1)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, -1, 1);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, -1, -1)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, -1, -1);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(1, -1, 1)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, -1, 1);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(-2, 0, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(-2, 0, 0);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(2, 0, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(2, 0, 0);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, -2)).getBlock() != Blocks.air) {
            currentPos = var1.add(0, 0, -2);
            currentFacing = EnumFacing.SOUTH;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, 2)).getBlock() != Blocks.air) {
            currentPos = var1.add(0, 0, 2);
            currentFacing = EnumFacing.NORTH;
        } else if (mc.theWorld.getBlockState(var1.add(-2, 0, -2)).getBlock() != Blocks.air) {
            currentPos = var1.add(-2, 0, -2);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(-2, 0, 2)).getBlock() != Blocks.air) {
            currentPos = var1.add(-2, 0, 2);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(2, 0, -2)).getBlock() != Blocks.air) {
            currentPos = var1.add(2, 0, -2);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(2, 0, 2)).getBlock() != Blocks.air) {
            currentPos = var1.add(2, 0, 2);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, 1, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(0, 1, 0);
            currentFacing = EnumFacing.DOWN;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 1, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, 1, 0);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 1, 0)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, 1, 0);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, 1, -1)).getBlock() != Blocks.air) {
            currentPos = var1.add(0, 1, -1);
            currentFacing = EnumFacing.SOUTH;
        } else if (mc.theWorld.getBlockState(var1.add(0, 1, 1)).getBlock() != Blocks.air) {
            currentPos = var1.add(0, 1, 1);
            currentFacing = EnumFacing.NORTH;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 1, -1)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, 1, -1);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 1, 1)).getBlock() != Blocks.air) {
            currentPos = var1.add(-1, 1, 1);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 1, -1)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, 1, -1);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 1, 1)).getBlock() != Blocks.air) {
            currentPos = var1.add(1, 1, 1);
            currentFacing = EnumFacing.WEST;
        }
    }



    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.keyBindJump.setPressed(false);
        this.setSneaking(false);
        mc.timer.timerSpeed = 1f;
    }

    private void setSneaking(boolean b) {
        KeyBinding sneakBinding = mc.gameSettings.keyBindSneak;
        mc.gameSettings.keyBindSneak.setPressed(b);
    }

    public BlockData getBlockData() {
        final EnumFacing[] invert = {EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
        double yValue = 0;
    /*if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && !mc.gameSettings.keyBindJump.isKeyDown() && blockfly.getValue ().booleanValue ()) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        yValue -= 0.6;
    }*/
        BlockPos aa = new BlockPos( mc.thePlayer.getPositionVector()).offset(EnumFacing.DOWN).add(0, yValue, 0);
        BlockPos playerpos = aa;

     //   if (/*!this.blockfly.getValue ().booleanValue () &&*/ this.keepy.getValue ().booleanValue () && !tower && mc.thePlayer.isMoving()) {
            playerpos = new BlockPos(new Vec3( mc.thePlayer.getPositionVector().xCoord, this.startY, mc.thePlayer.getPositionVector().zCoord)).offset(EnumFacing.DOWN);
        // } else {
        //       this.startY = mc.thePlayer.posY;
        //  }




        for (EnumFacing facing : EnumFacing.values()) {
            if (playerpos.offset(facing).getBlock().getMaterial() != Material.air) {
                return new BlockData(playerpos.offset(facing), invert[facing.ordinal()]);
            }
        }
        final BlockPos[] addons = {
                new BlockPos(-1, 0, 0),
                new BlockPos(1, 0, 0),
                new BlockPos(0, 0, -1),
                new BlockPos(0, 0, 1)};

        for (int length2 = addons.length, j = 0; j < length2; ++j) {
            final BlockPos offsetPos = playerpos.add(addons[j].getX(), 0, addons[j].getZ());
            if (mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                for (int k = 0; k < EnumFacing.values().length; ++k) {
                    if (mc.theWorld.getBlockState(offsetPos.offset(EnumFacing.values()[k])).getBlock().getMaterial() != Material.air) {

                        return new BlockData(offsetPos.offset(EnumFacing.values()[k]), invert[EnumFacing.values()[k].ordinal()]);
                    }
                }
            }
        }

        return null;
    }







    private int getSlot() {
        ArrayList<Integer> slots = new ArrayList<>();
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null && this.isValid(itemStack) && itemStack.stackSize >= 2) {
                slots.add(k);
            }
        }
        if (slots.isEmpty()) {
            return -1;
        }

        return slots.get(slotIndex);
    }

    private boolean isValid(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemBlock) {
            boolean isBad = false;

            ItemBlock block = (ItemBlock) itemStack.getItem();
            for (int i = 0; i < this.badBlocks.size(); i++) {
                if (block.getBlock().equals(this.badBlocks.get(i))) {
                    isBad = true;
                }
            }

            return !isBad;
        }
        return false;
    }

    public int getBlockCount() {
        int count = -1;
        for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; ++k) {
            final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null && this.isValid(itemStack) && itemStack.stackSize >= 1) {
                count += itemStack.stackSize;
            }
        }
        return count;
    }

    public class BlockData {
        private final BlockPos blockPos;
        private final EnumFacing enumFacing;

        public BlockData(final BlockPos blockPos, final EnumFacing enumFacing) {
            this.blockPos = blockPos;
            this.enumFacing = enumFacing;
        }

        public EnumFacing getFacing() {
            return this.enumFacing;
        }

        public BlockPos getPosition() {
            return this.blockPos;
        }
    }

    private void moveBlocksToHotbar() {
        boolean added = false;
        if (!isHotbarFull()) {
            for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; ++k) {
                if (k > 8 && !added) {
                    final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
                    if (itemStack != null && this.isValid(itemStack)) {
                        shiftClick(k);
                        added = true;
                    }
                }
            }
        }
    }

    public boolean isHotbarFull() {
        int count = 0;
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null) {
                count++;
            }
        }
        return count == 8;
    }

    public float setSmooth(float current, float target, float speed) {
        if (target - current > 0) {
            current -= speed;
        } else {
            current += speed;
        }
        return current;
    }

    public static void shiftClick(int slot) {
        Minecraft.getMinecraft().playerController.windowClick( Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer );
        Minecraft.getMinecraft().playerController.windowClick( Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer );
    }

    public boolean isAirBlock(Block block) {
        if (block.getMaterial().isReplaceable()) {
            return !(block instanceof BlockSnow) || !(block.getBlockBoundsMaxY() > 0.125);
        }

        return false;
    }

    public static int randomNumber(int max, int min) {
        return Math.round(min + (float) Math.random() * ((max - min)));
    }


    private boolean isOnEdgeWithOffset(double paramDouble) {
        double d1 = mc.thePlayer.posX;
        double d2 = mc.thePlayer.posY;
        double d3 = mc.thePlayer.posZ;
        BlockPos blockPos1 = new BlockPos(d1 - paramDouble, d2 - 0.5D, d3 - paramDouble);
        BlockPos blockPos2 = new BlockPos(d1 - paramDouble, d2 - 0.5D, d3 + paramDouble);
        BlockPos blockPos3 = new BlockPos(d1 + paramDouble, d2 - 0.5D, d3 + paramDouble);
        BlockPos blockPos4 = new BlockPos(d1 + paramDouble, d2 - 0.5D, d3 - paramDouble);
        return (mc.thePlayer.worldObj.getBlockState(blockPos1).getBlock() == Blocks.air && mc.thePlayer.worldObj.getBlockState(blockPos2).getBlock() == Blocks.air && mc.thePlayer.worldObj.getBlockState(blockPos3).getBlock() == Blocks.air && mc.thePlayer.worldObj.getBlockState(blockPos4).getBlock() == Blocks.air);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        timer.reset();
        if (mc.thePlayer !=null) {
            startY = mc.thePlayer.posY;
        }

    }


}
