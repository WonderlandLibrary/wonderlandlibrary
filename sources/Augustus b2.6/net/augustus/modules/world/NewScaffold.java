// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.world;

import net.augustus.utils.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.MathHelper;
import net.augustus.events.EventJump;
import net.augustus.events.EventMove;
import org.lwjgl.input.Keyboard;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventSilent;
import net.minecraft.block.material.Material;
import net.minecraft.util.Vec3;
import net.minecraft.item.ItemBlock;
import net.augustus.events.EventClick;
import com.sun.javafx.geom.Vec2d;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import net.augustus.Augustus;
import net.augustus.events.EventRayCast;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventEarlyTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.util.MovingObjectPosition;
import java.util.HashMap;
import net.augustus.utils.RotationUtil;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class NewScaffold extends Module
{
    private final TimeHelper startTimeHelper;
    private final TimeHelper startTimeHelper2;
    private final TimeHelper adTimeHelper;
    private final RotationUtil rotationUtil;
    private final double[] lastXYZ;
    private final HashMap<float[], MovingObjectPosition> hashMap;
    public StringValue mode;
    public DoubleValue yawSpeed;
    public DoubleValue pitchSpeed;
    public BooleanValue moveFix;
    public BooleanValue esp;
    public BooleanValue adStrafe;
    public StringValue silentMode;
    ArrayList<double[]> hitpoints;
    private float[] lastRots;
    private float[] rots;
    private int slotID;
    private ItemStack block;
    private int lastSlotID;
    private EnumFacing enumFacing;
    private BlockPos blockPos;
    private boolean start;
    private double[] xyz;
    private MovingObjectPosition objectPosition;
    
    public NewScaffold() {
        super("ScaffoldWalk", new Color(18, 35, 50), Categorys.WORLD);
        this.startTimeHelper = new TimeHelper();
        this.startTimeHelper2 = new TimeHelper();
        this.adTimeHelper = new TimeHelper();
        this.lastXYZ = new double[3];
        this.hashMap = new HashMap<float[], MovingObjectPosition>();
        this.mode = new StringValue(8, "Mode", this, "Legit", new String[] { "Legit", "Basic" });
        this.yawSpeed = new DoubleValue(3, "YawSpeed", this, 40.0, 0.0, 180.0, 0);
        this.pitchSpeed = new DoubleValue(4, "PitchSpeed", this, 40.0, 0.0, 180.0, 0);
        this.moveFix = new BooleanValue(7, "MoveFix", this, true);
        this.esp = new BooleanValue(5, "ESP", this, true);
        this.adStrafe = new BooleanValue(11, "AdStrafe", this, true);
        this.silentMode = new StringValue(6, "SilentMode", this, "Spoof", new String[] { "Switch", "Spoof", "None" });
        this.hitpoints = new ArrayList<double[]>();
        this.lastRots = new float[] { 0.0f, 0.0f };
        this.rots = new float[] { 0.0f, 0.0f };
        this.start = true;
        this.xyz = new double[3];
        this.objectPosition = null;
        this.rotationUtil = new RotationUtil();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (NewScaffold.mc.thePlayer != null && NewScaffold.mc.theWorld != null) {
            this.restRotation();
            this.slotID = NewScaffold.mc.thePlayer.inventory.currentItem;
            this.lastSlotID = NewScaffold.mc.thePlayer.inventory.currentItem;
            this.start = true;
            this.startTimeHelper.reset();
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (NewScaffold.mc.thePlayer.inventory.currentItem != this.slotID) {}
        this.slotID = NewScaffold.mc.thePlayer.inventory.currentItem;
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        if (NewScaffold.mc.thePlayer == null || NewScaffold.mc.theWorld == null) {
            return;
        }
        this.blockPos = this.getAimBlockPos();
        this.start = ((NewScaffold.mc.thePlayer.motionX == 0.0 && NewScaffold.mc.thePlayer.motionZ == 0.0 && NewScaffold.mc.thePlayer.onGround) || !this.startTimeHelper.reached(200L));
        if (this.start) {
            this.startTimeHelper2.reset();
        }
        if (this.blockPos != null) {
            float[] floats = { 1.0f, 1.0f };
            final String selected = this.mode.getSelected();
            switch (selected) {
                case "Legit": {
                    floats = this.getNearestRotation();
                    break;
                }
                case "Basic": {
                    floats = this.basicRotation();
                    break;
                }
            }
            this.lastRots = this.rots;
            if (floats != null) {
                this.rots = floats;
            }
            this.setRotation();
        }
    }
    
    @EventTarget
    public void onEventRayCastPost(final EventRayCast eventRayCast) {
        if (this.objectPosition != null) {
            NewScaffold.mc.objectMouseOver = this.objectPosition;
        }
    }
    
    private float[] basicRotation() {
        final double x = NewScaffold.mc.thePlayer.posX;
        final double z = NewScaffold.mc.thePlayer.posZ;
        final double add1 = 1.05;
        final double add2 = 0.05;
        this.xyz = new double[] { NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY, NewScaffold.mc.thePlayer.posZ };
        final double maX = this.blockPos.getX() + add1;
        final double miX = this.blockPos.getX() - add2;
        final double maZ = this.blockPos.getZ() + add1;
        final double miZ = this.blockPos.getZ() - add2;
        if (x > maX || x < miX || z > maZ || z < miZ) {
            final double[] ex = this.getAdvancedDiagonalExpandXZ(this.blockPos);
            final float[] f = this.rotationUtil.scaffoldRots(this.blockPos.getX() + ex[0], this.blockPos.getY() + 0.85, this.blockPos.getZ() + ex[1], this.lastRots[0], this.lastRots[1], (float)this.yawSpeed.getValue(), (float)this.pitchSpeed.getValue(), false);
            return new float[] { NewScaffold.mc.thePlayer.rotationYaw - 180.0f, f[1] };
        }
        return new float[] { NewScaffold.mc.thePlayer.rotationYaw - 180.0f, this.rots[1] };
    }
    
    private float[] getNearestRotation() {
        this.objectPosition = null;
        final float[] floats = this.rots;
        final BlockPos b = new BlockPos(NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY - 0.5, NewScaffold.mc.thePlayer.posZ);
        this.hashMap.clear();
        if (this.start) {
            final float yaw = this.rotationUtil.rotateToYaw((float)this.yawSpeed.getValue(), this.rots[0], Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f);
            RotationUtil.mouseSens(yaw, 80.34f, this.rots[0], this.rots[1]);
            floats[1] = 80.34f;
            floats[0] = yaw;
        }
        else {
            final float yaww = Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f;
            floats[0] = yaww;
            double x = NewScaffold.mc.thePlayer.posX;
            double z = NewScaffold.mc.thePlayer.posZ;
            final double add1 = 1.288;
            final double add2 = 0.288;
            if (!this.buildForward()) {
                x += NewScaffold.mc.thePlayer.posX - this.xyz[0];
                z += NewScaffold.mc.thePlayer.posZ - this.xyz[2];
            }
            this.xyz = new double[] { NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY, NewScaffold.mc.thePlayer.posZ };
            final double maX = this.blockPos.getX() + add1;
            final double miX = this.blockPos.getX() - add2;
            final double maZ = this.blockPos.getZ() + add1;
            final double miZ = this.blockPos.getZ() - add2;
            if (x > maX || x < miX || z > maZ || z < miZ) {
                final ArrayList<MovingObjectPosition> movingObjectPositions = new ArrayList<MovingObjectPosition>();
                final ArrayList<Float> pitchs = new ArrayList<Float>();
                for (float i = Math.max(this.rots[1] - 20.0f, -90.0f); i < Math.min(this.rots[1] + 20.0f, 90.0f); i += 0.05f) {
                    final float[] f = RotationUtil.mouseSens(yaww, i, this.rots[0], this.rots[1]);
                    final MovingObjectPosition m2 = NewScaffold.mc.thePlayer.customRayTrace(4.5, 1.0f, yaww, f[1]);
                    if (m2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.isOkBlock(m2.getBlockPos()) && !movingObjectPositions.contains(m2) && m2.getBlockPos().equalsBlockPos(this.blockPos) && m2.sideHit != EnumFacing.DOWN && m2.sideHit != EnumFacing.UP && m2.getBlockPos().getY() <= b.getY()) {
                        movingObjectPositions.add(m2);
                        this.hashMap.put(f, m2);
                        pitchs.add(f[1]);
                    }
                }
                movingObjectPositions.sort(Comparator.comparingDouble(m -> NewScaffold.mc.thePlayer.getDistanceSq(m.getBlockPos().add(0.5, 0.5, 0.5))));
                MovingObjectPosition mm = null;
                if (movingObjectPositions.size() > 0) {
                    mm = movingObjectPositions.get(0);
                }
                if (mm != null) {
                    floats[0] = yaww;
                    pitchs.sort(Comparator.comparingDouble((ToDoubleFunction<? super Float>)this::distanceToLastPitch));
                    if (!pitchs.isEmpty()) {
                        floats[1] = pitchs.get(0);
                        this.objectPosition = this.hashMap.get(floats);
                    }
                    return floats;
                }
            }
            else {
                floats[1] = this.rots[1];
            }
        }
        return floats;
    }
    
    private boolean canPlace(final float[] yawPitch) {
        final BlockPos b = new BlockPos(NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY - 0.5, NewScaffold.mc.thePlayer.posZ);
        final MovingObjectPosition m4 = NewScaffold.mc.thePlayer.customRayTrace(4.5, 1.0f, yawPitch[0], yawPitch[1]);
        if (m4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.isOkBlock(m4.getBlockPos()) && m4.getBlockPos().equalsBlockPos(this.blockPos) && m4.sideHit != EnumFacing.DOWN && m4.sideHit != EnumFacing.UP && m4.getBlockPos().getY() <= b.getY()) {
            this.hashMap.put(yawPitch, m4);
            return true;
        }
        return false;
    }
    
    private double distanceToLastRots(final float[] predictRots) {
        final float diff1 = Math.abs(predictRots[0] - this.rots[0]);
        final float diff2 = Math.abs(predictRots[1] - this.rots[1]);
        return diff1 * diff1 + diff2 * diff2;
    }
    
    private double distanceToLastPitch(final float pitch) {
        return Math.abs(pitch - this.rots[1]);
    }
    
    private double[] getAdvancedDiagonalExpandXZ(final BlockPos blockPos) {
        final double[] xz = new double[2];
        final Vec2d difference = new Vec2d(blockPos.getX() - NewScaffold.mc.thePlayer.posX, blockPos.getZ() - NewScaffold.mc.thePlayer.posZ);
        if (difference.x > -1.0 && difference.x < 0.0 && difference.y < -1.0) {
            this.enumFacing = EnumFacing.SOUTH;
            xz[0] = difference.x * -1.0;
            xz[1] = 1.0;
        }
        if (difference.y < 0.0 && difference.y > -1.0 && difference.x < -1.0) {
            this.enumFacing = EnumFacing.EAST;
            xz[0] = 1.0;
            xz[1] = difference.y * -1.0;
        }
        if (difference.x > -1.0 && difference.x < 0.0 && difference.y > 0.0) {
            this.enumFacing = EnumFacing.NORTH;
            xz[0] = difference.x * -1.0;
            xz[1] = 0.0;
        }
        if (difference.y < 0.0 && difference.y > -1.0 && difference.x > 0.0) {
            this.enumFacing = EnumFacing.WEST;
            xz[0] = 0.0;
            xz[1] = difference.y * -1.0;
            this.enumFacing = EnumFacing.WEST;
        }
        if (difference.x >= 0.0 && difference.y < -1.0) {
            xz[1] = 1.0;
        }
        if (difference.y >= 0.0 & difference.x < -1.0) {
            xz[0] = 1.0;
        }
        if (difference.x < 0.0 || difference.y > 0.0) {}
        if (difference.y <= -1.0 && difference.x < -1.0) {
            xz[1] = (xz[0] = 1.0);
        }
        return xz;
    }
    
    private EnumFacing getPlaceSide() {
        final BlockPos playerPos = new BlockPos(NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY - 0.5, NewScaffold.mc.thePlayer.posZ);
        if (playerPos.equalsBlockPos(this.blockPos)) {
            System.out.println("Error");
        }
        if (playerPos.add(0, 1, 0).equalsBlockPos(this.blockPos)) {
            return EnumFacing.UP;
        }
        if (playerPos.add(0, -1, 0).equalsBlockPos(this.blockPos)) {
            return EnumFacing.DOWN;
        }
        if (playerPos.add(1, 0, 0).equalsBlockPos(this.blockPos)) {
            return EnumFacing.WEST;
        }
        if (playerPos.add(-1, 0, 0).equalsBlockPos(this.blockPos)) {
            return EnumFacing.EAST;
        }
        if (playerPos.add(0, 0, 1).equalsBlockPos(this.blockPos)) {
            return EnumFacing.NORTH;
        }
        if (playerPos.add(0, 0, -1).equalsBlockPos(this.blockPos)) {
            return EnumFacing.SOUTH;
        }
        if (playerPos.add(1, 0, 1).equalsBlockPos(this.blockPos)) {
            return EnumFacing.WEST;
        }
        if (playerPos.add(-1, 0, 1).equalsBlockPos(this.blockPos)) {
            return EnumFacing.EAST;
        }
        if (playerPos.add(-1, 0, 1).equalsBlockPos(this.blockPos)) {
            return EnumFacing.NORTH;
        }
        if (playerPos.add(-1, 0, -1).equalsBlockPos(this.blockPos)) {
            return EnumFacing.SOUTH;
        }
        return null;
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        eventClick.setCanceled(true);
        if (this.block == null) {
            this.block = NewScaffold.mc.thePlayer.inventory.getCurrentItem();
        }
        if (this.blockPos == null || NewScaffold.mc.currentScreen != null) {
            return;
        }
        final ItemStack lastItem = NewScaffold.mc.thePlayer.inventory.getCurrentItem();
        ItemStack itemstack = NewScaffold.mc.thePlayer.inventory.getCurrentItem();
        if (!this.silentMode.getSelected().equals("None")) {
            for (int i = 36; i < NewScaffold.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack itemStack = NewScaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0 && Augustus.getInstance().getBlockUtil().isValidStack(itemStack)) {
                    this.block = itemStack;
                    this.slotID = i - 36;
                    break;
                }
            }
            if (!this.silentMode.getSelected().equals("Spoof") || this.lastSlotID != this.slotID) {}
            itemstack = NewScaffold.mc.thePlayer.inventoryContainer.getSlot(this.slotID + 36).getStack();
        }
        else {
            this.slotID = NewScaffold.mc.thePlayer.inventory.currentItem;
            this.lastSlotID = NewScaffold.mc.thePlayer.inventory.currentItem;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Basic": {
                final double x = NewScaffold.mc.thePlayer.posX;
                final double z = NewScaffold.mc.thePlayer.posZ;
                final double add1 = 1.05;
                final double add2 = 0.05;
                this.xyz = new double[] { NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY, NewScaffold.mc.thePlayer.posZ };
                final double maX = this.blockPos.getX() + add1;
                final double miX = this.blockPos.getX() - add2;
                final double maZ = this.blockPos.getZ() + add1;
                final double miZ = this.blockPos.getZ() - add2;
                if (x > maX || x < miX || z > maZ || z < miZ) {
                    if (this.silentMode.getSelected().equals("Switch")) {
                        NewScaffold.mc.thePlayer.inventory.setCurrentItem(this.block.getItem(), 0, false, false);
                    }
                    final EnumFacing e = this.getPlaceSide();
                    if (e != null) {
                        final double[] ex = this.getAdvancedDiagonalExpandXZ(this.blockPos);
                        if (NewScaffold.mc.playerController.onPlayerRightClick(NewScaffold.mc.thePlayer, NewScaffold.mc.theWorld, itemstack, this.blockPos, e, new Vec3(this.blockPos.getX() + ex[0], this.blockPos.getY() - 0.5234234, this.blockPos.getZ() + ex[1]))) {
                            NewScaffold.mc.thePlayer.swingItem();
                        }
                    }
                    if (itemstack != null && itemstack.stackSize == 0) {
                        NewScaffold.mc.thePlayer.inventory.mainInventory[this.slotID] = null;
                    }
                    break;
                }
                break;
            }
            default: {
                final MovingObjectPosition blockOver = NewScaffold.mc.objectMouseOver;
                if (blockOver == null) {
                    break;
                }
                final BlockPos blockpos = blockOver.getBlockPos();
                if (blockOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || NewScaffold.mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
                    break;
                }
                if (itemstack != null && !(itemstack.getItem() instanceof ItemBlock)) {
                    return;
                }
                this.hitpoints.add(new double[] { blockOver.hitVec.xCoord, blockOver.hitVec.yCoord, blockOver.hitVec.zCoord });
                if (NewScaffold.mc.thePlayer.posY < blockpos.getY() + 1.5) {
                    if (blockOver.sideHit != EnumFacing.UP && blockOver.sideHit != EnumFacing.DOWN) {
                        if (this.silentMode.getSelected().equals("Switch")) {
                            NewScaffold.mc.thePlayer.inventory.setCurrentItem(this.block.getItem(), 0, false, false);
                        }
                        if (NewScaffold.mc.playerController.onPlayerRightClick(NewScaffold.mc.thePlayer, NewScaffold.mc.theWorld, itemstack, blockpos, blockOver.sideHit, blockOver.hitVec)) {
                            NewScaffold.mc.thePlayer.swingItem();
                        }
                        if (itemstack != null && itemstack.stackSize == 0) {
                            NewScaffold.mc.thePlayer.inventory.mainInventory[this.slotID] = null;
                        }
                        NewScaffold.mc.sendClickBlockToController(NewScaffold.mc.currentScreen == null && NewScaffold.mc.gameSettings.keyBindAttack.isKeyDown() && NewScaffold.mc.inGameHasFocus);
                        break;
                    }
                    break;
                }
                else {
                    if (blockOver.sideHit != EnumFacing.DOWN && blockOver.getBlockPos().equalsBlockPos(this.blockPos) && NewScaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (this.silentMode.getSelected().equals("Switch")) {
                            NewScaffold.mc.thePlayer.inventory.setCurrentItem(this.block.getItem(), 0, false, false);
                        }
                        if (NewScaffold.mc.playerController.onPlayerRightClick(NewScaffold.mc.thePlayer, NewScaffold.mc.theWorld, itemstack, blockpos, blockOver.sideHit, blockOver.hitVec)) {
                            NewScaffold.mc.thePlayer.swingItem();
                        }
                        if (itemstack != null && itemstack.stackSize == 0) {
                            NewScaffold.mc.thePlayer.inventory.mainInventory[this.slotID] = null;
                        }
                        NewScaffold.mc.sendClickBlockToController(NewScaffold.mc.currentScreen == null && NewScaffold.mc.gameSettings.keyBindAttack.isKeyDown() && NewScaffold.mc.inGameHasFocus);
                        break;
                    }
                    break;
                }
                break;
            }
        }
        if (lastItem != null && this.silentMode.getSelected().equals("Switch")) {
            NewScaffold.mc.thePlayer.inventory.setCurrentItem(lastItem.getItem(), 0, false, false);
        }
        this.lastSlotID = this.slotID;
    }
    
    @EventTarget
    public void onEventSilent(final EventSilent eventSilent) {
        eventSilent.setSlotID(this.slotID);
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        if (this.moveFix.getBoolean()) {
            eventSilentMove.setSilent(true);
        }
        if (this.mode.getSelected().equals("Legit") && this.adStrafe.getBoolean()) {
            final BlockPos b = new BlockPos(NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY - 0.5, NewScaffold.mc.thePlayer.posZ);
            if (NewScaffold.mc.theWorld.getBlockState(b).getBlock().getMaterial() == Material.air && NewScaffold.mc.currentScreen == null && !Keyboard.isKeyDown(NewScaffold.mc.gameSettings.keyBindJump.getKeyCodeDefault()) && this.buildForward() && NewScaffold.mc.thePlayer.movementInput.moveForward != 0.0f) {
                if (NewScaffold.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.EAST) {
                    if (b.getZ() + 0.5 > NewScaffold.mc.thePlayer.posZ) {
                        NewScaffold.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                    }
                    else {
                        NewScaffold.mc.thePlayer.movementInput.moveStrafe = -1.0f;
                    }
                }
                else if (NewScaffold.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.WEST) {
                    if (b.getZ() + 0.5 < NewScaffold.mc.thePlayer.posZ) {
                        NewScaffold.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                    }
                    else {
                        NewScaffold.mc.thePlayer.movementInput.moveStrafe = -1.0f;
                    }
                }
                else if (NewScaffold.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.SOUTH) {
                    if (b.getX() + 0.5 < NewScaffold.mc.thePlayer.posX) {
                        NewScaffold.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                    }
                    else {
                        NewScaffold.mc.thePlayer.movementInput.moveStrafe = -1.0f;
                    }
                }
                else if (b.getX() + 0.5 > NewScaffold.mc.thePlayer.posX) {
                    NewScaffold.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                }
                else {
                    NewScaffold.mc.thePlayer.movementInput.moveStrafe = -1.0f;
                }
                this.adTimeHelper.reset();
            }
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (!this.moveFix.getBoolean()) {
            eventMove.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (!this.moveFix.getBoolean()) {
            eventJump.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    private void setRotation() {
        if (NewScaffold.mc.currentScreen != null) {
            return;
        }
        NewScaffold.mc.thePlayer.rotationYaw = this.rots[0];
        NewScaffold.mc.thePlayer.rotationPitch = this.rots[1];
        NewScaffold.mc.thePlayer.prevRotationYaw = this.lastRots[0];
        NewScaffold.mc.thePlayer.prevRotationPitch = this.lastRots[1];
    }
    
    private boolean buildForward() {
        final float realYaw = MathHelper.wrapAngleTo180_float(Augustus.getInstance().getYawPitchHelper().realYaw);
        return (realYaw > 77.5 && realYaw < 102.5) || (realYaw > 167.5 || realYaw < -167.0f) || (realYaw < -77.5 && realYaw > -102.5) || (realYaw > -12.5 && realYaw < 12.5);
    }
    
    private BlockPos getAimBlockPos() {
        final BlockPos playerPos = new BlockPos(NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY - 1.0, NewScaffold.mc.thePlayer.posZ);
        if ((NewScaffold.mc.gameSettings.keyBindJump.isKeyDown() || !NewScaffold.mc.thePlayer.onGround) && NewScaffold.mc.thePlayer.moveForward == 0.0f && NewScaffold.mc.thePlayer.moveStrafing == 0.0f && this.isOkBlock(playerPos.add(0, -1, 0))) {
            return playerPos.add(0, -1, 0);
        }
        BlockPos blockPos = null;
        final ArrayList<BlockPos> bp = this.getBlockPos();
        final ArrayList<BlockPos> blockPositions = new ArrayList<BlockPos>();
        if (bp.size() > 0) {
            for (int i = 0; i < Math.min(bp.size(), 18); ++i) {
                blockPositions.add(bp.get(i));
            }
            blockPositions.sort(Comparator.comparingDouble((ToDoubleFunction<? super BlockPos>)this::getDistanceToBlockPos));
            if (blockPositions.size() > 0) {
                blockPos = blockPositions.get(0);
            }
        }
        return blockPos;
    }
    
    private ArrayList<BlockPos> getBlockPos() {
        final BlockPos playerPos = new BlockPos(NewScaffold.mc.thePlayer.posX, NewScaffold.mc.thePlayer.posY - 1.0, NewScaffold.mc.thePlayer.posZ);
        final ArrayList<BlockPos> blockPoses = new ArrayList<BlockPos>();
        for (int x = playerPos.getX() - 2; x <= playerPos.getX() + 2; ++x) {
            for (int y = playerPos.getY() - 1; y <= playerPos.getY(); ++y) {
                for (int z = playerPos.getZ() - 2; z <= playerPos.getZ() + 2; ++z) {
                    if (this.isOkBlock(new BlockPos(x, y, z))) {
                        blockPoses.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        if (!blockPoses.isEmpty()) {
            blockPoses.sort(Comparator.comparingDouble(blockPos -> NewScaffold.mc.thePlayer.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5)));
        }
        return blockPoses;
    }
    
    private double getDistanceToBlockPos(final BlockPos blockPos) {
        double distance = 1337.0;
        for (float x = (float)blockPos.getX(); x <= blockPos.getX() + 1; x += (float)0.2) {
            for (float y = (float)blockPos.getY(); y <= blockPos.getY() + 1; y += (float)0.2) {
                for (float z = (float)blockPos.getZ(); z <= blockPos.getZ() + 1; z += (float)0.2) {
                    final double d0 = NewScaffold.mc.thePlayer.getDistance(x, y, z);
                    if (d0 < distance) {
                        distance = d0;
                    }
                }
            }
        }
        return distance;
    }
    
    private boolean isOkBlock(final BlockPos blockPos) {
        final Block block = NewScaffold.mc.theWorld.getBlockState(blockPos).getBlock();
        return !(block instanceof BlockLiquid) && !(block instanceof BlockAir) && !(block instanceof BlockChest) && !(block instanceof BlockFurnace);
    }
    
    private void restRotation() {
        this.rots[0] = Augustus.getInstance().getYawPitchHelper().realYaw;
        this.rots[1] = Augustus.getInstance().getYawPitchHelper().realPitch;
        this.lastRots[0] = Augustus.getInstance().getYawPitchHelper().realLastYaw;
        this.lastRots[1] = Augustus.getInstance().getYawPitchHelper().realLastPitch;
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (this.esp.getBoolean()) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GlStateManager.disableCull();
            GL11.glDepthMask(false);
            final float red = 0.16470589f;
            final float green = 0.5686275f;
            final float blue = 0.96862745f;
            float lineWidth = 0.0f;
            if (this.blockPos != null) {
                if (NewScaffold.mc.thePlayer.getDistance(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ()) > 1.0) {
                    double d0 = 1.0 - NewScaffold.mc.thePlayer.getDistance(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ()) / 20.0;
                    if (d0 < 0.3) {
                        d0 = 0.3;
                    }
                    lineWidth *= (float)d0;
                }
                RenderUtil.drawBlockESP(this.blockPos, red, green, blue, 0.39215687f, 1.0f, lineWidth);
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GlStateManager.enableCull();
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2848);
        }
    }
    
    public int getSlotID() {
        return this.slotID;
    }
}
