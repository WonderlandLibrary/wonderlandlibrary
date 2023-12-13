// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.world;

import net.augustus.utils.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.augustus.utils.BlockUtil;
import net.augustus.utils.RayTraceUtil;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import net.augustus.events.EventSwingItemClientSide;
import net.augustus.events.EventPostMouseOver;
import net.augustus.events.EventPreMotion;
import net.augustus.utils.MoveUtil;
import net.minecraft.block.material.Material;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventSaveWalk;
import net.augustus.events.EventJump;
import net.augustus.events.EventMove;
import java.security.SecureRandom;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.augustus.events.EventClick;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.Augustus;
import net.minecraft.util.MathHelper;
import net.augustus.utils.RandomUtil;
import net.augustus.events.EventEarlyTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.augustus.settings.StringValue;
import net.minecraft.util.MovingObjectPosition;
import java.util.HashMap;
import net.minecraft.util.Vec3;
import java.util.ArrayList;
import net.augustus.utils.TimeHelper;
import net.augustus.utils.RotationUtil;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class BlockFly extends Module
{
    public final DoubleValue yawSpeed;
    public final DoubleValue pitchSpeed;
    public final BooleanValue rayCast;
    public final BooleanValue playerYaw;
    public final BooleanValue blockSafe;
    public final BooleanValue moonWalk;
    public final BooleanValue latestRotate;
    public final BooleanValue latestPlace;
    public final BooleanValue predict;
    public final DoubleValue backupTicks;
    public final BooleanValue adStrafe;
    public final BooleanValue adStrafeLegit;
    public final BooleanValue sprint;
    public final BooleanValue moveFix;
    public final BooleanValue spamClick;
    public final DoubleValue spamClickDelay;
    public final BooleanValue intaveHit;
    public final BooleanValue rotateToBlock;
    public final BooleanValue correctSide;
    public final BooleanValue sameY;
    public final BooleanValue esp;
    public final BooleanValue noSwing;
    public final BooleanValue startSneak;
    public final BooleanValue sneak;
    public final BooleanValue sneakOnPlace;
    public final DoubleValue sneakTicks;
    public final DoubleValue sneakBlocks;
    public final DoubleValue sneakBlocksDiagonal;
    public final BooleanValue sneakDelayBool;
    public final DoubleValue sneakDelay;
    public final DoubleValue timerSpeed;
    private final RotationUtil rotationUtil;
    private final TimeHelper startTimeHelper;
    private final TimeHelper sneakTimeHelper;
    private final TimeHelper hitTimeHelper;
    private final ArrayList<Vec3> lastPositions;
    private final HashMap<float[], MovingObjectPosition> map;
    public StringValue silentMode;
    public float[] rots;
    public float[] lastRots;
    private int slotID;
    private BlockPos b;
    private Vec3 aimPos;
    private long lastTime;
    private int blockCounter;
    private Block playerBlock;
    private double[] xyz;
    private MovingObjectPosition objectPosition;
    private int sneakCounter;
    private int isSneakingTicks;
    private int randomDelay;
    
    public BlockFly() {
        super("BlockFly", new Color(182, 194, 207), Categorys.WORLD);
        this.yawSpeed = new DoubleValue(2, "YawSpeed", this, 60.0, 0.0, 180.0, 0);
        this.pitchSpeed = new DoubleValue(3, "PitchSpeed", this, 60.0, 0.0, 180.0, 0);
        this.rayCast = new BooleanValue(6, "RayCast", this, false);
        this.playerYaw = new BooleanValue(9, "PlayerYaw", this, false);
        this.blockSafe = new BooleanValue(25, "BlockSafe", this, false);
        this.moonWalk = new BooleanValue(21, "MoonWalk", this, false);
        this.latestRotate = new BooleanValue(7, "LatestRotate", this, false);
        this.latestPlace = new BooleanValue(24, "LatestPlace", this, false);
        this.predict = new BooleanValue(9, "Predict", this, false);
        this.backupTicks = new DoubleValue(8, "Backup", this, 1.0, 0.0, 3.0, 0);
        this.adStrafe = new BooleanValue(13, "AdStrafe", this, true);
        this.adStrafeLegit = new BooleanValue(19, "AdStrafeLegit", this, true);
        this.sprint = new BooleanValue(4, "Sprint", this, false);
        this.moveFix = new BooleanValue(5, "MoveFix", this, false);
        this.spamClick = new BooleanValue(20, "SpamClick", this, false);
        this.spamClickDelay = new DoubleValue(22, "ClickDelay", this, 0.0, 0.0, 200.0, 0);
        this.intaveHit = new BooleanValue(23, "IntaveHit", this, false);
        this.rotateToBlock = new BooleanValue(10, "Rotate", this, true);
        this.correctSide = new BooleanValue(12, "CorrectSide", this, true);
        this.sameY = new BooleanValue(18, "SameY", this, false);
        this.esp = new BooleanValue(26, "ESP", this, true);
        this.noSwing = new BooleanValue(25, "NoSwing", this, false);
        this.startSneak = new BooleanValue(29, "StartSneak", this, false);
        this.sneak = new BooleanValue(13, "Sneak", this, false);
        this.sneakOnPlace = new BooleanValue(17, "SneakOnPlace", this, false);
        this.sneakTicks = new DoubleValue(27, "SneakTicks", this, 1.0, 1.0, 10.0, 0);
        this.sneakBlocks = new DoubleValue(28, "SneakBlocksF", this, 1.0, 1.0, 15.0, 0);
        this.sneakBlocksDiagonal = new DoubleValue(31, "SneakBlocksD", this, 1.0, 1.0, 15.0, 0);
        this.sneakDelayBool = new BooleanValue(30, "SneakDelay", this, false);
        this.sneakDelay = new DoubleValue(16, "SneakDelay", this, 1000.0, 0.0, 4000.0, 0);
        this.timerSpeed = new DoubleValue(15, "Timer", this, 1.0, 0.1, 4.0, 2);
        this.rotationUtil = new RotationUtil();
        this.startTimeHelper = new TimeHelper();
        this.sneakTimeHelper = new TimeHelper();
        this.hitTimeHelper = new TimeHelper();
        this.lastPositions = new ArrayList<Vec3>();
        this.map = new HashMap<float[], MovingObjectPosition>();
        this.silentMode = new StringValue(1, "SilentMode", this, "Spoof", new String[] { "Switch", "Spoof", "None" });
        this.rots = new float[2];
        this.lastRots = new float[2];
        this.blockCounter = 0;
        this.xyz = new double[3];
        this.objectPosition = null;
        this.sneakCounter = 4;
        this.isSneakingTicks = 0;
        this.randomDelay = 0;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.sneakCounter = 4;
        this.blockCounter = 0;
        if (BlockFly.mc.thePlayer != null) {
            BlockFly.mc.getTimer().timerSpeed = 1.0f;
            this.rots[0] = BlockFly.mc.thePlayer.rotationYaw;
            this.rots[1] = BlockFly.mc.thePlayer.rotationPitch;
            this.lastRots[0] = BlockFly.mc.thePlayer.prevRotationYaw;
            this.lastRots[1] = BlockFly.mc.thePlayer.prevRotationPitch;
            this.b = null;
            this.startTimeHelper.reset();
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        BlockFly.mc.getTimer().timerSpeed = 1.0f;
        this.rots = this.lastRots;
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventEarlyTick) {
        this.objectPosition = null;
        if (this.shouldScaffold()) {
            this.b = this.getBlockPos();
            if (this.playerYaw.getBoolean() && this.rayCast.getBoolean()) {
                if (this.b != null) {
                    if (this.lastPositions.size() > 20) {
                        this.lastPositions.remove(0);
                    }
                    final Vec3 playerPosition = new Vec3(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY, BlockFly.mc.thePlayer.posZ);
                    this.lastPositions.add(playerPosition);
                    this.lastRots[0] = this.rots[0];
                    this.lastRots[1] = this.rots[1];
                    this.rots = this.getPlayerYawRotation();
                    if (BlockFly.mc.thePlayer.hurtResistantTime > 0 && this.blockSafe.getBoolean()) {
                        this.rots = this.getRayCastRots();
                    }
                    if (this.objectPosition != null) {
                        this.aimPos = this.objectPosition.hitVec;
                    }
                }
                else {
                    this.lastRots[0] = this.rots[0];
                    this.lastRots[1] = this.rots[1];
                }
            }
            else if (!this.rayCast.getBoolean()) {
                final Vec3 pos = this.getAimPosBasic();
                this.aimPos = pos;
                if (this.rotateToBlock.getBoolean()) {
                    if (this.correctSide.getBoolean()) {
                        if (this.b != null && this.shouldBuild() && pos != null) {
                            final float yawSpeed = MathHelper.clamp_float((float)(this.yawSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f)), 0.0f, 180.0f);
                            final float pitchSpeed = MathHelper.clamp_float((float)(this.pitchSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f)), 0.0f, 180.0f);
                            this.lastRots[0] = this.rots[0];
                            this.lastRots[1] = this.rots[1];
                            this.rots = RotationUtil.positionRotation(pos.xCoord, pos.yCoord, pos.zCoord, this.lastRots, yawSpeed, pitchSpeed, false);
                        }
                        else {
                            this.lastRots[0] = this.rots[0];
                            this.lastRots[1] = this.rots[1];
                        }
                    }
                    else if (this.b != null && this.shouldBuild()) {
                        final MovingObjectPosition objectPosition = BlockFly.mc.thePlayer.customRayTrace(4.5, 1.0f, this.rots[0], this.rots[1]);
                        if (objectPosition == null || objectPosition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !objectPosition.getBlockPos().equalsBlockPos(this.b)) {
                            final float yawSpeed2 = MathHelper.clamp_float((float)(this.yawSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f)), 0.0f, 180.0f);
                            final float pitchSpeed2 = MathHelper.clamp_float((float)(this.pitchSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f)), 0.0f, 180.0f);
                            this.lastRots[0] = this.rots[0];
                            this.lastRots[1] = this.rots[1];
                            this.rots = RotationUtil.positionRotation(this.b.getX() + 0.5, this.b.getY() + 0.5, this.b.getZ() + 0.5, this.lastRots, yawSpeed2, pitchSpeed2, false);
                            this.aimPos = new Vec3(this.b.getX() + 0.5, this.b.getY() + 1, this.b.getZ() + 0.5);
                        }
                        else {
                            this.lastRots[0] = this.rots[0];
                            this.lastRots[1] = this.rots[1];
                        }
                    }
                    else {
                        this.lastRots[0] = this.rots[0];
                        this.lastRots[1] = this.rots[1];
                    }
                }
                else {
                    this.rots = new float[] { Augustus.getInstance().getYawPitchHelper().realYaw, Augustus.getInstance().getYawPitchHelper().realPitch };
                    this.lastRots = new float[] { Augustus.getInstance().getYawPitchHelper().realLastYaw, Augustus.getInstance().getYawPitchHelper().realLastPitch };
                    this.aimPos = new Vec3(this.b.getX() + 0.5, this.b.getY() + 0.5, this.b.getZ() + 0.5);
                }
            }
            else if (this.rayCast.getBoolean() && !this.playerYaw.getBoolean()) {
                if (this.b != null) {
                    if (this.lastPositions.size() > 20) {
                        this.lastPositions.remove(0);
                    }
                    final Vec3 playerPosition = new Vec3(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY, BlockFly.mc.thePlayer.posZ);
                    this.lastPositions.add(playerPosition);
                    this.lastRots[0] = this.rots[0];
                    this.lastRots[1] = this.rots[1];
                    this.rots = this.getRayCastRots();
                    if (this.objectPosition != null) {
                        this.aimPos = this.objectPosition.hitVec;
                    }
                }
                else {
                    this.lastRots[0] = this.rots[0];
                    this.lastRots[1] = this.rots[1];
                }
            }
            this.setRotation();
        }
        this.playerBlock = BlockFly.mc.theWorld.getBlockState(new BlockPos(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY - 0.5, BlockFly.mc.thePlayer.posZ)).getBlock();
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        eventClick.setCanceled(true);
        if (this.shouldScaffold() && this.b != null) {
            final ItemStack itemStack = this.getItemStack();
            final ItemStack lastItem = BlockFly.mc.thePlayer.inventory.getCurrentItem();
            final int slot = BlockFly.mc.thePlayer.inventory.currentItem;
            if (!this.rayCast.getBoolean()) {
                boolean flag = this.hitTimeHelper.reached(this.randomDelay);
                if (flag) {
                    this.hitTimeHelper.reset();
                }
                if (this.shouldBuild()) {
                    final EnumFacing enumFacing = this.getPlaceSide(this.b);
                    if (enumFacing != null) {
                        if (this.aimPos == null) {
                            this.aimPos = new Vec3(this.b.getX() + 0.5, this.b.getY() + 0.5, this.b.getZ() + 0.5);
                        }
                        if (this.silentMode.getSelected().equals("Switch")) {
                            BlockFly.mc.thePlayer.inventory.setCurrentItem(itemStack.getItem(), 0, false, false);
                        }
                        if (BlockFly.mc.playerController.onPlayerRightClick(BlockFly.mc.thePlayer, BlockFly.mc.theWorld, itemStack, this.b, enumFacing, this.aimPos)) {
                            BlockFly.mc.thePlayer.swingItem();
                            this.sneakCounter = 0;
                            ++this.blockCounter;
                            flag = false;
                        }
                    }
                }
                if (flag && itemStack != null && this.spamClick.getBoolean() && BlockFly.mc.playerController.sendUseItem(BlockFly.mc.thePlayer, BlockFly.mc.theWorld, itemStack)) {
                    BlockFly.mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                }
            }
            else {
                MovingObjectPosition objectPosition = BlockFly.mc.objectMouseOver;
                if (this.objectPosition != null) {
                    objectPosition = this.objectPosition;
                }
                if (objectPosition != null) {
                    boolean flag2 = this.hitTimeHelper.reached(this.randomDelay);
                    if (flag2) {
                        this.hitTimeHelper.reset();
                    }
                    switch (objectPosition.typeOfHit) {
                        case ENTITY: {
                            if (BlockFly.mc.playerController.func_178894_a(BlockFly.mc.thePlayer, objectPosition.entityHit, objectPosition)) {
                                flag2 = false;
                                break;
                            }
                            if (BlockFly.mc.playerController.interactWithEntitySendPacket(BlockFly.mc.thePlayer, objectPosition.entityHit)) {
                                flag2 = false;
                                break;
                            }
                            break;
                        }
                        case BLOCK: {
                            if (objectPosition.getBlockPos().equalsBlockPos(this.b)) {
                                if (objectPosition.sideHit == EnumFacing.UP) {
                                    if (this.sameY.getBoolean() && (!BlockFly.mc.gameSettings.keyBindJump.isKeyDown() || !Mouse.isButtonDown(1))) {
                                        break;
                                    }
                                    if (this.silentMode.getSelected().equals("Switch")) {
                                        BlockFly.mc.thePlayer.inventory.setCurrentItem(itemStack.getItem(), 0, false, false);
                                    }
                                    if (BlockFly.mc.playerController.onPlayerRightClick(BlockFly.mc.thePlayer, BlockFly.mc.theWorld, itemStack, objectPosition.getBlockPos(), objectPosition.sideHit, objectPosition.hitVec)) {
                                        BlockFly.mc.thePlayer.swingItem();
                                        System.out.println("Placed");
                                        this.sneakCounter = 0;
                                        ++this.blockCounter;
                                        flag2 = false;
                                        break;
                                    }
                                    break;
                                }
                                else {
                                    if (this.silentMode.getSelected().equals("Switch")) {
                                        BlockFly.mc.thePlayer.inventory.setCurrentItem(itemStack.getItem(), 0, false, false);
                                    }
                                    if ((this.shouldBuild() || !this.latestPlace.getBoolean() || (this.latestPlace.getBoolean() && !this.latestRotate.getBoolean())) && BlockFly.mc.playerController.onPlayerRightClick(BlockFly.mc.thePlayer, BlockFly.mc.theWorld, itemStack, objectPosition.getBlockPos(), objectPosition.sideHit, objectPosition.hitVec)) {
                                        BlockFly.mc.thePlayer.swingItem();
                                        System.out.println("Placed");
                                        this.sneakCounter = 0;
                                        ++this.blockCounter;
                                        flag2 = false;
                                        break;
                                    }
                                    break;
                                }
                            }
                            else {
                                if (!this.isNearbyBlockPos(objectPosition.getBlockPos()) || objectPosition.sideHit == EnumFacing.UP) {
                                    break;
                                }
                                if (this.silentMode.getSelected().equals("Switch")) {
                                    BlockFly.mc.thePlayer.inventory.setCurrentItem(itemStack.getItem(), 0, false, false);
                                }
                                if ((this.shouldBuild() || !this.latestPlace.getBoolean() || (this.latestPlace.getBoolean() && !this.latestRotate.getBoolean())) && BlockFly.mc.playerController.onPlayerRightClick(BlockFly.mc.thePlayer, BlockFly.mc.theWorld, itemStack, objectPosition.getBlockPos(), objectPosition.sideHit, objectPosition.hitVec)) {
                                    BlockFly.mc.thePlayer.swingItem();
                                    System.out.println("Placed");
                                    this.sneakCounter = 0;
                                    ++this.blockCounter;
                                    flag2 = false;
                                    break;
                                }
                                break;
                            }
                            break;
                        }
                    }
                    if (flag2 && itemStack != null && this.spamClick.getBoolean() && BlockFly.mc.playerController.sendUseItem(BlockFly.mc.thePlayer, BlockFly.mc.theWorld, itemStack)) {
                        BlockFly.mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                    }
                }
            }
            if (itemStack != null && itemStack.stackSize == 0) {
                BlockFly.mc.thePlayer.inventory.mainInventory[this.slotID] = null;
            }
            if (this.silentMode.getSelected().equals("Switch")) {
                if (lastItem != null) {
                    BlockFly.mc.thePlayer.inventory.setCurrentItem(lastItem.getItem(), 0, false, false);
                }
                else {
                    BlockFly.mc.thePlayer.inventory.currentItem = slot;
                }
            }
        }
        BlockFly.mc.sendClickBlockToController(false);
        this.setRandomDelay();
    }
    
    private void setRandomDelay() {
        if (this.intaveHit.getBoolean()) {
            this.randomDelay = 50;
        }
        else if (this.spamClickDelay.getValue() == 0.0) {
            this.randomDelay = 0;
        }
        else {
            final SecureRandom secureRandom = new SecureRandom();
            this.randomDelay = (int)(this.spamClickDelay.getValue() + secureRandom.nextInt(60));
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
    
    @EventTarget
    public void onEventSaveWalk(final EventSaveWalk eventSaveWalk) {
        if (this.sneak.getBoolean() && this.sneakOnPlace.getBoolean()) {
            eventSaveWalk.setDisableSneak(true);
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        if (this.moveFix.getBoolean()) {
            eventSilentMove.setSilent(true);
        }
        if (this.startSneak.getBoolean() && (!this.startTimeHelper.reached(200L) || (BlockFly.mc.thePlayer.motionX == 0.0 && BlockFly.mc.thePlayer.motionZ == 0.0 && BlockFly.mc.thePlayer.onGround))) {
            BlockFly.mc.thePlayer.movementInput.sneak = true;
        }
        Block playerBlock = BlockFly.mc.theWorld.getBlockState(new BlockPos(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY - 0.5, BlockFly.mc.thePlayer.posZ)).getBlock();
        if (this.sneak.getBoolean() && !BlockFly.mc.gameSettings.keyBindJump.isKeyDown()) {
            if (!this.sneakOnPlace.getBoolean()) {
                if (this.sneakDelayBool.getBoolean()) {
                    if (this.sneakTimeHelper.reached((long)this.sneakDelay.getValue()) && BlockFly.mc.thePlayer.onGround && this.shouldSneak()) {
                        this.isSneakingTicks = 0;
                        this.sneakTimeHelper.reset();
                    }
                }
                else if (this.buildForward()) {
                    if (this.blockCounter >= this.sneakBlocks.getValue() && BlockFly.mc.thePlayer.onGround && this.shouldSneak()) {
                        this.isSneakingTicks = 0;
                    }
                }
                else if (this.blockCounter >= this.sneakBlocksDiagonal.getValue() && BlockFly.mc.thePlayer.onGround && this.shouldSneak()) {
                    this.isSneakingTicks = 0;
                }
                if (this.isSneakingTicks < this.sneakTicks.getValue()) {
                    this.blockCounter = 0;
                    BlockFly.mc.thePlayer.movementInput.sneak = true;
                    this.sneakTimeHelper.reset();
                    ++this.isSneakingTicks;
                }
            }
            else if (!BlockFly.mc.thePlayer.movementInput.jump) {
                playerBlock = BlockFly.mc.theWorld.getBlockState(new BlockPos(BlockFly.mc.thePlayer.posX - BlockFly.mc.thePlayer.motionX, BlockFly.mc.thePlayer.posY - 0.5, BlockFly.mc.thePlayer.posZ - BlockFly.mc.thePlayer.motionZ)).getBlock();
                final int random = RandomUtil.nextInt(2, 3);
                if (this.sneakCounter == 1 || this.sneakCounter <= random) {
                    BlockFly.mc.thePlayer.movementInput.sneak = true;
                    if (this.sneakCounter == random) {
                        this.sneakCounter = 10;
                    }
                }
            }
        }
        ++this.sneakCounter;
        if (this.isTower()) {
            return;
        }
        if (this.rayCast.getBoolean() && this.adStrafe.getBoolean() && this.b != null && (playerBlock.getMaterial() == Material.air || this.adStrafeLegit.getBoolean()) && this.shouldScaffold() && !BlockFly.mc.gameSettings.keyBindJump.isKeyDown() && MoveUtil.isMoving() && this.buildForward() && (!this.moonWalk.getBoolean() || !this.playerYaw.getBoolean())) {
            if (BlockFly.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.EAST) {
                if (this.b.getZ() + 0.5 > BlockFly.mc.thePlayer.posZ) {
                    this.ad1();
                }
                else {
                    this.ad2();
                }
            }
            else if (BlockFly.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.WEST) {
                if (this.b.getZ() + 0.5 < BlockFly.mc.thePlayer.posZ) {
                    this.ad1();
                }
                else {
                    this.ad2();
                }
            }
            else if (BlockFly.mc.thePlayer.getHorizontalFacing(this.rots[0]) == EnumFacing.SOUTH) {
                if (this.b.getX() + 0.5 < BlockFly.mc.thePlayer.posX) {
                    this.ad1();
                }
                else {
                    this.ad2();
                }
            }
            else if (this.b.getX() + 0.5 > BlockFly.mc.thePlayer.posX) {
                this.ad1();
            }
            else {
                this.ad2();
            }
        }
        if (this.moonWalk.getBoolean() && this.playerYaw.getBoolean() && this.b != null && this.rayCast.getBoolean() && this.buildForward() && MoveUtil.isMoving()) {
            if (BlockFly.mc.thePlayer.getHorizontalFacing(this.rots[0] - 18.6f) == EnumFacing.EAST) {
                if (this.b.getZ() + 0.5 > BlockFly.mc.thePlayer.posZ) {
                    BlockFly.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                }
            }
            else if (BlockFly.mc.thePlayer.getHorizontalFacing(this.rots[0] - 18.6f) == EnumFacing.WEST) {
                if (this.b.getZ() + 0.5 < BlockFly.mc.thePlayer.posZ) {
                    BlockFly.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                }
            }
            else if (BlockFly.mc.thePlayer.getHorizontalFacing(this.rots[0] - 18.6f) == EnumFacing.SOUTH) {
                if (this.b.getX() + 0.5 < BlockFly.mc.thePlayer.posX) {
                    BlockFly.mc.thePlayer.movementInput.moveStrafe = 1.0f;
                }
            }
            else if (this.b.getX() + 0.5 > BlockFly.mc.thePlayer.posX) {
                BlockFly.mc.thePlayer.movementInput.moveStrafe = 1.0f;
            }
        }
    }
    
    @EventTarget
    public void onEventPreMotion(final EventPreMotion eventPreMotion) {
        BlockFly.mc.getTimer().timerSpeed = (float)this.timerSpeed.getValue();
    }
    
    @EventTarget
    public void onEventPostMouseOver(final EventPostMouseOver eventPostMouseOver) {
        if (this.objectPosition != null) {
            BlockFly.mc.objectMouseOver = this.objectPosition;
        }
    }
    
    @EventTarget
    public void onEventSwingItemClientSide(final EventSwingItemClientSide eventSwingItemClientSide) {
        if (this.noSwing.getBoolean()) {
            eventSwingItemClientSide.cancel = true;
        }
    }
    
    private void ad1() {
        if (BlockFly.mc.thePlayer.movementInput.moveForward != 0.0f) {
            BlockFly.mc.thePlayer.movementInput.moveStrafe = ((BlockFly.mc.thePlayer.movementInput.moveForward > 0.0f) ? 1.0f : -1.0f);
        }
        else if (BlockFly.mc.thePlayer.movementInput.moveStrafe != 0.0f) {
            BlockFly.mc.thePlayer.movementInput.moveForward = ((BlockFly.mc.thePlayer.movementInput.moveStrafe > 0.0f) ? -1.0f : 1.0f);
        }
    }
    
    private void ad2() {
        if (BlockFly.mc.thePlayer.movementInput.moveForward != 0.0f) {
            BlockFly.mc.thePlayer.movementInput.moveStrafe = ((BlockFly.mc.thePlayer.movementInput.moveForward > 0.0f) ? -1.0f : 1.0f);
        }
        else if (BlockFly.mc.thePlayer.movementInput.moveStrafe != 0.0f) {
            BlockFly.mc.thePlayer.movementInput.moveForward = ((BlockFly.mc.thePlayer.movementInput.moveStrafe > 0.0f) ? 1.0f : -1.0f);
        }
    }
    
    private float[] getRayCastRots() {
        final float yawSpeed = MathHelper.clamp_float((float)(this.yawSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f)), 0.0f, 180.0f);
        final float pitchSpeed = MathHelper.clamp_float((float)(this.pitchSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f)), 0.0f, 180.0f);
        if (this.isTower()) {
            final Vec3 pos = this.getAimPosBasic();
            final MovingObjectPosition objectPosition = BlockFly.mc.objectMouseOver;
            if (objectPosition != null) {
                if (objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && objectPosition.getBlockPos().equalsBlockPos(this.b) && objectPosition.sideHit == EnumFacing.UP) {
                    return this.rots;
                }
                if (pos != null) {
                    return RotationUtil.positionRotation(pos.xCoord, pos.yCoord, pos.zCoord, this.lastRots, yawSpeed, pitchSpeed, false);
                }
                return this.rots;
            }
            else if (pos != null) {
                return RotationUtil.positionRotation(pos.xCoord, pos.yCoord, pos.zCoord, this.lastRots, yawSpeed, pitchSpeed, false);
            }
        }
        float yaw = this.rots[0];
        float[] rotations = { yaw, this.rots[1] };
        if ((BlockFly.mc.thePlayer.motionX == 0.0 && BlockFly.mc.thePlayer.motionZ == 0.0 && BlockFly.mc.thePlayer.onGround) || !this.startTimeHelper.reached(200L)) {
            final float pitch = this.rotationUtil.rotateToPitch(yawSpeed, this.rots[1], 80.34f);
            yaw = this.rotationUtil.rotateToYaw(pitchSpeed, this.rots[0], Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f);
            RotationUtil.mouseSens(yaw, pitch, this.rots[0], this.rots[1]);
            rotations = new float[] { yaw, pitch };
        }
        else if (BlockFly.mc.thePlayer.motionX == 0.0 && BlockFly.mc.thePlayer.motionZ == 0.0 && BlockFly.mc.thePlayer.onGround) {
            this.startTimeHelper.reset();
        }
        if (this.shouldBuild()) {
            final ArrayList<float[]> rots = new ArrayList<float[]>();
            float difference = 0.1f;
            float currentX = this.rots[0];
            float currentY = this.rots[1];
            for (float dist = 0.0f; dist < 180.0f; dist += difference) {
                final float maxX = this.rots[0] + dist;
                final float minX = this.rots[0] - dist;
                final float maxY = Math.min(this.rots[1] + dist, 90.0f);
                final float minY = Math.max(this.rots[1] - dist, -90.0f);
                while (currentY < maxY) {
                    final float[] f = RotationUtil.mouseSens(currentX, currentY, this.rots[0], this.rots[1]);
                    if (this.canPlace(f)) {
                        rots.add(f);
                    }
                    currentY += difference;
                }
                while (currentX <= maxX) {
                    final float[] f = RotationUtil.mouseSens(currentX, currentY, this.rots[0], this.rots[1]);
                    if (this.canPlace(f)) {
                        rots.add(f);
                    }
                    currentX += difference;
                }
                while (currentY >= minY) {
                    final float[] f = RotationUtil.mouseSens(currentX, currentY, this.rots[0], this.rots[1]);
                    if (this.canPlace(f)) {
                        rots.add(f);
                    }
                    currentY -= difference;
                }
                while (currentX >= minX) {
                    final float[] f = RotationUtil.mouseSens(currentX, currentY, this.rots[0], this.rots[1]);
                    if (this.canPlace(f)) {
                        rots.add(f);
                    }
                    currentX -= difference;
                }
                if (dist > 5.0f && dist <= 10.0f) {
                    difference = 0.3f;
                }
                if (dist > 10.0f) {
                    difference += (float)(dist / 500.0f + 0.01);
                }
            }
            rots.sort(Comparator.comparingDouble((ToDoubleFunction<? super float[]>)this::distanceToLastRots));
            if (!rots.isEmpty()) {
                rotations = rots.get(0);
                this.objectPosition = this.map.get(rotations);
            }
        }
        return rotations;
    }
    
    private boolean canPlace(final float[] yawPitch) {
        final BlockPos b = new BlockPos(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY - 0.5, BlockFly.mc.thePlayer.posZ);
        final MovingObjectPosition m4 = RayTraceUtil.rayCast(1.0f, new float[] { yawPitch[0], yawPitch[1] });
        if (m4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && BlockUtil.isValidBock(m4.getBlockPos()) && m4.getBlockPos().equalsBlockPos(this.b) && m4.sideHit != EnumFacing.DOWN && m4.sideHit != EnumFacing.UP && m4.getBlockPos().getY() <= b.getY()) {
            this.map.put(yawPitch, m4);
            return true;
        }
        return false;
    }
    
    private double distanceToLastRots(final float[] predictRots) {
        final float diff1 = Math.abs(predictRots[0] - this.rots[0]);
        final float diff2 = Math.abs(predictRots[1] - this.rots[1]);
        return diff1 * diff1 + diff1 * diff1 + diff2 * diff2;
    }
    
    private boolean buildForward() {
        final float realYaw = this.moonWalk.getBoolean() ? MathHelper.wrapAngleTo180_float(Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f) : MathHelper.wrapAngleTo180_float(this.rots[0]);
        return (realYaw > 77.5 && realYaw < 102.5) || (realYaw > 167.5 || realYaw < -167.0f) || (realYaw < -77.5 && realYaw > -102.5) || (realYaw > -12.5 && realYaw < 12.5);
    }
    
    private boolean prediction() {
        final Vec3 predictedPosition = this.getPredictedPosition(1);
        final BlockPos blockPos = this.getPredictedBlockPos();
        if (blockPos != null && predictedPosition != null) {
            final double maX = blockPos.getX() + 1.285;
            final double miX = blockPos.getX() - 0.285;
            final double maZ = blockPos.getZ() + 1.285;
            final double miZ = blockPos.getZ() - 0.285;
            return predictedPosition.xCoord > maX || predictedPosition.xCoord < miX || predictedPosition.zCoord > maZ || predictedPosition.zCoord < miZ;
        }
        return false;
    }
    
    private Vec3 getPredictedPosition(final int predictTicks) {
        final Vec3 playerPosition = new Vec3(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY, BlockFly.mc.thePlayer.posZ);
        Vec3 vec3 = null;
        if (!this.lastPositions.isEmpty() && this.lastPositions.size() > 10 && this.lastPositions.size() > this.lastPositions.size() - predictTicks - 1) {
            vec3 = playerPosition.add(playerPosition.subtract(this.lastPositions.get(this.lastPositions.size() - predictTicks - 1)));
        }
        return vec3;
    }
    
    private BlockPos getPredictedBlockPos() {
        final ArrayList<Float> pitchs = new ArrayList<Float>();
        for (float i = Math.max(this.rots[1] - 30.0f, -90.0f); i < Math.min(this.rots[1] + 20.0f, 90.0f); i += 0.05f) {
            final float[] f = RotationUtil.mouseSens(this.rots[0], i, this.lastRots[0], this.lastRots[1]);
            final MovingObjectPosition m4 = BlockFly.mc.thePlayer.customRayTrace(4.5, 2.0f, this.rots[0], f[1]);
            if (m4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && BlockUtil.isValidBock(m4.getBlockPos()) && this.isNearbyBlockPos(m4.getBlockPos()) && m4.sideHit != EnumFacing.DOWN && m4.sideHit != EnumFacing.UP) {
                pitchs.add(f[1]);
            }
        }
        final float[] rotations = new float[2];
        if (!pitchs.isEmpty()) {
            pitchs.sort(Comparator.comparingDouble((ToDoubleFunction<? super Float>)this::distanceToLastPitch));
            if (!pitchs.isEmpty()) {
                rotations[1] = pitchs.get(0);
                rotations[0] = this.rots[0];
            }
            final MovingObjectPosition movingObjectPosition = BlockFly.mc.thePlayer.customRayTrace(4.5, 2.0f, rotations[0], rotations[1]);
            final EnumFacing enumFacing = movingObjectPosition.sideHit;
            final BlockPos blockPos = movingObjectPosition.getBlockPos();
            if (enumFacing == EnumFacing.EAST) {
                return blockPos.add(1, 0, 0);
            }
            if (enumFacing == EnumFacing.WEST) {
                return blockPos.add(-1, 0, 0);
            }
            if (enumFacing == EnumFacing.NORTH) {
                return blockPos.add(0, 0, -1);
            }
            if (enumFacing == EnumFacing.SOUTH) {
                return blockPos.add(0, 0, 1);
            }
        }
        return null;
    }
    
    private float[] getPlayerYawRotation() {
        final boolean moonWalk = this.moonWalk.getBoolean() && this.buildForward();
        float yaw = this.rots[0];
        final float yawSpeed = MathHelper.clamp_float((float)(this.yawSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f)), 0.0f, 180.0f);
        final float pitchSpeed = MathHelper.clamp_float((float)(this.pitchSpeed.getValue() + RandomUtil.nextFloat(0.0f, 15.0f)), 0.0f, 180.0f);
        if (this.isTower()) {
            final MovingObjectPosition objectPosition = BlockFly.mc.objectMouseOver;
            if (objectPosition != null) {
                final float pitch = this.rotationUtil.rotateToPitch(yawSpeed, this.rots, 90.0f);
                yaw = this.rotationUtil.rotateToYaw(pitchSpeed, this.rots, moonWalk ? (Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f + 18.5f) : (Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f));
                return new float[] { yaw, pitch };
            }
        }
        float[] rotations = { yaw, this.rots[1] };
        if ((BlockFly.mc.thePlayer.motionX == 0.0 && BlockFly.mc.thePlayer.motionZ == 0.0 && BlockFly.mc.thePlayer.onGround) || !this.startTimeHelper.reached(200L)) {
            final float pitch = this.rotationUtil.rotateToPitch(pitchSpeed, this.rots, moonWalk ? 80.0f : 80.34f);
            yaw = this.rotationUtil.rotateToYaw(yawSpeed, this.rots, moonWalk ? (Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f + 18.5f) : (Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f));
            rotations = new float[] { yaw, pitch };
        }
        else if (BlockFly.mc.thePlayer.motionX == 0.0 && BlockFly.mc.thePlayer.motionZ == 0.0 && BlockFly.mc.thePlayer.onGround) {
            this.startTimeHelper.reset();
        }
        float realYaw = BlockFly.mc.thePlayer.rotationYaw;
        if (BlockFly.mc.gameSettings.keyBindBack.pressed) {
            realYaw += 180.0f;
            if (BlockFly.mc.gameSettings.keyBindLeft.pressed) {
                realYaw += 45.0f;
            }
            else if (BlockFly.mc.gameSettings.keyBindRight.pressed) {
                realYaw -= 45.0f;
            }
        }
        else if (BlockFly.mc.gameSettings.keyBindForward.pressed) {
            if (BlockFly.mc.gameSettings.keyBindLeft.pressed) {
                realYaw -= 45.0f;
            }
            else if (BlockFly.mc.gameSettings.keyBindRight.pressed) {
                realYaw += 45.0f;
            }
        }
        else if (BlockFly.mc.gameSettings.keyBindRight.pressed) {
            realYaw += 90.0f;
        }
        else if (BlockFly.mc.gameSettings.keyBindLeft.pressed) {
            realYaw -= 90.0f;
        }
        yaw = this.rotationUtil.rotateToYaw(yawSpeed, this.rots[0], moonWalk ? (Augustus.getInstance().getYawPitchHelper().realYaw - 180.0f + 18.5f) : (realYaw - 180.0f));
        rotations[0] = yaw;
        if (this.shouldBuild()) {
            final MovingObjectPosition m1 = RayTraceUtil.rayCast(1.0f, new float[] { rotations[0], rotations[1] });
            if (m1.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && BlockUtil.isValidBock(m1.getBlockPos()) && this.isNearbyBlockPos(m1.getBlockPos()) && m1.sideHit != EnumFacing.DOWN && m1.sideHit != EnumFacing.UP) {
                this.objectPosition = m1;
                return rotations;
            }
            final HashMap<Float, MovingObjectPosition> hashMap = new HashMap<Float, MovingObjectPosition>();
            final ArrayList<Float> pitchs = new ArrayList<Float>();
            for (float i = Math.max(this.rots[1] - 30.0f, -90.0f); i < Math.min(this.rots[1] + 20.0f, 90.0f); i += 0.05f) {
                final float[] f = RotationUtil.mouseSens(yaw, i, this.rots[0], this.rots[1]);
                final MovingObjectPosition m2 = RayTraceUtil.rayCast(1.0f, new float[] { yaw, f[1] });
                if (m2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && BlockUtil.isValidBock(m2.getBlockPos()) && this.isNearbyBlockPos(m2.getBlockPos()) && m2.sideHit != EnumFacing.DOWN && m2.sideHit != EnumFacing.UP) {
                    hashMap.put(f[1], m2);
                    pitchs.add(f[1]);
                }
            }
            if (!pitchs.isEmpty()) {
                pitchs.sort(Comparator.comparingDouble((ToDoubleFunction<? super Float>)this::distanceToLastPitch));
                if (!pitchs.isEmpty()) {
                    rotations[1] = pitchs.get(0);
                    this.objectPosition = hashMap.get(rotations[1]);
                }
            }
            else {
                if (!this.blockSafe.getBoolean()) {
                    return rotations;
                }
                final int add = 1;
                for (int yawLoops = 0; yawLoops < 180; ++yawLoops) {
                    final float yaw2 = yaw + yawLoops * add;
                    final float yaw3 = yaw - yawLoops * add;
                    final float pitch2 = this.rots[1];
                    for (int pitchLoops = 0; pitchLoops < 25; ++pitchLoops) {
                        final float pitch3 = MathHelper.clamp_float(pitch2 + pitchLoops * add, -90.0f, 90.0f);
                        final float pitch4 = MathHelper.clamp_float(pitch2 - pitchLoops * add, -90.0f, 90.0f);
                        final float[] ffff = RotationUtil.mouseSens(yaw3, pitch4, this.rots[0], this.rots[1]);
                        final MovingObjectPosition m3 = RayTraceUtil.rayCast(1.0f, ffff);
                        if (m3.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && BlockUtil.isValidBock(m3.getBlockPos()) && this.isNearbyBlockPos(m3.getBlockPos()) && m3.sideHit != EnumFacing.DOWN && m3.sideHit != EnumFacing.UP) {
                            this.objectPosition = m3;
                            return ffff;
                        }
                        final float[] fff = RotationUtil.mouseSens(yaw3, pitch3, this.rots[0], this.rots[1]);
                        final MovingObjectPosition m4 = RayTraceUtil.rayCast(1.0f, fff);
                        if (m4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && BlockUtil.isValidBock(m4.getBlockPos()) && this.isNearbyBlockPos(m4.getBlockPos()) && m4.sideHit != EnumFacing.DOWN && m4.sideHit != EnumFacing.UP) {
                            this.objectPosition = m4;
                            return fff;
                        }
                        final float[] ff = RotationUtil.mouseSens(yaw2, pitch4, this.rots[0], this.rots[1]);
                        final MovingObjectPosition m5 = RayTraceUtil.rayCast(1.0f, ff);
                        if (m5.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && BlockUtil.isValidBock(m5.getBlockPos()) && this.isNearbyBlockPos(m5.getBlockPos()) && m5.sideHit != EnumFacing.DOWN && m5.sideHit != EnumFacing.UP) {
                            this.objectPosition = m5;
                            return ff;
                        }
                        final float[] f2 = RotationUtil.mouseSens(yaw2, pitch3, this.rots[0], this.rots[1]);
                        final MovingObjectPosition m6 = RayTraceUtil.rayCast(1.0f, f2);
                        if (m6.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && BlockUtil.isValidBock(m6.getBlockPos()) && this.isNearbyBlockPos(m6.getBlockPos()) && m6.sideHit != EnumFacing.DOWN && m6.sideHit != EnumFacing.UP) {
                            this.objectPosition = m6;
                            return f2;
                        }
                    }
                }
            }
        }
        return rotations;
    }
    
    private double distanceToLastPitch(final float pitch) {
        return Math.abs(pitch - this.rots[1]);
    }
    
    private double distanceToLastPitch(final float[] pitch) {
        return Math.abs(pitch[0] - this.rots[1]);
    }
    
    private boolean isNearbyBlockPos(final BlockPos blockPos) {
        if (BlockFly.mc.thePlayer.onGround) {
            for (int x = this.b.getX() - 1; x <= this.b.getX() + 1; ++x) {
                for (int z = this.b.getZ() - 1; z <= this.b.getZ() + 1; ++z) {
                    if (blockPos.equalsBlockPos(new BlockPos(x, this.b.getY(), z))) {
                        return true;
                    }
                }
            }
            return false;
        }
        return blockPos.equalsBlockPos(this.b);
    }
    
    private Vec3 getAimPosBasic() {
        if (this.b == null) {
            return null;
        }
        final EnumFacing enumFacing = this.getPlaceSide(this.b);
        final Block block = BlockFly.mc.theWorld.getBlockState(this.b).getBlock();
        final double add = 0.009999999776482582;
        Vec3 min = null;
        Vec3 max = null;
        if (enumFacing != null) {
            if (enumFacing == EnumFacing.UP) {
                min = new Vec3(this.b.getX() + add, this.b.getY() + block.getBlockBoundsMaxY(), this.b.getZ() + add);
                max = new Vec3(this.b.getX() + block.getBlockBoundsMaxX() - add, this.b.getY() + block.getBlockBoundsMaxY(), this.b.getZ() + block.getBlockBoundsMaxZ() - add);
            }
            else if (enumFacing == EnumFacing.WEST) {
                min = new Vec3(this.b.getX(), this.b.getY() + add, this.b.getZ() + add);
                max = new Vec3(this.b.getX(), this.b.getY() + block.getBlockBoundsMaxY() - add, this.b.getZ() + block.getBlockBoundsMaxZ() - add);
            }
            else if (enumFacing == EnumFacing.EAST) {
                min = new Vec3(this.b.getX() + block.getBlockBoundsMaxX(), this.b.getY() + add, this.b.getZ() + add);
                max = new Vec3(this.b.getX() + block.getBlockBoundsMaxX(), this.b.getY() + block.getBlockBoundsMaxY() - add, this.b.getZ() + block.getBlockBoundsMaxZ() - add);
            }
            else if (enumFacing == EnumFacing.SOUTH) {
                min = new Vec3(this.b.getX() + add, this.b.getY() + add, this.b.getZ() + block.getBlockBoundsMaxZ());
                max = new Vec3(this.b.getX() + block.getBlockBoundsMaxX() - add, this.b.getY() + block.getBlockBoundsMaxY() - add, this.b.getZ() + block.getBlockBoundsMaxZ());
            }
            else if (enumFacing == EnumFacing.NORTH) {
                min = new Vec3(this.b.getX() + add, this.b.getY() + add, this.b.getZ());
                max = new Vec3(this.b.getX() + block.getBlockBoundsMaxX() - add, this.b.getY() + block.getBlockBoundsMaxY() - add, this.b.getZ());
            }
            else if (enumFacing == EnumFacing.DOWN) {
                min = new Vec3(this.b.getX() + add, this.b.getY(), this.b.getZ() + add);
                max = new Vec3(this.b.getX() + block.getBlockBoundsMaxX() - add, this.b.getY(), this.b.getZ() + block.getBlockBoundsMaxZ() - add);
            }
        }
        if (min != null) {
            return this.getBestHit(min, max);
        }
        return null;
    }
    
    private EnumFacing getPlaceSide(final BlockPos blockPos) {
        final ArrayList<Vec3> positions = new ArrayList<Vec3>();
        final HashMap<Vec3, EnumFacing> hashMap = new HashMap<Vec3, EnumFacing>();
        final BlockPos playerPos = new BlockPos(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY, BlockFly.mc.thePlayer.posZ);
        if (BlockUtil.isAirBlock(blockPos.add(0, 1, 0)) && !blockPos.add(0, 1, 0).equalsBlockPos(playerPos) && !BlockFly.mc.thePlayer.onGround) {
            final BlockPos bp = blockPos.add(0, 1, 0);
            final Vec3 vec4 = this.getBestHitFeet(bp);
            positions.add(vec4);
            hashMap.put(vec4, EnumFacing.UP);
        }
        if (BlockUtil.isAirBlock(blockPos.add(1, 0, 0)) && !blockPos.add(1, 0, 0).equalsBlockPos(playerPos)) {
            final BlockPos bp = blockPos.add(1, 0, 0);
            final Vec3 vec4 = this.getBestHitFeet(bp);
            positions.add(vec4);
            hashMap.put(vec4, EnumFacing.EAST);
        }
        if (BlockUtil.isAirBlock(blockPos.add(-1, 0, 0)) && !blockPos.add(-1, 0, 0).equalsBlockPos(playerPos)) {
            final BlockPos bp = blockPos.add(-1, 0, 0);
            final Vec3 vec4 = this.getBestHitFeet(bp);
            positions.add(vec4);
            hashMap.put(vec4, EnumFacing.WEST);
        }
        if (BlockUtil.isAirBlock(blockPos.add(0, 0, 1)) && !blockPos.add(0, 0, 1).equalsBlockPos(playerPos)) {
            final BlockPos bp = blockPos.add(0, 0, 1);
            final Vec3 vec4 = this.getBestHitFeet(bp);
            positions.add(vec4);
            hashMap.put(vec4, EnumFacing.SOUTH);
        }
        if (BlockUtil.isAirBlock(blockPos.add(0, 0, -1)) && !blockPos.add(0, 0, -1).equalsBlockPos(playerPos)) {
            final BlockPos bp = blockPos.add(0, 0, -1);
            final Vec3 vec4 = this.getBestHitFeet(bp);
            positions.add(vec4);
            hashMap.put(vec4, EnumFacing.NORTH);
        }
        positions.sort(Comparator.comparingDouble(vec3 -> BlockFly.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord)));
        if (!positions.isEmpty()) {
            final Vec3 vec5 = this.getBestHitFeet(this.b);
            if (BlockFly.mc.thePlayer.getDistance(vec5.xCoord, vec5.yCoord, vec5.zCoord) >= BlockFly.mc.thePlayer.getDistance(positions.get(0).xCoord, positions.get(0).yCoord, positions.get(0).zCoord)) {
                return hashMap.get(positions.get(0));
            }
        }
        return null;
    }
    
    private Vec3 getBestHitFeet(final BlockPos blockPos) {
        final Block block = BlockFly.mc.theWorld.getBlockState(blockPos).getBlock();
        final double ex = MathHelper.clamp_double(BlockFly.mc.thePlayer.posX, blockPos.getX(), blockPos.getX() + block.getBlockBoundsMaxX());
        final double ey = MathHelper.clamp_double(BlockFly.mc.thePlayer.posY, blockPos.getY(), blockPos.getY() + block.getBlockBoundsMaxY());
        final double ez = MathHelper.clamp_double(BlockFly.mc.thePlayer.posZ, blockPos.getZ(), blockPos.getZ() + block.getBlockBoundsMaxZ());
        return new Vec3(ex, ey, ez);
    }
    
    private Vec3 getBestHit(final Vec3 min, final Vec3 max) {
        final Vec3 positionEyes = BlockFly.mc.thePlayer.getPositionEyes(1.0f);
        final double x = MathHelper.clamp_double(BlockFly.mc.thePlayer.posX, min.xCoord, max.xCoord);
        final double y = MathHelper.clamp_double(BlockFly.mc.thePlayer.posY, min.yCoord, max.yCoord);
        final double z = MathHelper.clamp_double(BlockFly.mc.thePlayer.posZ, min.zCoord, max.zCoord);
        return new Vec3(x, y, z);
    }
    
    private ItemStack getItemStack() {
        ItemStack itemStack = BlockFly.mc.thePlayer.getCurrentEquippedItem();
        if (!this.silentMode.getSelected().equals("None")) {
            for (int i = 36; i < BlockFly.mc.thePlayer.inventoryContainer.inventorySlots.size(); ++i) {
                final ItemStack stack = BlockFly.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack != null && stack.getItem() instanceof ItemBlock && stack.stackSize > 0 && Augustus.getInstance().getBlockUtil().isValidStack(stack)) {
                    this.slotID = i - 36;
                    break;
                }
            }
            itemStack = BlockFly.mc.thePlayer.inventoryContainer.getSlot(this.slotID + 36).getStack();
        }
        else {
            this.slotID = BlockFly.mc.thePlayer.inventory.currentItem;
        }
        return itemStack;
    }
    
    private BlockPos getBlockPos() {
        final BlockPos playerPos = new BlockPos(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY - 1.0, BlockFly.mc.thePlayer.posZ);
        final ArrayList<Vec3> positions = new ArrayList<Vec3>();
        final HashMap<Vec3, BlockPos> hashMap = new HashMap<Vec3, BlockPos>();
        for (int x = playerPos.getX() - 5; x <= playerPos.getX() + 5; ++x) {
            for (int y = playerPos.getY() - 1; y <= playerPos.getY(); ++y) {
                for (int z = playerPos.getZ() - 5; z <= playerPos.getZ() + 5; ++z) {
                    if (BlockUtil.isValidBock(new BlockPos(x, y, z))) {
                        final BlockPos blockPos = new BlockPos(x, y, z);
                        final Block block = BlockFly.mc.theWorld.getBlockState(blockPos).getBlock();
                        final double ex = MathHelper.clamp_double(BlockFly.mc.thePlayer.posX, blockPos.getX(), blockPos.getX() + block.getBlockBoundsMaxX());
                        final double ey = MathHelper.clamp_double(BlockFly.mc.thePlayer.posY, blockPos.getY(), blockPos.getY() + block.getBlockBoundsMaxY());
                        final double ez = MathHelper.clamp_double(BlockFly.mc.thePlayer.posZ, blockPos.getZ(), blockPos.getZ() + block.getBlockBoundsMaxZ());
                        final Vec3 vec3 = new Vec3(ex, ey, ez);
                        positions.add(vec3);
                        hashMap.put(vec3, blockPos);
                    }
                }
            }
        }
        if (positions.isEmpty()) {
            return null;
        }
        positions.sort(Comparator.comparingDouble((ToDoubleFunction<? super Vec3>)this::getBestBlock));
        if (this.isTower() && hashMap.get(positions.get(0)).getY() != BlockFly.mc.thePlayer.posY - 1.5) {
            return new BlockPos(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY - 1.5, BlockFly.mc.thePlayer.posZ);
        }
        return hashMap.get(positions.get(0));
    }
    
    private boolean shouldScaffold() {
        return BlockFly.mc.currentScreen == null;
    }
    
    private boolean shouldBuild() {
        if (this.latestRotate.getBoolean() && this.rayCast.getBoolean()) {
            final double add1 = 1.282;
            final double add2 = 0.282;
            double x = BlockFly.mc.thePlayer.posX;
            double z = BlockFly.mc.thePlayer.posZ;
            x += (BlockFly.mc.thePlayer.posX - this.xyz[0]) * this.backupTicks.getValue();
            z += (BlockFly.mc.thePlayer.posZ - this.xyz[2]) * this.backupTicks.getValue();
            this.xyz = new double[] { BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY, BlockFly.mc.thePlayer.posZ };
            final double maX = this.b.getX() + add1;
            final double miX = this.b.getX() - add2;
            final double maZ = this.b.getZ() + add1;
            final double miZ = this.b.getZ() - add2;
            return x > maX || x < miX || z > maZ || z < miZ || (this.predict.getBoolean() && this.rayCast.getBoolean() && this.prediction());
        }
        final BlockPos playerPos = new BlockPos(BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY - 0.5, BlockFly.mc.thePlayer.posZ);
        return BlockFly.mc.theWorld.isAirBlock(playerPos);
    }
    
    private boolean shouldSneak() {
        if (this.latestRotate.getBoolean() && this.rayCast.getBoolean()) {
            final double add1 = 1.15;
            final double add2 = 0.15;
            double x = BlockFly.mc.thePlayer.posX;
            double z = BlockFly.mc.thePlayer.posZ;
            x += (BlockFly.mc.thePlayer.posX - this.xyz[0]) * this.backupTicks.getValue();
            z += (BlockFly.mc.thePlayer.posZ - this.xyz[2]) * this.backupTicks.getValue();
            this.xyz = new double[] { BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY, BlockFly.mc.thePlayer.posZ };
            final double maX = this.b.getX() + add1;
            final double miX = this.b.getX() - add2;
            final double maZ = this.b.getZ() + add1;
            final double miZ = this.b.getZ() - add2;
            return x > maX || x < miX || z > maZ || z < miZ || (this.predict.getBoolean() && this.rayCast.getBoolean() && this.prediction());
        }
        final Block playerBlock = BlockFly.mc.theWorld.getBlockState(new BlockPos(BlockFly.mc.thePlayer.posX + BlockFly.mc.thePlayer.posX - this.xyz[0], BlockFly.mc.thePlayer.posY - 0.5, BlockFly.mc.thePlayer.posZ + BlockFly.mc.thePlayer.posZ - this.xyz[2])).getBlock();
        this.xyz = new double[] { BlockFly.mc.thePlayer.posX, BlockFly.mc.thePlayer.posY, BlockFly.mc.thePlayer.posZ };
        return playerBlock instanceof BlockAir;
    }
    
    private double getBestBlock(final Vec3 vec3) {
        return BlockFly.mc.thePlayer.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }
    
    private double[] getAdvancedDiagonalExpandXZ(final BlockPos blockPos) {
        final double[] xz = new double[2];
        final Vec2d difference = new Vec2d(blockPos.getX() - BlockFly.mc.thePlayer.posX, blockPos.getZ() - BlockFly.mc.thePlayer.posZ);
        if (difference.x > -1.0 && difference.x < 0.0 && difference.y < -1.0) {
            xz[0] = difference.x * -1.0;
            xz[1] = 1.0;
        }
        if (difference.y < 0.0 && difference.y > -1.0 && difference.x < -1.0) {
            xz[0] = 1.0;
            xz[1] = difference.y * -1.0;
        }
        if (difference.x > -1.0 && difference.x < 0.0 && difference.y > 0.0) {
            xz[0] = difference.x * -1.0;
            xz[1] = 0.0;
        }
        if (difference.y < 0.0 && difference.y > -1.0 && difference.x > 0.0) {
            xz[0] = 0.0;
            xz[1] = difference.y * -1.0;
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
    
    public int getSlotID() {
        return this.slotID;
    }
    
    private void setRotation() {
        if (BlockFly.mc.currentScreen != null) {
            return;
        }
        BlockFly.mc.thePlayer.rotationYaw = this.rots[0];
        BlockFly.mc.thePlayer.rotationPitch = this.rots[1];
        BlockFly.mc.thePlayer.prevRotationYaw = this.lastRots[0];
        BlockFly.mc.thePlayer.prevRotationPitch = this.lastRots[1];
    }
    
    private boolean isTower() {
        return BlockFly.mc.gameSettings.keyBindJump.isKeyDown() && BlockFly.mc.thePlayer.motionX == 0.0 && BlockFly.mc.thePlayer.motionZ == 0.0;
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
            final float red = 1.0f;
            final float green = 1.0f;
            final float blue = 1.0f;
            float lineWidth = 0.0f;
            if (this.b != null) {
                if (BlockFly.mc.thePlayer.getDistance(this.b.getX(), this.b.getY(), this.b.getZ()) > 1.0) {
                    double d0 = 1.0 - BlockFly.mc.thePlayer.getDistance(this.b.getX(), this.b.getY(), this.b.getZ()) / 20.0;
                    if (d0 < 0.3) {
                        d0 = 0.3;
                    }
                    lineWidth *= (float)d0;
                }
                RenderUtil.drawBlockESP(this.b, red, green, blue, 0.3137255f, 1.0f, lineWidth);
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
}
