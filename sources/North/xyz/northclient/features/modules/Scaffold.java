package xyz.northclient.features.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import xyz.northclient.NorthSingleton;
import xyz.northclient.draggable.impl.Watermark;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.*;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.ModeValue;
import xyz.northclient.util.MoveUtil;
import xyz.northclient.util.RotationUtil;
import xyz.northclient.util.killaura.RotationsUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@ModuleInfo(name = "Scaffold", description = "very fast", category = Category.WORLD, keyCode = Keyboard.KEY_X)
public class Scaffold extends AbstractModule {

    public DoubleValue range = new DoubleValue("Search range", this)
            .setMin(1)
            .setMax(6)
            .setIncrement(1)
            .setDefault(4);

    public DoubleValue delay = new DoubleValue("Place delay", this)
            .setMin(0)
            .setMax(3)
            .setIncrement(1)
            .setDefault(0);

    public ModeValue clickMode = new ModeValue("Click Mode", this)
            .add(new Watermark.StringMode("Controller", this))
            .add(new Watermark.StringMode("Real Click", this))
            .setDefault("Real Click");

    public ModeValue timing = new ModeValue("Place timing", this)
            .add(new Watermark.StringMode("Legit", this))
            .add(new Watermark.StringMode("Pre", this))
            .add(new Watermark.StringMode("Post", this))
            .setDefault("Legit");

    public ModeValue sprint = new ModeValue("Sprint", this)
            .add(new Watermark.StringMode("Allow", this))
            .add(new Watermark.StringMode("Auto-sprint", this))
            .add(new Watermark.StringMode("Semi-disabled", this))
            .add(new Watermark.StringMode("Disabled", this))
            .add(new Watermark.StringMode("Watchdog Fast", this))
            .add(new Watermark.StringMode("Timer Bypass", this))
            .add(new Watermark.StringMode("NCP KeepY", this))
            .add(new Watermark.StringMode("BlocksMC", this))
            .add(new Watermark.StringMode("MMC", this))

            .setDefault("Allow");
    public ModeValue rotations = new ModeValue("Rotations", this)
            .add(new Watermark.StringMode("Hypixel", this))
            .add(new Watermark.StringMode("Back", this))
            .add(new Watermark.StringMode("Normal", this))
            .add(new Watermark.StringMode("Disabled", this))
            .setDefault("Hypixel");

    public static float rand(float x,float y) {
        float rand = (float)ThreadLocalRandom.current().nextDouble(x,y);
        return rand;
    }
    public DoubleValue timerSpeed = new DoubleValue("Timer speed", this)
            .setDefault(1)
            .setMin(0.01)
            .setIncrement(0.01)
            .setMax(10);

    public BoolValue swingItem = new BoolValue("Swing item", this)
            .setDefault(false);
    public BoolValue spoofItem = new BoolValue("Spoof item", this)
            .setDefault(true);
    public BoolValue keepRotation = new BoolValue("Keep rotation", this)
            .setDefault(true);
    public BoolValue clientSideRot = new BoolValue("Client-side rotation", this)
            .setDefault(true);
    public BoolValue keepY = new BoolValue("Keep Y", this)
            .setDefault(false);
    public BoolValue downwards = new BoolValue("Downwards", this)
            .setDefault(false);
    public BoolValue correctMovement = new BoolValue("Correct Movement", this)
            .setDefault(false);
    public BoolValue extraClick = new BoolValue("Extra click", this)
            .setDefault(false);
    public BoolValue rayCast = new BoolValue("RayCast", this)
            .setDefault(true);
    public BlockData blockData, prevBlockData;
    public int slot, lastSlot, slotBefore, ticks;
    public double lastY;
    private RotationsUtil fixedRotations;
    public boolean rotated = false;

    @Override
    public void onEnable() {
        super.onEnable();

        fixedRotations = new RotationsUtil(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        slotBefore = mc.thePlayer.inventory.currentItem;

        prevBlockData = null;
        blockData = null;
        rotated = false;

        slot = -1;
        ticks = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (!spoofItem.get().booleanValue())
            mc.thePlayer.inventory.currentItem = slotBefore;

        mc.timer.timerSpeed = 1;
    }

    @EventLink
    public void onTick(TickEvent e) {
        ticks++;

        mc.timer.timerSpeed = timerSpeed.get().floatValue();

        switch (sprint.get().getName()) {
            case "Allow":
                break;
            case "Auto-sprint":
                if (MoveUtil.isMoving()) {
                    mc.thePlayer.setSprinting(true);
                }
                break;
            case "Semi-disabled":
                mc.thePlayer.setSprinting(false);
                break;
            case "MMC":
                mc.thePlayer.setSprinting(false);
                mc.timer.timerSpeed = rand(0.9f,1.1f);
                break;
            case "Watchdog Fast":
                mc.thePlayer.setSprinting(false);
                if (mc.thePlayer.onGround || mc.thePlayer.getHealth() == 20)
                    mc.timer.timerSpeed = rand(0.9f,1.3f);
                else {
                    mc.timer.timerSpeed = 1.0f;
                }
                break;
            case "Timer Bypass":
                mc.thePlayer.setSprinting(false);
                if (mc.thePlayer.onGround || mc.thePlayer.getHealth() == 20)
                    mc.timer.timerSpeed = 1.2f;
                else {
                    mc.timer.timerSpeed = 0.9f;
                }
                break;
            case "NCP KeepY":
                mc.thePlayer.setSprinting(true);
            case "BlocksMC":
                mc.thePlayer.setSprinting(true);
                mc.timer.timerSpeed = rand(0.7f,1.5f);
            case "Disabled":
                mc.gameSettings.keyBindSprint.pressed = false;
                mc.thePlayer.setSprinting(false);
                break;
        }

        if (downwards.get().booleanValue()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            mc.thePlayer.movementInput.sneak = false;
        }

        if (mc.thePlayer.onGround && keepY.get().booleanValue()) {
            lastY = Math.floor(mc.thePlayer.posY - 1.0);
            if (MoveUtil.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown() && !GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
                mc.thePlayer.jump();
            }
        }
    }

    @EventLink
    public void onMove(MotionEvent e) {
        blockData = getBlockData();

        if (keepRotation.get().booleanValue()) {
            performRotation(e);
        }

        e.setYaw(fixedRotations.getYaw());
        e.setPitch(fixedRotations.getPitch());
        mc.thePlayer.renderYawOffset = fixedRotations.getYaw();
        mc.thePlayer.rotationYawHead = fixedRotations.getYaw();

        if (blockData != null) {
            prevBlockData = blockData;
        } else {
            return;
        }

        if (timing.is("Pre")) {
            if (rayCast.get() && !rotated) return;

            placeBlock();
            if (extraClick.get().booleanValue()) {
                ticks += 1;
            }
        }
    }

    @EventLink
    public void onPlace(LegitPlaceEvent e) {
        if (blockData != null || prevBlockData != null && timing.is("Legit")) {
            if (rayCast.get() && !rotated) return;

            placeBlock();
            if (extraClick.get().booleanValue()) {
                ticks += 1;
            }
        }
    }

    @EventLink
    public void onPost(PostMotionEvent e) {
        if (prevBlockData != null && timing.is("Post")) {
            if (rayCast.get() && !rotated) return;

            placeBlock();
            if (extraClick.get().booleanValue()) {
                ticks += 1;
            }
        }
    }

    @EventLink
    public void onStrafe(StrafeEvent e) {
        if (!correctMovement.get()) return;

        float[] moveFix = MoveUtil.getFixedMovement(fixedRotations, e.getForward() != 0 ? Math.abs(e.getForward()) : Math.abs(e.getStrafe()), 0);

        e.setYaw(fixedRotations.getYaw());
        e.setForward(moveFix[0]);
        e.setStrafe(moveFix[1]);
    }

    @EventLink
    public void onJump(JumpEvent e) {
        if (!correctMovement.get()) return;
        e.setYaw(fixedRotations.getYaw());
    }

    public void performRotation(MotionEvent e) {
        float yaw = fixedRotations.getYaw();
        float pitch = fixedRotations.getPitch();

        switch (rotations.get().getName()) {
            case "Hypixel":
                yaw = MoveUtil.getMoveYaw(e.getYaw()) - 180;
                pitch = (float) getRandomInRange(79.5f, 83.5f);
                break;
            case "Back":
                yaw = mc.thePlayer.rotationYaw + 180;
                pitch = 80.5f;
                break;
            case "Normal":
                Vec3 lv = getVec3(blockData != null ? blockData : prevBlockData);
                BlockPos pos = new BlockPos(new Vec3i(lv.xCoord, lv.yCoord, lv.zCoord));
                yaw = (float) (RotationsUtil.getRotationToBlock(pos)[0]);
                yaw = Math.round((yaw) / 90f) * 90f;
//                yaw += (Math.random() >= 0.5 ? -Math.random() * 2.5 : Math.random() * 2.5);
//                yaw += (Math.random() >= 0.5 ? -45f : 45f);
                pitch = 81;
                break;
            case "Disabled":
                yaw = mc.thePlayer.rotationYaw;
                pitch = mc.thePlayer.rotationPitch;
                break;
        }

        fixedRotations.updateRotations(yaw, pitch);
        rotated = true;
    }

    public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted;
    }

    public void placeBlock() {
        slot = getBlockSlot();
        if (slot == -1) return;

        Vec3 vec = mc.thePlayer.getVectorForRotation(fixedRotations.getPitch(), fixedRotations.getYaw());

        MovingObjectPosition movingObjectPosition = mc.theWorld.rayTraceBlocks(mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks),
                mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks)
                        .addVector(vec.xCoord * 4.5F, vec.yCoord * 4.5F, vec.zCoord * 4.5F));

        if (rayCast.get() && movingObjectPosition == null) return;

        lastSlot = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.inventory.currentItem = slot;

        if (ticks >= delay.get().floatValue()) {
            switch (clickMode.get().getName()) {
                case "Real Click":
                    clickMouse();
                    break;
                case "Controller":
                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
                            mc.thePlayer.inventory.getStackInSlot(slot),
                            prevBlockData.position, prevBlockData.facing,
                            getVec3(prevBlockData))) {

                        if (swingItem.get().booleanValue()) {
                            mc.thePlayer.swingItem();
                        } else {
                            mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                        }
                    }
                    break;
            }
            ticks = 0;
        }

        if (spoofItem.get().booleanValue())
            mc.thePlayer.inventory.currentItem = lastSlot;
        rotated = false;
    }

    public void clickMouse() {
        MovingObjectPosition objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.BLOCK, getVec3(prevBlockData), prevBlockData.facing, prevBlockData.position);

        if (!mc.playerController.isHittingBlock) {
            mc.rightClickDelayTimer = 4;
            boolean flag = true;
            ItemStack itemstack = mc.thePlayer.inventory.getCurrentItem();

            switch (objectMouseOver.typeOfHit) {
                case ENTITY:
                    if (mc.playerController.func_178894_a(mc.thePlayer, objectMouseOver.entityHit, objectMouseOver)) {
                        flag = false;
                    } else if (mc.playerController.interactWithEntitySendPacket(mc.thePlayer, objectMouseOver.entityHit)) {
                        flag = false;
                    }

                    break;

                case BLOCK:
                    BlockPos blockpos = objectMouseOver.getBlockPos();

                    if (mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                        int i = itemstack != null ? itemstack.stackSize : 0;

                        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemstack, blockpos, objectMouseOver.sideHit, objectMouseOver.hitVec)) {
                            flag = false;

                            if (swingItem.get().booleanValue()) {
                                mc.thePlayer.swingItem();
                            } else {
                                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                            }
                        }

                        if (itemstack == null) {
                            return;
                        }

                        if (itemstack.stackSize == 0) {
                            mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                        }
                    }
            }

            if (flag) {
                ItemStack itemstack1 = mc.thePlayer.inventory.getCurrentItem();

                if (itemstack1 != null && mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, itemstack1)) {
                    mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                }
            }
        }
    }

    public static int getBlockSlot() {
        List<Block> inwalidablok = Arrays.asList(Blocks.sand, Blocks.chest, Blocks.ender_chest,
                Blocks.trapped_chest, Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch,
                Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser,
                Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.noteblock,
                Blocks.tnt, Blocks.gravel, Blocks.glass_pane);

        for (int i = 0; i < 9; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
                if (!inwalidablok.contains(itemBlock.getBlock())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Vec3 getVec3(BlockData blockData) {
        BlockPos pos = blockData.position;
        EnumFacing face = blockData.facing;

        double x = (double) pos.getX() + 0.5,
                y = (double) pos.getY() + 0.5,
                z = (double) pos.getZ() + 0.5;

        if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            y += 0.5;
        } else {
            x += 0.3;
            z += 0.3;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += 0.15;
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += 0.15;
        }
        return new Vec3(x, y, z);
    }

    public BlockData getBlockData() {
        double y = downwards.get().booleanValue() && GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) ? mc.thePlayer.posY - 2 :
                keepY.get().booleanValue() && !GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) ? (mc.thePlayer.posY - 1.0 >= lastY && Math.max(mc.thePlayer.posY, lastY) - Math.min(mc.thePlayer.posY, lastY) <= 3.0
                        && !mc.gameSettings.keyBindJump.isKeyDown() ? lastY : mc.thePlayer.posY - 1.0) : mc.thePlayer.posY - 1;

        final BlockPos blockUnder = new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(blockUnder).getBlock() instanceof BlockAir) {
            for (int x = 0; x < this.range.get().intValue(); x++) {
                for (int z = 0; z < this.range.get().intValue(); z++) {
                    for (int i = 1; i > -3; i -= 2) {
                        final BlockPos blockPos = blockUnder.add(x * i, 0, z * i);
                        if (mc.theWorld.getBlockState(blockPos).getBlock() instanceof BlockAir) {
                            for (EnumFacing direction : EnumFacing.values()) {
                                final BlockPos block = blockPos.offset(direction);
                                final Material material = mc.theWorld.getBlockState(block).getBlock().getMaterial();
                                if (material.isSolid() && !material.isLiquid()) {
                                    return new BlockData(block, direction.getOpposite());
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public class BlockData {
        private final BlockPos position;
        private final EnumFacing facing;

        public BlockData(final BlockPos position, final EnumFacing facing) {
            this.position = position;
            this.facing = facing;
        }
    }
}
