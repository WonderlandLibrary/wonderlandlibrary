/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.math.MathKt
 *  kotlin.text.StringsKt
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.minecraft.block.material.IMaterial;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PlaceRotation;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.block.PlaceInfo;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Tower", description="Automatically builds a tower beneath you.", category=ModuleCategory.WORLD, keyBind=24)
public final class Tower
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Jump", "Motion", "ConstantMotion", "MotionTP", "Packet", "Teleport", "AAC3.3.9", "AAC3.6.4"}, "Motion");
    private final BoolValue autoBlockValue = new BoolValue("AutoBlock", true);
    private final BoolValue stayAutoBlock = new BoolValue("StayAutoBlock", false);
    private final BoolValue swingValue = new BoolValue("Swing", true);
    private final BoolValue stopWhenBlockAbove = new BoolValue("StopWhenBlockAbove", false);
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue keepRotationValue = new BoolValue("KeepRotation", false);
    private final BoolValue onJumpValue = new BoolValue("OnJump", false);
    private final ListValue placeModeValue = new ListValue("PlaceTiming", new String[]{"Pre", "Post"}, "Post");
    private final FloatValue timerValue = new FloatValue("Timer", 1.0f, 0.0f, 10.0f);
    private final FloatValue jumpMotionValue = new FloatValue("JumpMotion", 0.42f, 0.3681289f, 0.79f);
    private final IntegerValue jumpDelayValue = new IntegerValue("JumpDelay", 0, 0, 20);
    private final FloatValue constantMotionValue = new FloatValue("ConstantMotion", 0.42f, 0.1f, 1.0f);
    private final FloatValue constantMotionJumpGroundValue = new FloatValue("ConstantMotionJumpGround", 0.79f, 0.76f, 1.0f);
    private final FloatValue teleportHeightValue = new FloatValue("TeleportHeight", 1.15f, 0.1f, 5.0f);
    private final IntegerValue teleportDelayValue = new IntegerValue("TeleportDelay", 0, 0, 20);
    private final BoolValue teleportGroundValue = new BoolValue("TeleportGround", true);
    private final BoolValue teleportNoMotionValue = new BoolValue("TeleportNoMotion", false);
    private final BoolValue counterDisplayValue = new BoolValue("Counter", true);
    private PlaceInfo placeInfo;
    private Rotation lockRotation;
    private final TickTimer timer = new TickTimer();
    private double jumpGround;
    private int slot;

    @Override
    public void onDisable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
        this.lockRotation = null;
        if (this.slot != thePlayer.getInventory().getCurrentItem()) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(thePlayer.getInventory().getCurrentItem()));
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onMotion(MotionEvent event) {
        boolean bl;
        if (((Boolean)this.onJumpValue.get()).booleanValue() && !MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown()) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) return;
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (((Boolean)this.rotationsValue.get()).booleanValue() && ((Boolean)this.keepRotationValue.get()).booleanValue() && this.lockRotation != null) {
            RotationUtils.setTargetRotation(this.lockRotation);
        }
        MinecraftInstance.mc.getTimer().setTimerSpeed(((Number)this.timerValue.get()).floatValue());
        EventState eventState = event.getEventState();
        if (StringsKt.equals((String)((String)this.placeModeValue.get()), (String)eventState.getStateName(), (boolean)true)) {
            this.place();
        }
        if (eventState != EventState.PRE) return;
        this.placeInfo = null;
        this.timer.update();
        if (((Boolean)this.autoBlockValue.get()).booleanValue()) {
            if (InventoryUtils.findAutoBlockBlock() == -1) {
                if (thePlayer.getHeldItem() == null) return;
                IItemStack iItemStack = thePlayer.getHeldItem();
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                if (!MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem())) return;
            }
            bl = true;
        } else {
            if (thePlayer.getHeldItem() == null) return;
            IItemStack iItemStack = thePlayer.getHeldItem();
            if (iItemStack == null) {
                Intrinsics.throwNpe();
            }
            if (!MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem())) return;
            bl = true;
        }
        boolean update = bl;
        if (!update) return;
        if (!((Boolean)this.stopWhenBlockAbove.get()).booleanValue() || MinecraftInstance.classProvider.isBlockAir(BlockUtils.getBlock(new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() + (double)2, thePlayer.getPosZ())))) {
            this.move();
        }
        WBlockPos blockPos = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ());
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        if (!MinecraftInstance.classProvider.isBlockAir(iWorldClient.getBlockState(blockPos).getBlock())) return;
        if (!this.search(blockPos)) return;
        if ((Boolean)this.rotationsValue.get() == false) return;
        VecRotation vecRotation = RotationUtils.faceBlock(blockPos);
        if (vecRotation == null) return;
        RotationUtils.setTargetRotation(vecRotation.getRotation());
        PlaceInfo placeInfo = this.placeInfo;
        if (placeInfo == null) {
            Intrinsics.throwNpe();
        }
        placeInfo.setVec3(vecRotation.getVec());
    }

    private final void fakeJump() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP.setAirBorne(true);
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        iEntityPlayerSP2.triggerAchievement(MinecraftInstance.classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final void move() {
        IEntityPlayerSP thePlayer;
        block23: {
            block18: {
                block21: {
                    block20: {
                        block22: {
                            block19: {
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    return;
                                }
                                thePlayer = iEntityPlayerSP;
                                String string = (String)this.modeValue.get();
                                boolean bl = false;
                                String string2 = string;
                                if (string2 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                                }
                                string = string2.toLowerCase();
                                switch (string.hashCode()) {
                                    case 325228192: {
                                        if (!string.equals("aac3.3.9")) return;
                                        break block18;
                                    }
                                    case -157173582: {
                                        if (!string.equals("motiontp")) return;
                                        break block19;
                                    }
                                    case -1068318794: {
                                        if (!string.equals("motion")) return;
                                        break;
                                    }
                                    case -1360201941: {
                                        if (!string.equals("teleport")) return;
                                        break block20;
                                    }
                                    case 792877146: {
                                        if (!string.equals("constantmotion")) return;
                                        break block21;
                                    }
                                    case -995865464: {
                                        if (!string.equals("packet")) return;
                                        break block22;
                                    }
                                    case 325231070: {
                                        if (!string.equals("aac3.6.4")) return;
                                        break block23;
                                    }
                                    case 3273774: {
                                        if (!string.equals("jump") || !thePlayer.getOnGround() || !this.timer.hasTimePassed(((Number)this.jumpDelayValue.get()).intValue())) return;
                                        this.fakeJump();
                                        thePlayer.setMotionY(((Number)this.jumpMotionValue.get()).floatValue());
                                        this.timer.reset();
                                        return;
                                    }
                                }
                                if (thePlayer.getOnGround()) {
                                    this.fakeJump();
                                    thePlayer.setMotionY(0.42);
                                    return;
                                }
                                if (!(thePlayer.getMotionY() < 0.1)) return;
                                thePlayer.setMotionY(-0.3);
                                return;
                            }
                            if (thePlayer.getOnGround()) {
                                this.fakeJump();
                                thePlayer.setMotionY(0.42);
                                return;
                            }
                            if (!(thePlayer.getMotionY() < 0.23)) return;
                            thePlayer.setPosition(thePlayer.getPosX(), MathKt.truncate((double)thePlayer.getPosY()), thePlayer.getPosZ());
                            return;
                        }
                        if (!thePlayer.getOnGround() || !this.timer.hasTimePassed(2)) return;
                        this.fakeJump();
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.42, thePlayer.getPosZ(), false));
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.753, thePlayer.getPosZ(), false));
                        thePlayer.setPosition(thePlayer.getPosX(), thePlayer.getPosY() + 1.0, thePlayer.getPosZ());
                        this.timer.reset();
                        return;
                    }
                    if (((Boolean)this.teleportNoMotionValue.get()).booleanValue()) {
                        thePlayer.setMotionY(0.0);
                    }
                    if (!thePlayer.getOnGround() && ((Boolean)this.teleportGroundValue.get()).booleanValue() || !this.timer.hasTimePassed(((Number)this.teleportDelayValue.get()).intValue())) return;
                    this.fakeJump();
                    thePlayer.setPositionAndUpdate(thePlayer.getPosX(), thePlayer.getPosY() + ((Number)this.teleportHeightValue.get()).doubleValue(), thePlayer.getPosZ());
                    this.timer.reset();
                    return;
                }
                if (thePlayer.getOnGround()) {
                    this.fakeJump();
                    this.jumpGround = thePlayer.getPosY();
                    thePlayer.setMotionY(((Number)this.constantMotionValue.get()).floatValue());
                }
                if (!(thePlayer.getPosY() > this.jumpGround + ((Number)this.constantMotionJumpGroundValue.get()).doubleValue())) return;
                this.fakeJump();
                thePlayer.setPosition(thePlayer.getPosX(), MathKt.truncate((double)thePlayer.getPosY()), thePlayer.getPosZ());
                thePlayer.setMotionY(((Number)this.constantMotionValue.get()).floatValue());
                this.jumpGround = thePlayer.getPosY();
                return;
            }
            if (thePlayer.getOnGround()) {
                this.fakeJump();
                thePlayer.setMotionY(0.4001);
            }
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.0f);
            if (!(thePlayer.getMotionY() < 0.0)) return;
            IEntityPlayerSP iEntityPlayerSP = thePlayer;
            iEntityPlayerSP.setMotionY(iEntityPlayerSP.getMotionY() - 9.45E-6);
            MinecraftInstance.mc.getTimer().setTimerSpeed(1.6f);
            return;
        }
        if (thePlayer.getTicksExisted() % 4 == 1) {
            thePlayer.setMotionY(0.4195464);
            thePlayer.setPosition(thePlayer.getPosX() - 0.035, thePlayer.getPosY(), thePlayer.getPosZ());
            return;
        }
        if (thePlayer.getTicksExisted() % 4 != 0) return;
        thePlayer.setMotionY(-0.5);
        thePlayer.setPosition(thePlayer.getPosX() + 0.035, thePlayer.getPosY(), thePlayer.getPosZ());
    }

    private final void place() {
        if (this.placeInfo == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        int blockSlot = -1;
        IItemStack itemStack = thePlayer.getHeldItem();
        if (itemStack == null || !MinecraftInstance.classProvider.isItemBlock(itemStack.getItem())) {
            if (!((Boolean)this.autoBlockValue.get()).booleanValue()) {
                return;
            }
            blockSlot = InventoryUtils.findAutoBlockBlock();
            if (blockSlot == -1) {
                return;
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(blockSlot - 36));
            itemStack = thePlayer.getInventoryContainer().getSlot(blockSlot).getStack();
        }
        IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IItemStack iItemStack = itemStack;
        if (iItemStack == null) {
            Intrinsics.throwNpe();
        }
        PlaceInfo placeInfo = this.placeInfo;
        if (placeInfo == null) {
            Intrinsics.throwNpe();
        }
        WBlockPos wBlockPos = placeInfo.getBlockPos();
        PlaceInfo placeInfo2 = this.placeInfo;
        if (placeInfo2 == null) {
            Intrinsics.throwNpe();
        }
        IEnumFacing iEnumFacing = placeInfo2.getEnumFacing();
        PlaceInfo placeInfo3 = this.placeInfo;
        if (placeInfo3 == null) {
            Intrinsics.throwNpe();
        }
        if (iPlayerControllerMP.onPlayerRightClick(thePlayer, iWorldClient, iItemStack, wBlockPos, iEnumFacing, placeInfo3.getVec3())) {
            if (((Boolean)this.swingValue.get()).booleanValue()) {
                thePlayer.swingItem();
            } else {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
            }
        }
        this.placeInfo = null;
        if (!((Boolean)this.stayAutoBlock.get()).booleanValue() && blockSlot >= 0) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(thePlayer.getInventory().getCurrentItem()));
        }
    }

    /*
     * WARNING - void declaration
     */
    private final boolean search(WBlockPos blockPosition) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return false;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        boolean $i$f$isReplaceable = false;
        IMaterial iMaterial = BlockUtils.getMaterial(blockPosition);
        if (!(iMaterial != null ? iMaterial.isReplaceable() : false)) {
            return false;
        }
        WVec3 eyesPos = new WVec3(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY() + (double)thePlayer.getEyeHeight(), thePlayer.getPosZ());
        PlaceRotation placeRotation = null;
        for (EnumFacingType facingType : EnumFacingType.values()) {
            IEnumFacing side = MinecraftInstance.classProvider.getEnumFacing(facingType);
            WBlockPos neighbor = WBlockPos.offset$default(blockPosition, side, 0, 2, null);
            if (!BlockUtils.canBeClicked(neighbor)) continue;
            WVec3 dirVec = new WVec3(side.getDirectionVec());
            for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
                for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                    double zSearch = 0.1;
                    while (zSearch < 0.9) {
                        IMovingObjectPosition obj;
                        void y$iv;
                        void x$iv;
                        void this_$iv;
                        double d;
                        WVec3 hitVec;
                        boolean $i$f$addVector;
                        double z$iv$iv;
                        block17: {
                            block16: {
                                double d2$iv;
                                double d1$iv;
                                WVec3 wVec3;
                                WVec3 this_$iv2;
                                double d2$iv2;
                                double d1$iv2;
                                void y$iv$iv;
                                void x$iv$iv;
                                void this_$iv$iv;
                                WVec3 this_$iv3;
                                WVec3 this_$iv4 = new WVec3(blockPosition);
                                boolean $i$f$addVector2 = false;
                                WVec3 posVec = new WVec3(this_$iv4.getXCoord() + xSearch, this_$iv4.getYCoord() + ySearch, this_$iv4.getZCoord() + zSearch);
                                WVec3 this_$iv5 = eyesPos;
                                boolean $i$f$squareDistanceTo22 = false;
                                double d0$iv = posVec.getXCoord() - this_$iv5.getXCoord();
                                double d1$iv222 = posVec.getYCoord() - this_$iv5.getYCoord();
                                double d2$iv3 = posVec.getZCoord() - this_$iv5.getZCoord();
                                double distanceSqPosVec = d0$iv * d0$iv + d1$iv222 * d1$iv222 + d2$iv3 * d2$iv3;
                                WVec3 $i$f$squareDistanceTo22 = posVec;
                                WVec3 vec$iv = new WVec3(dirVec.getXCoord() * 0.5, dirVec.getYCoord() * 0.5, dirVec.getZCoord() * 0.5);
                                boolean $i$f$add = false;
                                void d1$iv222 = this_$iv3;
                                double d2 = vec$iv.getXCoord();
                                double d3 = vec$iv.getYCoord();
                                z$iv$iv = vec$iv.getZCoord();
                                $i$f$addVector = false;
                                hitVec = new WVec3(this_$iv$iv.getXCoord() + x$iv$iv, this_$iv$iv.getYCoord() + y$iv$iv, this_$iv$iv.getZCoord() + z$iv$iv);
                                this_$iv3 = eyesPos;
                                boolean $i$f$squareDistanceTo32 = false;
                                double d0$iv2 = hitVec.getXCoord() - this_$iv3.getXCoord();
                                if (d0$iv2 * d0$iv2 + (d1$iv2 = hitVec.getYCoord() - this_$iv3.getYCoord()) * d1$iv2 + (d2$iv2 = hitVec.getZCoord() - this_$iv3.getZCoord()) * d2$iv2 > 18.0) break block16;
                                this_$iv3 = eyesPos;
                                WVec3 $i$f$squareDistanceTo32 = posVec;
                                double d4 = distanceSqPosVec;
                                $i$f$add = false;
                                this_$iv$iv = this_$iv2;
                                d1$iv2 = dirVec.getXCoord();
                                d2$iv2 = dirVec.getYCoord();
                                z$iv$iv = dirVec.getZCoord();
                                $i$f$addVector = false;
                                this_$iv2 = wVec3 = new WVec3(this_$iv$iv.getXCoord() + x$iv$iv, this_$iv$iv.getYCoord() + y$iv$iv, this_$iv$iv.getZCoord() + z$iv$iv);
                                boolean $i$f$squareDistanceTo = false;
                                double d0$iv3 = vec$iv.getXCoord() - this_$iv3.getXCoord();
                                d = d0$iv3 * d0$iv3 + (d1$iv = vec$iv.getYCoord() - this_$iv3.getYCoord()) * d1$iv + (d2$iv = vec$iv.getZCoord() - this_$iv3.getZCoord()) * d2$iv;
                                if (d4 > d) break block16;
                                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                                if (iWorldClient == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (iWorldClient.rayTraceBlocks(eyesPos, hitVec, false, true, false) == null) break block17;
                            }
                            zSearch += 0.1;
                            continue;
                        }
                        double diffX = hitVec.getXCoord() - eyesPos.getXCoord();
                        double diffY = hitVec.getYCoord() - eyesPos.getYCoord();
                        double diffZ = hitVec.getZCoord() - eyesPos.getZCoord();
                        z$iv$iv = diffX * diffX + diffZ * diffZ;
                        $i$f$addVector = false;
                        double diffXZ = Math.sqrt(z$iv$iv);
                        boolean bl = false;
                        d = Math.atan2(diffZ, diffX);
                        float f = WMathHelper.wrapAngleTo180_float((float)Math.toDegrees(d) - 90.0f);
                        bl = false;
                        double d5 = Math.atan2(diffY, diffXZ);
                        float f2 = WMathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(d5)));
                        float f3 = f;
                        Rotation rotation = new Rotation(f3, f2);
                        WVec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                        WVec3 wVec3 = eyesPos;
                        double d6 = rotationVector.getXCoord() * (double)4;
                        double d7 = rotationVector.getYCoord() * (double)4;
                        double z$iv = rotationVector.getZCoord() * (double)4;
                        boolean $i$f$addVector3 = false;
                        WVec3 vector = new WVec3(this_$iv.getXCoord() + x$iv, this_$iv.getYCoord() + y$iv, this_$iv.getZCoord() + z$iv);
                        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient == null) {
                            Intrinsics.throwNpe();
                        }
                        IMovingObjectPosition iMovingObjectPosition = obj = iWorldClient.rayTraceBlocks(eyesPos, vector, false, false, true);
                        if (iMovingObjectPosition == null) {
                            Intrinsics.throwNpe();
                        }
                        if (iMovingObjectPosition.getTypeOfHit() != IMovingObjectPosition.WMovingObjectType.BLOCK || !((Object)obj.getBlockPos()).equals(neighbor)) {
                            zSearch += 0.1;
                            continue;
                        }
                        if (placeRotation == null || RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation())) {
                            placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                        }
                        zSearch += 0.1;
                    }
                }
            }
        }
        if (placeRotation == null) {
            return false;
        }
        if (((Boolean)this.rotationsValue.get()).booleanValue()) {
            RotationUtils.setTargetRotation(placeRotation.getRotation(), 0);
            this.lockRotation = placeRotation.getRotation();
        }
        this.placeInfo = placeRotation.getPlaceInfo();
        return true;
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHeldItemChange(packet)) {
            this.slot = packet.asCPacketHeldItemChange().getSlotId();
        }
    }

    @EventTarget
    public final void onRender2D(Render2DEvent event) {
        if (((Boolean)this.counterDisplayValue.get()).booleanValue()) {
            GL11.glPushMatrix();
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(BlockOverlay.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.render.BlockOverlay");
            }
            BlockOverlay blockOverlay = (BlockOverlay)module;
            if (blockOverlay.getState() && ((Boolean)blockOverlay.getInfoValue().get()).booleanValue() && blockOverlay.getCurrentBlock() != null) {
                GL11.glTranslatef((float)0.0f, (float)15.0f, (float)0.0f);
            }
            String info = "Blocks: \u00a77" + this.getBlocksAmount();
            IScaledResolution scaledResolution = MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc);
            RenderUtils.drawBorderedRect((float)(scaledResolution.getScaledWidth() / 2) - (float)2, (float)(scaledResolution.getScaledHeight() / 2) + (float)5, (float)(scaledResolution.getScaledWidth() / 2 + Fonts.font40.getStringWidth(info)) + (float)2, (float)(scaledResolution.getScaledHeight() / 2) + (float)16, 3.0f, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            MinecraftInstance.classProvider.getGlStateManager().resetColor();
            Fonts.font40.drawString(info, (float)scaledResolution.getScaledWidth() / (float)2, (float)(scaledResolution.getScaledHeight() / 2) + (float)7, Color.WHITE.getRGB());
            GL11.glPopMatrix();
        }
    }

    @EventTarget
    public final void onJump(JumpEvent event) {
        if (((Boolean)this.onJumpValue.get()).booleanValue()) {
            event.cancelEvent();
        }
    }

    /*
     * WARNING - void declaration
     */
    private final int getBlocksAmount() {
        int amount = 0;
        int n = 36;
        int n2 = 44;
        while (n <= n2) {
            void i;
            IItemStack itemStack;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if ((itemStack = iEntityPlayerSP.getInventoryContainer().getSlot((int)i).getStack()) != null && MinecraftInstance.classProvider.isItemBlock(itemStack.getItem())) {
                IItem iItem = itemStack.getItem();
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                IBlock block = iItem.asItemBlock().getBlock();
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                if (iEntityPlayerSP2.getHeldItem().equals(itemStack) || !InventoryUtils.BLOCK_BLACKLIST.contains(block)) {
                    amount += itemStack.getStackSize();
                }
            }
            ++i;
        }
        return amount;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

