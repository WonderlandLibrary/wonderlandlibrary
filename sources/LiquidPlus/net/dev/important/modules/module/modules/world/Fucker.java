/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.client.gui.ScaledResolution
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
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.world;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.player.AutoTool;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.VecRotation;
import net.dev.important.utils.block.BlockUtils;
import net.dev.important.utils.extensions.BlockExtensionKt;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BlockValue;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="Fucker", description="Destroys selected blocks around you. (aka.  IDNuker)", category=Category.WORLD, cnName="\u81ea\u52a8\u7834\u574f\u65b9\u5757")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010'\u001a\u0004\u0018\u00010\u00172\u0006\u0010(\u001a\u00020\u0006H\u0002J\u0010\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\u0017H\u0002J\b\u0010,\u001a\u00020-H\u0016J\u0010\u0010.\u001a\u00020-2\u0006\u0010/\u001a\u000200H\u0007J\u0010\u00101\u001a\u00020-2\u0006\u0010/\u001a\u000202H\u0007J\u0010\u00103\u001a\u00020-2\u0006\u0010/\u001a\u000204H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010!\u001a\u00020\"8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b#\u0010$R\u000e\u0010%\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2={"Lnet/dev/important/modules/module/modules/world/Fucker;", "Lnet/dev/important/modules/module/Module;", "()V", "actionValue", "Lnet/dev/important/value/ListValue;", "blockHitDelay", "", "blockValue", "Lnet/dev/important/value/BlockValue;", "coolDownTimer", "Lnet/dev/important/utils/timer/MSTimer;", "coolDownValue", "Lnet/dev/important/value/IntegerValue;", "currentDamage", "", "getCurrentDamage", "()F", "setCurrentDamage", "(F)V", "instantValue", "Lnet/dev/important/value/BoolValue;", "noHitValue", "oldPos", "Lnet/minecraft/util/BlockPos;", "pos", "rangeValue", "Lnet/dev/important/value/FloatValue;", "renderValue", "rotationsValue", "surroundingsValue", "swingValue", "switchTimer", "switchValue", "tag", "", "getTag", "()Ljava/lang/String;", "throughWallsValue", "toggleResetCDValue", "find", "targetID", "isHitable", "", "blockPos", "onEnable", "", "onRender2D", "event", "Lnet/dev/important/event/Render2DEvent;", "onRender3D", "Lnet/dev/important/event/Render3DEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Fucker
extends Module {
    @NotNull
    public static final Fucker INSTANCE = new Fucker();
    @NotNull
    private static final BlockValue blockValue = new BlockValue("Block", 26);
    @NotNull
    private static final ListValue renderValue;
    @NotNull
    private static final ListValue throughWallsValue;
    @NotNull
    private static final FloatValue rangeValue;
    @NotNull
    private static final ListValue actionValue;
    @NotNull
    private static final BoolValue instantValue;
    @NotNull
    private static final IntegerValue switchValue;
    @NotNull
    private static final IntegerValue coolDownValue;
    @NotNull
    private static final BoolValue swingValue;
    @NotNull
    private static final BoolValue rotationsValue;
    @NotNull
    private static final BoolValue surroundingsValue;
    @NotNull
    private static final BoolValue noHitValue;
    @NotNull
    private static final BoolValue toggleResetCDValue;
    @Nullable
    private static BlockPos pos;
    @Nullable
    private static BlockPos oldPos;
    private static int blockHitDelay;
    @NotNull
    private static final MSTimer switchTimer;
    @NotNull
    private static final MSTimer coolDownTimer;
    private static float currentDamage;

    private Fucker() {
    }

    public final float getCurrentDamage() {
        return currentDamage;
    }

    public final void setCurrentDamage(float f) {
        currentDamage = f;
    }

    @Override
    public void onEnable() {
        if (((Boolean)toggleResetCDValue.get()).booleanValue()) {
            coolDownTimer.reset();
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Vec3 eyes2;
        BlockPos blockPos;
        block36: {
            int targetId;
            block35: {
                Intrinsics.checkNotNullParameter(event, "event");
                if (((Boolean)noHitValue.get()).booleanValue()) {
                    Module module2 = Client.INSTANCE.getModuleManager().getModule(KillAura.class);
                    if (module2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.KillAura");
                    }
                    KillAura killAura = (KillAura)module2;
                    if (killAura.getState() && killAura.getTarget() != null) {
                        return;
                    }
                }
                targetId = ((Number)blockValue.get()).intValue();
                if (pos == null || Block.func_149682_b((Block)BlockUtils.getBlock(pos)) != targetId) break block35;
                BlockPos blockPos2 = pos;
                Intrinsics.checkNotNull(blockPos2);
                if (!(BlockUtils.getCenterDistance(blockPos2) > (double)((Number)rangeValue.get()).floatValue())) break block36;
            }
            pos = this.find(targetId);
        }
        if (pos == null) {
            currentDamage = 0.0f;
            return;
        }
        BlockPos blockPos3 = pos;
        if (blockPos3 == null) {
            return;
        }
        BlockPos currentPos = blockPos3;
        VecRotation vecRotation = RotationUtils.faceBlock(currentPos);
        if (vecRotation == null) {
            return;
        }
        VecRotation rotations = vecRotation;
        boolean surroundings = false;
        if (((Boolean)surroundingsValue.get()).booleanValue() && (blockPos = MinecraftInstance.mc.field_71441_e.func_147447_a(eyes2 = MinecraftInstance.mc.field_71439_g.func_174824_e(1.0f), rotations.getVec(), false, false, true).func_178782_a()) != null && !(BlockExtensionKt.getBlock(blockPos) instanceof BlockAir)) {
            if (currentPos.func_177958_n() != blockPos.func_177958_n() || currentPos.func_177956_o() != blockPos.func_177956_o() || currentPos.func_177952_p() != blockPos.func_177952_p()) {
                surroundings = true;
            }
            BlockPos blockPos4 = pos = blockPos;
            if (blockPos4 == null) {
                return;
            }
            currentPos = blockPos4;
            VecRotation vecRotation2 = RotationUtils.faceBlock(currentPos);
            if (vecRotation2 == null) {
                return;
            }
            rotations = vecRotation2;
        }
        if (oldPos != null && !Intrinsics.areEqual(oldPos, currentPos)) {
            currentDamage = 0.0f;
            switchTimer.reset();
        }
        oldPos = currentPos;
        if (!switchTimer.hasTimePassed(((Number)switchValue.get()).intValue()) || ((Number)coolDownValue.get()).intValue() > 0 && !coolDownTimer.hasTimePassed((long)((Number)coolDownValue.get()).intValue() * 1000L)) {
            return;
        }
        if (blockHitDelay > 0) {
            int eyes2 = blockHitDelay;
            blockHitDelay = eyes2 + -1;
            return;
        }
        if (((Boolean)rotationsValue.get()).booleanValue()) {
            RotationUtils.setTargetRotation(rotations.getRotation());
        }
        if (StringsKt.equals((String)actionValue.get(), "destroy", true) || surroundings) {
            Module module3 = Client.INSTANCE.getModuleManager().get(AutoTool.class);
            if (module3 == null) {
                throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.player.AutoTool");
            }
            AutoTool autoTool = (AutoTool)module3;
            if (autoTool.getState()) {
                autoTool.switchSlot(currentPos);
            }
            if (((Boolean)instantValue.get()).booleanValue()) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                if (((Boolean)swingValue.get()).booleanValue()) {
                    MinecraftInstance.mc.field_71439_g.func_71038_i();
                }
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                currentDamage = 0.0f;
                if (!((Boolean)surroundingsValue.get()).booleanValue()) {
                    coolDownTimer.reset();
                }
                return;
            }
            Block block = BlockExtensionKt.getBlock(currentPos);
            if (block == null) {
                return;
            }
            Block block2 = block;
            if (currentDamage == 0.0f) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                if (MinecraftInstance.mc.field_71439_g.field_71075_bZ.field_75098_d || block2.func_180647_a((EntityPlayer)MinecraftInstance.mc.field_71439_g, (World)MinecraftInstance.mc.field_71441_e, pos) >= 1.0f) {
                    if (((Boolean)swingValue.get()).booleanValue()) {
                        MinecraftInstance.mc.field_71439_g.func_71038_i();
                    }
                    MinecraftInstance.mc.field_71442_b.func_178888_a(pos, EnumFacing.DOWN);
                    currentDamage = 0.0f;
                    pos = null;
                    if (!((Boolean)surroundingsValue.get()).booleanValue()) {
                        coolDownTimer.reset();
                    }
                    return;
                }
            }
            if (((Boolean)swingValue.get()).booleanValue()) {
                MinecraftInstance.mc.field_71439_g.func_71038_i();
            }
            MinecraftInstance.mc.field_71441_e.func_175715_c(MinecraftInstance.mc.field_71439_g.func_145782_y(), currentPos, (int)((currentDamage += block2.func_180647_a((EntityPlayer)MinecraftInstance.mc.field_71439_g, (World)MinecraftInstance.mc.field_71441_e, currentPos)) * 10.0f) - 1);
            if (currentDamage >= 1.0f) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentPos, EnumFacing.DOWN));
                MinecraftInstance.mc.field_71442_b.func_178888_a(currentPos, EnumFacing.DOWN);
                blockHitDelay = 4;
                currentDamage = 0.0f;
                pos = null;
                if (!((Boolean)surroundingsValue.get()).booleanValue()) {
                    coolDownTimer.reset();
                }
            }
        } else if (StringsKt.equals((String)actionValue.get(), "use", true) && MinecraftInstance.mc.field_71442_b.func_178890_a(MinecraftInstance.mc.field_71439_g, MinecraftInstance.mc.field_71441_e, MinecraftInstance.mc.field_71439_g.func_70694_bm(), pos, EnumFacing.DOWN, new Vec3((double)currentPos.func_177958_n(), (double)currentPos.func_177956_o(), (double)currentPos.func_177952_p()))) {
            if (((Boolean)swingValue.get()).booleanValue()) {
                MinecraftInstance.mc.field_71439_g.func_71038_i();
            }
            blockHitDelay = 4;
            currentDamage = 0.0f;
            pos = null;
            coolDownTimer.reset();
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        String string = ((String)renderValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "box": {
                BlockPos blockPos = pos;
                if (blockPos == null) {
                    return;
                }
                RenderUtils.drawBlockBox(blockPos, !coolDownTimer.hasTimePassed((long)((Number)coolDownValue.get()).intValue() * 1000L) ? Color.DARK_GRAY : Color.RED, false);
                break;
            }
            case "outline": {
                BlockPos blockPos = pos;
                if (blockPos == null) {
                    return;
                }
                RenderUtils.drawBlockBox(blockPos, !coolDownTimer.hasTimePassed((long)((Number)coolDownValue.get()).intValue() * 1000L) ? Color.DARK_GRAY : Color.RED, true);
                break;
            }
            case "2d": {
                BlockPos blockPos = pos;
                if (blockPos == null) {
                    return;
                }
                RenderUtils.draw2D(blockPos, !coolDownTimer.hasTimePassed((long)((Number)coolDownValue.get()).intValue() * 1000L) ? Color.DARK_GRAY.getRGB() : Color.RED.getRGB(), Color.BLACK.getRGB());
            }
        }
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        ScaledResolution sc = new ScaledResolution(MinecraftInstance.mc);
        if (((Number)coolDownValue.get()).intValue() > 0 && !coolDownTimer.hasTimePassed((long)((Number)coolDownValue.get()).intValue() * 1000L)) {
            String timeLeft = "Cooldown: " + (int)(coolDownTimer.hasTimeLeft((long)((Number)coolDownValue.get()).intValue() * 1000L) / 1000L) + 's';
            int strWidth = Fonts.minecraftFont.func_78256_a(timeLeft);
            Fonts.minecraftFont.func_78276_b(timeLeft, sc.func_78326_a() / 2 - strWidth / 2 - 1, sc.func_78328_b() / 2 - 70, 0);
            Fonts.minecraftFont.func_78276_b(timeLeft, sc.func_78326_a() / 2 - strWidth / 2 + 1, sc.func_78328_b() / 2 - 70, 0);
            Fonts.minecraftFont.func_78276_b(timeLeft, sc.func_78326_a() / 2 - strWidth / 2, sc.func_78328_b() / 2 - 69, 0);
            Fonts.minecraftFont.func_78276_b(timeLeft, sc.func_78326_a() / 2 - strWidth / 2, sc.func_78328_b() / 2 - 71, 0);
            Fonts.minecraftFont.func_78276_b(timeLeft, sc.func_78326_a() / 2 - strWidth / 2, sc.func_78328_b() / 2 - 70, -1);
        }
    }

    private final BlockPos find(int targetID) {
        int radius = (int)((Number)rangeValue.get()).floatValue() + 1;
        double nearestBlockDistance = Double.MAX_VALUE;
        BlockPos nearestBlock = null;
        int n = -radius + 1;
        int n2 = radius;
        if (n <= n2) {
            int x;
            do {
                int y;
                x = n2--;
                int n3 = -radius + 1;
                int n4 = radius;
                if (n3 > n4) continue;
                do {
                    int z;
                    y = n4--;
                    int n5 = -radius + 1;
                    int n6 = radius;
                    if (n5 > n6) continue;
                    do {
                        double distance;
                        Block block;
                        BlockPos blockPos;
                        if (BlockUtils.getBlock(blockPos = new BlockPos((int)MinecraftInstance.mc.field_71439_g.field_70165_t + x, (int)MinecraftInstance.mc.field_71439_g.field_70163_u + y, (int)MinecraftInstance.mc.field_71439_g.field_70161_v + (z = n6--))) == null || Block.func_149682_b((Block)block) != targetID || (distance = BlockUtils.getCenterDistance(blockPos)) > (double)((Number)rangeValue.get()).floatValue() || nearestBlockDistance < distance || !this.isHitable(blockPos) && !((Boolean)surroundingsValue.get()).booleanValue()) continue;
                        nearestBlockDistance = distance;
                        nearestBlock = blockPos;
                    } while (z != n5);
                } while (y != n3);
            } while (x != n);
        }
        return nearestBlock;
    }

    private final boolean isHitable(BlockPos blockPos) {
        Vec3 eyesPos;
        MovingObjectPosition movingObjectPosition;
        String string = ((String)throughWallsValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        String string2 = string;
        return Intrinsics.areEqual(string2, "raycast") ? (movingObjectPosition = MinecraftInstance.mc.field_71441_e.func_147447_a(eyesPos = new Vec3(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)MinecraftInstance.mc.field_71439_g.func_70047_e(), MinecraftInstance.mc.field_71439_g.field_70161_v), new Vec3((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.5, (double)blockPos.func_177952_p() + 0.5), false, true, false)) != null && Intrinsics.areEqual(movingObjectPosition.func_178782_a(), blockPos) : (Intrinsics.areEqual(string2, "around") ? !(BlockUtils.isFullBlock(blockPos.func_177977_b()) && BlockUtils.isFullBlock(blockPos.func_177984_a()) && BlockUtils.isFullBlock(blockPos.func_177978_c()) && BlockUtils.isFullBlock(blockPos.func_177974_f()) && BlockUtils.isFullBlock(blockPos.func_177968_d()) && BlockUtils.isFullBlock(blockPos.func_177976_e())) : true);
    }

    @Override
    @NotNull
    public String getTag() {
        return BlockUtils.getBlockName(((Number)blockValue.get()).intValue());
    }

    static {
        String[] stringArray = new String[]{"Box", "Outline", "2D", "None"};
        renderValue = new ListValue("Render-Mode", stringArray, "Box");
        stringArray = new String[]{"None", "Raycast", "Around"};
        throughWallsValue = new ListValue("ThroughWalls", stringArray, "None");
        rangeValue = new FloatValue("Range", 5.0f, 1.0f, 7.0f, "m");
        stringArray = new String[]{"Destroy", "Use"};
        actionValue = new ListValue("Action", stringArray, "Destroy");
        instantValue = new BoolValue("Instant", false);
        switchValue = new IntegerValue("SwitchDelay", 250, 0, 1000, "ms");
        coolDownValue = new IntegerValue("Cooldown-Seconds", 15, 0, 60);
        swingValue = new BoolValue("Swing", true);
        rotationsValue = new BoolValue("Rotations", true);
        surroundingsValue = new BoolValue("Surroundings", true);
        noHitValue = new BoolValue("NoAura", false);
        toggleResetCDValue = new BoolValue("ResetCoolDownWhenToggled", false);
        switchTimer = new MSTimer();
        coolDownTimer = new MSTimer();
    }
}

