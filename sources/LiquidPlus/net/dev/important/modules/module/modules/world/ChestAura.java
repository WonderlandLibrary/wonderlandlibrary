/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
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
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.player.Blink;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.VecRotation;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.utils.extensions.BlockExtensionKt;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BlockValue;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.block.Block;
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
import org.jetbrains.annotations.Nullable;

@Info(name="ChestAura", spacedName="Chest Aura", description="Automatically opens chests around you.", category=Category.WORLD, cnName="\u7bb1\u5b50\u5149\u73af")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lnet/dev/important/modules/module/modules/world/ChestAura;", "Lnet/dev/important/modules/module/Module;", "()V", "chestValue", "Lnet/dev/important/value/BlockValue;", "clickedBlocks", "", "Lnet/minecraft/util/BlockPos;", "getClickedBlocks", "()Ljava/util/List;", "currentBlock", "delayValue", "Lnet/dev/important/value/IntegerValue;", "rangeValue", "Lnet/dev/important/value/FloatValue;", "rotationsValue", "Lnet/dev/important/value/BoolValue;", "throughWallsValue", "timer", "Lnet/dev/important/utils/timer/MSTimer;", "visualSwing", "onDisable", "", "onMotion", "event", "Lnet/dev/important/event/MotionEvent;", "LiquidBounce"})
public final class ChestAura
extends Module {
    @NotNull
    public static final ChestAura INSTANCE = new ChestAura();
    @NotNull
    private static final FloatValue rangeValue = new FloatValue("Range", 5.0f, 1.0f, 6.0f, "m");
    @NotNull
    private static final IntegerValue delayValue = new IntegerValue("Delay", 100, 50, 200, "ms");
    @NotNull
    private static final BoolValue throughWallsValue = new BoolValue("ThroughWalls", true);
    @NotNull
    private static final BoolValue visualSwing = new BoolValue("VisualSwing", true);
    @NotNull
    private static final BlockValue chestValue = new BlockValue("Chest", Block.func_149682_b((Block)((Block)Blocks.field_150486_ae)));
    @NotNull
    private static final BoolValue rotationsValue = new BoolValue("Rotations", true);
    @Nullable
    private static BlockPos currentBlock;
    @NotNull
    private static final MSTimer timer;
    @NotNull
    private static final List<BlockPos> clickedBlocks;

    private ChestAura() {
    }

    @NotNull
    public final List<BlockPos> getClickedBlocks() {
        return clickedBlocks;
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Module module2 = Client.INSTANCE.getModuleManager().get(Blink.class);
        Intrinsics.checkNotNull(module2);
        if (module2.getState()) {
            return;
        }
        switch (WhenMappings.$EnumSwitchMapping$0[event.getEventState().ordinal()]) {
            case 1: {
                Object v2;
                Map.Entry it;
                Map.Entry element$iv$iv;
                Map $this$filterTo$iv$iv;
                Map $this$filter$iv;
                if (MinecraftInstance.mc.field_71462_r instanceof GuiContainer) {
                    timer.reset();
                }
                float radius = ((Number)rangeValue.get()).floatValue() + 1.0f;
                Vec3 eyesPos = new Vec3(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)MinecraftInstance.mc.field_71439_g.func_70047_e(), MinecraftInstance.mc.field_71439_g.field_70161_v);
                Map<BlockPos, Block> map = BlockUtils.searchBlocks((int)radius);
                boolean $i$f$filter = false;
                Iterator iterator2 = $this$filter$iv;
                Map destination$iv$iv = new LinkedHashMap();
                boolean $i$f$filterTo = false;
                Iterator iterator3 = $this$filterTo$iv$iv.entrySet().iterator();
                while (iterator3.hasNext()) {
                    it = element$iv$iv = iterator3.next();
                    boolean bl = false;
                    boolean bl2 = Block.func_149682_b((Block)((Block)it.getValue())) == ((Number)chestValue.get()).intValue() && !INSTANCE.getClickedBlocks().contains(it.getKey()) && BlockUtils.getCenterDistance((BlockPos)it.getKey()) < (double)((Number)rangeValue.get()).floatValue();
                    if (!bl2) continue;
                    destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
                }
                $this$filter$iv = destination$iv$iv;
                $i$f$filter = false;
                $this$filterTo$iv$iv = $this$filter$iv;
                destination$iv$iv = new LinkedHashMap();
                $i$f$filterTo = false;
                iterator3 = $this$filterTo$iv$iv.entrySet().iterator();
                while (iterator3.hasNext()) {
                    BlockPos blockPos;
                    MovingObjectPosition movingObjectPosition;
                    it = element$iv$iv = iterator3.next();
                    boolean bl = false;
                    if (!((Boolean)throughWallsValue.get() != false ? true : (movingObjectPosition = MinecraftInstance.mc.field_71441_e.func_147447_a(eyesPos, BlockExtensionKt.getVec(blockPos = (BlockPos)it.getKey()), false, true, false)) != null && Intrinsics.areEqual(movingObjectPosition.func_178782_a(), blockPos))) continue;
                    destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
                }
                iterator2 = ((Iterable)destination$iv$iv.entrySet()).iterator();
                if (!iterator2.hasNext()) {
                    v2 = null;
                } else {
                    Object t = iterator2.next();
                    if (!iterator2.hasNext()) {
                        v2 = t;
                    } else {
                        Map.Entry it2 = (Map.Entry)t;
                        boolean bl = false;
                        double d = BlockUtils.getCenterDistance((BlockPos)it2.getKey());
                        do {
                            Object t2 = iterator2.next();
                            Map.Entry it3 = (Map.Entry)t2;
                            $i$a$-minByOrNull-ChestAura$onMotion$3 = false;
                            double d2 = BlockUtils.getCenterDistance((BlockPos)it3.getKey());
                            if (Double.compare(d, d2) <= 0) continue;
                            t = t2;
                            d = d2;
                        } while (iterator2.hasNext());
                        v2 = t;
                    }
                }
                Map.Entry entry = v2;
                BlockPos blockPos = currentBlock = entry == null ? null : (BlockPos)entry.getKey();
                if (!((Boolean)rotationsValue.get()).booleanValue()) break;
                BlockPos blockPos2 = currentBlock;
                if (blockPos2 == null) {
                    return;
                }
                VecRotation vecRotation = RotationUtils.faceBlock(blockPos2);
                if (vecRotation == null) {
                    return;
                }
                RotationUtils.setTargetRotation(vecRotation.getRotation());
                break;
            }
            case 2: {
                if (currentBlock == null || !timer.hasTimePassed(((Number)delayValue.get()).intValue())) break;
                PlayerControllerMP playerControllerMP = MinecraftInstance.mc.field_71442_b;
                EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                WorldClient worldClient = MinecraftInstance.mc.field_71441_e;
                ItemStack itemStack = MinecraftInstance.mc.field_71439_g.func_70694_bm();
                BlockPos blockPos = currentBlock;
                Intrinsics.checkNotNull(blockPos);
                if (!playerControllerMP.func_178890_a(entityPlayerSP, worldClient, itemStack, currentBlock, EnumFacing.DOWN, BlockExtensionKt.getVec(blockPos))) break;
                if (((Boolean)visualSwing.get()).booleanValue()) {
                    MinecraftInstance.mc.field_71439_g.func_71038_i();
                } else {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0APacketAnimation());
                }
                BlockPos blockPos3 = currentBlock;
                Intrinsics.checkNotNull(blockPos3);
                clickedBlocks.add(blockPos3);
                currentBlock = null;
                timer.reset();
            }
        }
    }

    @Override
    public void onDisable() {
        clickedBlocks.clear();
    }

    static {
        timer = new MSTimer();
        clickedBlocks = new ArrayList();
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[EventState.values().length];
            nArray[EventState.PRE.ordinal()] = 1;
            nArray[EventState.POST.ordinal()] = 2;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

