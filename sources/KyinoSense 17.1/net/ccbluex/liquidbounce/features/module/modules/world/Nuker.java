/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import net.ccbluex.liquidbounce.LiquidBounce;
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
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Nuker", description="Breaks all blocks around you.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 #2\u00020\u0001:\u0001#B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0007J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001eH\u0007J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0002R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Nuker;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "attackedBlocks", "Ljava/util/ArrayList;", "Lnet/minecraft/util/BlockPos;", "Lkotlin/collections/ArrayList;", "blockHitDelay", "", "currentBlock", "hitDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "layerValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "nuke", "nukeDelay", "nukeTimer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "nukeValue", "priorityValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "radiusValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rotationsValue", "throughWallsValue", "onRender3D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "validBlock", "", "block", "Lnet/minecraft/block/Block;", "Companion", "KyinoClient"})
public final class Nuker
extends Module {
    private final FloatValue radiusValue = new FloatValue("Radius", 5.2f, 1.0f, 6.0f);
    private final BoolValue throughWallsValue = new BoolValue("Through Walls", false);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Distance", "Hardness"}, "Distance");
    private final BoolValue rotationsValue = new BoolValue("Rotations", true);
    private final BoolValue layerValue = new BoolValue("Layer", false);
    private final IntegerValue hitDelayValue = new IntegerValue("Hit Delay", 4, 0, 20);
    private final IntegerValue nukeValue = new IntegerValue("Nuke", 1, 1, 20);
    private final IntegerValue nukeDelay = new IntegerValue("Nuke Delay", 1, 1, 20);
    private final ArrayList<BlockPos> attackedBlocks;
    private BlockPos currentBlock;
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
    public final void onUpdate(@NotNull UpdateEvent event) {
        void $this$filterTo$iv$iv;
        block44: {
            Map.Entry $this$filterTo$iv$iv2;
            Intrinsics.checkParameterIsNotNull(event, "event");
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
            PlayerControllerMP playerControllerMP = Nuker.access$getMc$p$s1046033730().field_71442_b;
            Intrinsics.checkExpressionValueIsNotNull(playerControllerMP, "mc.playerController");
            if (!playerControllerMP.func_78762_g()) break block44;
            BlockPos $this$filter$iv = BlockUtils.searchBlocks(MathKt.roundToInt(((Number)this.radiusValue.get()).floatValue()) + 1);
            boolean $i$f$filter = false;
            BlockPos blockPos = $this$filter$iv;
            Map destination$iv$iv2 = new LinkedHashMap();
            boolean $i$f$filterTo = false;
            void var8_20 = $this$filterTo$iv$iv2;
            boolean bl = false;
            Iterator iterator2 = var8_20.entrySet().iterator();
            while (iterator2.hasNext()) {
                boolean bl2;
                Map.Entry element$iv$iv;
                Map.Entry $dstr$pos$block = element$iv$iv = iterator2.next();
                boolean bl3 = false;
                Map.Entry entry = $dstr$pos$block;
                boolean bl4 = false;
                BlockPos pos3 = (BlockPos)entry.getKey();
                entry = $dstr$pos$block;
                bl4 = false;
                Block block = (Block)entry.getValue();
                if (BlockUtils.getCenterDistance(pos3) <= ((Number)this.radiusValue.get()).doubleValue() && this.validBlock(block)) {
                    if (((Boolean)this.layerValue.get()).booleanValue() && (double)pos3.func_177956_o() < Nuker.access$getMc$p$s1046033730().field_71439_g.field_70163_u) {
                        bl2 = false;
                    } else if (!((Boolean)this.throughWallsValue.get()).booleanValue()) {
                        double d = Nuker.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                        EntityPlayerSP entityPlayerSP = Nuker.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                        Vec3 eyesPos = new Vec3(d, entityPlayerSP.func_174813_aQ().field_72338_b + (double)Nuker.access$getMc$p$s1046033730().field_71439_g.func_70047_e(), Nuker.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                        Vec3 blockVec = new Vec3((double)pos3.func_177958_n() + 0.5, (double)pos3.func_177956_o() + 0.5, (double)pos3.func_177952_p() + 0.5);
                        MovingObjectPosition rayTrace = Nuker.access$getMc$p$s1046033730().field_71441_e.func_147447_a(eyesPos, blockVec, false, true, false);
                        bl2 = rayTrace != null && Intrinsics.areEqual(rayTrace.func_178782_a(), pos3);
                    } else {
                        bl2 = true;
                    }
                } else {
                    bl2 = false;
                }
                if (!bl2) continue;
                destination$iv$iv2.put(element$iv$iv.getKey(), element$iv$iv.getValue());
            }
            Map validBlocks = MapsKt.toMutableMap(destination$iv$iv2);
            do {
                void blockPos2;
                boolean $this$minBy$iv2;
                block47: {
                    Block block;
                    block46: {
                        Map.Entry entry;
                        block45: {
                            Object v7;
                            BlockPos safePos;
                            Block block2;
                            BlockPos pos;
                            BlockPos safePos2;
                            Object pos2;
                            Object minElem$iv$iv;
                            Iterator iterator$iv$iv;
                            boolean $i$f$minBy;
                            Iterable $this$minBy$iv$iv;
                            boolean $i$f$minBy2;
                            Map $this$minBy$iv2;
                            destination$iv$iv2 = (String)this.priorityValue.get();
                            switch (((String)((Object)destination$iv$iv2)).hashCode()) {
                                case 181289442: {
                                    if (!((String)((Object)destination$iv$iv2)).equals("Hardness")) return;
                                    break;
                                }
                                case 353103893: {
                                    Object v5;
                                    if (!((String)((Object)destination$iv$iv2)).equals("Distance")) return;
                                    $this$minBy$iv2 = validBlocks;
                                    $i$f$minBy2 = false;
                                    $this$minBy$iv$iv = $this$minBy$iv2.entrySet();
                                    $i$f$minBy = false;
                                    iterator$iv$iv = $this$minBy$iv$iv.iterator();
                                    if (!iterator$iv$iv.hasNext()) {
                                        v5 = null;
                                    } else {
                                        minElem$iv$iv = iterator$iv$iv.next();
                                        if (!iterator$iv$iv.hasNext()) {
                                            v5 = minElem$iv$iv;
                                        } else {
                                            Map.Entry $dstr$pos$block = (Map.Entry)minElem$iv$iv;
                                            boolean bl5 = false;
                                            Map.Entry blockVec = $dstr$pos$block;
                                            boolean pos3 = false;
                                            pos2 = (BlockPos)blockVec.getKey();
                                            blockVec = $dstr$pos$block;
                                            pos3 = false;
                                            Block block32 = (Block)blockVec.getValue();
                                            double distance = BlockUtils.getCenterDistance((BlockPos)pos2);
                                            safePos2 = new BlockPos(Nuker.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.0, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                                            double minValue$iv$iv = pos2.func_177958_n() == safePos2.func_177958_n() && safePos2.func_177956_o() <= pos2.func_177956_o() && pos2.func_177952_p() == safePos2.func_177952_p() ? DoubleCompanionObject.INSTANCE.getMAX_VALUE() - distance : distance;
                                            do {
                                                Object e$iv$iv = iterator$iv$iv.next();
                                                Map.Entry $dstr$pos$block2 = (Map.Entry)e$iv$iv;
                                                bl7 = false;
                                                pos2 = $dstr$pos$block2;
                                                boolean block32 = false;
                                                pos = (BlockPos)pos2.getKey();
                                                pos2 = $dstr$pos$block2;
                                                block32 = false;
                                                block2 = (Block)pos2.getValue();
                                                double distance2 = BlockUtils.getCenterDistance(pos);
                                                safePos = new BlockPos(Nuker.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.0, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                                                double v$iv$iv = pos.func_177958_n() == safePos.func_177958_n() && safePos.func_177956_o() <= pos.func_177956_o() && pos.func_177952_p() == safePos.func_177952_p() ? DoubleCompanionObject.INSTANCE.getMAX_VALUE() - distance2 : distance2;
                                                if (Double.compare(minValue$iv$iv, v$iv$iv) <= 0) continue;
                                                minElem$iv$iv = e$iv$iv;
                                                minValue$iv$iv = v$iv$iv;
                                            } while (iterator$iv$iv.hasNext());
                                            v5 = minElem$iv$iv;
                                        }
                                    }
                                    entry = v5;
                                    break block45;
                                }
                            }
                            $this$minBy$iv2 = validBlocks;
                            $i$f$minBy2 = false;
                            $this$minBy$iv$iv = $this$minBy$iv2.entrySet();
                            $i$f$minBy = false;
                            iterator$iv$iv = $this$minBy$iv$iv.iterator();
                            if (!iterator$iv$iv.hasNext()) {
                                v7 = null;
                            } else {
                                minElem$iv$iv = iterator$iv$iv.next();
                                if (!iterator$iv$iv.hasNext()) {
                                    v7 = minElem$iv$iv;
                                } else {
                                    Map.Entry $dstr$pos$block22 = (Map.Entry)minElem$iv$iv;
                                    boolean bl62 = false;
                                    Map.Entry v$iv$iv = $dstr$pos$block22;
                                    boolean bl7 = false;
                                    pos2 = (BlockPos)v$iv$iv.getKey();
                                    v$iv$iv = $dstr$pos$block22;
                                    bl7 = false;
                                    Block block4 = (Block)v$iv$iv.getValue();
                                    double hardness = block4.func_180647_a((EntityPlayer)Nuker.access$getMc$p$s1046033730().field_71439_g, (World)Nuker.access$getMc$p$s1046033730().field_71441_e, (BlockPos)pos2);
                                    safePos2 = new BlockPos(Nuker.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.0, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                                    double $dstr$pos$block22 = pos2.func_177958_n() == safePos2.func_177958_n() && safePos2.func_177956_o() <= pos2.func_177956_o() && pos2.func_177952_p() == safePos2.func_177952_p() ? DoubleCompanionObject.INSTANCE.getMIN_VALUE() + hardness : hardness;
                                    do {
                                        Object bl62 = iterator$iv$iv.next();
                                        Map.Entry $dstr$pos$block32 = (Map.Entry)bl62;
                                        boolean $i$a$-maxBy-Nuker$onUpdate$3 = false;
                                        pos2 = $dstr$pos$block32;
                                        boolean bl8 = false;
                                        pos = (BlockPos)pos2.getKey();
                                        pos2 = $dstr$pos$block32;
                                        bl8 = false;
                                        block2 = (Block)pos2.getValue();
                                        double hardness2 = block2.func_180647_a((EntityPlayer)Nuker.access$getMc$p$s1046033730().field_71439_g, (World)Nuker.access$getMc$p$s1046033730().field_71441_e, pos);
                                        safePos = new BlockPos(Nuker.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.0, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                                        double $dstr$pos$block32 = pos.func_177958_n() == safePos.func_177958_n() && safePos.func_177956_o() <= pos.func_177956_o() && pos.func_177952_p() == safePos.func_177952_p() ? DoubleCompanionObject.INSTANCE.getMIN_VALUE() + hardness2 : hardness2;
                                        if (Double.compare($dstr$pos$block22, $dstr$pos$block32) >= 0) continue;
                                        minElem$iv$iv = bl62;
                                        $dstr$pos$block22 = $dstr$pos$block32;
                                    } while (iterator$iv$iv.hasNext());
                                    v7 = minElem$iv$iv;
                                }
                            }
                            entry = v7;
                            break block45;
                            return;
                        }
                        if (entry == null) {
                            return;
                        }
                        $this$filterTo$iv$iv2 = entry;
                        destination$iv$iv2 = $this$filterTo$iv$iv2;
                        $this$minBy$iv2 = false;
                        $this$filter$iv = (BlockPos)destination$iv$iv2.getKey();
                        destination$iv$iv2 = $this$filterTo$iv$iv2;
                        $this$minBy$iv2 = false;
                        block = (Block)destination$iv$iv2.getValue();
                        if (Intrinsics.areEqual(blockPos2, this.currentBlock) ^ true) {
                            currentDamage = 0.0f;
                        }
                        if (((Boolean)this.rotationsValue.get()).booleanValue()) {
                            VecRotation rotation;
                            if (RotationUtils.faceBlock((BlockPos)blockPos2) == null) {
                                return;
                            }
                            RotationUtils.setTargetRotation(rotation.getRotation());
                        }
                        this.currentBlock = blockPos2;
                        this.attackedBlocks.add((BlockPos)blockPos2);
                        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(AutoTool.class);
                        if (module == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.AutoTool");
                        }
                        AutoTool autoTool = (AutoTool)module;
                        if (autoTool.getState()) {
                            autoTool.switchSlot((BlockPos)blockPos2);
                        }
                        if (currentDamage != 0.0f) break block46;
                        Minecraft minecraft = Nuker.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, (BlockPos)blockPos2, EnumFacing.DOWN));
                        if (block.func_180647_a((EntityPlayer)Nuker.access$getMc$p$s1046033730().field_71439_g, (World)Nuker.access$getMc$p$s1046033730().field_71441_e, (BlockPos)blockPos2) >= 1.0f) break block47;
                    }
                    Nuker.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
                    WorldClient worldClient = Nuker.access$getMc$p$s1046033730().field_71441_e;
                    EntityPlayerSP entityPlayerSP = Nuker.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    worldClient.func_175715_c(entityPlayerSP.func_145782_y(), (BlockPos)blockPos2, (int)((currentDamage += block.func_180647_a((EntityPlayer)Nuker.access$getMc$p$s1046033730().field_71439_g, (World)Nuker.access$getMc$p$s1046033730().field_71441_e, (BlockPos)blockPos2)) * 10.0f) - 1);
                    if (!(currentDamage >= 1.0f)) return;
                    Minecraft minecraft = Nuker.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, (BlockPos)blockPos2, EnumFacing.DOWN));
                    Nuker.access$getMc$p$s1046033730().field_71442_b.func_178888_a((BlockPos)blockPos2, EnumFacing.DOWN);
                    this.blockHitDelay = ((Number)this.hitDelayValue.get()).intValue();
                    currentDamage = 0.0f;
                    return;
                }
                currentDamage = 0.0f;
                Nuker.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
                Nuker.access$getMc$p$s1046033730().field_71442_b.func_178888_a((BlockPos)blockPos2, EnumFacing.DOWN);
                this.blockHitDelay = ((Number)this.hitDelayValue.get()).intValue();
                destination$iv$iv2 = validBlocks;
                $this$minBy$iv2 = false;
                destination$iv$iv2.remove(blockPos2);
                int destination$iv$iv2 = this.nuke;
                this.nuke = destination$iv$iv2 + 1;
            } while (this.nuke < ((Number)this.nukeValue.get()).intValue());
            return;
        }
        EntityPlayerSP entityPlayerSP = Nuker.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        ItemStack itemStack = entityPlayerSP.func_70694_bm();
        if ((itemStack != null ? itemStack.func_77973_b() : null) instanceof ItemSword) {
            return;
        }
        Map<BlockPos, Block> $this$filter$iv = BlockUtils.searchBlocks(MathKt.roundToInt(((Number)this.radiusValue.get()).floatValue()) + 1);
        boolean $i$f$filter = false;
        Map<BlockPos, Block> block = $this$filter$iv;
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
            BlockPos pos = (BlockPos)$dstr$pos$block22.getKey();
            $dstr$pos$block22 = $dstr$pos$block;
            bl62 = false;
            Block block5 = (Block)$dstr$pos$block22.getValue();
            if (BlockUtils.getCenterDistance(pos) <= ((Number)this.radiusValue.get()).doubleValue() && this.validBlock(block5)) {
                if (((Boolean)this.layerValue.get()).booleanValue() && (double)pos.func_177956_o() < Nuker.access$getMc$p$s1046033730().field_71439_g.field_70163_u) {
                    bl = false;
                } else if (!((Boolean)this.throughWallsValue.get()).booleanValue()) {
                    double d = Nuker.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                    EntityPlayerSP entityPlayerSP2 = Nuker.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                    Vec3 eyesPos = new Vec3(d, entityPlayerSP2.func_174813_aQ().field_72338_b + (double)Nuker.access$getMc$p$s1046033730().field_71439_g.func_70047_e(), Nuker.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                    Vec3 blockVec = new Vec3((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5);
                    MovingObjectPosition rayTrace = Nuker.access$getMc$p$s1046033730().field_71441_e.func_147447_a(eyesPos, blockVec, false, true, false);
                    bl = rayTrace != null && Intrinsics.areEqual(rayTrace.func_178782_a(), pos);
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
        Iterator iterator3 = map.entrySet().iterator();
        while (iterator3.hasNext()) {
            Map.Entry element$iv;
            Map.Entry $dstr$pos$block = element$iv = iterator3.next();
            boolean bl10 = false;
            Map.Entry entry = $dstr$pos$block;
            boolean bl11 = false;
            BlockPos pos = (BlockPos)entry.getKey();
            entry = $dstr$pos$block;
            bl11 = false;
            Block block6 = (Block)entry.getValue();
            Minecraft minecraft = Nuker.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            Nuker.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
            Minecraft minecraft2 = Nuker.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            minecraft2.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            this.attackedBlocks.add(pos);
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        BlockPos safePos;
        Block safeBlock;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!((Boolean)this.layerValue.get()).booleanValue() && (safeBlock = BlockUtils.getBlock(safePos = new BlockPos(Nuker.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.0, Nuker.access$getMc$p$s1046033730().field_71439_g.field_70161_v))) != null && this.validBlock(safeBlock)) {
            RenderUtils.drawBlockBox(safePos, Color.GREEN, true);
        }
        for (BlockPos blockPos : this.attackedBlocks) {
            RenderUtils.drawBlockBox(blockPos, Color.RED, true);
        }
    }

    private final boolean validBlock(Block block) {
        return !(block instanceof BlockAir) && !(block instanceof BlockLiquid) && Intrinsics.areEqual(block, Blocks.field_150357_h) ^ true;
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

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Nuker$Companion;", "", "()V", "currentDamage", "", "getCurrentDamage", "()F", "setCurrentDamage", "(F)V", "KyinoClient"})
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

