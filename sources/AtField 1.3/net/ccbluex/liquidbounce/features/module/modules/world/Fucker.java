/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import kotlin.TypeCastException;
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
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
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
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Fucker", description="Destroys selected blocks around you. (aka.  IDNuker)", category=ModuleCategory.WORLD)
public final class Fucker
extends Module {
    public static final Fucker INSTANCE;
    private static final BoolValue instantValue;
    private static final IntegerValue switchValue;
    private static WBlockPos oldPos;
    private static final BoolValue surroundingsValue;
    private static final MSTimer switchTimer;
    private static WBlockPos pos;
    private static float currentDamage;
    private static final ListValue throughWallsValue;
    private static final BoolValue noHitValue;
    private static final FloatValue rangeValue;
    private static int blockHitDelay;
    private static final BoolValue rotationsValue;
    private static final BoolValue swingValue;
    private static final ListValue actionValue;
    private static final BlockValue blockValue;

    @Override
    public String getTag() {
        return BlockUtils.getBlockName(((Number)blockValue.get()).intValue());
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        WBlockPos wBlockPos = pos;
        if (wBlockPos == null) {
            return;
        }
        RenderUtils.drawBlockBox(wBlockPos, Color.RED, true);
    }

    /*
     * Exception decompiling
     */
    public final WBlockPos find(int var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl116 : ILOAD - null : trying to set 1 previously set to 0
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        block52: {
            WBlockPos wBlockPos;
            IEntityPlayerSP iEntityPlayerSP;
            block49: {
                Object object;
                block50: {
                    block51: {
                        Object object2;
                        block48: {
                            int n;
                            block47: {
                                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP2 == null) {
                                    return;
                                }
                                iEntityPlayerSP = iEntityPlayerSP2;
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
                                n = ((Number)blockValue.get()).intValue();
                                if (pos == null) break block47;
                                IExtractedFunctions iExtractedFunctions = Fucker.access$getFunctions$p$s1046033730();
                                WBlockPos wBlockPos2 = pos;
                                if (wBlockPos2 == null) {
                                    Intrinsics.throwNpe();
                                }
                                IBlock iBlock = BlockUtils.getBlock(wBlockPos2);
                                if (iBlock == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (iExtractedFunctions.getIdFromBlock(iBlock) != n) break block47;
                                WBlockPos wBlockPos3 = pos;
                                if (wBlockPos3 == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (!(BlockUtils.getCenterDistance(wBlockPos3) > ((Number)rangeValue.get()).doubleValue())) break block48;
                            }
                            pos = this.find(n);
                        }
                        if (pos == null) {
                            currentDamage = 0.0f;
                            return;
                        }
                        WBlockPos wBlockPos4 = pos;
                        if (wBlockPos4 == null) {
                            return;
                        }
                        wBlockPos = wBlockPos4;
                        VecRotation vecRotation = RotationUtils.faceBlock(wBlockPos);
                        if (vecRotation == null) {
                            return;
                        }
                        VecRotation vecRotation2 = vecRotation;
                        boolean bl = false;
                        if (((Boolean)surroundingsValue.get()).booleanValue()) {
                            object2 = iEntityPlayerSP.getPositionEyes(1.0f);
                            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                            if (iWorldClient == null) {
                                Intrinsics.throwNpe();
                            }
                            IMovingObjectPosition iMovingObjectPosition = iWorldClient.rayTraceBlocks((WVec3)object2, vecRotation2.getVec(), false, false, true);
                            Object object3 = object = iMovingObjectPosition != null ? iMovingObjectPosition.getBlockPos() : null;
                            if (object != null && !MinecraftInstance.classProvider.isBlockAir(object)) {
                                if (wBlockPos.getX() != ((WVec3i)object).getX() || wBlockPos.getY() != ((WVec3i)object).getY() || wBlockPos.getZ() != ((WVec3i)object).getZ()) {
                                    bl = true;
                                }
                                WBlockPos wBlockPos5 = pos = object;
                                if (wBlockPos5 == null) {
                                    return;
                                }
                                wBlockPos = wBlockPos5;
                                VecRotation vecRotation3 = RotationUtils.faceBlock(wBlockPos);
                                if (vecRotation3 == null) {
                                    return;
                                }
                                vecRotation2 = vecRotation3;
                            }
                        }
                        if (oldPos != null && ((Object)oldPos).equals(wBlockPos) ^ true) {
                            currentDamage = 0.0f;
                            switchTimer.reset();
                        }
                        oldPos = wBlockPos;
                        if (!switchTimer.hasTimePassed(((Number)switchValue.get()).intValue())) {
                            return;
                        }
                        if (blockHitDelay > 0) {
                            int n = blockHitDelay;
                            blockHitDelay = n + -1;
                            return;
                        }
                        if (((Boolean)rotationsValue.get()).booleanValue()) {
                            RotationUtils.setTargetRotation(vecRotation2.getRotation());
                        }
                        if (!StringsKt.equals((String)((String)actionValue.get()), (String)"destroy", (boolean)true) && !bl) break block49;
                        Module module = LiquidBounce.INSTANCE.getModuleManager().get(AutoTool.class);
                        if (module == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.AutoTool");
                        }
                        object2 = (AutoTool)module;
                        if (((Module)object2).getState()) {
                            ((AutoTool)object2).switchSlot(wBlockPos);
                        }
                        if (((Boolean)instantValue.get()).booleanValue()) {
                            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, wBlockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                            if (((Boolean)swingValue.get()).booleanValue()) {
                                iEntityPlayerSP.swingItem();
                            }
                            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, wBlockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                            currentDamage = 0.0f;
                            return;
                        }
                        IBlock iBlock = wBlockPos.getBlock();
                        if (iBlock == null) {
                            return;
                        }
                        object = iBlock;
                        if (currentDamage != 0.0f) break block50;
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, wBlockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                        if (iEntityPlayerSP.getCapabilities().isCreativeMode()) break block51;
                        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient == null) {
                            Intrinsics.throwNpe();
                        }
                        IWorld iWorld = iWorldClient;
                        WBlockPos wBlockPos6 = pos;
                        if (wBlockPos6 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!(object.getPlayerRelativeBlockHardness(iEntityPlayerSP, iWorld, wBlockPos6) >= 1.0f)) break block50;
                    }
                    if (((Boolean)swingValue.get()).booleanValue()) {
                        iEntityPlayerSP.swingItem();
                    }
                    IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                    WBlockPos wBlockPos7 = pos;
                    if (wBlockPos7 == null) {
                        Intrinsics.throwNpe();
                    }
                    iPlayerControllerMP.onPlayerDestroyBlock(wBlockPos7, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN));
                    currentDamage = 0.0f;
                    pos = null;
                    return;
                }
                if (((Boolean)swingValue.get()).booleanValue()) {
                    iEntityPlayerSP.swingItem();
                }
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                currentDamage += object.getPlayerRelativeBlockHardness(iEntityPlayerSP, iWorldClient, wBlockPos);
                IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient2 == null) {
                    Intrinsics.throwNpe();
                }
                iWorldClient2.sendBlockBreakProgress(iEntityPlayerSP.getEntityId(), wBlockPos, (int)(currentDamage * 10.0f) - 1);
                if (!(currentDamage >= 1.0f)) break block52;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, wBlockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                MinecraftInstance.mc.getPlayerController().onPlayerDestroyBlock(wBlockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN));
                blockHitDelay = 4;
                currentDamage = 0.0f;
                pos = null;
                break block52;
            }
            if (!StringsKt.equals((String)((String)actionValue.get()), (String)"use", (boolean)true)) break block52;
            IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            IItemStack iItemStack = iEntityPlayerSP.getHeldItem();
            if (iItemStack == null) {
                Intrinsics.throwNpe();
            }
            WBlockPos wBlockPos8 = pos;
            if (wBlockPos8 == null) {
                Intrinsics.throwNpe();
            }
            if (iPlayerControllerMP.onPlayerRightClick(iEntityPlayerSP, iWorldClient, iItemStack, wBlockPos8, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN), new WVec3(wBlockPos.getX(), wBlockPos.getY(), wBlockPos.getZ()))) {
                if (((Boolean)swingValue.get()).booleanValue()) {
                    iEntityPlayerSP.swingItem();
                }
                blockHitDelay = 4;
                currentDamage = 0.0f;
                pos = null;
            }
        }
    }

    private Fucker() {
    }

    public final void setCurrentDamage(float f) {
        currentDamage = f;
    }

    public final WBlockPos getPos() {
        return pos;
    }

    public final void setPos(@Nullable WBlockPos wBlockPos) {
        pos = wBlockPos;
    }

    public final float getCurrentDamage() {
        return currentDamage;
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

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }
}

