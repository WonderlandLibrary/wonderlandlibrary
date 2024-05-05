/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemBucket
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.network.play.server.S12PacketEntityVelocity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.Rotation;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.NewFall;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoFall", category=ModuleCategory.PLAYER, description="Prevents you from taking fall damage.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0094\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u00107\u001a\u00020\u00042\u0006\u00108\u001a\u00020\u001e2\u0006\u00109\u001a\u00020\u001eH\u0002J\b\u0010:\u001a\u00020\u0004H\u0002J\b\u0010;\u001a\u00020<H\u0016J\b\u0010=\u001a\u00020<H\u0016J\u0010\u0010>\u001a\u00020<2\u0006\u0010?\u001a\u00020@H\u0007J\u0010\u0010A\u001a\u00020<2\u0006\u0010?\u001a\u00020BH\u0007J\u0010\u0010C\u001a\u00020<2\u0006\u0010?\u001a\u00020DH\u0007J\u0010\u0010E\u001a\u00020<2\u0006\u0010?\u001a\u00020FH\u0007J\u0010\u0010G\u001a\u00020<2\u0006\u0010?\u001a\u00020HH\u0007J\u0010\u0010I\u001a\u00020<2\u0006\u0010?\u001a\u00020JH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010$\u001a\u00020%\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u000e\u0010(\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020#X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u00100\u001a\u0002018VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b2\u00103R\u000e\u00104\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006K"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/NoFall;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aac4Fakelag", "", "aac4FlagCooldown", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "aac4FlagCount", "", "aac4Packets", "", "Lnet/minecraft/network/play/client/C03PacketPlayer;", "aac5Check", "aac5Timer", "aac5doFlag", "currentMlgBlock", "Lnet/minecraft/util/BlockPos;", "currentMlgItemIndex", "currentMlgRotation", "Lnet/ccbluex/liquidbounce/utils/VecRotation;", "doSpoof", "flySpeedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "isDmgFalling", "jumped", "matrixCanSpoof", "matrixFallTicks", "matrixFlagWait", "matrixIsFall", "matrixLastMotionY", "", "matrixSend", "matrixTestYMotion", "minFallDistanceValue", "mlgTimer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "needSpoof", "nextSpoof", "oldaacState", "packet1Count", "packetModify", "phaseOffsetValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "spartanTimer", "tag", "", "getTag", "()Ljava/lang/String;", "vulCanNoFall", "vulCantNoFall", "wasTimer", "inAir", "height", "plus", "inVoid", "onDisable", "", "onEnable", "onJump", "event", "Lnet/ccbluex/liquidbounce/event/JumpEvent;", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onMove", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "KyinoClient"})
public final class NoFall
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"SpoofGround", "AlwaysSpoofGround", "NoGround", "Packet", "Packet1", "Packet2", "Vulcan", "OldAAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "AACv4", "AAC4.4.X-Flag", "AAC4.4.2", "AAC5.0.4", "AAC5.0.14", "Spartan", "CubeCraft", "Hypixel", "HypSpoof", "Phase", "Verus", "Medusa", "Damage", "MotionFlag", "OldMatrix", "Matrix6.2.X", "Matrix6.2.X-Packet", "Matrix6.6.3", "MatrixTest", "Vulcan"}, "SpoofGround");
    private final IntegerValue phaseOffsetValue = new IntegerValue("PhaseOffset", 1, 0, 5);
    private final FloatValue minFallDistanceValue = new FloatValue("MinMLGHeight", 5.0f, 2.0f, 50.0f);
    private final FloatValue flySpeedValue = new FloatValue("MotionSpeed", -0.01f, -5.0f, 5.0f);
    private boolean vulCanNoFall;
    private boolean vulCantNoFall;
    private boolean nextSpoof;
    private boolean doSpoof;
    private int oldaacState;
    private boolean jumped;
    private final TickTimer spartanTimer = new TickTimer();
    private boolean aac4Fakelag;
    private boolean packetModify;
    private boolean aac5doFlag;
    private boolean aac5Check;
    private int aac5Timer;
    private final List<C03PacketPlayer> aac4Packets;
    private boolean needSpoof;
    private int packet1Count;
    private final TickTimer mlgTimer;
    private VecRotation currentMlgRotation;
    private int currentMlgItemIndex;
    private BlockPos currentMlgBlock;
    private boolean matrixIsFall;
    private boolean matrixCanSpoof;
    private int matrixFallTicks;
    private double matrixLastMotionY;
    private double matrixTestYMotion;
    private boolean isDmgFalling;
    private int matrixFlagWait;
    private final MSTimer aac4FlagCooldown;
    private int aac4FlagCount;
    private boolean wasTimer;
    private boolean matrixSend;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @Override
    public void onEnable() {
        this.nextSpoof = false;
        this.vulCanNoFall = false;
        this.vulCantNoFall = false;
        this.nextSpoof = false;
        this.doSpoof = false;
        this.aac4FlagCount = 0;
        this.aac4Fakelag = false;
        this.aac5Check = false;
        this.packetModify = false;
        this.aac4Packets.clear();
        this.needSpoof = false;
        this.aac5doFlag = false;
        this.aac5Timer = 0;
        this.packet1Count = 0;
        this.oldaacState = 0;
        this.matrixIsFall = false;
        this.matrixCanSpoof = false;
        this.matrixFallTicks = 0;
        this.matrixLastMotionY = 0.0;
        this.isDmgFalling = false;
        this.matrixFlagWait = 0;
        this.aac4FlagCooldown.reset();
    }

    @Override
    public void onDisable() {
        this.matrixSend = false;
        this.aac4FlagCount = 0;
        this.aac4Fakelag = false;
        this.aac5Check = false;
        this.packetModify = false;
        this.aac4Packets.clear();
        this.needSpoof = false;
        this.aac5doFlag = false;
        this.aac5Timer = 0;
        this.packet1Count = 0;
        this.oldaacState = 0;
        this.matrixIsFall = false;
        this.matrixCanSpoof = false;
        this.matrixFallTicks = 0;
        this.matrixLastMotionY = 0.0;
        this.isDmgFalling = false;
        this.matrixFlagWait = 0;
        this.aac4FlagCooldown.reset();
        if (this.wasTimer) {
            NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
            this.wasTimer = false;
        }
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.vulCantNoFall = true;
        this.vulCanNoFall = false;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block98: {
            block97: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (this.wasTimer) {
                    NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
                    this.wasTimer = false;
                }
                if (this.matrixFlagWait > 0) {
                    int n = this.matrixFlagWait;
                    this.matrixFlagWait = n + -1;
                    if (this.matrixFlagWait == 0) {
                        NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
                    }
                }
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.jumped = false;
                }
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x > 0.0) {
                    this.jumped = true;
                }
                if (!this.getState()) break block97;
                Module module = LiquidBounce.INSTANCE.getModuleManager().get(FreeCam.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (!module.getState()) break block98;
            }
            return;
        }
        EntityPlayerSP entityPlayerSP = NoFall.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_175149_v() || NoFall.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75101_c || NoFall.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75102_a) {
            return;
        }
        String string = (String)this.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "medusa": {
                if (!((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 2.5)) break;
                this.needSpoof = true;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                break;
            }
            case "vulcan": {
                if (!this.vulCanNoFall && (double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 3.25) {
                    this.vulCanNoFall = true;
                }
                if (this.vulCanNoFall && NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E && this.vulCantNoFall) {
                    this.vulCantNoFall = false;
                }
                if (this.vulCantNoFall) {
                    return;
                }
                if (this.nextSpoof) {
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -0.1;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = -0.1f;
                    MovementUtils.strafe(0.3f);
                    this.nextSpoof = false;
                }
                if (!(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 3.5625f)) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                this.doSpoof = true;
                this.nextSpoof = true;
                break;
            }
            case "packet": {
                if (!((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R - NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x > (double)3.0f)) break;
                Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                break;
            }
            case "matrix6.2.x-packet": {
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E || !((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R - NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x > (double)3.0f)) break;
                this.wasTimer = true;
                NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = RangesKt.coerceAtLeast(NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d * ((double)NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d < 0.6 ? 0.25f : 0.5f), 0.2f);
                Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(false));
                Minecraft minecraft2 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(false));
                Minecraft minecraft3 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                minecraft3.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(false));
                Minecraft minecraft4 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft4, "mc");
                minecraft4.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(false));
                Minecraft minecraft5 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft5, "mc");
                minecraft5.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(false));
                Minecraft minecraft6 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft6, "mc");
                minecraft6.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                Minecraft minecraft7 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft7, "mc");
                minecraft7.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(false));
                Minecraft minecraft8 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft8, "mc");
                minecraft8.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(false));
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                break;
            }
            case "cubecraft": {
                if (!(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 2.0f)) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E = false;
                Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                break;
            }
            case "oldaac": {
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 2.0f) {
                    Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                    this.oldaacState = 2;
                } else if (this.oldaacState == 2 && NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R < (float)2) {
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.1;
                    this.oldaacState = 3;
                    return;
                }
                switch (this.oldaacState) {
                    case 3: {
                        NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.1;
                        this.oldaacState = 4;
                        break;
                    }
                    case 4: {
                        NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.1;
                        this.oldaacState = 5;
                        break;
                    }
                    case 5: {
                        NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.1;
                        this.oldaacState = 1;
                    }
                }
                break;
            }
            case "laac": {
                if (this.jumped || !NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                EntityPlayerSP entityPlayerSP2 = NoFall.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                if (entityPlayerSP2.func_70617_f_()) break;
                EntityPlayerSP entityPlayerSP3 = NoFall.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                if (entityPlayerSP3.func_70090_H() || NoFall.access$getMc$p$s1046033730().field_71439_g.field_70134_J) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -6.0;
                break;
            }
            case "aac3.3.11": {
                if (!(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > (float)2)) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70159_w = NoFall.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 0.001, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                Minecraft minecraft9 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft9, "mc");
                minecraft9.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                break;
            }
            case "aac3.3.15": {
                if (!(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > (float)2)) break;
                Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                if (!minecraft.func_71387_A()) {
                    Minecraft minecraft10 = NoFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft10, "mc");
                    minecraft10.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, DoubleCompanionObject.INSTANCE.getNaN(), NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                }
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = -9999.0f;
                break;
            }
            case "motionflag": {
                if (!(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > (float)3)) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.flySpeedValue.get()).floatValue();
                break;
            }
            case "spartan": {
                this.spartanTimer.update();
                if (!((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 1.5) || !this.spartanTimer.hasTimePassed(10)) break;
                Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u + (double)10, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                Minecraft minecraft11 = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft11, "mc");
                minecraft11.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u - (double)10, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                this.spartanTimer.reset();
                break;
            }
            case "aac5.0.4": 
            case "aac4.4.2": 
            case "oldmatrix": {
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > (float)3) {
                    this.isDmgFalling = true;
                }
                if (!Intrinsics.areEqual((String)this.modeValue.get(), "AAC4.4.2")) break;
                if (this.aac4FlagCount >= 3 || this.aac4FlagCooldown.hasTimePassed(1500L)) {
                    return;
                }
                if (this.aac4FlagCooldown.hasTimePassed(1500L) || !NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E && !((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R < 0.5)) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E = false;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.0f;
                break;
            }
            case "aac5.0.14": {
                this.aac5Check = false;
                for (double offsetYs = 0.0; NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x - 1.5 < offsetYs; offsetYs -= 0.5) {
                    AxisAlignedBB axisAlignedBB;
                    Block block;
                    BlockPos blockPos = new BlockPos(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u + offsetYs, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                    Block block2 = block = BlockUtils.getBlock(blockPos);
                    if (block2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if ((axisAlignedBB = block2.func_180640_a((World)NoFall.access$getMc$p$s1046033730().field_71441_e, blockPos, BlockUtils.getState(blockPos))) == null) continue;
                    offsetYs = -999.9;
                    this.aac5Check = true;
                }
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = -2.0f;
                    this.aac5Check = false;
                }
                if (this.aac5Timer > 0) {
                    --this.aac5Timer;
                }
                if (this.aac5Check && (double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 2.5 && !NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.aac5doFlag = true;
                    this.aac5Timer = 18;
                } else if (this.aac5Timer < 2) {
                    this.aac5doFlag = false;
                }
                if (!this.aac5doFlag) break;
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.5, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                    break;
                }
                Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.42, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                break;
            }
            case "phase": {
                if (!(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > (float)(3 + ((Number)this.phaseOffsetValue.get()).intValue()))) break;
                EntityPlayerSP entityPlayerSP4 = NoFall.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
                BlockPos blockPos = new NewFall((EntityPlayer)entityPlayerSP4).findCollision(5);
                if (blockPos == null) {
                    return;
                }
                BlockPos fallPos = blockPos;
                if (!((double)fallPos.func_177956_o() - NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x / 20.0 < NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u)) break;
                NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.05f;
                Timer timer = new Timer();
                long l = 100L;
                boolean bl = false;
                boolean bl2 = false;
                TimerTask timerTask2 = new TimerTask(fallPos){
                    final /* synthetic */ BlockPos $fallPos$inlined;
                    {
                        this.$fallPos$inlined = blockPos;
                    }

                    public void run() {
                        TimerTask $this$schedule = this;
                        boolean bl = false;
                        Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition((double)this.$fallPos$inlined.func_177958_n(), (double)this.$fallPos$inlined.func_177956_o(), (double)this.$fallPos$inlined.func_177952_p(), true));
                        NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
                    }
                };
                timer.schedule(timerTask2, l);
                break;
            }
            case "verus": {
                if ((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R - NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x > (double)3) {
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.6;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.6;
                    this.needSpoof = true;
                }
                if ((int)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R / 3 > this.packet1Count) {
                    this.packet1Count = (int)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R / 3;
                    this.packetModify = true;
                }
                if (!NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                this.packet1Count = 0;
                break;
            }
            case "packet1": {
                if ((int)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R / 3 > this.packet1Count) {
                    this.packet1Count = (int)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R / 3;
                    this.packetModify = true;
                }
                if (!NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                this.packet1Count = 0;
                break;
            }
            case "packet2": {
                if ((int)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R / 2 > this.packet1Count) {
                    this.packet1Count = (int)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R / 2;
                    this.packetModify = true;
                }
                if (!NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                this.packet1Count = 0;
                break;
            }
            case "matrix6.6.3": {
                if (!((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R - NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x > (double)3)) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                this.matrixSend = true;
                NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.5f;
                this.wasTimer = true;
                break;
            }
            case "matrix6.2.x": {
                if (this.matrixIsFall) {
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.0f;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                    if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                        this.matrixIsFall = false;
                    }
                }
                if ((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R - NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x > (double)3) {
                    this.matrixIsFall = true;
                    if (this.matrixFallTicks == 0) {
                        this.matrixLastMotionY = NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x;
                    }
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70159_w = 0.0;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70747_aH = 0.0f;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70179_y = 0.0;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 3.2f;
                    n = this.matrixFallTicks;
                    if (8 <= n && 9 >= n) {
                        this.matrixCanSpoof = true;
                    }
                    n = this.matrixFallTicks;
                    this.matrixFallTicks = n + 1;
                }
                if (this.matrixFallTicks <= 12 || NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = this.matrixLastMotionY;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                this.matrixFallTicks = 0;
                this.matrixCanSpoof = false;
                break;
            }
            case "matrixtest": {
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x < -0.7) {
                    this.matrixTestYMotion += -0.1;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = this.matrixTestYMotion;
                    if (!((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R - NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x > (double)3)) break;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                    this.matrixSend = true;
                    NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.5f;
                    this.wasTimer = true;
                    break;
                }
                this.matrixTestYMotion = -0.7;
                break;
            }
            case "vulcan": {
                if (!this.vulCanNoFall && (double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 3.25) {
                    this.vulCanNoFall = true;
                }
                if (this.vulCanNoFall && NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E && this.vulCantNoFall) {
                    this.vulCantNoFall = false;
                }
                if (this.vulCantNoFall) {
                    return;
                }
                if (this.nextSpoof) {
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = -0.1;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = -0.1f;
                    MovementUtils.strafe(0.3f);
                    this.nextSpoof = false;
                }
                if (!(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 3.5625f)) break;
                NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                this.doSpoof = true;
                this.nextSpoof = true;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        block31: {
            block32: {
                boolean ok;
                block30: {
                    Intrinsics.checkParameterIsNotNull(event, "event");
                    if (!this.modeValue.equals("AACv4") || event.getEventState() != EventState.PRE) break block30;
                    if (!this.inVoid()) {
                        if (this.aac4Fakelag) {
                            this.aac4Fakelag = false;
                            if (this.aac4Packets.size() > 0) {
                                for (C03PacketPlayer packet : this.aac4Packets) {
                                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)packet);
                                }
                                this.aac4Packets.clear();
                            }
                        }
                        return;
                    }
                    if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E && this.aac4Fakelag) {
                        this.aac4Fakelag = false;
                        if (this.aac4Packets.size() > 0) {
                            for (C03PacketPlayer packet : this.aac4Packets) {
                                NoFall.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)packet);
                            }
                            this.aac4Packets.clear();
                        }
                        return;
                    }
                    if ((double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 2.5 && this.aac4Fakelag) {
                        this.packetModify = true;
                        NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                    }
                    if (this.inAir(4.0, 1.0)) {
                        return;
                    }
                    if (!this.aac4Fakelag) {
                        this.aac4Fakelag = true;
                    }
                    break block31;
                }
                if (!this.modeValue.equals("MLG")) break block31;
                if (event.getEventState() != EventState.PRE) break block32;
                this.currentMlgRotation = null;
                this.mlgTimer.update();
                if (!this.mlgTimer.hasTimePassed(10)) {
                    return;
                }
                if (!(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > ((Number)this.minFallDistanceValue.get()).floatValue())) break block31;
                EntityPlayerSP entityPlayerSP = NoFall.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                NewFall fallingPlayer = new NewFall((EntityPlayer)entityPlayerSP);
                PlayerControllerMP playerControllerMP = NoFall.access$getMc$p$s1046033730().field_71442_b;
                Intrinsics.checkExpressionValueIsNotNull(playerControllerMP, "mc.playerController");
                double maxDist = (double)playerControllerMP.func_78757_d() + 1.5;
                double d = 1.0 / NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x * -maxDist;
                NewFall newFall = fallingPlayer;
                int n = 0;
                double d2 = Math.ceil(d);
                BlockPos blockPos = newFall.findCollision((int)d2);
                if (blockPos == null) {
                    return;
                }
                BlockPos collision = blockPos;
                double d3 = new Vec3(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u + (double)NoFall.access$getMc$p$s1046033730().field_71439_g.eyeHeight, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v).func_72438_d(new Vec3((Vec3i)collision).func_72441_c(0.5, 0.5, 0.5));
                PlayerControllerMP playerControllerMP2 = NoFall.access$getMc$p$s1046033730().field_71442_b;
                Intrinsics.checkExpressionValueIsNotNull(playerControllerMP2, "mc.playerController");
                double d4 = 0.75;
                double d5 = playerControllerMP2.func_78757_d();
                double d6 = d3;
                int n2 = 0;
                double d7 = Math.sqrt(d4);
                boolean bl = ok = d6 < d5 + d7;
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x < (double)(collision.func_177956_o() + 1) - NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u) {
                    ok = true;
                }
                if (!ok) {
                    return;
                }
                int index = -1;
                n = 36;
                n2 = 44;
                while (n <= n2) {
                    void i;
                    block33: {
                        block34: {
                            Slot slot = NoFall.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a((int)i);
                            Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(i)");
                            ItemStack itemStack = slot.func_75211_c();
                            if (itemStack == null) break block33;
                            if (Intrinsics.areEqual(itemStack.func_77973_b(), Items.field_151131_as)) break block34;
                            if (!(itemStack.func_77973_b() instanceof ItemBlock)) break block33;
                            Item item = itemStack.func_77973_b();
                            if (item == null) {
                                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBlock");
                            }
                            if (!Intrinsics.areEqual(((ItemBlock)item).field_150939_a, Blocks.field_150321_G)) break block33;
                        }
                        if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c == (index = i - 36)) break;
                    }
                    ++i;
                }
                if (index == -1) {
                    return;
                }
                this.currentMlgItemIndex = index;
                this.currentMlgBlock = collision;
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c != index) {
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C09PacketHeldItemChange(index));
                }
                VecRotation vecRotation = this.currentMlgRotation = RotationUtils.faceBlock(collision);
                if (vecRotation == null) {
                    Intrinsics.throwNpe();
                }
                Rotation rotation = vecRotation.getRotation();
                EntityPlayerSP entityPlayerSP2 = NoFall.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                rotation.toPlayer((EntityPlayer)entityPlayerSP2);
                break block31;
            }
            if (this.currentMlgRotation != null) {
                ItemStack stack;
                ItemStack itemStack = stack = NoFall.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a[this.currentMlgItemIndex];
                Intrinsics.checkExpressionValueIsNotNull(itemStack, "stack");
                if (itemStack.func_77973_b() instanceof ItemBucket) {
                    NoFall.access$getMc$p$s1046033730().field_71442_b.func_78769_a((EntityPlayer)NoFall.access$getMc$p$s1046033730().field_71439_g, (World)NoFall.access$getMc$p$s1046033730().field_71441_e, stack);
                } else {
                    BlockPos blockPos = this.currentMlgBlock;
                    if (blockPos == null) {
                        return;
                    }
                    if (NoFall.access$getMc$p$s1046033730().field_71442_b.func_178890_a(NoFall.access$getMc$p$s1046033730().field_71439_g, NoFall.access$getMc$p$s1046033730().field_71441_e, stack, this.currentMlgBlock, EnumFacing.UP, new Vec3(0.0, 0.5, 0.0).func_178787_e(new Vec3((Vec3i)blockPos)))) {
                        this.mlgTimer.reset();
                    }
                }
                if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c != this.currentMlgItemIndex) {
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C09PacketHeldItemChange(NoFall.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                }
            }
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String mode = (String)this.modeValue.get();
        if (event.getPacket() instanceof S12PacketEntityVelocity && StringsKt.equals(mode, "AAC4.4.X-Flag", true) && (double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 1.8) {
            ((S12PacketEntityVelocity)event.getPacket()).field_149416_c = (int)((double)((S12PacketEntityVelocity)event.getPacket()).field_149416_c * -0.1);
        }
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            if (StringsKt.equals(mode, "Vulcan", true) && event.getPacket() instanceof C03PacketPlayer && this.doSpoof) {
                ((C03PacketPlayer)event.getPacket()).field_149474_g = true;
                this.doSpoof = false;
                ((S08PacketPlayerPosLook)event.getPacket()).field_148938_b = (double)Math.round(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u * (double)2) / (double)2;
                NoFall.access$getMc$p$s1046033730().field_71439_g.func_70107_b(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, ((S08PacketPlayerPosLook)event.getPacket()).field_148938_b, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
            }
            if (StringsKt.equals(mode, "OldMatrix", true) && this.matrixFlagWait > 0) {
                this.matrixFlagWait = 0;
                NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
                event.cancelEvent();
            }
        }
        if (event.getPacket() instanceof C03PacketPlayer) {
            Packet<?> packet = event.getPacket();
            if (this.matrixSend) {
                this.matrixSend = false;
                event.cancelEvent();
                PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer)packet).field_149479_a, ((C03PacketPlayer)packet).field_149477_b, ((C03PacketPlayer)packet).field_149478_c, true));
                PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer)packet).field_149479_a, ((C03PacketPlayer)packet).field_149477_b, ((C03PacketPlayer)packet).field_149478_c, false));
            }
            if (this.doSpoof) {
                ((C03PacketPlayer)packet).field_149474_g = true;
                this.doSpoof = false;
                ((C03PacketPlayer)packet).field_149477_b = (double)Math.round(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u * (double)2) / (double)2;
                NoFall.access$getMc$p$s1046033730().field_71439_g.func_70107_b(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, ((C03PacketPlayer)packet).field_149477_b, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
            }
            if (StringsKt.equals(mode, "SpoofGround", true) && (double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 2.5) {
                ((C03PacketPlayer)packet).field_149474_g = true;
            } else if (StringsKt.equals(mode, "AlwaysSpoofGround", true)) {
                ((C03PacketPlayer)packet).field_149474_g = true;
            } else if (StringsKt.equals(mode, "NoGround", true)) {
                ((C03PacketPlayer)packet).field_149474_g = false;
            } else if (StringsKt.equals(mode, "Hypixel", true) && NoFall.access$getMc$p$s1046033730().field_71439_g != null && (double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 1.5) {
                ((C03PacketPlayer)packet).field_149474_g = NoFall.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 2 == 0;
            } else if (StringsKt.equals(mode, "HypSpoof", true)) {
                PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer)packet).field_149479_a, ((C03PacketPlayer)packet).field_149477_b, ((C03PacketPlayer)packet).field_149478_c, true));
            } else if (StringsKt.equals(mode, "AACv4", true) && this.aac4Fakelag) {
                event.cancelEvent();
                if (this.packetModify) {
                    ((C03PacketPlayer)packet).field_149474_g = true;
                    this.packetModify = false;
                }
                this.aac4Packets.add((C03PacketPlayer)packet);
            } else if (StringsKt.equals(mode, "Verus", true) && this.needSpoof) {
                ((C03PacketPlayer)packet).field_149474_g = true;
                this.needSpoof = false;
            } else if (StringsKt.equals(mode, "Damage", true) && NoFall.access$getMc$p$s1046033730().field_71439_g != null && (double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 3.5) {
                ((C03PacketPlayer)packet).field_149474_g = true;
            } else if (StringsKt.equals(mode, "Packet1", true) && this.packetModify) {
                ((C03PacketPlayer)packet).field_149474_g = true;
                this.packetModify = false;
            } else if (StringsKt.equals(mode, "Packet2", true) && this.packetModify) {
                ((C03PacketPlayer)packet).field_149474_g = true;
                this.packetModify = false;
            } else if (StringsKt.equals(mode, "Matrix6.2.X", true) && this.matrixCanSpoof) {
                ((C03PacketPlayer)packet).field_149474_g = true;
                this.matrixCanSpoof = false;
            } else if (StringsKt.equals(mode, "AAC4.4.X-Flag", true) && (double)NoFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > 1.6) {
                ((C03PacketPlayer)packet).field_149474_g = true;
            } else if (StringsKt.equals(mode, "AAC5.0.4", true) && this.isDmgFalling) {
                if (((C03PacketPlayer)packet).field_149474_g && NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.isDmgFalling = false;
                    ((C03PacketPlayer)packet).field_149474_g = true;
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E = false;
                    ((C03PacketPlayer)packet).field_149477_b += 1.0;
                    Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer)packet).field_149479_a, ((C03PacketPlayer)packet).field_149477_b - 1.0784, ((C03PacketPlayer)packet).field_149478_c, false));
                    Minecraft minecraft2 = NoFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                    minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer)packet).field_149479_a, ((C03PacketPlayer)packet).field_149477_b - 0.5, ((C03PacketPlayer)packet).field_149478_c, true));
                }
            } else if ((StringsKt.equals(mode, "OldMatrix", true) || StringsKt.equals(mode, "AAC4.4.2", true)) && this.isDmgFalling) {
                if (((C03PacketPlayer)packet).field_149474_g && NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                    this.matrixFlagWait = 2;
                    this.isDmgFalling = false;
                    event.cancelEvent();
                    NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E = false;
                    Minecraft minecraft = NoFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer)packet).field_149479_a, ((C03PacketPlayer)packet).field_149477_b - (double)256, ((C03PacketPlayer)packet).field_149478_c, false));
                    Minecraft minecraft3 = NoFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                    minecraft3.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer)packet).field_149479_a, (double)-10, ((C03PacketPlayer)packet).field_149478_c, true));
                    NoFall.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.18f;
                }
            } else if (StringsKt.equals(mode, "Medusa", true) && this.needSpoof) {
                ((C03PacketPlayer)packet).field_149474_g = true;
                this.needSpoof = false;
            }
        }
    }

    @EventTarget
    public final void onMove(@NotNull MoveEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.modeValue.equals("laac") && !this.jumped && !NoFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            EntityPlayerSP entityPlayerSP = NoFall.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (!entityPlayerSP.func_70617_f_()) {
                EntityPlayerSP entityPlayerSP2 = NoFall.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                if (!entityPlayerSP2.func_70090_H() && !NoFall.access$getMc$p$s1046033730().field_71439_g.field_70134_J && NoFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x < 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                }
            }
        }
    }

    @EventTarget
    public final void onJump(@NotNull JumpEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.jumped = true;
    }

    private final boolean inVoid() {
        if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        int off = 0;
        while ((double)off < NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u + (double)2) {
            AxisAlignedBB bb = new AxisAlignedBB(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, (double)off, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
            if (!NoFall.access$getMc$p$s1046033730().field_71441_e.func_72945_a((Entity)NoFall.access$getMc$p$s1046033730().field_71439_g, bb).isEmpty()) {
                return true;
            }
            off += 2;
        }
        return false;
    }

    private final boolean inAir(double height, double plus2) {
        if (NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u < 0.0) {
            return false;
        }
        int off = 0;
        while ((double)off < height) {
            AxisAlignedBB bb = new AxisAlignedBB(NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u - (double)off, NoFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
            if (!NoFall.access$getMc$p$s1046033730().field_71441_e.func_72945_a((Entity)NoFall.access$getMc$p$s1046033730().field_71439_g, bb).isEmpty()) {
                return true;
            }
            off += (int)plus2;
        }
        return false;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public NoFall() {
        List list;
        NoFall noFall = this;
        boolean bl = false;
        noFall.aac4Packets = list = (List)new ArrayList();
        this.mlgTimer = new TickTimer();
        this.aac4FlagCooldown = new MSTimer();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

