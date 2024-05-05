/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="GameSpeed", spacedName="Game Speed", description="Changes the speed of the entire game.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0012H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0013"}, d2={"Lme/report/liquidware/modules/world/GameSpeed;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "autoDisableValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onMoveValue", "speedValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "tag", "", "getTag", "()Ljava/lang/String;", "onDisable", "", "onUpdate", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "KyinoClient"})
public final class GameSpeed
extends Module {
    private final FloatValue speedValue = new FloatValue("Speed", 2.0f, 0.1f, 10.0f);
    private final BoolValue onMoveValue = new BoolValue("OnMove", true);
    private final BoolValue autoDisableValue = new BoolValue("LagBack", true);

    @Override
    public void onDisable() {
        GameSpeed.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (GameSpeed.access$getMc$p$s1046033730().field_71439_g == null || GameSpeed.access$getMc$p$s1046033730().field_71441_e == null) {
            return;
        }
        if (MovementUtils.isMoving() || !((Boolean)this.onMoveValue.get()).booleanValue()) {
            GameSpeed.access$getMc$p$s1046033730().field_71428_T.field_74278_d = ((Number)this.speedValue.get()).floatValue();
            return;
        }
        GameSpeed.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getWorldClient() != null) {
            return;
        }
        if (((Boolean)this.autoDisableValue.get()).booleanValue()) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(this.getName());
            new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
            new Notification("LagBack! " + module, Notification.Type.INFO);
            this.setState(false);
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return String.valueOf(((Number)this.speedValue.get()).floatValue());
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

