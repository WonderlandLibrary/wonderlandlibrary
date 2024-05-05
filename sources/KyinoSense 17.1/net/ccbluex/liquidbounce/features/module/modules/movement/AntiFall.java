/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.time.TimerUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="AntiFall", category=ModuleCategory.MOVEMENT, description="Automatically attacks targets around you.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0013\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u00103\u001a\u00020\u0004H\u0002J\u0006\u00104\u001a\u00020\u0004J\b\u00105\u001a\u000206H\u0016J\u0010\u00107\u001a\u0002062\u0006\u00108\u001a\u000209H\u0007J\u0010\u0010:\u001a\u0002062\u0006\u00108\u001a\u00020;H\u0007J\u0010\u0010<\u001a\u0002062\u0006\u0010=\u001a\u00020>H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u00020 0\u001fj\b\u0012\u0004\u0012\u00020 `!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\"\u001a\b\u0012\u0004\u0012\u00020 0\u001fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u000e\u0010'\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010+\u001a\u00020,X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u000e\u00101\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006?"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/AntiFall;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "blink", "", "canBlink", "canCancel", "canSpoof", "detectedLocation", "Lnet/minecraft/util/BlockPos;", "flagged", "indicator", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "lastFound", "", "lastGroundPos", "", "getLastGroundPos", "()[D", "setLastGroundPos", "([D)V", "lastRecY", "", "maxDistanceWithoutGround", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "maxFallDistance", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "notify", "packetCache", "Ljava/util/ArrayList;", "Lnet/minecraft/network/play/client/C03PacketPlayer;", "Lkotlin/collections/ArrayList;", "packets", "getPackets", "()Ljava/util/ArrayList;", "setPackets", "(Ljava/util/ArrayList;)V", "prevX", "prevY", "prevZ", "pullbackTime", "timer", "Lnet/ccbluex/liquidbounce/utils/time/TimerUtil;", "getTimer", "()Lnet/ccbluex/liquidbounce/utils/time/TimerUtil;", "setTimer", "(Lnet/ccbluex/liquidbounce/utils/time/TimerUtil;)V", "tried", "voidOnlyValue", "checkVoid", "isInVoid", "onDisable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "e", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AntiFall
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"BlocksmcTest", "Report", "TeleportBack", "FlyFlag", "OnGroundSpoof", "MotionTeleport-Flag", "Hypixel"}, "TeleportBack");
    private final IntegerValue maxFallDistance = new IntegerValue("MaxFallDistance", 10, 2, 255);
    private FloatValue pullbackTime = new FloatValue("PullbackTime", 1500.0f, 100.0f, 2000.0f);
    private final FloatValue maxDistanceWithoutGround = new FloatValue("MaxDistanceToSetback", 2.5f, 1.0f, 30.0f);
    private final BoolValue voidOnlyValue = new BoolValue("OnlyVoid", true);
    private final BoolValue notify = new BoolValue("Notifications", true);
    private final BoolValue indicator = new BoolValue("Indicator", false);
    private BlockPos detectedLocation;
    private float lastFound;
    private double prevX;
    private double prevY;
    private double prevZ;
    private double lastRecY;
    @NotNull
    private double[] lastGroundPos = new double[3];
    @NotNull
    private TimerUtil timer = new TimerUtil();
    @NotNull
    private ArrayList<C03PacketPlayer> packets = new ArrayList();
    private final ArrayList<C03PacketPlayer> packetCache = new ArrayList();
    private boolean blink;
    private boolean canBlink;
    private boolean canCancel;
    private boolean canSpoof;
    private boolean tried;
    private boolean flagged;

    @NotNull
    public final double[] getLastGroundPos() {
        return this.lastGroundPos;
    }

    public final void setLastGroundPos(@NotNull double[] dArray) {
        Intrinsics.checkParameterIsNotNull(dArray, "<set-?>");
        this.lastGroundPos = dArray;
    }

    @NotNull
    public final TimerUtil getTimer() {
        return this.timer;
    }

    public final void setTimer(@NotNull TimerUtil timerUtil) {
        Intrinsics.checkParameterIsNotNull(timerUtil, "<set-?>");
        this.timer = timerUtil;
    }

    @NotNull
    public final ArrayList<C03PacketPlayer> getPackets() {
        return this.packets;
    }

    public final void setPackets(@NotNull ArrayList<C03PacketPlayer> arrayList) {
        Intrinsics.checkParameterIsNotNull(arrayList, "<set-?>");
        this.packets = arrayList;
    }

    @Override
    public void onDisable() {
        this.prevX = 0.0;
        this.prevY = 0.0;
        this.prevZ = 0.0;
    }

    private final boolean checkVoid() {
        boolean dangerous = true;
        for (int i = (int)(-(AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u - 1.4857625)); i <= 0; ++i) {
            WorldClient worldClient = AntiFall.access$getMc$p$s1046033730().field_71441_e;
            EntityPlayerSP entityPlayerSP = AntiFall.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            dangerous = worldClient.func_147461_a(entityPlayerSP.func_174813_aQ().func_72317_d(AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70159_w * 0.5, (double)i, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70179_y * 0.5)).isEmpty();
            if (dangerous) continue;
            break;
        }
        return dangerous;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        this.detectedLocation = null;
        if (AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            this.prevX = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70169_q;
            this.prevY = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70167_r;
            this.prevZ = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70166_s;
        }
        if (!AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            EntityPlayerSP entityPlayerSP = AntiFall.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (!entityPlayerSP.func_70617_f_()) {
                EntityPlayerSP entityPlayerSP2 = AntiFall.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                if (!entityPlayerSP2.func_70090_H()) {
                    boolean bl;
                    FallingPlayer fallingPlayer = new FallingPlayer(AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70159_w, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70179_y, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70177_z, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70702_br, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70701_bs);
                    FallingPlayer.CollisionResult collisionResult = fallingPlayer.findCollision(60);
                    Object object = this.detectedLocation = collisionResult != null ? collisionResult.getPos() : null;
                    if (this.detectedLocation != null) {
                        double d = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                        BlockPos blockPos = this.detectedLocation;
                        if (blockPos == null) {
                            Intrinsics.throwNpe();
                        }
                        double d2 = d - (double)blockPos.func_177956_o();
                        bl = false;
                        if (Math.abs(d2) + (double)AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R <= ((Number)this.maxFallDistance.get()).doubleValue()) {
                            this.lastFound = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R;
                        }
                    }
                    if (AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R - this.lastFound > ((Number)this.maxDistanceWithoutGround.get()).floatValue()) {
                        String mode;
                        String string = mode = (String)this.modeValue.get();
                        bl = false;
                        String string2 = string;
                        if (string2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        String string3 = string2.toLowerCase();
                        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
                        switch (string3) {
                            case "blocksmctest": {
                                this.canSpoof = false;
                                if ((!((Boolean)this.voidOnlyValue.get()).booleanValue() || this.checkVoid()) && AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R > ((Number)this.maxFallDistance.get()).floatValue() && AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u < this.lastRecY + 0.01 && AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x <= 0.0 && !AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70122_E && !this.flagged) {
                                    AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                                    AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.838;
                                    AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.838;
                                    this.canSpoof = true;
                                }
                                this.lastRecY = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                                break;
                            }
                            case "teleportback": {
                                WorldClient worldClient = AntiFall.access$getMc$p$s1046033730().field_71441_e;
                                Entity entity = (Entity)AntiFall.access$getMc$p$s1046033730().field_71439_g;
                                EntityPlayerSP entityPlayerSP3 = AntiFall.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                                if (worldClient.func_72945_a(entity, entityPlayerSP3.func_174813_aQ().func_72317_d(0.0, 0.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                                    WorldClient worldClient2 = AntiFall.access$getMc$p$s1046033730().field_71441_e;
                                    Entity entity2 = (Entity)AntiFall.access$getMc$p$s1046033730().field_71439_g;
                                    EntityPlayerSP entityPlayerSP4 = AntiFall.access$getMc$p$s1046033730().field_71439_g;
                                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
                                    if (worldClient2.func_72945_a(entity2, entityPlayerSP4.func_174813_aQ().func_72317_d(0.0, -10002.25, 0.0).func_72314_b(0.0, -10003.75, 0.0)).isEmpty()) {
                                        AntiFall.access$getMc$p$s1046033730().field_71439_g.func_70634_a(this.prevX, this.prevY, this.prevZ);
                                        AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                                        AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                                    }
                                }
                                if (!((Boolean)this.notify.get()).booleanValue()) break;
                                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AntiFall detect!", Notification.Type.INFO));
                                break;
                            }
                            case "flyflag": {
                                AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x += 0.1;
                                AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                                if (!((Boolean)this.notify.get()).booleanValue()) break;
                                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AntiFall detect!", Notification.Type.INFO));
                                break;
                            }
                            case "ongroundspoof": {
                                Minecraft minecraft = AntiFall.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(true));
                                if (!((Boolean)this.notify.get()).booleanValue()) break;
                                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AntiFall detect!", Notification.Type.INFO));
                                break;
                            }
                            case "motionteleport-flag": {
                                AntiFall.access$getMc$p$s1046033730().field_71439_g.func_70634_a(AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u + (double)1.0f, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v);
                                Minecraft minecraft = AntiFall.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                                AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.1;
                                MovementUtils.strafe();
                                AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                                if (!((Boolean)this.notify.get()).booleanValue()) break;
                                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AntiFall detect!", Notification.Type.INFO));
                                break;
                            }
                            case "hypixel": {
                                WorldClient worldClient = AntiFall.access$getMc$p$s1046033730().field_71441_e;
                                Entity entity = (Entity)AntiFall.access$getMc$p$s1046033730().field_71439_g;
                                EntityPlayerSP entityPlayerSP5 = AntiFall.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc.thePlayer");
                                if (worldClient.func_72945_a(entity, entityPlayerSP5.func_174813_aQ().func_72317_d(0.0, 0.0, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                                    WorldClient worldClient3 = AntiFall.access$getMc$p$s1046033730().field_71441_e;
                                    Entity entity3 = (Entity)AntiFall.access$getMc$p$s1046033730().field_71439_g;
                                    EntityPlayerSP entityPlayerSP6 = AntiFall.access$getMc$p$s1046033730().field_71439_g;
                                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP6, "mc.thePlayer");
                                    if (worldClient3.func_72945_a(entity3, entityPlayerSP6.func_174813_aQ().func_72317_d(0.0, -10002.25, 0.0).func_72314_b(0.0, -10003.75, 0.0)).isEmpty()) {
                                        AntiFall.access$getMc$p$s1046033730().field_71439_g.func_70634_a(this.prevX, this.prevY, this.prevZ);
                                        AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R = 0.0f;
                                        AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.0;
                                        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Scaffold.class);
                                        if (module == null) {
                                            Intrinsics.throwNpe();
                                        }
                                        module.setState(true);
                                    }
                                }
                                if (!((Boolean)this.notify.get()).booleanValue()) break;
                                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AntiFall detect!", Notification.Type.INFO));
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    public final boolean isInVoid() {
        int n = 0;
        int n2 = 128;
        while (n <= n2) {
            void i;
            if (MovementUtils.isOnGround((double)i)) {
                return false;
            }
            ++i;
        }
        return true;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "hypixel": {
                if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                    Minecraft minecraft = AntiFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                    break;
                }
                if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                    Minecraft minecraft = AntiFall.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                    break;
                }
                if (!(event.getPacket() instanceof C08PacketPlayerBlockPlacement)) break;
                PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u, AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                break;
            }
            case "report": {
                if (!this.packets.isEmpty() && AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70173_aa < 100) {
                    this.packets.clear();
                }
                if (!(packet instanceof C03PacketPlayer)) break;
                C03PacketPlayer packet2 = (C03PacketPlayer)packet;
                if (this.isInVoid()) {
                    event.cancelEvent();
                    this.packets.add(packet2);
                    if (!this.timer.delay(((Number)this.pullbackTime.get()).floatValue())) break;
                    PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.lastGroundPos[0], this.lastGroundPos[1] - 1.0, this.lastGroundPos[2], true));
                    break;
                }
                this.lastGroundPos[0] = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                this.lastGroundPos[1] = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                this.lastGroundPos[2] = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
                if (!this.packets.isEmpty()) {
                    Iterator<C03PacketPlayer> iterator2 = this.packets.iterator();
                    while (iterator2.hasNext()) {
                        C03PacketPlayer p;
                        C03PacketPlayer c03PacketPlayer = p = iterator2.next();
                        if (c03PacketPlayer == null) {
                            Intrinsics.throwNpe();
                        }
                        Intrinsics.checkExpressionValueIsNotNull(c03PacketPlayer, "p!!");
                        PacketUtils.sendPacketNoEvent((Packet)c03PacketPlayer);
                    }
                    this.packets.clear();
                }
                this.timer.reset();
                break;
            }
            case "blocksmc": {
                if (this.canSpoof && packet instanceof C03PacketPlayer) {
                    ((C03PacketPlayer)packet).field_149474_g = true;
                }
                if (!this.canSpoof || !(packet instanceof S08PacketPlayerPosLook)) break;
                this.flagged = true;
            }
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        block8: {
            block7: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                if (this.detectedLocation == null || !((Boolean)this.indicator.get()).booleanValue()) break block7;
                double d = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R;
                double d2 = AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                BlockPos blockPos = this.detectedLocation;
                if (blockPos == null) {
                    Intrinsics.throwNpe();
                }
                if (!(d + (d2 - (double)(blockPos.func_177956_o() + 1)) < (double)3)) break block8;
            }
            return;
        }
        BlockPos blockPos = this.detectedLocation;
        if (blockPos == null) {
            Intrinsics.throwNpe();
        }
        int x = blockPos.func_177958_n();
        BlockPos blockPos2 = this.detectedLocation;
        if (blockPos2 == null) {
            Intrinsics.throwNpe();
        }
        int y = blockPos2.func_177956_o();
        BlockPos blockPos3 = this.detectedLocation;
        if (blockPos3 == null) {
            Intrinsics.throwNpe();
        }
        int z = blockPos3.func_177952_p();
        Minecraft minecraft = AntiFall.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        RenderManager renderManager = minecraft.func_175598_ae();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        RenderUtils.glColor(new Color(255, 0, 0, 90));
        RenderUtils.drawFilledBox(new AxisAlignedBB((double)x - renderManager.field_78725_b, (double)(y + 1) - renderManager.field_78726_c, (double)z - renderManager.field_78723_d, (double)x - renderManager.field_78725_b + 1.0, (double)y + 1.2 - renderManager.field_78726_c, (double)z - renderManager.field_78723_d + 1.0));
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        double d = (double)AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70143_R + (AntiFall.access$getMc$p$s1046033730().field_71439_g.field_70163_u - ((double)y + 0.5));
        boolean bl = false;
        int fallDist = (int)Math.floor(d);
        int n = 0;
        int n2 = fallDist - 3;
        StringBuilder stringBuilder = new StringBuilder().append(fallDist).append("m (~");
        bl = false;
        int n3 = Math.max(n, n2);
        RenderUtils.renderNameTag(stringBuilder.append(n3).append(" damage)").toString(), (double)x + 0.5, (double)y + 1.7, (double)z + 0.5);
        GlStateManager.func_179117_G();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

