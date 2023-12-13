// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import com.sun.javafx.geom.Vec2d;
import java.util.Comparator;
import java.util.ArrayList;
import net.augustus.events.EventMove;
import net.augustus.events.EventJump;
import net.augustus.events.EventSaveWalk;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.item.ItemBlock;
import net.augustus.events.EventClick;
import net.augustus.Augustus;
import net.minecraft.util.MathHelper;
import net.augustus.utils.RandomUtil;
import net.augustus.events.EventEarlyTick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.block.material.Material;
import net.augustus.events.EventSilentMove;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import net.augustus.utils.RotationUtil;
import net.minecraft.util.BlockPos;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class ScaffoldWalk extends Module
{
    private final TimeHelper timeHelper;
    private final TimeHelper timeHelper2;
    public StringValue mode;
    public BooleanValue adStrafe;
    public BooleanValue moveFix;
    public DoubleValue yawSpeed;
    public DoubleValue pitchSpeed;
    public StringValue silentMode;
    private float[] lastRots;
    private float[] rots;
    private BlockPos b;
    private BlockPos lastB;
    private RotationUtil rotationUtil;
    private boolean rotated;
    private int slotID;
    private ItemStack block;
    private int lastSlotID;
    private long lastTime;
    private EnumFacing enumFacing;
    
    public ScaffoldWalk() {
        super("OldScaffold", new Color(18, 35, 50, 255), Categorys.WORLD);
        this.timeHelper = new TimeHelper();
        this.timeHelper2 = new TimeHelper();
        this.mode = new StringValue(1, "Modes", this, "Intave", new String[] { "Normal", "Legit", "Intave" });
        this.adStrafe = new BooleanValue(5, "ADStrafe", this, false);
        this.moveFix = new BooleanValue(6, "MoveFix", this, true);
        this.yawSpeed = new DoubleValue(3, "YawSpeed", this, 40.0, 0.0, 180.0, 0);
        this.pitchSpeed = new DoubleValue(4, "PitchSpeed", this, 40.0, 0.0, 180.0, 0);
        this.silentMode = new StringValue(6, "SilentMode", this, "Spoof", new String[] { "Switch", "Spoof", "None" });
        this.lastRots = new float[] { 0.0f, 0.0f };
        this.rots = new float[] { 0.0f, 0.0f };
        this.lastTime = 0L;
        this.rotationUtil = new RotationUtil();
    }
    
    @Override
    public void onEnable() {
        if (ScaffoldWalk.mc.thePlayer == null) {
            return;
        }
        this.rotationUtil = new RotationUtil();
        this.rots[0] = ScaffoldWalk.mc.thePlayer.rotationYaw;
        this.rots[1] = ScaffoldWalk.mc.thePlayer.rotationPitch;
        this.lastRots[0] = ScaffoldWalk.mc.thePlayer.prevRotationYaw;
        this.lastRots[1] = ScaffoldWalk.mc.thePlayer.prevRotationPitch;
        this.slotID = ScaffoldWalk.mc.thePlayer.inventory.currentItem;
        this.lastSlotID = ScaffoldWalk.mc.thePlayer.inventory.currentItem;
    }
    
    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(ScaffoldWalk.mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(ScaffoldWalk.mc.gameSettings.keyBindLeft.getKeyCode()));
        KeyBinding.setKeyBindState(ScaffoldWalk.mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(ScaffoldWalk.mc.gameSettings.keyBindRight.getKeyCode()));
        if (ScaffoldWalk.mc.thePlayer.inventory.currentItem != this.slotID) {}
        this.slotID = ScaffoldWalk.mc.thePlayer.inventory.currentItem;
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        if (this.mode.getSelected().equals("Legit") && this.b != null) {
            final BlockPos bb = new BlockPos(ScaffoldWalk.mc.thePlayer.posX, ScaffoldWalk.mc.thePlayer.posY - 1.0, ScaffoldWalk.mc.thePlayer.posZ);
            if (ScaffoldWalk.mc.theWorld.getBlockState(bb).getBlock().getMaterial() == Material.air && ScaffoldWalk.mc.currentScreen == null && !Keyboard.isKeyDown(ScaffoldWalk.mc.gameSettings.keyBindJump.getKeyCodeDefault()) && this.adStrafe.getBoolean()) {
                if (ScaffoldWalk.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.EAST) {
                    if (this.b.getZ() + 0.5 > ScaffoldWalk.mc.thePlayer.posZ) {
                        ScaffoldWalk.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                    }
                    else {
                        ScaffoldWalk.mc.thePlayer.movementInput.moveStrafe = -1.0f;
                    }
                }
                else if (ScaffoldWalk.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.WEST) {
                    if (this.b.getZ() + 0.5 < ScaffoldWalk.mc.thePlayer.posZ) {
                        ScaffoldWalk.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                    }
                    else {
                        ScaffoldWalk.mc.thePlayer.movementInput.moveStrafe = -1.0f;
                    }
                }
                else if (ScaffoldWalk.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.SOUTH) {
                    if (this.b.getX() + 0.5 < ScaffoldWalk.mc.thePlayer.posX) {
                        ScaffoldWalk.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                    }
                    else {
                        ScaffoldWalk.mc.thePlayer.movementInput.moveStrafe = -1.0f;
                    }
                }
                else if (this.b.getX() + 0.5 > ScaffoldWalk.mc.thePlayer.posX) {
                    ScaffoldWalk.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                }
                else {
                    ScaffoldWalk.mc.thePlayer.movementInput.moveStrafe = -1.0f;
                }
                this.timeHelper.reset();
            }
            else {
                KeyBinding.setKeyBindState(ScaffoldWalk.mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(ScaffoldWalk.mc.gameSettings.keyBindLeft.getKeyCode()));
                KeyBinding.setKeyBindState(ScaffoldWalk.mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(ScaffoldWalk.mc.gameSettings.keyBindRight.getKeyCode()));
            }
        }
        if (this.moveFix.getBoolean()) {
            eventSilentMove.setSilent(true);
        }
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        this.rotated = false;
        this.b = this.getBlockPos();
        if (this.b == null) {
            this.rots[1] = this.rotationUtil.rotateToPitch((float)this.pitchSpeed.getValue(), this.rots[1], 85.34f + RandomUtil.nextFloat(-0.1f, 0.1f));
            this.lastRots = this.rots;
            this.setRotation();
            return;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Legit": {
                final float realYawwww = MathHelper.wrapAngleTo180_float(ScaffoldWalk.mc.thePlayer.rotationYaw);
                final double[] ex = this.getAdvancedDiagonalExpandXZ(this.b);
                double g = 0.85;
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward != 0.0f) {
                    g = 0.5;
                }
                final float[] f = this.rotationUtil.scaffoldRots(this.b.getX() + ex[0], this.b.getY() + g, this.b.getZ() + ex[1], this.lastRots[0], this.lastRots[1], (float)this.yawSpeed.getValue(), (float)this.pitchSpeed.getValue(), false);
                if (!ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || ScaffoldWalk.mc.thePlayer.onGround || ScaffoldWalk.mc.thePlayer.moveForward == 0.0f) {
                    this.rots[1] = this.rotationUtil.rotateToPitch((float)this.pitchSpeed.getValue(), this.rots[1], 80.34f + RandomUtil.nextFloat(-0.1f, 0.1f));
                }
                else {
                    this.rots[1] = f[1];
                }
                this.rots[0] = f[0];
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f) {
                    this.rots = this.rotationUtil.scaffoldRots(this.b.getX() + 0.5, this.b.getY(), this.b.getZ() + 0.5, this.lastRots[0], this.lastRots[1], (float)this.yawSpeed.getValue(), (float)this.pitchSpeed.getValue(), true);
                }
                final BlockPos bb = new BlockPos(ScaffoldWalk.mc.thePlayer.posX, ScaffoldWalk.mc.thePlayer.posY - 1.0, ScaffoldWalk.mc.thePlayer.posZ);
                if (ScaffoldWalk.mc.theWorld.getBlockState(bb).getBlock().getMaterial() == Material.air && ScaffoldWalk.mc.currentScreen == null && !Keyboard.isKeyDown(ScaffoldWalk.mc.gameSettings.keyBindJump.getKeyCodeDefault()) && !this.adStrafe.getBoolean() && ScaffoldWalk.mc.thePlayer.moveForward < 0.0f) {
                    if (this.enumFacing == EnumFacing.EAST) {
                        ScaffoldWalk.mc.thePlayer.motionX = 0.09505206927498006;
                    }
                    else if (this.enumFacing == EnumFacing.WEST) {
                        ScaffoldWalk.mc.thePlayer.motionX = -0.09505206927498006;
                    }
                    else if (this.enumFacing == EnumFacing.SOUTH) {
                        ScaffoldWalk.mc.thePlayer.motionZ = 0.09505206927498006;
                    }
                    else {
                        ScaffoldWalk.mc.thePlayer.motionZ = -0.09505206927498006;
                    }
                }
                this.rots = RotationUtil.mouseSens(this.rots[0], this.rots[1], this.lastRots[0], this.lastRots[1]);
                this.setRotation();
                this.lastRots = this.rots;
                break;
            }
            case "Normal": {
                final float realYaw = MathHelper.wrapAngleTo180_float(ScaffoldWalk.mc.thePlayer.rotationYaw);
                final double[] expandXZ = this.getAdvancedDiagonalExpandXZ(this.b);
                double p = 0.85;
                if (((realYaw > 22.5 && realYaw < 67.5) || (realYaw > 112.5 && realYaw < 157.5) || (realYaw < -22.5 && realYaw > -67.5) || (realYaw < -112.5 && realYaw > -157.0f)) && (ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward != 0.0f) {
                    if (this.b.getY() + 1 < ScaffoldWalk.mc.thePlayer.posY) {
                        p = 1.3;
                    }
                    else {
                        p = 0.8;
                    }
                }
                else if (ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() && !ScaffoldWalk.mc.thePlayer.onGround && ScaffoldWalk.mc.thePlayer.moveForward != 0.0f) {
                    p = 0.1;
                }
                final float[] floats = this.rotationUtil.scaffoldRots(this.b.getX() + expandXZ[0], this.b.getY() + p, this.b.getZ() + expandXZ[1], this.lastRots[0], this.lastRots[1], (float)this.yawSpeed.getValue(), (float)this.pitchSpeed.getValue(), false);
                final BlockPos bbb = new BlockPos(ScaffoldWalk.mc.thePlayer.posX, ScaffoldWalk.mc.thePlayer.posY - 1.0, ScaffoldWalk.mc.thePlayer.posZ);
                if (ScaffoldWalk.mc.theWorld.getBlockState(bbb).getBlock().getMaterial() == Material.air) {
                    this.rots = floats;
                }
                this.rots[0] = floats[0];
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f) {
                    this.rots = this.rotationUtil.scaffoldRots(this.b.getX() + 0.5, this.b.getY(), this.b.getZ() + 0.5, this.lastRots[0], this.lastRots[1], (float)this.yawSpeed.getValue(), (float)this.pitchSpeed.getValue(), true);
                }
                if (realYaw > 22.5 && realYaw < 67.5) {
                    this.rots[0] = this.rotationUtil.rotateToYaw((float)this.yawSpeed.getValue(), this.rots[0], 225.53f + RandomUtil.nextFloat(-0.1f, 0.1f));
                    if (ScaffoldWalk.mc.thePlayer.onGround) {
                        this.getYawBasedPitch(this.rots[0]);
                    }
                }
                else if (realYaw > 112.5 && realYaw < 157.5) {
                    this.rots[0] = this.rotationUtil.rotateToYaw((float)this.yawSpeed.getValue(), this.rots[0], 315.28f + RandomUtil.nextFloat(-0.1f, 0.1f));
                    if (ScaffoldWalk.mc.thePlayer.onGround) {
                        this.getYawBasedPitch(this.rots[0]);
                    }
                }
                else if (realYaw < -22.5 && realYaw > -67.5) {
                    this.rots[0] = this.rotationUtil.rotateToYaw((float)this.yawSpeed.getValue(), this.rots[0], 135.36f + RandomUtil.nextFloat(-0.1f, 0.1f));
                    if (ScaffoldWalk.mc.thePlayer.onGround) {
                        this.getYawBasedPitch(this.rots[0]);
                    }
                }
                else if (realYaw < -112.5 && realYaw > -157.0f) {
                    this.rots[0] = this.rotationUtil.rotateToYaw((float)this.yawSpeed.getValue(), this.rots[0], 45.19f + RandomUtil.nextFloat(-0.1f, 0.1f));
                    if (ScaffoldWalk.mc.thePlayer.onGround) {
                        this.getYawBasedPitch(this.rots[0]);
                    }
                }
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f) {
                    this.rots[1] = this.rotationUtil.rotateToPitch((float)this.pitchSpeed.getValue(), this.lastRots[1], 88.0f + RandomUtil.nextFloat(-1.0f, 1.0f));
                    this.rots[0] = this.lastRots[0];
                }
                this.rots = RotationUtil.mouseSens(this.rots[0], this.rots[1], this.lastRots[0], this.lastRots[1]);
                this.setRotation();
                this.lastRots = this.rots;
                break;
            }
            case "Intave": {
                final double[] expandXZ2 = this.getAdvancedDiagonalExpandXZ(this.b);
                double pp = 0.85;
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward != 0.0f) {
                    pp = -0.2;
                }
                final float[] floatss = this.rotationUtil.scaffoldRots(this.b.getX() + expandXZ2[0], this.b.getY() + pp, this.b.getZ() + expandXZ2[1], this.lastRots[0], this.lastRots[1], (float)this.yawSpeed.getValue(), (float)this.pitchSpeed.getValue(), false);
                final BlockPos bbbb = new BlockPos(ScaffoldWalk.mc.thePlayer.posX, ScaffoldWalk.mc.thePlayer.posY - 1.0, ScaffoldWalk.mc.thePlayer.posZ);
                if (ScaffoldWalk.mc.theWorld.getBlockState(bbbb).getBlock().getMaterial() == Material.air && ScaffoldWalk.mc.thePlayer.hurtTime == 0) {
                    this.rots = floatss;
                }
                else {
                    this.rots[1] = this.lastRots[1];
                }
                this.rots[0] = floatss[0];
                final float yaw = Augustus.getInstance().getYawPitchHelper().realYaw - 181.78935f;
                this.getYawBasedPitch(yaw);
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f) {
                    this.rots[1] = this.rotationUtil.rotateToPitch((float)this.pitchSpeed.getValue(), this.lastRots[1], 88.0f + RandomUtil.nextFloat(-1.0f, 1.0f));
                    this.rots[0] = this.lastRots[0];
                }
                this.rots = RotationUtil.mouseSens(this.rots[0], this.rots[1], this.lastRots[0], this.lastRots[1]);
                this.setRotation();
                this.lastRots = this.rots;
                this.lastB = this.b;
                break;
            }
        }
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        eventClick.setCanceled(true);
        boolean flag = true;
        if (this.block == null) {
            this.block = ScaffoldWalk.mc.thePlayer.inventory.getCurrentItem();
        }
        if (this.b == null || ScaffoldWalk.mc.currentScreen != null) {
            return;
        }
        if (!this.rotated) {
            return;
        }
        final ItemStack lastItem = ScaffoldWalk.mc.thePlayer.inventory.getCurrentItem();
        ItemStack itemstack = ScaffoldWalk.mc.thePlayer.inventory.getCurrentItem();
        if (!this.silentMode.getSelected().equals("None")) {
            for (int i = 36; i < ScaffoldWalk.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack itemStack = ScaffoldWalk.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0 && Augustus.getInstance().getBlockUtil().isValidStack(itemStack)) {
                    this.block = itemStack;
                    this.slotID = i - 36;
                    break;
                }
            }
            if (!this.silentMode.getSelected().equals("Spoof") || this.lastSlotID != this.slotID) {}
            itemstack = ScaffoldWalk.mc.thePlayer.inventoryContainer.getSlot(this.slotID + 36).getStack();
        }
        else {
            this.slotID = ScaffoldWalk.mc.thePlayer.inventory.currentItem;
            this.lastSlotID = ScaffoldWalk.mc.thePlayer.inventory.currentItem;
        }
        final MovingObjectPosition blockOver = ScaffoldWalk.mc.objectMouseOver;
        final BlockPos blockpos = blockOver.getBlockPos();
        if (ScaffoldWalk.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && ScaffoldWalk.mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air && !ScaffoldWalk.mc.playerController.func_181040_m()) {
            if (itemstack != null && !(itemstack.getItem() instanceof ItemBlock)) {
                return;
            }
            if (ScaffoldWalk.mc.thePlayer.posY < blockpos.getY() + 2) {
                if (blockOver.sideHit != EnumFacing.UP && blockOver.sideHit != EnumFacing.DOWN) {
                    if (this.silentMode.getSelected().equals("Switch")) {
                        ScaffoldWalk.mc.thePlayer.inventory.setCurrentItem(this.block.getItem(), 0, false, false);
                    }
                    if (ScaffoldWalk.mc.playerController.onPlayerRightClick(ScaffoldWalk.mc.thePlayer, ScaffoldWalk.mc.theWorld, itemstack, blockpos, ScaffoldWalk.mc.objectMouseOver.sideHit, ScaffoldWalk.mc.objectMouseOver.hitVec)) {
                        ScaffoldWalk.mc.thePlayer.swingItem();
                        this.lastTime = System.currentTimeMillis();
                        flag = false;
                        this.timeHelper.reset();
                    }
                    if (itemstack != null && itemstack.stackSize == 0) {
                        ScaffoldWalk.mc.thePlayer.inventory.mainInventory[this.slotID] = null;
                    }
                    ScaffoldWalk.mc.sendClickBlockToController(ScaffoldWalk.mc.currentScreen == null && ScaffoldWalk.mc.gameSettings.keyBindAttack.isKeyDown() && ScaffoldWalk.mc.inGameHasFocus);
                }
            }
            else if (blockOver.sideHit != EnumFacing.DOWN) {
                if (this.silentMode.getSelected().equals("Switch")) {
                    ScaffoldWalk.mc.thePlayer.inventory.setCurrentItem(this.block.getItem(), 0, false, false);
                }
                if (ScaffoldWalk.mc.playerController.onPlayerRightClick(ScaffoldWalk.mc.thePlayer, ScaffoldWalk.mc.theWorld, itemstack, blockpos, ScaffoldWalk.mc.objectMouseOver.sideHit, ScaffoldWalk.mc.objectMouseOver.hitVec)) {
                    ScaffoldWalk.mc.thePlayer.swingItem();
                    this.lastTime = System.currentTimeMillis();
                    flag = false;
                }
                if (itemstack != null && itemstack.stackSize == 0) {
                    ScaffoldWalk.mc.thePlayer.inventory.mainInventory[this.slotID] = null;
                }
                ScaffoldWalk.mc.sendClickBlockToController(ScaffoldWalk.mc.currentScreen == null && ScaffoldWalk.mc.gameSettings.keyBindAttack.isKeyDown() && ScaffoldWalk.mc.inGameHasFocus);
            }
        }
        if (lastItem != null && this.silentMode.getSelected().equals("Switch")) {
            ScaffoldWalk.mc.thePlayer.inventory.setCurrentItem(lastItem.getItem(), 0, false, false);
        }
        this.lastSlotID = this.slotID;
        if (!flag || itemstack == null || !this.silentMode.getSelected().equals("Switch")) {}
    }
    
    @EventTarget
    public void onEventSaveWalk(final EventSaveWalk eventSaveWalk) {
        if (ScaffoldWalk.mc.thePlayer.onGround) {}
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (!this.moveFix.getBoolean()) {
            eventJump.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (!this.moveFix.getBoolean()) {
            eventMove.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    private boolean buildForward() {
        final float realYaw = MathHelper.wrapAngleTo180_float(ScaffoldWalk.mc.thePlayer.rotationYaw);
        if (realYaw <= 77.5 || realYaw >= 102.5) {
            if (realYaw <= 167.5) {
                if (realYaw >= -167.0f) {
                    if (realYaw >= -77.5 || realYaw <= -102.5) {
                        if (realYaw <= -12.5 || realYaw < 12.5) {}
                    }
                }
            }
        }
        return true;
    }
    
    private void setRotation() {
        if (ScaffoldWalk.mc.currentScreen != null) {
            return;
        }
        ScaffoldWalk.mc.thePlayer.rotationYaw = this.rots[0];
        ScaffoldWalk.mc.thePlayer.rotationPitch = this.rots[1];
        ScaffoldWalk.mc.thePlayer.prevRotationYaw = this.lastRots[0];
        ScaffoldWalk.mc.thePlayer.prevRotationPitch = this.lastRots[1];
        this.rotated = true;
    }
    
    private void getYawBasedPitch(final float yaw) {
        final ArrayList<MovingObjectPosition> movingObjectPositions = new ArrayList<MovingObjectPosition>();
        final ArrayList<MovingObjectPosition> movingObjectPositions2 = new ArrayList<MovingObjectPosition>();
        double d = 2.5;
        if (Keyboard.isKeyDown(ScaffoldWalk.mc.gameSettings.keyBindJump.getKeyCode())) {
            d = 3.5;
        }
        final BlockPos bbbb = new BlockPos(ScaffoldWalk.mc.thePlayer.posX, ScaffoldWalk.mc.thePlayer.posY - 1.0, ScaffoldWalk.mc.thePlayer.posZ);
        for (float i = 40.0f; i < 89.0f; i += 0.01f) {
            final MovingObjectPosition j = ScaffoldWalk.mc.thePlayer.customRayTrace(d, 1.0f, yaw, i);
            if (j.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.isOkBlock(j.getBlockPos()) && !movingObjectPositions.contains(j)) {
                if (j.sideHit != EnumFacing.DOWN && j.sideHit != EnumFacing.UP && j.getBlockPos().getY() <= bbbb.getY()) {
                    movingObjectPositions.add(j);
                }
                movingObjectPositions2.add(j);
            }
        }
        movingObjectPositions.sort(Comparator.comparingDouble(m -> ScaffoldWalk.mc.thePlayer.getDistanceSq(m.getBlockPos().add(0.5, 0.5, 0.5))));
        MovingObjectPosition k = null;
        if (movingObjectPositions.size() > 0) {
            k = movingObjectPositions.get(0);
        }
        if (ScaffoldWalk.mc.theWorld.getBlockState(bbbb).getBlock().getMaterial() == Material.air && k != null && ScaffoldWalk.mc.thePlayer.hurtTime == 0) {
            final MovingObjectPosition objectPosition = ScaffoldWalk.mc.thePlayer.customRayTrace(d, 1.0f, yaw, this.lastRots[1]);
            if (objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.isOkBlock(k.getBlockPos()) && objectPosition.sideHit != EnumFacing.DOWN && objectPosition.sideHit != EnumFacing.UP && objectPosition.getBlockPos().getY() <= bbbb.getY()) {
                this.rots[1] = this.lastRots[1];
            }
            else {
                final float[] f = this.rotationUtil.scaffoldRots(k.hitVec.xCoord, k.getBlockPos().getY() + RandomUtil.nextDouble(0.45, 0.55), k.hitVec.zCoord, this.lastRots[0], this.lastRots[1], (float)this.yawSpeed.getValue(), (float)this.pitchSpeed.getValue(), false);
                this.rots[1] = f[1];
            }
        }
        if (movingObjectPositions2.size() != 0 || ScaffoldWalk.mc.thePlayer.hurtTime > 0 || ScaffoldWalk.mc.thePlayer.onGround) {
            this.rots[0] = yaw;
        }
    }
    
    private double[] getAdvancedDiagonalExpandXZ(final BlockPos blockPos) {
        final double[] xz = new double[2];
        final Vec2d difference = new Vec2d(blockPos.getX() - ScaffoldWalk.mc.thePlayer.posX, blockPos.getZ() - ScaffoldWalk.mc.thePlayer.posZ);
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
    
    private BlockPos getBlockPos() {
        final BlockPos blockPos = new BlockPos(ScaffoldWalk.mc.thePlayer.posX, ScaffoldWalk.mc.thePlayer.posY - 1.0, ScaffoldWalk.mc.thePlayer.posZ);
        if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f && this.isOkBlock(blockPos.add(0, -1, 0))) {
            return blockPos.add(0, -1, 0);
        }
        switch (ScaffoldWalk.mc.getRenderViewEntity().getHorizontalFacing()) {
            case EAST: {
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f && this.isOkBlock(blockPos.add(0, -1, 0))) {
                    return blockPos.add(0, -1, 0);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, 0))) {
                    return blockPos.add(-1, 0, 0);
                }
                if (this.isOkBlock(blockPos.add(1, 0, 0))) {
                    return blockPos.add(1, 0, 0);
                }
                if (this.isOkBlock(blockPos.add(0, 0, 1))) {
                    return blockPos.add(0, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(0, 0, -1))) {
                    return blockPos.add(0, 0, -1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, 1))) {
                    return blockPos.add(1, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, -1))) {
                    return blockPos.add(-1, 0, -1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, 1))) {
                    return blockPos.add(-1, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, -1))) {
                    return blockPos.add(1, 0, -1);
                }
                if (!ScaffoldWalk.mc.thePlayer.onGround) {
                    if (this.isOkBlock(blockPos.add(-1, -1, 0))) {
                        return blockPos.add(-1, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, 0))) {
                        return blockPos.add(1, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, 1))) {
                        return blockPos.add(0, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, -1))) {
                        return blockPos.add(0, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, 1))) {
                        return blockPos.add(1, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, -1))) {
                        return blockPos.add(-1, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, 1))) {
                        return blockPos.add(-1, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(2, -1, -2))) {
                        return blockPos.add(2, -1, -2);
                    }
                    if (this.isOkBlock(blockPos.add(-2, -1, 0))) {
                        return blockPos.add(-2, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(2, -1, 0))) {
                        return blockPos.add(2, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, 2))) {
                        return blockPos.add(0, -1, 2);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, -2))) {
                        return blockPos.add(0, -1, -2);
                    }
                }
                System.out.println("No Block found");
                return null;
            }
            case SOUTH: {
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f && this.isOkBlock(blockPos.add(0, -1, 0))) {
                    return blockPos.add(0, -1, 0);
                }
                if (this.isOkBlock(blockPos.add(0, 0, -1))) {
                    return blockPos.add(0, 0, -1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, 0))) {
                    return blockPos.add(-1, 0, 0);
                }
                if (this.isOkBlock(blockPos.add(1, 0, 0))) {
                    return blockPos.add(1, 0, 0);
                }
                if (this.isOkBlock(blockPos.add(0, 0, 1))) {
                    return blockPos.add(0, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, 1))) {
                    return blockPos.add(1, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, -1))) {
                    return blockPos.add(-1, 0, -1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, 1))) {
                    return blockPos.add(-1, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, -1))) {
                    return blockPos.add(1, 0, -1);
                }
                if (!ScaffoldWalk.mc.thePlayer.onGround) {
                    if (this.isOkBlock(blockPos.add(0, -1, -1))) {
                        return blockPos.add(0, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, 0))) {
                        return blockPos.add(-1, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, 0))) {
                        return blockPos.add(1, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, 1))) {
                        return blockPos.add(0, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, 1))) {
                        return blockPos.add(1, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, -1))) {
                        return blockPos.add(-1, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, 1))) {
                        return blockPos.add(-1, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, -1))) {
                        return blockPos.add(1, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, -2))) {
                        return blockPos.add(0, -1, -2);
                    }
                    if (this.isOkBlock(blockPos.add(-2, -1, 0))) {
                        return blockPos.add(-2, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(2, -1, 0))) {
                        return blockPos.add(2, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, 2))) {
                        return blockPos.add(0, -1, 2);
                    }
                }
                System.out.println("No Block found");
                return null;
            }
            case WEST: {
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f && this.isOkBlock(blockPos.add(0, -1, 0))) {
                    return blockPos.add(0, -1, 0);
                }
                if (this.isOkBlock(blockPos.add(1, 0, 0))) {
                    return blockPos.add(1, 0, 0);
                }
                if (this.isOkBlock(blockPos.add(0, 0, 1))) {
                    return blockPos.add(0, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, 0))) {
                    return blockPos.add(-1, 0, 0);
                }
                if (this.isOkBlock(blockPos.add(0, 0, -1))) {
                    return blockPos.add(0, 0, -1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, 1))) {
                    return blockPos.add(1, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, -1))) {
                    return blockPos.add(-1, 0, -1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, 1))) {
                    return blockPos.add(-1, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, -1))) {
                    return blockPos.add(1, 0, -1);
                }
                if (!ScaffoldWalk.mc.thePlayer.onGround) {
                    if (this.isOkBlock(blockPos.add(1, -1, 0))) {
                        return blockPos.add(1, 0, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, 1))) {
                        return blockPos.add(0, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, 0))) {
                        return blockPos.add(-1, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, -1))) {
                        return blockPos.add(0, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, 1))) {
                        return blockPos.add(1, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, -1))) {
                        return blockPos.add(-1, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, 1))) {
                        return blockPos.add(-1, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, -1))) {
                        return blockPos.add(1, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(2, -1, 0))) {
                        return blockPos.add(2, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, 2))) {
                        return blockPos.add(0, -1, 2);
                    }
                    if (this.isOkBlock(blockPos.add(-2, -1, 0))) {
                        return blockPos.add(-2, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, -2))) {
                        return blockPos.add(0, -1, -2);
                    }
                }
                System.out.println("No Block found");
                return null;
            }
            default: {
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && ScaffoldWalk.mc.thePlayer.moveForward == 0.0f && ScaffoldWalk.mc.thePlayer.moveStrafing == 0.0f && this.isOkBlock(blockPos.add(0, -1, 0))) {
                    return blockPos.add(0, -1, 0);
                }
                if (this.isOkBlock(blockPos.add(0, 0, 1))) {
                    return blockPos.add(0, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, 0))) {
                    return blockPos.add(1, 0, 0);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, 0))) {
                    return blockPos.add(-1, 0, 0);
                }
                if (this.isOkBlock(blockPos.add(0, 0, -1))) {
                    return blockPos.add(0, 0, -1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, 1))) {
                    return blockPos.add(1, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, -1))) {
                    return blockPos.add(-1, 0, -1);
                }
                if (this.isOkBlock(blockPos.add(-1, 0, 1))) {
                    return blockPos.add(-1, 0, 1);
                }
                if (this.isOkBlock(blockPos.add(1, 0, -1))) {
                    return blockPos.add(1, 0, -1);
                }
                if (!ScaffoldWalk.mc.thePlayer.onGround) {
                    if (this.isOkBlock(blockPos.add(0, -1, 1))) {
                        return blockPos.add(0, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, 0))) {
                        return blockPos.add(1, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, 0))) {
                        return blockPos.add(-1, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, -1))) {
                        return blockPos.add(0, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, 1))) {
                        return blockPos.add(1, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, -1))) {
                        return blockPos.add(-1, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(-1, -1, 1))) {
                        return blockPos.add(-1, -1, 1);
                    }
                    if (this.isOkBlock(blockPos.add(1, -1, -1))) {
                        return blockPos.add(1, -1, -1);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, 2))) {
                        return blockPos.add(0, -1, 2);
                    }
                    if (this.isOkBlock(blockPos.add(2, -1, 0))) {
                        return blockPos.add(2, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(-2, -1, 0))) {
                        return blockPos.add(-2, -1, 0);
                    }
                    if (this.isOkBlock(blockPos.add(0, -1, -2))) {
                        return blockPos.add(0, -1, -2);
                    }
                }
                if ((ScaffoldWalk.mc.gameSettings.keyBindJump.isKeyDown() || !ScaffoldWalk.mc.thePlayer.onGround) && this.isOkBlock(blockPos.add(0, -1, 0))) {
                    return blockPos.add(0, -1, 0);
                }
                System.out.println("No Block found");
                return null;
            }
        }
    }
    
    private boolean isOkBlock(final BlockPos blockPos) {
        final Block block = ScaffoldWalk.mc.theWorld.getBlockState(blockPos).getBlock();
        return !(block instanceof BlockLiquid) && !(block instanceof BlockAir) && !(block instanceof BlockChest) && !(block instanceof BlockFurnace);
    }
    
    public int getSlotID() {
        return this.slotID;
    }
}
