package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorld;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Fucker", description="Destroys selected blocks around you. (aka.  IDNuker)", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000l\n\n\n\b\n\n\u0000\n\b\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\bÇ\u000020B\b¢J'02(0J)0*2+0HJ,0-2.0/HJ00-2.01HR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R\t0\nX¢\n\u0000\b\f\"\b\rR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000\b\"\bR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R 0!X¢\n\u0000R\"0#8VX¢\b$%R&0X¢\n\u0000¨2"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Fucker;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "actionValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "blockHitDelay", "", "blockValue", "Lnet/ccbluex/liquidbounce/value/BlockValue;", "currentDamage", "", "getCurrentDamage", "()F", "setCurrentDamage", "(F)V", "instantValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "noHitValue", "oldPos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "pos", "getPos", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "setPos", "(Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;)V", "rangeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rotationsValue", "surroundingsValue", "swingValue", "switchTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "switchValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "tag", "", "getTag", "()Ljava/lang/String;", "throughWallsValue", "find", "targetID", "isHitable", "", "blockPos", "onRender3D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class Fucker
extends Module {
    private static final BlockValue blockValue;
    private static final ListValue throughWallsValue;
    private static final FloatValue rangeValue;
    private static final ListValue actionValue;
    private static final BoolValue instantValue;
    private static final IntegerValue switchValue;
    private static final BoolValue swingValue;
    private static final BoolValue rotationsValue;
    private static final BoolValue surroundingsValue;
    private static final BoolValue noHitValue;
    @Nullable
    private static WBlockPos pos;
    private static WBlockPos oldPos;
    private static int blockHitDelay;
    private static final MSTimer switchTimer;
    private static float currentDamage;
    public static final Fucker INSTANCE;

    @Nullable
    public final WBlockPos getPos() {
        return pos;
    }

    public final void setPos(@Nullable WBlockPos wBlockPos) {
        pos = wBlockPos;
    }

    public final float getCurrentDamage() {
        return currentDamage;
    }

    public final void setCurrentDamage(float f) {
        currentDamage = f;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block52: {
            WBlockPos currentPos;
            IEntityPlayerSP thePlayer;
            block49: {
                IBlock block;
                block50: {
                    block51: {
                        block48: {
                            int targetId;
                            block47: {
                                void blockPos$iv;
                                IBlock iBlock;
                                Intrinsics.checkParameterIsNotNull(event, "event");
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    return;
                                }
                                thePlayer = iEntityPlayerSP;
                                if (((Boolean)noHitValue.get()).booleanValue()) {
                                    Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
                                    if (module == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
                                    }
                                    KillAura killAura = (KillAura)module;
                                    if (killAura.getState() && killAura.getTarget() != null) {
                                        return;
                                    }
                                }
                                targetId = ((Number)blockValue.get()).intValue();
                                if (pos == null) break block47;
                                WBlockPos wBlockPos = pos;
                                if (wBlockPos == null) {
                                    Intrinsics.throwNpe();
                                }
                                WBlockPos wBlockPos2 = wBlockPos;
                                IExtractedFunctions iExtractedFunctions = MinecraftInstance.functions;
                                boolean $i$f$getBlock = false;
                                Object object = MinecraftInstance.mc.getTheWorld();
                                IBlock iBlock2 = iBlock = object != null && (object = object.getBlockState((WBlockPos)blockPos$iv)) != null ? object.getBlock() : null;
                                if (iBlock2 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (iExtractedFunctions.getIdFromBlock(iBlock2) != targetId) break block47;
                                WBlockPos wBlockPos3 = pos;
                                if (wBlockPos3 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (!(BlockUtils.getCenterDistance(wBlockPos3) > ((Number)rangeValue.get()).doubleValue())) break block48;
                            }
                            pos = this.find(targetId);
                        }
                        if (pos == null) {
                            currentDamage = 0.0f;
                            return;
                        }
                        WBlockPos wBlockPos = pos;
                        if (wBlockPos == null) {
                            return;
                        }
                        currentPos = wBlockPos;
                        VecRotation vecRotation = RotationUtils.faceBlock(currentPos);
                        if (vecRotation == null) {
                            return;
                        }
                        VecRotation rotations = vecRotation;
                        boolean surroundings = false;
                        if (((Boolean)surroundingsValue.get()).booleanValue()) {
                            WBlockPos blockPos;
                            WVec3 eyes = thePlayer.getPositionEyes(1.0f);
                            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                            if (iWorldClient == null) {
                                Intrinsics.throwNpe();
                            }
                            IMovingObjectPosition iMovingObjectPosition = iWorldClient.rayTraceBlocks(eyes, rotations.getVec(), false, false, true);
                            WBlockPos wBlockPos4 = blockPos = iMovingObjectPosition != null ? iMovingObjectPosition.getBlockPos() : null;
                            if (blockPos != null && !MinecraftInstance.classProvider.isBlockAir(blockPos)) {
                                if (currentPos.getX() != blockPos.getX() || currentPos.getY() != blockPos.getY() || currentPos.getZ() != blockPos.getZ()) {
                                    surroundings = true;
                                }
                                WBlockPos wBlockPos5 = pos = blockPos;
                                if (wBlockPos5 == null) {
                                    return;
                                }
                                currentPos = wBlockPos5;
                                VecRotation vecRotation2 = RotationUtils.faceBlock(currentPos);
                                if (vecRotation2 == null) {
                                    return;
                                }
                                rotations = vecRotation2;
                            }
                        }
                        if (oldPos != null && Intrinsics.areEqual(oldPos, currentPos) ^ true) {
                            currentDamage = 0.0f;
                            switchTimer.reset();
                        }
                        oldPos = currentPos;
                        if (!switchTimer.hasTimePassed(((Number)switchValue.get()).intValue())) {
                            return;
                        }
                        if (blockHitDelay > 0) {
                            int eyes = blockHitDelay;
                            blockHitDelay = eyes + -1;
                            return;
                        }
                        if (((Boolean)rotationsValue.get()).booleanValue()) {
                            RotationUtils.setTargetRotation(rotations.getRotation());
                        }
                        if (!StringsKt.equals((String)actionValue.get(), "destroy", true) && !surroundings) break block49;
                        Module module = LiquidBounce.INSTANCE.getModuleManager().get(AutoTool.class);
                        if (module == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.AutoTool");
                        }
                        AutoTool autoTool = (AutoTool)module;
                        if (autoTool.getState()) {
                            autoTool.switchSlot(currentPos);
                        }
                        if (((Boolean)instantValue.get()).booleanValue()) {
                            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, currentPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                            if (((Boolean)swingValue.get()).booleanValue()) {
                                thePlayer.swingItem();
                            }
                            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, currentPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                            currentDamage = 0.0f;
                            return;
                        }
                        IBlock iBlock = currentPos.getBlock();
                        if (iBlock == null) {
                            return;
                        }
                        block = iBlock;
                        if (currentDamage != 0.0f) break block50;
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, currentPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                        if (thePlayer.getCapabilities().isCreativeMode()) break block51;
                        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient == null) {
                            Intrinsics.throwNpe();
                        }
                        IWorld iWorld = iWorldClient;
                        WBlockPos wBlockPos6 = pos;
                        if (wBlockPos6 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!(block.getPlayerRelativeBlockHardness(thePlayer, iWorld, wBlockPos6) >= 1.0f)) break block50;
                    }
                    if (((Boolean)swingValue.get()).booleanValue()) {
                        thePlayer.swingItem();
                    }
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    WBlockPos wBlockPos = pos;
                    if (wBlockPos == null) {
                        Intrinsics.throwNpe();
                    }
                    iPlayerControllerMP.onPlayerDestroyBlock(wBlockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN));
                    currentDamage = 0.0f;
                    pos = null;
                    return;
                }
                if (((Boolean)swingValue.get()).booleanValue()) {
                    thePlayer.swingItem();
                }
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                currentDamage += block.getPlayerRelativeBlockHardness(thePlayer, iWorldClient, currentPos);
                IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient2 == null) {
                    Intrinsics.throwNpe();
                }
                iWorldClient2.sendBlockBreakProgress(thePlayer.getEntityId(), currentPos, (int)(currentDamage * 10.0f) - 1);
                if (!(currentDamage >= 1.0f)) break block52;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, currentPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                MinecraftInstance.mc.getPlayerController().onPlayerDestroyBlock(currentPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN));
                blockHitDelay = 4;
                currentDamage = 0.0f;
                pos = null;
                break block52;
            }
            if (!StringsKt.equals((String)actionValue.get(), "use", true)) break block52;
            IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IItemStack iItemStack = thePlayer.getHeldItem();
            if (iItemStack == null) {
                Intrinsics.throwNpe();
            }
            WBlockPos wBlockPos = pos;
            if (wBlockPos == null) {
                Intrinsics.throwNpe();
            }
            if (iPlayerControllerMP.onPlayerRightClick(thePlayer, iWorldClient, iItemStack, wBlockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN), new WVec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()))) {
                if (((Boolean)swingValue.get()).booleanValue()) {
                    thePlayer.swingItem();
                }
                blockHitDelay = 4;
                currentDamage = 0.0f;
                pos = null;
            }
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        WBlockPos wBlockPos = pos;
        if (wBlockPos == null) {
            return;
        }
        RenderUtils.drawBlockBox(wBlockPos, Color.RED, true);
    }

    /*
     * WARNING - void declaration
     */
    @Nullable
    public final WBlockPos find(int targetID) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return null;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        int radius = (int)((Number)rangeValue.get()).floatValue() + 1;
        double nearestBlockDistance = DoubleCompanionObject.INSTANCE.getMAX_VALUE();
        WBlockPos nearestBlock = null;
        int n = radius;
        int n2 = -radius + 1;
        if (n >= n2) {
            while (true) {
                void x;
                int n3;
                int n4;
                if ((n4 = radius) >= (n3 = -radius + 1)) {
                    while (true) {
                        void y;
                        int n5;
                        int n6;
                        if ((n6 = radius) >= (n5 = -radius + 1)) {
                            while (true) {
                                void z;
                                WBlockPos blockPos = new WBlockPos((int)thePlayer.getPosX() + x, (int)thePlayer.getPosY() + y, (int)thePlayer.getPosZ() + z);
                                boolean $i$f$getBlock = false;
                                Object object = MinecraftInstance.mc.getTheWorld();
                                if ((object != null && (object = object.getBlockState(blockPos)) != null ? object.getBlock() : null) == null) {
                                } else {
                                    double distance;
                                    IBlock block;
                                    block = block;
                                    if (MinecraftInstance.functions.getIdFromBlock(block) == targetID && !((distance = BlockUtils.getCenterDistance(blockPos)) > ((Number)rangeValue.get()).doubleValue()) && !(nearestBlockDistance < distance) && (this.isHitable(blockPos) || ((Boolean)surroundingsValue.get()).booleanValue())) {
                                        nearestBlockDistance = distance;
                                        nearestBlock = blockPos;
                                    }
                                }
                                if (z == n5) break;
                                --z;
                            }
                        }
                        if (y == n3) break;
                        --y;
                    }
                }
                if (x == n2) break;
                --x;
            }
        }
        return nearestBlock;
    }

    private final boolean isHitable(WBlockPos blockPos) {
        boolean bl;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return false;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        String string = (String)throughWallsValue.get();
        boolean bl2 = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "raycast": {
                IMovingObjectPosition movingObjectPosition;
                WVec3 eyesPos = new WVec3(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY() + (double)thePlayer.getEyeHeight(), thePlayer.getPosZ());
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                if ((movingObjectPosition = iWorldClient.rayTraceBlocks(eyesPos, new WVec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5), false, true, false)) != null && Intrinsics.areEqual(movingObjectPosition.getBlockPos(), blockPos)) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            case "around": {
                if (!(BlockUtils.isFullBlock(blockPos.down()) && BlockUtils.isFullBlock(blockPos.up()) && BlockUtils.isFullBlock(blockPos.north()) && BlockUtils.isFullBlock(blockPos.east()) && BlockUtils.isFullBlock(blockPos.south()) && BlockUtils.isFullBlock(blockPos.west()))) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            default: {
                bl = true;
            }
        }
        return bl;
    }

    @Override
    @NotNull
    public String getTag() {
        return BlockUtils.getBlockName(((Number)blockValue.get()).intValue());
    }

    private Fucker() {
    }

    static {
        Fucker fucker;
        INSTANCE = fucker = new Fucker();
        blockValue = new BlockValue("Block", 26);
        throughWallsValue = new ListValue("ThroughWalls", new String[]{"None", "Raycast", "Around"}, "None");
        rangeValue = new FloatValue("Range", 5.0f, 1.0f, 7.0f);
        actionValue = new ListValue("Action", new String[]{"Destroy", "Use"}, "Destroy");
        instantValue = new BoolValue("Instant", false);
        switchValue = new IntegerValue("SwitchDelay", 250, 0, 1000);
        swingValue = new BoolValue("Swing", true);
        rotationsValue = new BoolValue("Rotations", true);
        surroundingsValue = new BoolValue("Surroundings", true);
        noHitValue = new BoolValue("NoHit", false);
        switchTimer = new MSTimer();
    }
}
