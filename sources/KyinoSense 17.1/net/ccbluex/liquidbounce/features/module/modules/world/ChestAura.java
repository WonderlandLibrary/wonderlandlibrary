/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
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
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="ChestAura", description="Automatically opens chests around you.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/ChestAura;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "chestValue", "Lnet/ccbluex/liquidbounce/value/BlockValue;", "clickedBlocks", "", "Lnet/minecraft/util/BlockPos;", "getClickedBlocks", "()Ljava/util/List;", "currentBlock", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "rangeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rotationsValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "throughWallsValue", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "visualSwing", "onDisable", "", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "KyinoClient"})
public final class ChestAura
extends Module {
    private static final FloatValue rangeValue;
    private static final IntegerValue delayValue;
    private static final BoolValue throughWallsValue;
    private static final BoolValue visualSwing;
    private static final BlockValue chestValue;
    private static final BoolValue rotationsValue;
    private static BlockPos currentBlock;
    private static final MSTimer timer;
    @NotNull
    private static final List<BlockPos> clickedBlocks;
    public static final ChestAura INSTANCE;

    @NotNull
    public final List<BlockPos> getClickedBlocks() {
        return clickedBlocks;
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(Blink.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        if (module.getState()) {
            return;
        }
        switch (ChestAura$WhenMappings.$EnumSwitchMapping$0[event.getEventState().ordinal()]) {
            case 1: {
                Object v4;
                Map.Entry it;
                Map.Entry element$iv$iv;
                Map $this$filterTo$iv$iv;
                if (ChestAura.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer) {
                    timer.reset();
                }
                float radius = ((Number)rangeValue.get()).floatValue() + 1.0f;
                double d = ChestAura.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                EntityPlayerSP entityPlayerSP = ChestAura.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                Vec3 eyesPos = new Vec3(d, entityPlayerSP.func_174813_aQ().field_72338_b + (double)ChestAura.access$getMc$p$s1046033730().field_71439_g.func_70047_e(), ChestAura.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                Map $this$filter$iv = BlockUtils.searchBlocks((int)radius);
                boolean $i$f$filter = false;
                Map map = $this$filter$iv;
                Map destination$iv$iv = new LinkedHashMap();
                boolean $i$f$filterTo = false;
                Map map2 = $this$filterTo$iv$iv;
                boolean bl = false;
                Iterator iterator2 = map2.entrySet().iterator();
                while (iterator2.hasNext()) {
                    it = element$iv$iv = iterator2.next();
                    boolean bl2 = false;
                    if (!(Block.func_149682_b((Block)((Block)it.getValue())) == ((Number)chestValue.get()).intValue() && !clickedBlocks.contains(it.getKey()) && BlockUtils.getCenterDistance((BlockPos)it.getKey()) < ((Number)rangeValue.get()).doubleValue())) continue;
                    destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
                }
                $this$filter$iv = destination$iv$iv;
                $i$f$filter = false;
                $this$filterTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new LinkedHashMap();
                $i$f$filterTo = false;
                map2 = $this$filterTo$iv$iv;
                bl = false;
                iterator2 = map2.entrySet().iterator();
                while (iterator2.hasNext()) {
                    BlockPos blockPos;
                    MovingObjectPosition movingObjectPosition;
                    it = element$iv$iv = iterator2.next();
                    boolean bl3 = false;
                    boolean bl4 = ((Boolean)throughWallsValue.get()).booleanValue() ? true : (movingObjectPosition = ChestAura.access$getMc$p$s1046033730().field_71441_e.func_147447_a(eyesPos, BlockExtensionKt.getVec(blockPos = (BlockPos)it.getKey()), false, true, false)) != null && Intrinsics.areEqual(movingObjectPosition.func_178782_a(), blockPos);
                    if (!bl4) continue;
                    destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
                }
                Map $this$minBy$iv = destination$iv$iv;
                boolean $i$f$minBy = false;
                Iterable $this$minBy$iv$iv = $this$minBy$iv.entrySet();
                boolean $i$f$minBy2 = false;
                Iterator iterator$iv$iv = $this$minBy$iv$iv.iterator();
                if (!iterator$iv$iv.hasNext()) {
                    v4 = null;
                } else {
                    Object minElem$iv$iv = iterator$iv$iv.next();
                    if (!iterator$iv$iv.hasNext()) {
                        v4 = minElem$iv$iv;
                    } else {
                        Map.Entry it2 = (Map.Entry)minElem$iv$iv;
                        boolean bl5 = false;
                        double minValue$iv$iv = BlockUtils.getCenterDistance((BlockPos)it2.getKey());
                        do {
                            Object e$iv$iv = iterator$iv$iv.next();
                            Map.Entry it3 = (Map.Entry)e$iv$iv;
                            $i$a$-minBy-ChestAura$onMotion$3 = false;
                            double v$iv$iv = BlockUtils.getCenterDistance((BlockPos)it3.getKey());
                            if (Double.compare(minValue$iv$iv, v$iv$iv) <= 0) continue;
                            minElem$iv$iv = e$iv$iv;
                            minValue$iv$iv = v$iv$iv;
                        } while (iterator$iv$iv.hasNext());
                        v4 = minElem$iv$iv;
                    }
                }
                Map.Entry entry = v4;
                Object object = currentBlock = entry != null ? (BlockPos)entry.getKey() : null;
                if (!((Boolean)rotationsValue.get()).booleanValue()) break;
                BlockPos blockPos = currentBlock;
                if (blockPos == null) {
                    return;
                }
                VecRotation vecRotation = RotationUtils.faceBlock(blockPos);
                if (vecRotation == null) {
                    return;
                }
                RotationUtils.setTargetRotation(vecRotation.getRotation());
                break;
            }
            case 2: {
                if (currentBlock == null || !timer.hasTimePassed(((Number)delayValue.get()).intValue())) break;
                PlayerControllerMP playerControllerMP = ChestAura.access$getMc$p$s1046033730().field_71442_b;
                EntityPlayerSP entityPlayerSP = ChestAura.access$getMc$p$s1046033730().field_71439_g;
                WorldClient worldClient = ChestAura.access$getMc$p$s1046033730().field_71441_e;
                EntityPlayerSP entityPlayerSP2 = ChestAura.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                ItemStack itemStack = entityPlayerSP2.func_70694_bm();
                BlockPos blockPos = currentBlock;
                if (blockPos == null) {
                    Intrinsics.throwNpe();
                }
                if (!playerControllerMP.func_178890_a(entityPlayerSP, worldClient, itemStack, currentBlock, EnumFacing.DOWN, BlockExtensionKt.getVec(blockPos))) break;
                if (((Boolean)visualSwing.get()).booleanValue()) {
                    ChestAura.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
                } else {
                    Minecraft minecraft = ChestAura.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
                }
                BlockPos blockPos2 = currentBlock;
                if (blockPos2 == null) {
                    Intrinsics.throwNpe();
                }
                clickedBlocks.add(blockPos2);
                currentBlock = null;
                timer.reset();
            }
        }
    }

    @Override
    public void onDisable() {
        clickedBlocks.clear();
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
        chestValue = new BlockValue("Chest", Block.func_149682_b((Block)((Block)Blocks.field_150486_ae)));
        rotationsValue = new BoolValue("Rotations", true);
        timer = new MSTimer();
        boolean bl = false;
        clickedBlocks = new ArrayList();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

