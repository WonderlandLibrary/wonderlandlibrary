/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.world;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.util.vector.Vector2f;
import tk.rektsky.Client;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.ClientTickEvent;
import tk.rektsky.event.impl.JumpEvent;
import tk.rektsky.event.impl.MotionUpdateEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.UpdateSprintingEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.movement.Fly;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.utils.Timer;
import tk.rektsky.utils.combat.RotationUtil;
import tk.rektsky.utils.inventory.InventoryUtils;

public class Scaffold
extends Module {
    public DoubleSetting timerBoost = new DoubleSetting("Timer Boost", 1.0, 2.0, 1.0);
    public BooleanSetting swing = new BooleanSetting("Swing", true);
    public BooleanSetting sameY = new BooleanSetting("Same Y", false);
    public BooleanSetting noSprint = new BooleanSetting("NoSprint", true);
    public BooleanSetting doTower = new BooleanSetting("Tower", true);
    public DoubleSetting towerMotion = new DoubleSetting("Tower Motion", 0.5, 10.0, 5.0);
    public static Timer timer = new Timer();
    public static Timer stopTimer = new Timer();
    public static int placedBlocks = 0;
    public static int heldItem = 0;
    public static int oldItem = 0;
    public static boolean towering = false;
    public static boolean lastTowering = false;
    private long tick = 0L;
    private long startY = 0L;
    private long lastPlaceTick = 0L;

    public Scaffold() {
        super("Scaffold", "Places blocks under you", Category.WORLD);
        this.registerSetting(this.timerBoost);
    }

    private Vector2f tryFaceBlock(BlockPos position, EnumFacing face) {
        double actualFacingX = (double)position.getX() + 0.5;
        double actualFacingY = (double)position.getY() + 0.5;
        double actualFacingZ = (double)position.getZ() + 0.5;
        actualFacingX += (double)face.getDirectionVec().getX() / 2.0;
        actualFacingY += (double)face.getDirectionVec().getY() / 2.0;
        actualFacingZ += (double)face.getDirectionVec().getZ() / 2.0;
        actualFacingX = this.mc.thePlayer.posX - actualFacingX;
        actualFacingY = this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight() - actualFacingY;
        actualFacingZ = this.mc.thePlayer.posZ - actualFacingZ;
        float yaw = (float)(Math.toDegrees(Math.atan2(actualFacingZ, actualFacingX)) + 90.0);
        float pitch = (float)Math.toDegrees(Math.atan(actualFacingY / this.distanceTo(actualFacingX, 0.0, actualFacingZ)));
        return new Vector2f(yaw, pitch);
    }

    private double distanceTo(double x2, double y2, double z2) {
        double distance = 0.0;
        distance = Math.sqrt(x2 * x2 + z2 * z2);
        distance = Math.sqrt(distance * distance + y2 * y2);
        return distance;
    }

    @Override
    public void onEnable() {
        RotationUtil.setReverseRotating("scaffold", false);
        if (this.sameY.getValue().booleanValue() && this.doTower.getValue().booleanValue()) {
            this.sameY.setValue(false);
            Client.addClientChat("You can't SameY and Tower at the same time! Disabled SameY!");
        }
        this.mc.timer.timerSpeed = this.timerBoost.getValue();
        stopTimer.reset();
        placedBlocks = 0;
        this.tick = 0L;
        this.startY = (long)this.mc.thePlayer.posY;
        oldItem = this.mc.thePlayer.inventory.currentItem;
        lastTowering = false;
        towering = false;
    }

    @Override
    public void onDisable() {
        try {
            if (heldItem != this.mc.thePlayer.inventory.currentItem) {
                heldItem = this.mc.thePlayer.inventory.currentItem;
                this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
            }
            this.mc.timer.timerSpeed = 1.0;
            ModulesManager.getModuleByClass(Fly.class);
            Fly.verusFly.startTime = System.currentTimeMillis();
            RotationUtil.setReverseRotating("scaffold", true);
            RotationUtil.reset();
            this.mc.gameSettings.keyBindSneak.pressed = false;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void onEvent(Event e2) {
        if (e2 instanceof ClientTickEvent) {
            ++this.tick;
            if (this.mc.theWorld == null) {
                this.setToggled(false);
            }
        }
        if (e2 instanceof PacketSentEvent && ((PacketSentEvent)e2).getPacket() instanceof C09PacketHeldItemChange) {
            ((PacketSentEvent)e2).setCanceled(true);
        }
        if (e2 instanceof UpdateSprintingEvent && this.noSprint.getValue().booleanValue() && !this.mc.gameSettings.keyBindSneak.pressed) {
            ((UpdateSprintingEvent)e2).setSprinting(false);
        }
        if (e2 instanceof JumpEvent && !((JumpEvent)e2).cancelled && this.mc.gameSettings.keyBindJump.pressed) {
            ((JumpEvent)e2).cancelled = this.doTower.getValue();
        }
        if (e2 instanceof MotionUpdateEvent) {
            Vec3i[] sides;
            placedBlocks = 0;
            MotionUpdateEvent event = (MotionUpdateEvent)e2;
            boolean standingOnBlock = false;
            for (Vec3i side : sides = new Vec3i[]{new Vec3i(0, -1, 1), new Vec3i(1, -1, 0), new Vec3i(0, -1, -1), new Vec3i(-1, -1, 0), new Vec3i(1, -1, -1), new Vec3i(1, -1, 1), new Vec3i(-1, -1, -1), new Vec3i(-1, -1, 1), new Vec3i(0, -1, 0)}) {
                double modZ;
                double modY;
                double v2 = new Random().nextDouble() % 0.1;
                double modX = (double)side.getX() * v2;
                BlockPos pos = new BlockPos(this.mc.thePlayer.posX + modX, this.mc.thePlayer.posY + (modY = (double)side.getY()), this.mc.thePlayer.posZ + (modZ = (double)side.getZ() * v2));
                if (this.mc.theWorld.getBlockState(pos).getBlock().getMaterial().isReplaceable()) continue;
                standingOnBlock = true;
                break;
            }
            BlockPos gonnaPlacePos = new BlockPos(this.mc.thePlayer.posX, (this.sameY.getValue() != false ? (double)this.startY : this.mc.thePlayer.posY) - 1.0, this.mc.thePlayer.posZ);
            BlockData data = this.getBlockData(gonnaPlacePos);
            int blockSlot = this.getBlockSlot();
            if (blockSlot != -1 && data != null && this.mc.gameSettings.keyBindJump.pressed && this.doTower.getValue().booleanValue()) {
                this.mc.thePlayer.motionY = this.towerMotion.getValue() / 10.0;
                towering = true;
            } else {
                towering = false;
            }
            if (lastTowering != towering) {
                if (towering) {
                    oldItem = this.mc.thePlayer.inventory.currentItem;
                    lastTowering = true;
                } else {
                    lastTowering = false;
                    this.mc.thePlayer.inventory.currentItem = oldItem;
                }
            }
            if (towering) {
                this.mc.thePlayer.inventory.currentItem = blockSlot;
            }
            if (!standingOnBlock && blockSlot != -1) {
                if (data != null) {
                    if (this.mc.thePlayer.posY % 1.0 < 0.1 && this.mc.gameSettings.keyBindJump.pressed) {
                        return;
                    }
                    Vector2f yp = this.tryFaceBlock(data.position, data.face);
                    EnumFacing[] faces = new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH};
                    event.setYaw(yp.x);
                    event.setPitch(yp.y);
                    for (EnumFacing face : faces) {
                        if (!data.position.add(face.getDirectionVec()).equals(gonnaPlacePos)) continue;
                        event.setPitch(87.0f);
                        if (face == EnumFacing.SOUTH) {
                            event.setYaw(180.0f);
                        }
                        if (face == EnumFacing.NORTH) {
                            event.setYaw(0.0f);
                        }
                        if (face == EnumFacing.WEST) {
                            event.setYaw(-90.0f);
                        }
                        if (face != EnumFacing.EAST) continue;
                        event.setYaw(90.0f);
                    }
                    RotationUtil.setYaw(event.yaw);
                    RotationUtil.setPitch(event.pitch);
                    if (heldItem != blockSlot) {
                        heldItem = blockSlot;
                        this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C09PacketHeldItemChange(blockSlot));
                    }
                    this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.mainInventory[blockSlot], data.position, data.face, this.getVec3(data.position, data.face));
                    if (this.mc.thePlayer.inventory.mainInventory[blockSlot].stackSize == 0) {
                        this.mc.thePlayer.inventory.mainInventory[blockSlot] = null;
                    }
                    placedBlocks = 1;
                    this.lastPlaceTick = this.tick;
                    if (this.swing.getValue().booleanValue()) {
                        this.mc.thePlayer.swingItem();
                    } else {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                }
            } else if (this.tick - this.lastPlaceTick >= 1L) {
                event.setYaw(this.mc.thePlayer.rotationYaw);
                event.setPitch(this.mc.thePlayer.rotationPitch);
                RotationUtil.setYaw(event.yaw);
                RotationUtil.setPitch(event.pitch);
                if (heldItem != this.mc.thePlayer.inventory.currentItem) {
                    heldItem = this.mc.thePlayer.inventory.currentItem;
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
                }
            }
        }
    }

    private int getBlockSlot() {
        int item = -1;
        int stacksize = 0;
        for (int i2 = 36; i2 < 45; ++i2) {
            if (!InventoryUtils.isBlock(this.mc.thePlayer.inventoryContainer.getSlot(i2).getStack()) || this.mc.thePlayer.inventoryContainer.getSlot((int)i2).getStack().stackSize >= stacksize && stacksize != 0) continue;
            item = i2 - 36;
            stacksize = this.mc.thePlayer.inventoryContainer.getSlot((int)i2).getStack().stackSize;
        }
        if (item == -1) {
            int switchSlot = 0;
            for (Slot slot : this.mc.thePlayer.inventoryContainer.inventorySlots) {
                if (!InventoryUtils.isBlock(slot.getStack()) || slot.getStack().stackSize >= stacksize && stacksize != 0) continue;
                item = 8;
                stacksize = slot.getStack().stackSize;
                switchSlot = slot.slotNumber;
            }
            this.mc.playerController.windowClick(0, switchSlot, 8, 2, this.mc.thePlayer);
        }
        return item;
    }

    public Vec3 getVec3(BlockPos pos, EnumFacing face) {
        double x2 = (double)pos.getX() + 0.5;
        double y2 = (double)pos.getY() + 0.5;
        double z2 = (double)pos.getZ() + 0.5;
        x2 += (double)face.getFrontOffsetX() / 2.0;
        z2 += (double)face.getFrontOffsetZ() / 2.0;
        y2 += (double)face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x2 += new Random().nextDouble() / 2.0 - 0.25;
            z2 += new Random().nextDouble() / 2.0 - 0.25;
        } else {
            y2 += new Random().nextDouble() / 2.0 - 0.25;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z2 += new Random().nextDouble() / 2.0 - 0.25;
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x2 += new Random().nextDouble() / 2.0 - 0.25;
        }
        return new Vec3(x2, y2, z2);
    }

    private BlockData getBlockData(BlockPos pos) {
        if (Scaffold.isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, -1, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, -1, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, -1, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, -1, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos.add(0, -1, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        BlockPos pos2 = pos.add(0, -1, 0).add(1, 0, 0);
        BlockPos pos3 = pos.add(0, -1, 0).add(0, 0, 1);
        BlockPos pos4 = pos.add(0, -1, 0).add(-1, 0, 0);
        BlockPos pos5 = pos.add(0, -1, 0).add(0, 0, -1);
        if (Scaffold.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        if (Scaffold.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP, null);
        }
        if (Scaffold.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST, null);
        }
        if (Scaffold.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST, null);
        }
        if (Scaffold.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH, null);
        }
        if (Scaffold.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH, null);
        }
        return null;
    }

    public static boolean isPosSolid(BlockPos pos) {
        Block block = Client.mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

    private class BlockData {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face, BlockData blockData) {
            this.position = position;
            this.face = face;
        }
    }
}

