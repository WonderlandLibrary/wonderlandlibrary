/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.MapsKt
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.DoubleCompanionObject
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.math.MathKt
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.TypeCastException;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoTool;
import net.ccbluex.liquidbounce.features.module.modules.world.FastBreak;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="Nuker", description="Breaks all blocks around you.", category=ModuleCategory.WORLD)
public final class Nuker
extends Module {
    private final FloatValue radiusValue = new FloatValue("Radius", 5.2f, 1.0f, 6.0f);
    private final BoolValue throughWallsValue = new BoolValue("ThroughWalls", false);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Distance", "Hardness"}, "Distance");
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue layerValue = new BoolValue("Layer", false);
    private final IntegerValue hitDelayValue = new IntegerValue("HitDelay", 4, 0, 20);
    private final IntegerValue nukeValue = new IntegerValue("Nuke", 1, 1, 20);
    private final IntegerValue nukeDelay = new IntegerValue("NukeDelay", 1, 1, 20);
    private final ArrayList<WBlockPos> attackedBlocks;
    private WBlockPos currentBlock;
    private int blockHitDelay;
    private TickTimer nukeTimer;
    private int nuke;
    private static float currentDamage;
    public static final Companion Companion;

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        void $this$filterTo$iv$iv;
        IEntityPlayerSP thePlayer;
        block52: {
            Map.Entry $this$filterTo$iv$iv2;
            if (this.blockHitDelay > 0) {
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(FastBreak.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (!module.getState()) {
                    int n = this.blockHitDelay;
                    this.blockHitDelay = n + -1;
                    return;
                }
            }
            this.nukeTimer.update();
            if (this.nukeTimer.hasTimePassed(((Number)this.nukeDelay.get()).intValue())) {
                this.nuke = 0;
                this.nukeTimer.reset();
            }
            this.attackedBlocks.clear();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            thePlayer = iEntityPlayerSP;
            if (MinecraftInstance.mc.getPlayerController().isInCreativeMode()) break block52;
            Object $this$filter$iv = BlockUtils.searchBlocks(MathKt.roundToInt((float)((Number)this.radiusValue.get()).floatValue()) + 1);
            boolean $i$f$filter = false;
            Map<WBlockPos, IBlock> map = $this$filter$iv;
            Object destination$iv$iv2 = new LinkedHashMap();
            boolean $i$f$filterTo = false;
            void var9_19 = $this$filterTo$iv$iv2;
            boolean bl = false;
            Iterator iterator = var9_19.entrySet().iterator();
            while (iterator.hasNext()) {
                boolean bl2;
                Map.Entry element$iv$iv;
                Map.Entry $dstr$pos$block = element$iv$iv = iterator.next();
                boolean bl3 = false;
                Map.Entry entry = $dstr$pos$block;
                boolean bl4 = false;
                WBlockPos pos3 = (WBlockPos)entry.getKey();
                entry = $dstr$pos$block;
                bl4 = false;
                IBlock block = (IBlock)entry.getValue();
                if (BlockUtils.getCenterDistance(pos3) <= ((Number)this.radiusValue.get()).doubleValue() && this.validBlock(block)) {
                    if (((Boolean)this.layerValue.get()).booleanValue() && (double)pos3.getY() < thePlayer.getPosY()) {
                        bl2 = false;
                    } else if (!((Boolean)this.throughWallsValue.get()).booleanValue()) {
                        IMovingObjectPosition rayTrace;
                        WVec3 eyesPos = new WVec3(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY() + (double)thePlayer.getEyeHeight(), thePlayer.getPosZ());
                        WVec3 blockVec = new WVec3((double)pos3.getX() + 0.5, (double)pos3.getY() + 0.5, (double)pos3.getZ() + 0.5);
                        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient == null) {
                            Intrinsics.throwNpe();
                        }
                        bl2 = (rayTrace = iWorldClient.rayTraceBlocks(eyesPos, blockVec, false, true, false)) != null && ((Object)rayTrace.getBlockPos()).equals(pos3);
                    } else {
                        bl2 = true;
                    }
                } else {
                    bl2 = false;
                }
                if (!bl2) continue;
                destination$iv$iv2.put(element$iv$iv.getKey(), element$iv$iv.getValue());
            }
            Map validBlocks = MapsKt.toMutableMap((Map)destination$iv$iv2);
            do {
                void blockPos;
                boolean $this$minBy$iv2;
                block55: {
                    IBlock block;
                    block54: {
                        Map.Entry entry;
                        block53: {
                            Object v6;
                            WBlockPos safePos;
                            IBlock block2;
                            WBlockPos pos;
                            WBlockPos safePos2;
                            Object pos2;
                            Object minElem$iv$iv;
                            Iterator iterator$iv$iv;
                            boolean $i$f$minBy;
                            Iterable $this$minBy$iv$iv;
                            boolean $i$f$minBy2;
                            Map $this$minBy$iv2;
                            destination$iv$iv2 = (String)this.priorityValue.get();
                            switch (((String)destination$iv$iv2).hashCode()) {
                                case 181289442: {
                                    if (!((String)destination$iv$iv2).equals("Hardness")) return;
                                    break;
                                }
                                case 353103893: {
                                    Object v4;
                                    if (!((String)destination$iv$iv2).equals("Distance")) return;
                                    $this$minBy$iv2 = validBlocks;
                                    $i$f$minBy2 = false;
                                    $this$minBy$iv$iv = $this$minBy$iv2.entrySet();
                                    $i$f$minBy = false;
                                    iterator$iv$iv = $this$minBy$iv$iv.iterator();
                                    if (!iterator$iv$iv.hasNext()) {
                                        v4 = null;
                                    } else {
                                        minElem$iv$iv = iterator$iv$iv.next();
                                        if (!iterator$iv$iv.hasNext()) {
                                            v4 = minElem$iv$iv;
                                        } else {
                                            Map.Entry $dstr$pos$block = (Map.Entry)minElem$iv$iv;
                                            boolean bl5 = false;
                                            Map.Entry blockVec = $dstr$pos$block;
                                            boolean pos3 = false;
                                            pos2 = (WBlockPos)blockVec.getKey();
                                            blockVec = $dstr$pos$block;
                                            pos3 = false;
                                            IBlock block32 = (IBlock)blockVec.getValue();
                                            double distance = BlockUtils.getCenterDistance((WBlockPos)pos2);
                                            safePos2 = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ());
                                            double minValue$iv$iv = ((WVec3i)pos2).getX() == safePos2.getX() && safePos2.getY() <= ((WVec3i)pos2).getY() && ((WVec3i)pos2).getZ() == safePos2.getZ() ? DoubleCompanionObject.INSTANCE.getMAX_VALUE() - distance : distance;
                                            do {
                                                Object e$iv$iv = iterator$iv$iv.next();
                                                Map.Entry $dstr$pos$block2 = (Map.Entry)e$iv$iv;
                                                bl7 = false;
                                                pos2 = $dstr$pos$block2;
                                                boolean block32 = false;
                                                pos = (WBlockPos)pos2.getKey();
                                                pos2 = $dstr$pos$block2;
                                                block32 = false;
                                                block2 = (IBlock)pos2.getValue();
                                                double distance2 = BlockUtils.getCenterDistance(pos);
                                                safePos = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ());
                                                double v$iv$iv = pos.getX() == safePos.getX() && safePos.getY() <= pos.getY() && pos.getZ() == safePos.getZ() ? DoubleCompanionObject.INSTANCE.getMAX_VALUE() - distance2 : distance2;
                                                if (Double.compare(minValue$iv$iv, v$iv$iv) <= 0) continue;
                                                minElem$iv$iv = e$iv$iv;
                                                minValue$iv$iv = v$iv$iv;
                                            } while (iterator$iv$iv.hasNext());
                                            v4 = minElem$iv$iv;
                                        }
                                    }
                                    entry = v4;
                                    break block53;
                                }
                            }
                            $this$minBy$iv2 = validBlocks;
                            $i$f$minBy2 = false;
                            $this$minBy$iv$iv = $this$minBy$iv2.entrySet();
                            $i$f$minBy = false;
                            iterator$iv$iv = $this$minBy$iv$iv.iterator();
                            if (!iterator$iv$iv.hasNext()) {
                                v6 = null;
                            } else {
                                minElem$iv$iv = iterator$iv$iv.next();
                                if (!iterator$iv$iv.hasNext()) {
                                    v6 = minElem$iv$iv;
                                } else {
                                    Map.Entry $dstr$pos$block22 = (Map.Entry)minElem$iv$iv;
                                    boolean bl62 = false;
                                    Map.Entry v$iv$iv = $dstr$pos$block22;
                                    boolean bl7 = false;
                                    pos2 = (WBlockPos)v$iv$iv.getKey();
                                    v$iv$iv = $dstr$pos$block22;
                                    bl7 = false;
                                    IBlock block4 = (IBlock)v$iv$iv.getValue();
                                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                                    if (iWorldClient == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    double hardness = block4.getPlayerRelativeBlockHardness(thePlayer, iWorldClient, (WBlockPos)pos2);
                                    safePos2 = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ());
                                    double $dstr$pos$block22 = ((WVec3i)pos2).getX() == safePos2.getX() && safePos2.getY() <= ((WVec3i)pos2).getY() && ((WVec3i)pos2).getZ() == safePos2.getZ() ? DoubleCompanionObject.INSTANCE.getMIN_VALUE() + hardness : hardness;
                                    do {
                                        Object bl62 = iterator$iv$iv.next();
                                        Map.Entry $dstr$pos$block32 = (Map.Entry)bl62;
                                        boolean $i$a$-maxBy-Nuker$onUpdate$3 = false;
                                        pos2 = $dstr$pos$block32;
                                        boolean bl8 = false;
                                        pos = (WBlockPos)pos2.getKey();
                                        pos2 = $dstr$pos$block32;
                                        bl8 = false;
                                        block2 = (IBlock)pos2.getValue();
                                        IWorldClient iWorldClient2 = MinecraftInstance.mc.getTheWorld();
                                        if (iWorldClient2 == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        double hardness2 = block2.getPlayerRelativeBlockHardness(thePlayer, iWorldClient2, pos);
                                        safePos = new WBlockPos(thePlayer.getPosX(), thePlayer.getPosY() - 1.0, thePlayer.getPosZ());
                                        double $dstr$pos$block32 = pos.getX() == safePos.getX() && safePos.getY() <= pos.getY() && pos.getZ() == safePos.getZ() ? DoubleCompanionObject.INSTANCE.getMIN_VALUE() + hardness2 : hardness2;
                                        if (Double.compare($dstr$pos$block22, $dstr$pos$block32) >= 0) continue;
                                        minElem$iv$iv = bl62;
                                        $dstr$pos$block22 = $dstr$pos$block32;
                                    } while (iterator$iv$iv.hasNext());
                                    v6 = minElem$iv$iv;
                                }
                            }
                            entry = v6;
                            break block53;
                            return;
                        }
                        if (entry == null) {
                            return;
                        }
                        $this$filterTo$iv$iv2 = entry;
                        destination$iv$iv2 = $this$filterTo$iv$iv2;
                        $this$minBy$iv2 = false;
                        $this$filter$iv = (WBlockPos)destination$iv$iv2.getKey();
                        destination$iv$iv2 = $this$filterTo$iv$iv2;
                        $this$minBy$iv2 = false;
                        block = (IBlock)destination$iv$iv2.getValue();
                        if (blockPos.equals(this.currentBlock) ^ true) {
                            currentDamage = 0.0f;
                        }
                        if (((Boolean)this.rotationsValue.get()).booleanValue()) {
                            VecRotation rotation;
                            if (RotationUtils.faceBlock((WBlockPos)blockPos) == null) {
                                return;
                            }
                            RotationUtils.setTargetRotation(rotation.getRotation());
                        }
                        this.currentBlock = blockPos;
                        this.attackedBlocks.add((WBlockPos)blockPos);
                        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(AutoTool.class);
                        if (module == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.AutoTool");
                        }
                        AutoTool autoTool = (AutoTool)module;
                        if (autoTool.getState()) {
                            autoTool.switchSlot((WBlockPos)blockPos);
                        }
                        if (currentDamage != 0.0f) break block54;
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, (WBlockPos)blockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                        if (iWorldClient == null) {
                            Intrinsics.throwNpe();
                        }
                        if (block.getPlayerRelativeBlockHardness(thePlayer, iWorldClient, (WBlockPos)blockPos) >= 1.0f) break block55;
                    }
                    thePlayer.swingItem();
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    currentDamage += block.getPlayerRelativeBlockHardness(thePlayer, iWorldClient, (WBlockPos)blockPos);
                    IWorldClient iWorldClient3 = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient3 == null) {
                        Intrinsics.throwNpe();
                    }
                    iWorldClient3.sendBlockBreakProgress(thePlayer.getEntityId(), (WBlockPos)blockPos, (int)(currentDamage * 10.0f) - 1);
                    if (!(currentDamage >= 1.0f)) return;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, (WBlockPos)blockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
                    MinecraftInstance.mc.getPlayerController().onPlayerDestroyBlock((WBlockPos)blockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN));
                    this.blockHitDelay = ((Number)this.hitDelayValue.get()).intValue();
                    currentDamage = 0.0f;
                    return;
                }
                currentDamage = 0.0f;
                thePlayer.swingItem();
                MinecraftInstance.mc.getPlayerController().onPlayerDestroyBlock((WBlockPos)blockPos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN));
                this.blockHitDelay = ((Number)this.hitDelayValue.get()).intValue();
                destination$iv$iv2 = validBlocks;
                $this$minBy$iv2 = false;
                destination$iv$iv2.remove(blockPos);
                int destination$iv$iv2 = this.nuke;
                this.nuke = destination$iv$iv2 + 1;
            } while (this.nuke < ((Number)this.nukeValue.get()).intValue());
            return;
        }
        IItemStack iItemStack = thePlayer.getHeldItem();
        if (MinecraftInstance.classProvider.isItemSword(iItemStack != null ? iItemStack.getItem() : null)) {
            return;
        }
        Map<WBlockPos, IBlock> $this$filter$iv = BlockUtils.searchBlocks(MathKt.roundToInt((float)((Number)this.radiusValue.get()).floatValue()) + 1);
        boolean $i$f$filter = false;
        Map<WBlockPos, IBlock> block = $this$filter$iv;
        Map destination$iv$iv = new LinkedHashMap();
        boolean $i$f$filterTo = false;
        void $this$minBy$iv2 = $this$filterTo$iv$iv;
        boolean $i$f$minBy2 = false;
        Iterator $this$minBy$iv$iv = $this$minBy$iv2.entrySet().iterator();
        while ($this$minBy$iv$iv.hasNext()) {
            boolean bl;
            Map.Entry element$iv$iv;
            Map.Entry $dstr$pos$block = element$iv$iv = $this$minBy$iv$iv.next();
            boolean bl9 = false;
            Map.Entry $dstr$pos$block22 = $dstr$pos$block;
            boolean bl62 = false;
            WBlockPos pos = (WBlockPos)$dstr$pos$block22.getKey();
            $dstr$pos$block22 = $dstr$pos$block;
            bl62 = false;
            IBlock block5 = (IBlock)$dstr$pos$block22.getValue();
            if (BlockUtils.getCenterDistance(pos) <= ((Number)this.radiusValue.get()).doubleValue() && this.validBlock(block5)) {
                if (((Boolean)this.layerValue.get()).booleanValue() && (double)pos.getY() < thePlayer.getPosY()) {
                    bl = false;
                } else if (!((Boolean)this.throughWallsValue.get()).booleanValue()) {
                    IMovingObjectPosition rayTrace;
                    WVec3 eyesPos = new WVec3(thePlayer.getPosX(), thePlayer.getEntityBoundingBox().getMinY() + (double)thePlayer.getEyeHeight(), thePlayer.getPosZ());
                    WVec3 blockVec = new WVec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
                    IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                    if (iWorldClient == null) {
                        Intrinsics.throwNpe();
                    }
                    bl = (rayTrace = iWorldClient.rayTraceBlocks(eyesPos, blockVec, false, true, false)) != null && ((Object)rayTrace.getBlockPos()).equals(pos);
                } else {
                    bl = true;
                }
            } else {
                bl = false;
            }
            if (!bl) continue;
            destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
        }
        Map $this$forEach$iv = destination$iv$iv;
        boolean $i$f$forEach = false;
        Map map = $this$forEach$iv;
        boolean bl = false;
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry element$iv;
            Map.Entry $dstr$pos$_u24__u24 = element$iv = iterator.next();
            boolean bl10 = false;
            Map.Entry entry = $dstr$pos$_u24__u24;
            boolean bl11 = false;
            WBlockPos pos = (WBlockPos)entry.getKey();
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK, pos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            thePlayer.swingItem();
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK, pos, MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            this.attackedBlocks.add(pos);
        }
    }

    @EventTarget
    public final void onRender3D(Render3DEvent event) {
        if (!((Boolean)this.layerValue.get()).booleanValue()) {
            WBlockPos safePos;
            IBlock safeBlock;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            double d = iEntityPlayerSP.getPosX();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            double d2 = iEntityPlayerSP2.getPosY() - 1.0;
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            if ((safeBlock = BlockUtils.getBlock(safePos = new WBlockPos(d, d2, iEntityPlayerSP3.getPosZ()))) != null && this.validBlock(safeBlock)) {
                RenderUtils.drawBlockBox(safePos, Color.GREEN, true);
            }
        }
        for (WBlockPos blockPos : this.attackedBlocks) {
            RenderUtils.drawBlockBox(blockPos, Color.RED, true);
        }
    }

    private final boolean validBlock(IBlock block) {
        return !MinecraftInstance.classProvider.isBlockAir(block) && !MinecraftInstance.classProvider.isBlockLiquid(block) && !MinecraftInstance.classProvider.isBlockBedrock(block);
    }

    public Nuker() {
        Nuker nuker = this;
        boolean bl = false;
        ArrayList arrayList = new ArrayList();
        nuker.attackedBlocks = arrayList;
        this.nukeTimer = new TickTimer();
    }

    static {
        Companion = new Companion(null);
    }

    public static final class Companion {
        public final float getCurrentDamage() {
            return currentDamage;
        }

        public final void setCurrentDamage(float f) {
            currentDamage = f;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

