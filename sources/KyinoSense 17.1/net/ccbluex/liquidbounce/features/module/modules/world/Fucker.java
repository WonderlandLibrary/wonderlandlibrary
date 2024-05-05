/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.player.EntityPlayer
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
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
import net.ccbluex.liquidbounce.utils.extensions.BlockExtensionKt;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BlockValue;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Fucker", description="Destroys selected blocks around you. (aka.  IDNuker)", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010#\u001a\u0004\u0018\u00010\u00132\u0006\u0010$\u001a\u00020\u0006H\u0002J\u0010\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u0013H\u0002J\u0010\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0007J\u0010\u0010,\u001a\u00020)2\u0006\u0010*\u001a\u00020-H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\u00020\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/Fucker;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "actionValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "blockHitDelay", "", "blockValue", "Lnet/ccbluex/liquidbounce/value/BlockValue;", "currentDamage", "", "getCurrentDamage", "()F", "setCurrentDamage", "(F)V", "instantValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "noHitValue", "oldPos", "Lnet/minecraft/util/BlockPos;", "pos", "rangeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "rotationsValue", "surroundingsValue", "swingValue", "switchTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "switchValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "tag", "", "getTag", "()Ljava/lang/String;", "throughWallsValue", "find", "targetID", "isHitable", "", "blockPos", "onRender3D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
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
    private static BlockPos pos;
    private static BlockPos oldPos;
    private static int blockHitDelay;
    private static final MSTimer switchTimer;
    private static float currentDamage;
    public static final Fucker INSTANCE;

    public final float getCurrentDamage() {
        return currentDamage;
    }

    public final void setCurrentDamage(float f) {
        currentDamage = f;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block36: {
            int targetId;
            block35: {
                Intrinsics.checkParameterIsNotNull(event, "event");
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
                if (pos == null || Block.func_149682_b((Block)BlockUtils.getBlock(pos)) != targetId) break block35;
                BlockPos blockPos = pos;
                if (blockPos == null) {
                    Intrinsics.throwNpe();
                }
                if (!(BlockUtils.getCenterDistance(blockPos) > ((Number)rangeValue.get()).doubleValue())) break block36;
            }
            pos = this.find(targetId);
        }
        if (pos == null) {
            currentDamage = 0.0f;
            return;
        }
        BlockPos blockPos = pos;
        if (blockPos == null) {
            return;
        }
        BlockPos currentPos = blockPos;
        VecRotation vecRotation = RotationUtils.faceBlock(currentPos);
        if (vecRotation == null) {
            return;
        }
        VecRotation rotations = vecRotation;
        boolean surroundings = false;
        if (((Boolean)surroundingsValue.get()).booleanValue()) {
            Vec3 eyes = Fucker.access$getMc$p$s1046033730().field_71439_g.func_174824_e(1.0f);
            MovingObjectPosition movingObjectPosition = Fucker.access$getMc$p$s1046033730().field_71441_e.func_147447_a(eyes, rotations.getVec(), false, false, true);
            Intrinsics.checkExpressionValueIsNotNull(movingObjectPosition, "mc.theWorld.rayTraceBloc\u2026             false, true)");
            BlockPos blockPos2 = movingObjectPosition.func_178782_a();
            if (blockPos2 != null && !(BlockExtensionKt.getBlock(blockPos2) instanceof BlockAir)) {
                if (currentPos.func_177958_n() != blockPos2.func_177958_n() || currentPos.func_177956_o() != blockPos2.func_177956_o() || currentPos.func_177952_p() != blockPos2.func_177952_p()) {
                    surroundings = true;
                }
                BlockPos blockPos3 = pos = blockPos2;
                if (blockPos3 == null) {
                    return;
                }
                currentPos = blockPos3;
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
        if (StringsKt.equals((String)actionValue.get(), "destroy", true) || surroundings) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(AutoTool.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.AutoTool");
            }
            AutoTool autoTool = (AutoTool)module;
            if (autoTool.getState()) {
                autoTool.switchSlot(currentPos);
            }
            if (((Boolean)instantValue.get()).booleanValue()) {
                Minecraft minecraft = Fucker.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                if (((Boolean)swingValue.get()).booleanValue()) {
                    Fucker.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
                }
                Minecraft minecraft2 = Fucker.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                minecraft2.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                currentDamage = 0.0f;
                return;
            }
            Block block = BlockExtensionKt.getBlock(currentPos);
            if (block == null) {
                return;
            }
            Block block2 = block;
            if (currentDamage == 0.0f) {
                Minecraft minecraft = Fucker.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                if (Fucker.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75098_d || block2.func_180647_a((EntityPlayer)Fucker.access$getMc$p$s1046033730().field_71439_g, (World)Fucker.access$getMc$p$s1046033730().field_71441_e, pos) >= 1.0f) {
                    if (((Boolean)swingValue.get()).booleanValue()) {
                        Fucker.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
                    }
                    Fucker.access$getMc$p$s1046033730().field_71442_b.func_178888_a(pos, EnumFacing.DOWN);
                    currentDamage = 0.0f;
                    pos = null;
                    return;
                }
            }
            if (((Boolean)swingValue.get()).booleanValue()) {
                Fucker.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
            }
            WorldClient worldClient = Fucker.access$getMc$p$s1046033730().field_71441_e;
            EntityPlayerSP entityPlayerSP = Fucker.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            worldClient.func_175715_c(entityPlayerSP.func_145782_y(), currentPos, (int)((currentDamage += block2.func_180647_a((EntityPlayer)Fucker.access$getMc$p$s1046033730().field_71439_g, (World)Fucker.access$getMc$p$s1046033730().field_71441_e, currentPos)) * 10.0f) - 1);
            if (currentDamage >= 1.0f) {
                Minecraft minecraft = Fucker.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                Fucker.access$getMc$p$s1046033730().field_71442_b.func_178888_a(currentPos, EnumFacing.DOWN);
                blockHitDelay = 4;
                currentDamage = 0.0f;
                pos = null;
            }
        } else if (StringsKt.equals((String)actionValue.get(), "use", true)) {
            PlayerControllerMP playerControllerMP = Fucker.access$getMc$p$s1046033730().field_71442_b;
            EntityPlayerSP entityPlayerSP = Fucker.access$getMc$p$s1046033730().field_71439_g;
            WorldClient worldClient = Fucker.access$getMc$p$s1046033730().field_71441_e;
            EntityPlayerSP entityPlayerSP2 = Fucker.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
            if (playerControllerMP.func_178890_a(entityPlayerSP, worldClient, entityPlayerSP2.func_70694_bm(), pos, EnumFacing.DOWN, new Vec3((double)currentPos.func_177958_n(), (double)currentPos.func_177956_o(), (double)currentPos.func_177952_p()))) {
                if (((Boolean)swingValue.get()).booleanValue()) {
                    Fucker.access$getMc$p$s1046033730().field_71439_g.func_71038_i();
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
        BlockPos blockPos = pos;
        if (blockPos == null) {
            return;
        }
        RenderUtils.drawBlockBox(blockPos, Color.RED, true);
    }

    /*
     * WARNING - void declaration
     */
    private final BlockPos find(int targetID) {
        Object v0;
        void $this$filterTo$iv$iv;
        Map<BlockPos, Block> $this$filter$iv = BlockUtils.searchBlocks((int)((Number)rangeValue.get()).floatValue() + 1);
        boolean $i$f$filter = false;
        Map<BlockPos, Block> map = $this$filter$iv;
        Map destination$iv$iv = new LinkedHashMap();
        boolean $i$f$filterTo = false;
        void var7_9 = $this$filterTo$iv$iv;
        boolean bl = false;
        Iterator iterator2 = var7_9.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry element$iv$iv;
            Map.Entry it = element$iv$iv = iterator2.next();
            boolean bl2 = false;
            if (!(Block.func_149682_b((Block)((Block)it.getValue())) == targetID && BlockUtils.getCenterDistance((BlockPos)it.getKey()) <= ((Number)rangeValue.get()).doubleValue() && (INSTANCE.isHitable((BlockPos)it.getKey()) || (Boolean)surroundingsValue.get() != false))) continue;
            destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
        }
        Map $this$minBy$iv = destination$iv$iv;
        boolean $i$f$minBy = false;
        Iterable $this$minBy$iv$iv = $this$minBy$iv.entrySet();
        boolean $i$f$minBy2 = false;
        Iterator iterator$iv$iv = $this$minBy$iv$iv.iterator();
        if (!iterator$iv$iv.hasNext()) {
            v0 = null;
        } else {
            Object minElem$iv$iv = iterator$iv$iv.next();
            if (!iterator$iv$iv.hasNext()) {
                v0 = minElem$iv$iv;
            } else {
                Map.Entry it = (Map.Entry)minElem$iv$iv;
                boolean bl3 = false;
                double minValue$iv$iv = BlockUtils.getCenterDistance((BlockPos)it.getKey());
                do {
                    Object e$iv$iv = iterator$iv$iv.next();
                    Map.Entry it2 = (Map.Entry)e$iv$iv;
                    $i$a$-minBy-Fucker$find$2 = false;
                    double v$iv$iv = BlockUtils.getCenterDistance((BlockPos)it2.getKey());
                    if (Double.compare(minValue$iv$iv, v$iv$iv) <= 0) continue;
                    minElem$iv$iv = e$iv$iv;
                    minValue$iv$iv = v$iv$iv;
                } while (iterator$iv$iv.hasNext());
                v0 = minElem$iv$iv;
            }
        }
        Map.Entry entry = v0;
        return entry != null ? (BlockPos)entry.getKey() : null;
    }

    private final boolean isHitable(BlockPos blockPos) {
        boolean bl;
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
                double d = Fucker.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                EntityPlayerSP entityPlayerSP = Fucker.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                Vec3 eyesPos = new Vec3(d, entityPlayerSP.func_174813_aQ().field_72338_b + (double)Fucker.access$getMc$p$s1046033730().field_71439_g.func_70047_e(), Fucker.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                MovingObjectPosition movingObjectPosition = Fucker.access$getMc$p$s1046033730().field_71441_e.func_147447_a(eyesPos, new Vec3((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.5, (double)blockPos.func_177952_p() + 0.5), false, true, false);
                if (movingObjectPosition != null && Intrinsics.areEqual(movingObjectPosition.func_178782_a(), blockPos)) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            case "around": {
                if (!(BlockUtils.isFullBlock(blockPos.func_177977_b()) && BlockUtils.isFullBlock(blockPos.func_177984_a()) && BlockUtils.isFullBlock(blockPos.func_177978_c()) && BlockUtils.isFullBlock(blockPos.func_177974_f()) && BlockUtils.isFullBlock(blockPos.func_177968_d()) && BlockUtils.isFullBlock(blockPos.func_177976_e()))) {
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

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

