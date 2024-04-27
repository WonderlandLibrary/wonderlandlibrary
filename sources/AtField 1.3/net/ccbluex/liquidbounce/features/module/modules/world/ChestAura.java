/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura$WhenMappings;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.extensions.BlockExtensionKt;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="ChestAura", description="Automatically opens chests around you.", category=ModuleCategory.WORLD)
public final class ChestAura
extends Module {
    private static final IntegerValue delayValue;
    private static final List clickedBlocks;
    private static final FloatValue rangeValue;
    private static final BoolValue rotationsValue;
    private static final BlockValue chestValue;
    private static WBlockPos currentBlock;
    private static final BoolValue throughWallsValue;
    private static final MSTimer timer;
    public static final ChestAura INSTANCE;
    private static final BoolValue visualSwing;

    @EventTarget
    public final void onMotion(MotionEvent motionEvent) {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Blink.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module.getState()) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        IWorldClient iWorldClient2 = iWorldClient;
        switch (ChestAura$WhenMappings.$EnumSwitchMapping$0[motionEvent.getEventState().ordinal()]) {
            case 1: {
                Map map;
                boolean bl;
                Map.Entry entry;
                Map.Entry entry2;
                if (MinecraftInstance.classProvider.isGuiContainer(MinecraftInstance.mc.getCurrentScreen())) {
                    timer.reset();
                }
                float f = ((Number)rangeValue.get()).floatValue() + 1.0f;
                WVec3 wVec3 = new WVec3(iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY() + (double)iEntityPlayerSP2.getEyeHeight(), iEntityPlayerSP2.getPosZ());
                Map map2 = BlockUtils.searchBlocks((int)f);
                boolean bl2 = false;
                Object object = map2;
                Map map3 = new LinkedHashMap();
                boolean bl3 = false;
                Map map4 = object;
                boolean bl4 = false;
                Iterator iterator2 = map4.entrySet().iterator();
                while (iterator2.hasNext()) {
                    entry = entry2 = iterator2.next();
                    bl = false;
                    if (!(ChestAura.access$getFunctions$p$s1046033730().getIdFromBlock((IBlock)entry.getValue()) == ((Number)chestValue.get()).intValue() && !clickedBlocks.contains(entry.getKey()) && BlockUtils.getCenterDistance((WBlockPos)entry.getKey()) < ((Number)rangeValue.get()).doubleValue())) continue;
                    map3.put(entry2.getKey(), entry2.getValue());
                }
                map2 = map3;
                bl2 = false;
                object = map2;
                map3 = new LinkedHashMap();
                bl3 = false;
                map4 = object;
                bl4 = false;
                iterator2 = map4.entrySet().iterator();
                while (iterator2.hasNext()) {
                    WBlockPos wBlockPos;
                    IMovingObjectPosition iMovingObjectPosition;
                    entry = entry2 = iterator2.next();
                    bl = false;
                    boolean bl5 = ((Boolean)throughWallsValue.get()).booleanValue() ? true : (iMovingObjectPosition = iWorldClient2.rayTraceBlocks(wVec3, BlockExtensionKt.getVec(wBlockPos = (WBlockPos)entry.getKey()), false, true, false)) != null && ((Object)iMovingObjectPosition.getBlockPos()).equals(wBlockPos);
                    if (!bl5) continue;
                    map3.put(entry2.getKey(), entry2.getValue());
                }
                map2 = map3;
                bl2 = false;
                object = map2.entrySet();
                boolean bl6 = false;
                Iterator iterator3 = object.iterator();
                if (!iterator3.hasNext()) {
                    map = null;
                } else {
                    map4 = iterator3.next();
                    if (!iterator3.hasNext()) {
                        map = map4;
                    } else {
                        Map.Entry entry3 = (Map.Entry)((Object)map4);
                        boolean bl7 = false;
                        double d = BlockUtils.getCenterDistance((WBlockPos)entry3.getKey());
                        do {
                            Object t = iterator3.next();
                            Map.Entry entry4 = (Map.Entry)t;
                            boolean bl8 = false;
                            double d2 = BlockUtils.getCenterDistance((WBlockPos)entry4.getKey());
                            if (Double.compare(d, d2) <= 0) continue;
                            map4 = t;
                            d = d2;
                        } while (iterator3.hasNext());
                        map = map4;
                    }
                }
                Map.Entry entry5 = (Map.Entry)((Object)map);
                WBlockPos wBlockPos = currentBlock = entry5 != null ? (WBlockPos)entry5.getKey() : null;
                if (!((Boolean)rotationsValue.get()).booleanValue()) break;
                WBlockPos wBlockPos2 = currentBlock;
                if (wBlockPos2 == null) {
                    return;
                }
                VecRotation vecRotation = RotationUtils.faceBlock(wBlockPos2);
                if (vecRotation == null) {
                    return;
                }
                RotationUtils.setTargetRotation(vecRotation.getRotation());
                break;
            }
            case 2: {
                if (currentBlock == null || !timer.hasTimePassed(((Number)delayValue.get()).intValue())) break;
                IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
                IWorldClient iWorldClient3 = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient3 == null) {
                    Intrinsics.throwNpe();
                }
                IItemStack iItemStack = iEntityPlayerSP2.getHeldItem();
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                WBlockPos wBlockPos = currentBlock;
                if (wBlockPos == null) {
                    Intrinsics.throwNpe();
                }
                IEnumFacing iEnumFacing = MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN);
                WBlockPos wBlockPos3 = currentBlock;
                if (wBlockPos3 == null) {
                    Intrinsics.throwNpe();
                }
                if (!iPlayerControllerMP.onPlayerRightClick(iEntityPlayerSP2, iWorldClient3, iItemStack, wBlockPos, iEnumFacing, BlockExtensionKt.getVec(wBlockPos3))) break;
                if (((Boolean)visualSwing.get()).booleanValue()) {
                    iEntityPlayerSP2.swingItem();
                } else {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketAnimation());
                }
                WBlockPos wBlockPos4 = currentBlock;
                if (wBlockPos4 == null) {
                    Intrinsics.throwNpe();
                }
                clickedBlocks.add(wBlockPos4);
                currentBlock = null;
                timer.reset();
                break;
            }
        }
    }

    private ChestAura() {
    }

    static {
        ChestAura chestAura;
        INSTANCE = chestAura = new ChestAura();
        rangeValue = new FloatValue("Range", 5.0f, 1.0f, 6.0f);
        delayValue = new IntegerValue("Delay", 100, 50, 200);
        throughWallsValue = new BoolValue("ThroughWalls", true);
        visualSwing = new BoolValue("VisualSwing", true);
        chestValue = new BlockValue("Chest", ChestAura.access$getFunctions$p$s1046033730().getIdFromBlock(MinecraftInstance.classProvider.getBlockEnum(BlockType.CHEST)));
        rotationsValue = new BoolValue("Rotations", true);
        timer = new MSTimer();
        boolean bl = false;
        clickedBlocks = new ArrayList();
    }

    public final List getClickedBlocks() {
        return clickedBlocks;
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    @Override
    public void onDisable() {
        clickedBlocks.clear();
    }
}

