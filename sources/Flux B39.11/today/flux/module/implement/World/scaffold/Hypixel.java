package today.flux.module.implement.World.scaffold;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import com.soterdev.SoterObfuscator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.Vec3;
import net.minecraft.util.*;
import today.flux.event.*;
import today.flux.module.ModuleManager;
import today.flux.module.SubModule;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.World.Scaffold;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.*;
import today.flux.utility.tojatta.api.utilities.angle.AngleUtility;

import java.util.Arrays;
import java.util.List;

import static today.flux.module.implement.World.Scaffold.isScaffoldBlock;

public class Hypixel extends SubModule {
    public static AngleUtility angleUtility = new AngleUtility(110, 120, 30, 40);
    public static FloatValue timerBoost = new FloatValue("Scaffold", "Hypixel Timer Boost", 1.2f, 0.7f, 2f, 0.01f);
    public static BooleanValue keepY = new BooleanValue("Scaffold", "Hypixel Keep Y", true);
    public static List<Block> blacklistedBlocks;
    public static TimeHelper isScaffolding = new TimeHelper();
    private DelayTimer placeTimer = new DelayTimer();
    private DelayTimer clickTimer = new DelayTimer();
    private boolean rotated;
    BlockData data = null;
    Vec3 lastVec3;
    float yaw;
    float pitch;
    double playerY;

    static {
        blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
                Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane,
                Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice,
                Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest,
                Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
                Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore,
                Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate,
                Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button,
                Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook,
                Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom,
                Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand,
                Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web,
                Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence, Blocks.redstone_torch);
    }

    public Hypixel() {
        super("Hypixel", "Scaffold");
    }

    @Override
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void onEnable() {
        playerY = mc.thePlayer.posY;
        slots = mc.thePlayer.inventory.currentItem;
        curRotate = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch - 0.5f};
        ticks = 0;
        placeTimer.reset();

        super.onEnable();
    }

    @Override
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void onDisable() {
        if (slots != mc.thePlayer.inventory.currentItem) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }

        if (mc.theWorld == null)
            return;
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }

    float[] curRotate;
    int keepRotationTicks;
    @EventTarget(Priority.HIGHEST) // scaffold->aura
    private void onUpdate(PreUpdateEvent event) {
        if (KillAura.blocked) {
            KillAura.blocked = false;
            ChatUtils.debug("UNBLOCKED in Scaffold");
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
        }

        ModuleManager.killAuraMod.disableHelper.reset();

        //Bps Limited

        //BlockData
        if (hasBlock()) {
            data = null;
            findBlockData();
            if (data != null) {
                // Rotation
                lastVec3 = data.hitVec;
                rotated = true;
                keepRotationTicks = 0;
            } else {
                if (++ keepRotationTicks > 15)
                    playerY = (int) mc.thePlayer.posY;
            }
        }

        RotationUtils.Rotation rotation = toRotation(lastVec3, false);
        event.setYaw(rotation.getYaw());
        event.setPitch(rotation.getPitch());
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public static float getYaw() {
        float yaw = 0;

        if (mc.gameSettings.keyBindForward.isKeyDown())
            yaw = mc.thePlayer.rotationYaw + 180;
        if (mc.gameSettings.keyBindLeft.isKeyDown())
            yaw = mc.thePlayer.rotationYaw + 90;
        if (mc.gameSettings.keyBindRight.isKeyDown())
            yaw = mc.thePlayer.rotationYaw - 90;
        if (mc.gameSettings.keyBindBack.isKeyDown())
            yaw = mc.thePlayer.rotationYaw;

        if (mc.gameSettings.keyBindForward.isKeyDown() && mc.gameSettings.keyBindLeft.isKeyDown())
            yaw = mc.thePlayer.rotationYaw + 90 + 45;
        if (mc.gameSettings.keyBindForward.isKeyDown() && mc.gameSettings.keyBindRight.isKeyDown())
            yaw = mc.thePlayer.rotationYaw - 90 - 45;

        if (mc.gameSettings.keyBindBack.isKeyDown() && mc.gameSettings.keyBindLeft.isKeyDown())
            yaw = mc.thePlayer.rotationYaw + 90 - 45;
        if (mc.gameSettings.keyBindBack.isKeyDown() && mc.gameSettings.keyBindRight.isKeyDown())
            yaw = mc.thePlayer.rotationYaw - 90 + 45;

        if (!PlayerUtils.isMoving())
            yaw = mc.thePlayer.rotationYaw + 180;

        return yaw;
    }

    public RotationUtils.Rotation toRotation(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        if (predict)
            eyesPos.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);

        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;

        return new RotationUtils.Rotation(MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
                MathHelper.wrapAngleTo180_float(
                        (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    @EventTarget
    private void onTick(TickEvent event) {
        final int slot = this.getBlockFromInventory();

        if (slot == -1)
            return;

        if (getHotbarBlocksLeft() < 2 && this.getBlockFromInventory() != -1) {
            if (clickTimer.isDelayComplete(200)) {
                this.swap(this.getBlockFromInventory(), findEmptySlot());
                clickTimer.reset();
            }
        }
    }

    public int findEmptySlot() {
        for (int i = 0; i < 8; i++) {
            if (mc.thePlayer.inventory.mainInventory[i] == null)
                return i;
        }

        return mc.thePlayer.inventory.currentItem + (mc.thePlayer.inventory.getCurrentItem() == null ? 0
                : ((mc.thePlayer.inventory.currentItem < 8) ? 1 : -1));
    }

    int ticks;
    int slots;

    @EventTarget
    public void onPacket(PacketSendEvent e) {
        if (e.getPacket() instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange packet = ((C09PacketHeldItemChange) e.getPacket());
            slots = packet.getSlotId();
            ChatUtils.debug(slots);
        }
    }

    TimeHelper slowdownTimer = new TimeHelper();
    int hotbar;

    // @SoterObfuscator.Obfuscation(flags = "+native")
    @EventTarget(Priority.LOW)
    private void onPostUpdate(PostUpdateEvent e) {
        if (!rotated)
            return;

        rotated = false;

        hotbar = this.getBlockFromHotbar();

        if (hotbar == -1) // no blocks in hotbar!
            return;

        if (slots != hotbar) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(hotbar));
        }

        ItemStack stack = this.mc.thePlayer.inventory.getStackInSlot(hotbar);


        if (this.data != null && stack != null
                && stack.getItem() instanceof ItemBlock
                && stack.stackSize > 0) {
            if (this.placeTimer.isDelayComplete(0)) {
                this.placeTimer.reset();
                ChatUtils.debug(data.face);
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, stack, data.pos, data.face, data.hitVec)) {
                    slowdownTimer.reset();
                    if (Scaffold.swing.getValueState()) {
                        mc.thePlayer.swingItem();
                    } else {
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0APacketAnimation());
                    }
                }
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                // if (!noSlowdown.getValueState())
                isScaffolding.reset();
            }
        }

    }

    private int getHotbarBlocksLeft() {
        return InvUtils.getHotbarContent().stream().filter(Scaffold::isScaffoldBlock)
                .mapToInt(itemStack -> itemStack.stackSize).sum();
    }

    private int getBlockFromInventory() {
        int biggest = 0;
        int biggestSlot = -1;
        for (int i = 9; i < 36; ++i) {
            final ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];

            if (!isScaffoldBlock(itemStack))
                continue;

            if (biggest < itemStack.stackSize) {
                biggest = itemStack.stackSize;
                biggestSlot = i;
            }
        }

        return biggestSlot;
    }

    /**
     * Anti ZERO
     **/
    @EventTarget
    private void onLoop(LoopEvent event) {
        for (int i = 0; i < 8; i++) {
            if (this.mc.thePlayer.inventory.mainInventory[i] != null
                    && this.mc.thePlayer.inventory.mainInventory[i].stackSize <= 0)
                this.mc.thePlayer.inventory.mainInventory[i] = null;
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    private void swap(final int slot, final int hotbarNum) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
                this.mc.thePlayer);
    }

    int lastSlot;
    TimeHelper blockTimer = new TimeHelper();

    private int getBlockFromHotbar() {
        if (blockTimer.isDelayComplete(5000) || mc.thePlayer.inventory.getStackInSlot(hotbar).stackSize < 2) {
            int biggest = 0;
            int biggestSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];

                if (!isScaffoldBlock(itemStack))
                    continue;

                if (biggest < itemStack.stackSize) {
                    biggest = itemStack.stackSize;
                    biggestSlot = i;
                }
            }
            blockTimer.reset();
            lastSlot = biggestSlot;
            return biggestSlot;
        } else {
            return lastSlot;
        }
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        if (ServerUtils.INSTANCE.isOnHypixel()) {
            if (!isScaffolding.isDelayComplete(500)) {
                MoveUtils.setMotion(e, 0.195);
            } else {
                mc.timer.timerSpeed = 1;
            }

            if (keepY.getValue() && !MoveUtils.isOnGround(0.01)) {
                mc.timer.timerSpeed = timerBoost.getValue();
                MoveUtils.setMotion(e, 0.24);
            } else {
                mc.timer.timerSpeed = 1;
            }
        }

        if (!Scaffold.safewalk.getValue() && ModuleManager.scaffoldMod.getBlocksCount() > 0
                || mc.gameSettings.keyBindSneak.pressed)
            return;

        e.setSafeWalk(true);
    }

    private boolean hasBlock() {
        int BlockInInventory = findBlock(9, 36);
        int BlockInHotbar = findBlock(36, 45);

        if (BlockInInventory == -1 && BlockInHotbar == -1) {
            return false;
        }
        return true;
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    private int findBlock(int startSlot, int endSlot) {
        int i = startSlot;
        while (i < endSlot) {
            ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBlock
                    && ((ItemBlock) stack.getItem()).getBlock().isFullBlock()) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public boolean isAirBlock(Block block) {
        return block.getMaterial().isReplaceable() && (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125D);
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    BlockPos blockBelow;

    public void findBlockData() {
        if ((mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.isMoving2()) || !keepY.getValue()) {
            playerY = mc.thePlayer.posY;
        }

        double yPos = MathHelper.floor_double(playerY) - 1;
        boolean air = isAirBlock(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, yPos, mc.thePlayer.posZ)).getBlock());
        double xPos = air ? mc.thePlayer.posX : getExpandCoords(yPos)[0], zPos = air ? mc.thePlayer.posZ : getExpandCoords(yPos)[1];
        blockBelow = new BlockPos(xPos, yPos, zPos);

        boolean setBlockData = mc.theWorld.getBlockState(blockBelow).getBlock().getMaterial().isReplaceable() || mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air;
        data = setBlockData ? getBlockData(blockBelow) : null;
    }

    public double[] getExpandCoords(double y) {
        BlockPos underPos = new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ);
        Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        MovementInput movementInput = mc.thePlayer.movementInput;
        float forward = movementInput.moveForward, strafe = movementInput.moveStrafe, yaw = mc.thePlayer.rotationYaw;
        double xCalc = -999, zCalc = -999, dist = 0, expandDist = 0.0;

        while (!isAirBlock(underBlock)) {
            xCalc = mc.thePlayer.posX;
            zCalc = mc.thePlayer.posZ;
            dist++;
            if (dist > expandDist) dist = expandDist;
            xCalc += (forward * 0.45 * MathHelper.cos((float) Math.toRadians(yaw + 90.0f)) + strafe * 0.45 * MathHelper.sin((float) Math.toRadians(yaw + 90.0f))) * dist;
            zCalc += (forward * 0.45 * MathHelper.sin((float) Math.toRadians(yaw + 90.0f)) - strafe * 0.45 * MathHelper.cos((float) Math.toRadians(yaw + 90.0f))) * dist;
            if (dist == expandDist) break;
            underPos = new BlockPos(xCalc, y, zCalc);
            underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        }

        return new double[]{xCalc, zCalc};
    }

    private BlockData getBlockData(BlockPos pos) {
        if (isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos1 = pos.add(-1, 0, 0);

        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos2 = pos.add(1, 0, 0);

        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos3 = pos.add(0, 0, 1);

        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos4 = pos.add(0, 0, -1);

        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos19 = pos.add(-2, 0, 0);

        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos29 = pos.add(2, 0, 0);

        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos39 = pos.add(0, 0, 2);

        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos49 = pos.add(0, 0, -2);

        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos5 = pos.add(0, -1, 0);

        if (isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos6 = pos5.add(1, 0, 0);

        if (isPosSolid(pos6.add(0, -1, 0))) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos6.add(-1, 0, 0))) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos6.add(1, 0, 0))) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos6.add(0, 0, 1))) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos6.add(0, 0, -1))) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos7 = pos5.add(-1, 0, 0);

        if (isPosSolid(pos7.add(0, -1, 0))) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos7.add(-1, 0, 0))) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos7.add(1, 0, 0))) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos7.add(0, 0, 1))) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos7.add(0, 0, -1))) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos8 = pos5.add(0, 0, 1);

        if (isPosSolid(pos8.add(0, -1, 0))) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos8.add(-1, 0, 0))) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos8.add(1, 0, 0))) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos8.add(0, 0, 1))) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos8.add(0, 0, -1))) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }

        BlockPos pos9 = pos5.add(0, 0, -1);

        if (isPosSolid(pos9.add(0, -1, 0))) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        } else if (isPosSolid(pos9.add(-1, 0, 0))) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        } else if (isPosSolid(pos9.add(1, 0, 0))) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        } else if (isPosSolid(pos9.add(0, 0, 1))) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        } else if (isPosSolid(pos9.add(0, 0, -1))) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    private boolean isPosSolid(BlockPos pos) {
        final Block block = mc.theWorld.getBlockState(pos).getBlock();
        return !blacklistedBlocks.contains(block);
    }

    public static Vec3 getVec3(BlockPos pos, EnumFacing facing) {
        Vec3 vector = new Vec3(pos);
        double random = 0;

        if (facing == EnumFacing.NORTH) {
            vector.xCoord = mc.thePlayer.posX + random * 0.01;
        } else if (facing == EnumFacing.SOUTH) {
            vector.xCoord = mc.thePlayer.posX + random * 0.01;
            vector.zCoord += 1.0;
        } else if (facing == EnumFacing.WEST) {
            vector.zCoord = mc.thePlayer.posZ + random * 0.01;
        } else if (facing == EnumFacing.EAST) {
            vector.zCoord = mc.thePlayer.posZ + random * 0.01;
            vector.xCoord += 1.0;
        }

        if (facing == EnumFacing.UP) {
            vector.xCoord += random;
            vector.zCoord += random;
            vector.yCoord += 1.0;
        } else {
            vector.yCoord += random;
        }

        return vector;
    }

    class BlockData {
        public BlockPos pos;
        public EnumFacing face;
        public Vec3 hitVec;

        public BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
            this.hitVec = getVec3(pos, face);
        }
    }
}