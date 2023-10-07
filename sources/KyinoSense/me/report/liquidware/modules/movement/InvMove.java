/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C16PacketClientStatus$EnumState
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.report.liquidware.modules.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="InvMove", category=ModuleCategory.MOVEMENT, description="Allows you to walk while an inventory is opened.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0017\u001a\u00020\u0018J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0007J\b\u0010\u001d\u001a\u00020\u001aH\u0016J\u0010\u0010\u001e\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001fH\u0007J\u0010\u0010 \u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020!H\u0007J\u0010\u0010\"\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020#H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\bR\u0016\u0010\u0013\u001a\u0004\u0018\u00010\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006$"}, d2={"Lme/report/liquidware/modules/movement/InvMove;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "displayTag", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "noDetectableValue", "getNoDetectableValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "noMoveClicksValue", "getNoMoveClicksValue", "playerPackets", "", "Lnet/minecraft/network/play/client/C03PacketPlayer;", "sprintModeValue", "getSprintModeValue", "tag", "", "getTag", "()Ljava/lang/String;", "isAACAP", "", "onClick", "", "event", "Lnet/ccbluex/liquidbounce/event/ClickWindowEvent;", "onDisable", "onMotion", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class InvMove
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Silent", "Blink"}, "Silent");
    @NotNull
    private final ListValue sprintModeValue = new ListValue("InvSprint", new String[]{"Keep"}, "Keep");
    @NotNull
    private final BoolValue noDetectableValue = new BoolValue("NoDetectable", false);
    @NotNull
    private final BoolValue noMoveClicksValue = new BoolValue("NoMoveClicks", false);
    private final BoolValue displayTag = new BoolValue("ArrayList-Tag", true);
    private final List<C03PacketPlayer> playerPackets;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @NotNull
    public final ListValue getSprintModeValue() {
        return this.sprintModeValue;
    }

    @NotNull
    public final BoolValue getNoDetectableValue() {
        return this.noDetectableValue;
    }

    @NotNull
    public final BoolValue getNoMoveClicksValue() {
        return this.noMoveClicksValue;
    }

    @Override
    @Nullable
    public String getTag() {
        return (Boolean)this.displayTag.get() != false ? (String)this.modeValue.get() : null;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Speed.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.movement.Speed");
        }
        Speed speedModule = (Speed)module;
        if (!(InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiChat || InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiIngameMenu || ((Boolean)this.noDetectableValue.get()).booleanValue() && InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer)) {
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74351_w.field_74513_e = GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74351_w);
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74368_y.field_74513_e = GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74368_y);
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74366_z.field_74513_e = GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74366_z);
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74370_x.field_74513_e = GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74370_x);
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74314_A.field_74513_e = GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74314_A);
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_151444_V.field_74513_e = GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_151444_V);
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getEventState() == EventState.PRE && this.playerPackets.size() > 0 && (InvMove.access$getMc$p$s1046033730().field_71462_r == null || InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiChat || InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiIngameMenu || ((Boolean)this.noDetectableValue.get()).booleanValue() && InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer)) {
            Iterable $this$forEach$iv = this.playerPackets;
            boolean $i$f$forEach = false;
            for (Object element$iv : $this$forEach$iv) {
                C03PacketPlayer it = (C03PacketPlayer)element$iv;
                boolean bl = false;
                Minecraft minecraft = InvMove.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)it);
            }
            this.playerPackets.clear();
        }
    }

    @EventTarget
    public final void onClick(@NotNull ClickWindowEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.noMoveClicksValue.get()).booleanValue() && MovementUtils.isMoving()) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        String string = (String)this.modeValue.get();
        Locale locale = Locale.getDefault();
        Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.getDefault()");
        Locale locale2 = locale;
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase(locale2);
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase(locale)");
        switch (string3) {
            case "silent": {
                if (!(packet instanceof C16PacketClientStatus) || ((C16PacketClientStatus)packet).func_149435_c() != C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) break;
                event.cancelEvent();
                break;
            }
            case "blink": {
                if (InvMove.access$getMc$p$s1046033730().field_71462_r == null || InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiChat || InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiIngameMenu || ((Boolean)this.noDetectableValue.get()).booleanValue() && InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer || !(packet instanceof C03PacketPlayer)) break;
                event.cancelEvent();
                this.playerPackets.add((C03PacketPlayer)packet);
            }
        }
    }

    @Override
    public void onDisable() {
        if (!GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74351_w) || InvMove.access$getMc$p$s1046033730().field_71462_r != null) {
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74351_w.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74368_y) || InvMove.access$getMc$p$s1046033730().field_71462_r != null) {
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74368_y.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74366_z) || InvMove.access$getMc$p$s1046033730().field_71462_r != null) {
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74366_z.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74370_x) || InvMove.access$getMc$p$s1046033730().field_71462_r != null) {
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74370_x.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_74314_A) || InvMove.access$getMc$p$s1046033730().field_71462_r != null) {
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_74314_A.field_74513_e = false;
        }
        if (!GameSettings.func_100015_a((KeyBinding)InvMove.access$getMc$p$s1046033730().field_71474_y.field_151444_V) || InvMove.access$getMc$p$s1046033730().field_71462_r != null) {
            InvMove.access$getMc$p$s1046033730().field_71474_y.field_151444_V.field_74513_e = false;
        }
    }

    public final boolean isAACAP() {
        return StringsKt.equals((String)this.sprintModeValue.get(), "aacap", true) && InvMove.access$getMc$p$s1046033730().field_71462_r != null && !(InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiChat) && !(InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiIngameMenu) && ((Boolean)this.noDetectableValue.get() == false || !(InvMove.access$getMc$p$s1046033730().field_71462_r instanceof GuiContainer));
    }

    public InvMove() {
        List list;
        InvMove invMove = this;
        boolean bl = false;
        invMove.playerPackets = list = (List)new ArrayList();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

