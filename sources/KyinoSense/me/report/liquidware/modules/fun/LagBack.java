/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.fun;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="LagBack", category=ModuleCategory.FUN, description="A system for tracking and disabling functions detected by anti-cheat")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\tX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\n\u00a8\u0006\u000f"}, d2={"Lme/report/liquidware/modules/fun/LagBack;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "flyValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "gameSpeedValue", "longJumpValue", "speedValue", "valueList", "", "[Lnet/ccbluex/liquidbounce/value/BoolValue;", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class LagBack
extends Module {
    private final BoolValue longJumpValue = new BoolValue("LongJump", true);
    private final BoolValue speedValue = new BoolValue("Speed", true);
    private final BoolValue gameSpeedValue = new BoolValue("GameSpeed", true);
    private final BoolValue flyValue = new BoolValue("Flight", true);
    private final BoolValue[] valueList = new BoolValue[]{this.longJumpValue, this.speedValue, this.gameSpeedValue, this.flyValue};

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            for (BoolValue i : this.valueList) {
                Module module;
                Module module2 = module = LiquidBounce.INSTANCE.getModuleManager().getModule(i.getName());
                if (module2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!module2.getState()) continue;
                module.setState(false);
                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("LagBack! " + module.getName(), Notification.Type.INFO));
            }
        }
    }

    public LagBack() {
        this.setState(true);
    }
}

